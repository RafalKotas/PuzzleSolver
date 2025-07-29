package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ColouringHelperTest {

    @DisplayName("calculateOverlappingRange should return empty list for range [0, 5] and sequenceLength = 1 o06005 - row 0")
    @Test
    void shouldReturnEmptyListWhenNoOverlapExists() {
        // given
        List<Integer> range = List.of(0, 5);
        int sequenceLength = 1;

        // when
        List<Integer> result = ColouringHelper.calculateOverlappingRange(range, sequenceLength);

        // then
        assertTrue(result.isEmpty(), "Expected empty list for non-overlapping case");
    }

    @DisplayName("calculateOverlappingRange should return [3, 4, 5, 6] for range [2, 7] and sequenceLength = 5 o06005 - row 2")
    @Test
    void shouldReturnCorrectOverlappingRangeWhenExists() {
        // given
        List<Integer> range = List.of(2, 7);
        int sequenceLength = 5;

        // when
        List<Integer> result = ColouringHelper.calculateOverlappingRange(range, sequenceLength);

        // then
        assertEquals(List.of(3, 4, 5, 6), result);
    }

    @DisplayName("findPossibleSequenceLengths should return [7] for colouredRange [3, 7] within [1, 9] and length 7 o06005 - column 0")
    @Test
    void shouldReturnMatchingLengthWhenColouredRangeFits() {
        // given
        List<List<Integer>> ranges = List.of(List.of(1, 9));
        List<Integer> colouredRange = List.of(3, 7);
        List<Integer> lengths = List.of(7);

        // when
        List<Integer> result = ColouringHelper.findPossibleSequenceLengths(ranges, colouredRange, lengths);

        // then
        assertEquals(List.of(7), result);
    }

    @DisplayName("findPossibleSequenceLengths should filter out non-matching ranges o06005 - column 2")
    @Test
    void shouldReturnOnlyMatchingLengthsWhenColouredRangeFitsSomeRanges() {
        // given
        List<List<Integer>> ranges = List.of(List.of(0, 7), List.of(9, 9));
        List<Integer> colouredRange = List.of(0, 7);
        List<Integer> lengths = List.of(8, 1);

        // when
        List<Integer> result = ColouringHelper.findPossibleSequenceLengths(ranges, colouredRange, lengths);

        // then
        assertEquals(List.of(8), result);
    }

    @DisplayName("findColouredSequenceRangeTop should return [2, 2] when starting at coloured cell with X above o07836 - column 7")
    @Test
    void shouldReturnSingleFieldRangeWhenXIsAboveColouredField() {
        // given
        List<List<String>> board = List.of(
                List.of("-", "-", "X", "X", "-", "-", "-", "-", "-", "-"),
                List.of("-", "X", "-", "X", "-", "-", "-", "-", "-", "-"),
                List.of("X", "X", "O", "O", "O", "O", "O", "O", "X", "X"),
                List.of("X", "X", "O", "O", "O", "O", "O", "O", "O", "O"),
                List.of("O", "O", "O", "O", "X", "X", "X", "X", "-", "O"),
                List.of("O", "O", "O", "O", "X", "-", "-", "-", "-", "O"),
                List.of("X", "-", "O", "O", "O", "O", "O", "X", "X", "X"),
                List.of("X", "X", "X", "O", "O", "O", "O", "X", "X", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "-", "-"),
                List.of("O", "O", "O", "O", "-", "-", "-", "-", "-", "-")
        );
        int columnIdx = 7;
        int startRowIdx = 2;

        // when
        List<Integer> result = ColouringHelper.findColouredSequenceRangeTop(board, columnIdx, startRowIdx);

        // then
        assertEquals(List.of(2, 2), result);
    }

    @DisplayName("findColouredSequenceRangeTop should return [1, 2] for vertical coloured segment extended upwards o07986 column 5")
    @Test
    void shouldReturnVerticalColouredRangeAboveWhenOsPresent() {
        // given
        List<List<String>> board = List.of(
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "X", "-"),
                List.of("-", "-", "-", "-", "-", "O", "-", "X", "-", "O"),
                List.of("-", "-", "-", "-", "-", "O", "-", "X", "-", "O"),
                List.of("-", "-", "-", "-", "-", "X", "O", "O", "O", "X"),
                List.of("-", "-", "-", "-", "-", "X", "O", "O", "O", "X"),
                List.of("-", "X", "X", "O", "O", "X", "O", "O", "O", "X"),
                List.of("-", "X", "O", "O", "O", "X", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "X", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "X", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "X", "O", "O", "O", "O")
        );
        int columnIdx = 5;
        int startRowIdx = 2;

        // when
        List<Integer> result = ColouringHelper.findColouredSequenceRangeTop(board, columnIdx, startRowIdx);

        // then
        assertEquals(List.of(1, 2), result);
    }

    @DisplayName("findColouredSequenceRangeTop should return [0, 0] when startRowIdx is 0 (while skipped) o07986 column 10")
    @Test
    void shouldReturnSingleFieldWhenStartRowIsZero() {
        // given
        List<List<String>> board = List.of(
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "X", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "O"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "O", "O", "X"),
                List.of("-", "-", "-", "-", "-", "-", "X", "O", "O", "X"),
                List.of("-", "-", "-", "-", "-", "X", "O", "O", "O", "X"),
                List.of("X", "O", "O", "O", "O", "O", "O", "O", "X", "X"),
                List.of("X", "O", "O", "O", "O", "O", "O", "O", "X", "X"),
                List.of("O", "O", "X", "O", "O", "X", "O", "X", "X", "X"),
                List.of("O", "O", "O", "X", "O", "O", "X", "O", "O", "O")
        );
        int columnIdx = 10;
        int startRowIdx = 0;

        // when
        List<Integer> result = ColouringHelper.findColouredSequenceRangeTop(board, columnIdx, startRowIdx);

        // then
        assertEquals(List.of(0, 0), result);
    }

    @Test
    @DisplayName("findColouredSequenceRangeBottom should return [3, 7] when startRowIdx is 3 o06371 column 6")
    void shouldFindColouredRangeBottomFromRow3To7() {
        // given
        List<List<String>> board = List.of(
                List.of("-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-"),
                List.of("-", "O", "O", "O", "O", "O", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-"),
                List.of("O", "O", "O", "O", "O", "X", "O", "O", "O", "O", "O", "O", "O", "X", "O", "O", "O", "O", "X", "O"),
                List.of("-", "-", "O", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "O", "O", "-", "-"),
                List.of("-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("O", "O", "O", "O", "X", "O", "O", "O", "O", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O"),
                List.of("-", "-", "-", "-", "-", "X", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "O", "O", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-")
        );

        int columnIdx = 6;
        int startRowIdx = 3;

        // when
        List<Integer> result = ColouringHelper.findColouredSequenceRangeBottom(board, columnIdx, startRowIdx);

        // then
        assertEquals(List.of(3, 7), result);
    }

    @Test
    @DisplayName("findColouredSequenceRangeBottom should return [19, 19] when startRowIdx is 19 o08079 column 9")
    void shouldReturnSameStartAndEndWhenStartingFromLastRow() {
        // given
        List<List<String>> board = List.of(
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-"),
                List.of("O", "O", "O", "O", "O", "O", "O", "X", "O", "X", "O", "O", "X", "O", "O", "O", "O", "X", "O", "O"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O"),
                List.of("-", "-", "-", "-", "-", "-", "-", "O", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "O", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
        );
        int columnIdx = 9;
        int startRowIdx = 19;

        // when
        List<Integer> result = ColouringHelper.findColouredSequenceRangeBottom(board, columnIdx, startRowIdx);

        // then
        assertEquals(List.of(19, 19), result);
    }

    @DisplayName("Should return 0 when no 'X' found within maxDist above coloured range o08079 column 0")
    @Test
    void shouldReturnZeroWhenNoXFoundAboveWithinMaxDist() {
        // given
        List<List<String>> board = List.of(
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-"),
                List.of("O", "O", "O", "O", "O", "O", "O", "X", "O", "X", "O", "O", "X", "O", "O", "O", "O", "X", "O", "O"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")
        );
        int columnIdx = 0;
        int maxDist = 2;
        List<Integer> colouredRange = List.of(16, 17);

        // when
        int result = ColouringHelper.findDistanceFromTopX(board, columnIdx, colouredRange, maxDist);

        // then
        assertEquals(0, result);
    }
}