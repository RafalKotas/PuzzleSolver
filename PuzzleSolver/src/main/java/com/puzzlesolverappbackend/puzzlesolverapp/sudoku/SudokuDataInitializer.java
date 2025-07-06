package com.puzzlesolverappbackend.puzzlesolverapp.sudoku;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzlesolverapp.common.CommonService;
import com.puzzlesolverappbackend.puzzlesolverapp.constants.InitializerConstants;
import jdk.security.jarsigner.JarSignerException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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
@Order(6)
@Slf4j
public class SudokuDataInitializer implements CommandLineRunner {

    private final SudokuRepository sudokuRepository;

    private final CommonService commonService;

    Sudoku sudoku;

    String sudokuFileNameWithoutExtension;
    String source;
    Double difficulty;
    String year;
    String month;
    Integer filled;

    int sudokusSaved;
    int sudokusRepeated;

    public static final String PUZZLE_PATH = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.SUDOKU_PATH_SUFFIX;

    public SudokuDataInitializer(SudokuRepository sudokuRepository, CommonService commonService) {
        this.sudokuRepository = sudokuRepository;
        this.commonService = commonService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Sudoku init(6)");

        sudokusSaved = 0;
        sudokusRepeated = 0;

        Set<String> existingSudokuFilesNames = commonService
                .listFilesUsingJavaIO(PUZZLE_PATH);

        for (String sudokuFileName : existingSudokuFilesNames) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                SudokuFileDetails sudokuFileDetails = objectMapper.readValue(new File(PUZZLE_PATH + sudokuFileName), SudokuFileDetails.class);

                sudokuFileNameWithoutExtension = sudokuFileName.substring(0, sudokuFileName.length() - 5);
                source = sudokuFileDetails.getSource();

                difficulty = sudokuFileDetails.getDifficulty();

                year = sudokuFileDetails.getYear();
                month = sudokuFileDetails.getMonth();
                if (source.equals("Logi")) {
                    if (month.length() > 2) {
                        source = "logiMix";
                        month = month.substring(0, 2);
                    } else {
                        source = "logi";
                    }
                }
                filled = sudokuFileDetails.getFilled();

                sudoku = new Sudoku(sudokuFileNameWithoutExtension, source, year, month, difficulty, filled);

                if (sudokuRepository.existsSudokuByGivenParamsFromFile(sudokuFileNameWithoutExtension, source, year, month, filled, difficulty).isPresent()) {
                    sudokusRepeated++;
                } else {
                    sudokusSaved++;
                    sudokuRepository.save(sudoku);
                }
            } catch (JarSignerException jsonParseException) {
                log.error("Wrong file part: {}", sudokuFileName);
                log.error("Exception: {}", jsonParseException.getMessage());
            }
        }

        log.info("Saving sudokus to DB part is done.");

        if (InitializerConstants.PRINT_PUZZLE_STATUS_INFO) {
            log.info("Sudoku saved: {}", sudokusSaved);
            log.info("Sudoku repeated: {}", sudokusRepeated);
        }
    }
}
