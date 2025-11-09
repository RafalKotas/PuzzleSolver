package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
