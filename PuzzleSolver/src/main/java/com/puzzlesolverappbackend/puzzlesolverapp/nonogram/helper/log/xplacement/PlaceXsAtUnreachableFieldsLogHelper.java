package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class PlaceXsAtUnreachableFieldsLogHelper {

    private static final String LIST_OF_START_WITH_OPENING_BRACKET = "List.of(";

    public static String generateLog(
            int index,
            List<String> initialState,
            List<String> finalState,
            List<List<Integer>> sequenceRanges,
            boolean isRow
    ) {
        return String.format(
                """
                        PLACE_XS_AT_UNREACHABLE_FIELDS_IN_%s: %s=%d
                        initial=%s
                        ranges=%s
                        final=%s
                        """,
                isRow ? "ROW" : "COLUMN",
                isRow ? "row" : "col",
                index,
                LogFormatUtils.formatList(initialState),
                LogFormatUtils.formatNestedList(sequenceRanges),
                LogFormatUtils.formatList(finalState)
        );
    }

    public static String convertLogToTestArguments(String log, String solutionName) {
        String[] lines = log.split("\n");
        String header = lines[0];

        boolean isRow = header.contains("IN_ROW");
        int index = Integer.parseInt(header.split("=")[1]);

        List<String> initial = parseList(lines[1].split("=")[1]);
        List<List<Integer>> ranges = parseNestedListFromListOfString(lines[2].split("=")[1].trim());
        List<String> expectedFinal = parseList(lines[3].split("=")[1]);

        return String.format(
                """
                        Arguments.of("%s / %s %d - unreachable fields",
                            %s,
                            %s,
                            %s,
                            %s
                        )""",
                solutionName,
                isRow ? "Row" : "Column",
                index,
                formatAsList(initial),
                formatAsNestedList(ranges),
                formatAsList(expectedFinal),
                isRow
        );
    }

    private static List<String> parseList(String line) {
        return Arrays.stream(line.replaceAll("[\\[\\]\"]", "").split(","))
                .map(String::trim)
                .toList();
    }

    private static List<List<Integer>> parseNestedListFromListOfString(String input) {
        String trimmed = input.trim();
        if (trimmed.startsWith(LIST_OF_START_WITH_OPENING_BRACKET)) {
            trimmed = trimmed.substring(8, trimmed.length() - 1); // remove List.of( ... )
        }

        return Arrays.stream(trimmed.split("List.of\\("))
                .map(s -> s.replace(")", "").trim())
                .filter(s -> !s.isEmpty())
                .map(inner -> Arrays.stream(inner.split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .toList())
                .toList();
    }

    private static String formatAsList(List<String> list) {
        return LIST_OF_START_WITH_OPENING_BRACKET + list.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(", ")) + ")";
    }

    private static String formatAsNestedList(List<List<Integer>> nestedList) {
        return LIST_OF_START_WITH_OPENING_BRACKET +
                nestedList.stream()
                        .map(inner -> LIST_OF_START_WITH_OPENING_BRACKET + inner.stream().map(String::valueOf).collect(Collectors.joining(", ")) + ")")
                        .collect(Collectors.joining(", ")) +
                ")";
    }
}
