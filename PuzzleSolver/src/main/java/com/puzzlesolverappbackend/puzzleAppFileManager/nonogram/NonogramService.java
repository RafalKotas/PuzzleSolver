package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram;

import com.google.gson.Gson;
import com.puzzlesolverappbackend.puzzleAppFileManager.helpers.FileHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramJsonWriter;
import com.puzzlesolverappbackend.puzzleAppFileManager.runners.InitializerConstants;
import com.puzzlesolverappbackend.puzzleAppFileManager.services.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.puzzlesolverappbackend.puzzleAppFileManager.constants.SharedConsts.JSON_EXTENSION;
import static com.puzzlesolverappbackend.puzzleAppFileManager.constants.SharedConsts.JSON_EXTENSION_LENGTH;
import static com.puzzlesolverappbackend.puzzleAppFileManager.utils.ArrayUtils.sumListElements;

@Service
@Slf4j
public class NonogramService {

    private static final String nonogramsPath = InitializerConstants.PUZZLE_RELATIVE_PATH + InitializerConstants.PuzzleMappings.NONOGRAM_PATH_SUFFIX;

    private final static int DEFAULT_MIN_DIMENSION = 5;

    private final static int DEFAULT_MAX_DIMENSION = 10;

    private final static double DEFAULT_MIN_DIFFICULTY = 1.0;

    private final static double DEFAULT_MAX_DIFFICULTY = 2.0;

    private final CommonService commonService;
    private final NonogramRepository nonogramRepository;

    public NonogramService(CommonService commonService, NonogramRepository nonogramRepository) {
        this.commonService = commonService;
        this.nonogramRepository = nonogramRepository;
    }

    public List<String> getNonogramSources() {
      return nonogramRepository.selectNonogramSources().stream().sorted().collect(Collectors.toList());
    }

    public List<String> getNonogramYears() {
        return nonogramRepository.selectNonogramYears().stream().sorted().collect(Collectors.toList());
    }

    public List<String> getNonogramMonths() {
        return nonogramRepository.selectNonogramMonths().stream().sorted().collect(Collectors.toList());
    }

    public List<Double> getNonogramDifficulties() {
        return nonogramRepository.selectNonogramDifficulties().stream().sorted().collect(Collectors.toList());
    }

    public List<Integer> getNonogramHeights() {
        return nonogramRepository.selectNonogramHeights().stream().sorted().collect(Collectors.toList());
    }

    public List<Integer> getNonogramWidths() {
        return nonogramRepository.selectNonogramWidths().stream().sorted().collect(Collectors.toList());
    }

    public List<Integer> inferDimensionRange(List<Integer> integerList) {
        List<Integer> bounds = new ArrayList<>(List.of(DEFAULT_MIN_DIMENSION, DEFAULT_MAX_DIMENSION));
        if (integerList.size() >= 2) {
            bounds.set(0, integerList.get(0));
            bounds.set(1, integerList.get(integerList.size() - 1));
        }

        return bounds;
    }

    /**
     * @param  difficultiesList list of possible difficulties
     * @return range created from difficultiesList (2 elements)
     * **/
    public List<Double> inferDifficulties(List<Double> difficultiesList) {
        List<Double> difficulties = new ArrayList<>();
        if (difficultiesList.size() >= 2) {
            difficulties.add(difficultiesList.get(0));
            difficulties.add(difficultiesList.get(difficultiesList.size() - 1));
        } else {
            difficulties.add(DEFAULT_MIN_DIFFICULTY);
            difficulties.add(DEFAULT_MAX_DIFFICULTY);
        }

        return difficulties;
    }

    public NonogramFiltersResponse getNonogramFilters() {
        List<String> sources = getNonogramSources();
        List<String> years = getNonogramYears();
        List<String> months = getNonogramMonths();
        List<Double> difficulties = inferDifficulties(getNonogramDifficulties());
        List<Integer> heights = inferDimensionRange(getNonogramHeights());
        List<Integer> widths = inferDimensionRange(getNonogramWidths());

        return new NonogramFiltersResponse(sources, years, months, difficulties, heights, widths);
    }

    public NonogramCorrectnessIndicator checkNonogramCorrectness(NonogramFileDetails nonogramFileDetails) {
        int width = nonogramFileDetails.getWidth();
        int height = nonogramFileDetails.getHeight();

        List<List<Integer>> rowSequences = nonogramFileDetails.getRowSequences();
        List<List<Integer>> columnSequences = nonogramFileDetails.getColumnSequences();

        if (rowSequences.size() != height) {
            return NonogramCorrectnessIndicator.INVALID_DIMENSIONS_ROWS;
        }
        if (columnSequences.size() != width) {
            return NonogramCorrectnessIndicator.INVALID_DIMENSIONS_COLUMNS;
        }

        for (List<Integer> row : rowSequences) {
            if (sumListElements(row) + row.size() - 1 > width) {
                return NonogramCorrectnessIndicator.TOO_LONG_ROW_SEQUENCE;
            }
        }

        for (List<Integer> column : columnSequences) {
            if (sumListElements(column) + column.size() - 1 > height) {
                return NonogramCorrectnessIndicator.TOO_LONG_COLUMN_SEQUENCE;
            }
        }

        int sumRows = rowSequences.stream()
                .mapToInt(ArrayUtils::sumListElements)
                .sum();

        int sumColumns = columnSequences.stream()
                .mapToInt(ArrayUtils::sumListElements)
                .sum();

        if (sumRows != sumColumns) {
            return NonogramCorrectnessIndicator.SUM_MISMATCH_ROWS_COLUMNS;
        }

        return NonogramCorrectnessIndicator.VALID;
    }



    public String saveCreatedNonogramToFile(String fileName, NonogramFileDetails nonogramFileDetails) {
        nonogramFileDetails.setFilename(fileName + JSON_EXTENSION);

        Set<String> existingFilesNames = commonService.listFilesUsingJavaIO(nonogramsPath);

        String[] fileNamesWithoutExtension = existingFilesNames.toArray(String[]::new);
        List<String> fileNamesWithoutExtensionArray = Arrays.stream(fileNamesWithoutExtension
                        .clone())
                .map(fN -> fN.substring(0, fN.length() - JSON_EXTENSION_LENGTH))
                .toList();

        if (fileNamesWithoutExtensionArray.contains(fileName)) {
            log.warn("File with name {} already exists.", fileName);
            return "Nonogram not saved. File with same name already exists.";
        }

        try {
            NonogramJsonWriter.writeToFile(nonogramFileDetails, nonogramsPath + fileName + JSON_EXTENSION);
            log.info("Successfully saved nonogram: {}", fileName);
            return "Save success!";
        } catch (IOException e) {
            log.error("Error while saving nonogram {}: {}", fileName, e.getMessage());
            return "Exception external problem.";
        }
    }

    public void saveSolutionToFile(String filename, NonogramLogic nonogramSolutionLogic) {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(FileHelper.generateSavePathForFilename(filename));
            Gson gson = new Gson();
            NonogramBoardTemplate nonogramBoardTemplate = new NonogramBoardTemplate(nonogramSolutionLogic);
            nonogramBoardTemplate.setBoard(nonogramSolutionLogic.getNonogramSolutionBoard());
            gson.toJson(nonogramBoardTemplate, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
