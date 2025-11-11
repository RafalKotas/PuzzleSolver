package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.actions;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.mixed.ColumnMixedActionsHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.mixed.ColumnOverextensionPrevention.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ColumnMixedActionsHelperTest {

    @Test
    @DisplayName("ColumnMixedActionsHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<ColumnMixedActionsHelper> constructor = ColumnMixedActionsHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("sequencesIdsInColumnIncludingField(o06005) – should return sequence ID(0) when given field is inside sequence range")
    void shouldReturnSequenceIdWhenFieldInsideRange() {
        // given
        int columnIdx = 3;
        List<List<Integer>> columnsSequencesRanges = List.of(
                List.of(2, 7)
        );
        Field fieldToCheckColoured = new Field(7, columnIdx);

        // when
        List<Integer> result = sequencesIdsInColumnIncludingField(columnsSequencesRanges, fieldToCheckColoured);

        // then
        assertThat(result).containsExactly(0);
    }

    @Test
    @DisplayName("sequencesIdsInColumnIncludingField – o06005, columnIdx=7 – should return [1] when field is inside second sequence range")
    void shouldReturnSecondSequenceIdForFieldInsideRange_o06005_column7() {
        // given
        List<List<Integer>> columnSequencesRanges = List.of(
                List.of(3, 4),
                List.of(6, 7),
                List.of(9, 9)
        );
        Field fieldToCheckColoured = new Field(7, 7);

        // when
        List<Integer> sequencesIds = sequencesIdsInColumnIncludingField(
                columnSequencesRanges,
                fieldToCheckColoured
        );

        // then
        assertThat(sequencesIds).containsExactly(1);
    }

    /*
        cases:
        firstIfNegative +
        firstIfPositive +
        secondIfNegative -
        secondIfPositive +
     */
    @Test
    @DisplayName("o06005: getColouredSequencesRangesInColumnInRangeToTop → [[2,7]] for col=3 row=7 maxLen=6 (3/4 conditions)")
    void shouldReturnCorrectColouredRangesForGivenColumnAndRow() {
        // given
        List<List<String>> solutionBoard = List.of(
                List.of("X", "X", "O", "X", "-", "-", "O", "-", "-", "-"),
                List.of("-", "-", "O", "X", "-", "X", "O", "-", "-", "-"),
                List.of("-", "-", "O", "O", "O", "O", "O", "X", "-", "-"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "X", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O"),
                List.of("-", "-", "X", "X", "-", "X", "O", "X", "-", "-"),
                List.of("-", "-", "O", "X", "-", "-", "O", "-", "-", "-")
        );
        int columnIdx = 3;
        int potentiallyColouredFieldRowIndex = 7;
        int maxSequenceLength = 6;

        // when
        List<List<Integer>> result = getColouredSequencesRangesInColumnInRangeToTop(
                        solutionBoard,
                        columnIdx,
                        potentiallyColouredFieldRowIndex,
                        maxSequenceLength
                );

        // then
        assertEquals(List.of(List.of(2, 7)), result);
    }

    @Test
    @DisplayName("should leave loop when currentRowIdx < minRow for o06005 (scan to top)")
    void shouldLeaveLoopWhenCurrentRowBelowMinRow_o06005() {
        // given
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("X","X","O","X","O","X","O","X","X","X")),
                new ArrayList<>(List.of("O","X","O","X","O","X","O","X","O","X")),
                new ArrayList<>(List.of("O","X","O","O","O","O","O","X","O","X")),
                new ArrayList<>(List.of("O","O","O","O","O","O","O","O","O","X")),
                new ArrayList<>(List.of("O","O","O","O","O","O","O","O","O","X")),
                new ArrayList<>(List.of("O","O","O","O","O","O","O","X","O","X")),
                new ArrayList<>(List.of("O","O","O","O","O","O","O","O","O","X")),
                new ArrayList<>(List.of("O","O","O","O","O","O","O","O","O","O")),
                new ArrayList<>(List.of("X","O","X","X","X","X","O","X","X","X")),
                new ArrayList<>(List.of("X","O","O","X","X","X","O","O","X","X"))
        ));
        int columnIdx = 4;
        int potentiallyColouredFieldRowIndex = 7;
        int maxSequenceLength = 8;

        // when
        List<List<Integer>> ranges = getColouredSequencesRangesInColumnInRangeToTop(
                board, columnIdx, potentiallyColouredFieldRowIndex, maxSequenceLength
        );

        // then
        assertThat(ranges).isEqualTo(List.of(List.of(0, 7)))
                .hasSize(1);
    }

    @Test
    @DisplayName("getColouredSequencesRangesInColumnInRangeToTop — o06041, col=7, hits empty cell branch and returns [3,5]")
    void shouldCollectColouredRangeToTop_whenEmptyCellBetweenOAndX_o06041_col7() {
        // given
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-")),
                new ArrayList<>(List.of("X", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "-", "-", "X", "O")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "X", "X", "-", "-", "-", "-", "-", "-", "X", "O")),
                new ArrayList<>(List.of("-", "O", "-", "X", "O", "X", "O", "O", "O", "O", "O", "O", "O", "X", "O")),
                new ArrayList<>(List.of("-", "-", "-", "X", "O", "X", "O", "O", "O", "O", "O", "O", "O", "X", "O")),
                new ArrayList<>(List.of("-", "-", "-", "-", "O", "X", "X", "O", "O", "O", "O", "O", "X", "X", "O")),
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "-", "X", "X", "O", "O", "O", "X", "-", "X", "O")),
                new ArrayList<>(List.of("-", "-", "-", "-", "O", "-", "X", "X", "-", "-", "-", "-", "-", "X", "O")),
                new ArrayList<>(List.of("-", "-", "-", "-", "O", "-", "X", "X", "X", "X", "X", "X", "X", "-", "O")),
                new ArrayList<>(List.of("-", "-", "-", "-", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-"))
        ));
        int columnIdx = 7;
        int potentiallyColouredFieldRowIndex = 5;
        int maxSequenceLength = 4;

        // when
        List<List<Integer>> ranges = getColouredSequencesRangesInColumnInRangeToTop(
                        board, columnIdx, potentiallyColouredFieldRowIndex, maxSequenceLength);

        // then
        assertThat(ranges).isEqualTo(List.of(List.of(3, 5)))
                .hasSize(1);
    }

    /*
        cases:
        firstIfPositive +
        firstIfNegative +
        secondIfPositive +
        secondIfNegative -
     */
    @Test
    @DisplayName("o06005: getColouredSequencesRangesInColumnInRangeToBottom → [[2,7]] for col=3 row=2 maxLen=6 (3/4 conditions)")
    void shouldReturnCorrectColouredRangesForGivenColumnAndRow_Bottom() {
        // given
        List<List<String>> solutionBoard = List.of(
                List.of("X", "X", "O", "X", "-", "-", "O", "-", "-", "-"),
                List.of("-", "-", "O", "X", "-", "X", "O", "-", "-", "-"),
                List.of("-", "-", "O", "O", "O", "O", "O", "X", "-", "-"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "X", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O"),
                List.of("-", "-", "X", "X", "-", "X", "O", "X", "-", "-"),
                List.of("-", "-", "O", "X", "-", "-", "O", "-", "-", "-")
        );
        int columnIdx = 3;
        int potentiallyColouredFieldRowIndex = 2;
        int maxSequenceLength = 6;

        // when
        List<List<Integer>> result = getColouredSequencesRangesInColumnInRangeToBottom(
                solutionBoard,
                columnIdx,
                potentiallyColouredFieldRowIndex,
                maxSequenceLength
        );

        // then
        assertEquals(List.of(List.of(2, 7)), result);
    }

    /*
        conditions:
        1. next field row is equal to nonogram height
        2. currentField is not coloured and not with X (empty)
        3. currentRowIdx > maxRow
     */
    @Test
    @DisplayName("o10642: getColouredSequencesRangesInColumnInRangeToBottom → [[8,17],[19,19]] for col=5 row=8 maxLen=12 (other conditions)")
    void shouldReturnTwoColouredRanges_AllConditionsCovered() {
        // given
        List<List<String>> solutionBoard = List.of(
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "O", "-", "-"),
                List.of("X", "X", "X", "-", "O", "O", "O", "O", "O", "-", "-", "-", "O", "-", "-"),
                List.of("X", "X", "O", "O", "O", "O", "O", "O", "O", "X", "-", "-", "X", "-", "-"),
                List.of("X", "O", "O", "O", "O", "O", "O", "O", "O", "X", "-", "-", "O", "-", "-"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "-", "-", "O", "-", "-"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X"),
                List.of("O", "X", "O", "O", "O", "O", "O", "O", "O", "X", "-", "-", "X", "X", "X"),
                List.of("O", "X", "X", "-", "-", "O", "O", "-", "-", "-", "X", "X", "X", "X", "X"),
                List.of("O", "X", "O", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("O", "O", "X", "X", "X", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"),
                List.of("X", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X")
        );
        int columnIdx = 5;
        int potentiallyColouredFieldRowIndex = 8;
        int maxSequenceLength = 12;

        // when
        List<List<Integer>> ranges = getColouredSequencesRangesInColumnInRangeToBottom(
                solutionBoard, columnIdx, potentiallyColouredFieldRowIndex, maxSequenceLength
        );

        // then
        assertEquals(List.of(List.of(8, 17), List.of(19, 19)), ranges);
    }

    @Test
    @DisplayName("o07986: findValidSequencesIdsMergingToTop should return [1], column 8")
    void shouldReturnValidSequenceIdsMergingToTop_o07986() {
        // given
        List<Integer> sequenceIds = List.of(0, 1);
        List<Integer> expectedLengths = List.of(1, 2);
        int rowIndexBeforeX = 4;
        List<List<Integer>> colouredSequences = List.of(
                List.of(3, 4)
        );

        // when
        List<Integer> result = findValidSequencesIdsMergingToTop(
                sequenceIds, expectedLengths, rowIndexBeforeX, colouredSequences
        );

        // then
        assertThat(result).isEqualTo(List.of(1));
    }

    @Test
    @DisplayName("o07986: findValidSequencesIdsMergingToBottom should return [1], column 8")
    void shouldReturnValidSequenceIdsMergingToBottom_o07986() {
        // given
        List<Integer> sequenceIds = List.of(0, 1);
        List<Integer> expectedLengths = List.of(1, 2);
        int colouredRowIndexAfterX = 3;
        List<List<Integer>> colouredSequences = List.of(
                List.of(3, 4)
        );

        // when
        List<Integer> result = findValidSequencesIdsMergingToBottom(
                sequenceIds, expectedLengths, colouredRowIndexAfterX, colouredSequences
        );

        // then
        assertThat(result).isEqualTo(List.of(1));
    }
}