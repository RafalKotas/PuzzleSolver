package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramRules;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramState;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.solutions.NonogramSolutionDecision;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.model.NonogramActionDetails;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.model.NonogramConstants.X_FIELD;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramBoardUtils.isFieldColoured;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Slf4j
public abstract class NonogramLogicParams {

    protected final static Logger logger = LoggerFactory.getLogger(NonogramLogic.class);
    public static boolean SHOW_REPETITIONS = false;
    protected String tmpLog;

    protected NonogramRules nonogramRules;
    protected NonogramState nonogramState;
    protected List<List<String>> nonogramSolutionBoard;
    protected List<List<String>> nonogramSolutionBoardWithMarks;

    protected List<NonogramActionDetails> actionsToDoList = new ArrayList<>();
    protected List<String> logs = new ArrayList<>();
    protected List<NonogramSolutionDecision> availableChoices;

    public NonogramLogicParams(NonogramRules nonogramRules,
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
        List<String> solutionBoardColumn = new ArrayList<>();

        for (int rowIdx = 0; rowIdx < this.getNonogramRules().getHeight(); rowIdx++) {
            solutionBoardColumn.add(this.nonogramSolutionBoard.get(rowIdx).get(columnIdx));
        }

        return solutionBoardColumn;
    }

    public List<String> getNonogramBoardColumnWithMarks(int columnIdx) {
        List<String> solutionBoardColumnWithMarks = new ArrayList<>();

        for (int rowIdx = 0; rowIdx < this.getNonogramRules().getHeight(); rowIdx++) {
            solutionBoardColumnWithMarks.add(this.nonogramSolutionBoardWithMarks.get(rowIdx).get(columnIdx));
        }

        return solutionBoardColumnWithMarks;
    }

    protected void addLog() {
        if (this.tmpLog.isEmpty()) {
            System.out.println("Trying to add empty log!!!");
        } else {
            this.logs.add(this.tmpLog);
        }
    }

    public void printSolutionBoardAsCode() {
        int rowIdx = 0;
        int columnIdx;
        System.out.println("List.of(");
        for (List<String> solutionBoardRow : this.getNonogramSolutionBoard()) {
            columnIdx = 0;
            System.out.print("List.of(");
            for (String boardCell : solutionBoardRow) {
                System.out.print("\"" + boardCell + "\"");
                if (columnIdx != solutionBoardRow.size() - 1) {
                    System.out.print(", ");
                }
                columnIdx++;
            }
            if (rowIdx != this.getNonogramSolutionBoard().size() - 1) {
                System.out.println("),");
            } else {
                System.out.println(")");
            }

            rowIdx++;
        }
        System.out.println(");");
    }

    public void printSolutionBoard() {
        for (List<String> solutionBoardRow : this.getNonogramSolutionBoard()) {
            System.out.println(solutionBoardRow);
        }
    }

    public void printSolutionBoardWithMarks() {
        for (List<String> solutionBoardRowWithMarks : this.getNonogramSolutionBoardWithMarks()) {
            System.out.println(solutionBoardRowWithMarks);
        }
    }
}
