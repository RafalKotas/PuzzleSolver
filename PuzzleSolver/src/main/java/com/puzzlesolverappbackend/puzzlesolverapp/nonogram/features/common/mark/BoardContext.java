package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.mark;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class BoardContext {
    private final int lineIdx;
    private final boolean isRow;
    private final NonogramRules rules;
    private final List<List<String>> solutionBoard;
    private final List<List<String>> boardWithMarks;
}

