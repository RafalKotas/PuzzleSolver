package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.Field;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.NonogramConstants.*;


@UtilityClass
public class NonogramBoardUtils {

    public static List<Integer> findColouredFieldsIndexesInRow(List<List<String>> solutionBoard, int rowIdx) {
        List<String> solutionBoardRow = solutionBoard.get(rowIdx);
        return IntStream.range(0, solutionBoardRow.size())
                .filter(columnIdx -> isFieldColoured(solutionBoard, new Field(rowIdx, columnIdx)))
                .boxed()
                .toList();
    }

    public static List<Integer> findColouredFieldsIndexesInColumn(List<List<String>> solutionBoard, int columnIdx) {
        List<String> solutionBoardColumn = getSolutionBoardColumn(solutionBoard, columnIdx);
        return IntStream.range(0, solutionBoardColumn.size())
                .filter(rowIdx -> isFieldColoured(solutionBoard, new Field(rowIdx, columnIdx)))
                .boxed()
                .toList();
    }

    public static List<List<Integer>> groupConsecutiveIndices(List<Integer> indices) {
        List<List<Integer>> result = new ArrayList<>();
        if (indices == null || indices.isEmpty()) {
            return result;
        }

        List<Integer> currentRange = new ArrayList<>();
        int colouredRangeStartIndex = indices.get(0);
        currentRange.add(colouredRangeStartIndex);

        for (int i = 1; i < indices.size(); i++) {
            if (indices.get(i) == indices.get(i - 1) + 1) {
                if (currentRange.size() == 1) {
                    currentRange.add(indices.get(i));
                } else {
                    currentRange.set(1, indices.get(i));
                }
            } else {
                if (currentRange.size() == 1) {
                    currentRange.add(currentRange.get(0));
                }
                result.add(new ArrayList<>(currentRange));
                currentRange.clear();
                currentRange.add(indices.get(i));
            }
        }
        if (currentRange.size() == 1) {
            currentRange.add(currentRange.get(0));
        }
        result.add(new ArrayList<>(currentRange));

        return result;
    }

    public List<List<List<Integer>>> createSequencesRangesWithColouredFieldAdded(List<List<Integer>> colouredSequences) {
        List<List<List<Integer>>> sequencesRangesWithColouredFieldAdded = new ArrayList<>();
        List<List<Integer>> currentRangesWithFieldAdded;

        for (List<Integer> colouredSequence : colouredSequences) {
            currentRangesWithFieldAdded = List.of(
                    List.of(colouredSequence.get(0) - 1, colouredSequence.get(1)),
                    List.of(colouredSequence.get(0), colouredSequence.get(1) + 1)
            );
            sequencesRangesWithColouredFieldAdded.add(currentRangesWithFieldAdded);
        }

        return sequencesRangesWithColouredFieldAdded;
    }

    public List<Integer> tryToMergeColouredSequenceWithPrevious(List<Integer> previousColouredSequenceRange,
                                                          List<Integer> colouredSequenceRangeWithColouredFieldAddedBefore) {
        if (previousColouredSequenceRange.get(1) + 1 == colouredSequenceRangeWithColouredFieldAddedBefore.get(0)) {
            return List.of(previousColouredSequenceRange.get(0), colouredSequenceRangeWithColouredFieldAddedBefore.get(1));
        } else {
            return colouredSequenceRangeWithColouredFieldAddedBefore;
        }
    }

    public List<Integer> tryToMergeColouredSequenceWithNext(List<Integer> colouredSequenceRangeWithColouredFieldAddedAtEnd,
                                                            List<Integer> nextColouredSequenceRange) {
        if (colouredSequenceRangeWithColouredFieldAddedAtEnd.get(1) + 1 == nextColouredSequenceRange.get(0)) {
            return List.of(colouredSequenceRangeWithColouredFieldAddedAtEnd.get(1), nextColouredSequenceRange.get(0));
        } else {
            return colouredSequenceRangeWithColouredFieldAddedAtEnd;
        }
    }

    /**
     * @param columnIdx - column index from solution board to select
     * @return selected column
     */
    public static List<String> getSolutionBoardColumn(List<List<String>> solutionBoard, int columnIdx) {
        return solutionBoard.stream()
                .map(row -> row.get(columnIdx))
                .toList();
    }

    public static boolean isFieldColoured(List<List<String>> solutionBoard, Field field) {
        return solutionBoard.get(field.getRowIdx()).get(field.getColumnIdx()).equals(COLOURED_FIELD);
    }

    public static boolean isFieldEmpty(List<List<String>> solutionBoard, Field field) {
        return solutionBoard.get(field.getRowIdx()).get(field.getColumnIdx()).equals(EMPTY_FIELD);
    }

    public static boolean isFieldWithX(List<List<String>> solutionBoard, Field field) {
        return solutionBoard.get(field.getRowIdx()).get(field.getColumnIdx()).equals(X_FIELD);
    }
}
