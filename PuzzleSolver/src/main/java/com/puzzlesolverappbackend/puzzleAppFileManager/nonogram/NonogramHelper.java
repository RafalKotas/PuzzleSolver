package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram;

import java.util.ArrayList;
import java.util.List;

public class NonogramHelper {
    /**
     * @param marksNo - number of sequences marks to create array
     * @return list of marks created from given marksNo (1 -> ["a"], 5 -> ["a", "b", "c", "d", "e"], etc...)
     */
    public static List<String> generateArrayOfSequenceMarks (int marksNo) {
        List<String> arrayOfSequenceMarks = new ArrayList<>();

        for (int charIdx = 0; charIdx < marksNo; charIdx++) {
            arrayOfSequenceMarks.add( indexToSequenceCharMark(charIdx) );
        }

        return arrayOfSequenceMarks;
    }


    /**
     * @param index - index that would be converted into char (sequence char mark)
     * @return sequence character ("a", "b", "c", ... )
     */
    public static String indexToSequenceCharMark(int index) {
        return Character.toString((char) ((int) 'a' + index));
    }

    /**
     * @param arrLength - length(size) of created array
     * @return list of <arrLength> elements filled with "----"(empty)
     */
    public static List<String> createArrayOfEmptyFields (int arrLength) {
        List<String> emptyFields = new ArrayList<>();
        for (int i = 0; i < arrLength; i++) {
            emptyFields.add("----");
        }
        return emptyFields;
    }
}
