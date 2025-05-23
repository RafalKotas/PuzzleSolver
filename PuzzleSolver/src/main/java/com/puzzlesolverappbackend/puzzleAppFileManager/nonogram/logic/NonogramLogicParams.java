package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.NonogramActionDetails;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.ActionDependencyMap;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.NonogramConstants.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramBoardUtils.isFieldColoured;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramBoardUtils.isFieldWithX;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Slf4j
public abstract class NonogramLogicParams {

    protected final static Logger logger = LoggerFactory.getLogger(NonogramLogic.class);
    protected boolean SHOW_REPETITIONS = false;
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

    public void colourFieldAtGivenPosition(Field fieldToColour, String mask) {
        int fieldRowIdx = fieldToColour.getRowIdx();
        int fieldColIdx = fieldToColour.getColumnIdx();
        String currentFieldWithMarks = this.nonogramSolutionBoardWithMarks.get(fieldRowIdx).get(fieldColIdx);
        if (areFieldIndexesValid(fieldToColour)) {
            this.nonogramSolutionBoard.get(fieldRowIdx).set(fieldColIdx, COLOURED_FIELD);
            this.nonogramSolutionBoardWithMarks.get(fieldRowIdx).set(fieldColIdx, getUpdatedFieldWithMarks(currentFieldWithMarks, mask));
        }
    }

    public void placeXAtGivenField(Field xField, boolean exclude) {
        int fieldColIdx = xField.getColumnIdx();
        int fieldRowIdx = xField.getRowIdx();

        if (areFieldIndexesValid(xField)) {
            this.nonogramSolutionBoard.get(fieldRowIdx).set(fieldColIdx, X_FIELD);
            this.nonogramSolutionBoardWithMarks.get(fieldRowIdx).set(fieldColIdx, X_FIELD.repeat(4));

            if (exclude) {
                excludeFieldLogicSpecific(xField);
            }
        }
    }

    protected void excludeFieldLogicSpecific(Field field) {

    }

    public void clearField(Field x_field) {
        int fieldColIdx = x_field.getColumnIdx();
        int fieldRowIdx = x_field.getRowIdx();
        if (areFieldIndexesValid(x_field)) {
            this.nonogramSolutionBoard.get(fieldRowIdx).set(fieldColIdx, EMPTY_FIELD);
            this.nonogramSolutionBoardWithMarks.get(fieldRowIdx).set(fieldColIdx, EMPTY_FIELD_MARKED_BOARD);
        }
    }

    protected void placeXAtGivenFields(List<Field> x_fields) {
        x_fields.forEach(field -> placeXAtGivenField(field, true));
    }












    protected void addRowAndColumnToAffectedByIdentifiers(Field field, NonogramSolveAction actionTriggered) {
        List<NonogramSolveAction> actionsToDo = ActionDependencyMap.actionDependencies.get(actionTriggered);

        int rowIdx = field.getRowIdx();
        int columnIdx = field.getColumnIdx();

        for (NonogramSolveAction actionToDo : actionsToDo) {
            if (actionToDo.isRowAction()) {
                this.actionsToDoList.add(new NonogramActionDetails(rowIdx, actionToDo, actionTriggered, false));
            } else {
                this.actionsToDoList.add(new NonogramActionDetails(columnIdx, actionToDo, actionTriggered, false));
            }
        }
    }

    protected void addColumnToAffectedActionsByIdentifiers(int columnIdx, NonogramSolveAction actionTriggered) {
        List<NonogramSolveAction> actionsToDo = ActionDependencyMap.actionDependencies.get(actionTriggered);
        for (NonogramSolveAction actionToDo : actionsToDo) {
            if (isColumnIndexValid(columnIdx)) {
                this.actionsToDoList.add(new NonogramActionDetails(columnIdx, actionToDo, actionTriggered, false));
            }
        }
    }

    protected void addRowToAffectedActionsByIdentifiers(int rowIdx, NonogramSolveAction actionTriggered) {
        List<NonogramSolveAction> actionsToDo = ActionDependencyMap.actionDependencies.get(actionTriggered);
        for (NonogramSolveAction actionToDo : actionsToDo) {
            if (isRowIndexValid(rowIdx)) {
                this.actionsToDoList.add(new NonogramActionDetails(rowIdx, actionToDo, actionTriggered, false));
            }
        }
    }

    protected boolean areFieldIndexesValid (Field fieldToValidate) {
        int fieldRowIdx = fieldToValidate.getRowIdx();
        int fieldColIdx = fieldToValidate.getColumnIdx();
        return isRowIndexValid(fieldRowIdx) && isColumnIndexValid(fieldColIdx);
    }

    protected boolean isRowIndexValid (int rowIdx) {
        return rowIdx >= 0 && rowIdx < this.getNonogramRules().getHeight();
    }

    protected boolean isColumnIndexValid (int columnIdx) {
        return columnIdx >= 0 && columnIdx < this.getNonogramRules().getWidth();
    }

    private String getUpdatedFieldWithMarks(String currentField, String mask) {
        StringBuilder updatedField = new StringBuilder();
        for (int i = 0; i < currentField.length(); i++) {
            if (currentField.charAt(i) == '-') {
                updatedField.append(mask.charAt(i));
            } else {
                updatedField.append(currentField.charAt(i));
            }
        }

        return updatedField.toString();
    }

    protected String generateAddingRowSequenceToNotToIncludeDescription(int rowIdx, int seqNo) {
        return String.format("ROW %d - seqNo = %d excluded", rowIdx, seqNo);
    }

    protected String generateAddingColumnSequenceToNotToIncludeDescription(int columnIdx, int seqNo) {
        return String.format("COLUMN %d - seqNo = %d excluded", columnIdx, seqNo);
    }

    public List<String> getNonogramBoardColumn(int columnIdx) {
        List<String> solutionBoardColumn = new ArrayList<>();

        for (int rowIdx = 0; rowIdx < this.getNonogramRules().getWidth(); rowIdx++) {
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

    protected void printSolutionBoardAsCode() {
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

    protected List<Integer> getRowSequenceMaxPossibleRange(int rowIdx, List<Integer> colouredSequencePartRange) {
        Field fieldToCheckX;

        int columnLeft = colouredSequencePartRange.get(0) - 1;
        fieldToCheckX = new Field(rowIdx, columnLeft);
        while (areFieldIndexesValid(fieldToCheckX) && !isFieldWithX(this.getNonogramSolutionBoard(), fieldToCheckX)) {
            fieldToCheckX = new Field(rowIdx, --columnLeft);
        }
        columnLeft++;

        int columnRight = colouredSequencePartRange.get(1) + 1;
        fieldToCheckX = new Field(rowIdx, columnRight);
        while (areFieldIndexesValid(fieldToCheckX) && !isFieldWithX(this.getNonogramSolutionBoard(), fieldToCheckX)) {
            fieldToCheckX = new Field(rowIdx, ++columnRight);
        }
        columnRight--;

        return new ArrayList<>(List.of(columnLeft, columnRight));
    }

    protected List<Integer> getColumnSequenceMaxPossibleRange(int columnIdx, List<Integer> colouredSequencePartRange) {
        Field fieldToCheckX;

        int rowTop = colouredSequencePartRange.get(0) - 1;
        fieldToCheckX = new Field(rowTop, columnIdx);
        while (areFieldIndexesValid(fieldToCheckX) && !isFieldWithX(this.getNonogramSolutionBoard(), fieldToCheckX)) {
            fieldToCheckX = new Field(--rowTop, columnIdx);
        }
        rowTop++;

        int rowBottom = colouredSequencePartRange.get(1) + 1;
        fieldToCheckX = new Field(rowBottom, columnIdx);
        while (areFieldIndexesValid(fieldToCheckX) && !isFieldWithX(this.getNonogramSolutionBoard(), fieldToCheckX)) {
            fieldToCheckX = new Field(++rowBottom, columnIdx);
        }
        rowBottom--;

        return new ArrayList<>(List.of(rowTop, rowBottom));
    }

    protected List<List<Integer>> getColouredSequencesPartsMaxRanges(int rowIdx, List<List<Integer>> colouredSequencesPartsRanges) {
        List<List<Integer>> colouredSequencesPartsMaxRanges = new ArrayList<>();
        List<Integer> colouredSequencePartMaxRange;

        // calculate max possible ranges for corresponding coloured sequences
        for (List<Integer> colouredSequencesPartsRange : colouredSequencesPartsRanges) {
            colouredSequencePartMaxRange = getRowSequenceMaxPossibleRange(rowIdx, colouredSequencesPartsRange);
            colouredSequencesPartsMaxRanges.add(colouredSequencePartMaxRange);
        }

        return colouredSequencesPartsMaxRanges;
    }


    protected void printSolutionBoard() {
        for (List<String> solutionBoardRow : this.getNonogramSolutionBoard()) {
            System.out.println(solutionBoardRow);
        }
    }

    protected void printSolutionBoardWithMarks() {
        for (List<String> solutionBoardRowWithMarks : this.getNonogramSolutionBoardWithMarks()) {
            System.out.println(solutionBoardRowWithMarks);
        }
    }
}
