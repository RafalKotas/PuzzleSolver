package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class SequenceCorrectionWhenPlacingXsLogHelper {

    public static String generateLog(
            int rowIdx,
            int seqIdx,
            List<List<Integer>> allRanges,
            List<Integer> updatedRange,
            List<String> rowState,
            List<Integer> lengths
    ) {
        return String.format(
                "ROW_SEQUENCE_CORRECTION_WHEN_PLACING_X: row=%d\n" +
                        "seq=%d\n" +
                        "old=%s\n" +
                        "new=%s\n" +
                        "state=%s\n" +
                        "lengths=%s\n",
                rowIdx,
                seqIdx,
                allRanges,
                updatedRange,
                rowState,
                lengths
        );
    }
}
