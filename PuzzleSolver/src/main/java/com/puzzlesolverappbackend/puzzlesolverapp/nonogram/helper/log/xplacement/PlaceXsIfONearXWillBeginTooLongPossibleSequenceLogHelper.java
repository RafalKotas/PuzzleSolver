package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.extractValue;

@UtilityClass
public class PlaceXsIfONearXWillBeginTooLongPossibleSequenceLogHelper {

    private static final String LIST_STRING_FORMAT = "    List.of(%s),%n";

    public static String generateLog(
            int index,
            List<String> initialLine,
            List<String> updatedLine,
            List<Integer> sequencesLengths,
            List<List<Integer>> sequencesRanges,
            boolean isRow
    ) {
        return String.format(
                """
                        PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_%s: %s=%d
                        initialLine=%s
                        updatedLine=%s
                        sequencesLengths=%s
                        sequencesRanges=%s
                        """,
                isRow ? "ROW" : "COLUMN",
                isRow ? "row" : "column",
                index,
                initialLine.toString(),
                updatedLine.toString(),
                sequencesLengths.toString(),
                sequencesRanges.toString()
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
        String sequencesRanges = extractValue(lines, "sequencesRanges");

        return String.format(
                """
                        Arguments.of("%s / %s=%d - place X if O near X will begin too long possible sequence",
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
                sequencesRanges
        );
    }
}
