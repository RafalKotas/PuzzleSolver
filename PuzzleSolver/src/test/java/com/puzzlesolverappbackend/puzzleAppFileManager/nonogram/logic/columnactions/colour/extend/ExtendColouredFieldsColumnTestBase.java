package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.extend;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramRules;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramState;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.NonogramColumnLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExtendColouredFieldsColumnTestBase {

    protected static NonogramColumnLogic prepareColumnLogic(
            List<String> initialColumnState,
            List<List<Integer>> columnSequenceRanges,
            List<Integer> columnSequenceLengths
    ) {
        NonogramColumnLogic nonogramColumnLogic = new NonogramColumnLogic();

        nonogramColumnLogic.setNonogramState(buildInitialEmptyNonogramState());
        nonogramColumnLogic.setNonogramRules(createMockRulesWithWidth(initialColumnState.size(), columnSequenceLengths));

        List<List<List<Integer>>> columnsSequencesRanges = new ArrayList<>(List.of(columnSequenceRanges));
        nonogramColumnLogic.setColumnsSequencesRanges(columnsSequencesRanges);

        List<List<String>> nonogramSolutionBoard = initialColumnState.stream()
                .map(cell -> new ArrayList<>(List.of(cell)))
                .collect(Collectors.toList());
        nonogramColumnLogic.setNonogramSolutionBoard(nonogramSolutionBoard);

        List<List<String>> nonogramSolutionBoardWithMarks =  initialColumnState.stream()
                .map(val -> new ArrayList<>(List.of("----")))
                .collect(Collectors.toList());
        nonogramColumnLogic.setNonogramSolutionBoardWithMarks(nonogramSolutionBoardWithMarks);

        return nonogramColumnLogic;
    }

    protected static NonogramState buildInitialEmptyNonogramState() {
        return new NonogramState();
    }

    protected static NonogramRules createMockRulesWithWidth(int height, List<Integer> columnSeqLengths) {
        NonogramRules rules = mock(NonogramRules.class);
        when(rules.getHeight()).thenReturn(height);
        when(rules.getWidth()).thenReturn(1);
        when(rules.getColumnSequencesLengths()).thenReturn(List.of(columnSeqLengths));
        return rules;
    }
}
