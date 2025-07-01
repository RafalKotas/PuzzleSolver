package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.exclusion;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramLogic;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.LogFormatUtils.formatList;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.LogFormatUtils.formatNestedList;

@UtilityClass
public class ExcludedSequenceLogHelper {

    public static String generateExcludedSequenceLog(
            int index,
            int sequenceIndex,
            boolean isRow,
            List<String> state,
            List<Integer> sequenceLengths,
            List<List<Integer>> sequenceRanges
    ) {
        String label = isRow ? "EXCLUDED_ROW_SEQUENCE" : "EXCLUDED_COLUMN_SEQUENCE";
        String indexLabel = isRow ? "row" : "col";

        return String.format(
                "%s: %s=%d seq=%d\nstate=%s\nlengths=%s\nranges=%s\n",
                label,
                indexLabel,
                index,
                sequenceIndex,
                state.toString(),
                sequenceLengths.toString(),
                sequenceRanges.toString()
        );
    }

    public static String convertLogToTestArguments(
            String logText,
            String solutionName,
            NonogramLogic logic
    ) {
        String[] lines = logText.strip().split("\n");

        String header = lines[0]
                .replace("EXCLUDED_ROW_SEQUENCE:", "")
                .replace("EXCLUDED_COLUMN_SEQUENCE:", "")
                .trim();

        Map<String, String> params = Arrays.stream(header.split(" "))
                .map(part -> part.split("="))
                .filter(parts -> parts.length == 2)
                .collect(Collectors.toMap(p -> p[0].trim(), p -> p[1].trim()));

        boolean isRow = header.contains("row=");
        String label = isRow ? "Row" : "Column";

        int index = Integer.parseInt(params.get(isRow ? "row" : "col"));
        int seq = Integer.parseInt(params.get("seq"));

        String fileName = solutionName.replace(".json", "");
        if (fileName.startsWith("r")) {
            fileName = fileName.substring(1);
        }

        int height = logic.getNonogramRules().getHeight();
        int width = logic.getNonogramRules().getWidth();

        String testLabel = String.format(
                "%s / %dx%d / diff  / %s %d - seq %d",
                fileName,
                height,
                width,
                label,
                index,
                seq
        );

        String stateLine = lines[1].replace("state=", "").trim();
        String lengthsLine = lines[2].replace("lengths=", "").trim();
        String rangesLine = lines[3].replace("ranges=", "").trim();

        return String.format("""
        Arguments.of("%s",
            List.of(%s),
            List.of(%s),
            List.of(%s),
            List.of(%s)
        )""",
                testLabel,
                formatList(stateLine),
                formatNestedList(rangesLine),
                formatList(lengthsLine),
                formatList(stateLine)
        );
    }
}
