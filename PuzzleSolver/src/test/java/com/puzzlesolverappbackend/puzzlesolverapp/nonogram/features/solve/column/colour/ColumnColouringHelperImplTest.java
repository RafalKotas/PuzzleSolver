package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.colour;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.NonogramActionScheduler;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.NonogramBoardAccessHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.config.GuessMode;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.NonogramColumnLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.colour.ColumnColouringHelperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ColumnColouringHelperImplTest {

    ColumnColouringHelperImpl subject;

    NonogramLogic logic;

    @BeforeEach
    void setup() {
        logic = prepareNonogramLogic();
    }

    @Test
    @DisplayName("colourOverlappingFieldsInColumn – o06005, col=0: should overlap 4 fields and increase steps made by 4")
    void shouldColourOverlappingFieldsInColumn_o06005_col0() {
        // given
        List<List<String>> board = new ArrayList<>();
        for (int r = 0; r < 10; r++) {
            List<String> row = new ArrayList<>();
            for (int c = 0; c < 10; c++) {
                row.add("-");
            }
            board.add(row);
        }
        board.get(7).set(0, "O");

        logic.setNonogramSolutionBoard(board);

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int c = 0; c < 10; c++) {
            colRanges.add(new ArrayList<>());
        }
        colRanges.get(0).add(List.of(0, 9));
        logic.setColumnsSequencesRanges(colRanges);

        NonogramColumnLogic columnLogic = new NonogramColumnLogic(
                logic,
                new NonogramBoardAccessHelper(logic),
                new NonogramActionScheduler(logic.getActionsToDoList())
        );

        int columnIdx = 0;
        List<String> expectedAfter = List.of("-", "-", "-", "O", "O", "O", "O", "O", "-", "-");
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        subject = new ColumnColouringHelperImpl(columnLogic);

        // when
        subject.colourOverlappingFieldsInColumn(columnIdx);

        // then
        List<String> after = subject.getNonogramColumnLogic().getBoardAccessHelper().getColumnCopy(columnIdx);
        assertThat(after).isEqualTo(expectedAfter);

        int stepsAfter = logic.getNonogramState().getNewStepsMade();
        assertThat(stepsAfter - stepsBefore).isEqualTo(4); // four coloured fields
    }

    @Test
    @DisplayName("colourOverlappingFieldsInColumn – column already coloured, no changes (anyFieldColoured = false)")
    void shouldNotColourAnythingWhenAlreadyColoured() {
        // given
        List<List<String>> board = new ArrayList<>();
        for (int r = 0; r < 10; r++) {
            List<String> row = new ArrayList<>();
            for (int c = 0; c < 10; c++) {
                row.add("-");
            }
            board.add(row);
        }
        board.get(3).set(1, "O");
        board.get(4).set(1, "O");
        board.get(5).set(1, "O");
        board.get(6).set(1, "O");
        board.get(7).set(1, "O");

        logic.setNonogramSolutionBoard(board);

        List<List<List<Integer>>> colRanges = new ArrayList<>();
        for (int c = 0; c < 10; c++) {
            colRanges.add(new ArrayList<>());
        }
        colRanges.get(1).add(List.of(0, 9));
        logic.setColumnsSequencesRanges(colRanges);

        NonogramColumnLogic columnLogic = new NonogramColumnLogic(logic,
                new NonogramBoardAccessHelper(logic),
                new NonogramActionScheduler(logic.getActionsToDoList()));
        int columnIdx = 1;
        List<String> before = columnLogic.getBoardAccessHelper().getColumnCopy(columnIdx);
        int stepsBefore = logic.getNonogramState().getNewStepsMade();

        subject = new ColumnColouringHelperImpl(columnLogic);

        // when
        subject.colourOverlappingFieldsInColumn(columnIdx);

        // then
        List<String> after = columnLogic.getBoardAccessHelper().getColumnCopy(columnIdx);
        assertThat(after).isEqualTo(before);
        int stepsAfter = logic.getNonogramState().getNewStepsMade();
        assertThat(stepsAfter - stepsBefore).isZero();
    }

    private static NonogramLogic prepareNonogramLogic() {
        List<List<Integer>> rowSequences = List.of(
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

        List<List<Integer>> columnSequences = List.of(
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

        NonogramRules rules = new NonogramRules(rowSequences, columnSequences, 10, 10);
        return new NonogramLogic(rules, GuessMode.DISABLED);
    }
}