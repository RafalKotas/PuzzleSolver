package com.puzzlesolverappbackend.puzzlesolverapp.hitori;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HitoriFileDetailsTest {

    @Test
    @DisplayName("Should set all fields correctly through AllArgsConstructor")
    void allArgsConstructor_shouldSetAllFieldsCorrectly() {
        // given
        String source = "source";
        double difficulty = 2.3;
        int height = 5;
        int width = 5;
        List<List<String>> board = List.of(List.of("1", "2"), List.of("3", "4"));

        // when
        HitoriFileDetails details = new HitoriFileDetails(
                source,
                difficulty,
                height,
                width,
                board
        );

        // then
        assertThat(details.getSource()).isEqualTo("source");
        assertThat(details.getDifficulty()).isEqualTo(2.3);
        assertThat(details.getHeight()).isEqualTo(5);
        assertThat(details.getWidth()).isEqualTo(5);
        assertThat(details.getBoard()).isEqualTo(List.of(List.of("1", "2"), List.of("3", "4")));
    }

    @Test
    @DisplayName("Should create instance with null or default fields through NoArgsConstructor")
    void noArgsConstructor_shouldCreateInstanceWithDefaultFields() {
        // given & when
        HitoriFileDetails details = new HitoriFileDetails();

        // then
        assertThat(details).isNotNull();
        assertThat(details.getSource()).isNull();
        assertThat(details.getDifficulty()).isZero();
        assertThat(details.getHeight()).isZero();
        assertThat(details.getWidth()).isZero();
        assertThat(details.getBoard()).isNull();
    }

    @Test
    @DisplayName("Should get values after using setters")
    void settersAndGetters_shouldWorkCorrectly() {
        // given
        HitoriFileDetails details = new HitoriFileDetails();

        String source = "test";
        double difficulty = 1.9;
        int height = 4;
        int width = 4;
        List<List<String>> board = List.of(List.of("A", "B"));

        // when
        details.setSource(source);
        details.setDifficulty(difficulty);
        details.setHeight(height);
        details.setWidth(width);
        details.setBoard(board);

        // then
        assertThat(details.getSource()).isEqualTo("test");
        assertThat(details.getDifficulty()).isEqualTo(1.9);
        assertThat(details.getHeight()).isEqualTo(4);
        assertThat(details.getWidth()).isEqualTo(4);
        assertThat(details.getBoard()).isEqualTo(List.of(List.of("A", "B")));
    }

    @Test
    @DisplayName("toString() should contain all desired values")
    void toString_shouldContainAllFields() {
        // given
        HitoriFileDetails details = new HitoriFileDetails(
                "src",
                3.2,
                3,
                3,
                List.of(List.of("X"))
        );

        // when
        String result = details.toString();

        // then
        assertThat(result)
                .contains("source=src")
                .contains("difficulty=3.2")
                .contains("height=3")
                .contains("width=3")
                .contains("board=[[X]]");
    }
}