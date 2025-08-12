package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class LogFormatUtils {

    private static final String BRACKETS_REGEX = "[\\[\\]]";
    private static final String LIST_OF_PREFIX = "List.of(";

    // 1+
    public static String formatList(String input) {
        return Arrays.stream(input.replaceAll(BRACKETS_REGEX, "").split(","))
                .map(String::trim)
                .map(s -> s.matches("-?\\d+") ? s : "\"" + s + "\"")
                .collect(Collectors.joining(", "));
    }

    // 2+
    public static String formatNestedList(String input) {
        String[] parts = input.replaceAll("\\[\\[|\\]\\]", "").split("\\],\\s*\\[");
        return Arrays.stream(parts)
                .map(p -> LIST_OF_PREFIX + formatList("[" + p + "]") + ")")
                .collect(Collectors.joining(", "));
    }

    // 3+
    public static String formatList(List<?> list) {
        return list.stream()
                .map(e -> (e instanceof Number) ? e.toString() : "\"" + e + "\"")
                .collect(Collectors.joining(", "));
    }

    // 4+
    public static String formatNestedList(List<? extends List<?>> nestedList) {
        return nestedList.stream()
                .map(inner -> LIST_OF_PREFIX + formatList(inner) + ")")
                .collect(Collectors.joining(", "));
    }

    // 5+
    public static String toQuotedStringList(List<String> list) {
        return list.stream()
                .map(s -> "\"" + s + "\"")
                .collect(Collectors.joining(", "));
    }

    // 6+
    public static String toRangeStringList(List<List<Integer>> ranges) {
        return ranges.stream()
                .map(inner -> LIST_OF_PREFIX + inner.stream().map(String::valueOf).collect(Collectors.joining(", ")) + ")")
                .collect(Collectors.joining(", "));
    }

    // 7+
    public static List<String> parseStringListLine(String line) {
        String content = line.contains("=") ? line.substring(line.indexOf('=') + 1) : line;

        return Arrays.stream(content.replaceAll(BRACKETS_REGEX, "").split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    // 8+
    public static List<Integer> parseIntegerListLine(String line) {
        String content = line.contains("=") ? line.substring(line.indexOf('=') + 1) : line;
        content = content.replaceAll(BRACKETS_REGEX, "").trim();

        if (content.isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.stream(content.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .toList();
    }

    // TODO - SequenceRangeCorrectionLogHelper 9
    public static List<List<Integer>> parseNestedListLine(String line) {
        if (line == null || line.isBlank()) return List.of();

        List<List<Integer>> result = new ArrayList<>();

        String trimmed = line.trim();
        if (trimmed.startsWith(LIST_OF_PREFIX) && trimmed.endsWith(")")) {
            trimmed = trimmed.substring(LIST_OF_PREFIX.length(), trimmed.length() - 1);
        } else {
            throw new IllegalArgumentException("Line does not start with 'List.of(': " + line);
        }

        String[] parts = trimmed.split("List\\.of\\(");
        for (String part : parts) {
            String content = part.replace(")", "").trim();
            if (content.isEmpty()) continue;

            List<Integer> inner = Arrays.stream(content.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt)
                    .collect(Collectors.toCollection(ArrayList::new));

            result.add(inner);
        }

        return result;
    }

    // 10 +
    public static List<List<Integer>> parseNestedListLineWrappedInListOf(String input) {
        String trimmed = input.trim();

        if (trimmed.startsWith(LIST_OF_PREFIX)) {
            trimmed = trimmed.substring(8, trimmed.length() - 1); // remove "List.of(" and final ")"
        }

        return Arrays.stream(trimmed.split("\\),\\s*List.of\\("))
                .map(s -> Arrays.stream(s.replaceAll("[\\[\\]()]", "").split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .toList())
                .toList();
    }
}
