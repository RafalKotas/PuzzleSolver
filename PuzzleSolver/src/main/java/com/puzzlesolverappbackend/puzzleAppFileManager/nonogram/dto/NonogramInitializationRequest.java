package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NonogramInitializationRequest {
    private String filename;
    private List<List<Integer>> rowSequences;
    private List<List<Integer>> columnSequences;
    private int height;
    private int width;
}
