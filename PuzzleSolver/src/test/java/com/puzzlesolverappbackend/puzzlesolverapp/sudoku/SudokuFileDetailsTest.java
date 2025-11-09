package com.puzzlesolverappbackend.puzzlesolverapp.sudoku;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SudokuFileDetailsTest {

    @Test
    @DisplayName("Should set all fields correctly through AllArgsConstructor")
    void constructor_shouldSetAllFieldsCorrectly() {
        String source = "source";
        double difficulty = 1.5;
        int filled = 45;
        String year = "2025";
        String month = "07";
        List<List<Integer>> board = List.of(List.of(1, 2, 3), List.of(4, 5, 6));
        SudokuFileDetails details = new SudokuFileDetails(
                source,
                difficulty,
                filled,
                year,
                month,
                board
        );

        assertThat(details.getSource()).isEqualTo("source");
        assertThat(details.getDifficulty()).isEqualTo(1.5);
        assertThat(details.getFilled()).isEqualTo(45);
        assertThat(details.getYear()).isEqualTo("2025");
        assertThat(details.getMonth()).isEqualTo("07");
        assertThat(details.getBoard()).isEqualTo(List.of(List.of(1, 2, 3), List.of(4, 5, 6)));
    }

    @Test
    @DisplayName("Should create instance with null or default fields through NoArgsConstructor")
    void noArgsConstructor_shouldCreateInstanceWithNullOrDefaultFields() {
        // given & when
        SudokuFileDetails details = new SudokuFileDetails();

        // then
        assertThat(details).isNotNull();
        assertThat(details.getSource()).isNull();
        assertThat(details.getDifficulty()).isZero();
        assertThat(details.getFilled()).isZero();
        assertThat(details.getYear()).isNull();
        assertThat(details.getMonth()).isNull();
        assertThat(details.getBoard()).isNull();

    }

    @Test
    @DisplayName("Should get values after using setters")
    void settersAndGetters_shouldWorkCorrectly() {
        // given
        SudokuFileDetails details = new SudokuFileDetails();

        String source = "src";
        double difficulty = 2.0;
        int filled = 50;
        String year = "2024";
        String month = "12";
        List<List<Integer>> board = List.of(List.of(7, 8), List.of(9));

        // when
        details.setSource(source);
        details.setDifficulty(difficulty);
        details.setFilled(filled);
        details.setYear(year);
        details.setMonth(month);
        details.setBoard(board);

        // then
        assertThat(details.getSource()).isEqualTo("src");
        assertThat(details.getDifficulty()).isEqualTo(2.0);
        assertThat(details.getFilled()).isEqualTo(50);
        assertThat(details.getYear()).isEqualTo("2024");
        assertThat(details.getMonth()).isEqualTo("12");
        assertThat(details.getBoard()).isEqualTo(board);
    }

    @Test
    @DisplayName("toString() should contain all desired values")
    void toString_shouldContainAllFields() {
        SudokuFileDetails details = new SudokuFileDetails("src",
                1.0,
                30,
                "2023",
                "11",
                List.of(List.of(1, 2)));

        String result = details.toString();

        assertThat(result)
                .contains("source=src")
                .contains("difficulty=1.0")
                .contains("filled=30")
                .contains("year=2023")
                .contains("month=11")
                .contains("board=[[1, 2]]");
    }
}