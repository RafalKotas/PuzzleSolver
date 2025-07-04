package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.features.solve.column;

public interface ColumnColouringHelper {
    void colourOverlappingFieldsInColumn(int columnIdx);
    void colourFieldsInColumnIfXWouldForceTooLongColouredFieldsSequence(int columnIdx);
    void extendColouredFieldsNearXToMaximumPossibleLengthInColumn(int columnIdx);
}
