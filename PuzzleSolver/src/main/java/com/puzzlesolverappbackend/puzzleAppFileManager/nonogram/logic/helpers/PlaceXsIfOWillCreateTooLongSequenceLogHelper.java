package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramLogic;

import java.util.List;
import java.util.stream.Collectors;

public class PlaceXsIfOWillCreateTooLongSequenceLogHelper {

    public static String generateLog(
            int index,
            List<String> before,
            List<String> after,
            List<Integer> sequenceLengths,
            List<List<Integer>> ranges,
            boolean isRow
    ) {
        return String.format(
                "PLACE_X_IF_O_TOO_LONG_%s: %s=%d\n" +
                        "before=%s\n" +
                        "after=%s\n" +
                        "lengths=%s\n" +
                        "ranges=%s\n",
                isRow ? "ROW" : "COLUMN",
                isRow ? "row" : "col",
                index,
                before.toString(),
                after.toString(),
                LogFormatUtils.formatList(sequenceLengths),
                LogFormatUtils.formatNestedList(ranges)
        );
    }

    public static String convertLogToTestArguments(String log, String solutionName, NonogramLogic logic) {
        String[] lines = log.split("\\n");

        boolean isRow = lines[0].startsWith("PLACE_X_IF_O_TOO_LONG_ROW");
        String axisLabel = isRow ? "row" : "col";

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());

        List<String> before = LogFormatUtils.parseStringListLine(lines[1].split("=")[1].trim());
        List<String> after = LogFormatUtils.parseStringListLine(lines[2].split("=")[1].trim());
        List<Integer> lengths = LogFormatUtils.parseIntegerListLine(lines[3].split("=")[1].trim());
        List<List<Integer>> ranges = LogFormatUtils.parseNestedListLine(lines[4].split("=")[1].trim());

        return String.format(
                "Arguments.of(\"%s / %dx%d / %s %d - X if O too long\",%n" +
                        "    List.of(%s),%n" +   // before
                        "    List.of(%s),%n" +   // after
                        "    List.of(%s),%n" +   // lengths
                        "    List.of(%s),%n" +   // ranges
                        "    %s%n" +             // isRow
                        ")",
                solutionName,
                logic.getNonogramRules().getWidth(),
                logic.getNonogramRules().getHeight(),
                isRow ? "Row" : "Column",
                index,
                before.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(", ")),
                after.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(", ")),
                lengths.toString(),
                LogFormatUtils.toRangeStringList(ranges),
                isRow
        );
    }
}
