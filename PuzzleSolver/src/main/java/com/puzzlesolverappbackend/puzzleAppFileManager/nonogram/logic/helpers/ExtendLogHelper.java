package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ExtendLogHelper {

    public static String generateExtendSequenceLog(
            int index,
            String direction,
            List<String> initialState,
            List<List<Integer>> sequenceRanges,
            List<Integer> sequenceLengths,
            List<String> finalState,
            boolean isRow
    ) {
        return String.format(
                "EXTEND_%s_SEQUENCE: %s=%d, dir=%s\n" +
                        "initial=%s\n" +
                        "ranges=%s\n" +
                        "lengths=%s\n" +
                        "final=%s\n",
                isRow ? "ROW" : "COLUMN",
                isRow ? "row" : "col",
                index,
                direction,
                initialState.toString(),
                sequenceRanges.toString(),
                sequenceLengths.toString(),
                finalState.toString()
        );
    }

    public static String convertLogToTestArguments(String logText) {
        String[] lines = logText.strip().split("\n");

        String header = lines[0].replace("EXTEND_", "").replace("_SEQUENCE:", "").trim(); // e.g. "ROW row=2, dir=toLeft"
        String[] headerParts = header.split(", ");
        String indexInfo = headerParts[0]; // "row=2" or "col=5"
        int indexNumber = Integer.parseInt(indexInfo.split("=")[1]);
        String isRow = indexInfo.startsWith("row") ? "Row" : "Column";
        String testLabel = "/ " + isRow + " " + indexNumber;

        String initialLine = lines[1].replace("initial=", "").trim();
        String rangesLine = lines[2].replace("ranges=", "").trim();
        String lengthsLine = lines[3].replace("lengths=", "").trim();
        String finalLine = lines[4].replace("final=", "").trim();

        return String.format("""
        Arguments.of("%s",
            List.of(%s),
            List.of(%s),
            List.of(%s),
            List.of(%s)
        )""",
                testLabel,
                formatList(initialLine),
                formatNestedList(rangesLine),
                formatList(lengthsLine),
                formatList(finalLine)
        );
    }

    private static String formatList(String input) {
        return Arrays.stream(input.replaceAll("[\\[\\]]", "").split(","))
                .map(String::trim)
                .map(s -> s.matches("-?\\d+") ? s : "\"" + s + "\"")
                .collect(Collectors.joining(", "));
    }

    private static String formatNestedList(String input) {
        String[] parts = input.replaceAll("\\[\\[|\\]\\]", "").split("\\],\\s*\\[");
        return Arrays.stream(parts)
                .map(p -> "List.of(" + formatList("[" + p + "]") + ")")
                .collect(Collectors.joining(", "));
    }
}
