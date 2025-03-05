package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.initializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.Nonogram;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.NonogramFileDetails;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.NonogramRepository;
import com.puzzlesolverappbackend.puzzleAppFileManager.runners.InitializerConstants;
import com.puzzlesolverappbackend.puzzleAppFileManager.services.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@Order(4)
@Slf4j
public class NonogramsDataInitializer implements CommandLineRunner {

    private static final int JSON_EXTENSION_LENGTH = 5;

    private static final double SEQUENCES_IN_ONE_SECTION_COUNT = 5.0;

    private final NonogramRepository nonogramRepository;

    private final CommonService commonService;

    private final List<String> fileConstantProperties = List.of(
            "filename",
            "source",
            "year",
            "month",
            "difficulty",
            "height",
            "width",
            "rowSequences",
            "columnSequences");

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

    List<List<String>> sourceMonthCombinations;

    List<String> filesToCorrect;

    int filesCount = 0;
    int filesOK = 0;

    public final static String puzzlePath = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.NONOGRAM_PATH_SUFFIX;

    public NonogramsDataInitializer(NonogramRepository nonogramRepository, CommonService commonService) {
        this.nonogramRepository = nonogramRepository;
        this.commonService = commonService;
        this.sourceMonthCombinations = new ArrayList<>();
        this.filesToCorrect = new ArrayList<>();
    }

    @Override
    public void run(String... args) throws Exception {

        newNonogramsSaved = 0;
        nonogramsRepeated = 0;

        System.out.println("Nonograms init(4)");

        Set<String> existingFilesNames = commonService
                .listFilesUsingJavaIO(puzzlePath);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Get files paths from given folder
            List<Path> files = Files.list(Paths.get(puzzlePath)).toList();

            // Iterate through files list and print its contents line after line
            for (Path filePath : files) {
                parseNonogramFile(filePath);
            }
        } catch (IOException e) {
            throw new IOException("Can't read file/s from directory " + puzzlePath);
        }

        System.out.println("Files OK: " + filesOK + " filesCount: " + filesCount + " to Correct: " + (filesCount - filesOK));

        for (String s : filesToCorrect) {
            System.out.print("\"" + s + "\" ");
        }

        for (String nonogramFileName : existingFilesNames) {
            try {
                NonogramFileDetails nonogramFileDetails = objectMapper.readValue(new File(puzzlePath + nonogramFileName), NonogramFileDetails.class);

                nonogramFileNameWithoutExtension = nonogramFileName.substring(0, nonogramFileName.length() - JSON_EXTENSION_LENGTH);
                source = nonogramFileDetails.getSource();
                year = nonogramFileDetails.getYear();
                month = nonogramFileDetails.getMonth();

                difficulty = nonogramFileDetails.getDifficulty();
                height = nonogramFileDetails.getHeight();
                width = nonogramFileDetails.getWidth();

                List<String> sourceMonthCombination = new ArrayList<>();
                sourceMonthCombination.add(source);
                sourceMonthCombination.add(month);
                addToSourceMonthCombinationsIfNotExist(sourceMonthCombination);

                nonogram = new Nonogram(nonogramFileNameWithoutExtension, source, year, month, difficulty, height, width);


                updateOldNewNonogramsStats();
            } catch(Exception e) {
                System.out.printf("Parse Exception for filename: %s\n", nonogramFileName);
            }
        }

        printStatsIfEnabled();
    }

    private void updateOldNewNonogramsStats() {

        if (nonogramRepository.existsNonogramByGivenParamsFromFile(nonogramFileNameWithoutExtension, source, year, month, difficulty,
                height, width).isPresent()) {
            nonogramsRepeated++;
        } else {
            log.info("Saving nonogram with name {}", nonogram.getFilename());
            nonogramRepository.save(nonogram);
            newNonogramsSaved++;
        }
    }

    private void printStatsIfEnabled() {
        if (InitializerConstants.PRINT_PUZZLE_STATUS_INFO) {
            System.out.println("newNonogramsSaved count: " + newNonogramsSaved);
            System.out.println("nonogramsRepeated count: " + nonogramsRepeated);
        }
    }

    private void addToSourceMonthCombinationsIfNotExist(List<String> sourceMonthCombination) {
        boolean sourceMonthCombinationExist = this.sourceMonthCombinations
                .stream()
                .anyMatch(combination -> combination.size() == 2 &&
                        combination.get(0).equals(sourceMonthCombination.get(0)) &&
                        combination.get(1).equals(sourceMonthCombination.get(1)));

        if (!sourceMonthCombinationExist) {
            this.sourceMonthCombinations.add(sourceMonthCombination);
        }
    }

    private void parseNonogramFile(Path filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Read lines from file
        List<String> fileLines;
        try {
            fileLines = Files.lines(filePath).collect(Collectors.toList());
        } catch (IOException e) {
            throw new IOException(e);
        }
        NonogramFileDetails nonogramFileDetails;
        try {
            nonogramFileDetails = objectMapper.readValue(new File(puzzlePath + filePath.getFileName()), NonogramFileDetails.class);
            analyzeNonogramFileCorrectness(fileLines, filePath, nonogramFileDetails);
        } catch (IOException e) {
            log.error("error while mapping, filename: {}", filePath.getFileName());
            throw new IOException(e);
        }

        filesCount++;
    }

    private void analyzeNonogramFileCorrectness(List<String> fileLines, Path filePath, NonogramFileDetails nonogramFileDetails) {
        String fullFilename = filePath.getFileName().toString();
        String fileNameWithoutExtension = fullFilename.substring(0, fullFilename.length() - JSON_EXTENSION_LENGTH);

        List<String> nonogramPropsInOrder = getNonogramPropsFromFile(fileLines);

        int minimumLinesRequired = calculateMinimumNonogramFileLinesNeeded(nonogramPropsInOrder, nonogramFileDetails);

        boolean requiredMinimumLinesCondition = fileLines.size() == minimumLinesRequired;
        boolean arePropsInRequiredOrder = verifyIfPropsInRequiredOrder(nonogramPropsInOrder);

        if (!requiredMinimumLinesCondition) {
            log.error("File {} not has minimum required lines({})", fullFilename, minimumLinesRequired);
            filesToCorrect.add(fileNameWithoutExtension);
        } else if (!arePropsInRequiredOrder) {
            log.error("File {} props are not in correct order", fullFilename);
            filesToCorrect.add(fileNameWithoutExtension);
        } else {
            filesOK++;
        }
    }

    private List<String> getNonogramPropsFromFile(List<String> fileLines) {
        Pattern propertyPattern = Pattern.compile("\"[a-zA-Z]*\" :");

        return fileLines.stream().filter(fileLine -> {
            Matcher matcher = propertyPattern.matcher(fileLine);
            return matcher.find();
        }).map(propertyLine -> {
            Matcher matcher = propertyPattern.matcher(propertyLine);
            matcher.find();
            return matcher.group();
        }).map(property -> property.replace("\"", "").replace(" ", "").replace(":", "")).collect(Collectors.toList());
    }

    private int calculateMinimumNonogramFileLinesNeeded(List<String> nonogramPropsInOrder, NonogramFileDetails nonogramFileDetails) {
        int FIXED_LINES_COUNT = 4;

        int linesForRowsSequences = (int) Math.ceil(nonogramFileDetails.getHeight() / SEQUENCES_IN_ONE_SECTION_COUNT);
        int linesForColumnsSequences = (int) Math.ceil(nonogramFileDetails.getWidth() / SEQUENCES_IN_ONE_SECTION_COUNT);

        return FIXED_LINES_COUNT + nonogramPropsInOrder.size() + linesForRowsSequences + linesForColumnsSequences;
    }

    private boolean verifyIfPropsInRequiredOrder(List<String> propsFromFile) {
        List<String> propertiesToCheck = propsFromFile.stream()
                .filter(fileConstantProperties::contains)
                .toList();

        return propertiesToCheck.equals(fileConstantProperties);
    }
}