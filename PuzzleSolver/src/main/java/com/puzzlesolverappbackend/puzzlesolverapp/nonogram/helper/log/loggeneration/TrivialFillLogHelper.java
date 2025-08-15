package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.extractValue;

@UtilityClass
public class TrivialFillLogHelper {

    public static String generateTrivialLineLog(
            int index,
            boolean isRow,
            List<String> initialLine,
            List<String> updatedLine,
            List<Integer> sequencesLengths,
            List<List<Integer>> sequencesRanges
    ) {
        String label = isRow ? "ROW" : "COLUMN";

        return String.format(
                """
                        FILL_TRIVIAL_%s_SEQUENCE: %s=%d
                        initialLine=%s
                        sequencesLengths=%s
                        sequencesRanges=%s
                        updatedLine=%s
                        """,
                label,
                isRow ? "row" : "column",
                index,
                initialLine.toString(),
                sequencesLengths.toString(),
                sequencesRanges.toString(),
                updatedLine.toString()
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
        int index = Integer.parseInt(headerParts[1].trim());

        String fileName = solutionName.replaceFirst("^r", "").replaceFirst("\\.json$", "");

        String initialLine = extractValue(lines, "initialLine");
        String lengthsLine = extractValue(lines, "sequencesLengths");
        String rangesLine = extractValue(lines, "sequencesRanges");
        String updatedLine = extractValue(lines, "updatedLine");

        String rowOrColumn = isRow ? "Row" : "Column";

        return String.format(
                """
                        Arguments.of("%s / %s=%d - trivial %s fill",
                            List.of(%s),
                            List.of(%s),
                            List.of(%s),
                            List.of(%s)
                        )""",
                fileName,
                rowOrColumn,
                index,
                rowOrColumn,
                initialLine,
                lengthsLine,
                rangesLine,
                updatedLine
        );
    }
}
