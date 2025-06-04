package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.extend.toright;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.NonogramRowLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.extend.ExtendColouredFieldsRowTestBase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class ExtendColouredFieldsToRightRowTestBase extends ExtendColouredFieldsRowTestBase {

    protected static void assertExtensionRight(
            String testLabel,
            List<String> initialRowState,
            List<List<Integer>> rowSequenceRanges,
            List<Integer> rowSequenceLengths,
            List<String> expectedRowState
    ) {
        NonogramRowLogic nonogramRowLogic = prepareRowLogic(initialRowState, rowSequenceRanges, rowSequenceLengths);
        nonogramRowLogic.extendColouredFieldsToRightNearXToMaximumPossibleLengthInRow(0);
        List<String> actualRow = nonogramRowLogic.getNonogramSolutionBoard().get(0);
        assertEquals(expectedRowState, actualRow, "Mismatch in row state for: " + testLabel);
    }
}

