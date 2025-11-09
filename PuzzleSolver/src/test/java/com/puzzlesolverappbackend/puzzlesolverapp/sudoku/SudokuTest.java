package com.puzzlesolverappbackend.puzzlesolverapp.sudoku;

import com.puzzlesolverappbackend.puzzlesolverapp.common.vo.Publication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SudokuTest {

    @Test
    @DisplayName("AllArgsConstructor should set fields correctly")
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
        assertThat(sudoku.getFilename()).isEqualTo("sudoku_01.txt");
        assertThat(sudoku.getSource()).isEqualTo("book");
        assertThat(sudoku.getPublication().getYear()).isEqualTo("2023");
        assertThat(sudoku.getPublication().getMonth()).isEqualTo("12");
        assertThat(sudoku.getDifficulty()).isEqualTo(4.5);
        assertThat(sudoku.getFilled()).isEqualTo(28);
    }

    @Test
    @DisplayName("NoArgsConstructor should create empty instance")
    void noArgsConstructor_shouldCreateNonNullInstance() {
        // given & when
        Sudoku sudoku = new Sudoku();

        // then
        assertNotNull(sudoku);
    }

    @Test
    @DisplayName("Should get fields after setting values")
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
        assertThat(sudoku.getFilename()).isEqualTo("grid.txt");
        assertThat(sudoku.getSource()).isEqualTo("web");
        assertThat(sudoku.getPublication().getYear()).isEqualTo("2024");
        assertThat(sudoku.getPublication().getMonth()).isEqualTo("07");
        assertThat(sudoku.getDifficulty()).isEqualTo(3.8);
        assertThat(sudoku.getFilled()).isEqualTo(40);
    }

    @Test
    @DisplayName("toString() should contain all desired values")
    void toString_shouldIncludeAllFields() {
        // given
        Sudoku sudoku = new Sudoku(
                "grid.txt",
                "source",
                "2024",
                "01",
                2.1,
                36
        );

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