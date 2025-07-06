package com.puzzlesolverappbackend.puzzlesolverapp.hitori;

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
@Order(3)
@Slf4j
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

    public static final String PUZZLE_PATH = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.HITORI_PATH_SUFFIX;

    @Override
    public void run(String... args) throws Exception {

        log.info("Hitoris init(3)");

        hitorisSaved = 0;
        hitorisRepeated = 0;

        Set<String> existingHitoriFilesNames = commonService
                .listFilesUsingJavaIO(PUZZLE_PATH);

        for (String hitoriFileName : existingHitoriFilesNames) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                HitoriFileDetails hitoriFileDetails = objectMapper.readValue(new File(PUZZLE_PATH + hitoriFileName), HitoriFileDetails.class);

                hitoriFileNameWithoutExtension = hitoriFileName.substring(0, hitoriFileName.length() - 5);
                source = hitoriFileDetails.getSource();

                difficulty = hitoriFileDetails.getDifficulty();
                height = hitoriFileDetails.getHeight();
                width = hitoriFileDetails.getWidth();

                hitori = new Hitori(hitoriFileNameWithoutExtension, source, difficulty, height, width);

                if (hitoriRepository.existsHitoriByGivenParamsFromFile(hitoriFileNameWithoutExtension, source, difficulty, height, width).isPresent()) {
                    hitorisRepeated++;
                } else {
                    hitorisSaved++;
                    hitoriRepository.save(hitori);
                }
            } catch (JsonParseException jsonParseException) {
                log.error("Wrong file part: {} ", hitoriFileName);
                log.error("Exception: {}", jsonParseException.getMessage());
            }
        }

        if (InitializerConstants.PRINT_PUZZLE_STATUS_INFO) {
            log.info("hitori saved count: " + hitorisSaved);
            log.info("hitori repeated count: " + hitorisRepeated);
        }
    }
}
