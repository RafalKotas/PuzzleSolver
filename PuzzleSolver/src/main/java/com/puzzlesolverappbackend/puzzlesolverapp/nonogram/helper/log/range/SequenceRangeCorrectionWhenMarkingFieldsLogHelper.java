package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class SequenceRangeCorrectionWhenMarkingFieldsLogHelper {

    private static final String LIST_STRING_FORMAT = "    List.of(%s),%n";

    public static String generateLog(
            int index,
            int seqIdx,
            List<List<Integer>> allRanges,
            List<Integer> newRange,
            List<Integer> sequenceLengths,
            boolean isRow
    ) {
        return String.format(
                """
                        %s_SEQUENCE_RANGE_CORRECTION_WHEN_MARKING_FIELDS: %s=%d, seq=%d
                        ranges=%s
                        new=%s
                        lengths=%s
                        """,
                isRow ? "ROW" : "COLUMN",
                isRow ? "row" : "col",
                index,
                seqIdx,
                LogFormatUtils.formatNestedList(allRanges),
                newRange,
                LogFormatUtils.formatList(sequenceLengths)
        );
    }

    public static String convertLogToTestArguments(String log, String fileName, NonogramLogic logic) {
        String[] lines = log.split("\\n");

        boolean isRow = lines[0].startsWith("ROW_SEQUENCE_RANGE_CORRECTION_WHEN_MARKING_FIELDS");
        String axisLabel = isRow ? "row" : "col";

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].split(",")[0].trim());
        int seqIdx = Integer.parseInt(lines[0].split("seq=")[1].split(",")[0].trim());

        List<List<Integer>> ranges = LogFormatUtils.parseNestedListLineWrappedInListOf(lines[1].split("=", 2)[1].trim());
        List<Integer> newRange = LogFormatUtils.parseIntegerListLine(lines[2].split("=", 2)[1].trim());
        List<Integer> lengths = LogFormatUtils.parseIntegerListLine(lines[3].split("=", 2)[1].trim());

        List<String> lineState = isRow
                ? logic.getNonogramSolutionBoard().get(index)
                : logic.getNonogramSolutionBoard().stream().map(row -> row.get(index)).toList();

        return String.format(
                "Arguments.of(\"%s / %dx%d / %s / %s %d - seq %d\",%n" +
                        LIST_STRING_FORMAT +
                        LIST_STRING_FORMAT +
                        LIST_STRING_FORMAT +
                        "    List.of(%s))",
                fileName,
                logic.getNonogramRules().getWidth(),
                logic.getNonogramRules().getHeight(),
                "marking",
                isRow ? "Row" : "Column",
                index,
                seqIdx,
                LogFormatUtils.toQuotedStringList(lineState),
                LogFormatUtils.toRangeStringList(ranges),
                lengths.toString(),
                newRange.toString()
        );
    }
}

