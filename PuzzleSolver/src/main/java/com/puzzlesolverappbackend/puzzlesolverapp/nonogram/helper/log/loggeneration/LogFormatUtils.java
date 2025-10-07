package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@UtilityClass
public class LogFormatUtils {

    private static final String LIST_OF_PREFIX = "List.of(";
    private static final String ARRAY_LIST_PREFIX = "new ArrayList<>(";

    // Pattern to match an inner integer list: [0, 14], [3, 16], ...
    private static final Pattern INNER_LIST = Pattern.compile(
            "\\[(\\s*-?\\d+(?:\\s*,\\s*-?\\d+)*)\\]"
    );

    // =====================================================================================
    //  PARSERS FOR LIST<LIST<INTEGER>> FROM "[[...]]" LITERALS
    // =====================================================================================

    /**
     * Parses string literal "[[0, 14], [3, 16], [18, 18]]"
     * to a mutable, deep-copied List<List<Integer>> structure.
     */
    public static List<List<Integer>> toMutableRangesList(String arrayLiteral) {
        if (arrayLiteral == null) throw new IllegalArgumentException("arrayLiteral is null");
        String s = arrayLiteral.trim();
        if (!s.startsWith("[[") || !s.endsWith("]]")) {
            throw new IllegalArgumentException("Unsupported format (expected [[...]]): " + arrayLiteral);
        }

        List<List<Integer>> result = new ArrayList<>();
        Matcher m = INNER_LIST.matcher(s);

        while (m.find()) {
            String body = m.group(1); // e.g. "0, 14"
            String[] parts = body.split(",");
            List<Integer> inner = new ArrayList<>(parts.length);
            for (String p : parts) {
                String t = p.trim();
                if (!t.isEmpty()) inner.add(Integer.parseInt(t));
            }
            // add mutable list
            result.add(inner);
        }

        return result;
    }

    // =====================================================================================
    //  FORMATTERS FROM PARSED LIST STRUCTURES → "List.of(...)" LITERALS
    // =====================================================================================

    /**
     * Extracts the value from a log line starting with "prefix=".
     * Example: prefix="initialRanges", line="initialRanges=[[0, 14], [3, 16]]"
     * will return "[[0, 14], [3, 16]]".
     */
    public static String extractValue(String[] lines, String prefix) {
        return Arrays.stream(lines)
                .filter(l -> l.startsWith(prefix + "="))
                .map(l -> l.replace(prefix + "=", "").trim())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Missing line for: " + prefix));
    }

    // =====================================================================================
    //  NEW: STRING → STRING LITERAL CONVERTERS FOR TEST ARGUMENTS
    // =====================================================================================

    // -------- Flat integer lists --------

    /** Converts "[2, 2, 1]" to "List.of(2, 2, 1)" (immutable). */
    public static String toImmutableIntListLiteral(String arrayLiteral) {
        List<Integer> nums = parseFlatIntArrayLiteral(arrayLiteral);
        return LIST_OF_PREFIX + nums.stream().map(String::valueOf).collect(Collectors.joining(", ")) + ")";
    }

    /** Converts "[2, 2, 1]" to "new ArrayList<>(List.of(2, 2, 1))" (mutable). */
    public static String toMutableIntListLiteral(String arrayLiteral) {
        return ARRAY_LIST_PREFIX + toImmutableIntListLiteral(arrayLiteral) + ")";
    }

    // -------- Ranges (list of lists) --------

    /** Converts "[[0, 14], [3, 16], [18, 18]]" to "List.of(List.of(0, 14), List.of(3, 16), List.of(18, 18))" (immutable). */
    public static String toImmutableRangesListLiteral(String arrayLiteral) {
        List<List<Integer>> ranges = toMutableRangesList(arrayLiteral);
        String inner = ranges.stream()
                .map(in -> LIST_OF_PREFIX + in.stream().map(String::valueOf).collect(Collectors.joining(", ")) + ")")
                .collect(Collectors.joining(", "));
        return LIST_OF_PREFIX + inner + ")";
    }

    /**
     * Converts "[[0, 14], [3, 16], [18, 18]]" to mutable structure literal:
     * "new ArrayList<>(List.of(new ArrayList<>(List.of(0, 14)), new ArrayList<>(List.of(3, 16)), ...))".
     */
    public static String toMutableRangesListLiteral(String arrayLiteral) {
        List<List<Integer>> ranges = toMutableRangesList(arrayLiteral);
        String inner = ranges.stream()
                .map(in -> ARRAY_LIST_PREFIX + LIST_OF_PREFIX + in.stream().map(String::valueOf).collect(Collectors.joining(", ")) + "))")
                .collect(Collectors.joining(", "));
        return ARRAY_LIST_PREFIX + LIST_OF_PREFIX + inner + "))";
    }

    // =====================================================================================
    //  INTERNAL HELPERS
    // =====================================================================================

    /** Minimal parser for a flat integer list from a literal "[...]" (allows spaces). */
    private static List<Integer> parseFlatIntArrayLiteral(String arrayLiteral) {
        if (arrayLiteral == null) throw new IllegalArgumentException("arrayLiteral is null");
        String s = arrayLiteral.trim();
        if (!s.startsWith("[") || !s.endsWith("]")) {
            throw new IllegalArgumentException("Unsupported format (expected [...]): " + arrayLiteral);
        }
        s = s.substring(1, s.length() - 1).trim();
        if (s.isEmpty()) return List.of();
        return Arrays.stream(s.split(","))
                .map(String::trim)
                .filter(t -> !t.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    /**
     * Converts literal like "[-, -, X, -, -, -, -, -, X, O, -, -, -, -, -]"
     * into immutable List.of("-", "-", "X", "-", ...)
     */
    public static String toImmutableStringListLiteral(String arrayLiteral) {
        if (arrayLiteral == null) throw new IllegalArgumentException("arrayLiteral is null");
        String trimmed = arrayLiteral.trim();
        if (!trimmed.startsWith("[") || !trimmed.endsWith("]")) {
            throw new IllegalArgumentException("Unsupported format (expected [ ... ]): " + arrayLiteral);
        }

        String body = trimmed.substring(1, trimmed.length() - 1).trim();
        if (body.isEmpty()) {
            return LIST_OF_PREFIX;
        }

        String elements = Arrays.stream(body.split(","))
                .map(String::trim)
                .map(s -> "\"" + s + "\"")
                .collect(Collectors.joining(", "));

        return LIST_OF_PREFIX + elements + ")";
    }

    /**
     * Converts literal like "[-, -, X, -, -, -, -, -, X, O, -, -, -, -, -]"
     * into mutable new ArrayList<>(List.of("-", "-", "X", "-", ...))
     */
    public static String toMutableStringListLiteral(String arrayLiteral) {
        return ARRAY_LIST_PREFIX + toImmutableStringListLiteral(arrayLiteral) + ")";
    }
}
