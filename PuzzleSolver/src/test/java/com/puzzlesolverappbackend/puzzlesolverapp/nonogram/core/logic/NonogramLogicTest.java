package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramActionDetails;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solutions.NonogramSolutionDecision;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.NonogramActionScheduler;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.NonogramBoardAccessHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.config.GuessMode;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.clearing.NonogramFieldClearingHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.NonogramColumnLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row.NonogramRowLogic;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramConstants.EMPTY_FIELD;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramConstants.X_FIELD;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction.COLOUR_FIELD_GUESS_OR_RECURSIVE;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction.PLACE_X_FIELD_GUESS_OR_RECURSIVE;
import static org.junit.jupiter.api.Assertions.*;

class NonogramLogicTest {

    @Test
    void shouldSetAndGetAllFields() {
        NonogramLogic logic = new NonogramLogic();

        List<List<String>> board = List.of(List.of("O", "X"));
        List<List<List<Integer>>> ranges3D = List.of(List.of(List.of(1, 2)));
        List<List<Integer>> ranges2D = List.of(List.of(0, 1));

        logic.setCorrectSolutionBoard(board);
        logic.setCorrectRowRanges(ranges3D);
        logic.setCorrectColumnRanges(ranges3D);
        logic.setGuessMode(GuessMode.ENABLED);
        logic.setRowsFieldsNotToInclude(ranges2D);
        logic.setColumnsFieldsNotToInclude(ranges2D);
        logic.setRowsSequencesIdsNotToInclude(ranges2D);
        logic.setColumnsSequencesIdsNotToInclude(ranges2D);
        logic.setRowsSequencesRanges(ranges3D);
        logic.setColumnsSequencesRanges(ranges3D);

        assertEquals(board, logic.getCorrectSolutionBoard());
        assertEquals(ranges3D, logic.getCorrectRowRanges());
        assertEquals(ranges3D, logic.getCorrectColumnRanges());
        assertEquals(GuessMode.ENABLED, logic.getGuessMode());
        assertEquals(ranges2D, logic.getRowsFieldsNotToInclude());
        assertEquals(ranges2D, logic.getColumnsFieldsNotToInclude());
        assertEquals(ranges2D, logic.getRowsSequencesIdsNotToInclude());
        assertEquals(ranges2D, logic.getColumnsSequencesIdsNotToInclude());
        assertEquals(ranges3D, logic.getRowsSequencesRanges());
        assertEquals(ranges3D, logic.getColumnsSequencesRanges());
    }

    @Test
    void testDeepCopyCreatesIndependentClone() {
        // given
        List<List<Integer>> rowSequences = List.of(List.of(3));
        List<List<Integer>> columnSequences = List.of(List.of(3));
        NonogramRules rules = new NonogramRules(rowSequences, columnSequences, 1, 1);

        NonogramLogic original = new NonogramLogic(rules, GuessMode.DISABLED);
        original.getNonogramSolutionBoard().get(0).set(0, "O");
        original.setRowsFieldsNotToInclude(List.of(new ArrayList<>(List.of(0))));
        original.setRowsSequencesRanges(List.of(new ArrayList<>(List.of(List.of(0, 0)))));
        original.getLogs().add("Some log");

        // when
        NonogramLogic copy = original.deepCopy();

        // then
        assertEquals(original.getNonogramSolutionBoard(), copy.getNonogramSolutionBoard());
        assertEquals(original.getRowsFieldsNotToInclude(), copy.getRowsFieldsNotToInclude());
        assertEquals(original.getRowsSequencesRanges(), copy.getRowsSequencesRanges());
        assertEquals(original.getLogs(), copy.getLogs());
        assertEquals(original.getNonogramState().isInvalidSolution(), copy.getNonogramState().isInvalidSolution());

        copy.getNonogramSolutionBoard().get(0).set(0, "X");
        copy.getRowsFieldsNotToInclude().get(0).add(1);
        copy.getLogs().add("Another log");

        assertNotEquals(original.getNonogramSolutionBoard(), copy.getNonogramSolutionBoard());
        assertNotEquals(original.getRowsFieldsNotToInclude(), copy.getRowsFieldsNotToInclude());
        assertNotEquals(original.getLogs(), copy.getLogs());
    }

    @Test
    void initializeHelpers_setsAllRequiredHelpers() {
        // given
        List<List<Integer>> rowSequences = List.of(List.of(1));
        List<List<Integer>> columnSequences = List.of(List.of(1));
        NonogramRules rules = new NonogramRules(rowSequences, columnSequences, 1, 1);

        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);
        logic.setNonogramSolutionBoard(List.of(new ArrayList<>(List.of(""))));
        logic.setNonogramSolutionBoardWithMarks(List.of(new ArrayList<>(List.of(""))));
        logic.setActionsToDoList(new ArrayList<>());

        // when
        logic.initializeHelpers();

        // then
        assertNotNull(logic.getBoardAccessHelper());
        assertNotNull(logic.getFieldClearingHelper());
        assertNotNull(logic.getNonogramRowLogic());
        assertNotNull(logic.getNonogramColumnLogic());
        assertNotNull(logic.getActionScheduler());

        assertInstanceOf(NonogramBoardAccessHelper.class, logic.getBoardAccessHelper());
        assertInstanceOf(NonogramFieldClearingHelper.class, logic.getFieldClearingHelper());
        assertInstanceOf(NonogramRowLogic.class, logic.getNonogramRowLogic());
        assertInstanceOf(NonogramColumnLogic.class, logic.getNonogramColumnLogic());
        assertInstanceOf(NonogramActionScheduler.class, logic.getActionScheduler());
    }


    @Test
    void fillTrivialRowsAndColumns() {
        // given
        NonogramRules rules = new NonogramRules();
        rules.setHeight(3);
        rules.setWidth(3);
        rules.setRowSequencesLengths(List.of(List.of(1, 1), List.of(1), List.of(1, 1)));
        rules.setColumnSequencesLengths(List.of(List.of(1, 1), List.of(1), List.of(1, 1)));

        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);
        logic.initializeHelpers();

        // when
        logic.fillTrivialRowsAndColumns();

        // then
        List<List<String>> board = logic.getNonogramSolutionBoard();
        for (List<String> row : board) {
            for (String field : row) {
                assertNotNull(field);
            }
        }
    }

    @Test
    void fillEmptyRowsAndColumns() {
        // given
        NonogramRules rules = new NonogramRules();
        rules.setHeight(3);
        rules.setWidth(3);
        rules.setRowSequencesLengths(List.of(List.of(0), List.of(1), List.of(0)));
        rules.setColumnSequencesLengths(List.of(List.of(0), List.of(1), List.of(0)));

        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);
        logic.initializeHelpers();

        // when
        logic.fillTrivialRowsAndColumns();

        // then
        List<List<String>> board = logic.getNonogramSolutionBoard();
        for (List<String> row : board) {
            for (String field : row) {
                assertNotNull(field);
            }
        }
    }

    // TODO - add some real case
    @Test
    void updateCurrentAvailableChoices_populatesChoicesFoNotExcludedFieldsOnly() {
        // given
        NonogramRules rules = new NonogramRules();
        rules.setHeight(2);
        rules.setWidth(2);
        rules.setRowSequencesLengths(List.of(List.of(2), List.of(0)));
        rules.setColumnSequencesLengths(List.of(List.of(1), List.of(1)));

        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        logic.setNonogramSolutionBoard(List.of(
                List.of(EMPTY_FIELD, EMPTY_FIELD),
                List.of(X_FIELD, X_FIELD)
        ));

        logic.setRowsFieldsNotToInclude(List.of(
                List.of(),
                List.of(0, 1)
        ));

        // when
        logic.updateCurrentAvailableChoices();

        // then
        List<NonogramSolutionDecision> choices = logic.getAvailableChoices();

        assertEquals(2, choices.size());
        assertTrue(choices.contains(new NonogramSolutionDecision(X_FIELD, new Field(0, 0))));
        assertTrue(choices.contains(new NonogramSolutionDecision(X_FIELD, new Field(0, 1))));
    }

    @Test
    void addAffectedRowAndColumnAfterColouringField_addsExpectedActionsToList() {
        // given
        NonogramRules rules = new NonogramRules(
                List.of(List.of(1), List.of(1)),
                List.of(List.of(1), List.of(1)),
                2,
                2
        );

        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);
        logic.setActionsToDoList(new ArrayList<>());

        Field field = new Field(0, 1);
        NonogramSolutionDecision decision = new NonogramSolutionDecision(X_FIELD, field);

        // when
        logic.addAffectedRowAndColumnAfterColouringField(decision);

        // then
        List<NonogramActionDetails> actions = logic.getActionsToDoList();
        assertEquals(8, actions.size());

        assertTrue(actions.contains(new NonogramActionDetails(0,
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                COLOUR_FIELD_GUESS_OR_RECURSIVE, false)));

        assertTrue(actions.contains(new NonogramActionDetails(1,
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                COLOUR_FIELD_GUESS_OR_RECURSIVE, false)));

        assertTrue(actions.contains(new NonogramActionDetails(0,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                COLOUR_FIELD_GUESS_OR_RECURSIVE, false)));

        assertTrue(actions.contains(new NonogramActionDetails(1,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                COLOUR_FIELD_GUESS_OR_RECURSIVE, false)));

        assertTrue(actions.contains(new NonogramActionDetails(1,
                NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES,
                COLOUR_FIELD_GUESS_OR_RECURSIVE, false)));

        assertTrue(actions.contains(new NonogramActionDetails(1,
                NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES,
                COLOUR_FIELD_GUESS_OR_RECURSIVE, false)));

        assertTrue(actions.contains(new NonogramActionDetails(0,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW,
                COLOUR_FIELD_GUESS_OR_RECURSIVE, false)));

        assertTrue(actions.contains(new NonogramActionDetails(1,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN,
                COLOUR_FIELD_GUESS_OR_RECURSIVE, false)));
    }

    @Test
    void addAffectedRowAndColumnAfterPlacingXAtField_addsExpectedActionsToList() {
        // given
        NonogramRules rules = new NonogramRules(
                List.of(List.of(1), List.of(1)),
                List.of(List.of(1), List.of(1)),
                2,
                2
        );

        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);
        logic.setActionsToDoList(new ArrayList<>());

        Field field = new Field(0, 1);
        NonogramSolutionDecision decision = new NonogramSolutionDecision(X_FIELD, field);

        // when
        logic.addAffectedRowAndColumnAfterPlacingXAtField(decision);

        // then
        List<NonogramActionDetails> actions = logic.getActionsToDoList();
        assertEquals(8, actions.size());

        assertTrue(actions.contains(new NonogramActionDetails(0,
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES,
                PLACE_X_FIELD_GUESS_OR_RECURSIVE, false)));

        assertTrue(actions.contains(new NonogramActionDetails(1,
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES,
                PLACE_X_FIELD_GUESS_OR_RECURSIVE, false)));

        assertTrue(actions.contains(new NonogramActionDetails(0,
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY,
                PLACE_X_FIELD_GUESS_OR_RECURSIVE, false)));

        assertTrue(actions.contains(new NonogramActionDetails(1,
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY,
                PLACE_X_FIELD_GUESS_OR_RECURSIVE, false)));

        assertTrue(actions.contains(new NonogramActionDetails(0,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                PLACE_X_FIELD_GUESS_OR_RECURSIVE, false)));

        assertTrue(actions.contains(new NonogramActionDetails(1,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                PLACE_X_FIELD_GUESS_OR_RECURSIVE, false)));

        assertTrue(actions.contains(new NonogramActionDetails(0,
                NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES,
                PLACE_X_FIELD_GUESS_OR_RECURSIVE, false)));

        assertTrue(actions.contains(new NonogramActionDetails(1,
                NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES,
                PLACE_X_FIELD_GUESS_OR_RECURSIVE, false)));
    }
}