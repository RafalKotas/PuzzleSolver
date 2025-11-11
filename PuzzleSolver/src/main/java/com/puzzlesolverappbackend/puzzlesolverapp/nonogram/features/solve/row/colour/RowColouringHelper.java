package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row.colour;

public interface RowColouringHelper {
    void colourOverlappingFieldsInRow(int rowIdx);
    void colourFieldsInRowIfXWouldForceTooLongColouredFieldsSequence(int rowIdx);
    void extendColouredFieldsNearXToMaximumPossibleLengthInRow(int rowIdx);
}
