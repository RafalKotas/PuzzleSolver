package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NonogramStructureFactory {

    public static List<List<Integer>> generateEmptyRows(int height) {
        return IntStream.range(0, height)
                .mapToObj(i -> new ArrayList<Integer>())
                .collect(Collectors.toList());
    }

    public static List<List<Integer>> generateEmptyColumns(int width) {
        return IntStream.range(0, width)
                .mapToObj(i -> new ArrayList<Integer>())
                .collect(Collectors.toList());
    }
}
