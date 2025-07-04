package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.xplacement;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.LogFormatUtils;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PlaceXsAtTooShortEmptySequencesLogHelper {

    public static String generateLog(
            int index,
            List<String> initialState,
            List<String> finalState,
            List<Integer> sequenceLengths,
            List<Integer> excludedSequences,
            boolean isRow
    ) {
        return String.format(
                """
                        PLACE_XS_IN_%s_AT_TOO_SHORT_EMPTY_SEQUENCES: %s=%d
                        initial=%s
                        final=%s
                        lengths=%s
                        excluded=%s
                        """,
                isRow ? "ROW" : "COLUMN",
                isRow ? "row" : "col",
                index,
                initialState.toString(),
                finalState.toString(),
                LogFormatUtils.formatList(sequenceLengths),
                LogFormatUtils.formatList(excludedSequences)
        );
    }

    public static String convertLogToTestArguments(String log, String solutionName, NonogramLogic logic) {
        String[] lines = log.split("\n");

        boolean isRow = lines[0].startsWith("PLACE_XS_IN_ROW");
        String axisLabel = isRow ? "row" : "col";

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());

        List<String> initialState = LogFormatUtils.parseStringListLine(lines[1].split("=")[1].trim());
        List<String> finalState = LogFormatUtils.parseStringListLine(lines[2].split("=")[1].trim());
        List<Integer> lengths = LogFormatUtils.parseIntegerListLine(lines[3].split("=")[1].trim());
        String[] splittedExcludedLine = lines[4].split("=");
        List<Integer> excluded = LogFormatUtils.parseIntegerListLine(splittedExcludedLine.length == 1 ? "" : splittedExcludedLine[1].trim());

        return String.format(
                "Arguments.of(\"%s / %dx%d / %s %d - place Xs in too short empty sequences\",\n" +
                        "    List.of(%s),\n" + // initial
                        "    List.of(%s),\n" + // final
                        "    List.of(%s),\n" + // lengths
                        "    List.of(%s),\n" + // excluded
                        "    %s\n" +           // isRow
                        ")",
                solutionName,
                logic.getNonogramRules().getWidth(),
                logic.getNonogramRules().getHeight(),
                isRow ? "Row" : "Column",
                index,
                initialState.toString(),
                finalState.toString(),
                lengths.toString(),
                excluded.toString(),
                isRow
        );
    }
}
