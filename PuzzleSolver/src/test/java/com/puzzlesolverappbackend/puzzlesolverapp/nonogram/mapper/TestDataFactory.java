package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.mapper;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramState;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;

import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramState.buildInitialEmptyNonogramState;

public class TestDataFactory {

    public static NonogramLogic minimalNonogramLogicForSaveRequest() {
        NonogramLogic logic = new NonogramLogic();

        List<List<String>> board = List.of(
                List.of("X", "X", "O", "X", "O", "X", "O", "X", "X", "X"),
                List.of("O", "X", "O", "X", "O", "X", "O", "X", "O", "X"),
                List.of("O", "X", "O", "O", "O", "O", "O", "X", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "X", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O"),
                List.of("X", "O", "X", "X", "X", "X", "O", "X", "X", "X"),
                List.of("X", "O", "O", "X", "X", "X", "O", "O", "X", "X")
        );

        List<List<Integer>> rowSeq = List.of(
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

        List<List<Integer>> colSeq = List.of(
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

        NonogramRules rules = new NonogramRules(rowSeq, colSeq, 10, 10);
        logic.setNonogramRules(rules);
        logic.setNonogramSolutionBoard(board);

        return logic;
    }

    public static NonogramLogic minimalNonogramLogicForResponse() {
        NonogramLogic logic = new NonogramLogic();

        logic.setNonogramSolutionBoard(List.of(
                List.of("X", "X", "O", "X", "O", "X", "O", "X", "X", "X"),
                List.of("O", "X", "O", "X", "O", "X", "O", "X", "O", "X"),
                List.of("O", "X", "O", "O", "O", "O", "O", "X", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "X", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O"),
                List.of("X", "O", "X", "X", "X", "X", "O", "X", "X", "X"),
                List.of("X", "O", "O", "X", "X", "X", "O", "O", "X", "X")
        ));

        logic.setNonogramSolutionBoardWithMarks(List.of(
                List.of("XXXX", "XXXX", "RaCa", "XXXX", "RbCa", "XXXX", "RcCa", "XXXX", "XXXX", "XXXX"),
                List.of("RaCa", "XXXX", "RbCa", "XXXX", "RcCa", "XXXX", "RdCa", "XXXX", "ReCa", "XXXX"),
                List.of("RaCa", "XXXX", "RbCa", "RbCa", "RbCa", "RbCa", "RbCa", "XXXX", "RcCa", "XXXX"),
                List.of("RaCa", "RaCa", "RaCa", "RaCa", "RaCa", "RaCa", "RaCa", "RaCa", "RaCa", "XXXX"),
                List.of("RaCa", "RaCa", "RaCa", "RaCa", "RaCa", "RaCa", "RaCa", "RaCa", "RaCa", "XXXX"),
                List.of("RaCa", "RaCa", "RaCa", "RaCa", "RaCa", "RaCa", "RaCa", "XXXX", "RbCa", "XXXX"),
                List.of("RaCa", "RaCa", "RaCa", "RaCa", "RaCa", "RaCa", "RaCa", "RaCb", "RaCa", "XXXX"),
                List.of("RaCa", "RaCa", "RaCa", "RaCa", "RaCa", "RaCa", "RaCa", "RaCb", "RaCa", "RaCa"),
                List.of("XXXX", "RaCa", "XXXX", "XXXX", "XXXX", "XXXX", "RbCa", "XXXX", "XXXX", "XXXX"),
                List.of("XXXX", "RaCa", "RaCb", "XXXX", "XXXX", "XXXX", "RbCa", "RbCc", "XXXX", "XXXX")
        ));

        logic.setRowsSequencesRanges(List.of(
                List.of(List.of(2, 2), List.of(4, 4), List.of(6, 6)),
                List.of(List.of(0, 0), List.of(2, 2), List.of(4, 4), List.of(6, 6), List.of(8, 8)),
                List.of(List.of(0, 0), List.of(2, 6), List.of(8, 8)),
                List.of(List.of(0, 8)),
                List.of(List.of(0, 8)),
                List.of(List.of(0, 6), List.of(8, 8)),
                List.of(List.of(0, 8)),
                List.of(List.of(0, 9)),
                List.of(List.of(1, 1), List.of(6, 6)),
                List.of(List.of(1, 2), List.of(6, 7))
        ));

        logic.setColumnsSequencesRanges(List.of(
                List.of(List.of(1, 7)),
                List.of(List.of(3, 9)),
                List.of(List.of(0, 7), List.of(9, 9)),
                List.of(List.of(2, 7)),
                List.of(List.of(0, 7)),
                List.of(List.of(2, 7)),
                List.of(List.of(0, 9)),
                List.of(List.of(3, 4), List.of(6, 7), List.of(9, 9)),
                List.of(List.of(1, 7)),
                List.of(List.of(7, 7))
        ));

        logic.setRowsFieldsNotToInclude(List.of(
                List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
                List.of(2),
                List.of()
        ));

        logic.setColumnsFieldsNotToInclude(List.of(
                List.of(), List.of(),
                List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
                List.of(), List.of(), List.of(),
                List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
                List.of(), List.of(), List.of()
        ));

        logic.setRowsSequencesIdsNotToInclude(List.of(
                List.of(0, 1, 2), List.of(0, 1, 2, 3, 4), List.of(0, 1, 2),
                List.of(0), List.of(0), List.of(0, 1),
                List.of(0), List.of(0), List.of(0, 1), List.of(0, 1)
        ));

        logic.setColumnsSequencesIdsNotToInclude(List.of(
                List.of(), List.of(), List.of(), List.of(), List.of(),
                List.of(), List.of(), List.of(), List.of(), List.of()
        ));

        logic.setNonogramRules(new NonogramRules(
                List.of(
                        List.of(1, 1, 1), List.of(1, 1, 1, 1, 1), List.of(1, 5, 1), List.of(9), List.of(9),
                        List.of(7, 1), List.of(9), List.of(10), List.of(1, 1), List.of(2, 2)
                ),
                List.of(
                        List.of(7), List.of(7), List.of(8, 1), List.of(6), List.of(8),
                        List.of(6), List.of(10), List.of(2, 2, 1), List.of(7), List.of(1)
                ),
                10, 10
        ));

        NonogramState state = buildInitialEmptyNonogramState();
        logic.setNonogramState(state);

        return logic;
    }
}

