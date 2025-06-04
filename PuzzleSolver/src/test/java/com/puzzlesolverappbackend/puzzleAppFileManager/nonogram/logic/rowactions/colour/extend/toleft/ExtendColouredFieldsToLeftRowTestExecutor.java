package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.extend.toleft;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

public interface ExtendColouredFieldsToLeftRowTestExecutor {

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideTestCasesForExtendLeft")
    default void shouldExtendColouredFieldsToLeftNearXCorrectly(
            String testLabel,
            List<String> initialRowState,
            List<List<Integer>> rowSequenceRanges,
            List<Integer> rowSequenceLengths,
            List<String> expectedRowState
    ) {
        ExtendColouredFieldsToLeftRowTestBase.assertExtensionLeft(
                testLabel, initialRowState, rowSequenceRanges, rowSequenceLengths, expectedRowState
        );
    }

    static Stream<Arguments> provideTestCasesForExtendLeft() {
        throw new UnsupportedOperationException("Each implementing class must override this method.");
    }
}

