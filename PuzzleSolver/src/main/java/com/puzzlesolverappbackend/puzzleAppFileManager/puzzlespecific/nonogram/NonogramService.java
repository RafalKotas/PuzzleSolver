package com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram;

import com.google.gson.Gson;
import com.puzzlesolverappbackend.puzzleAppFileManager.helpers.FileHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.repository.NonogramRepository;
import com.puzzlesolverappbackend.puzzleAppFileManager.templates.nonogram.NonogramBoardTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NonogramService {

    private final static int DEFAULT_MIN_DIMENSION = 5;

    private final static int DEFAULT_MAX_DIMENSION = 10;

    private final static double DEFAULT_MIN_DIFFICULTY = 1.0;

    private final static double DEFAULT_MAX_DIFFICULTY = 2.0;

    @Autowired
    NonogramRepository nonogramRepository;

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
        List<Integer> bounds = new ArrayList<>();
        if (integerList.size() >= 2) {
            bounds.add(integerList.get(0));
            bounds.add(integerList.get(integerList.size() - 1));
        } else {
            bounds.add(DEFAULT_MIN_DIMENSION);
            bounds.add(DEFAULT_MAX_DIMENSION);
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
