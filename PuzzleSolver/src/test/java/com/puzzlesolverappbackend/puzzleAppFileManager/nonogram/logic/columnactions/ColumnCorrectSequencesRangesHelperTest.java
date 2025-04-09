package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColumnCorrectSequencesRangesHelperTest {

    @Test
    @DisplayName("Reduce coloured sequence matches 9 2 5")
    public void testReduceColouredSequenceMatches1() {
        // given
        List<Integer> columnSequencesLengths = Arrays.asList(9, 2, 5);
        List<List<Integer>> colouredSequencesRanges = Arrays.asList(
                Arrays.asList(8, 10),
                Arrays.asList(16, 16),
                Arrays.asList(20, 20)
        );
        List<List<Integer>> colouredSequencesPartsMatches = new ArrayList<>();
        colouredSequencesPartsMatches.add(List.of(0));
        colouredSequencesPartsMatches.add(Arrays.asList(1, 2));
        colouredSequencesPartsMatches.add(Arrays.asList(1, 2));

        // when
        List<List<Integer>> result = ColumnCorrectSequencesRangesHelper.reduceColouredSequenceMatches(
                columnSequencesLengths,
                colouredSequencesRanges,
                colouredSequencesPartsMatches
        );

        // then
        assertEquals(List.of(0), result.get(0));
        assertEquals(List.of(1, 2), result.get(1));
        assertEquals(List.of(2), result.get(2));
    }

    @Test
    @DisplayName("Reduce coloured sequence matches 3 8 1 5 1")
    public void testReduceColouredSequenceMatches2() {
        // given
        List<Integer> columnSequencesLengths = Arrays.asList(3, 8, 1, 5, 1);
        List<List<Integer>> colouredSequencesRanges = Arrays.asList(
                Arrays.asList(3, 3),
                Arrays.asList(12, 12),
                Arrays.asList(19, 19),
                Arrays.asList(24, 24)
        );
        List<List<Integer>> colouredSequencesPartsMatches = new ArrayList<>();
        colouredSequencesPartsMatches.add(List.of(0));
        colouredSequencesPartsMatches.add(List.of(1));
        colouredSequencesPartsMatches.add(Arrays.asList(1, 2, 3));
        colouredSequencesPartsMatches.add(Arrays.asList(3, 4));

        // when
        List<List<Integer>> result = ColumnCorrectSequencesRangesHelper.reduceColouredSequenceMatches(
                columnSequencesLengths,
                colouredSequencesRanges,
                colouredSequencesPartsMatches
        );

        // then
        assertEquals(List.of(0), result.get(0));
        assertEquals(List.of(1), result.get(1));
        assertEquals(List.of(1, 2, 3), result.get(2));
        assertEquals(List.of(3, 4), result.get(3));
    }
}