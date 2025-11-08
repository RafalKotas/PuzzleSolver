package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction.*;
import static org.junit.jupiter.api.Assertions.*;

class NonogramSolveActionTest {
    @Test
    @DisplayName("getRowSolveActions should return list with row-related actions")
    void shouldReturnRowSolveActions() {
        // given & when
        List<NonogramSolveAction> actions = NonogramSolveAction.getRowSolveActions();

        // then
        assertTrue(actions.contains(CORRECT_SEQUENCES_RANGES_IN_ROW));
        assertTrue(actions.contains(MARK_AVAILABLE_FIELDS_IN_ROW));
        assertEquals(17, actions.size());
    }

    @Test
    @DisplayName("getCorrectRowRangesSolveActions should return only correcting actions for rows")
    void shouldReturnCorrectRowRangesActions() {
        // given & when
        List<NonogramSolveAction> actions = NonogramSolveAction.getCorrectRowRangesSolveActions();

        // then
        assertTrue(actions.contains(CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_ROW));
        assertFalse(actions.contains(PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_ROW));
        assertEquals(5, actions.size());
    }

    @Test
    @DisplayName("isMarkRowAction should return true only for MARK_AVAILABLE_FIELDS_IN_ROW")
    void shouldIdentifyMarkRowAction() {
        // given & when
        boolean isMark = NonogramSolveAction.isMarkRowAction(MARK_AVAILABLE_FIELDS_IN_ROW);
        boolean isNotMark = NonogramSolveAction.isMarkRowAction(COLOUR_OVERLAPPING_FIELDS_IN_ROW);

        // then
        assertTrue(isMark);
        assertFalse(isNotMark);
    }

    @Test
    @DisplayName("getColourRowSolveActions should return only colouring actions in row")
    void shouldReturnColourRowSolveActions() {
        // given & when
        List<NonogramSolveAction> actions = NonogramSolveAction.getColourRowSolveActions();

        // then
        assertTrue(actions.contains(COLOUR_OVERLAPPING_FIELDS_IN_ROW));
        assertTrue(actions.contains(EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW));
        assertEquals(2, actions.size());
    }

    @Test
    @DisplayName("getCorrectColumnRangesSolveActions should return only correcting actions for columns")
    void shouldReturnCorrectColumnRangesActions() {
        // given & when
        List<NonogramSolveAction> actions = NonogramSolveAction.getCorrectColumnRangesSolveActions();

        // then
        assertTrue(actions.contains(CORRECT_SEQUENCES_RANGES_IN_COLUMN));
        assertFalse(actions.contains(PLACE_XS_AT_UNREACHABLE_FIELDS_IN_COLUMN));
        assertEquals(4, actions.size());
    }

    @Test
    @DisplayName("isMarkColumnAction should return true only for MARK_AVAILABLE_FIELDS_IN_COLUMN")
    void shouldIdentifyMarkColumnAction() {
        // given & when
        boolean isMark = NonogramSolveAction.isMarkColumnAction(MARK_AVAILABLE_FIELDS_IN_COLUMN);
        boolean isNotMark = NonogramSolveAction.isMarkColumnAction(COLOUR_OVERLAPPING_FIELDS_IN_COLUMN);

        // then
        assertTrue(isMark);
        assertFalse(isNotMark);
    }

    @Test
    @DisplayName("getColourColumnSolveActions should return only colouring actions in column")
    void shouldReturnColourColumnSolveActions() {
        // given & when
        List<NonogramSolveAction> actions = NonogramSolveAction.getColourColumnSolveActions();

        // then
        assertTrue(actions.contains(COLOUR_OVERLAPPING_FIELDS_IN_COLUMN));
        assertTrue(actions.contains(EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN));
        assertEquals(2, actions.size());
    }

    @Test
    @DisplayName("isRowAction should return true if action name contains 'ROW'")
    void shouldIdentifyRowActions() {
        // given & when
        boolean isRow = CORRECT_SEQUENCES_RANGES_IN_ROW.isRowAction();
        boolean isNotRow = CORRECT_SEQUENCES_RANGES_IN_COLUMN.isRowAction();

        // then
        assertTrue(isRow);
        assertFalse(isNotRow);
    }

    @Test
    @DisplayName("isColumnAction should return true if action name contains 'COLUMN'")
    void shouldIdentifyColumnActions() {
        // given & when
        boolean isColumn = CORRECT_SEQUENCES_RANGES_IN_COLUMN.isColumnAction();
        boolean isNotColumn = MARK_AVAILABLE_FIELDS_IN_ROW.isColumnAction();

        // then
        assertTrue(isColumn);
        assertFalse(isNotColumn);
    }
}