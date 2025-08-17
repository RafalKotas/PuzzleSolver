package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NonogramSolutionTest {
    
    @Test
    @DisplayName("Should create NonogramSolution object - example nonogram")
    void shouldCreateNonogramSolution() {
        // given
        List<List<String>> nonogramBoard = new ArrayList<>();
        nonogramBoard.add(new ArrayList<>(List.of("O", "O", "O")));
        nonogramBoard.add(new ArrayList<>(List.of("O", "X", "O")));
        nonogramBoard.add(new ArrayList<>(List.of("O", "O", "O")));

        // when
        NonogramSolution nonogramSolution = new NonogramSolution();
        nonogramSolution.setNonogramBoard(nonogramBoard);

        // then
        assertThat(nonogramSolution.getNonogramBoard()).isEqualTo(
                new ArrayList<>(
                        List.of(
                                new ArrayList<>(List.of("O", "O", "O")),
                                new ArrayList<>(List.of("O", "X", "O")),
                                new ArrayList<>(List.of("O", "O", "O"))
                        )
                )
        );
    }
}