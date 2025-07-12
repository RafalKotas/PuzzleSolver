package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solutions.NonogramSolutionDecision;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solutions.NonogramSolutionNode;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DecisionOutcomeTest {

    @Test
    void bothWrong_shouldHaveEmptyDecisionAndNodeAndTwoWrongCount() {
        DecisionOutcome outcome = DecisionOutcome.bothWrong();

        assertTrue(outcome.correctDecision().isEmpty());
        assertTrue(outcome.updatedNode().isEmpty());
        assertEquals(2, outcome.wrongCount());
        assertFalse(outcome.oneOfTwoWrong());
        assertTrue(outcome.shouldBreak());
    }

    @Test
    void bothValid_shouldHaveZeroWrongCountAndNoBreak() {
        DecisionOutcome outcome = DecisionOutcome.bothValid();

        assertTrue(outcome.correctDecision().isEmpty());
        assertTrue(outcome.updatedNode().isEmpty());
        assertEquals(0, outcome.wrongCount());
        assertFalse(outcome.oneOfTwoWrong());
        assertFalse(outcome.shouldBreak());
    }

    @Test
    void oneCorrect_shouldContainDecisionAndNodeAndOneWrongFlag() {
        NonogramSolutionDecision decision = mock(NonogramSolutionDecision.class);
        NonogramSolutionNode node = mock(NonogramSolutionNode.class);

        DecisionOutcome outcome = DecisionOutcome.oneCorrect(decision, node, 1);

        assertEquals(Optional.of(decision), outcome.correctDecision());
        assertEquals(Optional.of(node), outcome.updatedNode());
        assertEquals(1, outcome.wrongCount());
        assertTrue(outcome.oneOfTwoWrong());
        assertTrue(outcome.shouldBreak());
    }
}