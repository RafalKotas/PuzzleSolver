package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NonogramFiltersResponse {

    List<String> sources;
    List<String> years;
    List<String> months;
    List<Double> difficulties;
    List<Integer> heights;
    List<Integer> widths;
}
