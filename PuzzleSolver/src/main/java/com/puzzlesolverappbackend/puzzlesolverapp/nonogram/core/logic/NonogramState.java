package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
public class NonogramState {

    private int newStepsMade;

    @Setter
    private boolean invalidSolution;

    public static NonogramState buildInitialEmptyNonogramState() {
        return NonogramState.builder()
                .newStepsMade(0)
                .invalidSolution(false)
                .build();
    }

    public void increaseMadeSteps() {
        this.newStepsMade += 1;
    }

    public void invalidateSolution() {
        this.invalidSolution = true;
    }
}

