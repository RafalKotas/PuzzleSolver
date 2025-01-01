package com.puzzlesolverappbackend.puzzleAppFileManager.logs.nonogram;

import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.NonogramSolutionDecision;

import java.util.List;

public class NonogramGuessActionsLog {

    private final NonogramSolutionDecision guessDecision;

    private final List<String> actions;

    public NonogramGuessActionsLog(NonogramSolutionDecision nonogramSolutionDecision, List<String> actions) {
        this.guessDecision = nonogramSolutionDecision;
        this.actions = actions;
    }
}
