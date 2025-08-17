package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.extractValue;

@UtilityClass
public class SequenceRangeCorrectionWhenMetColouredFieldsLogHelper {

    public static String generateLog(
            boolean isRow,
            int index,
            List<String> line,
            List<Integer> sequencesLengths,
            List<List<Integer>> initialRanges,
            List<List<Integer>> updatedRanges
    ) {

        return String.format(
                """
                        SEQUENCE_CORRECTION_WHEN_MET_COLOURED_FIELDS_IN_%s: %s=%d
                        line=%s
                        sequencesLengths=%s
                        initialRanges=%s
                        updatedRanges=%s
                        """,
                isRow ? ROW_ACTION_NAME : COLUMN_ACTION_NAME,
                isRow ? ROW : COLUMN,
                index,
                line,
                sequencesLengths,
                initialRanges,
                updatedRanges
        );
    }

    public static String convertLogToTestArguments(String log, String solutionName) {
        String[] lines = log.split("\\n");

        boolean isRow = lines[0].contains(ROW_ACTION_NAME);
        String axisLabel = isRow ? ROW : COLUMN;

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());

        String fileName = solutionName.replaceFirst("^r", "").replaceFirst("\\.json$", "");

        String line = extractValue(lines, "line");
        String sequencesLengths = extractValue(lines, "sequencesLengths");

        String initialRanges = extractValue(lines, "initialRanges");
        String updatedRanges = extractValue(lines, "updatedRanges");

        return String.format(
                """
                        Arguments.of("%s / %s=%d - sequences range correction when met coloured fields",
                            %s,
                            %s,
                            %s,
                            %s
                        ),""",
                fileName,
                isRow ? ROW : COLUMN,
                index,
                line,
                sequencesLengths,
                initialRanges,
                updatedRanges
        );
    }
}

