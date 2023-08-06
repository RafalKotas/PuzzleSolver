package com.puzzlesolverappbackend.puzzleAppFileManager.runners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.puzzlesolverappbackend.puzzleAppFileManager.payload.NonogramFileDetails;
import com.puzzlesolverappbackend.puzzleAppFileManager.payload.NonogramLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.services.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//@Component
//@Order(9)
public class SymmetricNonogramLoggerInitializer implements CommandLineRunner {
    public final static String puzzlePath = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.NONOGRAM_PATH_SUFFIX;

    @Autowired
    CommonService commonService;

    private List<String> nonograms1Dsymmetrical = new ArrayList<>();
    private List<String> nonograms2Dsymmetrical = new ArrayList<>();
    private List<String> nonograms3Dsymmetrical = new ArrayList<>();

    private Gson gsonMapper = new Gson();


    @Override
    public void run(String... args) throws Exception {

        System.out.println("Symmetrical nonograms logger(9)");

        Set<String> existingFilesNames = commonService
                .listJsonFilesUsingJavaIO(puzzlePath);

        String filename;

        for (String nonogramFileName : existingFilesNames) {
            ObjectMapper objectMapper = new ObjectMapper();
            NonogramFileDetails nonogramFileDetails = objectMapper.readValue(new File(puzzlePath + nonogramFileName), NonogramFileDetails.class);
            NonogramLogic nonogramLogic = new NonogramLogic(nonogramFileDetails.getRowSequences(), nonogramFileDetails.getColumnSequences());

            switch(nonogramLogic.nonogramSymmetricalGrade()) {
                case "4 axis":
                    nonograms3Dsymmetrical.add(nonogramFileName);
                    break;
                case "2 axis":
                    nonograms2Dsymmetrical.add(nonogramFileName);
                    break;
                case "1 axis":
                    nonograms1Dsymmetrical.add(nonogramFileName);
                    break;
                default:
                    break;
            }
        }

        System.out.println("Nonograms 4 axis symmetrical filenames: ");
        for(String nonogramSym : nonograms3Dsymmetrical) {
            System.out.println(nonogramSym);
        }
    }
}
