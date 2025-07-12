package com.puzzlesolverappbackend.puzzlesolverapp.sudoku;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SudokuFileDetailsTest {

    @Test
    void constructor_shouldSetAllFieldsCorrectly() {
        List<List<Integer>> board = List.of(List.of(1, 2, 3), List.of(4, 5, 6));
        SudokuFileDetails details = new SudokuFileDetails("source", 1.5, 45, "2025", "07", board);

        assertThat(details.getSource()).isEqualTo("source");
        assertThat(details.getDifficulty()).isEqualTo(1.5);
        assertThat(details.getFilled()).isEqualTo(45);
        assertThat(details.getYear()).isEqualTo("2025");
        assertThat(details.getMonth()).isEqualTo("07");
        assertThat(details.getBoard()).isEqualTo(board);
    }

    @Test
    void noArgsConstructor_shouldCreateNonNullInstance() {
        SudokuFileDetails details = new SudokuFileDetails();

        assertThat(details).isNotNull();
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        SudokuFileDetails details = new SudokuFileDetails();
        details.setSource("src");
        details.setDifficulty(2.0);
        details.setFilled(50);
        details.setYear("2024");
        details.setMonth("12");
        List<List<Integer>> board = List.of(List.of(7, 8), List.of(9));
        details.setBoard(board);

        assertThat(details.getSource()).isEqualTo("src");
        assertThat(details.getDifficulty()).isEqualTo(2.0);
        assertThat(details.getFilled()).isEqualTo(50);
        assertThat(details.getYear()).isEqualTo("2024");
        assertThat(details.getMonth()).isEqualTo("12");
        assertThat(details.getBoard()).isEqualTo(board);
    }

    @Test
    void toString_shouldContainAllFields() {
        List<List<Integer>> board = List.of(List.of(1, 2));
        SudokuFileDetails details = new SudokuFileDetails("src", 1.0, 30, "2023", "11", board);

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