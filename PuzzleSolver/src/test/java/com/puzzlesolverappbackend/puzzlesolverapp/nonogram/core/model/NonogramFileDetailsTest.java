package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NonogramFileDetailsTest {

    @Test
    @DisplayName("Should set all fields correctly through AllArgsConstructor")
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
                rowSeq,
                colSeq,
                filename,
                height,
                width,
                source,
                year,
                month,
                difficulty,
                additionalContent
        );

        // then
        assertThat(details.getRowSequences()).isEqualTo(List.of(List.of(1, 2), List.of(3)));
        assertThat(details.getColumnSequences()).isEqualTo(List.of(List.of(1), List.of(2, 1)));
        assertThat(details.getFilename()).isEqualTo("nonogram.txt");
        assertThat(details.getHeight()).isEqualTo(5);
        assertThat(details.getWidth()).isEqualTo(5);
        assertThat(details.getSource()).isEqualTo("source");
        assertThat(details.getYear()).isEqualTo("2025");
        assertThat(details.getMonth()).isEqualTo("07");
        assertThat(details.getDifficulty()).isEqualTo(2.0);
        assertThat(details.getAdditionalContent()).isEqualTo("bonus");
    }

    @Test
    @DisplayName("Should create instance with null or default fields through NoArgsConstructor")
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
    @DisplayName("Should get values after using setters")
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
        assertThat(details.getRowSequences()).isEqualTo(List.of(List.of(1)));
        assertThat(details.getColumnSequences()).isEqualTo(List.of(List.of(2)));
        assertThat(details.getFilename()).isEqualTo( "f.txt");
        assertThat(details.getHeight()).isEqualTo(3);
        assertThat(details.getWidth()).isEqualTo(3);
        assertThat(details.getSource()).isEqualTo("src");
        assertThat(details.getYear()).isEqualTo("2022");
        assertThat(details.getMonth()).isEqualTo("06");
        assertThat(details.getDifficulty()).isEqualTo(3.5);
        assertThat(details.getAdditionalContent()).isEqualTo("extra");
    }

    @Test
    @DisplayName("toString() should contain all desired values")
    void toString_shouldIncludeAllFields() {
        // given
        NonogramFileDetails details = new NonogramFileDetails(
                List.of(List.of(1, 2)),
                List.of(List.of(3, 4)),
                "file.txt",
                5,
                5,
                "source",
                "2024",
                "07",
                4.5,
                "extraData"
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