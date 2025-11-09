package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.marking;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MarkAvailableFieldsLogHelperTest {

    @Test
    @DisplayName("MarkAvailableFieldsLogHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<MarkAvailableFieldsLogHelper> constructor = MarkAvailableFieldsLogHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("Generate example log - o06005 column 0")
    void shouldGenerateLogColumnCase() {
        // given
        boolean isRow = false;
        int index = 0;
        int sequenceIndex = 0;
        String marker = "a";
        List<String> intialLine = new ArrayList<>(
                List.of("----", "----", "----", "--C-", "--C-",
                        "--C-", "--C-", "Ra--", "----", "----")
        );
        List<String> updatedLine = new ArrayList<>(
                List.of("----", "----", "----", "--Ca", "--Ca",
                        "--Ca", "--Ca", "RaCa", "----", "----")
        );

        // when
        String log = MarkAvailableFieldsLogHelper.generateLog(isRow,
                index,
                sequenceIndex,
                marker,
                intialLine,
                updatedLine
        );

        // then
        String expected =
                """
                        MARK_AVAILABLE_FIELDS_IN_COLUMN: column=0
                        sequenceIndex=0
                        marker=a
                        initialLine=[----, ----, ----, --C-, --C-, --C-, --C-, Ra--, ----, ----]
                        updatedLine=[----, ----, ----, --Ca, --Ca, --Ca, --Ca, RaCa, ----, ----]
                        """;
        assertThat(log).isEqualTo(expected);
    }

    @Test
    @DisplayName("Convert example log to test arguments - o10035 column 0")
    void shouldConvertGeneratedLogToTestArgumentsColumnCase() {
        // given
        String generatedLog =
                """
                        MARK_AVAILABLE_FIELDS_IN_COLUMN: column=0
                        sequenceIndex=0
                        marker=a
                        initialLine=[----, ----, ----, --C-, --C-, --C-, --C-, Ra--, ----, ----]
                        updatedLine=[----, ----, ----, --Ca, --Ca, --Ca, --Ca, RaCa, ----, ----]
                        """;

        // when
        String convertedLog = MarkAvailableFieldsLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro06005"
        );

        // then
        String expected = """
                Arguments.of("o06005 / column=0 - mark available fields",
                    0,
                    a,
                    new ArrayList<>(List.of("----", "----", "----", "--C-", "--C-", "--C-", "--C-", "Ra--", "----", "----")),
                    new ArrayList<>(List.of("----", "----", "----", "--Ca", "--Ca", "--Ca", "--Ca", "RaCa", "----", "----"))
                )""";
        AssertionsForClassTypes.assertThat(convertedLog).isEqualTo(expected);
    }

    @Test
    @DisplayName("Generate example log - o06005 row 1")
    void shouldGenerateLogRowCase() {
        // given
        boolean isRow = true;
        int index = 1;
        int sequenceIndex = 1;
        String marker = "b";
        List<String> intialLine = new ArrayList<>(
                List.of("----", "----", "--Ca", "----", "----",
                        "----", "--Ca", "----", "----", "----")
        );
        List<String> updatedLine = new ArrayList<>(
                List.of("----", "----", "RbCa", "----", "----",
                        "----", "--Ca", "----", "----", "----")
        );

        // when
        String log = MarkAvailableFieldsLogHelper.generateLog(isRow,
                index,
                sequenceIndex,
                marker,
                intialLine,
                updatedLine
        );

        // then
        String expected =
                """
                        MARK_AVAILABLE_FIELDS_IN_ROW: row=1
                        sequenceIndex=1
                        marker=b
                        initialLine=[----, ----, --Ca, ----, ----, ----, --Ca, ----, ----, ----]
                        updatedLine=[----, ----, RbCa, ----, ----, ----, --Ca, ----, ----, ----]
                        """;
        assertThat(log).isEqualTo(expected);
    }

    @Test
    @DisplayName("Convert example log to test arguments - o10035 row 1")
    void shouldConvertGeneratedLogToTestArgumentsRowCase() {
        // given
        String generatedLog =
                """
                        MARK_AVAILABLE_FIELDS_IN_ROW: row=1
                        sequenceIndex=1
                        marker=b
                        initialLine=[----, ----, --Ca, ----, ----, ----, --Ca, ----, ----, ----]
                        updatedLine=[----, ----, RbCa, ----, ----, ----, --Ca, ----, ----, ----]
                        """;

        // when
        String convertedLog = MarkAvailableFieldsLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro06005"
        );

        // then
        String expected = """
                Arguments.of("o06005 / row=1 - mark available fields",
                    1,
                    b,
                    new ArrayList<>(List.of("----", "----", "--Ca", "----", "----", "----", "--Ca", "----", "----", "----")),
                    new ArrayList<>(List.of("----", "----", "RbCa", "----", "----", "----", "--Ca", "----", "----", "----"))
                )""";
        AssertionsForClassTypes.assertThat(convertedLog).isEqualTo(expected);
    }
}