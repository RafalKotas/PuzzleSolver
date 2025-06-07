package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.*;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.model.NonogramFullSolutionData;
import lombok.experimental.UtilityClass;

import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.model.NonogramConstants.COLOURED_FIELD;

@UtilityClass
public class NonogramSolverUtils {

    public static boolean isBoardConsistentWithSequences(List<List<String>> board, List<List<Integer>> rowSeq, List<List<Integer>> colSeq) {
        List<List<List<Integer>>> rowRanges = inferSequenceRangesFromBoard(board);
        List<List<List<Integer>>> colRanges = inferSequenceRangesFromColumns(board);

        return sequencesMatch(rowRanges, rowSeq) && sequencesMatch(colRanges, colSeq);
    }

    public static List<List<List<Integer>>> inferSequenceRangesFromBoard(List<List<String>> board) {
        List<List<List<Integer>>> allRowRanges = new ArrayList<>();

        for (List<String> row : board) {
            List<List<Integer>> rowRanges = new ArrayList<>();
            int start = -1;

            for (int i = 0; i < row.size(); i++) {
                if (COLOURED_FIELD.equals(row.get(i))) {
                    if (start == -1) start = i;
                } else if (start != -1) {
                    rowRanges.add(List.of(start, i - 1));
                    start = -1;
                }
            }

            if (start != -1) {
                rowRanges.add(List.of(start, row.size() - 1));
            }

            if (rowRanges.isEmpty()) {
                rowRanges.add(List.of(-1, -1));
            }

            allRowRanges.add(rowRanges);
        }

        return allRowRanges;
    }

    public static List<List<List<Integer>>> inferSequenceRangesFromColumns(List<List<String>> board) {
        int height = board.size();
        int width = board.get(0).size();
        List<List<List<Integer>>> allColumnRanges = new ArrayList<>();

        for (int col = 0; col < width; col++) {
            List<List<Integer>> colRanges = new ArrayList<>();
            int start = -1;

            for (int row = 0; row < height; row++) {
                if (COLOURED_FIELD.equals(board.get(row).get(col))) {
                    if (start == -1) start = row;
                } else if (start != -1) {
                    colRanges.add(List.of(start, row - 1));
                    start = -1;
                }
            }

            if (start != -1) {
                colRanges.add(List.of(start, height - 1));
            }

            allColumnRanges.add(colRanges);
        }

        return allColumnRanges;
    }

    private static boolean sequencesMatch(List<List<List<Integer>>> rowsRanges, List<List<Integer>> expected) {
        for (int i = 0; i < rowsRanges.size(); i++) {
            List<Integer> expectedLengths = expected.get(i);
            List<List<Integer>> ranges = rowsRanges.get(i);
            List<Integer> actualLengths;

            // empty range only sequence 0 ([0])
            if (ranges.size() == 1 && ranges.get(0).equals(List.of(-1, -1))) {
                actualLengths = List.of(0);
            } else {
                actualLengths = ranges.stream()
                        .map(range -> range.get(1) - range.get(0) + 1)
                        .toList();
            }

            if (expectedLengths.isEmpty()) {
                expectedLengths = List.of(0);
            }

            if (!actualLengths.equals(expectedLengths)) {
                return false;
            }
        }
        return true;
    }

    public static NonogramFullSolutionData loadFullSolutionData(String filename) {
        Path projectDir = Paths.get(System.getProperty("user.dir")).normalize();

        Path solutionPath = projectDir.resolve(Paths.get("data", "solutions", "Nonograms", filename));
        try (FileReader reader = new FileReader(solutionPath.toFile())) {
            return new Gson().fromJson(JsonParser.parseReader(reader), NonogramFullSolutionData.class);
        } catch (Exception e) {
            System.out.println("Error reading solution file: " + solutionPath);
            e.printStackTrace();
            return null;
        }
    }

    public static boolean rangesContainCorrectRanges(
            List<List<List<Integer>>> correctRanges,
            List<List<List<Integer>>> currentRanges
    ) {
        if (correctRanges.size() != currentRanges.size()) {
            return false;
        }

        for (int i = 0; i < correctRanges.size(); i++) {
            List<List<Integer>> correctRow = correctRanges.get(i);
            List<List<Integer>> currentRow = currentRanges.get(i);

            if (correctRow.size() != currentRow.size()) {
                return false;
            }

            for (int j = 0; j < correctRow.size(); j++) {
                List<Integer> correctRange = correctRow.get(j);
                List<Integer> currentRange = currentRow.get(j);

                if (!rangeContains(currentRange, correctRange)) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean rangeContains(List<Integer> outer, List<Integer> inner) {
        return outer.get(0) <= inner.get(0) && outer.get(1) >= inner.get(1);
    }

    public static boolean partialBoardMatchesSolution(List<List<String>> partialBoard, List<List<String>> correctBoard) {
        for (int i = 0; i < partialBoard.size(); i++) {
            for (int j = 0; j < partialBoard.get(i).size(); j++) {
                String current = partialBoard.get(i).get(j);
                if (!"-".equals(current)) {
                    if (!current.equals(correctBoard.get(i).get(j))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static Optional<String> convertLogByAction(
            String log,
            String solutionName,
            NonogramLogic logic,
            String actionType
    ) {
        return switch (actionType) {
            case "OVERLAP" -> Optional.of(OverlappingLogHelper.convertLogToTestArguments(log, solutionName, logic));
            case "EXTEND" -> Optional.of(ExtendLogHelper.convertLogToTestArguments(log, solutionName, logic));
            case "TOO_LONG_MERGE" -> Optional.of(TooLongMergeLogHelper.convertLogToTestArguments(log, solutionName, logic));
            case "TRIVIAL" -> Optional.of(TrivialFillLogHelper.convertLogToTestArguments(log, solutionName, logic));
            case "EXCLUDED" -> Optional.of(ExcludedSequenceLogHelper.convertLogToTestArguments(log, solutionName, logic));
            default -> Optional.empty();
        };
    }

    public static String detectActionTypeFromLog(String log) {
        if (log.startsWith("OVERLAP_")) return "OVERLAP";
        if (log.startsWith("EXTEND_")) return "EXTEND";
        if (log.startsWith("TOO_LONG_MERGE_")) return "TOO_LONG_MERGE";
        if (log.startsWith("TRIVIAL_ROW_SEQUENCE:") || log.startsWith("TRIVIAL_COLUMN_SEQUENCE:")) return "TRIVIAL";
        if (log.startsWith("EXCLUDED_ROW_SEQUENCE:") || log.startsWith("EXCLUDED_COLUMN_SEQUENCE:")) return "EXCLUDED";
        return "UNKNOWN";
    }

    public static void numberedLogToArgumentsWithCaseNumbers(List<String> convertedLogs) {
        Map<String, Integer> labelCounters = new HashMap<>();

        for (String converted : convertedLogs) {
            String prefix = converted.split(",")[0];

            Matcher matcher = Pattern.compile("(Row|Column) \\d+").matcher(prefix);
            if (!matcher.find()) {
                System.out.println("// Not found Row/Column in: " + prefix);
                continue;
            }

            String label = matcher.group();
            int count = labelCounters.getOrDefault(label, 0) + 1;
            labelCounters.put(label, count);

            String numberedLabel = label + " #" + count;
            String newConverted = converted.replace(label, numberedLabel);

            System.out.println(newConverted + ",");
        }
    }
}
