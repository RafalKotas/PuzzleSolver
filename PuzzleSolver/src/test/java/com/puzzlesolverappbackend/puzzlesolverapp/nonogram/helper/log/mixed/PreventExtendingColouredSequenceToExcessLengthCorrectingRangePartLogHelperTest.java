package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelperTest {

    @Test
    @DisplayName("PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelper> constructor = PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelper - generate example log - o10155 column 1")
    void shouldGenerateLogColumnCase() {
        // given
        boolean isRow = false;
        int index = 1;
        List<Integer> sequencesLengths = List.of(4);
        List<String> initialLine = new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "X", "-", "X", "O", "X", "X", "O", "O", "O", "-", "-", "-", "-", "-"));
        List<String> updatedLine = new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "X", "-", "X", "O", "X", "X", "O", "O", "O", "O", "X", "-", "-", "-"));
        List<List<Integer>> initialRanges = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(0, 7)),
                new ArrayList<>(Arrays.asList(2, 9)),
                new ArrayList<>(Arrays.asList(4, 10)),
                new ArrayList<>(Arrays.asList(11, 15)),
                new ArrayList<>(Arrays.asList(14, 19))
        ));

        List<List<Integer>> updatedRanges = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(0, 7)),
                new ArrayList<>(Arrays.asList(2, 9)),
                new ArrayList<>(Arrays.asList(4, 10)),
                new ArrayList<>(Arrays.asList(12, 15)),
                new ArrayList<>(Arrays.asList(14, 19))
        ));

        // when
        String actual = PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelper.generateLog(
                isRow,
                index,
                sequencesLengths,
                initialLine,
                updatedLine,
                initialRanges,
                updatedRanges
        );

        // then
        String expected =
                """
                        PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART_IN_COLUMN: column=1
                        sequencesLengths=[4]
                        initialLine=[-, -, -, -, -, -, X, -, X, O, X, X, O, O, O, -, -, -, -, -]
                        updatedLine=[-, -, -, -, -, -, X, -, X, O, X, X, O, O, O, O, X, -, -, -]
                        initialRanges=[[0, 7], [2, 9], [4, 10], [11, 15], [14, 19]]
                        updatedRanges=[[0, 7], [2, 9], [4, 10], [12, 15], [14, 19]]
                        """;

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelper - convert example log to test arguments - o10155 column 1")
    void shouldConvertGeneratedLogToTestArgumentsColumnCase() {
        // given
        String generatedLog =
                """
                        PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART_IN_COLUMN: column=1
                        sequencesLengths=[4]
                        initialLine=[-, -, -, -, -, -, X, -, X, O, X, X, O, O, O, -, -, -, -, -]
                        updatedLine=[-, -, -, -, -, -, X, -, X, O, X, X, O, O, O, O, X, -, -, -]
                        initialRanges=[[0, 7], [2, 9], [4, 10], [11, 15], [14, 19]]
                        updatedRanges=[[0, 7], [2, 9], [4, 10], [12, 15], [14, 19]]
                        """;

        // when
        String convertedLog = PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro10155"
        );

        // then
        String expected = """
                Arguments.of("ro10155 / column=1 - prevent extending coloured sequence to excess length correcting range part",
                    List.of(4),
                    new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "X", "-", "X", "O", "X", "X", "O", "O", "O", "-", "-", "-", "-", "-")),
                    new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "X", "-", "X", "O", "X", "X", "O", "O", "O", "O", "X", "-", "-", "-")),
                    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 7)), new ArrayList<>(List.of(2, 9)), new ArrayList<>(List.of(4, 10)), new ArrayList<>(List.of(11, 15)), new ArrayList<>(List.of(14, 19)))),
                    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 7)), new ArrayList<>(List.of(2, 9)), new ArrayList<>(List.of(4, 10)), new ArrayList<>(List.of(12, 15)), new ArrayList<>(List.of(14, 19))))
                )""";
        assertThat(convertedLog).isEqualTo(expected);
    }

    @Test
    @DisplayName("PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelper - generate example log - o07940 row 17")
    void shouldGenerateLogRowCase() {
        // given
        boolean isRow = true;
        int index = 17;
        List<Integer> sequencesLengths = List.of(1);
        List<String> initialLine = new ArrayList<>(Arrays.asList("O", "O", "O", "O", "X", "-", "X", "O", "-", "-", "-", "X", "O", "O", "X", "-", "O", "O", "-", "X"));
        List<String> updatedLine = new ArrayList<>(Arrays.asList("O", "O", "O", "O", "X", "-", "X", "O", "X", "-", "-", "X", "O", "O", "X", "-", "O", "O", "-", "X"));
        List<List<Integer>> initialRanges = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(0, 3)),
                new ArrayList<>(Arrays.asList(5, 7)),
                new ArrayList<>(Arrays.asList(12, 13)),
                new ArrayList<>(Arrays.asList(15, 18))
        ));

        List<List<Integer>> updatedRanges = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(0, 3)),
                new ArrayList<>(Arrays.asList(5, 7)),
                new ArrayList<>(Arrays.asList(12, 13)),
                new ArrayList<>(Arrays.asList(15, 18))
        ));

        // when
        String actual = PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelper.generateLog(
                isRow,
                index,
                sequencesLengths,
                initialLine,
                updatedLine,
                initialRanges,
                updatedRanges
        );

        // then
        String expected =
                """
                        PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART_IN_ROW: row=17
                        sequencesLengths=[1]
                        initialLine=[O, O, O, O, X, -, X, O, -, -, -, X, O, O, X, -, O, O, -, X]
                        updatedLine=[O, O, O, O, X, -, X, O, X, -, -, X, O, O, X, -, O, O, -, X]
                        initialRanges=[[0, 3], [5, 7], [12, 13], [15, 18]]
                        updatedRanges=[[0, 3], [5, 7], [12, 13], [15, 18]]
                        """;

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelper - convert example log to test arguments - o07940 row 17")
    void shouldConvertGeneratedLogToTestArgumentsRowCase() {
        // given
        String generatedLog =
                """
                        PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART_IN_ROW: row=17
                        sequencesLengths=[1]
                        initialLine=[O, O, O, O, X, -, X, O, -, -, -, X, O, O, X, -, O, O, -, X]
                        updatedLine=[O, O, O, O, X, -, X, O, X, -, -, X, O, O, X, -, O, O, -, X]
                        initialRanges=[[0, 3], [5, 7], [12, 13], [15, 18]]
                        updatedRanges=[[0, 3], [5, 7], [12, 13], [15, 18]]
                        """;

        // when
        String convertedLog = PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro07940"
        );

        // then
        String expected = """
                Arguments.of("ro07940 / row=17 - prevent extending coloured sequence to excess length correcting range part",
                    List.of(1),
                    new ArrayList<>(List.of("O", "O", "O", "O", "X", "-", "X", "O", "-", "-", "-", "X", "O", "O", "X", "-", "O", "O", "-", "X")),
                    new ArrayList<>(List.of("O", "O", "O", "O", "X", "-", "X", "O", "X", "-", "-", "X", "O", "O", "X", "-", "O", "O", "-", "X")),
                    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 3)), new ArrayList<>(List.of(5, 7)), new ArrayList<>(List.of(12, 13)), new ArrayList<>(List.of(15, 18)))),
                    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 3)), new ArrayList<>(List.of(5, 7)), new ArrayList<>(List.of(12, 13)), new ArrayList<>(List.of(15, 18))))
                )""";
        assertThat(convertedLog).isEqualTo(expected);
    }
}