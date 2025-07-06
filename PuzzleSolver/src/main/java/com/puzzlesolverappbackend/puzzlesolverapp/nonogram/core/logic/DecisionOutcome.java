package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solutions.NonogramSolutionDecision;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solutions.NonogramSolutionNode;

import java.util.Optional;

public class DecisionOutcome {
    private final Optional<NonogramSolutionDecision> correctDecision;
    private final Optional<NonogramSolutionNode> updatedNode;
    private final int wrongCount;
    private final boolean oneOfTwoWrong;

    private DecisionOutcome(Optional<NonogramSolutionDecision> correctDecision,
                            Optional<NonogramSolutionNode> updatedNode,
                            int wrongCount,
                            boolean oneOfTwoWrong) {
        this.correctDecision = correctDecision;
        this.updatedNode = updatedNode;
        this.wrongCount = wrongCount;
        this.oneOfTwoWrong = oneOfTwoWrong;
    }

    public static DecisionOutcome bothWrong() {
        return new DecisionOutcome(Optional.empty(), Optional.empty(), 2, false);
    }

    public static DecisionOutcome oneCorrect(NonogramSolutionDecision decision, NonogramSolutionNode updatedNode, int wrongCount) {
        return new DecisionOutcome(Optional.of(decision), Optional.of(updatedNode), wrongCount, true);
    }

    public static DecisionOutcome bothValid() {
        return new DecisionOutcome(Optional.empty(), Optional.empty(), 0, false);
    }

    public boolean shouldBreak() {
        return wrongCount > 0;
    }

    public Optional<NonogramSolutionDecision> correctDecision() {
        return correctDecision;
    }

    public Optional<NonogramSolutionNode> updatedNode() {
        return updatedNode;
    }

    public int wrongCount() {
        return wrongCount;
    }

    public boolean oneOfTwoWrong() {
        return oneOfTwoWrong;
    }
}

