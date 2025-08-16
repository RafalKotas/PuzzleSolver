package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.config.GuessMode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RowColouringHelperImplTest {

    @DisplayName("Should not colour any field in row if there isn't overlapping fields - o06005 row 0")
    @Test
    void colourOverlappingFieldsInRow_shouldNotChangeRow_whenNoOverlaps() {
        // given
        NonogramLogic logic = buildLogic_o06005();

        int rowIdx = 0;

        // set row
        List<String> customRow = new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"));
        logic.getNonogramSolutionBoard().set(rowIdx, customRow);

        // set sequences ranges
        List<List<Integer>> rangesForRow = new ArrayList<>();
        rangesForRow.add(List.of(0, 5));
        rangesForRow.add(List.of(2, 7));
        rangesForRow.add(List.of(4, 9));
        logic.getRowsSequencesRanges().set(rowIdx, rangesForRow);

        // snapshot before
        List<String> initialRow = new ArrayList<>(logic.getNonogramSolutionBoard().get(rowIdx));

        // when
        logic.getNonogramRowLogic().getRowColouringHelper().colourOverlappingFieldsInRow(rowIdx);

        // then: row didn't change
        List<String> updatedRow = logic.getNonogramSolutionBoard().get(rowIdx);
        assertEquals(initialRow, updatedRow, "Row should not be changed when no overlaps exist");
    }

    @DisplayName("Should colour overlapping fields - o06005 row 2")
    @Test
    void shouldColourOverlappingFields_row2() {
        // given
        NonogramLogic logic = buildLogic_o06005();
        int rowIdx = 2;

        // initial row
        List<String> initialRow = new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"));
        logic.getNonogramSolutionBoard().set(rowIdx, initialRow);

        // ranges for rowIdx=2
        List<List<Integer>> rangesForRow = new ArrayList<>();
        rangesForRow.add(new ArrayList<>(List.of(0, 1)));
        rangesForRow.add(new ArrayList<>(List.of(2, 7)));  // overlapping sequence
        rangesForRow.add(new ArrayList<>(List.of(8, 9)));
        logic.getRowsSequencesRanges().set(rowIdx, rangesForRow);

        // when
        logic.getNonogramRowLogic().getRowColouringHelper().colourOverlappingFieldsInRow(rowIdx);

        // then
        List<String> expectedAfter = List.of("-", "-", "O", "O", "O", "O", "O", "-", "-", "-");
        assertEquals(expectedAfter, logic.getNonogramSolutionBoard().get(rowIdx),
                "Row should have overlapping fields coloured (center of [2,7] for length=5)");
    }

    @DisplayName("Should exclude sequence when fully determined after overlap colouring (o06005, row 1)")
    @Test
    void shouldExcludeSequence_whenFullyDetermined() {
        // given
        NonogramLogic logic = buildLogic_o06005();
        int rowIdx = 1;

        List<String> initialRow =
                new ArrayList<>(List.of("-", "-", "O", "X", "-", "X", "O", "X", "-", "X"));
        logic.getNonogramSolutionBoard().set(rowIdx, initialRow);

        logic.getRowsSequencesIdsNotToInclude().set(rowIdx, new ArrayList<>(List.of(1, 3)));

        List<List<Integer>> ranges = new ArrayList<>();
        ranges.add(new ArrayList<>(List.of(0, 0)));  // seq 0
        ranges.add(new ArrayList<>(List.of(2, 2)));  // seq 1
        ranges.add(new ArrayList<>(List.of(4, 4)));  // seq 2
        ranges.add(new ArrayList<>(List.of(6, 6)));  // seq 3
        ranges.add(new ArrayList<>(List.of(8, 9)));  // seq 4
        logic.getRowsSequencesRanges().set(rowIdx, ranges);

        // when
        logic.getNonogramRowLogic().getRowColouringHelper().colourOverlappingFieldsInRow(rowIdx);

        // then
        List<Integer> excluded = logic.getRowsSequencesIdsNotToInclude().get(rowIdx);
        assertThat(excluded).contains(0)
                .contains(1, 3);
    }

    @DisplayName("colourFieldsInRowIfXWouldForceTooLongColouredFieldsSequence — nothing coloured when row already satisfies ranges (o06005 row 3)")
    @Test
    void shouldNotColour_whenNoOverlengthRisk() {
        // given
        NonogramLogic logic = buildLogic_o06005();
        int rowIdx = 3;

        List<String> initialRow = new ArrayList<>(List.of(
                "O","O","O","O","O","O","O","O","O","-"
        ));
        logic.getNonogramSolutionBoard().set(rowIdx, initialRow);

        List<List<Integer>> ranges = new ArrayList<>();
        ranges.add(new ArrayList<>(List.of(0, 8)));
        logic.getRowsSequencesRanges().set(rowIdx, ranges);

        int logsBefore = logic.getLogs().size();

        // when
        logic.getNonogramRowLogic()
                .getRowColouringHelper()
                .colourFieldsInRowIfXWouldForceTooLongColouredFieldsSequence(rowIdx);

        // then
        assertThat(logic.getNonogramSolutionBoard().get(rowIdx)).isEqualTo(initialRow);
        assertThat(logic.getRowsSequencesRanges().get(rowIdx)).isEqualTo(ranges);
        assertThat(logic.getLogs().size()).isEqualTo(logsBefore);
    }

    @DisplayName("Should colour fields when X would force overlength — o07836 row 6")
    @Test
    void shouldColour_whenOverlengthWouldBeForced() {
        // given
        NonogramLogic logic = buildLogic_o07836();
        int rowIdx = 6;

        // initial row & expected row after colouring
        List<String> initialRow = new ArrayList<>(List.of("-", "-", "-", "O", "-", "-", "O", "O", "-", "-"));

        logic.getNonogramSolutionBoard().set(rowIdx, initialRow);

        List<List<Integer>> ranges = new ArrayList<>();
        ranges.add(new ArrayList<>(List.of(1, 4)));
        ranges.add(new ArrayList<>(List.of(6, 7)));
        logic.getRowsSequencesRanges().set(rowIdx, ranges);

        int logsBefore = logic.getLogs().size();

        // when
        logic.getNonogramRowLogic()
                .getRowColouringHelper()
                .colourFieldsInRowIfXWouldForceTooLongColouredFieldsSequence(rowIdx);

        // then
        List<String> expectedRow = List.of("-", "-", "O", "O", "-", "-", "O", "O", "-", "-");
        assertThat(logic.getNonogramSolutionBoard().get(rowIdx)).isEqualTo(expectedRow);
        assertThat(logic.getLogs().size()).isEqualTo(logsBefore + 1);
    }

    private NonogramLogic buildLogic_o06005() {
        List<List<Integer>> rowsSequences = List.of(
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

        List<List<Integer>> colsSequences = List.of(
                List.of(7), List.of(7), List.of(8, 1),
                List.of(6), List.of(8), List.of(6),
                List.of(10), List.of(2, 2, 1), List.of(7), List.of(1)
        );

        NonogramRules rules = new NonogramRules(rowsSequences, colsSequences, 10, 10);
        return new NonogramLogic(rules, GuessMode.DISABLED);
    }

    private NonogramLogic buildLogic_o07836() {
        List<List<Integer>> rowsSequences = List.of(
                List.of(3),
                List.of(2, 3),
                List.of(5),
                List.of(4, 1),
                List.of(4, 1),
                List.of(4, 1),
                List.of(3, 2), // 6
                List.of(1, 2),
                List.of(1, 2, 2),
                List.of(5, 2)
        );

        List<List<Integer>> colsSequences = List.of(
                List.of(2),
                List.of(2, 1),
                List.of(3, 2),
                List.of(9),
                List.of(5, 1),
                List.of(3),
                List.of(3, 4),
                List.of(3, 4),
                List.of(2),
                List.of(1, 3)
        );

        NonogramRules rules = new NonogramRules(rowsSequences, colsSequences, 10, 10);
        return new NonogramLogic(rules, GuessMode.DISABLED);
    }
}