package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic;

import java.util.List;

public class TestRanges {

    public static List<List<List<Integer>>> rowRanges() {
        return List.of(
                List.of(List.of(0, 14), List.of(2, 17), List.of(5, 19)),
                List.of(List.of(0, 14), List.of(3, 17), List.of(6, 19)),
                List.of(List.of(0, 17), List.of(6, 19)),
                List.of(List.of(0, 17), List.of(5, 19)),
                List.of(List.of(0, 17), List.of(6, 19)),
                List.of(List.of(0, 17), List.of(6, 19)),
                List.of(List.of(0, 12), List.of(3, 17), List.of(8, 19)),
                List.of(List.of(0, 15), List.of(7, 17), List.of(9, 19)),
                List.of(List.of(0, 15), List.of(6, 17), List.of(8, 19)),
                List.of(List.of(0, 6), List.of(5, 19)),
                List.of(List.of(0, 4), List.of(2, 19)),
                List.of(List.of(0, 8), List.of(7, 11), List.of(10, 14), List.of(13, 19)),
                List.of(List.of(0, 15), List.of(4, 19)),
                List.of(List.of(0, 7), List.of(2, 9), List.of(4, 11), List.of(6, 13), List.of(8, 15), List.of(10, 19)),
                List.of(List.of(0, 17), List.of(16, 19)),
                List.of(List.of(0, 19)),
                List.of(List.of(0, 7), List.of(2, 10), List.of(5, 12), List.of(7, 14), List.of(9, 17), List.of(12, 19)),
                List.of(List.of(0, 10), List.of(3, 13), List.of(6, 16), List.of(9, 19)),
                List.of(List.of(0, 12), List.of(7, 19)),
                List.of(List.of(0, 14), List.of(5, 19))
        );
    }

    public static List<List<List<Integer>>> columnRanges() {
        return List.of(
                List.of(List.of(0, 19)),
                List.of(List.of(0, 14), List.of(4, 19)),
                List.of(List.of(0, 19)),
                List.of(List.of(0, 1), List.of(2, 6), List.of(7, 19)),
                List.of(List.of(0, 9), List.of(10, 12), List.of(13, 15), List.of(16, 19)),
                List.of(List.of(0, 8), List.of(8, 12), List.of(12, 16), List.of(16, 19)),
                List.of(List.of(0, 7), List.of(4, 11), List.of(8, 16), List.of(13, 19)),
                List.of(List.of(0, 12), List.of(7, 15), List.of(10, 19)),
                List.of(List.of(0, 13), List.of(4, 19)),
                List.of(List.of(0, 15), List.of(3, 19)),
                List.of(List.of(0, 16), List.of(4, 19)),
                List.of(List.of(0, 16), List.of(13, 19)),
                List.of(List.of(0, 12), List.of(3, 19)),
                List.of(List.of(0, 12), List.of(4, 15), List.of(7, 19)),
                List.of(List.of(0, 12), List.of(4, 16), List.of(8, 19)),
                List.of(List.of(0, 11), List.of(3, 16), List.of(8, 19)),
                List.of(List.of(0, 12), List.of(4, 15), List.of(7, 19)),
                List.of(List.of(0, 19)),
                List.of(List.of(0, 19)),
                List.of(List.of(0, 19))
        );
    }
}
