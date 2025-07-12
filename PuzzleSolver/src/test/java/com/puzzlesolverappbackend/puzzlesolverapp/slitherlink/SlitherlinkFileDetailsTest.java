package com.puzzlesolverappbackend.puzzlesolverapp.slitherlink;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SlitherlinkFileDetailsTest {

    @Test
    void allArgsConstructor_shouldSetAllFieldsCorrectly() {
        // given
        String source = "source";
        String year = "2024";
        String month = "07";
        double difficulty = 2.5;
        List<List<Integer>> board = List.of(List.of(1, 2), List.of(3, 4));
        int height = 2;
        int width = 2;

        // when
        SlitherlinkFileDetails details = new SlitherlinkFileDetails(
                source, year, month, difficulty, board, height, width
        );

        // then
        assertThat(details.getSource()).isEqualTo(source);
        assertThat(details.getYear()).isEqualTo(year);
        assertThat(details.getMonth()).isEqualTo(month);
        assertThat(details.getDifficulty()).isEqualTo(difficulty);
        assertThat(details.getBoard()).isEqualTo(board);
        assertThat(details.getHeight()).isEqualTo(height);
        assertThat(details.getWidth()).isEqualTo(width);
    }

    @Test
    void noArgsConstructor_shouldCreateInstanceWithNullFields() {
        // when
        SlitherlinkFileDetails details = new SlitherlinkFileDetails();

        // then
        assertThat(details).isNotNull();
        assertThat(details.getSource()).isNull();
        assertThat(details.getYear()).isNull();
        assertThat(details.getMonth()).isNull();
        assertThat(details.getBoard()).isNull();
        assertThat(details.getDifficulty()).isZero();
        assertThat(details.getHeight()).isZero();
        assertThat(details.getWidth()).isZero();
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        // given
        SlitherlinkFileDetails details = new SlitherlinkFileDetails();

        String source = "source";
        String year = "2024";
        String month = "07";
        double difficulty = 3.0;
        List<List<Integer>> board = List.of(List.of(5, 6), List.of(7, 8));
        int height = 2;
        int width = 2;

        // when
        details.setSource(source);
        details.setYear(year);
        details.setMonth(month);
        details.setDifficulty(difficulty);
        details.setBoard(board);
        details.setHeight(height);
        details.setWidth(width);

        // then
        assertThat(details.getSource()).isEqualTo(source);
        assertThat(details.getYear()).isEqualTo(year);
        assertThat(details.getMonth()).isEqualTo(month);
        assertThat(details.getDifficulty()).isEqualTo(difficulty);
        assertThat(details.getBoard()).isEqualTo(board);
        assertThat(details.getHeight()).isEqualTo(height);
        assertThat(details.getWidth()).isEqualTo(width);
    }

    @Test
    void toString_shouldContainAllFieldValues() {
        // given
        SlitherlinkFileDetails details = new SlitherlinkFileDetails(
                "source", "2024", "07", 2.0,
                List.of(List.of(1, 0), List.of(0, 1)), 2, 2
        );

        // when
        String result = details.toString();

        // then
        assertThat(result)
                .contains("source=source")
                .contains("year=2024")
                .contains("month=07")
                .contains("difficulty=2.0")
                .contains("board=[[1, 0], [0, 1]]")
                .contains("height=2")
                .contains("width=2");
    }
}