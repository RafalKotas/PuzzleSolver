package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.*;

@UtilityClass
public class SequenceRangeCorrectionFromColouredEdgesLogHelper {

    public static String generateLog(
            boolean isRow,
            int index,
            List<List<Integer>> initialRanges,
            List<List<Integer>> updatedRanges,
            List<Integer> sequenceLengths,
            List<String> line
    ) {
        return String.format(
                """
                        CORRECTING_SEQUENCES_RANGES_FROM_COLOURED_EDGES_IN_%s: %s=%d
                        initialRanges=%s
                        updatedRanges=%s
                        sequencesLengths=%s
                        line=%s
                        """,
                isRow ? ROW_ACTION_NAME : COLUMN_ACTION_NAME,
                isRow ? ROW : COLUMN,
                index,
                initialRanges,
                updatedRanges,
                sequenceLengths,
                line
        );
    }

    public static String convertLogToTestArguments(String log, String solutionName) {
        String[] lines = log.split("\\n");

        boolean isRow = lines[0].contains(ROW_ACTION_NAME);
        String axisLabel = isRow ? ROW : COLUMN;

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());

        String fileName = solutionName.replaceFirst("^r", "").replaceFirst("\\.json$", "");

        String initialRanges = extractValue(lines, "initialRanges");
        String updatedRanges = extractValue(lines, "updatedRanges");
        String sequencesLengths = extractValue(lines, "sequencesLengths");
        String line = extractValue(lines, "line");

        return String.format(
                """
                        Arguments.of("%s / %s=%d - correct sequences ranges from coloured edges",
                            %s,
                            %s,
                            %s,
                            %s
                        )""",
                fileName,
                isRow ? ROW : COLUMN,
                index,
                toMutableRangesListLiteral(initialRanges),
                toImmutableRangesListLiteral(updatedRanges),
                toImmutableIntListLiteral(sequencesLengths),
                toMutableStringListLiteral(line)
        );
    }
}


