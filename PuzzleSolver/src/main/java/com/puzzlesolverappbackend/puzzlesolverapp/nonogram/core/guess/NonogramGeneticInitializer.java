package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.guess;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzlesolverapp.constants.InitializerConstants;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramFileDetails;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.config.GuessMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import java.io.File;

import static com.puzzlesolverappbackend.puzzlesolverapp.constants.SharedConsts.JSON_EXTENSION;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules.mapNonogramFileDetailsToNonogramRules;

//@Component
//@Order(8)
@Slf4j
public class NonogramGeneticInitializer implements CommandLineRunner {

    public static final String NONOGRAM_PUZZLE_PATH = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.NONOGRAM_PATH_SUFFIX;
    ObjectMapper objectMapper;

    NonogramFileDetails nonogramFileDetails;
    NonogramRules nonogramRules;

    NonogramLogic nonogramLogicToSolve;
    NonogramGenetic nonogramGenetic;

    @Override
    public void run(String... args) throws Exception {

        log.info("Nonogram genetic solve initializer(8)");

        objectMapper = new ObjectMapper();
        nonogramFileDetails = objectMapper.readValue(
                new File(NONOGRAM_PUZZLE_PATH + InitializerConstants.PUZZLE_NAME + JSON_EXTENSION), NonogramFileDetails.class
        );
        nonogramRules = mapNonogramFileDetailsToNonogramRules(nonogramFileDetails);
        nonogramLogicToSolve = new NonogramLogic(nonogramRules, GuessMode.DISABLED);
        nonogramGenetic = new NonogramGenetic(nonogramLogicToSolve);
        nonogramGenetic.solve();
    }
}
