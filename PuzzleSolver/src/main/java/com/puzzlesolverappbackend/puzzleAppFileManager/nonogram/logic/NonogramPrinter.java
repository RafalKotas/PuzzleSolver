package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.NonogramColumnLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.NonogramRowLogic;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.IntStream;

@Getter
@Setter
public class NonogramPrinter extends NonogramLogicParams implements NonogramLogicPrinter {

    protected NonogramColumnLogic nonogramColumnLogic;
    protected NonogramRowLogic nonogramRowLogic;

    public void printNonogramBoard() {
        List<String> rowsWithIndexes = IntStream.range(0, this.getNonogramSolutionBoard().size())
                .mapToObj(rowIndex -> this.getNonogramSolutionBoard().get(rowIndex) + " " + rowIndex)
                .toList();
        for (String boardRow : rowsWithIndexes) {
            System.out.println(boardRow);
        }
    }

    public NonogramPrinter(NonogramLogic nonogramLogic) {
        this.SHOW_REPETITIONS = nonogramLogic.SHOW_REPETITIONS;
        this.logs = nonogramLogic.getLogs();
        this.tmpLog = nonogramLogic.getTmpLog();
        this.nonogramRowLogic = nonogramLogic.getNonogramRowLogic();
        this.nonogramColumnLogic = nonogramLogic.getNonogramColumnLogic();
        this.nonogramSolutionBoardWithMarks = nonogramLogic.getNonogramSolutionBoardWithMarks();
        this.nonogramSolutionBoard = nonogramLogic.getNonogramSolutionBoard();

        this.availableChoices = nonogramLogic.getAvailableChoices();
    }

    public void printNonogramBoardWithMarks() {
        for (List<String> boardRow : this.getNonogramSolutionBoardWithMarks()) {
            System.out.println(boardRow);
        }
    }

    public void printRowsSequencesRanges() {
        int rowIdx = 0;
        for (List<List<Integer>> rowSequencesRanges : this.getNonogramRowLogic().getRowsSequencesRanges()) {
            System.out.println(rowIdx + " " + rowSequencesRanges);
            rowIdx++;
        }
    }

    public void printColumnsSequencesRanges() {
        int colIdx = 0;
        for (List<List<Integer>> colSequencesRanges : this.getNonogramColumnLogic().getColumnsSequencesRanges()) {
            System.out.println(colIdx + " " + colSequencesRanges);
            colIdx++;
        }
    }

    public void printLogs() {
        int logIndex = 0;
        if (!this.getLogs().isEmpty()) {
            for (String log : this.getLogs()) {
                System.out.println(logIndex + " : "  + log);
                logIndex++;
            }
        }
    }

    public void printStats() {
        int fieldsXPlaced = this.fieldsWithXPlaced();
        int fieldsColoured = this.fieldsColoured();
        int fieldsXTotal  = this.fieldsToPlaceXTotal();
        this.printNonogramBoard();
        System.out.printf("%-12s %-12s %-12s | %-12s %-12s %-12s%n",
                "'X' placed", "'X' total", "'X' percent",
                "'O' placed", "'O' total", "'O' percent");
        System.out.printf("%-12s %-12s %-12s | %-12s %-12s %-12s%n",
                fieldsXPlaced, fieldsXTotal, this.fieldsWithXPlacedPercent(),
                fieldsColoured, this.fieldsToColourTotal(), this.fieldsColouredPercent());
        System.out.println("Overall completion percentage: " + this.getCompletionPercentage() + "%");
        //System.out.println("newStepsMade: " + this.getNewStepsMade());
        System.out.println("possible fields to make decision: " + this.getAvailableChoices());
        this.printLogs();
    }
}
