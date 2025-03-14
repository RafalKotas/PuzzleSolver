package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.NonogramSolutionDecision;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class NonogramGuessActionsLog {

    private final NonogramSolutionDecision guessDecision;

    private final List<String> actions;
}
