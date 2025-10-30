package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;

@UtilityClass
public class ColouringFieldsIfXWouldForceTooLongColouredFieldsSequenceLogHelper {

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
                        COLOURING_FIELDS_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE_IN_%s: %s=%d
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
        ColouringCovertLogBaseContext colouringCovertLogBaseContext = new ColouringCovertLogBaseContext(log, solutionName);

        return String.format(
                """
                        Arguments.of("%s / %s=%d - colouring fields if X would force too long coloured field sequence",
                            %s,
                            %s,
                            %s,
                            %s)
                        )""",
                colouringCovertLogBaseContext.getFileName(),
                colouringCovertLogBaseContext.getAxisLabel(),
                colouringCovertLogBaseContext.getIndex(),
                colouringCovertLogBaseContext.getInitialLine(),
                colouringCovertLogBaseContext.getSequencesRanges(),
                colouringCovertLogBaseContext.getSequencesLengths(),
                colouringCovertLogBaseContext.getUpdatedLine()
        );
    }
}
