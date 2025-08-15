package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PlaceXsAroundLongestSequenceLogHelperTest {

    @Test
    @DisplayName("PlaceXsAroundLongestSequenceLogHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<PlaceXsAroundLongestSequenceLogHelper> constructor = PlaceXsAroundLongestSequenceLogHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("PlaceXsAroundLongestSequenceLogHelperTest - generate example log - o07942 column 14")
    void shouldGenerateLogColumnCase() {
        // given
        boolean isRow = false;
        int index = 14;
        List<Integer> xEdges = List.of(18, 20);
        List<String> initialLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-", "O"));
        List<String> updatedLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "X", "O"));
        boolean onlyMatching = true;

        // when
        String actual = PlaceXsAroundLongestSequenceLogHelper.generateLog(
                isRow,
                index,
                xEdges,
                initialLine,
                updatedLine,
                onlyMatching
        );

        // then
        String expected =
                """
                        PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCE: column=14
                        xEdges=[18, 20]
                        onlyMatching=true
                        initialLine=[-, -, -, -, -, -, -, -, -, -, X, -, -, -, -, -, -, -, -, O]
                        updatedLine=[-, -, -, -, -, -, -, -, -, -, X, -, -, -, -, -, -, -, X, O]
                        """;

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("PlaceXsAroundLongestSequenceLogHelperTest - convert example log to test arguments - o07942 column 14")
    void shouldConvertGeneratedLogToTestArgumentsColumnCase() {
        // given
        String generatedLog =
                """
                        PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCE: column=14
                        xEdges=[18, 20]
                        onlyMatching=true
                        initialLine=[-, -, -, -, -, -, -, -, -, -, X, -, -, -, -, -, -, -, -, O]
                        updatedLine=[-, -, -, -, -, -, -, -, -, -, X, -, -, -, -, -, -, -, X, O]
                        """;

        // when
        String convertedLog = PlaceXsAroundLongestSequenceLogHelper.convertLogToTestArguments(
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
    @DisplayName("PlaceXsAroundLongestSequenceLogHelperTest - generate example log - o07942 row 11")
    void shouldGenerateLogRowCase() {
        // given
        boolean isRow = true;
        int index = 11;
        List<Integer> xEdges = List.of(15, 17);
        List<String> initialLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "X", "-", "-", "-", "O", "-", "O", "-"));
        List<String> updatedLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "X", "-", "-", "X", "O", "X", "O", "-"));
        boolean onlyMatching = true;

        // when
        String actual = PlaceXsAroundLongestSequenceLogHelper.generateLog(
                isRow,
                index,
                xEdges,
                initialLine,
                updatedLine,
                onlyMatching
        );

        // then
        String expected =
                """
                        PLACE_XS_ROW_AROUND_LONGEST_SEQUENCE: row=11
                        xEdges=[15, 17]
                        onlyMatching=true
                        initialLine=[-, -, -, -, -, -, -, O, O, O, -, -, X, -, -, -, O, -, O, -]
                        updatedLine=[-, -, -, -, -, -, -, O, O, O, -, -, X, -, -, X, O, X, O, -]
                        """;

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("PlaceXsAroundLongestSequenceLogHelperTest - convert example log to test arguments - o07942 row 11")
    void shouldConvertGeneratedLogToTestArgumentsRowCase() {
        // given
        String generatedLog =
                """
                        PLACE_XS_ROW_AROUND_LONGEST_SEQUENCE: row=11
                        xEdges=[15, 17]
                        onlyMatching=true
                        initialLine=[-, -, -, -, -, -, -, O, O, O, -, -, X, -, -, -, O, -, O, -]
                        updatedLine=[-, -, -, -, -, -, -, O, O, O, -, -, X, -, -, X, O, X, O, -]
                        """;

        // when
        String convertedLog = PlaceXsAroundLongestSequenceLogHelper.convertLogToTestArguments(
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