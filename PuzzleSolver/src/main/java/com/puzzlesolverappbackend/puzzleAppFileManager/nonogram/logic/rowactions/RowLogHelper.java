package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.stream.Collectors;

@UtilityClass
public class RowLogHelper {

    public static String convertExtendRowLogToTestArguments(String logText) {
        String[] lines = logText.strip().split("\n");

        String header = lines[0].replace("EXTEND_ROW_SEQUENCE: ", "").trim();
        String[] headerParts = header.split(", ");
        String rowPart = headerParts[0]; // "row=2"
        int rowNumber = Integer.parseInt(rowPart.split("=")[1]);
        String testLabel = "/ Row " + rowNumber;

        String initialLine = lines[1].replace("initial=", "").trim();
        String rangesLine = lines[2].replace("ranges=", "").trim();
        String lengthsLine = lines[3].replace("lengths=", "").trim();
        String finalLine = lines[4].replace("final=", "").trim();

        return String.format("""
        Arguments.of("%s",
            List.of(%s),
            List.of(%s),
            List.of(%s),
            List.of(%s)
        )""",
                testLabel,
                formatList(initialLine),
                formatNestedList(rangesLine),
                formatList(lengthsLine),
                formatList(finalLine)
        );
    }

    private static String formatList(String input) {
        return Arrays.stream(input.replaceAll("[\\[\\]]", "").split(","))
                .map(String::trim)
                .map(s -> s.matches("-?\\d+") ? s : "\"" + s + "\"")
                .collect(Collectors.joining(", "));
    }

    private static String formatNestedList(String input) {
        String[] parts = input.replaceAll("\\[\\[|\\]\\]", "").split("\\],\\s*\\[");
        return Arrays.stream(parts)
                .map(p -> "List.of(" + formatList("[" + p + "]") + ")")
                .collect(Collectors.joining(", "));
    }
}
