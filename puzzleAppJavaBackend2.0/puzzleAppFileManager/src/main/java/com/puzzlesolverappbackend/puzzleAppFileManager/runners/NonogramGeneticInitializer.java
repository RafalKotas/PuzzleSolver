package com.puzzlesolverappbackend.puzzleAppFileManager.runners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.NonogramFileDetails;
import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.NonogramGenetic;
import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.NonogramLogic;
import org.springframework.boot.CommandLineRunner;

import java.io.File;

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
                new File(puzzlePath + InitializerConstants.PUZZLE_NAME + ".json"), NonogramFileDetails.class
        );
        nonogramLogicToSolve = new NonogramLogic(nonogramFileDetails.getRowSequences(), nonogramFileDetails.getColumnSequences(), false);
        nonogramGenetic = new NonogramGenetic(nonogramLogicToSolve);
        nonogramGenetic.solve();
    }
}
