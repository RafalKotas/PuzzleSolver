package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramActionDetails;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solutions.NonogramSolutionDecision;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramConstants.X_FIELD;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.isFieldColoured;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
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

    protected List<String> getRowCopy(int rowIdx) {
        return new ArrayList<>(this.nonogramSolutionBoard.get(rowIdx));
    }

    protected List<String> getColumnCopy(int columnIdx) {
        return new ArrayList<>(this.getNonogramBoardColumn(columnIdx));
    }

    public int fieldsColoured() {
        int colouredFieldsOnBoard = 0;
        Field potentiallyColouredField;
        for (int rowIndex = 0; rowIndex < this.getNonogramRules().getHeight(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < this.getNonogramRules().getWidth(); columnIndex++) {
                potentiallyColouredField = new Field(rowIndex, columnIndex);
                if (isFieldColoured(this.nonogramSolutionBoard, potentiallyColouredField)) {
                    colouredFieldsOnBoard++;
                }
            }
        }
        return colouredFieldsOnBoard;
    }

    public int fieldsWithXPlaced() {
        int fieldsWithXOnBoard = 0;
        for (int rowIndex = 0; rowIndex < this.getNonogramRules().getHeight(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < this.getNonogramRules().getWidth(); columnIndex++) {
                if (this.getNonogramSolutionBoard().get(rowIndex).get(columnIndex).equals(X_FIELD)) {
                    fieldsWithXOnBoard++;
                }
            }
        }
        return fieldsWithXOnBoard;
    }

    public int fieldsFilled() {
        return this.fieldsWithXPlaced() + this.fieldsColoured();
    }

    public int nonogramAreaInFieldsCount() {
        return this.getNonogramRules().getWidth() * this.getNonogramRules().getHeight();
    }

    public int fieldsToColourTotal() {
        int fieldsToColourOnBoard = 0;
        int fieldsToColourInRow;

        for (int rowIndex = 0; rowIndex < this.getNonogramRules().getHeight(); rowIndex++) {
            fieldsToColourInRow = this.getNonogramRules().getRowSequencesLengths()
                    .get(rowIndex)
                    .stream()
                    .reduce(0, Integer::sum);
            fieldsToColourOnBoard += fieldsToColourInRow;
        }

        return fieldsToColourOnBoard;
    }

    public int fieldsToPlaceXTotal() {
        return this.nonogramAreaInFieldsCount() - this.fieldsToColourTotal();
    }

    public double getPercent(int part, int whole) {
        return Math.round(((double)(part) / whole) * 10000 ) / 100.0;
    }

    public double fieldsColouredPercent() {
        return getPercent(fieldsColoured(), fieldsToColourTotal());
    }

    public double fieldsWithXPlacedPercent() {
        return getPercent(this.fieldsWithXPlaced(), this.fieldsToPlaceXTotal());
    }

    public double getCompletionPercentage() {
        int xPlaced = this.fieldsWithXPlaced();
        int coloured = this.fieldsColoured();

        return getPercent(xPlaced + coloured, this.nonogramAreaInFieldsCount());
    }

    public boolean isSolved() {
        return this.fieldsFilled() == this.nonogramAreaInFieldsCount();
    }

    public List<String> getNonogramBoardColumn(int columnIdx) {
        return IntStream.range(0, this.getNonogramRules().getHeight())
                .mapToObj(rowIdx -> this.nonogramSolutionBoard.get(rowIdx).get(columnIdx))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<String> getNonogramBoardColumnWithMarks(int columnIdx) {
        return IntStream.range(0, this.getNonogramRules().getHeight())
                .mapToObj(rowIdx -> this.nonogramSolutionBoardWithMarks.get(rowIdx).get(columnIdx))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    protected void addLog() {
        if (this.tmpLog.isEmpty()) {
            log.warn("Trying to add empty log!!!");
        } else {
            this.logs.add(this.tmpLog);
        }
    }
}
