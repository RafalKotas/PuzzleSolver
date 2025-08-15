package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model;

import lombok.Getter;

import java.util.List;

@Getter
public class NonogramFullSolutionData {
    private List<List<String>> finalBoard;
    private List<List<List<Integer>>> derivedRowRanges;
    private List<List<List<Integer>>> derivedColumnRanges;
}
