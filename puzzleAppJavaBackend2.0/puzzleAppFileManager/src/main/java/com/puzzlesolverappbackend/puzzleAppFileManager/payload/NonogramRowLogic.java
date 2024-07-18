package com.puzzlesolverappbackend.puzzleAppFileManager.payload;

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
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzleAppFileManager.payload.ActionsConstants.actionsToDoAfterCorrectingRangesWhenMarkingSequencesInRows;
import static com.puzzlesolverappbackend.puzzleAppFileManager.services.NonogramLogicService.*;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Slf4j
public class NonogramRowLogic {

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
        String sequenceMarker;

        List<Integer> oldOnlyMatchingSequenceRange;
        List<Integer> newSequenceRange;

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
                lastSequenceIndex = colouredSequenceIndexes.get(1);
                colouredSequenceLength = rangeLength(colouredSequenceIndexes);

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

                    //mark sequence part
                    sequenceMarker = NonogramLogic.indexToSequenceCharMark(lastMatchingSequenceIndex);
                    for(int sequenceColumnIdx = firstSequenceIndex; sequenceColumnIdx <= lastSequenceIndex; sequenceColumnIdx++) {
                        if(this.getNonogramSolutionBoardWithMarks().get(rowIdx).get(sequenceColumnIdx).startsWith("--")) {
                            this.markRowBoardField(rowIdx, sequenceColumnIdx, sequenceMarker);
                            this.nonogramState.increaseMadeSteps();
                        } else if(showRepetitions) {
                            System.out.println("Row field was marked earlier.");
                        }
                    }

                    //correct sequence range if new range is shorter
                    oldOnlyMatchingSequenceRange = rowSequencesRanges.get(lastMatchingSequenceIndex);
                    newSequenceRange = calculateNewRangeFromParameters(colouredSequenceIndexes, rowSequences.get(lastMatchingSequenceIndex));
                    if(rangeLength(newSequenceRange) < rangeLength(oldOnlyMatchingSequenceRange)) {
                        this.changeRowSequenceRange(rowIdx, lastMatchingSequenceIndex, newSequenceRange);
                        tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, lastMatchingSequenceIndex, oldOnlyMatchingSequenceRange, newSequenceRange, "correcting row sequence range when marking field");
                        addLog(tmpLog);
                        addRowToAffectedActionsByIdentifiers(rowIdx, actionsToDoAfterCorrectingRangesWhenMarkingSequencesInRows);
                    }
                }
            }
        }
    }

    private List<Integer> calculateNewRangeFromParameters(List<Integer> colouredSequenceIndexes, int sequenceLength) {
        int newRangeBegin = Math.max(0, colouredSequenceIndexes.get(1) - sequenceLength + 1);
        int newRangeEnd = Math.min(colouredSequenceIndexes.get(0) + sequenceLength - 1, this.getWidth() - 1);
        return List.of(newRangeBegin, newRangeEnd);
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
        List<Integer> range;

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
            range = sequencesInRowRanges.get(sequenceIdx);
            rangeBeginIndex = range.get(0);
            rangeEndIndex = range.get(1);

            // the range in which we can color the fields from sequence <cBCI, cECI>
            colourBeginColumnIndex = rangeEndIndex - sequenceLength + 1;
            colourEndColumnIndex = rangeBeginIndex + sequenceLength - 1;

            if(colourBeginColumnIndex <= colourEndColumnIndex) {

                sequenceCharMark = NonogramLogic.indexToSequenceCharMark(sequenceIdx);

                for (int columnIdx = colourBeginColumnIndex; columnIdx <= colourEndColumnIndex; columnIdx++) {
                    rowToChangeSolutionBoardWithMarks = this.getNonogramSolutionBoardWithMarks().get(rowIdx);
                    elementToChangeInsideRowBoardWithMarks = rowToChangeSolutionBoardWithMarks.get(columnIdx);

                    if(rowToChangeSolutionBoardWithMarks.get(columnIdx).startsWith("--")) {
                        this.nonogramState.increaseMadeSteps();
                        rowToChangeSolutionBoardWithMarks.set(columnIdx, "R" + sequenceCharMark + elementToChangeInsideRowBoardWithMarks.substring(2, 4));

                        this.addColumnToAffectedActionsByIdentifiers(columnIdx, ActionsConstants.actionsToDoAfterColouringOverlappingSequencesInRows);

                        rowToChangeSolutionBoard = this.getNonogramSolutionBoard().get(rowIdx);
                        if(rowToChangeSolutionBoard.get(columnIdx).equals("-")) {
                            this.colourFieldAtGivenPosition(rowIdx, columnIdx);
                            tmpLog = generateColourStepDescription(rowIdx, columnIdx, "fill overlapping fields");
                            addLog(tmpLog);
                            this.addColumnToAffectedActionsByIdentifiers(columnIdx, ActionsConstants.actionsToDoAfterColouringOverlappingSequencesInRows);
                        }
                    } else if (showRepetitions) {
                        System.out.println("Row field was coloured earlier!");
                    }

                    if(rangeLength(List.of(colourBeginColumnIndex, colourEndColumnIndex)) == rowsSequences.get(rowIdx).get(sequenceIdx)) {
                        this.addRowSequenceIdxToNotToInclude(rowIdx, sequenceIdx);
                    }
                }

                if(rangeLength(List.of(colourBeginColumnIndex, colourEndColumnIndex)) == sequenceLength) {
                    this.addRowSequenceIdxToNotToInclude(rowIdx, sequenceIdx);
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

        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);

        int nonogramWidth = this.getWidth();

        List<Integer> colouredSequenceRange;
        List<Integer> rowSequenceRange;
        int rowSequenceLength;
        int sequenceOnBoardLength;

        List<Integer> rowSequencesIndexesIncludingSequenceRange;
        List<Integer> rowSequencesLengthsIncludingSequenceRange;

        int firstXColumnIndex;
        int lastXColumnIndex;

        int rowSequenceIdxNotToInclude;

        List<Integer> oldSequenceRange;
        List<Integer> updatedSequenceRange;

        List<String> nonogramSolutionBoardRow = this.getNonogramSolutionBoard().get(rowIdx);

        for(int columnIdx = 0; columnIdx < nonogramWidth; columnIdx++) {

            if(nonogramSolutionBoardRow.get(columnIdx).equals("O")) {

                colouredSequenceRange = new ArrayList<>();
                colouredSequenceRange.add(columnIdx);

                while(columnIdx < nonogramWidth && nonogramSolutionBoardRow.get(columnIdx).equals("O")) {
                    columnIdx++;
                }

                colouredSequenceRange.add(columnIdx - 1);
                sequenceOnBoardLength = rangeLength(colouredSequenceRange);
                rowSequencesIndexesIncludingSequenceRange = new ArrayList<>();
                rowSequencesLengthsIncludingSequenceRange = new ArrayList<>();

                for(int seqNo = 0; seqNo < rowSequencesRanges.size(); seqNo++) {
                    rowSequenceRange = rowSequencesRanges.get(seqNo);
                    rowSequenceLength = rowSequencesLengths.get(seqNo);
                    if(rangeInsideAnotherRange(colouredSequenceRange, rowSequenceRange) && rangeLength(colouredSequenceRange) <= rowSequenceLength) {
                        rowSequencesIndexesIncludingSequenceRange.add(seqNo);
                        rowSequencesLengthsIncludingSequenceRange.add(rowSequencesLengths.get(seqNo));
                    }
                }

                firstXColumnIndex = colouredSequenceRange.get(0) - 1;
                lastXColumnIndex = colouredSequenceRange.get(1) + 1;

                if(rowSequencesIndexesIncludingSequenceRange.size() == 1 ) {

                    rowSequenceIdxNotToInclude = rowSequencesIndexesIncludingSequenceRange.get(0);

                    if(sequenceOnBoardLength == rowSequencesLengthsIncludingSequenceRange.get(0)) {

                        if(firstXColumnIndex > -1) {
                            if (this.getNonogramSolutionBoard().get(rowIdx).get(firstXColumnIndex).equals("-")) {

                                tmpLog = generatePlacingXStepDescription(rowIdx, firstXColumnIndex, "placing \"X\" before longest sequence (only possible)");
                                addLog(tmpLog);

                                this.placeXAtGivenPosition(rowIdx, firstXColumnIndex)
                                        .addRowFieldToNotToInclude(rowIdx, firstXColumnIndex)
                                        .addColumnFieldToNotToInclude(firstXColumnIndex, rowIdx);

                                this.nonogramState.increaseMadeSteps();

                                this.addColumnToAffectedActionsByIdentifiers(firstXColumnIndex, ActionsConstants.actionsToDoAfterPlacingXsAroundLongestSequencesInRows);
                                this.addRowToAffectedActionsByIdentifiers(rowIdx, ActionsConstants.actionsToDoAfterPlacingXsAroundLongestSequencesInRows);
                            } else if (showRepetitions) {
                                System.out.println("Longest sequence in row firstXColumnIndex added earlier!");
                            }
                        }

                        if(lastXColumnIndex < this.getWidth() ) {
                            if(this.getNonogramSolutionBoard().get(rowIdx).get(lastXColumnIndex).equals("-")) {

                                tmpLog = generatePlacingXStepDescription(rowIdx, lastXColumnIndex, "placing \"X\" after longest sequence in row (only possible)");
                                addLog(tmpLog);

                                this.placeXAtGivenPosition(rowIdx, lastXColumnIndex)
                                        .addRowFieldToNotToInclude(rowIdx, lastXColumnIndex)
                                        .addColumnFieldToNotToInclude(lastXColumnIndex, rowIdx);

                                this.nonogramState.increaseMadeSteps();

                                this.addColumnToAffectedActionsByIdentifiers(lastXColumnIndex, ActionsConstants.actionsToDoAfterPlacingXsAroundLongestSequencesInRows);
                                this.addRowToAffectedActionsByIdentifiers(rowIdx, ActionsConstants.actionsToDoAfterPlacingXsAroundLongestSequencesInRows);
                            } else if(showRepetitions) {
                                System.out.println("Longest sequence in row lastXColumnIndex added earlier!");
                            }
                        }

                        for(int sequenceColumnIdx = firstXColumnIndex + 1; sequenceColumnIdx < lastXColumnIndex; sequenceColumnIdx++) {
                            this.addRowFieldToNotToInclude(rowIdx, sequenceColumnIdx);
                            this.nonogramState.increaseMadeSteps();
                        }

                        oldSequenceRange = rowSequencesRanges.get(rowSequenceIdxNotToInclude);
                        updatedSequenceRange = List.of(firstXColumnIndex + 1, lastXColumnIndex - 1);
                        this.changeRowSequenceRange(rowIdx, rowSequenceIdxNotToInclude, updatedSequenceRange);
                        tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, rowSequenceIdxNotToInclude, oldSequenceRange, updatedSequenceRange, "correcting sequence while placing X before only matching coloured sequence");
                        addLog(tmpLog);

                        this.addRowSequenceIdxToNotToInclude(rowIdx, rowSequenceIdxNotToInclude);
                    }
                } else if(rowSequencesLengthsIncludingSequenceRange.size() > 1) {

                    //check if length of sequence == Max(foundSequences_lengths)
                    if(sequenceOnBoardLength == Collections.max(rowSequencesLengthsIncludingSequenceRange)) {

                        if(this.getNonogramSolutionBoard().get(rowIdx).get(firstXColumnIndex).equals("-")) {
                            this.placeXAtGivenPosition(rowIdx, firstXColumnIndex)
                                    .addRowFieldToNotToInclude(rowIdx, firstXColumnIndex)
                                    .addColumnFieldToNotToInclude(firstXColumnIndex, rowIdx);
                            this.nonogramState.increaseMadeSteps();

                            tmpLog = generatePlacingXStepDescription(rowIdx, firstXColumnIndex, "placing \"X\" before longest sequence (index not known)");
                            addLog(tmpLog);

                            this.addColumnToAffectedActionsByIdentifiers(firstXColumnIndex, ActionsConstants.actionsToDoAfterPlacingXsAroundLongestSequencesInRows);
                            this.addRowToAffectedActionsByIdentifiers(rowIdx, ActionsConstants.actionsToDoAfterPlacingXsAroundLongestSequencesInRows);
                        } else if(showRepetitions) {
                            System.out.println("Longest sequence in row firstXIndex added earlier!");
                        }

                        if(this.getNonogramSolutionBoard().get(rowIdx).get(lastXColumnIndex).equals("-")) {
                            this.placeXAtGivenPosition(rowIdx, lastXColumnIndex)
                                    .addRowFieldToNotToInclude(rowIdx, lastXColumnIndex)
                                    .addColumnFieldToNotToInclude(lastXColumnIndex, rowIdx);
                            this.nonogramState.increaseMadeSteps();

                            tmpLog = generatePlacingXStepDescription(rowIdx, lastXColumnIndex, "placing \"X\" after longest sequence (index not known)");
                            addLog(tmpLog);

                            this.addColumnToAffectedActionsByIdentifiers(lastXColumnIndex, ActionsConstants.actionsToDoAfterPlacingXsAroundLongestSequencesInRows);
                            this.addRowToAffectedActionsByIdentifiers(rowIdx, ActionsConstants.actionsToDoAfterPlacingXsAroundLongestSequencesInRows);
                        } else if(showRepetitions) {
                            System.out.println("Longest sequence in row lastXIndex added earlier!");
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
                                this.nonogramState.increaseMadeSteps();

                                tmpLog = generatePlacingXStepDescription(rowIdx, emptyFieldColumnIdx, "placing \"X\" inside too short empty fields sequence");
                                addLog(tmpLog);

                                addColumnToAffectedActionsByIdentifiers(emptyFieldColumnIdx, ActionsConstants.actionsToDoAfterPlacingXsAtTooShortEmptySequencesInRows);
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
    public void correctRowsSequencesRanges() {

        for (Integer rowIndex : this.getAffectedRowsToCorrectSequencesRanges()) {
            correctSequencesRangesInRow(rowIndex);

            List<Integer> sequencesLengths = this.getRowsSequences().get(rowIndex);
            List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIndex);

            if(!emptyRow(rowIndex)) {
                for(int seqNo = 0; seqNo < rowSequencesRanges.size(); seqNo++) {
                    if(rangeLength(rowSequencesRanges.get(seqNo)) < sequencesLengths.get(seqNo)) {
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
    public void correctSequencesRangesInRow(int rowIdx) {
        correctSequencesRangesInRowFromLeft(rowIdx);
        correctSequencesRangesInRowFromRight(rowIdx);
    }
    public void correctSequencesRangesInRowFromLeft(int rowIdx) {
        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowFieldsNotToInclude = this.getRowsFieldsNotToInclude().get(rowIdx);

        List<Integer> fullSequenceRange;
        List<Integer> currentSequenceRange;
        List<Integer> nextSequenceRange;
        int updatedNextSequenceBeginRangeColumnIndex;
        int updatedStartIndex;
        int nextSequenceOldBeginRangeColumnIndex;
        int nextSequenceId;
        List<Integer> updatedNextSequenceRange;

        List<Integer> rowSequencesIdsNotToInclude = this.getRowsSequencesIdsNotToInclude().get(rowIdx);

        for(int sequenceIdx = 0; sequenceIdx < rowSequencesRanges.size() - 1; sequenceIdx++) {
            nextSequenceId = sequenceIdx + 1;
            if(rowSequencesIdsNotToInclude.contains(sequenceIdx) && !rowSequencesIdsNotToInclude.contains(sequenceIdx + 1)) {
                fullSequenceRange = rowSequencesRanges.get(sequenceIdx);

                nextSequenceRange = rowSequencesRanges.get(nextSequenceId);
                updatedNextSequenceBeginRangeColumnIndex = fullSequenceRange.get(1) + 2;

                while(rowFieldsNotToInclude.contains(updatedNextSequenceBeginRangeColumnIndex)) {
                    updatedNextSequenceBeginRangeColumnIndex++;
                }

                nextSequenceOldBeginRangeColumnIndex = nextSequenceRange.get(0);

                updatedNextSequenceRange = new ArrayList<>();

                updatedStartIndex = Math.max(nextSequenceOldBeginRangeColumnIndex, updatedNextSequenceBeginRangeColumnIndex);
                updatedNextSequenceRange.add(updatedStartIndex);
                updatedNextSequenceRange.add(nextSequenceRange.get(1));

                correctRowRangeFromLeft(rowIdx, rowSequencesLengths, nextSequenceRange, updatedStartIndex, nextSequenceOldBeginRangeColumnIndex, nextSequenceId, updatedNextSequenceRange);
            }   else if(!rowSequencesIdsNotToInclude.contains(sequenceIdx) && !rowSequencesIdsNotToInclude.contains(sequenceIdx + 1)) {
                currentSequenceRange = rowSequencesRanges.get(sequenceIdx);
                nextSequenceRange = rowSequencesRanges.get(nextSequenceId);

                //get the possible new minimum start index of next sequence checking where is the earliest index that current sequence can end (+1 for 'X' after sequence)
                updatedNextSequenceBeginRangeColumnIndex = currentSequenceRange.get(0) + rowSequencesLengths.get(sequenceIdx) + 1;

                nextSequenceOldBeginRangeColumnIndex = nextSequenceRange.get(0);

                updatedNextSequenceRange = new ArrayList<>();

                updatedStartIndex = Math.max(nextSequenceOldBeginRangeColumnIndex, updatedNextSequenceBeginRangeColumnIndex);
                updatedNextSequenceRange.add(updatedStartIndex);
                updatedNextSequenceRange.add(nextSequenceRange.get(1));

                correctRowRangeFromLeft(rowIdx, rowSequencesLengths, nextSequenceRange, updatedStartIndex, nextSequenceOldBeginRangeColumnIndex, nextSequenceId, updatedNextSequenceRange);
            }
        }
    }

    private void correctRowRangeFromLeft(int rowIdx, List<Integer> rowSequencesLengths, List<Integer> nextSequenceRange, int updatedStartIndex, int nextSequenceOldBeginRangeColumnIndex, int nextSequenceId, List<Integer> updatedNextSequenceRange) {
        if(updatedStartIndex != nextSequenceOldBeginRangeColumnIndex) {
            this.getRowsSequencesRanges().get(rowIdx).set(nextSequenceId, updatedNextSequenceRange);
            this.addRowToAffectedActionsByIdentifiers(rowIdx, ActionsConstants.actionsToDoAfterCorrectingRowsSequences);
            this.nonogramState.increaseMadeSteps();
            tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, nextSequenceId, nextSequenceRange, updatedNextSequenceRange, "correcting from left");
            addLog(tmpLog);

            if(rangeLength(updatedNextSequenceRange) == rowSequencesLengths.get(nextSequenceId) && isColumnRangeColoured(rowIdx, updatedNextSequenceRange)) {
                this.addRowSequenceIdxToNotToInclude(rowIdx, nextSequenceId);
            }
        }
    }

    public void correctSequencesRangesInRowFromRight(int rowIdx) {

        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowFieldsNotToInclude = this.getRowsFieldsNotToInclude().get(rowIdx);

        List<Integer> fullSequenceRange;
        List<Integer> currentSequenceRange;
        List<Integer> oldPreviousSequenceRange;
        int previousSequenceUpdatedRangeEndColumnIndex;
        int updatedEndIndex;
        int previousSequenceOldRangeEndColumnIndex;
        int previousSequenceId;
        List<Integer> updatedPreviousSequenceRange;

        List<Integer> rowSequencesIdsNotToInclude = this.getRowsSequencesIdsNotToInclude().get(rowIdx);

        int currentSequenceLength;

        for(int sequenceIdx = rowSequencesRanges.size() - 1; sequenceIdx > 0; sequenceIdx--) {
            previousSequenceId = sequenceIdx - 1;
            if(rowSequencesIdsNotToInclude.contains(sequenceIdx) && !rowSequencesIdsNotToInclude.contains(previousSequenceId)) {

                fullSequenceRange = rowSequencesRanges.get(sequenceIdx);
                oldPreviousSequenceRange = rowSequencesRanges.get(previousSequenceId);

                previousSequenceUpdatedRangeEndColumnIndex = fullSequenceRange.get(0) - 2;

                while(rowFieldsNotToInclude.contains(previousSequenceUpdatedRangeEndColumnIndex)) {
                    previousSequenceUpdatedRangeEndColumnIndex--;
                }

                previousSequenceOldRangeEndColumnIndex = oldPreviousSequenceRange.get(1);

                updatedPreviousSequenceRange = new ArrayList<>();

                updatedEndIndex = Math.min(previousSequenceOldRangeEndColumnIndex, previousSequenceUpdatedRangeEndColumnIndex);

                updatedPreviousSequenceRange.add(oldPreviousSequenceRange.get(0));
                updatedPreviousSequenceRange.add(updatedEndIndex);

                correctRowRangeFromRight(rowIdx, rowSequencesLengths, oldPreviousSequenceRange, updatedEndIndex, previousSequenceOldRangeEndColumnIndex, previousSequenceId, updatedPreviousSequenceRange);
            } else if(!rowSequencesIdsNotToInclude.contains(sequenceIdx) && !rowSequencesIdsNotToInclude.contains(previousSequenceId)) {
                currentSequenceLength = rowSequencesLengths.get(sequenceIdx);
                currentSequenceRange = rowSequencesRanges.get(sequenceIdx);
                oldPreviousSequenceRange = rowSequencesRanges.get(previousSequenceId);

                previousSequenceUpdatedRangeEndColumnIndex = currentSequenceRange.get(1) - currentSequenceLength - 1;

                previousSequenceOldRangeEndColumnIndex = oldPreviousSequenceRange.get(1);

                updatedPreviousSequenceRange = new ArrayList<>();

                updatedEndIndex = Math.min(previousSequenceOldRangeEndColumnIndex, previousSequenceUpdatedRangeEndColumnIndex);

                updatedPreviousSequenceRange.add(oldPreviousSequenceRange.get(0));
                updatedPreviousSequenceRange.add(updatedEndIndex);

                correctRowRangeFromRight(rowIdx, rowSequencesLengths, oldPreviousSequenceRange, updatedEndIndex, previousSequenceOldRangeEndColumnIndex, previousSequenceId, updatedPreviousSequenceRange);
            }

        }

    }

    private void correctRowRangeFromRight(int rowIdx, List<Integer> rowSequencesLengths, List<Integer> oldPreviousSequenceRange, int updatedEndIndex, int previousSequenceOldRangeEndColumnIndex, int previousSequenceId, List<Integer> updatedPreviousSequenceRange) {
        if(updatedEndIndex != previousSequenceOldRangeEndColumnIndex) {
            this.getRowsSequencesRanges().get(rowIdx).set(previousSequenceId, updatedPreviousSequenceRange);
            this.nonogramState.increaseMadeSteps();
            tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, previousSequenceId, oldPreviousSequenceRange, updatedPreviousSequenceRange, "correcting from right");
            addLog(tmpLog);

            if(rangeLength(updatedPreviousSequenceRange) == rowSequencesLengths.get(previousSequenceId) && isColumnRangeColoured(rowIdx, updatedPreviousSequenceRange)) {
                this.addRowSequenceIdxToNotToInclude(rowIdx, previousSequenceId);
            }

            this.addRowToAffectedActionsByIdentifiers(rowIdx, ActionsConstants.actionsToDoAfterCorrectingRowsSequences);
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
                    tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, sequenceId, rowSequenceRange, updatedRange, "met coloured field from left side");
                    addLog(tmpLog);
                    this.getRowsSequencesRanges().get(rowIdx).set(sequenceId, updatedRange);
                    if(rangeLength(updatedRange) == rowSequencesLengths.get(sequenceId) && isColumnRangeColoured(rowIdx, updatedRange)) {
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
                    tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, sequenceId, rowSequenceRange, updatedRange, "met coloured field from right side");
                    addLog(tmpLog);
                    this.getRowsSequencesRanges().get(rowIdx).set(sequenceId, updatedRange);
                    if(rangeLength(updatedRange) == rowSequencesLengths.get(sequenceId) && isColumnRangeColoured(rowIdx, updatedRange)) {
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
                    tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, seqNo, rowSequenceRange, updatedRange, "\"X\" on way");
                    addLog(tmpLog);
                    this.updateRowSequenceRange(rowIdx, seqNo, updatedRange);
                    if(rangeLength(updatedRange) == rowSequenceLength && isColumnRangeColoured(rowIdx, updatedRange)) {
                        this.addRowSequenceIdxToNotToInclude(rowIdx, seqNo);
                    }
                }
            }
        }

        if(rowSequenceRangesChanged) {
            this.nonogramState.increaseMadeSteps();
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
            existRangeIncludingColumn = rangesListIncludingAnotherRange(rowSequencesRanges, fieldAsRange);

            if(!existRangeIncludingColumn) {
                if(this.getNonogramSolutionBoard().get(rowIdx).get(columnIdx).equals("-")) {
                    this.placeXAtGivenPosition(rowIdx, columnIdx)
                            .addRowFieldToNotToInclude(rowIdx, columnIdx)
                            .addColumnFieldToNotToInclude(columnIdx, rowIdx);

                    tmpLog = generatePlacingXStepDescription(rowIdx, columnIdx, "placing \"X\" at unreachable field");
                    addColumnToAffectedActionsByIdentifiers(columnIdx, ActionsConstants.actionsToDoAfterPlacingXsAtRowsUnreachableFields);

                    this.nonogramState.increaseMadeSteps();
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

                if(possibleSequenceLengths.isEmpty()) {
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
                            this.colourFieldAtGivenPosition(rowIdx, colourColumnIdx);
                            this.addColumnToAffectedActionsByIdentifiers(colourColumnIdx, ActionsConstants.actionsToDoAfterExtendingColouredFieldsNearXInRows);
                            this.nonogramState.increaseMadeSteps();

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

                if(possibleSequenceLengths.isEmpty()) {
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
                            this.nonogramState.increaseMadeSteps();
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
            tmpLog = generateAddingSequenceToNotToIncludeDescription(rowIdx, seqIdx);
            addLog(tmpLog);
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

    public void changeRowSequenceRange(int rowIndex, int sequenceIndex, List<Integer> updatedRange) {
        this.rowsSequencesRanges.get(rowIndex).set(sequenceIndex, updatedRange);
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

    /**
     * @param rowIdx - row index to update one of sequences range
     * @param sequenceIdx - row sequence index to update sequence range
     * @param updatedRange - updated row sequence range
     */
    public void updateRowSequenceRange(int rowIdx, int sequenceIdx, List<Integer> updatedRange) {
        this.rowsSequencesRanges.get(rowIdx).set(sequenceIdx, updatedRange);
    }

    private boolean isColumnRangeColoured(int rowIdx, List<Integer> columnRange) {
        for(Integer columnIdx : columnRange) {
            if(!this.getNonogramSolutionBoard().get(rowIdx).get(columnIdx).equals("O")) {
                return false;
            }
        }

        return true;
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
        if (log.isEmpty()) {
            System.out.println("Trying to add empty log!!!");
        } else {
            this.logs.add(log);
        }
    }

    public String generateColourStepDescription(int rowIndex, int columnIndex, String actionType) {
        return String.format("ROW %d, COLUMN %d - field colouring - %s.", rowIndex, columnIndex, actionType);
    }

    public String generatePlacingXStepDescription(int rowIndex, int columnIndex, String actionType) {
        return String.format("ROW %d, COLUMN %d - X placing - %s.", rowIndex, columnIndex, actionType);
    }

    public String generateCorrectingRowSequenceRangeStepDescription(int rowIndex, int sequenceIndex, List<Integer> oldRange, List<Integer> correctedRange, String actionType) {
        return String.format("ROW %d, SEQUENCE %d - range correcting - from [%d, %d] to [%d, %d] - %s", rowIndex, sequenceIndex,
                oldRange.get(0), oldRange.get(1), correctedRange.get(0), correctedRange.get(1), actionType);
    }

    private void addRowToAffectedActionsByIdentifiers(int rowIdx, List<Integer> affectedActionIds) {
        for(int affectedActionId : affectedActionIds) {
            switch (affectedActionId) {
                case ActionsConstants.CORRECT_ROWS_SEQUENCES_RANGES ->
                        this.getAffectedRowsToCorrectSequencesRanges().add(rowIdx);
                case ActionsConstants.CORRECT_ROWS_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS ->
                        this.getAffectedRowsToCorrectSequencesRangesWhenMetColouredField().add(rowIdx);
                case ActionsConstants.CORRECT_ROWS_SEQUENCES_RANGES_IF_X_ON_WAY ->
                        this.getAffectedRowsToChangeSequencesRangeIfXOnWay().add(rowIdx);
                case ActionsConstants.COLOUR_OVERLAPPING_FIELDS_IN_ROWS ->
                        this.getAffectedRowsToFillOverlappingFields().add(rowIdx);
                case ActionsConstants.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROWS ->
                        this.getAffectedRowsToExtendColouredFieldsNearX().add(rowIdx);
                case ActionsConstants.PLACE_XS_ROWS_AT_UNREACHABLE_FIELDS ->
                        this.getAffectedRowsToPlaceXsAtUnreachableFields().add(rowIdx);
                case ActionsConstants.PLACE_XS_ROWS_AROUND_LONGEST_SEQUENCES ->
                        this.getAffectedRowsToPlaceXsAroundLongestSequences().add(rowIdx);
                case ActionsConstants.PLACE_XS_ROWS_AT_TOO_SHORT_EMPTY_SEQUENCES ->
                        this.getAffectedRowsToPlaceXsAtTooShortEmptySequences().add(rowIdx);
                case ActionsConstants.MARK_AVAILABLE_FIELDS_IN_ROWS ->
                        this.getAffectedRowsToMarkAvailableSequences().add(rowIdx);
                default -> {
                }
            }
        }
    }

    public String generateAddingSequenceToNotToIncludeDescription(int rowIdx, int seqNo) {
        return String.format("ROW %d - SeqNo = %d added to not to include", rowIdx, seqNo);
    }

    private void addColumnToAffectedActionsByIdentifiers(int columnIdx, List<Integer> affectedActionIds) {
        for(int affectedActionId : affectedActionIds) {
            switch (affectedActionId) {
                case ActionsConstants.CORRECT_COLUMNS_SEQUENCES_RANGES ->
                        this.getAffectedColumnsToCorrectSequencesRanges().add(columnIdx);
                case ActionsConstants.CORRECT_COLUMNS_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS ->
                        this.getAffectedColumnsToCorrectSequencesRangesWhenMetColouredField().add(columnIdx);
                case ActionsConstants.CORRECT_COLUMNS_SEQUENCES_RANGES_IF_X_ON_WAY ->
                        this.getAffectedColumnsToCorrectSequencesRangesIfXOnWay().add(columnIdx);
                case ActionsConstants.COLOUR_OVERLAPPING_FIELDS_IN_COLUMNS ->
                        this.getAffectedColumnsToFillOverlappingFields().add(columnIdx);
                case ActionsConstants.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMNS ->
                        this.getAffectedColumnsToExtendColouredFieldsNearX().add(columnIdx);
                case ActionsConstants.PLACE_XS_COLUMNS_AT_UNREACHABLE_FIELDS ->
                        this.getAffectedColumnsToPlaceXsAtUnreachableFields().add(columnIdx);
                case ActionsConstants.PLACE_XS_COLUMNS_AROUND_LONGEST_SEQUENCES ->
                        this.getAffectedColumnsToPlaceXsAroundLongestSequences().add(columnIdx);
                case ActionsConstants.MARK_AVAILABLE_FIELDS_IN_COLUMNS ->
                        this.getAffectedColumnsToMarkAvailableSequences().add(columnIdx);
                default -> {
                }
            }
        }
    }

    public void printNonogramBoard() {
        List<String> rowsWithIndexes = IntStream.range(0, this.getNonogramSolutionBoard().size())
                .mapToObj(rowIndex -> this.getNonogramSolutionBoard().get(rowIndex) + " " + rowIndex)
                .toList();
        for(String boardRow : rowsWithIndexes) {
            System.out.println(boardRow);
        }
    }
}
