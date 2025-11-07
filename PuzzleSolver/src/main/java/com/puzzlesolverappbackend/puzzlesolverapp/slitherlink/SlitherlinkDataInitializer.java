package com.puzzlesolverappbackend.puzzlesolverapp.slitherlink;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzlesolverapp.common.CommonService;
import com.puzzlesolverappbackend.puzzlesolverapp.constants.InitializerConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.io.IOException;
import java.util.Set;

//@Component
@Profile("!test")
//@Order(5)
@Getter
@Setter
@Slf4j
public class SlitherlinkDataInitializer implements CommandLineRunner {

    private final SlitherlinkRepository slitherlinkRepository;
    private final CommonService commonService;

    public SlitherlinkDataInitializer(SlitherlinkRepository slitherlinkRepository, CommonService commonService) {
        this.slitherlinkRepository = slitherlinkRepository;
        this.commonService = commonService;
    }

    protected String puzzlePath = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.SLITHERLINK_PATH_SUFFIX;

    @Override
    public void run(String... args) {
        log.info("Slitherlinks init(5)");

        int slitherlinksSaved = 0;
        int slitherlinksRepeated = 0;

        Set<String> existingSlitherlinkFilesNames = commonService
                .listFilesUsingJavaIO(puzzlePath);

        for (String slitherlinkFileName : existingSlitherlinkFilesNames) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                SlitherlinkFileDetails slitherlinkFileDetails = objectMapper.readValue(
                        new File(puzzlePath + slitherlinkFileName), SlitherlinkFileDetails.class);

                String slitherlinkFileNameWithoutExtension = slitherlinkFileName.substring(0, slitherlinkFileName.length() - 5);
                String source = slitherlinkFileDetails.getSource();
                Double difficulty = slitherlinkFileDetails.getDifficulty();
                Integer height = slitherlinkFileDetails.getHeight();
                Integer width = slitherlinkFileDetails.getWidth();
                String year = slitherlinkFileDetails.getYear();
                String month = slitherlinkFileDetails.getMonth();

                Slitherlink slitherlink = new Slitherlink(slitherlinkFileNameWithoutExtension, source, year, month, difficulty, height, width);

                if (slitherlinkRepository.existsSlitherlinkByGivenParamsFromFile(
                        slitherlinkFileNameWithoutExtension, source, year, month, difficulty, height, width).isPresent()) {
                    slitherlinksRepeated++;
                } else {
                    slitherlinkRepository.save(slitherlink);
                    slitherlinksSaved++;
                    log.info("New slitherlink saved: {}", slitherlinkFileName);
                }
            } catch (IOException e) {
                log.error("Wrong file part: {} ", slitherlinkFileName);
                log.error("Exception: {}", e.getMessage());
            }
        }

        if (InitializerConstants.PRINT_PUZZLE_STATUS_INFO) {
            log.info("Slitherlinks saved count: {}", slitherlinksSaved);
            log.info("Slitherlinks repeated count: {}", slitherlinksRepeated);
        }

        log.info("Saving slitherlinks to DB part is done.");
    }
}
