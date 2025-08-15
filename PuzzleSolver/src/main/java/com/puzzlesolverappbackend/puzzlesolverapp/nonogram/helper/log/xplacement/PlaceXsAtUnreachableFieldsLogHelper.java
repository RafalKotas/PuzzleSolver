package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.extractValue;

@UtilityClass
public class PlaceXsAtUnreachableFieldsLogHelper {

    private static final String LIST_OF_START_WITH_OPENING_BRACKET = "List.of(";

    public static String generateLog(
            int index,
            List<String> initialLine,
            List<String> updatedLine,
            List<List<Integer>> sequencesRanges,
            boolean isRow
    ) {
        return String.format(
                """
                        PLACE_XS_AT_UNREACHABLE_FIELDS_IN_%s: %s=%d
                        initialLine=%s
                        sequencesRanges=%s
                        updatedLine=%s
                        """,
                isRow ? "ROW" : "COLUMN",
                isRow ? "row" : "column",
                index,
                initialLine,
                sequencesRanges,
                updatedLine
        );
    }

    public static String convertLogToTestArguments(String log, String solutionName) {
        String[] lines = log.split("\\n");

        boolean isRow = lines[0].contains("ROW");
        String axisLabel = isRow ? "row" : "column";

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());

        String fileName = solutionName.replaceFirst("^r", "").replaceFirst("\\.json$", "");

        String initialLine = extractValue(lines, "initialLine");
        String sequencesRanges = extractValue(lines, "sequencesRanges");
        String updatedLine = extractValue(lines, "updatedLine");


        return String.format(
                """
                        Arguments.of("%s / %s=%d - sequences range correction",
                            %s,
                            %s,
                            %s)
                        )""",
                fileName,
                isRow ? "Row" : "Column",
                index,
                initialLine,
                sequencesRanges,
                updatedLine
        );
    }
}
