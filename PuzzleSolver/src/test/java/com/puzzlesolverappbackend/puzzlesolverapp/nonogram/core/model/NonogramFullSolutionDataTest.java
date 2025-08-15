package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class NonogramFullSolutionDataTest {

    @DisplayName("All-args constructor should set fields and getters should return the same data - ro07929")
    @Test
    void allArgsConstructor_and_getters() {
        // given
        List<List<String>> finalBoard = List.of(
                List.of("X","X","X","X","X","X","X","X","X","X","X","O","X","X","X","X","O","O","O","O"),
                List.of("X","X","X","X","X","X","X","X","X","X","O","O","O","X","X","X","O","X","X","O"),
                List.of("X","X","X","X","X","X","X","X","X","O","O","X","O","O","X","X","O","X","X","O"),
                List.of("X","X","X","X","X","X","X","X","O","O","O","O","O","O","O","X","O","X","X","O"),
                List.of("X","X","X","X","X","X","X","O","O","O","X","X","O","X","O","X","O","O","O","O"),
                List.of("X","X","X","X","X","X","O","O","O","X","X","X","O","O","O","O","O","O","O","O"),
                List.of("X","X","X","X","X","O","O","O","X","X","X","X","X","X","O","O","O","O","X","X"),
                List.of("X","X","X","X","O","O","O","X","X","X","X","X","O","O","O","O","O","O","O","O"),
                List.of("X","X","O","O","O","O","X","X","X","X","X","X","O","O","X","O","X","O","X","O"),
                List.of("O","O","O","X","O","X","X","X","X","X","X","X","O","O","X","O","X","O","X","O"),
                List.of("O","O","O","O","O","X","X","X","X","X","X","X","O","O","O","O","O","O","O","O"),
                List.of("O","X","X","X","X","X","X","X","O","O","O","O","O","X","O","O","O","X","O","O"),
                List.of("O","X","X","X","X","X","X","X","O","X","X","X","X","X","X","X","X","X","X","X"),
                List.of("O","X","X","X","X","X","X","X","X","X","X","X","X","X","X","O","O","O","X","X"),
                List.of("O","O","O","O","X","X","X","O","O","O","X","X","X","X","O","O","X","O","O","X"),
                List.of("X","X","X","X","X","X","O","O","X","O","O","X","X","O","O","X","X","X","O","O"),
                List.of("O","X","X","X","O","O","O","X","X","X","O","O","O","O","X","X","X","X","X","X"),
                List.of("O","O","X","X","O","X","X","O","O","O","X","X","O","X","X","X","O","O","O","O"),
                List.of("X","O","O","O","O","X","O","O","O","O","O","X","O","O","X","O","O","O","O","O"),
                List.of("X","X","O","X","X","X","O","O","X","O","O","X","X","O","O","O","O","O","O","O")
        );

        List<List<List<Integer>>> derivedRowRanges = List.of(
                List.of(List.of(11,11), List.of(16,19)),
                List.of(List.of(10,12), List.of(16,16), List.of(19,19)),
                List.of(List.of(9,10), List.of(12,13), List.of(16,16), List.of(19,19)),
                List.of(List.of(8,14), List.of(16,16), List.of(19,19)),
                List.of(List.of(7,9), List.of(12,12), List.of(14,14), List.of(16,19)),
                List.of(List.of(6,8), List.of(12,19)),
                List.of(List.of(5,7), List.of(14,17)),
                List.of(List.of(4,6), List.of(12,19)),
                List.of(List.of(2,5), List.of(12,13), List.of(15,15), List.of(17,17), List.of(19,19)),
                List.of(List.of(0,2), List.of(4,4), List.of(12,13), List.of(15,15), List.of(17,17), List.of(19,19)),
                List.of(List.of(0,4), List.of(12,19)),
                List.of(List.of(0,0), List.of(8,12), List.of(14,16), List.of(18,19)),
                List.of(List.of(0,0), List.of(8,8)),
                List.of(List.of(0,0), List.of(15,17)),
                List.of(List.of(0,3), List.of(7,9), List.of(14,15), List.of(17,18)),
                List.of(List.of(6,7), List.of(9,10), List.of(13,14), List.of(18,19)),
                List.of(List.of(0,0), List.of(4,6), List.of(10,13)),
                List.of(List.of(0,1), List.of(4,4), List.of(7,9), List.of(12,12), List.of(16,19)),
                List.of(List.of(1,4), List.of(6,10), List.of(12,13), List.of(15,19)),
                List.of(List.of(2,2), List.of(6,7), List.of(9,10), List.of(13,19))
        );

        List<List<List<Integer>>> derivedColumnRanges = List.of(
                List.of(List.of(9,14), List.of(16,17)),
                List.of(List.of(9,10), List.of(14,14), List.of(17,18)),
                List.of(List.of(8,10), List.of(14,14), List.of(18,19)),
                List.of(List.of(8,8), List.of(10,10), List.of(14,14), List.of(18,18)),
                List.of(List.of(7,10), List.of(16,18)),
                List.of(List.of(6,8), List.of(16,16)),
                List.of(List.of(5,7), List.of(15,16), List.of(18,19)),
                List.of(List.of(4,6), List.of(14,15), List.of(17,19)),
                List.of(List.of(3,5), List.of(11,12), List.of(14,14), List.of(17,18)),
                List.of(List.of(2,4), List.of(11,11), List.of(14,15), List.of(17,19)),
                List.of(List.of(1,3), List.of(11,11), List.of(15,16), List.of(18,19)),
                List.of(List.of(0,1), List.of(3,3), List.of(11,11), List.of(16,16)),
                List.of(List.of(1,5), List.of(7,11), List.of(16,18)),
                List.of(List.of(2,3), List.of(5,5), List.of(7,10), List.of(15,16), List.of(18,19)),
                List.of(List.of(3,7), List.of(10,11), List.of(14,15), List.of(19,19)),
                List.of(List.of(5,11), List.of(13,14), List.of(18,19)),
                List.of(List.of(0,7), List.of(10,11), List.of(13,13), List.of(17,19)),
                List.of(List.of(0,0), List.of(4,10), List.of(13,14), List.of(17,19)),
                List.of(List.of(0,0), List.of(4,5), List.of(7,7), List.of(10,11), List.of(14,15), List.of(17,19)),
                List.of(List.of(0,5), List.of(7,11), List.of(15,15), List.of(17,19))
        );

        // when
        NonogramFullSolutionData data = new NonogramFullSolutionData(finalBoard, derivedRowRanges, derivedColumnRanges);

        // then
        assertThat(data.getFinalBoard()).isEqualTo(finalBoard);
        assertThat(data.getDerivedRowRanges()).isEqualTo(derivedRowRanges);
        assertThat(data.getDerivedColumnRanges()).isEqualTo(derivedColumnRanges);
    }

    @DisplayName("Getters should expose the same references that were provided to the constructor")
    @Test
    void getters_returnSameReferences() {
        // given
        List<List<String>> finalBoard = List.of(List.of("X"));
        List<List<List<Integer>>> derivedRowRanges = List.of(List.of(List.of(0, 0)));
        List<List<List<Integer>>> derivedColumnRanges = List.of(List.of(List.of(1, 1)));

        // when
        NonogramFullSolutionData data = new NonogramFullSolutionData(finalBoard, derivedRowRanges, derivedColumnRanges);

        // then
        assertThat(data.getFinalBoard()).isSameAs(finalBoard);
        assertThat(data.getDerivedRowRanges()).isSameAs(derivedRowRanges);
        assertThat(data.getDerivedColumnRanges()).isSameAs(derivedColumnRanges);
    }
}