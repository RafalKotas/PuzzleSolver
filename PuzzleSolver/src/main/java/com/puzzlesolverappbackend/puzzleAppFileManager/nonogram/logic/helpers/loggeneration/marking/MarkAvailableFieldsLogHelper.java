package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.marking;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.LogFormatUtils;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class MarkAvailableFieldsLogHelper {

    public static String generateLog(
            int index,
            List<String> initialState,
            List<String> finalState,
            int seqIdx,
            String marker,
            boolean isRow
    ) {
        return String.format(
                "MARK_AVAILABLE_FIELDS_IN_%s: %s=%d, seq=%d, marker=%s\n" +
                        "initial=%s\n" +
                        "final=%s\n",
                isRow ? "ROW" : "COLUMN",
                isRow ? "row" : "col",
                index,
                seqIdx,
                marker,
                LogFormatUtils.formatList(initialState),
                LogFormatUtils.formatList(finalState)
        );
    }

    public static String convertLogToTestArguments(String log, String fileName, NonogramLogic logic) {
        String[] lines = log.split("\\n");

        boolean isRow = lines[0].startsWith("MARK_AVAILABLE_FIELDS_IN_ROW");
        String axisLabel = isRow ? "row" : "col";

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].split(",")[0].trim());
        int seqIdx = Integer.parseInt(lines[0].split("seq=")[1].split(",")[0].trim());
        String marker = lines[0].split("marker=")[1].trim();

        List<String> initialState = LogFormatUtils.parseStringListLine(lines[1]);
        List<String> finalState = LogFormatUtils.parseStringListLine(lines[2]);

        List<String> fieldState = isRow
                ? logic.getNonogramSolutionBoard().get(index)
                : logic.getNonogramSolutionBoard().stream().map(row -> row.get(index)).collect(Collectors.toList());

        List<List<Integer>> ranges = isRow
                ? logic.getRowsSequencesRanges().get(index)
                : logic.getColumnsSequencesRanges().get(index);

        List<Integer> lengths = isRow
                ? logic.getNonogramRules().getRowSequencesLengths().get(index)
                : logic.getNonogramRules().getColumnSequencesLengths().get(index);

        return String.format(
                "Arguments.of(\"%s / %dx%d / %s / %s %d - seq %d\",%n" +
                        "    List.of(%s),%n" +
                        "    List.of(%s),%n" +
                        "    List.of(%s),%n" +
                        "    List.of(%s),%n" +
                        "    List.of(%s))",
                fileName,
                logic.getNonogramRules().getWidth(),
                logic.getNonogramRules().getHeight(),
                marker,
                isRow ? "Row" : "Column",
                index,
                seqIdx,
                LogFormatUtils.toQuotedStringList(initialState),
                LogFormatUtils.toQuotedStringList(finalState),
                LogFormatUtils.toQuotedStringList(fieldState),
                LogFormatUtils.toRangeStringList(ranges),
                lengths.toString()
        );
    }
}
