package com.puzzlesolverappbackend.puzzlesolverapp.akari;

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
import java.io.IOException;
import java.util.Set;

@Component
@Profile("!test")
@Order(1)
@Getter
@Setter
@Slf4j
public class AkariDataInitializer implements CommandLineRunner {

    private final AkariRepository akariRepository;
    private final CommonService commonService;

    @Autowired
    public AkariDataInitializer(AkariRepository akariRepository, CommonService commonService) {
        this.akariRepository = akariRepository;
        this.commonService = commonService;
    }

    protected String puzzlePath = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.AKARI_PATH_SUFFIX;

    @Override
    public void run(String... args) throws Exception {
        log.info("Akari init(1)");

        int akariSaved = 0;
        int akariRepeated = 0;

        Set<String> existingAkariFilesNames = commonService
                .listFilesUsingJavaIO(puzzlePath);

        for (String akariFileName : existingAkariFilesNames) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                AkariFileDetails akariFileDetails = objectMapper.readValue(
                        new File(puzzlePath + akariFileName), AkariFileDetails.class);

                String akariFileNameWithoutExtension = akariFileName.substring(0, akariFileName.length() - 5);
                String source = akariFileDetails.getSource();
                Double difficulty = akariFileDetails.getDifficulty();
                Integer height = akariFileDetails.getHeight();
                Integer width = akariFileDetails.getWidth();

                Akari akari = new Akari(akariFileNameWithoutExtension, source, difficulty, height, width);

                if (this.akariRepository.existsAkariByGivenParamsFromFile(
                        akariFileNameWithoutExtension, source, difficulty, height, width).isPresent()) {
                    akariRepeated++;
                } else {
                    akariRepository.save(akari);
                    akariSaved++;
                    log.info("New akari saved: {}", akari);
                }
            } catch (IOException e) {
                log.error("Wrong file part: {}", akariFileName);
                log.error("Error {}", e.getMessage());
            }
        }

        if (InitializerConstants.PRINT_PUZZLE_STATUS_INFO) {
            log.info("akari saved: {}", akariSaved);
            log.info("akari repeated: {}", akariRepeated);
        }

        log.info("Saving akaris to DB part is done.");
    }
}
