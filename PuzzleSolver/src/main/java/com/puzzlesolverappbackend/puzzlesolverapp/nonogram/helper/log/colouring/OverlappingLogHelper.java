package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;

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
        ColouringLogContext colouringLogContext = new ColouringLogContext(log, solutionName);

        return String.format(
                """
                        Arguments.of("%s / %s=%d - colour overlapping fields",
                            %s,
                            %s,
                            %s,
                            %s)
                        )""",
                colouringLogContext.getFileName(),
                colouringLogContext.getAxisLabel(),
                colouringLogContext.getIndex(),
                colouringLogContext.getInitialLine(),
                colouringLogContext.getSequencesRanges(),
                colouringLogContext.getSequencesLengths(),
                colouringLogContext.getUpdatedLine()
        );
    }
}
