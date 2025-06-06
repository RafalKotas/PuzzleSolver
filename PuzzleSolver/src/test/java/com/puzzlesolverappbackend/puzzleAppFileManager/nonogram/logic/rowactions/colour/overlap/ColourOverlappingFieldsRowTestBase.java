package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.overlap;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramRules;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramState;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.NonogramRowLogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ColourOverlappingFieldsRowTestBase {

    protected static void assertOverlappingInRow(
            String testLabel,
            List<String> initialRowState,
            List<List<Integer>> rowSequenceRanges,
            List<Integer> rowSequenceLengths,
            List<String> expectedRowState
    ) {
        NonogramRowLogic nonogramRowLogic = prepareRowLogic(initialRowState, rowSequenceRanges, rowSequenceLengths);
        nonogramRowLogic.colourOverlappingFieldsInRow(0);
        List<String> actualRow = nonogramRowLogic.getNonogramSolutionBoard().get(0);
        assertEquals(expectedRowState, actualRow, "Mismatch in row state for: " + testLabel);
    }

    protected static NonogramRowLogic prepareRowLogic(
            List<String> initialRowState,
            List<List<Integer>> rowSequenceRanges,
            List<Integer> rowSequenceLengths
    ) {
        NonogramRowLogic nonogramRowLogic = new NonogramRowLogic();

        nonogramRowLogic.setNonogramState(buildInitialEmptyNonogramState());
        nonogramRowLogic.setNonogramRules(createMockRulesWithWidth(initialRowState.size(), rowSequenceLengths));
        nonogramRowLogic.setRowsSequencesIdsNotToInclude(buildInitialEmptyRowsSequencesIdsNotToInclude());

        List<List<List<Integer>>> rowsSequencesRanges = new ArrayList<>(List.of(rowSequenceRanges));
        nonogramRowLogic.setRowsSequencesRanges(rowsSequencesRanges);

        List<List<String>> nonogramSolutionBoard = new ArrayList<>();
        nonogramSolutionBoard.add(new ArrayList<>(initialRowState));
        nonogramRowLogic.setNonogramSolutionBoard(nonogramSolutionBoard);

        List<List<String>> nonogramSolutionBoardWithMarks = new ArrayList<>();
        nonogramSolutionBoardWithMarks.add(new ArrayList<>(Collections.nCopies(initialRowState.size(), "----")));
        nonogramRowLogic.setNonogramSolutionBoardWithMarks(nonogramSolutionBoardWithMarks);

        return nonogramRowLogic;
    }

    protected static NonogramState buildInitialEmptyNonogramState() {
        return new NonogramState();
    }

    protected static List<List<Integer>> buildInitialEmptyRowsSequencesIdsNotToInclude() {
        List<List<Integer>> rowsSequencesIdsNotToInclude = new ArrayList<>();
        rowsSequencesIdsNotToInclude.add(new ArrayList<>());

        return rowsSequencesIdsNotToInclude;
    }

    protected static NonogramRules createMockRulesWithWidth(int width, List<Integer> rowSeqLengths) {
        NonogramRules rules = mock(NonogramRules.class);
        when(rules.getHeight()).thenReturn(1);
        when(rules.getWidth()).thenReturn(width);
        when(rules.getRowSequencesLengths()).thenReturn(List.of(rowSeqLengths));
        return rules;
    }
}
