package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;

@AllArgsConstructor
@Getter
public class NonogramFilterRequest {
    private Collection<String> sources;
    private Collection<String> years;
    private Collection<String> months;
    private Double minDifficulty;
    private Double maxDifficulty;
    private Integer minHeight;
    private Integer maxHeight;
    private Integer minWidth;
    private Integer maxWidth;
}

