package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.initializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzlesolverapp.common.CommonService;
import com.puzzlesolverappbackend.puzzlesolverapp.constants.InitializerConstants;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Nonogram;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramFileDetails;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.repository.NonogramRepository;
import lombok.Getter;
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
@Getter
@Slf4j
public class NonogramsDataInitializer implements CommandLineRunner {

    private static final int JSON_EXTENSION_LENGTH = 5;
    private static final double SEQUENCES_IN_ONE_SECTION_COUNT = 5.0;

    private final NonogramRepository nonogramRepository;
    private final CommonService commonService;

    private final List<String> requiredPropertyOrder = List.of(
            "filename", "source", "year", "month", "difficulty", "height", "width", "rowSequences", "columnSequences"
    );

    protected static final List<List<String>> sourceMonthCombinations = new ArrayList<>();
    protected static final List<String> filesToCorrect = new ArrayList<>();

    protected String puzzlePath = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.NONOGRAM_PATH_SUFFIX;

    public NonogramsDataInitializer(NonogramRepository nonogramRepository, CommonService commonService) {
        this.nonogramRepository = nonogramRepository;
        this.commonService = commonService;
    }

    @Override
    public void run(String... args) throws IOException {
        log.info("Nonograms init(4)");

        int filesCount = 0;
        int filesOK = 0;
        int nonogramsSaved = 0;
        int nonogramsRepeated = 0;

        ObjectMapper objectMapper = new ObjectMapper();

        try (Stream<Path> files = Files.list(Paths.get(puzzlePath))) {
            for (Path filePath : files.toList()) {
                try {
                    List<String> fileLines = Files.readAllLines(filePath);
                    NonogramFileDetails fileDetails = objectMapper.readValue(
                            new File(puzzlePath + filePath.getFileName()), NonogramFileDetails.class
                    );
                    boolean isOK = analyzeFileCorrectness(fileLines, filePath, fileDetails);
                    filesCount++;
                    if (isOK) filesOK++;
                } catch (IOException e) {
                    log.error("IOException while parsing file '{}': {}", filePath.getFileName(), e.getMessage());
                    filesToCorrect.add(filePath.getFileName().toString().replace(".json", ""));
                }
            }
        }

        log.info("Files OK: {}, filesCount: {}, to Correct: {}", filesOK, filesCount, filesCount - filesOK);
        filesToCorrect.forEach(file -> log.info("\"{}\" ", file));

        Set<String> existingFiles = commonService.listFilesUsingJavaIO(puzzlePath);
        for (String nonogramFileName : existingFiles) {
            try {
                NonogramFileDetails details = objectMapper.readValue(new File(puzzlePath + nonogramFileName), NonogramFileDetails.class);
                String nameWithoutExtension = nonogramFileName.substring(0, nonogramFileName.length() - JSON_EXTENSION_LENGTH);

                List<String> combination = List.of(details.getSource(), details.getMonth());
                if (!sourceMonthCombinations.contains(combination)) {
                    sourceMonthCombinations.add(combination);
                }

                Nonogram nonogram = new Nonogram(
                        nameWithoutExtension,
                        details.getSource(),
                        details.getYear(),
                        details.getMonth(),
                        details.getDifficulty(),
                        details.getHeight(),
                        details.getWidth()
                );

                boolean exists = nonogramRepository.existsNonogramByGivenParamsFromFile(
                        nameWithoutExtension, details.getSource(), details.getYear(), details.getMonth(),
                        details.getDifficulty(), details.getHeight(), details.getWidth()
                ).isPresent();

                if (exists) {
                    nonogramsRepeated++;
                } else {
                    nonogramRepository.save(nonogram);
                    nonogramsSaved++;
                    log.info("New nonogram saved: {}", nonogramFileName);
                }
            } catch (IOException e) {
                log.error("Wrong file part: {} ", nonogramFileName);
                log.error("Exception: {}", e.getMessage());
            }
        }

        if (InitializerConstants.PRINT_PUZZLE_STATUS_INFO) {
            log.info("New nonograms saved: {}", nonogramsSaved);
            log.info("Nonograms repeated: {}", nonogramsRepeated);
        }

        log.info("Saving nonograms to DB part is done.");
    }

    private boolean analyzeFileCorrectness(List<String> lines, Path filePath, NonogramFileDetails details) {
        String filename = filePath.getFileName().toString();
        String nameWithoutExtension = filename.substring(0, filename.length() - JSON_EXTENSION_LENGTH);

        List<String> propsInOrder = extractProperties(lines);
        int requiredLines = calculateRequiredLines(propsInOrder, details);

        boolean lineCountCorrect = lines.size() == requiredLines;
        boolean orderCorrect = propsInOrder.stream()
                .filter(requiredPropertyOrder::contains)
                .toList()
                .equals(requiredPropertyOrder);

        if (!lineCountCorrect) {
            log.error("File {} does not have required line count ({})", filename, requiredLines);
            filesToCorrect.add(nameWithoutExtension);
        } else if (!orderCorrect) {
            log.error("File {} has incorrect property order", filename);
            filesToCorrect.add(nameWithoutExtension);
        }

        return lineCountCorrect && orderCorrect;
    }

    private List<String> extractProperties(List<String> lines) {
        Pattern pattern = Pattern.compile("\"[a-zA-Z]*\"\\s*:");
        List<String> props = new ArrayList<>();

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String prop = matcher.group().replace("\"", "").replace(":", "").trim();
                props.add(prop);
            }
        }
        return props;
    }

    private int calculateRequiredLines(List<String> props, NonogramFileDetails details) {
        int fixedLines = 4;
        int rowsSeqLines = (int) Math.ceil(details.getHeight() / SEQUENCES_IN_ONE_SECTION_COUNT);
        int colsSeqLines = (int) Math.ceil(details.getWidth() / SEQUENCES_IN_ONE_SECTION_COUNT);
        return fixedLines + props.size() + rowsSeqLines + colsSeqLines;
    }
}
