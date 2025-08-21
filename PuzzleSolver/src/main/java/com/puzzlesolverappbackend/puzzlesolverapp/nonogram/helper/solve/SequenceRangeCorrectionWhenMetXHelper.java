package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.solve;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.isFieldWithX;

@UtilityClass
public class SequenceRangeCorrectionWhenMetXHelper {

    public static List<Integer> calculateCorrectedRangeWithoutX(
            boolean isVertical,
            int index,
            List<Integer> currentRange,
            int sequenceLength,
            List<List<String>> board
    ) {
        int newStart = findFirstValidSequenceStartIndexWithoutX(
                currentRange.get(0), currentRange.get(1), sequenceLength, index, isVertical, board);

        int newEnd = findLastValidSequenceEndIndexWithoutX(
                currentRange.get(0), currentRange.get(1), sequenceLength, index, isVertical, board);

        return List.of(newStart, newEnd);
    }

    private static int findFirstValidSequenceStartIndexWithoutX(
            int sequenceStart, int sequenceEnd, int sequenceLength,
            int fixedIdx, boolean isVertical, List<List<String>> board) {

        for (int start = sequenceStart; start <= sequenceEnd - sequenceLength + 1; start++) {
            boolean valid = true;
            for (int offset = 0; offset < sequenceLength; offset++) {
                Field f = isVertical ? new Field(start + offset, fixedIdx)
                        : new Field(fixedIdx, start + offset);
                if (isFieldWithX(board, f)) {
                    valid = false;
                    break;
                }
            }
            if (valid) return start;
        }
        return sequenceStart;
    }

    private static int findLastValidSequenceEndIndexWithoutX(
            int sequenceStart, int sequenceEnd, int sequenceLength,
            int fixedIdx, boolean isVertical, List<List<String>> board) {

        for (int end = sequenceEnd; end >= sequenceStart + sequenceLength - 1; end--) {
            boolean valid = true;
            for (int offset = 0; offset < sequenceLength; offset++) {
                Field f = isVertical ? new Field(end - offset, fixedIdx)
                        : new Field(fixedIdx, end - offset);
                if (isFieldWithX(board, f)) {
                    valid = false;
                    break;
                }
            }
            if (valid) return end;
        }
        return sequenceEnd;
    }
}
