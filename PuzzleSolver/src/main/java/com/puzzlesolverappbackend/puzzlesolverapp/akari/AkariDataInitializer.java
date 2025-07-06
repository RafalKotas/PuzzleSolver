package com.puzzlesolverappbackend.puzzlesolverapp.akari;

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
@Order(1)
@Slf4j
public class AkariDataInitializer implements CommandLineRunner {

    @Autowired
    private AkariRepository akariRepository;

    @Autowired
    CommonService commonService;

    Akari akari;

    String akariFileNameWithoutExtension;
    String source;
    Double difficulty;
    Integer height;
    Integer width;

    int akariSaved;
    int akariRepeated;

    public static final String PUZZLE_PATH = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.AKARI_PATH_SUFFIX;

    @Override
    public void run(String... args) throws Exception {


        log.info("Akari init(1)");

        akariSaved = 0;
        akariRepeated = 0;

        Set<String> existingAkariFilesNames = commonService
                .listFilesUsingJavaIO(PUZZLE_PATH);

        for (String akariFileName : existingAkariFilesNames) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                AkariFileDetails akariFileDetails = objectMapper.readValue(new File(PUZZLE_PATH + akariFileName), AkariFileDetails.class);

                akariFileNameWithoutExtension = akariFileName.substring(0, akariFileName.length() - 5);
                source = akariFileDetails.getSource();

                difficulty = akariFileDetails.getDifficulty();
                height = akariFileDetails.getHeight();
                width = akariFileDetails.getWidth();

                akari = new Akari(akariFileNameWithoutExtension, source, difficulty, height, width);

                if (akariRepository.existsAkariByGivenParamsFromFile(akariFileNameWithoutExtension, source, difficulty, height, width).isPresent()) {
                    akariRepeated++;
                } else {
                    log.info("New akari saved: {}", akari);
                    akariSaved++;
                    akariRepository.save(akari);
                }
            } catch (JsonParseException jsonParseException) {
                log.error("Wrong file part: {}", akariFileName);
                log.error("Error {}", jsonParseException.getMessage());
            }
        }

        if (InitializerConstants.PRINT_PUZZLE_STATUS_INFO) {
            log.info("akari saved: {}", akariSaved);
            log.info("akari repeated: {}", akariRepeated);
        }
    }
}
