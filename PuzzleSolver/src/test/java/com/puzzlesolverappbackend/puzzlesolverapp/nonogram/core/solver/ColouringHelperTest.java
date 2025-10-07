package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramState;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.colouring.NonogramFieldColouringHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.NonogramColumnLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row.NonogramRowLogic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ColouringHelperTest {

    @Mock NonogramColumnLogic nonogramColumnLogic;

    @Mock NonogramRowLogic nonogramRowLogic;

    @Mock NonogramFieldColouringHelper colouringHelper;

    @Mock NonogramActionScheduler scheduler;

    @Mock NonogramState state;

    @Mock
    NonogramRules rules;

    @Captor
    ArgumentCaptor<Field> fieldCaptor;

    @Test
    @DisplayName("ColouringHelper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<ColouringHelper> constructor = ColouringHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
    }

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

    @DisplayName("findDistanceFromTopX - should return 0 when no 'X' found within maxDist above coloured range")
    @Test // o08079 column 0
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

    @DisplayName("findDistanceFromTopX - should return offset when 'X' is below at a distance no greater than the maximum")
    @Test  // o07836 column 7
    void shouldReturnTheOffsetWhenXIsAboveAt_a_DistanceNoGreaterThanTheMaximum() {
        // given
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("X", "X", "X", "X", "-", "X", "X", "O", "-", "-")),
                new ArrayList<>(List.of("X", "X", "X", "O", "O", "-", "-", "O", "-", "-")),
                new ArrayList<>(List.of("X", "X", "X", "O", "O", "O", "O", "O", "X", "X")),
                new ArrayList<>(List.of("X", "X", "X", "O", "O", "O", "O", "X", "X", "O")),
                new ArrayList<>(List.of("X", "-", "O", "O", "O", "-", "-", "-", "X", "O")),
                new ArrayList<>(List.of("X", "O", "O", "O", "O", "X", "X", "X", "X", "O")),
                new ArrayList<>(List.of("X", "-", "O", "O", "X", "X", "O", "O", "X", "X")),
                new ArrayList<>(List.of("X", "X", "X", "O", "X", "X", "O", "O", "X", "X")),
                new ArrayList<>(List.of("O", "X", "O", "O", "X", "X", "O", "-", "-", "X")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "X", "O", "-", "-", "X"))
        ));
        int columnIdx = 7;
        int maxDist = 4;
        List<Integer> colouredRange = List.of(6, 7);

        // when
        int resultOffset = ColouringHelper.findDistanceFromTopX(board, columnIdx, colouredRange, maxDist);

        // then
        assertThat(resultOffset).isEqualTo(2);
    }

    @DisplayName("findDistanceFromTopX - should return 0 if rowIdxToCheck less than zero")
    @Test
    void shouldReturnZeroWhenRowIdxLessThanZero() {
        // given
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "-", "-", "O", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "O", "O", "O", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "O", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "O", "O", "O", "O", "O", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "O", "O", "-", "O", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("O", "O", "O", "-", "O", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "-", "-", "-", "-")),
                new ArrayList<>(List.of("O", "O", "-", "O", "O", "O", "O", "-", "-", "-")),
                new ArrayList<>(List.of("O", "O", "-", "-", "O", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "O", "-", "-", "X", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "O", "O", "O", "O", "O", "-", "-", "-", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O")),
                new ArrayList<>(List.of("-", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("-", "-", "-", "-", "O", "O", "-", "-", "-", "-"))
        ));

        int columnIdx = 6;
        int maxDist = 3;
        List<Integer> colouredRange = List.of(1, 1);

        // when
        int resultOffset = ColouringHelper.findDistanceFromTopX(board, columnIdx, colouredRange, maxDist);

        // then
        assertThat(resultOffset).isEqualTo(0);
    }

    @DisplayName("findDistanceFromBottomX - should return 0 when no 'X' found within maxDist below coloured range")
    @Test // o06005 column 0
    void shouldReturnZeroWhenNoXFoundBelowWithinMaxDist() {
        // given
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "O", "O", "-", "-", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "-", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O")),
                new ArrayList<>(List.of("-", "-", "X", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"))
        ));
        int columnIdx = 0;
        int maxDist = 7;
        List<Integer> colouredRange = List.of(3, 7);

        // when
        int result = ColouringHelper.findDistanceFromBottomX(board, columnIdx, colouredRange, maxDist);

        // then
        assertEquals(0, result);
    }

    @DisplayName("findDistanceFromBottomX - should return offset when 'X' is below at a distance no greater than the maximum")
    @Test  // o07836 column 2
    void shouldReturnTheOffsetWhenXIsBelowAt_a_DistanceNoGreaterThanTheMaximum() {
        // given
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "O", "X", "X", "X", "-")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "O", "X", "-", "-", "-", "X", "X", "O", "O", "O")),
                new ArrayList<>(List.of("-", "O", "O", "-", "X", "O", "O", "X", "X", "X", "O", "O", "O", "O", "X")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "X", "X", "O", "O", "O", "O", "X", "O", "X")),
                new ArrayList<>(List.of("-", "-", "-", "-", "X", "X", "O", "O", "O", "O", "X", "O", "X", "O", "X")),
                new ArrayList<>(List.of("-", "-", "X", "X", "O", "O", "O", "O", "X", "O", "X", "O", "X", "O", "X")),
                new ArrayList<>(List.of("X", "X", "O", "O", "O", "O", "X", "O", "X", "O", "X", "O", "X", "O", "X")),
                new ArrayList<>(List.of("X", "O", "O", "O", "X", "O", "X", "O", "X", "O", "X", "O", "X", "O", "X")),
                new ArrayList<>(List.of("O", "O", "X", "O", "X", "O", "X", "O", "X", "O", "X", "O", "X", "O", "X")),
                new ArrayList<>(List.of("O", "O", "O", "O", "X", "O", "O", "O", "O", "O", "X", "O", "O", "O", "O"))
        ));
        int columnIdx = 14;
        int maxDist = 2;
        List<Integer> colouredRange = List.of(1, 1);

        // when
        int resultOffset = ColouringHelper.findDistanceFromBottomX(board, columnIdx, colouredRange, maxDist);

        // then
        assertThat(resultOffset).isEqualTo(1);
    }

    @DisplayName("findDistanceFromBottomX - should return 0 if rowIdxToCheck equal to board size")
    @Test // o07836
    void shouldReturnZeroWhenRowIdxEqualToBoardSize() {
        // given
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "-", "-", "O", "O", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "O", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "O", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "-", "-", "O", "O", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "-", "-", "O", "O", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "-", "-", "-", "-", "-"))
        ));

        int columnIdx = 2;
        int maxDist = 2;
        List<Integer> colouredRange = List.of(9, 9);

        // when
        int resultOffset = ColouringHelper.findDistanceFromBottomX(board, columnIdx, colouredRange, maxDist);

        // then
        assertThat(resultOffset).isEqualTo(0);
    }

    // extendToTop
    // extended: false(), true(x)
    @DisplayName("extendToTop should colour (0,14) and return true when start=0 and minExtensionIdx=0")
    @Test // o07986
    void shouldExtendSingleCellAtTopAndReturnTrue() {
        List<String> row0 = new ArrayList<>(List.of(
                "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"
        ));
        List<List<String>> board = new ArrayList<>(List.of(row0));

        when(nonogramColumnLogic.getNonogramSolutionBoard()).thenReturn(board);
        when(nonogramColumnLogic.getNonogramState()).thenReturn(state);

        int columnIdx = 14;
        int fromInclusive = 0;
        int toInclusive = 0;

        // when
        boolean extended = ColouringHelper.extendToTop(
                nonogramColumnLogic,
                colouringHelper,
                scheduler,
                columnIdx,
                fromInclusive,
                toInclusive
        );

        // then
        assertTrue(extended, "Should return true (at least one field was coloured)");

        // Verify that exactly (0,14) was coloured and actions were scheduled
        verify(colouringHelper, times(1))
                .colourFieldAtGivenPosition(argThat(f -> f.getRowIdx() == 0 && f.getColumnIdx() == 14), eq("--C-"));
        verify(scheduler, times(1))
                .scheduleActionsBasedOnField(argThat(f -> f.getRowIdx() == 0 && f.getColumnIdx() == 14),
                        eq(NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN));


        // Step counter should increase; no invalidation should occur
        verify(state, times(1)).increaseMadeSteps();
        verify(state, never()).invalidateSolution();

        // Ensure no unexpected interactions
        verifyNoMoreInteractions(colouringHelper, scheduler, state);
    }

    // extendToBottom
    // extended: false(), true(x)
    @DisplayName("extendToBottom should colour rows 8..9 in column 7 and return true for range [6,7] -> max 9")
    @Test // o07836
    void shouldExtendLastSequenceDownwardsAndReturnTrue() {
        // given: 10x10 board (row-major), column 7 has O,O at rows 6..7 and '-' at rows 8..9
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("X", "X", "X", "X", "-", "X", "X", "O", "-", "-")), // row 0
                new ArrayList<>(List.of("X", "X", "X", "O", "O", "-", "-", "O", "-", "-")), // row 1
                new ArrayList<>(List.of("X", "X", "X", "O", "O", "O", "O", "O", "X", "X")), // row 2
                new ArrayList<>(List.of("X", "X", "X", "O", "O", "O", "O", "X", "X", "O")), // row 3
                new ArrayList<>(List.of("X", "-", "O", "O", "O", "-", "-", "-", "X", "O")), // row 4
                new ArrayList<>(List.of("X", "O", "O", "O", "O", "X", "X", "X", "X", "O")), // row 5
                new ArrayList<>(List.of("X", "-", "O", "O", "X", "X", "O", "O", "X", "X")), // row 6
                new ArrayList<>(List.of("X", "X", "X", "O", "X", "X", "O", "O", "X", "X")), // row 7
                new ArrayList<>(List.of("O", "X", "O", "O", "X", "X", "O", "-", "-", "X")), // row 8
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "X", "O", "-", "-", "X"))  // row 9
        ));

        when(nonogramColumnLogic.getNonogramSolutionBoard()).thenReturn(board);
        when(nonogramColumnLogic.getNonogramState()).thenReturn(state);
        when(nonogramColumnLogic.getNonogramRules()).thenReturn(rules);
        when(rules.getHeight()).thenReturn(board.size());

        int columnIdx = 7;
        List<Integer> colouredRange = List.of(6, 7);
        int maxExtensionIdx = 9;

        int fromInclusive = colouredRange.get(1) + 1; // start just after the coloured range (row 8)
        int toInclusive = maxExtensionIdx;             // extend down to row 9

        // when
        boolean extended = ColouringHelper.extendToBottom(
                nonogramColumnLogic,
                colouringHelper,
                scheduler,
                columnIdx,
                fromInclusive,
                toInclusive
        );

        // then
        assertTrue(extended, "Should return true (at least one field was coloured)");

        // both (8,7) and (9,7) should be coloured and scheduled
        verify(colouringHelper, times(1))
                .colourFieldAtGivenPosition(argThat(f -> f.getRowIdx() == 8 && f.getColumnIdx() == 7), eq("--C-"));
        verify(colouringHelper, times(1))
                .colourFieldAtGivenPosition(argThat(f -> f.getRowIdx() == 9 && f.getColumnIdx() == 7), eq("--C-"));

        verify(scheduler, times(1))
                .scheduleActionsBasedOnField(argThat(f -> f.getRowIdx() == 8 && f.getColumnIdx() == 7),
                        eq(NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN));
        verify(scheduler, times(1))
                .scheduleActionsBasedOnField(argThat(f -> f.getRowIdx() == 9 && f.getColumnIdx() == 7),
                        eq(NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN));

        // two steps were made, no invalidation
        verify(state, times(2)).increaseMadeSteps();
        verify(state, never()).invalidateSolution();

        verifyNoMoreInteractions(colouringHelper, scheduler, state);
    }

    // extendToRight
    // extended: false(), true(x)
    @DisplayName("extendToRight should colour (1,4) and return true for range [3,3] -> max 4")
    @Test // o07836
    void shouldExtendRightOneCellAndReturnTrue() {
        // given: 10x10 board; row 1 has 'O' at col 3 and '-' at col 4
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "X", "X", "-", "-", "-", "-", "-", "-")), // 0
                new ArrayList<>(List.of("-", "-", "X", "O", "-", "-", "-", "-", "-", "-")), // 1  <-- target row
                new ArrayList<>(List.of("X", "X", "X", "O", "O", "O", "O", "O", "X", "X")), // 2
                new ArrayList<>(List.of("X", "X", "X", "O", "O", "O", "O", "X", "-", "-")), // 3
                new ArrayList<>(List.of("X", "-", "O", "O", "O", "-", "-", "-", "-", "-")), // 4
                new ArrayList<>(List.of("-", "O", "O", "O", "-", "X", "-", "-", "-", "-")), // 5
                new ArrayList<>(List.of("X", "-", "O", "O", "-", "X", "O", "O", "X", "X")), // 6
                new ArrayList<>(List.of("X", "X", "X", "O", "X", "X", "O", "O", "X", "X")), // 7
                new ArrayList<>(List.of("-", "X", "O", "O", "X", "-", "-", "-", "-", "-")), // 8
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "-", "-", "-", "-", "-"))  // 9
        ));

        when(nonogramRowLogic.getNonogramSolutionBoard()).thenReturn(board);
        when(nonogramRowLogic.getNonogramState()).thenReturn(state);
        when(nonogramRowLogic.getNonogramRules()).thenReturn(rules);
        when(rules.getWidth()).thenReturn(board.get(0).size()); // width = 10

        int rowIdx = 1;
        List<Integer> colouredRange = List.of(3, 3);
        int maxExtensionIdx = 4;

        int fromInclusive = colouredRange.get(1) + 1; // start just after current segment -> 4
        int toInclusive = maxExtensionIdx;             // stop at 4 (inclusive)

        // when
        boolean extended = ColouringHelper.extendToRight(
                nonogramRowLogic,
                colouringHelper,
                scheduler,
                rowIdx,
                fromInclusive,
                toInclusive
        );

        // then: should colour exactly (1,4)
        assertTrue(extended, "Should return true (the cell at (1,4) was coloured)");

        verify(colouringHelper, times(1))
                .colourFieldAtGivenPosition(argThat(f -> f.getRowIdx() == 1 && f.getColumnIdx() == 4), eq("R---"));
        verify(scheduler, times(1))
                .scheduleActionsBasedOnField(argThat(f -> f.getRowIdx() == 1 && f.getColumnIdx() == 4),
                        eq(NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW));

        verify(state, times(1)).increaseMadeSteps();
        verify(state, never()).invalidateSolution();

        verifyNoMoreInteractions(colouringHelper, scheduler, state, rules);
    }

    // extendToLeft
    // extended: false(), true(x)
    @DisplayName("extendToLeft should colour (2,5) and (2,4) and return true for range [6,7] with minExtensionIdx=3")
    @Test // o07836
    void shouldExtendLeftOverEmptyCellsAndReturnTrue() {
        // given: updated 10x10 board; row 2 has 'O' at 3, '-' at 4 and 5, 'O' at 6..7, 'X' at 2 and 8
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "-", "X", "-", "-", "-", "-", "-", "-")), // 0
                new ArrayList<>(List.of("-", "-", "-", "O", "-", "-", "-", "-", "-", "-")), // 1
                new ArrayList<>(List.of("-", "-", "X", "O", "-", "-", "O", "O", "X", "-")), // 2  <-- target row
                new ArrayList<>(List.of("-", "-", "-", "O", "O", "-", "-", "-", "-", "-")), // 3
                new ArrayList<>(List.of("-", "-", "-", "O", "O", "-", "-", "-", "-", "-")), // 4
                new ArrayList<>(List.of("-", "-", "-", "O", "-", "-", "-", "-", "-", "-")), // 5
                new ArrayList<>(List.of("-", "-", "O", "O", "-", "X", "O", "O", "X", "-")), // 6
                new ArrayList<>(List.of("-", "-", "X", "O", "X", "X", "O", "O", "X", "-")), // 7
                new ArrayList<>(List.of("-", "-", "-", "O", "X", "-", "-", "-", "-", "-")), // 8
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "-", "-", "-", "-", "-"))  // 9
        ));

        when(nonogramRowLogic.getNonogramSolutionBoard()).thenReturn(board);
        when(nonogramRowLogic.getNonogramState()).thenReturn(state);

        int rowIdx = 2;
        List<Integer> colouredRange = List.of(6, 7); // current coloured segment [6..7]
        int minExtensionIdx = 3;

        int fromInclusive = colouredRange.get(0) - 1; // 5
        int toInclusive = minExtensionIdx;            // 3

        // when
        boolean extended = ColouringHelper.extendToLeft(
                nonogramRowLogic,
                colouringHelper,
                scheduler,
                rowIdx,
                fromInclusive,
                toInclusive
        );

        // then: should colour (2,5) and (2,4); stop at (2,3) because it's 'O'
        assertTrue(extended, "Should return true (at least one empty field was coloured)");

        verify(colouringHelper, times(1))
                .colourFieldAtGivenPosition(argThat(f -> f.getRowIdx() == 2 && f.getColumnIdx() == 5), eq("R---"));
        verify(colouringHelper, times(1))
                .colourFieldAtGivenPosition(argThat(f -> f.getRowIdx() == 2 && f.getColumnIdx() == 4), eq("R---"));

        verify(scheduler, times(1))
                .scheduleActionsBasedOnField(argThat(f -> f.getRowIdx() == 2 && f.getColumnIdx() == 5),
                        eq(NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW));
        verify(scheduler, times(1))
                .scheduleActionsBasedOnField(argThat(f -> f.getRowIdx() == 2 && f.getColumnIdx() == 4),
                        eq(NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW));

        // two steps made, no invalidation
        verify(state, times(2)).increaseMadeSteps();
        verify(state, never()).invalidateSolution();

        verifyNoMoreInteractions(colouringHelper, scheduler, state);
    }

    @DisplayName("findDistanceFromLeftX - should return 0 when no 'X' found within maxDist before coloured range")
    @Test // o06005 row 0 (with columnIdxToCheck < 0 -> false step)
    void shouldReturnZeroWhenNoXFoundBeforeColouredSequenceWithinMaxDist() {
        // given
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "O", "O", "-", "-", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "-", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O")),
                new ArrayList<>(List.of("-", "-", "X", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"))
        ));
        int rowIdx = 9;
        int maxDist = 2;
        List<Integer> colouredRange = List.of(2, 2);

        // when
        int result = ColouringHelper.findDistanceFromLeftX(board, rowIdx, colouredRange, maxDist);

        // then
        assertEquals(0, result);
    }

    @DisplayName("findDistanceFromLeftX - should return offset when 'X' is before at a distance no greater than the maximum")
    @Test  // o06005 row 1
    void shouldReturnTheOffsetWhenXIsBeforeAt_a_DistanceNoGreaterThanTheMaximum() {
        // given
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "X", "X", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "X", "O", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("X", "X", "X", "O", "O", "O", "O", "O", "X", "X")),
                new ArrayList<>(List.of("X", "X", "X", "O", "O", "O", "O", "X", "-", "-")),
                new ArrayList<>(List.of("X", "-", "O", "O", "O", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "O", "O", "O", "-", "X", "-", "-", "-", "-")),
                new ArrayList<>(List.of("X", "-", "O", "O", "-", "X", "O", "O", "X", "X")),
                new ArrayList<>(List.of("X", "X", "X", "O", "X", "X", "O", "O", "X", "X")),
                new ArrayList<>(List.of("-", "X", "O", "O", "X", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "-", "-", "-", "-", "-"))
        ));
        int rowIdx = 1;
        int maxDist = 2;
        List<Integer> colouredRange = List.of(3, 3);

        // when
        int resultOffset = ColouringHelper.findDistanceFromLeftX(board, rowIdx, colouredRange, maxDist);

        // then
        assertThat(resultOffset).isEqualTo(1);
    }

    @DisplayName("findDistanceFromLeftX - should return 0 if columnIdxToCheck is less than zero")
    @Test // o07836
    void shouldReturnZeroWhenColumnIdxIsLessThanZero() {
        // given
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "-", "-", "O", "O", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "O", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "O", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "-", "-", "O", "O", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "-", "-", "O", "O", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "-", "-", "-", "-", "-"))
        ));

        int rowIdx = 1;
        int maxDist = 2;
        List<Integer> colouredRange = List.of(0, 0);

        // when
        int resultOffset = ColouringHelper.findDistanceFromLeftX(board, rowIdx, colouredRange, maxDist);

        // then
        assertThat(resultOffset).isEqualTo(0);
    }

    @DisplayName("findDistanceFromRightX - should return 0 when no 'X' found within maxDist before coloured range")
    @Test // o06005 row 0 (with columnIdxToCheck == board.get(0).size() -> false step)
    void shouldReturnZeroWhenNoXFoundAfterColouredSequenceWithinMaxDist() {
        // given
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "O", "O", "-", "-", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "-", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O")),
                new ArrayList<>(List.of("-", "-", "X", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"))
        ));
        int rowIdx = 9;
        int maxDist = 2;
        List<Integer> colouredRange = List.of(6, 6);

        // when
        int result = ColouringHelper.findDistanceFromRightX(board, rowIdx, colouredRange, maxDist);

        // then
        assertEquals(0, result);
    }

    @DisplayName("findDistanceFromRightX - should return offset when 'X' is after at a distance no greater than the maximum")
    @Test // o07386
    void shouldReturnTheOffsetWhenXIsAfterAt_a_DistanceNoGreaterThanTheMaximum() {
        // given
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "-", "X", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "X", "O", "-", "-", "O", "O", "X", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "O", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "O", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "-", "X", "O", "O", "X", "-")),
                new ArrayList<>(List.of("-", "-", "X", "O", "X", "X", "O", "O", "X", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "X", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "-", "-", "-", "-", "-"))
        ));

        int rowIdx = 2;
        int maxDist = 5;
        List<Integer> colouredRange = List.of(6, 7);

        // when
        int resultOffset = ColouringHelper.findDistanceFromRightX(board, rowIdx, colouredRange, maxDist);

        // then
        assertThat(resultOffset).isEqualTo(2);
    }

    @DisplayName("findDistanceFromRightX  - should return 0 if columnIdxToCheck equal to width")
    @Test // o07836
    void shouldReturnZeroWhenColumnIdxIsEqualToWidth() {
        // given
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "-", "X", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "-", "-", "O", "O", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "O", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "O", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "-", "-", "O", "O", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "-", "-", "O", "O", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "X", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "-", "-", "-", "-", "-"))
        ));

        int rowIdx = 2;
        int maxDist = 5;
        List<Integer> colouredRange = List.of(6, 7);

        // when
        int resultOffset = ColouringHelper.findDistanceFromRightX(board, rowIdx, colouredRange, maxDist);

        // then
        assertThat(resultOffset).isEqualTo(0);
    }

    // findColouredSequenceRangeLeft
    @DisplayName("findColouredSequenceRangeLeft should return [6, 6] when starting at coloured cell with possible X before")
    @Test // o06005
    void shouldReturnSingleFieldRangeWhenXCanBeBeforeColouredField() {
        // given
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "O", "O", "-", "-", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "-", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O")),
                new ArrayList<>(List.of("-", "-", "X", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"))
        ));
        int rowIdx = 0;
        int startColumnIdx = 6;

        // when
        List<Integer> result = ColouringHelper.findColouredSequenceRangeLeft(board, rowIdx, startColumnIdx);

        // then
        assertEquals(List.of(6, 6), result);
    }

    @DisplayName("findColouredSequenceRangeLeft should return [2, 6] for horizontal coloured segment extended backwards")
    @Test // o06005
    void shouldReturnHorizontalColouredRangeBeforeWhenOsPresent() {
        // given
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "O", "O", "-", "-", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "-", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O")),
                new ArrayList<>(List.of("-", "-", "X", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"))
        ));
        int rowIdx = 2;
        int startColumnIdx = 6;

        // when
        List<Integer> result = ColouringHelper.findColouredSequenceRangeLeft(board, rowIdx, startColumnIdx);

        // then
        assertEquals(List.of(2, 6), result);
    }

    @DisplayName("findColouredSequenceRangeLeft should return [0, 0] when starColumnIdx is 0 (while skipped)")
    @Test // o06005
    void shouldReturnSingleFieldWhenStartColumnIsZero() {
        // given
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("X", "X", "O", "X", "-", "X", "O", "X", "X", "X")),
                new ArrayList<>(List.of("O", "X", "O", "X", "O", "X", "O", "X", "-", "X")),
                new ArrayList<>(List.of("O", "X", "O", "O", "O", "O", "O", "X", "-", "X")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "X", "O", "X")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O")),
                new ArrayList<>(List.of("-", "-", "X", "X", "-", "X", "O", "X", "-", "X")),
                new ArrayList<>(List.of("X", "O", "O", "X", "X", "X", "O", "O", "X", "X"))
        ));
        int rowIdx = 1;
        int startColumnIdx = 0;

        // when
        List<Integer> result = ColouringHelper.findColouredSequenceRangeLeft(board, rowIdx, startColumnIdx);

        // then
        assertEquals(List.of(0, 0), result);
    }

    // findColouredSequenceRangeRight

    @DisplayName("findColouredSequenceRangeRight should return [2, 2] when starting at coloured cell with possible X before")
    @Test // o06005
    void shouldReturnSingleFieldRangeWhenXCanBeBeforeColouredFieldRight() {
        // given
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "O", "O", "-", "-", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "-", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O")),
                new ArrayList<>(List.of("-", "-", "X", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"))
        ));
        int rowIdx = 0;
        int startColumnIdx = 2;

        // when
        List<Integer> result = ColouringHelper.findColouredSequenceRangeRight(board, rowIdx, startColumnIdx);

        // then
        assertEquals(List.of(2, 2), result);
    }

    @DisplayName("findColouredSequenceRangeRight should return [2, 6] for horizontal coloured segment extended forwards")
    @Test // o06005
    void shouldReturnHorizontalColouredRangeAfterWhenOsPresent() {
        // given
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "O", "O", "-", "-", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "-", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O")),
                new ArrayList<>(List.of("-", "-", "X", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"))
        ));
        int rowIdx = 2;
        int startColumnIdx = 2;

        // when
        List<Integer> result = ColouringHelper.findColouredSequenceRangeRight(board, rowIdx, startColumnIdx);

        // then
        assertEquals(List.of(2, 6), result);
    }

    @DisplayName("findColouredSequenceRangeRight should return [9, 9] when startColumnIdx is the last column in row (while skipped)")
    @Test // o06005
    void shouldReturnSingleFieldWhenStartColumnIsLast() {
        // given
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "-", "X", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("O", "-", "-", "X", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("O", "O", "-", "X", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("O", "-", "-", "X", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "X", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("X", "X", "X", "X", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("O", "O", "O", "X", "X", "X", "X", "X", "X", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "X", "O")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "X", "O"))
        ));
        int rowIdx = 8;
        int startColumnIdx = 9;

        // when
        List<Integer> result = ColouringHelper.findColouredSequenceRangeRight(board, rowIdx, startColumnIdx);

        // then
        assertEquals(List.of(9, 9), result);
    }
}