package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class SequenceRangeCorrectionWhenMetXLogHelper {

    private static final String LIST_STRING_FORMAT = "    List.of(%s),%n";

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
        List<String> excluded = LogFormatUtils.parseStringListLine(lines[4]);

        return String.format(
                "Arguments.of(\"%s / %dx%d / %s %d - ranges correction if X on way\",%n" +
                        LIST_STRING_FORMAT +   // initial
                        LIST_STRING_FORMAT +   // updated
                        LIST_STRING_FORMAT +   // lengths
                        LIST_STRING_FORMAT +   // excluded
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
