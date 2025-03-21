package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.GuessMode;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.NonogramGenetic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.runners.InitializerConstants;
import org.springframework.boot.CommandLineRunner;

import java.io.File;

import static com.puzzlesolverappbackend.puzzleAppFileManager.constants.SharedConsts.JSON_EXTENSION;

//@Component
//@Order(8)
public class NonogramGeneticInitializer implements CommandLineRunner {

    public final static String puzzlePath = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.NONOGRAM_PATH_SUFFIX;
    ObjectMapper objectMapper;

    NonogramFileDetails nonogramFileDetails;

    NonogramLogic nonogramLogicToSolve;
    NonogramGenetic nonogramGenetic;

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Nonogram genetic solve initializer(8)");

        objectMapper = new ObjectMapper();
        nonogramFileDetails = objectMapper.readValue(
                new File(puzzlePath + InitializerConstants.PUZZLE_NAME + JSON_EXTENSION), NonogramFileDetails.class
        );
        nonogramLogicToSolve = new NonogramLogic(nonogramFileDetails.getRowSequences(), nonogramFileDetails.getColumnSequences(), GuessMode.DISABLED);
        nonogramGenetic = new NonogramGenetic(nonogramLogicToSolve);
        nonogramGenetic.solve();
    }
}
