package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.exclusion;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ExcludedSequenceLogHelperTest {

    @Test
    @DisplayName("ExcludedSequenceLogHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<ExcludedSequenceLogHelper> constructor = ExcludedSequenceLogHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("Generate example log - o10035 column 11")
    void shouldGenerateLogColumnCase() {
        // given
        boolean isRow = false;
        int index = 11;
        int sequenceIndex = 1;
        List<String> line = new ArrayList<>(List.of("-", "O", "X", "O", "O",
                "O", "O", "O", "O", "X",
                "-", "O", "-", "X", "X",
                "O", "O", "O", "O", "X",
                "X", "O", "O", "X", "O",
                "O", "O", "O", "O", "X")
        );
        List<List<Integer>> sequencesRanges = new ArrayList<>(
                List.of(
                        new ArrayList<>(List.of(0, 2)),
                        new ArrayList<>(List.of(3, 8)),
                        new ArrayList<>(List.of(10, 12)),
                        new ArrayList<>(List.of(15, 18)),
                        new ArrayList<>(List.of(21, 22)),
                        new ArrayList<>(List.of(24, 28))
                )
        );
        List<Integer> sequencesLengths = List.of(2, 6, 2, 4, 2, 5);

        // when
        String log = ExcludedSequenceLogHelper.generateLog(isRow,
                index,
                sequenceIndex,
                line,
                sequencesLengths,
                sequencesRanges
        );

        // then
        String expected =
                """
                        EXCLUSION_SEQUENCE_IN_COLUMN: column=11
                        sequenceIndex=1
                        line=[-, O, X, O, O, O, O, O, O, X, -, O, -, X, X, O, O, O, O, X, X, O, O, X, O, O, O, O, O, X]
                        sequencesLengths=[2, 6, 2, 4, 2, 5]
                        sequencesRanges=[[0, 2], [3, 8], [10, 12], [15, 18], [21, 22], [24, 28]]
                        """;
        assertThat(log).isEqualTo(expected);
    }

    @Test
    @DisplayName("ExcludedSequenceLogHelper - convert example log to test arguments - o10035 column 11")
    void shouldConvertGeneratedLogToTestArgumentsColumnCase() {
        // given
        String generatedLog =
                """
                        EXCLUSION_SEQUENCE_IN_COLUMN: column=11
                        sequenceIndex=1
                        line=[-, O, X, O, O, O, O, O, O, X, -, O, -, X, X, O, O, O, O, X, X, O, O, X, O, O, O, O, O, X]
                        sequencesLengths=[2, 6, 2, 4, 2, 5]
                        sequencesRanges=[[0, 2], [3, 8], [10, 12], [15, 18], [21, 22], [24, 28]]
                        """;

        // when
        String convertedLog = ExcludedSequenceLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro10035"
        );

        // then
        String expected = """
                Arguments.of("o10035 / column=11 - excluding sequence",
                    1,
                    List.of("-", "O", "X", "O", "O", "O", "O", "O", "O", "X", "-", "O", "-", "X", "X", "O", "O", "O", "O", "X", "X", "O", "O", "X", "O", "O", "O", "O", "O", "X"),
                    List.of(2, 6, 2, 4, 2, 5),
                    List.of(List.of(0, 2), List.of(3, 8), List.of(10, 12), List.of(15, 18), List.of(21, 22), List.of(24, 28)))
                )""";
        AssertionsForClassTypes.assertThat(convertedLog).isEqualTo(expected);
    }

    @Test
    @DisplayName("Generate example log - o10035 row 14")
    void shouldGenerateLogRowCase() {
        // given
        boolean isRow = true;
        int index = 14;
        int sequenceIndex = 1;
        List<String> line = new ArrayList<>(List.of("-", "-", "-", "O", "O",
                "O", "X", "-", "-", "O",
                "-", "-", "O", "O", "O",
                "O", "O", "O", "O", "O",
                "O", "O", "O", "O", "O",
                "-", "-", "-", "-", "-")
        );
        List<List<Integer>> sequencesRanges = new ArrayList<>(
                List.of(
                        new ArrayList<>(List.of(0, 3)),
                        new ArrayList<>(List.of(3, 5)),
                        new ArrayList<>(List.of(7, 9)),
                        new ArrayList<>(List.of(9, 12)),
                        new ArrayList<>(List.of(12, 26)),
                        new ArrayList<>(List.of(26, 29))
                )
        );
        List<Integer> sequencesLengths = List.of(2, 3, 1, 2, 13, 2);

        // when
        String log = ExcludedSequenceLogHelper.generateLog(isRow,
                index,
                sequenceIndex,
                line,
                sequencesLengths,
                sequencesRanges
        );

        // then
        String expected =
                """
                        EXCLUSION_SEQUENCE_IN_ROW: row=14
                        sequenceIndex=1
                        line=[-, -, -, O, O, O, X, -, -, O, -, -, O, O, O, O, O, O, O, O, O, O, O, O, O, -, -, -, -, -]
                        sequencesLengths=[2, 3, 1, 2, 13, 2]
                        sequencesRanges=[[0, 3], [3, 5], [7, 9], [9, 12], [12, 26], [26, 29]]
                        """;
        assertThat(log).isEqualTo(expected);
    }

    @Test
    @DisplayName("ExcludedSequenceLogHelper - convert example log to test arguments - o10035 row 14")
    void shouldConvertGeneratedLogToTestArgumentsRowCase() {
        // given
        String generatedLog =
                """
                        EXCLUSION_SEQUENCE_IN_ROW: row=14
                        sequenceIndex=1
                        line=[-, -, -, O, O, O, X, -, -, O, -, -, O, O, O, O, O, O, O, O, O, O, O, O, O, -, -, -, -, -]
                        sequencesLengths=[2, 3, 1, 2, 13, 2]
                        sequencesRanges=[[0, 3], [3, 5], [7, 9], [9, 12], [12, 26], [26, 29]]
                        """;

        // when
        String convertedLog = ExcludedSequenceLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro10035"
        );

        // then
        String expected = """
                Arguments.of("o10035 / row=14 - excluding sequence",
                    1,
                    List.of("-", "-", "-", "O", "O", "O", "X", "-", "-", "O", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-"),
                    List.of(2, 3, 1, 2, 13, 2),
                    List.of(List.of(0, 3), List.of(3, 5), List.of(7, 9), List.of(9, 12), List.of(12, 26), List.of(26, 29)))
                )""";
        AssertionsForClassTypes.assertThat(convertedLog).isEqualTo(expected);
    }
}