package com.puzzlesolverappbackend.puzzleAppFileManager.runners;

import com.puzzlesolverappbackend.puzzleAppFileManager.model.Hitori;
import com.puzzlesolverappbackend.puzzleAppFileManager.payload.HitoriFileDetails;
import com.puzzlesolverappbackend.puzzleAppFileManager.repository.HitoriRepository;
import com.puzzlesolverappbackend.puzzleAppFileManager.services.CommonService;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Set;

@Component
@Order(3)
public class HitoriDataInitializer implements CommandLineRunner {

    @Autowired
    private HitoriRepository hitoriRepository;

    @Autowired
    CommonService commonService;

    Hitori hitori;

    String hitoriFileNameWithoutExtension;
    String source;
    Double difficulty;
    Integer height;
    Integer width;

    int hitorisSaved;
    int hitorisRepeated;

    public final static String puzzlePath = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.HITORI_PATH_SUFFIX;

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Hitoris init(3)");

        hitorisSaved = 0;
        hitorisRepeated = 0;

        Set<String> existingHitoriFilesNames = commonService
                .listFilesUsingJavaIO(puzzlePath);

        for (String hitoriFileName : existingHitoriFilesNames) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                HitoriFileDetails hitoriFileDetails = objectMapper.readValue(new File(puzzlePath + hitoriFileName), HitoriFileDetails.class);

                hitoriFileNameWithoutExtension = hitoriFileName.substring(0, hitoriFileName.length() - 5);
                source = hitoriFileDetails.getSource();

                difficulty = hitoriFileDetails.getDifficulty();
                height = hitoriFileDetails.getHeight();
                width = hitoriFileDetails.getWidth();

                hitori = new Hitori(hitoriFileNameWithoutExtension, source, difficulty, height, width);

                if(hitoriRepository.existsHitoriByGivenParamsFromFile(hitoriFileNameWithoutExtension, source, difficulty, height, width).isPresent()) {
                    hitorisRepeated++;
                } else {
                    System.out.println(hitori);
                    hitorisSaved++;
                    hitoriRepository.save(hitori);
                }
            } catch (JsonParseException jsonParseException) {
                System.out.println("Wrong file part: " + hitoriFileName);
                System.out.println(jsonParseException);
            }
        }

        if(InitializerConstants.PRINT_PUZZLE_STATUS_INFO) {
            System.out.println("hitorisSaved count: " + hitorisSaved);
            System.out.println("hitorisRepeated count: " + hitorisRepeated);
        }
    }
}
