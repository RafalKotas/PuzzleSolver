package com.puzzlesolverappbackend.puzzleAppFileManager.logs.nonogram;

import com.puzzlesolverappbackend.puzzleAppFileManager.NonogramSolutionDecision;

import java.util.List;

public class NonogramGuessActionsLog {

    NonogramSolutionDecision guessDecision;

    List<String> actions;

    public NonogramGuessActionsLog(NonogramSolutionDecision nonogramSolutionDecision, List<String> actions) {
        this.guessDecision = nonogramSolutionDecision;
        this.actions = actions;
    }
}
