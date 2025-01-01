package com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram;

import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.NonogramLogic;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

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

    public static boolean sequencesRangesEqual(List<List<Integer>> sequencesRanges_A, List<List<Integer>> sequenceRanges_B) {
        Iterator<List<Integer>> sequencesRanges_AIterator = sequencesRanges_A.iterator();
        Iterator<List<Integer>> sequenceRanges_BIterator = sequenceRanges_B.iterator();

        List<Integer> sequenceRange_A;
        List<Integer> sequenceRange_B;

        while(sequencesRanges_AIterator.hasNext() && sequenceRanges_BIterator.hasNext()) {
            sequenceRange_A = sequencesRanges_AIterator.next();
            sequenceRange_B = sequenceRanges_BIterator.next();
            if (!rangesEqual(sequenceRange_A, sequenceRange_B)) {
                return false;
            }
        }

        return true;
    }

    public static boolean solutionBoardsDiffers(List<List<String>> solutionBoard_A, List<List<String>> solutionBoard_B) {
        Iterator<List<String>> solutionBoard_ARowsIterator = solutionBoard_A.iterator();
        Iterator<List<String>> solutionBoard_BRowsIterator = solutionBoard_B.iterator();

        List<String> solutionBoard_ARow;
        List<String> solutionBoard_BRow;

        while(solutionBoard_ARowsIterator.hasNext() && solutionBoard_BRowsIterator.hasNext()) {
            solutionBoard_ARow = solutionBoard_ARowsIterator.next();
            solutionBoard_BRow = solutionBoard_BRowsIterator.next();

            if (solutionBoardRowsDiffers(solutionBoard_ARow, solutionBoard_BRow)) {
                return true;
            }
        }

        return false;
    }

    public static boolean solutionBoardRowsDiffers(List<String> solutionBoard_ARow, List<String> solutionBoard_BRow) {
        Iterator<String> solutionBoard_ARowIterator = solutionBoard_ARow.iterator();
        Iterator<String> solutionBoard_BRowIterator = solutionBoard_BRow.iterator();

        String rowField_A;
        String rowField_B;

        while(solutionBoard_ARowIterator.hasNext() && solutionBoard_BRowIterator.hasNext()) {
            rowField_A = solutionBoard_ARowIterator.next();
            rowField_B = solutionBoard_BRowIterator.next();
            if (!rowField_A.equals(rowField_B)) {
                return true;
            }
        }

        return false;
    }

    public static boolean sequenceIndexesNotToIncludeAdded(NonogramLogic logicBeforeActionsMade, NonogramLogic logicAfterActionsMade) {
        boolean sequencesIndexesSizeNotToIncludeAddedInRowsChanged = sequenceIndexesNotToIncludeAddedOneDimension(
                logicBeforeActionsMade.getRowsSequencesIdsNotToInclude(), logicAfterActionsMade.getRowsSequencesIdsNotToInclude());
        boolean sequencesIndexesSizeNotToIncludeAddedInColumnsChanged = sequenceIndexesNotToIncludeAddedOneDimension(
                logicBeforeActionsMade.getColumnsSequencesIdsNotToInclude(), logicAfterActionsMade.getColumnsSequencesIdsNotToInclude());

        return sequencesIndexesSizeNotToIncludeAddedInRowsChanged || sequencesIndexesSizeNotToIncludeAddedInColumnsChanged;
    }

    public static boolean sequenceIndexesNotToIncludeAddedOneDimension(List<List<Integer>> sequencesIdsNotToIncludeBefore, List<List<Integer>> sequencesIdsNotToIncludeAfter) {

        Iterator<List<Integer>> sequencesIdsNotToIncludeBeforeActionsMadeIterator = sequencesIdsNotToIncludeBefore.iterator();
        Iterator<List<Integer>> sequencesIdsNotToIncludeAfterActionsMadeIterator = sequencesIdsNotToIncludeAfter.iterator();

        List<Integer> sequencesIdsNotToIncludeBeforeActionsMade;
        List<Integer> sequencesIdsNotToIncludeAfterActionsMade;

        while(sequencesIdsNotToIncludeBeforeActionsMadeIterator.hasNext() && sequencesIdsNotToIncludeAfterActionsMadeIterator.hasNext()) {
            sequencesIdsNotToIncludeBeforeActionsMade = sequencesIdsNotToIncludeBeforeActionsMadeIterator.next();
            sequencesIdsNotToIncludeAfterActionsMade = sequencesIdsNotToIncludeAfterActionsMadeIterator.next();

            // ids not to include differs only if size of Lists differs (ids are sorted ascending)
            if (!(sequencesIdsNotToIncludeBeforeActionsMade.size() == sequencesIdsNotToIncludeAfterActionsMade.size()) ) {
                return true;
            }
        }

        return false;
    }

    /***
     * range_A in format [A_1, A_2]
     * range_B in format [B_1, B_2]
     ***/
    public static boolean rangesEqual(List<Integer> range_A, List<Integer> range_B) {
        return Objects.equals(range_A.get(0), range_B.get(0)) && Objects.equals(range_A.get(1), range_B.get(1));
    }
}
