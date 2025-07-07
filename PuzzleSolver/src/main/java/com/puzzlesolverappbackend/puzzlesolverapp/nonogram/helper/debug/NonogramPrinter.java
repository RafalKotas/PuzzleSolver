package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.debug;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Setter
@Slf4j
public class NonogramPrinter implements NonogramLogicPrinter {

    private final NonogramLogic logic;

    private static final String LOG_NONOGRAM_PROPS_FORMAT = "%3d %s";

    public NonogramPrinter(NonogramLogic logic) {
        this.logic = logic;
    }

    @Override
    public void printNonogramBoard() {
        int height = logic.getNonogramRules().getHeight();
        for(int rowIdx = 0; rowIdx < height; rowIdx++) {
            log.info(String.format(LOG_NONOGRAM_PROPS_FORMAT, rowIdx, logic.getNonogramSolutionBoard().get(rowIdx)));
        }
    }

    @Override
    public void printNonogramBoardWithMarks() {
        int height = logic.getNonogramRules().getHeight();
        for(int rowIdx = 0; rowIdx < height; rowIdx++) {
            log.info(String.format(LOG_NONOGRAM_PROPS_FORMAT, rowIdx, logic.getNonogramSolutionBoardWithMarks().get(rowIdx)));
        }
    }

    @Override
    public void printRowsSequencesRanges() {
        List<List<List<Integer>>> rowsSequencesRanges = logic.getNonogramRowLogic().getRowsSequencesRanges();
        for(int rowIdx = 0; rowIdx < rowsSequencesRanges.size(); rowIdx++) {
            log.info(String.format(LOG_NONOGRAM_PROPS_FORMAT, rowIdx, rowsSequencesRanges.get(rowIdx)));
        }
    }

    @Override
    public void printColumnsSequencesRanges() {
        List<List<List<Integer>>> columnsSequencesRanges = logic.getNonogramColumnLogic().getColumnsSequencesRanges();
        for(int columnIdx = 0; columnIdx < columnsSequencesRanges.size(); columnIdx++) {
            log.info(String.format(LOG_NONOGRAM_PROPS_FORMAT, columnIdx, columnsSequencesRanges.get(columnIdx)));
        }
    }

    @Override
    public void printLogs() {
        List<String> logs = logic.getLogs();
        for (int logIdx = 0; logIdx < logs.size(); logIdx++) {
            log.info(String.format(LOG_NONOGRAM_PROPS_FORMAT, logIdx, logs.get(logIdx)));
        }
    }

    @Override
    public void printStats() {
        log.info("Nonogram board:");
        printNonogramBoard();

        log.info(String.format("%-12s %-12s %-12s | %-12s %-12s %-12s",
                "'X' placed", "'X' total", "'X' percent",
                "'O' placed", "'O' total", "'O' percent"));

        log.info(String.format("%-12s %-12s %-12s | %-12s %-12s %-12s",
                logic.fieldsWithXPlaced(), logic.fieldsToPlaceXTotal(), logic.fieldsWithXPlacedPercent(),
                logic.fieldsColoured(), logic.fieldsToColourTotal(), logic.fieldsColouredPercent()));

        log.info("Overall completion percentage: {}%", logic.getCompletionPercentage());
        log.info("Possible fields to make decision: {}", logic.getAvailableChoices());
        printLogs();
    }
}

