package com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.solutions;

import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.NonogramRowLogic;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Objects;

import static com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.NonogramConstants.EMPTY_FIELD;

@UtilityClass
public class NonogramSolutions {

    private boolean subSolutionDifferFromSolutionPart(NonogramLogic subSolution, NonogramLogic fullSolution) {
        List<List<String>> subSolutionBoard = subSolution.getNonogramRowLogic().getNonogramSolutionBoard();
        List<List<String>> solutionBoard = fullSolution.getNonogramRowLogic().getNonogramSolutionBoard();

        for (int rowIdx = 0; rowIdx < solutionBoard.size(); rowIdx++) {
            for (int columnIdx = 0; columnIdx < solutionBoard.get(rowIdx).size(); columnIdx++) {
                if (!subSolutionBoard.get(rowIdx).get(columnIdx).equals(EMPTY_FIELD) && !Objects.equals(subSolutionBoard.get(rowIdx).get(columnIdx), solutionBoard.get(rowIdx).get(columnIdx))) {
                    System.out.println("rowIdx: " + rowIdx + " , columnIdx: " + columnIdx);
                    return true;
                }
            }
        }

        return false;
    }

    public static NonogramLogic o08350_solutionToCompare() {
        NonogramRowLogic o08350_rowLogic = new NonogramRowLogic();

        List<List<String>> o08350_solution_board =
                List.of(
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "O", "O", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "X", "X", "O", "O", "X", "X", "O", "O", "O", "O", "O", "O", "O", "X", "O", "X", "X", "O", "O", "O", "O", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "O", "O", "O", "O", "X", "O", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "O", "X", "O", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "O", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "O", "O", "O", "O", "O", "O", "X", "O", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "O", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "O", "O", "O", "O", "O", "X", "O", "O", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "O", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "O", "O", "O", "O", "X", "O", "X", "X", "O", "O"),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "O", "O", "X", "O", "O", "O", "O", "O", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "O", "O", "O", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O"),
                        List.of("X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O"),
                        List.of("X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "x", "X", "X"),
                        List.of("X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O"),
                        List.of("X", "O", "O", "X", "X", "O", "X", "X", "O", "O", "O", "O", "O", "X", "O", "O", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "O", "O", "X", "X"),
                        List.of("X", "O", "X", "O", "O", "O", "O", "O", "X", "O", "O", "X", "X", "O", "O", "X", "X", "O", "O", "O", "Z", "X", "O", "O", "O", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X"),
                        List.of("O", "O", "X", "O", "O", "O", "O", "O", "O", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X"),
                        List.of("X", "O", "X", "O", "O", "O", "O", "O", "X", "X", "O", "O", "X", "O", "O", "O", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X"),
                        List.of("O", "O", "X", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "O", "O", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "O", "X", "X", "X", "X", "X", "X"),
                        List.of("O", "O", "X", "O", "O", "O", "X", "X", "O", "X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "O", "O", "X", "O", "O", "O", "X", "X", "X", "O", "O", "X", "X", "X", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "O", "O", "O", "O", "X", "X", "X", "O", "O", "X", "X", "X", "X", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "X", "X", "X", "X", "X", "X", "X", "X")
                );

        o08350_rowLogic.setNonogramSolutionBoard(o08350_solution_board);

        return NonogramLogic.builder()
                .nonogramRowLogic(o08350_rowLogic)
                .build();
    }

    public static NonogramLogic o08545_solutionToCompare() {
        NonogramRowLogic o08545_rowLogic = new NonogramRowLogic();

        List<List<String>> o08545_solution_board =
                List.of(
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "O", "X", "X", "O", "O", "O", "X", "O", "O", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "O", "X", "X", "X", "X", "X", "O", "X", "X", "X", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "O", "O", "O", "X", "O", "X", "X", "X", "X", "X", "O", "X", "X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "O", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "O", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "O", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "O", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "O", "O", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "O", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "O", "O", "X", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "O", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "X", "X", "O", "O", "X", "O", "O", "X", "O", "O", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "O", "X", "O", "X", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "O", "X", "O", "O", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "O", "X", "O", "X", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "O", "O", "X", "O", "X", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "O", "O", "X", "O", "X", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "X", "X", "O", "X", "O", "O", "X", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "O", "O", "X", "O", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "O", "X", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X"),
                        List.of("X", "O", "O", "O", "O", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of("X", "O", "O", "O", "O", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "X", "X", "O", "O", "O", "O", "O", "O", "X"),
                        List.of("X", "O", "O", "O", "O", "X", "X", "O", "O", "X", "X", "X", "X", "O", "O", "O", "O", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "O", "O"),
                        List.of("O", "O", "O", "O", "X", "O", "X", "O", "O", "X", "X", "X", "X", "O", "O", "X", "O", "X", "X", "X", "X", "X", "O", "X", "X", "X", "X", "X", "X", "O"),
                        List.of("O", "O", "O", "O", "X", "O", "X", "O", "O", "O", "X", "X", "X", "O", "O", "X", "O", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O"),
                        List.of("O", "O", "O", "O", "X", "O", "X", "X", "O", "O", "X", "X", "X", "O", "O", "X", "O", "O", "X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "X"),
                        List.of("O", "O", "O", "O", "O", "X", "O", "X", "O", "O", "X", "X", "X", "O", "O", "X", "X", "O", "X", "X", "X", "X", "O", "O", "O", "O", "X", "X", "X", "X"),
                        List.of("O", "O", "O", "O", "O", "X", "O", "X", "O", "O", "O", "X", "X", "O", "O", "X", "X", "O", "O", "X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X"),
                        List.of("O", "O", "X", "O", "O", "O", "X", "O", "X", "O", "O", "X", "X", "O", "O", "X", "X", "X", "O", "X", "X", "X", "X", "O", "O", "O", "O", "X", "X", "X"),
                        List.of("O", "O", "X", "O", "O", "O", "X", "O", "X", "O", "O", "X", "X", "O", "O", "O", "X", "X", "O", "O", "X", "X", "X", "O", "O", "O", "O", "O", "X", "X"),
                        List.of("O", "X", "X", "X", "O", "O", "X", "O", "X", "O", "O", "O", "X", "X", "O", "O", "X", "X", "O", "O", "X", "X", "X", "O", "O", "O", "O", "O", "O", "X"),
                        List.of("O", "X", "X", "X", "X", "O", "O", "O", "X", "O", "O", "O", "O", "X", "O", "O", "O", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O")
                );

        o08545_rowLogic.setNonogramSolutionBoard(o08545_solution_board);

        return NonogramLogic.builder()
                .nonogramRowLogic(o08545_rowLogic)
                .build();
    }


    public static NonogramLogic o11775_solutionToCompare() {
        NonogramRowLogic o11775_rowLogic = new NonogramRowLogic();

        List<List<String>> o11775_solution_board =
                List.of(
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "O", "O", "X", "X", "O", "O", "X", "X", "X", "X", "X", "O", "X", "X", "X", "O", "O", "O", "X", "X", "O", "O", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "O", "O", "X", "O", "O", "O", "O", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "X", "O", "O", "X", "X", "X"),
                        List.of("X", "X", "O", "O", "X", "O", "O", "O", "O", "O", "X", "X", "O", "O", "X", "X", "X", "O", "O", "X", "O", "O", "O", "O", "O", "X", "O", "O", "O", "X"),
                        List.of("O", "O", "O", "X", "O", "O", "O", "O", "O", "O", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "O", "O", "X", "O", "O", "O", "O", "O", "X", "X", "X", "X", "O", "X", "X", "X", "O", "O", "O", "O", "O", "X", "O", "O", "X", "X", "X"),
                        List.of("X", "X", "O", "O", "X", "X", "O", "X", "X", "O", "O", "O", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "X", "O", "X", "X", "O", "O", "O", "O"),
                        List.of("O", "O", "O", "X", "X", "X", "O", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "O", "O", "O", "O", "X", "O", "X", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "O", "X", "O", "X", "X", "O", "O", "O", "O", "X", "X", "O", "O", "X", "X", "O", "X", "O", "X", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "O", "X", "O", "X", "X", "X", "O", "O", "O", "O", "O", "O", "X", "X", "X", "O", "X", "O", "O", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "O", "O", "X", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "X", "X", "O", "O", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "O", "O", "X", "X", "O", "X", "X", "X", "X", "X", "X", "O", "X", "X", "X", "X", "O", "O", "X", "X", "X", "O", "O", "X", "X", "X"),
                        List.of("X", "X", "O", "O", "O", "X", "X", "X", "O", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "O", "O", "O", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X")
                );

        o11775_rowLogic.setNonogramSolutionBoard(o11775_solution_board);

        return NonogramLogic.builder()
                .nonogramRowLogic(o11775_rowLogic)
                .build();
    }

    public static NonogramLogic o09923_solutionToCompare() {
        NonogramRowLogic nonogramRowLogic = new NonogramRowLogic();

        List<List<String>> o09923_solution_board =
                List.of(
                        List.of("X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "O", "X", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "O", "O", "X", "X"),
                        List.of("X", "X", "X", "X", "X", "O", "O", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "O", "O", "X"),
                        List.of("X", "X", "X", "X", "O", "O", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "O", "O"),
                        List.of("X", "X", "X", "X", "O", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "O"),
                        List.of("X", "X", "X", "O", "O", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "O", "O", "X", "O"),
                        List.of("X", "X", "X", "O", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                        List.of("X", "X", "X", "O", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "X", "O", "O", "X"),
                        List.of("X", "X", "O", "O", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "O", "O", "X", "O"),
                        List.of("X", "O", "O", "X", "X", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "X", "O", "O"),
                        List.of("O", "O", "X", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "O", "O", "X", "X", "X", "X", "O", "X", "X", "O", "O", "X"),
                        List.of("O", "X", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "O", "O", "O", "O", "X", "O", "O", "O", "X", "X", "O", "X", "X"),
                        List.of("O", "X", "O", "O", "O", "X", "X", "O", "O", "X", "X", "X", "X", "O", "O", "O", "X", "O", "O", "X", "X", "X", "O", "X", "X"),
                        List.of("O", "O", "O", "O", "O", "X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "O", "X", "X", "X", "X", "O", "O", "X"),
                        List.of("X", "O", "O", "O", "O", "X", "X", "O", "O", "X", "X", "X", "X", "O", "O", "X", "X", "O", "O", "X", "X", "X", "X", "O", "X"),
                        List.of("X", "X", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "X", "O", "X", "X", "X", "X", "O", "X", "X", "X", "X", "O", "O"),
                        List.of("X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "O", "X", "X", "O", "O", "O", "O", "O", "O", "X", "O", "X", "X", "X", "O"),
                        List.of("X", "X", "X", "X", "X", "X", "O", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "O"),
                        List.of("X", "X", "X", "X", "X", "X", "O", "O", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "O"),
                        List.of("X", "X", "X", "X", "X", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X", "O", "O", "O", "X", "X", "X", "X", "O", "O", "O"),
                        List.of("X", "X", "X", "O", "O", "O", "X", "X", "O", "O", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "X", "O", "O", "O"),
                        List.of("X", "X", "O", "O", "X", "O", "O", "X", "X", "O", "O", "O", "X", "X", "X", "X", "O", "O", "X", "X", "O", "O", "O", "O", "O"),
                        List.of("X", "O", "O", "X", "X", "X", "O", "O", "X", "X", "X", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "O", "O", "O", "O"),
                        List.of("O", "O", "O", "O", "X", "X", "X", "O", "O", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X"),
                        List.of("O", "O", "O", "O", "O", "X", "X", "X", "O", "O", "X", "O", "O", "O", "O", "X", "O", "O", "O", "O", "X", "X", "X", "X", "X"),
                        List.of("O", "O", "O", "O", "O", "O", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X"),
                        List.of("X", "O", "O", "O", "O", "O", "X", "X", "X", "X", "O", "O", "O", "O", "O", "X", "O", "O", "O", "O", "O", "O", "X", "X", "X"),
                        List.of("X", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "O", "O", "O", "O", "X", "O", "O", "O", "O", "O", "O", "O", "X", "X"),
                        List.of("X", "X", "O", "O", "O", "O", "O", "O", "X", "X", "X", "X", "O", "O", "X", "X", "O", "O", "O", "O", "X", "X", "O", "O", "X"),
                        List.of("X", "X", "X", "O", "O", "O", "O", "O", "O", "X", "X", "X", "O", "O", "X", "X", "O", "O", "O", "O", "X", "X", "X", "O", "O"),
                        List.of("X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "X", "X", "O", "O", "X", "X", "O", "O", "O", "X", "X", "X", "X", "X", "O"),
                        List.of("X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "X", "O", "O", "X", "O", "O", "O", "O", "X", "X", "X", "X", "X", "X"),
                        List.of("X", "X", "X", "O", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "O", "X", "X", "X", "X"),
                        List.of("X", "X", "O", "O", "X", "X", "X", "X", "X", "O", "O", "O", "O", "O", "O", "O", "O", "X", "X", "X", "O", "O", "X", "X", "X"),
                        List.of("X", "O", "O", "O", "X", "X", "X", "X", "X", "X", "X", "X", "O", "O", "O", "O", "X", "X", "X", "X", "O", "O", "O", "X", "X")
                );

        nonogramRowLogic.setNonogramSolutionBoard(o09923_solution_board);

        return NonogramLogic.builder()
                .nonogramRowLogic(nonogramRowLogic)
                .build();
    }
}
