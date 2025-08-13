package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class SequenceRangeCorrectionByMatchingLogHelper {

    public static String generateLog(
            int index,
            List<List<Integer>> initialRanges,
            List<List<Integer>> finalRanges,
            List<Integer> sequenceLengths,
            List<String> lineState,
            boolean isRow //+
    ) {
        return String.format(
                """
                        %s_CORRECTING_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES: %s=%d
                        initial=%s
                        final=%s
                        lengths=%s
                        line=%s
                        """,
                isRow ? "ROW" : "COLUMN",
                isRow ? "row" : "col",
                index,
                LogFormatUtils.formatNestedList(initialRanges),
                LogFormatUtils.formatNestedList(finalRanges),
                LogFormatUtils.formatList(sequenceLengths),
                LogFormatUtils.formatList(lineState)
        );
    }

    public static String convertLogToTestArguments(String log, String solutionName, NonogramLogic logic) {
        return "TODO CORRECT BY MATCHING";
    }
}
