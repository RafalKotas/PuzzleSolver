package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.range;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.LogFormatUtils;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class SequenceRangeCorrectionLogHelper {

    public static String generateLog(
            int index,
            List<List<Integer>> initialRanges,
            List<List<Integer>> finalRanges,
            List<Integer> sequenceLengths,
            List<Integer> fieldsNotToInclude,
            List<Integer> sequencesIdsNotToInclude,
            boolean isRow
    ) {
        return String.format(
                "%s_SEQUENCES_RANGES_CORRECTED: %s=%d\n" +
                        "initial=%s\n" +
                        "final=%s\n" +
                        "lengths=%s\n" +
                        "excludedFields=%s\n" +
                        "excludedSequences=%s\n",
                isRow ? "ROW" : "COLUMN",
                isRow ? "row" : "col",
                index,
                "List.of(" + initialRanges.stream()
                        .map(range -> "List.of(" + range.stream().map(String::valueOf).collect(Collectors.joining(", ")) + ")")
                        .collect(Collectors.joining(", ")) + ")",
                "List.of(" + finalRanges.stream()
                        .map(range -> "List.of(" + range.stream().map(String::valueOf).collect(Collectors.joining(", ")) + ")")
                        .collect(Collectors.joining(", ")) + ")",
                LogFormatUtils.formatList(sequenceLengths),
                LogFormatUtils.formatList(fieldsNotToInclude),
                LogFormatUtils.formatList(sequencesIdsNotToInclude)
        );
    }

    public static String convertLogToTestArguments(String log, String solutionName) {
        String[] lines = log.split("\\n");

        boolean isRow = lines[0].startsWith("ROW_");
        String axisLabel = isRow ? "row" : "col";

        int index = Integer.parseInt(lines[0].split(axisLabel + "=")[1].trim());

        List<List<Integer>> initialRanges = LogFormatUtils.parseNestedListLine(lines[1].split("=")[1].trim());
        List<List<Integer>> finalRanges = LogFormatUtils.parseNestedListLine(lines[2].split("=")[1].trim());
        List<Integer> lengths = LogFormatUtils.parseIntegerListLine(lines[3].split("=")[1].trim());

        String excludedFieldsRaw = lines[4].contains("=") ? lines[4].split("=", 2)[1].trim() : "";
        List<Integer> excludedFields = LogFormatUtils.parseIntegerListLine(excludedFieldsRaw);

        String excludedSequencesRaw = lines[5].contains("=") ? lines[5].split("=", 2)[1].trim() : "";
        List<Integer> excludedSequences = LogFormatUtils.parseIntegerListLine(excludedSequencesRaw);

        return String.format(
                "Arguments.of(\"%s / %s %d - sequences range correction\",\n" +
                        "    List.of(%s),\n" +
                        "    List.of(%s),\n" +
                        "    List.of(%s),\n" +
                        "    List.of(%s),\n" +
                        "    List.of(%s),\n" +
                        "    %s\n" +
                        ")",
                solutionName,
                isRow ? "Row" : "Column",
                index,
                LogFormatUtils.toRangeStringList(initialRanges),
                LogFormatUtils.toRangeStringList(finalRanges),
                lengths.toString(),
                excludedFields.toString(),
                excludedSequences.toString(),
                isRow
        );
    }
}
