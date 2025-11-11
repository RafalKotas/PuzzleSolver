package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramState;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.config.GuessMode;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row.xplacement.RowXPlacementHelperImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramState.buildInitialEmptyNonogramState;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RowXPlacementHelperImplTest {

    @DisplayName("Should place Xs at unreachable fields - o10683 row 0")
    @Test
    void shouldPlaceXsAtUnreachableFields() {
        // given
        List<List<String>> board = new ArrayList<>();
        board.add(new ArrayList<>(List.of("-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-", "-")));
        board.add(new ArrayList<>(List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "-")));
        board.add(new ArrayList<>(List.of("-", "-", "-", "-", "-", "X", "X", "X", "-", "-", "-", "-", "-", "-", "-")));
        board.add(new ArrayList<>(List.of("-", "-", "-", "-", "-", "O", "-", "O", "X", "X", "-", "-", "-", "-", "-")));
        board.add(new ArrayList<>(List.of("X", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "X")));
        board.add(new ArrayList<>(List.of("O", "O", "-", "-", "O", "O", "-", "O", "O", "O", "O", "-", "-", "O", "O")));
        board.add(new ArrayList<>(List.of("O", "O", "O", "O", "O", "X", "X", "O", "O", "O", "O", "X", "-", "O", "O")));
        board.add(new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "X", "O", "O", "O", "O", "X", "O", "O", "O")));
        board.add(new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "X", "O", "O", "O", "O", "X", "O", "O", "O")));
        board.add(new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "X", "O", "O", "O", "O", "O", "O", "O", "O")));
        board.add(new ArrayList<>(List.of("X", "O", "O", "O", "O", "X", "X", "X", "O", "O", "O", "O", "O", "O", "X")));
        board.add(new ArrayList<>(List.of("-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-")));
        board.add(new ArrayList<>(List.of("-", "-", "-", "-", "-", "X", "X", "X", "X", "X", "-", "O", "O", "-", "-")));
        board.add(new ArrayList<>(List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "O", "-", "-", "-")));
        board.add(new ArrayList<>(List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "X", "-", "-", "-")));

        int rowIdx = 0;
        int initialColourOverlappingActionsCount = 30;

        List<List<List<Integer>>> rowSequencesRanges = new ArrayList<>();
        rowSequencesRanges.add(List.of(List.of(3, 9)));

        List<List<Integer>> rowsFieldsNotToInclude = new ArrayList<>();
        rowsFieldsNotToInclude.add(new ArrayList<>());

        NonogramRules nonogramRules = createTestNonogramRules_placingXAtUnreachableFields_o10683();

        NonogramState state = buildInitialEmptyNonogramState();

        NonogramLogic nonogramLogic = new NonogramLogic(nonogramRules, GuessMode.DISABLED);
        nonogramLogic.setNonogramSolutionBoard(board);
        nonogramLogic.initializeHelpers();

        NonogramRowLogic nonogramRowLogic = new NonogramRowLogic(nonogramLogic, nonogramLogic.getBoardAccessHelper(), nonogramLogic.getActionScheduler());
        nonogramRowLogic.setNonogramSolutionBoard(board);
        nonogramRowLogic.setRowsSequencesRanges(rowSequencesRanges);
        nonogramRowLogic.setRowsFieldsNotToInclude(rowsFieldsNotToInclude);
        nonogramRowLogic.setNonogramRules(nonogramRules);
        nonogramRowLogic.setNonogramState(state);

        RowXPlacementHelperImpl rowXPlacementHelper = new RowXPlacementHelperImpl(nonogramRowLogic);

        // when
        rowXPlacementHelper.placeXsRowAtUnreachableFields(rowIdx);

        // then
        List<String> expectedRow = List.of("X", "X", "X", "-", "-", "O", "O", "O", "-", "-", "X", "X", "X", "X", "X");
        assertThat(nonogramRowLogic.getBoardAccessHelper().getRowCopy(rowIdx)).isEqualTo(expectedRow);

        assertThat(state.getNewStepsMade()).isEqualTo(8);
        assertThat(nonogramRowLogic.getActionScheduler().getActionsToDoList()).hasSize(initialColourOverlappingActionsCount + 64);

        assertNotNull(rowXPlacementHelper.getNonogramFieldPlacingXHelper());
        assertNotNull(rowXPlacementHelper.getNonogramRowLogic());
    }

    @DisplayName("Should not place any Xs if all fields are within ranges - o10017 row 0")
    @Test
    void shouldNotPlaceXsIfAllFieldsReachable() {
        // given
        List<List<String>> board = new ArrayList<>();
        board.add(new ArrayList<>(List.of("X", "X", "X", "X", "X", "-", "-", "-", "O", "-", "-", "-", "X", "X", "X")));

        int rowIdx = 0;
        int initialColourOverlappingActionsCount = 30;

        List<List<List<Integer>>> rowSequencesRanges = new ArrayList<>();
        rowSequencesRanges.add(List.of(List.of(5, 11)));

        List<List<Integer>> rowsFieldsNotToInclude = new ArrayList<>();
        rowsFieldsNotToInclude.add(new ArrayList<>());

        NonogramRules nonogramRules = createTestNonogramRules_o10017();
        NonogramState state = buildInitialEmptyNonogramState();

        NonogramLogic nonogramLogic = new NonogramLogic(nonogramRules, GuessMode.DISABLED);
        nonogramLogic.setNonogramSolutionBoard(board);
        nonogramLogic.initializeHelpers();

        NonogramRowLogic nonogramRowLogic = new NonogramRowLogic(nonogramLogic,
                nonogramLogic.getBoardAccessHelper(), nonogramLogic.getActionScheduler());
        nonogramRowLogic.setNonogramSolutionBoard(board);
        nonogramRowLogic.setRowsSequencesRanges(rowSequencesRanges);
        nonogramRowLogic.setRowsFieldsNotToInclude(rowsFieldsNotToInclude);
        nonogramRowLogic.setNonogramRules(nonogramRules);
        nonogramRowLogic.setNonogramState(state);

        RowXPlacementHelperImpl rowXPlacementHelper = new RowXPlacementHelperImpl(nonogramRowLogic);

        // when
        rowXPlacementHelper.placeXsRowAtUnreachableFields(rowIdx);

        // then
        List<String> expectedRow = List.of("X", "X", "X", "X", "X", "-", "-", "-", "O", "-", "-", "-", "X", "X", "X");
        assertThat(nonogramRowLogic.getBoardAccessHelper().getRowCopy(rowIdx)).isEqualTo(expectedRow);

        assertThat(state.getNewStepsMade()).isZero();
        assertThat(nonogramRowLogic.getActionScheduler().getActionsToDoList()).hasSize(initialColourOverlappingActionsCount);

        assertNotNull(rowXPlacementHelper.getNonogramFieldPlacingXHelper());
        assertNotNull(rowXPlacementHelper.getNonogramRowLogic());
    }

    @DisplayName("Should continue while when field is not coloured during placing Xs around longest sequences in row")
    @Test
    void shouldContinueWhileWhenFieldIsNotColouredDuringPlacingXsAroundLongestSequencesInRow() {
        // given
        List<List<String>> board = List.of(
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "O", "-", "X", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "O", "-", "O", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "O", "-", "O", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "O", "-", "O", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "O", "-", "O", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-", "-"),
                List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-")
        );

        int rowIdx = 3;

        List<List<List<Integer>>> rowSequencesRanges = List.of(
                List.of(List.of(5, 11)),
                List.of(List.of(0, 9), List.of(7, 14)),
                List.of(List.of(0, 7), List.of(5, 14)),
                List.of(List.of(3, 10), List.of(9, 14))
        );

        List<List<Integer>> rowsFieldsNotToInclude = IntStream.range(0, 15)
                .mapToObj(i -> new ArrayList<Integer>())
                .collect(Collectors.toList());

        NonogramRules rules = createTestNonogramRules_o10017();
        NonogramState state = buildInitialEmptyNonogramState();

        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);
        logic.setNonogramSolutionBoard(new ArrayList<>(board));
        logic.initializeHelpers();

        NonogramRowLogic rowLogic = new NonogramRowLogic(logic, logic.getBoardAccessHelper(), logic.getActionScheduler());
        rowLogic.setNonogramSolutionBoard(new ArrayList<>(board));
        rowLogic.setRowsSequencesRanges(new ArrayList<>(rowSequencesRanges));
        rowLogic.setRowsFieldsNotToInclude(rowsFieldsNotToInclude);
        rowLogic.setNonogramRules(rules);
        rowLogic.setNonogramState(state);

        RowXPlacementHelperImpl xPlacementHelper = new RowXPlacementHelperImpl(rowLogic);

        // when
        xPlacementHelper.placeXsAroundLongestSequencesInRow(rowIdx);

        // then
        List<String> expected = board.get(rowIdx);
        assertThat(rowLogic.getBoardAccessHelper().getRowCopy(rowIdx)).isEqualTo(expected);
    }

    @DisplayName("Should not place any Xs around longest sequences in row – o10017 row 6")
    @Test
    void shouldNotPlaceXsAroundLongestSequencesInRow_o10017_row6() {
        // given
        List<List<String>> board = List.of(
                new ArrayList<>(List.of("X", "X", "X", "X", "X", "-", "O", "O", "O", "-", "-", "-", "X", "X", "X")),
                new ArrayList<>(List.of("X", "X", "X", "-", "X", "O", "O", "X", "O", "O", "X", "-", "-", "X", "-")),
                new ArrayList<>(List.of("X", "X", "X", "-", "O", "-", "O", "-", "X", "-", "-", "-", "-", "X", "-")),
                new ArrayList<>(List.of("X", "X", "X", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "X", "-")),
                new ArrayList<>(List.of("X", "X", "X", "O", "O", "O", "X", "O", "O", "O", "O", "X", "X", "X", "X")),
                new ArrayList<>(List.of("-", "O", "-", "-", "O", "O", "O", "O", "O", "-", "-", "-", "-", "X", "X")),
                new ArrayList<>(List.of("O", "O", "-", "-", "X", "O", "O", "O", "O", "X", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "O", "-", "-", "X", "O", "O", "O", "O", "X", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "O", "-", "-", "X", "O", "O", "O", "O", "X", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("X", "O", "O", "O", "O", "O", "X", "O", "O", "O", "X", "X", "X", "X", "X")),
                new ArrayList<>(List.of("X", "X", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "X", "X", "X")),
                new ArrayList<>(List.of("-", "X", "-", "-", "X", "O", "O", "O", "O", "O", "-", "-", "-", "-", "X")),
                new ArrayList<>(List.of("-", "X", "-", "-", "-", "-", "O", "O", "O", "X", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "X", "-", "-", "-", "-", "X", "O", "O", "O", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("X", "X", "X", "-", "-", "-", "X", "-", "O", "O", "-", "-", "-", "-", "X"))
        );

        int rowIdx = 6;
        List<List<List<Integer>>> rowSequencesRanges = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            rowSequencesRanges.add(new ArrayList<>());
        }
        rowSequencesRanges.set(6, new ArrayList<>(List.of(
                new ArrayList<>(List.of(0, 2)),
                new ArrayList<>(List.of(3, 9))
        )));

        List<List<Integer>> rowsFieldsNotToInclude = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            rowsFieldsNotToInclude.add(new ArrayList<>());
        }

        NonogramRules nonogramRules = createTestNonogramRules_o10017();
        NonogramState state = buildInitialEmptyNonogramState();

        NonogramLogic nonogramLogic = new NonogramLogic(nonogramRules, GuessMode.DISABLED);
        nonogramLogic.setNonogramSolutionBoard(board);
        nonogramLogic.initializeHelpers();

        NonogramRowLogic nonogramRowLogic = new NonogramRowLogic(
                nonogramLogic,
                nonogramLogic.getBoardAccessHelper(),
                nonogramLogic.getActionScheduler()
        );
        nonogramRowLogic.setNonogramSolutionBoard(board);
        nonogramRowLogic.setRowsSequencesRanges(rowSequencesRanges);
        nonogramRowLogic.setRowsFieldsNotToInclude(rowsFieldsNotToInclude);
        nonogramRowLogic.setNonogramRules(nonogramRules);
        nonogramRowLogic.setNonogramState(state);

        RowXPlacementHelperImpl rowXPlacementHelper = new RowXPlacementHelperImpl(nonogramRowLogic);

        List<String> rowBefore = new ArrayList<>(nonogramRowLogic.getBoardAccessHelper().getRowCopy(rowIdx));
        int initialSteps = state.getNewStepsMade();
        int initialActions = nonogramRowLogic.getActionScheduler().getActionsToDoList().size();

        // when
        rowXPlacementHelper.placeXsAroundLongestSequencesInRow(rowIdx);

        // then
        List<String> rowAfter = nonogramRowLogic.getBoardAccessHelper().getRowCopy(rowIdx);
        assertThat(rowAfter).isEqualTo(rowBefore);
        assertThat(state.getNewStepsMade()).isEqualTo(initialSteps + 4); // excluding 4 fields between Xs
        assertThat(nonogramRowLogic.getActionScheduler().getActionsToDoList()).hasSize(initialActions);
    }

    @DisplayName("Should place X before longest sequence in row – o07942 row 16")
    @Test
    void shouldPlaceXsBeforeLongestSequenceInRow_o07942_row16() {
        // given
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "-", "X", "O", "O", "O", "O", "X", "O", "O", "O", "O", "X", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "X")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "X", "O", "X", "-", "-", "-", "-", "-", "O", "O", "O", "-")),
                new ArrayList<>(List.of("X", "X", "X", "X", "X", "X", "X", "-", "O", "O", "-", "X", "X", "X", "X", "-", "O", "O", "O", "-")),
                new ArrayList<>(List.of("X", "-", "-", "-", "-", "-", "-", "O", "O", "O", "-", "-", "X", "-", "-", "X", "O", "X", "O", "X")),
                new ArrayList<>(List.of("X", "X", "-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-", "-", "X", "O", "X", "O", "X")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "-", "O", "-", "-", "-", "O", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "X", "O", "O", "O", "O", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "X", "O", "O", "O", "O", "O", "O", "O", "X", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "O", "-", "O", "O", "O", "O", "O", "-", "-", "-", "O", "O", "O", "O")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "O", "-", "O", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("X", "X", "X", "-", "-", "-", "O", "-", "O", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-"))
        ));

        int rowIdx = 16;
        List<List<List<Integer>>> rowSequencesRanges = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            rowSequencesRanges.add(new ArrayList<>());
        }
        rowSequencesRanges.set(16, new ArrayList<>(List.of(
                new ArrayList<>(List.of(0, 6)),
                new ArrayList<>(List.of(6, 14)),
                new ArrayList<>(List.of(16, 19))
        )));

        List<List<Integer>> rowsFieldsNotToInclude = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            rowsFieldsNotToInclude.add(new ArrayList<>());
        }

        NonogramRules nonogramRules = createTestNonogramRules_o07942();
        NonogramState state = buildInitialEmptyNonogramState();

        NonogramLogic nonogramLogic = new NonogramLogic(nonogramRules, GuessMode.DISABLED);
        nonogramLogic.setNonogramSolutionBoard(board);
        nonogramLogic.initializeHelpers();

        NonogramRowLogic nonogramRowLogic = new NonogramRowLogic(
                nonogramLogic,
                nonogramLogic.getBoardAccessHelper(),
                nonogramLogic.getActionScheduler()
        );
        nonogramRowLogic.setNonogramSolutionBoard(board);
        nonogramRowLogic.setRowsSequencesRanges(rowSequencesRanges);
        nonogramRowLogic.setRowsFieldsNotToInclude(rowsFieldsNotToInclude);
        nonogramRowLogic.setNonogramRules(nonogramRules);
        nonogramRowLogic.setNonogramState(state);

        RowXPlacementHelperImpl rowXPlacementHelper = new RowXPlacementHelperImpl(nonogramRowLogic);

        int initialSteps = state.getNewStepsMade();
        int initialActions = nonogramRowLogic.getActionScheduler().getActionsToDoList().size();

        // when
        rowXPlacementHelper.placeXsAroundLongestSequencesInRow(rowIdx);

        // then
        List<String> rowAfter = nonogramRowLogic.getBoardAccessHelper().getRowCopy(rowIdx);
        assertThat(rowAfter.get(15)).isEqualTo("X");
        int xPlacedCount = 1;
        int fieldsExcudedCount = 4;
        assertThat(state.getNewStepsMade()).isEqualTo(initialSteps + xPlacedCount + fieldsExcudedCount);
        assertThat(nonogramRowLogic.getActionScheduler().getActionsToDoList()).hasSize(initialActions + 9); // 9 actions for one X placed before
    }

    @DisplayName("Should handle case where coloured range is inside allowed range but too long – o06147 row 2")
    @Test
    void shouldNotMatchSequenceIfColouredRangeIsTooLong_o06147_row2() {
        // given
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "X", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "-", "-", "O", "-")),
                new ArrayList<>(List.of("-", "-", "-", "X", "-", "-", "X", "O", "O", "O", "-", "-", "-", "O", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "-", "X", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("-", "-", "-", "X", "-", "O", "O", "-", "X", "O", "O", "O", "O", "O", "-")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "X", "X", "-", "O", "O", "O", "O", "O", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "O", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "X", "X", "O", "X", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"))
        ));

        int rowIdx = 2;
        List<List<List<Integer>>> rowSequencesRanges = new ArrayList<>();
        for (int i = 0; i < board.size(); i++) {
            rowSequencesRanges.add(new ArrayList<>());
        }
        rowSequencesRanges.set(rowIdx, new ArrayList<>(List.of(
                new ArrayList<>(List.of(0, 9)),
                new ArrayList<>(List.of(3, 11)),
                new ArrayList<>(List.of(8, 14))
        )));

        List<List<Integer>> rowsFieldsNotToInclude = new ArrayList<>();
        for (int i = 0; i < board.size(); i++) {
            rowsFieldsNotToInclude.add(new ArrayList<>());
        }

        NonogramRules nonogramRules = createTestNonogramRules_o06147();

        NonogramState state = buildInitialEmptyNonogramState();

        NonogramLogic logic = new NonogramLogic(nonogramRules, GuessMode.DISABLED);
        logic.setNonogramSolutionBoard(board);
        logic.initializeHelpers();

        NonogramRowLogic rowLogic = new NonogramRowLogic(
                logic,
                logic.getBoardAccessHelper(),
                logic.getActionScheduler()
        );
        rowLogic.setNonogramSolutionBoard(board);
        rowLogic.setRowsSequencesRanges(rowSequencesRanges);
        rowLogic.setRowsFieldsNotToInclude(rowsFieldsNotToInclude);
        rowLogic.setNonogramRules(nonogramRules);
        rowLogic.setNonogramState(state);

        RowXPlacementHelperImpl rowXPlacementHelper = new RowXPlacementHelperImpl(rowLogic);

        int initialSteps = state.getNewStepsMade();
        int initialActions = rowLogic.getActionScheduler().getActionsToDoList().size();

        // when
        rowXPlacementHelper.placeXsAroundLongestSequencesInRow(rowIdx);

        // then
        List<String> rowAfter = rowLogic.getBoardAccessHelper().getRowCopy(rowIdx);
        assertThat(rowAfter.get(7)).isEqualTo("X");
        assertThat(rowAfter.get(10)).isEqualTo("X");
        assertThat(state.getNewStepsMade()).isEqualTo(initialSteps + 2);
        assertThat(rowLogic.getActionScheduler().getActionsToDoList()).hasSize(initialActions + 18);  // 2 x 9 for every X placed
    }

    @DisplayName("Should place X in range too short for any sequence – o07836 row 3")
    @Test
    void shouldPlaceXsAtTooShortEmptySequence_o07836_row3() {
        // given
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "-", "X", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "-", "O", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("X", "X", "X", "O", "O", "O", "O", "O", "X", "X")),
                new ArrayList<>(List.of("X", "-", "X", "O", "O", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "-", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "-", "X", "O", "O", "X", "-")),
                new ArrayList<>(List.of("-", "-", "X", "O", "X", "X", "O", "O", "X", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "X", "-", "-", "-", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "-", "-", "-", "-", "-"))
        ));

        int rowIdx = 3;

        List<List<List<Integer>>> rowSequencesRanges = new ArrayList<>();
        for (int i = 0; i < board.size(); i++) {
            rowSequencesRanges.add(new ArrayList<>());
        }
        rowSequencesRanges.set(rowIdx, new ArrayList<>(List.of(
                new ArrayList<>(List.of(1, 6)),
                new ArrayList<>(List.of(6, 9))
        )));

        List<List<Integer>> rowsFieldsNotToInclude = new ArrayList<>();
        for (int i = 0; i < board.size(); i++) {
            rowsFieldsNotToInclude.add(new ArrayList<>());
        }

        NonogramRules nonogramRules = createTestNonogramRules_o07836();

        NonogramState state = buildInitialEmptyNonogramState();

        NonogramLogic logic = new NonogramLogic(nonogramRules, GuessMode.DISABLED);
        logic.setNonogramSolutionBoard(board);
        logic.initializeHelpers();

        NonogramRowLogic rowLogic = new NonogramRowLogic(
                logic,
                logic.getBoardAccessHelper(),
                logic.getActionScheduler()
        );
        rowLogic.setNonogramSolutionBoard(board);
        rowLogic.setRowsSequencesRanges(rowSequencesRanges);
        rowLogic.setRowsFieldsNotToInclude(rowsFieldsNotToInclude);
        rowLogic.setNonogramRules(nonogramRules);
        rowLogic.setNonogramState(state);

        RowXPlacementHelperImpl helper = new RowXPlacementHelperImpl(rowLogic);

        int initialSteps = state.getNewStepsMade();
        int initialActions = rowLogic.getActionScheduler().getActionsToDoList().size();

        // when
        helper.placeXsRowAtTooShortEmptySequences(rowIdx);

        // then
        List<String> rowAfter = rowLogic.getBoardAccessHelper().getRowCopy(rowIdx);
        assertThat(rowAfter.get(1)).isEqualTo("X");
        assertThat(state.getNewStepsMade()).isEqualTo(initialSteps + 1);
        assertThat(rowLogic.getActionScheduler().getActionsToDoList()).hasSize(initialActions + 2); // 2 actions for one X placed
    }

    @DisplayName("Should not place Xs in short range if not all fitting sequences are too long – o06005 row 0")
    @Test
    void shouldNotPlaceXsIfNotAllFittingSequencesAreTooLong_o06005_row0() {
        // given
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "O", "X", "-", "X", "O", "X", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "X", "-", "X", "O", "X", "-", "-")),
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "O", "O", "X", "-", "-")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "X", "O", "X")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X")),
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O")),
                new ArrayList<>(List.of("-", "-", "X", "X", "-", "X", "O", "X", "-", "X")),
                new ArrayList<>(List.of("-", "-", "O", "X", "-", "X", "O", "O", "-", "-"))
        ));

        int rowIdx = 0;

        List<List<List<Integer>>> rowSequencesRanges = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            rowSequencesRanges.add(new ArrayList<>());
        }
        rowSequencesRanges.set(rowIdx, new ArrayList<>(List.of(
                new ArrayList<>(List.of(0, 2)),
                new ArrayList<>(List.of(2, 6)),
                new ArrayList<>(List.of(6, 9))
        )));

        List<List<Integer>> rowsFieldsNotToInclude = new ArrayList<>();
        List<List<Integer>> rowsSequencesIdsNotToInclude = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            rowsFieldsNotToInclude.add(new ArrayList<>());
            rowsSequencesIdsNotToInclude.add(new ArrayList<>());
        }

        NonogramRules rules = createTestNonogramRules_o06005();
        NonogramState state = buildInitialEmptyNonogramState();

        NonogramLogic logic = new NonogramLogic(rules, GuessMode.DISABLED);
        logic.setNonogramSolutionBoard(board);
        logic.initializeHelpers();

        NonogramRowLogic rowLogic = new NonogramRowLogic(
                logic,
                logic.getBoardAccessHelper(),
                logic.getActionScheduler()
        );
        rowLogic.setNonogramSolutionBoard(board);
        rowLogic.setRowsSequencesRanges(rowSequencesRanges);
        rowLogic.setRowsFieldsNotToInclude(rowsFieldsNotToInclude);
        rowLogic.setRowsSequencesIdsNotToInclude(rowsSequencesIdsNotToInclude);
        rowLogic.setNonogramRules(rules);
        rowLogic.setNonogramState(state);

        RowXPlacementHelperImpl rowXPlacementHelper = new RowXPlacementHelperImpl(rowLogic);

        int initialSteps = state.getNewStepsMade();
        int initialActions = rowLogic.getActionScheduler().getActionsToDoList().size();
        List<String> rowBefore = rowLogic.getBoardAccessHelper().getRowCopy(rowIdx);

        // when
        rowXPlacementHelper.placeXsRowAtTooShortEmptySequences(rowIdx);

        // then
        List<String> rowAfter = rowLogic.getBoardAccessHelper().getRowCopy(rowIdx);
        assertThat(rowAfter).isEqualTo(rowBefore);
        assertThat(state.getNewStepsMade()).isEqualTo(initialSteps);
        assertThat(rowLogic.getActionScheduler().getActionsToDoList()).hasSize(initialActions);
    }

    private static NonogramRules createTestNonogramRules_placingXAtUnreachableFields_o10683() {
        List<List<Integer>> rowSequencesLengths = List.of(
                List.of(5),
                List.of(9),
                List.of(3, 3),
                List.of(2, 3, 3),
                List.of(1, 6, 2),
                List.of(2, 3, 4),
                List.of(5, 4, 2),
                List.of(6, 4, 3),
                List.of(6, 4),
                List.of(4, 6),
                List.of(6, 4),
                List.of(4, 1, 6),
                List.of(2, 3, 9),
                List.of(5),
                List.of(5)
        );
        List<List<Integer>> columnSequencesLengths = List.of(
                List.of(5),
                List.of(9),
                List.of(7),
                List.of(9),
                List.of(1, 2, 2, 2, 2),
                List.of(2, 1, 1, 8),
                List.of(2, 3, 7),
                List.of(2, 2),
                List.of(2, 2, 2),
                List.of(3, 3),
                List.of(3),
                List.of(2, 3),
                List.of(3, 3),
                List.of(2),
                List.of(2)
        );
        return new NonogramRules(rowSequencesLengths, columnSequencesLengths,
                rowSequencesLengths.size(),
                columnSequencesLengths.size());
    }

    private static NonogramRules createTestNonogramRules_o10017() {
        List<List<Integer>> rowSequencesLengths = List.of(
                List.of(4),
                List.of(2, 2),
                List.of(4, 2),
                List.of(6, 1),
                List.of(3, 4),
                List.of(2, 6),
                List.of(4, 4),
                List.of(4, 4),
                List.of(4, 4),
                List.of(5, 3),
                List.of(7),
                List.of(9),
                List.of(3, 5),
                List.of(5, 2),
                List.of(6)
        );

        List<List<Integer>> columnSequencesLengths = List.of(
                List.of(3, 5),
                List.of(5),
                List.of(4),
                List.of(2, 4),
                List.of(4, 1, 2),
                List.of(3, 12),
                List.of(3, 12),
                List.of(2, 12),
                List.of(2),
                List.of(5),
                List.of(4, 1),
                List.of(4),
                List.of(1, 4),
                List.of(4),
                List.of(2)
        );

        return new NonogramRules(
                rowSequencesLengths,
                columnSequencesLengths,
                rowSequencesLengths.size(),
                columnSequencesLengths.size()
        );
    }

    private static NonogramRules createTestNonogramRules_o07942() {
        List<List<Integer>> rowSequencesLengths = List.of(
                List.of(2, 2),
                List.of(2, 3, 2),
                List.of(2, 5, 2),
                List.of(2, 2),
                List.of(4, 4),
                List.of(3, 1, 1),
                List.of(1, 1, 1),
                List.of(2, 2, 1),
                List.of(2, 2, 3),
                List.of(1, 1, 1, 4),
                List.of(3, 4),
                List.of(1, 5, 1, 1, 1),
                List.of(11, 1, 1),
                List.of(2, 7, 3, 1),
                List.of(2, 7, 2, 1),
                List.of(2, 7, 1, 1),
                List.of(2, 7, 4),
                List.of(1, 4, 1, 4),
                List.of(4, 1, 4),
                List.of(4, 8)
        );

        List<List<Integer>> columnSequencesLengths = List.of(
                List.of(1),
                List.of(1, 2),
                List.of(2, 2),
                List.of(2, 2),
                List.of(2, 2),
                List.of(2, 2),
                List.of(6, 8),
                List.of(3, 2, 9),
                List.of(5, 10),
                List.of(2, 2, 11),
                List.of(5, 7),
                List.of(3, 2, 6),
                List.of(6, 8),
                List.of(2, 2, 1),
                List.of(2, 2, 1),
                List.of(2, 2, 1),
                List.of(2, 11),
                List.of(1, 3, 4),
                List.of(13),
                List.of(3, 4)
        );

        return new NonogramRules(
                rowSequencesLengths,
                columnSequencesLengths,
                rowSequencesLengths.size(),
                columnSequencesLengths.size()
        );
    }

    private static NonogramRules createTestNonogramRules_o06147() {
        List<List<Integer>> rowSequencesLengths = List.of(
                List.of(5),
                List.of(7),
                List.of(2, 1, 2),
                List.of(7),
                List.of(2, 2),
                List.of(1, 3, 3),
                List.of(2, 7),
                List.of(4, 8),
                List.of(13),
                List.of(2, 4, 5),
                List.of(2, 5),
                List.of(10),
                List.of(7),
                List.of(1),
                List.of(2)
        );

        List<List<Integer>> columnSequencesLengths = List.of(
                List.of(3),
                List.of(4),
                List.of(4),
                List.of(2, 2),
                List.of(2, 2),
                List.of(2, 2),
                List.of(3, 2),
                List.of(4, 4),
                List.of(8, 3, 1),
                List.of(13),
                List.of(2, 1, 8),
                List.of(4, 6),
                List.of(2, 1, 7),
                List.of(10),
                List.of(5)
        );

        return new NonogramRules(
                rowSequencesLengths,
                columnSequencesLengths,
                rowSequencesLengths.size(),
                columnSequencesLengths.size()
        );
    }

    private static NonogramRules createTestNonogramRules_o07836() {
        List<List<Integer>> rowSequencesLengths = List.of(
                List.of(3),
                List.of(2, 3),
                List.of(5),
                List.of(4, 1),
                List.of(4, 1),
                List.of(4, 1),
                List.of(3, 2),
                List.of(1, 2),
                List.of(1, 2),
                List.of(5, 2)
        );

        List<List<Integer>> columnSequencesLengths = List.of(
                List.of(2, 1),
                List.of(2, 1),
                List.of(2, 1),
                List.of(3, 1),
                List.of(5),
                List.of(1, 3),
                List.of(3),
                List.of(4),
                List.of(3),
                List.of(1, 3)
        );

        return new NonogramRules(
                rowSequencesLengths,
                columnSequencesLengths,
                rowSequencesLengths.size(),
                columnSequencesLengths.size()
        );
    }

    private static NonogramRules createTestNonogramRules_o06005() {
        List<List<Integer>> rowSequencesLengths = List.of(
                List.of(1, 1, 1),
                List.of(1, 1, 1),
                List.of(1, 5, 1),
                List.of(9),
                List.of(9),
                List.of(7, 1),
                List.of(9),
                List.of(10),
                List.of(1, 1),
                List.of(2, 2)
        );

        List<List<Integer>> columnSequencesLengths = List.of(
                List.of(2),
                List.of(2),
                List.of(3),
                List.of(5),
                List.of(3),
                List.of(1),
                List.of(3),
                List.of(4),
                List.of(2),
                List.of(3)
        );

        return new NonogramRules(
                rowSequencesLengths,
                columnSequencesLengths,
                rowSequencesLengths.size(),
                columnSequencesLengths.size()
        );
    }
}