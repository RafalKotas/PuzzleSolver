package com.puzzlesolverappbackend.puzzleAppFileManager.interfaces;

public interface NonogramLogicPrinter {

    void printNonogramBoard();

    void printNonogramBoardWithMarks();

    void printRowsSequencesRanges();

    void printColumnsSequencesRanges();

    void printLogs();

    void printStatsAfterBasicActionsMade();

    void printAffectedRowsAndColumns(boolean printAffectedArrays, boolean printAffectedSum);
}
