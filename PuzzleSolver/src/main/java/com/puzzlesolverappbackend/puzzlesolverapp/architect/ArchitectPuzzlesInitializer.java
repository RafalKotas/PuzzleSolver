package com.puzzlesolverappbackend.puzzlesolverapp.architect;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzlesolverapp.common.CommonService;
import com.puzzlesolverappbackend.puzzlesolverapp.constants.InitializerConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Order(2)
@Slf4j
public class ArchitectPuzzlesInitializer implements CommandLineRunner {

    protected static final Logger logger = LoggerFactory.getLogger(ArchitectPuzzlesInitializer.class);

    @Autowired
    private ArchitectRepository architectRepository;

    @Autowired
    CommonService commonService;

    Architect architect;

    String architectFileNameWithoutExtension;
    String source;
    String year;
    String month;
    Double difficulty;
    Integer height;
    Integer width;

    int architectsSaved;
    int architectsRepeated;

    public static final String PUZZLE_PATH = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.ARCHITECT_PATH_SUFFIX;

    @Override
    public void run(String... args) throws Exception {

        log.info("Architects init(2)");

        architectsSaved = 0;
        architectsRepeated = 0;

        Set<String> existingArchitectFilesNames = commonService
                .listFilesUsingJavaIO(PUZZLE_PATH);

        for (String architectFileName : existingArchitectFilesNames) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                ArchitectFileDetails architectFileDetails = objectMapper.readValue(new File(PUZZLE_PATH + architectFileName), ArchitectFileDetails.class);

                architectFileNameWithoutExtension = architectFileName.substring(0, architectFileName.length() - 5);
                source = architectFileDetails.getSource();
                year = architectFileDetails.getYear();
                month = architectFileDetails.getMonth();
                if (source.equals("logiNonograms")) {
                    if (month.length() > 2) {
                        source = "logiMix";
                    } else {
                        source = "logi";
                    }
                }

                difficulty = architectFileDetails.getDifficulty();
                height = architectFileDetails.getHeight();
                width = architectFileDetails.getWidth();

                architect = new Architect(architectFileNameWithoutExtension, source, year, month, difficulty, height, width);

                if (architectRepository.existsArchitectByGivenParamsFromFile(architectFileNameWithoutExtension, source, year, month, difficulty, height, width).isPresent()) {
                    architectsRepeated++;
                } else {
                    architectsSaved++;
                    architectRepository.save(architect);
                }
            } catch (JsonParseException jsonParseException) {
                log.info("Wrong file part: {}", architectFileName);
                log.info("Exception: {}", jsonParseException.getMessage());
            }
        }

        if (InitializerConstants.PRINT_PUZZLE_STATUS_INFO) {
            log.info("Architects saved: {}", architectsSaved);
            log.info("Architects repeated: {}", architectsRepeated);
        }

        log.info("Saving architects to DB part is done.");
    }

}
