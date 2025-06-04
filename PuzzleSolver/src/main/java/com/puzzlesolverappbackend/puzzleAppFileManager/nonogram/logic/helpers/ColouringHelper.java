package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ColouringHelper {

    public static List<Integer> calculateOverlappingRange(List<Integer> range, int sequenceLength) {
        int start = range.get(1) - sequenceLength + 1;
        int end = range.get(0) + sequenceLength - 1;

        if (start > end) return List.of();

        return IntStream.rangeClosed(start, end)
                .boxed()
                .collect(Collectors.toList());
    }

}

