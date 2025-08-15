package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SequencesRangesCorrectionLogHelperTest {

    @Test
    @DisplayName("SequencesRangesCorrectionLogHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<SequencesRangesCorrectionLogHelper> constructor = SequencesRangesCorrectionLogHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("SequencesRangesCorrectionLogHelper - generate example log - o07942")
    void shouldGenerateLogWhenAtLeastOneSequenceIsCorrected() {
        // given
        boolean isRow = true;
        int index = 7;
        List<Integer> sequencesLengths = List.of(2, 2, 1);
        List<Integer> excludedFields = List.of();
        List<Integer> excludedSequencesIds = List.of();
        List<List<Integer>> initialRanges = List.of(
                List.of(0, 14), List.of(3, 16), List.of(18, 18)
        );
        List<List<Integer>> updatedRanges = List.of(
                List.of(0, 13), List.of(3, 16), List.of(18, 18)
        );

        // when
        String actual = SequencesRangesCorrectionLogHelper.generateLog(
                isRow,
                index,
                sequencesLengths,
                excludedFields,
                excludedSequencesIds,
                initialRanges,
                updatedRanges
        );

        // then
        String expected =
                "ROW_SEQUENCES_RANGES_CORRECTION: row=7\n" +
                        "sequencesLengths=[2, 2, 1]\n" +
                        "excludedFields=[]\n" +
                        "excludedSequencesIndexes=[]\n" +
                        "initialRanges=[[0, 14], [3, 16], [18, 18]]\n" +
                        "updatedRanges=[[0, 13], [3, 16], [18, 18]]\n";

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("SequencesRangesCorrectionLogHelper - convert example log to test arguments - o07942")
    void shouldConvertGeneratedLogToTestArguments() {
        // given
        String generatedLog =
                "ROW_SEQUENCES_RANGES_CORRECTION: row=7\n" +
                        "sequencesLengths=[2, 2, 1]\n" +
                        "excludedFields=[]\n" +
                        "excludedSequencesIndexes=[]\n" +
                        "initialRanges=[[0, 14], [3, 16], [18, 18]]\n" +
                        "updatedRanges=[[0, 13], [3, 16], [18, 18]]\n";

        // when
        String convertedLog = SequencesRangesCorrectionLogHelper.convertLogToTestArguments(
                generatedLog,
                "ro07942"
        );

        // then
        String expected = "Arguments.of(\"o07942 / Row=7 - sequences range correction\",\n" +
                "    List.of(2, 2, 1),\n" +
                "    new ArrayList<>(List.of()),\n" +
                "    new ArrayList<>(List.of()),\n" +
                "    new ArrayList<>(List.of(new ArrayList<>(List.of(0, 14)), new ArrayList<>(List.of(3, 16)), new ArrayList<>(List.of(18, 18)))),\n" +
                "    List.of(List.of(0, 13), List.of(3, 16), List.of(18, 18)))\n" +
                ")";
        assertThat(convertedLog).isEqualTo(expected);
    }
}