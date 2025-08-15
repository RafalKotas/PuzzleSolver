package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.exclusion;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.*;

@UtilityClass
public class ExcludedSequenceLogHelper {

    public static String generateExcludedSequenceLog(
            int index,
            int sequenceIndex,
            boolean isRow,
            List<String> line,
            List<Integer> sequenceLengths,
            List<List<Integer>> sequenceRanges
    ) {
        return String.format(
                """
                        EXCLUSION_%s_SEQUENCE: %s=%d
                        seq=%d
                        line=%s
                        sequencesLengths=%s
                        sequencesRanges=%s
                        """,
                isRow ? "ROW" : "COLUMN",
                isRow ? "row" : "column",
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

        boolean isRow = lines[0].startsWith("EXCLUSION_ROW");
        String axisLabel = isRow ? "row" : "column";

        String fileName = solutionName.startsWith("r") ? solutionName.substring(1) : solutionName;

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());
        int sequenceIdx = Integer.parseInt(lines[1].split("seq=")[1].trim());
        String line = lines[2].replace("line=", "").trim();
        String sequencesLengths = lines[3].replace("sequencesLengths=", "").trim();
        String sequencesRanges = lines[4].replace("sequencesRanges=", "").trim();

        return String.format(
                """
                        Arguments.of("%s / %s=%d - excluding sequence",
                            %d
                            %s,
                            %s,
                            %s)
                        )""",
                fileName,
                isRow ? "Row" : "Column",
                index,
                sequenceIdx,
                toImmutableStringListLiteral(line),
                toImmutableIntListLiteral(sequencesLengths),
                toImmutableRangesListLiteral(sequencesRanges)
        );
    }
}
