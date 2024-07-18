package com.puzzlesolverappbackend.puzzleAppFileManager.payload;

import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.NonogramSolutionDecision;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public abstract class NonogramLogicParams {

    protected boolean showRepetitions = false;

    protected List<String> logs;

    protected String tmpLog;
    protected NonogramColumnLogic nonogramColumnLogic;
    protected NonogramRowLogic nonogramRowLogic;

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

    //1
    protected Set<Integer> affectedRowsToFillOverlappingFields;
    //2
    protected Set<Integer> affectedColumnsToMarkAvailableSequences;
    //3
    protected Set<Integer> affectedColumnsToFillOverlappingFields;
    //4
    protected Set<Integer> affectedRowsToMarkAvailableSequences;
    //5
    protected Set<Integer> affectedRowsToCorrectSequencesRanges;
    //6
    protected Set<Integer> affectedRowsToCorrectSequencesRangesWhenMetColouredField;
    //7
    protected Set<Integer> affectedRowsToCorrectSequencesRangesIfXOnWay;
    //8
    protected Set<Integer> affectedColumnsToCorrectSequencesRanges;
    //9
    protected Set<Integer> affectedColumnsToCorrectSequencesRangesWhenMetColouredField;
    //10
    protected Set<Integer> affectedColumnsToCorrectSequencesRangesIfXOnWay;
    //11
    protected Set<Integer> affectedRowsToPlaceXsAtUnreachableFields;
    //12
    protected Set<Integer> affectedColumnsToPlaceXsAtUnreachableFields;
    //13
    protected Set<Integer> affectedRowsToPlaceXsAroundLongestSequences;
    //14
    protected Set<Integer> affectedColumnsToPlaceXsAroundLongestSequences;
    //15
    protected Set<Integer> affectedRowsToPlaceXsAtTooShortEmptySequences;
    //16
    protected Set<Integer> affectedColumnsToPlaceXsAtTooShortEmptySequences;
    //17
    protected Set<Integer> affectedRowsToExtendColouredFieldsNearX;
    //18
    protected Set<Integer> affectedColumnsToExtendColouredFieldsNearX;

    /**
     * @return "X" count on nonogram solution board
     */
    public int fieldsWithXPlaced() {
        int fieldsWithXOnBoard = 0;
        for(int rowIndex = 0; rowIndex < this.getHeight(); rowIndex++) {
            for(int columnIndex = 0; columnIndex < this.getWidth(); columnIndex++) {
                if(this.getNonogramSolutionBoard().get(rowIndex).get(columnIndex).equals("X")) {
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
        for(int rowIndex = 0; rowIndex < this.getHeight(); rowIndex++) {
            for(int columnIndex = 0; columnIndex < this.getWidth(); columnIndex++) {
                if(this.getNonogramSolutionBoard().get(rowIndex).get(columnIndex).equals("O")) {
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

        for(int rowIndex = 0; rowIndex < this.getHeight(); rowIndex++) {
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
}
