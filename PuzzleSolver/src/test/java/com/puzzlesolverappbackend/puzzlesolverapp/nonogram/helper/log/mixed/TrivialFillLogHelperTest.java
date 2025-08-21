package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TrivialFillLogHelperTest {

    @Test
    @DisplayName("TrivialFillLogHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<TrivialFillLogHelper> constructor = TrivialFillLogHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("Generate example log - o06005 row 7")
    void shouldGenerateLogRowCase() {
        // given
        boolean isRow = true;
        int index = 7;
        List<String> initialLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-"));
        List<String> updatedLine = new ArrayList<>(List.of("O", "-", "-", "-", "-", "-", "-", "-", "-", "-"));
        List<Integer> sequencesLengths = List.of(10);
        List<List<Integer>> sequencesRanges = new ArrayList<>(
                List.of(
                        new ArrayList<>(List.of(0, 9))
                )
        );

        // when
        String log = TrivialFillLogHelper.generateLog(isRow,
                index,
                sequencesLengths,
                sequencesRanges,
                initialLine,
                updatedLine
        );

        // then
        String expected =
                """
                        FILL_TRIVIAL_SEQUENCE_IN_ROW: row=7
                        sequencesLengths=[10]
                        sequencesRanges=[[0, 9]]
                        initialLine=[-, -, -, -, -, -, -, -, -, -]
                        updatedLine=[O, -, -, -, -, -, -, -, -, -]
                        """;
        assertThat(log).isEqualTo(expected);
    }

    @Test
    @DisplayName("TrivialFillLogHelper - convert example log to test arguments - o06005 row 7")
    void shouldConvertGeneratedLogToTestArgumentsRowCase() {
        // given
        String generatedLog =
                """
                        FILL_TRIVIAL_SEQUENCE_IN_ROW: row=7
                        sequencesLengths=[10]
                        sequencesRanges=[[0, 9]]
                        initialLine=[-, -, -, -, -, -, -, -, -, -]
                        updatedLine=[O, -, -, -, -, -, -, -, -, -]
                        """;

        // when
        String convertedLog = TrivialFillLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro06005"
        );

        // then
        String expected = """
                Arguments.of("o06005 / row=7 - trivial row fill",
                    List.of(List.of(10)),
                    List.of(new ArrayList<>(List.of(new ArrayList<>(List.of(0, 9))))),
                    List.of(new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-"))),
                    List.of(new ArrayList<>(List.of("O", "-", "-", "-", "-", "-", "-", "-", "-", "-")))
                )""";
        AssertionsForClassTypes.assertThat(convertedLog).isEqualTo(expected);
    }

    @Test
    @DisplayName("Generate example log - o06005 column 2")
    void shouldGenerateLogColumnCase() {
        // given
        boolean isRow = false;
        int index = 2;
        List<String> initialLine = new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-"));
        List<String> updatedLine = new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "X", "O"));
        List<Integer> sequencesLengths = List.of(8, 1);
        List<List<Integer>> sequencesRanges = new ArrayList<>(
                List.of(
                        new ArrayList<>(List.of(0, 7)),
                        new ArrayList<>(List.of(9, 9))
                )
        );

        // when
        String log = TrivialFillLogHelper.generateLog(isRow,
                index,
                sequencesLengths,
                sequencesRanges,
                initialLine,
                updatedLine
        );

        // then
        String expected =
                """
                        FILL_TRIVIAL_SEQUENCE_IN_COLUMN: column=2
                        sequencesLengths=[8, 1]
                        sequencesRanges=[[0, 7], [9, 9]]
                        initialLine=[-, -, -, -, -, -, -, -, -, -]
                        updatedLine=[O, O, O, O, O, O, O, O, X, O]
                        """;
        assertThat(log).isEqualTo(expected);
    }

    @Test
    @DisplayName("TrivialFillLogHelper - convert example log to test arguments - o06005 column 2")
    void shouldConvertGeneratedLogToTestArgumentsColumnCase() {
        // given
        String generatedLog =
                """
                        FILL_TRIVIAL_SEQUENCE_IN_COLUMN: column=2
                        sequencesLengths=[8, 1]
                        sequencesRanges=[[0, 7], [9, 9]]
                        initialLine=[-, -, -, -, -, -, -, -, -, -]
                        updatedLine=[O, O, O, O, O, O, O, O, X, O]
                        """;

        // when
        String convertedLog = TrivialFillLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro06005"
        );

        // then
        String expected = """
                Arguments.of("o06005 / column=2 - trivial column fill",
                    List.of(List.of(8, 1)),
                    List.of(new ArrayList<>(List.of(new ArrayList<>(List.of(0, 7)), new ArrayList<>(List.of(9, 9))))),
                    List.of(new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-"))),
                    List.of(new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "X", "O")))
                )""";
        AssertionsForClassTypes.assertThat(convertedLog).isEqualTo(expected);
    }
}