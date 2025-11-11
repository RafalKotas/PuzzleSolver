package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramActionDetails;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solutions.NonogramSolutionDecision;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Setter
@Getter
@NoArgsConstructor
@Slf4j
public abstract class NonogramLogicParams {

    protected static final Logger logger = LoggerFactory.getLogger(NonogramLogicParams.class);
    public static final boolean SHOW_REPETITIONS = false;
    protected String tmpLog;

    protected NonogramRules nonogramRules;
    protected NonogramState nonogramState;
    protected List<List<String>> nonogramSolutionBoard;
    protected List<List<String>> nonogramSolutionBoardWithMarks;

    protected List<NonogramActionDetails> actionsToDoList = new ArrayList<>();
    protected List<String> logs = new ArrayList<>();
    protected List<NonogramSolutionDecision> availableChoices;

    protected NonogramLogicParams(NonogramRules nonogramRules,
                                  List<List<String>> nonogramSolutionBoard,
                                  List<List<String>> nonogramSolutionBoardWithMarks,
                                  List<NonogramActionDetails> actionsToDoList,
                                  NonogramState nonogramState,
                                  List<String> logs) {
        this.nonogramRules = nonogramRules;
        this.nonogramSolutionBoard = nonogramSolutionBoard;
        this.nonogramSolutionBoardWithMarks = nonogramSolutionBoardWithMarks;
        this.actionsToDoList = actionsToDoList;
        this.nonogramState = nonogramState;
        this.logs = logs;
    }

    public List<String> getRowCopy(int rowIdx) {
        return new ArrayList<>(this.nonogramSolutionBoard.get(rowIdx));
    }

    public List<String> getColumnCopy(int columnIdx) {
        return new ArrayList<>(this.getNonogramBoardColumn(columnIdx));
    }

    public List<String> getNonogramBoardRow(int rowIdx) {
        return this.getNonogramSolutionBoard().get(rowIdx);
    }

    public List<String> getNonogramBoardColumn(int columnIdx) {
        return IntStream.range(0, this.getNonogramRules().getHeight())
                .mapToObj(rowIdx -> this.nonogramSolutionBoard.get(rowIdx).get(columnIdx))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void addLog() {
        if (this.tmpLog.isEmpty()) {
            log.warn("Trying to add empty log!!!");
        } else {
            this.logs.add(this.tmpLog);
        }
    }
}
