package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class SequenceRangeCorrectionWhenMarkingFieldsLogHelper {

    public static String generateLog(
            int index,
            List<List<Integer>> sequencesRanges,
            List<Integer> updatedRange,
            List<Integer> sequencesLengths,
            boolean isRow
    ) {
        return String.format(
                """
                        %s_SEQUENCE_RANGE_CORRECTION_WHEN_MARKING_FIELDS: %s=%d
                        sequencesRanges=%s
                        updatedRange=%s
                        sequencesLengths=%s
                        """,
                isRow ? "ROW" : "COLUMN",
                isRow ? "row" : "column",
                index,
                sequencesRanges.toString(),
                updatedRange.toString(),
                sequencesLengths.toString()
        );
    }

    public static String convertLogToTestArguments(String log, String solutionName) {
        String[] lines = log.split("\\n");

        boolean isRow = lines[0].startsWith("ROW_");
        String axisLabel = isRow ? "row" : "column";

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());

        String sequencesRanges = lines[1].contains("=") ? lines[1].split("=")[1].trim() : "";
        String updatedRange = lines[2].contains("=") ? lines[1].split("=")[1].trim() : "";
        String sequencesLengths = lines[3].contains("=") ? lines[1].split("=")[1].trim() : "";

        return String.format(
                """
                        Arguments.of("%s / %s=%d - sequences range correction when marking fields",
                            List.of(%s),
                            List.of(%s),
                            List.of(%s)
                        )""",
                solutionName,
                isRow ? "row" : "column",
                index,
                sequencesRanges,
                updatedRange,
                sequencesLengths
        );
    }
}

