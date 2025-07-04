package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve.xplacement;

public interface ColumnXPlacementHelper {
    void placeXsColumnAtUnreachableFields(int columnIdx);
    void placeXsAroundLongestSequencesInColumn(int columnIdx);
    void placeXsColumnAtTooShortEmptySequences(int columnIdx);
    void placeXsColumnIfOWillMergeNearFieldsToTooLongColouredSequence(int columnIdx);
    void placeXsColumnIfONearXWillBeginTooLongPossibleColouredSequence(int columnIdx);
}
