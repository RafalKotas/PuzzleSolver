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
            List<Integer> initialRange,
            List<Integer> updatedRange,
            List<String> line,
            List<Integer> sequencesLengths
    ) {
        return String.format(
                """
                        SEQUENCE_CORRECTION_WHEN_PLACING_X_IN_%s: %s=%d
                        sequenceIndex=%d
                        initialRange=%s
                        updatedRange=%s
                        line=%s
                        sequencesLengths=%s
                        """,
                isRow ? ROW_ACTION_NAME : COLUMN_ACTION_NAME,
                isRow ? ROW : COLUMN,
                rowIdx,
                sequenceIndex,
                initialRange,
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

        String fileName = solutionName.replaceFirst("^r", "").replaceFirst("\\.json$", "");

        String sequenceIndex = lines[1].replace("sequenceIndex=", "").trim();
        String initialRange = lines[2].replace("initialRange=", "").trim();
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
                toMutableIntListLiteral(initialRange),
                toMutableIntListLiteral(updatedRange),
                toMutableStringListLiteral(line),
                toImmutableIntListLiteral(sequencesLengths)
        );
    }
}
