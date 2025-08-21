package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SequenceRangeCorrectionWhenPlacingXsLogHelperTest {

    @Test
    @DisplayName("SequenceRangeCorrectionWhenPlacingXsLogHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<SequenceRangeCorrectionWhenPlacingXsLogHelper> constructor = SequenceRangeCorrectionWhenPlacingXsLogHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
    }

    @Test
    @DisplayName("Generate example log - o10035 row 5")
    void shouldGenerateLogRowCase() {
        // given
        boolean isRow = true;
        int index = 5;
        int sequenceIndex = 3;
        List<Integer> initialRange = new ArrayList<>(Arrays.asList(14, 25));
        List<Integer> updatedRange = new ArrayList<>(Arrays.asList(15, 22));
        List<Integer> sequencesLengths = List.of(1, 1, 7, 8, 3);
        List<String> line = new ArrayList<>(List.of("-", "-", "-", "-", "-",
                "-", "O", "-", "-", "-",
                "O", "O", "-", "-", "X",
                "O", "O", "O", "O", "0",
                "O", "O", "O", "X", "O",
                "-", "-", "-", "-", "-")
        );

        // when
        String log = SequenceRangeCorrectionWhenPlacingXsLogHelper.generateLog(isRow,
                index,
                sequenceIndex,
                initialRange,
                updatedRange,
                line,
                sequencesLengths
        );

        // then
        String expected =
                """
                        SEQUENCE_CORRECTION_WHEN_PLACING_X_IN_ROW: row=5
                        sequenceIndex=3
                        initialRange=[14, 25]
                        updatedRange=[15, 22]
                        line=[-, -, -, -, -, -, O, -, -, -, O, O, -, -, X, O, O, O, O, 0, O, O, O, X, O, -, -, -, -, -]
                        sequencesLengths=[1, 1, 7, 8, 3]
                        """;
        assertThat(log).isEqualTo(expected);
    }

    @Test
    @DisplayName("SequenceRangeCorrectionWhenPlacingXsLogHelper - convert example log to test arguments - o10035 row 5")
    void shouldConvertGeneratedLogToTestArgumentsRowCase() {
        // given
        String generatedLog =
                """
                        SEQUENCE_CORRECTION_WHEN_PLACING_X_IN_ROW: row=5
                        sequenceIndex=3
                        initialRange=[14, 25]
                        updatedRange=[15, 22]
                        line=[-, -, -, -, -, -, O, -, -, -, O, O, -, -, X, O, O, O, O, 0, O, O, O, X, O, -, -, -, -, -]
                        sequencesLengths=[1, 1, 7, 8, 3]
                        """;

        // when
        String convertedLog = SequenceRangeCorrectionWhenPlacingXsLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro10035"
        );

        // then
        String expected = """
                Arguments.of("o10035 / row=5 - correct sequence range when placing X",
                    3,
                    new ArrayList<>(List.of(14, 25)),
                    new ArrayList<>(List.of(15, 22)),
                    new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "O", "O", "-", "-", "X", "O", "O", "O", "O", "0", "O", "O", "O", "X", "O", "-", "-", "-", "-", "-")),
                    List.of(1, 1, 7, 8, 3))
                )""";
        AssertionsForClassTypes.assertThat(convertedLog).isEqualTo(expected);
    }

    @Test
    @DisplayName("Generate example log - o10035 column 11")
    void shouldGenerateLogColumnCase() {
        // given
        boolean isRow = false;
        int index = 11;
        int sequenceIndex = 1;
        List<Integer> initialRange = new ArrayList<>(Arrays.asList(3, 9));
        List<Integer> updatedRange = new ArrayList<>(Arrays.asList(3, 8));
        List<Integer> sequencesLengths = List.of(2, 6, 2, 4, 2, 5);
        List<String> line = new ArrayList<>(List.of("-", "O", "X", "O", "O",
                "O", "O", "O", "O", "X",
                "-", "O", "-", "X", "X",
                "O", "O", "O", "O", "X",
                "X", "O", "O", "X", "O",
                "O", "O", "O", "O", "X")
        );

        // when
        String log = SequenceRangeCorrectionWhenPlacingXsLogHelper.generateLog(isRow,
                index,
                sequenceIndex,
                initialRange,
                updatedRange,
                line,
                sequencesLengths
        );

        // then
        String expected =
                """
                        SEQUENCE_CORRECTION_WHEN_PLACING_X_IN_COLUMN: column=11
                        sequenceIndex=1
                        initialRange=[3, 9]
                        updatedRange=[3, 8]
                        line=[-, O, X, O, O, O, O, O, O, X, -, O, -, X, X, O, O, O, O, X, X, O, O, X, O, O, O, O, O, X]
                        sequencesLengths=[2, 6, 2, 4, 2, 5]
                        """;
        assertThat(log).isEqualTo(expected);
    }

    @Test
    @DisplayName("SequenceRangeCorrectionWhenPlacingXsLogHelper - convert example log to test arguments - o10035 column 11")
    void shouldConvertGeneratedLogToTestArgumentsColumnCase() {
        // given
        String generatedLog =
                """
                        SEQUENCE_CORRECTION_WHEN_PLACING_X_IN_COLUMN: column=11
                        sequenceIndex=1
                        initialRange=[3, 9]
                        updatedRange=[3, 8]
                        line=[-, O, X, O, O, O, O, O, O, X, -, O, -, X, X, O, O, O, O, X, X, O, O, X, O, O, O, O, O, X]
                        sequencesLengths=[2, 6, 2, 4, 2, 5]
                        """;

        // when
        String convertedLog = SequenceRangeCorrectionWhenPlacingXsLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro10035"
        );

        // then
        String expected = """
                Arguments.of("o10035 / column=11 - correct sequence range when placing X",
                    1,
                    new ArrayList<>(List.of(3, 9)),
                    new ArrayList<>(List.of(3, 8)),
                    new ArrayList<>(List.of("-", "O", "X", "O", "O", "O", "O", "O", "O", "X", "-", "O", "-", "X", "X", "O", "O", "O", "O", "X", "X", "O", "O", "X", "O", "O", "O", "O", "O", "X")),
                    List.of(2, 6, 2, 4, 2, 5))
                )""";
        AssertionsForClassTypes.assertThat(convertedLog).isEqualTo(expected);
    }
}
