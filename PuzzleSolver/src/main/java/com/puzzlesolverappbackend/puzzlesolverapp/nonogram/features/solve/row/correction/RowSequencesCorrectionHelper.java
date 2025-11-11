package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row.correction;

public interface RowSequencesCorrectionHelper {
    void correctRowSequencesRanges(int rowIdx);
    void correctRowSequencesRangesWhenMetColouredField(int rowIdx);
    void correctRowSequencesRangesIfXOnWay(int rowIdx);
    void correctRowSequencesRangesWhenMatchingFieldsToSequences(int rowIdx);
    void correctRowSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(int rowIdx);
}
