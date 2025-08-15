package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class SequenceRangeCorrectionWhenPlacingXsLogHelper {

    public static String generateLog(
            int rowIdx,
            int sequenceIdx,
            List<List<Integer>> sequencesRanges,
            List<Integer> updatedRange,
            List<String> line,
            List<Integer> sequencesLengths
    ) {
        return String.format(
                """
                        ROW_SEQUENCE_CORRECTION_WHEN_PLACING_X: rowIdx=%d
                        sequenceIdx=%d
                        sequencesRanges=%s
                        updatedRange=%s
                        line=%s
                        lengths=%s
                        """,
                rowIdx,
                sequenceIdx,
                sequencesRanges,
                updatedRange,
                line,
                sequencesLengths
        );
    }
}
