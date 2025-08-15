package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.*;

@UtilityClass
public class SequenceRangeCorrectionWhenPlacingXsLogHelper {

    public static String generateLog(
            boolean isRow,
            int rowIdx,
            int sequenceIndex,
            List<List<Integer>> sequencesRanges,
            List<Integer> updatedRange,
            List<String> line,
            List<Integer> sequencesLengths
    ) {
        return String.format(
                """
                        %s_SEQUENCE_CORRECTION_WHEN_PLACING_X: %s=%d
                        sequenceIndex=%d
                        sequencesRanges=%s
                        updatedRange=%s
                        line=%s
                        sequencesLengths=%s
                        """,
                isRow ? ROW_ACTION_NAME : COLUMN_ACTION_NAME,
                isRow ? ROW : COLUMN,
                rowIdx,
                sequenceIndex,
                sequencesRanges,
                updatedRange,
                line,
                sequencesLengths
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

        String fileName = solutionName.startsWith("r") ? solutionName.substring(1) : solutionName;

        String sequenceIndex = lines[1].replace("sequenceIndex=", "").trim();
        String sequencesRanges = lines[2].replace("sequencesRanges=", "").trim();
        String updatedRange = lines[3].replace("updatedRange=", "").trim();
        String line = lines[4].replace("line=", "").trim();
        String sequencesLengths = lines[5].replace("sequencesLengths=", "").trim();

        return String.format(
                """
                        Arguments.of("%s / %s=%d - correct sequence range when placing X",
                            %s,
                            %s,
                            %s,
                            %s,
                            %s)
                        )""",
                fileName,
                isRow ? ROW : COLUMN,
                index,
                sequenceIndex,
                toMutableRangesList(sequencesRanges),
                toImmutableIntListLiteral(updatedRange),
                toImmutableStringListLiteral(line),
                toImmutableIntListLiteral(sequencesLengths)
        );
    }
}
