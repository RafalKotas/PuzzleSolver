package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.toImmutableIntListLiteral;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.toMutableRangesListLiteral;

@UtilityClass
public class SequenceRangeCorrectionWhenMarkingFieldsLogHelper {

    public static String generateLog(
            boolean isRow,
            int index,
            List<List<Integer>> sequencesRanges,
            List<Integer> updatedRange,
            List<Integer> sequencesLengths
    ) {
        return String.format(
                """
                        %s_SEQUENCE_RANGE_CORRECTION_WHEN_MARKING_FIELDS: %s=%d
                        sequencesRanges=%s
                        updatedRange=%s
                        sequencesLengths=%s
                        """,
                isRow ? ROW_ACTION_NAME : COLUMN_ACTION_NAME,
                isRow ? ROW : COLUMN,
                index,
                sequencesRanges.toString(),
                updatedRange.toString(),
                sequencesLengths.toString()
        );
    }

    public static String convertLogToTestArguments(String log, String solutionName) {
        String[] lines = log.split("\\n");

        boolean isRow = lines[0].contains(ROW_ACTION_NAME);
        String axisLabel = isRow ? ROW : COLUMN;

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());

        String sequencesRanges = lines[1].contains("=") ? lines[1].split("=")[1].trim() : "";
        String updatedRange = lines[2].contains("=") ? lines[2].split("=")[1].trim() : "";
        String sequencesLengths = lines[3].contains("=") ? lines[3].split("=")[1].trim() : "";

        return String.format(
                """
                        Arguments.of("%s / %s=%d - sequences range correction when marking fields",
                            %s,
                            %s,
                            %s
                        )""",
                solutionName,
                isRow ? ROW : COLUMN,
                index,
                toMutableRangesListLiteral(sequencesRanges),
                toImmutableIntListLiteral(updatedRange),
                toImmutableIntListLiteral(sequencesLengths)
        );
    }
}

