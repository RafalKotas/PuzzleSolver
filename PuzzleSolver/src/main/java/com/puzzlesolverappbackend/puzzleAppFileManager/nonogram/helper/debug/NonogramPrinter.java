package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.helper.debug;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.logic.NonogramLogic;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.IntStream;

@Getter
@Setter
public class NonogramPrinter implements NonogramLogicPrinter {

    private final NonogramLogic logic;

    public NonogramPrinter(NonogramLogic logic) {
        this.logic = logic;
    }

    @Override
    public void printNonogramBoard() {
        IntStream.range(0, logic.getNonogramSolutionBoard().size())
                .mapToObj(rowIndex -> logic.getNonogramSolutionBoard().get(rowIndex) + " " + rowIndex)
                .forEach(System.out::println);
    }

    @Override
    public void printNonogramBoardWithMarks() {
        logic.getNonogramSolutionBoardWithMarks().forEach(System.out::println);
    }

    @Override
    public void printRowsSequencesRanges() {
        IntStream.range(0, logic.getNonogramRowLogic().getRowsSequencesRanges().size())
                .forEach(i -> System.out.println(i + " " + logic.getNonogramRowLogic().getRowsSequencesRanges().get(i)));
    }

    @Override
    public void printColumnsSequencesRanges() {
        IntStream.range(0, logic.getNonogramColumnLogic().getColumnsSequencesRanges().size())
                .forEach(i -> System.out.println(i + " " + logic.getNonogramColumnLogic().getColumnsSequencesRanges().get(i)));
    }

    @Override
    public void printLogs() {
        List<String> logs = logic.getLogs();
        IntStream.range(0, logs.size())
                .forEach(i -> System.out.println(i + " : " + logs.get(i)));
    }

    @Override
    public void printStats() {
        System.out.println("Nonogram board:");
        printNonogramBoard();

        System.out.printf("%-12s %-12s %-12s | %-12s %-12s %-12s%n",
                "'X' placed", "'X' total", "'X' percent",
                "'O' placed", "'O' total", "'O' percent");

        System.out.printf("%-12s %-12s %-12s | %-12s %-12s %-12s%n",
                logic.fieldsWithXPlaced(), logic.fieldsToPlaceXTotal(), logic.fieldsWithXPlacedPercent(),
                logic.fieldsColoured(), logic.fieldsToColourTotal(), logic.fieldsColouredPercent());

        System.out.println("Overall completion percentage: " + logic.getCompletionPercentage() + "%");
        System.out.println("possible fields to make decision: " + logic.getAvailableChoices());
        printLogs();
    }
}

