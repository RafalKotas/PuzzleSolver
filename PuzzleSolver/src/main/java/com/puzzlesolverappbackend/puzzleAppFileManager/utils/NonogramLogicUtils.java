package com.puzzlesolverappbackend.puzzleAppFileManager.utils;

import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.NonogramRowLogic;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.NonogramLogicService.rangeLength;
import static com.puzzlesolverappbackend.puzzleAppFileManager.utils.ArrayUtils.rangeInsideAnotherRange;

@UtilityClass
public class NonogramLogicUtils {

    public static boolean colouredSequenceInRowIsValid(List<Integer> colouredSequence, int rowIdx,
                                                                     NonogramRowLogic nonogramRowLogic) {
        List<Integer> rowSequencesLengths = nonogramRowLogic.getRowsSequences().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);

        if (rowIdx == 10 && nonogramRowLogic.getNonogramSolutionBoard().get(10).get(6).equals("O")) {
            System.out.println("abc");
        }

        for(int seqNo = 0; seqNo < rowSequencesLengths.size(); seqNo++) {
            if (rangeInsideAnotherRange(colouredSequence, rowSequencesRanges.get(seqNo)) &&
                    rangeLength(colouredSequence) <= rowSequencesLengths.get(seqNo)) {
                return true;
            }
        }

        return false;
    }
}
