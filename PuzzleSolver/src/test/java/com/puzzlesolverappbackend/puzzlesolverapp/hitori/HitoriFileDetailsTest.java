package com.puzzlesolverappbackend.puzzlesolverapp.hitori;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HitoriFileDetailsTest {

    @Test
    void allArgsConstructor_shouldSetAllFieldsCorrectly() {
        // given
        String source = "source";
        double difficulty = 2.3;
        int height = 5;
        int width = 5;
        List<List<String>> board = List.of(List.of("1", "2"), List.of("3", "4"));

        // when
        HitoriFileDetails details = new HitoriFileDetails(source, difficulty, height, width, board);

        // then
        assertThat(details.getSource()).isEqualTo(source);
        assertThat(details.getDifficulty()).isEqualTo(difficulty);
        assertThat(details.getHeight()).isEqualTo(height);
        assertThat(details.getWidth()).isEqualTo(width);
        assertThat(details.getBoard()).isEqualTo(board);
    }

    @Test
    void noArgsConstructor_shouldCreateInstanceWithDefaultFields() {
        // when
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
        assertThat(details.getSource()).isEqualTo(source);
        assertThat(details.getDifficulty()).isEqualTo(difficulty);
        assertThat(details.getHeight()).isEqualTo(height);
        assertThat(details.getWidth()).isEqualTo(width);
        assertThat(details.getBoard()).isEqualTo(board);
    }

    @Test
    void toString_shouldContainAllFields() {
        // given
        HitoriFileDetails details = new HitoriFileDetails(
                "src", 3.2, 3, 3, List.of(List.of("X"))
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