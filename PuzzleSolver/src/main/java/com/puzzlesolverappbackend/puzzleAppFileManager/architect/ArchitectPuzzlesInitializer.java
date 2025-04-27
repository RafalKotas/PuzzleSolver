package com.puzzlesolverappbackend.puzzleAppFileManager.architect;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzleAppFileManager.common.CommonService;
import com.puzzlesolverappbackend.puzzleAppFileManager.constants.InitializerConstants;
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

    protected final static Logger logger = LoggerFactory.getLogger(ArchitectPuzzlesInitializer.class);

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

    public final static String puzzlePath = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.ARCHITECT_PATH_SUFFIX;

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Architects init(2)");

        architectsSaved = 0;
        architectsRepeated = 0;

        Set<String> existingArchitectFilesNames = commonService
                .listFilesUsingJavaIO(puzzlePath);

        for (String architectFileName : existingArchitectFilesNames) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                ArchitectFileDetails architectFileDetails = objectMapper.readValue(new File(puzzlePath + architectFileName), ArchitectFileDetails.class);

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
                System.out.println("Wrong file part: " + architectFileName);
                System.out.println(jsonParseException);
            }
        }

        if (InitializerConstants.PRINT_PUZZLE_STATUS_INFO) {
            System.out.println("architectsSaved count: " + architectsSaved);
            System.out.println("architectsRepeated count: " + architectsRepeated);
        }

        log.info("Saving architects to DB part is done.");
    }

}
