package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class LogFormatUtils {

    public static String formatList(String input) {
        return Arrays.stream(input.replaceAll("[\\[\\]]", "").split(","))
                .map(String::trim)
                .map(s -> s.matches("-?\\d+") ? s : "\"" + s + "\"")
                .collect(Collectors.joining(", "));
    }

    public static String formatNestedList(String input) {
        String[] parts = input.replaceAll("\\[\\[|\\]\\]", "").split("\\],\\s*\\[");
        return Arrays.stream(parts)
                .map(p -> "List.of(" + formatList("[" + p + "]") + ")")
                .collect(Collectors.joining(", "));
    }

    public static String formatList(List<?> list) {
        return list.stream()
                .map(e -> (e instanceof Number) ? e.toString() : "\"" + e + "\"")
                .collect(Collectors.joining(", "));
    }

    public static String formatNestedList(List<? extends List<?>> nestedList) {
        return nestedList.stream()
                .map(inner -> "List.of(" + formatList(inner) + ")")
                .collect(Collectors.joining(", "));
    }

    public static String toQuotedStringList(List<String> list) {
        return list.stream()
                .map(s -> "\"" + s + "\"")
                .collect(Collectors.joining(", "));
    }

    public static String toRangeStringList(List<List<Integer>> ranges) {
        return ranges.stream()
                .map(inner -> "List.of(" + inner.stream().map(String::valueOf).collect(Collectors.joining(", ")) + ")")
                .collect(Collectors.joining(", "));
    }

    public static List<String> parseStringListLine(String line) {
        return Arrays.stream(line.replaceAll(".*=", "")
                        .replaceAll("[\\[\\]]", "")
                        .split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    public static List<Integer> parseIntegerListLine(String line) {
        String content = line.replaceAll(".*=", "").replaceAll("[\\[\\]]", "").trim();

        if (content.isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.stream(content.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    public static List<List<Integer>> parseNestedListLine(String line) {
        if (line == null || line.isBlank()) return List.of();

        List<List<Integer>> result = new ArrayList<>();

        String trimmed = line.trim();
        if (trimmed.startsWith("List.of(") && trimmed.endsWith(")")) {
            trimmed = trimmed.substring(8, trimmed.length() - 1);
        } else {
            throw new IllegalArgumentException("Line does not start with 'List.of(': " + line);
        }

        String[] parts = trimmed.split("List\\.of\\(");

        for (String part : parts) {
            if (part.isBlank()) continue;

            String content = part.replace(")", "").trim();
            if (content.isBlank()) continue;

            List<Integer> inner = Arrays.stream(content.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            result.add(inner);
        }

        return result;
    }

    public static List<List<Integer>> parseNestedListLineWrappedInListOf(String input) {
        String trimmed = input.trim();
        if (trimmed.startsWith("List.of(")) {
            trimmed = trimmed.substring(8, trimmed.length() - 1); // usuń "List.of(" i końcowe ")"
        }
        return Arrays.stream(trimmed.split("\\),\\s*List.of\\("))
                .map(s -> Arrays.stream(s.replaceAll("[\\[\\]()]", "").split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    public static List<Integer> safeParseIntegerListLine(String line) {
        if (!line.contains("=") || line.split("=").length < 2 || line.split("=")[1].trim().isEmpty()) {
            return List.of();
        }
        return parseIntegerListLine(line.split("=")[1].trim());
    }
}
