package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.extractValue;

@UtilityClass
public class SequenceRangeCorrectionFromColouredEdgesLogHelper {

    public static String generateLog(
            int index,
            List<List<Integer>> initialRanges,
            List<List<Integer>> updatedRanges,
            List<Integer> sequenceLengths,
            List<String> line,
            boolean isRow
    ) {
        return String.format(
                """
                        %s_CORRECTING_SEQUENCES_RANGES_FROM_COLOURED_EDGES: %s=%d
                        initialRanges=%s
                        updatedRanges=%s
                        sequencesLengths=%s
                        line=%s
                        """,
                isRow ? "ROW" : "COLUMN",
                isRow ? "row" : "column",
                index,
                initialRanges,
                updatedRanges,
                sequenceLengths,
                line
        );
    }

    public static String convertLogToTestArguments(String log, String solutionName) {
        String[] lines = log.split("\\n");

        boolean isRow = lines[0].startsWith("ROW_");
        String axisLabel = isRow ? "row" : "column";

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());
        String initialRanges = extractValue(lines, "initialRanges");
        String updatedRanges = extractValue(lines, "updatedRanges");
        String sequencesLengths = extractValue(lines, "sequencesLengths");
        String line = extractValue(lines, "line");

        return String.format(
                """
                        Arguments.of("%s / %s %d - corrected ranges from coloured edges",
                            List.of(%s),
                            List.of(%s),
                            List.of(%s),
                            List.of(%s),
                            %s
                        )""",
                solutionName,
                isRow ? "Row" : "Column",
                index,
                initialRanges,
                updatedRanges,
                sequencesLengths,
                line,
                isRow
        );
    }
}


