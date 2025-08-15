package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.extractValue;

@UtilityClass
public class SequenceRangeCorrectionWhenMetXLogHelper {

    private static final String LIST_STRING_FORMAT = "    List.of(%s),%n";

    public static String generateLog(
            int index,
            List<String> line,
            List<List<Integer>> initialRanges,
            List<List<Integer>> updatedRanges,
            List<Integer> sequenceLengths,
            List<Integer> excludedSequencesIndexes,
            boolean isRow
    ) {
        return String.format(
                """
                        %s_SEQUENCES_RANGES_CORRECTION_IF_X_ON_WAY: %s=%d
                        line=%s
                        initialRanges=%s
                        updatedRanges=%s
                        sequenceLengths=%s
                        excludedSequencesIndexes=%s
                        """,
                isRow ? "ROW" : "COLUMN",
                isRow ? "row" : "column",
                index,
                line.toString(),
                initialRanges.toString(),
                updatedRanges.toString(),
                sequenceLengths.toString(),
                excludedSequencesIndexes.toString()
        );
    }

    public static String convertLogToTestArguments(String log, String solutionName, NonogramLogic logic) {
        String[] lines = log.split("\n");

        boolean isRow = lines[0].startsWith("ROW_");
        String axisLabel = isRow ? "row" : "column";

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());

        String fileName = solutionName.replaceFirst("^r", "").replaceFirst("\\.json$", "");

        String initialRanges = extractValue(lines, "initialRanges");
        String updatedRanges = extractValue(lines, "updatedRanges");

        String sequenceLengths = extractValue(lines, "sequenceLengths");
        String excludedSequencesIndexes = extractValue(lines, "excludedSequencesIndexes");


        return String.format(
                """
                        Arguments.of("%s / %s=%d - sequences range correction",
                            %s,
                            %s,
                            %s,
                            %s)
                        )""",
                fileName,
                isRow ? "row" : "column",
                index,
                initialRanges,
                updatedRanges,
                sequenceLengths,
                excludedSequencesIndexes
        );
    }

}
