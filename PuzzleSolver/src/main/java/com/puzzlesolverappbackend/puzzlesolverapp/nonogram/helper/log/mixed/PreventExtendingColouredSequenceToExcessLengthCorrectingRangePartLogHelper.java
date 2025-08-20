package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.extractValue;

@UtilityClass
public class PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelper {

    public static String generateLog(
            boolean isRow,
            int index,
            List<Integer> sequenceLengths,
            List<String> initialLine,
            List<String> updatedLine, // TODO - not needed(?)
            List<List<Integer>> initialRanges,
            List<List<Integer>> updatedRanges
    ) {
        return String.format(
                """
                        PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART_IN_%s: %s=%d
                        sequencesLengths=%s
                        initialLine=%s,
                        updatedLine=%s,
                        initialRanges=%s
                        updatedRanges=%s
                        """,
                isRow ? ROW_ACTION_NAME : COLUMN_ACTION_NAME,
                isRow ? ROW : COLUMN,
                index,
                sequenceLengths.toString(),
                initialLine.toString(),
                updatedLine.toString(),
                initialRanges.toString(),
                updatedRanges.toString()
        );
    }

    public static String convertLogToTestArguments(
            String logText,
            String solutionName
    ) {
        String[] lines = logText.strip().split("\n");

        boolean isRow = lines[0].contains(ROW_ACTION_NAME);
        String axisLabel = isRow ? ROW : COLUMN;

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());

        String sequencesLengths = extractValue(lines, "sequencesLengths");
        String initialLine = extractValue(lines, "initialLine");
        String updatedLine = extractValue(lines, "updatedLine");

        String initialRanges = extractValue(lines, "initialRanges");
        String updatedRanges = extractValue(lines, "updatedRanges");

        return String.format(
                """
                       Arguments.of("%s / %s=%d - prevent extending coloured sequence to excess length correcting range part",
                           %s,
                           %s,
                           %s,
                           %s,
                           %s
                       )""",
                solutionName,
                isRow ? ROW : COLUMN,
                index,
                sequencesLengths,
                initialLine,
                updatedLine,
                initialRanges,
                updatedRanges
        );
    }
}
