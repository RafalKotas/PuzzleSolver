package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SequenceRangeCorrectionWhenMetColouredFieldsLogHelperTest {

    @Test
    @DisplayName("SequenceRangeCorrectionWhenMetColouredFieldsLogHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<SequenceRangeCorrectionWhenMetColouredFieldsLogHelper> constructor = SequenceRangeCorrectionWhenMetColouredFieldsLogHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("Generate example log - o08007 column 6")
    void shouldGenerateLogColumnCase() {
        // given
        boolean isRow = false;
        int index = 6;
        List<String> line = new ArrayList<>(List.of("-", "-", "X", "X", "-", "-", "-", "X", "X", "-", "-", "-", "-", "-", "-"));
        List<List<Integer>> initialRanges = List.of(
                List.of(0, 2), List.of(3, 5), List.of(6, 8), List.of(9, 11), List.of(12, 14)
        );
        List<List<Integer>> updatedRanges = List.of(
                List.of(0, 2), List.of(3, 5), List.of(6, 8), List.of(9, 10), List.of(12, 14)
        );
        List<Integer> sequencesLengths = List.of(2, 2, 2, 2, 2);


        // when
        String actual = SequenceRangeCorrectionWhenMetColouredFieldsLogHelper.generateLog(
                isRow,
                index,
                line,
                sequencesLengths,
                initialRanges,
                updatedRanges
        );

        // then
        String expected =
                "SEQUENCE_CORRECTION_WHEN_MET_COLOURED_FIELDS_IN_COLUMN: column=6\n" +
                        "line=[-, -, X, X, -, -, -, X, X, -, -, -, -, -, -]\n" +
                        "sequencesLengths=[2, 2, 2, 2, 2]\n" +
                        "initialRanges=[[0, 2], [3, 5], [6, 8], [9, 11], [12, 14]]\n" +
                        "updatedRanges=[[0, 2], [3, 5], [6, 8], [9, 10], [12, 14]]\n";

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Convert example log to test arguments - o08007 column 6")
    void shouldConvertGeneratedLogToTestArgumentsColumnCase() {
        // given
        String generatedLog =
                "SEQUENCE_CORRECTION_WHEN_MET_COLOURED_FIELDS_IN_COLUMN: column=6\n" +
                        "line=[-, -, X, X, -, -, -, X, X, -, -, -, -, -, -]\n" +
                        "sequencesLengths=[2, 2, 2, 2, 2]\n" +
                        "initialRanges=[[0, 2], [3, 5], [6, 8], [9, 11], [12, 14]]\n" +
                        "updatedRanges=[[0, 2], [3, 5], [6, 8], [9, 10], [12, 14]]\n";

        // when
        String convertedLog = SequenceRangeCorrectionWhenMetColouredFieldsLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro08007"
        );

        // then
        String expected = "Arguments.of(\"o08007 / column=6 - sequences range correction when met coloured fields\",\n" +
                "    new ArrayList<>(List.of(\"-\", \"-\", \"X\", \"X\", \"-\", \"-\", \"-\", \"X\", \"X\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\")),\n" +
                "    List.of(2, 2, 2, 2, 2),\n" +
                "    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 2)), new ArrayList<>(List.of(3, 5)), new ArrayList<>(List.of(6, 8)), new ArrayList<>(List.of(9, 11)), new ArrayList<>(List.of(12, 14)))),\n" +
                "    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 2)), new ArrayList<>(List.of(3, 5)), new ArrayList<>(List.of(6, 8)), new ArrayList<>(List.of(9, 10)), new ArrayList<>(List.of(12, 14))))\n" +
                ")";
        assertThat(convertedLog).isEqualTo(expected);
    }

    @Test
    @DisplayName("Generate example log - o08007 row 2")
    void shouldGenerateLogRowCase() {
        // given
        boolean isRow = true;
        int index = 2;
        List<String> line = new ArrayList<>(List.of("-", "-", "O", "-", "-",
                "-", "-", "-", "-", "-",
                "-", "-", "-", "-", "-"));
        List<List<Integer>> initialRanges = List.of(
                List.of(0, 9), List.of(2, 11), List.of(4, 14)
        );
        List<List<Integer>> updatedRanges = List.of(
                List.of(0, 2), List.of(2, 11), List.of(4, 14)
        );
        List<Integer> sequencesLengths = List.of(4, 5, 2);


        // when
        String actual = SequenceRangeCorrectionWhenMetColouredFieldsLogHelper.generateLog(
                isRow,
                index,
                line,
                sequencesLengths,
                initialRanges,
                updatedRanges
        );

        // then
        String expected =
                "SEQUENCE_CORRECTION_WHEN_MET_COLOURED_FIELDS_IN_ROW: row=2\n" +
                        "line=[-, -, O, -, -, -, -, -, -, -, -, -, -, -, -]\n" +
                        "sequencesLengths=[4, 5, 2]\n" +
                        "initialRanges=[[0, 9], [2, 11], [4, 14]]\n" +
                        "updatedRanges=[[0, 2], [2, 11], [4, 14]]\n";

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Convert example log to test arguments - o08007 row 2")
    void shouldConvertGeneratedLogToTestArgumentsRowCase() {
        // given
        String generatedLog =
                "SEQUENCE_CORRECTION_WHEN_MET_COLOURED_FIELDS_IN_ROW: row=2\n" +
                        "line=[-, -, O, -, -, -, -, -, -, -, -, -, -, -, -]\n" +
                        "sequencesLengths=[4, 5, 2]\n" +
                        "initialRanges=[[0, 9], [2, 11], [4, 14]]\n" +
                        "updatedRanges=[[0, 2], [2, 11], [4, 14]]\n";

        // when
        String convertedLog = SequenceRangeCorrectionWhenMetColouredFieldsLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro08007"
        );

        // then
        String expected = "Arguments.of(\"o08007 / row=2 - sequences range correction when met coloured fields\",\n" +
                "    new ArrayList<>(List.of(\"-\", \"-\", \"O\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\")),\n" +
                "    List.of(4, 5, 2),\n" +
                "    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 9)), new ArrayList<>(List.of(2, 11)), new ArrayList<>(List.of(4, 14)))),\n" +
                "    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 2)), new ArrayList<>(List.of(2, 11)), new ArrayList<>(List.of(4, 14))))\n" +
                ")";
        assertThat(convertedLog).isEqualTo(expected);
    }
}