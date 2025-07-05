package com.puzzlesolverappbackend.puzzlesolverapp.slitherlink;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzlesolverapp.common.CommonService;
import com.puzzlesolverappbackend.puzzlesolverapp.constants.InitializerConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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

    private final SlitherlinkRepository SlitherlinkRepository;

    final
    CommonService commonService;

    Slitherlink Slitherlink;

    String slitherlinkFileNameWithoutExtension;
    String source;
    Double difficulty;
    Integer height;
    Integer width;
    String year;
    String month;

    int SlitherlinksSaved;
    int SlitherlinksRepeated;

    public final static String puzzlePath = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.SLITHERLINK_PATH_SUFFIX;

    public SlitherlinkDataInitializer(SlitherlinkRepository SlitherlinkRepository, CommonService commonService) {
        this.SlitherlinkRepository = SlitherlinkRepository;
        this.commonService = commonService;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Slitherlinks init(5)");

        SlitherlinksSaved = 0;
        SlitherlinksRepeated = 0;

        Set<String> existingSlitherlinkFilesNames = commonService
                .listFilesUsingJavaIO(puzzlePath);

        for (String SlitherlinkFileName : existingSlitherlinkFilesNames) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                SlitherlinkFileDetails SlitherlinkFileDetails = objectMapper.readValue(new File(puzzlePath + SlitherlinkFileName), SlitherlinkFileDetails.class);

                slitherlinkFileNameWithoutExtension = SlitherlinkFileName.substring(0, SlitherlinkFileName.length() - 5);
                source = SlitherlinkFileDetails.getSource();

                difficulty = SlitherlinkFileDetails.getDifficulty();
                height = SlitherlinkFileDetails.getHeight();
                width = SlitherlinkFileDetails.getWidth();

                year = SlitherlinkFileDetails.getYear();
                month = SlitherlinkFileDetails.getMonth();
                if (source.equals("Logi")) {
                    if (month.length() > 2) {
                        source = "logiMix";
                        month = month.substring(0, 2);
                    } else {
                        source = "logi";
                    }
                }

                Slitherlink = new Slitherlink(slitherlinkFileNameWithoutExtension, source, year, month, difficulty, height, width);

                if (SlitherlinkRepository.existsSlitherlinkByGivenParamsFromFile(slitherlinkFileNameWithoutExtension, source, year, month, difficulty, height, width).isPresent()) {
                    SlitherlinksRepeated++;
                } else {
                    SlitherlinksSaved++;
                    SlitherlinkRepository.save(Slitherlink);
                }
            } catch (JsonParseException jsonParseException) {
                System.out.println("Wrong file part: " + SlitherlinkFileName);
                System.out.println(jsonParseException);
            }
        }

        log.info("Saving slitherlinks to DB part is done.");

        if (InitializerConstants.PRINT_PUZZLE_STATUS_INFO) {
            System.out.println("SlitherlinksSaved count: " + SlitherlinksSaved);
            System.out.println("SlitherlinksRepeated count: " + SlitherlinksRepeated);
        }
    }
}
