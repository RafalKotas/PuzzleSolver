package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.*;

@UtilityClass
public class SequencesRangesCorrectionLogHelper {

    public static String generateLog(
            boolean isRow,
            int index,
            List<Integer> sequencesLengths,
            List<Integer> excludedFields,
            List<Integer> excludedSequencesIndexes,
            List<List<Integer>> initialRanges,
            List<List<Integer>> updatedRanges
    ) {
        return String.format(
                """
                        %s_SEQUENCES_RANGES_CORRECTION: %s=%d
                        sequencesLengths=%s
                        excludedFields=%s
                        excludedSequencesIndexes=%s
                        initialRanges=%s
                        updatedRanges=%s
                        """,
                isRow ? "ROW" : "COLUMN",
                isRow ? "row" : "column",
                index,
                sequencesLengths.toString(),
                excludedFields.toString(),
                excludedSequencesIndexes.toString(),
                initialRanges.toString(),
                updatedRanges.toString()
        );
    }

    public static String convertLogToTestArguments(String log, String solutionName) {
        String[] lines = log.split("\\n");

        boolean isRow = lines[0].startsWith("ROW_");
        String axisLabel = isRow ? "row" : "column";

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());

        String fileName = solutionName.replaceFirst("^r", "").replaceFirst("\\.json$", "");

        String sequencesLengths = extractValue(lines, "sequencesLengths");
        String excludedFields = extractValue(lines, "excludedFields");
        String excludedSequencesIndexes = extractValue(lines, "excludedSequencesIndexes");
        String initialRanges = extractValue(lines, "initialRanges");
        String updatedRanges = extractValue(lines, "updatedRanges");

        return String.format(
                """
                        Arguments.of("%s / %s=%d - sequences range correction",
                            %s,
                            %s,
                            %s,
                            %s,
                            %s)
                        )""",
                fileName,
                isRow ? "Row" : "Column",
                index,
                toImmutableIntListLiteral(sequencesLengths),
                toMutableIntListLiteral(excludedFields),
                toMutableIntListLiteral(excludedSequencesIndexes),
                toMutableRangesListLiteral(initialRanges),
                toImmutableRangesListLiteral(updatedRanges)
        );
    }
}
