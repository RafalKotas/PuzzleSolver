package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramActionDetails;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.config.GuessMode;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.isFieldWithX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ColumnXPlacementHelperImplTest {

    @Test
    @DisplayName("should correctly initialize logic and nonogramFieldPlacingXHelper in constructor")
    void shouldInitializeLogicAndHelperFields() {
        // given
        NonogramRules rules = new NonogramRules(new ArrayList<>(), new ArrayList<>(), 0, 0);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        // when
        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());

        // then
        assertNotNull(helper.getLogic(), "Logic should be initialized");
        assertNotNull(helper.getNonogramFieldPlacingXHelper(), "NonogramFieldPlacingXHelper should be initialized");
    }

    @Test
    @DisplayName("o10155 - should place X in unreachable fields (column 4)")
    void shouldPlaceXInUnreachableFieldsInColumn() {
        // given
        int height = 20;
        int width = 15;
        int targetColumnIdx = 4;

        List<List<Integer>> rowSequences = new ArrayList<>();
        List<List<Integer>> colSequences = new ArrayList<>();
        for (int i = 0; i < height; i++) rowSequences.add(Collections.singletonList(0));
        for (int i = 0; i < width; i++) colSequences.add(Collections.singletonList(0));

        NonogramRules rules = new NonogramRules(rowSequences, colSequences, height, width);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        List<String> col4 = List.of(
                "-", "-", "X", "O", "O", "O", "O", "X",
                "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-"
        );
        List<String> col4Marks = List.of(
                "----", "----", "XXXX", "--Ca", "--Ca", "--Ca", "RaCa", "XXXX",
                "RaCb", "RaCb", "RaCb", "RaCb", "RaCb", "RaCb", "RbCb", "RaCb", "RaCb", "--Cb", "----", "----"
        );

        for (int row = 0; row < height; row++) {
            logic.getNonogramSolutionBoard().get(row).set(targetColumnIdx, col4.get(row));
            logic.getNonogramSolutionBoardWithMarks().get(row).set(targetColumnIdx, col4Marks.get(row));
        }

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int i = 0; i < width; i++) colRanges.add(new ArrayList<>());
        List<List<Integer>> targetRanges = new ArrayList<>();
        targetRanges.add(new ArrayList<>(List.of(3, 6)));
        targetRanges.add(new ArrayList<>(List.of(8, 17)));
        colRanges.set(targetColumnIdx, targetRanges);
        logic.getNonogramColumnLogic().setColumnsSequencesRanges(colRanges);

        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());

        // when
        helper.placeXsColumnAtUnreachableFields(targetColumnIdx);

        // then
        assertEquals("X", logic.getNonogramSolutionBoard().get(0).get(targetColumnIdx));
        assertEquals("X", logic.getNonogramSolutionBoard().get(1).get(targetColumnIdx));
        assertEquals("X", logic.getNonogramSolutionBoard().get(18).get(targetColumnIdx));
        assertEquals("X", logic.getNonogramSolutionBoard().get(19).get(targetColumnIdx));
    }

    @Test
    @DisplayName("o10155 - should not place any X if all empty fields are reachable (column 0)")
    void shouldNotPlaceXIfAllEmptyFieldsAreReachable() {
        // given
        int height = 20;
        int width = 15;
        int targetColumnIdx = 0;

        List<List<Integer>> rowSequences = new ArrayList<>();
        List<List<Integer>> colSequences = new ArrayList<>();
        for (int i = 0; i < height; i++) rowSequences.add(Collections.singletonList(0));
        for (int i = 0; i < width; i++) colSequences.add(Collections.singletonList(0));

        NonogramRules rules = new NonogramRules(rowSequences, colSequences, height, width);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        List<String> col0 = List.of(
                "-", "-", "-", "-", "-", "-", "-", "-", "-", "X",
                "-", "-", "X", "O", "O", "-", "-", "-", "-", "-"
        );

        for (int row = 0; row < height; row++) {
            logic.getNonogramSolutionBoard().get(row).set(targetColumnIdx, col0.get(row));
        }

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int i = 0; i < width; i++) colRanges.add(new ArrayList<>());
        List<List<Integer>> targetRanges = new ArrayList<>();
        targetRanges.add(new ArrayList<>(List.of(0, 15)));
        targetRanges.add(new ArrayList<>(List.of(13, 19)));
        colRanges.set(targetColumnIdx, targetRanges);
        logic.getNonogramColumnLogic().setColumnsSequencesRanges(colRanges);

        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        // when
        helper.placeXsColumnAtUnreachableFields(targetColumnIdx);

        // then
        assertEquals(col0, logic.getBoardAccessHelper().getColumnCopy(targetColumnIdx));
        assertEquals(stepsBefore, logic.getNonogramState().getNewStepsMade());
    }

    @Test
    @DisplayName("o10155 - should place Xs around a coloured sequence and update range when matching a single sequence (column 7)")
    void shouldPlaceXsAroundSingleMatchingSequenceAndUpdateRange() {
        // given
        int height = 20;
        int width = 15;
        int targetColumnIdx = 7;

        List<List<Integer>> rowSequences = new ArrayList<>();
        List<List<Integer>> colSequences = new ArrayList<>();
        for (int i = 0; i < height; i++) rowSequences.add(Collections.singletonList(0));
        for (int i = 0; i < width; i++) colSequences.add(Collections.singletonList(0));

        colSequences.set(targetColumnIdx, List.of(7, 5, 4));

        NonogramRules rules = new NonogramRules(rowSequences, colSequences, height, width);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        List<String> col7 = List.of(
                "-", "-", "O", "O", "O", "O", "O", "O", "O", "-",
                "O", "O", "O", "O", "O", "-", "O", "O", "-", "-"
        );

        for (int row = 0; row < height; row++) {
            logic.getNonogramSolutionBoard().get(row).set(targetColumnIdx, col7.get(row));
        }

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int i = 0; i < width; i++) colRanges.add(new ArrayList<>());
        List<List<Integer>> targetRanges = new ArrayList<>();
        targetRanges.add(new ArrayList<>(List.of(2, 8)));
        targetRanges.add(new ArrayList<>(List.of(10, 14)));
        targetRanges.add(new ArrayList<>(List.of(15, 19)));
        colRanges.set(targetColumnIdx, targetRanges);
        logic.getNonogramColumnLogic().setColumnsSequencesRanges(colRanges);

        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        // when
        helper.placeXsAroundLongestSequencesInColumn(targetColumnIdx);

        // then
        assertEquals("X", logic.getNonogramSolutionBoard().get(1).get(targetColumnIdx));
        assertEquals("X", logic.getNonogramSolutionBoard().get(9).get(targetColumnIdx));
        /*
            2 X around 1st seq with length 7 (9)
            1 X  after 2nd seq with length 5 (6)
        */
        assertEquals(stepsBefore + 2 + 7 + 1 + 5, logic.getNonogramState().getNewStepsMade());
    }

    @Test
    @DisplayName("o10155 - should place Xs around a coloured sequence and update changed range if it differs from old (column 6)")
    void shouldUpdateSequenceRangeIfChanged() {
        // given
        int height = 20;
        int width = 15;
        int targetColumnIdx = 6;

        List<List<Integer>> rowSequences = new ArrayList<>();
        List<List<Integer>> colSequences = new ArrayList<>();
        for (int i = 0; i < height; i++) rowSequences.add(Collections.singletonList(0));
        for (int i = 0; i < width; i++) colSequences.add(Collections.singletonList(0));

        colSequences.set(targetColumnIdx, List.of(4, 1));

        NonogramRules rules = new NonogramRules(rowSequences, colSequences, height, width);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        List<String> col6 = List.of(
                "-", "-", "-", "-", "-", "O", "-", "-", "-", "-",
                "-", "-", "-", "-", "-", "-", "-", "X", "O", "X"
        );

        for (int row = 0; row < height; row++) {
            logic.getNonogramSolutionBoard().get(row).set(targetColumnIdx, col6.get(row));
        }

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int i = 0; i < width; i++) colRanges.add(new ArrayList<>());
        List<List<Integer>> targetRanges = new ArrayList<>();
        targetRanges.add(new ArrayList<>(List.of(0, 17)));
        targetRanges.add(new ArrayList<>(List.of(5, 19)));
        colRanges.set(targetColumnIdx, targetRanges);
        logic.getNonogramColumnLogic().setColumnsSequencesRanges(colRanges);

        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        // when
        helper.placeXsAroundLongestSequencesInColumn(targetColumnIdx);

        // then
        assertEquals("X", logic.getNonogramSolutionBoard().get(17).get(targetColumnIdx));
        assertEquals("X", logic.getNonogramSolutionBoard().get(19).get(targetColumnIdx));
        /*
            0 X around 2nd seq with length 1 (1)
        */
        assertEquals(stepsBefore + 1, logic.getNonogramState().getNewStepsMade());
    }

    @Test
    @DisplayName("o11424 - should not schedule action for top edge when index is invalid (column 27)")
    void shouldNotScheduleActionForInvalidTopEdge() {
        // given
        int height = 20;
        int width = 30;
        int columnIdx = 27;

        List<List<Integer>> rowSequences = new ArrayList<>();
        List<List<Integer>> colSequences = new ArrayList<>();
        for (int i = 0; i < height; i++) rowSequences.add(Collections.singletonList(0));
        for (int i = 0; i < width; i++) colSequences.add(Collections.singletonList(0));

        colSequences.set(columnIdx, List.of(5, 2, 5));
        NonogramRules rules = new NonogramRules(rowSequences, colSequences, height, width);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        List<String> col = List.of(
                "O", "O", "O", "O", "O", "X", "X", "-", "-", "-",
                "-", "X", "O", "O", "-", "O", "O", "O", "O", "-"
        );

        for (int row = 0; row < height; row++) {
            logic.getNonogramSolutionBoard().get(row).set(columnIdx, col.get(row));
        }

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int i = 0; i < width; i++) colRanges.add(new ArrayList<>());
        colRanges.set(columnIdx, List.of(
                List.of(0, 4),
                List.of(6, 13),
                List.of(9, 19)
        ));

        logic.getNonogramColumnLogic().setColumnsSequencesRanges(colRanges);

        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());
        List<String> columnBefore = logic.getNonogramBoardColumn(columnIdx);
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        // when
        helper.placeXsAroundLongestSequencesInColumn(columnIdx);

        // then
        List<String> columnAfter = logic.getNonogramBoardColumn(columnIdx);
        assertEquals(columnBefore, columnAfter);
        // 5 coloured fields // TODO - not increase steps when seqIdx is in columnSequencesIdxNotToInclude
        assertEquals(stepsBefore + 5, logic.getNonogramState().getNewStepsMade());
    }

    @Test
    @DisplayName("o11424 - should not place X if empty sequence can still fit valid sequence (column 27)")
    void shouldNotPlaceXIfEmptyRangeCouldFitValidSequence() {
        // given
        int height = 20;
        int width = 30;
        int columnIdx = 27;

        List<List<Integer>> rowSequences = new ArrayList<>();
        List<List<Integer>> colSequences = new ArrayList<>();
        for (int i = 0; i < height; i++) rowSequences.add(Collections.singletonList(0));
        for (int i = 0; i < width; i++) colSequences.add(Collections.singletonList(0));

        colSequences.set(columnIdx, List.of(5, 2, 5));
        NonogramRules rules = new NonogramRules(rowSequences, colSequences, height, width);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        List<String> col = List.of(
                "O", "O", "O", "O", "O", "X", "X", "-", "-", "-",
                "-", "X", "-", "-", "-", "O", "O", "-", "-", "-"
        );

        for (int row = 0; row < height; row++) {
            logic.getNonogramSolutionBoard().get(row).set(columnIdx, col.get(row));
        }

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int i = 0; i < width; i++) colRanges.add(new ArrayList<>());
        colRanges.set(columnIdx, new ArrayList<>(List.of(
                List.of(0, 10),
                List.of(6, 13),
                List.of(9, 19)
        )));

        logic.getNonogramColumnLogic().setColumnsSequencesRanges(colRanges);

        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());
        List<String> columnBefore = logic.getNonogramBoardColumn(columnIdx);
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        // when
        helper.placeXsColumnAtTooShortEmptySequences(columnIdx);

        // then
        List<String> columnAfter = logic.getNonogramBoardColumn(columnIdx);
        assertEquals(columnBefore, columnAfter);
        assertEquals(stepsBefore, logic.getNonogramState().getNewStepsMade());
    }


    @Test
    @DisplayName("o06611 - should not place X when at least one non-excluded sequence fits into empty range")
    void shouldNotPlaceXWhenNonExcludedSequenceFitsInEmptyRange() {
        // given
        int height = 20;
        int width = 40;
        int columnIdx = 11;

        List<List<Integer>> rowSequences = new ArrayList<>();
        List<List<Integer>> colSequences = new ArrayList<>();
        for (int i = 0; i < height; i++) rowSequences.add(Collections.singletonList(0));
        for (int i = 0; i < width; i++) colSequences.add(Collections.singletonList(0));

        colSequences.set(columnIdx, List.of(1, 3, 4, 1));
        NonogramRules rules = new NonogramRules(rowSequences, colSequences, height, width);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        List<String> col = List.of(
                "O", "X", "-", "X", "O", "O", "O", "X", "X", "X",
                "X", "X", "-", "O", "O", "O", "-", "-", "-", "-"
        );

        for (int row = 0; row < height; row++) {
            logic.getNonogramSolutionBoard().get(row).set(columnIdx, col.get(row));
        }

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int i = 0; i < width; i++) colRanges.add(new ArrayList<>());
        colRanges.set(columnIdx, new ArrayList<>(List.of(
                List.of(0, 0),
                List.of(4, 6),
                List.of(6, 17),
                List.of(11, 19)
        )));

        logic.getNonogramColumnLogic().setColumnsSequencesRanges(colRanges);
        logic.getNonogramColumnLogic().setColumnsSequencesIdsNotToInclude(List.of(
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(List.of(0, 1)), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()
        ));

        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());
        List<String> columnBefore = logic.getNonogramBoardColumn(columnIdx);
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        // when
        helper.placeXsColumnAtTooShortEmptySequences(columnIdx);

        // then
        List<String> columnAfter = logic.getNonogramBoardColumn(columnIdx);
        assertEquals(columnBefore, columnAfter);
        assertEquals(stepsBefore, logic.getNonogramState().getNewStepsMade());
    }

    @Test
    @DisplayName("o07513 - do nothing when no X is found after empty range to the end of column")
    void shouldDoNothingWhenNoXFoundToTheEnd() {
        // given
        int height = 30;
        int width = 30;
        int columnIdx = 7;

        List<List<Integer>> rowSequences = new ArrayList<>();
        List<List<Integer>> colSequences = new ArrayList<>();
        for (int i = 0; i < height; i++) rowSequences.add(Collections.singletonList(0));
        for (int i = 0; i < width; i++) colSequences.add(Collections.singletonList(0));

        colSequences.set(columnIdx, List.of(3, 1, 2, 6, 2, 1, 1));
        NonogramRules rules = new NonogramRules(rowSequences, colSequences, height, width);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        List<String> col = new ArrayList<>(Collections.nCopies(height, "-"));
        col.set(6, "O");
        col.set(14, "X");

        for (int row = 0; row < height; row++) {
            logic.getNonogramSolutionBoard().get(row).set(columnIdx, col.get(row));
        }

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int i = 0; i < width; i++) colRanges.add(new ArrayList<>());
        colRanges.set(columnIdx, new ArrayList<>(List.of(
                List.of(0, 10),
                List.of(4, 12),
                List.of(6, 15),
                List.of(9, 22),
                List.of(16, 25),
                List.of(19, 27),
                List.of(21, 29)
        )));

        logic.getNonogramColumnLogic().setColumnsSequencesRanges(colRanges);

        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());
        List<String> columnBefore = logic.getNonogramBoardColumn(columnIdx);
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        // when
        helper.placeXsColumnAtTooShortEmptySequences(columnIdx);

        // then
        List<String> columnAfter = logic.getNonogramBoardColumn(columnIdx);
        assertEquals(columnBefore, columnAfter);
        assertEquals(stepsBefore, logic.getNonogramState().getNewStepsMade());
    }

    @Test
    @DisplayName("o11424 - should reach invalid rowIdx in findEmptyRangesBetweenXs when no X found (column 24)")
    void shouldReachInvalidRowIdxWhenNoSecondXFound() {
        // given
        int height = 20;
        int width = 30;
        int columnIdx = 24;

        List<List<Integer>> rowSequences = new ArrayList<>();
        List<List<Integer>> colSequences = new ArrayList<>();
        for (int i = 0; i < height; i++) rowSequences.add(Collections.singletonList(0));
        for (int i = 0; i < width; i++) colSequences.add(Collections.singletonList(0));

        colSequences.set(columnIdx, List.of(7, 2, 3, 1));
        NonogramRules rules = new NonogramRules(rowSequences, colSequences, height, width);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        List<String> col = List.of(
                "-", "-", "-", "O", "O", "O", "O", "-", "-", "-",
                "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"
        );

        for (int row = 0; row < height; row++) {
            logic.getNonogramSolutionBoard().get(row).set(columnIdx, col.get(row));
        }

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int i = 0; i < width; i++) colRanges.add(new ArrayList<>());
        colRanges.set(columnIdx, new ArrayList<>(List.of(
                List.of(0, 10),
                List.of(8, 13),
                List.of(11, 17),
                List.of(15, 19)
        )));

        logic.getNonogramColumnLogic().setColumnsSequencesRanges(colRanges);
        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());

        List<String> columnBefore = logic.getNonogramBoardColumn(columnIdx);
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        // when
        helper.placeXsColumnAtTooShortEmptySequences(columnIdx);

        // then
        List<String> columnAfter = logic.getNonogramBoardColumn(columnIdx);
        assertEquals(columnBefore, columnAfter);
        assertEquals(stepsBefore, logic.getNonogramState().getNewStepsMade());
    }

    @Test
    @DisplayName("o10155 - should place Xs around longest coloured sequence if it matches the max of multiple matching sequences and Xs already exist at edges")
    void shouldPlaceXsAroundLongestIfMultipleMatchingLengthsAndMaxMatch() {
        // given
        int height = 20;
        int width = 15;
        int columnIdx = 1;

        List<List<Integer>> rowSequences = new ArrayList<>();
        List<List<Integer>> colSequences = new ArrayList<>();
        for (int i = 0; i < height; i++) rowSequences.add(Collections.singletonList(0));
        for (int i = 0; i < width; i++) colSequences.add(Collections.singletonList(0));

        colSequences.set(columnIdx, List.of(1, 1, 1, 4, 2));
        NonogramRules rules = new NonogramRules(rowSequences, colSequences, height, width);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);
        logic.getNonogramColumnLogic().getColumnsSequencesIdsNotToInclude().set(columnIdx, List.of(3, 4));

        List<String> col = List.of(
                "-", "-", "X", "-", "-", "-", "X", "X", "X", "O",
                "X", "X", "O", "O", "O", "O", "X", "X", "O", "O"
        );

        for (int row = 0; row < height; row++) {
            logic.getNonogramSolutionBoard().get(row).set(columnIdx, col.get(row));
        }

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int i = 0; i < width; i++) colRanges.add(new ArrayList<>());
        colRanges.set(columnIdx, new ArrayList<>(List.of(
                new ArrayList<>(List.of(0, 7)),
                new ArrayList<>(List.of(2, 9)),
                new ArrayList<>(List.of(4, 11)),
                new ArrayList<>(List.of(12, 15)),
                new ArrayList<>(List.of(18, 19))
        )));

        logic.getNonogramColumnLogic().setColumnsSequencesRanges(colRanges);

        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        // when
        helper.placeXsAroundLongestSequencesInColumn(columnIdx);

        // then
        assertEquals(stepsBefore, logic.getNonogramState().getNewStepsMade());
    }

    @Test
    @DisplayName("o10155 - should not place X when coloured range doesn't match any sequence (column 7)")
    void shouldNotPlaceXWhenColouredRangeDoesNotMatchAnySequence() {
        // given
        int height = 20;
        int width = 15;
        int columnIdx = 7;

        List<List<Integer>> rowSequences = new ArrayList<>();
        List<List<Integer>> colSequences = new ArrayList<>();
        for (int i = 0; i < height; i++) rowSequences.add(Collections.singletonList(0));
        for (int i = 0; i < width; i++) colSequences.add(Collections.singletonList(0));

        colSequences.set(columnIdx, List.of(7, 5, 4));
        NonogramRules rules = new NonogramRules(rowSequences, colSequences, height, width);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);
        logic.getNonogramColumnLogic().getColumnsSequencesIdsNotToInclude().set(columnIdx, new ArrayList<>(List.of(0, 1)));

        List<String> col = List.of(
                "-", "X", "O", "O", "O", "O", "O", "O", "O", "X",
                "O", "O", "O", "O", "O", "X", "O", "O", "-", "-"
        );

        for (int row = 0; row < height; row++) {
            logic.getNonogramSolutionBoard().get(row).set(columnIdx, col.get(row));
        }

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int i = 0; i < width; i++) colRanges.add(new ArrayList<>());
        colRanges.set(columnIdx, new ArrayList<>(List.of(
                new ArrayList<>(List.of(2, 8)),
                new ArrayList<>(List.of(10, 14)),
                new ArrayList<>(List.of(14, 19))
        )));

        logic.getNonogramColumnLogic().setColumnsSequencesRanges(colRanges);

        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        // when
        helper.placeXsAroundLongestSequencesInColumn(columnIdx);

        // then
        assertEquals(stepsBefore, logic.getNonogramState().getNewStepsMade());
    }

    @Test
    @DisplayName("o10155 - should skip placing X when xEdges contains invalid row index (column 7)")
    void shouldSkipPlacingXWhenRowIndexIsInvalid() {
        // given
        int height = 20;
        int width = 15;
        int targetColumnIdx = 7;

        List<List<Integer>> rowSequences = new ArrayList<>();
        List<List<Integer>> colSequences = new ArrayList<>();
        for (int i = 0; i < height; i++) rowSequences.add(Collections.singletonList(0));
        for (int i = 0; i < width; i++) colSequences.add(Collections.singletonList(0));
        colSequences.set(targetColumnIdx, List.of(7, 5, 4));

        NonogramRules rules = new NonogramRules(rowSequences, colSequences, height, width);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        List<String> col7 = List.of(
                "-", "X", "O", "O", "O", "O", "O", "O", "O", "X",
                "O", "O", "O", "O", "O", "X", "O", "O", "O", "O"
        );

        for (int row = 0; row < height; row++) {
            logic.getNonogramSolutionBoard().get(row).set(targetColumnIdx, col7.get(row));
        }

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int i = 0; i < width; i++) colRanges.add(new ArrayList<>());
        List<List<Integer>> targetRanges = new ArrayList<>();
        targetRanges.add(new ArrayList<>(List.of(2, 8)));
        targetRanges.add(new ArrayList<>(List.of(10, 14)));
        targetRanges.add(new ArrayList<>(List.of(14, 19)));
        colRanges.set(targetColumnIdx, targetRanges);
        logic.getNonogramColumnLogic().setColumnsSequencesRanges(colRanges);

        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        // when
        helper.placeXsAroundLongestSequencesInColumn(targetColumnIdx);

        /*
            0 X around 1st seq(idx=0) with length 7 (7)
            0 X around 2nd seq(idx=1) with length 5 (5)
            0 X around 3rd seq(idx=2) with length 4 (4)
        */
        // then
        assertEquals(stepsBefore + 7 + 5 + 4, logic.getNonogramState().getNewStepsMade());
    }

    @Test
    @DisplayName("o06005 - should NOT place Xs if coloured range is inside expected, but too long (column 7)")
    void shouldNotPlaceXsIfLengthTooLongEvenIfInsideRange_TF() {
        // given
        int height = 10;
        int width = 10;
        int targetColumnIdx = 7;

        List<List<Integer>> rowSequences = new ArrayList<>();
        List<List<Integer>> colSequences = new ArrayList<>();
        for (int i = 0; i < height; i++) rowSequences.add(Collections.singletonList(0));
        for (int i = 0; i < width; i++) colSequences.add(Collections.singletonList(0));

        colSequences.set(targetColumnIdx, List.of(2, 2, 1));

        NonogramRules rules = new NonogramRules(rowSequences, colSequences, height, width);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        List<String> col7 = List.of(
                "-", "-", "X", "O", "O", "X", "O", "O", "-", "-"
        );

        for (int row = 0; row < height; row++) {
            logic.getNonogramSolutionBoard().get(row).set(targetColumnIdx, col7.get(row));
        }

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int i = 0; i < width; i++) colRanges.add(new ArrayList<>());

        List<List<Integer>> targetRanges = new ArrayList<>();
        targetRanges.add(List.of(0, 4));  // corresponds to [2, 2, 1]
        targetRanges.add(List.of(3, 7));
        targetRanges.add(List.of(6, 9));
        colRanges.set(targetColumnIdx, targetRanges);
        logic.getNonogramColumnLogic().setColumnsSequencesRanges(colRanges);

        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        // when
        helper.placeXsAroundLongestSequencesInColumn(targetColumnIdx);

        // then
        assertEquals("X", logic.getNonogramSolutionBoard().get(7 + 1).get(targetColumnIdx));
        /* X placed + 2 fields excluded */
        assertEquals(stepsBefore + 3, logic.getNonogramState().getNewStepsMade());
    }

    @Test
    @DisplayName("o10155 - should place X on unreachable empty field in short empty sequence (column 11)")
    void shouldPlaceXInTooShortEmptySequence() {
        // given
        int height = 20;
        int width = 15;
        int columnIdx = 11;

        List<List<Integer>> rowSequences = new ArrayList<>();
        List<List<Integer>> colSequences = new ArrayList<>();
        for (int i = 0; i < height; i++) rowSequences.add(Collections.singletonList(0));
        for (int i = 0; i < width; i++) colSequences.add(Collections.singletonList(0));

        colSequences.set(columnIdx, List.of(3, 1, 7, 1));
        NonogramRules rules = new NonogramRules(rowSequences, colSequences, height, width);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        List<String> col = List.of(
                "-", "-", "O", "-", "-", "X", "-", "X", "O", "X",
                "O", "O", "O", "O", "O", "O", "O", "X", "O", "X"
        );

        for (int row = 0; row < height; row++) {
            logic.getNonogramSolutionBoard().get(row).set(columnIdx, col.get(row));
        }

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int i = 0; i < width; i++) colRanges.add(new ArrayList<>());
        colRanges.set(columnIdx, new ArrayList<>(List.of(
                new ArrayList<>(List.of(0, 7)),
                new ArrayList<>(List.of(8, 8)),
                new ArrayList<>(List.of(10, 16)),
                new ArrayList<>(List.of(18, 18))
        )));

        logic.getNonogramColumnLogic().setColumnsSequencesRanges(colRanges);
        logic.getNonogramColumnLogic().setColumnsSequencesIdsNotToInclude(List.of(
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(List.of(1, 2, 3)), // column 11
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>()
        ));

        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        // when
        helper.placeXsColumnAtTooShortEmptySequences(columnIdx);

        // then
        assertEquals("X", logic.getNonogramSolutionBoard().get(5).get(columnIdx));
        /*
            1 X in tooShortEmptySequenceRange ([6, 6])
        */
        assertEquals(stepsBefore + 1, logic.getNonogramState().getNewStepsMade());
    }

    @Test
    @DisplayName("o07150 - should not place X when empty range is not valid for exclusion (column 12)")
    void shouldNotPlaceXIfNoValidExclusionRange() {
        // given
        int height = 20;
        int width = 30;
        int columnIdx = 12;

        List<List<Integer>> rowSequences = new ArrayList<>();
        List<List<Integer>> colSequences = new ArrayList<>();
        for (int i = 0; i < height; i++) rowSequences.add(Collections.singletonList(0));
        for (int i = 0; i < width; i++) colSequences.add(Collections.singletonList(0));

        colSequences.set(columnIdx, List.of(2, 1, 2, 4, 2));
        NonogramRules rules = new NonogramRules(rowSequences, colSequences, height, width);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        List<String> col = List.of(
                "X", "O", "O", "X", "O", "X", "O", "O", "X", "-",
                "-", "O", "O", "-", "-", "-", "-", "-", "-", "-"
        );

        for (int row = 0; row < height; row++) {
            logic.getNonogramSolutionBoard().get(row).set(columnIdx, col.get(row));
        }

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int i = 0; i < width; i++) colRanges.add(new ArrayList<>());
        colRanges.set(columnIdx, new ArrayList<>(List.of(
                List.of(1, 2),
                List.of(4, 4),
                List.of(6, 7),
                List.of(8, 16),
                List.of(13, 19)
        )));

        logic.getNonogramColumnLogic().setColumnsSequencesRanges(colRanges);
        logic.getNonogramColumnLogic().getColumnsSequencesIdsNotToInclude().set(columnIdx, List.of(0, 1, 2));

        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());
        List<String> columnBefore = logic.getNonogramBoardColumn(columnIdx);
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        // when
        helper.placeXsColumnAtTooShortEmptySequences(columnIdx);

        // then
        List<String> columnAfter = logic.getNonogramBoardColumn(columnIdx);
        assertEquals(columnBefore, columnAfter);
        assertEquals(stepsBefore, logic.getNonogramState().getNewStepsMade());
    }

    @Test // placeXsColumnIfOWillMergeNearFieldsToTooLongColouredSequence
    @DisplayName("o10155 - should place X if O would merge segments into too long sequence (column 5)")
    void shouldPlaceXToPreventTooLongSequenceMerge() {
        // given
        int height = 20;
        int width = 15;
        int columnIdx = 5;

        List<List<Integer>> rowSequences = new ArrayList<>();
        List<List<Integer>> colSequences = new ArrayList<>();
        for (int i = 0; i < height; i++) rowSequences.add(Collections.singletonList(0));
        for (int i = 0; i < width; i++) colSequences.add(Collections.singletonList(0));

        colSequences.set(columnIdx, List.of(10, 5));
        NonogramRules rules = new NonogramRules(rowSequences, colSequences, height, width);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        List<String> col = List.of(
                "-", "-", "-", "O", "O", "O", "O", "O", "O", "O",
                "O", "O", "-", "O", "O", "O", "O", "-", "-", "-"
        );

        for (int row = 0; row < height; row++) {
            logic.getNonogramSolutionBoard().get(row).set(columnIdx, col.get(row));
        }

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int i = 0; i < width; i++) colRanges.add(new ArrayList<>());
        colRanges.set(columnIdx, new ArrayList<>(List.of(
                new ArrayList<>(List.of(0, 13)),
                new ArrayList<>(List.of(11, 19))
        )));

        logic.getNonogramColumnLogic().setColumnsSequencesRanges(colRanges);

        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        // when
        helper.placeXsColumnIfOWillMergeNearFieldsToTooLongColouredSequence(columnIdx);

        // then
        assertEquals(stepsBefore + 1, logic.getNonogramState().getNewStepsMade()); // one X placed
    }

    @Test // placeXsColumnIfOWillMergeNearFieldsToTooLongColouredSequence
    @DisplayName("o10155 - should not place X if merging coloured ranges still matches column sequences (column 7)")
    void shouldNotPlaceXWhenMergingStillValid() {
        // given
        int height = 20;
        int width = 15;
        int columnIdx = 7;

        List<List<Integer>> rowSequences = new ArrayList<>();
        List<List<Integer>> colSequences = new ArrayList<>();
        for (int i = 0; i < height; i++) rowSequences.add(Collections.singletonList(0));
        for (int i = 0; i < width; i++) colSequences.add(Collections.singletonList(0));

        colSequences.set(columnIdx, List.of(7, 5, 4));
        NonogramRules rules = new NonogramRules(rowSequences, colSequences, height, width);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        List<String> col = List.of(
                "-", "X", "O", "O", "O", "O", "O", "O", "O", "X",
                "O", "O", "O", "O", "O", "X", "O", "O", "-", "-"
        );

        for (int row = 0; row < height; row++) {
            logic.getNonogramSolutionBoard().get(row).set(columnIdx, col.get(row));
        }

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int i = 0; i < width; i++) colRanges.add(new ArrayList<>());
        colRanges.set(columnIdx, new ArrayList<>(List.of(
                new ArrayList<>(List.of(2, 8)),
                new ArrayList<>(List.of(10, 14)),
                new ArrayList<>(List.of(14, 19))
        )));

        logic.getNonogramColumnLogic().setColumnsSequencesRanges(colRanges);

        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        // when
        helper.placeXsColumnIfOWillMergeNearFieldsToTooLongColouredSequence(columnIdx);

        // then
        // no X placed
        assertEquals(stepsBefore, logic.getNonogramState().getNewStepsMade());
    }

    @Test // placeXsColumnIfOWillMergeNearFieldsToTooLongColouredSequence
    @DisplayName("o11424 - don't place X if next row is out of bounds when checking placement after coloured group")
    void shouldNotPlaceXIfNextRowIsOutOfBoundsAfterColouredSequence() {
        // given
        int height = 20;
        int width = 30;
        int columnIdx = 27;

        List<List<Integer>> rowSequences = new ArrayList<>();
        List<List<Integer>> colSequences = new ArrayList<>();
        for (int i = 0; i < height; i++) rowSequences.add(Collections.singletonList(0));
        for (int i = 0; i < width; i++) colSequences.add(Collections.singletonList(0));

        colSequences.set(columnIdx, List.of(5, 2, 5));
        NonogramRules rules = new NonogramRules(rowSequences, colSequences, height, width);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        List<String> col = List.of(
                "O", "O", "O", "O", "O", "X", "X", "-", "-", "-",
                "-", "X", "O", "O", "X", "O", "O", "O", "O", "O"
        );

        for (int row = 0; row < height; row++) {
            logic.getNonogramSolutionBoard().get(row).set(columnIdx, col.get(row));
        }

        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());
        List<String> columnBefore = logic.getNonogramBoardColumn(columnIdx);
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        // when
        helper.placeXsColumnIfOWillMergeNearFieldsToTooLongColouredSequence(columnIdx);

        // then
        List<String> columnAfter = logic.getNonogramBoardColumn(columnIdx);
        assertEquals(columnBefore, columnAfter);
        assertEquals(stepsBefore, logic.getNonogramState().getNewStepsMade());
    }

    @Test
    @DisplayName("o07150 - should place X before coloured range to avoid too long merged sequence (column 13)")
    void shouldPlaceXBeforeTooLongMergedSequence() {
        // given
        int height = 20;
        int width = 30;
        int columnIdx = 13;

        List<List<Integer>> rowSequences = new ArrayList<>();
        List<List<Integer>> colSequences = new ArrayList<>();
        for (int i = 0; i < height; i++) rowSequences.add(Collections.singletonList(0));
        for (int i = 0; i < width; i++) colSequences.add(Collections.singletonList(0));

        colSequences.set(columnIdx, List.of(2, 4, 1, 3, 2));
        NonogramRules rules = new NonogramRules(rowSequences, colSequences, height, width);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        List<String> col = List.of(
                "-", "O", "O", "-", "O", "O", "O", "O", "X", "O",
                "-", "O", "O", "O", "-", "-", "-", "-", "-", "-"
        );
        for (int row = 0; row < height; row++) {
            logic.getNonogramSolutionBoard().get(row).set(columnIdx, col.get(row));
        }

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int i = 0; i < width; i++) colRanges.add(new ArrayList<>());
        colRanges.set(columnIdx, List.of(
                List.of(0, 5),
                List.of(3, 10),
                List.of(8, 12),
                List.of(10, 16),
                List.of(14, 19)
        ));
        logic.getNonogramColumnLogic().setColumnsSequencesRanges(colRanges);

        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        // when
        helper.placeXsColumnIfOWillMergeNearFieldsToTooLongColouredSequence(columnIdx);

        // then
        assertTrue(isFieldWithX(logic.getNonogramSolutionBoard(), new Field(10, columnIdx)));
        assertEquals(stepsBefore + 4, logic.getNonogramState().getNewStepsMade());
    }

    @Test
    @DisplayName("o10155 - should place X if O near X would start a too long sequence (column 10)")
    void shouldPlaceXIfONearXStartsTooLongSequenceInColumn10() {
        // given
        int height = 20;
        int width = 15;
        int columnIdx = 10;

        List<List<Integer>> rowSequences = new ArrayList<>();
        List<List<Integer>> colSequences = new ArrayList<>();
        for (int i = 0; i < height; i++) rowSequences.add(Collections.singletonList(0));
        for (int i = 0; i < width; i++) colSequences.add(Collections.singletonList(0));

        colSequences.set(columnIdx, List.of(1, 1, 5, 4, 2));
        NonogramRules rules = new NonogramRules(rowSequences, colSequences, height, width);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        List<String> col = List.of(
                "-", "-", "O", "-", "X", "O", "X", "O", "O", "O",
                "O", "O", "X", "O", "O", "O", "O", "X", "O", "O"
        );

        for (int row = 0; row < height; row++) {
            logic.getNonogramSolutionBoard().get(row).set(columnIdx, col.get(row));
        }

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int i = 0; i < width; i++) colRanges.add(new ArrayList<>());
        colRanges.set(columnIdx, new ArrayList<>(List.of(
                List.of(0, 3),
                List.of(5, 5),
                List.of(7, 11),
                List.of(13, 16),
                List.of(18, 19)
        )));

        logic.getNonogramColumnLogic().setColumnsSequencesRanges(colRanges);

        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        // when
        helper.placeXsColumnIfONearXWillBeginTooLongPossibleColouredSequence(columnIdx);

        // then
        assertEquals(stepsBefore + 1, logic.getNonogramState().getNewStepsMade());
        assertEquals("X", logic.getNonogramSolutionBoard().get(3).get(columnIdx));

        List<NonogramActionDetails> actions = logic.getActionScheduler().getActionsToDoList();
        assertTrue(actions.stream().anyMatch(a ->
                a.getIndex() == 3 &&
                        a.getTriggeringActionName() == NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE &&
                        a.getActionName() == NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY));
        assertTrue(actions.stream().anyMatch(a ->
                a.getIndex() == 3 &&
                        a.getTriggeringActionName() == NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE &&
                        a.getActionName() == NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES));
    }

    @Test
    @DisplayName("o11424 - add range start index if range list has one element in updateRange for backward direction (column 28)")
    void shouldAddIndexStartRangeToOneElemListBackwardInUpdateRange() {
        // given
        int height = 20;
        int width = 30;
        int columnIdx = 28;

        List<List<Integer>> rowSequences = new ArrayList<>();
        List<List<Integer>> colSequences = new ArrayList<>();
        for (int i = 0; i < height; i++) rowSequences.add(Collections.singletonList(0));
        for (int i = 0; i < width; i++) colSequences.add(Collections.singletonList(0));

        colSequences.set(columnIdx, List.of(1, 4, 2, 4));
        NonogramRules rules = new NonogramRules(rowSequences, colSequences, height, width);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        List<String> col = List.of(
                "-", "-", "-", "-", "-", "-", "-", "-", "-", "-",
                "X", "X", "X", "-", "-", "-", "-", "-", "-", "-"
        );

        for (int row = 0; row < height; row++) {
            logic.getNonogramSolutionBoard().get(row).set(columnIdx, col.get(row));
        }

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int i = 0; i < width; i++) colRanges.add(new ArrayList<>());
        colRanges.set(columnIdx, new ArrayList<>(List.of(
                List.of(0, 6),
                List.of(2, 11),
                List.of(7, 14),
                List.of(10, 19)
        )));

        logic.getNonogramColumnLogic().setColumnsSequencesRanges(colRanges);

        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());
        List<String> columnBefore = logic.getNonogramBoardColumn(columnIdx);
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        // when
        helper.placeXsColumnIfONearXWillBeginTooLongPossibleColouredSequence(columnIdx);

        // then
        List<String> columnAfter = logic.getNonogramBoardColumn(columnIdx);
        assertEquals(columnBefore, columnAfter);
        assertEquals(stepsBefore, logic.getNonogramState().getNewStepsMade());
    }

    @Test
    @DisplayName("o07150 - don't place X if sequence fits in empty fields sequence after X and before coloured sequence")
    void shouldNotPlaceXIfSequenceFitsInEmptyFieldsBetweenXAndColouredSequence() {
        // given
        int height = 20;
        int width = 30;
        int columnIdx = 27;

        List<List<Integer>> rowSequences = new ArrayList<>();
        List<List<Integer>> colSequences = new ArrayList<>();
        for (int i = 0; i < height; i++) rowSequences.add(Collections.singletonList(0));
        for (int i = 0; i < width; i++) colSequences.add(Collections.singletonList(0));

        colSequences.set(columnIdx, List.of(5, 2, 5));
        NonogramRules rules = new NonogramRules(rowSequences, colSequences, height, width);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        List<String> col = List.of(
                "O", "O", "O", "O", "O", "X", "-", "-", "-", "-",
                "-", "X", "-", "-", "-", "O", "O", "-", "-", "-"
        );

        for (int row = 0; row < height; row++) {
            logic.getNonogramSolutionBoard().get(row).set(columnIdx, col.get(row));
        }

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int i = 0; i < width; i++) colRanges.add(new ArrayList<>());
        colRanges.set(columnIdx, new ArrayList<>(List.of(
                List.of(0, 10),
                List.of(6, 13),
                List.of(9, 19)
        )));

        logic.getNonogramColumnLogic().setColumnsSequencesRanges(colRanges);

        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());
        List<String> columnBefore = logic.getNonogramBoardColumn(columnIdx);
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        // when
        helper.placeXsColumnIfONearXWillBeginTooLongPossibleColouredSequence(columnIdx);

        // then
        List<String> columnAfter = logic.getNonogramBoardColumn(columnIdx);
        assertEquals(columnBefore, columnAfter);
        assertEquals(stepsBefore, logic.getNonogramState().getNewStepsMade());
    }

    @Test
    @DisplayName("o09946 - should place X if no sequence fits between X and coloured sequence")
    void shouldPlaceXIfNoSequenceFitsBetweenXAndColouredAndNoFittingCandidates() {
        // given
        int height = 20;
        int width = 30;
        int columnIdx = 21;

        List<List<Integer>> rowSequences = new ArrayList<>();
        List<List<Integer>> colSequences = new ArrayList<>();
        for (int i = 0; i < height; i++) rowSequences.add(Collections.singletonList(0));
        for (int i = 0; i < width; i++) colSequences.add(Collections.singletonList(0));

        colSequences.set(columnIdx, List.of(3, 4, 3));
        NonogramRules rules = new NonogramRules(rowSequences, colSequences, height, width);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        List<String> col = List.of(
                "O", "O", "O", "X", "X", "X", "X", "X", "X", "-",
                "-", "O", "O", "O", "-", "X", "X", "O", "O", "O"
        );

        for (int row = 0; row < height; row++) {
            logic.getNonogramSolutionBoard().get(row).set(columnIdx, col.get(row));
        }

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int i = 0; i < width; i++) colRanges.add(new ArrayList<>());
        colRanges.set(columnIdx, new ArrayList<>(List.of(
                List.of(0, 10),
                List.of(4, 15),
                List.of(17, 19)
        )));

        logic.getNonogramColumnLogic().setColumnsSequencesRanges(colRanges);

        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());
        List<String> columnBefore = logic.getNonogramBoardColumn(columnIdx);
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        // when
        helper.placeXsColumnIfONearXWillBeginTooLongPossibleColouredSequence(columnIdx);

        // then
        List<String> columnAfter = logic.getNonogramBoardColumn(columnIdx);
        assertNotEquals(columnBefore, columnAfter);
        assertEquals(stepsBefore + 1, logic.getNonogramState().getNewStepsMade());
    }

    @Test
    @DisplayName("o07150 - should not place X and direction isForward==true (column 12)")
    void shouldNotPlaceXWhenIsForwardTrueAndNoExclusion() {
        // given
        int height = 20;
        int width = 30;
        int columnIdx = 12;

        List<List<Integer>> rowSequences = new ArrayList<>();
        List<List<Integer>> colSequences = new ArrayList<>();
        for (int i = 0; i < height; i++) rowSequences.add(Collections.singletonList(0));
        for (int i = 0; i < width; i++) colSequences.add(Collections.singletonList(0));

        colSequences.set(columnIdx, List.of(2, 1, 2, 4, 2));
        NonogramRules rules = new NonogramRules(rowSequences, colSequences, height, width);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        List<String> col = List.of(
                "X", "O", "O", "X", "O", "X", "O", "O", "X", "-",
                "-", "O", "O", "-", "-", "-", "-", "-", "-", "-"
        );

        for (int row = 0; row < height; row++) {
            logic.getNonogramSolutionBoard().get(row).set(columnIdx, col.get(row));
        }

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int i = 0; i < width; i++) colRanges.add(new ArrayList<>());
        colRanges.set(columnIdx, new ArrayList<>(List.of(
                List.of(1, 2),
                List.of(4, 4),
                List.of(6, 7),
                List.of(8, 16),
                List.of(13, 19)
        )));

        logic.getNonogramColumnLogic().setColumnsSequencesRanges(colRanges);
        logic.getNonogramColumnLogic().getColumnsSequencesIdsNotToInclude().set(columnIdx, List.of(0, 1, 2));

        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());
        List<String> columnBefore = logic.getNonogramBoardColumn(columnIdx);
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        // when
        helper.placeXsColumnIfONearXWillBeginTooLongPossibleColouredSequence(columnIdx);

        // then
        List<String> columnAfter = logic.getNonogramBoardColumn(columnIdx);
        assertEquals(columnBefore, columnAfter, "Column should not change if no exclusion occurs from top (isForward==true)");
        assertEquals(stepsBefore, logic.getNonogramState().getNewStepsMade(), "No new steps should be registered");
    }

    @Test
    @DisplayName("o11479 - should not place X if fitting sequence fits inside empty space above coloured")
    void shouldNotPlaceXIfFittingSequenceFitsAboveColouredRange() {
        // given
        int height = 20;
        int width = 30;
        int columnIdx = 11;

        List<List<Integer>> rowSequences = new ArrayList<>();
        List<List<Integer>> colSequences = new ArrayList<>();
        for (int i = 0; i < height; i++) rowSequences.add(Collections.singletonList(0));
        for (int i = 0; i < width; i++) colSequences.add(Collections.singletonList(0));

        colSequences.set(columnIdx, List.of(8, 2));
        NonogramRules rules = new NonogramRules(rowSequences, colSequences, height, width);
        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);

        List<String> col = List.of(
                "-", "-", "-", "O", "O", "O", "O", "O", "-", "-",
                "-", "X", "X", "X", "-", "-", "-", "-", "-", "-"
        );

        for (int row = 0; row < height; row++) {
            logic.getNonogramSolutionBoard().get(row).set(columnIdx, col.get(row));
        }

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int i = 0; i < width; i++) colRanges.add(new ArrayList<>());
        colRanges.set(columnIdx, new ArrayList<>(List.of(
                List.of(0, 16),
                List.of(9, 19)
        )));

        logic.getNonogramColumnLogic().setColumnsSequencesRanges(colRanges);

        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(logic.getNonogramColumnLogic());
        List<String> columnBefore = logic.getNonogramBoardColumn(columnIdx);
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        // when
        helper.placeXsColumnIfONearXWillBeginTooLongPossibleColouredSequence(columnIdx);

        // then
        List<String> columnAfter = logic.getNonogramBoardColumn(columnIdx);
        assertEquals(columnBefore, columnAfter);
        assertEquals(stepsBefore, logic.getNonogramState().getNewStepsMade());
    }

    @DisplayName("refreshFrom: should copy columnsSequencesRanges & columnsFieldsNotToInclude for column 0 (o06005)")
    @Test
    void shouldRefreshFromAnotherLogicInstance_minimalDiffOnSingleColumn() {
        // given
        int height = 10;
        int width = 10;
        NonogramRules rules = new NonogramRules(
                buildConstantSeqs(height, 0),
                buildConstantSeqs(width, 0),
                height, width
        );

        NonogramLogic logicForHelper = new NonogramLogic(rules, GuessMode.DISABLED);
        NonogramColumnLogic columnLogicForHelper = new NonogramColumnLogic(logicForHelper);

        NonogramLogic logicToCopySrc = new NonogramLogic(rules, GuessMode.DISABLED);
        NonogramColumnLogic columnLogicToCopy = new NonogramColumnLogic(logicToCopySrc);

        var helperRanges = deepCopy(columnLogicForHelper.getColumnsSequencesRanges());
        helperRanges.set(0, new ArrayList<>(List.of(new ArrayList<>(List.of(0, 9)))));
        columnLogicForHelper.setColumnsSequencesRanges(helperRanges);

        var helperFields = deepCopy1D(columnLogicForHelper.getColumnsFieldsNotToInclude());
        helperFields.set(0, new ArrayList<>());
        columnLogicForHelper.setColumnsFieldsNotToInclude(helperFields);

        var copyRanges = deepCopy(columnLogicToCopy.getColumnsSequencesRanges());
        copyRanges.set(0, new ArrayList<>(List.of(new ArrayList<>(List.of(1, 9)))));
        columnLogicToCopy.setColumnsSequencesRanges(copyRanges);

        var copyFields = deepCopy1D(columnLogicToCopy.getColumnsFieldsNotToInclude());
        copyFields.set(0, new ArrayList<>(List.of(3)));
        columnLogicToCopy.setColumnsFieldsNotToInclude(copyFields);

        ColumnXPlacementHelperImpl helper = new ColumnXPlacementHelperImpl(columnLogicForHelper);

        assertThat(helper.getLogic().getColumnsSequencesRanges().get(0)).isEqualTo(List.of(List.of(0, 9)));
        assertThat(helper.getLogic().getColumnsFieldsNotToInclude().get(0)).isEmpty();

        // when
        helper.refreshFrom(columnLogicToCopy);

        // then
        assertThat(helper.getLogic().getColumnsSequencesRanges().get(0)).isEqualTo(List.of(List.of(1, 9)));
        assertThat(helper.getLogic().getColumnsFieldsNotToInclude().get(0)).containsExactly(3);
    }

    private static List<List<Integer>> buildConstantSeqs(int count, int val) {
        List<List<Integer>> out = new ArrayList<>(count);
        for (int i = 0; i < count; i++) out.add(List.of(val));
        return out;
    }

    private static List<List<List<Integer>>> deepCopy(List<List<List<Integer>>> src) {
        List<List<List<Integer>>> copy = new ArrayList<>(src.size());
        for (var col : src) {
            List<List<Integer>> inner = new ArrayList<>(col.size());
            for (var range : col) inner.add(new ArrayList<>(range));
            copy.add(inner);
        }
        return copy;
    }

    private static List<List<Integer>> deepCopy1D(List<List<Integer>> src) {
        List<List<Integer>> copy = new ArrayList<>(src.size());
        for (var lst : src) copy.add(new ArrayList<>(lst));
        return copy;
    }
}
