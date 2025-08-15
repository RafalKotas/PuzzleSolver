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

class PlaceXsAtUnreachableFieldsLogHelperTest {

    @Test
    @DisplayName("PlaceXsAtUnreachableFieldsLogHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<PlaceXsAtUnreachableFieldsLogHelper> constructor = PlaceXsAtUnreachableFieldsLogHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("PlaceXsAtUnreachableFieldsLogHelper - generate example log - o08007 column 6")
    void shouldGenerateLogColumnCase() {
        // given
        int index = 6;
        List<String> initialLine = new ArrayList<>(List.of("O", "O", "-", "O", "O", "X", "O", "O", "X", "O", "O", "X", "-", "O", "-"));
        List<String> updatedLine = new ArrayList<>(List.of("O", "O", "X", "O", "O", "X", "O", "O", "X", "O", "O", "X", "-", "O", "-"));
        List<List<Integer>> sequencesRanges = new ArrayList<>(
                Arrays.asList(
                        new ArrayList<>(List.of(0, 1)),
                        new ArrayList<>(List.of(3, 4)),
                        new ArrayList<>(List.of(6, 7)),
                        new ArrayList<>(List.of(9, 10)),
                        new ArrayList<>(List.of(12, 14))
                )
        );

        // when
        String actual = PlaceXsAtUnreachableFieldsLogHelper.generateLog(
                false,
                index,
                initialLine,
                updatedLine,
                sequencesRanges
        );

        // then
        String expected =
                """
                        PLACE_XS_AT_UNREACHABLE_FIELDS_IN_COLUMN: column=6
                        initialLine=[O, O, -, O, O, X, O, O, X, O, O, X, -, O, -]
                        sequencesRanges=[[0, 1], [3, 4], [6, 7], [9, 10], [12, 14]]
                        updatedLine=[O, O, X, O, O, X, O, O, X, O, O, X, -, O, -]
                        """;

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("PlaceXsAtUnreachableFieldsLogHelper - convert example log to test arguments - o08007 column 6")
    void shouldConvertGeneratedLogToTestArgumentsColumnCase() {
        // given
        String generatedLog =
                """
                        PLACE_XS_AT_UNREACHABLE_FIELDS_IN_COLUMN: column=6
                        initialLine=[O, O, -, O, O, X, O, O, X, O, O, X, -, O, -]
                        sequencesRanges=[[0, 1], [3, 4], [6, 7], [9, 10], [12, 14]]
                        updatedLine=[O, O, X, O, O, X, O, O, X, O, O, X, -, O, -]
                        """;

        // when
        String convertedLog = PlaceXsAtUnreachableFieldsLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro08007"
        );

        // then
        String expected = """
                Arguments.of("o08007 / column=6 - place X at unreachable fields",
                    [O, O, -, O, O, X, O, O, X, O, O, X, -, O, -],
                    [[0, 1], [3, 4], [6, 7], [9, 10], [12, 14]],
                    [O, O, X, O, O, X, O, O, X, O, O, X, -, O, -])
                )""";
        assertThat(convertedLog).isEqualTo(expected);
    }

    @Test
    @DisplayName("PlaceXsAtUnreachableFieldsLogHelper - generate example log - o08007 row 9")
    void shouldGenerateLogRowCase() {
        // given
        int index = 9;
        List<String> initialLine = new ArrayList<>(List.of("-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-"));
        List<String> updatedLine = new ArrayList<>(List.of("-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "X", "X", "X", "X"));
        List<List<Integer>> sequencesRanges = new ArrayList<>(
                Arrays.asList(
                        new ArrayList<>(List.of(0, 10))
                )
        );

        // when
        String actual = PlaceXsAtUnreachableFieldsLogHelper.generateLog(
                true,
                index,
                initialLine,
                updatedLine,
                sequencesRanges
        );

        // then
        String expected =
                """
                        PLACE_XS_AT_UNREACHABLE_FIELDS_IN_ROW: row=9
                        initialLine=[-, -, O, O, O, O, O, O, O, -, -, -, -, -, -]
                        sequencesRanges=[[0, 10]]
                        updatedLine=[-, -, O, O, O, O, O, O, O, -, -, X, X, X, X]
                        """;

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("PlaceXsAtUnreachableFieldsLogHelper - convert example log to test arguments - o08007 row 9")
    void shouldConvertGeneratedLogToTestArgumentsRowCase() {
        // given
        String generatedLog =
                """
                        PLACE_XS_AT_UNREACHABLE_FIELDS_IN_ROW: row=9
                        initialLine=[-, -, O, O, O, O, O, O, O, -, -, -, -, -, -]
                        sequencesRanges=[[0, 10]]
                        updatedLine=[-, -, O, O, O, O, O, O, O, -, -, X, X, X, X]
                        """;

        // when
        String convertedLog = PlaceXsAtUnreachableFieldsLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro08007"
        );

        // then
        String expected = """
                Arguments.of("o08007 / row=9 - place X at unreachable fields",
                    [-, -, O, O, O, O, O, O, O, -, -, -, -, -, -],
                    [[0, 10]],
                    [-, -, O, O, O, O, O, O, O, -, -, X, X, X, X])
                )""";
        assertThat(convertedLog).isEqualTo(expected);
    }
}