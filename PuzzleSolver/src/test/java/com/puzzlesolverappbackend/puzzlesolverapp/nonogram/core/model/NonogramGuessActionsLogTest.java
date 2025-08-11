package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solutions.NonogramSolutionDecision;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

class NonogramGuessActionsLogTest {

    @Test
    @DisplayName("All-args constructor should set both fields and getters should return them")
    void constructorShouldSetFieldsAndReturnViaGetters() {
        // given
        NonogramSolutionDecision decision = mock(NonogramSolutionDecision.class);
        List<String> actions = List.of("place X", "colour O");

        // when
        NonogramGuessActionsLog log = new NonogramGuessActionsLog(decision, actions);

        // then
        assertThat(log.getGuessDecision()).isSameAs(decision);
        assertThat(log.getActions()).isEqualTo(actions);
    }
}