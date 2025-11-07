package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.initializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzlesolverapp.constants.InitializerConstants;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Nonogram;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramFileDetails;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.config.GuessMode;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.repository.NonogramRepository;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service.NonogramLogicService;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service.NonogramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.io.File;
import java.util.*;

import static com.puzzlesolverappbackend.puzzlesolverapp.constants.SharedConstants.JSON_EXTENSION;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules.mapNonogramFileDetailsToNonogramRules;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.utils.NonogramStatsUtils.getCompletionPercentage;

//@Component
//@ConditionalOnProperty(
//        prefix = "nonogram.init",
//        name = "enabled",
//        havingValue = "true",
//        matchIfMissing = false
//)
//@Order(7)
@Slf4j
public class NonogramSolveInitializer implements CommandLineRunner {

    public static final String PUZZLE_PATH = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.NONOGRAM_PATH_SUFFIX;

    @Autowired
    private NonogramRepository nonogramRepository;

    @Autowired
    NonogramLogicService nonogramLogicService;

    @Autowired
    NonogramService nonogramService;

    String filename;
    String source;
    String year;
    String month;
    Double difficulty;
    Integer height;
    Integer width;

    List<Double> difficultyRange;
    Set<String> sources;

    public void initParameters(double minDifficulty, double maxDifficulty, List<String> sources) {
        difficultyRange = new ArrayList<>();
        difficultyRange.add(minDifficulty);
        difficultyRange.add(maxDifficulty);

        this.sources = new HashSet<>(sources);
    }

    @Override
    public void run(String... args) throws Exception {

        initParameters(1.0, 1.0, Arrays.asList("logi", "logiMix"));
        List<Nonogram> selectedNonogramsList = nonogramRepository.selectNonogramBySourceAndDifficulty(sources,
                difficultyRange.get(0), difficultyRange.get(1));

        int selectedCount = 0;
        int solvedCount = 0;

        log.info("Selected nonograms count: {}", selectedCount);

        NonogramFileDetails nonogramFileDetails;
        NonogramRules nonogramRules;
        NonogramLogic nonogramLogicToSolve;
        NonogramLogic nonogramLogicSolved;

        int nonogramNo = 1;


        for (Nonogram selectedNonogram : selectedNonogramsList) {
            difficulty = selectedNonogram.getDifficulty();
            filename = selectedNonogram.getFilename();
            height = selectedNonogram.getDimensions().getHeight();
            month = selectedNonogram.getPublication().getMonth();
            source = selectedNonogram.getSource();
            width = selectedNonogram.getDimensions().getWidth();
            year = selectedNonogram.getPublication().getYear();

            ObjectMapper objectMapper = new ObjectMapper();
            nonogramFileDetails = objectMapper.readValue(
                    new File(PUZZLE_PATH + filename + JSON_EXTENSION), NonogramFileDetails.class
            );

            nonogramRules = mapNonogramFileDetailsToNonogramRules(nonogramFileDetails);
            nonogramLogicToSolve = new NonogramLogic(nonogramRules, GuessMode.DISABLED);

            long start = System.currentTimeMillis();
            nonogramLogicSolved = nonogramLogicService.runSolverWithCorrectnessCheck(nonogramLogicToSolve,
                    filename + JSON_EXTENSION);
            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            double secondsElapsed = timeElapsed / 1000.0;

            log.info("{}s {}%", secondsElapsed, getCompletionPercentage(nonogramLogicSolved));

            if (getCompletionPercentage(nonogramLogicSolved) == 100) {
                solvedCount = solvedCount + 1;
            }
            selectedCount = selectedCount + 1;

            nonogramNo = nonogramNo + 1;
        }

        log.info("Solved count: {}", solvedCount);
        double percentageSolved = selectedCount != 0 ?
                Math.round(((double)(solvedCount) / selectedCount) * 10000 ) / 100.0 : 0.0;
        log.info("Percentage solved: {}", percentageSolved);
    }
}