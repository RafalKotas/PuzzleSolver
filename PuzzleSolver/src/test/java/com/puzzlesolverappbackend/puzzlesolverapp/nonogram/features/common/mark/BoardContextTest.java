package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.mark;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardContextTest {

    @Test
    void shouldCreateBoardContextWithAllFields() {
        // given
        int lineIdx = 2;
        boolean isRow = true;
        NonogramRules rules = new NonogramRules();
        List<List<String>> solutionBoard = List.of(List.of("X", ".", "X"));
        List<List<String>> boardWithMarks = List.of(List.of("X", ".", "X"));

        // when
        BoardContext context = new BoardContext(lineIdx, isRow, rules, solutionBoard, boardWithMarks);

        // then
        assertEquals(lineIdx, context.getLineIdx());
        assertEquals(isRow, context.isRow());
        assertEquals(rules, context.getRules());
        assertEquals(solutionBoard, context.getSolutionBoard());
        assertEquals(boardWithMarks, context.getBoardWithMarks());
    }
}
