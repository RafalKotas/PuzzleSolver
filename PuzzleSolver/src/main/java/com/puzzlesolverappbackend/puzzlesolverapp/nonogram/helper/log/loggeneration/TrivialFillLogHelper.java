package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.extractValue;

@UtilityClass
public class TrivialFillLogHelper {

    public static String generateTrivialLineLog(
            boolean isRow,
            int index,
            List<String> initialLine,
            List<String> updatedLine,
            List<Integer> sequencesLengths,
            List<List<Integer>> sequencesRanges
    ) {
        return String.format(
                """
                        FILL_TRIVIAL_%s_SEQUENCE: %s=%d
                        initialLine=%s
                        sequencesLengths=%s
                        sequencesRanges=%s
                        updatedLine=%s
                        """,
                isRow ? ROW_ACTION_NAME : COLUMN_ACTION_NAME,
                isRow ? ROW : COLUMN,
                index,
                initialLine.toString(),
                sequencesLengths.toString(),
                sequencesRanges.toString(),
                updatedLine.toString()
        );
    }

    public static String convertLogToTestArguments(String log, String solutionName) {
        String[] lines = log.split("\\n");

        boolean isRow = lines[0].contains(ROW_ACTION_NAME);
        String axisLabel = isRow ? ROW : COLUMN;

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());

        String fileName = solutionName.replaceFirst("^r", "").replaceFirst("\\.json$", "");

        String initialLine = extractValue(lines, "initialLine");
        String lengthsLine = extractValue(lines, "sequencesLengths");
        String rangesLine = extractValue(lines, "sequencesRanges");
        String updatedLine = extractValue(lines, "updatedLine");

        return String.format(
                """
                        Arguments.of("%s / %s=%d - trivial %s fill",
                            List.of(%s),
                            List.of(%s),
                            List.of(%s),
                            List.of(%s)
                        )""",
                fileName,
                isRow ? ROW : COLUMN,
                index,
                isRow ? ROW : COLUMN,
                initialLine,
                lengthsLine,
                rangesLine,
                updatedLine
        );
    }
}
