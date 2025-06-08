package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
}
