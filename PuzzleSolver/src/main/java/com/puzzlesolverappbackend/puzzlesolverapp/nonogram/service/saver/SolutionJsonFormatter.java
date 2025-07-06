package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service.saver;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.FinalNonogramSolutionDTO;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.StringJoiner;

/**
 * Utility for formatting JSON output of final Nonogram solutions.
 * Ensures readable formatting with:
 * - inner arrays (rows/columns) on single lines
 * - outer arrays formatted vertically
 * - clear key-value spacing
 */
/**
 * Utility for formatting JSON output of final Nonogram solutions.
 * Ensures readable formatting with:
 * - inner arrays (rows/columns) on single lines
 * - outer arrays formatted vertically
 * - clear key-value spacing
 */
@UtilityClass
public class SolutionJsonFormatter {

    private static final String LIST_END_COMMA_NEWLINE = "  ],\n";

    public static String format(FinalNonogramSolutionDTO dto) {
        StringBuilder sb = new StringBuilder();

        sb.append("{\n");
        sb.append("  \"finalBoard\" : [\n");
        appendListOfStringLists(sb, dto.getFinalBoard());
        sb.append(LIST_END_COMMA_NEWLINE);

        sb.append("  \"derivedRowRanges\" : [\n");
        appendListOfIntPairs(sb, dto.getDerivedRowRanges());
        sb.append(LIST_END_COMMA_NEWLINE);

        sb.append("  \"derivedColumnRanges\" : [\n");
        appendListOfIntPairs(sb, dto.getDerivedColumnRanges());
        sb.append(LIST_END_COMMA_NEWLINE);

        sb.append("  \"verifiedAgainstOriginal\" : \"")
                .append(dto.getVerifiedAgainstOriginal())
                .append("\"\n");

        sb.append("}");

        return sb.toString();
    }

    private static void appendListOfStringLists(StringBuilder sb, List<List<String>> list) {
        for (int i = 0; i < list.size(); i++) {
            sb.append("    [");
            sb.append(String.join(", ", list.get(i).stream()
                    .map(s -> "\"" + s + "\"")
                    .toList()));
            sb.append("]");
            if (i < list.size() - 1) sb.append(",");
            sb.append("\n");
        }
    }

    private static void appendListOfIntPairs(StringBuilder sb, List<List<List<Integer>>> list) {
        for (int i = 0; i < list.size(); i++) {
            sb.append("    [");
            StringJoiner innerJoiner = new StringJoiner(", ");
            for (List<Integer> pair : list.get(i)) {
                innerJoiner.add("[" + pair.get(0) + ", " + pair.get(1) + "]");
            }
            sb.append(innerJoiner);
            sb.append("]");
            if (i < list.size() - 1) sb.append(",");
            sb.append("\n");
        }
    }
}
