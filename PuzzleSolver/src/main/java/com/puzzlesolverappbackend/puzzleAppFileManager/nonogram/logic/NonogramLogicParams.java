package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.NonogramActionDetails;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.NonogramSolveAction;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.NonogramConstants.COLOURED_FIELD;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.NonogramConstants.X_FIELD;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.NonogramParametersComparatorHelper.sequencesRangesEqual;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramBoardUtils.isFieldColoured;
import static com.puzzlesolverappbackend.puzzleAppFileManager.utils.ArrayUtils.rangeLength;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Slf4j
public abstract class NonogramLogicParams {

    protected final static Logger logger = LoggerFactory.getLogger(NonogramLogic.class);
    protected boolean showRepetitions = false;
    protected NonogramState nonogramState;
    protected List<NonogramActionDetails> actionsToDoList = new ArrayList<>();

    protected List<String> logs = new ArrayList<>();

    protected String tmpLog;

    protected List<List<Integer>> rowsSequences;
    protected List<List<Integer>> columnsSequences;

    protected List<List<String>> nonogramSolutionBoardWithMarks;
    protected List<List<String>> nonogramSolutionBoard;

    protected List<List<List<Integer>>> rowsSequencesRanges;
    protected List<List<List<Integer>>> columnsSequencesRanges;

    protected List<List<Integer>> rowsFieldsNotToInclude;
    protected List<List<Integer>> columnsFieldsNotToInclude;
    protected List<List<Integer>> rowsSequencesIdsNotToInclude;
    protected List<List<Integer>> columnsSequencesIdsNotToInclude;

    protected List<NonogramSolutionDecision> availableChoices;

    public void setColumnSequencesRanges(int columnIdx, List<List<Integer>> columnSequencesRanges) {
        this.getColumnsSequencesRanges().set(columnIdx, columnSequencesRanges);
    }

    public void setNonogramSolutionBoardColumn(int columnIdx, List<String> boardColumn) {
        for (int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            this.nonogramSolutionBoard.get(rowIdx).set(columnIdx, boardColumn.get(rowIdx));
        }
    }

    public void setColumnSequencesLengths(int columnIdx, List<Integer> sequencesLengths) {
        this.getColumnsSequences().set(columnIdx, sequencesLengths);
    }

    public void setRowSequencesRanges(int rowIdx, List<List<Integer>> rowSequencesRanges) {
        this.getRowsSequencesRanges().set(rowIdx, rowSequencesRanges);
    }

    public void setNonogramSolutionBoardRow(int rowIdx, List<String> boardRow) {
        for (int columnIdx = 0; columnIdx < this.getWidth(); columnIdx++) {
            this.nonogramSolutionBoard.get(rowIdx).set(columnIdx, boardRow.get(columnIdx));
        }
    }

    public void setRowSequencesLengths(int rowIdx, List<Integer> sequencesLengths) {
        this.getRowsSequences().set(rowIdx, sequencesLengths);
    }

    /**
     * @return "X" count on nonogram solution board
     */
    public int fieldsWithXPlaced() {
        int fieldsWithXOnBoard = 0;
        for (int rowIndex = 0; rowIndex < this.getHeight(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < this.getWidth(); columnIndex++) {
                if (this.getNonogramSolutionBoard().get(rowIndex).get(columnIndex).equals("X")) {
                    fieldsWithXOnBoard++;
                }
            }
        }
        return fieldsWithXOnBoard;
    }

    /**
     * @return nonogram width based on columnsSequences size
     */
    public int getWidth() {
        return this.columnsSequences.size();
    }

    /**
     * @return nonogram width based on rowSequences size
     */
    public int getHeight() {
        return this.rowsSequences.size();
    }

    /**
     * @return "O" count on nonogram solution board
     */
    public int fieldsColoured() {
        int colouredFieldsOnBoard = 0;
        Field potentiallyColouredField;
        for (int rowIndex = 0; rowIndex < this.getHeight(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < this.getWidth(); columnIndex++) {
                potentiallyColouredField = new Field(rowIndex, columnIndex);
                if (isFieldColoured(nonogramSolutionBoard, potentiallyColouredField)) {
                    colouredFieldsOnBoard++;
                }
            }
        }
        return colouredFieldsOnBoard;
    }

    /**
     * @return "X" and "O" count(sum of occurrences) on nonogram solution board
     */
    public int fieldsFilled() {
        return this.fieldsWithXPlaced() + this.fieldsColoured();
    }

    /**
     * @return total area of nonogram (fields to fill)
     */
    public int area() {
        return this.getWidth() * this.getHeight();
    }

    /**
     * @return fields that need to be proper coloured to solve nonogram
     */
    public int fieldsToColourTotal() {
        int fieldsToColourOnBoard = 0;
        int fieldsToColourInRow;

        for (int rowIndex = 0; rowIndex < this.getHeight(); rowIndex++) {
            fieldsToColourInRow = this.getRowsSequences()
                    .get(rowIndex)
                    .stream()
                    .reduce(0, Integer::sum);
            fieldsToColourOnBoard += fieldsToColourInRow;
        }

        return fieldsToColourOnBoard;
    }

    /**
     * @return percent of "O" placed (fields coloured) from all required
     */
    public double fieldsColouredPercent() {
        return getPercent(this.fieldsColoured(), this.fieldsToColourTotal());
    }

    /**
     * @param part - part of whole thing
     * @param whole - total amount
     * @return percentage of the total
     */
    public double getPercent(int part, int whole) {
        return Math.round(((double)(part) / whole) * 10000 ) / 100.0;
    }

    /**
     * @return completionPercentage of nonogram in given nonogramLogic node
     */
    public double getCompletionPercentage() {
        int xPlaced = this.fieldsWithXPlaced();
        int coloured = this.fieldsColoured();

        return getPercent(xPlaced + coloured, this.area());
    }

    /**
     * @return fields to proper place "X" to solve nonogram
     */
    public int fieldsToPlaceXTotal() {
        return this.area() - fieldsToColourTotal();
    }

    /**
     * @return percent of "X" placed from all required
     */
    public double fieldsWithXPlacedPercent() {
        return getPercent(this.fieldsWithXPlaced(), this.fieldsToPlaceXTotal());
    }

    public boolean isSolved() {
        return this.fieldsColoured() + this.fieldsWithXPlaced() == this.area();
    }

    protected void addColumnToAffectedActionsByIdentifiers(int columnIdx, List<NonogramSolveAction> actions) {
        for (NonogramSolveAction action : actions) {
            if (isColumnIndexValid(columnIdx)) {
                this.actionsToDoList.add(new NonogramActionDetails(columnIdx, action));
            }
        }
    }

    protected void addRowToAffectedActionsByIdentifiers(int rowIdx, List<NonogramSolveAction> actions) {
        for (NonogramSolveAction action : actions) {
            if (isRowIndexValid(rowIdx)) {
                this.actionsToDoList.add(new NonogramActionDetails(rowIdx, action));
            }
        }
    }

    /**
     * @param fieldToValidate - field to validate
     * @return true if field is inside nonogram board
     */
    protected boolean areFieldIndexesValid (Field fieldToValidate) {
        int fieldRowIdx = fieldToValidate.getRowIdx();
        int fieldColIdx = fieldToValidate.getColumnIdx();
        return isRowIndexValid(fieldRowIdx) && isColumnIndexValid(fieldColIdx);
    }


    /**
     * @param rowIdx - row index to validate if is in range <0, height)
     * @return true if row index is valid(in range) or false if not
     */
    protected boolean isRowIndexValid (int rowIdx) {
        return rowIdx >= 0 && rowIdx < this.getHeight();
    }

    /**
     * @param columnIdx - column index to validate if is in range <0, width - 1>
     * @return true if column index is valid(in range) or false if not
     */
    protected boolean isColumnIndexValid (int columnIdx) {
        return columnIdx >= 0 && columnIdx < this.getWidth();
    }

    /**
     * @param fieldToColour - field to colour - place COLOURED_FIELD mark ("O")
     */
    public void colourFieldAtGivenPosition(Field fieldToColour, String mask) {
        int fieldRowIdx = fieldToColour.getRowIdx();
        int fieldColIdx = fieldToColour.getColumnIdx();
        String currentFieldWithMarks = this.nonogramSolutionBoardWithMarks.get(fieldRowIdx).get(fieldColIdx);
        if (areFieldIndexesValid(fieldToColour)) {
            this.nonogramSolutionBoard.get(fieldRowIdx).set(fieldColIdx, COLOURED_FIELD);
            this.nonogramSolutionBoardWithMarks.get(fieldRowIdx).set(fieldColIdx, getUpdatedFieldWithMarks(currentFieldWithMarks, mask));
        }
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


    protected void placeXAtGivenFields(List<Field> x_fields) {
        x_fields.forEach(this::placeXAtGivenField);
    }

    /**
     * @param x_field - field to place "X"
     */
    public void placeXAtGivenField(Field x_field) {
        int fieldColIdx = x_field.getColumnIdx();
        int fieldRowIdx = x_field.getRowIdx();
        this.excludeFieldInRow(x_field);
        this.excludeFieldInColumn(x_field);
        if (areFieldIndexesValid(x_field)) {
            this.nonogramSolutionBoard.get(fieldRowIdx).set(fieldColIdx, X_FIELD);
            this.nonogramSolutionBoardWithMarks.get(fieldRowIdx).set(fieldColIdx, X_FIELD.repeat(4));
        }
    }

    /**
     * @param rowIdx row index to exclude sequence (fully coloured and marked sequence, sequence range == sequence length)
     * @param seqIdx sequence index to exclude in specified row
     */
    protected void excludeSequenceInRow(int rowIdx, int seqIdx) {
        boolean rowValid = isRowIndexValid(rowIdx);
        if (rowValid && !this.rowsSequencesIdsNotToInclude.get(rowIdx).contains(seqIdx)) {
            tmpLog = generateAddingRowSequenceToNotToIncludeDescription(rowIdx, seqIdx);
            addLog(tmpLog);
            this.rowsSequencesIdsNotToInclude.get(rowIdx).add(seqIdx);
            Collections.sort(this.rowsSequencesIdsNotToInclude.get(rowIdx));
        }
    }

    /**
     * @param columnIdx - column index to exclude sequence (fully coloured and marked sequence, sequence range == sequence length)
     * @param seqIdx - sequence index to exclude in specified column
     */
    protected void excludeSequenceInColumn(int columnIdx, int seqIdx) {
        if (!this.columnsSequencesIdsNotToInclude.get(columnIdx).contains(seqIdx)) {
            tmpLog = generateAddingColumnSequenceToNotToIncludeDescription(columnIdx, seqIdx);
            addLog(tmpLog);
            this.columnsSequencesIdsNotToInclude.get(columnIdx).add(seqIdx);
            Collections.sort(this.columnsSequencesIdsNotToInclude.get(columnIdx));
        }
    }

    /**
     * @param fieldsToExclude fields in row to exclude (part of filled "O" sequence or "X")
     */
    protected void excludeFieldsInRow(List<Field> fieldsToExclude) {
        fieldsToExclude.forEach(this::excludeFieldInRow);
    }

    /**
     * @param fieldToExclude - field to not exclude - not consider in future
     */
    protected void excludeFieldInRow(Field fieldToExclude) {
        int fieldRowIdx = fieldToExclude.getRowIdx();
        int fieldColIdx = fieldToExclude.getColumnIdx();
        if (areFieldIndexesValid(fieldToExclude) && !this.rowsFieldsNotToInclude.get(fieldRowIdx).contains(fieldColIdx)) {
            this.rowsFieldsNotToInclude.get(fieldRowIdx).add(fieldColIdx);
            Collections.sort(this.rowsFieldsNotToInclude.get(fieldRowIdx));
        }
    }

    /**
     * @param fieldsToExclude fields in column to exclude (part of filled "O" sequence or "X")
     */
    public void excludeFieldsInColumn(List<Field> fieldsToExclude) {
        fieldsToExclude.forEach(this::excludeFieldInColumn);
    }

    /**
     * @param fieldToExclude - field to not exclude - not consider in future
     */
    protected void excludeFieldInColumn(Field fieldToExclude) {
        int fieldColIdx = fieldToExclude.getColumnIdx();
        int fieldRowIdx = fieldToExclude.getRowIdx();
        if (areFieldIndexesValid(fieldToExclude) && !this.columnsFieldsNotToInclude.get(fieldColIdx).contains(fieldRowIdx)) {
            this.columnsFieldsNotToInclude.get(fieldColIdx).add(fieldRowIdx);
            Collections.sort(this.columnsFieldsNotToInclude.get(fieldColIdx));
        }
    }

    /**
     * @param rowIdx row index to update one of sequences range
     * @param sequenceIdx row sequence index to update sequence range
     * @param updatedRange updated row sequence range
     */
    protected void updateRowSequenceRange(int rowIdx, int sequenceIdx, List<Integer> updatedRange) {
        this.rowsSequencesRanges.get(rowIdx).set(sequenceIdx, updatedRange);
    }

    /**
     * @param columnIdx - column index to update one of sequences range
     * @param sequenceIdx - column sequence index to update sequence range
     * @param updatedRange - updated column sequence range
     */
    public void updateColumnSequenceRange(int columnIdx, int sequenceIdx, List<Integer> updatedRange) {
        this.columnsSequencesRanges.get(columnIdx).set(sequenceIdx, updatedRange);
    }

    protected boolean isRowTrivial(int rowIdx) {
        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        for (int seqNo = 0; seqNo < rowSequencesLengths.size(); seqNo++) {
            if (rowSequencesLengths.get(seqNo) != rangeLength(rowSequencesRanges.get(seqNo))) {
                return false;
            }
        }

        return true;
    }

    protected boolean isColumnTrivial(int columnIdx) {
        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        for (int seqNo = 0; seqNo < columnSequencesLengths.size(); seqNo++) {
            if (columnSequencesLengths.get(seqNo) != rangeLength(columnSequencesRanges.get(seqNo))) {
                return false;
            }
        }

        return true;
    }

    protected boolean isRowEmpty(int rowIdx) {
        return sequencesRangesEqual(this.getRowsSequencesRanges().get(rowIdx), List.of(List.of(-1, -1)));
    }

    protected boolean isColumnEmpty(int columnIdx) {
        return sequencesRangesEqual(this.getColumnsSequencesRanges().get(columnIdx), List.of(List.of(-1, -1)));
    }

    protected String generateAddingRowSequenceToNotToIncludeDescription(int rowIdx, int seqNo) {
        return String.format("ROW %d - seqNo = %d excluded", rowIdx, seqNo);
    }

    protected String generateAddingColumnSequenceToNotToIncludeDescription(int columnIdx, int seqNo) {
        return String.format("COLUMN %d - seqNo = %d excluded", columnIdx, seqNo);
    }

    /**
     * @param columnIdx board column index
     * @return nonogram board column of given index
     */
    public List<String> getNonogramBoardColumn(int columnIdx) {
        List<String> solutionBoardColumn = new ArrayList<>();

        for (int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            solutionBoardColumn.add(this.nonogramSolutionBoard.get(rowIdx).get(columnIdx));
        }

        return solutionBoardColumn;
    }

    /**
     * @param columnIdx board column index
     * @return nonogram board column with marks of given index
     */
    public List<String> getNonogramBoardColumnWithMarks(int columnIdx) {
        List<String> solutionBoardColumnWithMarks = new ArrayList<>();

        for (int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            solutionBoardColumnWithMarks.add(nonogramSolutionBoardWithMarks.get(rowIdx).get(columnIdx));
        }

        return solutionBoardColumnWithMarks;
    }

    protected void addLog(String log) {
        if (log.isEmpty()) {
            System.out.println("Trying to add empty log!!!");
        } else {
            this.logs.add(log);
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
