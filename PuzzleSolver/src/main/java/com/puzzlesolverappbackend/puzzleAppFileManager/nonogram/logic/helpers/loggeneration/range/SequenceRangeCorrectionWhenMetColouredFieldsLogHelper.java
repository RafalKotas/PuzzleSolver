package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.range;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramLogic;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class SequenceRangeCorrectionWhenMetColouredFieldsLogHelper {

    public static String generateLog(
            int index,
            int seqIdx,
            List<List<Integer>> ranges,
            List<Integer> newRange,
            List<String> state,
            List<Integer> sequenceLengths,
            boolean isRow,
            String direction
    ) {
        return String.format(
                "%s_SEQUENCE_CORRECTION_WHEN_MET_COLOURED_FIELDS: %s=%d, dir=%s\n" +
                        "seq=%d\n" +
                        "new=%s\n" +
                        "state=%s\n" +
                        "lengths=%s\n" +
                        "ranges=%s\n",
                isRow ? "ROW" : "COLUMN",
                isRow ? "row" : "col",
                index,
                direction,
                seqIdx,
                newRange,
                state,
                sequenceLengths,
                ranges
        );
    }

    public static String convertLogToTestArguments(String log, String solutionName, NonogramLogic logic) {
        String[] lines = log.split("\n");

        // --- Header ---
        String headerLine = lines[0];
        boolean isRow = headerLine.startsWith("ROW_");
        int index = extractIntFromLine(headerLine, isRow ? "row=" : "col=");
        String direction = extractValueFromLine(headerLine, "dir=");

        // --- Content ---
        int seqIdx = extractIntFromLine(lines[1], "seq=");
        List<Integer> newRange = parseListOfInts(lines[2].substring("new=".length()));
        List<String> state = parseListOfStrings(lines[3].substring("state=".length()));
        List<Integer> sequenceLengths = parseListOfInts(lines[4].substring("lengths=".length()));
        List<List<Integer>> ranges = parseListOfListOfInts(lines[5].substring("ranges=".length()));

        // --- Label ---
        String label = String.format("%s / %dx%d / %s / %s %d - seq %d",
                solutionName,
                logic.getNonogramRules().getWidth(),
                logic.getNonogramRules().getHeight(),
                "1.0",
                isRow ? "Row" : "Column",
                index,
                seqIdx
        );

        return String.format(
                "Arguments.of(\"%s\",\n" +
                        "    %s,\n" +
                        "    %s,\n" +
                        "    %s,\n" +
                        "    %s\n" +
                        "),",
                label,
                formatList(state),
                formatNestedList(ranges),
                formatList(sequenceLengths),
                formatList(newRange)
        );
    }

    // --- Utility Parsers ---

    private static int extractIntFromLine(String line, String key) {
        return Integer.parseInt(line.substring(line.indexOf(key) + key.length()).split("[,\\s]")[0]);
    }

    private static String extractValueFromLine(String line, String key) {
        int start = line.indexOf(key) + key.length();
        int end = line.indexOf(",", start);
        return (end == -1) ? line.substring(start).trim() : line.substring(start, end).trim();
    }

    private static List<Integer> parseListOfInts(String input) {
        return Arrays.stream(input.replaceAll("[\\[\\]]", "").split(","))
                .filter(s -> !s.isBlank())
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private static List<List<Integer>> parseListOfListOfInts(String input) {
        String[] parts = input.replaceAll("\\[\\[|\\]\\]", "").split("\\],\\s*\\[");
        return Arrays.stream(parts)
                .map(part -> parseListOfInts("[" + part + "]"))
                .collect(Collectors.toList());
    }

    private static List<String> parseListOfStrings(String input) {
        return Arrays.stream(input.replaceAll("[\\[\\]]", "").split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    private static String formatList(List<?> list) {
        return "List.of(" + list.stream()
                .map(e -> (e instanceof String) ? "\"" + e + "\"" : e.toString())
                .collect(Collectors.joining(", ")) + ")";
    }

    private static String formatNestedList(List<List<Integer>> list) {
        return "List.of(" + list.stream()
                .map(inner -> "List.of(" + inner.stream().map(Object::toString).collect(Collectors.joining(", ")) + ")")
                .collect(Collectors.joining(", ")) + ")";
    }
}

