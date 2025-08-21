package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.marking;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.extractValue;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.toMutableStringListLiteral;

@UtilityClass
public class MarkAvailableFieldsLogHelper {

    public static String generateLog(
            boolean isRow,
            int index,
            int sequenceIndex,
            String marker,
            List<String> initialLine,
            List<String> updatedLine
    ) {
        return String.format(
                """
                        MARK_AVAILABLE_FIELDS_IN_%s: %s=%d
                        sequenceIndex=%d
                        marker=%s
                        initialLine=%s
                        updatedLine=%s
                        """,
                isRow ? ROW_ACTION_NAME : COLUMN_ACTION_NAME,
                isRow ? ROW : COLUMN,
                index,
                sequenceIndex,
                marker,
                initialLine,
                updatedLine
        );
    }

    public static String convertLogToTestArguments(String log, String solutionName) {
        String[] lines = log.split("\\n");

        boolean isRow = lines[0].contains(ROW_ACTION_NAME);
        String axisLabel = isRow ? ROW : COLUMN;

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].split(",")[0].trim());

        String fileName = solutionName.replaceFirst("^r", "").replaceFirst("\\.json$", "");

        int sequenceIndex = Integer.parseInt(lines[1].split("sequenceIndex=")[1].split(",")[0].trim());
        String marker = extractValue(lines, "marker");
        String initialLine = extractValue(lines, "initialLine");
        String updatedLine = extractValue(lines, "updatedLine");

        return String.format(
                """
                        Arguments.of("%s / %s=%d - mark available fields",
                            %s,
                            %s,
                            %s,
                            %s
                        )""",
                fileName,
                isRow ? ROW : COLUMN,
                index,
                sequenceIndex,
                marker,
                toMutableStringListLiteral(initialLine),
                toMutableStringListLiteral(updatedLine)
        );
    }
}
