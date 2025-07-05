package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.guess;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzlesolverapp.constants.InitializerConstants;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramFileDetails;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.config.GuessMode;
import org.springframework.boot.CommandLineRunner;

import java.io.File;

import static com.puzzlesolverappbackend.puzzlesolverapp.constants.SharedConsts.JSON_EXTENSION;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules.mapNonogramFileDetailsToNonogramRules;

//@Component
//@Order(8)
public class NonogramGeneticInitializer implements CommandLineRunner {

    public final static String puzzlePath = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.NONOGRAM_PATH_SUFFIX;
    ObjectMapper objectMapper;

    NonogramFileDetails nonogramFileDetails;
    NonogramRules nonogramRules;

    NonogramLogic nonogramLogicToSolve;
    NonogramGenetic nonogramGenetic;

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Nonogram genetic solve initializer(8)");

        objectMapper = new ObjectMapper();
        nonogramFileDetails = objectMapper.readValue(
                new File(puzzlePath + InitializerConstants.PUZZLE_NAME + JSON_EXTENSION), NonogramFileDetails.class
        );
        nonogramRules = mapNonogramFileDetailsToNonogramRules(nonogramFileDetails);
        nonogramLogicToSolve = new NonogramLogic(nonogramRules, GuessMode.DISABLED);
        nonogramGenetic = new NonogramGenetic(nonogramLogicToSolve);
        nonogramGenetic.solve();
    }
}
