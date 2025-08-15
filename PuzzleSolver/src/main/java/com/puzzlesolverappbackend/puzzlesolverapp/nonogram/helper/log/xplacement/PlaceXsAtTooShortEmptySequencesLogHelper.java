package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.extractValue;

@UtilityClass
public class PlaceXsAtTooShortEmptySequencesLogHelper {

    public static String generateLog(
            boolean isRow,
            int index,
            List<String> initialLine,
            List<String> updatedLine,
            List<Integer> sequencesLengths,
            List<Integer> excludedSequencesIndexes
    ) {
        return String.format(
                """
                        PLACE_XS_IN_%s_AT_TOO_SHORT_EMPTY_SEQUENCES: %s=%d
                        initialLine=%s
                        updatedLine=%s
                        sequencesLengths=%s
                        excludedSequencesIndexes=%s
                        """,
                isRow ? ROW_ACTION_NAME : COLUMN_ACTION_NAME,
                isRow ? ROW : COLUMN,
                index,
                initialLine.toString(),
                updatedLine.toString(),
                sequencesLengths.toString(),
                excludedSequencesIndexes.toString()
        );
    }

    public static String convertLogToTestArguments(String log, String solutionName) {
        String[] lines = log.split("\\n");

        boolean isRow = lines[0].contains(ROW_ACTION_NAME);
        String axisLabel = isRow ? ROW : COLUMN;

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());

        String fileName = solutionName.replaceFirst("^r", "").replaceFirst("\\.json$", "");

        String initialLine = extractValue(lines, "initialLine");
        String updatedLine = extractValue(lines, "updatedLine");
        String sequencesLengths = extractValue(lines, "sequencesLengths");
        String excludedSequencesIndexes = extractValue(lines, "excludedSequencesIndexes");

        return String.format(
                """
                        Arguments.of("%s / %s=%d - place X at too short empty sequences",
                            %s,
                            %s,
                            %s,
                            %s)
                        )""",
                fileName,
                isRow ? ROW : COLUMN,
                index,
                initialLine,
                updatedLine,
                sequencesLengths,
                excludedSequencesIndexes
        );
    }
}
