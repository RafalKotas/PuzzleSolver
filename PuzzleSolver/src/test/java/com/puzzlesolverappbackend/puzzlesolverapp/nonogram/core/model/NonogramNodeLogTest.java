package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solutions.NonogramSolutionDecision;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NonogramNodeLogTest {

    @Test
    @DisplayName("Should create NonogramNodeLog and set guessesLogs")
    void shouldCreateNonogramNodeLogAndSetGuessesLogs() {
        // given
        NonogramNodeLog nonogramNodeLog = new NonogramNodeLog(
                List.of(
                        "example heuristic log"
                )
        );

        // when
        NonogramSolutionDecision nonogramSolutionDecision = new NonogramSolutionDecision(
                "X", new Field(0, 0)
        );
        NonogramGuessActionsLog nonogramGuessActionsLog = new NonogramGuessActionsLog(
                nonogramSolutionDecision,
                List.of("action1", "action2", "action3")
        );
        nonogramNodeLog.setGuessesLogs(
                List.of(nonogramGuessActionsLog)
        );
        nonogramNodeLog.setHeuristicsLogs(
                List.of("action4", "action5", "action6")
        );

        // then
        assertThat(nonogramNodeLog.getHeuristicsLogs()).containsExactly("action4", "action5", "action6");
        assertThat(nonogramNodeLog.getGuessesLogs().get(0)).isEqualTo(nonogramGuessActionsLog);
    }
}