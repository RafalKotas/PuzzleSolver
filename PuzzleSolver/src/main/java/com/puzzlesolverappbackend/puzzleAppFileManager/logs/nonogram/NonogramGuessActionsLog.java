package com.puzzlesolverappbackend.puzzleAppFileManager.logs.nonogram;

import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.NonogramSolutionDecision;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class NonogramGuessActionsLog {

    private final NonogramSolutionDecision guessDecision;

    private final List<String> actions;
}
