package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.initializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzlesolverapp.common.CommonService;
import com.puzzlesolverappbackend.puzzlesolverapp.constants.InitializerConstants;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramFileDetails;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.config.GuessMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules.mapNonogramFileDetailsToNonogramRules;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.utils.NonogramSymmetryHelper.getSymmetryGrade;

//@Component
//@Order(9)
@Slf4j
public class SymmetricNonogramLoggerInitializer implements CommandLineRunner {
    static final String DEFAULT_BASE_PATH =
            InitializerConstants.PUZZLE_RELATIVE_PATH + InitializerConstants.PuzzleMappings.NONOGRAM_PATH_SUFFIX;

    private final CommonService commonService;
    private final ObjectMapper objectMapper;
    private final String basePath;

    private final List<String> nonograms1Dsymmetrical = new ArrayList<>();
    private final List<String> nonograms2Dsymmetrical = new ArrayList<>();
    private final List<String> nonograms3Dsymmetrical = new ArrayList<>();

    public SymmetricNonogramLoggerInitializer(CommonService commonService) {
        this(commonService, new ObjectMapper(), DEFAULT_BASE_PATH);
    }

    SymmetricNonogramLoggerInitializer(CommonService commonService, ObjectMapper objectMapper, String basePath) {
        this.commonService = commonService;
        this.objectMapper = objectMapper;
        this.basePath = basePath.endsWith(File.separator) ? basePath : (basePath + File.separator);
    }

    @Override
    public void run(String... args) {
        Set<String> existingFilesNames = commonService.listFilesUsingJavaIO(basePath);

        for (String fileName : existingFilesNames) {
            try {
                NonogramFileDetails nfd = objectMapper.readValue(new File(basePath + fileName), NonogramFileDetails.class);
                NonogramRules rules = mapNonogramFileDetailsToNonogramRules(nfd);
                NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

                switch (getSymmetryGrade(logic)) {
                    case "4 axis" -> nonograms3Dsymmetrical.add(fileName);
                    case "2 axis" -> nonograms2Dsymmetrical.add(fileName);
                    case "1 axis" -> nonograms1Dsymmetrical.add(fileName);
                    default -> { /* ignore */ }
                }
            } catch (Exception e) {
                log.error("Can't parse file with name: {}", fileName, e);
            }
        }

        log.info("Nonograms 4 axis symmetrical filenames: ");
        for (String f : nonograms3Dsymmetrical) log.info("{}", f);
    }

    List<String> getNonograms1Dsymmetrical() { return nonograms1Dsymmetrical; }
    List<String> getNonograms2Dsymmetrical() { return nonograms2Dsymmetrical; }
    List<String> getNonograms3Dsymmetrical() { return nonograms3Dsymmetrical; }
}
