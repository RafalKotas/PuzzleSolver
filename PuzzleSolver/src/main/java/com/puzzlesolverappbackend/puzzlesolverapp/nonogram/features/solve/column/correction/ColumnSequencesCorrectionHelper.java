package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.correction;

public interface ColumnSequencesCorrectionHelper {
    void correctColumnSequencesRanges(int columnIdx);
    void correctColumnSequencesRangesWhenMetColouredField(int columnIdx);
    void correctColumnSequencesRangesIfXOnWay(int columnIdx);
    void correctColumnSequencesRangesWhenMatchingFieldsToSequences(int columnIdx);
    void correctColumnSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(int columnIdx);
}
