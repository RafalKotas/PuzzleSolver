package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.NonogramColumnLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.NonogramRowLogic;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.common.ArrayUtils.rangeInsideAnotherRange;
import static com.puzzlesolverappbackend.puzzleAppFileManager.common.ArrayUtils.rangeLength;

@UtilityClass
public class NonogramLogicUtils {

    public static boolean colouredSequenceInRowIsValid(List<Integer> colouredSequence, int rowIdx,
                                                                     NonogramRowLogic nonogramRowLogic) {
        List<Integer> rowSequencesLengths = nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);

        for (int seqNo = 0; seqNo < rowSequencesLengths.size(); seqNo++) {
            if (rangeInsideAnotherRange(colouredSequence, rowSequencesRanges.get(seqNo)) &&
                    rangeLength(colouredSequence) <= rowSequencesLengths.get(seqNo)) {
                return true;
            }
        }

        return false;
    }

    public static boolean colouredSequenceInColumnIsValid(List<Integer> colouredSequence, int columnIdx,
                                                       NonogramColumnLogic nonogramColumnLogic) {
        List<Integer> columnSequencesLengths = nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx);

        for (int seqNo = 0; seqNo < columnSequencesLengths.size(); seqNo++) {
            if (rangeInsideAnotherRange(colouredSequence, columnSequencesRanges.get(seqNo)) &&
                    rangeLength(colouredSequence) <= columnSequencesLengths.get(seqNo)) {
                return true;
            }
        }

        return false;
    }
}
