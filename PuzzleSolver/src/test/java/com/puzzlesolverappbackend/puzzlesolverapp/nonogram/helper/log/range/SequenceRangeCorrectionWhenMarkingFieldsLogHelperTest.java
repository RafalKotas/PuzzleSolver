package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SequenceRangeCorrectionWhenMarkingFieldsLogHelperTest {

    @Test
    @DisplayName("SequenceRangeCorrectionWhenMarkingFieldsLogHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<SequenceRangeCorrectionWhenMarkingFieldsLogHelper> constructor = SequenceRangeCorrectionWhenMarkingFieldsLogHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("Generate example log - o08007 row 9")
    void shouldGenerateLogRowCase() {
        // given
        boolean isRow = true;
        int index = 9;
        int sequenceIndex = 0;

        List<List<Integer>> sequencesRanges = new ArrayList<>(
                List.of(
                        new ArrayList<>(Arrays.asList(0, 14))
                )
        );
        List<Integer> updatedRange = new ArrayList<>(
                List.of(0, 10)
        );
        List<Integer> sequencesLengths = List.of(9);

        // when
        String log = SequenceRangeCorrectionWhenMarkingFieldsLogHelper.generateLog(isRow,
                index,
                sequenceIndex,
                sequencesRanges,
                sequencesLengths,
                updatedRange);

        // then
        String expected =
                """
                        SEQUENCE_RANGE_CORRECTION_WHEN_MARKING_FIELDS_IN_ROW: row=9
                        sequenceIndex=0
                        sequencesRanges=[[0, 14]]
                        sequencesLengths=[9]
                        updatedRange=[0, 10]
                        """;
        assertThat(log).isEqualTo(expected);
    }

    @Test
    @DisplayName("Convert example log to test arguments - o08007 row 9")
    void shouldConvertGeneratedLogToTestArgumentsRowCase() {
        // given
        String generatedLog =
                """
                        SEQUENCE_RANGE_CORRECTION_WHEN_MARKING_FIELDS_IN_ROW: row=9
                        sequenceIndex=0
                        sequencesRanges=[[0, 14]]
                        sequencesLengths=[9]
                        updatedRange=[0, 10]
                        """;

        // when
        String convertedLog = SequenceRangeCorrectionWhenMarkingFieldsLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro08007"
        );

        // then
        String expected = """
                Arguments.of("o08007 / row=9 - sequences range correction when marking fields",
                    0,
                    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 14)))),
                    List.of(9),
                    new ArrayList<>(List.of(0, 10))
                )""";
        AssertionsForClassTypes.assertThat(convertedLog).isEqualTo(expected);
    }

    @Test
    @DisplayName("Generate example log - o08007 column 2")
    void shouldGenerateLogColumnCase() {
        // given
        boolean isRow = false;
        int index = 2;
        int sequenceIndex = 1;

        List<List<Integer>> sequencesRanges = new ArrayList<>(
                List.of(
                        new ArrayList<>(Arrays.asList(1, 4)),
                        new ArrayList<>(Arrays.asList(6, 11)),
                        new ArrayList<>(Arrays.asList(13, 14))
                )
        );
        List<Integer> updatedRange = new ArrayList<>(
                List.of(6, 10)
        );
        List<Integer> sequencesLengths = List.of(4, 5, 2);

        // when
        String log = SequenceRangeCorrectionWhenMarkingFieldsLogHelper.generateLog(isRow,
                index,
                sequenceIndex,
                sequencesRanges,
                sequencesLengths,
                updatedRange);

        // then
        String expected =
                """
                        SEQUENCE_RANGE_CORRECTION_WHEN_MARKING_FIELDS_IN_COLUMN: column=2
                        sequenceIndex=1
                        sequencesRanges=[[1, 4], [6, 11], [13, 14]]
                        sequencesLengths=[4, 5, 2]
                        updatedRange=[6, 10]
                        """;
        assertThat(log).isEqualTo(expected);
    }

    @Test
    @DisplayName("Convert example log to test arguments - o08007  column 2")
    void shouldConvertGeneratedLogToTestArgumentsColumnCase() {
        // given
        String generatedLog =
                """
                        SEQUENCE_RANGE_CORRECTION_WHEN_MARKING_FIELDS_IN_COLUMN: column=2
                        sequenceIndex=1
                        sequencesRanges=[[1, 4], [6, 11], [13, 14]]
                        sequencesLengths=[4, 5, 2]
                        updatedRange=[6, 10]
                        """;

        // when
        String convertedLog = SequenceRangeCorrectionWhenMarkingFieldsLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro08007"
        );

        // then
        String expected = """
                Arguments.of("o08007 / column=2 - sequences range correction when marking fields",
                    1,
                    new ArrayList<>(List.of(new ArrayList<>(List.of(1, 4)), new ArrayList<>(List.of(6, 11)), new ArrayList<>(List.of(13, 14)))),
                    List.of(4, 5, 2),
                    new ArrayList<>(List.of(6, 10))
                )""";
        AssertionsForClassTypes.assertThat(convertedLog).isEqualTo(expected);
    }
}