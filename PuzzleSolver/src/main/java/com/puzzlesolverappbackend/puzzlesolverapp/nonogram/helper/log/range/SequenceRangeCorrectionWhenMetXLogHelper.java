package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.extractValue;

@UtilityClass
public class SequenceRangeCorrectionWhenMetXLogHelper {

    public static String generateLog(
            boolean isRow,
            int index,
            List<String> line,
            List<List<Integer>> initialRanges,
            List<List<Integer>> updatedRanges,
            List<Integer> sequencesLengths,
            List<Integer> excludedSequencesIndexes

    ) {
        return String.format(
                """
                        %s_SEQUENCES_RANGES_CORRECTION_IF_X_ON_WAY: %s=%d
                        line=%s
                        initialRanges=%s
                        updatedRanges=%s
                        sequencesLengths=%s
                        excludedSequencesIndexes=%s
                        """,
                isRow ? ROW_ACTION_NAME : COLUMN_ACTION_NAME,
                isRow ? ROW : COLUMN,
                index,
                line.toString(),
                initialRanges.toString(),
                updatedRanges.toString(),
                sequencesLengths.toString(),
                excludedSequencesIndexes.toString()
        );
    }

    public static String convertLogToTestArguments(String log, String solutionName) {
        String[] lines = log.split("\n");

        boolean isRow = lines[0].contains(ROW_ACTION_NAME);
        String axisLabel = isRow ? ROW : COLUMN;

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());

        String fileName = solutionName.replaceFirst("^r", "").replaceFirst("\\.json$", "");

        String initialRanges = extractValue(lines, "initialRanges");
        String updatedRanges = extractValue(lines, "updatedRanges");

        String sequenceLengths = extractValue(lines, "sequencesLengths");
        String excludedSequencesIndexes = extractValue(lines, "excludedSequencesIndexes");


        return String.format(
                """
                        Arguments.of("%s / %s=%d - sequences range correction if X on way",
                            %s,
                            %s,
                            %s,
                            %s)
                        )""",
                fileName,
                isRow ? ROW : COLUMN,
                index,
                initialRanges,
                updatedRanges,
                sequenceLengths,
                excludedSequencesIndexes
        );
    }

}
