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
    }

    @Test
    @DisplayName("ExtendLogHelper - generate example log - o08007")
    void shouldGenerateLogWhenAtLeastOneSequenceIsExtended() {
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

        // when
        String actual = ExtendLogHelper.generateLog(
                index,
                direction,
                initialLine,
                sequencesRanges,
                sequencesLengths,
                updatedLine,
                isRow
        );

        // then
        String expected =
                "EXTEND_COLUMN_SEQUENCE: column=3, dir=toBottom\n" +
                        "initialLine=[-, -, X, -, -, -, -, -, X, O, -, -, -, -, -]\n" +
                        "sequencesRanges=[[0, 5], [3, 7], [9, 10], [10, 14]]\n" +
                        "sequencesLengths=[2, 3, 2, 1]\n" +
                        "updatedLine=[-, -, X, -, -, -, -, -, X, O, O, -, -, -, -]\n";

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("ExtendLogHelper - convert example log to test arguments - o08007")
    void shouldConvertGeneratedLogToTestArguments() {
        // given
        String generatedLog =
                "EXTEND_COLUMN_SEQUENCE: column=3, dir=toBottom\n" +
                        "initialLine=[-, -, X, -, -, -, -, -, X, O, -, -, -, -, -]\n" +
                        "sequencesRanges=[[0, 5], [3, 7], [9, 10], [10, 14]]\n" +
                        "sequencesLengths=[2, 3, 2, 1]\n" +
                        "updatedLine=[-, -, X, -, -, -, -, -, X, O, O, -, -, -, -]\n";

        // when
        String convertedLog = ExtendLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro08007"
        );

        // then
        String expected = "Arguments.of(\"o08007 / Column=3 - extending coloured fields near X\",\n" +
                "    new ArrayList<>(List.of(\"-\", \"-\", \"X\", \"-\", \"-\", \"-\", \"-\", \"-\", \"X\", \"O\", \"-\", \"-\", \"-\", \"-\", \"-\")),\n" +
                "    List.of(List.of(0, 5), List.of(3, 7), List.of(9, 10), List.of(10, 14)),\n" +
                "    List.of(2, 3, 2, 1),\n" +
                "    List.of(\"-\", \"-\", \"X\", \"-\", \"-\", \"-\", \"-\", \"-\", \"X\", \"O\", \"O\", \"-\", \"-\", \"-\", \"-\")\n" +
                ")";
        assertThat(convertedLog).isEqualTo(expected);
    }
}