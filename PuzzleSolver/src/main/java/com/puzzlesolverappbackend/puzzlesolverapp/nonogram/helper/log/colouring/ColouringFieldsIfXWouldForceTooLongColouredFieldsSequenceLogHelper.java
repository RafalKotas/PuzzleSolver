package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.extractValue;

@UtilityClass
public class ColouringFieldsIfXWouldForceTooLongColouredFieldsSequenceLogHelper {

    public static String generateLog(
            boolean isRow,
            int index,
            List<String> initialLine,
            List<List<Integer>> sequencesRanges,
            List<Integer> sequencesLengths,
            List<String> updatedLine
    ) {
        return String.format(
                """
                        COLOURING_FIELDS_IN_%s_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE: %s=%d
                        initialLine=%s
                        sequencesRanges=%s
                        sequencesLengths=%s
                        updatedLine=%s
                        """,
                isRow ? "ROW" : "COLUMN",
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

        String initialLine = extractValue(lines, "initialLine");
        String sequencesRanges = extractValue(lines, "sequencesRanges");
        String sequencesLengths = extractValue(lines, "sequencesLengths");
        String updatedLine = extractValue(lines, "updatedLine");

        return String.format(
                """
                        Arguments.of("%s / %s=%d - colouring fields if X would force too long coloured field sequence",
                            %s,
                            %s,
                            %s,
                            %s)
                        )""",
                fileName,
                isRow ? "row" : "column",
                index,
                initialLine,
                sequencesRanges,
                sequencesLengths,
                updatedLine
        );
    }
}
