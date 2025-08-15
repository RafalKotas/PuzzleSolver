package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.*;

@UtilityClass
public class ExtendLogHelper {

    public static String generateLog(
            int index,
            String direction,
            List<String> initialLine,
            List<List<Integer>> sequencesRanges,
            List<Integer> sequencesLengths,
            List<String> updatedLine,
            boolean isRow
    ) {
        return String.format(
                """
                        EXTEND_%s_SEQUENCE: %s=%d
                        direction=%s
                        initialLine=%s
                        sequencesRanges=%s
                        sequencesLengths=%s
                        updatedLine=%s
                        """,
                isRow ? "ROW" : "COLUMN",
                isRow ? "row" : "column",
                index,
                direction,
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

        boolean isRow = lines[0].contains("ROW");
        String axisLabel = isRow ? "row" : "column";

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());

        String fileName = solutionName.replaceFirst("^r", "").replaceFirst("\\.json$", "");

        String initialLine = extractValue(lines, "initialLine");
        String sequencesRanges = extractValue(lines, "sequencesRanges");
        String sequencesLengths = extractValue(lines, "sequencesLengths");
        String updatedLine = extractValue(lines, "updatedLine");

        return String.format(
                """
                        Arguments.of("%s / %s=%d - extending coloured fields near X",
                            %s,
                            %s,
                            %s,
                            %s
                        )""",
                fileName,
                isRow ? "row" : "column",
                index,
                toMutableStringListLiteral(initialLine),
                toImmutableRangesListLiteral(sequencesRanges),
                toImmutableIntListLiteral(sequencesLengths),
                toImmutableStringListLiteral(updatedLine)
        );
    }
}
