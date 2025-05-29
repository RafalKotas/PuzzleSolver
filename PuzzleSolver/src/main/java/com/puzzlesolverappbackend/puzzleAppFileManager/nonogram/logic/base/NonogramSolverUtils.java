package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.puzzlesolverappbackend.puzzleAppFileManager.common.FileHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.model.NonogramFullSolutionData;
import lombok.experimental.UtilityClass;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.model.NonogramConstants.COLOURED_FIELD;

@UtilityClass
public class NonogramSolverUtils {

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

    public static boolean isBoardConsistentWithSequences(List<List<String>> board, List<List<Integer>> rowSeq, List<List<Integer>> colSeq) {
        List<List<List<Integer>>> rowRanges = inferSequenceRangesFromBoard(board);
        List<List<List<Integer>>> colRanges = inferSequenceRangesFromColumns(board);

        return sequencesMatch(rowRanges, rowSeq) && sequencesMatch(colRanges, colSeq);
    }

    private static boolean sequencesMatch(List<List<List<Integer>>> ranges, List<List<Integer>> expected) {
        for (int i = 0; i < ranges.size(); i++) {
            List<Integer> expectedLengths = expected.get(i);
            List<Integer> actualLengths = ranges.get(i).stream()
                    .map(range -> range.get(1) - range.get(0) + 1)
                    .toList();

            if (!actualLengths.equals(expectedLengths)) {
                return false;
            }
        }
        return true;
    }

    public static NonogramFullSolutionData loadFullSolutionData(String filename) {
        try (FileReader reader = new FileReader(FileHelper.nonogramSolutionLoadPathForFilename(filename))) {
            return new Gson().fromJson(JsonParser.parseReader(reader), NonogramFullSolutionData.class);
        } catch (Exception e) {
            System.out.println(e);
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
}
