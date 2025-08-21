package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.common.HelpersConstants.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.*;

@UtilityClass
public class SequenceRangeCorrectionWhenMarkingFieldsLogHelper {

    public static String generateLog(
            boolean isRow,
            int index,
            int sequenceIndex,
            List<List<Integer>> sequencesRanges,
            List<Integer> sequencesLengths,
            List<Integer> updatedRange
    ) {
        return String.format(
                """
                        SEQUENCE_RANGE_CORRECTION_WHEN_MARKING_FIELDS_IN_%s: %s=%d
                        sequenceIndex=%s
                        sequencesRanges=%s
                        sequencesLengths=%s
                        updatedRange=%s
                        """,
                isRow ? ROW_ACTION_NAME : COLUMN_ACTION_NAME,
                isRow ? ROW : COLUMN,
                index,
                sequenceIndex,
                sequencesRanges.toString(),
                sequencesLengths.toString(),
                updatedRange.toString()
        );
    }

    public static String convertLogToTestArguments(String log, String solutionName) {
        String[] lines = log.split("\\n");

        boolean isRow = lines[0].contains(ROW_ACTION_NAME);
        String axisLabel = isRow ? ROW : COLUMN;

        String fileName = solutionName.replaceFirst("^r", "").replaceFirst("\\.json$", "");

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());
        int sequenceIndex = Integer.parseInt(lines[1].split("=")[1].trim());

        String sequencesRanges = extractValue(lines, "sequencesRanges");
        String sequencesLengths = extractValue(lines, "sequencesLengths");
        String updatedRange = extractValue(lines, "updatedRange");

        return String.format(
                """
                        Arguments.of("%s / %s=%d - sequences range correction when marking fields",
                            %d,
                            %s,
                            %s,
                            %s
                        )""",
                fileName,
                isRow ? ROW : COLUMN,
                index,
                sequenceIndex,
                toMutableRangesListLiteral(sequencesRanges),
                toImmutableIntListLiteral(sequencesLengths),
                toMutableIntListLiteral(updatedRange)
        );
    }
}

