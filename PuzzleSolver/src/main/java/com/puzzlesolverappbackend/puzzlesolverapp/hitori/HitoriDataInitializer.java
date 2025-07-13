package com.puzzlesolverappbackend.puzzlesolverapp.hitori;

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
import java.io.IOException;
import java.util.Set;

@Component
@Profile("!test")
@Order(3)
@Getter
@Setter
@Slf4j
public class HitoriDataInitializer implements CommandLineRunner {

    private final HitoriRepository hitoriRepository;
    private final CommonService commonService;

    public HitoriDataInitializer(HitoriRepository hitoriRepository, CommonService commonService) {
        this.hitoriRepository = hitoriRepository;
        this.commonService = commonService;
    }

    protected String puzzlePath = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.HITORI_PATH_SUFFIX;

    @Override
    public void run(String... args) {
        log.info("Hitoris init(3)");

        int hitorisSaved = 0;
        int hitorisRepeated = 0;

        Set<String> existingHitoriFilesNames = commonService
                .listFilesUsingJavaIO(puzzlePath);

        for (String hitoriFileName : existingHitoriFilesNames) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                HitoriFileDetails hitoriFileDetails = objectMapper.readValue(
                        new File(puzzlePath + hitoriFileName), HitoriFileDetails.class);

                String hitoriFileNameWithoutExtension = hitoriFileName.substring(0, hitoriFileName.length() - 5);
                String source = hitoriFileDetails.getSource();
                Double difficulty = hitoriFileDetails.getDifficulty();
                Integer height = hitoriFileDetails.getHeight();
                Integer width = hitoriFileDetails.getWidth();

                Hitori hitori = new Hitori(hitoriFileNameWithoutExtension, source, difficulty, height, width);

                if (hitoriRepository.existsHitoriByGivenParamsFromFile(
                        hitoriFileNameWithoutExtension, source, difficulty, height, width).isPresent()) {
                    hitorisRepeated++;
                } else {
                    hitoriRepository.save(hitori);
                    hitorisSaved++;
                    log.info("New hitori saved: {}", hitoriFileName);
                }
            } catch (IOException e) {
                log.error("Wrong file part: {}", hitoriFileName);
                log.error("Error {}", e.getMessage());
            }
        }

        if (InitializerConstants.PRINT_PUZZLE_STATUS_INFO) {
            log.info("hitori saved count: {}", hitorisSaved);
            log.info("hitori repeated count: {}", hitorisRepeated);
        }

        log.info("Saving hitoris to DB part is done.");
    }
}
