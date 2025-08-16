package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.util;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.rangeInsideAnotherRange;
import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.rangeLength;

@UtilityClass
public class NonogramLogicUtils {

    public static boolean colouredSequenceInRowIsValid(List<Integer> colouredSequence,
                                                       List<Integer> rowSequencesLengths,
                                                       List<List<Integer>> rowSequencesRanges) {

        for (int seqNo = 0; seqNo < rowSequencesLengths.size(); seqNo++) {
            if (rangeInsideAnotherRange(colouredSequence, rowSequencesRanges.get(seqNo)) &&
                    rangeLength(colouredSequence) <= rowSequencesLengths.get(seqNo)) {
                return true;
            }
        }

        return false;
    }

    public static boolean colouredSequenceInColumnIsValid(List<Integer> colouredSequence,
                                                          List<Integer> columnSequencesLengths,
                                                          List<List<Integer>> columnSequencesRanges) {

        for (int seqNo = 0; seqNo < columnSequencesLengths.size(); seqNo++) {
            if (rangeInsideAnotherRange(colouredSequence, columnSequencesRanges.get(seqNo)) &&
                    rangeLength(colouredSequence) <= columnSequencesLengths.get(seqNo)) {
                return true;
            }
        }

        return false;
    }
}
