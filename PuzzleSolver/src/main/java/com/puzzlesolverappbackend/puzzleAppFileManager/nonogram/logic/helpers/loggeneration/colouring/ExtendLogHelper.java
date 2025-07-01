package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.colouring;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramLogic;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.LogFormatUtils.formatList;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.LogFormatUtils.formatNestedList;

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

    public static String convertLogToTestArguments(
            String logText,
            String solutionName,
            NonogramLogic logic
    ) {
        String[] lines = logText.strip().split("\n");

        String header = lines[0].replace("EXTEND_", "").replace("_SEQUENCE:", "").trim(); // e.g. "COLUMN col=2, dir=toBottom"
        String[] headerParts = header.split(", ");
        String indexInfo = headerParts[0]; // "row=14" or "col=2"
        String direction = headerParts[1].split("=")[1]; // e.g. "toBottom"

        int indexNumber = Integer.parseInt(indexInfo.split("=")[1]);
        String isRow = indexInfo.startsWith("row") ? "Row" : "Column";

        String fileName = solutionName.startsWith("r") ? solutionName.substring(1) : solutionName;

        int height = logic.getNonogramRules().getHeight();
        int width = logic.getNonogramRules().getWidth();

        String testLabel = String.format(
                "%s / %dx%d / diff  / %s %d / %s",
                fileName,
                height,
                width,
                isRow,
                indexNumber,
                direction
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
