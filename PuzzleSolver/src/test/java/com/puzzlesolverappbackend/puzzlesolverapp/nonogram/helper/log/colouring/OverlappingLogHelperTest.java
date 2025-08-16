package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OverlappingLogHelperTest {

    @Test
    @DisplayName("OverlappingLogHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<OverlappingLogHelper> constructor = OverlappingLogHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("OverlappingLogHelper - generate example log - o08007 column 2")
    void shouldGenerateLogColumnCase() {
        // given
        boolean isRow = false;
        int index = 2;
        List<String> initialLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"));
        List<List<Integer>> sequencesRanges = new ArrayList<>(
                List.of(
                        new ArrayList<>(List.of(0, 5)),
                        new ArrayList<>(List.of(5, 11)),
                        new ArrayList<>(List.of(11, 14))
                )
        );
        List<Integer> sequencesLengths = List.of(4, 5, 2);
        List<String> updatedLine = new ArrayList<>(List.of("-", "-", "O", "O", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-"));

        // when
        String actual = OverlappingLogHelper.generateLog(
                isRow,
                index,
                initialLine,
                sequencesRanges,
                sequencesLengths,
                updatedLine
        );

        // then
        String expected =
                "COLOUR_OVERLAPPING_FIELDS_IN_COLUMN: column=2\n" +
                        "initialLine=[-, -, -, -, -, -, -, -, -, -, -, -, -, -, -]\n" +
                        "sequencesRanges=[[0, 5], [5, 11], [11, 14]]\n" +
                        "sequencesLengths=[4, 5, 2]\n" +
                        "updatedLine=[-, -, O, O, -, -, -, O, O, O, -, -, -, -, -]\n";
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("OverlappingLogHelper - convert example log to test arguments - o08007 column 2")
    void shouldConvertGeneratedLogToTestArgumentsColumnCase() {
        // given
        String generatedLog =
                "COLOUR_OVERLAPPING_FIELDS_IN_COLUMN: column=2\n" +
                        "initialLine=[-, -, -, -, -, -, -, -, -, -, -, -, -, -, -]\n" +
                        "sequencesRanges=[[0, 5], [5, 11], [11, 14]]\n" +
                        "sequencesLengths=[4, 5, 2]\n" +
                        "updatedLine=[-, -, O, O, -, -, -, O, O, O, -, -, -, -, -]\n";

        // when
        String convertedLog = OverlappingLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro08007"
        );

        // then
        String expected = "Arguments.of(\"o08007 / column=2 - colour overlapping fields\",\n" +
                "    new ArrayList<>(List.of(\"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\")),\n" +
                "    List.of(List.of(0, 5), List.of(5, 11), List.of(11, 14)),\n" +
                "    List.of(4, 5, 2),\n" +
                "    List.of(\"-\", \"-\", \"O\", \"O\", \"-\", \"-\", \"-\", \"O\", \"O\", \"O\", \"-\", \"-\", \"-\", \"-\", \"-\"))\n" +
                ")";
        assertThat(convertedLog).isEqualTo(expected);
    }

    @Test
    @DisplayName("OverlappingLogHelper - generate example log - o08007 row 9")
    void shouldGenerateLogRowCase() {
        // given
        boolean isRow = true;
        int index = 9;
        List<String> initialLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"));
        List<List<Integer>> sequencesRanges = new ArrayList<>(
                List.of(
                        new ArrayList<>(List.of(0, 14))
                )
        );
        List<Integer> sequencesLengths = List.of(9);
        List<String> updatedLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-"));

        // when
        String actual = OverlappingLogHelper.generateLog(
                isRow,
                index,
                initialLine,
                sequencesRanges,
                sequencesLengths,
                updatedLine
        );

        // then
        String expected =
                "COLOUR_OVERLAPPING_FIELDS_IN_ROW: row=9\n" +
                        "initialLine=[-, -, -, -, -, -, -, -, -, -, -, -, -, -, -]\n" +
                        "sequencesRanges=[[0, 14]]\n" +
                        "sequencesLengths=[9]\n" +
                        "updatedLine=[-, -, -, -, -, -, O, O, O, -, -, -, -, -, -]\n";
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("OverlappingLogHelper - convert example log to test arguments - o08007 row 9")
    void shouldConvertGeneratedLogToTestArgumentsRowCase() {
        // given
        String generatedLog =
                "COLOUR_OVERLAPPING_FIELDS_IN_ROW: row=9\n" +
                        "initialLine=[-, -, -, -, -, -, -, -, -, -, -, -, -, -, -]\n" +
                        "sequencesRanges=[[0, 14]]\n" +
                        "sequencesLengths=[9]\n" +
                        "updatedLine=[-, -, -, -, -, -, O, O, O, -, -, -, -, -, -]\n";

        // when
        String convertedLog = OverlappingLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro08007"
        );

        // then
        String expected = "Arguments.of(\"o08007 / row=9 - colour overlapping fields\",\n" +
                "    new ArrayList<>(List.of(\"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\")),\n" +
                "    List.of(List.of(0, 14)),\n" +
                "    List.of(9),\n" +
                "    List.of(\"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"O\", \"O\", \"O\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\"))\n" +
                ")";
        assertThat(convertedLog).isEqualTo(expected);
    }
}