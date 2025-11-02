package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring;

import lombok.experimental.UtilityClass;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;

@UtilityClass
public class ColouringFieldsIfXWouldForceTooLongColouredFieldsSequenceLogHelper {

    public static String generateLog(
            ColouringGenerateLogBaseContext context
    ) {
        return String.format(
                """
                        COLOURING_FIELDS_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE_IN_%s: %s=%d
                        initialLine=%s
                        updatedLine=%s
                        sequencesRanges=%s
                        sequencesLengths=%s
                        """,
                context.isRow() ? ROW_ACTION_NAME : COLUMN_ACTION_NAME,
                context.isRow() ? ROW : COLUMN,
                context.getIndex(),
                context.getInitialLine(),
                context.getUpdatedLine(),
                context.getSequencesRanges(),
                context.getSequencesLengths()
        );
    }

    public static String convertLogToTestArguments(
            String log,
            String solutionName
    ) {
        ColouringConvertLogBaseContext colouringConvertLogBaseContext = new ColouringConvertLogBaseContext(log, solutionName);

        return String.format(
                """
                        Arguments.of("%s / %s=%d - colouring fields if X would force too long coloured field sequence",
                            %s,
                            %s,
                            %s,
                            %s)
                        )""",
                colouringConvertLogBaseContext.getFileName(),
                colouringConvertLogBaseContext.getAxisLabel(),
                colouringConvertLogBaseContext.getIndex(),
                colouringConvertLogBaseContext.getInitialLine(),
                colouringConvertLogBaseContext.getSequencesRanges(),
                colouringConvertLogBaseContext.getSequencesLengths(),
                colouringConvertLogBaseContext.getUpdatedLine()
        );
    }
}
