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

import static com.puzzlesolverappbackend.puzzleAppFileManager.payload.ActionsConstants.actionsToDoAfterColouringOverlappingSequencesInColumns;
import static com.puzzlesolverappbackend.puzzleAppFileManager.payload.ActionsConstants.actionsToDoAfterCorrectingRangesWhenMarkingSequencesInColumns;
import static com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.NonogramConstants.EMPTY_FIELD;
import static com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.NonogramConstants.MARKED_COLUMN_INDICATOR;
import static com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.NonogramLogicService.*;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
public class NonogramColumnLogic extends NonogramLogicParams {

    private final static String CORRECT_COLUMN_SEQ_RANGE_MARKING_FIELD = "correcting column sequence range when marking field";

    private final static String FILL_OVERLAPPING_FIELDS = "fill overlapping fields";

    public NonogramColumnLogic(NonogramLogic nonogramLogic) {
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
     * @param columnIdx - column index on which mark fields with char sequences identifiers
     */
    public void markAvailableFieldsInColumn(int columnIdx) {
        Field potentiallyColouredField;
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

        List<Integer> onlyMatchingSequenceOldRange;
        List<Integer> newSequenceRange;

        for(int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(potentiallyColouredField)) {

                colouredSequenceIndexes = new ArrayList<>();
                colouredSequenceIndexes.add(rowIdx);

                // TODO - do while(?)
                while(rowIdx < this.getHeight() && isFieldColoured(potentiallyColouredField)) {
                    rowIdx++;
                    potentiallyColouredField = new Field(rowIdx, columnIdx);
                }

                colouredSequenceIndexes.add(rowIdx - 1);

                firstSequenceIndex = colouredSequenceIndexes.get(0);
                lastSequenceIndex = colouredSequenceIndexes.get(1);
                colouredSequenceLength = lastSequenceIndex - firstSequenceIndex  + 1;

                matchingSequencesCount = 0;

                for(int seqNo = 0; seqNo < columnSequences.size(); seqNo++) {
                    columnSequenceRange = columnSequencesRanges.get(seqNo);

                    if ( rangeInsideAnotherRange(colouredSequenceIndexes, columnSequenceRange)
                            && colouredSequenceLength <= columnSequences.get(seqNo)) {
                        matchingSequencesCount++;
                        lastMatchingSequenceIndex = seqNo;
                    }
                }

                if (matchingSequencesCount == 1) {

                    sequenceMarker = NonogramLogic.indexToSequenceCharMark(lastMatchingSequenceIndex);
                    for(int sequenceRowIdx = firstSequenceIndex; sequenceRowIdx <= lastSequenceIndex; sequenceRowIdx++) {
                        if (this.getNonogramSolutionBoardWithMarks().get(sequenceRowIdx).get(columnIdx).substring(3).equals(EMPTY_FIELD)) {
                            this.markColumnBoardField(sequenceRowIdx, columnIdx, sequenceMarker);
                            this.nonogramState.increaseMadeSteps();
                        } else if (this.showRepetitions) {
                            System.out.println("Column field was marked earlier.");
                        }
                    }

                    //correct sequence range if new range is shorter
                    onlyMatchingSequenceOldRange = columnSequencesRanges.get(lastMatchingSequenceIndex);
                    newSequenceRange = calculateNewRangeFromParameters(colouredSequenceIndexes, columnSequences.get(lastMatchingSequenceIndex));
                    if (rangeLength(newSequenceRange) < rangeLength(onlyMatchingSequenceOldRange)) {
                        this.changeColumnSequenceRange(columnIdx, lastMatchingSequenceIndex, newSequenceRange);
                        tmpLog = generateCorrectingColumnSequenceRangeStepDescription(columnIdx, lastMatchingSequenceIndex, onlyMatchingSequenceOldRange, newSequenceRange, CORRECT_COLUMN_SEQ_RANGE_MARKING_FIELD);
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
     * fill fields in column where ranges met specific condition (range_length < sequence_length * 2)
     */
    public void fillOverlappingFieldsInColumn (int columnIdx) {
        List<Integer> sequencesInColumnLengths = this.getColumnsSequences().get(columnIdx);
        List<List<Integer>> sequencesInColumnRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> range;

        int sequenceLength;
        int rangeBeginIndex;
        int rangeEndIndex;
        List<Integer> colouringRange;
        Field fieldToColour;

        for(int sequenceIdx = 0; sequenceIdx < sequencesInColumnLengths.size(); sequenceIdx++) {
            sequenceLength = sequencesInColumnLengths.get(sequenceIdx);
            range = sequencesInColumnRanges.get(sequenceIdx);
            rangeBeginIndex = range.get(0);
            rangeEndIndex = range.get(1);

            colouringRange = IntStream.rangeClosed(rangeEndIndex - sequenceLength + 1, rangeBeginIndex + sequenceLength - 1)
                    .boxed()
                    .toList();

            for (int rowIdx : colouringRange) {
                fieldToColour = new Field(rowIdx, columnIdx);

                if (isFieldEmpty(fieldToColour)) {
                    this.colourFieldAtGivenPosition(fieldToColour, "--C-");
                    tmpLog = generateColourStepDescription(columnIdx, rowIdx, FILL_OVERLAPPING_FIELDS);
                    addLog(tmpLog);
                    this.addRowToAffectedActionsByIdentifiers(rowIdx, actionsToDoAfterColouringOverlappingSequencesInColumns);
                    this.getNonogramState().increaseMadeSteps();
                } else if (showRepetitions) {
                    logger.warn("Column field was coloured earlier!");
                }

                if (rangeLength(colouringRange) == columnsSequences.get(columnIdx).get(sequenceIdx)) {
                    this.excludeSequenceInColumn(columnIdx, sequenceIdx);
                }
            }
        }
    }

    /**
     * @param columnIdx - column index on which place "X" around coloured fields
     */
    public void placeXsAroundLongestSequencesInColumn(int columnIdx) {
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);

        List<Integer> colouredSequenceRange;
        List<Integer> columnSequenceRange;
        int sequenceOnBoardLength;

        List<Integer> columnSequencesIndexesIncludingSequenceRange;
        List<Integer> columnSequencesLengthsIncludingSequenceRange;

        List<Integer> xAtEdges;
        Field potentiallyColouredField;

        int sequenceIdxToExclude;

        List<Integer> updatedSequenceRange;

        for(int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(potentiallyColouredField)) {
                colouredSequenceRange = findColouredSequenceRangeInColumn(rowIdx, columnIdx);
                rowIdx = colouredSequenceRange.get(1); //start from end of current coloured sequence

                sequenceOnBoardLength = rangeLength(colouredSequenceRange);
                columnSequencesIndexesIncludingSequenceRange = new ArrayList<>();
                columnSequencesLengthsIncludingSequenceRange = new ArrayList<>();

                List<Integer> columnSequencesIdsNotToInclude = this.getColumnsSequencesIdsNotToInclude().get(columnIdx);
                for(int seqNo = 0; seqNo < columnSequencesRanges.size(); seqNo++) {
                    columnSequenceRange = columnSequencesRanges.get(seqNo);
                    if (rangeInsideAnotherRange(colouredSequenceRange, columnSequenceRange) && !columnSequencesIdsNotToInclude.contains(seqNo)) {
                        columnSequencesIndexesIncludingSequenceRange.add(seqNo);
                        columnSequencesLengthsIncludingSequenceRange.add(columnSequencesLengths.get(seqNo));
                    }
                }

                xAtEdges = new ArrayList<>(Arrays.asList(colouredSequenceRange.get(0) - 1, colouredSequenceRange.get(1) + 1));

                /* given coloured sequence can be part of only one column sequence
                AND given coloured sequence has exactly same length as only matching column range */
                if (columnSequencesIndexesIncludingSequenceRange.size() == 1 && sequenceOnBoardLength == columnSequencesLengthsIncludingSequenceRange.get(0)) {
                    doStuffWhenPlacingXsAroundLongestSequence(columnIdx, xAtEdges, true);

                    updatedSequenceRange = IntStream.rangeClosed(xAtEdges.get(0) + 1, xAtEdges.get(1) - 1).boxed().toList();
                    excludeColouredFieldsBetweenXs(columnIdx, updatedSequenceRange);
                    sequenceIdxToExclude = columnSequencesIndexesIncludingSequenceRange.get(0);
                    updateLogicAfterPlacingXsAroundOnlyOneMatchingSequenceInColumn(columnIdx, sequenceIdxToExclude, updatedSequenceRange);
                } else if (columnSequencesIndexesIncludingSequenceRange.size() > 1 && sequenceOnBoardLength == Collections.max(columnSequencesLengthsIncludingSequenceRange)) {
                    /*more than one column sequence fit in coloured range & length of coloured sequence is a max length of possible matching column sequences lengths */
                    doStuffWhenPlacingXsAroundLongestSequence(columnIdx, xAtEdges, false);
                }
            }
        }
    }

    private List<Integer> findColouredSequenceRangeInColumn(int startRowIdx, int columnIdx) {
        int rowIdx = startRowIdx;

        List<Integer> colouredSequenceRange = new ArrayList<>();
        colouredSequenceRange.add(rowIdx);
        while(rowIdx < this.getHeight() && isFieldColoured(new Field(rowIdx, columnIdx))) {
            rowIdx++;
        }
        colouredSequenceRange.add(rowIdx - 1);

        return colouredSequenceRange;
    }

    private void doStuffWhenPlacingXsAroundLongestSequence(int columnIdx, List<Integer> xAtEdges, boolean onlyMatching) {
        String logEndPart = onlyMatching ? "[only possible]" : "[sequence index not specified]";
        for(int currentRowIndex : xAtEdges) {
            Field fieldAtEdge = new Field(currentRowIndex, columnIdx);
            if (isRowIndexValid(currentRowIndex)) {
                if (isFieldEmpty(fieldAtEdge)) {
                    doStuffWhenPlacingXsAfterOrBeforeLongestSequence(columnIdx, currentRowIndex, xAtEdges, logEndPart);
                } else if (showRepetitions) {
                    log.warn("X around longest sequence(pos={}) in column added earlier! {}", fieldAtEdge, logEndPart);
                }
            }
        }
    }

    private void doStuffWhenPlacingXsAfterOrBeforeLongestSequence(int columnIdx, int currentRowIndex, List<Integer> xAtEdges, String logEndPart) {
        if (isRowIndexValid(currentRowIndex)) {
            addLogWhenPlacingXAroundLongestSequence(columnIdx, currentRowIndex, xAtEdges, logEndPart);

            Field fieldToExclude = new Field(currentRowIndex, columnIdx);
            this.placeXAtGivenField(fieldToExclude);
            this.excludeFieldInRow(fieldToExclude);
            this.excludeFieldInColumn(fieldToExclude);

            this.addRowToAffectedActionsByIdentifiers(currentRowIndex, ActionsConstants.actionsToDoAfterPlacingXsAroundLongestSequencesInColumns);
            this.addColumnToAffectedActionsByIdentifiers(columnIdx, ActionsConstants.actionsToDoAfterPlacingXsAroundLongestSequencesInColumns);

            this.nonogramState.increaseMadeSteps();
        }
    }

    private void addLogWhenPlacingXAroundLongestSequence(int columnIdx, int currentRowIndex, List<Integer> xAtEdges, String logEndPart) {
        String logPart;
        if (currentRowIndex == xAtEdges.get(0)) {
            logPart = "placing \"X\" before longest sequence in column " + logEndPart;
        } else {
            logPart = "placing \"X\" after longest sequence in column " + logEndPart;
        }

        tmpLog = generatePlacingXStepDescription(columnIdx, currentRowIndex, logPart);
        addLog(tmpLog);
    }

    private void excludeColouredFieldsBetweenXs(int columnIdx, List<Integer> updatedSequenceRange) {
        for(int sequenceRowIdx : updatedSequenceRange) {
            this.excludeFieldInColumn(new Field(sequenceRowIdx, columnIdx));
            this.nonogramState.increaseMadeSteps();
        }
    }

    private void updateLogicAfterPlacingXsAroundOnlyOneMatchingSequenceInColumn(int columnIdx, int columnSequenceToExclude, List<Integer> updatedSequenceRange) {
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> oldSequenceRange = columnSequencesRanges.get(columnSequenceToExclude);
        this.changeColumnSequenceRange(columnIdx, columnSequenceToExclude, updatedSequenceRange);
        tmpLog = generateCorrectingColumnSequenceRangeStepDescription(columnIdx, columnSequenceToExclude,
                oldSequenceRange, updatedSequenceRange, "correcting sequence while placing X before only matching coloured sequence");
        addLog(tmpLog);
        this.excludeSequenceInColumn(columnIdx, columnSequenceToExclude);
    }

    /**
     * place an "X" at too short empty fields sequences in column, when none of row sequences can fit in hole
     */
    public void placeXsColumnAtTooShortEmptySequences(int columnIdx) {

        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequenceRange;

        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);
        int columnSequenceLength;

        Field potentiallyXPlacedField;
        Field fieldAfterXToCheck;

        List<Integer> sequencesWhichNotFit;
        List<Integer> sequencesWithinRange;

        int firstXIndex;
        int lastXIndex;
        int emptyFieldsSequenceLength;
        List<Integer> emptyFieldsRange;
        Field fieldToExclude;

        boolean onlyEmptyFieldsInFieldsSequence;

        for(int rowIdx = 1; rowIdx < this.getHeight() - 1; rowIdx++) {
            onlyEmptyFieldsInFieldsSequence = true;
            potentiallyXPlacedField = new Field(rowIdx, columnIdx);
            if (isFieldWithX(potentiallyXPlacedField)) {

                sequencesWhichNotFit = new ArrayList<>();
                sequencesWithinRange = new ArrayList<>();

                firstXIndex = rowIdx;
                fieldAfterXToCheck = new Field(++rowIdx, columnIdx);
                while(rowIdx < this.getHeight() && !isFieldWithX((fieldAfterXToCheck))) {
                    if (isFieldEmpty(fieldAfterXToCheck)) {
                        fieldAfterXToCheck = new Field(++rowIdx, columnIdx);
                    } else if (isFieldColoured(fieldAfterXToCheck)) {
                        onlyEmptyFieldsInFieldsSequence = false;
                        break;
                    }
                }

                if (onlyEmptyFieldsInFieldsSequence) {
                    lastXIndex = rowIdx;

                    emptyFieldsRange = createRangeFromTwoIntegers(firstXIndex + 1, lastXIndex - 1);
                    emptyFieldsSequenceLength = rangeLength(emptyFieldsRange);

                    for(int seqNo = 0; seqNo < columnSequencesLengths.size(); seqNo++) {
                        columnSequenceRange = columnSequencesRanges.get(seqNo);
                        columnSequenceLength = columnSequencesLengths.get(seqNo);

                        if (rangeInsideAnotherRange(emptyFieldsRange, columnSequenceRange) || emptyFieldsSequenceLength > columnSequenceLength) {
                            sequencesWithinRange.add(seqNo);
                            if (emptyFieldsSequenceLength < columnSequenceLength) {
                                sequencesWhichNotFit.add(seqNo);
                            }
                        }
                    }

                    // if there is not any sequence with length equal or less than emptyFieldSequenceLength
                    if (sequencesWhichNotFit.size() == sequencesWithinRange.size() && emptyFieldsSequenceLength > 0) {
                        for(int emptyFieldRowIdx = emptyFieldsRange.get(0); emptyFieldRowIdx <= emptyFieldsRange.get(1); emptyFieldRowIdx++) {
                            fieldToExclude = new Field(emptyFieldRowIdx, columnIdx);
                            if (isFieldEmpty(fieldToExclude)) {
                                this.placeXAtGivenField(fieldToExclude);
                                this.excludeFieldInRow(fieldToExclude);
                                this.excludeFieldInColumn(fieldToExclude);
                                this.addRowToAffectedActionsByIdentifiers(emptyFieldRowIdx, ActionsConstants.actionsToDoAfterPlacingXsAtTooShortSequencesInColumns);

                                tmpLog = generatePlacingXStepDescription(columnIdx, emptyFieldRowIdx, "placing \"X\" inside too short empty fields sequence");
                                addLog(tmpLog);

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

    public void correctColumnSequencesRanges(int columnIdx) {
        correctSequencesRangesInColumnFromTop(columnIdx);
        correctSequencesRangesInColumnFromBottom(columnIdx);
    }

    private void correctSequencesRangesInColumnFromTop(int columnIdx) {
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

        for(int sequenceIdx = 0; sequenceIdx < columnSequencesRanges.size() - 1; sequenceIdx++) {
            nextSequenceId = sequenceIdx + 1;
            if (columnSequencesIdsNotToInclude.contains(sequenceIdx) && !columnSequencesIdsNotToInclude.contains(nextSequenceId)) {
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

                correctColumnRangeFromTop(columnIdx, columnSequencesLengths, nextSequenceRange, updatedStartIndex, nextSequenceOldBeginRangeRowIndex, nextSequenceId, updatedNextSequenceRange);
            }  else if (!columnSequencesIdsNotToInclude.contains(sequenceIdx) && !columnSequencesIdsNotToInclude.contains(nextSequenceId)) {
                currentSequenceRange = columnSequencesRanges.get(sequenceIdx);
                nextSequenceRange = columnSequencesRanges.get(nextSequenceId);

                updatedNextSequenceBeginRangeRowIndex = currentSequenceRange.get(0) + columnSequencesLengths.get(sequenceIdx) + 1;

                nextSequenceOldBeginRangeRowIndex = nextSequenceRange.get(0);

                updatedNextSequenceRange = new ArrayList<>();

                updatedStartIndex = Math.max(nextSequenceOldBeginRangeRowIndex, updatedNextSequenceBeginRangeRowIndex);
                updatedNextSequenceRange.add(updatedStartIndex);
                updatedNextSequenceRange.add(nextSequenceRange.get(1));

                correctColumnRangeFromTop(columnIdx, columnSequencesLengths, nextSequenceRange, updatedStartIndex, nextSequenceOldBeginRangeRowIndex, nextSequenceId, updatedNextSequenceRange);
            }
        }
    }

    private void correctColumnRangeFromTop(int columnIdx,
                                           List<Integer> columnSequencesLengths,
                                           List<Integer> nextSequenceRange,
                                           int updatedStartIndex,
                                           int nextSequenceOldBeginRangeRowIndex,
                                           int nextSequenceId,
                                           List<Integer> updatedNextSequenceRange) {
        if (updatedStartIndex != nextSequenceOldBeginRangeRowIndex) {
            this.updateColumnSequenceRange(columnIdx, nextSequenceId, updatedNextSequenceRange);
            this.addColumnToAffectedActionsByIdentifiers(columnIdx, ActionsConstants.actionsToDoAfterCorrectingColumnsSequences);
            this.nonogramState.increaseMadeSteps();
            tmpLog = generateCorrectingColumnSequenceRangeStepDescription(columnIdx, nextSequenceId, nextSequenceRange, updatedNextSequenceRange, "correcting from top");
            addLog(tmpLog);

            if (rangeLength(updatedNextSequenceRange) == columnSequencesLengths.get(nextSequenceId) && isRowRangeColoured(columnIdx, updatedNextSequenceRange)) {
                this.excludeSequenceInColumn(columnIdx, nextSequenceId);
            }
        }
    }

    private void correctSequencesRangesInColumnFromBottom(int columnIdx) {

        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnFieldsNotToInclude = this.getColumnsFieldsNotToInclude().get(columnIdx);

        List<Integer> fullSequenceRange;
        List<Integer> currentSequenceRange;
        List<Integer> oldPreviousSequenceRange;
        int previousSequenceUpdatedRangeEndRowIndex;
        int updatedEndIndex;
        int previousSequenceOldRangeEndRowIndex;
        int previousSequenceId;
        List<Integer> updatedPreviousSequenceRange;

        List<Integer> columnSequencesIdsNotToInclude = this.getColumnsSequencesIdsNotToInclude().get(columnIdx);

        int currentSequenceLength;

        for(int sequenceIdx = columnSequencesRanges.size() - 1; sequenceIdx > 0; sequenceIdx--) {
            previousSequenceId = sequenceIdx - 1;
            if (columnSequencesIdsNotToInclude.contains(sequenceIdx) && !columnSequencesIdsNotToInclude.contains(previousSequenceId)) {
                fullSequenceRange = columnSequencesRanges.get(sequenceIdx);

                oldPreviousSequenceRange = columnSequencesRanges.get(previousSequenceId);
                previousSequenceUpdatedRangeEndRowIndex = fullSequenceRange.get(0) - 2;

                while(columnFieldsNotToInclude.contains(previousSequenceUpdatedRangeEndRowIndex)) {
                    previousSequenceUpdatedRangeEndRowIndex--;
                }

                previousSequenceOldRangeEndRowIndex = oldPreviousSequenceRange.get(1);

                updatedPreviousSequenceRange = new ArrayList<>();

                updatedEndIndex = Math.min(previousSequenceOldRangeEndRowIndex, previousSequenceUpdatedRangeEndRowIndex);

                updatedPreviousSequenceRange.add(oldPreviousSequenceRange.get(0));
                updatedPreviousSequenceRange.add(updatedEndIndex);

                correctColumnRangeFromBottom(columnIdx,
                        columnSequencesLengths,
                        oldPreviousSequenceRange,
                        updatedEndIndex,
                        previousSequenceOldRangeEndRowIndex,
                        previousSequenceId,
                        updatedPreviousSequenceRange);
            } else if (!columnSequencesIdsNotToInclude.contains(sequenceIdx) && !columnSequencesIdsNotToInclude.contains(previousSequenceId)) {
                currentSequenceLength = columnSequencesLengths.get(sequenceIdx);
                currentSequenceRange = columnSequencesRanges.get(sequenceIdx);
                oldPreviousSequenceRange = columnSequencesRanges.get(previousSequenceId);

                previousSequenceUpdatedRangeEndRowIndex = currentSequenceRange.get(1) - currentSequenceLength - 1;

                previousSequenceOldRangeEndRowIndex = oldPreviousSequenceRange.get(1);

                updatedPreviousSequenceRange = new ArrayList<>();

                updatedEndIndex = Math.min(previousSequenceOldRangeEndRowIndex, previousSequenceUpdatedRangeEndRowIndex);

                updatedPreviousSequenceRange.add(oldPreviousSequenceRange.get(0));
                updatedPreviousSequenceRange.add(updatedEndIndex);

                correctColumnRangeFromBottom(columnIdx,
                        columnSequencesLengths,
                        oldPreviousSequenceRange,
                        updatedEndIndex,
                        previousSequenceOldRangeEndRowIndex,
                        previousSequenceId,
                        updatedPreviousSequenceRange);
            }
        }
    }

    private void correctColumnRangeFromBottom(int columnIdx,
                                              List<Integer> columnSequencesLengths,
                                              List<Integer> oldPreviousSequenceRange,
                                              int updatedEndIndex,
                                              int previousSequenceOldRangeEndRowIndex,
                                              int previousSequenceId,
                                              List<Integer> updatedPreviousSequenceRange) {
        if (updatedEndIndex != previousSequenceOldRangeEndRowIndex) {
            this.getColumnsSequencesRanges().get(columnIdx).set(previousSequenceId, updatedPreviousSequenceRange);
            this.addColumnToAffectedActionsByIdentifiers(columnIdx, ActionsConstants.actionsToDoAfterCorrectingColumnsSequences);

            if (rangeLength(updatedPreviousSequenceRange) == columnSequencesLengths.get(previousSequenceId) && isRowRangeColoured(columnIdx, updatedPreviousSequenceRange)) {
                this.excludeSequenceInColumn(columnIdx, previousSequenceId);
            }

            this.nonogramState.increaseMadeSteps();
            tmpLog = generateCorrectingColumnSequenceRangeStepDescription(columnIdx, previousSequenceId, oldPreviousSequenceRange, updatedPreviousSequenceRange, "correcting from right");
            addLog(tmpLog);
        }
    }

    /**
     * corrects column sequences when met coloured field in column (also from top and bottom)
     * @param columnIdx - column index to correct sequences ranges when coloured field met
     */
    public void correctColumnSequencesWhenMetColouredField (int columnIdx) {
        correctColumnSequencesRangesWhenMetColouredFieldFromTop(columnIdx);
        correctColumnSequencesRangesWhenMetColouredFieldFromBottom(columnIdx);
    }

    /**
     * corrects column sequences when met coloured field in row (from top)
     * @param columnIdx - column index to correct sequences ranges when coloured field met (from top)
     */
    public void correctColumnSequencesRangesWhenMetColouredFieldFromTop(int columnIdx) {
        boolean columnSequencesRangesChanged = false;

        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);
        int columnSequenceRangeStart;
        int columnSequenceRangeEnd;
        List<Integer> oldSequenceRange;
        List<Integer> updatedSequenceRange;

        Field potentiallyColouredField;

        int updatedRangeEndIndex;
        int maximumPossibleSequenceRangeEnd;
        int sequenceId = 0;
        int sequenceLength = columnSequencesLengths.get(sequenceId);

        for(int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(potentiallyColouredField)) {
                oldSequenceRange = columnSequencesRanges.get(sequenceId);
                columnSequenceRangeStart = oldSequenceRange.get(0);
                columnSequenceRangeEnd = oldSequenceRange.get(1);
                maximumPossibleSequenceRangeEnd = rowIdx + sequenceLength - 1;

                updatedRangeEndIndex = Math.min(columnSequenceRangeEnd, maximumPossibleSequenceRangeEnd);
                updatedSequenceRange = new ArrayList<>(Arrays.asList(columnSequenceRangeStart, updatedRangeEndIndex));

                if (updatedRangeEndIndex != columnSequenceRangeEnd) {
                    columnSequencesRangesChanged = true;
                    this.getColumnsSequencesRanges().get(columnIdx).set(sequenceId, updatedSequenceRange);
//                    if (rangeLength(updatedRange) == rowSequencesLengths.get(sequenceId) && isRowRangeColoured(columnIdx, updatedRange)) {
//                        this.excludeSequenceInColumn(columnIdx, sequenceId);
//                    }
                    tmpLog = generateCorrectingColumnSequenceRangeStepDescription(columnIdx, sequenceId,
                            oldSequenceRange, updatedSequenceRange, "correcting from top (met coloured field).");
                    addLog(tmpLog);
                }

                rowIdx = rowIdx + sequenceLength;
                sequenceId++;
                if (sequenceId < columnSequencesLengths.size()) {
                    sequenceLength = columnSequencesLengths.get(sequenceId);
                } else {
                    break;
                }
            }
        }

        if (columnSequencesRangesChanged) {
            this.nonogramState.increaseMadeSteps();
            this.addColumnToAffectedActionsByIdentifiers(columnIdx, ActionsConstants.actionsToDoAfterCorrectingColumnsSequencesWhenMetColouredField);
        }
    }

    /**
     * corrects column sequences when met coloured field in column (from bottom)
     * @param columnIdx - column index to correct sequences ranges when coloured field met (from bottom)
     */
    public void correctColumnSequencesRangesWhenMetColouredFieldFromBottom(int columnIdx) {
        boolean columnSequencesRangesChanged = false;

        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);
        int columnSequenceRangeStart;
        int columnSequenceRangeEnd;
        List<Integer> oldSequenceRange;
        List<Integer> updatedSequenceRange;

        Field potentiallyColouredField;

        int sequenceId = columnSequencesLengths.size() - 1;
        int sequenceLength = columnSequencesLengths.get(sequenceId);
        int updatedRangeStartIndex;
        int minimumPossibleSequenceRangeStart;

        for(int rowIdx = this.getHeight() - 1; rowIdx >= 0 ; rowIdx--) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(potentiallyColouredField)) {
                oldSequenceRange = columnSequencesRanges.get(sequenceId);
                columnSequenceRangeStart = oldSequenceRange.get(0);
                columnSequenceRangeEnd = oldSequenceRange.get(1);
                minimumPossibleSequenceRangeStart = rowIdx - sequenceLength + 1;

                updatedRangeStartIndex = Math.max(columnSequenceRangeStart, minimumPossibleSequenceRangeStart);
                updatedSequenceRange = new ArrayList<>(Arrays.asList(updatedRangeStartIndex, columnSequenceRangeEnd));
                if (updatedRangeStartIndex != columnSequenceRangeStart) {
                    columnSequencesRangesChanged = true;
                    this.updateColumnSequenceRange(columnIdx, sequenceId, updatedSequenceRange);
                    if (rangeLength(columnSequencesRanges.get(sequenceId)) == sequenceLength && isRowRangeColoured(columnIdx, columnSequencesRanges.get(sequenceId))) {
                        this.excludeSequenceInColumn(columnIdx, sequenceId);
                    }

                    tmpLog = generateCorrectingColumnSequenceRangeStepDescription(columnIdx, sequenceId,
                            oldSequenceRange, updatedSequenceRange, "correcting from bottom (met coloured field).");
                    addLog(tmpLog);
                }

                rowIdx = rowIdx - sequenceLength;
                sequenceId--;
                if (sequenceId >= 0) {
                    sequenceLength = columnSequencesLengths.get(sequenceId);
                } else {
                    break;
                }
            }
        }

        if (columnSequencesRangesChanged) {
            this.nonogramState.increaseMadeSteps();
            this.addColumnToAffectedActionsByIdentifiers(columnIdx, ActionsConstants.actionsToDoAfterCorrectingColumnsSequencesWhenMetColouredField);
        }
    }

    /**
     * @param columnIdx - column to correct sequence/s range/s if x on way (sequence won't fit)
     */
    public void correctColumnRangeIndexesIfXOnWay(int columnIdx) {

        boolean columnSequencesRangesChanged = false;

        List<Integer> columnSequences = this.getColumnsSequences().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> excludedColumnSequences = this.getColumnsSequencesIdsNotToInclude().get(columnIdx);
        int columnSequenceLength;
        List<Integer> columnSequenceRange;

        int updatedColumnSequenceRangeStartIndex;
        int updatedColumnSequenceRangeEndIndex;
        List<Integer> updatedRange;

        for(int seqNo = 0; seqNo < columnSequencesRanges.size(); seqNo++) {
            if (!excludedColumnSequences.contains(seqNo)) {

                columnSequenceRange = columnSequencesRanges.get(seqNo);
                columnSequenceLength = columnSequences.get(seqNo);

                updatedColumnSequenceRangeStartIndex = getUpdatedColumnRangeStartIndexBecauseXOnWay(columnIdx, columnSequenceRange, columnSequenceLength);
                updatedColumnSequenceRangeEndIndex = getUpdatedColumnRangeEndIndexBecauseXOnWay(columnIdx, columnSequenceRange, columnSequenceLength);
                updatedRange = new ArrayList<>(Arrays.asList(updatedColumnSequenceRangeStartIndex, updatedColumnSequenceRangeEndIndex));

                if (!NonogramParametersComparatorHelper.rangesEqual(columnSequenceRange, updatedRange)) {
                    columnSequencesRangesChanged = true;
                    tmpLog = generateCorrectingColumnSequenceRangeStepDescription(columnIdx, seqNo, columnSequenceRange, updatedRange, "\"X\" on way");
                    addLog(tmpLog);
                    this.changeColumnSequenceRange(columnIdx, seqNo, updatedRange);
                    if (rangeLength(updatedRange) == this.getColumnsSequences().get(columnIdx).get(seqNo) && isRowRangeColoured(columnIdx, updatedRange)) {
                        this.excludeSequenceInColumn(columnIdx, seqNo);
                    }
                }
            }
        }

        if (columnSequencesRangesChanged) {
            this.nonogramState.increaseMadeSteps();
            this.addColumnToAffectedActionsByIdentifiers(columnIdx, ActionsConstants.actionsToDoAfterCorrectingColumnsSequencesIfXOnWay);
        }
    }

    private int getUpdatedColumnRangeStartIndexBecauseXOnWay(int columnIdx,
                                                List<Integer> columnSequenceRange,
                                                int columnSequenceLength) {
        Field potentiallyXOnWayField;

        int updatedColumnSequenceRangeStartIndex = columnSequenceRange.get(0);
        boolean indexOk;

        for(int rowStartIndex = columnSequenceRange.get(0); rowStartIndex < columnSequenceRange.get(1) - columnSequenceLength + 1; rowStartIndex++) {
            indexOk = true;
            for(int rowIdx = rowStartIndex; rowIdx < rowStartIndex + columnSequenceLength; rowIdx++) {
                potentiallyXOnWayField = new Field(rowIdx, columnIdx);
                if (isFieldWithX(potentiallyXOnWayField)) {
                    indexOk = false;
                    break;
                }
            }
            if (indexOk) {
                break;
            } else {
                updatedColumnSequenceRangeStartIndex++;
            }
        }

        return updatedColumnSequenceRangeStartIndex;
    }

    private int getUpdatedColumnRangeEndIndexBecauseXOnWay(int columnIdx,
                                              List<Integer> columnSequenceRange,
                                              int columnSequenceLength) {
        Field potentiallyXOnWayField;

        int updatedColumnSequenceRangeEndIndex = columnSequenceRange.get(1);
        boolean indexOk;

        for(int rowEndIndex = columnSequenceRange.get(1); rowEndIndex > columnSequenceRange.get(0) + columnSequenceLength - 1; rowEndIndex--) {
            indexOk = true;
            for(int rowIdx = rowEndIndex; rowIdx > rowEndIndex - columnSequenceLength; rowIdx--) {
                potentiallyXOnWayField = new Field(rowIdx, columnIdx);
                if (isFieldWithX(potentiallyXOnWayField)) {
                    indexOk = false;
                    break;
                }
            }
            if (indexOk) {
                break;
            } else {
                updatedColumnSequenceRangeEndIndex--;
            }
        }

        return  updatedColumnSequenceRangeEndIndex;
    }

    /**
     * place an "X" on fields outside the columns sequence ranges
     */
    public void placeXsColumnAtUnreachableFields(int columnIdx) {

        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        int nonogramHeight = this.getHeight();
        boolean existRangeIncludingRow;
        List<Integer> fieldAsRange;
        Field fieldToExclude;

        for(int rowIdx = 0; rowIdx < nonogramHeight; rowIdx++) {
            fieldAsRange = List.of(rowIdx, rowIdx);
            existRangeIncludingRow = rangesListIncludingAnotherRange(columnSequencesRanges, fieldAsRange);

            if (!existRangeIncludingRow) {
                fieldToExclude = new Field(rowIdx, columnIdx);
                if (isFieldEmpty(fieldToExclude)) {
                    this.placeXAtGivenField(fieldToExclude);
                    this.excludeFieldInRow(fieldToExclude);
                    this.excludeFieldInColumn(fieldToExclude);
                    this.addRowToAffectedActionsByIdentifiers(rowIdx, ActionsConstants.actionsToDoAfterPlacingXsAtColumnsUnreachableFields);

                    tmpLog = generatePlacingXStepDescription(columnIdx, rowIdx, "placing \"X\" at unreachable field");
                    addLog(tmpLog);

                    this.nonogramState.increaseMadeSteps();
                } else if (showRepetitions) {
                    System.out.println("X at unreachable field in column placed earlier!");
                }
            }
        }
    }

    /**
     * @param columnIdx - column index on which try to extend subsequence to minimum possible matching
     */
    public void extendColouredFieldsToTopNearXToMaximumPossibleLengthInColumn(int columnIdx) {
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);

        List<Integer> possibleSequenceLengths;
        int minimumPossibleLength;
        List<Integer> colouredSubsequenceRange;
        int distanceToX;
        Field potentiallyXField;
        Field potentiallyColouredField;

        Field fieldToColour;

        for(int rowIdx = this.getHeight() - 1; rowIdx >= 0; rowIdx--) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(potentiallyColouredField)) {
                //find range of current coloured sequence (bottom to top)
                colouredSubsequenceRange = this.findCurrentColouredSequenceRangeBottomToTop(rowIdx, columnIdx);

                //filter sequences that is possibly that coloured sequence is part of them
                possibleSequenceLengths = filterSequencesRangesIncludingAnotherAndReturnCorrespondingLengths (
                        columnSequencesRanges, colouredSubsequenceRange, columnSequencesLengths);

                if ((possibleSequenceLengths.isEmpty())) {
                    this.nonogramState.invalidateSolution();
                    break;
                }

                //get minimum length from possible sequences ('worst case', but realistic, to be sure extending is justified)
                minimumPossibleLength = Collections.min(possibleSequenceLengths);

                distanceToX = 0;

                /* find distance to nearest X (from colouredSubsequenceRange.get(0) it will be minimum:
                 range length , f.e. [4,5] -> rangeLength([4,5]) = 2) -> pos [3, 5] */
                for(int rowsToX = rangeLength(colouredSubsequenceRange); colouredSubsequenceRange.get(0) + rowsToX < this.getHeight() && rowsToX < minimumPossibleLength; rowsToX++) {
                    potentiallyXField = new Field(colouredSubsequenceRange.get(0) + rowsToX, columnIdx);
                    if (isFieldWithX(potentiallyXField)) {
                        distanceToX = rowsToX;
                        break;
                    }
                }

                /* there could be situation when 'X' is f.e. too far from coloured sequence(distanceToX not changed), check that case*/
                if (distanceToX != 0) {
                    int mostTopSequenceToExtendRowIndex = colouredSubsequenceRange.get(0) + distanceToX - minimumPossibleLength;
                    for(int colourRowIdx = colouredSubsequenceRange.get(0) - 1; colourRowIdx >= 0 /*tmp cond*/ && colourRowIdx >= mostTopSequenceToExtendRowIndex; colourRowIdx--) {
                        fieldToColour = new Field(colourRowIdx, columnIdx);
                        try {
                            if (isFieldEmpty(fieldToColour)) {
                                this.colourFieldAtGivenPosition(fieldToColour, "--C-");
                                this.addRowToAffectedActionsByIdentifiers(colourRowIdx, ActionsConstants.actionsToDoAfterExtendingColouredFieldsNearXInColumns);
                                this.nonogramState.increaseMadeSteps();

                                tmpLog = generateColourStepDescription(columnIdx, colourRowIdx, "extend coloured fields in sequence to left near X " +
                                        "to length of shortest possible sequence in column");
                                addLog(tmpLog);
                            } else if (showRepetitions) {
                                System.out.println("Column field was coloured earlier (extending to minimum required - to top).");
                            }
                        } catch (IndexOutOfBoundsException e) {
                            this.nonogramState.invalidateSolution();
                        }
                    }
                }
            }
        }
    }

    private List<Integer> findCurrentColouredSequenceRangeBottomToTop(int rowIdx, int columnIdx) {
        Field fieldBeforeColouredToCheck;

        int lastColouredFieldRowIndexInSubsequence = rowIdx;
        do {
            fieldBeforeColouredToCheck = new Field(--rowIdx, columnIdx);
        } while (rowIdx >= 0 && isFieldColoured(fieldBeforeColouredToCheck));
        int firstColouredFieldRowIndexInSubsequence = ++rowIdx;

        return List.of(firstColouredFieldRowIndexInSubsequence, lastColouredFieldRowIndexInSubsequence);
    }

    /**
     * @param columnIdx - column index on which try to extend subsequence to minimum possible matching (to bottom)
     */
    public void extendColouredFieldsToBottomNearXToMaximumPossibleLengthInColumn(int columnIdx) {
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);

        List<Integer> possibleSequenceLengths;
        int minimumPossibleLength;
        List<Integer> colouredSubsequenceRange;
        int distanceFromX;
        Field potentiallyXField;
        Field potentiallyColouredField;

        Field fieldToColour;

        for(int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(potentiallyColouredField)) {
                //find range of current coloured sequence (top to bottom)
                colouredSubsequenceRange = this.findCurrentColouredSequenceRangeTopToBottom(columnIdx, rowIdx);

                //filter sequences that is possibly that coloured sequence is part of them
                possibleSequenceLengths = filterSequencesRangesIncludingAnotherAndReturnCorrespondingLengths (
                        columnSequencesRanges, colouredSubsequenceRange, columnSequencesLengths);

                if (possibleSequenceLengths.isEmpty()) {
                    this.nonogramState.invalidateSolution();
                    break;
                }

                //get minimum length from possible sequences ('worst case', but realistic, to be sure extending is justified)
                minimumPossibleLength = Collections.min(possibleSequenceLengths);

                distanceFromX = 0;

                 /* find distance from nearest X (from colouredSubsequenceRange.get(1) it will be maximum:
                 range length , f.e. [6, 7] -> rangeLength([6, 7]) = 2) -> pos [rowIdx, 5] */
                for(int rowsFromX = rangeLength(colouredSubsequenceRange); colouredSubsequenceRange.get(1) - rowsFromX >= 0 && rowsFromX < minimumPossibleLength - 1; rowsFromX++) {
                    potentiallyXField = new Field(colouredSubsequenceRange.get(1) - rowsFromX, columnIdx);
                    if (isFieldWithX(potentiallyXField)) {
                        distanceFromX = rowsFromX;
                        break;
                    }
                }

                /* there could be situation when 'X' is f.e. too far from coloured sequence(distanceFromX not changed), check that case*/
                if (distanceFromX != 0) {
                    int mostBottomSequenceToExtendColumnIndex = colouredSubsequenceRange.get(1) - distanceFromX + minimumPossibleLength;
                    for(int colourRowIdx = colouredSubsequenceRange.get(1) + 1; colourRowIdx < this.getHeight() /*tmp cond*/ && colourRowIdx <= mostBottomSequenceToExtendColumnIndex; colourRowIdx++) {
                        try {
                            fieldToColour = new Field(colourRowIdx, columnIdx);
                            if (isFieldColoured(fieldToColour)) {
                                fieldToColour = new Field(colourRowIdx, columnIdx);
                                this.colourFieldAtGivenPosition(fieldToColour, "--C-");
                                this.addRowToAffectedActionsByIdentifiers(colourRowIdx, ActionsConstants.actionsToDoAfterExtendingColouredFieldsNearXInColumns);
                                this.nonogramState.increaseMadeSteps();

                                tmpLog = generateColourStepDescription(columnIdx, colourRowIdx, "extend coloured fields in sequence to bottom near X " +
                                        "to length of shortest possible sequence in row");
                                addLog(tmpLog);
                            } else if (showRepetitions) {
                                System.out.println("Column field was coloured earlier (extending to minimum required - to bottom).");
                            }
                        } catch (IndexOutOfBoundsException e) {
                            this.nonogramState.invalidateSolution();
                        }
                    }
                }
            }
        }
    }

    private List<Integer> findCurrentColouredSequenceRangeTopToBottom(int columnIdx, int rowIdx) {
        Field fieldBeforeColouredToCheck;

        int firstColouredFieldRowIndexInSubsequence = rowIdx;
        do {
            fieldBeforeColouredToCheck = new Field(++rowIdx, columnIdx);
        } while (rowIdx < this.getHeight() - 1 && isFieldColoured(fieldBeforeColouredToCheck));
        int lastColouredFieldRowIndexInSubsequence = --rowIdx;

        return List.of(firstColouredFieldRowIndexInSubsequence, lastColouredFieldRowIndexInSubsequence);
    }

    private boolean isRowRangeColoured(int columnIdx, List<Integer> rowRange) {
        Field potentiallyColouredField;
        for(Integer rowIdx : rowRange) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (!isFieldColoured(potentiallyColouredField)) {
                return false;
            }
        }

        return true;
    }

    public int minimumRowIndexWithoutX(int columnIdx, int lastSequenceRowIdx, int sequenceFullLength) {
        int minimumRowIndex = lastSequenceRowIdx;
        int minimumRowIndexLimit = Math.max(lastSequenceRowIdx - sequenceFullLength + 1, 0);
        Field fieldToCheck;

        for(; minimumRowIndex >= minimumRowIndexLimit; minimumRowIndex--) {
            fieldToCheck = new Field(minimumRowIndex, columnIdx);
            if (isFieldWithX(fieldToCheck)) {
                break;
            }
        }

        return minimumRowIndex + 1;
    }

    public int maximumRowIndexWithoutX(int columnIdx, int firstSequenceRowIdx, int sequenceFullLength) {
        int maximumRowIndex = firstSequenceRowIdx;
        int maximumRowIndexLimit = Math.min(firstSequenceRowIdx + sequenceFullLength - 1, this.getHeight() - 1);
        Field fieldToCheck;

        for(; maximumRowIndex <= maximumRowIndexLimit; maximumRowIndex++) {
            fieldToCheck = new Field(maximumRowIndex, columnIdx);
            if (isFieldWithX(fieldToCheck)) {
                break;
            }
        }

        return maximumRowIndex - 1;
    }

    /**
     * @param colIdx - column to mark sequence with its identifier (1st sequence -> "b", 2nd sequence -> "c", etc)
     * @param rowIdx - row of field to mark with column sequence identifier
     * @param colSeqMark - column sequence marker/identifier
     */
    public void markColumnBoardField(int rowIdx, int colIdx, String colSeqMark) {
        String oldRowField = this.nonogramSolutionBoardWithMarks.get(rowIdx).get(colIdx);
        this.nonogramSolutionBoardWithMarks.get(rowIdx).set(colIdx, oldRowField.substring(0, 2) + MARKED_COLUMN_INDICATOR + colSeqMark);
    }

    /**
     * @param columnIndex - column index to change one of its sequences range
     * @param sequenceIndex - sequence index to change range
     * @param updatedRange - column sequence updatedRange
     */
    public void changeColumnSequenceRange(int columnIndex, int sequenceIndex, List<Integer> updatedRange) {
        this.columnsSequencesRanges.get(columnIndex).set(sequenceIndex, updatedRange);
    }

    private String generateColourStepDescription(int columnIndex, int rowIndex, String actionType) {
        return String.format("COLUMN %d, ROW %d - field colouring - %s.", columnIndex, rowIndex, actionType);
    }

    private String generatePlacingXStepDescription(int columnIndex, int rowIndex, String actionType) {
        return String.format("COLUMN %d, ROW %d - X placing - %s.", columnIndex, rowIndex, actionType);
    }

    private String generateCorrectingColumnSequenceRangeStepDescription(int columnIndex, int sequenceIndex, List<Integer> oldRange, List<Integer> correctedRange, String actionType) {
        return String.format("COLUMN %d, SEQUENCE %d - range correcting - from [%d, %d] to [%d, %d] - %s", columnIndex, sequenceIndex,
                oldRange.get(0), oldRange.get(1), correctedRange.get(0), correctedRange.get(1), actionType);
    }
}