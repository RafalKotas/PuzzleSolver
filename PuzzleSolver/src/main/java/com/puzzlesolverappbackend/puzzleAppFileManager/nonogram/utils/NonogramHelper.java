package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.Field;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramRules;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.model.NonogramConstants.EMPTY_FIELD_MARKED_BOARD;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramBoardUtils.isFieldColoured;

public class NonogramHelper {

    public static List<String> generateArrayOfSequenceMarks (int marksNo) {
        return IntStream.range(0, marksNo)
                .mapToObj(NonogramHelper::indexToSequenceCharMark)
                .collect(Collectors.toList());
    }

    public static String indexToSequenceCharMark(int index) {
        return Character.toString((char) ((int) 'a' + index));
    }

    public static List<String> createArrayOfEmptyFields (int arrLength) {
        return IntStream.range(0, arrLength)
                .mapToObj(i -> EMPTY_FIELD_MARKED_BOARD)
                .collect(Collectors.toList());
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

    public static List<Integer> calculateNewMarkedRangeFromParameters(List<Integer> oldRange,
                                                                List<Integer> colouredSequenceIndexes,
                                                                int sequenceLength) {
        int newRangeBegin = Math.max(oldRange.get(0), colouredSequenceIndexes.get(1) - sequenceLength + 1);
        int newRangeEnd = Math.min(oldRange.get(1), colouredSequenceIndexes.get(0) + sequenceLength - 1);
        return List.of(newRangeBegin, newRangeEnd);
    }
}
