package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.model;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.solutions.NonogramSolutionDecision;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class NonogramGuessActionsLog {

    private final NonogramSolutionDecision guessDecision;

    private final List<String> actions;
}
