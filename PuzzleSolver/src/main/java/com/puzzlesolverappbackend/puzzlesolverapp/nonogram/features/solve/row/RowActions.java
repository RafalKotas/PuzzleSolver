package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row;

public interface RowActions {
    void correctRowSequencesRanges(int rowIdx);
    void correctRowSequencesRangesWhenMetColouredField(int rowIdx);
    void correctRowSequencesRangesIfXOnWay(int rowIdx, boolean changeLogicDetails);
    void correctRowSequencesRangesWhenMatchingFieldsToSequences(int rowIdx);
    void correctRowSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(int rowIdx);
    void colourOverlappingFieldsInRow(int rowIdx);
    void colourFieldsIfInRowXWouldForceTooLongColouredFieldsSequence(int rowIdx);
    void extendColouredFieldsNearXToMaximumPossibleLengthInRow(int rowIdx);
    //  colour fields in row if X causes assignment conflict
    void placeXsRowAtUnreachableFields(int rowIdx);
    void placeXsAroundLongestSequencesInRow(int rowIdx);
    void placeXsRowAtTooShortEmptySequences(int rowIdx);
    void placeXsRowIfOWillMergeNearFieldsToTooLongColouredSequence(int rowIdx);
    void placeXsRowIfONearXWillBeginTooLongPossibleColouredSequence(int rowIdx);
    void preventExtendingColouredSequenceToExcessLengthInRow(int rowIdx);
    void markAvailableFieldsInRow(int rowIdx);
}
