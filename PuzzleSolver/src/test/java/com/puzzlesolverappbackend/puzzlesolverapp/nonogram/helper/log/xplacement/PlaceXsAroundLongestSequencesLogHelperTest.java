package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PlaceXsAroundLongestSequencesLogHelperTest {

    @Test
    @DisplayName("PlaceXsAroundLongestSequenceLogHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<PlaceXsAroundLongestSequencesLogHelper> constructor = PlaceXsAroundLongestSequencesLogHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("PlaceXsAroundLongestSequenceLogHelper - generate example log - o07942 column 14")
    void shouldGenerateLogColumnCase() {
        // given
        boolean isRow = false;
        int index = 14;
        List<Integer> xEdges = List.of(18, 20);
        List<String> initialLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-", "O"));
        List<String> updatedLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "X", "O"));
        boolean onlyMatching = true;
        PlaceXBaseLogContext placeXBaseLogContext = new PlaceXBaseLogContext(
                isRow,
                index,
                initialLine,
                updatedLine
        );

        // when
        String actual = PlaceXsAroundLongestSequencesLogHelper.generateLog(
                placeXBaseLogContext,
                xEdges,
                onlyMatching
        );

        // then
        String expected =
                """
                        PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_COLUMN: column=14
                        initialLine=[-, -, -, -, -, -, -, -, -, -, X, -, -, -, -, -, -, -, -, O]
                        updatedLine=[-, -, -, -, -, -, -, -, -, -, X, -, -, -, -, -, -, -, X, O]
                        xEdges=[18, 20]
                        onlyMatching=true
                        """;

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("PlaceXsAroundLongestSequenceLogHelper - convert example log to test arguments - o07942 column 14")
    void shouldConvertGeneratedLogToTestArgumentsColumnCase() {
        // given
        String generatedLog =
                """
                        PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_COLUMN: column=14
                        initialLine=[-, -, -, -, -, -, -, -, -, -, X, -, -, -, -, -, -, -, -, O]
                        updatedLine=[-, -, -, -, -, -, -, -, -, -, X, -, -, -, -, -, -, -, X, O]
                        xEdges=[18, 20]
                        onlyMatching=true
                        """;

        // when
        String convertedLog = PlaceXsAroundLongestSequencesLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro07942"
        );

        // then
        String expected = """
                Arguments.of("o07942 / column=14 - place X around longest sequences",
                    List.of(18, 20),
                    true,
                    new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-", "O")),
                    new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "X", "O"))
                )""";
        assertThat(convertedLog).isEqualTo(expected);
    }

    @Test
    @DisplayName("PlaceXsAroundLongestSequenceLogHelper - generate example log - o07942 row 11")
    void shouldGenerateLogRowCase() {
        // given
        boolean isRow = true;
        int index = 11;
        List<Integer> xEdges = List.of(15, 17);
        List<String> initialLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "X", "-", "-", "-", "O", "-", "O", "-"));
        List<String> updatedLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "X", "-", "-", "X", "O", "X", "O", "-"));
        boolean onlyMatching = true;
        PlaceXBaseLogContext placeXBaseLogContext = new PlaceXBaseLogContext(
                isRow,
                index,
                initialLine,
                updatedLine
        );

        // when
        String actual = PlaceXsAroundLongestSequencesLogHelper.generateLog(
                placeXBaseLogContext,
                xEdges,
                onlyMatching
        );

        // then
        String expected =
                """
                        PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_ROW: row=11
                        initialLine=[-, -, -, -, -, -, -, O, O, O, -, -, X, -, -, -, O, -, O, -]
                        updatedLine=[-, -, -, -, -, -, -, O, O, O, -, -, X, -, -, X, O, X, O, -]
                        xEdges=[15, 17]
                        onlyMatching=true
                        """;

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("PlaceXsAroundLongestSequenceLogHelper - convert example log to test arguments - o07942 row 11")
    void shouldConvertGeneratedLogToTestArgumentsRowCase() {
        // given
        String generatedLog =
                """
                        PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_ROW: row=11
                        initialLine=[-, -, -, -, -, -, -, O, O, O, -, -, X, -, -, -, O, -, O, -]
                        updatedLine=[-, -, -, -, -, -, -, O, O, O, -, -, X, -, -, X, O, X, O, -]
                        xEdges=[15, 17]
                        onlyMatching=true
                        """;

        // when
        String convertedLog = PlaceXsAroundLongestSequencesLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro07942"
        );

        // then
        String expected = """
                Arguments.of("o07942 / row=11 - place X around longest sequences",
                    List.of(15, 17),
                    true,
                    new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "X", "-", "-", "-", "O", "-", "O", "-")),
                    new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "X", "-", "-", "X", "O", "X", "O", "-"))
                )""";
        assertThat(convertedLog).isEqualTo(expected);
    }
}