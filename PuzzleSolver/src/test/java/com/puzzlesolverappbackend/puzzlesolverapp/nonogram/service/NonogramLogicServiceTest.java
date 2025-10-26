package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogicFactory;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solutions.NonogramSolutionNode;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.config.GuessMode;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.NonogramInitializationRequest;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service.saver.NonogramSolutionSaver;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.utils.NonogramStatsUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.isFieldWithX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NonogramLogicServiceTest {

    private NonogramLogicService subject;

    @Mock
    private NonogramSolutionSaver nonogramSolutionSaver;

    @Mock
    private NonogramLogicFactory nonogramLogicFactory;

    @BeforeEach
    void setUp() {
        subject = new NonogramLogicService(nonogramSolutionSaver, nonogramLogicFactory);
    }

    @Test
    @DisplayName("constructor should correctly assign dependencies via reflection")
    void constructorShouldCorrectlyAssignDependencies() throws Exception {

        // when & then
        // Access private fields via reflection
        java.lang.reflect.Field saverField = NonogramLogicService.class.getDeclaredField("nonogramSolutionSaver");
        saverField.setAccessible(true);
        Object actualSaver = saverField.get(subject);

        java.lang.reflect.Field factoryField = NonogramLogicService.class.getDeclaredField("logicFactory");
        factoryField.setAccessible(true);
        Object actualFactory = factoryField.get(subject);

        // Verify dependencies are correctly assigned
        assertThat(actualSaver).isSameAs(nonogramSolutionSaver);
        assertThat(actualFactory).isSameAs(nonogramLogicFactory);
    }

    @Test
    @DisplayName("initializeLogicFromRequest should correctly create NonogramLogic from request data")
    void initializeLogicFromRequest_shouldCreateNonogramLogic() {
        // given
        NonogramInitializationRequest request = new NonogramInitializationRequest();
        request.setRowSequences(List.of(
                List.of(3, 1),
                List.of(2, 2),
                List.of(5)
        ));
        request.setColumnSequences(List.of(
                List.of(1, 2),
                List.of(3),
                List.of(2, 1)
        ));

        request.setHeight(3);
        request.setWidth(3);

        // when
        NonogramLogic logic = subject.initializeLogicFromRequest(request);

        // then
        assertThat(logic).isNotNull();

        NonogramRules rules = logic.getNonogramRules();
        assertThat(rules.getRowSequencesLengths()).isEqualTo(request.getRowSequences());
        assertThat(rules.getColumnSequencesLengths()).isEqualTo(request.getColumnSequences());
        assertThat(rules.getHeight()).isEqualTo(request.getHeight());
        assertThat(rules.getWidth()).isEqualTo(request.getWidth());
    }

    @DisplayName("fillOverlappingFieldsInColumnsRange - should fill overlapping fields in columns 0 and 1 only")
    @Test
    void shouldFillOverlappingFieldsInColumns0and1and2Only() {
        // given
        NonogramLogic logic = create_o08311_logic();

        // when
        subject.fillOverlappingFieldsInColumnsRange(logic, 0, 2);

        // then
        // columns [0, 2]
        assertThat(logic.getBoardAccessHelper().isColumnRangeColoured(0, List.of(2, 9))).isTrue();
        assertThat(logic.getBoardAccessHelper().isColumnRangeColoured(0, List.of(13, 17))).isTrue();
        assertThat(logic.getBoardAccessHelper().isColumnRangeColoured(1, List.of(5, 8))).isTrue();
        assertThat(logic.getBoardAccessHelper().isColumnRangeColoured(1, List.of(14, 15))).isTrue();
        assertThat(logic.getBoardAccessHelper().isColumnRangeColoured(2, List.of(3, 8))).isTrue();
        assertThat(logic.getBoardAccessHelper().isColumnRangeColoured(2, List.of(15, 16))).isTrue();

        // columns [3, 19]
        assertThat(logic.getBoardAccessHelper().isColumnRangeColoured(3, List.of(3, 3))).isFalse();
        assertThat(logic.getBoardAccessHelper().isColumnRangeColoured(3, List.of(15, 16))).isFalse();
    }

    @DisplayName("fillOverlappingFieldsInRowsRange - should fill overlapping fields in rows 16-18 only")
    @Test
    void shouldFillOverlappingFieldsInRows16and17and18Only() {
        // given
        NonogramLogic logic = create_o08311_logic();

        // when
        subject.fillOverLappingFieldsInRowsRange(logic, 16, 18);

        // then
        // rows [16, 18]
        assertThat(logic.getBoardAccessHelper().isRowRangeColoured(16, List.of(0, 6))).isTrue();
        assertThat(logic.getBoardAccessHelper().isRowRangeColoured(16, List.of(8, 10))).isTrue();
        assertThat(logic.getBoardAccessHelper().isRowRangeColoured(16, List.of(12, 14))).isTrue();
        assertThat(logic.getBoardAccessHelper().isRowRangeColoured(17, List.of(2, 6))).isTrue();
        assertThat(logic.getBoardAccessHelper().isRowRangeColoured(17, List.of(12, 12))).isTrue();
        assertThat(logic.getBoardAccessHelper().isRowRangeColoured(18, List.of(3, 7))).isTrue();

        // rows [0, 15] & [19, 19]
        assertThat(logic.getBoardAccessHelper().isRowRangeColoured(15, List.of(3, 3))).isFalse();
        assertThat(logic.getBoardAccessHelper().isRowRangeColoured(15, List.of(8, 8))).isFalse();
    }

    @DisplayName("markAvailableSequencesInRows - should mark fields in rows 16, 17 only")
    @Test // TODO - integration -> change to unit
    void shouldMarkAvailableSequencesInRow16and17Only() {
        // given
        NonogramLogic logic = create_o08311_logic();

        // when
        subject.fillOverLappingFieldsInRowsRange(logic,16, 18);
        subject.markAvailableSequencesInRows(logic, 16, 17);

        // then
        // rows [16, 17]
        assertThat(logic.getNonogramSolutionBoardWithMarks().get(16).get(0)).isEqualTo("Ra--");
        assertThat(logic.getNonogramSolutionBoardWithMarks().get(16).get(1)).isEqualTo("Ra--");
        assertThat(logic.getNonogramSolutionBoardWithMarks().get(16).get(2)).isEqualTo("Ra--");
        assertThat(logic.getNonogramSolutionBoardWithMarks().get(16).get(3)).isEqualTo("Ra--");
        assertThat(logic.getNonogramSolutionBoardWithMarks().get(16).get(4)).isEqualTo("Ra--");
        assertThat(logic.getNonogramSolutionBoardWithMarks().get(16).get(5)).isEqualTo("Ra--");
        assertThat(logic.getNonogramSolutionBoardWithMarks().get(16).get(6)).isEqualTo("Ra--");

        assertThat(logic.getNonogramSolutionBoardWithMarks().get(16).get(8)).isEqualTo("Rb--");
        assertThat(logic.getNonogramSolutionBoardWithMarks().get(16).get(9)).isEqualTo("Rb--");
        assertThat(logic.getNonogramSolutionBoardWithMarks().get(16).get(10)).isEqualTo("Rb--");

        assertThat(logic.getNonogramSolutionBoardWithMarks().get(16).get(12)).isEqualTo("Rc--");
        assertThat(logic.getNonogramSolutionBoardWithMarks().get(16).get(13)).isEqualTo("Rc--");
        assertThat(logic.getNonogramSolutionBoardWithMarks().get(16).get(14)).isEqualTo("Rc--");

        assertThat(logic.getNonogramSolutionBoardWithMarks().get(17).get(2)).isEqualTo("Ra--");
        assertThat(logic.getNonogramSolutionBoardWithMarks().get(17).get(3)).isEqualTo("Ra--");
        assertThat(logic.getNonogramSolutionBoardWithMarks().get(17).get(4)).isEqualTo("Ra--");
        assertThat(logic.getNonogramSolutionBoardWithMarks().get(17).get(5)).isEqualTo("Ra--");
        assertThat(logic.getNonogramSolutionBoardWithMarks().get(17).get(6)).isEqualTo("Ra--");

        assertThat(logic.getNonogramSolutionBoardWithMarks().get(17).get(12)).isEqualTo("Rc--");

        // row 18 (coloured, but not marked)
        assertThat(logic.getNonogramSolutionBoardWithMarks().get(18).get(3)).isEqualTo("R---");
        assertThat(logic.getNonogramSolutionBoardWithMarks().get(18).get(4)).isEqualTo("R---");
        assertThat(logic.getNonogramSolutionBoardWithMarks().get(18).get(5)).isEqualTo("R---");
        assertThat(logic.getNonogramSolutionBoardWithMarks().get(18).get(6)).isEqualTo("R---");
        assertThat(logic.getNonogramSolutionBoardWithMarks().get(18).get(7)).isEqualTo("R---");
    }

    @DisplayName("markAvailableSequencesInColumns - should mark fields in columns 0, 1 only")
    @Test // TODO - integration -> change to unit
    void shouldMarkAvailableSequencesInColumns0and1Only() {
        // given
        NonogramLogic logic = create_o08311_logic();

        // when
        subject.fillOverlappingFieldsInColumnsRange(logic,0, 2);
        subject.markAvailableSequencesInColumns(logic, 0, 1);

        // then
        // columns [0, 1]
        int[] rowsCaCol0 = {2,3,4,5,6,7,8,9};
        for (int r : rowsCaCol0) {
            assertThat(logic.getNonogramSolutionBoardWithMarks().get(r).get(0)).isEqualTo("--Ca");
        }

        int[] rowsCbCol0 = {13,14,15,16,17};
        for (int r : rowsCbCol0) {
            assertThat(logic.getNonogramSolutionBoardWithMarks().get(r).get(0)).isEqualTo("--Cb");
        }

        int[] rowsCaCol1 = {4,5,6,7,8};
        for (int r : rowsCaCol1) {
            assertThat(logic.getNonogramSolutionBoardWithMarks().get(r).get(1)).isEqualTo("--Ca");
        }

        int[] rowsCbCol1 = {14,15};
        for (int r : rowsCbCol1) {
            assertThat(logic.getNonogramSolutionBoardWithMarks().get(r).get(1)).isEqualTo("--Cb");
        }

        // column 2 (coloured, but not marked)
        int[] rowsCol2 = {3,4,5,6,7,8,15,16};
        for (int r : rowsCol2) {
            assertThat(logic.getNonogramSolutionBoardWithMarks().get(r).get(2)).isEqualTo("--C-");
        }
    }

    @DisplayName("placeXsAroundLongestSequencesInRowsRange - should place X around longest sequences in rows [11, 12]")
    @Test
    void shouldPlaceXAroundLongestSequencesInRowsRange() {
        // given
        NonogramLogic logic = create_o08311_logic();
        logic.colourFieldAtGivenPosition(new Field(11, 3));
        logic.colourFieldAtGivenPosition(new Field(11, 4));
        logic.colourFieldAtGivenPosition(new Field(11, 5));
        logic.colourFieldAtGivenPosition(new Field(11, 6));

        logic.colourFieldAtGivenPosition(new Field(11, 12));
        logic.colourFieldAtGivenPosition(new Field(11, 13));

        logic.colourFieldAtGivenPosition(new Field(12, 2));
        logic.colourFieldAtGivenPosition(new Field(12, 3));
        logic.colourFieldAtGivenPosition(new Field(12, 4));
        logic.colourFieldAtGivenPosition(new Field(12, 5));
        logic.colourFieldAtGivenPosition(new Field(12, 6));
        logic.colourFieldAtGivenPosition(new Field(12, 7));
        logic.colourFieldAtGivenPosition(new Field(12, 8));

        // when
        subject.placeXsAroundLongestSequencesInRowsRange(logic, 11, 12);

        // then
        assertThat(isFieldWithX(logic.getNonogramSolutionBoard(), new Field(11, 2))).isTrue();
        assertThat(isFieldWithX(logic.getNonogramSolutionBoard(), new Field(11, 7))).isTrue();
        assertThat(isFieldWithX(logic.getNonogramSolutionBoard(), new Field(11, 11))).isTrue();
        assertThat(isFieldWithX(logic.getNonogramSolutionBoard(), new Field(11, 14))).isTrue();

        assertThat(isFieldWithX(logic.getNonogramSolutionBoard(), new Field(12, 1))).isTrue();
        assertThat(isFieldWithX(logic.getNonogramSolutionBoard(), new Field(12, 9))).isTrue();
    }

    @DisplayName("placeXsAroundLongestSequencesInColumnsRange - should place X around longest sequences in columns [5, 6]")
    @Test
    void shouldPlaceXAroundLongestSequencesInColumnsRange() {
        // given
        NonogramLogic logic = create_o08311_logic();
        logic.colourFieldAtGivenPosition(new Field(0, 5));
        logic.colourFieldAtGivenPosition(new Field(1, 5));
        logic.colourFieldAtGivenPosition(new Field(5, 5));
        logic.colourFieldAtGivenPosition(new Field(16, 5));
        logic.colourFieldAtGivenPosition(new Field(17, 5));
        logic.colourFieldAtGivenPosition(new Field(18, 5));
        logic.colourFieldAtGivenPosition(new Field(19, 5));

        logic.colourFieldAtGivenPosition(new Field(0, 6));
        logic.colourFieldAtGivenPosition(new Field(5, 6));
        logic.colourFieldAtGivenPosition(new Field(8, 6));
        logic.colourFieldAtGivenPosition(new Field(15, 6));
        logic.colourFieldAtGivenPosition(new Field(16, 6));
        logic.colourFieldAtGivenPosition(new Field(17, 6));
        logic.colourFieldAtGivenPosition(new Field(18, 6));
        logic.colourFieldAtGivenPosition(new Field(19, 6));

        // when
        subject.placeXsAroundLongestSequencesInColumnsRange(logic, 5, 6);

        // then
        assertThat(isFieldWithX(logic.getNonogramSolutionBoard(), new Field(2, 5))).isTrue();
        assertThat(isFieldWithX(logic.getNonogramSolutionBoard(), new Field(4, 5))).isTrue();
        assertThat(isFieldWithX(logic.getNonogramSolutionBoard(), new Field(6, 5))).isTrue();
        assertThat(isFieldWithX(logic.getNonogramSolutionBoard(), new Field(15, 5))).isTrue();

        assertThat(isFieldWithX(logic.getNonogramSolutionBoard(), new Field(1, 6))).isTrue();
        assertThat(isFieldWithX(logic.getNonogramSolutionBoard(), new Field(14, 6))).isTrue();
    }

    @DisplayName("placeXsAtUnreachableFieldsInRowsRange - should place Xs correctly in given row range (1–2)")
    @Test
    void shouldPlaceXsAtUnreachableFieldsInRowsRange() {
        // given
        NonogramLogic logic = create_o06005_logic();
        List<List<Integer>> row1Ranges = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(0, 0)),
                new ArrayList<>(Arrays.asList(2, 2)),
                new ArrayList<>(Arrays.asList(4, 4)),
                new ArrayList<>(Arrays.asList(6, 6)),
                new ArrayList<>(Arrays.asList(8, 9))
        ));
        List<List<Integer>> row2Ranges = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(0, 0)),
                new ArrayList<>(Arrays.asList(2, 6)),
                new ArrayList<>(Arrays.asList(8, 9))
        ));
        logic.getNonogramRowLogic().setRowsSequencesRanges(
                new ArrayList<>(Arrays.asList(
                        new ArrayList<>(),
                        row1Ranges,
                        row2Ranges,
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>()
                ))
        );

        // --- expected before & after from your screenshots ---
        List<String> expectedInitialRow1 = List.of("O", "-", "O", "X", "O", "X", "O", "X", "-", "X");
        List<String> expectedFinalRow1   = List.of("O", "X", "O", "X", "O", "X", "O", "X", "-", "X");

        List<String> expectedInitialRow2 = List.of("O", "-", "O", "O", "O", "O", "O", "X", "-", "X");
        List<String> expectedFinalRow2   = List.of("O", "X", "O", "O", "O", "O", "O", "X", "-", "X");

        // simulate that these rows are already on board before operation
        logic.getNonogramRowLogic().getNonogramSolutionBoard().set(1, new ArrayList<>(expectedInitialRow1));
        logic.getNonogramRowLogic().getNonogramSolutionBoard().set(2, new ArrayList<>(expectedInitialRow2));

        // when
        subject.placeXsAtUnreachableFieldsInRowsRange(logic, 1, 2);

        // then
        List<String> updatedRow1 = logic.getNonogramBoardRow(1);
        List<String> updatedRow2 = logic.getNonogramBoardRow(2);

        assertEquals(expectedFinalRow1, updatedRow1, "Row 1 should have X placed in correct unreachable fields");
        assertEquals(expectedFinalRow2, updatedRow2, "Row 2 should have X placed in correct unreachable fields");
    }

    @DisplayName("placeXsAtUnreachableFieldsInColumnsRange - should place Xs correctly in given column range (0–1)")
    @Test
    void shouldPlaceXsAtUnreachableFieldsInColumnsRange() {
        // given
        NonogramLogic logic = create_o06005_logic();
        List<List<Integer>> column0Ranges = new ArrayList<>(List.of(
                new ArrayList<>(Arrays.asList(1, 9))
        ));
        List<List<Integer>> column1Ranges = new ArrayList<>(List.of(
                new ArrayList<>(Arrays.asList(1, 9))
        ));
        logic.getNonogramColumnLogic().setColumnsSequencesRanges(
                new ArrayList<>(Arrays.asList(
                        column0Ranges,
                        column1Ranges,
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>()
                ))
        );

        // --- expected before & after from your screenshots ---
        List<String> expectedInitialColumn0 = List.of("-", "-", "-", "O", "O", "O", "O", "O", "-", "-");
        List<String> expectedFinalColumn0   = List.of("X", "-", "-", "O", "O", "O", "O", "O", "-", "-");

        List<String> expectedInitialColumn1 = List.of("-", "-", "-", "O", "O", "O", "O", "O", "-", "-");
        List<String> expectedFinalColumn1   = List.of("X", "-", "-", "O", "O", "O", "O", "O", "-", "-");

        // simulate that these columns are already on board before operation
        List<List<String>> board = logic.getNonogramColumnLogic().getNonogramSolutionBoard();
        for (int row = 0; row < expectedInitialColumn0.size(); row++) {
            board.get(row).set(0, expectedInitialColumn0.get(row));
            board.get(row).set(1, expectedInitialColumn1.get(row));
        }

        // when
        subject.placeXsAtUnreachableFieldsInColumnsRange(logic, 0, 1);

        // then
        List<String> updatedColumn0 = logic.getNonogramColumnLogic().getNonogramBoardColumn(0);
        List<String> updatedColumn1 = logic.getNonogramColumnLogic().getNonogramBoardColumn(1);

        assertEquals(expectedFinalColumn0, updatedColumn0, "Column 0 should have X placed in correct unreachable fields");
        assertEquals(expectedFinalColumn1, updatedColumn1, "Column 1 should have X placed in correct unreachable fields");
    }

    @DisplayName("correctRowsSequencesRanges - should correct rows sequences ranges (1–2)")
    @Test
    void shouldCorrectRowsSequencesRangesInRowsRange() {
        // given
        NonogramLogic logic = create_o06005_logic();
        List<List<Integer>> row1Ranges = new ArrayList<>(List.of(
                new ArrayList<>(Arrays.asList(0, 1)),
                new ArrayList<>(Arrays.asList(2, 2)),
                new ArrayList<>(Arrays.asList(4, 5)),
                new ArrayList<>(Arrays.asList(6, 6)),
                new ArrayList<>(Arrays.asList(8, 9))
        ));
        List<List<Integer>> row2Ranges = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(0, 1)),
                new ArrayList<>(Arrays.asList(2, 6)),
                new ArrayList<>(Arrays.asList(8, 9))
        ));
        logic.getNonogramRowLogic().setRowsSequencesRanges(
                new ArrayList<>(Arrays.asList(
                        new ArrayList<>(),
                        row1Ranges,
                        row2Ranges,
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>()
                ))
        );

        // when
        subject.correctRowsSequencesRanges(logic, 1, 2);

        // then
        List<List<Integer>> expectedFinalRow1Ranges = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(0, 0)),
                new ArrayList<>(Arrays.asList(2, 2)),
                new ArrayList<>(Arrays.asList(4, 4)),
                new ArrayList<>(Arrays.asList(6, 6)),
                new ArrayList<>(Arrays.asList(8, 9))
        ));
        List<List<Integer>> expectedFinalRow2Ranges = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(0, 0)),
                new ArrayList<>(Arrays.asList(2, 6)),
                new ArrayList<>(Arrays.asList(8, 9))
        ));
        assertThat(logic.getNonogramRowLogic().getRowsSequencesRanges().get(1)).isEqualTo(expectedFinalRow1Ranges);
        assertThat(logic.getNonogramRowLogic().getRowsSequencesRanges().get(2)).isEqualTo(expectedFinalRow2Ranges);
    }

    @DisplayName("correctColumnsSequencesRanges - should correct column sequences ranges (9–10)")
    @Test
    void shouldCorrectColumnsSequencesRangesInColumnsRange() {
        // given
        NonogramLogic logic = create_o08311_logic();
        List<List<Integer>> column9Ranges = new ArrayList<>(List.of(
                new ArrayList<>(Arrays.asList(0, 6)),
                new ArrayList<>(Arrays.asList(4, 13)),
                new ArrayList<>(Arrays.asList(13, 14)),
                new ArrayList<>(Arrays.asList(15, 18))
        ));
        List<List<Integer>> column10Ranges = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(0, 0)),
                new ArrayList<>(Arrays.asList(3, 7)),
                new ArrayList<>(Arrays.asList(7, 14)),
                new ArrayList<>(Arrays.asList(9, 17)),
                new ArrayList<>(Arrays.asList(16, 19))
        ));
        logic.getNonogramColumnLogic().setColumnsSequencesRanges(
                new ArrayList<>(Arrays.asList(
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        column9Ranges,
                        column10Ranges,
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>()
                ))
        );

        // when
        subject.correctColumnsSequencesRanges(logic, 9, 10);

        // then
        List<List<Integer>> expectedFinalColumn9Ranges = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(0, 6)),
                new ArrayList<>(Arrays.asList(4, 12)),
                new ArrayList<>(Arrays.asList(13, 14)),
                new ArrayList<>(Arrays.asList(15, 18))
        ));
        List<List<Integer>> expectedFinalColumn10Ranges = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(0, 0)),
                new ArrayList<>(Arrays.asList(3, 7)),
                new ArrayList<>(Arrays.asList(7, 14)),
                new ArrayList<>(Arrays.asList(10, 17)),
                new ArrayList<>(Arrays.asList(16, 19))
        ));
        assertThat(logic.getNonogramColumnLogic().getColumnsSequencesRanges().get(9)).isEqualTo(expectedFinalColumn9Ranges);
        assertThat(logic.getNonogramColumnLogic().getColumnsSequencesRanges().get(10)).isEqualTo(expectedFinalColumn10Ranges);
    }

    @Test
    @DisplayName("runSolverWithCorrectnessCheck should handle unsolved nonogram safely")
    void shouldRunSolverWithoutNPEWhenUnsolved() throws IOException {
        // given
        NonogramLogic unsolvedLogic = create_o08086_logic();

        when(nonogramLogicFactory.copy(any())).thenAnswer(inv -> inv.getArgument(0));
        when(nonogramLogicFactory.copyNode(any()))
                .thenAnswer(inv -> {
                    NonogramSolutionNode node = inv.getArgument(0);
                    return new NonogramSolutionNode(node.getNonogramLogic(), nonogramLogicFactory);
        });

        NonogramLogicService subject = new NonogramLogicService(nonogramSolutionSaver, nonogramLogicFactory);
        String fileName = "o08086";

        // when
        NonogramLogic result = subject.runSolverWithCorrectnessCheck(unsolvedLogic, fileName);

        // then
        assertNotNull(result);
        assertFalse(NonogramStatsUtils.isSolved(result),
                "Expected nonogram to remain unsolved");
        verify(nonogramSolutionSaver, never()).saveIfCorrect(any());
    }

    NonogramLogic create_o08311_logic() {
        List<List<Integer>> rowSequences = List.of(
                List.of(7, 5), List.of(6, 3), List.of(4, 1, 3), List.of(4, 2), List.of(3, 5),
                List.of(3, 10), List.of(3, 5), List.of(4, 1), List.of(9), List.of(1, 6),
                List.of(5), List.of(4, 2), List.of(7), List.of(1, 8), List.of(2, 2),
                List.of(4, 4, 2), List.of(7, 3, 3), List.of(7, 1, 3), List.of(8, 3), List.of(8, 4)
        );

        List<List<Integer>> columnSequences = List.of(
                List.of(10, 7), List.of(9, 6), List.of(9, 1, 5), List.of(4, 2, 3, 5), List.of(2, 1, 3, 4),
                List.of(2, 1, 1, 3, 4), List.of(1, 2, 1, 3, 5), List.of(2, 2, 2, 1, 2), List.of(1, 2, 2, 2, 2), List.of(2, 2, 1, 3),
                List.of(1, 3, 2, 1, 1), List.of(1, 2, 2, 1, 1), List.of(3, 2, 3, 6), List.of(4, 2, 2, 5), List.of(4, 3, 4)
        );

        NonogramRules rules = new NonogramRules(rowSequences, columnSequences, 20, 15);

        return new NonogramLogic(rules, GuessMode.DISABLED);
    }

    NonogramLogic create_o06005_logic() {
        List<List<Integer>> rowSequences = List.of(
                List.of(1, 1, 1), List.of(1, 1, 1, 1, 1), List.of(1, 5, 1), List.of(9), List.of(9),
                List.of(7, 1), List.of(9), List.of(10), List.of(1, 1), List.of(2, 2)
        );

        List<List<Integer>> columnSequences = List.of(
                List.of(7), List.of(7), List.of(8, 1), List.of(6), List.of(8),
                List.of(6), List.of(10), List.of(2, 2, 1), List.of(7), List.of(1)
        );

        NonogramRules rules = new NonogramRules(rowSequences, columnSequences, 10, 10);

        return new NonogramLogic(rules, GuessMode.DISABLED);
    }

    NonogramLogic create_o08086_logic() {
        List<List<Integer>> rowSequences = List.of(
                List.of(6, 1, 1), List.of(14, 1, 2, 1), List.of(3, 12, 2, 3), List.of(1, 2, 4, 1, 4), List.of(3, 3, 4, 1, 5),
                List.of(3, 1, 4, 3), List.of(3, 1, 4, 1, 4), List.of(4, 4, 1, 1, 4), List.of(4, 1, 1, 1, 1, 2, 3), List.of(4, 1, 2, 1, 4),
                List.of(3, 2, 1, 2), List.of(2, 1, 2, 1), List.of(3, 2, 7, 1), List.of(1, 2, 9, 3, 1), List.of(2, 2, 2, 3, 4),
                List.of(4, 8, 4, 3), List.of(4, 5, 4, 2), List.of(4, 6, 4, 1), List.of(4, 7, 5, 1), List.of(3, 8, 9),
                List.of(2, 4, 4, 2), List.of(6, 4, 1), List.of(4, 5, 1), List.of(1, 5, 1), List.of(1, 4, 5, 2),
                List.of(1, 1, 1, 6, 3), List.of(1, 1, 1, 9, 2, 1), List.of(1, 2, 1, 9, 2, 2), List.of(1, 1, 2, 9, 2), List.of(6, 3, 3, 3, 1)
        );

        List<List<Integer>> columnSequences = List.of(
                List.of(3, 3), List.of(7, 13), List.of(1, 6, 4, 2, 1), List.of(2, 4, 4, 3, 4, 1), List.of(1, 7, 4, 4, 1, 1, 1),
                List.of(1, 2, 8, 4, 1, 1), List.of(4, 2, 1, 5, 6), List.of(2, 2, 2, 4), List.of(2, 1, 2, 8, 4), List.of(2, 1, 1, 16),
                List.of(2, 2, 13, 1), List.of(2, 1, 13), List.of(2, 2, 2, 1, 7), List.of(2, 2, 1, 1, 5), List.of(2, 2, 1, 1, 1, 5),
                List.of(4, 1, 1, 1, 1, 4), List.of(5, 1, 3, 4), List.of(1, 3, 1, 4, 1), List.of(1, 3, 2, 1), List.of(1, 5, 4, 3),
                List.of(3, 1, 9, 4), List.of(4, 2, 7, 3, 1), List.of(1, 11), List.of(5, 2, 4, 3), List.of(6, 1, 3, 2),
                List.of(1, 6, 2, 2, 2), List.of(1, 7, 2, 1), List.of(1, 5, 2, 1), List.of(10, 1), List.of(1, 4)
        );

        NonogramRules rules = new NonogramRules(rowSequences, columnSequences, 30, 30);
        return new NonogramLogic(rules, GuessMode.DISABLED);
    }
}