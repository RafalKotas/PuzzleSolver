package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramConstants.*;

@UtilityClass
public class BoardUtils {

    // === Board Access ===
    public static List<String> getColumn(List<List<String>> board, int colIdx) {
        return board.stream()
                .map(row -> row.get(colIdx))
                .toList();
    }

    public static boolean isFieldColoured(List<List<String>> board, Field field) {
        return board.get(field.getRowIdx()).get(field.getColumnIdx()).equals(COLOURED_FIELD);
    }

    public static boolean isFieldEmpty(List<List<String>> board, Field field) {
        return board.get(field.getRowIdx()).get(field.getColumnIdx()).equals(EMPTY_FIELD);
    }

    public static boolean isFieldWithX(List<List<String>> board, Field field) {
        return board.get(field.getRowIdx()).get(field.getColumnIdx()).equals(X_FIELD);
    }

    // === Sequence Identification ===
    public static List<Integer> findColouredFieldsInRow(List<List<String>> board, int rowIdx) {
        List<String> row = board.get(rowIdx);
        return IntStream.range(0, row.size())
                .filter(colIdx -> isFieldColoured(board, new Field(rowIdx, colIdx)))
                .boxed()
                .toList();
    }

    public static List<Integer> findColouredFieldsInColumn(List<List<String>> board, int colIdx) {
        List<String> column = getColumn(board, colIdx);
        return IntStream.range(0, column.size())
                .filter(rowIdx -> isFieldColoured(board, new Field(rowIdx, colIdx)))
                .boxed()
                .toList();
    }

    public static List<List<Integer>> groupConsecutiveIndices(List<Integer> indices) {
        List<List<Integer>> result = new ArrayList<>();
        if (indices == null || indices.isEmpty()) return result;

        List<Integer> current = new ArrayList<>();
        current.add(indices.get(0));

        for (int i = 1; i < indices.size(); i++) {
            if (indices.get(i) == indices.get(i - 1) + 1) {
                if (current.size() == 1) {
                    current.add(indices.get(i));
                } else {
                    current.set(1, indices.get(i));
                }
            } else {
                if (current.size() == 1) current.add(current.get(0));
                result.add(new ArrayList<>(current));
                current.clear();
                current.add(indices.get(i));
            }
        }
        if (current.size() == 1) current.add(current.get(0));
        result.add(new ArrayList<>(current));

        return result;
    }

    public static List<Integer> findColouredSequenceRange(List<List<String>> board, Field startField, boolean isRow, NonogramRules rules) {
        int start = isRow ? startField.getColumnIdx() : startField.getRowIdx();
        int end = start;

        while (start > 0) {
            Field prev = isRow ? new Field(startField.getRowIdx(), start - 1) : new Field(start - 1, startField.getColumnIdx());
            if (!isFieldColoured(board, prev)) break;
            start--;
        }

        int limit = isRow ? rules.getWidth() : rules.getHeight();
        while (end + 1 < limit) {
            Field next = isRow ? new Field(startField.getRowIdx(), end + 1) : new Field(end + 1, startField.getColumnIdx());
            if (!isFieldColoured(board, next)) break;
            end++;
        }

        return List.of(start, end);
    }

    // === Sequence Ranges + Merge ===
    public static List<List<List<Integer>>> createCandidateRangesAroundSequences(List<List<Integer>> colouredSequences) {
        List<List<List<Integer>>> result = new ArrayList<>();
        for (List<Integer> seq : colouredSequences) {
            result.add(List.of(
                    List.of(seq.get(0) - 1, seq.get(1)),
                    List.of(seq.get(0), seq.get(1) + 1)
            ));
        }
        return result;
    }

    public static List<Integer> mergeWithPreviousIfAdjacent(List<Integer> prev, List<Integer> current) {
        return (prev.get(1) + 1 == current.get(0))
                ? List.of(prev.get(0), current.get(1))
                : current;
    }

    public static List<Integer> mergeWithNextIfAdjacent(List<Integer> current, List<Integer> next) {
        return (current.get(1) + 1 == next.get(0))
                ? List.of(current.get(0), next.get(1))
                : current;
    }

    public static List<Integer> calculateNewMarkedRange(List<Integer> oldRange, List<Integer> coloured, int length) {
        int start = Math.max(oldRange.get(0), coloured.get(1) - length + 1);
        int end = Math.min(oldRange.get(1), coloured.get(0) + length - 1);
        return List.of(start, end);
    }

    // === Sequence Marks and Initial States ===
    public static List<String> generateSequenceMarks(int count) {
        return new ArrayList<>(IntStream.range(0, count)
                .mapToObj(BoardUtils::indexToSequenceCharMark)
                .toList());
    }

    public static String indexToSequenceCharMark(int index) {
        return Character.toString((char) ('a' + index));
    }

    public static List<String> createEmptyMarkedLine(int length) {
        return new ArrayList<>(IntStream.range(0, length)
                .mapToObj(i -> EMPTY_FIELD_MARKED_BOARD)
                .toList());
    }
}
