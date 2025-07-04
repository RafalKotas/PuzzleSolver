package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.features.solve.column;

public interface ColumnXPlacementHelper {
    void placeXsColumnAtUnreachableFields(int columnIdx);
    void placeXsAroundLongestSequencesInColumn(int columnIdx);
    void placeXsColumnAtTooShortEmptySequences(int columnIdx);
    void placeXsColumnIfOWillMergeNearFieldsToTooLongColouredSequence(int columnIdx);
    void placeXsColumnIfONearXWillBeginTooLongPossibleColouredSequence(int columnIdx);
}
