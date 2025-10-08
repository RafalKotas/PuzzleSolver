package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.util;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Objects;

@UtilityClass
public class NonogramParametersComparatorHelper {

    public static boolean sequencesRangesEqual(List<List<Integer>> firstSequencesRanges, List<List<Integer>> secondSequencesRanges) {
        if (firstSequencesRanges.size() != secondSequencesRanges.size()) {
            return false;
        }

        for (int i = 0; i < firstSequencesRanges.size(); i++) {
            if (rangesNotEqual(firstSequencesRanges.get(i), secondSequencesRanges.get(i))) {
                return false;
            }
        }

        return true;
    }

    /***
     * range_A in format [A_1, A_2]
     * range_B in format [B_1, B_2]
     ***/
    public static boolean rangesNotEqual(List<Integer> firstRange, List<Integer> secondRange) {
        boolean rangesAreEqual = Objects.equals(firstRange.get(0), secondRange.get(0)) && Objects.equals(firstRange.get(1), secondRange.get(1));
        return !rangesAreEqual;
    }
}
