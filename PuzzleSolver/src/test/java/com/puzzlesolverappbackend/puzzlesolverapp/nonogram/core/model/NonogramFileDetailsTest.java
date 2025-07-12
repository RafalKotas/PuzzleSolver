package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NonogramFileDetailsTest {

    @Test
    void allArgsConstructor_shouldSetAllFieldsCorrectly() {
        // given
        List<List<Integer>> rowSeq = List.of(List.of(1, 2), List.of(3));
        List<List<Integer>> colSeq = List.of(List.of(1), List.of(2, 1));
        String filename = "nonogram.txt";
        int height = 5;
        int width = 5;
        String source = "source";
        String year = "2025";
        String month = "07";
        double difficulty = 2.0;
        String additionalContent = "bonus";

        // when
        NonogramFileDetails details = new NonogramFileDetails(
                rowSeq, colSeq, filename, height, width, source, year, month, difficulty, additionalContent
        );

        // then
        assertThat(details.getRowSequences()).isEqualTo(rowSeq);
        assertThat(details.getColumnSequences()).isEqualTo(colSeq);
        assertThat(details.getFilename()).isEqualTo(filename);
        assertThat(details.getHeight()).isEqualTo(height);
        assertThat(details.getWidth()).isEqualTo(width);
        assertThat(details.getSource()).isEqualTo(source);
        assertThat(details.getYear()).isEqualTo(year);
        assertThat(details.getMonth()).isEqualTo(month);
        assertThat(details.getDifficulty()).isEqualTo(difficulty);
        assertThat(details.getAdditionalContent()).isEqualTo(additionalContent);
    }

    @Test
    void noArgsConstructor_shouldCreateInstanceWithNullOrDefaultFields() {
        // when
        NonogramFileDetails details = new NonogramFileDetails();

        // then
        assertThat(details).isNotNull();
        assertThat(details.getRowSequences()).isNull();
        assertThat(details.getColumnSequences()).isNull();
        assertThat(details.getFilename()).isNull();
        assertThat(details.getHeight()).isZero();
        assertThat(details.getWidth()).isZero();
        assertThat(details.getSource()).isNull();
        assertThat(details.getYear()).isNull();
        assertThat(details.getMonth()).isNull();
        assertThat(details.getDifficulty()).isZero();
        assertThat(details.getAdditionalContent()).isNull();
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        // given
        NonogramFileDetails details = new NonogramFileDetails();
        List<List<Integer>> rowSeq = List.of(List.of(1));
        List<List<Integer>> colSeq = List.of(List.of(2));
        String filename = "f.txt";
        int height = 3;
        int width = 3;
        String source = "src";
        String year = "2022";
        String month = "06";
        double difficulty = 3.5;
        String content = "extra";

        // when
        details.setRowSequences(rowSeq);
        details.setColumnSequences(colSeq);
        details.setFilename(filename);
        details.setHeight(height);
        details.setWidth(width);
        details.setSource(source);
        details.setYear(year);
        details.setMonth(month);
        details.setDifficulty(difficulty);
        details.setAdditionalContent(content);

        // then
        assertThat(details.getRowSequences()).isEqualTo(rowSeq);
        assertThat(details.getColumnSequences()).isEqualTo(colSeq);
        assertThat(details.getFilename()).isEqualTo(filename);
        assertThat(details.getHeight()).isEqualTo(height);
        assertThat(details.getWidth()).isEqualTo(width);
        assertThat(details.getSource()).isEqualTo(source);
        assertThat(details.getYear()).isEqualTo(year);
        assertThat(details.getMonth()).isEqualTo(month);
        assertThat(details.getDifficulty()).isEqualTo(difficulty);
        assertThat(details.getAdditionalContent()).isEqualTo(content);
    }

    @Test
    void toString_shouldIncludeAllFields() {
        // given
        NonogramFileDetails details = new NonogramFileDetails(
                List.of(List.of(1, 2)), List.of(List.of(3, 4)), "file.txt",
                5, 5, "source", "2024", "07", 4.5, "extraData"
        );

        // when
        String result = details.toString();

        // then
        assertThat(result)
                .contains("rowSequences=[[1, 2]]")
                .contains("columnSequences=[[3, 4]]")
                .contains("filename=file.txt")
                .contains("height=5")
                .contains("width=5")
                .contains("source=source")
                .contains("year=2024")
                .contains("month=07")
                .contains("difficulty=4.5")
                .contains("additionalContent=extraData");
    }
}