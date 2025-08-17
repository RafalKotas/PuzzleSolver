package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.*;

@UtilityClass
public class PlaceXsAroundLongestSequenceLogHelper {

    public static String generateLog(
            boolean isRow,
            int index,
            List<Integer> xEdges,
            List<String> initialLine,
            List<String> updatedLine,
            boolean onlyMatching
    ) {
        return String.format(
                """
                        PLACE_XS_AROUND_LONGEST_SEQUENCE_IN_%s: %s=%d
                        xEdges=%s
                        onlyMatching=%b
                        initialLine=%s
                        updatedLine=%s
                        """,
                isRow ? ROW_ACTION_NAME : COLUMN_ACTION_NAME,
                isRow ? ROW : COLUMN,
                index,
                xEdges,
                onlyMatching,
                initialLine,
                updatedLine
        );
    }

    public static String convertLogToTestArguments(String log, String solutionName) {
        String[] lines = log.strip().split("\n");

        boolean isRow = lines[0].contains(ROW_ACTION_NAME);
        String axisLabel = isRow ? ROW : COLUMN;

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());

        String fileName = solutionName.replaceFirst("^r", "").replaceFirst("\\.json$", "");

        String xEdges = extractValue(lines, "xEdges");
        String onlyMatching = extractValue(lines, "onlyMatching");
        String initialLine = extractValue(lines, "initialLine");
        String updatedLine = extractValue(lines, "updatedLine");

        return String.format(
                """
                        Arguments.of("%s / %s=%d - place X around longest sequences",
                            %s,
                            %s,
                            %s,
                            %s
                        )""",
                fileName,
                isRow ? ROW : COLUMN,
                index,
                toImmutableIntListLiteral(xEdges),
                onlyMatching,
                toMutableStringListLiteral(initialLine),
                toMutableStringListLiteral(updatedLine)
        );
    }
}
