package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.*;

@UtilityClass
public class OverlappingLogHelper {

    public static String generateLog(
            boolean isRow,
            int index,
            List<String> initialLine,
            List<List<Integer>> sequencesRanges,
            List<Integer> sequencesLengths,
            List<String> updatedLine
    ) {
        return String.format(
                """
                        COLOUR_OVERLAPPING_FIELDS_IN_%s: %s=%d
                        initialLine=%s
                        sequencesRanges=%s
                        sequencesLengths=%s
                        updatedLine=%s
                        """,
                isRow ? ROW_ACTION_NAME : COLUMN_ACTION_NAME,
                isRow ? ROW : COLUMN,
                index,
                initialLine.toString(),
                sequencesRanges.toString(),
                sequencesLengths.toString(),
                updatedLine.toString()
        );
    }

    public static String convertLogToTestArguments(
            String log,
            String solutionName
    ) {
        String[] lines = log.split("\\n");

        boolean isRow = lines[0].contains(ROW_ACTION_NAME);
        String axisLabel = isRow ? ROW : COLUMN;

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());

        String fileName = solutionName.startsWith("r") ? solutionName.substring(1) : solutionName;

        String initialLine = lines[1].replace("initialLine=", "").trim();
        String sequencesRanges = lines[2].replace("sequencesRanges=", "").trim();
        String sequencesLengths = lines[3].replace("sequencesLengths=", "").trim();
        String updatedLine = lines[4].replace("updatedLine=", "").trim();

        return String.format(
                """
                        Arguments.of("%s / %s=%d - colour overlapping fields",
                            %s,
                            %s,
                            %s,
                            %s)
                        )""",
                fileName,
                isRow ? ROW : COLUMN,
                index,
                toMutableStringListLiteral(initialLine),
                toImmutableRangesListLiteral(sequencesRanges),
                toImmutableIntListLiteral(sequencesLengths),
                toImmutableStringListLiteral(updatedLine)
        );
    }
}
