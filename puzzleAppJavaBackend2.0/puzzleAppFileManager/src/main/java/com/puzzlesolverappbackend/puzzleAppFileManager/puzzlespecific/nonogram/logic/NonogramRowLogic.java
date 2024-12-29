package com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic;

import com.puzzlesolverappbackend.puzzleAppFileManager.payload.ActionsConstants;
import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.NonogramParametersComparatorHelper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzleAppFileManager.payload.ActionsConstants.actionsToDoAfterCorrectingRangesWhenMarkingSequencesInRows;
import static com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.NonogramConstants.EMPTY_FIELD;
import static com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.NonogramLogicService.*;

@Setter
@Getter
@Slf4j
@NoArgsConstructor
public class NonogramRowLogic extends NonogramLogicParams {

    private final static String CORRECT_ROW_SEQ_RANGE_MARKING_FIELD = "correcting row sequence range when marking field";

    private final static String FILL_OVERLAPPING_FIELDS = "fill overlapping fields";

    public NonogramRowLogic(NonogramLogic nonogramLogic) {
        this.nonogramState = nonogramLogic.getNonogramState();
        this.logs = nonogramLogic.getLogs();

        this.rowsSequences = nonogramLogic.getRowsSequences();
        this.rowsSequencesRanges = nonogramLogic.getRowsSequencesRanges();
        this.rowsSequencesIdsNotToInclude = nonogramLogic.getRowsSequencesIdsNotToInclude();
        this.rowsFieldsNotToInclude = nonogramLogic.getRowsFieldsNotToInclude();

        this.columnsSequences = nonogramLogic.getColumnsSequences();
        this.columnsSequencesRanges = nonogramLogic.getColumnsSequencesRanges();
        this.columnsSequencesIdsNotToInclude = nonogramLogic.getColumnsSequencesIdsNotToInclude();
        this.columnsFieldsNotToInclude = nonogramLogic.getColumnsFieldsNotToInclude();

        this.nonogramSolutionBoardWithMarks = nonogramLogic.getNonogramSolutionBoardWithMarks();
        this.nonogramSolutionBoard = nonogramLogic.getNonogramSolutionBoard();
        this.actionsToDoList = nonogramLogic.getActionsToDoList();
    }

    /**
     * @param rowIdx - row index on which mark fields with char sequencesr identifiers
     */
    public void markAvailableFieldsInRow(int rowIdx) {
        Field potentiallyColouredField;
        List<Integer> colouredSequenceIndexes;
        int firstSequenceIndex;
        int lastSequenceIndex;
        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequenceRange;
        int matchingSequencesCount;
        int lastMatchingSequenceIndex = -1;
        int colouredSequenceLength;
        String sequenceMarker;

        List<Integer> oldOnlyMatchingSequenceRange;
        List<Integer> newSequenceRange;

        for(int columnIdx = 0; columnIdx < this.getWidth(); columnIdx++) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(potentiallyColouredField)) {

                colouredSequenceIndexes = new ArrayList<>();
                colouredSequenceIndexes.add(columnIdx);

                // TODO - do while(?)
                while(columnIdx < this.getWidth() && isFieldColoured(potentiallyColouredField)) {
                    columnIdx++;
                    potentiallyColouredField = new Field(rowIdx, columnIdx);
                }

                colouredSequenceIndexes.add(columnIdx - 1);

                firstSequenceIndex = colouredSequenceIndexes.get(0);
                lastSequenceIndex = colouredSequenceIndexes.get(1);
                colouredSequenceLength = rangeLength(colouredSequenceIndexes);

                matchingSequencesCount = 0;

                for(int seqNo = 0; seqNo < rowSequencesLengths.size(); seqNo++) {
                    rowSequenceRange = rowSequencesRanges.get(seqNo);

                    if ( rangeInsideAnotherRange(colouredSequenceIndexes, rowSequenceRange)
                            && colouredSequenceLength <= rowSequencesLengths.get(seqNo)) {
                        matchingSequencesCount++;
                        lastMatchingSequenceIndex = seqNo;
                    }
                }

                if (matchingSequencesCount == 1) {

                    sequenceMarker = NonogramLogic.indexToSequenceCharMark(lastMatchingSequenceIndex);
                    for(int sequenceColumnIdx = firstSequenceIndex; sequenceColumnIdx <= lastSequenceIndex; sequenceColumnIdx++) {
                        if (this.getNonogramSolutionBoardWithMarks().get(rowIdx).get(sequenceColumnIdx).startsWith(EMPTY_FIELD, 1)) {
                            this.markRowBoardField(rowIdx, sequenceColumnIdx, sequenceMarker);
                            this.nonogramState.increaseMadeSteps();
                        } else if (showRepetitions) {
                            System.out.println("Row field was marked earlier.");
                        }
                    }

                    //correct sequence range if new range is shorter
                    oldOnlyMatchingSequenceRange = rowSequencesRanges.get(lastMatchingSequenceIndex);
                    newSequenceRange = calculateNewRangeFromParameters(colouredSequenceIndexes, rowSequencesLengths.get(lastMatchingSequenceIndex));
                    if (rangeLength(newSequenceRange) < rangeLength(oldOnlyMatchingSequenceRange)) {
                        this.changeRowSequenceRange(rowIdx, lastMatchingSequenceIndex, newSequenceRange);
                        tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, lastMatchingSequenceIndex, oldOnlyMatchingSequenceRange, newSequenceRange, CORRECT_ROW_SEQ_RANGE_MARKING_FIELD);
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
     * fill fields in row where ranges met specific condition (range_length < sequence_length * 2)
     */
    public void fillOverlappingFieldsInRow (int rowIdx) {
        List<Integer> sequencesInRowLengths = this.getRowsSequences().get(rowIdx);
        List<List<Integer>> sequencesInRowRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> range;

        int sequenceLength;
        int rangeBeginIndex;
        int rangeEndIndex;
        List<Integer> colouringRange;
        Field fieldToColour;

        for(int sequenceIdx = 0; sequenceIdx < sequencesInRowLengths.size(); sequenceIdx++) {
            sequenceLength = sequencesInRowLengths.get(sequenceIdx);
            range = sequencesInRowRanges.get(sequenceIdx);
            rangeBeginIndex = range.get(0);
            rangeEndIndex = range.get(1);

            colouringRange = IntStream.rangeClosed(rangeEndIndex - sequenceLength + 1, rangeBeginIndex + sequenceLength - 1)
                    .boxed()
                    .toList();

            for (int columnIdx : colouringRange) {
                fieldToColour = new Field(rowIdx, columnIdx);

                if (isFieldEmpty(fieldToColour)) {
                    this.colourFieldAtGivenPosition(fieldToColour, "R---");
                    tmpLog = generateColourStepDescription(rowIdx, columnIdx, FILL_OVERLAPPING_FIELDS);
                    addLog(tmpLog);
                    this.addColumnToAffectedActionsByIdentifiers(columnIdx, ActionsConstants.actionsToDoAfterColouringOverlappingSequencesInRows);
                    this.addRowToAffectedActionsByIdentifiers(rowIdx, ActionsConstants.actionsToDoInRowAfterColouringOverlappingSequencesInRows);
                    this.getNonogramState().increaseMadeSteps();
                }
                else if (showRepetitions) {
                    logger.warn("Row field was coloured earlier!");
                }

                if (rangeLength(colouringRange) == rowsSequences.get(rowIdx).get(sequenceIdx)) {
                    this.excludeSequenceInRow(rowIdx, sequenceIdx);
                }
            }
        }
    }

    /**
     * @param rowIdx - row index on which place "X" around coloured fields
     */
    public void placeXsAroundLongestSequencesInRow(int rowIdx) {
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);

        List<Integer> colouredSequenceRange;
        List<Integer> rowSequenceRange;
        int sequenceOnBoardLength;

        List<Integer> rowSequencesIndexesIncludingSequenceRange;
        List<Integer> rowSequencesLengthsIncludingSequenceRange;

        List<Integer> xAtEdges;
        Field potentiallyColouredField;

        int sequenceIdxToExclude;

        List<Integer> updatedSequenceRange;

        for(int columnIdx = 0; columnIdx < this.getWidth(); columnIdx++) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(potentiallyColouredField)) {
                colouredSequenceRange = findColouredSequenceRangeInRow(columnIdx, rowIdx);
                columnIdx = colouredSequenceRange.get(1); //start from end of current coloured sequence

                sequenceOnBoardLength = rangeLength(colouredSequenceRange);
                rowSequencesIndexesIncludingSequenceRange = new ArrayList<>();
                rowSequencesLengthsIncludingSequenceRange = new ArrayList<>();

                for(int seqNo = 0; seqNo < rowSequencesRanges.size(); seqNo++) {
                    rowSequenceRange = rowSequencesRanges.get(seqNo);
                    if (rangeInsideAnotherRange(colouredSequenceRange, rowSequenceRange)) {
                        rowSequencesIndexesIncludingSequenceRange.add(seqNo);
                        rowSequencesLengthsIncludingSequenceRange.add(rowSequencesLengths.get(seqNo));
                    }
                }

                xAtEdges = new ArrayList<>(Arrays.asList(colouredSequenceRange.get(0) - 1, colouredSequenceRange.get(1) + 1));

                /* given coloured sequence can be part of only one row sequence
                AND given coloured sequence has exactly same length as only matching row range */
                if (rowSequencesIndexesIncludingSequenceRange.size() == 1 && sequenceOnBoardLength == rowSequencesLengthsIncludingSequenceRange.get(0)) {
                    doStuffWhenPlacingXsAroundLongestSequence(rowIdx, xAtEdges, true);

                    updatedSequenceRange = Arrays.asList(xAtEdges.get(0) + 1, xAtEdges.get(1) - 1);
                    excludeColouredFieldsBetweenXs(rowIdx, updatedSequenceRange);
                    sequenceIdxToExclude = rowSequencesIndexesIncludingSequenceRange.get(0);
                    updateLogicAfterPlacingXsAroundOnlyOneMatchingSequenceInRow(rowIdx, sequenceIdxToExclude, colouredSequenceRange);
                } else if (rowSequencesLengthsIncludingSequenceRange.size() > 1 && sequenceOnBoardLength == Collections.max(rowSequencesLengthsIncludingSequenceRange)) {
                    /*more than one row sequence fit in coloured range & length of coloured sequence is a max length of possible matching row sequences lengths */
                    doStuffWhenPlacingXsAroundLongestSequence(rowIdx, xAtEdges, false);
                }
            }
        }
    }

    private List<Integer> findColouredSequenceRangeInRow(int startColumnIdx, int rowIdx) {
        int columnIdx = startColumnIdx;

        List<Integer> colouredSequenceRange = new ArrayList<>();
        colouredSequenceRange.add(columnIdx);
        while(columnIdx < this.getWidth() && isFieldColoured(new Field(rowIdx, columnIdx))) {
            columnIdx++;
        }
        colouredSequenceRange.add(columnIdx - 1);

        return colouredSequenceRange;
    }

    private void doStuffWhenPlacingXsAroundLongestSequence(int rowIdx, List<Integer> xAtEdges, boolean onlyMatching) {
        String logEndPart = onlyMatching ? "[only possible]" : "[sequence index not specified]";
        for(int currentColumnIndex : xAtEdges) {
            Field fieldAtEdge = new Field(rowIdx, currentColumnIndex);
            if (isColumnIndexValid(currentColumnIndex)) {
                if (isFieldEmpty(fieldAtEdge)) {
                    doStuffWhenPlacingXsAfterOrBeforeLongestSequence(rowIdx, currentColumnIndex, xAtEdges, logEndPart);
                } else if (showRepetitions) {
                    log.warn("X around longest sequence(pos={}) in row added earlier! {}", fieldAtEdge, logEndPart);
                }
            }
        }
    }

    private void doStuffWhenPlacingXsAfterOrBeforeLongestSequence(int rowIdx, int currentColumnIndex, List<Integer> xAtEdges, String logEndPart) {
        if (isColumnIndexValid(currentColumnIndex)) {
            addLogWhenPlacingXAroundLongestSequence(rowIdx, currentColumnIndex, xAtEdges, logEndPart);

            Field fieldToExclude = new Field(rowIdx, currentColumnIndex);
            this.placeXAtGivenField(fieldToExclude);
            this.excludeFieldInRow(fieldToExclude);
            this.excludeFieldInColumn(fieldToExclude);

            this.addColumnToAffectedActionsByIdentifiers(currentColumnIndex, ActionsConstants.actionsToDoAfterPlacingXsAroundLongestSequencesInRows);
            this.addRowToAffectedActionsByIdentifiers(rowIdx, ActionsConstants.actionsToDoAfterPlacingXsAroundLongestSequencesInRows);

            this.nonogramState.increaseMadeSteps();
        }
    }

    private void addLogWhenPlacingXAroundLongestSequence(int rowIdx, int currentColumnIndex, List<Integer> xAtEdges, String logEndPart) {
        String logPart;
        if (currentColumnIndex == xAtEdges.get(0)) {
            logPart = "placing \"X\" before longest sequence in row " + logEndPart;
        } else {
            logPart = "placing \"X\" after longest sequence in row " + logEndPart;
        }

        tmpLog = generatePlacingXStepDescription(rowIdx, currentColumnIndex, logPart);
        addLog(tmpLog);
    }

    private void excludeColouredFieldsBetweenXs(int rowIdx, List<Integer> updatedSequenceRange) {
        for(int sequenceColumnIdx : updatedSequenceRange) {
            this.excludeFieldInRow(new Field(rowIdx, sequenceColumnIdx));
            this.nonogramState.increaseMadeSteps();
        }
    }

    private void updateLogicAfterPlacingXsAroundOnlyOneMatchingSequenceInRow(int rowIdx, int rowSequenceToExclude, List<Integer> updatedSequenceRange) {
        List<List<Integer>> rowsSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> oldSequenceRange = rowsSequencesRanges.get(rowSequenceToExclude);
        this.changeRowSequenceRange(rowIdx, rowSequenceToExclude, updatedSequenceRange);
        tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, rowSequenceToExclude,
                oldSequenceRange, updatedSequenceRange, "correcting sequence while placing X before only matching coloured sequence");
        addLog(tmpLog);
        this.excludeSequenceInRow(rowIdx, rowSequenceToExclude);
    }

    /**
     * place an "X" at too short empty fields sequences in row, when none of row sequences can fit in hole
     */
    public void placeXsRowAtTooShortEmptySequences(int rowIdx) {

        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequenceRange;

        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);
        int rowSequenceLength;

        Field potentiallyXPlacedField;
        Field fieldAfterXToCheck;

        List<Integer> sequencesWhichNotFit;
        List<Integer> sequencesWithinRange;

        int firstXIndex;
        int lastXIndex;
        int emptyFieldsSequenceLength;
        List<Integer> emptyFieldsRange;
        Field fieldToExclude;

        boolean notColouredFieldsInFieldsSequence;

        for(int columnIdx = 1; columnIdx < this.getWidth() - 1; columnIdx++) {
            notColouredFieldsInFieldsSequence = true;
            potentiallyXPlacedField = new Field(rowIdx, columnIdx);
            if (isFieldWithX(potentiallyXPlacedField)) {

                sequencesWhichNotFit = new ArrayList<>();
                sequencesWithinRange = new ArrayList<>();

                firstXIndex = columnIdx;
                fieldAfterXToCheck = new Field(rowIdx, ++columnIdx);
                while(columnIdx < this.getWidth() && !isFieldWithX(fieldAfterXToCheck)) {
                    if (isFieldEmpty(fieldAfterXToCheck)) {
                        fieldAfterXToCheck = new Field(rowIdx, ++columnIdx);
                    } else if (isFieldColoured(fieldAfterXToCheck)) {
                        notColouredFieldsInFieldsSequence = false;
                        break;
                    }
                }

                if (notColouredFieldsInFieldsSequence) {
                    lastXIndex = columnIdx;

                    emptyFieldsRange = Arrays.asList(firstXIndex + 1, lastXIndex - 1);
                    emptyFieldsSequenceLength = rangeLength(emptyFieldsRange);

                    if (emptyFieldsSequenceLength > 0) {
                        for(int seqNo = 0; seqNo < rowSequencesLengths.size(); seqNo++) {
                            rowSequenceRange = rowSequencesRanges.get(seqNo);
                            rowSequenceLength = rowSequencesLengths.get(seqNo);

                            if (rangeInsideAnotherRange(emptyFieldsRange, rowSequenceRange) || emptyFieldsSequenceLength > rowSequenceLength) {
                                sequencesWithinRange.add(seqNo);
                                if (emptyFieldsSequenceLength < rowSequenceLength) {
                                    sequencesWhichNotFit.add(seqNo);
                                }
                            }
                        }

                        // if there's not any sequence with length equal or less than emptyFieldSequenceLength
                        if (sequencesWhichNotFit.size() == sequencesWithinRange.size() && emptyFieldsSequenceLength > 0) {
                            for(int emptyFieldColumnIdx = emptyFieldsRange.get(0); emptyFieldColumnIdx <= emptyFieldsRange.get(1); emptyFieldColumnIdx++) {
                                fieldToExclude = new Field(rowIdx, emptyFieldColumnIdx);
                                if (isFieldEmpty(fieldToExclude)) {
                                    this.placeXAtGivenField(fieldToExclude);
                                    this.excludeFieldInRow(fieldToExclude);
                                    this.excludeFieldInColumn(fieldToExclude);
                                    this.addColumnToAffectedActionsByIdentifiers(emptyFieldColumnIdx, ActionsConstants.actionsToDoAfterPlacingXsAtTooShortEmptySequencesInRows);

                                    tmpLog = generatePlacingXStepDescription(rowIdx, emptyFieldColumnIdx, "placing \"X\" inside too short empty fields sequence");
                                    addLog(tmpLog);

                                    this.nonogramState.increaseMadeSteps();
                                } else if (showRepetitions) {
                                    System.out.println("X placed in too short row empty field sequence earlier!");
                                }
                            }
                        }
                    } else {
                        columnIdx--; // to start from next field with X near to first one
                    }
                }
            }
        }
    }

    public void correctRowSequencesRanges(int rowIdx) {
        correctSequencesRangesInRowFromLeft(rowIdx);
        correctSequencesRangesInRowFromRight(rowIdx);
    }

    private void correctSequencesRangesInRowFromLeft(int rowIdx) {
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
            if (rowSequencesIdsNotToInclude.contains(sequenceIdx) && !rowSequencesIdsNotToInclude.contains(nextSequenceId)) {
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
            } else if (!rowSequencesIdsNotToInclude.contains(sequenceIdx) && !rowSequencesIdsNotToInclude.contains(nextSequenceId)) {
                currentSequenceRange = rowSequencesRanges.get(sequenceIdx);
                nextSequenceRange = rowSequencesRanges.get(nextSequenceId);

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

    private void correctRowRangeFromLeft(int rowIdx,
                                         List<Integer> rowSequencesLengths,
                                         List<Integer> nextSequenceRange,
                                         int updatedStartIndex,
                                         int nextSequenceOldBeginRangeColumnIndex,
                                         int nextSequenceId,
                                         List<Integer> updatedNextSequenceRange) {
        if (updatedStartIndex != nextSequenceOldBeginRangeColumnIndex) {
            this.updateRowSequenceRange(rowIdx, nextSequenceId, updatedNextSequenceRange);
            this.addRowToAffectedActionsByIdentifiers(rowIdx, ActionsConstants.actionsToDoAfterCorrectingRowsSequences);
            this.nonogramState.increaseMadeSteps();
            tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, nextSequenceId, nextSequenceRange, updatedNextSequenceRange, "correcting from left");
            addLog(tmpLog);

            if (rangeLength(updatedNextSequenceRange) == rowSequencesLengths.get(nextSequenceId) && isColumnRangeColoured(rowIdx, updatedNextSequenceRange)) {
                this.excludeSequenceInRow(rowIdx, nextSequenceId);
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
            if (rowSequencesIdsNotToInclude.contains(sequenceIdx) && !rowSequencesIdsNotToInclude.contains(previousSequenceId)) {
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

                correctRowRangeFromRight(rowIdx,
                        rowSequencesLengths,
                        oldPreviousSequenceRange,
                        updatedEndIndex,
                        previousSequenceOldRangeEndColumnIndex,
                        previousSequenceId,
                        updatedPreviousSequenceRange);
            } else if (!rowSequencesIdsNotToInclude.contains(sequenceIdx) && !rowSequencesIdsNotToInclude.contains(previousSequenceId)) {
                currentSequenceLength = rowSequencesLengths.get(sequenceIdx);
                currentSequenceRange = rowSequencesRanges.get(sequenceIdx);
                oldPreviousSequenceRange = rowSequencesRanges.get(previousSequenceId);

                previousSequenceUpdatedRangeEndColumnIndex = currentSequenceRange.get(1) - currentSequenceLength - 1;

                previousSequenceOldRangeEndColumnIndex = oldPreviousSequenceRange.get(1);

                updatedPreviousSequenceRange = new ArrayList<>();

                updatedEndIndex = Math.min(previousSequenceOldRangeEndColumnIndex, previousSequenceUpdatedRangeEndColumnIndex);

                updatedPreviousSequenceRange.add(oldPreviousSequenceRange.get(0));
                updatedPreviousSequenceRange.add(updatedEndIndex);

                correctRowRangeFromRight(rowIdx,
                        rowSequencesLengths,
                        oldPreviousSequenceRange,
                        updatedEndIndex,
                        previousSequenceOldRangeEndColumnIndex,
                        previousSequenceId,
                        updatedPreviousSequenceRange);
            }
        }
    }

    private void correctRowRangeFromRight(int rowIdx,
                                          List<Integer> rowSequencesLengths,
                                          List<Integer> oldPreviousSequenceRange,
                                          int updatedEndIndex,
                                          int previousSequenceOldRangeEndColumnIndex,
                                          int previousSequenceId,
                                          List<Integer> updatedPreviousSequenceRange) {
        if (updatedEndIndex != previousSequenceOldRangeEndColumnIndex) {
            this.getRowsSequencesRanges().get(rowIdx).set(previousSequenceId, updatedPreviousSequenceRange);
            this.addRowToAffectedActionsByIdentifiers(rowIdx, ActionsConstants.actionsToDoAfterCorrectingRowsSequences);

            if (rangeLength(updatedPreviousSequenceRange) == rowSequencesLengths.get(previousSequenceId) && isColumnRangeColoured(rowIdx, updatedPreviousSequenceRange)) {
                this.excludeSequenceInRow(rowIdx, previousSequenceId);
            }

            this.nonogramState.increaseMadeSteps();
            tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, previousSequenceId, oldPreviousSequenceRange, updatedPreviousSequenceRange, "correcting from right");
            addLog(tmpLog);
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

        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);
        int rowSequenceRangeStart;
        int rowSequenceRangeEnd;
        List<Integer> oldSequenceRange;
        List<Integer> updatedSequenceRange;

        Field potentiallyColouredField;

        int updatedRangeEndIndex;
        int maximumPossibleSequenceRangeEnd;
        int sequenceId = 0;
        int sequenceLength = rowSequencesLengths.get(0);

        for(int columnIdx = 0; columnIdx < this.getWidth(); columnIdx++) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(potentiallyColouredField)) {
                oldSequenceRange = rowSequencesRanges.get(sequenceId);
                rowSequenceRangeStart = oldSequenceRange.get(0);
                rowSequenceRangeEnd = oldSequenceRange.get(1);
                maximumPossibleSequenceRangeEnd = columnIdx + sequenceLength - 1;

                updatedRangeEndIndex = Math.min(rowSequenceRangeEnd, maximumPossibleSequenceRangeEnd);
                updatedSequenceRange = new ArrayList<>(Arrays.asList(rowSequenceRangeStart, updatedRangeEndIndex));

                if (updatedRangeEndIndex != rowSequenceRangeEnd) {
                    rowSequenceRangesChanged = true;
                    this.getRowsSequencesRanges().get(rowIdx).set(sequenceId, updatedSequenceRange);
//                    if (rangeLength(updatedRange) == rowSequencesLengths.get(sequenceId) && isColumnRangeColoured(rowIdx, updatedRange)) {
//                        this.excludeSequenceInRow(rowIdx, sequenceId);
//                    }
                    tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, sequenceId,
                            oldSequenceRange, updatedSequenceRange, "met coloured field from left side");
                    addLog(tmpLog);
                }

                columnIdx = columnIdx + sequenceLength;
                sequenceId++;
                if (sequenceId < rowSequencesLengths.size()) {
                    sequenceLength = rowSequencesLengths.get(sequenceId);
                } else {
                    break;
                }
            }
        }

        if (rowSequenceRangesChanged) {
            this.nonogramState.increaseMadeSteps();
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
        int rowSequenceRangeStart;
        int rowSequenceRangeEnd;
        List<Integer> oldSequenceRange;
        List<Integer> updatedSequenceRange;

        Field potentiallyColouredField;

        int sequenceId = rowSequencesLengths.size() - 1;
        int sequenceLength = rowSequencesLengths.get(sequenceId);
        int updatedRangeStartIndex;
        int minimumPossibleSequenceRangeStart;

        for(int columnIdx = this.getWidth() - 1; columnIdx >= 0; columnIdx--) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(potentiallyColouredField)) {
                oldSequenceRange = rowSequencesRanges.get(sequenceId);
                rowSequenceRangeStart = oldSequenceRange.get(0);
                rowSequenceRangeEnd = oldSequenceRange.get(1);
                minimumPossibleSequenceRangeStart = columnIdx - sequenceLength + 1;

                updatedRangeStartIndex = Math.max(minimumPossibleSequenceRangeStart, rowSequenceRangeStart);
                updatedSequenceRange = new ArrayList<>(Arrays.asList(updatedRangeStartIndex, rowSequenceRangeEnd));
                if (updatedRangeStartIndex != rowSequenceRangeStart) {
                    rowSequenceRangesChanged = true;
                    this.updateRowSequenceRange(rowIdx, sequenceId, updatedSequenceRange);
                    if (rangeLength(updatedSequenceRange) == rowSequencesLengths.get(sequenceId) && isColumnRangeColoured(rowIdx, updatedSequenceRange)) {
                        this.excludeSequenceInRow(rowIdx, sequenceId);
                    }

                    tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, sequenceId,
                            oldSequenceRange, updatedSequenceRange, "met coloured field from right side");
                    addLog(tmpLog);
                }

                columnIdx = columnIdx - sequenceLength;
                sequenceId--;
                if (sequenceId >= 0) {
                    sequenceLength = rowSequencesLengths.get(sequenceId);
                } else {
                    break;
                }
            }
        }

        if (rowSequenceRangesChanged) {
            this.nonogramState.increaseMadeSteps();
            addRowToAffectedActionsByIdentifiers(rowIdx, ActionsConstants.actionsToDoAfterCorrectingRowsSequencesWhenMetColouredField);
        }
    }

    /**
     * @param rowIdx - column to correct sequence/s range/s if x on way (sequence won't fit)
     */
    public void correctRowRangeIndexesIfXOnWay(int rowIdx) {

        boolean rowSequenceRangesChanged = false;

        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> excludedRowSequences = this.getRowsSequencesIdsNotToInclude().get(rowIdx);
        int rowSequenceLength;
        List<Integer> rowSequenceRange;

        int updatedRowRangeStartIndex;
        int updatedRowRangeEndIndex;
        List<Integer> updatedRange;

        for(int seqNo = 0; seqNo < rowSequencesRanges.size(); seqNo++) {
            if (!excludedRowSequences.contains(seqNo)) {

                rowSequenceRange = rowSequencesRanges.get(seqNo);
                rowSequenceLength = rowSequencesLengths.get(seqNo);

                updatedRowRangeStartIndex = getUpdatedRowRangeStartIndexBecauseXOnWay(rowIdx, rowSequenceRange, rowSequenceLength);
                updatedRowRangeEndIndex = getUpdatedRowRangeEndIndexBecauseXOnWay(rowIdx, rowSequenceRange, rowSequenceLength);
                updatedRange = new ArrayList<>(Arrays.asList(updatedRowRangeStartIndex, updatedRowRangeEndIndex));

                if (!NonogramParametersComparatorHelper.rangesEqual(rowSequenceRange, updatedRange)) {
                    rowSequenceRangesChanged = true;
                    tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, seqNo, rowSequenceRange, updatedRange, "\"X\" on way");
                    addLog(tmpLog);
                    this.updateRowSequenceRange(rowIdx, seqNo, updatedRange);
                    if (rangeLength(updatedRange) == rowSequenceLength && isColumnRangeColoured(rowIdx, updatedRange)) {
                        this.excludeSequenceInRow(rowIdx, seqNo);
                    }
                }
            }
        }

        if (rowSequenceRangesChanged) {
            this.nonogramState.increaseMadeSteps();
            addRowToAffectedActionsByIdentifiers(rowIdx, ActionsConstants.actionsToDoAfterCorrectingRowsSequencesIfXOnWay);
        }
    }

    private int getUpdatedRowRangeStartIndexBecauseXOnWay(int rowIdx,
                                             List<Integer> rowSequenceRange,
                                             int rowSequenceLength) {
        Field potentiallyXOnWayField;

        int updatedRowRangeStartIndex = rowSequenceRange.get(0);
        boolean indexOk;

        for(int columnStartIndex = rowSequenceRange.get(0); columnStartIndex <= rowSequenceRange.get(1) - rowSequenceLength + 1; columnStartIndex++) {
            indexOk = true;
            for(int columnIdx = columnStartIndex; columnIdx < columnStartIndex + rowSequenceLength; columnIdx++) {
                potentiallyXOnWayField = new Field(rowIdx, columnIdx);
                if (isFieldWithX(potentiallyXOnWayField)) {
                    indexOk = false;
                    break;
                }
            }
            if (indexOk) {
                break;
            } else {
                updatedRowRangeStartIndex++;
            }
        }

        return updatedRowRangeStartIndex;
    }

    private int getUpdatedRowRangeEndIndexBecauseXOnWay(int rowIdx,
                                           List<Integer> rowSequenceRange,
                                           int rowSequenceLength) {
        Field potentiallyXOnWayField;

        int updatedRowRangeEndIndex = rowSequenceRange.get(1);
        boolean indexOk;

        for(int columnEndIndex = rowSequenceRange.get(1); columnEndIndex > rowSequenceRange.get(0) + rowSequenceLength - 1; columnEndIndex--) {
            indexOk = true;
            for(int columnIdx = columnEndIndex; columnIdx > columnEndIndex - rowSequenceLength; columnIdx--) {
                potentiallyXOnWayField = new Field(rowIdx, columnIdx);
                if (isFieldWithX(potentiallyXOnWayField)) {
                    indexOk = false;
                    break;
                }
            }
            if (indexOk) {
                break;
            } else {
                updatedRowRangeEndIndex--;
            }
        }

        return updatedRowRangeEndIndex;
    }

    /**
     * place an "X" on fields outside the rows sequence ranges
     */
    public void placeXsRowAtUnreachableFields(int rowIdx) {

        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        int nonogramWidth = this.getWidth();
        boolean existRangeIncludingColumn;
        List<Integer> fieldAsRange;
        Field fieldToExclude;

        for(int columnIdx = 0; columnIdx < nonogramWidth; columnIdx++) {
            fieldAsRange = List.of(columnIdx, columnIdx);
            existRangeIncludingColumn = rangesListIncludingAnotherRange(rowSequencesRanges, fieldAsRange);

            if (!existRangeIncludingColumn) {
                fieldToExclude = new Field(rowIdx, columnIdx);
                if (isFieldEmpty(fieldToExclude)) {
                    this.placeXAtGivenField(fieldToExclude);
                    this.excludeFieldInRow(fieldToExclude);
                    this.excludeFieldInColumn(fieldToExclude);
                    this.addColumnToAffectedActionsByIdentifiers(columnIdx, ActionsConstants.actionsToDoAfterPlacingXsAtRowsUnreachableFields);

                    tmpLog = generatePlacingXStepDescription(rowIdx, columnIdx, "placing \"X\" at unreachable field");
                    addLog(tmpLog);

                    this.nonogramState.increaseMadeSteps();
                } else if (showRepetitions) {
                    System.out.println("X at unreachable field in row placed earlier!");
                }
            }
        }
    }

    /**
     * @param rowIdx - row index on which try to extend subsequence to minimum possible matching (to left)
     */
    public void extendColouredFieldsToLeftNearXToMaximumPossibleLengthInRow(int rowIdx) {
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);

        List<Integer> possibleSequenceLengths;
        int minimumPossibleLength;
        List<Integer> colouredSubsequenceRange;
        int distanceToX;
        Field potentiallyXField;
        Field potentiallyColouredField;

        Field fieldToColour;

        for(int columnIdx = this.getWidth() - 1; columnIdx >= 0; columnIdx--) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(potentiallyColouredField)) {
                //find range of current coloured sequence (right to left)
                colouredSubsequenceRange = this.findCurrentColouredSequenceRangeRightToLeft(rowIdx, columnIdx);

                //filter sequences that is possibly that coloured sequence is part of them
                possibleSequenceLengths = filterSequencesRangesIncludingAnotherAndReturnCorrespondingLengths (
                        rowSequencesRanges, colouredSubsequenceRange, rowSequencesLengths);

                if (possibleSequenceLengths.isEmpty()) {
                    this.nonogramState.invalidateSolution();
                    break;
                }

                //get minimum length from possible sequences ('worst case', but realistic, to be sure extending is justified)
                minimumPossibleLength = Collections.min(possibleSequenceLengths);

                distanceToX = 0;

                /* find distance to nearest X (from firstColouredFieldColumnIndexInSubsequence it will be minimum:
                 range length , f.e. [4,5] -> rangeLength([4,5]) = 2) */
                for(int columnsToX = rangeLength(colouredSubsequenceRange); colouredSubsequenceRange.get(0) + columnsToX < this.getWidth() && columnsToX < minimumPossibleLength; columnsToX++) {
                    potentiallyXField = new Field(rowIdx, colouredSubsequenceRange.get(0) + columnsToX);
                    if (isFieldWithX(potentiallyXField)) {
                        distanceToX = columnsToX;
                        break;
                    }
                }

                /* there could be situation when 'X' is f.e. too far from coloured sequence(distanceToX not changed), check that case*/
                if (distanceToX != 0) {
                    int mostLeftSequenceToExtendColumnIndex = colouredSubsequenceRange.get(0) + distanceToX - minimumPossibleLength;
                    for(int colourColumnIdx = colouredSubsequenceRange.get(0); colourColumnIdx >= 0 /*tmp cond*/ && colourColumnIdx >= mostLeftSequenceToExtendColumnIndex; colourColumnIdx--) {
                        fieldToColour = new Field(rowIdx, colourColumnIdx);
                        try {
                            if (isFieldEmpty(fieldToColour)) {
                                this.colourFieldAtGivenPosition(fieldToColour, "R---");
                                this.addColumnToAffectedActionsByIdentifiers(colourColumnIdx, ActionsConstants.actionsToDoAfterExtendingColouredFieldsNearXInRows);
                                this.nonogramState.increaseMadeSteps();

                                tmpLog = generateColourStepDescription(rowIdx, colourColumnIdx, "extend coloured fields in sequence to left near X " +
                                        "to length of shortest possible sequence in row");
                                addLog(tmpLog);
                            } else if (showRepetitions) {
                                System.out.println("Row field was coloured earlier (extending to minimum required - to left).");
                            }
                        } catch (IndexOutOfBoundsException e) {
                            this.nonogramState.invalidateSolution();
                        }
                    }
                }
            }
        }
    }

    private List<Integer> findCurrentColouredSequenceRangeRightToLeft(int rowIdx, int columnIdx) {
        Field fieldBeforeColouredToCheck;

        int lastColouredFieldColumnIndexInSubsequence = columnIdx;
        do {
            fieldBeforeColouredToCheck = new Field(rowIdx, --columnIdx);
        } while (columnIdx >= 0 && isFieldColoured(fieldBeforeColouredToCheck));
        int firstColouredFieldColumnIndexInSubsequence = ++columnIdx;

        return List.of(lastColouredFieldColumnIndexInSubsequence, firstColouredFieldColumnIndexInSubsequence);
    }

    /**
     * @param rowIdx - row index on which try to extend subsequence to minimum possible matching (to right)
     */
    public void extendColouredFieldsToRightNearXToMaximumPossibleLengthInRow(int rowIdx) {
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);

        List<Integer> possibleSequenceLengths;
        int minimumPossibleLength;
        List<Integer> colouredSubsequenceRange;
        int distanceFromX;
        Field potentiallyXField;
        Field potentiallyColouredField;

        Field fieldToColour;

        for(int columnIdx = 0; columnIdx < this.getWidth(); columnIdx++) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(potentiallyColouredField)) {
                //find range of current coloured sequence (left to right)
                colouredSubsequenceRange = this.findCurrentColouredSequenceRangeLeftToRight(rowIdx, columnIdx);

                //filter sequences that is possibly that coloured sequence is part of them
                possibleSequenceLengths = filterSequencesRangesIncludingAnotherAndReturnCorrespondingLengths (
                        rowSequencesRanges, colouredSubsequenceRange, rowSequencesLengths);

                if (possibleSequenceLengths.isEmpty()) {
                    this.nonogramState.invalidateSolution();
                    break;
                }

                //get minimum length from possible sequences ('worst case', but realistic, to be sure extending is justified)
                minimumPossibleLength = Collections.min(possibleSequenceLengths);

                distanceFromX = 0;

                /* find distance from nearest X (from colouredSubsequenceRange.get(1) it will be maximum:
                 range length , f.e. [6, 7] -> rangeLength([6, 7]) = 2) -> pos [5, columnIdx] */
                for(int columnsFromX = rangeLength(colouredSubsequenceRange); colouredSubsequenceRange.get(1) - columnsFromX >= 0 && columnsFromX < minimumPossibleLength - 1; columnsFromX++) {
                    potentiallyXField = new Field(rowIdx, colouredSubsequenceRange.get(1) - columnsFromX);
                    if (isFieldWithX(potentiallyXField)) {
                        distanceFromX = columnsFromX;
                        break;
                    }
                }

                /* there could be situation when 'X' is f.e. too far from coloured sequence(distanceFromX not changed), check that case*/
                if (distanceFromX != 0) {
                    int mostRightSequenceToExtendColumnIndex = colouredSubsequenceRange.get(1) - distanceFromX + minimumPossibleLength;
                    for(int colourColumnIdx = colouredSubsequenceRange.get(1) + 1; colourColumnIdx < this.getWidth() /*tmp cond*/ && colourColumnIdx <= mostRightSequenceToExtendColumnIndex; colourColumnIdx++) {
                        try {
                            fieldToColour = new Field(rowIdx, colourColumnIdx);
                            if (isFieldEmpty(fieldToColour)) {
                                this.colourFieldAtGivenPosition(fieldToColour, "R---");
                                this.addColumnToAffectedActionsByIdentifiers(colourColumnIdx, ActionsConstants.actionsToDoAfterExtendingColouredFieldsNearXInRows);
                                this.nonogramState.increaseMadeSteps();

                                tmpLog = generateColourStepDescription(rowIdx, colourColumnIdx, "extend coloured fields in sequence to right near X " +
                                        "to length of shortest possible sequence in row");
                                addLog(tmpLog);

                            } else if (showRepetitions) {
                                System.out.println("Row field was coloured earlier (extending to minimum required - to right).");
                            }
                        } catch (IndexOutOfBoundsException e) {
                            this.nonogramState.invalidateSolution();
                        }
                    }
                }
            }
        }
    }

    private List<Integer> findCurrentColouredSequenceRangeLeftToRight(int rowIdx, int columnIdx) {
        Field fieldBeforeColouredToCheck;

        int firstColouredFieldColumnIndexInSubsequence = columnIdx;
        do {
            fieldBeforeColouredToCheck = new Field(rowIdx, ++columnIdx);
        } while (columnIdx < this.getWidth() - 1 && isFieldColoured(fieldBeforeColouredToCheck));
        int lastColouredFieldColumnIndexInSubsequence = --columnIdx;

        return List.of(firstColouredFieldColumnIndexInSubsequence, lastColouredFieldColumnIndexInSubsequence);
    }

    private boolean isColumnRangeColoured(int rowIdx, List<Integer> columnRange) {
        Field potentiallyColouredField;
        for(Integer columnIdx : columnRange) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (!isFieldColoured(potentiallyColouredField)) {
                return false;
            }
        }

        return true;
    }

    public int minimumColumnIndexWithoutX(int rowIdx, int lastSequenceColumnIdx, int sequenceFullLength) {
        int minimumColumnIndex = lastSequenceColumnIdx;
        int minimumColumnIndexLimit = Math.max(lastSequenceColumnIdx - sequenceFullLength + 1, 0);
        Field fieldToCheck;

        for(; minimumColumnIndex >= minimumColumnIndexLimit; minimumColumnIndex--) {
            fieldToCheck = new Field(rowIdx, minimumColumnIndex);
            if (isFieldWithX(fieldToCheck)) {
                break;
            }
        }

        return minimumColumnIndex + 1;
    }

    public int maximumColumnIndexWithoutX(int rowIdx, int firstSequenceColumnIdx, int sequenceFullLength) {
        int maximumColumnIndex = firstSequenceColumnIdx;
        int maximumColumnIndexLimit = Math.min(firstSequenceColumnIdx + sequenceFullLength - 1, this.getWidth() - 1);
        Field fieldToCheck;

        for(; maximumColumnIndex <= maximumColumnIndexLimit; maximumColumnIndex++) {
            fieldToCheck = new Field(rowIdx, maximumColumnIndex);
            if (isFieldWithX(fieldToCheck)) {
                break;
            }
        }

        return maximumColumnIndex - 1;
    }

    /**
     * @param rowIdx row to mark sequence with its identifier (1st sequence -> "b", 2nd sequence -> "c", etc.)
     * @param colIdx column of field to mark with row sequence identifier
     * @param marker row sequence marker/identifier
     */
    public void markRowBoardField(int rowIdx, int colIdx, String marker) {
        String oldRowField = this.nonogramSolutionBoardWithMarks.get(rowIdx).get(colIdx);
        this.nonogramSolutionBoardWithMarks.get(rowIdx).set(colIdx, "R" + marker + oldRowField.substring(2, 4));
    }

    /**
     * @param rowIndex - row index to change one of its sequences range
     * @param sequenceIndex - sequence index to change range
     * @param updatedRange - row sequence updatedRange
     */
    public void changeRowSequenceRange(int rowIndex, int sequenceIndex, List<Integer> updatedRange) {
        this.rowsSequencesRanges.get(rowIndex).set(sequenceIndex, updatedRange);
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
}
