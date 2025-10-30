package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.*;

@UtilityClass
public class AssignmentConflictLogHelper {

    public static String generateLog(
            int index,
            boolean isRow,
            List<String> initialLine,
            List<String> updatedLine,
            List<Integer> sequencesLengths,
            List<List<Integer>> sequencesRanges
    ) {
        return String.format(
                """
                        COLOURING_FIELDS_IF_X_WILL_CAUSE_ASSIGNMENT_CONFLICT_IN_%s: %s=%d
                        initialLine=%s
                        updatedLine=%s
                        sequencesRanges=%s
                        sequencesLengths=%s
                        """,
                isRow ? ROW_ACTION_NAME : COLUMN_ACTION_NAME,
                isRow ? ROW : COLUMN,
                index,
                initialLine.toString(),
                updatedLine.toString(),
                sequencesRanges.toString(),
                sequencesLengths.toString()
        );
    }

    public static String convertLogToTestArguments(
            String log,
            String solutionName
    ) {
        String[] lines = log.split("\\n");

        boolean isRow = lines[0].contains(ROW_ACTION_NAME);
        String axisLabel = isRow ? ROW : COLUMN;

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());

        String fileName = solutionName.replaceFirst("^r", "").replaceFirst("\\.json$", "");

        String initialLine = extractValue(lines, "initialLine");
        String updatedLine = extractValue(lines, "updatedLine");
        String sequencesRanges = extractValue(lines, "sequencesRanges");
        String sequencesLengths = extractValue(lines, "sequencesLengths");

        return String.format(
                """
                        Arguments.of("%s / %s=%d - colouring fields if X will cause assignment conflict",
                            %s,
                            %s,
                            %s,
                            %s)
                        )""",
                fileName,
                axisLabel,
                index,
                toMutableStringListLiteral(initialLine),
                toMutableStringListLiteral(updatedLine),
                toMutableRangesListLiteral(sequencesRanges),
                toImmutableIntListLiteral(sequencesLengths)
        );
    }
}
