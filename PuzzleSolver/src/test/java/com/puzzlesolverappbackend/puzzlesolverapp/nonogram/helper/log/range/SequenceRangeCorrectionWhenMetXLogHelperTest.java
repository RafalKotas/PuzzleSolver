package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SequenceRangeCorrectionWhenMetXLogHelperTest {

    @Test
    @DisplayName("Constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<SequenceRangeCorrectionWhenMetXLogHelper> constructor = SequenceRangeCorrectionWhenMetXLogHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("Generate example log - o08007 column 1")
    void shouldGenerateLogColumnCase() {
        // given
        boolean isRow = false;
        int index = 1;
        List<String> line = new ArrayList<>(List.of("-", "-", "X", "X", "-", "-", "-", "X", "X", "-", "-", "-", "-", "-", "-"));
        List<List<Integer>> initialRanges = List.of(
                List.of(0, 11), List.of(3, 14)
        );
        List<List<Integer>> updatedRanges = List.of(
                List.of(0, 11), List.of(4, 14)
        );
        List<Integer> sequencesLengths = List.of(2, 2);
        List<Integer> excludedSequencesIds = List.of();


        // when
        String actual = SequenceRangeCorrectionWhenMetXLogHelper.generateLog(
                isRow,
                index,
                line,
                initialRanges,
                updatedRanges,
                sequencesLengths,
                excludedSequencesIds
        );

        // then
        String expected =
                "SEQUENCES_RANGES_CORRECTION_IF_X_ON_WAY_IN_COLUMN: column=1\n" +
                        "line=[-, -, X, X, -, -, -, X, X, -, -, -, -, -, -]\n" +
                        "initialRanges=[[0, 11], [3, 14]]\n" +
                        "updatedRanges=[[0, 11], [4, 14]]\n" +
                        "sequencesLengths=[2, 2]\n" +
                        "excludedSequencesIndexes=[]\n";

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Convert example log to test arguments - o08007 column 1")
    void shouldConvertGeneratedLogToTestArgumentsColumnCase() {
        // given
        String generatedLog =
                "SEQUENCES_RANGES_CORRECTION_IF_X_ON_WAY_IN_COLUMN: column=1\n" +
                        "line=[-, -, X, X, -, -, -, X, X, -, -, -, -, -, -]\n" +
                        "initialRanges=[[0, 11], [3, 14]]\n" +
                        "updatedRanges=[[0, 11], [4, 14]]\n" +
                        "sequencesLengths=[2, 2]\n" +
                        "excludedSequencesIndexes=[]\n";

        // when
        String convertedLog = SequenceRangeCorrectionWhenMetXLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro08007"
        );

        // then
        String expected = """
                Arguments.of("o08007 / column=1 - sequences range correction if X on way",
                    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 11)), new ArrayList<>(List.of(3, 14)))),
                    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 11)), new ArrayList<>(List.of(4, 14)))),
                    List.of(2, 2),
                    new ArrayList<>(List.of()))
                )""";
        assertThat(convertedLog).isEqualTo(expected);
    }

    @Test
    @DisplayName("Generate example log - o08007 - row 13")
    void shouldGenerateLogRowCase() {
        // given
        boolean isRow = true;
        int index = 13;
        List<String> line = new ArrayList<>(List.of("-", "-", "-", "-", "X", "O", "O", "X", "X", "-", "-", "-", "-", "-", "-"));
        List<List<Integer>> initialRanges = List.of(
                List.of(0, 7), List.of(5, 14)
        );
        List<List<Integer>> updatedRanges = List.of(
                List.of(0, 6), List.of(5, 14)
        );
        List<Integer> sequencesLengths = List.of(2, 2);
        List<Integer> excludedSequencesIds = List.of();


        // when
        String actual = SequenceRangeCorrectionWhenMetXLogHelper.generateLog(
                isRow,
                index,
                line,
                initialRanges,
                updatedRanges,
                sequencesLengths,
                excludedSequencesIds
        );

        // then
        String expected =
                "SEQUENCES_RANGES_CORRECTION_IF_X_ON_WAY_IN_ROW: row=13\n" +
                        "line=[-, -, -, -, X, O, O, X, X, -, -, -, -, -, -]\n" +
                        "initialRanges=[[0, 7], [5, 14]]\n" +
                        "updatedRanges=[[0, 6], [5, 14]]\n" +
                        "sequencesLengths=[2, 2]\n" +
                        "excludedSequencesIndexes=[]\n";

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Convert example log to test arguments - o08007 row 13")
    void shouldConvertGeneratedLogToTestArgumentsRowCase() {
        // given
        String generatedLog =
                "SEQUENCES_RANGES_CORRECTION_IF_X_ON_WAY_IN_ROW: row=13\n" +
                        "line=[-, -, -, -, X, O, O, X, X, -, -, -, -, -, -]\n" +
                        "initialRanges=[[0, 7], [5, 14]]\n" +
                        "updatedRanges=[[0, 6], [5, 14]]\n" +
                        "sequencesLengths=[2, 2]\n" +
                        "excludedSequencesIndexes=[]\n";
        // when
        String convertedLog = SequenceRangeCorrectionWhenMetXLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro08007"
        );

        // then
        String expected = """
                Arguments.of("o08007 / row=13 - sequences range correction if X on way",
                    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 7)), new ArrayList<>(List.of(5, 14)))),
                    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 6)), new ArrayList<>(List.of(5, 14)))),
                    List.of(2, 2),
                    new ArrayList<>(List.of()))
                )""";
        assertThat(convertedLog).isEqualTo(expected);
    }
}