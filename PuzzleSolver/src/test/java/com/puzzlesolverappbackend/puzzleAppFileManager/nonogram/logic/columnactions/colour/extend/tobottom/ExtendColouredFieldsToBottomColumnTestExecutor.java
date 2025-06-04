package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.extend.tobottom;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

public interface ExtendColouredFieldsToBottomColumnTestExecutor {

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideTestCasesForExtendBottom")
    default void shouldExtendColouredFieldsToBottomNearXCorrectly(
            String testLabel,
            List<String> initialColumnState,
            List<List<Integer>> columnSequenceRanges,
            List<Integer> columnSequenceLengths,
            List<String> expectedColumnState
    ) {
        ExtendColouredFieldsToBottomColumnTestBase.assertExtensionBottom(
                testLabel, initialColumnState, columnSequenceRanges, columnSequenceLengths, expectedColumnState
        );
    }

    static Stream<Arguments> provideTestCasesForExtendBottom() {
        throw new UnsupportedOperationException("Each implementing class must override this method.");
    }
}
