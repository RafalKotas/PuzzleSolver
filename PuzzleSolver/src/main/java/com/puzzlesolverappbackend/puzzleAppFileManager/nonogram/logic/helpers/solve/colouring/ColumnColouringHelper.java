package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve.colouring;

public interface ColumnColouringHelper {
    void colourOverlappingFieldsInColumn(int columnIdx);
    void colourFieldsInColumnIfXWouldForceTooLongColouredFieldsSequence(int columnIdx);
    void extendColouredFieldsNearXToMaximumPossibleLengthInColumn(int columnIdx);
}
