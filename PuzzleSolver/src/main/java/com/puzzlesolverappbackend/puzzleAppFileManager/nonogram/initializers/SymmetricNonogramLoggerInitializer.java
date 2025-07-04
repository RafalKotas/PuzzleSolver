package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.initializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzleAppFileManager.common.CommonService;
import com.puzzlesolverappbackend.puzzleAppFileManager.constants.InitializerConstants;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.model.NonogramFileDetails;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.solver.config.GuessMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.rules.NonogramRules.mapNonogramFileDetailsToNonogramRules;

//@Component
//@Order(9)
public class SymmetricNonogramLoggerInitializer implements CommandLineRunner {
    public final static String puzzlePath = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.NONOGRAM_PATH_SUFFIX;

    @Autowired
    CommonService commonService;

    private final List<String> nonograms1Dsymmetrical = new ArrayList<>();
    private final List<String> nonograms2Dsymmetrical = new ArrayList<>();
    private final List<String> nonograms3Dsymmetrical = new ArrayList<>();


    @Override
    public void run(String... args) {

        System.out.println("Symmetrical nonograms logger(9)");

        Set<String> existingFilesNames = commonService
                .listFilesUsingJavaIO(puzzlePath);

        ObjectMapper objectMapper = new ObjectMapper();

        for (String nonogramFileName : existingFilesNames) {

            try {
                NonogramFileDetails nonogramFileDetails = objectMapper.readValue(new File(puzzlePath + nonogramFileName), NonogramFileDetails.class);
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
                System.out.printf("Can't parse file with name %s\n", nonogramFileName);
            }

        }

        System.out.println("Nonograms 4 axis symmetrical filenames: ");
        for (String nonogramSym : nonograms3Dsymmetrical) {
            System.out.println(nonogramSym);
        }
    }
}
