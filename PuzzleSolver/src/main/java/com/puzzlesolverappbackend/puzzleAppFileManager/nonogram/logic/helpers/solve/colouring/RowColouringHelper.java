package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve.colouring;

public interface RowColouringHelper {
    void colourOverlappingFieldsInRow(int rowIdx);
    void colourFieldsIfXWouldForceTooLongColouredFieldsSequence(int rowIdx);
    void extendColouredFieldsNearXToMaximumPossibleLengthInRow(int rowIdx);
}
