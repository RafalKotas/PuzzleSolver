package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.colour;

public interface ColumnColouringHelper {
    void colourOverlappingFieldsInColumn(int columnIdx);
    void colourFieldsInColumnIfXWouldForceTooLongColouredFieldsSequence(int columnIdx);
    void extendColouredFieldsNearXToMaximumPossibleLengthInColumn(int columnIdx);
}
