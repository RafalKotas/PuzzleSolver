package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class NonogramSolutionSaveRequest {

    private String fileName;
    private List<List<String>> board;
    private List<List<Integer>> rowSequences;
    private List<List<Integer>> columnSequences;
}
