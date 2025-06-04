package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.extend.toleft;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.NonogramRowLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.extend.ExtendColouredFieldsRowTestBase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class ExtendColouredFieldsToLeftRowTestBase extends ExtendColouredFieldsRowTestBase {

    protected static void assertExtensionLeft(
            String testLabel,
            List<String> initialRowState,
            List<List<Integer>> rowSequenceRanges,
            List<Integer> rowSequenceLengths,
            List<String> expectedRowState
    ) {
        NonogramRowLogic logic = prepareRowLogic(initialRowState, rowSequenceRanges, rowSequenceLengths);
        logic.extendColouredFieldsToLeftNearXToMaximumPossibleLengthInRow(0);
        List<String> actualRow = logic.getNonogramSolutionBoard().get(0);
        assertEquals(expectedRowState, actualRow, "Mismatch in row state for: " + testLabel);
    }
}

