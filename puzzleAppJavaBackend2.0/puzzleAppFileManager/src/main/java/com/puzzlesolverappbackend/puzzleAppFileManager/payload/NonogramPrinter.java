package com.puzzlesolverappbackend.puzzleAppFileManager.payload;

import com.puzzlesolverappbackend.puzzleAppFileManager.interfaces.NonogramLogicPrinter;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.IntStream;

@Getter
@Setter
public class NonogramPrinter extends NonogramLogicParams implements NonogramLogicPrinter {

    /**
     * prints nonogramSolutionBoard(only "X"/"O"/"-") in readable format
     */
    public void printNonogramBoard() {
        List<String> rowsWithIndexes = IntStream.range(0, this.getNonogramSolutionBoard().size())
                .mapToObj(rowIndex -> this.getNonogramSolutionBoard().get(rowIndex) + " " + rowIndex)
                .toList();
        for(String boardRow : rowsWithIndexes) {
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

        //1
        this.affectedRowsToFillOverlappingFields = nonogramLogic.getAffectedRowsToFillOverlappingFields();
        //2
        this.affectedColumnsToMarkAvailableSequences = nonogramLogic.getAffectedColumnsToMarkAvailableSequences();
        //3
        this.affectedColumnsToFillOverlappingFields = nonogramLogic.getAffectedColumnsToFillOverlappingFields();
        //4
        this.affectedRowsToMarkAvailableSequences = nonogramLogic.getAffectedRowsToMarkAvailableSequences();
        //5
        this.affectedRowsToCorrectSequencesRanges = nonogramLogic.getAffectedRowsToCorrectSequencesRanges();
        //6
        this.affectedRowsToCorrectSequencesRangesWhenMetColouredField = nonogramLogic.getAffectedRowsToCorrectSequencesRangesWhenMetColouredField();
        //7
        this.affectedRowsToCorrectSequencesRangesIfXOnWay = nonogramLogic.getAffectedRowsToCorrectSequencesRangesIfXOnWay();
        //8
        this.affectedColumnsToCorrectSequencesRanges = nonogramLogic.getAffectedColumnsToCorrectSequencesRanges();
        //9
        this.affectedColumnsToCorrectSequencesRangesWhenMetColouredField = nonogramLogic.getAffectedColumnsToCorrectSequencesRangesWhenMetColouredField();
        //10
        this.affectedColumnsToCorrectSequencesRangesIfXOnWay = nonogramLogic.getAffectedColumnsToCorrectSequencesRangesIfXOnWay();
        //11
        this.affectedRowsToPlaceXsAtUnreachableFields = nonogramLogic.getAffectedRowsToPlaceXsAtUnreachableFields();
        //12
        this.affectedColumnsToPlaceXsAtUnreachableFields = nonogramLogic.getAffectedColumnsToPlaceXsAtUnreachableFields();
        //13
        this.affectedRowsToPlaceXsAroundLongestSequences = nonogramLogic.getAffectedRowsToPlaceXsAroundLongestSequences();
        //14
        this.affectedColumnsToPlaceXsAroundLongestSequences = nonogramLogic.getAffectedColumnsToPlaceXsAroundLongestSequences();
        //15
        this.affectedRowsToPlaceXsAtTooShortEmptySequences = nonogramLogic.getAffectedColumnsToPlaceXsAtTooShortEmptySequences();
        //16
        this.affectedColumnsToPlaceXsAtTooShortEmptySequences = nonogramLogic.getAffectedColumnsToPlaceXsAtTooShortEmptySequences();
        //17
        this.affectedRowsToExtendColouredFieldsNearX = nonogramLogic.getAffectedRowsToExtendColouredFieldsNearX();
        //18
        this.affectedColumnsToExtendColouredFieldsNearX = nonogramLogic.getAffectedColumnsToExtendColouredFieldsNearX();
    }

    /**
     * prints nonogramSolutionBoardWithMarks(fields like "RxCy"/"Rx--"/"--Cx"/"----") in readable format
     */
    public void printNonogramBoardWithMarks() {
        for(List<String> boardRow : this.getNonogramSolutionBoardWithMarks()) {
            System.out.println(boardRow);
        }
    }

    /**
     * prints current rows sequences ranges line by line
     */
    public void printRowsSequencesRanges() {
        int rowIdx = 0;
        for(List<List<Integer>> rowSequencesRanges : this.getRowsSequencesRanges()) {
            System.out.println(rowIdx + " " + rowSequencesRanges);
            rowIdx++;
        }
    }

    /**
     * prints current columns sequences ranges line by line
     */
    public void printColumnsSequencesRanges() {
        int colIdx = 0;
        for(List<List<Integer>> colSequencesRanges : this.getColumnsSequencesRanges()) {
            System.out.println(colIdx + " " + colSequencesRanges);
            colIdx++;
        }
    }

    /**
     * print solver logs line by line
     */
    public void printLogs() {
        int logIndex = 0;
        if(!this.getLogs().isEmpty()) {
            for(String log : this.getLogs()) {
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

    /**
     * prints affected rows and columns at current time
     */
    public void printAffectedRowsAndColumns(boolean printAffectedArrays, boolean printAffectedCount) {
        //1
        int affectedRowsToColourOverlappingFieldsCount = this.getAffectedRowsToFillOverlappingFields().size();
        //2
        int affectedColumnsToMarkAvailableSequencesCount = this.getAffectedColumnsToMarkAvailableSequences().size();
        //3
        int affectedColumnsToColourOverlappingFieldsCount = this.getAffectedColumnsToFillOverlappingFields().size();
        //4
        int affectedRowsToMarkAvailableSequencesCount = this.getAffectedRowsToMarkAvailableSequences().size();
        //5
        int affectedRowsToCorrectSequencesRangesCount = this.getAffectedRowsToCorrectSequencesRanges().size();
        //6
        int affectedRowsToCorrectSequencesWhenMetColouredFieldCount = this.getAffectedRowsToCorrectSequencesRangesWhenMetColouredField().size();
        //7
        int affectedRowsToCorrectSequencesIfXOnWayCount = this.getAffectedRowsToCorrectSequencesRangesIfXOnWay().size();
        //8
        int affectedColumnsToCorrectSequencesRangesCount = this.getAffectedColumnsToCorrectSequencesRanges().size();
        //9
        int affectedColumnsToCorrectSequencesWhenMetColouredFieldCount = this.getAffectedColumnsToCorrectSequencesRangesWhenMetColouredField().size();
        //10
        int affectedColumnsToCorrectSequencesIfXOnWayCount = this.getAffectedColumnsToCorrectSequencesRangesIfXOnWay().size();
        //11
        int affectedRowsToPlaceXsAtUnreachableFieldsCount = this.getAffectedRowsToPlaceXsAtUnreachableFields().size();
        //12
        int affectedColumnsToPlaceXsAtUnreachableFieldsCount = this.getAffectedColumnsToPlaceXsAtUnreachableFields().size();
        //13
        int affectedRowsToPlaceXsAroundLongestSequencesCount = this.getAffectedRowsToPlaceXsAroundLongestSequences().size();
        //14
        int affectedColumnsToPlaceXsAroundLongestSequencesCount = this.getAffectedColumnsToPlaceXsAroundLongestSequences().size();
        //15
        int affectedRowsToPlaceXsAtTooShortEmptySequencesCount = this.getAffectedRowsToPlaceXsAtTooShortEmptySequences().size();
        //16
        int affectedColumnsToPlaceXsAtTooShortEmptySequencesCount = this.getAffectedColumnsToPlaceXsAtTooShortEmptySequences().size();
        //17
        int affectedRowsToExtendColouredFieldsNearXCount = this.getAffectedRowsToExtendColouredFieldsNearX().size();
        //18
        int affectedColumnsToExtendColouredFieldsNearXCount = this.getAffectedColumnsToExtendColouredFieldsNearX().size();

        int sumOfAffectedRows = affectedRowsToColourOverlappingFieldsCount + affectedRowsToMarkAvailableSequencesCount +
                affectedRowsToCorrectSequencesRangesCount + affectedRowsToCorrectSequencesWhenMetColouredFieldCount +
                affectedRowsToCorrectSequencesIfXOnWayCount + affectedRowsToPlaceXsAtUnreachableFieldsCount +
                affectedRowsToPlaceXsAroundLongestSequencesCount + affectedRowsToPlaceXsAtTooShortEmptySequencesCount
                + affectedRowsToExtendColouredFieldsNearXCount;

        int sumOfAffectedColumns = affectedColumnsToColourOverlappingFieldsCount + affectedColumnsToMarkAvailableSequencesCount +
                affectedColumnsToCorrectSequencesRangesCount + affectedColumnsToCorrectSequencesWhenMetColouredFieldCount +
                affectedColumnsToCorrectSequencesIfXOnWayCount + affectedColumnsToPlaceXsAtUnreachableFieldsCount +
                affectedColumnsToPlaceXsAroundLongestSequencesCount + affectedColumnsToPlaceXsAtTooShortEmptySequencesCount
                + affectedColumnsToExtendColouredFieldsNearXCount;

        if(printAffectedArrays) {
            System.out.println("-".repeat(50));
            System.out.println("Rows affected: ");
            System.out.println("affectedRowsToColourOverlappingFields=" + getAffectedRowsToFillOverlappingFields());
            System.out.println("affectedRowsToMarkAvailableSequences=" + getAffectedRowsToMarkAvailableSequences());
            System.out.println("affectedRowsToCorrectSequencesRanges=" + getAffectedRowsToCorrectSequencesRanges());
            System.out.println("affectedRowsToCorrectSequencesRangesWhenMetColouredField=" + getAffectedRowsToCorrectSequencesRangesWhenMetColouredField());
            System.out.println("affectedRowsToChangeSequencesRangeIfXOnWay=" + getAffectedRowsToCorrectSequencesRangesIfXOnWay());
            System.out.println("affectedRowsToPlaceXsAtUnreachableFields=" + getAffectedRowsToPlaceXsAtUnreachableFields());
            System.out.println("affectedRowsToPlaceXsAroundLongestSequences=" + getAffectedRowsToPlaceXsAroundLongestSequences());
            System.out.println("affectedRowsToPlaceXsAtTooShortEmptySequences=" + getAffectedRowsToPlaceXsAtTooShortEmptySequences());
            System.out.println("affectedRowsToExtendColouredFieldsNearX=" + getAffectedRowsToExtendColouredFieldsNearX());
            System.out.println("-".repeat(50));
            System.out.println("Columns affected: ");
            System.out.println("affectedColumnsToColourOverlappingFields=" + getAffectedColumnsToFillOverlappingFields());
            System.out.println("affectedColumnsToMarkAvailableSequences=" + getAffectedColumnsToMarkAvailableSequences());
            System.out.println("affectedColumnsToCorrectSequencesRanges=" + getAffectedColumnsToCorrectSequencesRanges());
            System.out.println("affectedColumnsToCorrectSequencesRangesWhenMetColouredField=" + getAffectedColumnsToCorrectSequencesRangesWhenMetColouredField());
            System.out.println("affectedColumnsToChangeSequencesRangeIfXOnWay=" + getAffectedColumnsToCorrectSequencesRangesIfXOnWay());
            System.out.println("affectedColumnsToPlaceXsAtUnreachableFields=" + getAffectedColumnsToPlaceXsAtUnreachableFields());
            System.out.println("affectedColumnsToPlaceXsAroundLongestSequences=" + getAffectedColumnsToPlaceXsAroundLongestSequences());
            System.out.println("affectedColumnsToPlaceXsAtTooShortEmptySequences=" + getAffectedColumnsToPlaceXsAtTooShortEmptySequences());
            System.out.println("affectedColumnsToExtendColouredFieldsNearX=" + getAffectedColumnsToExtendColouredFieldsNearX());
            System.out.println("-".repeat(50));
        }
        if(printAffectedCount) {
            System.out.println("Affected rows sum(can be repeated in different arrays): " + sumOfAffectedRows);
            System.out.println("Affected columns sum(can be repeated in different arrays): " + sumOfAffectedColumns);
        }
    }
}
