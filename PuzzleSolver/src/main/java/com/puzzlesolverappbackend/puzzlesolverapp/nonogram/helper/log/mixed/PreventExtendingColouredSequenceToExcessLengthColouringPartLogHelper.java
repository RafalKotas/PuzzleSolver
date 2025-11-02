package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.ColouringConvertLogBaseContext;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.ColouringGenerateLogBaseContext;
import lombok.experimental.UtilityClass;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.extractValue;

@UtilityClass
public class PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper {

    public static String generateLog(
            ColouringGenerateLogBaseContext context,
            String direction
    ) {
        return String.format(
                """
                        PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART_IN_%s: %s=%d
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
        String[] lines = log.split("\\n");
        String direction = extractValue(lines, "direction");

        return String.format(
                """
                        Arguments.of("%s / %s=%d - prevent extending coloured sequence to excess length colouring part",
                            %s,
                            %s,
                            %s,
                            %s,
                            %s
                        )""",
                colouringConvertLogBaseContext.getFileName(),
                colouringConvertLogBaseContext.getAxisLabel(),
                colouringConvertLogBaseContext.getIndex(),
                direction,
                colouringConvertLogBaseContext.getInitialLine(),
                colouringConvertLogBaseContext.getSequencesRanges(),
                colouringConvertLogBaseContext.getSequencesLengths(),
                colouringConvertLogBaseContext.getUpdatedLine()
        );
    }
}
