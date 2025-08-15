package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils.extractValue;

@UtilityClass
public class SequenceRangeCorrectionWhenMetColouredFieldsLogHelper {

    public static String generateLog(
            boolean isRow,
            int index,
            List<String> line,
            List<Integer> sequencesLengths,
            List<List<Integer>> initialRanges,
            List<List<Integer>> updatedRanges
    ) {

        return String.format(
                """
                        %s_SEQUENCE_CORRECTION_WHEN_MET_COLOURED_FIELDS: %s=%d
                        line=%s
                        sequencesLengths=%s
                        initialRanges=%s
                        updatedRanges=%s
                        """,
                isRow ? "ROW" : "COLUMN",
                isRow ? "row" : "column",
                index,
                line,
                sequencesLengths,
                initialRanges,
                updatedRanges
        );
    }

    public static String convertLogToTestArguments(String log, String solutionName, NonogramLogic logic) {
        String[] lines = log.split("\\n");

        boolean isRow = lines[0].startsWith("ROW_");
        String axisLabel = isRow ? "row" : "column";

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());

        String fileName = solutionName.replaceFirst("^r", "").replaceFirst("\\.json$", "");

        String line = extractValue(lines, "line");
        String sequencesLengths = extractValue(lines, "sequencesLengths");

        String initialRanges = extractValue(lines, "initialRanges");
        String updatedRanges = extractValue(lines, "updatedRanges");

        return String.format(
                """
                        Arguments.of("%s / %s=%d - sequences range correction when met coloured fields",
                            %s,
                            %s,
                            %s,
                            %s
                        ),""",
                fileName,
                isRow ? "Row" : "Column",
                index,
                line,
                sequencesLengths,
                initialRanges,
                updatedRanges
        );
    }
}

