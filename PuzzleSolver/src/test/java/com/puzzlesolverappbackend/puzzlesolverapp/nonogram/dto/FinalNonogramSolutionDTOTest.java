package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FinalNonogramSolutionDTOTest {

    @Test
    @DisplayName("Should correctly set and get all values in FinalNonogramSolutionDTO")
    void shouldCorrectlySetAndGetAllFields() {
        // given
        List<List<String>> finalBoard = List.of(
                List.of("X", "X", "O", "X", "O", "X", "O", "X", "X", "X"),
                List.of("O", "X", "O", "X", "O", "X", "O", "X", "O", "X"),
                List.of("O", "X", "O", "O", "O", "O", "O", "X", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "X", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O"),
                List.of("X", "O", "X", "X", "X", "X", "O", "X", "X", "X"),
                List.of("X", "O", "O", "X", "X", "X", "O", "O", "X", "X")
        );

        List<List<List<Integer>>> derivedRowRanges = List.of(
                List.of(List.of(2, 2), List.of(4, 4), List.of(6, 6)),
                List.of(List.of(0, 0), List.of(2, 2), List.of(4, 4), List.of(6, 6), List.of(8, 8)),
                List.of(List.of(0, 0), List.of(2, 6), List.of(8, 8)),
                List.of(List.of(0, 8)),
                List.of(List.of(0, 8)),
                List.of(List.of(0, 6), List.of(8, 8)),
                List.of(List.of(0, 8)),
                List.of(List.of(0, 9)),
                List.of(List.of(1, 1), List.of(6, 6)),
                List.of(List.of(1, 2), List.of(6, 7))
        );

        List<List<List<Integer>>> derivedColumnRanges = List.of(
                List.of(List.of(1, 7)),
                List.of(List.of(3, 9)),
                List.of(List.of(0, 7), List.of(9, 9)),
                List.of(List.of(2, 7)),
                List.of(List.of(0, 7)),
                List.of(List.of(2, 7)),
                List.of(List.of(0, 9)),
                List.of(List.of(3, 4), List.of(6, 7), List.of(9, 9)),
                List.of(List.of(1, 7)),
                List.of(List.of(7, 7))
        );

        String verified = "PASS";

        FinalNonogramSolutionDTO dto = new FinalNonogramSolutionDTO();

        // when
        dto.setFinalBoard(finalBoard);
        dto.setDerivedRowRanges(derivedRowRanges);
        dto.setDerivedColumnRanges(derivedColumnRanges);
        dto.setVerifiedAgainstOriginal(verified);

        // then
        assertThat(dto.getFinalBoard()).isEqualTo(finalBoard);
        assertThat(dto.getDerivedRowRanges()).isEqualTo(derivedRowRanges);
        assertThat(dto.getDerivedColumnRanges()).isEqualTo(derivedColumnRanges);
        assertThat(dto.getVerifiedAgainstOriginal()).isEqualTo("PASS");
    }
}
