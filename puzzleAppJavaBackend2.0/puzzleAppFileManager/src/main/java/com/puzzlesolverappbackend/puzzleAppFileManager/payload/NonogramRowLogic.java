package com.puzzlesolverappbackend.puzzleAppFileManager.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

import static com.puzzlesolverappbackend.puzzleAppFileManager.services.NonogramLogicService.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.services.NonogramLogicService.filterSequencesRangesIncludingAnotherAndReturnCorrespondingLengths;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class NonogramRowLogic {
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
    private Set<Integer> affectedRowsToCorrectSequencesRanges;
    //2
    private Set<Integer> affectedColumnsToCorrectSequencesRanges;
    //3
    private Set<Integer> affectedRowsToCorrectSequencesRangesWhenMetColouredField;
    //4
    private Set<Integer> affectedColumnsToCorrectSequencesRangesWhenMetColouredField;
    //5
    private Set<Integer> affectedRowsToChangeSequencesRangeIfXOnWay;
    //6
    private Set<Integer> affectedColumnsToCorrectSequencesRangesIfXOnWay;
    //7
    private Set<Integer> affectedRowsToFillOverlappingFields;
    //8
    private Set<Integer> affectedColumnsToFillOverlappingFields;
    //9
    private Set<Integer> affectedRowsToExtendColouredFieldsNearX;
    //10
    private Set<Integer> affectedColumnsToExtendColouredFieldsNearX;
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
    private Set<Integer> affectedRowsToMarkAvailableSequences;
    //18
    private Set<Integer> affectedColumnsToMarkAvailableSequences;

    private List<List<Integer>> rowsFieldsNotToInclude;
    private List<List<Integer>> columnsFieldsNotToInclude;
    private List<List<Integer>> rowsSequencesIdsNotToInclude;
    private List<List<Integer>> columnsSequencesIdsNotToInclude;

    String tmpLog;

    private boolean solutionInvalid = false;

    public NonogramRowLogic(NonogramLogic nonogramLogic) {
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
     * mark rows sequences with char identifiers
     */
    public void markAvailableSequencesInRows() {
        for (Integer rowIndex : this.getAffectedRowsToMarkAvailableSequences()) {
            markAvailableSequencesInRow(rowIndex);
        }
    }

    /**
     * @param rowIdx - row index on which mark sequences with char identifiers
     */
    public void markAvailableSequencesInRow(int rowIdx) {

        List<String> boardRow = this.getNonogramSolutionBoard().get(rowIdx);
        List<Integer> colouredSequenceIndexes;
        int firstSequenceIndex;
        int lastSequenceIndex;
        List<Integer> rowSequences = this.getRowsSequences().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequenceRange;
        int matchingSequencesCount;
        int lastMatchingSequenceIndex = -1;
        int colouredSequenceLength;
        int matchingSequenceLength;
        boolean sequenceEqualsRowSequenceLength;
        String sequenceMarker;

        List<Integer> oldRange;
        int oldRangeBeginIndex;
        int oldRangeEndIndex;
        int updatedRangeBeginIndex;
        int updatedRangeEndIndex;

        for(int columnIdx = 0; columnIdx < boardRow.size(); columnIdx++) {

            if(boardRow.get(columnIdx).equals("O")) {

                colouredSequenceIndexes = new ArrayList<>();
                colouredSequenceIndexes.add(columnIdx);

                //collect indexes of current coloured sequence
                while(columnIdx < boardRow.size() && boardRow.get(columnIdx).equals("O")) {
                    columnIdx++;
                }

                colouredSequenceIndexes.add(columnIdx - 1);

                firstSequenceIndex = colouredSequenceIndexes.get(0);
                lastSequenceIndex = colouredSequenceIndexes.get(colouredSequenceIndexes.size() - 1);
                colouredSequenceLength = lastSequenceIndex - firstSequenceIndex  + 1;

                matchingSequencesCount = 0;

                //check how many sequences matching conditions for mark
                for(int seqNo = 0; seqNo < rowSequences.size(); seqNo++) {

                    rowSequenceRange = rowSequencesRanges.get(seqNo);

                    if( rangeInsideAnotherRange(colouredSequenceIndexes, rowSequenceRange)
                            && colouredSequenceLength <= rowSequences.get(seqNo)) {
                        matchingSequencesCount++;
                        lastMatchingSequenceIndex = seqNo;
                    }
                }

                //NOTE!!! if matching count == 0 and there are sequences that are not fulfilled -> wrong solution
                if(matchingSequencesCount == 1) {

                    matchingSequenceLength = rowSequences.get(lastMatchingSequenceIndex);
                    sequenceEqualsRowSequenceLength = colouredSequenceLength == matchingSequenceLength;
                    rowSequenceRange = new ArrayList<>();

                    oldRange = this.getRowsSequencesRanges().get(rowIdx).get(lastMatchingSequenceIndex);
                    oldRangeBeginIndex = oldRange.get(0);
                    oldRangeEndIndex = oldRange.get(1);

                    if(sequenceEqualsRowSequenceLength) {
                        rowSequenceRange.add(firstSequenceIndex);
                        rowSequenceRange.add(lastSequenceIndex);
                        this.addRowSequenceIdxToNotToInclude(rowIdx, lastMatchingSequenceIndex);
                    } else {

                        updatedRangeBeginIndex = Math.max(0,
                                this.minimumColumnIndexWithoutX(rowIdx, lastSequenceIndex, matchingSequenceLength));
                        updatedRangeEndIndex = Math.min(this.getWidth() - 1,
                                this.maximumColumnIndexWithoutX(rowIdx, firstSequenceIndex, matchingSequenceLength));

                        rowSequenceRange.add( Math.max(oldRangeBeginIndex ,updatedRangeBeginIndex) );
                        rowSequenceRange.add( Math.min(oldRangeEndIndex, updatedRangeEndIndex) );
                    }

                    if(!rangesEqual(rowSequenceRange, oldRange)) {
                        this.updateRowSequenceRange(rowIdx, lastMatchingSequenceIndex, rowSequenceRange);
                        addRowToAffectedActionsByIdentifiers(rowIdx, ActionsConstants.actionsToDoAfterMarkingSequencesInRows);

                        if(rangeLength(rowSequenceRange) == matchingSequenceLength) {
                            this.addRowSequenceIdxToNotToInclude(rowIdx, lastMatchingSequenceIndex);
                        }
                    }

                    //mark sequence part
                    sequenceMarker = NonogramLogic.indexToSequenceCharMark(lastMatchingSequenceIndex);
                    for(int sequenceColumnIdx = firstSequenceIndex; sequenceColumnIdx <= lastSequenceIndex; sequenceColumnIdx++) {
                        if(this.getNonogramSolutionBoardWithMarks().get(rowIdx).get(sequenceColumnIdx).startsWith("--")) {
                            this.markRowBoardField(rowIdx, sequenceColumnIdx, sequenceMarker);
                            this.increaseStepsMade();
                        } else if(showRepetitions) {
                            System.out.println("Row field was marked earlier.");
                        }
                    }
                }
            }
        }
    }

    /**
     * fill fields in rows where ranges met specific condition
     */
    public void fillOverlappingFieldsInRows() {
        for (Integer rowIndex : this.getAffectedRowsToFillOverlappingFields()) {
            fillOverlappingFieldsInRow(rowIndex);
        }
    }

    /**
     * fill fields in row where ranges met specific condition (range_length < sequence_length * 2)
     */
    public void fillOverlappingFieldsInRow (int rowIdx) {
        List<Integer> sequencesInRow = this.getRowsSequences().get(rowIdx);
        List<List<Integer>> sequencesInRowRanges = this.getRowsSequencesRanges().get(rowIdx);

        int sequenceLength;
        int rangeBeginIndex;
        int rangeEndIndex;
        int colourBeginColumnIndex;
        int colourEndColumnIndex;

        String sequenceCharMark;

        List<String> rowToChangeSolutionBoard;
        List<String> rowToChangeSolutionBoardWithMarks;

        String elementToChangeInsideRowBoardWithMarks;

        for(int sequenceIdx = 0; sequenceIdx < sequencesInRow.size(); sequenceIdx++) {
            sequenceLength = sequencesInRow.get(sequenceIdx);
            rangeBeginIndex = sequencesInRowRanges.get(sequenceIdx).get(0);
            rangeEndIndex = sequencesInRowRanges.get(sequenceIdx).get(1);

            // the range in which we can color the fields from sequence <cBCI, cECI>
            colourBeginColumnIndex = rangeEndIndex - sequenceLength + 1;
            colourEndColumnIndex = rangeBeginIndex + sequenceLength - 1;

            if(colourBeginColumnIndex <= colourEndColumnIndex) {

                sequenceCharMark = NonogramLogic.indexToSequenceCharMark(sequenceIdx);
                rowToChangeSolutionBoardWithMarks = this.getNonogramSolutionBoardWithMarks().get(rowIdx);

                for (int columnIdx = colourBeginColumnIndex; columnIdx <= colourEndColumnIndex; columnIdx++) {
                    elementToChangeInsideRowBoardWithMarks = rowToChangeSolutionBoardWithMarks.get(columnIdx);

                    if(rowToChangeSolutionBoardWithMarks.get(columnIdx).startsWith("--")) {
                        this.increaseStepsMade();
                        rowToChangeSolutionBoardWithMarks.set(columnIdx, "R" + sequenceCharMark + elementToChangeInsideRowBoardWithMarks.substring(2, 4));

                        addColumnToAffectedActionsByIdentifiers(columnIdx, ActionsConstants.actionsToDoAfterColouringOverlappingSequencesInRows);

                        rowToChangeSolutionBoard = this.getNonogramSolutionBoard().get(rowIdx);
                        if(rowToChangeSolutionBoard.get(columnIdx).equals("-")) {
                            this.colourFieldAtGivenPosition(rowIdx, columnIdx);
                            tmpLog = generateColourStepDescription(rowIdx, columnIdx, "fill overlapping fields");
                            addLog(tmpLog);
                        }
                    } else if (showRepetitions) {
                        System.out.println("Row field was coloured earlier!");
                    }
                }
            }
        }
    }

    /**
     * place "X" around coloured fields in rows where sequence of these coloured fields have length of the longest
     * possible sequence length in specific row, that can be located in coloured field sequence range
     */
    // iterations through all rows
    public void placeXsRowsAroundLongestSequences() {
        for (Integer rowIndex : this.getAffectedRowsToPlaceXsAroundLongestSequences()) {
            placeXsRowAroundLongestSequences(rowIndex);
        }
    }


    /**
     * @param rowIdx - row index on which place "X" around coloured fields
     */
    public void placeXsRowAroundLongestSequences(int rowIdx) {

        List<String> nonogramSolutionBoardRow = this.getNonogramSolutionBoard().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);
        int nonogramWidth = this.getWidth();

        List<Integer> colouredSequenceRange;
        List<Integer> rowSequenceRange;
        int sequenceOnBoardLength;

        List<Integer> rowSequencesIndexesIncludingSequenceRange;
        List<Integer> rowSequencesLengthsIncludingSequenceRange;

        int firstXIndex;
        int lastXIndex;

        int rowSequenceIdxNotToInclude;

        for(int columnIdx = 0; columnIdx < nonogramWidth; columnIdx++) {

            if(nonogramSolutionBoardRow.get(columnIdx).equals("O")) {

                colouredSequenceRange = new ArrayList<>();
                colouredSequenceRange.add(columnIdx);

                while(columnIdx < nonogramWidth && nonogramSolutionBoardRow.get(columnIdx).equals("O")) {
                    columnIdx++;
                }

                //when solutionBoard[rowIdx][columnIdx] != "O"
                colouredSequenceRange.add(columnIdx - 1);
                sequenceOnBoardLength = rangeLength(colouredSequenceRange);
                rowSequencesIndexesIncludingSequenceRange = new ArrayList<>();
                rowSequencesLengthsIncludingSequenceRange = new ArrayList<>();

                for(int seqNo = 0; seqNo < rowSequencesRanges.size(); seqNo++) {
                    rowSequenceRange = rowSequencesRanges.get(seqNo);
                    if(rangeInsideAnotherRange(colouredSequenceRange, rowSequenceRange)) {
                        rowSequencesIndexesIncludingSequenceRange.add(seqNo);
                        rowSequencesLengthsIncludingSequenceRange.add(rowSequencesLengths.get(seqNo));
                    }
                }

                firstXIndex = colouredSequenceRange.get(0) - 1;
                lastXIndex = colouredSequenceRange.get(1) + 1;

                if(rowSequencesIndexesIncludingSequenceRange.size() >= 1) {
                    if(rowSequencesIndexesIncludingSequenceRange.size() == 1 ) {

                        rowSequenceIdxNotToInclude = rowSequencesIndexesIncludingSequenceRange.get(0);

                        if(sequenceOnBoardLength == rowSequencesLengthsIncludingSequenceRange.get(0)) {

                            if(!this.getRowsSequencesIdsNotToInclude().get(rowIdx).contains(rowSequenceIdxNotToInclude)) {

                                this.addRowSequenceIdxToNotToInclude(rowIdx, rowSequenceIdxNotToInclude);

                                if(firstXIndex > -1) {
                                    if (this.getNonogramSolutionBoard().get(rowIdx).get(firstXIndex).equals("-")) {

                                        tmpLog = generatePlacingXStepDescription(rowIdx, firstXIndex, "placing \"X\" before longest sequence (only possible)");
                                        addLog(tmpLog);

                                        this.increaseStepsMade().placeXAtGivenPosition(rowIdx, firstXIndex)
                                                .addRowFieldToNotToInclude(rowIdx, firstXIndex)
                                                .addColumnFieldToNotToInclude(firstXIndex, rowIdx);

                                        addColumnToAffectedActionsByIdentifiers(firstXIndex, ActionsConstants.actionsToDoAfterPlacingXsAroundLongestSequencesInRows);

                                    } else if (showRepetitions) {
                                        System.out.println("Placed Xs before longest sequence in row earlier!");
                                    }
                                }
                                if(lastXIndex < this.getWidth() ) {
                                    if(this.getNonogramSolutionBoard().get(rowIdx).get(lastXIndex).equals("-")) {

                                        tmpLog = generatePlacingXStepDescription(rowIdx, lastXIndex, "placing \"X\" before longest sequence in row (only possible)");
                                        addLog(tmpLog);

                                        this.increaseStepsMade().placeXAtGivenPosition(rowIdx, lastXIndex)
                                                .addRowFieldToNotToInclude(rowIdx, lastXIndex)
                                                .addColumnFieldToNotToInclude(lastXIndex, rowIdx);

                                        addColumnToAffectedActionsByIdentifiers(lastXIndex, ActionsConstants.actionsToDoAfterPlacingXsAroundLongestSequencesInRows);
                                    }

                                } else if(showRepetitions) {
                                    System.out.println("Placed Xs after longest sequence in row earlier!");
                                }
                            }

                            for(int sequenceColumnIdx = firstXIndex + 1; sequenceColumnIdx < lastXIndex; sequenceColumnIdx++) {
                                this.addRowFieldToNotToInclude(rowIdx, sequenceColumnIdx);
                            }


                            this.updateRowSequenceRange(rowIdx, rowSequencesIndexesIncludingSequenceRange.get(0),
                                    colouredSequenceRange);

                            this.addRowToAffectedActionsByIdentifiers(rowIdx, Arrays. asList(
                                    ActionsConstants.PLACE_XS_ROWS_AT_UNREACHABLE_FIELDS));
                        }

                    } else if(rowSequencesLengthsIncludingSequenceRange.size() > 1) {

                        //check if length of sequence == Max(foundSequences_lengths)
                        if(sequenceOnBoardLength == Collections.max(rowSequencesLengthsIncludingSequenceRange)) {

                            if(this.getNonogramSolutionBoard().get(rowIdx).get(firstXIndex).equals("-")) {
                                this.increaseStepsMade().placeXAtGivenPosition(rowIdx, firstXIndex)
                                        .addRowFieldToNotToInclude(rowIdx, firstXIndex)
                                        .addColumnFieldToNotToInclude(firstXIndex, rowIdx);

                                tmpLog = generatePlacingXStepDescription(rowIdx, firstXIndex, "placing \"X\" before longest sequence (index not known)");
                                addLog(tmpLog);

                                addColumnToAffectedActionsByIdentifiers(firstXIndex, ActionsConstants.actionsToDoAfterPlacingXsAroundLongestSequencesInRows);

                            } else if(showRepetitions) {
                                System.out.println("Longest sequence in row firstXIndex added earlier!");
                            }

                            if(this.getNonogramSolutionBoard().get(rowIdx).get(lastXIndex).equals("-")) {
                                this.increaseStepsMade().placeXAtGivenPosition(rowIdx, lastXIndex)
                                        .addRowFieldToNotToInclude(rowIdx, lastXIndex)
                                        .addColumnFieldToNotToInclude(lastXIndex, rowIdx);

                                tmpLog = generatePlacingXStepDescription(rowIdx, lastXIndex, "placing \"X\" after longest sequence (index not known)");
                                addLog(tmpLog);

                                addColumnToAffectedActionsByIdentifiers(lastXIndex, ActionsConstants.actionsToDoAfterPlacingXsAroundLongestSequencesInRows);
                            } else if(showRepetitions) {
                                System.out.println("Longest sequence in row lastXIndex added earlier!");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * place an "X" at too short empty fields sequences in rows, when none of row sequences can fit in hole
     */
    public void placeXsRowsAtTooShortEmptySequences() {

        for (Integer rowIndex : this.getAffectedRowsToPlaceXsAtTooShortEmptySequences()) {
            placeXsRowAtTooShortEmptySequences(rowIndex);
        }
    }

    /**
     * place an "X" at too short empty fields sequences in row, when none of row sequences can fit in hole
     */
    public void placeXsRowAtTooShortEmptySequences(int rowIdx) {

        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequenceRange;

        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);
        int rowSequenceLength;

        List<String> solutionBoardRow = this.getNonogramSolutionBoard().get(rowIdx);

        List<Integer> sequencesWhichNotFit;
        List<Integer> sequencesWithinRange;

        int firstXIndex;
        int lastXIndex;
        int emptyFieldsSequenceLength;
        List<Integer> emptyFieldsRange;
        int nonogramWidth = this.getWidth();

        boolean colouredFieldInFieldsSequence;

        for(int columnIdx = 1; columnIdx < nonogramWidth - 1; columnIdx++) {
            if(solutionBoardRow.get(columnIdx).equals("X")) {

                sequencesWhichNotFit = new ArrayList<>();
                sequencesWithinRange = new ArrayList<>();

                firstXIndex = columnIdx;
                if(solutionBoardRow.get(columnIdx + 1).equals("-")) {
                    colouredFieldInFieldsSequence = false;
                    columnIdx++;
                } else {
                    continue;
                }

                while(columnIdx < nonogramWidth && (solutionBoardRow.get(columnIdx).equals("-") ||  solutionBoardRow.get(columnIdx).equals("O")) ) {
                    if (solutionBoardRow.get(columnIdx).equals("O")) {
                        colouredFieldInFieldsSequence = true;
                        break;
                    }
                    columnIdx++;
                }

                if(!colouredFieldInFieldsSequence) {
                    lastXIndex = columnIdx;
                    columnIdx--;

                    emptyFieldsRange = createRangeFromTwoIntegers(firstXIndex + 1, lastXIndex - 1);

                    emptyFieldsSequenceLength = rangeLength(emptyFieldsRange);


                    for(int seqNo = 0; seqNo < rowSequencesLengths.size(); seqNo++) {
                        rowSequenceRange = rowSequencesRanges.get(seqNo);
                        rowSequenceLength = rowSequencesLengths.get(seqNo);

                        if(rangeInsideAnotherRange(emptyFieldsRange, rowSequenceRange) || emptyFieldsSequenceLength > rowSequenceLength) {
                            sequencesWithinRange.add(seqNo);
                            if(emptyFieldsSequenceLength < rowSequenceLength) {
                                sequencesWhichNotFit.add(seqNo);
                            }
                        }
                    }

                    // if there's not any sequence with length equal or less than emptyFieldSequenceLength
                    if(sequencesWhichNotFit.size() == sequencesWithinRange.size() && emptyFieldsSequenceLength > 0) {
                        for(int emptyFieldColumnIdx = emptyFieldsRange.get(0); emptyFieldColumnIdx <= emptyFieldsRange.get(1); emptyFieldColumnIdx++) {
                            this.addRowFieldToNotToInclude(rowIdx, emptyFieldColumnIdx);
                            this.addColumnFieldToNotToInclude(emptyFieldColumnIdx, rowIdx);

                            if(this.getNonogramSolutionBoard().get(rowIdx).get(emptyFieldColumnIdx).equals("-")) {
                                this.placeXAtGivenPosition(rowIdx, emptyFieldColumnIdx);

                                addLog(this.getNonogramSolutionBoard().get(rowIdx).toString());
                                tmpLog = generatePlacingXStepDescription(rowIdx, emptyFieldColumnIdx, "placing \"X\" inside too short empty fields sequence");
                                addLog(tmpLog);

                                addColumnToAffectedActionsByIdentifiers(firstXIndex, ActionsConstants.actionsToDoAfterPlacingXsAtTooShortEmptySequencesInRows);

                                this.increaseStepsMade();
                            } else if(showRepetitions) {
                                System.out.println("X placed in too short row empty field sequence earlier!");
                            }
                        }
                    }
                }
            }
        }

    }

    /**
     * correct rows sequences ranges
     */
    public void correctSequencesRangesInRows() {

        for (Integer rowIndex : this.getAffectedRowsToCorrectSequencesRanges()) {
            correctSequencesRangesInRow(rowIndex);

            List<Integer> sequencesLengths = this.getRowsSequences().get(rowIndex);
            List<List<Integer>> sequencesRanges = this.getRowsSequencesRanges().get(rowIndex);

            if(!emptyRow(rowIndex)) {
                for(int seqNo = 0; seqNo < this.getRowsSequencesRanges().get(rowIndex).size(); seqNo++) {
                    if(rangeLength(sequencesRanges.get(seqNo)) < sequencesLengths.get(seqNo)) {
                        if(rowIndex == 27) {
                            System.out.println("HERE setting solution invalid!!! ranges: " + sequencesRanges);
                        }
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

    /**
     * correct row sequences ranges
     */
    public void correctSequencesRangesInRow (int rowIdx) {

        boolean rowSequenceRangesChanged = false;

        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowFieldsNotToInclude = this.getRowsFieldsNotToInclude().get(rowIdx);

        List<Integer> fullSequenceRange;
        List<Integer> currentSequenceRange;
        List<Integer> nextSequenceRange;
        int updatedNextSequenceBeginRangeColumnIndex;
        int updatedStartIndex;
        int updatedEndIndex;
        int oldNextSequenceBeginRangeColumnIndex;
        List<Integer> updatedNextSequenceRange;

        List<Integer> rowSequencesIdsNotToInclude = this.getRowsSequencesIdsNotToInclude().get(rowIdx);

        //from left - start
        for(int sequenceIdx = 0; sequenceIdx < rowSequencesRanges.size() - 1; sequenceIdx++) {

            // row sequences not to include should be marked and rangeLength==sequenceLength
            if(rowSequencesIdsNotToInclude.contains(sequenceIdx) && !rowSequencesIdsNotToInclude.contains(sequenceIdx + 1)) {
                fullSequenceRange = this.getRowsSequencesRanges().get(rowIdx).get(sequenceIdx);

                nextSequenceRange = this.getRowsSequencesRanges().get(rowIdx).get(sequenceIdx + 1);
                //XXXffffXu---- (f - full sequence that position and mark is known, u -> the minimum start index of next sequence)
                updatedNextSequenceBeginRangeColumnIndex = fullSequenceRange.get(1) + 2;

                while(rowFieldsNotToInclude.contains(updatedNextSequenceBeginRangeColumnIndex)) {
                    updatedNextSequenceBeginRangeColumnIndex++;
                }

                oldNextSequenceBeginRangeColumnIndex = nextSequenceRange.get(0);

                updatedNextSequenceRange = new ArrayList<>();

                updatedStartIndex = Math.max(oldNextSequenceBeginRangeColumnIndex, updatedNextSequenceBeginRangeColumnIndex);
                updatedNextSequenceRange.add(updatedStartIndex);
                updatedNextSequenceRange.add(nextSequenceRange.get(1));

                if( !Integer.valueOf(updatedStartIndex).equals(oldNextSequenceBeginRangeColumnIndex) ) {
                    this.getRowsSequencesRanges().get(rowIdx).set(sequenceIdx + 1, updatedNextSequenceRange);
                    rowSequenceRangesChanged = true;
                    this.increaseStepsMade();
                    tmpLog = generateUpdateRangesStepDescription(rowIdx, sequenceIdx + 1, nextSequenceRange, updatedNextSequenceRange,
                            "correcting from left");
                    addLog(tmpLog);

                    if(rangeLength(updatedNextSequenceRange) == rowSequencesLengths.get(sequenceIdx + 1)) {
                        this.addRowSequenceIdxToNotToInclude(rowIdx, sequenceIdx + 1);
                    }
                }
            }   else if(!rowSequencesIdsNotToInclude.contains(sequenceIdx) && !rowSequencesIdsNotToInclude.contains(sequenceIdx + 1)) {
                currentSequenceRange = rowSequencesRanges.get(sequenceIdx);

                nextSequenceRange = rowSequencesRanges.get(sequenceIdx + 1);
                //get the possible new minimum start index of next sequence checking where is the earliest index that current sequence can end (+1 for 'X' after sequence)
                updatedNextSequenceBeginRangeColumnIndex = currentSequenceRange.get(0) + rowSequencesLengths.get(sequenceIdx) + 1;

                oldNextSequenceBeginRangeColumnIndex = nextSequenceRange.get(0);

                updatedNextSequenceRange = new ArrayList<>();

                updatedStartIndex = Math.max(oldNextSequenceBeginRangeColumnIndex, updatedNextSequenceBeginRangeColumnIndex);
                updatedNextSequenceRange.add(updatedStartIndex);
                updatedNextSequenceRange.add(nextSequenceRange.get(1));

                if(updatedStartIndex != nextSequenceRange.get(0)) {
                    rowSequenceRangesChanged = true;
                    this.increaseStepsMade();
                    tmpLog = generateUpdateRangesStepDescription(rowIdx, sequenceIdx + 1, nextSequenceRange, updatedNextSequenceRange, "correcting from left");
                    addLog(tmpLog);
                    this.getRowsSequencesRanges().get(rowIdx).set(sequenceIdx + 1, updatedNextSequenceRange);

                    if(rangeLength(updatedNextSequenceRange) == rowSequencesLengths.get(sequenceIdx + 1)) {
                        this.addRowSequenceIdxToNotToInclude(rowIdx, sequenceIdx + 1);
                    }
                }
            }
        }

        List<Integer> oldPreviousSequenceRange;
        int previousSequenceBeginRangeColumnIndex;
        int previousSequenceEndRangeColumnIndex;
        int updatedPreviousSequenceEndRangeColumnIndex;
        List<Integer> updatedPreviousSequenceRange;

        int currentSequenceLength;

        //from right - end
        for(int sequenceIdx = rowSequencesRanges.size() - 1; sequenceIdx > 0; sequenceIdx--) {

            //currentSequenceRange is marked with row seqNo and fully coloured
            if(rowSequencesIdsNotToInclude.contains(sequenceIdx) && !rowSequencesIdsNotToInclude.contains(sequenceIdx - 1)) {

                fullSequenceRange = rowSequencesRanges.get(sequenceIdx);
                oldPreviousSequenceRange = rowSequencesRanges.get(sequenceIdx - 1);

                updatedPreviousSequenceEndRangeColumnIndex = fullSequenceRange.get(0) - 2;

                while(rowFieldsNotToInclude.contains(updatedPreviousSequenceEndRangeColumnIndex)) {
                    updatedPreviousSequenceEndRangeColumnIndex--;
                }

                previousSequenceBeginRangeColumnIndex = oldPreviousSequenceRange.get(0);
                previousSequenceEndRangeColumnIndex = oldPreviousSequenceRange.get(1);

                updatedPreviousSequenceRange = new ArrayList<>();

                updatedPreviousSequenceRange.add(previousSequenceBeginRangeColumnIndex);
                updatedEndIndex = Math.min(previousSequenceEndRangeColumnIndex, updatedPreviousSequenceEndRangeColumnIndex);
                updatedPreviousSequenceRange.add(updatedEndIndex);

                if(updatedEndIndex != previousSequenceEndRangeColumnIndex) {
                    this.getRowsSequencesRanges().get(rowIdx).set(sequenceIdx - 1, updatedPreviousSequenceRange);
                    rowSequenceRangesChanged = true;
                    this.increaseStepsMade();
                    tmpLog = generateUpdateRangesStepDescription(rowIdx, sequenceIdx - 1,
                            oldPreviousSequenceRange, updatedPreviousSequenceRange, "correcting from right");
                    addLog(tmpLog);

                    if(rangeLength(updatedPreviousSequenceRange) ==  rowSequencesLengths.get(sequenceIdx - 1)) {
                        this.addRowSequenceIdxToNotToInclude(rowIdx, sequenceIdx - 1);
                    }
                }
            } else if(!rowSequencesIdsNotToInclude.contains(sequenceIdx) && !rowSequencesIdsNotToInclude.contains(sequenceIdx - 1)) {
                currentSequenceLength = rowSequencesLengths.get(sequenceIdx);
                currentSequenceRange = rowSequencesRanges.get(sequenceIdx);
                oldPreviousSequenceRange = rowSequencesRanges.get(sequenceIdx - 1);

                updatedPreviousSequenceEndRangeColumnIndex = currentSequenceRange.get(1) - currentSequenceLength - 1;

                previousSequenceBeginRangeColumnIndex = oldPreviousSequenceRange.get(0);
                previousSequenceEndRangeColumnIndex = oldPreviousSequenceRange.get(1);

                updatedPreviousSequenceRange = new ArrayList<>();

                updatedPreviousSequenceRange.add(previousSequenceBeginRangeColumnIndex);
                updatedEndIndex = Math.min(previousSequenceEndRangeColumnIndex, updatedPreviousSequenceEndRangeColumnIndex);
                updatedPreviousSequenceRange.add(updatedEndIndex);

                if(updatedEndIndex != previousSequenceEndRangeColumnIndex) {
                    this.getRowsSequencesRanges().get(rowIdx).set(sequenceIdx - 1, updatedPreviousSequenceRange);
                    rowSequenceRangesChanged = true;
                }
            }

        }

        if(rowSequenceRangesChanged) {
            this.increaseStepsMade();
            addRowToAffectedActionsByIdentifiers(rowIdx, ActionsConstants.actionsToDoAfterCorrectingRowsSequences);
        }

    }

    /**
     * corrects rows sequences when met coloured field in row
     * (f.e. first sequence FS can't start later than first met coloured field FMCF,
     * second - not later than first coloured field on index FMCF + FS.length, etc.)
     */
    // iterations through all rows
    public void correctRowsSequencesRangesWhenMetColouredFields() {
        for (Integer rowIndex : this.getAffectedRowsToCorrectSequencesRangesWhenMetColouredField()) {
            correctRowSequencesRangesWhenMetColouredField(rowIndex);

            List<Integer> sequencesLengths = this.getRowsSequences().get(rowIndex);
            List<List<Integer>> sequencesRanges = this.getRowsSequencesRanges().get(rowIndex);
            for(int seqNo = 0; seqNo < this.getRowsSequencesRanges().get(rowIndex).size(); seqNo++) {
                if(rangeLength(sequencesRanges.get(seqNo)) < sequencesLengths.get(seqNo)) {
                    this.setSolutionInvalid(true);
                    break;
                }
            }
            if(solutionInvalid) {
                break;
            }
        }
    }


    /**
     * corrects row sequences when met coloured field in row (also from left and right)
     * @param rowIdx - row index to correct sequences ranges when coloured field met
     */
    public void correctRowSequencesRangesWhenMetColouredField (int rowIdx) {
        correctRowSequencesRangesWhenMetColouredFieldFromLeft(rowIdx);
        correctRowSequencesRangesWhenMetColouredFieldFromRight(rowIdx);
    }

    /**
     * corrects row sequences when met coloured field in row (from left)
     * @param rowIdx - row index to correct sequences ranges when coloured field met (from left)
     */
    public void correctRowSequencesRangesWhenMetColouredFieldFromLeft (int rowIdx) {
        boolean rowSequenceRangesChanged = false;

        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);
        List<Integer> rowSequenceRange;
        int rowSequenceRangeStart;
        int rowSequenceRangeEnd;
        int maximumPossibleSequenceRangeEnd;
        List<Integer> updatedRange;
        int updatedRangeEnd;
        List<String> solutionBoardRow = this.getNonogramSolutionBoard().get(rowIdx);
        int nonogramWidth = this.getWidth();
        int sequenceId = 0;
        int sequenceLength = rowSequencesLengths.get(0);

        for(int columnIdx = 0; columnIdx < nonogramWidth; columnIdx++) {

            //look for first coloured field from left to right
            if(solutionBoardRow.get(columnIdx).equals("O")) {
                rowSequenceRange = this.getRowsSequencesRanges().get(rowIdx).get(sequenceId);
                rowSequenceRangeStart = rowSequenceRange.get(0);
                rowSequenceRangeEnd = rowSequenceRange.get(1);

                // new maximum end index cannot be higher than: (first coloured field index + sequence length - 1), 1 stands for already coloured field
                maximumPossibleSequenceRangeEnd = columnIdx + sequenceLength - 1;

                updatedRange = new ArrayList<>();
                // start column index doesn't change
                updatedRange.add(rowSequenceRangeStart);

                // new end index is minimum of two values - old end index and new calculated when met coloured field
                // (sequence can be far away from start, so we can check if coloured field is close enough to set new minimum end range index
                updatedRangeEnd = Math.min(rowSequenceRangeEnd, maximumPossibleSequenceRangeEnd);
                updatedRange.add(updatedRangeEnd);

                if(updatedRangeEnd != rowSequenceRangeEnd) {
                    rowSequenceRangesChanged = true;
                    tmpLog = generateUpdateRangesStepDescription(rowIdx, sequenceId, rowSequenceRange, updatedRange, "met coloured field from left side");
                    addLog(tmpLog);
                    this.getRowsSequencesRanges().get(rowIdx).set(sequenceId, updatedRange);
                    if(rangeLength(updatedRange) == rowSequencesLengths.get(sequenceId)) {
                        this.addRowSequenceIdxToNotToInclude(rowIdx, sequenceId);
                    }
                }

                columnIdx = columnIdx + sequenceLength;
                sequenceId++;
                // check if met enough coloured fields in certain intervals (not go to sequenceId > rowSequences.size())
                if(sequenceId < rowSequencesLengths.size()) {
                    sequenceLength = rowSequencesLengths.get(sequenceId);
                } else {
                    break;
                }
            }
        }

        if(rowSequenceRangesChanged) {
            addRowToAffectedActionsByIdentifiers(rowIdx, ActionsConstants.actionsToDoAfterCorrectingRowsSequencesWhenMetColouredField);
        }
    }

    /**
     * corrects row sequences when met coloured field in row (from right)
     * @param rowIdx - row index to correct sequences ranges when coloured field met (from right)
     */
    public void correctRowSequencesRangesWhenMetColouredFieldFromRight (int rowIdx) {
        boolean rowSequenceRangesChanged = false;

        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);
        List<Integer> rowSequenceRange;
        int rowSequenceRangeStart;
        int rowSequenceRangeEnd;
        int updatedRangeStart;
        int minimumPossibleSequenceRangeStart;
        List<Integer> updatedRange;
        List<String> solutionBoardRow = this.getNonogramSolutionBoard().get(rowIdx);
        int nonogramWidth = this.getWidth();
        int sequenceId = rowSequencesLengths.size() - 1;
        int sequenceLength = rowSequencesLengths.get(sequenceId);

        for(int columnIdx = nonogramWidth - 1; columnIdx >= 0; columnIdx--) {
            if(solutionBoardRow.get(columnIdx).equals("O")) {
                minimumPossibleSequenceRangeStart = columnIdx - sequenceLength + 1;
                rowSequenceRange = rowSequencesRanges.get(sequenceId);
                rowSequenceRangeStart = rowSequenceRange.get(0);
                rowSequenceRangeEnd = rowSequenceRange.get(1);

                updatedRange = new ArrayList<>();
                updatedRangeStart = Math.max(minimumPossibleSequenceRangeStart, rowSequenceRangeStart);
                updatedRange.add(updatedRangeStart);

                updatedRange.add(rowSequenceRangeEnd);

                if(updatedRangeStart != rowSequenceRangeStart) {
                    rowSequenceRangesChanged = true;
                    tmpLog = generateUpdateRangesStepDescription(rowIdx, sequenceId, rowSequenceRange, updatedRange, "met coloured field from right side");
                    addLog(tmpLog);
                    this.getRowsSequencesRanges().get(rowIdx).set(sequenceId, updatedRange);
                    if(rangeLength(updatedRange) == rowSequencesLengths.get(sequenceId)) {
                        this.addRowSequenceIdxToNotToInclude(rowIdx, sequenceId);
                    }
                }

                //go back - make step of length of current sequence, columnIdx-- will work as placing imaginary "X" before coloured sequence
                columnIdx = columnIdx - sequenceLength;
                sequenceId--;
                if(sequenceId > -1) {
                    sequenceLength = rowSequencesLengths.get(sequenceId);
                } else {
                    break;
                }
            }
        }

        if(rowSequenceRangesChanged) {
            addRowToAffectedActionsByIdentifiers(rowIdx, ActionsConstants.actionsToDoAfterCorrectingRowsSequencesWhenMetColouredField);
        }
    }



    /**
     * Corrects row sequence(s) range(s) "if X on way" -> if "X" is located too close to the potential starting
     * point counting from the beginning or end of the potential range, then correct the potential range
     */
    public void correctRowsSequencesRangesIndexesIfXOnWay() {
        for(Integer rowIndex : this.getAffectedRowsToChangeSequencesRangeIfXOnWay()) {
            correctRowRangeIndexesIfXOnWay(rowIndex);

            List<Integer> sequencesLengths = this.getRowsSequences().get(rowIndex);
            List<List<Integer>> sequencesRanges = this.getRowsSequencesRanges().get(rowIndex);
            for(int seqNo = 0; seqNo < this.getRowsSequencesRanges().get(rowIndex).size(); seqNo++) {
                if(rangeLength(sequencesRanges.get(seqNo)) < sequencesLengths.get(seqNo)) {
                    this.setSolutionInvalid(true);
                    break;
                }
            }
            if(solutionInvalid) {
                break;
            }
        }
    }

    /**
     * @param rowIdx - column to correct sequence/s range/s if x on way
     */
    public void correctRowRangeIndexesIfXOnWay (int rowIdx) {

        boolean rowSequenceRangesChanged = false;

        List<Integer> rowSequences = this.getRowsSequences().get(rowIdx);
        List<List<Integer>> nonogramRowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> nonogramRowSequencesIdsNotToInclude = this.getRowsSequencesIdsNotToInclude().get(rowIdx);
        List<List<String>> nonogramBoard = this.getNonogramSolutionBoard();
        int oldRowSequenceRangeStartIndex;
        int oldRowSequenceRangeEndIndex;
        int rowSequenceLength;
        List<Integer> rowSequenceRange;

        boolean indexOk;
        int updatedRowRangeStartIndex;
        int updatedRowRangeEndIndex;
        List<Integer> updatedRange;

        for(int seqNo = 0; seqNo < nonogramRowSequencesRanges.size(); seqNo++) {
            if(!nonogramRowSequencesIdsNotToInclude.contains(seqNo)) {

                rowSequenceRange = nonogramRowSequencesRanges.get(seqNo);
                rowSequenceLength = rowSequences.get(seqNo);
                oldRowSequenceRangeStartIndex = rowSequenceRange.get(0);
                oldRowSequenceRangeEndIndex = rowSequenceRange.get(1);

                updatedRowRangeStartIndex = oldRowSequenceRangeStartIndex;

                for(int columnStartIndex = oldRowSequenceRangeStartIndex; columnStartIndex <= (oldRowSequenceRangeEndIndex - rowSequenceLength + 1); columnStartIndex++) {
                    indexOk = true;
                    for(int columnIdx = columnStartIndex; columnIdx < columnStartIndex + rowSequenceLength; columnIdx++) {
                        if(nonogramBoard.get(rowIdx).get(columnIdx).equals("X")) {
                            indexOk = false;
                            break;
                        }
                    }
                    if(indexOk) {
                        break;
                    } else {
                        updatedRowRangeStartIndex++;
                    }
                }

                updatedRowRangeEndIndex = oldRowSequenceRangeEndIndex;

                for(int columnEndIndex = oldRowSequenceRangeEndIndex; columnEndIndex > (oldRowSequenceRangeStartIndex + rowSequenceLength - 1); columnEndIndex--) {
                    indexOk = true;
                    for(int columnIdx = columnEndIndex; columnIdx > columnEndIndex - rowSequenceLength; columnIdx--) {
                        if(nonogramBoard.get(rowIdx).get(columnIdx).equals("X")) {
                            indexOk = false;
                            break;
                        }
                    }
                    if(indexOk) {
                        break;
                    } else {
                        updatedRowRangeEndIndex--;
                    }
                }

                if((updatedRowRangeStartIndex != oldRowSequenceRangeStartIndex || updatedRowRangeEndIndex != oldRowSequenceRangeEndIndex)) {
                    rowSequenceRangesChanged = true;
                    updatedRange = new ArrayList<>();
                    updatedRange.add(updatedRowRangeStartIndex);
                    updatedRange.add(updatedRowRangeEndIndex);
                    tmpLog = generateUpdateRangesStepDescription(rowIdx, seqNo, rowSequenceRange, updatedRange, "\"X\" on way");
                    addLog(tmpLog);
                    this.updateRowSequenceRange(rowIdx, seqNo, updatedRange);
                    if(rangeLength(updatedRange) == rowSequenceLength) {
                        this.addRowSequenceIdxToNotToInclude(rowIdx, seqNo);
                    }
                }
            }
        }

        if(rowSequenceRangesChanged) {
            this.increaseStepsMade();
            addRowToAffectedActionsByIdentifiers(rowIdx, ActionsConstants.actionsToDoAfterCorrectingRowsSequencesIfXOnWay);
        }

    }

    /**
     * place an "X" on fields outside the rows sequence ranges
     */
    public void placeXsRowsAtUnreachableFields() {

        for (Integer rowIndex : this.getAffectedRowsToPlaceXsAtUnreachableFields()) {
            placeXsRowAtUnreachableFields(rowIndex);
        }
    }

    /**
     * place an "X" on fields outside the row sequence ranges
     */
    public void placeXsRowAtUnreachableFields(int rowIdx) {

        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        int nonogramWidth = this.getWidth();
        boolean existRangeIncludingColumn;
        List<Integer> fieldAsRange;

        for(int columnIdx = 0; columnIdx < nonogramWidth; columnIdx++) {
            fieldAsRange = new ArrayList<>();
            fieldAsRange.add(columnIdx);
            fieldAsRange.add(columnIdx);
            existRangeIncludingColumn = rangesListIncludingAnotherRange(rowSequencesRanges ,fieldAsRange);

            if(!existRangeIncludingColumn) {
                if(this.getNonogramSolutionBoard().get(rowIdx).get(columnIdx).equals("-")) {
                    this.placeXAtGivenPosition(rowIdx, columnIdx)
                            .addRowFieldToNotToInclude(rowIdx, columnIdx)
                            .addColumnFieldToNotToInclude(columnIdx, rowIdx);

                    tmpLog = generatePlacingXStepDescription(rowIdx, columnIdx, "placing \"X\" at unreachable field");
                    this.logs.add(tmpLog);
                    addColumnToAffectedActionsByIdentifiers(columnIdx, ActionsConstants.actionsToDoAfterPlacingXsAtRowsUnreachableFields);

                    this.increaseStepsMade();
                } else if(showRepetitions) {
                    System.out.println("X at unreachable field in row placed earlier!");
                }
            }
        }

    }

    /**
     * extends coloured subsequences in row to maximum possible length, looking for minimum possible length sequence
     * that can be placed in filled fields range
     */
    public void extendColouredFieldsNearXToMaximumPossibleLengthInRows() {

        for (Integer rowIndex : this.getAffectedRowsToExtendColouredFieldsNearX()) {
            extendColouredFieldsToLeftNearXToMaximumPossibleLengthInRow(rowIndex);
            extendColouredFieldsToRightNearXToMaximumPossibleLengthInRow(rowIndex);
        }

    }

    /**
     * @param rowIdx - row index on which try to extend subsequence to minimum possible matching (to left)
     */
    public void extendColouredFieldsToLeftNearXToMaximumPossibleLengthInRow(int rowIdx) {

        List<String> nonogramSolutionBoardRow = this.getNonogramSolutionBoard().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);

        List<Integer> possibleSequenceLengths;
        int minimumPossibleLength;
        List<Integer> firstColouredSubsequence;
        int sequenceStartIndex;
        int distanceToX;

        int nonogramWidth = this.getWidth();

        int firstColouredFieldIndexInSubsequence;

        for(int columnIdx = 0; columnIdx < nonogramWidth; columnIdx++) {

            if(nonogramSolutionBoardRow.get(columnIdx).equals("O")) {
                firstColouredSubsequence = new ArrayList<>();

                firstColouredFieldIndexInSubsequence = columnIdx;
                while(firstColouredFieldIndexInSubsequence >= 0 &&
                        nonogramSolutionBoardRow.get(firstColouredFieldIndexInSubsequence).equals("O")) {
                    firstColouredFieldIndexInSubsequence--;
                }
                firstColouredFieldIndexInSubsequence++;

                // create range from coloured subsequence
                firstColouredSubsequence.add(firstColouredFieldIndexInSubsequence);
                firstColouredSubsequence.add(columnIdx);

                possibleSequenceLengths = filterSequencesRangesIncludingAnotherAndReturnCorrespondingLengths (
                        rowSequencesRanges, firstColouredSubsequence, rowSequencesLengths);

                if(possibleSequenceLengths.size() == 0) {
                    this.setSolutionInvalid(true);
                    break;
                }

                minimumPossibleLength = Collections.min(possibleSequenceLengths);

                sequenceStartIndex = columnIdx;

                distanceToX = 0;

                for(int columnsToX = 1; columnsToX < minimumPossibleLength && columnIdx + columnsToX < nonogramWidth; columnsToX++) {
                    if(nonogramSolutionBoardRow.get(columnIdx + columnsToX).equals("X")) {
                        distanceToX = columnsToX;
                        break;
                    }
                }

                int extendedSequenceEndIndex = sequenceStartIndex + distanceToX - minimumPossibleLength;

                if(distanceToX != 0) {
                    for(int colourColumnIdx = sequenceStartIndex;
                        colourColumnIdx >= 0 /*tmp cond*/
                                && colourColumnIdx >= extendedSequenceEndIndex;
                        colourColumnIdx--) {
                        if(this.getNonogramSolutionBoard().get(rowIdx).get(colourColumnIdx).equals("-")) {

                            this.increaseStepsMade();
                            this.colourFieldAtGivenPosition(rowIdx, colourColumnIdx);
                            addColumnToAffectedActionsByIdentifiers(colourColumnIdx, ActionsConstants.actionsToDoAfterExtendingColouredFieldsNearXInRows);
                            addLog("possible sequences size: " + possibleSequenceLengths.size());
                            addLog(this.getRowsSequencesRanges().get(rowIdx).toString());
                            addLog(this.getNonogramSolutionBoard().get(rowIdx).toString());
                            tmpLog = generateColourStepDescription(rowIdx, colourColumnIdx, "extend coloured fields in sequence to left near X " +
                                    "to length of shortest possible sequence in row");
                            addLog(tmpLog);

                        } else if(showRepetitions) {
                            System.out.println("Row field was coloured earlier (extending to minimum required - to left).");
                        }
                    }
                }
            }

        }
    }

    /**
     * @param rowIdx - row index on which try to extend subsequence to minimum possible matching (to right)
     */
    public void extendColouredFieldsToRightNearXToMaximumPossibleLengthInRow(int rowIdx) {

        List<String> nonogramSolutionBoardRow = this.getNonogramSolutionBoard().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);

        List<Integer> possibleSequenceLengths;
        int minimumPossibleLength;
        List<Integer> lastColouredSubsequence;
        int sequenceStartIndex;
        int distanceFromX;

        int nonogramWidth = this.getWidth();

        int lastColouredFieldInRange;

        for(int columnIdx = nonogramWidth - 1; columnIdx >= 0; columnIdx--) {

            if(nonogramSolutionBoardRow.get(columnIdx).equals("O")) {
                lastColouredSubsequence = new ArrayList<>();

                lastColouredFieldInRange = columnIdx;
                while(lastColouredFieldInRange < nonogramWidth &&
                        nonogramSolutionBoardRow.get(lastColouredFieldInRange).equals("O")) {
                    lastColouredFieldInRange++;
                }
                lastColouredFieldInRange--;

                // create range from coloured subsequence
                lastColouredSubsequence.add(columnIdx);
                lastColouredSubsequence.add(lastColouredFieldInRange);

                possibleSequenceLengths = filterSequencesRangesIncludingAnotherAndReturnCorrespondingLengths (
                        rowSequencesRanges, lastColouredSubsequence, rowSequencesLengths);

                if(possibleSequenceLengths.size() == 0) {
                    this.setSolutionInvalid(true);
                    break;
                }

                minimumPossibleLength = Collections.min(possibleSequenceLengths);

                sequenceStartIndex = columnIdx;

                distanceFromX = 0;

                for(int columnsFromX = 1; columnsFromX < minimumPossibleLength && columnIdx - columnsFromX >= 0; columnsFromX++) {
                    if(nonogramSolutionBoardRow.get(columnIdx - columnsFromX).equals("X")) {
                        distanceFromX = columnsFromX;
                        break;
                    }
                }

                if(distanceFromX != 0) {
                    for(int colourColumnIdx = sequenceStartIndex;
                        colourColumnIdx < this.getWidth() /*tmp cond*/ && colourColumnIdx <= sequenceStartIndex - distanceFromX + minimumPossibleLength;
                        colourColumnIdx++) {
                        if(this.getNonogramSolutionBoard().get(rowIdx).get(colourColumnIdx).equals("-")) {

                            this.increaseStepsMade();
                            this.colourFieldAtGivenPosition(rowIdx, colourColumnIdx);
                            tmpLog = generateColourStepDescription(rowIdx, colourColumnIdx, "extend coloured fields in sequence to right near X " +
                                    "to length of shortest possible sequence in row");
                            addLog(tmpLog);
                            addColumnToAffectedActionsByIdentifiers(colourColumnIdx, ActionsConstants.actionsToDoAfterExtendingColouredFieldsNearXInRows);
                        } else if (showRepetitions) {
                            System.out.println("Row field was coloured earlier (extending to minimum required - to right).");
                        }
                    }
                }
            }

        }
    }

    /**
     * @param rowIdx - row index to exclude sequence (fully coloured and marked sequence, sequence range == sequence length)
     * @param seqIdx - sequence index to exclude in specified row
     * @return NonogramLogic object with sequence index in specified row excluded
     */
    public NonogramRowLogic addRowSequenceIdxToNotToInclude(int rowIdx, int seqIdx) {
        boolean rowValid = isRowIndexValid(rowIdx);
        if(rowValid && !this.rowsSequencesIdsNotToInclude.get(rowIdx).contains(seqIdx)) {
            this.rowsSequencesIdsNotToInclude.get(rowIdx).add(seqIdx);
            Collections.sort(this.rowsSequencesIdsNotToInclude.get(rowIdx));
        }

        return this;
    }

    /**
     * @param rowIdx - field row index
     * @param columnIdx - field column index
     * @return NonogramLogic object with "X" placed on specified position
     */
    public NonogramRowLogic placeXAtGivenPosition(int rowIdx, int columnIdx) {
        boolean rowValid = isRowIndexValid(rowIdx);
        boolean columnValid = isColumnIndexValid(columnIdx);
        if(rowValid && columnValid) {
            this.nonogramSolutionBoard.get(rowIdx).set(columnIdx, "X");
            this.nonogramSolutionBoardWithMarks.get(rowIdx).set(columnIdx, "X".repeat(4));
        }

        return this;
    }

    /**
     * @param rowIdx - field row index
     * @param columnIdx - field column index
     */
    public void colourFieldAtGivenPosition(int rowIdx, int columnIdx) {
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
    public NonogramRowLogic addRowFieldToNotToInclude(int rowIdx, int columnIdx) {
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
     * @param rowIdx - row to check if is empty
     * @return true if row is empty ([0]), false if not
     */
    private boolean emptyRow(int rowIdx) {
        if(rowIdx < this.getHeight()) {
            if(this.getRowsSequences().get(rowIdx).size() == 1) {
                return this.getRowsSequences().get(rowIdx).get(0) == 0;
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

    public NonogramRowLogic increaseStepsMade() {
        this.newStepsMade = this.newStepsMade + 1;
        return this;
    }

    public int minimumColumnIndexWithoutX(int rowIdx, int lastSequenceColumnIdx, int sequenceFullLength) {
        List<String> boardRow = this.getNonogramSolutionBoard().get(rowIdx);
        int minimumColumnIndex = lastSequenceColumnIdx;
        int minimumColumnIndexLimit = Math.max(lastSequenceColumnIdx - sequenceFullLength + 1, 0);
        String fieldMark;

        for(; minimumColumnIndex >= minimumColumnIndexLimit; minimumColumnIndex--) {
            fieldMark = boardRow.get(minimumColumnIndex);
            if(fieldMark.equals("X")) {
                break;
            }
        }

        return minimumColumnIndex + 1;
    }

    public int maximumColumnIndexWithoutX(int rowIdx, int firstSequenceColumnIdx, int sequenceFullLength) {
        List<String> boardRow = this.getNonogramSolutionBoard().get(rowIdx);
        int maximumColumnIndex = firstSequenceColumnIdx;
        int maximumColumnIndexLimit = Math.min(firstSequenceColumnIdx + sequenceFullLength - 1, this.getWidth() - 1);
        String fieldMark;

        for(; maximumColumnIndex <= maximumColumnIndexLimit; maximumColumnIndex++) {
            fieldMark = boardRow.get(maximumColumnIndex);
            if(fieldMark.equals("X")) {
                break;
            }
        }

        return maximumColumnIndex - 1;
    }

    /**
     * @param rowIdx - row index to update one of sequences range
     * @param sequenceIdx - row sequence index to update sequence range
     * @param updatedRange - updated row sequence range
     */
    public void updateRowSequenceRange(int rowIdx, int sequenceIdx, List<Integer> updatedRange) {
        this.rowsSequencesRanges.get(rowIdx).set(sequenceIdx, updatedRange);
    }

    /**
     * @param rowIdx - row to mark sequence with its identifier (1st sequence -> "b", 2nd sequence -> "c", etc)
     * @param colIdx - column of field to mark with row sequence identifier
     * @param marker - row sequence marker/identifier
     */
    public void markRowBoardField(int rowIdx, int colIdx, String marker) {
        String oldRowField = this.nonogramSolutionBoardWithMarks.get(rowIdx).get(colIdx);
        this.nonogramSolutionBoardWithMarks.get(rowIdx).set(colIdx, "R" + marker + oldRowField.substring(2, 4));

    }

    /**
     * @param columnIdx - column index to exclude field ("X" or part of fully coloured and marked sequence)
     * @param rowIdx - field row index to exclude in given column
     * @return NonogramLogic object with field in given column excluded
     */
    public NonogramRowLogic addColumnFieldToNotToInclude(int columnIdx, int rowIdx) {
        boolean rowValid = isRowIndexValid(rowIdx);
        boolean columnValid = isColumnIndexValid(columnIdx);
        if(rowValid && columnValid && !this.columnsFieldsNotToInclude.get(columnIdx).contains(rowIdx) && rowIdx < this.getHeight() && rowIdx >= 0) {
            this.columnsFieldsNotToInclude.get(columnIdx).add(rowIdx);
            Collections.sort(this.columnsFieldsNotToInclude.get(columnIdx));
        }

        return this;
    }

    public int getHeight() {
        return this.getRowsSequences().size();
    }

    public int getWidth() {
        return this.getColumnsSequences().size();
    }

    public void addLog(String log) {
        if (log.length() == 0) {
            System.out.println("Trying to add empty log!!!");
        } else {
            this.logs.add(log);
        }
    }

    public String generateColourStepDescription(int rowIndex, int columnIndex, String actionType) {
        return String.format("Row field colouring - rowIndex: %d, columnIndex: %d - %s.", rowIndex, columnIndex, actionType);
    }

    public String generatePlacingXStepDescription(int rowIndex, int columnIndex, String actionType) {
        return String.format("Row field X placing - rowIndex: %d, columnIndex: %d - %s.", rowIndex, columnIndex, actionType);
    }

    public String generateUpdateRangesStepDescription(int rowIndex, int sequenceIndex, List<Integer> oldRange, List<Integer> updatedRange, String actionType) {
        return String.format("Row sequence range updating - rowIndex: %d, sequenceIndex %d, from [%d, %d] to [%d, %d] - %s", rowIndex, sequenceIndex,
                oldRange.get(0), oldRange.get(1), updatedRange.get(0), updatedRange.get(1), actionType);
    }

    private void addRowToAffectedActionsByIdentifiers(int rowIdx, List<Integer> affectedActionIds) {
        for(int affectedActionId : affectedActionIds) {
            switch (affectedActionId) {
                case ActionsConstants.CORRECT_ROWS_SEQUENCES_RANGES:
                    this.getAffectedRowsToCorrectSequencesRanges().add(rowIdx);
                    break;
                case ActionsConstants.CORRECT_ROWS_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS:
                    this.getAffectedRowsToCorrectSequencesRangesWhenMetColouredField().add(rowIdx);
                    break;
                case ActionsConstants.CORRECT_ROWS_SEQUENCES_RANGES_IF_X_ON_WAY:
                    this.getAffectedRowsToChangeSequencesRangeIfXOnWay().add(rowIdx);
                    break;
                case ActionsConstants.COLOUR_OVERLAPPING_FIELDS_IN_ROWS:
                    this.getAffectedRowsToFillOverlappingFields().add(rowIdx);
                    break;
                case ActionsConstants.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROWS:
                    this.getAffectedRowsToExtendColouredFieldsNearX().add(rowIdx);
                    break;
                case ActionsConstants.PLACE_XS_ROWS_AT_UNREACHABLE_FIELDS:
                    this.getAffectedRowsToPlaceXsAtUnreachableFields().add(rowIdx);
                    break;
                case ActionsConstants.PLACE_XS_ROWS_AROUND_LONGEST_SEQUENCES:
                    this.getAffectedRowsToPlaceXsAroundLongestSequences().add(rowIdx);
                    break;
                case ActionsConstants.PLACE_XS_ROWS_AT_TOO_SHORT_EMPTY_SEQUENCES:
                    this.getAffectedRowsToPlaceXsAtTooShortEmptySequences().add(rowIdx);
                    break;
                case ActionsConstants.MARK_AVAILABLE_FIELDS_IN_ROWS:
                    this.getAffectedRowsToMarkAvailableSequences().add(rowIdx);
                    break;
                default:
                    break;
            }
        }
    }

    private void addColumnToAffectedActionsByIdentifiers(int columnIdx, List<Integer> affectedActionIds) {
        for(int affectedActionId : affectedActionIds) {
            switch (affectedActionId) {
                case ActionsConstants.CORRECT_COLUMNS_SEQUENCES_RANGES:
                    this.getAffectedColumnsToCorrectSequencesRanges().add(columnIdx);
                    break;
                case ActionsConstants.CORRECT_COLUMNS_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS:
                    this.getAffectedColumnsToCorrectSequencesRangesWhenMetColouredField().add(columnIdx);
                    break;
                case ActionsConstants.CORRECT_COLUMNS_SEQUENCES_RANGES_IF_X_ON_WAY:
                    this.getAffectedColumnsToCorrectSequencesRangesIfXOnWay().add(columnIdx);
                    break;
                case ActionsConstants.COLOUR_OVERLAPPING_FIELDS_IN_COLUMNS:
                    this.getAffectedColumnsToFillOverlappingFields().add(columnIdx);
                    break;
                case ActionsConstants.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMNS:
                    this.getAffectedColumnsToExtendColouredFieldsNearX().add(columnIdx);
                    break;
                case ActionsConstants.PLACE_XS_COLUMNS_AT_UNREACHABLE_FIELDS:
                    this.getAffectedColumnsToPlaceXsAtUnreachableFields().add(columnIdx);
                    break;
                case ActionsConstants.PLACE_XS_COLUMNS_AROUND_LONGEST_SEQUENCES:
                    this.getAffectedColumnsToPlaceXsAroundLongestSequences().add(columnIdx);
                    break;
                case ActionsConstants.PLACE_XS_COLUMNS_AT_TOO_SHORT_EMPTY_SEQUENCES:
                    this.getAffectedColumnsToPlaceXsAtTooShortEmptySequences().add(columnIdx);
                    break;
                case ActionsConstants.MARK_AVAILABLE_FIELDS_IN_COLUMNS:
                    this.getAffectedColumnsToMarkAvailableSequences().add(columnIdx);
                    break;
                default:
                    break;
            }
        }
    }
}
