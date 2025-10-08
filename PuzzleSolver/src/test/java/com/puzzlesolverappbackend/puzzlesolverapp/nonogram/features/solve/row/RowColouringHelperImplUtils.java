package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.NonogramActionScheduler;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.NonogramBoardAccessHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.config.GuessMode;

import java.util.List;

public class RowColouringHelperImplUtils {


    public static NonogramLogic buildLogic_o06005() {
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

    public static NonogramLogic buildLogic_o07836() {
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

    public static NonogramLogic buildLogic_o06041() {
        List<List<Integer>> rowsSequences = List.of(
                List.of(11),
                List.of(4, 1),
                List.of(5, 2, 2, 1),
                List.of(2, 1, 7, 1),
                List.of(1, 1, 7, 1),
                List.of(2, 1, 5, 1),
                List.of(5, 3, 1),
                List.of(4, 1, 1),
                List.of(2, 2),
                List.of(9)
        );

        List<List<Integer>> colsSequences = List.of(
                List.of(5),
                List.of(3, 3),
                List.of(2, 2),
                List.of(2, 2),
                List.of(9),
                List.of(1, 2),
                List.of(1, 2, 1),
                List.of(1, 4, 1),
                List.of(1, 5, 1),
                List.of(1, 5, 1),
                List.of(1, 5, 1),
                List.of(1, 4, 1),
                List.of(1, 2, 1),
                List.of(1, 2),
                List.of(9)
        );

        NonogramRules rules = new NonogramRules(rowsSequences, colsSequences, 10, 15);
        return new NonogramLogic(rules, GuessMode.DISABLED);
    }

    public static void mountBoard(NonogramLogic logic, List<List<String>> board) {
        logic.setNonogramSolutionBoard(board);
    }

    public static NonogramRowLogic buildRowLogic(NonogramLogic logic) {
        NonogramBoardAccessHelper accessHelper = new NonogramBoardAccessHelper(logic.getNonogramSolutionBoard());
        NonogramActionScheduler scheduler = new NonogramActionScheduler(logic.getActionsToDoList());
        return new NonogramRowLogic(logic, accessHelper, scheduler);
    }
}
