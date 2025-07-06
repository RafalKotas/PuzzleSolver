package com.puzzlesolverappbackend.puzzlesolverapp.slitherlink;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzlesolverapp.common.CommonService;
import com.puzzlesolverappbackend.puzzlesolverapp.constants.InitializerConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Set;

@Component
@Getter
@Setter
@Profile("!test")
@Order(5)
@Slf4j
public class SlitherlinkDataInitializer implements CommandLineRunner {

    @Autowired
    private SlitherlinkRepository slitherlinkRepository;

    @Autowired
    CommonService commonService;

    Slitherlink slitherlink;

    String slitherlinkFileNameWithoutExtension;
    String source;
    Double difficulty;
    Integer height;
    Integer width;
    String year;
    String month;

    int slitherlinksSaved;
    int slitherlinksRepeated;

    public static final String PUZZLE_PATH = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.SLITHERLINK_PATH_SUFFIX;

    public SlitherlinkDataInitializer(SlitherlinkRepository slitherlinkRepository, CommonService commonService) {
        this.slitherlinkRepository = slitherlinkRepository;
        this.commonService = commonService;
    }

    @Override
    public void run(String... args) throws Exception {

        log.info("Slitherlinks init(5)");

        slitherlinksSaved = 0;
        slitherlinksRepeated = 0;

        Set<String> existingSlitherlinkFilesNames = commonService
                .listFilesUsingJavaIO(PUZZLE_PATH);

        for (String slitherlinkFileName : existingSlitherlinkFilesNames) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                SlitherlinkFileDetails slitherlinkFileDetails = objectMapper.readValue(new File(PUZZLE_PATH + slitherlinkFileName), SlitherlinkFileDetails.class);

                slitherlinkFileNameWithoutExtension = slitherlinkFileName.substring(0, slitherlinkFileName.length() - 5);
                source = slitherlinkFileDetails.getSource();

                difficulty = slitherlinkFileDetails.getDifficulty();
                height = slitherlinkFileDetails.getHeight();
                width = slitherlinkFileDetails.getWidth();

                year = slitherlinkFileDetails.getYear();
                month = slitherlinkFileDetails.getMonth();
                if (source.equals("Logi")) {
                    if (month.length() > 2) {
                        source = "logiMix";
                        month = month.substring(0, 2);
                    } else {
                        source = "logi";
                    }
                }

                slitherlink = new Slitherlink(slitherlinkFileNameWithoutExtension, source, year, month, difficulty, height, width);

                if (slitherlinkRepository.existsSlitherlinkByGivenParamsFromFile(slitherlinkFileNameWithoutExtension, source, year, month, difficulty, height, width).isPresent()) {
                    slitherlinksRepeated++;
                } else {
                    slitherlinksSaved++;
                    slitherlinkRepository.save(slitherlink);
                }
            } catch (JsonParseException jsonParseException) {
                log.error("Wrong file part: {}", slitherlinkFileName);
                log.error("Exception: {}", jsonParseException.getMessage());
            }
        }

        log.info("Saving slitherlinks to DB part is done.");

        if (InitializerConstants.PRINT_PUZZLE_STATUS_INFO) {
            log.info("Slitherlinks saved count: {}", slitherlinksSaved);
            log.info("Slitherlinks repeated count: {}", slitherlinksRepeated);
        }
    }
}
