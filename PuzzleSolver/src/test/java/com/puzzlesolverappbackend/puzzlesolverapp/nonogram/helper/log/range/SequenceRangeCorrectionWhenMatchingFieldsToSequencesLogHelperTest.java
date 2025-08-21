package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SequenceRangeCorrectionWhenMatchingFieldsToSequencesLogHelperTest {

    @Test
    @DisplayName("SequenceRangeCorrectionByMatchingLogHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<SequenceRangeCorrectionWhenMatchingFieldsToSequencesLogHelper> constructor = SequenceRangeCorrectionWhenMatchingFieldsToSequencesLogHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("Generate example log - o08007 column 7")
    void shouldGenerateLogColumnCase() {
        // given
        boolean isRow = false;
        int index = 7;
        List<Integer> sequencesLengths = List.of(1, 3, 4);
        List<String> line = new ArrayList<>(List.of("-", "-", "-", "-", "-",
                "-", "-", "-", "-", "O",
                "O", "-", "-", "-", "-")
        );
        List<List<Integer>> initialRanges = List.of(
                List.of(0, 5), List.of(2, 9), List.of(7, 14)
        );
        List<List<Integer>> updatedRanges = List.of(
                List.of(0, 5), List.of(2, 9), List.of(7, 12)
        );

        // when
        String actual = SequenceRangeCorrectionWhenMatchingFieldsToSequencesLogHelper.generateLog(
                isRow,
                index,
                sequencesLengths,
                line,
                initialRanges,
                updatedRanges
        );

        // then
        String expected =
                "CORRECTING_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_COLUMN: column=7\n" +
                        "sequencesLengths=[1, 3, 4]\n" +
                        "line=[-, -, -, -, -, -, -, -, -, O, O, -, -, -, -]\n" +
                        "initialRanges=[[0, 5], [2, 9], [7, 14]]\n" +
                        "updatedRanges=[[0, 5], [2, 9], [7, 12]]\n";

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("SequenceRangeCorrectionWhenMatchingFieldsToSequencesLogHelper - convert example log to test arguments - o08007 column 7")
    void shouldConvertGeneratedLogToTestArgumentsColumnCase() {
        // given
        String generatedLog =
                "CORRECTING_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_COLUMN: column=7\n" +
                        "sequencesLengths=[1, 3, 4]\n" +
                        "line=[-, -, -, -, -, -, -, -, -, O, O, -, -, -, -]\n" +
                        "initialRanges=[[0, 5], [2, 9], [7, 14]]\n" +
                        "updatedRanges=[[0, 5], [2, 9], [7, 12]]\n";

        // when
        String convertedLog = SequenceRangeCorrectionWhenMatchingFieldsToSequencesLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro08007"
        );

        // then
        String expected = """
                Arguments.of("o08007 / column=7 - sequences range correction when matching fields to sequences",
                    List.of(1, 3, 4),
                    new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-")),
                    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 5)), new ArrayList<>(List.of(2, 9)), new ArrayList<>(List.of(7, 14)))),
                    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 5)), new ArrayList<>(List.of(2, 9)), new ArrayList<>(List.of(7, 12))))
                )""";
        AssertionsForClassTypes.assertThat(convertedLog).isEqualTo(expected);
    }

    @Test
    @DisplayName("Generate example log - o08007 row 1")
    void shouldGenerateLogRowCase() {
        // given
        boolean isRow = true;
        int index = 1;
        List<Integer> sequencesLengths = List.of(2, 2, 2);
        List<String> line = new ArrayList<>(List.of("-", "-", "O", "O", "-",
                "-", "O", "-", "X", "-",
                "-", "-", "-", "-", "-")
        );
        List<List<Integer>> initialRanges = List.of(
                List.of(0, 4), List.of(3, 7), List.of(6, 14)
        );
        List<List<Integer>> updatedRanges = List.of(
                List.of(2, 3), List.of(3, 7), List.of(6, 14)
        );

        // when
        String actual = SequenceRangeCorrectionWhenMatchingFieldsToSequencesLogHelper.generateLog(
                isRow,
                index,
                sequencesLengths,
                line,
                initialRanges,
                updatedRanges
        );

        // then
        String expected =
                "CORRECTING_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_ROW: row=1\n" +
                        "sequencesLengths=[2, 2, 2]\n" +
                        "line=[-, -, O, O, -, -, O, -, X, -, -, -, -, -, -]\n" +
                        "initialRanges=[[0, 4], [3, 7], [6, 14]]\n" +
                        "updatedRanges=[[2, 3], [3, 7], [6, 14]]\n";

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("SequenceRangeCorrectionWhenMatchingFieldsToSequencesLogHelper - convert example log to test arguments - o08007 row 1")
    void shouldConvertGeneratedLogToTestArgumentsRowCase() {
        // given
        String generatedLog =
                "CORRECTING_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_ROW: row=1\n" +
                        "sequencesLengths=[2, 2, 2]\n" +
                        "line=[-, -, O, O, -, -, O, -, X, -, -, -, -, -, -]\n" +
                        "initialRanges=[[0, 4], [3, 7], [6, 14]]\n" +
                        "updatedRanges=[[2, 3], [3, 7], [6, 14]]\n";

        // when
        String convertedLog = SequenceRangeCorrectionWhenMatchingFieldsToSequencesLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro08007"
        );

        // then
        String expected = """
                Arguments.of("o08007 / row=1 - sequences range correction when matching fields to sequences",
                    List.of(2, 2, 2),
                    new ArrayList<>(List.of("-", "-", "O", "O", "-", "-", "O", "-", "X", "-", "-", "-", "-", "-", "-")),
                    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 4)), new ArrayList<>(List.of(3, 7)), new ArrayList<>(List.of(6, 14)))),
                    new ArrayList<>(List.of(new ArrayList<>(List.of(2, 3)), new ArrayList<>(List.of(3, 7)), new ArrayList<>(List.of(6, 14))))
                )""";
        AssertionsForClassTypes.assertThat(convertedLog).isEqualTo(expected);
    }
}