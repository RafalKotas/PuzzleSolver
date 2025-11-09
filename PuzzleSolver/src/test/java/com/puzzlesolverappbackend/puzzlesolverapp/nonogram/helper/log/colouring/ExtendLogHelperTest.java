package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ExtendLogHelperTest {

    @Test
    @DisplayName("ExtendLogHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<ExtendLogHelper> constructor = ExtendLogHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("ExtendLogHelper - generate example log - o08007 column 3")
    void shouldGenerateLogColumnCase() {
        // given
        int index = 3;
        String direction = "toBottom";
        boolean isRow = false;
        List<String> initialLine = new ArrayList<>(List.of("-", "-", "X", "-", "-", "-", "-", "-", "X", "O", "-", "-", "-", "-", "-"));
        List<List<Integer>> sequencesRanges = new ArrayList<>(
                Arrays.asList(
                        new ArrayList<>(List.of(0, 5)),
                        new ArrayList<>(List.of(3, 7)),
                        new ArrayList<>(List.of(9, 10)),
                        new ArrayList<>(List.of(10, 14))
                )
        );
        List<Integer> sequencesLengths = List.of(2, 3, 2, 1);
        List<String> updatedLine = new ArrayList<>(List.of("-", "-", "X", "-", "-", "-", "-", "-", "X", "O", "O", "-", "-", "-", "-"));
        ColouringGenerateLogBaseContext colouringGenerateLogBaseContext = new ColouringGenerateLogBaseContext(
                isRow,
                index,
                initialLine,
                updatedLine,
                sequencesRanges,
                sequencesLengths
        );

        // when
        String actual = ExtendLogHelper.generateLog(
                colouringGenerateLogBaseContext,
                direction
        );

        // then
        String expected =
                "EXTEND_COLUMN_SEQUENCE: column=3\n" +
                        "direction=toBottom\n" +
                        "initialLine=[-, -, X, -, -, -, -, -, X, O, -, -, -, -, -]\n" +
                        "updatedLine=[-, -, X, -, -, -, -, -, X, O, O, -, -, -, -]\n" +
                        "sequencesRanges=[[0, 5], [3, 7], [9, 10], [10, 14]]\n" +
                        "sequencesLengths=[2, 3, 2, 1]\n";

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("ExtendLogHelper - convert example log to test arguments - o08007 column 3")
    void shouldConvertGeneratedLogToTestArgumentsColumnCase() {
        // given
        String generatedLog =
                "EXTEND_COLUMN_SEQUENCE: column=3\n" +
                        "direction=toBottom\n" +
                        "initialLine=[-, -, X, -, -, -, -, -, X, O, -, -, -, -, -]\n" +
                        "updatedLine=[-, -, X, -, -, -, -, -, X, O, O, -, -, -, -]\n" +
                        "sequencesRanges=[[0, 5], [3, 7], [9, 10], [10, 14]]\n" +
                        "sequencesLengths=[2, 3, 2, 1]\n";

        // when
        String convertedLog = ExtendLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro08007"
        );

        // then
        String expected = "Arguments.of(\"o08007 / column=3 - extending coloured fields near X\",\n" +
                "    new ArrayList<>(List.of(\"-\", \"-\", \"X\", \"-\", \"-\", \"-\", \"-\", \"-\", \"X\", \"O\", \"-\", \"-\", \"-\", \"-\", \"-\")),\n" +
                "    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 5)), new ArrayList<>(List.of(3, 7)), new ArrayList<>(List.of(9, 10)), new ArrayList<>(List.of(10, 14)))),\n" +
                "    List.of(2, 3, 2, 1),\n" +
                "    new ArrayList<>(List.of(\"-\", \"-\", \"X\", \"-\", \"-\", \"-\", \"-\", \"-\", \"X\", \"O\", \"O\", \"-\", \"-\", \"-\", \"-\"))\n" +
                ")";
        assertThat(convertedLog).isEqualTo(expected);
    }

    @Test
    @DisplayName("ExtendLogHelper - generate example log - o07942 row 12")
    void shouldGenerateLogRowCase() {
        // given
        boolean isRow = true;
        int index = 12;
        String direction = "toLeft";
        List<String> initialLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "X", "O", "X", "O", "X"));
        List<List<Integer>> sequencesRanges = new ArrayList<>(
                Arrays.asList(
                        new ArrayList<>(List.of(2, 14)),
                        new ArrayList<>(List.of(16, 16)),
                        new ArrayList<>(List.of(18, 18))
                )
        );
        List<Integer> sequencesLengths = List.of(11, 1, 1);
        List<String> updatedLine = new ArrayList<>(List.of("-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "X", "O", "X", "O", "X"));
        ColouringGenerateLogBaseContext colouringGenerateLogBaseContext = new ColouringGenerateLogBaseContext(
                isRow,
                index,
                initialLine,
                updatedLine,
                sequencesRanges,
                sequencesLengths
        );

        // when
        String actual = ExtendLogHelper.generateLog(
                colouringGenerateLogBaseContext,
                direction
        );

        // then
        String expected =
                "EXTEND_ROW_SEQUENCE: row=12\n" +
                        "direction=toLeft\n" +
                        "initialLine=[-, -, -, -, -, O, O, O, O, O, O, O, O, -, -, X, O, X, O, X]\n" +
                        "updatedLine=[-, -, -, -, O, O, O, O, O, O, O, O, O, -, -, X, O, X, O, X]\n" +
                        "sequencesRanges=[[2, 14], [16, 16], [18, 18]]\n" +
                        "sequencesLengths=[11, 1, 1]\n";

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("ExtendLogHelper - convert example log to test arguments - o07942 row 12")
    void shouldConvertGeneratedLogToTestArgumentsRowCase() {
        // given
        String generatedLog =
                "EXTEND_ROW_SEQUENCE: row=12\n" +
                        "direction=toLeft\n" +
                        "initialLine=[-, -, -, -, -, O, O, O, O, O, O, O, O, -, -, X, O, X, O, X]\n" +
                        "sequencesRanges=[[2, 14], [16, 16], [18, 18]]\n" +
                        "sequencesLengths=[11, 1, 1]\n" +
                        "updatedLine=[-, -, -, -, O, O, O, O, O, O, O, O, O, -, -, X, O, X, O, X]\n";

        // when
        String convertedLog = ExtendLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro07942"
        );

        // then
        String expected = "Arguments.of(\"o07942 / row=12 - extending coloured fields near X\",\n" +
                "    new ArrayList<>(List.of(\"-\", \"-\", \"-\", \"-\", \"-\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"-\", \"-\", \"X\", \"O\", \"X\", \"O\", \"X\")),\n" +
                "    new ArrayList<>(List.of(new ArrayList<>(List.of(2, 14)), new ArrayList<>(List.of(16, 16)), new ArrayList<>(List.of(18, 18)))),\n" +
                "    List.of(11, 1, 1),\n" +
                "    new ArrayList<>(List.of(\"-\", \"-\", \"-\", \"-\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"-\", \"-\", \"X\", \"O\", \"X\", \"O\", \"X\"))\n" +
                ")";
        assertThat(convertedLog).isEqualTo(expected);
    }
}