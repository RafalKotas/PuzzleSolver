package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model;

import lombok.Data;

import java.util.List;

@Data
public class NonogramFullSolutionData {
    private List<List<String>> finalBoard;
    private List<List<List<Integer>>> derivedRowRanges;
    private List<List<List<Integer>>> derivedColumnRanges;
}
