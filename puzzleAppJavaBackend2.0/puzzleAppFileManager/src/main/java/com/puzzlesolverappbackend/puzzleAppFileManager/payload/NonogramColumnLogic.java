package com.puzzlesolverappbackend.puzzleAppFileManager.payload;

import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.ActionEnum;
import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.NonogramState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.puzzlesolverappbackend.puzzleAppFileManager.payload.ActionsConstants.actionsToDoAfterColouringOverlappingSequencesInColumns;
import static com.puzzlesolverappbackend.puzzleAppFileManager.payload.ActionsConstants.actionsToDoAfterCorrectingRangesWhenMarkingSequencesInColumns;
import static com.puzzlesolverappbackend.puzzleAppFileManager.services.NonogramLogicService.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Slf4j
public class NonogramColumnLogic {

    boolean showRepetitions = false;
    private NonogramState nonogramState;
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
        this.nonogramState = nonogramLogic.getNonogramState();
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
        this.affectedRowsToChangeSequencesRangeIfXOnWay = nonogramLogic.getAffectedRowsToCorrectSequencesRangesIfXOnWay();
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
        int matchingSequencesCount;
        int lastMatchingSequenceIndex = -1;
        int colouredSequenceLength;
        String sequenceMarker;

        List<Integer> oldOnlyMatchingSequenceRange;
        List<Integer> newSequenceRange;

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

                for(int seqNo = 0; seqNo < columnSequences.size(); seqNo++) {
                    columnSequenceRange = columnSequencesRanges.get(seqNo);

                    if( rangeInsideAnotherRange(colouredSequenceIndexes, columnSequenceRange)
                            && colouredSequenceLength <= columnSequences.get(seqNo)) {
                        matchingSequencesCount++;
                        lastMatchingSequenceIndex = seqNo;
                    }
                }

                if(matchingSequencesCount == 1) {

                    sequenceMarker = NonogramLogic.indexToSequenceCharMark(lastMatchingSequenceIndex);
                    for(int sequenceRowIdx = firstSequenceIndex; sequenceRowIdx <= lastSequenceIndex; sequenceRowIdx++) {
                        if(this.getNonogramSolutionBoardWithMarks().get(sequenceRowIdx).get(columnIdx).substring(2).equals("--")) {
                            this.markColumnBoardField(sequenceRowIdx, columnIdx, sequenceMarker);
                            this.nonogramState.increaseMadeSteps();
                        } else if (this.showRepetitions) {
                            System.out.println("Column field was marked earlier.");
                        }
                    }

                    //correct sequence range if new range is shorter
                    oldOnlyMatchingSequenceRange = columnSequencesRanges.get(lastMatchingSequenceIndex);
                    newSequenceRange = calculateNewRangeFromParameters(colouredSequenceIndexes, columnSequences.get(lastMatchingSequenceIndex));
                    if(rangeLength(newSequenceRange) < rangeLength(oldOnlyMatchingSequenceRange)) {
                        this.changeColumnSequenceRange(columnIdx, lastMatchingSequenceIndex, newSequenceRange);
                        tmpLog = generateCorrectingColumnSequenceRangeStepDescription(columnIdx, lastMatchingSequenceIndex, oldOnlyMatchingSequenceRange, newSequenceRange, "correcting column sequence range when marking field");
                        addLog(tmpLog);
                        addColumnToAffectedActionsByIdentifiers(columnIdx, actionsToDoAfterCorrectingRangesWhenMarkingSequencesInColumns);
                    }
                }
            }
        }
    }

    private List<Integer> calculateNewRangeFromParameters(List<Integer> colouredSequenceIndexes, int sequenceLength) {
        int newRangeBegin = Math.max(0, colouredSequenceIndexes.get(1) - sequenceLength + 1);
        int newRangeEnd = Math.min(colouredSequenceIndexes.get(0) + sequenceLength - 1, this.getHeight() - 1);
        return List.of(newRangeBegin, newRangeEnd);
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
        List<Integer> range;

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
            range = sequencesInColumnRanges.get(sequenceIdx);
            rangeBeginIndex = range.get(0);
            rangeEndIndex = range.get(1);

            colourBeginRowIndex = rangeEndIndex - sequenceLength + 1;
            colourEndRowIndex = rangeBeginIndex + sequenceLength - 1;

            if(colourBeginRowIndex <= colourEndRowIndex) {

                sequenceCharMark = NonogramLogic.indexToSequenceCharMark(sequenceIdx);

                for (int rowIdx = colourBeginRowIndex; rowIdx <= colourEndRowIndex; rowIdx++) {
                    rowToChangeColumnBoardWithMarks = this.getNonogramSolutionBoardWithMarks().get(rowIdx);
                    elementToChangeInsideRowBoardWithMarks = rowToChangeColumnBoardWithMarks.get(columnIdx);

                    if(rowToChangeColumnBoardWithMarks.get(columnIdx).substring(2).equals("--")) {
                        this.nonogramState.increaseMadeSteps();
                        rowToChangeColumnBoardWithMarks.set(columnIdx, elementToChangeInsideRowBoardWithMarks.substring(0, 2) + "C" + sequenceCharMark);

                        this.addRowToAffectedActionsByIdentifiers(rowIdx, actionsToDoAfterColouringOverlappingSequencesInColumns); // TODO - move to if below ?

                        this.getNonogramSolutionBoardWithMarks().set(rowIdx, rowToChangeColumnBoardWithMarks);

                        rowToChangeColumnBoard = this.getNonogramSolutionBoard().get(rowIdx);
                        if(rowToChangeColumnBoard.get(columnIdx).equals("-")) {
                            this.colourFieldAtGivenPosition(rowIdx, columnIdx);
                            tmpLog = generateColourStepDescription(columnIdx, rowIdx, "fill overlapping fields");
                            addLog(tmpLog);

                            this.getNonogramSolutionBoard().set(rowIdx, rowToChangeColumnBoard);
                        }
                    } else if (showRepetitions) {
                            System.out.println("Column field was coloured earlier!");
                    }

                    if(rangeLength(List.of(colourBeginRowIndex, colourEndRowIndex)) == columnsSequences.get(columnIdx).get(sequenceIdx)) {
                        this.addColumnSequenceIdxToNotToInclude(columnIdx, sequenceIdx);
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
        int columnSequenceLength;
        int sequenceOnBoardLength;

        List<Integer> columnSequencesIndexesIncludingSequenceRange;
        List<Integer> columnSequencesLengthsIncludingSequenceRange;

        int firstXRowIndex;
        int lastXRowIndex;

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
                sequenceOnBoardLength = rangeLength(colouredSequenceRange);
                columnSequencesIndexesIncludingSequenceRange = new ArrayList<>();
                columnSequencesLengthsIncludingSequenceRange = new ArrayList<>();

                for(int seqNo = 0; seqNo < columnSequencesRanges.size(); seqNo++) {
                    columnSequenceRange = columnSequencesRanges.get(seqNo);
                    columnSequenceLength = columnSequencesLengths.get(seqNo);
                    if(rangeInsideAnotherRange(colouredSequenceRange, columnSequenceRange) && rangeLength(colouredSequenceRange) <= columnSequenceLength) {
                        columnSequencesIndexesIncludingSequenceRange.add(seqNo);
                        columnSequencesLengthsIncludingSequenceRange.add(columnSequencesLengths.get(seqNo));
                    }
                }

                firstXRowIndex = colouredSequenceRange.get(0) - 1;
                lastXRowIndex = colouredSequenceRange.get(1) + 1;


                if(columnSequencesIndexesIncludingSequenceRange.size() == 1 ) {

                    columnSequenceIdxNotToInclude = columnSequencesIndexesIncludingSequenceRange.get(0);

                    if(sequenceOnBoardLength == columnSequencesLengthsIncludingSequenceRange.get(0)) {

                        if(firstXRowIndex > -1) {

                            if (this.getNonogramSolutionBoard().get(firstXRowIndex).get(columnIdx).equals("-")) {

                                tmpLog = generatePlacingXStepDescription(columnIdx, firstXRowIndex, "placing \"X\" before longest sequence (only possible)");
                                addLog(tmpLog);

                                this.placeXAtGivenPosition(firstXRowIndex, columnIdx)
                                        .addRowFieldToNotToInclude(firstXRowIndex, columnIdx)
                                        .addColumnFieldToNotToInclude(columnIdx, firstXRowIndex);

                                this.nonogramState.increaseMadeSteps();

                                this.addRowToAffectedActionsByIdentifiers(firstXRowIndex, ActionsConstants.actionsToDoAfterPlacingXsAroundLongestSequencesInColumns);
                                this.addColumnToAffectedActionsByIdentifiers(columnIdx, ActionsConstants.actionsToDoAfterPlacingXsAroundLongestSequencesInColumns);
                            } else if(showRepetitions) {
                                System.out.println("Longest sequence in column firstXRowIndex added earlier!");
                            }
                        }

                        if (lastXRowIndex < this.getHeight()) {
                            if( this.getNonogramSolutionBoard().get(lastXRowIndex).get(columnIdx).equals("-")) {

                                tmpLog = generatePlacingXStepDescription(columnIdx, lastXRowIndex, "placing \"X\" after longest sequence in column (only possible)");
                                addLog(tmpLog);

                                this.placeXAtGivenPosition(lastXRowIndex, columnIdx)
                                        .addColumnFieldToNotToInclude(columnIdx, lastXRowIndex)
                                        .addRowFieldToNotToInclude(lastXRowIndex, columnIdx);

                                this.nonogramState.increaseMadeSteps();

                                this.addRowToAffectedActionsByIdentifiers(lastXRowIndex, ActionsConstants.actionsToDoAfterPlacingXsAroundLongestSequencesInColumns);
                                this.addColumnToAffectedActionsByIdentifiers(columnIdx, ActionsConstants.actionsToDoAfterPlacingXsAroundLongestSequencesInColumns);
                            } else if(showRepetitions) {
                                System.out.println("Longest sequence in column lastXRowIndex added earlier!");
                            }
                        }

                        for(int sequenceRowIdx = firstXRowIndex + 1; sequenceRowIdx < lastXRowIndex; sequenceRowIdx++) {
                            this.addColumnFieldToNotToInclude(columnIdx, sequenceRowIdx);
                            this.nonogramState.increaseMadeSteps();
                        }

                        oldSequenceRange = columnSequencesRanges.get(columnSequenceIdxNotToInclude);
                        updatedSequenceRange = List.of(firstXRowIndex + 1, lastXRowIndex - 1);
                        this.changeColumnSequenceRange(columnIdx, columnSequenceIdxNotToInclude, updatedSequenceRange);
                        tmpLog = generateCorrectingColumnSequenceRangeStepDescription(columnIdx, columnSequenceIdxNotToInclude, oldSequenceRange, updatedSequenceRange, "correcting sequence while placing X before only matching coloured sequence");
                        addLog(tmpLog);

                        this.addColumnSequenceIdxToNotToInclude(columnIdx, columnSequenceIdxNotToInclude);
                    }
                } else if(columnSequencesIndexesIncludingSequenceRange.size() > 1) {
                    //check if length of sequence == Max(foundSequences_lengths)
                    if(sequenceOnBoardLength == Collections.max(columnSequencesLengthsIncludingSequenceRange)) {
                        if(this.getNonogramSolutionBoard().get(firstXRowIndex).get(columnIdx).equals("-")) {
                            this.placeXAtGivenPosition(firstXRowIndex, columnIdx)
                                    .addColumnFieldToNotToInclude(columnIdx, firstXRowIndex)
                                    .addRowFieldToNotToInclude(firstXRowIndex, columnIdx);

                            this.nonogramState.increaseMadeSteps();

                            tmpLog = generatePlacingXStepDescription(columnIdx, firstXRowIndex, "placing \"X\" before longest sequence in column - sequence index not specified");
                            addLog(tmpLog);

                            this.addRowToAffectedActionsByIdentifiers(firstXRowIndex, ActionsConstants.actionsToDoAfterPlacingXsAroundLongestSequencesInColumns);
                            this.addColumnToAffectedActionsByIdentifiers(columnIdx, ActionsConstants.actionsToDoAfterPlacingXsAroundLongestSequencesInColumns);
                        } else if (showRepetitions) {
                            System.out.println("Sequence with maximum length in area firstXRowIndex placed earlier!");
                        }
                        if(this.getNonogramSolutionBoard().get(lastXRowIndex).get(columnIdx).equals("-")) {
                            this.placeXAtGivenPosition(lastXRowIndex, columnIdx)
                                    .addColumnFieldToNotToInclude(columnIdx, lastXRowIndex)
                                    .addRowFieldToNotToInclude(lastXRowIndex, columnIdx);

                            this.nonogramState.increaseMadeSteps();

                            tmpLog = generatePlacingXStepDescription(columnIdx, lastXRowIndex, "placing \"X\" after longest sequence in column - sequence index not specified");
                            addLog(tmpLog);

                            this.addRowToAffectedActionsByIdentifiers(lastXRowIndex, ActionsConstants.actionsToDoAfterPlacingXsAroundLongestSequencesInColumns);
                            this.addColumnToAffectedActionsByIdentifiers(columnIdx, ActionsConstants.actionsToDoAfterPlacingXsAroundLongestSequencesInColumns);
                        } else if(showRepetitions) {
                            System.out.println("Sequence with maximum length in area lastXRowIndex placed earlier!");
                        }
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
            List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIndex);

            if(!emptyColumn(columnIndex)) {
                for(int seqNo = 0; seqNo < columnSequencesRanges.size(); seqNo++) {
                    if(rangeLength(columnSequencesRanges.get(seqNo)) < sequencesLengths.get(seqNo)) {
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

    public void correctColumnSequencesRanges(int columnIdx) {
        correctSequencesRangesInColumnFromTop(columnIdx);
        correctSequencesRangesInColumnFromBottom(columnIdx);
    }

    public void correctSequencesRangesInColumnFromTop(int columnIdx) {

        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnFieldsNotToInclude = this.getColumnsFieldsNotToInclude().get(columnIdx);

        List<Integer> fullSequenceRange;
        List<Integer> currentSequenceRange;
        List<Integer> nextSequenceRange;
        int updatedNextSequenceBeginRangeRowIndex;
        int updatedStartIndex;
        int nextSequenceOldBeginRangeRowIndex;
        int nextSequenceId;
        List<Integer> updatedNextSequenceRange;

        List<Integer> columnSequencesIdsNotToInclude = this.getColumnsSequencesIdsNotToInclude().get(columnIdx);

        int currentSequenceLength;

        for(int sequenceIdx = 0; sequenceIdx < columnSequencesRanges.size() - 1; sequenceIdx++) {
            nextSequenceId = sequenceIdx + 1;
            if(columnSequencesIdsNotToInclude.contains(sequenceIdx) && !columnSequencesIdsNotToInclude.contains(nextSequenceId)) {
                fullSequenceRange = columnSequencesRanges.get(sequenceIdx);

                nextSequenceRange = columnSequencesRanges.get(nextSequenceId);
                updatedNextSequenceBeginRangeRowIndex = fullSequenceRange.get(1) + 2;

                while(columnFieldsNotToInclude.contains(updatedNextSequenceBeginRangeRowIndex)) {
                    updatedNextSequenceBeginRangeRowIndex++;
                }

                nextSequenceOldBeginRangeRowIndex = nextSequenceRange.get(0);

                updatedNextSequenceRange = new ArrayList<>();

                updatedStartIndex = Math.max(nextSequenceOldBeginRangeRowIndex, updatedNextSequenceBeginRangeRowIndex);
                updatedNextSequenceRange.add(updatedStartIndex);
                updatedNextSequenceRange.add(nextSequenceRange.get(1));

                if(updatedStartIndex != nextSequenceOldBeginRangeRowIndex) {
                    this.updateColumnSequenceRange(columnIdx, nextSequenceId, updatedNextSequenceRange);
                    this.addColumnToAffectedActionsByIdentifiers(columnIdx, ActionsConstants.actionsToDoAfterCorrectingColumnsSequences);
                    this.nonogramState.increaseMadeSteps();
                    tmpLog = generateCorrectingColumnSequenceRangeStepDescription(columnIdx, nextSequenceId, nextSequenceRange, updatedNextSequenceRange, "correcting from top");
                    addLog(tmpLog);

                    if(rangeLength(updatedNextSequenceRange) == columnSequencesLengths.get(nextSequenceId) && isRowRangeColoured(columnIdx, updatedNextSequenceRange)) {
                        this.addColumnSequenceIdxToNotToInclude(columnIdx, nextSequenceId);
                    }
                }
            }  else if(!columnSequencesIdsNotToInclude.contains(sequenceIdx) && !columnSequencesIdsNotToInclude.contains(nextSequenceId)) {
                currentSequenceLength = columnSequencesLengths.get(sequenceIdx);
                currentSequenceRange = columnSequencesRanges.get(sequenceIdx);
                nextSequenceRange = columnSequencesRanges.get(nextSequenceId);

                updatedNextSequenceBeginRangeRowIndex = currentSequenceRange.get(0) + currentSequenceLength + 1;

                nextSequenceOldBeginRangeRowIndex = nextSequenceRange.get(0);

                updatedNextSequenceRange = new ArrayList<>();

                updatedStartIndex = Math.max(nextSequenceOldBeginRangeRowIndex, updatedNextSequenceBeginRangeRowIndex);

                updatedNextSequenceRange.add(updatedStartIndex);
                updatedNextSequenceRange.add(nextSequenceRange.get(1));

                if(updatedStartIndex != nextSequenceOldBeginRangeRowIndex) {
                    this.updateColumnSequenceRange(columnIdx, nextSequenceId, updatedNextSequenceRange);
                    this.addColumnToAffectedActionsByIdentifiers(columnIdx, ActionsConstants.actionsToDoAfterCorrectingColumnsSequences);
                    this.nonogramState.increaseMadeSteps();
                    tmpLog = generateCorrectingColumnSequenceRangeStepDescription(columnIdx, nextSequenceId, nextSequenceRange, updatedNextSequenceRange, "correcting from top");
                    addLog(tmpLog);

                    if(rangeLength(updatedNextSequenceRange) == columnSequencesLengths.get(nextSequenceId) && isRowRangeColoured(columnIdx, updatedNextSequenceRange)) {
                        this.addColumnSequenceIdxToNotToInclude(columnIdx, nextSequenceId);
                    }
                }
            }
        }
    }

    public void correctSequencesRangesInColumnFromBottom(int columnIdx) {

        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnFieldsNotToInclude = this.getColumnsFieldsNotToInclude().get(columnIdx);

        List<Integer> fullSequenceRange;
        List<Integer> currentSequenceRange;
        List<Integer> oldPreviousSequenceRange;
        int previousSequenceUpdatedRangeEndRowIndex;
        int updatedEndIndex;
        int previousSequenceOldRangeEndRowIndex;
        int previousSequenceIdx;
        List<Integer> updatedPreviousSequenceRange;

        List<Integer> columnSequencesIdsNotToInclude = this.getColumnsSequencesIdsNotToInclude().get(columnIdx);

        int currentSequenceLength;

        for(int sequenceIdx = columnSequencesRanges.size() - 1; sequenceIdx > 0; sequenceIdx--) {
            previousSequenceIdx = sequenceIdx - 1;

            if(columnSequencesIdsNotToInclude.contains(sequenceIdx) && !columnSequencesIdsNotToInclude.contains(previousSequenceIdx)) {
                fullSequenceRange = columnSequencesRanges.get(sequenceIdx);

                oldPreviousSequenceRange = columnSequencesRanges.get(previousSequenceIdx);
                previousSequenceUpdatedRangeEndRowIndex = fullSequenceRange.get(0) - 2;

                while(columnFieldsNotToInclude.contains(previousSequenceUpdatedRangeEndRowIndex)) {
                    previousSequenceUpdatedRangeEndRowIndex--;
                }

                previousSequenceOldRangeEndRowIndex = oldPreviousSequenceRange.get(1);

                updatedPreviousSequenceRange = new ArrayList<>();

                updatedEndIndex = Math.min(previousSequenceOldRangeEndRowIndex, previousSequenceUpdatedRangeEndRowIndex);

                updatedPreviousSequenceRange.add(oldPreviousSequenceRange.get(0));
                updatedPreviousSequenceRange.add(updatedEndIndex);

                if(updatedEndIndex != previousSequenceOldRangeEndRowIndex) {
                    this.getColumnsSequencesRanges().get(columnIdx).set(previousSequenceIdx, updatedPreviousSequenceRange);
                    this.addColumnToAffectedActionsByIdentifiers(columnIdx, ActionsConstants.actionsToDoAfterCorrectingColumnsSequences);
                    this.nonogramState.increaseMadeSteps();
                    tmpLog = generateCorrectingColumnSequenceRangeStepDescription(columnIdx, previousSequenceIdx, oldPreviousSequenceRange, updatedPreviousSequenceRange,
                            "correcting from bottom (sequenceIdx=" + sequenceIdx + " not to include)");
                    addLog(tmpLog);

                    if(rangeLength(updatedPreviousSequenceRange) == columnSequencesLengths.get(previousSequenceIdx) && isRowRangeColoured(columnIdx, updatedPreviousSequenceRange)) {
                        this.addColumnSequenceIdxToNotToInclude(columnIdx, previousSequenceIdx);
                    }
                }
            } else if(!columnSequencesIdsNotToInclude.contains(sequenceIdx) && !columnSequencesIdsNotToInclude.contains(previousSequenceIdx)) {
                currentSequenceLength = columnSequencesLengths.get(sequenceIdx);
                currentSequenceRange = columnSequencesRanges.get(sequenceIdx);
                oldPreviousSequenceRange = columnSequencesRanges.get(previousSequenceIdx);

                previousSequenceUpdatedRangeEndRowIndex = currentSequenceRange.get(1) - currentSequenceLength - 1;

                previousSequenceOldRangeEndRowIndex = oldPreviousSequenceRange.get(1);

                updatedPreviousSequenceRange = new ArrayList<>();

                updatedEndIndex = Math.min(previousSequenceOldRangeEndRowIndex, previousSequenceUpdatedRangeEndRowIndex);

                updatedPreviousSequenceRange.add(oldPreviousSequenceRange.get(0));
                updatedPreviousSequenceRange.add(updatedEndIndex);

                if(updatedEndIndex != previousSequenceOldRangeEndRowIndex) {
                    this.getColumnsSequencesRanges().get(columnIdx).set(previousSequenceIdx, updatedPreviousSequenceRange);
                    this.addColumnToAffectedActionsByIdentifiers(columnIdx, ActionsConstants.actionsToDoAfterCorrectingColumnsSequences);
                    this.nonogramState.increaseMadeSteps();
                    tmpLog = generateCorrectingColumnSequenceRangeStepDescription(columnIdx, previousSequenceIdx, oldPreviousSequenceRange, updatedPreviousSequenceRange,
                            "correcting from bottom (sequenceIdx=" + sequenceIdx + " to include)");
                    addLog(tmpLog);

                    if(rangeLength(updatedPreviousSequenceRange) == columnSequencesLengths.get(previousSequenceIdx) && isRowRangeColoured(columnIdx, updatedPreviousSequenceRange)) {
                        this.addColumnSequenceIdxToNotToInclude(columnIdx, previousSequenceIdx);
                    }
                }
            }
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
        correctSequencesRangesWhenMetColouredFieldInColumnFromTop(columnIdx);
        correctSequencesRangesWhenMetColouredFieldInColumnFromBottom(columnIdx);
    }

    public void correctSequencesRangesWhenMetColouredFieldInColumnFromTop(int columnIdx) {
        boolean columnSequencesRangesChanged = false;

        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);
        int columnSequenceRangeStart;
        int columnSequenceRangeEnd;
        List<Integer> oldSequenceRange;
        List<Integer> updatedSequenceRange;

        List<String> solutionBoardColumn = getNonogramBoardColumn(columnIdx);

        int sequenceId = 0;
        int sequenceLength = columnSequencesLengths.get(sequenceId);
        int updatedEndIndex;
        int maximumPossibleSequenceRangeEnd;

        for(int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            if(solutionBoardColumn.get(rowIdx).equals("O")) {
                oldSequenceRange = columnSequencesRanges.get(sequenceId);
                columnSequenceRangeStart = oldSequenceRange.get(0);
                columnSequenceRangeEnd = oldSequenceRange.get(1);
                maximumPossibleSequenceRangeEnd = rowIdx + sequenceLength - 1;
                if(columnIdx == 15) {
                    System.out.printf("sequenceId: %d, rowIdx: %d, maximumPossibleSequenceRangeEnd: %d\n", sequenceId, rowIdx, maximumPossibleSequenceRangeEnd);
                }

                updatedSequenceRange = new ArrayList<>();
                updatedSequenceRange.add(columnSequenceRangeStart);
                updatedEndIndex = Math.min(columnSequenceRangeEnd, maximumPossibleSequenceRangeEnd);
                updatedSequenceRange.add(updatedEndIndex);
                if(updatedEndIndex != columnSequenceRangeEnd) {
                    columnSequencesRangesChanged = true;
                    this.getColumnsSequencesRanges().get(columnIdx).set(sequenceId, updatedSequenceRange);
                    tmpLog = generateCorrectingColumnSequenceRangeStepDescription(columnIdx, sequenceId, oldSequenceRange, updatedSequenceRange, "correcting from top (met coloured field).");
                    addLog(tmpLog);
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

        if(columnSequencesRangesChanged) {
            this.addColumnToAffectedActionsByIdentifiers(columnIdx, ActionsConstants.actionsToDoAfterCorrectingColumnsSequencesWhenMetColouredField);
        }
    }

    public void correctSequencesRangesWhenMetColouredFieldInColumnFromBottom(int columnIdx) {
        boolean columnSequencesRangesChanged = false;

        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);
        int columnSequenceRangeStart;
        int columnSequenceRangeEnd;
        List<Integer> oldSequenceRange;
        List<Integer> updatedSequenceRange;

        List<String> solutionBoardColumn = getNonogramBoardColumn(columnIdx);

        int sequenceId = columnSequencesLengths.size() - 1;
        int sequenceLength = columnSequencesLengths.get(sequenceId);
        int updatedStartIndex;
        int minimumPossibleSequenceRangeStart;

        for(int rowIdx = this.getHeight() - 1; rowIdx >= 0 ; rowIdx--) {
            if(solutionBoardColumn.get(rowIdx).equals("O")) {
                oldSequenceRange = columnSequencesRanges.get(sequenceId);
                columnSequenceRangeStart = oldSequenceRange.get(0);
                columnSequenceRangeEnd = oldSequenceRange.get(1);
                minimumPossibleSequenceRangeStart = rowIdx - sequenceLength + 1;

                updatedSequenceRange = new ArrayList<>();
                updatedStartIndex = Math.max(columnSequenceRangeStart, minimumPossibleSequenceRangeStart);
                updatedSequenceRange.add(updatedStartIndex);
                updatedSequenceRange.add(columnSequenceRangeEnd);
                if(updatedStartIndex != columnSequenceRangeStart) {
                    columnSequencesRangesChanged = true;
                    columnSequencesRanges.set(sequenceId, updatedSequenceRange);
                    tmpLog = generateCorrectingColumnSequenceRangeStepDescription(columnIdx, sequenceId, oldSequenceRange, updatedSequenceRange, "correcting from bottom (met coloured field).");
                    addLog(tmpLog);
                    if(rangeLength(columnSequencesRanges.get(sequenceId)) == sequenceLength && isRowRangeColoured(columnIdx, columnSequencesRanges.get(sequenceId))) {
                        this.addColumnSequenceIdxToNotToInclude(columnIdx, sequenceId);
                    }
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
            this.addColumnToAffectedActionsByIdentifiers(columnIdx, ActionsConstants.actionsToDoAfterCorrectingColumnsSequencesWhenMetColouredField);
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

                                tmpLog = generatePlacingXStepDescription(columnIdx, emptyFieldRowIdx, "placing \"X\" inside too short empty fields sequence");
                                addLog(tmpLog);

                                this.addRowFieldToNotToInclude(emptyFieldRowIdx, columnIdx);
                                this.addColumnFieldToNotToInclude(columnIdx, emptyFieldRowIdx);
                                this.addRowToAffectedActionsByIdentifiers(emptyFieldRowIdx, ActionsConstants.actionsToDoAfterPlacingXsAtTooShortSequencesInColumns);

                                this.nonogramState.increaseMadeSteps();
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
                    updatedRange = new ArrayList<>();
                    updatedRange.add(updatedColumnSequenceRangeStartIndex);
                    updatedRange.add(updatedColumnSequenceRangeEndIndex);
                    tmpLog = generateCorrectingColumnSequenceRangeStepDescription(columnIdx, seqNo, columnSequenceRange, updatedRange, "\"X\" on way");
                    addLog(tmpLog);
                    this.changeColumnSequenceRange(columnIdx, seqNo, updatedRange);
                    if(rangeLength(updatedRange) == this.getColumnsSequences().get(columnIdx).get(seqNo) && isRowRangeColoured(columnIdx, updatedRange)) {
                        this.addColumnSequenceIdxToNotToInclude(columnIdx, seqNo);
                    }
                }
            }
        }

        if(columnSequencesRangesChanged) {
            this.addColumnToAffectedActionsByIdentifiers(columnIdx, ActionsConstants.actionsToDoAfterCorrectingColumnsSequencesIfXOnWay);
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

        if(columnIdx == 0 && columnSequencesRanges.get(0).get(0) == 6 && columnSequencesRanges.get(0).get(1) == 14 && this.getNonogramSolutionBoard().get(5).get(0).equals("-")) {
            addLog("Here!!! Should place X at (5,0)!!!");
        }

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
                    this.addRowToAffectedActionsByIdentifiers(rowIdx, ActionsConstants.actionsToDoAfterPlacingXsAtColumnsUnreachableFields);

                    tmpLog = generatePlacingXStepDescription(columnIdx, rowIdx, "placing \"X\" at unreachable field");
                    addLog(tmpLog);
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

                if((possibleSequenceLengths.isEmpty())) {
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
                                this.addRowToAffectedActionsByIdentifiers(colourRowIdx, ActionsConstants.actionsToDoAfterExtendingColouredFieldsNearXInColumns);
                                this.nonogramState.increaseMadeSteps();

                                tmpLog = generateColourStepDescription(columnIdx, colourRowIdx, "extend coloured fields in sequence to left near X " +
                                        "to length of shortest possible sequence in column");
                                addLog(tmpLog);
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

                if(possibleSequenceLengths.isEmpty()) {
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
                                this.addRowToAffectedActionsByIdentifiers(colourRowIdx, ActionsConstants.actionsToDoAfterExtendingColouredFieldsNearXInColumns);
                                this.nonogramState.increaseMadeSteps();

                                tmpLog = generateColourStepDescription(columnIdx, colourRowIdx, "extend coloured fields in sequence to bottom near X " +
                                        "to length of shortest possible sequence in row");
                                addLog(tmpLog);
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

    private boolean isRowRangeColoured(int columnIdx, List<Integer> rowRange) {
        for(Integer rowIdx : rowRange) {
            if(!this.getNonogramSolutionBoard().get(rowIdx).get(columnIdx).equals("O")) {
                return false;
            }
        }

        return true;
    }

    /**
     * @param columnIdx - column index to exclude sequence (fully coloured and marked sequence, sequence range == sequence length)
     * @param seqIdx - sequence index to exclude in specified column
     */
    public void addColumnSequenceIdxToNotToInclude(int columnIdx, int seqIdx) {
        if(!this.columnsSequencesIdsNotToInclude.get(columnIdx).contains(seqIdx)) {
            tmpLog = generateAddingSequenceToNotToIncludeDescription(columnIdx, seqIdx);
            addLog(tmpLog);
            this.columnsSequencesIdsNotToInclude.get(columnIdx).add(seqIdx);
            Collections.sort(this.columnsSequencesIdsNotToInclude.get(columnIdx));
        }
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
     */
    public void colourFieldAtGivenPosittion(int rowIdx, int columnIdx) {
        boolean rowValid = isRowIndexValid(rowIdx);
        boolean columnValid = isColumnIndexValid(columnIdx);
        if(rowValid && columnValid) {
            this.nonogramSolutionBoard.get(rowIdx).set(columnIdx, "O");
        }
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
     */
    public void changeColumnSequenceRange(int columnIndex, int sequenceIndex, List<Integer> updatedRange) {
        this.columnsSequencesRanges.get(columnIndex).set(sequenceIndex, updatedRange);
    }

    public void colourFieldAtGivenPosition(int rowIdx, int columnIdx) {
        boolean rowValid = isRowIndexValid(rowIdx);
        boolean columnValid = isColumnIndexValid(columnIdx);
        if(rowValid && columnValid) {
            this.nonogramSolutionBoard.get(rowIdx).set(columnIdx, "O");
        }
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
                return this.getColumnsSequences().get(columnIdx).get(0) == 0;
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

    public int getHeight() {
        return this.getRowsSequences().size();
    }

    public int getWidth() {
        return this.getColumnsSequences().size();
    }

    public void addLog(String log) {

        //String situationOccuredMarker = situationDetectionCondition()  ? "+" : "-";
        if (log.isEmpty()) {
            System.out.println("Trying to add empty log!!!");
        } else {
            this.logs.add(log/* + situationOccuredMarker*/);
        }
    }

    public boolean situationDetectionCondition() {
        return true;
    }

    private String generateColourStepDescription(int columnIndex, int rowIndex, String actionType) {
        if(columnIndex == 3 && rowIndex == 0) {
            this.addLog(this.getColumnsSequencesRanges().get(columnIndex).toString());
        }
        return String.format("COLUMN %d, ROW %d - field colouring - %s.", columnIndex, rowIndex, actionType);
    }

    public String generatePlacingXStepDescription(int columnIndex, int rowIndex, String actionType) {
        return String.format("COLUMN %d, ROW %d - X placing - %s.", columnIndex, rowIndex, actionType);
    }

    public String generateCorrectingColumnSequenceRangeStepDescription(int columnIndex, int sequenceIndex, List<Integer> oldRange, List<Integer> correctedRange, String actionType) {
        return String.format("COLUMN %d, SEQUENCE %d - range correcting - from [%d, %d] to [%d, %d] - %s", columnIndex, sequenceIndex,
                oldRange.get(0), oldRange.get(1), correctedRange.get(0), correctedRange.get(1), actionType);
    }

    public String generateMarkingSequenceStepDescription(int columnIdx, int rowIdx, String actionType) {
        return String.format("COLUMN %d, ROW %d - %s", columnIdx, rowIdx, actionType);
    }

    public String generateAddingSequenceToNotToIncludeDescription(int columnIdx, int seqNo) {
        return String.format("COLUMN %d - SeqNo = %d added to not to include", columnIdx, seqNo);
    }

    private void addRowToAffectedActionsByIdentifiers(int rowIdx, List<ActionEnum> affectedActions) {
        for(ActionEnum affectedAction : affectedActions) {
            switch (affectedAction) {
                case CORRECT_ROWS_SEQUENCES_RANGES -> this.getAffectedRowsToCorrectSequencesRanges().add(rowIdx);
                case CORRECT_ROWS_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS -> this.getAffectedRowsToCorrectSequencesRangesWhenMetColouredField().add(rowIdx);
                case CORRECT_ROWS_SEQUENCES_RANGES_IF_X_ON_WAY -> this.getAffectedRowsToChangeSequencesRangeIfXOnWay().add(rowIdx);
                case COLOUR_OVERLAPPING_FIELDS_IN_ROWS -> this.getAffectedRowsToFillOverlappingFields().add(rowIdx);
                case EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROWS -> this.getAffectedRowsToExtendColouredFieldsNearX().add(rowIdx);
                case PLACE_XS_ROWS_AT_UNREACHABLE_FIELDS -> this.getAffectedRowsToPlaceXsAtUnreachableFields().add(rowIdx);
                case PLACE_XS_ROWS_AROUND_LONGEST_SEQUENCES -> this.getAffectedRowsToPlaceXsAroundLongestSequences().add(rowIdx);
                case PLACE_XS_ROWS_AT_TOO_SHORT_EMPTY_SEQUENCES -> this.getAffectedRowsToPlaceXsAtTooShortEmptySequences().add(rowIdx);
                case MARK_AVAILABLE_FIELDS_IN_ROWS -> this.getAffectedRowsToMarkAvailableSequences().add(rowIdx);
                default -> {}
            }
        }
    }

    private void addColumnToAffectedActionsByIdentifiers(int columnIdx, List<ActionEnum> affectedActions) {
        for(ActionEnum affectedAction : affectedActions) {
            switch (affectedAction) {
                case CORRECT_COLUMNS_SEQUENCES_RANGES -> this.getAffectedColumnsToCorrectSequencesRanges().add(columnIdx);
                case CORRECT_COLUMNS_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS -> this.getAffectedColumnsToCorrectSequencesRangesWhenMetColouredField().add(columnIdx);
                case CORRECT_COLUMNS_SEQUENCES_RANGES_IF_X_ON_WAY -> this.getAffectedColumnsToCorrectSequencesRangesIfXOnWay().add(columnIdx);
                case COLOUR_OVERLAPPING_FIELDS_IN_COLUMNS -> this.getAffectedColumnsToFillOverlappingFields().add(columnIdx);
                case EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMNS -> this.getAffectedColumnsToExtendColouredFieldsNearX().add(columnIdx);
                case PLACE_XS_COLUMNS_AT_UNREACHABLE_FIELDS -> this.getAffectedColumnsToPlaceXsAtUnreachableFields().add(columnIdx);
                case PLACE_XS_COLUMNS_AROUND_LONGEST_SEQUENCES -> this.getAffectedColumnsToPlaceXsAroundLongestSequences().add(columnIdx);
                case MARK_AVAILABLE_FIELDS_IN_COLUMNS -> this.getAffectedColumnsToMarkAvailableSequences().add(columnIdx);
                default -> {}
            }
        }
    }

    private void printSolutionBoard() {
        log.info("Solution board: ");
        for(List<String> boardRow : this.getNonogramSolutionBoard()) {
            System.out.println(boardRow);
        }
    }
}