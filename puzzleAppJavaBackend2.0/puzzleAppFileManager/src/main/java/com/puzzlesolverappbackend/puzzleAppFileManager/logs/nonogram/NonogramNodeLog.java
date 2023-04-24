package com.puzzlesolverappbackend.puzzleAppFileManager.logs.nonogram;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class NonogramNodeLog {

    // logs from actions done by using heuristics at given node
    List<String> heuristicsLogs;

    // logs for actions done after every decision made
    List<NonogramGuessActionsLog> guessesLogs;

    public NonogramNodeLog(List<String> heuristicsLogs) {
        this.heuristicsLogs = heuristicsLogs;
        this.guessesLogs = new ArrayList<>();
    }
}
