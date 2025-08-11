package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class NonogramCorrectnessIndicatorTest {

    static Stream<NonogramCorrectnessIndicator> allValues() {
        return Arrays.stream(NonogramCorrectnessIndicator.values());
    }

    @Test
    @DisplayName("values() should contain all constants in declared order")
    void valuesShouldContainAllInOrder() {
        // given
        List<NonogramCorrectnessIndicator> expected = List.of(
                NonogramCorrectnessIndicator.VALID,
                NonogramCorrectnessIndicator.INVALID_DIMENSIONS_ROWS,
                NonogramCorrectnessIndicator.INVALID_DIMENSIONS_COLUMNS,
                NonogramCorrectnessIndicator.TOO_LONG_ROW_SEQUENCE,
                NonogramCorrectnessIndicator.TOO_LONG_COLUMN_SEQUENCE,
                NonogramCorrectnessIndicator.SUM_MISMATCH_ROWS_COLUMNS
        );

        // when
        NonogramCorrectnessIndicator[] actualValues = NonogramCorrectnessIndicator.values();

        // then
        assertThat(actualValues).containsExactlyElementsOf(expected);
        for (int i = 0; i < expected.size(); i++) {
            assertThat(expected.get(i).ordinal()).isEqualTo(i);
        }
    }

    @ParameterizedTest
    @MethodSource("allValues")
    @DisplayName("valueOf(name) round-trip should return the same enum constant")
    void valueOfRoundTrip(NonogramCorrectnessIndicator indicator) {
        // given
        String name = indicator.name();

        // when
        NonogramCorrectnessIndicator result = NonogramCorrectnessIndicator.valueOf(name);

        // then
        assertThat(result).isSameAs(indicator);
        assertThat(indicator.toString()).isEqualTo(name);
    }
}