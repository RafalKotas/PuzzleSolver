package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.marking;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class MarkAvailableFieldsLogHelper {

    private static final String LIST_STRING_FORMAT = "    List.of(%s),%n";

    public static String generateLog(
            int index,
            List<String> initialLine,
            List<String> updatedLine,
            int sequenceIdx,
            String marker,
            boolean isRow
    ) {
        return String.format(
                """
                        MARK_AVAILABLE_FIELDS_IN_%s: %s=%d, sequenceIdx=%d, marker=%s
                        initialLine=%s
                        updatedLine=%s
                        """,
                isRow ? "ROW" : "COLUMN",
                isRow ? "row" : "column",
                index,
                sequenceIdx,
                marker,
                initialLine,
                updatedLine
        );
    }

    public static String convertLogToTestArguments(String log, String fileName, NonogramLogic logic) {
        String[] lines = log.split("\\n");

        boolean isRow = lines[0].startsWith("MARK_AVAILABLE_FIELDS_IN_ROW");
        String axisLabel = isRow ? "row" : "column";

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].split(",")[0].trim());
        int seqIdx = Integer.parseInt(lines[0].split("sequenceIdx=")[1].split(",")[0].trim());
        String marker = lines[0].split("marker=")[1].trim();

        String initialLine = lines[1].split("initialLine=")[1].trim();
        String updatedLine = lines[2].split("updatedLine=")[1].trim();

        List<String> fieldState = isRow
                ? logic.getNonogramSolutionBoard().get(index)
                : logic.getNonogramSolutionBoard().stream().map(row -> row.get(index)).toList();

        List<List<Integer>> ranges = isRow
                ? logic.getRowsSequencesRanges().get(index)
                : logic.getColumnsSequencesRanges().get(index);

        List<Integer> lengths = isRow
                ? logic.getNonogramRules().getRowSequencesLengths().get(index)
                : logic.getNonogramRules().getColumnSequencesLengths().get(index);

        return String.format(
                "Arguments.of(\"%s / %dx%d / %s / %s %d - seq %d\",%n" +
                        LIST_STRING_FORMAT +
                        LIST_STRING_FORMAT +
                        LIST_STRING_FORMAT +
                        LIST_STRING_FORMAT +
                        "    List.of(%s))",
                fileName,
                logic.getNonogramRules().getWidth(),
                logic.getNonogramRules().getHeight(),
                marker,
                isRow ? "Row" : "Column",
                index,
                seqIdx,
                initialLine,
                updatedLine,
                fieldState,
                ranges,
                lengths.toString()
        );
    }
}
