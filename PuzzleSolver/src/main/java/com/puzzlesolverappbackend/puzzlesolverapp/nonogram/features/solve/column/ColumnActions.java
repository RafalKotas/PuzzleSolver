package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column;

/**
 * Defines full contract of all column-related logic steps used by the solver.
 * Implemented by NonogramColumnLogic.
 */
public interface ColumnActions {

    @SuppressWarnings("unused")
    void correctColumnSequencesRanges(int columnIdx);

    @SuppressWarnings("unused")
    void correctColumnSequencesRangesWhenMetColouredField(int columnIdx);

    @SuppressWarnings("unused")
    void correctColumnSequencesRangesIfXOnWay(int columnIdx);

    @SuppressWarnings("unused")
    void correctColumnSequencesRangesWhenMatchingFieldsToSequences(int columnIdx);

    @SuppressWarnings("unused")
    void correctColumnSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(int columnIdx);

    @SuppressWarnings("unused")
    void colourOverlappingFieldsInColumn(int columnIdx);

    @SuppressWarnings("unused")
    void extendColouredFieldsNearXToMaximumPossibleLengthInColumn(int columnIdx);

    @SuppressWarnings("unused")
    void colourFieldsInColumnIfXWouldForceTooLongColouredFieldsSequence(int columnIdx);

//    void colourFieldsInColumnIfXCausesAssignmentConflict(int columnIdx);

    @SuppressWarnings("unused")
    void placeXsColumnAtUnreachableFields(int columnIdx);

    @SuppressWarnings("unused")
    void placeXsAroundLongestSequencesInColumn(int columnIdx);

    @SuppressWarnings("unused")
    void placeXsColumnAtTooShortEmptySequences(int columnIdx);

    @SuppressWarnings("unused")
    void placeXsColumnIfOWillMergeNearFieldsToTooLongColouredSequence(int columnIdx);

    @SuppressWarnings("unused")
    void placeXsColumnIfONearXWillBeginTooLongPossibleColouredSequence(int columnIdx);

    @SuppressWarnings("unused")
    void placeXsColumnIfColouringFieldWillCauseAssignmentConflict(int rowIdx);

    @SuppressWarnings("unused")
    void preventExtendingColouredSequenceToExcessLengthInColumn(int columnIdx);

    @SuppressWarnings("unused")
    void markAvailableFieldsInColumn(int columnIdx);
}

