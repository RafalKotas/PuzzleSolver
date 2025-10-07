package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic;

import java.util.List;

public class TestSequences {

    public static List<List<Integer>> testRowSequences() {
        return List.of(
                List.of(1, 2, 1), List.of(2, 2, 1), List.of(5, 1), List.of(4, 1), List.of(5, 1),
                List.of(5, 1), List.of(2, 4, 1), List.of(6, 1, 1), List.of(5, 1, 1), List.of(4, 12),
                List.of(1, 14), List.of(6, 2, 2, 4), List.of(3, 3), List.of(1, 1, 1, 1, 1, 3),
                List.of(15, 1), List.of(15), List.of(1, 2, 1, 1, 2, 1), List.of(2, 2, 2, 2),
                List.of(6, 6), List.of(4, 4)
        );
    }

    public static List<List<Integer>> testColumnSequences() {
        return List.of(
                List.of(3), List.of(3, 4), List.of(9), List.of(1, 4, 12), List.of(9, 2, 2, 3),
                List.of(7, 3, 3, 2), List.of(3, 3, 4, 2), List.of(6, 2, 3), List.of(3, 5), List.of(2, 3),
                List.of(3, 2), List.of(12, 2), List.of(2, 6), List.of(3, 2, 3), List.of(3, 3, 2),
                List.of(2, 4, 2), List.of(3, 2, 3), List.of(10), List.of(4), List.of(4)
        );
    }
}
