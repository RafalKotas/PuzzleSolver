package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions;

public interface ColumnActions {
    void correctColumnSequencesRanges(int columnIdx);
    void correctColumnSequencesRangesWhenMetColouredField(int columnIdx);
    void correctColumnSequencesRangesIfXOnWay(int columnIdx);
    void correctColumnSequencesRangesWhenMatchingFieldsToSequences(int columnIdx);
    void correctColumnSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(int columnIdx);
    void colourOverlappingFieldsInColumn(int columnIdx);
    void colourFieldsInColumnIfXWouldForceTooLongColouredFieldsSequence(int columnIdx);
    void extendColouredFieldsNearXToMaximumPossibleLengthInColumn(int columnIdx);
    void placeXsColumnAtUnreachableFields(int columnIdx);
    void placeXsAroundLongestSequencesInColumn(int columnIdx);
    void placeXsColumnAtTooShortEmptySequences(int columnIdx);
    void placeXsColumnIfOWillMergeNearFieldsToTooLongColouredSequence(int columnIdx);
    void placeXsColumnIfONearXWillBeginTooLongPossibleColouredSequence(int columnIdx);
    void preventExtendingColouredSequenceToExcessLengthInColumn(int columnIdx);
    void markAvailableFieldsInColumn(int columnIdx);
}
