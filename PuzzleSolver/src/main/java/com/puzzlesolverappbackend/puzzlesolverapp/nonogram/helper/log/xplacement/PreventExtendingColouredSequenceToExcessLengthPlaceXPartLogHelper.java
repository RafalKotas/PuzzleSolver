package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.*;

@UtilityClass
public class PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper {

    public static String generateLog(
            PlaceXBaseLogContext context,
            String direction,
            int onlyValidSequenceIdx,
            List<Integer> sequencesLengths,
            List<List<Integer>> sequencesRanges
    ) {
        return String.format(
                """
                        PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART_IN_%s: %s=%d
                        initialLine=%s
                        updatedLine=%s
                        direction=%s
                        onlyValidSequenceIdx=%s
                        sequencesLengths=%s
                        sequencesRanges=%s
                        """,
                context.isRow() ? ROW_ACTION_NAME : COLUMN_ACTION_NAME,
                context.isRow() ? ROW : COLUMN,
                context.getIndex(),
                context.getInitialLine(),
                context.getUpdatedLine(),
                direction,
                onlyValidSequenceIdx,
                sequencesLengths,
                sequencesRanges
        );
    }

    public static String convertLogToTestArguments(
            String logText,
            String solutionName
    ) {
        String[] lines = logText.split("\\n");

        boolean isRow = lines[0].contains(ROW_ACTION_NAME);
        String axisLabel = isRow ? ROW : COLUMN;

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());

        String direction = lines[2].replace("direction=", "").trim();

        String fileName = solutionName.replaceFirst("^r", "").replaceFirst("\\.json$", "");

        String onlyValidSequenceIdx = lines[2].replace("onlyValidSequenceIdx=", "").trim();
        String sequencesLengths = lines[3].replace("sequencesLengths=", "").trim();
        String sequencesRanges = lines[4].replace("sequencesRanges=", "").trim();
        String initialLine = lines[5].replace("initialLine=", "").trim();
        String updatedLine = lines[6].replace("updatedLine=", "").trim();

        return String.format(
                """
                        Arguments.of("%s / %s=%d - prevent extending coloured sequence to excess length place X part",
                            direction=%s,
                            %s,
                            %s,
                            %s,
                            %s,
                            %s)
                        )""",
                fileName,
                isRow ? ROW : COLUMN,
                index,
                direction,
                onlyValidSequenceIdx,
                toImmutableIntListLiteral(sequencesLengths),
                toMutableRangesListLiteral(sequencesRanges),
                toMutableStringListLiteral(initialLine),
                toMutableStringListLiteral(updatedLine)
        );
    }
}
