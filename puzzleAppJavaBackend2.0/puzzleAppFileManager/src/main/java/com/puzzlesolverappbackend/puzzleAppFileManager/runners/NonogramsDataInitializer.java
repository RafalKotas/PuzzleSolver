package com.puzzlesolverappbackend.puzzleAppFileManager.runners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzleAppFileManager.model.Nonogram;
import com.puzzlesolverappbackend.puzzleAppFileManager.payload.NonogramFileDetails;
import com.puzzlesolverappbackend.puzzleAppFileManager.repository.NonogramRepository;

import com.puzzlesolverappbackend.puzzleAppFileManager.services.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;

//@Component
//@Order(4)
public class NonogramsDataInitializer implements CommandLineRunner {

    @Autowired
    private NonogramRepository nonogramRepository;

    @Autowired
    CommonService commonService;

    Nonogram nonogram;

    String nonogramFileNameWithoutExtension;
    String source;
    String year;
    String month;
    Double difficulty;
    Integer height;
    Integer width;

    int newNonogramsSaved;
    int nonogramsRepeated;

    public final static String puzzlePath = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.NONOGRAM_PATH_SUFFIX;

    @Override
    public void run(String... args) throws Exception {

        newNonogramsSaved = 0;
        nonogramsRepeated = 0;

        System.out.println("Nonograms init(4)");

        Set<String> existingFilesNames = commonService
                .listJsonFilesUsingJavaIO(puzzlePath);

        for (String nonogramFileName : existingFilesNames) {
            ObjectMapper objectMapper = new ObjectMapper();
            NonogramFileDetails nonogramFileDetails = objectMapper.readValue(new File(puzzlePath + nonogramFileName), NonogramFileDetails.class);

            nonogramFileNameWithoutExtension = nonogramFileName.substring(0, nonogramFileName.length() - 5);
            source = nonogramFileDetails.getSource();
            year = nonogramFileDetails.getYear();
            month = nonogramFileDetails.getMonth();

            this.correctSource();

            difficulty = nonogramFileDetails.getDifficulty();
            height = nonogramFileDetails.getHeight();
            width = nonogramFileDetails.getWidth();


            nonogram = new Nonogram(nonogramFileNameWithoutExtension, source, year, month, difficulty, height, width);

            updateOldNewNonogramsStats();
        }

        printStatsIfEnabled();
    }

    private void correctSource() {
        if(source.equals("logiNonograms")) {
            if(month.length() > 2) {
                if(month.endsWith("supplement")) {
                    source = "logi";
                    month = month.substring(0, 2);
                } else {
                    source = "logiMix";
                    month = month.substring(0, 2);
                }
            } else {
                source = "logi";
            }
        }
    }

    private void updateOldNewNonogramsStats() {
        if(nonogramRepository.existsNonogramByGivenParamsFromFile(nonogramFileNameWithoutExtension, source, year, month, difficulty,
                height, width).isPresent()) {
            nonogramsRepeated++;
        } else {
            System.out.println(nonogram);
            newNonogramsSaved++;
            nonogramRepository.save(nonogram);
        }
    }

    private void printStatsIfEnabled() {
        if(InitializerConstants.PRINT_PUZZLE_STATUS_INFO) {
            System.out.println("newNonogramsSaved count: " + newNonogramsSaved);
            System.out.println("nonogramsRepeated count: " + nonogramsRepeated);
        }
    }
}