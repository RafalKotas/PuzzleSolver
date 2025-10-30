package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PlaceXsAtTooShortEmptySequencesLogHelperTest {

    @Test
    @DisplayName("PlaceXsAtTooShortEmptySequencesLogHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<PlaceXsAtTooShortEmptySequencesLogHelper> constructor = PlaceXsAtTooShortEmptySequencesLogHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("PlaceXsAtTooShortEmptySequencesLogHelper - generate example log - o07942 column 0")
    void shouldGenerateLogColumnCase() {
        // given
        boolean isRow = false;
        int index = 0;
        List<String> initialLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "X", "-", "-", "-", "-", "-", "-", "-"));
        List<String> updatedLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "X", "-", "-", "-", "-", "-", "-", "-"));
        List<List<Integer>> sequencesRanges = new ArrayList<>(
                Arrays.asList(
                        new ArrayList<>(List.of(0, 19))
                )
        );
        List<Integer> sequencesLengths = List.of(2);
        List<Integer> excludedSequencesIndexes = new ArrayList<>(List.of());
        PlaceXGenerateLogBaseContext placeXGenerateLogBaseContext = new PlaceXGenerateLogBaseContext(
                isRow,
                index,
                initialLine,
                updatedLine
        );

        // when
        String actual = PlaceXsAtTooShortEmptySequencesLogHelper.generateLog(
                placeXGenerateLogBaseContext,
                sequencesRanges,
                sequencesLengths,
                excludedSequencesIndexes
        );

        // then
        String expected =
                """
                        PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES_IN_COLUMN: column=0
                        initialLine=[-, -, -, -, -, -, -, -, -, -, X, -, X, -, -, -, -, -, -, -]
                        updatedLine=[-, -, -, -, -, -, -, -, -, -, X, X, X, -, -, -, -, -, -, -]
                        sequencesRanges=[[0, 19]]
                        sequencesLengths=[2]
                        excludedSequencesIndexes=[]
                        """;

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("PlaceXsAtTooShortEmptySequencesLogHelper - convert example log to test arguments - o07942 column 0")
    void shouldConvertGeneratedLogToTestArgumentsColumnCase() {
        // given
        String generatedLog =
                """
                        PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES_IN_COLUMN: column=0
                        initialLine=[-, -, -, -, -, -, -, -, -, -, X, -, X, -, -, -, -, -, -, -]
                        updatedLine=[-, -, -, -, -, -, -, -, -, -, X, X, X, -, -, -, -, -, -, -]
                        sequencesRanges=[[0, 19]]
                        sequencesLengths=[2]
                        excludedSequencesIndexes=[]
                        """;

        // when
        String convertedLog = PlaceXsAtTooShortEmptySequencesLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro07942"
        );

        // then
        String expected = """
                Arguments.of("o07942 / column=0 - place X at too short empty sequences",
                    new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "X", "-", "-", "-", "-", "-", "-", "-")),
                    List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "X", "X", "-", "-", "-", "-", "-", "-", "-"),
                    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 19)))),
                    List.of(2),
                    new ArrayList<>(List.of()))
                )""";
        assertThat(convertedLog).isEqualTo(expected);
    }

    @Test
    @DisplayName("PlaceXsAtTooShortEmptySequencesLogHelper - generate example log - o07942 row 7")
    void shouldGenerateLogRowCase() {
        // given
        boolean isRow = true;
        int index = 7;
        List<String> initialLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "X", "-", "-", "X", "-", "X", "-", "-", "X", "-", "-", "-", "X", "O", "X"));
        List<String> updatedLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "X", "-", "-", "X", "X", "X", "-", "-", "X", "-", "-", "-", "X", "O", "X"));
        List<List<Integer>> sequencesRanges = new ArrayList<>(
                Arrays.asList(
                        new ArrayList<>(List.of(0, 12)),
                        new ArrayList<>(List.of(3, 16)),
                        new ArrayList<>(List.of(18, 18))
                )
        );
        List<Integer> sequencesLengths = List.of(2, 2, 1);
        List<Integer> excludedSequencesIndexes = new ArrayList<>(List.of(2));
        PlaceXGenerateLogBaseContext placeXGenerateLogBaseContext = new PlaceXGenerateLogBaseContext(
                isRow,
                index,
                initialLine,
                updatedLine
        );

        // when
        String actual = PlaceXsAtTooShortEmptySequencesLogHelper.generateLog(
                placeXGenerateLogBaseContext,
                sequencesRanges,
                sequencesLengths,
                excludedSequencesIndexes
        );

        // then
        String expected =
                """
                        PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES_IN_ROW: row=7
                        initialLine=[-, -, -, -, -, X, -, -, X, -, X, -, -, X, -, -, -, X, O, X]
                        updatedLine=[-, -, -, -, -, X, -, -, X, X, X, -, -, X, -, -, -, X, O, X]
                        sequencesRanges=[[0, 12], [3, 16], [18, 18]]
                        sequencesLengths=[2, 2, 1]
                        excludedSequencesIndexes=[2]
                        """;

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("PlaceXsAtTooShortEmptySequencesLogHelper - convert example log to test arguments - o07942 row 7")
    void shouldConvertGeneratedLogToTestArgumentsRowCase() {
        // given
        String generatedLog =
                """
                        PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES_IN_ROW: row=9
                        initialLine=[-, -, -, -, -, X, -, -, X, -, X, -, -, X, -, -, -, X, O, X]
                        updatedLine=[-, -, -, -, -, X, -, -, X, X, X, -, -, X, -, -, -, X, O, X]
                        sequencesRanges=[[0, 12], [3, 16], [18, 18]]
                        sequencesLengths=[2, 2, 1]
                        excludedSequencesIndexes=[2]
                        """;

        // when
        String convertedLog = PlaceXsAtTooShortEmptySequencesLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro07942"
        );

        // then
        String expected = """
                Arguments.of("o07942 / row=9 - place X at too short empty sequences",
                    new ArrayList<>(List.of("-", "-", "-", "-", "-", "X", "-", "-", "X", "-", "X", "-", "-", "X", "-", "-", "-", "X", "O", "X")),
                    List.of("-", "-", "-", "-", "-", "X", "-", "-", "X", "X", "X", "-", "-", "X", "-", "-", "-", "X", "O", "X"),
                    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 12)), new ArrayList<>(List.of(3, 16)), new ArrayList<>(List.of(18, 18)))),
                    List.of(2, 2, 1),
                    new ArrayList<>(List.of(2)))
                )""";
        assertThat(convertedLog).isEqualTo(expected);
    }
}