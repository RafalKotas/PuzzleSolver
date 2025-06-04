package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramRules;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class ExtendColouredFieldsToRightRowTestBase {

    protected static void assertExtensionRight(
            String testLabel,
            List<String> initialRowState,
            List<List<Integer>> rowSequenceRanges,
            List<Integer> rowSequenceLengths,
            List<String> expectedRowState
    ) {
        // given
        NonogramRowLogic logic = prepareLogic(initialRowState, rowSequenceRanges, rowSequenceLengths);

        // when
        logic.extendColouredFieldsToRightNearXToMaximumPossibleLengthInRow(0);

        // then
        List<String> actualRow = logic.getNonogramSolutionBoard().get(0);
        assertEquals(expectedRowState, actualRow, "Mismatch in row state for: " + testLabel);
    }

    protected static NonogramRowLogic prepareLogic(
            List<String> initialRowState,
            List<List<Integer>> rowSequenceRanges,
            List<Integer> rowSequenceLengths
    ) {
        NonogramRowLogic logic = new NonogramRowLogic();

        logic.setNonogramState(buildInitialEmptyNonogramState());
        logic.setNonogramRules(createMockRulesWithWidth(initialRowState.size(), rowSequenceLengths));

        List<List<List<Integer>>> rowsSequencesRanges = new ArrayList<>(List.of(rowSequenceRanges));
        logic.setRowsSequencesRanges(rowsSequencesRanges);

        List<List<String>> nonogramSolutionBoard = new ArrayList<>();
        nonogramSolutionBoard.add(new ArrayList<>(initialRowState));
        logic.setNonogramSolutionBoard(nonogramSolutionBoard);

        List<List<String>> nonogramSolutionBoardWithMarks = new ArrayList<>();
        nonogramSolutionBoardWithMarks.add(new ArrayList<>(Collections.nCopies(initialRowState.size(), "----")));
        logic.setNonogramSolutionBoardWithMarks(nonogramSolutionBoardWithMarks);

        return logic;
    }

    protected static NonogramRules createMockRulesWithWidth(int width, List<Integer> rowSeqLengths) {
        NonogramRules rules = mock(NonogramRules.class);
        when(rules.getHeight()).thenReturn(1);
        when(rules.getWidth()).thenReturn(width);
        when(rules.getRowSequencesLengths()).thenReturn(List.of(rowSeqLengths));
        return rules;
    }

    protected static NonogramState buildInitialEmptyNonogramState() {
        return new NonogramState();
    }
}

