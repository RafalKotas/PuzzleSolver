package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service;

import com.google.gson.Gson;
import com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils;
import com.puzzlesolverappbackend.puzzlesolverapp.common.CommonService;
import com.puzzlesolverappbackend.puzzlesolverapp.common.FileHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.constants.InitializerConstants;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.exception.NonogramFileReadException;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.exception.NonogramFileSaveException;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Nonogram;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramFileDetails;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramFiltersResponse;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.NonogramFilterRequest;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramCorrectnessIndicator;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.json.NonogramJsonWriter;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.repository.NonogramRepository;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service.specification.NonogramSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.sumListElements;
import static com.puzzlesolverappbackend.puzzlesolverapp.constants.SharedConstants.JSON_EXTENSION;
import static com.puzzlesolverappbackend.puzzlesolverapp.constants.SharedConstants.JSON_EXTENSION_LENGTH;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.json.NonogramJsonWriter.saveSolutionBoard;

@Service
@Slf4j
public class NonogramService {

    private static final String NONOGRAMS_PATH = InitializerConstants.PUZZLE_RELATIVE_PATH + InitializerConstants.PuzzleMappings.NONOGRAM_PATH_SUFFIX;

    private static final int DEFAULT_MIN_DIMENSION = 5;

    private static final int DEFAULT_MAX_DIMENSION = 10;

    private static final double DEFAULT_MIN_DIFFICULTY = 1.0;

    private static final double DEFAULT_MAX_DIFFICULTY = 2.0;

    private final CommonService commonService;
    private final NonogramRepository nonogramRepository;

    public NonogramService(CommonService commonService, NonogramRepository nonogramRepository) {
        this.commonService = commonService;
        this.nonogramRepository = nonogramRepository;
    }

    private List<Integer> inferDimensionRange(List<Integer> integerList) {
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
    private List<Double> inferDifficulties(List<Double> difficultiesList) {
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

    public Page<Nonogram> getNonogramsFiltered(NonogramFilterRequest filters, Pageable pageable) {
        return nonogramRepository.findAll(NonogramSpecification.withFilters(filters), pageable);
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

    private List<String> getNonogramSources() {
        return nonogramRepository.selectNonogramSources().stream().sorted().toList();
    }

    private List<String> getNonogramYears() {
        return nonogramRepository.selectNonogramYears().stream().sorted().toList();
    }

    private List<String> getNonogramMonths() {
        return nonogramRepository.selectNonogramMonths().stream().sorted().toList();
    }

    private List<Double> getNonogramDifficulties() {
        return nonogramRepository.selectNonogramDifficulties().stream().sorted().toList();
    }

    private List<Integer> getNonogramHeights() {
        return nonogramRepository.selectNonogramHeights().stream().sorted().toList();
    }

    private List<Integer> getNonogramWidths() {
        return nonogramRepository.selectNonogramWidths().stream().sorted().toList();
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

        Set<String> existingFilesNames = commonService.listFilesUsingJavaIO(NONOGRAMS_PATH);

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
            NonogramJsonWriter.writeToFile(nonogramFileDetails, NONOGRAMS_PATH + fileName + JSON_EXTENSION);
            log.info("Successfully saved nonogram: {}", fileName);
            return "Save success!";
        } catch (IOException e) {
            log.error("Error while saving nonogram {}: {}", fileName, e.getMessage());
            return "Exception external problem.";
        }
    }

    public List<Nonogram> getNonogramsList() {
        List<Nonogram> nonogramList = nonogramRepository.findAll();
        log.info("Found {} nonograms", nonogramList.size());
        return nonogramList;
    }

    // TODO - only used in tests (remove/change)
    public NonogramFileDetails getNonogramDetailsFromFile(String filePath) {

        Gson gson = new Gson();

        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, NonogramFileDetails.class);
        } catch (IOException e) {
            // TODO - return empty object(?)
            throw new NonogramFileReadException("Could not find nonogram solution file: " + filePath , e);
        }
    }

    public void saveSolutionToFile(String filename, List<List<String>> nonogramBoard) {
        try {
            File filePath = new File(FileHelper.nonogramSolutionSavePathForFilename(filename));
            filePath.getParentFile().mkdirs();

            saveSolutionBoard(nonogramBoard, filePath.getPath());
        } catch (IOException e) {
            throw new NonogramFileSaveException("Could not save nonogram solution to file: " + filename , e);
        }
    }
}
