package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.extractValue;

@UtilityClass
public class SequenceRangeCorrectionWhenMatchingFieldsToSequencesLogHelper {

    public static String generateLog(
            boolean isRow,
            int index,
            List<Integer> sequencesLengths,
            List<String> line,
            List<List<Integer>> initialRanges,
            List<List<Integer>> updatedRanges
    ) {
        return String.format(
                """
                        %s_CORRECTING_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES: %s=%d
                        sequencesLengths=%s
                        line=%s
                        initialRanges=%s
                        updatedRanges=%s
                        """,
                isRow ? "ROW" : "COLUMN",
                isRow ? "row" : "col",
                index,
                sequencesLengths,
                line,
                initialRanges,
                updatedRanges
        );
    }

    public static String convertLogToTestArguments(String log, String solutionName, NonogramLogic logic) {
        String[] lines = log.split("\\n");

        boolean isRow = lines[0].startsWith("ROW_");
        String axisLabel = isRow ? "row" : "column";

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());

        String fileName = solutionName.replaceFirst("^r", "").replaceFirst("\\.json$", "");

        String sequencesLengths = extractValue(lines, "sequencesLengths");
        String line = extractValue(lines, "line");

        String initialRanges = extractValue(lines, "initialRanges");
        String updatedRanges = extractValue(lines, "updatedRanges");

        return String.format(
                """
                        Arguments.of("%s / %s=%d - sequences range correction when matching fields to sequences",
                            List.of(%s),
                            List.of(%s),
                            List.of(%s),
                            List.of(%s)
                        )""",
                fileName,
                isRow ? "Row" : "Column",
                index,
                sequencesLengths,
                line,
                initialRanges,
                updatedRanges
        );
    }
}
