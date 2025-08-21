package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.*;

@UtilityClass
public class TrivialFillLogHelper {

    public static String generateLog(
            boolean isRow,
            int index,
            List<Integer> sequencesLengths,
            List<List<Integer>> sequencesRanges,
            List<String> initialLine,
            List<String> updatedLine
    ) {
        if (isRow) {
            System.out.println("IS ROW");
        } else {
            System.out.println("IS COLUMN");
        }
        return String.format(
                """
                        FILL_TRIVIAL_SEQUENCE_IN_%s: %s=%d
                        sequencesLengths=%s
                        sequencesRanges=%s
                        initialLine=%s
                        updatedLine=%s
                        """,
                isRow ? ROW_ACTION_NAME : COLUMN_ACTION_NAME,
                isRow ? ROW : COLUMN,
                index,
                sequencesLengths.toString(),
                sequencesRanges.toString(),
                initialLine.toString(),
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
                toImmutableIntListLiteral(lengthsLine),
                toMutableRangesListLiteral(rangesLine),
                toMutableStringListLiteral(initialLine),
                toMutableStringListLiteral(updatedLine)
        );
    }
}
