package com.puzzlesolverappbackend.puzzlesolverapp.architect;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ArchitectFileDetailsTest {

    @Test
    @DisplayName("Should set all fields correctly through allArgsConstructor")
    void allArgsConstructor_shouldSetAllFieldsCorrectly() {
        // given
        List<Integer> tanksInRows = List.of(1, 2);
        List<Integer> tanksInColumns = List.of(3, 4);
        List<List<String>> board = List.of(List.of("A", "B"), List.of("C", "D"));
        int height = 2;
        int width = 2;
        String source = "source";
        String year = "2025";
        String month = "07";
        double difficulty = 2.5;

        // when
        ArchitectFileDetails details = new ArchitectFileDetails(
                tanksInRows, tanksInColumns, board, height, width, source, year, month, difficulty
        );

        // then
        assertThat(details.getTanksInRows()).isEqualTo(List.of(1, 2));
        assertThat(details.getTanksInColumns()).isEqualTo(List.of(3, 4));
        assertThat(details.getBoard()).isEqualTo(List.of(List.of("A", "B"), List.of("C", "D")));
        assertThat(details.getHeight()).isEqualTo(2);
        assertThat(details.getWidth()).isEqualTo(2);
        assertThat(details.getSource()).isEqualTo("source");
        assertThat(details.getYear()).isEqualTo("2025");
        assertThat(details.getMonth()).isEqualTo("07");
        assertThat(details.getDifficulty()).isEqualTo(2.5);
    }

    @Test
    @DisplayName("Should create instance with null or default fields through NoArgsConstructor")
    void noArgsConstructor_shouldCreateInstanceWithNullOrDefaultFields() {
        // given & when
        ArchitectFileDetails details = new ArchitectFileDetails();

        // then
        assertThat(details).isNotNull();
        assertThat(details.getTanksInRows()).isNull();
        assertThat(details.getTanksInColumns()).isNull();
        assertThat(details.getBoard()).isNull();
        assertThat(details.getHeight()).isZero();
        assertThat(details.getWidth()).isZero();
        assertThat(details.getSource()).isNull();
        assertThat(details.getYear()).isNull();
        assertThat(details.getMonth()).isNull();
        assertThat(details.getDifficulty()).isZero();
    }

    @Test
    @DisplayName("Should get values after using setters")
    void settersAndGetters_shouldWorkCorrectly() {
        // given
        ArchitectFileDetails details = new ArchitectFileDetails();

        List<Integer> rows = List.of(5);
        List<Integer> columns = List.of(6);
        List<List<String>> board = List.of(List.of("X"));
        int height = 1;
        int width = 1;
        String source = "src";
        String year = "2024";
        String month = "06";
        double difficulty = 3.0;

        // when
        details.setTanksInRows(rows);
        details.setTanksInColumns(columns);
        details.setBoard(board);
        details.setHeight(height);
        details.setWidth(width);
        details.setSource(source);
        details.setYear(year);
        details.setMonth(month);
        details.setDifficulty(difficulty);

        // then
        assertThat(details.getTanksInRows()).isEqualTo(List.of(5));
        assertThat(details.getTanksInColumns()).isEqualTo(List.of(6));
        assertThat(details.getBoard()).isEqualTo(List.of(List.of("X")));
        assertThat(details.getHeight()).isEqualTo(1);
        assertThat(details.getWidth()).isEqualTo(1);
        assertThat(details.getSource()).isEqualTo("src");
        assertThat(details.getYear()).isEqualTo("2024");
        assertThat(details.getMonth()).isEqualTo("06");
        assertThat(details.getDifficulty()).isEqualTo(3.0);
    }

    @Test
    @DisplayName("toString() should contain all desired values")
    void toString_shouldIncludeAllFields() {
        // given
        ArchitectFileDetails details = new ArchitectFileDetails(
                List.of(1, 2),
                List.of(3, 4),
                List.of(List.of("A")),
                1,
                1,
                "src",
                "2023",
                "05",
                1.7
        );

        // when
        String result = details.toString();

        // then
        assertThat(result)
                .contains("tanksInRows=[1, 2]")
                .contains("tanksInColumns=[3, 4]")
                .contains("board=[[A]]")
                .contains("height=1")
                .contains("width=1")
                .contains("source=src")
                .contains("year=2023")
                .contains("month=05")
                .contains("difficulty=1.7");
    }
}