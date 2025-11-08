package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.ColouringGenerateLogBaseContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelperTest {

    @Test
    @DisplayName("PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper> constructor = PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
    }

    @Test
    @DisplayName("PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper - generate example log - o10155 column 1")
    void shouldGenerateLogColumnCase() {
        // given
        boolean isRow = false;
        int index = 1;
        List<String> initialLine = new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "X", "-", "X", "O", "X", "X", "O", "O", "O", "-", "-", "-", "-", "-"));
        List<String> updatedLine = new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "X", "-", "X", "O", "X", "X", "O", "O", "O", "O", "X", "-", "-", "-"));
        List<List<Integer>> sequencesRanges = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(0, 7)),
                new ArrayList<>(Arrays.asList(2, 9)),
                new ArrayList<>(Arrays.asList(4, 10)),
                new ArrayList<>(Arrays.asList(11, 15)),
                new ArrayList<>(Arrays.asList(14, 19))
        ));
        List<Integer> sequencesLengths = Arrays.asList(1, 1, 1, 4, 2);
        ColouringGenerateLogBaseContext context = new ColouringGenerateLogBaseContext(
                isRow,
                index,
                initialLine,
                updatedLine,
                sequencesRanges,
                sequencesLengths

        );
        String direction = "bottom";

        // when
        String actual = PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper.generateLog(
                context,
                direction
        );

        // then
        String expected =
                """
                        PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART_IN_COLUMN: column=1
                        direction="bottom"
                        initialLine=[-, -, -, -, -, -, X, -, X, O, X, X, O, O, O, -, -, -, -, -]
                        updatedLine=[-, -, -, -, -, -, X, -, X, O, X, X, O, O, O, O, X, -, -, -]
                        sequencesRanges=[[0, 7], [2, 9], [4, 10], [11, 15], [14, 19]]
                        sequencesLengths=[1, 1, 1, 4, 2]
                        """;

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper - convert example log to test arguments - o10155 column 1")
    void shouldConvertGeneratedLogToTestArgumentsColumnCase() {
        // given
        String generatedLog =
                """
                        PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART_IN_COLUMN: column=1
                        direction="bottom"
                        initialLine=[-, -, -, -, -, -, X, -, X, O, X, X, O, O, O, -, -, -, -, -]
                        updatedLine=[-, -, -, -, -, -, X, -, X, O, X, X, O, O, O, O, X, -, -, -]
                        sequencesRanges=[[0, 7], [2, 9], [4, 10], [11, 15], [14, 19]]
                        sequencesLengths=[1, 1, 1, 4, 2]
                        """;

        // when
        String convertedLog = PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro10155"
        );

        // then
        String expected = """
                Arguments.of("o10155 / column=1 - prevent extending coloured sequence to excess length colouring part",
                    "bottom",
                    new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "X", "-", "X", "O", "X", "X", "O", "O", "O", "-", "-", "-", "-", "-")),
                    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 7)), new ArrayList<>(List.of(2, 9)), new ArrayList<>(List.of(4, 10)), new ArrayList<>(List.of(11, 15)), new ArrayList<>(List.of(14, 19)))),
                    List.of(1, 1, 1, 4, 2),
                    new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "X", "-", "X", "O", "X", "X", "O", "O", "O", "O", "X", "-", "-", "-"))
                )""";
        assertThat(convertedLog).isEqualTo(expected);
    }

    @Test
    @DisplayName("PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper - generate example log - o06479 row 19")
    void shouldGenerateLogRowCase() {
        // given
        boolean isRow = true;
        int index = 19;
        List<String> initialLine = new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "X", "O", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-"));
        List<String> updatedLine = new ArrayList<>(Arrays.asList("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "X", "O", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-"));
        List<List<Integer>> sequencesRanges = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(0, 6)),
                new ArrayList<>(Arrays.asList(4, 8)),
                new ArrayList<>(Arrays.asList(6, 10)),
                new ArrayList<>(Arrays.asList(9, 14)),
                new ArrayList<>(Arrays.asList(14, 19)),
                new ArrayList<>(Arrays.asList(18, 22)),
                new ArrayList<>(Arrays.asList(21, 24))
        ));
        List<Integer> sequencesLengths = Arrays.asList(3, 1, 2, 2, 4, 2, 1);
        ColouringGenerateLogBaseContext context = new ColouringGenerateLogBaseContext(
                isRow,
                index,
                initialLine,
                updatedLine,
                sequencesRanges,
                sequencesLengths

        );
        String direction = "left";

        // when
        String actual = PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper.generateLog(
                context,
                direction
        );

        // then
        String expected =
                """
                        PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART_IN_ROW: row=19
                        direction="left"
                        initialLine=[-, -, -, -, -, -, -, -, -, -, -, -, O, X, O, -, O, O, -, -, -, -, -, -, -]
                        updatedLine=[-, -, -, -, -, -, -, -, -, -, -, O, O, X, O, -, O, O, -, -, -, -, -, -, -]
                        sequencesRanges=[[0, 6], [4, 8], [6, 10], [9, 14], [14, 19], [18, 22], [21, 24]]
                        sequencesLengths=[3, 1, 2, 2, 4, 2, 1]
                        """;

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper - convert example log to test arguments - o06479 row 19")
    void shouldConvertGeneratedLogToTestArgumentsRowCase() {
        // given
        String generatedLog =
                """
                        PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART_IN_ROW: row=19
                        direction="left"
                        initialLine=[-, -, -, -, -, -, -, -, -, -, -, -, O, X, O, -, O, O, -, -, -, -, -, -, -]
                        updatedLine=[-, -, -, -, -, -, -, -, -, -, -, O, O, X, O, -, O, O, -, -, -, -, -, -, -]
                        sequencesRanges=[[0, 6], [4, 8], [6, 10], [9, 14], [14, 19], [18, 22], [21, 24]]
                        sequencesLengths=[3, 1, 2, 2, 4, 2, 1]
                        """;

        // when
        String convertedLog = PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro06479"
        );

        // then
        String expected = """
                Arguments.of("o06479 / row=19 - prevent extending coloured sequence to excess length colouring part",
                    "left",
                    new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "X", "O", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-")),
                    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 6)), new ArrayList<>(List.of(4, 8)), new ArrayList<>(List.of(6, 10)), new ArrayList<>(List.of(9, 14)), new ArrayList<>(List.of(14, 19)), new ArrayList<>(List.of(18, 22)), new ArrayList<>(List.of(21, 24)))),
                    List.of(3, 1, 2, 2, 4, 2, 1),
                    new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "X", "O", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-"))
                )""";
        assertThat(convertedLog).isEqualTo(expected);
    }
}