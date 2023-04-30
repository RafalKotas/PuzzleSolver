package com.puzzlesolverappbackend.puzzleAppFileManager.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.puzzlesolverappbackend.puzzleAppFileManager.services.NonogramLogicService.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NonogramColumnLogic {

    boolean showRepetitions = false;
    private int newStepsMade;
    private List<String> logs;
    private List<List<Integer>> rowsSequences;
    private List<List<Integer>> columnsSequences;
    private List<List<List<Integer>>> columnsSequencesRanges;
    private List<List<List<Integer>>> rowsSequencesRanges;
    private List<List<String>> nonogramSolutionBoardWithMarks;
    private List<List<String>> nonogramSolutionBoard;
    //1
    private Set<Integer> affectedRowsToFillOverlappingFields;
    //2
    private Set<Integer> affectedColumnsToMarkAvailableSequences;
    //3
    private Set<Integer> affectedColumnsToFillOverlappingFields;
    //4
    private Set<Integer> affectedRowsToMarkAvailableSequences;
    //5
    private Set<Integer> affectedRowsToCorrectSequencesRanges;
    //6
    private Set<Integer> affectedRowsToCorrectSequencesRangesWhenMetColouredField;
    //7
    private Set<Integer> affectedRowsToChangeSequencesRangeIfXOnWay;
    //8
    private Set<Integer> affectedColumnsToCorrectSequencesRanges;
    //9
    private Set<Integer> affectedColumnsToCorrectSequencesRangesWhenMetColouredField;
    //10
    private Set<Integer> affectedColumnsToCorrectSequencesRangesIfXOnWay;
    //11
    private Set<Integer> affectedRowsToPlaceXsAtUnreachableFields;
    //12
    private Set<Integer> affectedColumnsToPlaceXsAtUnreachableFields;
    //13
    private Set<Integer> affectedRowsToPlaceXsAroundLongestSequences;
    //14
    private Set<Integer> affectedColumnsToPlaceXsAroundLongestSequences;
    //15
    private Set<Integer> affectedRowsToPlaceXsAtTooShortEmptySequences;
    //16
    private Set<Integer> affectedColumnsToPlaceXsAtTooShortEmptySequences;
    //17
    private Set<Integer> affectedRowsToExtendColouredFieldsNearX;
    //18
    private Set<Integer> affectedColumnsToExtendColouredFieldsNearX;

    private List<List<Integer>> rowsFieldsNotToInclude;
    private List<List<Integer>> columnsFieldsNotToInclude;
    private List<List<Integer>> rowsSequencesIdsNotToInclude;
    private List<List<Integer>> columnsSequencesIdsNotToInclude;

    String tmpLog;

    private boolean solutionInvalid = false;

    public NonogramColumnLogic(NonogramLogic nonogramLogic) {
        this.newStepsMade = nonogramLogic.getNewStepsMade();
        this.logs = nonogramLogic.getLogs();
        this.rowsSequences = nonogramLogic.getRowsSequences();
        this.columnsSequences = nonogramLogic.getColumnsSequences();
        this.columnsSequencesRanges = nonogramLogic.getColumnsSequencesRanges();
        this.nonogramSolutionBoardWithMarks = nonogramLogic.getNonogramSolutionBoardWithMarks();
        this.nonogramSolutionBoard = nonogramLogic.getNonogramSolutionBoard();
        this.affectedRowsToFillOverlappingFields = nonogramLogic.getAffectedRowsToFillOverlappingFields();
        this.affectedColumnsToMarkAvailableSequences = nonogramLogic.getAffectedColumnsToMarkAvailableSequences();
        this.affectedColumnsToFillOverlappingFields = nonogramLogic.getAffectedColumnsToFillOverlappingFields();
        this.affectedRowsToMarkAvailableSequences = nonogramLogic.getAffectedRowsToMarkAvailableSequences();
        this.affectedRowsToCorrectSequencesRanges = nonogramLogic.getAffectedRowsToCorrectSequencesRanges();
        this.affectedRowsToCorrectSequencesRangesWhenMetColouredField = nonogramLogic.getAffectedRowsToCorrectSequencesRangesWhenMetColouredField();
        this.affectedRowsToChangeSequencesRangeIfXOnWay = nonogramLogic.getAffectedRowsToChangeSequencesRangeIfXOnWay();
        this.affectedColumnsToCorrectSequencesRanges = nonogramLogic.getAffectedColumnsToCorrectSequencesRanges();
        this.affectedColumnsToCorrectSequencesRangesWhenMetColouredField = nonogramLogic.getAffectedColumnsToCorrectSequencesRangesWhenMetColouredField();
        this.affectedColumnsToCorrectSequencesRangesIfXOnWay = nonogramLogic.getAffectedColumnsToCorrectSequencesRangesIfXOnWay();
        this.affectedRowsToPlaceXsAtUnreachableFields = nonogramLogic.getAffectedRowsToPlaceXsAtUnreachableFields();
        this.affectedColumnsToPlaceXsAtUnreachableFields = nonogramLogic.getAffectedColumnsToPlaceXsAtUnreachableFields();
        this.affectedRowsToPlaceXsAroundLongestSequences = nonogramLogic.getAffectedRowsToPlaceXsAroundLongestSequences();
        this.affectedColumnsToPlaceXsAroundLongestSequences = nonogramLogic.getAffectedColumnsToPlaceXsAroundLongestSequences();
        this.affectedRowsToPlaceXsAtTooShortEmptySequences = nonogramLogic.getAffectedRowsToPlaceXsAtTooShortEmptySequences();
        this.affectedColumnsToPlaceXsAtTooShortEmptySequences = nonogramLogic.getAffectedColumnsToPlaceXsAtTooShortEmptySequences();
        this.affectedRowsToExtendColouredFieldsNearX = nonogramLogic.getAffectedRowsToExtendColouredFieldsNearX();
        this.affectedColumnsToExtendColouredFieldsNearX = nonogramLogic.getAffectedColumnsToExtendColouredFieldsNearX();
        this.solutionInvalid = nonogramLogic.isSolutionInvalid();
    }

    /**
     * mark columns sequences with char identifiers
     */
    public NonogramColumnLogic markAvailableSequencesInColumns() {
        for (Integer columnIndex : this.getAffectedColumnsToMarkAvailableSequences()) {
            markAvailableSequencesInColumn(columnIndex);
        }

        return this;
    }


    /**
     * @param columnIdx - column index on which mark sequences with char identifiers
     */
    public void markAvailableSequencesInColumn(int columnIdx) {

        List<List<String>> nonogramSolutionBoardWithMarks = this.getNonogramSolutionBoardWithMarks();
        List<List<String>> nonogramSolutionBoard = this.getNonogramSolutionBoard();
        List<Integer> colouredSequenceIndexes;
        int firstSequenceIndex;
        int lastSequenceIndex;
        List<Integer> columnSequences = this.getColumnsSequences().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequenceRange;
        List<Integer> updatedColumnSequenceRange;
        int matchingSequencesCount;
        int lastMatchingSequenceIndex = -1;
        int colouredSequenceLength;
        int matchingSequenceLength;
        boolean sequenceEqualsColumnSequenceLength;
        String sequenceMarker;

        int oldRangeBeginIndex;
        int oldRangeEndIndex;
        int updatedRangeBeginIndex;
        int updatedRangeEndIndex;

        for(int rowIdx = 0; rowIdx < nonogramSolutionBoardWithMarks.size(); rowIdx++) {

            //TODO - exclude fieldsNotToInclude

            if(nonogramSolutionBoard.get(rowIdx).get(columnIdx).equals("O")) {

                colouredSequenceIndexes = new ArrayList<>();

                colouredSequenceIndexes.add(rowIdx);
                //collect indexes of current coloured sequence
                while(rowIdx < nonogramSolutionBoard.size() && nonogramSolutionBoard.get(rowIdx).get(columnIdx).equals("O")) {
                    rowIdx++;
                }

                colouredSequenceIndexes.add(rowIdx - 1);

                firstSequenceIndex = colouredSequenceIndexes.get(0);
                lastSequenceIndex = colouredSequenceIndexes.get(colouredSequenceIndexes.size() - 1);
                matchingSequencesCount = 0;
                colouredSequenceLength = lastSequenceIndex - firstSequenceIndex  + 1;

                /*check how many sequences matching conditions for mark:
                    a)coloured sequence range inside sequenceRange(seqNo)
                    b)coloured sequence range length less or equal than sequenceRange(seqNo)
                 */
                for(int seqNo = 0; seqNo < columnSequences.size(); seqNo++) {
                    columnSequenceRange = columnSequencesRanges.get(seqNo);

                    if( rangeInsideAnotherRange(colouredSequenceIndexes, columnSequenceRange)
                            && colouredSequenceLength <= columnSequences.get(seqNo)) {
                        matchingSequencesCount++;
                        lastMatchingSequenceIndex = seqNo;
                    }
                }

                //NOTE!!! if matching count == 0 and there are sequences that are not fulfilled -> wrong solution
                //marking possible only if matching sequence count is 1
                if(matchingSequencesCount == 1) {

                    //3
                    this.getAffectedColumnsToFillOverlappingFields().add(columnIdx);
                    //8
                    this.getAffectedColumnsToCorrectSequencesRanges().add(columnIdx);
                    //12
                    this.getAffectedColumnsToPlaceXsAtUnreachableFields().add(columnIdx);
                    //14
                    this.getAffectedColumnsToPlaceXsAroundLongestSequences().add(columnIdx);
                    //16
                    this.getAffectedColumnsToPlaceXsAtTooShortEmptySequences().add(columnIdx);
                    //18
                    this.getAffectedColumnsToExtendColouredFieldsNearX().add(columnIdx);

                    matchingSequenceLength = columnSequences.get(lastMatchingSequenceIndex);
                    sequenceEqualsColumnSequenceLength = colouredSequenceLength == matchingSequenceLength;

                    if(sequenceEqualsColumnSequenceLength &&
                            (!columnSequencesRanges.get(lastMatchingSequenceIndex).get(0).equals(colouredSequenceIndexes.get(0)) ||
                                    !columnSequencesRanges.get(lastMatchingSequenceIndex).get(1).equals(colouredSequenceIndexes.get(1)))) {
                        tmpLog = "Updated column sequence range when marking coloured field(whole sequence) (columnIdx="+ columnIdx + "), seqNo: "+ lastMatchingSequenceIndex +
                                ", from " + columnSequencesRanges.get(lastMatchingSequenceIndex) + " to " + colouredSequenceIndexes;
                        this.getLogs().add(tmpLog);
                        this.updateColumnSequenceRange(columnIdx, lastMatchingSequenceIndex, colouredSequenceIndexes);
                    } else {
                        //current range bounds
                        oldRangeBeginIndex = this.getColumnsSequencesRanges().get(columnIdx).get(lastMatchingSequenceIndex).get(0);
                        oldRangeEndIndex = this.getColumnsSequencesRanges().get(columnIdx).get(lastMatchingSequenceIndex).get(1);

                        //updated marked sequence range basing on lastSequenceIndex(searching first 'X' going to top)
                        updatedRangeBeginIndex = Math.max(oldRangeBeginIndex,
                                this.minimumRowIndexWithoutX(columnIdx, lastSequenceIndex, matchingSequenceLength));

                        //updated marked sequence range basing on firstSequenceIndex(searching first 'X' going to bottom)
                        updatedRangeEndIndex = Math.min(oldRangeEndIndex,
                                this.maximumRowIndexWithoutX(columnIdx, firstSequenceIndex, matchingSequenceLength));

                        updatedColumnSequenceRange = new ArrayList<>();

                        updatedColumnSequenceRange.add( updatedRangeBeginIndex );
                        updatedColumnSequenceRange.add( updatedRangeEndIndex );

                        if(!columnSequencesRanges.get(lastMatchingSequenceIndex).get(0).equals(updatedColumnSequenceRange.get(0)) ||
                                !columnSequencesRanges.get(lastMatchingSequenceIndex).get(1).equals(updatedColumnSequenceRange.get(1))) {
                            tmpLog = "Updated column sequence range when marking coloured field (columnIdx="+ columnIdx + "), seqNo: "+ lastMatchingSequenceIndex +
                                    ", from " + columnSequencesRanges.get(lastMatchingSequenceIndex) + " to " + updatedColumnSequenceRange;
                            this.getLogs().add(tmpLog);
                            this.updateColumnSequenceRange(columnIdx, lastMatchingSequenceIndex, updatedColumnSequenceRange);
                        }
                    }


                    //mark sequence part
                    sequenceMarker = NonogramLogic.indexToSequenceCharMark(lastMatchingSequenceIndex);

                    for(int sequenceRowIdx = firstSequenceIndex; sequenceRowIdx <= lastSequenceIndex; sequenceRowIdx++) {
                        if(this.getNonogramSolutionBoardWithMarks().get(sequenceRowIdx).get(columnIdx).substring(2).equals("--")) {
                            tmpLog = "Sequence marked in column (seqIdx=" + lastMatchingSequenceIndex + ", mark="
                                    + sequenceMarker + "), column: " + columnIdx + " , row: " + sequenceRowIdx;
                            this.getLogs().add(tmpLog);
                            this.markColumnBoardField(sequenceRowIdx, columnIdx, sequenceMarker);
                            this.increaseStepsMade();
                        } else if (this.showRepetitions) {
                            System.out.println("Column field was marked earlier.");
                        }
                    }
                }
            }
        }
    }

    /**
     * fill fields in columns where ranges met specific condition
     */
    public void fillOverlappingFieldsInColumns () {
        for (Integer columnIndex : this.getAffectedColumnsToFillOverlappingFields()) {
            fillOverlappingFieldsInColumn(columnIndex);
        }
    }

    /**
     * fill fields in column where ranges met specific condition (range_length < sequence_length * 2)
     */
    public void fillOverlappingFieldsInColumn (int columnIdx) {
        List<Integer> sequencesInColumnLengths = this.getColumnsSequences().get(columnIdx);
        List<List<Integer>> sequencesInColumnRanges = this.getColumnsSequencesRanges().get(columnIdx);

        int sequenceLength;
        int rangeBeginIndex;
        int rangeEndIndex;
        int colourBeginRowIndex;
        int colourEndRowIndex;

        String sequenceCharMark;

        List<String> rowToChangeColumnBoard;

        List<String> rowToChangeColumnBoardWithMarks;
        String elementToChangeInsideRowBoardWithMarks;

        for(int sequenceIdx = 0; sequenceIdx < sequencesInColumnLengths.size(); sequenceIdx++) {
            sequenceLength = sequencesInColumnLengths.get(sequenceIdx);
            rangeBeginIndex = sequencesInColumnRanges.get(sequenceIdx).get(0);
            rangeEndIndex = sequencesInColumnRanges.get(sequenceIdx).get(1);

            colourBeginRowIndex = rangeEndIndex - sequenceLength + 1;
            colourEndRowIndex = rangeBeginIndex + sequenceLength - 1;

            if(colourBeginRowIndex <= colourEndRowIndex) {

                sequenceCharMark = NonogramLogic.indexToSequenceCharMark(sequenceIdx);

                if(colourEndRowIndex - colourBeginRowIndex + 1 == sequencesInColumnLengths.get(sequenceIdx)) {
                    this.addColumnSequenceIdxToNotToInclude(columnIdx, sequenceIdx);
                }

                for (int rowIdx = colourBeginRowIndex; rowIdx <= colourEndRowIndex; rowIdx++) {
                    try {
                        rowToChangeColumnBoardWithMarks = this.getNonogramSolutionBoardWithMarks().get(rowIdx);
                        elementToChangeInsideRowBoardWithMarks = rowToChangeColumnBoardWithMarks.get(columnIdx);


                        if(rowToChangeColumnBoardWithMarks.get(columnIdx).substring(2).equals("--")) {
                            this.increaseStepsMade();

                            //4
                            this.getAffectedRowsToMarkAvailableSequences().add(rowIdx);
                            //6
                            this.getAffectedRowsToCorrectSequencesRangesWhenMetColouredField().add(rowIdx);
                            //13
                            this.getAffectedRowsToPlaceXsAroundLongestSequences().add(rowIdx);
                            //17
                            this.getAffectedRowsToExtendColouredFieldsNearX().add(rowIdx);

                            rowToChangeColumnBoardWithMarks.set(columnIdx, elementToChangeInsideRowBoardWithMarks.substring(0, 2) + "C" + sequenceCharMark);
                            this.getNonogramSolutionBoardWithMarks().set(rowIdx, rowToChangeColumnBoardWithMarks);
                            this.increaseStepsMade();

                            rowToChangeColumnBoard = this.getNonogramSolutionBoard().get(rowIdx);
                            if(rowToChangeColumnBoard.get(columnIdx).equals("-")) {
                                rowToChangeColumnBoard.set(columnIdx, "O");

                                tmpLog = "O placed, col: " + columnIdx + " , row: " + rowIdx + " (overlapping fields in column).";
                                this.logs.add(tmpLog);

                                this.getNonogramSolutionBoard().set(rowIdx, rowToChangeColumnBoard);
                            }
                        } else if (showRepetitions) {
                            System.out.println("Column field was coloured earlier!");
                        }
                    } catch (IndexOutOfBoundsException e) {
                        this.setSolutionInvalid(true);
                    }
                }
            }
        }

    }

    // iterations through all columns
    /**
     * place "X" around coloured fields in columns where sequence of these coloured fields have length of longest
     * possible sequence length in specific row, that can be located in coloured field sequence range
     */
    public void placeXsColumnsAroundLongestSequences() {

        for (Integer columnIndex : this.getAffectedColumnsToPlaceXsAroundLongestSequences()) {
            placeXsColumnAroundLongestSequences(columnIndex);
        }
    }

    /**
     * @param columnIdx - column index on which place "X" around coloured fields
     */
    public void placeXsColumnAroundLongestSequences(int columnIdx) {

        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);

        int nonogramHeight = this.getHeight();

        List<Integer> colouredSequenceRange;
        List<Integer> columnSequenceRange;
        int sequenceLength;

        List<Integer> columnSequencesIndexesIncludingSequenceRange;
        List<Integer> columnSequencesLengthsIncludingSequenceRange;

        int firstXIndex;
        int lastXIndex;

        int columnSequenceIdxNotToInclude;

        List<Integer> oldSequenceRange;
        List<Integer> updatedSequenceRange;

        for(int rowIdx = 0; rowIdx < nonogramHeight; rowIdx++) {

            if(this.getNonogramSolutionBoard().get(rowIdx).get(columnIdx).equals("O")) {

                colouredSequenceRange = new ArrayList<>();
                colouredSequenceRange.add(rowIdx);

                while(rowIdx < nonogramHeight && this.getNonogramSolutionBoard().get(rowIdx).get(columnIdx).equals("O")) {
                    rowIdx++;
                }

                //when solutionBoard[rowIdx][columnIdx] != "O"
                colouredSequenceRange.add(rowIdx - 1);
                sequenceLength = rangeLength(colouredSequenceRange);
                columnSequencesIndexesIncludingSequenceRange = new ArrayList<>();
                columnSequencesLengthsIncludingSequenceRange = new ArrayList<>();

                for(int seqNo = 0; seqNo < columnSequencesRanges.size(); seqNo++) {
                    columnSequenceRange = columnSequencesRanges.get(seqNo);
                    if(rangeInsideAnotherRange(colouredSequenceRange, columnSequenceRange)) {
                        columnSequencesIndexesIncludingSequenceRange.add(seqNo);
                        columnSequencesLengthsIncludingSequenceRange.add(columnSequencesLengths.get(seqNo));
                    }
                }

                firstXIndex = colouredSequenceRange.get(0) - 1;
                lastXIndex = colouredSequenceRange.get(1) + 1;

                if(columnSequencesIndexesIncludingSequenceRange.size() >= 1) {
                    if(columnSequencesIndexesIncludingSequenceRange.size() == 1 ) {

                        columnSequenceIdxNotToInclude = columnSequencesIndexesIncludingSequenceRange.get(0);

                        if(sequenceLength == columnSequencesLengthsIncludingSequenceRange.get(0)) {

                            if(!this.getColumnsSequencesIdsNotToInclude().get(columnIdx).contains(columnSequenceIdxNotToInclude)) {

                                this.addColumnSequenceIdxToNotToInclude(columnIdx, columnSequenceIdxNotToInclude);

                                if(firstXIndex > -1) {
                                    if (this.getNonogramSolutionBoard().get(firstXIndex).get(columnIdx).equals("-")) {

                                        tmpLog = "X placed, columnIdx: " + columnIdx + " rowIdx: " + firstXIndex + " (Placing 'X' before sequence in column - only possible sequence).";
                                        this.logs.add(tmpLog);

                                        this.increaseStepsMade().placeXAtGivenPosition(firstXIndex, columnIdx)
                                                .addRowFieldToNotToInclude(firstXIndex, columnIdx)
                                                .addColumnFieldToNotToInclude(columnIdx, firstXIndex);
                                    } else if(showRepetitions) {
                                        System.out.println("Longest sequence in column firstXIndex added earlier!");
                                    }

                                    oldSequenceRange = columnSequencesRanges.get(columnSequenceIdxNotToInclude);

                                    updatedSequenceRange = new ArrayList<>();
                                    updatedSequenceRange.add(firstXIndex + 1);
                                    updatedSequenceRange.add(oldSequenceRange.get(1));
                                    this.changeColumnSequenceRange(columnIdx, columnSequenceIdxNotToInclude,
                                            updatedSequenceRange);
                                }

                                if (lastXIndex < this.getHeight()) {
                                    if( this.getNonogramSolutionBoard().get(lastXIndex).get(columnIdx).equals("-")) {

                                        tmpLog = "X placed, columnIdx: " + columnIdx + " rowIdx: " + lastXIndex + " (Placing 'X' after longest sequence in column).";
                                        this.logs.add(tmpLog);

                                        this.increaseStepsMade().placeXAtGivenPosition(lastXIndex, columnIdx)
                                                .addColumnFieldToNotToInclude(columnIdx, lastXIndex)
                                                .addRowFieldToNotToInclude(lastXIndex, columnIdx);
                                        oldSequenceRange = columnSequencesRanges.get(columnSequenceIdxNotToInclude);

                                        updatedSequenceRange = new ArrayList<>();
                                        updatedSequenceRange.add(oldSequenceRange.get(0));
                                        updatedSequenceRange.add(lastXIndex - 1);

                                        this.changeColumnSequenceRange(columnIdx, columnSequenceIdxNotToInclude,
                                                updatedSequenceRange);

                                    } else if(showRepetitions) {
                                        System.out.println("Longest sequence in column lastXIndex added earlier!");
                                    }
                                }

                                for(int sequenceRowIdx = firstXIndex + 1; sequenceRowIdx < lastXIndex; sequenceRowIdx++) {
                                    if (!this.getColumnsFieldsNotToInclude().get(columnIdx).contains(sequenceRowIdx)) {
                                        this.increaseStepsMade().addColumnFieldToNotToInclude(columnIdx, sequenceRowIdx);
                                    } else if(showRepetitions) {
                                        System.out.println("Field not to include in column has been inserted earlier");
                                    }
                                }
                            }

                        }

                    } else if(columnSequencesIndexesIncludingSequenceRange.size() > 0) {
                        //check if length of sequence == Max(foundSequences_lengths)
                        if(sequenceLength == Collections.max(columnSequencesLengthsIncludingSequenceRange)) {
                            if(this.getNonogramSolutionBoard().get(firstXIndex).get(columnIdx).equals("-")) {
                                this.placeXAtGivenPosition(firstXIndex, columnIdx)
                                        .addColumnFieldToNotToInclude(columnIdx, firstXIndex)
                                        .addRowFieldToNotToInclude(firstXIndex, columnIdx);

                                tmpLog = "X placed, columnIdx: " + columnIdx + " rowIdx: " + firstXIndex +
                                        " (Placing 'X' before longest sequence in column - sequence index not specified).";
                                this.logs.add(tmpLog);

                                this.increaseStepsMade();
                            } else if (showRepetitions) {
                                System.out.println("Sequence with maximum length in area firstXIndex placed earlier!");
                            }
                            if(this.getNonogramSolutionBoard().get(lastXIndex).get(columnIdx).equals("-")) {
                                this.placeXAtGivenPosition(lastXIndex, columnIdx)
                                        .addColumnFieldToNotToInclude(columnIdx, lastXIndex)
                                        .addRowFieldToNotToInclude(lastXIndex, columnIdx);

                                tmpLog = "X placed, columnIdx: " + columnIdx + " rowIdx: " + lastXIndex +
                                        " (Placing 'X' after longest sequence in column - sequence index not specified).";
                                this.logs.add(tmpLog);

                                this.increaseStepsMade();
                            } else if(showRepetitions) {
                                System.out.println("Sequence with maximum length in area lastXIndex placed earlier!");
                            }

                        }
                    }

                    //2
                    this.getAffectedColumnsToMarkAvailableSequences().add(columnIdx);
                    //7
                    this.getAffectedColumnsToCorrectSequencesRanges().add(columnIdx);

                    if(firstXIndex > -1 ) {
                        //7
                        this.getAffectedRowsToChangeSequencesRangeIfXOnWay().add(firstXIndex);
                        //11
                        this.getAffectedRowsToPlaceXsAtTooShortEmptySequences().add(firstXIndex);
                        //13
                        this.getAffectedRowsToExtendColouredFieldsNearX().add(firstXIndex);
                    }

                    if(lastXIndex < this.getHeight()) {
                        //7
                        this.getAffectedRowsToChangeSequencesRangeIfXOnWay().add(lastXIndex);
                        //11
                        this.getAffectedRowsToPlaceXsAtTooShortEmptySequences().add(lastXIndex);
                        //13
                        this.getAffectedRowsToExtendColouredFieldsNearX().add(lastXIndex);
                    }

                }
            }
        }

    }

    /**
     * correct the potential ranges of occurrence of sequences in the columns due to the ranges of previous and next sequences
     */
    public void correctColumnsSequencesRanges () {

        for (Integer columnIndex : this.getAffectedColumnsToCorrectSequencesRanges()) {
            correctColumnSequencesRanges(columnIndex);

            List<Integer> sequencesLengths = this.getColumnsSequences().get(columnIndex);
            List<List<Integer>> sequencesRanges = this.getColumnsSequencesRanges().get(columnIndex);

            if(!emptyColumn(columnIndex)) {
                for(int seqNo = 0; seqNo < sequencesRanges.size(); seqNo++) {
                    if(rangeLength(sequencesRanges.get(seqNo)) < sequencesLengths.get(seqNo)) {
                        this.logs.add("ERR correct column sequences ranges!!!");
                        this.logs.add("column ranges: " + sequencesRanges + "   ,  column sequences lengths: " + sequencesLengths);
                        this.setSolutionInvalid(true);
                        break;
                    }
                }
            }

            if(solutionInvalid) {
                break;
            }
        }
    }

    public void correctColumnSequencesRanges (int columnIdx) {

        boolean columnSequenceRangesChanged = false;

        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnFieldsNotToInclude = this.getColumnsFieldsNotToInclude().get(columnIdx);

        List<Integer> fullSequenceRange;
        List<Integer> currentSequenceRange;
        List<Integer> nextSequenceRange;
        int updatedNextSequenceBeginRangeRowIndex;
        int updatedStartIndex;
        int updatedEndIndex;
        int oldNextSequenceBeginRangeRowIndex;
        List<Integer> updatedNextSequenceRange;

        List<Integer> columnSequencesIdsNotToInclude = this.getColumnsSequencesIdsNotToInclude().get(columnIdx);

        //from top - start
        for(int sequenceIdx = 0; sequenceIdx < columnSequencesRanges.size() - 1; sequenceIdx++) {

            if(columnSequencesIdsNotToInclude.contains(sequenceIdx) && !columnSequencesIdsNotToInclude.contains(sequenceIdx + 1)) {
                fullSequenceRange = columnSequencesRanges.get(sequenceIdx);

                nextSequenceRange = columnSequencesRanges.get(sequenceIdx + 1);
                //XXXffffXu---- (f - full sequence that position and mark is known, u -> the minimum start index of next sequence)
                updatedNextSequenceBeginRangeRowIndex = fullSequenceRange.get(1) + 2;

                while(columnFieldsNotToInclude.contains(updatedNextSequenceBeginRangeRowIndex)) {
                    updatedNextSequenceBeginRangeRowIndex++;
                }

                oldNextSequenceBeginRangeRowIndex = nextSequenceRange.get(0);

                updatedNextSequenceRange = new ArrayList<>();

                updatedStartIndex = Math.max(oldNextSequenceBeginRangeRowIndex, updatedNextSequenceBeginRangeRowIndex);
                updatedNextSequenceRange.add(updatedStartIndex);
                updatedNextSequenceRange.add(nextSequenceRange.get(1));

                if(updatedNextSequenceBeginRangeRowIndex != oldNextSequenceBeginRangeRowIndex) {
                    this.getColumnsSequencesRanges().get(columnIdx).set(sequenceIdx + 1, updatedNextSequenceRange);
                    columnSequenceRangesChanged = true;
                    this.increaseStepsMade();
                    tmpLog = "Changed column sequence range(previous seqId not to include) from top (columnIdx=" + columnIdx + "), seqNo: " + (sequenceIdx + 1)
                            + ", from " + nextSequenceRange + " to " + updatedNextSequenceRange;
                    this.logs.add(tmpLog);
                }
            }   else if(!columnSequencesIdsNotToInclude.contains(sequenceIdx) && !columnSequencesIdsNotToInclude.contains(sequenceIdx + 1)) {

                currentSequenceRange = columnSequencesRanges.get(sequenceIdx);

                nextSequenceRange = columnSequencesRanges.get(sequenceIdx + 1);
                //get the miniumum start index of next sequence checking where is the earliest index that current sequence can end (+1 for 'X' after sequence)
                updatedNextSequenceBeginRangeRowIndex = currentSequenceRange.get(0) + columnSequencesLengths.get(sequenceIdx) + 1;

                oldNextSequenceBeginRangeRowIndex = nextSequenceRange.get(0); //6

                updatedNextSequenceRange = new ArrayList<>();

                updatedStartIndex = Math.max(oldNextSequenceBeginRangeRowIndex, updatedNextSequenceBeginRangeRowIndex);

                updatedNextSequenceRange.add(updatedStartIndex);  //6
                updatedNextSequenceRange.add(nextSequenceRange.get(1)); //8

                if(updatedStartIndex != oldNextSequenceBeginRangeRowIndex) {
                    columnSequenceRangesChanged = true;
                    this.increaseStepsMade();
                    tmpLog = "Changed column sequence range(previous seqId to include) from top (columnIdx=" + columnIdx + "), seqNo: " + (sequenceIdx + 1)
                            + ", from " + nextSequenceRange + " to " + updatedNextSequenceRange;
                    this.logs.add(tmpLog);
                }

                this.getColumnsSequencesRanges().get(columnIdx).set(sequenceIdx + 1, updatedNextSequenceRange);
            }
        }

        List<Integer> previousSequenceRange;
        int previousSequenceBeginRangeRowIndex;
        int previousSequenceEndRangeRowIndex;
        int updatedPreviousSequenceEndRangeRowIndex;
        List<Integer> updatedPreviousSequenceRange;

        int currentSequenceLength;

        //from bottom - end
        for(int sequenceIdx = columnSequencesRanges.size() - 1; sequenceIdx > 0; sequenceIdx--) {

            // f.e. [[0, 10], [2, 12]] (lengths: [1, 2], docelowo: [[0, 9], [10, 12]]
            if(columnSequencesIdsNotToInclude.contains(sequenceIdx) && !columnSequencesIdsNotToInclude.contains(sequenceIdx - 1)) {

                //currentSequenceRange is marked with column seqNo and fully coloured
                if(columnSequencesIdsNotToInclude.contains(sequenceIdx)) {
                    currentSequenceRange = columnSequencesRanges.get(sequenceIdx);
                    previousSequenceRange = columnSequencesRanges.get(sequenceIdx - 1);

                    updatedPreviousSequenceEndRangeRowIndex = currentSequenceRange.get(0) - 2;

                    while(columnFieldsNotToInclude.contains(updatedPreviousSequenceEndRangeRowIndex)) {
                        updatedPreviousSequenceEndRangeRowIndex--;
                    }

                    previousSequenceBeginRangeRowIndex = previousSequenceRange.get(0);
                    previousSequenceEndRangeRowIndex = previousSequenceRange.get(1);

                    updatedPreviousSequenceRange = new ArrayList<>();

                    updatedPreviousSequenceRange.add(previousSequenceBeginRangeRowIndex);
                    updatedEndIndex = Math.min(previousSequenceEndRangeRowIndex, updatedPreviousSequenceEndRangeRowIndex);
                    updatedPreviousSequenceRange.add(updatedEndIndex);

                    if(updatedEndIndex != previousSequenceEndRangeRowIndex) {
                        columnSequenceRangesChanged = true;
                        this.increaseStepsMade();
                        tmpLog = "Changed column sequence range from bottom - end (columnIdx=" + columnIdx + "), seqNo: " + (sequenceIdx - 1)
                                + ", from " + previousSequenceRange + " to " + updatedPreviousSequenceRange + " ranges: "
                                + columnSequencesRanges + " column: " + this.getNonogramBoardColumn(columnIdx)
                                + " column fields not to include: " + columnFieldsNotToInclude;
                        this.logs.add(tmpLog);
                    }

                    this.getColumnsSequencesRanges().get(columnIdx).set(sequenceIdx - 1, updatedPreviousSequenceRange);
                }
            } else if(!columnSequencesIdsNotToInclude.contains(sequenceIdx) && !columnSequencesIdsNotToInclude.contains(sequenceIdx - 1)) {
                currentSequenceLength = columnSequencesLengths.get(sequenceIdx);
                currentSequenceRange = columnSequencesRanges.get(sequenceIdx);
                previousSequenceRange = columnSequencesRanges.get(sequenceIdx - 1);

                updatedPreviousSequenceEndRangeRowIndex = currentSequenceRange.get(1) - currentSequenceLength - 1;

                previousSequenceBeginRangeRowIndex = previousSequenceRange.get(0);
                previousSequenceEndRangeRowIndex = previousSequenceRange.get(1);

                updatedPreviousSequenceRange = new ArrayList<>();

                updatedPreviousSequenceRange.add(previousSequenceBeginRangeRowIndex);
                updatedEndIndex = Math.min(previousSequenceEndRangeRowIndex, updatedPreviousSequenceEndRangeRowIndex);
                updatedPreviousSequenceRange.add(updatedEndIndex);

                if(updatedEndIndex != previousSequenceEndRangeRowIndex) {
                    this.getColumnsSequencesRanges().get(columnIdx).set(sequenceIdx - 1, updatedPreviousSequenceRange);
                    columnSequenceRangesChanged = true;
                }
            }
        }

        if(columnSequenceRangesChanged) {
            //this.increaseStepsMade();
            //2
            this.getAffectedColumnsToMarkAvailableSequences().add(columnIdx);
            //3
            this.getAffectedColumnsToFillOverlappingFields().add(columnIdx);
            //10
            this.getAffectedColumnsToCorrectSequencesRangesIfXOnWay().add(columnIdx);
            //12
            this.getAffectedColumnsToPlaceXsAtUnreachableFields().add(columnIdx);
            //14
            this.getAffectedColumnsToPlaceXsAroundLongestSequences().add(columnIdx);
            //16
            this.getAffectedColumnsToPlaceXsAtTooShortEmptySequences().add(columnIdx);
            //18
            this.getAffectedColumnsToExtendColouredFieldsNearX().add(columnIdx);
        }
    }

    /**
     * corrects columns sequences when met coloured field in columns
     * (f.e. first sequence FS can't start later than first met coloured field FMCF,
     * second - not later than first coloured field on index FMCF + FS.length, etc.)
     */
    public void correctColumnsSequencesWhenMetColouredField () {

        for (Integer columnIndex : this.getAffectedColumnsToCorrectSequencesRangesWhenMetColouredField()) {
            correctColumnSequencesWhenMetColouredField(columnIndex);
        }
    }


    /**
     * @param columnIdx - column to correct range when met coloured field
     */
    public void correctColumnSequencesWhenMetColouredField (int columnIdx) {


        boolean columnSequencesRangesChanged = false;

        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);
        int columnSequenceRangeStart;
        int columnSequenceRangeEnd;
        List<Integer> oldRange;
        List<Integer> updatedRange;

        List<String> solutionBoardColumn = getNonogramBoardColumn(columnIdx);

        int sequenceId = 0;
        int sequenceLength = columnSequencesLengths.get(0);
        int updatedEndIndex;
        int maximumPossibleSequenceRangeEnd;

        for(int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            if(solutionBoardColumn.get(rowIdx).equals("O")) {
                oldRange = columnSequencesRanges.get(sequenceId);
                columnSequenceRangeStart = oldRange.get(0);
                columnSequenceRangeEnd = oldRange.get(1);
                maximumPossibleSequenceRangeEnd = rowIdx + sequenceLength - 1;

                updatedRange = new ArrayList<>();
                updatedRange.add(columnSequenceRangeStart);
                updatedEndIndex = Math.min(columnSequenceRangeEnd, maximumPossibleSequenceRangeEnd);
                updatedRange.add(updatedEndIndex);
                if(updatedEndIndex != columnSequenceRangeEnd) {
                    columnSequencesRangesChanged = true;
                    this.getColumnsSequencesRanges().get(columnIdx).set(sequenceId, updatedRange);
                    tmpLog = "Changed column sequence range from top (columnIdx="+ columnIdx + "), seqNo: " + (sequenceId - 1) +
                            ", from " + oldRange + " to " + updatedRange + " ranges: " + columnSequencesRanges;
                    this.logs.add(tmpLog);
                }

                rowIdx = rowIdx + sequenceLength;
                sequenceId++;
                if(sequenceId < columnSequencesLengths.size()) {
                    sequenceLength = columnSequencesLengths.get(sequenceId);
                } else {
                    break;
                }
            }
        }

        int updatedStartIndex;
        int minimumPossibleSequenceRangeStart;

        sequenceId = columnSequencesLengths.size() - 1;
        sequenceLength = columnSequencesLengths.get(sequenceId);

        for(int rowIdx = this.getHeight() - 1; rowIdx >= 0 ; rowIdx--) {
            if(solutionBoardColumn.get(rowIdx).equals("O")) {
                oldRange = columnSequencesRanges.get(sequenceId);
                columnSequenceRangeStart = oldRange.get(0);
                columnSequenceRangeEnd = oldRange.get(1);
                minimumPossibleSequenceRangeStart = rowIdx - sequenceLength + 1;

                updatedRange = new ArrayList<>();
                updatedStartIndex = Math.max(columnSequenceRangeStart, minimumPossibleSequenceRangeStart);
                updatedRange.add(updatedStartIndex);
                updatedRange.add(columnSequenceRangeEnd);
                if(updatedStartIndex != columnSequenceRangeStart) {
                    columnSequencesRangesChanged = true;
                    this.getColumnsSequencesRanges().get(columnIdx).set(sequenceId, updatedRange);
                    tmpLog = "Changed column sequence range from bottom (columnIdx="+ columnIdx + "), seqNo: " + (sequenceId - 1) +
                            ", from " + oldRange + " to " + updatedRange + " ranges: " + columnSequencesRanges;
                    this.logs.add(tmpLog);
                }

                rowIdx = rowIdx - sequenceLength;
                sequenceId--;
                if(sequenceId >= 0) {
                    sequenceLength = columnSequencesLengths.get(sequenceId);
                } else {
                    break;
                }
            }
        }

        if(columnSequencesRangesChanged) {
            //2
            this.getAffectedColumnsToMarkAvailableSequences().add(columnIdx);
            //3
            this.getAffectedColumnsToFillOverlappingFields().add(columnIdx);
            //12
            this.getAffectedColumnsToPlaceXsAtUnreachableFields().add(columnIdx);
            //14
            this.getAffectedColumnsToPlaceXsAroundLongestSequences().add(columnIdx);
            //18
            this.getAffectedColumnsToExtendColouredFieldsNearX().add(columnIdx);
        }
    }

    /**
     * place an "X" at too short empty fields sequences in columns, when none of row sequences can fit in hole
     */
    public void placeXsColumnsAtTooShortEmptySequences() {

        for (Integer columnIndex : this.getAffectedColumnsToPlaceXsAtTooShortEmptySequences()) {
            placeXsColumnAtTooShortEmptySequences(columnIndex);
        }

    }

    /**
     * place an "X" at too short empty fields sequences in column, when none of row sequences can fit in hole
     */
    public void placeXsColumnAtTooShortEmptySequences(int columnIdx) {

        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequenceRange;

        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);
        int columnSequenceLength;

        List<String> solutionBoardColumn = getNonogramBoardColumn(columnIdx);

        List<Integer> sequencesWhichNotFit;
        List<Integer> sequencesWithinRange;

        //first "X" index (rowIndex) before empty fields sequence in column
        int firstXIndex;
        //last "X" index (rowIndex) after empty fields sequence in column
        int lastXIndex;
        // empty field sequence between "X"s
        int emptyFieldsSequenceLength;
        // empty field sequence range [firstXIndex + 1, lastXIndex - 1]
        List<Integer> emptyFieldsRange;

        // condition true if coloured field exists in emptyFieldsRange (can't place Xs)
        boolean colouredFieldInFieldsSequence;

        //
        for(int rowIdx = 1; rowIdx < this.getHeight() - 1; rowIdx++) {
            //start from first rowIdx(not rowIdx==1), because if rowIdx==0 other method does stuff (correctWhenMetXOnWay)
            if(solutionBoardColumn.get(rowIdx).equals("X")) {

                sequencesWhichNotFit = new ArrayList<>();
                sequencesWithinRange = new ArrayList<>();

                firstXIndex = rowIdx;

                if(solutionBoardColumn.get(rowIdx + 1).equals("-")) {
                    colouredFieldInFieldsSequence = false; // check if in sequence of fields is coloured field (only place 'X' consecutive "-")
                    rowIdx++;
                } else {
                    continue;
                }

                //iterate to the next "X" in column -> find last field of "-" sequence
                while(rowIdx < this.getHeight() && !solutionBoardColumn.get(rowIdx).equals("X")) {
                    //if "O" met, can't place 'X' between potential too short sequence of "-"
                    if (solutionBoardColumn.get(rowIdx).equals("O")) {
                        colouredFieldInFieldsSequence = true;
                        break;
                    }
                    rowIdx++;
                }

                // condition - only empty fields, coloured fields in at least one of consectuive fields not allowed
                if(!colouredFieldInFieldsSequence) {
                    lastXIndex = rowIdx; // index of "X" after empty fields sequence
                    rowIdx--;

                    //range doesn't include leading and trailing "X"s
                    emptyFieldsRange = createRangeFromTwoIntegers(firstXIndex + 1, lastXIndex - 1);
                    //empty sequence length
                    emptyFieldsSequenceLength = rangeLength(emptyFieldsRange);


                    for(int seqNo = 0; seqNo < columnSequencesLengths.size(); seqNo++) {
                        columnSequenceRange = columnSequencesRanges.get(seqNo);
                        columnSequenceLength = columnSequencesLengths.get(seqNo);

                        //check how many sequences ranges includes empty fields sequence range TODO think about second condition
                        if(rangeInsideAnotherRange(emptyFieldsRange, columnSequenceRange) || emptyFieldsSequenceLength > columnSequenceLength) {
                            sequencesWithinRange.add(seqNo);

                            //if empty fields sequence is too short for current columnSequence
                            if(emptyFieldsSequenceLength < columnSequenceLength) {
                                sequencesWhichNotFit.add(seqNo);
                            }
                        }
                    }

                    // if there not any sequence with length equal or less than emptyFieldSequenceLength
                    if(sequencesWhichNotFit.size() == sequencesWithinRange.size() && emptyFieldsSequenceLength > 0) {
                        for(int emptyFieldRowIdx = emptyFieldsRange.get(0); emptyFieldRowIdx <= emptyFieldsRange.get(1); emptyFieldRowIdx++) {
                            // always true? (empty fields sequence)
                            if(this.getNonogramSolutionBoard().get(emptyFieldRowIdx).get(columnIdx).equals("-")) {
                                this.placeXAtGivenPosition(emptyFieldRowIdx, columnIdx);

                                tmpLog = "X placed, column: " + columnIdx + " , row: " + emptyFieldRowIdx + " (too short empty fields in column for sequence). rowSequencesIdsNotToInclude: " + this.getRowsSequencesIdsNotToInclude().get(emptyFieldRowIdx);
                                this.logs.add(tmpLog);

                                this.addRowFieldToNotToInclude(emptyFieldRowIdx, columnIdx);
                                this.addColumnFieldToNotToInclude(columnIdx, emptyFieldRowIdx);

                                //7
                                this.getAffectedRowsToChangeSequencesRangeIfXOnWay().add(emptyFieldRowIdx);
                                //15
                                this.getAffectedRowsToPlaceXsAtTooShortEmptySequences().add(emptyFieldRowIdx);
                                //17
                                this.getAffectedRowsToExtendColouredFieldsNearX().add(emptyFieldRowIdx);

                                this.increaseStepsMade();
                            }  else if (showRepetitions) {
                                System.out.println("X placed in too short column empty field sequence earlier!");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Corrects column sequence(s) range(s) "if X on way" -> if "X" is located too close to the potential starting
     * point counting from the beginning or end of the potential range, then correct the potential range
     */
    public void correctColumnsSequencesIfXOnWay () {
        for (Integer columnIndex : this.getAffectedColumnsToCorrectSequencesRangesIfXOnWay()) {
            correctColumnRangeIndexesIfXOnWay(columnIndex);
        }
    }

    /**
     * @param columnIdx - column to correct sequence/s range/s
     */
    public void correctColumnRangeIndexesIfXOnWay (int columnIdx) {

        boolean columnSequencesRangesChanged = false;

        List<Integer> columnSequences = this.getColumnsSequences().get(columnIdx);
        List<List<Integer>> nonogramColumnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> nonogramColumnsSequencesIdsNotToInclude = this.getColumnsSequencesIdsNotToInclude().get(columnIdx);
        List<List<String>> nonogramBoard = this.getNonogramSolutionBoard();
        int columnSequenceRangeStartIndex;
        int columnSequenceRangeEndIndex;
        int columnSequenceLength;
        List<Integer> columnSequenceRange;

        boolean indexOk;
        int updatedColumnSequenceRangeStartIndex;
        int updatedColumnSequenceRangeEndIndex;
        List<Integer> updatedRange;

        for(int seqNo = 0; seqNo < nonogramColumnSequencesRanges.size(); seqNo++) {
            if(!nonogramColumnsSequencesIdsNotToInclude.contains(seqNo)) {

                columnSequenceRange = nonogramColumnSequencesRanges.get(seqNo);
                columnSequenceLength = columnSequences.get(seqNo);
                columnSequenceRangeStartIndex = columnSequenceRange.get(0);
                columnSequenceRangeEndIndex = columnSequenceRange.get(1);

                updatedColumnSequenceRangeStartIndex = columnSequenceRangeStartIndex;

                for(int rowStartIndex = columnSequenceRangeStartIndex; rowStartIndex < columnSequenceRangeEndIndex - columnSequenceLength + 1; rowStartIndex++) {
                    indexOk = true;
                    for(int rowIdx = rowStartIndex; rowIdx < rowStartIndex + columnSequenceLength; rowIdx++) {
                        if(nonogramBoard.get(rowIdx).get(columnIdx).equals("X")) {
                            indexOk = false;
                            break;
                        }
                    }
                    if(indexOk) {
                        break;
                    } else {
                        updatedColumnSequenceRangeStartIndex++;
                    }
                }

                updatedColumnSequenceRangeEndIndex = columnSequenceRangeEndIndex;

                for(int rowEndIndex = columnSequenceRangeEndIndex; rowEndIndex > columnSequenceRangeStartIndex + columnSequenceLength - 1; rowEndIndex--) {
                    indexOk = true;
                    for(int rowIdx = rowEndIndex; rowIdx > rowEndIndex - columnSequenceLength; rowIdx--) {
                        if(nonogramBoard.get(rowIdx).get(columnIdx).equals("X")) {
                            indexOk = false;
                            break;
                        }
                    }
                    if(indexOk) {
                        break;
                    } else {
                        updatedColumnSequenceRangeEndIndex--;
                    }
                }

                if(updatedColumnSequenceRangeStartIndex != columnSequenceRangeStartIndex || updatedColumnSequenceRangeEndIndex != columnSequenceRangeEndIndex) {
                    columnSequencesRangesChanged = true;
                }

                updatedRange = new ArrayList<>();
                updatedRange.add(updatedColumnSequenceRangeStartIndex);
                updatedRange.add(updatedColumnSequenceRangeEndIndex);
                this.changeColumnSequenceRange(columnIdx, seqNo, updatedRange);

                if(updatedColumnSequenceRangeEndIndex - updatedColumnSequenceRangeStartIndex + 1 == columnSequenceLength) {
                    this.addColumnSequenceIdxToNotToInclude(columnIdx, seqNo);
                }
            }
        }

        if(columnSequencesRangesChanged) {
            //2
            this.getAffectedColumnsToMarkAvailableSequences().add(columnIdx);
            //3
            this.getAffectedColumnsToFillOverlappingFields().add(columnIdx);
            //8
            this.getAffectedColumnsToCorrectSequencesRanges().add(columnIdx);
            //10
            this.getAffectedColumnsToCorrectSequencesRangesIfXOnWay().add(columnIdx);
            //12
            this.getAffectedColumnsToPlaceXsAtUnreachableFields().add(columnIdx);
            //14
            this.getAffectedColumnsToPlaceXsAroundLongestSequences().add(columnIdx);
            //16
            this.getAffectedColumnsToPlaceXsAtTooShortEmptySequences().add(columnIdx);
            //18
            this.getAffectedColumnsToExtendColouredFieldsNearX().add(columnIdx);
        }
    }

    /**
     * place an "X" on fields outside the columns sequence ranges
     */
    public void placeXsColumnsAtUnreachableFields() {

        for (Integer columnIndex : this.getAffectedColumnsToPlaceXsAtUnreachableFields()) {
            placeXsColumnAtUnreachableFields(columnIndex);
        }
    }

    public void placeXsColumnAtUnreachableFields(int columnIdx) {

        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        int nonogramHeight = this.getHeight();
        boolean existRangeIncludingRow;
        List<Integer> fieldAsRange;

        for(int rowIdx = 0; rowIdx < nonogramHeight; rowIdx++) {
            fieldAsRange = new ArrayList<>();
            fieldAsRange.add(rowIdx);
            fieldAsRange.add(rowIdx);
            existRangeIncludingRow = rangesListIncludingAnotherRange(columnSequencesRanges, fieldAsRange);

            if(!existRangeIncludingRow) {
                if(this.getNonogramSolutionBoard().get(rowIdx).get(columnIdx).equals("-")) {
                    this.placeXAtGivenPosition(rowIdx, columnIdx)
                            .addRowFieldToNotToInclude(rowIdx, columnIdx)
                            .addColumnFieldToNotToInclude(columnIdx, rowIdx);

                    tmpLog = "X placed, columnIdx: " + columnIdx + " , rowIdx: " + rowIdx + " (place X at unreachable fields in column).";
                    this.logs.add(tmpLog);

                    //7
                    this.getAffectedRowsToChangeSequencesRangeIfXOnWay().add(rowIdx);
                    //15
                    this.getAffectedRowsToPlaceXsAtTooShortEmptySequences().add(rowIdx);
                    //17
                    this.getAffectedRowsToExtendColouredFieldsNearX().add(rowIdx);
                } else if(showRepetitions) {
                    System.out.println("X at unreachable field in column placed earlier!");
                }
            }
        }
    }

    /**
     * extends coloured subsequences in column to maximum possible length, looking for minimum possible length sequence
     * that can be placed in filled fields range
     */
    public void extendColouredFieldsNearXToMaximumPossibleLengthInColumns() {
        for (Integer columnIndex : this.getAffectedColumnsToExtendColouredFieldsNearX()) {
            extendColouredFieldsToTopNearXToMaximumPossibleLengthInColumn(columnIndex);
            extendColouredFieldsToBottomNearXToMaximumPossibleLengthInColumn(columnIndex);
        }
    }

    /**
     * @param columnIdx - column index on which try to extend subsequence to minimum possible matching (to topo)
     */
    public void extendColouredFieldsToTopNearXToMaximumPossibleLengthInColumn(int columnIdx) {


        List<String> nonogramSolutionBoardColumn = new ArrayList<>();
        int nonogramHeight = this.getHeight();

        // save column to list (from top to bottom) - OK
        for(int rowIdx = 0; rowIdx < nonogramHeight; rowIdx++) {
            nonogramSolutionBoardColumn.add(this.getNonogramSolutionBoard().get(rowIdx).get(columnIdx));
        }

        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);

        List<Integer> possibleSequenceLengths;
        int minimumPossibleLength;
        List<Integer> firstColouredSubsequence;
        int sequenceStartIndex;
        int distanceToX;

        int firstColouredFieldInRange;

        for(int rowIdx = 0; rowIdx < nonogramHeight; rowIdx++) {

            if(nonogramSolutionBoardColumn.get(rowIdx).equals("O")) {

                firstColouredSubsequence = new ArrayList<>();

                firstColouredFieldInRange = rowIdx;
                while(firstColouredFieldInRange >= 0 && nonogramSolutionBoardColumn.get(firstColouredFieldInRange).equals("O")) {
                    firstColouredFieldInRange--;
                }
                firstColouredFieldInRange++; // back to last coloured field (after while index is on "-" (or "X" ???) field

                firstColouredSubsequence.add(firstColouredFieldInRange);
                firstColouredSubsequence.add(rowIdx);

                possibleSequenceLengths = filterSequencesRangesIncludingAnotherAndReturnCorrespondingLengths (
                        columnSequencesRanges, firstColouredSubsequence, columnSequencesLengths);

                if((possibleSequenceLengths.size() == 0)) {
                    this.setSolutionInvalid(true);
                    break;
                }

                minimumPossibleLength = Collections.min(possibleSequenceLengths);

                sequenceStartIndex = rowIdx;

                distanceToX = 0;

                for(int rowToX = 1; rowIdx + rowToX < nonogramHeight && rowToX < minimumPossibleLength; rowToX++) {
                    if(nonogramSolutionBoardColumn.get(rowIdx + rowToX).equals("X")) {
                        distanceToX = rowToX;
                        break;
                    }
                }

                if(distanceToX != 0) {
                    int sequenceEndIndex = sequenceStartIndex + distanceToX - minimumPossibleLength;
                    for(int colourRowIdx = sequenceStartIndex; colourRowIdx >= sequenceEndIndex; colourRowIdx--) {
                        try {
                            if(this.getNonogramSolutionBoard().get(colourRowIdx).get(columnIdx).equals("-")) {
                                this.colourFieldAtGivenPosittion(colourRowIdx, columnIdx);

                                tmpLog = "O placed, columnIdx: " + columnIdx + " , rowIdx: " + colourRowIdx + " (extend coloured fields in sequence to top near X to minimum available length in column).";
                                this.logs.add(tmpLog);

                                //2
                                this.getAffectedColumnsToMarkAvailableSequences().add(columnIdx);
                                //4
                                this.getAffectedRowsToMarkAvailableSequences().add(colourRowIdx);
                                //6
                                this.getAffectedRowsToCorrectSequencesRangesWhenMetColouredField().add(colourRowIdx);
                                //13
                                this.getAffectedRowsToPlaceXsAroundLongestSequences().add(colourRowIdx);
                                //17
                                this.getAffectedRowsToExtendColouredFieldsNearX().add(colourRowIdx);

                                this.increaseStepsMade();
                            } else if(showRepetitions) {
                                System.out.println("Column field was coloured earlier (extending to minimum required - to top).");
                            }
                        } catch (IndexOutOfBoundsException e) {
                            this.setSolutionInvalid(true);
                        }
                    }
                }
            }

        }

    }

    /**
     * @param columnIdx - column index on which try to extend subsequence to minimum possible matching (to bottom)
     */
    public void extendColouredFieldsToBottomNearXToMaximumPossibleLengthInColumn(int columnIdx) {

        List<String> nonogramSolutionBoardColumn = new ArrayList<>();
        int nonogramHeight = this.getHeight();

        for(int rowIdx = 0; rowIdx < nonogramHeight; rowIdx++) {
            nonogramSolutionBoardColumn.add(this.getNonogramSolutionBoard().get(rowIdx).get(columnIdx));
        }

        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);

        List<Integer> possibleSequenceLengths;
        int minimumPossibleLength;
        List<Integer> firstColouredSubsequence;
        int sequenceStartIndex;
        int distanceFromX;

        int lastColouredFieldInRange;

        for(int rowIdx = nonogramHeight - 1; rowIdx >= 0; rowIdx--) {

            if(nonogramSolutionBoardColumn.get(rowIdx).equals("O")) {
                firstColouredSubsequence = new ArrayList<>();

                lastColouredFieldInRange = rowIdx;
                while(lastColouredFieldInRange < nonogramHeight &&
                        nonogramSolutionBoardColumn.get(lastColouredFieldInRange).equals("O")) {
                    lastColouredFieldInRange++;
                }
                lastColouredFieldInRange--;


                // create range from coloured subsequence
                firstColouredSubsequence.add(rowIdx);
                firstColouredSubsequence.add(lastColouredFieldInRange);

                possibleSequenceLengths = filterSequencesRangesIncludingAnotherAndReturnCorrespondingLengths (
                        columnSequencesRanges, firstColouredSubsequence, columnSequencesLengths);

                if(possibleSequenceLengths.size() == 0) {
                    this.setSolutionInvalid(true);
                    break;
                }

                minimumPossibleLength = Collections.min(possibleSequenceLengths);

                sequenceStartIndex = rowIdx;

                distanceFromX = 0;

                for(int rowsFromX = 1; rowIdx - rowsFromX > -1 && rowsFromX < minimumPossibleLength; rowsFromX++) {
                    if(nonogramSolutionBoardColumn.get(rowIdx - rowsFromX).equals("X")) {
                        distanceFromX = rowsFromX;
                        break;
                    }
                }

                if(distanceFromX != 0) {
                    for(int colourRowIdx = sequenceStartIndex;
                        colourRowIdx <= sequenceStartIndex - distanceFromX + minimumPossibleLength; colourRowIdx++) {

                        try {
                            if(this.getNonogramSolutionBoard().get(colourRowIdx).get(columnIdx).equals("-")) {
                                this.colourFieldAtGivenPosittion(colourRowIdx, columnIdx);

                                tmpLog = "O placed, colIdx: " + columnIdx + " , rowIdx: " + colourRowIdx + " (extend coloured fields in sequence to bottom near X to minimum available length in column). Ranges: " + columnSequencesRanges;
                                this.logs.add(tmpLog);

                                //2
                                this.getAffectedColumnsToMarkAvailableSequences().add(columnIdx);
                                //4
                                this.getAffectedRowsToMarkAvailableSequences().add(colourRowIdx);
                                //6
                                this.getAffectedRowsToCorrectSequencesRangesWhenMetColouredField().add(colourRowIdx);
                                //13
                                this.getAffectedRowsToPlaceXsAroundLongestSequences().add(colourRowIdx);
                                //17
                                this.getAffectedRowsToExtendColouredFieldsNearX().add(colourRowIdx);

                                this.increaseStepsMade();
                            } else if(showRepetitions) {
                                System.out.println("Column field was coloured earlier (extending to minimum required - to bottom).");
                            }
                        } catch (IndexOutOfBoundsException e) {
                            this.setSolutionInvalid(true);
                        }

                    }
                }
            }
        }
    }

    /**
     * @param columnIdx - column index to update one of sequences range
     * @param sequenceIdx - column sequence index to update sequence range
     * @param updatedRange - updated column sequence range
     */
    public void updateColumnSequenceRange(int columnIdx, int sequenceIdx, List<Integer> updatedRange) {
        this.columnsSequencesRanges.get(columnIdx).set(sequenceIdx, updatedRange);

    }

    /**
     * @param columnIdx - column index to exclude sequence (fully coloured and marked sequence, sequence range == sequence length)
     * @param seqIdx - sequence index to exclude in specified column
     * @return NonogramLogic object with sequence index in specified column excluded
     */
    public NonogramColumnLogic addColumnSequenceIdxToNotToInclude(int columnIdx, int seqIdx) {
        if(!this.columnsSequencesIdsNotToInclude.get(columnIdx).contains(seqIdx)) {
            this.columnsSequencesIdsNotToInclude.get(columnIdx).add(seqIdx);
            Collections.sort(this.columnsSequencesIdsNotToInclude.get(columnIdx));
        }

        return this;
    }

    public int minimumRowIndexWithoutX(int columnIdx, int lastSequenceRowIdx, int sequenceFullLength) {

        int minimumRowIndex = lastSequenceRowIdx;
        int minimumRowIndexLimit = Math.max(lastSequenceRowIdx - sequenceFullLength + 1, 0);
        String fieldMark;

        for(; minimumRowIndex >= minimumRowIndexLimit; minimumRowIndex--) {
            fieldMark = this.nonogramSolutionBoard.get(minimumRowIndex).get(columnIdx);
            if(fieldMark.equals("X")) {
                break;
            }
        }

        return minimumRowIndex + 1;
    }

    public int maximumRowIndexWithoutX(int columnIdx, int firstSequenceRowIdx, int sequenceFullLength) {

        int maximumRowIndex = firstSequenceRowIdx;
        int maximumRowIndexLimit = Math.min(firstSequenceRowIdx + sequenceFullLength - 1, this.getHeight() - 1);
        String fieldMark;

        for(; maximumRowIndex <= maximumRowIndexLimit; maximumRowIndex++) {
            fieldMark = nonogramSolutionBoard.get(maximumRowIndex).get(columnIdx);
            if(fieldMark.equals("X")) {
                break;
            }
        }

        return maximumRowIndex - 1;
    }

    /**
     * @param colIdx - column to mark sequence with its identifier (1st sequence -> "b", 2nd sequence -> "c", etc)
     * @param rowIdx - row of field to mark with column sequence identifier
     * @param marker - column sequence marker/identifier
     */
    public void markColumnBoardField(int rowIdx, int colIdx, String marker) {
        String oldRowField = this.nonogramSolutionBoardWithMarks.get(rowIdx).get(colIdx);
        this.nonogramSolutionBoardWithMarks.get(rowIdx).set(colIdx, oldRowField.substring(0, 2) + "C" + marker);

    }

    /**
     * @param rowIdx - field row index
     * @param columnIdx - field column index
     * @return NonogramLogic object with "X" placed on specified position
     */
    public NonogramColumnLogic placeXAtGivenPosition(int rowIdx, int columnIdx) {
        boolean rowValid = isRowIndexValid(rowIdx);
        boolean columnValid = isColumnIndexValid(columnIdx);
        if(rowValid && columnValid) {
            this.nonogramSolutionBoard.get(rowIdx).set(columnIdx, "X");
            this.nonogramSolutionBoardWithMarks.get(rowIdx).set(columnIdx, "XXXX");
        }

        return this;
    }

    /**
     * @param rowIdx - field row index
     * @param columnIdx - field column index
     * @return NonogramLogic object with "O" placed on specified position (field coloured)
     */
    public NonogramColumnLogic colourFieldAtGivenPosittion(int rowIdx, int columnIdx) {
        boolean rowValid = isRowIndexValid(rowIdx);
        boolean columnValid = isColumnIndexValid(columnIdx);
        if(rowValid && columnValid) {
            this.nonogramSolutionBoard.get(rowIdx).set(columnIdx, "O");
        }

        return this;
    }

    /**
     * @param rowIdx - row index to exclude field ("X" or part of fully coloured and marked sequence)
     * @param columnIdx - field column index to exclude in given row
     * @return NonogramLogic object with field in given row excluded
     */
    public NonogramColumnLogic addRowFieldToNotToInclude(int rowIdx, int columnIdx) {
        boolean rowValid = isRowIndexValid(rowIdx);
        boolean columnValid = isColumnIndexValid(columnIdx);
        List<Integer> fieldsNotToIncludeSorted;
        if(rowValid && columnValid && !this.rowsFieldsNotToInclude.get(rowIdx).contains(columnIdx)) {
            this.rowsFieldsNotToInclude.get(rowIdx).add(columnIdx);

            fieldsNotToIncludeSorted = this.rowsFieldsNotToInclude.get(rowIdx);
            Collections.sort(fieldsNotToIncludeSorted);
        }

        return this;
    }

    /**
     * @param columnIndex - column index to change one of its sequences range
     * @param sequenceIndex - sequence index to change range
     * @param updatedRange - column sequence updatedRange
     * @return NonogramColumnLogic object with sequence in column with index <columnIdx> and sequence index <sequenceIndex>
     *     updated to range <updatedRange>
     */
    public NonogramColumnLogic changeColumnSequenceRange(int columnIndex, int sequenceIndex, List<Integer> updatedRange) {
        this.columnsSequencesRanges.get(columnIndex).set(sequenceIndex, updatedRange);
        return this;
    }

    /**
     * @param columnIdx - column index to exclude field ("X" or part of fully coloured and marked sequence)
     * @param rowIdx - field row index to exclude in given column
     * @return NonogramLogic object with field in given column excluded
     */
    public NonogramColumnLogic addColumnFieldToNotToInclude(int columnIdx, int rowIdx) {
        boolean rowValid = isRowIndexValid(rowIdx);
        boolean columnValid = isColumnIndexValid(columnIdx);
        if(rowValid && columnValid && !this.columnsFieldsNotToInclude.get(columnIdx).contains(rowIdx) && rowIdx < this.getHeight() && rowIdx >= 0) {
            this.columnsFieldsNotToInclude.get(columnIdx).add(rowIdx);
            Collections.sort(this.columnsFieldsNotToInclude.get(columnIdx));
        }

        return this;
    }

    /**
     * @param columnIdx board column index
     * @return nonogram board column of given index
     */
    public List<String> getNonogramBoardColumn(int columnIdx) {
        List<String> solutionBoardColumn = new ArrayList<>();

        for(int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            solutionBoardColumn.add(nonogramSolutionBoard.get(rowIdx).get(columnIdx));
        }

        return solutionBoardColumn;
    }

    /**
     * @param columnIdx - column to check if is empty
     * @return true if column is empty ([0]), false if not
     */
    private boolean emptyColumn(int columnIdx) {
        if(columnIdx < this.getWidth()) {
            if(this.getColumnsSequences().get(columnIdx).size() == 1) {
                if(this.getColumnsSequences().get(columnIdx).get(0) == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * @param rowIdx - row index to validate if is in range <0, height)
     * @return true if row index is valid(in range) or false if not
     */
    private boolean isRowIndexValid (int rowIdx) {
        return rowIdx >= 0 && rowIdx < this.getHeight();
    }

    /**
     * @param columnIdx - column index to validate if is in range <0, width)
     * @return true if column index is valid(in range) or false if not
     */
    private boolean isColumnIndexValid (int columnIdx) {
        return columnIdx >= 0 && columnIdx < this.getWidth();
    }

    public NonogramColumnLogic increaseStepsMade() {
        this.newStepsMade = this.newStepsMade + 1;
        return this;
    }

    public int getHeight() {
        return this.getRowsSequences().size();
    }

    public int getWidth() {
        return this.getColumnsSequences().size();
    }
}
