package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.extend.toright;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

public interface ExtendColouredFieldsToRightRowTestExecutor {

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideTestCasesForExtendRight")
    default void shouldExtendColouredFieldsToRightNearXCorrectly(
            String testLabel,
            List<String> initialRowState,
            List<List<Integer>> rowSequenceRanges,
            List<Integer> rowSequenceLengths,
            List<String> expectedRowState
    ) {
        ExtendColouredFieldsToRightRowTestBase.assertExtensionRight(
                testLabel, initialRowState, rowSequenceRanges, rowSequenceLengths, expectedRowState
        );
    }

    static Stream<Arguments> provideTestCasesForExtendRight() {
        throw new UnsupportedOperationException("Each implementing class must override this method.");
    }
}
