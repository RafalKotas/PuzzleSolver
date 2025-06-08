package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class PlaceXsAroundLongestSequenceLogHelper {

    public static String generateLog(
            int index,
            List<Integer> xEdges,
            List<String> initialState,
            List<String> finalState,
            boolean onlyMatching,
            boolean isRow
    ) {
        return String.format(
                "PLACE_XS_%s_AROUND_LONGEST_SEQUENCE: %s=%d\n" +
                        "xs=%s\n" +
                        "onlyMatching=%b\n" +
                        "initial=%s\n" +
                        "final=%s\n",
                isRow ? "ROW" : "COLUMN",
                isRow ? "row" : "col",
                index,
                xEdges,
                onlyMatching,
                initialState,
                finalState
        );
    }

    public static String convertLogToTestArguments(String log, String solutionName) {
        String[] lines = log.strip().split("\n");
        boolean isRow = lines[0].startsWith("PLACE_XS_ROW");

        int index = Integer.parseInt(lines[0].split("=")[1]);
        List<Integer> xEdges = parseIntList(lines[1].split("=")[1]);
        boolean onlyMatching = Boolean.parseBoolean(lines[2].split("=")[1]);
        List<String> initialState = parseStringList(lines[3].split("=")[1]);
        List<String> finalState = parseStringList(lines[4].split("=")[1]);

        String header = String.format("%s / %s %d",
                solutionName,
                isRow ? "Row" : "Column",
                index
        );

        return String.format(
                "Arguments.of(\"%s\",\n" +
                        "    %s,\n" +
                        "    %s,\n" +
                        "    %s,\n" +
                        "    %b\n" +
                        ")",
                header,
                formatList(initialState),
                formatList(xEdges),
                formatList(finalState),
                onlyMatching
        );
    }

    private static List<Integer> parseIntList(String raw) {
        return Arrays.stream(raw.replaceAll("[\\[\\]]", "").split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toList();
    }

    private static List<String> parseStringList(String raw) {
        return Arrays.stream(raw.replaceAll("[\\[\\]\"]", "").split(","))
                .map(String::trim)
                .toList();
    }

    private static String formatList(List<?> list) {
        return "List.of(" + list.stream()
                .map(e -> e instanceof String ? "\"" + e + "\"" : e.toString())
                .collect(Collectors.joining(", ")) + ")";
    }
}
