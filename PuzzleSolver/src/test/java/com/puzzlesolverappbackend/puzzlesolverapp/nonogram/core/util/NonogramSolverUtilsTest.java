package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.util;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.config.GuessMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class NonogramSolverUtilsTest {

    NonogramLogic nonogramLogic;

    @BeforeEach
    void setUp() {
        create_solved_o06005_nonogram();
    }

    @Test
    @DisplayName("NonogramSolverUtils constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<NonogramSolverUtils> constructor = NonogramSolverUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    // isBoardConsistentWithSequences

    @Test
    @DisplayName("Should mark solutionBoard as consistent with sequences lengths")
    void shouldRecognizeBoardAsConsistentWithSequences() {
        // given
        List<List<String>> board = nonogramLogic.getNonogramSolutionBoard();

        // when
        boolean consistent = NonogramSolverUtils.isBoardConsistentWithSequences(board,
                nonogramLogic.getNonogramRules().getRowSequencesLengths(),
                nonogramLogic.getNonogramRules().getColumnSequencesLengths());

        // then
        assertThat(consistent).isTrue();
    }

    @Test
    @DisplayName("Should mark solutionBoard as inconsistent with sequences lengths")
    void shouldRecognizeBoardAsInconsistentWithSequences() {
        // given
        List<List<String>> board = nonogramLogic.getNonogramSolutionBoard();
        board.get(0).set(0, "O");

        // when
        boolean consistent = NonogramSolverUtils.isBoardConsistentWithSequences(board,
                nonogramLogic.getNonogramRules().getRowSequencesLengths(),
                nonogramLogic.getNonogramRules().getColumnSequencesLengths());

        // then
        assertThat(consistent).isFalse();
    }

    //

    @Test
    @DisplayName("Should infer rows ranges from nonogram board")
    void shouldInferRowsRangesCorrectly() {
        // given
        List<List<String>> board = nonogramLogic.getNonogramSolutionBoard();

        // when
        var inferred = NonogramSolverUtils.inferSequenceRangesFromBoard(board);

        // then
        assertThat(inferred).isEqualTo(nonogramLogic.getRowsSequencesRanges());
    }

    @Test
    @DisplayName("Should infer columns ranges from nonogram board")
    void shouldInferColumnsRangesCorrectly() {
        // given
        List<List<String>> board = nonogramLogic.getNonogramSolutionBoard();

        // when
        var inferred = NonogramSolverUtils.inferSequenceRangesFromColumns(board);

        // that
        assertThat(inferred).isEqualTo(nonogramLogic.getColumnsSequencesRanges());
    }

    @Test
    @DisplayName("Should mark modified correct solutionBoard as inconsistent with sequences lengths")
    void shouldDetectInconsistentBoard() {
        // copy + modify one coloured field
        List<List<String>> modified = new ArrayList<>();
        for (List<String> row : nonogramLogic.getNonogramSolutionBoard()) {
            modified.add(new ArrayList<>(row));
        }
        modified.get(2).set(3, "X"); // breaks a coloured sequence

        // when
        boolean consistent = NonogramSolverUtils.isBoardConsistentWithSequences(modified,
                nonogramLogic.getNonogramRules().getRowSequencesLengths(),
                nonogramLogic.getNonogramRules().getColumnSequencesLengths());

        // then
        assertThat(consistent).isFalse();
    }

    @Test
    @DisplayName("Should return true when actual ranges don't contain correct ranges")
    void shouldReturnTrueWhenExpectedNotContainedInActual() {
        // given
        List<List<Integer>> expected = List.of(List.of(1, 3), List.of(5, 7));
        List<List<Integer>> actual = List.of(List.of(2, 4), List.of(6, 8));

        // when
        boolean actualRangesDoNotContainCorrectRanges = NonogramSolverUtils.actualRangesDoNotContainCorrectRanges(expected, actual);

        // then
        assertThat(actualRangesDoNotContainCorrectRanges).isTrue();
    }

    @Test
    @DisplayName("Should return false when actual ranges contain correct ranges")
    void shouldReturnFalseWhenExpectedContainedInActual() {
        // given
        List<List<Integer>> expected = List.of(List.of(2, 3), List.of(6, 7));
        List<List<Integer>> actual = List.of(List.of(1, 4), List.of(5, 8));

        // when
        boolean actualRangesDoNotContainCorrectRanges = NonogramSolverUtils.actualRangesDoNotContainCorrectRanges(expected, actual);

        // then
        assertThat(actualRangesDoNotContainCorrectRanges).isFalse();
    }

    void create_solved_o06005_nonogram() {
        nonogramLogic = create_o06005_logic();

        nonogramLogic.setNonogramSolutionBoard(new ArrayList<>(
                List.of(
                        new ArrayList<>(List.of("X", "X", "O", "X", "O", "X", "O", "X", "X", "X")),
                        new ArrayList<>(List.of("O", "X", "O", "X", "O", "X", "O", "X", "O", "X")),
                        new ArrayList<>(List.of("O", "X", "O", "O", "O", "O", "O", "X", "O", "X")),
                        new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X")),
                        new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X")),
                        new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "X", "O", "X")),
                        new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X")),
                        new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O")),
                        new ArrayList<>(List.of("X", "O", "X", "X", "X", "X", "O", "X", "X", "X")),
                        new ArrayList<>(List.of("X", "O", "O", "X", "X", "X", "O", "O", "X", "X"))
                )
        ));

        List<List<List<Integer>>> rowSequencesRanges = List.of(
                List.of(List.of(2, 2), List.of(4, 4), List.of(6, 6)),
                List.of(List.of(0, 0), List.of(2, 2), List.of(4, 4), List.of(6, 6), List.of(8, 8)),
                List.of(List.of(0, 0), List.of(2, 6), List.of(8, 8)),
                List.of(List.of(0, 8)),
                List.of(List.of(0, 8)),
                List.of(List.of(0, 6), List.of(8, 8)),
                List.of(List.of(0, 8)),
                List.of(List.of(0, 9)),
                List.of(List.of(1, 1), List.of(6, 6)),
                List.of(List.of(1, 2), List.of(6, 7))
        );

        List<List<List<Integer>>> columnSequencesRanges = List.of(
                List.of(List.of(1, 7)),
                List.of(List.of(3, 9)),
                List.of(List.of(0, 7), List.of(9, 9)),
                List.of(List.of(2, 7)),
                List.of(List.of(0, 7)),
                List.of(List.of(2, 7)),
                List.of(List.of(0, 9)),
                List.of(List.of(3, 4), List.of(6, 7), List.of(9, 9)),
                List.of(List.of(1, 7)),
                List.of(List.of(7, 7))
        );

        nonogramLogic.setRowsSequencesRanges(rowSequencesRanges);
        nonogramLogic.setColumnsSequencesRanges(columnSequencesRanges);
    }

    NonogramLogic create_o06005_logic() {
        List<List<Integer>> rowSequencesLengths = List.of(
                List.of(1, 1, 1),
                List.of(1, 1, 1, 1, 1),
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
                List.of(7),
                List.of(7),
                List.of(8, 1),
                List.of(6),
                List.of(8),
                List.of(6),
                List.of(10),
                List.of(2, 2, 1),
                List.of(7),
                List.of(1)
        );

        NonogramRules rules = new NonogramRules(rowSequencesLengths, columnSequencesLengths, 10, 10);
        return new NonogramLogic(rules, GuessMode.DISABLED);
    }
}