package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class PlaceXsIfOWillCreateTooLongSequenceLogHelper {

    private static final String LIST_STRING_FORMAT = "    List.of(%s),%n";

    public static String generateLog(
            int index,
            List<String> before,
            List<String> after,
            List<Integer> sequenceLengths,
            List<List<Integer>> ranges,
            boolean isRow
    ) {
        return String.format(
                """
                        PLACE_X_IF_O_WILL_CREATE_TOO_LONG_SEQUENCE_IN_%s: %s=%d
                        before=%s
                        after=%s
                        lengths=%s
                        ranges=%s
                        """,
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

        boolean isRow = lines[0].startsWith("PLACE_X_IF_O_WILL_CREATE_TOO_LONG_SEQUENCE_IN_ROW");
        String axisLabel = isRow ? "row" : "col";

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());

        List<String> before = LogFormatUtils.parseStringListLine(lines[1].split("=")[1].trim());
        List<String> after = LogFormatUtils.parseStringListLine(lines[2].split("=")[1].trim());
        List<Integer> lengths = LogFormatUtils.parseIntegerListLine(lines[3].split("=")[1].trim());
        List<List<Integer>> ranges = LogFormatUtils.parseNestedListLine(lines[4].split("=")[1].trim());

        return String.format(
                "Arguments.of(\"%s / %dx%d / %s %d - X if O too long\",%n" +
                        LIST_STRING_FORMAT +   // before
                        LIST_STRING_FORMAT +   // after
                        LIST_STRING_FORMAT +   // lengths
                        LIST_STRING_FORMAT +   // ranges
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
