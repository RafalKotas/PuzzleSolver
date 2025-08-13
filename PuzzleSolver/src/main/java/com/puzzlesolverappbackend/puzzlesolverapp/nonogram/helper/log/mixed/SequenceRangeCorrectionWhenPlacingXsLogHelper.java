package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class SequenceRangeCorrectionWhenPlacingXsLogHelper {

    public static String generateLog(
            int rowIdx,
            int seqIdx,
            List<List<Integer>> allRanges,
            List<Integer> updatedRange,
            List<String> rowState,
            List<Integer> lengths
    ) {
        return String.format(
                """
                        ROW_SEQUENCE_CORRECTION_WHEN_PLACING_X: row=%d
                        seq=%d
                        old=%s
                        new=%s
                        state=%s
                        lengths=%s
                        """,
                rowIdx,
                seqIdx,
                allRanges,
                updatedRange,
                rowState,
                lengths
        );
    }
}
