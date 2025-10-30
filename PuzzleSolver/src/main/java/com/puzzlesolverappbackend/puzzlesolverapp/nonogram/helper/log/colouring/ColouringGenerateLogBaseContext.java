package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ColouringGenerateLogBaseContext {

    boolean isRow;
    int index;
    List<String> initialLine;
    List<String> updatedLine;
    List<List<Integer>> sequencesRanges;
    List<Integer> sequencesLengths;
}
