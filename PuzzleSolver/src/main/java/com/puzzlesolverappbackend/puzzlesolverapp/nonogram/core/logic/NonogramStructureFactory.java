package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class NonogramStructureFactory {

    public static List<List<Integer>> generateEmptyRows(int height) {
        List<List<Integer>> rows = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            rows.add(new ArrayList<>());
        }
        return rows;
    }

    public static List<List<Integer>> generateEmptyColumns(int width) {
        List<List<Integer>> columns = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            columns.add(new ArrayList<>());
        }
        return columns;
    }

}
