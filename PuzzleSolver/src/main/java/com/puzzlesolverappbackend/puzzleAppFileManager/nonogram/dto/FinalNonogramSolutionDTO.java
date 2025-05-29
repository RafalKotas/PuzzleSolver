package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinalNonogramSolutionDTO {
    private List<List<String>> finalBoard;
    private List<List<List<Integer>>> derivedRowRanges;
    private List<List<List<Integer>>> derivedColumnRanges;

    private String verifiedAgainstOriginal;
}

