package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class NonogramStateTest {

    @Test
    @DisplayName("buildInitialEmptyNonogramState should create state with 0 steps and valid solution")
    void shouldBuildInitialEmptyState() {
        // when
        NonogramState state = NonogramState.buildInitialEmptyNonogramState();

        // then
        assertThat(state.getNewStepsMade()).isEqualTo(0);
        assertThat(state.isInvalidSolution()).isFalse();
    }

    @Test
    @DisplayName("increaseMadeSteps should increment steps counter")
    void shouldIncreaseMadeSteps() {
        // given
        NonogramState state = NonogramState.builder()
                .newStepsMade(5)
                .invalidSolution(false)
                .build();

        // when
        state.increaseMadeSteps();
        state.increaseMadeSteps();

        // then
        assertThat(state.getNewStepsMade()).isEqualTo(7);
        assertThat(state.isInvalidSolution()).isFalse();
    }

    @Test
    @DisplayName("invalidateSolution should mark solution as invalid")
    void shouldInvalidateSolution() {
        // given
        NonogramState state = NonogramState.buildInitialEmptyNonogramState();

        // when
        state.invalidateSolution();

        // then
        assertThat(state.isInvalidSolution()).isTrue();
        assertThat(state.getNewStepsMade()).isEqualTo(0);
    }

    @Test
    @DisplayName("Builder should set fields and getters should return them")
    void builderAndGettersShouldWork() {
        // when
        NonogramState state = NonogramState.builder()
                .newStepsMade(12)
                .invalidSolution(true)
                .build();

        // then
        assertThat(state.getNewStepsMade()).isEqualTo(12);
        assertThat(state.isInvalidSolution()).isTrue();
    }
}