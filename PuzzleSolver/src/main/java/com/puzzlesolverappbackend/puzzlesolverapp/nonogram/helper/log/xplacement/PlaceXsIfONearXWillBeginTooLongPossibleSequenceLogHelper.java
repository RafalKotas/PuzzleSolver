package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.*;

@UtilityClass
public class PlaceXsIfONearXWillBeginTooLongPossibleSequenceLogHelper {

    public static String generateLog(
            PlaceXGenerateLogBaseContext context,
            List<Integer> sequencesLengths,
            List<List<Integer>> sequencesRanges
    ) {
        return String.format(
                """
                        PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_%s: %s=%d
                        initialLine=%s
                        updatedLine=%s
                        sequencesLengths=%s
                        sequencesRanges=%s
                        """,
                context.isRow() ? ROW_ACTION_NAME : COLUMN_ACTION_NAME,
                context.isRow() ? ROW : COLUMN,
                context.getIndex(),
                context.getInitialLine(),
                context.getUpdatedLine(),
                sequencesLengths.toString(),
                sequencesRanges.toString()
        );
    }

    public static String convertLogToTestArguments(String log, String solutionName) {
        String[] lines = log.split("\\n");

        boolean isRow = lines[0].contains(ROW_ACTION_NAME);
        String axisLabel = isRow ? ROW : COLUMN;

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());

        String fileName = solutionName.replaceFirst("^r", "").replaceFirst("\\.json$", "");

        String initialLine = extractValue(lines, "initialLine");
        String updatedLine = extractValue(lines, "updatedLine");
        String sequencesLengths = extractValue(lines, "sequencesLengths");
        String sequencesRanges = extractValue(lines, "sequencesRanges");

        return String.format(
                """
                        Arguments.of("%s / %s=%d - place X if O near X will begin too long possible sequence",
                            %s,
                            %s,
                            %s,
                            %s)
                        )""",
                fileName,
                isRow ? ROW : COLUMN,
                index,
                toMutableStringListLiteral(initialLine),
                toImmutableStringListLiteral(updatedLine),
                toImmutableIntListLiteral(sequencesLengths),
                toMutableRangesListLiteral(sequencesRanges)
        );
    }
}
