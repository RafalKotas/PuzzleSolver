package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramFileDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NonogramRulesTest {

    @Test
    @DisplayName("no-args constructor should create an object with default field values")
    void noArgsConstructor_defaults() {
        // given & when
        NonogramRules rules = new NonogramRules();

        // then
        assertThat(rules.getRowSequencesLengths()).isNull();
        assertThat(rules.getColumnSequencesLengths()).isNull();
        assertThat(rules.getHeight()).isZero();
        assertThat(rules.getWidth()).isZero();
    }

    @Test
    @DisplayName("setters should populate fields and getters should return those values")
    void settersAndGetters_roundtrip() {
        // given
        NonogramRules rules = new NonogramRules();

        List<List<Integer>> rowSeq = List.of(
                List.of(1, 1, 1),
                List.of(1, 1, 1, 1, 1),
                List.of(1, 5, 1),
                List.of(9),
                List.of(9),
                List.of(7, 1),
                List.of(9),
                List.of(10),
                List.of(1, 1),
                List.of(2, 2)
        );

        List<List<Integer>> colSeq = List.of(
                List.of(7),
                List.of(7),
                List.of(8, 1),
                List.of(6),
                List.of(8),
                List.of(6),
                List.of(10),
                List.of(2, 2, 1),
                List.of(7),
                List.of(1)
        );

        // when
        rules.setRowSequencesLengths(rowSeq);
        rules.setColumnSequencesLengths(colSeq);
        rules.setHeight(10);
        rules.setWidth(10);

        // then
        assertThat(rules.getRowSequencesLengths()).isEqualTo(rowSeq);
        assertThat(rules.getColumnSequencesLengths()).isEqualTo(colSeq);
        assertThat(rules.getHeight()).isEqualTo(10);
        assertThat(rules.getWidth()).isEqualTo(10);
    }

    @Test
    @DisplayName("all-args constructor should set all fields")
    void allArgsConstructor_setsAllFields() {
        // given
        List<List<Integer>> rowSeq = List.of(
                List.of(1, 1, 1),
                List.of(1, 1, 1, 1, 1),
                List.of(1, 5, 1),
                List.of(9),
                List.of(9),
                List.of(7, 1),
                List.of(9),
                List.of(10),
                List.of(1, 1),
                List.of(2, 2)
        );

        List<List<Integer>> colSeq = List.of(
                List.of(7),
                List.of(7),
                List.of(8, 1),
                List.of(6),
                List.of(8),
                List.of(6),
                List.of(10),
                List.of(2, 2, 1),
                List.of(7),
                List.of(1)
        );

        NonogramFileDetails file = new NonogramFileDetails();
        file.setRowSequences(rowSeq);
        file.setColumnSequences(colSeq);
        file.setFilename("o06005");
        file.setHeight(10);
        file.setWidth(10);
        file.setSource("logi");
        file.setMonth("11");
        file.setDifficulty(1.0);
        file.setAdditionalContent(null);

        // when
        NonogramRules rules = new NonogramRules(rowSeq, colSeq, 10, 10);

        // then
        assertThat(rules.getRowSequencesLengths()).isEqualTo(rowSeq);
        assertThat(rules.getColumnSequencesLengths()).isEqualTo(colSeq);
        assertThat(rules.getHeight()).isEqualTo(10);
        assertThat(rules.getWidth()).isEqualTo(10);
    }

    @Test
    @DisplayName("mapNonogramFileDetailsToNonogramRules should copy sequences and dimensions from file details (o06005)")
    void mapFromFileDetails_copiesSequencesAndDimensions() {
        // given
        List<List<Integer>> rowSeq = List.of(
                List.of(1, 1, 1),
                List.of(1, 1, 1, 1, 1),
                List.of(1, 5, 1),
                List.of(9),
                List.of(9),
                List.of(7, 1),
                List.of(9),
                List.of(10),
                List.of(1, 1),
                List.of(2, 2)
        );

        List<List<Integer>> colSeq = List.of(
                List.of(7),
                List.of(7),
                List.of(8, 1),
                List.of(6),
                List.of(8),
                List.of(6),
                List.of(10),
                List.of(2, 2, 1),
                List.of(7),
                List.of(1)
        );

        NonogramFileDetails file = new NonogramFileDetails();
        file.setRowSequences(rowSeq);
        file.setColumnSequences(colSeq);
        file.setFilename("o06005");
        file.setHeight(10);
        file.setWidth(10);
        file.setSource("logi");
        file.setMonth("11");
        file.setDifficulty(1.0);
        file.setAdditionalContent(null);

        // when
        NonogramRules rules = NonogramRules.mapNonogramFileDetailsToNonogramRules(file);

        // then
        assertThat(rules.getRowSequencesLengths()).isEqualTo(rowSeq);
        assertThat(rules.getColumnSequencesLengths()).isEqualTo(colSeq);
        assertThat(rules.getHeight()).isEqualTo(10);
        assertThat(rules.getWidth()).isEqualTo(10);
    }
}