package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.Field;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramBoardUtils.isFieldColoured;

@UtilityClass
public class RangeCorrectionHelper {

    public static List<Integer> updatedSequenceRangeWhenMetColouredField(
            int sequenceStart, int sequenceEnd, int colouredIndex, int sequenceLength, boolean fromLeft) {

        if (fromLeft) {
            int maxEnd = colouredIndex + sequenceLength - 1;
            int updatedEnd = Math.min(sequenceEnd, maxEnd);
            return List.of(sequenceStart, updatedEnd);
        } else {
            int minStart = colouredIndex - sequenceLength + 1;
            int updatedStart = Math.max(sequenceStart, minStart);
            return List.of(updatedStart, sequenceEnd);
        }
    }

    public static List<Integer> adjustRangeIfColouredAtEdges(List<Integer> range, int lineIdx, boolean isVertical, List<List<String>> board, int lineLimit) {
        int start = range.get(0);
        int end = range.get(1);

        if (start > 0) {
            Field beforeStart = isVertical ? new Field(start - 1, lineIdx) : new Field(lineIdx, start - 1);
            if (isFieldColoured(board, beforeStart)) {
                start++;
            }
        }

        if (end < lineLimit - 1) {
            Field afterEnd = isVertical ? new Field(end + 1, lineIdx) : new Field(lineIdx, end + 1);
            if (isFieldColoured(board, afterEnd)) {
                end--;
            }
        }

        return List.of(start, end);
    }
}

