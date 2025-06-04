package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.extend.totop;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

public interface ExtendColouredFieldsToTopColumnTestExecutor {

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideTestCasesForExtendTop")
    default void shouldExtendColouredFieldsToTopNearXCorrectly(
            String testLabel,
            List<String> initialColumnState,
            List<List<Integer>> columnSequenceRanges,
            List<Integer> columnSequenceLengths,
            List<String> expectedColumnState
    ) {
        ExtendColouredFieldsToTopColumnTestBase.assertExtensionTop(
                testLabel, initialColumnState, columnSequenceRanges, columnSequenceLengths, expectedColumnState
        );
    }

    static Stream<Arguments> provideTestCasesForExtendTop() {
        throw new UnsupportedOperationException("Each implementing class must override this method.");
    }
}
