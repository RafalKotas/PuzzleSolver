package com.puzzlesolverappbackend.puzzleAppFileManager.utils;

import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.Field;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.NonogramConstants.*;


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
                currentRange.set(1, indices.get(i));
            } else {
                if (currentRange.size() == 1) {
                    currentRange.add(currentRange.get(0));
                }
                result.add(new ArrayList<>(currentRange));
                currentRange.clear();
                currentRange.add(indices.get(i));
            }
        }
        result.add(new ArrayList<>(currentRange));

        return result;
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
