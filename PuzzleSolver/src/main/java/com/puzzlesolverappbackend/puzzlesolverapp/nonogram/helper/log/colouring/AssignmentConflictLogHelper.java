package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;

@UtilityClass
public class AssignmentConflictLogHelper {

    public static String generateLog(
            int index,
            boolean isRow,
            List<String> initialLine,
            List<String> updatedLine,
            List<Integer> sequencesLengths,
            List<List<Integer>> sequencesRanges
    ) {
        return String.format(
                """
                        COLOURING_FIELDS_IF_X_WILL_CAUSE_ASSIGNMENT_CONFLICT_IN_%s: %s=%d
                        initialLine=%s
                        updatedLine=%s
                        sequencesRanges=%s
                        sequencesLengths=%s
                        """,
                isRow ? ROW_ACTION_NAME : COLUMN_ACTION_NAME,
                isRow ? ROW : COLUMN,
                index,
                initialLine.toString(),
                updatedLine.toString(),
                sequencesRanges.toString(),
                sequencesLengths.toString()
        );
    }

    public static String convertLogToTestArguments(
            String log,
            String solutionName
    ) {
        ColouringCovertLogBaseContext colouringCovertLogBaseContext = new ColouringCovertLogBaseContext(log, solutionName);

        return String.format(
                """
                        Arguments.of("%s / %s=%d - colouring fields if X will cause assignment conflict",
                            %s,
                            %s,
                            %s,
                            %s)
                        )""",
                colouringCovertLogBaseContext.getFileName(),
                colouringCovertLogBaseContext.getAxisLabel(),
                colouringCovertLogBaseContext.getIndex(),
                colouringCovertLogBaseContext.getInitialLine(),
                colouringCovertLogBaseContext.getUpdatedLine(),
                colouringCovertLogBaseContext.getSequencesRanges(),
                colouringCovertLogBaseContext.getSequencesLengths()
        );
    }
}
