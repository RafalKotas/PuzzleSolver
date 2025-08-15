package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.solve;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class SequenceRangeCorrectionHelper {

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
}
