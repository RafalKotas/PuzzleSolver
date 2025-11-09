package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.mark;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BoardContextTest {

    @Test
    @DisplayName("AllArgsConstructor should create BoardContext with all fields")
    void shouldCreateBoardContextWithAllFields() {
        // given
        int lineIdx = 2;
        boolean isRow = true;
        NonogramRules rules = new NonogramRules();
        List<List<String>> solutionBoard = List.of(List.of("X", "-", "X"));
        List<List<String>> boardWithMarks = List.of(List.of("XXXX", "----", "XXXX"));

        // when
        BoardContext context = new BoardContext(lineIdx, isRow, rules, solutionBoard, boardWithMarks);

        // then
        assertThat(context.getLineIdx()).isEqualTo(2);
        assertThat(context.isRow()).isEqualTo(true);
        assertThat(rules).isEqualTo(context.getRules());
        assertThat(context.getSolutionBoard()).isEqualTo(List.of(List.of("X", "-", "X")));
        assertThat(context.getBoardWithMarks()).isEqualTo(List.of(List.of("XXXX", "----", "XXXX")));
    }
}
