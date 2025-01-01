package com.puzzlesolverappbackend.puzzleAppFileManager.runners;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzleAppFileManager.model.Akari;
import com.puzzlesolverappbackend.puzzleAppFileManager.payload.AkariFileDetails;
import com.puzzlesolverappbackend.puzzleAppFileManager.repository.AkariRepository;
import com.puzzlesolverappbackend.puzzleAppFileManager.services.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.io.File;
import java.util.Set;

//@Component
//@Order(1)
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

    int akarisSaved;
    int akarisRepeated;

    public final static String puzzlePath = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.AKARI_PATH_SUFFIX;

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Akaris init(1)");

        akarisSaved = 0;
        akarisRepeated = 0;

        Set<String> existingAkariFilesNames = commonService
                .listFilesUsingJavaIO(puzzlePath);

        for (String akariFileName : existingAkariFilesNames) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                AkariFileDetails akariFileDetails = objectMapper.readValue(new File(puzzlePath + akariFileName), AkariFileDetails.class);

                akariFileNameWithoutExtension = akariFileName.substring(0, akariFileName.length() - 5);
                source = akariFileDetails.getSource();

                difficulty = akariFileDetails.getDifficulty();
                height = akariFileDetails.getHeight();
                width = akariFileDetails.getWidth();

                akari = new Akari(akariFileNameWithoutExtension, source, difficulty, height, width);

                if (akariRepository.existsAkariByGivenParamsFromFile(akariFileNameWithoutExtension, source, difficulty, height, width).isPresent()) {
                    akarisRepeated++;
                } else {
                    System.out.println(akari);
                    akarisSaved++;
                    akariRepository.save(akari);
                }
            } catch (JsonParseException jsonParseException) {
                System.out.println("Wrong file part: " + akariFileName);
                System.out.println(jsonParseException);
            }
        }

        if (InitializerConstants.PRINT_PUZZLE_STATUS_INFO) {
            System.out.println("akarisSaved count: " + akarisSaved);
            System.out.println("akarisRepeated count: " + akarisRepeated);
        }
    }
}
