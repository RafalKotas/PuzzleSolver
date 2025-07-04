package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.features.solve.row;

public interface RowColouringHelper {
    void colourOverlappingFieldsInRow(int rowIdx);
    void colourFieldsIfXWouldForceTooLongColouredFieldsSequence(int rowIdx);
    void extendColouredFieldsNearXToMaximumPossibleLengthInRow(int rowIdx);
}
