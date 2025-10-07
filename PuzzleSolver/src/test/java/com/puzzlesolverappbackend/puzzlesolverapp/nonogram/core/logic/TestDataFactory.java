package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.NonogramSolvePayload;

import java.util.ArrayList;
import java.util.List;

public class TestDataFactory {

    public static NonogramSolvePayload prepareFullNonogramPayload() {
        NonogramRules rules = new NonogramRules(
                TestSequences.testRowSequences(),
                TestSequences.testColumnSequences(),
                20,
                20
        );

        return new NonogramSolvePayload(
                rules,
                generateEmptyBoard(20, 20, "-"),
                generateEmptyBoard(20, 20, "----"),
                TestSequences.testRowSequences(),
                TestSequences.testColumnSequences(),
                TestRanges.rowRanges(),
                TestRanges.columnRanges(),
                generateEmptyLists(20),
                generateEmptyLists(20),
                generateEmptyLists(20),
                generateEmptyLists(20)
        );
    }

    private static List<List<String>> generateEmptyBoard(int rows, int cols, String symbol) {
        List<List<String>> board = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            List<String> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                row.add(symbol);
            }
            board.add(row);
        }
        return board;
    }

    private static List<List<Integer>> generateEmptyLists(int count) {
        List<List<Integer>> outer = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            outer.add(new ArrayList<>());
        }
        return outer;
    }
}
