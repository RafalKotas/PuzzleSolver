package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramLogic;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class OverlappingLogHelper {

    public static String generateOverlappingSequenceLog(
            int index,
            boolean isRow,
            List<String> initialState,
            List<List<Integer>> sequenceRanges,
            List<Integer> sequenceLengths,
            List<String> finalState
    ) {
        String type = isRow ? "ROW" : "COLUMN";
        return String.format(
                "OVERLAP_%s_SEQUENCE: %s=%d\n" +
                        "initial=%s\n" +
                        "ranges=%s\n" +
                        "lengths=%s\n" +
                        "final=%s\n",
                type,
                isRow ? "row" : "col",
                index,
                initialState.toString(),
                sequenceRanges.toString(),
                sequenceLengths.toString(),
                finalState.toString()
        );
    }

    public static String convertLogToTestArguments(
            String logText,
            String solutionName,
            NonogramLogic logic
    ) {
        String[] lines = logText.strip().split("\n");

        String header = lines[0].replace("OVERLAP_", "").replace("_SEQUENCE:", "").trim(); // "ROW row=2" lub "COLUMN col=3"
        String[] headerParts = header.split(" ");
        String indexInfo = headerParts[1]; // "row=2" lub "col=3"

        boolean isRow = indexInfo.startsWith("row");
        String isRowLabel = isRow ? "Row" : "Column";
        int index = Integer.parseInt(indexInfo.split("=")[1]);

        String fileName = solutionName.startsWith("r") ? solutionName.substring(1) : solutionName;
        int height = logic.getNonogramRules().getHeight();
        int width = logic.getNonogramRules().getWidth();

        String testLabel = String.format(
                "%s / %dx%d / diff  / %s %d",
                fileName,
                height,
                width,
                isRowLabel,
                index
        );

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
        if (input.equals("[]")) return "";

        String[] parts = input.replaceAll("\\[\\[|\\]\\]", "").split("\\],\\s*\\[");
        return Arrays.stream(parts)
                .map(p -> "List.of(" + formatList("[" + p + "]") + ")")
                .collect(Collectors.joining(", "));
    }
}
