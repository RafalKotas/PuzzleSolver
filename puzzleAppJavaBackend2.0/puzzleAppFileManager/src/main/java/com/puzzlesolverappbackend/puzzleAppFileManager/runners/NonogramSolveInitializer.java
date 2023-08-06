package com.puzzlesolverappbackend.puzzleAppFileManager.runners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzleAppFileManager.NonogramSolutionDecision;
import com.puzzlesolverappbackend.puzzleAppFileManager.model.Nonogram;
import com.puzzlesolverappbackend.puzzleAppFileManager.model.NonogramResult;
import com.puzzlesolverappbackend.puzzleAppFileManager.payload.NonogramFileDetails;
import com.puzzlesolverappbackend.puzzleAppFileManager.payload.NonogramLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.repository.NonogramRepository;
import com.puzzlesolverappbackend.puzzleAppFileManager.services.CommonService;
import com.puzzlesolverappbackend.puzzleAppFileManager.services.NonogramLogicService;
import com.puzzlesolverappbackend.puzzleAppFileManager.services.NonogramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.puzzlesolverappbackend.puzzleAppFileManager.services.NonogramLogicService.printHeaders;

//@Component
//@Order(7)
public class NonogramSolveInitializer implements CommandLineRunner {

    boolean saveSolutions = true;
    @Autowired
    private NonogramRepository nonogramRepository;

    @Autowired
    NonogramLogicService nonogramLogicService;

    @Autowired
    NonogramService nonogramService;

    @Autowired
    CommonService commonService;

    Nonogram nonogram;
    String filename;
    String source;
    String year;
    String month;
    Double difficulty;
    Integer height;
    Integer width;
    public final static String puzzlePath = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.NONOGRAM_PATH_SUFFIX;

    List<Double> difficultyRange;
    Set<String> sources;

    public void initParameters(double minDifficulty, double maxDifficulty, String source) {
        difficultyRange = new ArrayList<>();
        difficultyRange.add(minDifficulty);
        difficultyRange.add(maxDifficulty);

        sources = new HashSet<>();
        sources.add(source);
    }

    @Override
    public void run(String... args) throws Exception {

        initParameters(4.0, 5.0, "logi");
        List<Nonogram> selectedNonogramsList = nonogramRepository.selectNonogramBySourceAndDifficulty(sources,
                difficultyRange.get(0), difficultyRange.get(1));

//        List<Nonogram> selectedNonogramsList = nonogramRepository.selectNonogramsBySourceAndSize(sources,
//                15);

        int selectedCount = 0;
        int solvedCount = 0;

        List<String> filesTooLongSolving = new ArrayList<>();

        System.out.println("Selected nonograms count: " + selectedCount);

        NonogramFileDetails nonogramFileDetails;
        NonogramLogic nonogramLogicToSolve;
        NonogramLogic nonogramLogicSolved;

        int nonogramNo = 1;

        printHeaders();

        for (Nonogram nonogram : selectedNonogramsList) {
            difficulty = nonogram.getDifficulty();
            filename = nonogram.getFilename();
            height = nonogram.getHeight();
            month = nonogram.getMonth();
            source = nonogram.getSource();
            width = nonogram.getWidth();
            year = nonogram.getYear();

            ObjectMapper objectMapper = new ObjectMapper();
            nonogramFileDetails = objectMapper.readValue(
                    new File(puzzlePath + filename + ".json"), NonogramFileDetails.class
            );

            nonogramLogicToSolve = new NonogramLogic(
                    nonogramFileDetails.getRowSequences(), nonogramFileDetails.getColumnSequences());

            if(!filesTooLongSolving.contains(filename)) {
                nonogramLogicSolved = nonogramLogicService.runCustomSolverOperationWithCorrectnessCheck(nonogramLogicToSolve,
                        filename);

                if(nonogramLogicSolved.getCompletionPercentage() == 100) {
                    solvedCount = solvedCount + 1;
                    if(saveSolutions) {
                        nonogramService.saveSolutionToFile(filename, nonogramLogicSolved);
                    }
                }
                selectedCount = selectedCount + 1;
            }

            nonogramNo = nonogramNo + 1;
        }

        System.out.println("Solved count: " + solvedCount);
        double percentageSolved = Math.round(((double)(solvedCount) / selectedCount) * 10000 ) / 100.0;
        System.out.println("Percentage solved: " +  percentageSolved);
    }

    public void printExampleNonogramResult() {
        /*
        filename = "Snejinka(auth_satt3047)-result";

        //System.out.println(nonogramResult.getNonogramSolutionDecisionList());
        ObjectMapper objectMapper = new ObjectMapper();
        NonogramResult nonogramResult = objectMapper.readValue(
                    new File(getResultsPath() + filename + ".json"), NonogramResult.class
        );

        for(NonogramSolutionDecision decision : nonogramResult.getNonogramSolutionDecisionList()) {
            System.out.println(decision);
        }

        System.out.println(nonogramResult.getNonogramSolutionDecisionList().get(0).getDecisionTime());*/
    }
}