package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils;

import java.util.List;

public class SequenceRangeCorrectionWhenMetXLogHelper {

    public static String generateLog(
            int index,
            List<List<Integer>> initialRanges,
            List<List<Integer>> updatedRanges,
            List<Integer> sequenceLengths,
            List<Integer> excludedSequences,
            boolean isRow
    ) {
        return String.format(
                """
                        CORRECT_%s_SEQUENCES_RANGES_IF_X_ON_WAY: %s=%d
                        initialRanges=%s
                        updatedRanges=%s
                        lengths=%s
                        excluded=%s
                        """,
                isRow ? "ROW" : "COLUMN",
                isRow ? "row" : "col",
                index,
                LogFormatUtils.formatNestedList(initialRanges),
                LogFormatUtils.formatNestedList(updatedRanges),
                LogFormatUtils.formatList(sequenceLengths),
                LogFormatUtils.formatList(excludedSequences)
        );
    }

    public static String convertLogToTestArguments(String log, String fileName, NonogramLogic logic) {
        String[] lines = log.split("\n");

        boolean isRow = lines[0].startsWith("CORRECT_ROW");
        String axisLabel = isRow ? "row" : "col";

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());
        List<List<Integer>> initialRanges = LogFormatUtils.parseNestedListLine(lines[1].split("=")[1].trim());
        List<List<Integer>> updatedRanges = LogFormatUtils.parseNestedListLine(lines[2].split("=")[1].trim());
        List<Integer> lengths = LogFormatUtils.parseIntegerListLine(lines[3].split("=")[1].trim());
        List<Integer> excluded = LogFormatUtils.safeParseIntegerListLine(lines[4]);

        return String.format(
                "Arguments.of(\"%s / %dx%d / %s %d - ranges correction if X on way\",%n" +
                        "    List.of(%s),%n" +   // initial
                        "    List.of(%s),%n" +   // updated
                        "    List.of(%s),%n" +   // lengths
                        "    List.of(%s),%n" +   // excluded
                        "    %s%n" +             // isRow
                        ")",
                fileName,
                logic.getNonogramRules().getWidth(),
                logic.getNonogramRules().getHeight(),
                isRow ? "Row" : "Column",
                index,
                LogFormatUtils.toRangeStringList(initialRanges),
                LogFormatUtils.toRangeStringList(updatedRanges),
                lengths.toString(),
                excluded.toString(),
                isRow
        );
    }

}
