package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.colour.extend;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramRules;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramState;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.NonogramRowLogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class ExtendColouredFieldsRowTestBase {

    protected static NonogramRowLogic prepareRowLogic(
            List<String> initialRowState,
            List<List<Integer>> rowSequenceRanges,
            List<Integer> rowSequenceLengths
    ) {
        NonogramRowLogic nonogramRowLogic = new NonogramRowLogic();

        nonogramRowLogic.setNonogramState(buildInitialEmptyNonogramState());
        nonogramRowLogic.setNonogramRules(createMockRulesWithWidth(initialRowState.size(), rowSequenceLengths));

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

    protected static NonogramRules createMockRulesWithWidth(int width, List<Integer> rowSeqLengths) {
        NonogramRules rules = mock(NonogramRules.class);
        when(rules.getHeight()).thenReturn(1);
        when(rules.getWidth()).thenReturn(width);
        when(rules.getRowSequencesLengths()).thenReturn(List.of(rowSeqLengths));
        return rules;
    }
}

