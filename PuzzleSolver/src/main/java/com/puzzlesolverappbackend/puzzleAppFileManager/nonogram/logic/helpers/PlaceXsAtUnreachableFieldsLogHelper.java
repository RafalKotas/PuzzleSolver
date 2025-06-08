package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class PlaceXsAtUnreachableFieldsLogHelper {

    public static String generateLog(
            int index,
            List<String> initialState,
            List<String> finalState,
            List<List<Integer>> sequenceRanges,
            boolean isRow
    ) {
        return String.format(
                "PLACE_XS_AT_UNREACHABLE_FIELDS_IN_%s: %s=%d\n" +
                        "initial=%s\n" +
                        "ranges=%s\n" +
                        "final=%s\n",
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
                "Arguments.of(\"%s / %s %d - unreachable fields\",\n" +
                        "    %s,\n" +
                        "    %s,\n" +
                        "    %s,\n" +
                        "    %s\n" +
                        ")",
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
                .collect(Collectors.toList());
    }

    private static List<List<Integer>> parseNestedListFromListOfString(String input) {
        String trimmed = input.trim();
        if (trimmed.startsWith("List.of(")) {
            trimmed = trimmed.substring(8, trimmed.length() - 1); // usuń List.of( ... )
        }

        return Arrays.stream(trimmed.split("List.of\\("))
                .map(s -> s.replace(")", "").trim())
                .filter(s -> !s.isEmpty())
                .map(inner -> Arrays.stream(inner.split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private static String formatAsList(List<String> list) {
        return "List.of(" + list.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(", ")) + ")";
    }

    private static String formatAsNestedList(List<List<Integer>> nestedList) {
        return "List.of(" +
                nestedList.stream()
                        .map(inner -> "List.of(" + inner.stream().map(String::valueOf).collect(Collectors.joining(", ")) + ")")
                        .collect(Collectors.joining(", ")) +
                ")";
    }
}
