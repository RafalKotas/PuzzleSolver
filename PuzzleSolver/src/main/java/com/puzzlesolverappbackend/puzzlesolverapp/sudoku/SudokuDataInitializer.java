package com.puzzlesolverappbackend.puzzlesolverapp.sudoku;

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
@Order(6)
@Getter
@Setter
@Slf4j
public class SudokuDataInitializer implements CommandLineRunner {

    private final SudokuRepository sudokuRepository;
    private final CommonService commonService;

    public SudokuDataInitializer(SudokuRepository sudokuRepository, CommonService commonService) {
        this.sudokuRepository = sudokuRepository;
        this.commonService = commonService;
    }

    protected String puzzlePath = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.SUDOKU_PATH_SUFFIX;

    @Override
    public void run(String... args) {
        log.info("Sudoku init(6)");

        int sudokusSaved = 0;
        int sudokusRepeated = 0;

        Set<String> existingSudokuFilesNames = commonService.listFilesUsingJavaIO(puzzlePath);

        for (String sudokuFileName : existingSudokuFilesNames) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                SudokuFileDetails sudokuFileDetails = objectMapper.readValue(
                        new File(puzzlePath + sudokuFileName), SudokuFileDetails.class);

                String sudokuFileNameWithoutExtension = sudokuFileName.substring(0, sudokuFileName.length() - 5);
                String source = sudokuFileDetails.getSource();
                Double difficulty = sudokuFileDetails.getDifficulty();
                String year = sudokuFileDetails.getYear();
                String month = sudokuFileDetails.getMonth();
                Integer filled = sudokuFileDetails.getFilled();

                Sudoku sudoku = new Sudoku(sudokuFileNameWithoutExtension, source, year, month, difficulty, filled);

                if (sudokuRepository.existsSudokuByGivenParamsFromFile(
                        sudokuFileNameWithoutExtension, source, year, month, filled, difficulty).isPresent()) {
                    sudokusRepeated++;
                } else {
                    sudokuRepository.save(sudoku);
                    sudokusSaved++;
                    log.info("New sudoku saved: {}", sudokuFileName);
                }
            } catch (IOException e) {
                log.error("Wrong file part: {} ", sudokuFileName);
                log.error("Exception: {}", e.getMessage());
            }
        }

        if (InitializerConstants.PRINT_PUZZLE_STATUS_INFO) {
            log.info("Sudoku saved: {}", sudokusSaved);
            log.info("Sudoku repeated: {}", sudokusRepeated);
        }

        log.info("Saving sudokus to DB part is done.");
    }
}
