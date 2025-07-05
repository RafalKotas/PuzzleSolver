package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NonogramSolutionSaveRequest {

    private String fileName;
    private List<List<String>> board;
    private List<List<Integer>> rowSequences;
    private List<List<Integer>> columnSequences;

}
