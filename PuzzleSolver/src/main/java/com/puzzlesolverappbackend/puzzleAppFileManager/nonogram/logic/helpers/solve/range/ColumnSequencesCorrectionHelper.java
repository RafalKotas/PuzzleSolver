package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve.range;

public interface ColumnSequencesCorrectionHelper {
    void correctColumnSequencesRanges(int columnIdx);
    void correctColumnSequencesRangesWhenMetColouredField(int columnIdx);
    void correctColumnSequencesRangesIfXOnWay(int columnIdx, boolean changeLogicDetails);
    void correctColumnSequencesRangesWhenMatchingFieldsToSequences(int columnIdx);
    void correctColumnSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(int columnIdx);
}
