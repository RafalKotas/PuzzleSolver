package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.initializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzlesolverapp.common.CommonService;
import com.puzzlesolverappbackend.puzzlesolverapp.constants.InitializerConstants;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramFileDetails;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.config.GuessMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules.mapNonogramFileDetailsToNonogramRules;

//@Component
//@Order(9)
@Slf4j
public class SymmetricNonogramLoggerInitializer implements CommandLineRunner {
    public static final String PUZZLE_PATH = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.NONOGRAM_PATH_SUFFIX;

    @Autowired
    CommonService commonService;

    private final List<String> nonograms1Dsymmetrical = new ArrayList<>();
    private final List<String> nonograms2Dsymmetrical = new ArrayList<>();
    private final List<String> nonograms3Dsymmetrical = new ArrayList<>();


    @Override
    public void run(String... args) {

        log.info("Symmetrical nonograms logger(9)");

        Set<String> existingFilesNames = commonService
                .listFilesUsingJavaIO(PUZZLE_PATH);

        ObjectMapper objectMapper = new ObjectMapper();

        for (String nonogramFileName : existingFilesNames) {

            try {
                NonogramFileDetails nonogramFileDetails = objectMapper.readValue(new File(PUZZLE_PATH + nonogramFileName), NonogramFileDetails.class);
                NonogramRules nonogramRules = mapNonogramFileDetailsToNonogramRules(nonogramFileDetails);
                NonogramLogic nonogramLogic = new NonogramLogic(nonogramRules, GuessMode.DISABLED);

                switch(nonogramLogic.nonogramSymmetricalGrade()) {
                    case "4 axis":
                        nonograms3Dsymmetrical.add(nonogramFileName);
                        break;
                    case "2 axis":
                        nonograms2Dsymmetrical.add(nonogramFileName);
                        break;
                    case "1 axis":
                        nonograms1Dsymmetrical.add(nonogramFileName);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                log.error("Can't parse file with name: {}", nonogramFileName, e);
            }

        }

        log.info("Nonograms 4 axis symmetrical filenames: ");
        for (String nonogramSym : nonograms3Dsymmetrical) {
            log.info("{}", nonogramSym);
        }
    }
}
