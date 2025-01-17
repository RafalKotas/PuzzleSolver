package com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic;

import com.puzzlesolverappbackend.puzzleAppFileManager.interfaces.NonogramLogicPrinter;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.IntStream;

@Getter
@Setter
public class NonogramPrinter extends NonogramLogicParams implements NonogramLogicPrinter {

    protected NonogramColumnLogic nonogramColumnLogic;
    protected NonogramRowLogic nonogramRowLogic;

    /**
     * prints nonogramSolutionBoard(only "X"/"O"/"-") in readable format
     */
    public void printNonogramBoard() {
        List<String> rowsWithIndexes = IntStream.range(0, this.getNonogramSolutionBoard().size())
                .mapToObj(rowIndex -> this.getNonogramSolutionBoard().get(rowIndex) + " " + rowIndex)
                .toList();
        for (String boardRow : rowsWithIndexes) {
            System.out.println(boardRow);
        }
    }

    public NonogramPrinter(NonogramLogic nonogramLogic) {
        this.showRepetitions = nonogramLogic.showRepetitions;
        this.logs = nonogramLogic.getLogs();
        this.tmpLog = nonogramLogic.getTmpLog();
        this.nonogramColumnLogic = nonogramLogic.getNonogramColumnLogic();
        this.rowsSequences = nonogramLogic.getRowsSequences();
        this.columnsSequences = nonogramLogic.getColumnsSequences();
        this.nonogramSolutionBoardWithMarks = nonogramLogic.getNonogramSolutionBoardWithMarks();
        this.nonogramSolutionBoard = nonogramLogic.getNonogramSolutionBoard();
        this.rowsSequencesRanges = nonogramLogic.getRowsSequencesRanges();
        this.columnsSequencesRanges = nonogramLogic.getColumnsSequencesRanges();

        this.rowsFieldsNotToInclude = nonogramLogic.getRowsFieldsNotToInclude();
        this.columnsFieldsNotToInclude = nonogramLogic.getColumnsFieldsNotToInclude();
        this.rowsSequencesIdsNotToInclude = nonogramLogic.getRowsSequencesIdsNotToInclude();
        this.columnsSequencesIdsNotToInclude = nonogramLogic.getColumnsSequencesIdsNotToInclude();

        this.availableChoices = nonogramLogic.getAvailableChoices();
    }

    /**
     * prints nonogramSolutionBoardWithMarks(fields like "RxCy"/"Rx--"/"--Cx"/"----") in readable format
     */
    public void printNonogramBoardWithMarks() {
        for (List<String> boardRow : this.getNonogramSolutionBoardWithMarks()) {
            System.out.println(boardRow);
        }
    }

    /**
     * prints current rows sequences ranges line by line
     */
    public void printRowsSequencesRanges() {
        int rowIdx = 0;
        for (List<List<Integer>> rowSequencesRanges : this.getRowsSequencesRanges()) {
            System.out.println(rowIdx + " " + rowSequencesRanges);
            rowIdx++;
        }
    }

    /**
     * prints current columns sequences ranges line by line
     */
    public void printColumnsSequencesRanges() {
        int colIdx = 0;
        for (List<List<Integer>> colSequencesRanges : this.getColumnsSequencesRanges()) {
            System.out.println(colIdx + " " + colSequencesRanges);
            colIdx++;
        }
    }

    /**
     * print solver logs line by line
     */
    public void printLogs() {
        int logIndex = 0;
        if (!this.getLogs().isEmpty()) {
            for (String log : this.getLogs()) {
                System.out.println(logIndex + " : "  + log);
                logIndex++;
            }
        }
    }

    /**
     * function meant to print stats after using only heuristics + trial and error method to solve nonogram
     */
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
