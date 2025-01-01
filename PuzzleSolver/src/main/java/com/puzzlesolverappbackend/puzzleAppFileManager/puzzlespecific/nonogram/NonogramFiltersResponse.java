package com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
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
