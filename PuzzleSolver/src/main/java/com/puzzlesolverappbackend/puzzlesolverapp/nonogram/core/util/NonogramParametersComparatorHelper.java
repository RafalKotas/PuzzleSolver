package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.util;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import lombok.experimental.UtilityClass;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class NonogramParametersComparatorHelper {

    public static boolean sequencesRangesAfterActionsMadeDiffers(NonogramLogic logicBeforeActionsMade, NonogramLogic logicAfterActionsMade) {
        boolean sequencesRangesAfterActionsMadeDiffersInRows = sequencesRangesAfterActionsMadeDiffersInSection(
                logicBeforeActionsMade.getRowsSequencesRanges(),
                logicAfterActionsMade.getRowsSequencesRanges()
        );
        boolean sequencesRangesAfterActionsMadeDiffersInColumns = sequencesRangesAfterActionsMadeDiffersInSection(
                logicBeforeActionsMade.getColumnsSequencesRanges(),
                logicAfterActionsMade.getColumnsSequencesRanges()
        );
        return sequencesRangesAfterActionsMadeDiffersInRows || sequencesRangesAfterActionsMadeDiffersInColumns;
    }

    public static boolean sequencesRangesAfterActionsMadeDiffersInSection(List<List<List<Integer>>> sequencesRangesBefore, List<List<List<Integer>>> sequencesRangesAfter) {

        Iterator<List<List<Integer>>> logicBeforeActionsMadeRowsSequencesRangesIterator = sequencesRangesBefore.iterator();
        Iterator<List<List<Integer>>> logicAfterActionsMadeRowsSequencesRangesIterator = sequencesRangesAfter.iterator();

        while(logicBeforeActionsMadeRowsSequencesRangesIterator.hasNext() && logicAfterActionsMadeRowsSequencesRangesIterator.hasNext()) {

            List<List<Integer>> logicBeforeActionsMadeRowSequencesRanges = logicBeforeActionsMadeRowsSequencesRangesIterator.next();
            List<List<Integer>> logicAfterActionsMadeRowSequencesRanges = logicAfterActionsMadeRowsSequencesRangesIterator.next();

            if (!sequencesRangesEqual(logicBeforeActionsMadeRowSequencesRanges, logicAfterActionsMadeRowSequencesRanges)) {
                return true;
            }
        }

        return false;
    }

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
        return !Objects.equals(firstRange.get(0), secondRange.get(0)) || !Objects.equals(firstRange.get(1), secondRange.get(1));
    }
}
