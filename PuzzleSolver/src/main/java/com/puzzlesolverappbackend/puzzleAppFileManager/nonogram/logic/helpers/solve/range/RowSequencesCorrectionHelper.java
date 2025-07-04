package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve.range;

public interface RowSequencesCorrectionHelper {
    void correctRowSequencesRanges(int rowIdx);
    void correctRowSequencesRangesWhenMetColouredField(int rowIdx);
    void correctRowSequencesRangesIfXOnWay(int rowIdx, boolean changeLogicDetails);
    void correctRowSequencesRangesWhenMatchingFieldsToSequences(int rowIdx);
    void correctRowSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(int rowIdx);
}
