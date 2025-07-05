package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solutions.NonogramSolutionDecision;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class NonogramGuessActionsLog {

    private final NonogramSolutionDecision guessDecision;

    private final List<String> actions;
}
