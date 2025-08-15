package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.*;

@UtilityClass
public class TooLongMergeLogHelper {

    public static String generateTooLongMergeSequenceLog(
            int index,
            boolean isRow,
            List<String> initialLine,
            List<List<Integer>> sequencesRanges,
            List<Integer> sequencesLengths,
            List<String> updatedLine
    ) {
        String label = isRow ? "ROW" : "COLUMN";

        return String.format(
                """
                        TOO_LONG_MERGE_%s_SEQUENCE: %s=%d
                        initialLine=%s
                        sequencesRanges=%s
                        sequencesLengths=%s
                        updatedLine=%s
                        """,
                label,
                isRow ? "row" : "column",
                index,
                initialLine.toString(),
                sequencesRanges.toString(),
                sequencesLengths.toString(),
                updatedLine.toString()
        );
    }

    public static String convertLogToTestArguments(
            String log,
            String solutionName
    ) {
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
                toMutableRangesList(initialRanges),
                toImmutableRangesList(updatedRanges)
        );
    }
}
