package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.exclusion;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.*;

@UtilityClass
public class ExcludedSequenceLogHelper {

    public static String generateLog(
            boolean isRow,
            int index,
            int sequenceIndex,
            List<String> line,
            List<Integer> sequenceLengths,
            List<List<Integer>> sequenceRanges
    ) {
        return String.format(
                """
                        EXCLUSION_SEQUENCE_IN_%s: %s=%d
                        sequenceIndex=%d
                        line=%s
                        sequencesLengths=%s
                        sequencesRanges=%s
                        """,
                isRow ? ROW_ACTION_NAME : COLUMN_ACTION_NAME,
                isRow ? ROW : COLUMN,
                index,
                sequenceIndex,
                line.toString(),
                sequenceLengths.toString(),
                sequenceRanges.toString()
        );
    }

    public static String convertLogToTestArguments(
            String log,
            String solutionName
    ) {
        String[] lines = log.split("\\n");

        boolean isRow = lines[0].contains(ROW_ACTION_NAME);
        String axisLabel = isRow ? ROW : COLUMN;

        String fileName = solutionName.replaceFirst("^r", "").replaceFirst("\\.json$", "");

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());
        int sequenceIndex = Integer.parseInt(lines[1].split("sequenceIndex=")[1].trim());
        String line = lines[2].replace("line=", "").trim();
        String sequencesLengths = lines[3].replace("sequencesLengths=", "").trim();
        String sequencesRanges = lines[4].replace("sequencesRanges=", "").trim();

        return String.format(
                """
                        Arguments.of("%s / %s=%d - excluding sequence",
                            %d,
                            %s,
                            %s,
                            %s)
                        )""",
                fileName,
                isRow ? ROW : COLUMN,
                index,
                sequenceIndex,
                toImmutableStringListLiteral(line),
                toImmutableIntListLiteral(sequencesLengths),
                toImmutableRangesListLiteral(sequencesRanges)
        );
    }
}
