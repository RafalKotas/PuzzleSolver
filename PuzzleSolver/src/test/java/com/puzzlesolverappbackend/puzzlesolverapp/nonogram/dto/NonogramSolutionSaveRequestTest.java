package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NonogramSolutionSaveRequestTest {

    @Test
    @DisplayName("Should create NonogramSolutionSaveRequest set and set values")
    void shouldCreateNonogramSolutionSaveRequest() {
        // given
        NonogramSolutionSaveRequest subject = new NonogramSolutionSaveRequest();
        subject.setFileName("exampleFileName");
        List<List<String>> board = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList("O", "X", "O")),
                new ArrayList<>(Arrays.asList("X", "O", "O"))
        ));
        subject.setBoard(board);
        subject.setRowSequences(List.of(
                List.of(1, 1),
                List.of(2)
        ));
        subject.setColumnSequences(List.of(
                List.of(1),
                List.of(1),
                List.of(2)
        ));

        // when && then
        assertThat(subject.getFileName()).isEqualTo("exampleFileName");
        assertThat(subject.getBoard()).isEqualTo(new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList("O", "X", "O")),
                new ArrayList<>(Arrays.asList("X", "O", "O"))
        )));
        assertThat(subject.getRowSequences()).isEqualTo(List.of(
                List.of(1, 1),
                List.of(2)
        ));
        assertThat(subject.getColumnSequences()).isEqualTo(List.of(
                List.of(1),
                List.of(1),
                List.of(2)
        ));
    }
}