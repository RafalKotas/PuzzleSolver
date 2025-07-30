package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

// TODO - toString
@NoArgsConstructor
@Getter
@Setter
public class FinalNonogramSolutionDTO {
    private List<List<String>> finalBoard;
    private List<List<List<Integer>>> derivedRowRanges;
    private List<List<List<Integer>>> derivedColumnRanges;

    private String verifiedAgainstOriginal;
}

