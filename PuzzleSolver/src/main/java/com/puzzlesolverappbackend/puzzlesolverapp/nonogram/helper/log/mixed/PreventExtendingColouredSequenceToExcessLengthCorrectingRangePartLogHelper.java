package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;

import java.util.List;

public class PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelper {

    public static String generateCorrectingRangePartLog(
            int index,
            boolean isRow,
            List<Integer> sequenceLengths,
            List<String> initialLine,
            List<String> updatedLine, // TODO - niepotrzebne(?)
            List<List<Integer>> initialRanges,
            List<List<Integer>> updatedRanges
    ) {
        return String.format(
                """
                        %s_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART: %s=%d
                        sequencesLengths=%s
                        initialLine=%s,
                        updatedLine=%s,
                        initialRanges=%s
                        updatedRanges=%s
                        """,
                isRow ? "ROW" : "COLUMN",
                isRow ? "row" : "column",
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
            String solutionName,
            NonogramLogic logic
    ) {
        String[] lines = logText.strip().split("\n");

        boolean isRow = lines[0].startsWith("ROW_");
        String axisLabel = isRow ? "row" : "column";

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());

        String sequencesLengths = lines[1].contains("=") ? lines[1].split("=")[1].trim() : "";
        String initialLine = lines[2].contains("=") ? lines[2].split("=", 2)[1].trim() : "";
        String updatedLine = lines[3].contains("=") ? lines[3].split("=", 2)[1].trim() : "";

        String initialRanges = lines[4].contains("=") ? lines[4].split("=", 2)[1].trim() : "";
        String updatedRanges = lines[5].contains("=") ? lines[5].split("=", 2)[1].trim() : "";

        return String.format(
                """
                    Arguments.of("%s / %s=%d - prevent extending coloured sequence to excess length correcting range part",
                        List.of(%s),
                        List.of(%s),
                        List.of(%s),
                        List.of(%s),
                        List.of(%s)
                    )""",
                solutionName,
                isRow ? "row" : "column",
                index,
                sequencesLengths,
                initialLine,
                updatedLine,
                initialRanges,
                updatedRanges
        );
    }
}
