package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.range;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.LogFormatUtils;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class SequenceRangeCorrectionFromColouredEdgesLogHelper {

    public static String generateLog(
            int index,
            List<List<Integer>> initialRanges,
            List<List<Integer>> finalRanges,
            List<Integer> sequenceLengths,
            List<String> lineState,
            boolean isRow
    ) {
        return String.format(
                """
                        %s_CORRECTING_SEQUENCES_RANGES_FROM_COLOURED_EDGES: %s=%d
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

    public static String convertLogToTestArguments(String log, String solutionName) {
        String[] lines = log.split("\\n");

        boolean isRow = lines[0].startsWith("ROW_");
        String axisLabel = isRow ? "row" : "col";

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());
        List<List<Integer>> initial = LogFormatUtils.parseNestedListLine(lines[1].split("=", 2)[1].trim());
        List<List<Integer>> finalRanges = LogFormatUtils.parseNestedListLine(lines[2].split("=", 2)[1].trim());
        List<Integer> lengths = LogFormatUtils.parseIntegerListLine(lines[3].split("=", 2)[1].trim());
        List<String> line = LogFormatUtils.parseStringListLine(lines[4].split("=", 2)[1].trim());

        return String.format(
                """
                        Arguments.of("%s / %s %d - corrected ranges from coloured edges",
                            List.of(%s),
                            List.of(%s),
                            List.of(%s),
                            List.of(%s),
                            %s
                        )""",
                solutionName,
                isRow ? "Row" : "Column",
                index,
                LogFormatUtils.toRangeStringList(initial),
                LogFormatUtils.toRangeStringList(finalRanges),
                lengths.toString(),
                LogFormatUtils.toQuotedStringList(line),
                isRow
        );
    }
}


