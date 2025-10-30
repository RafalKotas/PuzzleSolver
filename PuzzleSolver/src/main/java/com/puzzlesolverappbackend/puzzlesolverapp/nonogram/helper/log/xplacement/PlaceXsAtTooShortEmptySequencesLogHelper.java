package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.*;

@UtilityClass
public class PlaceXsAtTooShortEmptySequencesLogHelper {

    public static String generateLog(
            PlaceXBaseLogContext context,
            List<List<Integer>> sequencesRanges,
            List<Integer> sequencesLengths,
            List<Integer> excludedSequencesIndexes
    ) {
        return String.format(
                """
                        PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES_IN_%s: %s=%d
                        initialLine=%s
                        updatedLine=%s
                        sequencesRanges=%s
                        sequencesLengths=%s
                        excludedSequencesIndexes=%s
                        """,
                context.isRow() ? ROW_ACTION_NAME : COLUMN_ACTION_NAME,
                context.isRow() ? ROW : COLUMN,
                context.getIndex(),
                context.getInitialLine(),
                context.getUpdatedLine(),
                sequencesRanges.toString(),
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
        String sequencesRanges = extractValue(lines, "sequencesRanges");
        String sequencesLengths = extractValue(lines, "sequencesLengths");
        String excludedSequencesIndexes = extractValue(lines, "excludedSequencesIndexes");

        return String.format(
                """
                        Arguments.of("%s / %s=%d - place X at too short empty sequences",
                            %s,
                            %s,
                            %s,
                            %s,
                            %s)
                        )""",
                fileName,
                isRow ? ROW : COLUMN,
                index,
                toMutableStringListLiteral(initialLine),
                toImmutableStringListLiteral(updatedLine),
                toMutableRangesListLiteral(sequencesRanges),
                toImmutableIntListLiteral(sequencesLengths),
                toMutableIntListLiteral(excludedSequencesIndexes)
        );
    }
}
