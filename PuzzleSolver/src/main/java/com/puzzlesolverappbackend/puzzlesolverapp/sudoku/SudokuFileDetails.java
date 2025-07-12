package com.puzzlesolverappbackend.puzzlesolverapp.sudoku;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SudokuFileDetails {

    private String source;

    private double difficulty;
    private int filled;

    private String year;
    private String month;

    private List<List<Integer>> board;
}

