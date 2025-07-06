package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.initializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzlesolverapp.common.CommonService;
import com.puzzlesolverappbackend.puzzlesolverapp.constants.InitializerConstants;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Nonogram;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramFileDetails;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.repository.NonogramRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
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
import java.util.stream.Stream;

@Component
@Profile("!test")
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

    protected static final List<List<String>> sourceMonthCombinations = new ArrayList<>();

    protected static final List<String> filesToCorrect= new ArrayList<>();

    int filesCount = 0;
    int filesOK = 0;

    public static final String PUZZLE_PATH = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.NONOGRAM_PATH_SUFFIX;

    public NonogramsDataInitializer(NonogramRepository nonogramRepository, CommonService commonService) {
        this.nonogramRepository = nonogramRepository;
        this.commonService = commonService;
    }

    @Override
    public void run(String... args) throws Exception {

        newNonogramsSaved = 0;
        nonogramsRepeated = 0;

        log.info("Nonograms init(4)");

        Set<String> existingFilesNames = commonService
                .listFilesUsingJavaIO(PUZZLE_PATH);

        ObjectMapper objectMapper = new ObjectMapper();

        try (Stream<Path> files = Files.list(Paths.get(PUZZLE_PATH))) {
            for (Path filePath : files.toList()) {
                parseNonogramFile(filePath);
            }
        } catch (IOException e) {
            throw new IOException("Can't read file(s) from directory " + PUZZLE_PATH, e);
        }


        log.info("Files OK: {}, filesCount: {}, to Correct: {}", filesOK, filesCount, (filesCount - filesOK));

        for (String fileToCorrect : filesToCorrect) {
            log.info("\"{}\" ", fileToCorrect);
        }

        for (String nonogramFileName : existingFilesNames) {
            try {
                NonogramFileDetails nonogramFileDetails = objectMapper.readValue(new File(PUZZLE_PATH + nonogramFileName), NonogramFileDetails.class);

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


                saveNewNonogramsToDatabaseWithNewAndOldPuzzlesCount();
            } catch(Exception e) {
                log.error("Parse Exception for filename: {}\n", nonogramFileName);
            }
        }

        log.info("Saving nonograms to DB part is done.");

        printStatsIfEnabled();
    }

    private void saveNewNonogramsToDatabaseWithNewAndOldPuzzlesCount() {

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
            log.info("newNonogramsSaved count: {}", newNonogramsSaved);
            log.info("nonogramsRepeated count: {}", nonogramsRepeated);
        }
    }

    private void addToSourceMonthCombinationsIfNotExist(List<String> sourceMonthCombination) {
        boolean sourceMonthCombinationExist = sourceMonthCombinations
                .stream()
                .anyMatch(combination -> combination.size() == 2 &&
                        combination.get(0).equals(sourceMonthCombination.get(0)) &&
                        combination.get(1).equals(sourceMonthCombination.get(1)));

        if (!sourceMonthCombinationExist) {
            sourceMonthCombinations.add(sourceMonthCombination);
        }
    }

    private void parseNonogramFile(Path filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> fileLines;

        try {
            fileLines = Files.readAllLines(filePath);
            NonogramFileDetails nonogramFileDetails = objectMapper.readValue(
                    new File(PUZZLE_PATH + filePath.getFileName()),
                    NonogramFileDetails.class
            );
            analyzeNonogramFileCorrectness(fileLines, filePath, nonogramFileDetails);
            filesCount++;
        } catch (IOException e) {
            log.error("IOException while parsing file '{}': {}", filePath.getFileName(), e.getMessage(), e);
            throw e;
        }
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

        return fileLines.stream()
                .filter(fileLine -> propertyPattern.matcher(fileLine).find())
                .map(propertyLine -> {
                    Matcher matcher = propertyPattern.matcher(propertyLine);
                    matcher.find();
                    return matcher.group();
                })
                .map(property -> property.replace("\"", "")
                        .replace(" ", "")
                        .replace(":", ""))
                .toList();
    }

    private int calculateMinimumNonogramFileLinesNeeded(List<String> nonogramPropsInOrder, NonogramFileDetails nonogramFileDetails) {
        int fixedLinesCount = 4;

        int linesForRowsSequences = (int) Math.ceil(nonogramFileDetails.getHeight() / SEQUENCES_IN_ONE_SECTION_COUNT);
        int linesForColumnsSequences = (int) Math.ceil(nonogramFileDetails.getWidth() / SEQUENCES_IN_ONE_SECTION_COUNT);

        return fixedLinesCount + nonogramPropsInOrder.size() + linesForRowsSequences + linesForColumnsSequences;
    }

    private boolean verifyIfPropsInRequiredOrder(List<String> propsFromFile) {
        List<String> propertiesToCheck = propsFromFile.stream()
                .filter(fileConstantProperties::contains)
                .toList();

        return propertiesToCheck.equals(fileConstantProperties);
    }
}