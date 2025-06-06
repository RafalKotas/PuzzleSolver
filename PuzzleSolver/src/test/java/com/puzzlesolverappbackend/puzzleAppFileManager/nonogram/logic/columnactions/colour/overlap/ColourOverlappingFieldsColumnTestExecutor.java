package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap.ColourOverlappingFieldsColumnTestBase.assertOverlappingInColumn;

public interface ColourOverlappingFieldsColumnTestExecutor {

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideTestCasesForOverlappingInColumn")
    default void shouldColourOverlappingFields(
            String testLabel,
            List<String> initialColumnState,
            List<List<Integer>> columnSequenceRanges,
            List<Integer> columnSequenceLengths,
            List<String> expectedColumnState
    ) {
        assertOverlappingInColumn(
                testLabel, initialColumnState, columnSequenceRanges, columnSequenceLengths, expectedColumnState
        );
    }

    static Stream<Arguments> provideTestCasesForOverlappingInColumn() {
        throw new UnsupportedOperationException("Each implementing class must override this method.");
    }
}
