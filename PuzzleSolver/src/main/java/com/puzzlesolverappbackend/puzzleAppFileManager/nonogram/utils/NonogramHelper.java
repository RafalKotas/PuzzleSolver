package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.model.NonogramConstants.EMPTY_FIELD_MARKED_BOARD;

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
}
