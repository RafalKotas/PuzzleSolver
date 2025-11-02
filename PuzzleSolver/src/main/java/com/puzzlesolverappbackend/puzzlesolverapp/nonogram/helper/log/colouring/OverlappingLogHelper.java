package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring;

import lombok.experimental.UtilityClass;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;

@UtilityClass
public class OverlappingLogHelper {

    public static String generateLog(
            ColouringGenerateLogBaseContext context
    ) {
        return String.format(
                """
                        COLOUR_OVERLAPPING_FIELDS_IN_%s: %s=%d
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
                        Arguments.of("%s / %s=%d - colour overlapping fields",
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
