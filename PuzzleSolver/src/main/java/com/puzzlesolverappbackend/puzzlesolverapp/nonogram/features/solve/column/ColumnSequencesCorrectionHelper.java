package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column;

public interface ColumnSequencesCorrectionHelper {
    void correctColumnSequencesRanges(int columnIdx);
    void correctColumnSequencesRangesWhenMetColouredField(int columnIdx);
    void correctColumnSequencesRangesIfXOnWay(int columnIdx, boolean changeLogicDetails);
    void correctColumnSequencesRangesWhenMatchingFieldsToSequences(int columnIdx);
    void correctColumnSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(int columnIdx);
}
