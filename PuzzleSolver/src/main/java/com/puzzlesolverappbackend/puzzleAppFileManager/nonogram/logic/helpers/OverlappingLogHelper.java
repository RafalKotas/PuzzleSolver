package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramLogic;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.LogFormatUtils.formatList;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.LogFormatUtils.formatNestedList;

@UtilityClass
public final class OverlappingLogHelper {

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

        String header = lines[0].replace("OVERLAP_", "").replace("_SEQUENCE:", "").trim();
        String[] headerParts = header.split(" ");

        if (headerParts.length < 2 || !headerParts[1].contains("=")) {
            throw new IllegalArgumentException("Invalid log header format: " + lines[0]);
        }

        String indexInfo = headerParts[1];
        boolean isRow = indexInfo.startsWith("row");
        String isRowLabel = isRow ? "Row" : "Column";

        String[] indexSplit = indexInfo.split("=");
        if (indexSplit.length != 2) {
            throw new IllegalArgumentException("Invalid index info: " + indexInfo);
        }

        int index;
        try {
            index = Integer.parseInt(indexSplit[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number in index info: " + indexInfo);
        }

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
}
