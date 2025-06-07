package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramLogic;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.LogFormatUtils.formatList;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.LogFormatUtils.formatNestedList;

@UtilityClass
public class TrivialFillLogHelper {

    public static String generateTrivialLineLog(
            int index,
            boolean isRow,
            List<String> initialState,
            List<String> finalState,
            List<Integer> sequenceLengths,
            List<List<Integer>> sequenceRanges
    ) {
        String label = isRow ? "ROW" : "COLUMN";

        return String.format(
                "TRIVIAL_%s_SEQUENCE: %s=%d\n" +
                        "initial=%s\n" +
                        "lengths=%s\n" +
                        "ranges=%s\n" +
                        "final=%s\n",
                label,
                isRow ? "row" : "col",
                index,
                initialState.toString(),
                sequenceLengths.toString(),
                sequenceRanges.toString(),
                finalState.toString()
        );
    }

    public static String convertLogToTestArguments(String logText, String solutionName, NonogramLogic logic) {
        String[] lines = logText.strip().split("\n");

        String header = lines[0]
                .replace("TRIVIAL_ROW_SEQUENCE:", "")
                .replace("TRIVIAL_COLUMN_SEQUENCE:", "")
                .trim();

        String[] headerParts = header.split("=");
        if (headerParts.length != 2) {
            throw new IllegalArgumentException("Invalid header format: " + lines[0]);
        }

        boolean isRow = lines[0].contains("ROW");
        String isRowLabel = isRow ? "Row" : "Column";
        int index = Integer.parseInt(headerParts[1].trim());

        // Remove "r" and ".json" from solution name
        String fileName = solutionName.replaceFirst("^r", "").replaceFirst("\\.json$", "");
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

        String initialLine = extractValue(lines, "initial");
        String lengthsLine = extractValue(lines, "lengths");
        String rangesLine = extractValue(lines, "ranges");
        String finalLine = extractValue(lines, "final");

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

    private static String extractValue(String[] lines, String prefix) {
        return Arrays.stream(lines)
                .filter(l -> l.startsWith(prefix + "="))
                .map(l -> l.replace(prefix + "=", "").trim())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Missing line for: " + prefix));
    }
}
