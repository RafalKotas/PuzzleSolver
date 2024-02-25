package com.puzzlesolverappbackend.puzzleAppFileManager.runners;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzleAppFileManager.model.Architect;
import com.puzzlesolverappbackend.puzzleAppFileManager.payload.ArchitectFileDetails;
import com.puzzlesolverappbackend.puzzleAppFileManager.repository.ArchitectRepository;
import com.puzzlesolverappbackend.puzzleAppFileManager.services.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Set;

@Component
@Order(2)
public class ArchitectPuzzlesInitializer implements CommandLineRunner {

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
                if(source.equals("logiNonograms")) {
                    if(month.length() > 2) {
                        source = "logiMix";
                    } else {
                        source = "logi";
                    }
                }

                difficulty = architectFileDetails.getDifficulty();
                height = architectFileDetails.getHeight();
                width = architectFileDetails.getWidth();

                architect = new Architect(architectFileNameWithoutExtension, source, year, month, difficulty, height, width);

                if(architectRepository.existsArchitectByGivenParamsFromFile(architectFileNameWithoutExtension, source, year, month, difficulty, height, width).isPresent()) {
                    architectsRepeated++;
                } else {
                    System.out.println(architect);
                    architectsSaved++;
                    architectRepository.save(architect);
                }
            } catch (JsonParseException jsonParseException) {
                System.out.println("Wrong file part: " + architectFileName);
                System.out.println(jsonParseException);
            }
        }

        if(InitializerConstants.PRINT_PUZZLE_STATUS_INFO) {
            System.out.println("architectsSaved count: " + architectsSaved);
            System.out.println("architectsRepeated count: " + architectsRepeated);
        }
    }
}
