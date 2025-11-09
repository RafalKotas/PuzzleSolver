package com.puzzlesolverappbackend.puzzlesolverapp.akari;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AkariFileDetailsTest {

    @Test
    @DisplayName("Should set all fields correctly through AllArgsConstructor")
    void allArgsConstructor_shouldSetAllFieldsCorrectly() {
        // given
        List<List<String>> board = List.of(List.of("0", "1"), List.of("1", "0"));
        String source = "source";
        String year = "2024";
        String month = "07";
        int height = 2;
        int width = 2;
        double difficulty = 3.0;

        // when
        AkariFileDetails details = new AkariFileDetails(
                board,
                source,
                year,
                month,
                height,
                width,
                difficulty
        );

        // then
        assertThat(details.getBoard()).isEqualTo(List.of(List.of("0", "1"), List.of("1", "0")));
        assertThat(details.getSource()).isEqualTo("source");
        assertThat(details.getYear()).isEqualTo("2024");
        assertThat(details.getMonth()).isEqualTo("07");
        assertThat(details.getHeight()).isEqualTo(2);
        assertThat(details.getWidth()).isEqualTo(2);
        assertThat(details.getDifficulty()).isEqualTo(3.0);
    }

    @Test
    @DisplayName("Should create instance with null or default fields through NoArgsConstructor")
    void noArgsConstructor_shouldCreateInstanceWithNullOrDefaultFields() {
        // given & when
        AkariFileDetails details = new AkariFileDetails();

        // then
        assertThat(details).isNotNull();
        assertThat(details.getBoard()).isNull();
        assertThat(details.getSource()).isNull();
        assertThat(details.getYear()).isNull();
        assertThat(details.getMonth()).isNull();
        assertThat(details.getHeight()).isZero();
        assertThat(details.getWidth()).isZero();
        assertThat(details.getDifficulty()).isZero();
    }

    @Test
    @DisplayName("Should get values after using setters")
    void settersAndGetters_shouldWorkCorrectly() {
        // given
        AkariFileDetails details = new AkariFileDetails();

        List<List<String>> board = List.of(List.of("X"));
        String source = "src";
        String year = "2023";
        String month = "05";
        int height = 1;
        int width = 1;
        double difficulty = 1.5;

        // when
        details.setBoard(board);
        details.setSource(source);
        details.setYear(year);
        details.setMonth(month);
        details.setHeight(height);
        details.setWidth(width);
        details.setDifficulty(difficulty);

        // then
        assertThat(details.getBoard()).isEqualTo(List.of(List.of("X")));
        assertThat(details.getSource()).isEqualTo("src");
        assertThat(details.getYear()).isEqualTo("2023");
        assertThat(details.getMonth()).isEqualTo("05");
        assertThat(details.getHeight()).isEqualTo(1);
        assertThat(details.getWidth()).isEqualTo(1);
        assertThat(details.getDifficulty()).isEqualTo(1.5);
    }

    @Test
    @DisplayName("toString() should contain all desired values")
    void toString_shouldIncludeAllFields() {
        // given
        AkariFileDetails details = new AkariFileDetails(
                List.of(List.of("1", "0")),
                "src",
                "2023",
                "05",
                2,
                2,
                4.0
        );

        // when
        String result = details.toString();

        // then
        assertThat(result)
                .contains("board=[[1, 0]]")
                .contains("source=src")
                .contains("year=2023")
                .contains("month=05")
                .contains("height=2")
                .contains("width=2")
                .contains("difficulty=4.0");
    }
}