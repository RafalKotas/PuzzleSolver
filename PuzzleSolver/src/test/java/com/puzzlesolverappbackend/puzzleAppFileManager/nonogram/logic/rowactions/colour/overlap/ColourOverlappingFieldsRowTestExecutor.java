package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap.ColourOverlappingFieldsRowTestBase.assertOverlappingInRow;

public interface ColourOverlappingFieldsRowTestExecutor {

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideTestCasesForOverlappingInRow")
    default void shouldColourOverlappingFields(
            String testLabel,
            List<String> initialRowState,
            List<List<Integer>> rowSequenceRanges,
            List<Integer> rowSequenceLengths,
            List<String> expectedRowState
    ) {
        assertOverlappingInRow(
                testLabel, initialRowState, rowSequenceRanges, rowSequenceLengths, expectedRowState
        );
    }

    static Stream<Arguments> provideTestCasesForOverlappingInRow() {
        throw new UnsupportedOperationException("Each implementing class must override this method.");
    }
}
