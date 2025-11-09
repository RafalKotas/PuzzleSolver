package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PlaceXsIfOWillMergeNearFieldsToTooLongColouredSequenceLogHelperTest {

    @Test
    @DisplayName("PlaceXsIfONearXWillMergeNearFieldsToTooLongColouredSequenceLogHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<PlaceXsIfOWillMergeNearFieldsToTooLongColouredSequenceLogHelper> constructor = PlaceXsIfOWillMergeNearFieldsToTooLongColouredSequenceLogHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("PlaceXsIfOWillMergeNearFieldsToTooLongColouredSequenceLogHelper - generate example log - o06147 row 5")
    void shouldGenerateLogRowCase() {
        // given
        boolean isRow = true;
        int index = 5;
        List<String> initialLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "X", "X", "X", "O", "O", "O", "-", "O", "O", "-"));
        List<String> updatedLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "X", "X", "X", "O", "O", "O", "X", "O", "O", "-"));
        PlaceXGenerateLogBaseContext placeXGenerateLogBaseContext = new PlaceXGenerateLogBaseContext(
                isRow,
                index,
                initialLine,
                updatedLine
        );
        List<Integer> sequencesLengths = List.of(1, 3, 3);
        List<List<Integer>> sequencesRanges = new ArrayList<>(
          List.of(
                  new ArrayList<>(List.of(0, 4)),
                  new ArrayList<>(List.of(8, 10)),
                  new ArrayList<>(List.of(11, 14))
          )
        );

        // when
        String actual = PlaceXsIfOWillMergeNearFieldsToTooLongColouredSequenceLogHelper.generateLog(
                placeXGenerateLogBaseContext,
                "after",
                sequencesLengths,
                sequencesRanges
        );

        // then
        String expected =
                """
                        PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_ROW: row=5
                        xPlacement="after coloured sequence"
                        initialLine=[-, -, -, -, -, X, X, X, O, O, O, -, O, O, -]
                        updatedLine=[-, -, -, -, -, X, X, X, O, O, O, X, O, O, -]
                        sequencesLengths=[1, 3, 3]
                        sequencesRanges=[[0, 4], [8, 10], [11, 14]]
                        """;

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("PlaceXsIfOWillMergeNearFieldsToTooLongColouredSequenceLogHelper - convert example log to test arguments - o06147 row 5")
    void shouldConvertGeneratedLogToTestArgumentsRowCase() {
        // given
        String generatedLog =
                """
                        PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_ROW: row=5
                        xPlacement="after coloured sequence"
                        initialLine=[-, -, -, -, -, X, X, X, O, O, O, -, O, O, -]
                        updatedLine=[-, -, -, -, -, X, X, X, O, O, O, X, O, O, -]
                        sequencesLengths=[1, 3, 3]
                        sequencesRanges=[[0, 4], [8, 10], [11, 14]]
                        """;

        // when
        String convertedLog = PlaceXsIfOWillMergeNearFieldsToTooLongColouredSequenceLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro06147"
        );

        // then
        String expected = """
                Arguments.of("o06147 / row=5 - place X if O will merge near fields to too long sequence",
                    "after coloured sequence",
                    new ArrayList<>(List.of("-", "-", "-", "-", "-", "X", "X", "X", "O", "O", "O", "-", "O", "O", "-")),
                    new ArrayList<>(List.of("-", "-", "-", "-", "-", "X", "X", "X", "O", "O", "O", "X", "O", "O", "-")),
                    List.of(1, 3, 3),
                    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 4)), new ArrayList<>(List.of(8, 10)), new ArrayList<>(List.of(11, 14)))))
                )""";
        assertThat(convertedLog).isEqualTo(expected);
    }

    @Test
    @DisplayName("PlaceXsIfOWillMergeNearFieldsToTooLongColouredSequenceLogHelper - generate example log - o10155 column 5")
    void shouldGenerateLogColumnCase() {
        // given
        boolean isRow = false;
        int index = 5;
        List<String> initialLine = new ArrayList<>(List.of("-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "O", "O", "O", "O", "-", "-", "-"));
        List<String> updatedLine = new ArrayList<>(List.of("-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "O", "O", "O", "O", "-", "-", "-"));
        PlaceXGenerateLogBaseContext placeXGenerateLogBaseContext = new PlaceXGenerateLogBaseContext(
                isRow,
                index,
                initialLine,
                updatedLine
        );
        List<Integer> sequencesLengths = List.of(10, 5);
        List<List<Integer>> sequencesRanges = new ArrayList<>(
                List.of(
                        new ArrayList<>(List.of(2, 12)),
                        new ArrayList<>(List.of(13, 17))
                )
        );

        // when
        String actual = PlaceXsIfOWillMergeNearFieldsToTooLongColouredSequenceLogHelper.generateLog(
                placeXGenerateLogBaseContext,
                "after",
                sequencesLengths,
                sequencesRanges
        );

        // then
        String expected =
                """
                        PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_COLUMN: column=5
                        xPlacement="after coloured sequence"
                        initialLine=[-, -, -, O, O, O, O, O, O, O, O, O, -, O, O, O, O, -, -, -]
                        updatedLine=[-, -, -, O, O, O, O, O, O, O, O, O, X, O, O, O, O, -, -, -]
                        sequencesLengths=[10, 5]
                        sequencesRanges=[[2, 12], [13, 17]]
                        """;

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("PlaceXsIfOWillMergeNearFieldsToTooLongColouredSequenceLogHelper - convert example log to test arguments - o10155 column 5")
    void shouldConvertGeneratedLogToTestArgumentsColumnCase() {
        // given
        String generatedLog =
                """
                        PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_COLUMN: column=5
                        xPlacement="after coloured sequence"
                        initialLine=[-, -, -, O, O, O, O, O, O, O, O, O, -, O, O, O, O, -, -, -]
                        updatedLine=[-, -, -, O, O, O, O, O, O, O, O, O, X, O, O, O, O, -, -, -]
                        sequencesLengths=[10, 5]
                        sequencesRanges=[[2, 12], [13, 17]]
                        """;

        // when
        String convertedLog = PlaceXsIfOWillMergeNearFieldsToTooLongColouredSequenceLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro10155"
        );

        // then
        String expected = """
                Arguments.of("o10155 / column=5 - place X if O will merge near fields to too long sequence",
                    "after coloured sequence",
                    new ArrayList<>(List.of("-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "O", "O", "O", "O", "-", "-", "-")),
                    new ArrayList<>(List.of("-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "O", "O", "O", "O", "-", "-", "-")),
                    List.of(10, 5),
                    new ArrayList<>(List.of(new ArrayList<>(List.of(2, 12)), new ArrayList<>(List.of(13, 17)))))
                )""";
        assertThat(convertedLog).isEqualTo(expected);
    }
}