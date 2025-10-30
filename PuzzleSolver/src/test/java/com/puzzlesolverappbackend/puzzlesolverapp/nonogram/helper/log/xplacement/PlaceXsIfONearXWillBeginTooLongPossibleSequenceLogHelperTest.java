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

class PlaceXsIfONearXWillBeginTooLongPossibleSequenceLogHelperTest {

    @Test
    @DisplayName("PlaceXsIfONearXWillBeginTooLongPossibleSequenceLogHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<PlaceXsIfONearXWillBeginTooLongPossibleSequenceLogHelper> constructor = PlaceXsIfONearXWillBeginTooLongPossibleSequenceLogHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("PlaceXsIfONearXWillBeginTooLongPossibleSequenceLogHelper - generate example log - o08007 column 7")
    void shouldGenerateLogColumnCase() {
        // given
        boolean isRow = false;
        int index = 7;
        List<String> initialLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "X", "-", "-", "-", "O", "O", "-", "-", "X", "-"));
        List<String> updatedLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "X", "X", "X", "X", "O", "O", "-", "-", "X", "-"));
        List<Integer> sequencesLengths = List.of(2, 1);
        List<List<Integer>> sequencesRanges = new ArrayList<>(
                Arrays.asList(
                        new ArrayList<>(List.of(9, 10)),
                        new ArrayList<>(List.of(9, 14))
                )
        );
        PlaceXBaseLogContext placeXBaseLogContext = new PlaceXBaseLogContext(
                isRow,
                index,
                initialLine,
                updatedLine
        );

        // when

        String actual = PlaceXsIfONearXWillBeginTooLongPossibleSequenceLogHelper.generateLog(
                placeXBaseLogContext,
                sequencesLengths,
                sequencesRanges
        );

        // then
        String expected =
                """
                        PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_COLUMN: column=7
                        initialLine=[-, -, -, -, -, X, -, -, -, O, O, -, -, X, -]
                        updatedLine=[-, -, -, -, -, X, X, X, X, O, O, -, -, X, -]
                        sequencesLengths=[2, 1]
                        sequencesRanges=[[9, 10], [9, 14]]
                        """;

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("PlaceXsIfONearXWillBeginTooLongPossibleSequenceLogHelper - convert example log to test arguments - o08007 column 7")
    void shouldConvertGeneratedLogToTestArgumentsColumnCase() {
        // given
        String generatedLog =
                """
                        PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_COLUMN: column=7
                        initialLine=[-, -, -, -, -, X, -, -, -, O, O, -, -, X, -]
                        updatedLine=[-, -, -, -, -, X, X, X, X, O, O, -, -, X, -]
                        sequencesLengths=[2, 1]
                        sequencesRanges=[[9, 10], [9, 14]]
                        """;

        // when
        String convertedLog = PlaceXsIfONearXWillBeginTooLongPossibleSequenceLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro08007"
        );

        // then
        String expected = """
                Arguments.of("o08007 / column=7 - place X if O near X will begin too long possible sequence",
                    new ArrayList<>(List.of("-", "-", "-", "-", "-", "X", "-", "-", "-", "O", "O", "-", "-", "X", "-")),
                    List.of("-", "-", "-", "-", "-", "X", "X", "X", "X", "O", "O", "-", "-", "X", "-"),
                    List.of(2, 1),
                    new ArrayList<>(List.of(new ArrayList<>(List.of(9, 10)), new ArrayList<>(List.of(9, 14)))))
                )""";
        assertThat(convertedLog).isEqualTo(expected);
    }

    @Test
    @DisplayName("PlaceXsIfONearXWillBeginTooLongPossibleSequenceLogHelper - generate example log - o08007 row 3")
    void shouldGenerateLogRowCase() {
        // given
        boolean isRow = true;
        int index = 3;
        List<String> initialLine = new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"));
        List<String> updatedLine = new ArrayList<>(List.of("-", "X", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"));
        List<Integer> sequencesLengths = List.of(1, 2, 2);
        List<List<Integer>> sequencesRanges = new ArrayList<>(
                Arrays.asList(
                        new ArrayList<>(List.of(0, 2)),
                        new ArrayList<>(List.of(2, 11)),
                        new ArrayList<>(List.of(5, 14))
                )
        );
        PlaceXBaseLogContext placeXBaseLogContext = new PlaceXBaseLogContext(
                isRow,
                index,
                initialLine,
                updatedLine
        );

        // when
        String actual = PlaceXsIfONearXWillBeginTooLongPossibleSequenceLogHelper.generateLog(
                placeXBaseLogContext,
                sequencesLengths,
                sequencesRanges
        );

        // then
        String expected =
                """
                PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_ROW: row=3
                initialLine=[-, -, O, -, -, -, -, -, -, -, -, -, -, -, -]
                updatedLine=[-, X, O, -, -, -, -, -, -, -, -, -, -, -, -]
                sequencesLengths=[1, 2, 2]
                sequencesRanges=[[0, 2], [2, 11], [5, 14]]
                """;

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("PlaceXsIfONearXWillBeginTooLongPossibleSequenceLogHelper - convert example log to test arguments - o08007 row 3")
    void shouldConvertGeneratedLogToTestArgumentsRowCase() {
        // given
        String generatedLog =
                """
                PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_ROW: row=3
                initialLine=[-, -, O, -, -, -, -, -, -, -, -, -, -, -, -]
                updatedLine=[-, X, O, -, -, -, -, -, -, -, -, -, -, -, -]
                sequencesLengths=[1, 2, 2]
                sequencesRanges=[[0, 2], [2, 11], [5, 14]]
                """;

        // when
        String convertedLog = PlaceXsIfONearXWillBeginTooLongPossibleSequenceLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro08007"
        );

        // then
        String expected = """
                Arguments.of("o08007 / row=3 - place X if O near X will begin too long possible sequence",
                    new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")),
                    List.of("-", "X", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                    List.of(1, 2, 2),
                    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 2)), new ArrayList<>(List.of(2, 11)), new ArrayList<>(List.of(5, 14)))))
                )""";
        assertThat(convertedLog).isEqualTo(expected);
    }
}