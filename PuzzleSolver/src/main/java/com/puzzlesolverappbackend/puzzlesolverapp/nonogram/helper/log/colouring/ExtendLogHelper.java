package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring;

import lombok.experimental.UtilityClass;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;

@UtilityClass
public class ExtendLogHelper {

    public static String generateLog(
            ColouringGenerateLogBaseContext context,
            String direction
    ) {
        return String.format(
                """
                        EXTEND_%s_SEQUENCE: %s=%d
                        direction=%s
                        initialLine=%s
                        updatedLine=%s
                        sequencesRanges=%s
                        sequencesLengths=%s
                        """,
                context.isRow() ? ROW_ACTION_NAME : COLUMN_ACTION_NAME,
                context.isRow() ? ROW : COLUMN,
                context.getIndex(),
                direction,
                context.getInitialLine(),
                context.getUpdatedLine(),
                context.getSequencesRanges(),
                context.getSequencesLengths()
        );
    }

    // TODO - include direction in log
    public static String convertLogToTestArguments(String log, String solutionName) {
        ColouringConvertLogBaseContext colouringConvertLogBaseContext = new ColouringConvertLogBaseContext(log, solutionName);

        return String.format(
                """
                        Arguments.of("%s / %s=%d - extending coloured fields near X",
                            %s,
                            %s,
                            %s,
                            %s
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
