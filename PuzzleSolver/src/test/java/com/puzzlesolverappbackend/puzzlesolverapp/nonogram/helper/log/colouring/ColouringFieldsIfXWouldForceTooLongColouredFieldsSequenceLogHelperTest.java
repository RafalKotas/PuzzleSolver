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

class ColouringFieldsIfXWouldForceTooLongColouredFieldsSequenceLogHelperTest {

    @Test
    @DisplayName("ColouringFieldsIfXWouldForceTooLongColouredFieldsSequenceLogHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<ColouringFieldsIfXWouldForceTooLongColouredFieldsSequenceLogHelper> constructor = ColouringFieldsIfXWouldForceTooLongColouredFieldsSequenceLogHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("ColouringFieldsIfXWouldForceTooLongColouredFieldsSequenceLogHelper - generate example log - o08007 column 5")
    void shouldGenerateLogColumnCase() {
        // given
        boolean isRow = false;
        int index = 5;
        List<String> initialLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "O", "-"));
        List<List<Integer>> sequencesRanges = new ArrayList<>(
                Arrays.asList(
                        new ArrayList<>(List.of(0, 6)),
                        new ArrayList<>(List.of(6, 11)),
                        new ArrayList<>(List.of(12, 14))
                )
        );
        List<Integer> sequencesLengths = List.of(3, 4, 2);
        List<String> updatedLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "O", "-"));
        ColouringGenerateLogBaseContext colouringGenerateLogBaseContext = new ColouringGenerateLogBaseContext(
                isRow,
                index,
                initialLine,
                updatedLine,
                sequencesRanges,
                sequencesLengths
        );

        // when
        String actual = ColouringFieldsIfXWouldForceTooLongColouredFieldsSequenceLogHelper.generateLog(
                colouringGenerateLogBaseContext
        );

        // then
        String expected =
                "COLOURING_FIELDS_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE_IN_COLUMN: column=5\n" +
                        "initialLine=[-, -, -, -, -, -, -, -, -, O, -, -, -, O, -]\n" +
                        "updatedLine=[-, -, -, -, -, -, -, -, O, O, -, -, -, O, -]\n" +
                        "sequencesRanges=[[0, 6], [6, 11], [12, 14]]\n" +
                        "sequencesLengths=[3, 4, 2]\n";


        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("ColouringFieldsIfXWouldForceTooLongColouredFieldsSequenceLogHelper - convert example log to test arguments - o08007 column 3")
    void shouldConvertGeneratedLogToTestArgumentsColumnCase() {
        // given
        String generatedLog =
                "COLOURING_FIELDS_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE_IN_COLUMN: column=5\n" +
                        "initialLine=[-, -, -, -, -, -, -, -, -, O, -, -, -, O, -]\n" +
                        "updatedLine=[-, -, -, -, -, -, -, -, O, O, -, -, -, O, -]\n" +
                        "sequencesRanges=[[0, 6], [6, 11], [12, 14]]\n" +
                        "sequencesLengths=[3, 4, 2]\n";

        // when
        String convertedLog = ColouringFieldsIfXWouldForceTooLongColouredFieldsSequenceLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro08007"
        );

        // then
        String expected = "Arguments.of(\"o08007 / column=5 - colouring fields if X would force too long coloured field sequence\",\n" +
                "    new ArrayList<>(List.of(\"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"O\", \"-\", \"-\", \"-\", \"O\", \"-\")),\n" +
                "    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 6)), new ArrayList<>(List.of(6, 11)), new ArrayList<>(List.of(12, 14)))),\n" +
                "    List.of(3, 4, 2),\n" +
                "    new ArrayList<>(List.of(\"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"-\", \"O\", \"O\", \"-\", \"-\", \"-\", \"O\", \"-\")))\n" +
                ")";
        assertThat(convertedLog).isEqualTo(expected);
    }

    @Test
    @DisplayName("ColouringFieldsIfXWouldForceTooLongColouredFieldsSequenceLogHelper - generate example log - o08007 row 12")
    void shouldGenerateLogRowCase() {
        // given
        boolean isRow = true;
        int index = 12;
        List<String> initialLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "O", "-", "-", "-", "O", "-", "O", "-"));
        List<List<Integer>> sequencesRanges = new ArrayList<>(
                Arrays.asList(
                        new ArrayList<>(List.of(2, 14)),
                        new ArrayList<>(List.of(16, 16)),
                        new ArrayList<>(List.of(18, 18))
                )
        );
        List<Integer> sequencesLengths = List.of(11, 1, 1);
        List<String> updatedLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "O", "-", "O", "-"));
        ColouringGenerateLogBaseContext colouringGenerateLogBaseContext = new ColouringGenerateLogBaseContext(
                isRow,
                index,
                initialLine,
                updatedLine,
                sequencesRanges,
                sequencesLengths
        );

        // when
        String actual = ColouringFieldsIfXWouldForceTooLongColouredFieldsSequenceLogHelper.generateLog(
                colouringGenerateLogBaseContext
        );

        // then
        String expected =
                "COLOURING_FIELDS_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE_IN_ROW: row=12\n" +
                        "initialLine=[-, -, -, -, -, O, O, O, O, O, O, -, O, -, -, -, O, -, O, -]\n" +
                        "updatedLine=[-, -, -, -, -, O, O, O, O, O, O, O, O, -, -, -, O, -, O, -]\n" +
                        "sequencesRanges=[[2, 14], [16, 16], [18, 18]]\n" +
                        "sequencesLengths=[11, 1, 1]\n";

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("ColouringFieldsIfXWouldForceTooLongColouredFieldsSequenceLogHelper - convert example log to test arguments - o07942 row 12")
    void shouldConvertGeneratedLogToTestArgumentsRowCase() {
        // given
        String generatedLog =
                "COLOURING_FIELDS_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE_IN_ROW: row=12\n" +
                        "initialLine=[-, -, -, -, -, O, O, O, O, O, O, -, O, -, -, -, O, -, O, -]\n" +
                        "updatedLine=[-, -, -, -, -, O, O, O, O, O, O, O, O, -, -, -, O, -, O, -]\n" +
                        "sequencesRanges=[[2, 14], [16, 16], [18, 18]]\n" +
                        "sequencesLengths=[11, 1, 1]\n";

        // when
        String convertedLog = ColouringFieldsIfXWouldForceTooLongColouredFieldsSequenceLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro07942"
        );

        // then
        String expected = "Arguments.of(\"o07942 / row=12 - colouring fields if X would force too long coloured field sequence\",\n" +
                "    new ArrayList<>(List.of(\"-\", \"-\", \"-\", \"-\", \"-\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"-\", \"O\", \"-\", \"-\", \"-\", \"O\", \"-\", \"O\", \"-\")),\n" +
                "    new ArrayList<>(List.of(new ArrayList<>(List.of(2, 14)), new ArrayList<>(List.of(16, 16)), new ArrayList<>(List.of(18, 18)))),\n" +
                "    List.of(11, 1, 1),\n" +
                "    new ArrayList<>(List.of(\"-\", \"-\", \"-\", \"-\", \"-\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"O\", \"-\", \"-\", \"-\", \"O\", \"-\", \"O\", \"-\")))\n" +
                ")";
        assertThat(convertedLog).isEqualTo(expected);
    }
}