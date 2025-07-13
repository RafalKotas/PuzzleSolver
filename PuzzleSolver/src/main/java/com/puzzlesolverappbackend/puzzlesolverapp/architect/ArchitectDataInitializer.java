package com.puzzlesolverappbackend.puzzlesolverapp.architect;

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
@Order(2)
@Getter
@Setter
@Slf4j
public class ArchitectDataInitializer implements CommandLineRunner {

    private final ArchitectRepository architectRepository;
    private final CommonService commonService;

    public ArchitectDataInitializer(ArchitectRepository architectRepository, CommonService commonService) {
        this.architectRepository = architectRepository;
        this.commonService = commonService;
    }

    protected String puzzlePath = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.ARCHITECT_PATH_SUFFIX;

    @Override
    public void run(String... args) {
        log.info("Architects init(2)");

        int architectsSaved = 0;
        int architectsRepeated = 0;

        Set<String> existingArchitectFilesNames = commonService.listFilesUsingJavaIO(puzzlePath);

        for (String architectFileName : existingArchitectFilesNames) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                ArchitectFileDetails details = objectMapper.readValue(
                        new File(puzzlePath + architectFileName), ArchitectFileDetails.class);

                String nameWithoutExtension = architectFileName.substring(0, architectFileName.length() - 5);
                String source = details.getSource();
                String year = details.getYear();
                String month = details.getMonth();
                Double difficulty = details.getDifficulty();
                Integer height = details.getHeight();
                Integer width = details.getWidth();

                Architect architect = new Architect(nameWithoutExtension, source, year, month, difficulty, height, width);

                if (this.architectRepository.existsArchitectByGivenParamsFromFile(
                        nameWithoutExtension, source, year, month, difficulty, height, width).isPresent()) {
                    architectsRepeated++;
                } else {
                    architectRepository.save(architect);
                    architectsSaved++;
                    log.info("New architect saved: {}", architectFileName);
                }
            } catch (IOException e) {
                log.error("Wrong file part: {} ", architectFileName);
                log.error("Exception: {}", e.getMessage());
            }
        }

        if (InitializerConstants.PRINT_PUZZLE_STATUS_INFO) {
            log.info("Architects saved: {}", architectsSaved);
            log.info("Architects repeated: {}", architectsRepeated);
        }

        log.info("Saving architects to DB part is done.");
    }
}
