package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration.LogFormatUtils;

import java.util.Arrays;
import java.util.List;

public class PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelper {

    public static String generateCorrectingRangePartLog(
            int index,
            String direction,
            boolean isRow,
            int matchingSequenceIndex,
            List<Integer> sequenceLengths,
            List<String> rowBefore,
            List<String> rowAfter,
            List<List<Integer>> initialRanges,
            List<List<Integer>> finalRanges
    ) {
        return String.format(
                """
                        %s_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART: %s=%d, dir=%s
                        matchingSequenceIndex=%d,
                        lengths=%s
                        rowBefore=%s,
                        rowAfter=%s,
                        initialRanges=%s
                        finalRanges=%s
                        """,
                isRow ? "ROW" : "COLUMN",
                isRow ? "row" : "col",
                index,
                direction,
                matchingSequenceIndex,
                sequenceLengths.toString(),
                rowBefore.toString(),
                rowAfter.toString(),
                initialRanges.toString(),
                finalRanges.toString()
        );
    }

    public static String convertLogToTestArguments(
            String logText,
            String solutionName,
            NonogramLogic logic
    ) {
        String[] lines = logText.strip().split("\n");

        String header = lines[0].trim();
        boolean isRow = header.startsWith("ROW_");
        String indexLabel = isRow ? "row=" : "col=";

        int idxEq = header.indexOf(indexLabel);
        if (idxEq < 0) {
            throw new IllegalArgumentException("Header without index: " + header);
        }
        int idxStart = idxEq + indexLabel.length();
        int idxEnd = header.indexOf(',', idxStart);
        if (idxEnd < 0) idxEnd = header.length();
        int index = Integer.parseInt(header.substring(idxStart, idxEnd).trim());

        String dirKey = "dir=";
        int dirEq = header.indexOf(dirKey);
        if (dirEq < 0) {
            throw new IllegalArgumentException("Header without dir: " + header);
        }
        String direction = header.substring(dirEq + dirKey.length()).trim();
        if (direction.endsWith(",")) direction = direction.substring(0, direction.length() - 1).trim();

        String lengthsLine      = extractCleanValue(lines, "lengths");
        String rowBeforeLine    = extractCleanValue(lines, "rowBefore");
        String rowAfterLine     = extractCleanValue(lines, "rowAfter");
        String initialRanges    = extractCleanValue(lines, "initialRanges");
        String finalRanges      = extractCleanValue(lines, "finalRanges");

        String fileName = solutionName.replaceFirst("^r", "").replaceFirst("\\.json$", "");
        int h = logic.getNonogramRules().getHeight();
        int w = logic.getNonogramRules().getWidth();
        String axis = isRow ? "Row" : "Column";
        String testLabel = String.format("%s / %dx%d / diff  / %s %d / dir=%s",
                fileName, h, w, axis, index, direction);

        String lengthsAsList = "List.of(" + LogFormatUtils.formatList(lengthsLine) + ")";
        String beforeAsList  = "List.of(" + LogFormatUtils.formatList(rowBeforeLine) + ")";
        String afterAsList   = "List.of(" + LogFormatUtils.formatList(rowAfterLine) + ")";
        String initialRangesAsList = "List.of(" + LogFormatUtils.formatNestedList(initialRanges) + ")";
        String finalRangesAsList   = "List.of(" + LogFormatUtils.formatNestedList(finalRanges) + ")";

        return String.format("""
            Arguments.of("%s",
                %s,
                %s,
                %s,
                %s,
                %s
            )""",
                testLabel,
                lengthsAsList,
                beforeAsList,
                afterAsList,
                initialRangesAsList,
                finalRangesAsList
        );
    }

    private static String extractCleanValue(String[] lines, String key) {
        return Arrays.stream(lines)
                .map(String::trim)
                .filter(l -> l.startsWith(key + "="))
                .map(l -> l.substring(l.indexOf('=') + 1).trim().replaceAll(",$", "")) // usuń końcowy przecinek
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Missing line for: " + key));
    }
}
