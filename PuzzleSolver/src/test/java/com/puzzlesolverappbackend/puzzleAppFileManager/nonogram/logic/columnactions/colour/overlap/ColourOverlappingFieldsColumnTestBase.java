package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.colour.overlap;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramRules;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramState;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.NonogramColumnLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ColourOverlappingFieldsColumnTestBase {

    protected static void assertOverlappingInColumn(
            String testLabel,
            List<String> initialColumnState,
            List<List<Integer>> columnSequenceRanges,
            List<Integer> columnSequenceLengths,
            List<String> expectedColumnState
    ) {
        NonogramColumnLogic nonogramColumnLogic = prepareColumnLogicForOverlapping(initialColumnState, columnSequenceRanges, columnSequenceLengths);
        nonogramColumnLogic.colourOverlappingFieldsInColumn(0);
        List<String> actualColumn = nonogramColumnLogic.getNonogramBoardColumn(0);
        assertEquals(expectedColumnState, actualColumn, "Mismatch in column state for: " + testLabel);
    }

    protected static NonogramColumnLogic prepareColumnLogicForOverlapping(
            List<String> initialColumnState,
            List<List<Integer>> columnSequenceRanges,
            List<Integer> columnSequenceLengths
    ) {
        NonogramColumnLogic nonogramColumnLogic = new NonogramColumnLogic();

        nonogramColumnLogic.setNonogramState(buildInitialEmptyNonogramState());
        nonogramColumnLogic.setNonogramRules(createMockRulesWithWidth(initialColumnState.size(), columnSequenceLengths));
        nonogramColumnLogic.setColumnsSequencesIdsNotToInclude(buildInitialEmptyColumnsSequencesIdsNotToInclude());

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

    protected static List<List<Integer>> buildInitialEmptyColumnsSequencesIdsNotToInclude() {
        List<List<Integer>> columnsSequencesIdsNotToInclude = new ArrayList<>();
        columnsSequencesIdsNotToInclude.add(new ArrayList<>());

        return columnsSequencesIdsNotToInclude;
    }

    protected static NonogramRules createMockRulesWithWidth(int height, List<Integer> columnSeqLengths) {
        NonogramRules rules = mock(NonogramRules.class);
        when(rules.getHeight()).thenReturn(height);
        when(rules.getWidth()).thenReturn(1);
        when(rules.getColumnSequencesLengths()).thenReturn(List.of(columnSeqLengths));
        return rules;
    }
}
