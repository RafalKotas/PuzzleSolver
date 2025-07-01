package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.colouring;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramLogic;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.LogFormatUtils.formatList;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.LogFormatUtils.formatNestedList;

@UtilityClass
public class TooLongMergeLogHelper {

    public static String generateTooLongMergeSequenceLog(
            int index,
            boolean isRow,
            List<String> initialState,
            List<List<Integer>> sequenceRanges,
            List<Integer> sequenceLengths,
            List<String> finalState
    ) {
        String label = isRow ? "ROW" : "COLUMN";

        return String.format(
                "TOO_LONG_MERGE_%s_SEQUENCE: %s=%d\n" +
                        "initial=%s\n" +
                        "ranges=%s\n" +
                        "lengths=%s\n" +
                        "final=%s\n",
                label,
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

        String header = lines[0]
                .replace("TOO_LONG_MERGE_ROW_SEQUENCE:", "")
                .replace("TOO_LONG_MERGE_COLUMN_SEQUENCE:", "")
                .trim();

        String[] headerParts = header.split("=");
        if (headerParts.length != 2) {
            throw new IllegalArgumentException("Invalid header format: " + lines[0]);
        }

        String indexKey = headerParts[0].trim(); // "row" lub "col"
        int index;
        try {
            index = Integer.parseInt(headerParts[1].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid index number in header: " + lines[0]);
        }

        boolean isRow = indexKey.equalsIgnoreCase("row");
        String isRowLabel = isRow ? "Row" : "Column";

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
}
