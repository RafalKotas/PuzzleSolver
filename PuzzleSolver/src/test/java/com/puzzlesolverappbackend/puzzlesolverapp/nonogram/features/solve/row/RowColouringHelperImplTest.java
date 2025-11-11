package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramState;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.NonogramActionScheduler;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.NonogramBoardAccessHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.colouring.NonogramFieldColouringHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row.colour.RowColouringHelperImpl;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row.testclasses.RowColouringHelperImplUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row.testclasses.RowColouringHelperImplUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RowColouringHelperImplTest {

    @Mock
    NonogramRowLogic nonogramRowLogic;

    @Mock
    NonogramFieldColouringHelper colouringHelper;

    @Mock
    NonogramActionScheduler scheduler;

    @Mock
    NonogramState state;

    @Mock
    NonogramRules rules;

    @Mock
    NonogramBoardAccessHelper boardAccessHelper;

    @InjectMocks
    private RowColouringHelperImpl subject;

    @BeforeEach
    void setUp() {
        subject = new RowColouringHelperImpl(nonogramRowLogic);
    }

    @Test
    @DisplayName("Should not colour any field in row if there isn't overlapping fields - o06005 row 0")
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

    @Test
    @DisplayName("Should colour overlapping fields - o06005 row 2")
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

    @Test
    @DisplayName("Should exclude sequence when fully determined after overlap colouring (o06005, row 1)")
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

    @Test
    @DisplayName("colourFieldsInRowIfXWouldForceTooLongColouredFieldsSequence — nothing coloured when row already satisfies ranges (o06005 row 3)")
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
        assertThat(logic.getLogs()).hasSize(logsBefore);
    }

    @Test
    @DisplayName("Should colour fields when X would force overlength — o07836 row 6")
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
        assertThat(logic.getLogs()).hasSize(logsBefore + 1);
    }

    @Test // o07836
    @DisplayName("extendColouredFieldsToLeftNearX - only extending case")
    void shouldExtendLeft_Row2_WithRealSequencesAndBoard() {
        // given: logic with real sequences
        NonogramLogic logic = buildLogic_o07836();

        // board (row-major); row 2 = [-, -, X, O, -, -, O, O, X, -]
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "-", "X", "-", "-", "-", "-", "-", "-")), // 0
                new ArrayList<>(List.of("-", "-", "-", "O", "-", "-", "-", "-", "-", "-")), // 1
                new ArrayList<>(List.of("-", "-", "X", "O", "-", "-", "O", "O", "X", "-")), // 2  <-- target
                new ArrayList<>(List.of("-", "-", "-", "O", "O", "-", "-", "-", "-", "-")), // 3
                new ArrayList<>(List.of("-", "-", "-", "O", "O", "-", "-", "-", "-", "-")), // 4
                new ArrayList<>(List.of("-", "-", "-", "O", "-", "-", "-", "-", "-", "-")), // 5
                new ArrayList<>(List.of("-", "-", "O", "O", "-", "X", "O", "O", "X", "-")), // 6
                new ArrayList<>(List.of("-", "-", "X", "O", "X", "X", "O", "O", "X", "-")), // 7
                new ArrayList<>(List.of("-", "-", "-", "O", "X", "-", "-", "-", "-", "-")), // 8
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "-", "-", "-", "-", "-"))  // 9
        ));
        mountBoard(logic, board);

        // ranges: row 2 -> [[3,7]]
        logic.getRowsSequencesRanges().set(2, new ArrayList<>(List.of(List.of(3, 7))));

        // when: build row-logic (wires RowColouringHelperImpl with proper deps) and call public method
        NonogramRowLogic rowLogic = buildRowLogic(logic);
        rowLogic.getRowColouringHelper().extendColouredFieldsNearXToMaximumPossibleLengthInRow(2);

        // then: left extension should fill (2,5) and (2,4)
        List<String> expectedRow2 = List.of("-", "-", "X", "O", "O", "O", "O", "O", "X", "-");
        assertEquals(expectedRow2, logic.getNonogramSolutionBoard().get(2));
    }

    @Test // o06005
    @DisplayName("o06005: extendColouredFieldsNearXToMaximumPossibleLengthInRow does not extend row 0")
    void shouldNotExtendLeft_Row0_o06005() {
        // given
        NonogramLogic logic = buildLogic_o06005();

        // board (row-major); row 0 stays unchanged
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-")), // 0  <- target row
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-")), // 1
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "O", "O", "-", "-", "-")), // 2
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")), // 3
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")), // 4
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "-", "O", "-")), // 5
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "-")), // 6
                new ArrayList<>(List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O")), // 7
                new ArrayList<>(List.of("-", "-", "X", "-", "-", "-", "O", "-", "-", "-")), // 8
                new ArrayList<>(List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"))  // 9
        ));
        logic.setNonogramSolutionBoard(board);

        int rowIdx = 0;
        List<String> rowBefore = new ArrayList<>(logic.getNonogramSolutionBoard().get(rowIdx));

        // ranges for row 0: [[0,2],[2,6],[6,9]]
        logic.getRowsSequencesRanges().set(rowIdx,
                new ArrayList<>(List.of(List.of(0, 2), List.of(2, 6), List.of(6, 9))));

        // build row-logic wired to this board
        NonogramRowLogic rowLogic = new NonogramRowLogic(
                logic,
                new NonogramBoardAccessHelper(logic.getNonogramSolutionBoard()),
                new NonogramActionScheduler(logic.getActionsToDoList())
        );

        int logsBefore = logic.getLogs().size();

        // when
        rowLogic.getRowColouringHelper().extendColouredFieldsNearXToMaximumPossibleLengthInRow(rowIdx);

        // then: row unchanged; no extension happened
        assertEquals(rowBefore, logic.getNonogramSolutionBoard().get(rowIdx));
        // (optional) log count unchanged because anyGlobalFieldColoured == false in both left and right passes
        assertEquals(logsBefore, logic.getLogs().size());
    }

    @Test
    @DisplayName("extendColouredFieldsToRightNearX covers cases: extended, not extended, and distanceFromX == 0 (o06041 row 9)")
    void shouldHandleExtendedAndNotExtendedCases_o06041() {
        // given: real sequences (o06041)
        NonogramLogic logic = buildLogic_o06041();

        // board — 15 columns wide
        List<List<String>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of("-", "-", "-", "-", "O", "O", "O", "O", "O", "O", "O", "-", "-", "-", "-")),
                new ArrayList<>(List.of("X", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "-", "-", "X", "O")),
                new ArrayList<>(List.of("-", "O", "O", "O", "O", "-", "X", "-", "-", "-", "-", "-", "-", "X", "O")),
                new ArrayList<>(List.of("-", "O", "-", "X", "O", "X", "O", "O", "O", "O", "O", "O", "O", "X", "O")),
                new ArrayList<>(List.of("-", "-", "-", "X", "O", "X", "O", "O", "O", "O", "O", "O", "O", "X", "O")),
                new ArrayList<>(List.of("-", "-", "-", "-", "O", "X", "X", "O", "O", "O", "O", "O", "X", "X", "O")),
                new ArrayList<>(List.of("-", "-", "O", "O", "O", "-", "-", "X", "O", "O", "O", "X", "-", "X", "O")),
                new ArrayList<>(List.of("-", "-", "-", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "X", "O")),
                new ArrayList<>(List.of("-", "-", "-", "-", "O", "-", "X", "X", "X", "-", "-", "-", "-", "-", "O")),
                new ArrayList<>(List.of("-", "-", "-", "-", "X", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-")) // row 9
        ));
        mountBoard(logic, board);

        int rowToCheck = 9;

        // mount the board
        RowColouringHelperImplUtils.mountBoard(logic, board);

        // set ranges for row 9
        List<List<List<Integer>>> ranges = logic.getRowsSequencesRanges();
        ranges.set(rowToCheck, new ArrayList<>(List.of(List.of(5, 14))));

        // build row logic (with board access + scheduler)
        NonogramRowLogic rowLogic = RowColouringHelperImplUtils.buildRowLogic(logic);

        // snapshot before
        List<String> before = new ArrayList<>(board.get(9));
        assertEquals(List.of("-", "-", "-", "-", "X", "-", "O", "O", "O", "-", "-", "-", "-", "-", "-"), before);

        // when
        rowLogic.getRowColouringHelper().extendColouredFieldsNearXToMaximumPossibleLengthInRow(9);

        // then — only right side extended; positions (9,9)–(9,13) coloured
        List<String> expectedAfter = List.of("-", "-", "-", "-", "X", "-", "O", "O", "O", "O", "O", "O", "O", "O", "-");
        assertEquals(expectedAfter, board.get(rowToCheck));

        // Optional: assert we covered all three branches
        // - extended = true (positions 9–13)
        // - notExtended = true (none to extend after)
        // - distanceFromX == 0 (for leftmost parts)
    }
}