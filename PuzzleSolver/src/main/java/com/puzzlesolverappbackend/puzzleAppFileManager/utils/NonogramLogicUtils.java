package com.puzzlesolverappbackend.puzzleAppFileManager.utils;

import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.NonogramColumnLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.NonogramRowLogic;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.utils.ArrayUtils.rangeInsideAnotherRange;
import static com.puzzlesolverappbackend.puzzleAppFileManager.utils.ArrayUtils.rangeLength;

@UtilityClass
public class NonogramLogicUtils {

    public static boolean colouredSequenceInRowIsValid(List<Integer> colouredSequence, int rowIdx,
                                                                     NonogramRowLogic nonogramRowLogic) {
        List<Integer> rowSequencesLengths = nonogramRowLogic.getRowsSequences().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);

        for(int seqNo = 0; seqNo < rowSequencesLengths.size(); seqNo++) {
            if (rangeInsideAnotherRange(colouredSequence, rowSequencesRanges.get(seqNo)) &&
                    rangeLength(colouredSequence) <= rowSequencesLengths.get(seqNo)) {
                return true;
            }
        }

        return false;
    }

    public static boolean colouredSequenceInColumnIsValid(List<Integer> colouredSequence, int columnIdx,
                                                       NonogramColumnLogic nonogramColumnLogic) {
        List<Integer> columnSequencesLengths = nonogramColumnLogic.getColumnsSequences().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx);

        for(int seqNo = 0; seqNo < columnSequencesLengths.size(); seqNo++) {
            if (rangeInsideAnotherRange(colouredSequence, columnSequencesRanges.get(seqNo)) &&
                    rangeLength(colouredSequence) <= columnSequencesLengths.get(seqNo)) {
                return true;
            }
        }

        return false;
    }
}
