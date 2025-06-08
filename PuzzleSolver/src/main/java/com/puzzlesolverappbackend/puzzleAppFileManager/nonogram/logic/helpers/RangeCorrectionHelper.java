package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.Field;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramBoardUtils.isFieldColoured;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramBoardUtils.isFieldWithX;

@UtilityClass
public class RangeCorrectionHelper {

    public static List<Integer> calculateUpdatedNextSequenceRangeAfterExcludedSequence(
            List<List<Integer>> ranges, List<Integer> notAllowedFields, int currentIdx, int nextIdx) {

        int proposedStart = ranges.get(currentIdx).get(1) + 2;
        while (notAllowedFields.contains(proposedStart)) {
            proposedStart++;
        }

        int oldStart = ranges.get(nextIdx).get(0);
        int newStart = Math.max(oldStart, proposedStart);
        return List.of(newStart, ranges.get(nextIdx).get(1));
    }

    public static List<Integer> calculateUpdatedNextSequenceRangeAfterIncludedSequence(
            List<List<Integer>> ranges, List<Integer> lengths, int currentIdx, int nextIdx) {

        int proposedStart = ranges.get(currentIdx).get(0) + lengths.get(currentIdx) + 1;
        int oldStart = ranges.get(nextIdx).get(0);
        int newStart = Math.max(oldStart, proposedStart);
        return List.of(newStart, ranges.get(nextIdx).get(1));
    }

    public static List<Integer> calculateUpdatedPreviousSequenceRangeAfterExcludedSequence(
            List<List<Integer>> ranges, List<Integer> notAllowedFields, int currentIdx, int prevIdx) {

        int proposedEnd = ranges.get(currentIdx).get(0) - 2;
        while (notAllowedFields.contains(proposedEnd)) {
            proposedEnd--;
        }

        int oldEnd = ranges.get(prevIdx).get(1);
        int newEnd = Math.min(oldEnd, proposedEnd);
        return List.of(ranges.get(prevIdx).get(0), newEnd);
    }

    public static List<Integer> calculateUpdatedPreviousSequenceRangeAfterIncludedSequence(
            List<List<Integer>> ranges, List<Integer> lengths, int currentIdx, int prevIdx) {

        int proposedEnd = ranges.get(currentIdx).get(1) - lengths.get(currentIdx) - 1;
        int oldEnd = ranges.get(prevIdx).get(1);
        int newEnd = Math.min(oldEnd, proposedEnd);
        return List.of(ranges.get(prevIdx).get(0), newEnd);
    }

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

