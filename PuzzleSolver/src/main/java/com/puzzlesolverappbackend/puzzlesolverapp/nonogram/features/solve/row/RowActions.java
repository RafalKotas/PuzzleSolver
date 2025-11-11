package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row;

public interface RowActions {

    @SuppressWarnings("unused")
    void correctRowSequencesRanges(int rowIdx);

    @SuppressWarnings("unused")
    void correctRowSequencesRangesWhenMetColouredField(int rowIdx);

    @SuppressWarnings("unused")
    void correctRowSequencesRangesIfXOnWay(int rowIdx);

    @SuppressWarnings("unused")
    void correctRowSequencesRangesWhenMatchingFieldsToSequences(int rowIdx);

    @SuppressWarnings("unused")
    void correctRowSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(int rowIdx);

    @SuppressWarnings("unused")
    void colourOverlappingFieldsInRow(int rowIdx);

    @SuppressWarnings("unused")
    void colourFieldsIfInRowXWouldForceTooLongColouredFieldsSequence(int rowIdx);

    @SuppressWarnings("unused")
    void extendColouredFieldsNearXToMaximumPossibleLengthInRow(int rowIdx);

//    void colourFieldsInRowIfXCausesAssignmentConflict(int rowIdx);

    @SuppressWarnings("unused")
    void placeXsRowAtUnreachableFields(int rowIdx);

    @SuppressWarnings("unused")
    void placeXsAroundLongestSequencesInRow(int rowIdx);

    @SuppressWarnings("unused")
    void placeXsRowAtTooShortEmptySequences(int rowIdx);

    @SuppressWarnings("unused")
    void placeXsRowIfOWillMergeNearFieldsToTooLongColouredSequence(int rowIdx);

    @SuppressWarnings("unused")
    void placeXsRowIfONearXWillBeginTooLongPossibleColouredSequence(int rowIdx);

    @SuppressWarnings("unused")
    void placeXsRowIfColouringFieldWillCauseAssignmentConflict(int rowIdx);

    @SuppressWarnings("unused")
    void preventExtendingColouredSequenceToExcessLengthInRow(int rowIdx);

    @SuppressWarnings("unused")
    void markAvailableFieldsInRow(int rowIdx);
}
