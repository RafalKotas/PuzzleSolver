package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.extend.totop;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.NonogramColumnLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.extend.ExtendColouredFieldsColumnTestBase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class ExtendColouredFieldsToTopColumnTestBase extends ExtendColouredFieldsColumnTestBase {

    protected static void assertExtensionTop(
            String testLabel,
            List<String> initialColumnState,
            List<List<Integer>> columnSequenceRanges,
            List<Integer> columnSequenceLengths,
            List<String> expectedColumnState
    ) {
        NonogramColumnLogic nonogramColumnLogic = prepareColumnLogic(initialColumnState, columnSequenceRanges, columnSequenceLengths);
        nonogramColumnLogic.extendColouredFieldsToTopNearXToMaximumPossibleLengthInColumn(0);
        List<String> actualColumn = nonogramColumnLogic.getNonogramBoardColumn(0);
        assertEquals(expectedColumnState, actualColumn, "Mismatch in column state for: " + testLabel);
    }
}
