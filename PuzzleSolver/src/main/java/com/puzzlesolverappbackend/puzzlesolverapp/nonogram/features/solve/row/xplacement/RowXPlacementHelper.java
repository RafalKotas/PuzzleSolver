package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row.xplacement;

public interface RowXPlacementHelper {
    void placeXsRowAtUnreachableFields(int rowIdx);
    void placeXsAroundLongestSequencesInRow(int rowIdx);
    void placeXsRowAtTooShortEmptySequences(int rowIdx);
    void placeXsRowIfOWillMergeNearFieldsToTooLongColouredSequence(int rowIdx);
    void placeXsRowIfONearXWillBeginTooLongPossibleColouredSequence(int rowIdx);
}
