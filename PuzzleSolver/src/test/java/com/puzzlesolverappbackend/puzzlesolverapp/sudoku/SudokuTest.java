package com.puzzlesolverappbackend.puzzlesolverapp.sudoku;

import com.puzzlesolverappbackend.puzzlesolverapp.common.vo.Publication;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SudokuTest {

    @Test
    void constructor_shouldSetAllFieldsCorrectly() {
        // given
        String filename = "sudoku_01.txt";
        String source = "book";
        String year = "2023";
        String month = "12";
        Double difficulty = 4.5;
        Integer filled = 28;

        // when
        Sudoku sudoku = new Sudoku(filename, source, year, month, difficulty, filled);

        // then
        assertThat(sudoku.getFilename()).isEqualTo(filename);
        assertThat(sudoku.getSource()).isEqualTo(source);
        assertThat(sudoku.getPublication().getYear()).isEqualTo(year);
        assertThat(sudoku.getPublication().getMonth()).isEqualTo(month);
        assertThat(sudoku.getDifficulty()).isEqualTo(difficulty);
        assertThat(sudoku.getFilled()).isEqualTo(filled);
    }

    @Test
    void noArgsConstructor_shouldCreateNonNullInstance() {
        // when
        Sudoku sudoku = new Sudoku();

        // then
        assertThat(sudoku).isNotNull();
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        // given
        Sudoku sudoku = new Sudoku();

        String filename = "grid.txt";
        String source = "web";
        String year = "2024";
        String month = "07";
        Double difficulty = 3.8;
        Integer filled = 40;

        // when
        sudoku.setFilename(filename);
        sudoku.setSource(source);
        sudoku.setPublication(new Publication(year, month));
        sudoku.setDifficulty(difficulty);
        sudoku.setFilled(filled);

        // then
        assertThat(sudoku.getFilename()).isEqualTo(filename);
        assertThat(sudoku.getSource()).isEqualTo(source);
        assertThat(sudoku.getPublication().getYear()).isEqualTo(year);
        assertThat(sudoku.getPublication().getMonth()).isEqualTo(month);
        assertThat(sudoku.getDifficulty()).isEqualTo(difficulty);
        assertThat(sudoku.getFilled()).isEqualTo(filled);
    }

    @Test
    void toString_shouldIncludeAllFields() {
        // given
        Sudoku sudoku = new Sudoku("grid.txt", "source", "2024", "01", 2.1, 36);

        // when
        String result = sudoku.toString();

        // then
        assertThat(result)
                .contains("filename=grid.txt",
                        "source=source",
                        "year=2024",
                        "month=01",
                        "difficulty=2.1")
                .startsWith("Sudoku(super=BasePuzzleEntity(");
    }
}