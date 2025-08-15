package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.extractValue;

@UtilityClass
public class PlaceXsAtTooShortEmptySequencesLogHelper {

    public static String generateLog(
            int index,
            List<String> initialLine,
            List<String> updatedLine,
            List<Integer> sequencesLengths,
            List<Integer> excludedSequencesIndexes,
            boolean isRow
    ) {
        return String.format(
                """
                        PLACE_XS_IN_%s_AT_TOO_SHORT_EMPTY_SEQUENCES: %s=%d
                        initialLine=%s
                        updatedLine=%s
                        sequencesLengths=%s
                        excludedSequencesIndexes=%s
                        """,
                isRow ? "ROW" : "COLUMN",
                isRow ? "row" : "column",
                index,
                initialLine.toString(),
                updatedLine.toString(),
                sequencesLengths.toString(),
                excludedSequencesIndexes.toString()
        );
    }

    public static String convertLogToTestArguments(String log, String solutionName, NonogramLogic logic) {
        String[] lines = log.split("\\n");

        boolean isRow = lines[0].startsWith("ROW_");
        String axisLabel = isRow ? "row" : "column";

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
                isRow ? "Row" : "Column",
                index,
                initialLine,
                updatedLine,
                sequencesLengths,
                excludedSequencesIndexes
        );
    }
}
