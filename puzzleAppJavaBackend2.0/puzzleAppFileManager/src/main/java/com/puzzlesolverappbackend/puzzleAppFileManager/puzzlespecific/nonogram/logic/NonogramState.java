package com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class NonogramState {

    private int newStepsMade;

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
