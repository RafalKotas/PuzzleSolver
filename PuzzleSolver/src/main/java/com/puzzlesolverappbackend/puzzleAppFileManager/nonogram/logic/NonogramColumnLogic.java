package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.NonogramConstants.EMPTY_FIELD;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.NonogramConstants.MARKED_COLUMN_INDICATOR;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.NonogramParametersComparatorHelper.rangesEqual;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.NonogramLogicService.filterSequencesRangesIncludingAnotherAndReturnCorrespondingLengths;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.NonogramLogicService.rangesListIncludingAnotherRange;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.ColumnMixedActionsHelper.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.ActionsConstants.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramBoardUtils.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramLogicUtils.colouredSequenceInColumnIsValid;
import static com.puzzlesolverappbackend.puzzleAppFileManager.utils.ArrayUtils.rangeInsideAnotherRange;
import static com.puzzlesolverappbackend.puzzleAppFileManager.utils.ArrayUtils.rangeLength;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
public class NonogramColumnLogic extends NonogramLogicParams {

    private final static String CORRECT_COLUMN_SEQ_RANGE_MARKING_FIELD = "correcting column sequence range when marking field";

    private final static String FILL_OVERLAPPING_FIELDS = "fill overlapping fields";

    private final static List<Integer> NOT_FOUND_EMPTY_FIELDS_RANGE_VALUE = List.of(-1, -1);

    private final static List<Integer> NOT_FOUND_COLOURED_FIELDS_RANGE_VALUE = List.of(-1, -1);

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

    // CORRECT_COLUMN_SEQUENCES_RANGES
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

        for (int sequenceIdx = 0; sequenceIdx < columnSequencesRanges.size() - 1; sequenceIdx++) {
            nextSequenceId = sequenceIdx + 1;
            if (!columnSequencesIdsNotToInclude.contains(nextSequenceId)) {
                if (columnSequencesIdsNotToInclude.contains(sequenceIdx)) {
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
                }  else {
                    currentSequenceRange = columnSequencesRanges.get(sequenceIdx);
                    nextSequenceRange = columnSequencesRanges.get(nextSequenceId);

                    updatedNextSequenceBeginRangeRowIndex = currentSequenceRange.get(0) + columnSequencesLengths.get(sequenceIdx) + 1;

                    nextSequenceOldBeginRangeRowIndex = nextSequenceRange.get(0);

                    updatedStartIndex = Math.max(nextSequenceOldBeginRangeRowIndex, updatedNextSequenceBeginRangeRowIndex);
                    updatedNextSequenceRange = new ArrayList<>(Arrays.asList(updatedStartIndex, nextSequenceRange.get(1)));

                    correctColumnRangeFromTop(columnIdx, columnSequencesLengths, nextSequenceRange, updatedStartIndex, nextSequenceOldBeginRangeRowIndex, nextSequenceId, updatedNextSequenceRange);
                }
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
            this.addColumnToAffectedActionsByIdentifiers(columnIdx, actionsToDoAfterCorrectingColumnsSequencesRanges);
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

        for (int sequenceIdx = columnSequencesRanges.size() - 1; sequenceIdx > 0; sequenceIdx--) {
            previousSequenceId = sequenceIdx - 1;
            if (!columnSequencesIdsNotToInclude.contains(previousSequenceId)) {
                if (columnSequencesIdsNotToInclude.contains(sequenceIdx)) {
                    fullSequenceRange = columnSequencesRanges.get(sequenceIdx);

                    oldPreviousSequenceRange = columnSequencesRanges.get(previousSequenceId);
                    previousSequenceUpdatedRangeEndRowIndex = fullSequenceRange.get(0) - 2;

                    while(columnFieldsNotToInclude.contains(previousSequenceUpdatedRangeEndRowIndex)) {
                        previousSequenceUpdatedRangeEndRowIndex--;
                    }

                    previousSequenceOldRangeEndRowIndex = oldPreviousSequenceRange.get(1);

                    updatedEndIndex = Math.min(previousSequenceOldRangeEndRowIndex, previousSequenceUpdatedRangeEndRowIndex);

                    updatedPreviousSequenceRange = new ArrayList<>(Arrays.asList(oldPreviousSequenceRange.get(0), updatedEndIndex));

                    correctColumnRangeFromBottom(columnIdx,
                            columnSequencesLengths,
                            oldPreviousSequenceRange,
                            updatedEndIndex,
                            previousSequenceOldRangeEndRowIndex,
                            previousSequenceId,
                            updatedPreviousSequenceRange);
                } else {
                    currentSequenceLength = columnSequencesLengths.get(sequenceIdx);
                    currentSequenceRange = columnSequencesRanges.get(sequenceIdx);
                    oldPreviousSequenceRange = columnSequencesRanges.get(previousSequenceId);

                    previousSequenceUpdatedRangeEndRowIndex = currentSequenceRange.get(1) - currentSequenceLength - 1;

                    previousSequenceOldRangeEndRowIndex = oldPreviousSequenceRange.get(1);

                    updatedEndIndex = Math.min(previousSequenceOldRangeEndRowIndex, previousSequenceUpdatedRangeEndRowIndex);

                    updatedPreviousSequenceRange = new ArrayList<>(Arrays.asList(oldPreviousSequenceRange.get(0), updatedEndIndex));

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
            this.addColumnToAffectedActionsByIdentifiers(columnIdx, actionsToDoAfterCorrectingColumnsSequencesRanges);

            if (rangeLength(updatedPreviousSequenceRange) == columnSequencesLengths.get(previousSequenceId) && isRowRangeColoured(columnIdx, updatedPreviousSequenceRange)) {
                this.excludeSequenceInColumn(columnIdx, previousSequenceId);
            }

            this.nonogramState.increaseMadeSteps();
            tmpLog = generateCorrectingColumnSequenceRangeStepDescription(columnIdx, previousSequenceId, oldPreviousSequenceRange, updatedPreviousSequenceRange, "correcting from bottom");
            addLog(tmpLog);
        }
    }

    /**
     * CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS
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

        for (int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(nonogramSolutionBoard, potentiallyColouredField)) {
                oldSequenceRange = columnSequencesRanges.get(sequenceId);
                columnSequenceRangeStart = oldSequenceRange.get(0);
                columnSequenceRangeEnd = oldSequenceRange.get(1);
                maximumPossibleSequenceRangeEnd = rowIdx + sequenceLength - 1;

                updatedRangeEndIndex = Math.min(columnSequenceRangeEnd, maximumPossibleSequenceRangeEnd);
                updatedSequenceRange = new ArrayList<>(Arrays.asList(columnSequenceRangeStart, updatedRangeEndIndex));

                if (updatedRangeEndIndex != columnSequenceRangeEnd) {
                    columnSequencesRangesChanged = true;
                    this.getColumnsSequencesRanges().get(columnIdx).set(sequenceId, updatedSequenceRange);
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
            this.addColumnToAffectedActionsByIdentifiers(columnIdx, actionsToDoAfterCorrectingColumnsSequencesWhenMetColouredField);
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

        for (int rowIdx = this.getHeight() - 1; rowIdx >= 0 ; rowIdx--) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(nonogramSolutionBoard, potentiallyColouredField)) {
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
            this.addColumnToAffectedActionsByIdentifiers(columnIdx, actionsToDoAfterCorrectingColumnsSequencesWhenMetColouredField);
        }
    }

    /**
     * CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY
     * @param columnIdx - column to correct sequence/s range/s if x on way (sequence won't fit)
     */
    public void correctColumnRangeIndexesIfXOnWay(int columnIdx) {

        boolean columnSequencesRangesChanged = false;

        List<Integer> columnSequences = this.getColumnsSequences().get(columnIdx);
        List<List<Integer>> columnSequencesRangesAtStart = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> excludedColumnSequences = this.getColumnsSequencesIdsNotToInclude().get(columnIdx);
        int columnSequenceLength;
        List<Integer> columnSequenceRange;

        int updatedColumnSequenceRangeStartIndex;
        int updatedColumnSequenceRangeEndIndex;
        List<Integer> updatedRange;

        for (int seqNo = 0; seqNo < columnSequencesRangesAtStart.size(); seqNo++) {
            if (!excludedColumnSequences.contains(seqNo)) {

                columnSequenceRange = columnSequencesRangesAtStart.get(seqNo);
                columnSequenceLength = columnSequences.get(seqNo);

                updatedColumnSequenceRangeStartIndex = getUpdatedColumnRangeStartIndexBecauseXOnWay(columnIdx, columnSequenceRange, columnSequenceLength);
                updatedColumnSequenceRangeEndIndex = getUpdatedColumnRangeEndIndexBecauseXOnWay(columnIdx, columnSequenceRange, columnSequenceLength);
                updatedRange = new ArrayList<>(Arrays.asList(updatedColumnSequenceRangeStartIndex, updatedColumnSequenceRangeEndIndex));

                if (!rangesEqual(columnSequenceRange, updatedRange)) {
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
            this.addColumnToAffectedActionsByIdentifiers(columnIdx, actionsToDoAfterCorrectingColumnsSequencesIfXOnWay);
        }
    }

    private int getUpdatedColumnRangeStartIndexBecauseXOnWay(int columnIdx,
                                                             List<Integer> columnSequenceRange,
                                                             int columnSequenceLength) {
        Field potentiallyXOnWayField;

        int updatedColumnSequenceRangeStartIndex = columnSequenceRange.get(0);
        boolean indexOk;

        // TODO - optimization(?) -> if isFieldWithX on rowIdx then rowStartIndex := rowIdx + 1 (next near X)
        for (int rowStartIndex = columnSequenceRange.get(0); rowStartIndex < columnSequenceRange.get(1) - columnSequenceLength + 1; rowStartIndex++) {
            indexOk = true;

            for (int rowIdx = rowStartIndex; rowIdx < rowStartIndex + columnSequenceLength; rowIdx++) {
                potentiallyXOnWayField = new Field(rowIdx, columnIdx);
                if (isFieldWithX(nonogramSolutionBoard, potentiallyXOnWayField)) {
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

        // TODO - optimization(?) -> if isFieldWithX on rowIdx then rowStartIndex := rowIdx - 1 (previous near X)
        for (int rowEndIndex = columnSequenceRange.get(1); rowEndIndex > columnSequenceRange.get(0) + columnSequenceLength - 1; rowEndIndex--) {
            indexOk = true;
            for (int rowIdx = rowEndIndex; rowIdx > rowEndIndex - columnSequenceLength; rowIdx--) {
                potentiallyXOnWayField = new Field(rowIdx, columnIdx);
                if (isFieldWithX(nonogramSolutionBoard, potentiallyXOnWayField)) {
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
     * CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES
     * @param columnIdx - column to correct sequence/s range/s when matching fields to corresponding sequences
     */
    public void correctColumnSequencesRangesWhenMatchingFieldsToSequences(int columnIdx) {
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);

        Field fieldToCheckIfIsColoured;
        List<List<Integer>> colouredSequencesPartsRanges = new ArrayList<>();
        List<Integer> colouredSequencePartRange;

        // extract coloured sequences ranges from board
        for (int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            fieldToCheckIfIsColoured = new Field(rowIdx, columnIdx);
            if (isFieldColoured(this.nonogramSolutionBoard, fieldToCheckIfIsColoured)) {
                colouredSequencePartRange = getColouredRangeInColumnNearField(fieldToCheckIfIsColoured);
                colouredSequencesPartsRanges.add(colouredSequencePartRange);
                rowIdx = colouredSequencePartRange.get(1);
            }
        }

        List<List<Integer>> partsMaxRanges = new ArrayList<>();
        List<Integer> partMaxRange;

        // calculate max possible ranges for corresponding coloured sequences
        for (List<Integer> colouredSequencesPartsRange : colouredSequencesPartsRanges) {
            partMaxRange = getColumnSequenceMaxPossibleRange(columnIdx, colouredSequencesPartsRange);
            partsMaxRanges.add(partMaxRange);
        }

        List<List<Integer>> colouredSequencesPartsMatches = new ArrayList<>();
        List<Integer> colouredSequencePartMatches;
        int minSeqNo = 0; // if X between coloured sequences parts -> increase (sequences that can't be merged)
        List<Integer> currentColouredSeqPart;
        int partMaxLength;

        // match coloured sequences parts to possible sequences that may include them
        for (int seqPartNo = 0; seqPartNo < partsMaxRanges.size(); seqPartNo++) {
            currentColouredSeqPart = colouredSequencesPartsRanges.get(seqPartNo);
            partMaxLength = rangeLength(partsMaxRanges.get(seqPartNo));

            colouredSequencePartMatches = new ArrayList<>();
            for (int seqNo = minSeqNo; seqNo < columnSequencesRanges.size(); seqNo++) {
                if (rangeInsideAnotherRange(currentColouredSeqPart, columnSequencesRanges.get(seqNo)) &&
                        columnSequencesLengths.get(seqNo) <= partMaxLength) {
                    colouredSequencePartMatches.add(seqNo);
                }
            }
            colouredSequencesPartsMatches.add(colouredSequencePartMatches);
            if (seqPartNo < partsMaxRanges.size() - 1 &&
                    areXsBetweenColouredRangesInColumn(columnIdx, currentColouredSeqPart, colouredSequencesPartsRanges.get(seqPartNo + 1))) {
                minSeqNo++;
            }
        }

        List<Integer> matchingSequencesIds;
        int matchingSeqId;
        int matchingSequenceLength;
        List<Integer> oldMatchingSequenceRange;

        int firstColouredPossibleRowAccordingToSequenceRanges;
        int updatedMatchingSequenceRangeStartIndex;

        int lastColouredPossibleRowAccordingToSequenceRanges;
        int updatedMatchingSequenceRangeEndIndex;

        List<Integer> updatedMatchingSequenceRange;

        // if there are uniquely assigned sequences then mark and correct range
        for (int matchedSeqNo = 0; matchedSeqNo < colouredSequencesPartsMatches.size(); matchedSeqNo++) {
            matchingSequencesIds = colouredSequencesPartsMatches.get(matchedSeqNo);
            if (matchingSequencesIds.size() == 1) {
                matchingSeqId = matchingSequencesIds.get(0);
                oldMatchingSequenceRange = columnSequencesRanges.get(matchingSeqId);
                matchingSequenceLength = columnSequencesLengths.get(matchingSeqId);

                firstColouredPossibleRowAccordingToSequenceRanges = colouredSequencesPartsRanges.get(matchedSeqNo).get(1) - matchingSequenceLength + 1;
                updatedMatchingSequenceRangeStartIndex = Math.max(oldMatchingSequenceRange.get(0), firstColouredPossibleRowAccordingToSequenceRanges);

                lastColouredPossibleRowAccordingToSequenceRanges = colouredSequencesPartsRanges.get(matchedSeqNo).get(0) + matchingSequenceLength - 1;
                updatedMatchingSequenceRangeEndIndex = Math.min(oldMatchingSequenceRange.get(1), lastColouredPossibleRowAccordingToSequenceRanges);

                updatedMatchingSequenceRange = new ArrayList<>(Arrays.asList(updatedMatchingSequenceRangeStartIndex, updatedMatchingSequenceRangeEndIndex));

                if (!rangesEqual(oldMatchingSequenceRange, updatedMatchingSequenceRange)) {
                    this.updateColumnSequenceRange(columnIdx, matchingSeqId, updatedMatchingSequenceRange);
                    this.addColumnToAffectedActionsByIdentifiers(columnIdx, actionsToDoInColumnAfterCorrectingColumnsSequencesRangesWhenMatchingFieldsToSequences);
                    this.nonogramState.increaseMadeSteps();
                    tmpLog = generateCorrectingColumnSequenceRangeStepDescription(columnIdx, matchingSeqId, oldMatchingSequenceRange, updatedMatchingSequenceRange, "correcting sequence when matching fields to only possible coloured sequences");
                    addLog(tmpLog);

                    if (rangeLength(updatedMatchingSequenceRange) == columnSequencesLengths.get(matchingSeqId) && isRowRangeColoured(columnIdx, updatedMatchingSequenceRange)) {
                        this.excludeSequenceInColumn(columnIdx, matchingSeqId);
                    }
                }
            }
        }
    }

    private List<Integer> getColouredRangeInColumnNearField(Field colouredField) {
        List<Integer> colouredSequenceRangeNearField = new ArrayList<>(List.of(colouredField.getRowIdx()));
        int columnIdx = colouredField.getColumnIdx();

        int rowToBottom = colouredField.getRowIdx() + 1;
        Field fieldBottom = new Field(rowToBottom, columnIdx);

        while (rowToBottom < this.getHeight() && isFieldColoured(this.getNonogramSolutionBoard(), fieldBottom)) {
            fieldBottom = new Field(++rowToBottom, columnIdx);
        }
        fieldBottom = new Field(--rowToBottom, columnIdx);

        colouredSequenceRangeNearField.add(fieldBottom.getRowIdx());

        return colouredSequenceRangeNearField;
    }

    private List<Integer> getColumnSequenceMaxPossibleRange(int columnIdx, List<Integer> colouredSequencePartRange) {
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

    private boolean areXsBetweenColouredRangesInColumn(int columnIdx, List<Integer> firstRange, List<Integer> secondRange) {
        if (firstRange.size() != 2 || secondRange.size() != 2 || firstRange.get(1) >= secondRange.get(0)) {
            return false;
        }

        List<Integer> rangeBetweenColouredRanges = new ArrayList<>(Arrays.asList(firstRange.get(1) + 1, secondRange.get(0) - 1));

        Field fieldToCheck;

        for (int rowIdx : rangeBetweenColouredRanges) {
            fieldToCheck = new Field(rowIdx, columnIdx);
            if (isFieldWithX(this.getNonogramSolutionBoard(), fieldToCheck)) {
                return true;
            }
        }

        return false;
    }

    /**
     *  CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE
     * @param columnIdx - column to correct sequence/s range/s if sequence possible start/end index if one of 'edge indexes'
     *              f.e. in:
     *               columnSequencesRanges.get(0).get(3) = [23, 28] -> edge indexes : 23, 28
     *               and field with row index 22 or 29 is coloured -> then sequence which start at idx 23/28 will create too long coloured sequence
     */
    public void correctColumnSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(int columnIdx) {
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequenceRange;
        int rangeStartIndex;
        int rangeEndIndex;

        List<Integer> updatedSequenceRange;

        boolean anySequenceWasUpdated = false;

        for (int seqNo = 0; seqNo < columnSequencesRanges.size(); seqNo++) {
            columnSequenceRange = columnSequencesRanges.get(seqNo);
            rangeStartIndex = columnSequenceRange.get(0);
            rangeEndIndex = columnSequenceRange.get(1);

            updatedSequenceRange = new ArrayList<>();
            if (rangeStartIndex >= 1 && isFieldColoured(this.nonogramSolutionBoard, new Field(rangeStartIndex - 1, columnIdx))) {
                updatedSequenceRange.add(rangeStartIndex + 1);
            } else {
                updatedSequenceRange.add(rangeStartIndex);
            }

            if (rangeEndIndex < this.getHeight() - 1 && isFieldColoured(this.nonogramSolutionBoard, new Field(rangeEndIndex + 1, columnIdx))) {
                updatedSequenceRange.add(rangeEndIndex - 1);
            } else {
                updatedSequenceRange.add(rangeEndIndex);
            }

            if (!updatedSequenceRange.equals(columnSequenceRange)) {
                anySequenceWasUpdated = true;
                this.updateColumnSequenceRange(columnIdx, seqNo, updatedSequenceRange);
                this.nonogramState.increaseMadeSteps();
                tmpLog = generateCorrectingColumnSequenceRangeStepDescription(columnIdx, seqNo, columnSequenceRange, updatedSequenceRange, "correcting column sequence range when start from edge index will create too long sequence.");
                addLog(tmpLog);
            }
        }

        if (anySequenceWasUpdated) {
            this.addColumnToAffectedActionsByIdentifiers(columnIdx, actionsToDoInColumnAfterCorrectingColumnSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence);
        }
    }

    /**
     * COLOUR_OVERLAPPING_FIELDS_IN_COLUMN
     * @param columnIdx - row in which action should be made
     * fill fields in column where ranges met specific condition (range_length < sequence_length * 2)
     */
    public void colourOverlappingFieldsInColumn(int columnIdx) {
        List<Integer> sequencesInColumnLengths = this.getColumnsSequences().get(columnIdx);
        List<List<Integer>> sequencesInColumnRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> range;

        int sequenceLength;
        int rangeBeginIndex;
        int rangeEndIndex;
        List<Integer> colouringRange;
        Field fieldToColour;

        for (int sequenceIdx = 0; sequenceIdx < sequencesInColumnLengths.size(); sequenceIdx++) {
            sequenceLength = sequencesInColumnLengths.get(sequenceIdx);
            range = sequencesInColumnRanges.get(sequenceIdx);
            rangeBeginIndex = range.get(0);
            rangeEndIndex = range.get(1);

            colouringRange = IntStream.rangeClosed(rangeEndIndex - sequenceLength + 1, rangeBeginIndex + sequenceLength - 1)
                    .boxed()
                    .toList();

            for (int rowIdx : colouringRange) {
                fieldToColour = new Field(rowIdx, columnIdx);

                if (isFieldEmpty(nonogramSolutionBoard, fieldToColour)) {
                    this.colourFieldAtGivenPosition(fieldToColour, "--C-");
                    tmpLog = generateColourStepDescription(columnIdx, rowIdx, FILL_OVERLAPPING_FIELDS);
                    addLog(tmpLog);
                    this.addRowToAffectedActionsByIdentifiers(rowIdx, actionsToDoInRowAfterColouringOverlappingSequencesInColumns);
                    this.addColumnToAffectedActionsByIdentifiers(columnIdx, actionsToDoInColumnAfterColouringOverlappingSequencesInColumns);
                    this.getNonogramState().increaseMadeSteps();
                } else if (showRepetitions) {
                    logger.warn("Column field was coloured earlier!");
                }

                // TODO - check if need to mark sequence before excluding
                if (rangeLength(colouringRange) == columnsSequences.get(columnIdx).get(sequenceIdx)) {
                    this.excludeSequenceInColumn(columnIdx, sequenceIdx);
                }
            }
        }
    }

    /**
     * EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN
     * @param columnIdx - column in which action should be made
     * coloring the fields in column next to the x to the distance of the shortest possible sequence in a given area
     */
    public void extendColouredFieldsNearXToMaximumPossibleLengthInColumn(int columnIdx) {
        extendColouredFieldsToTopNearXToMaximumPossibleLengthInColumn(columnIdx);
        extendColouredFieldsToBottomNearXToMaximumPossibleLengthInColumn(columnIdx);
    }

    /**
     * @param columnIdx - column index on which try to extend subsequence to minimum possible matching (to top)
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

        for (int rowIdx = this.getHeight() - 1; rowIdx >= 0; rowIdx--) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(nonogramSolutionBoard, potentiallyColouredField)) {
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
                for (int rowsToX = rangeLength(colouredSubsequenceRange); colouredSubsequenceRange.get(0) + rowsToX < this.getHeight() && rowsToX < minimumPossibleLength; rowsToX++) {
                    potentiallyXField = new Field(colouredSubsequenceRange.get(0) + rowsToX, columnIdx);
                    if (isFieldWithX(nonogramSolutionBoard, potentiallyXField)) {
                        distanceToX = rowsToX;
                        break;
                    }
                }

                /* there could be situation when 'X' is f.e. too far from coloured sequence(distanceToX not changed), check that case*/
                if (distanceToX != 0) {
                    int mostTopSequenceToExtendRowIndex = colouredSubsequenceRange.get(0) + distanceToX - minimumPossibleLength;
                    for (int colourRowIdx = colouredSubsequenceRange.get(0) - 1; colourRowIdx >= 0 /*tmp cond*/ && colourRowIdx >= mostTopSequenceToExtendRowIndex; colourRowIdx--) {
                        fieldToColour = new Field(colourRowIdx, columnIdx);
                        try {
                            if (isFieldEmpty(nonogramSolutionBoard, fieldToColour)) {
                                this.colourFieldAtGivenPosition(fieldToColour, "--C-");
                                this.addRowToAffectedActionsByIdentifiers(colourRowIdx, actionsToDoInRowAfterExtendingColouredFieldsNearXInColumns);
                                this.addColumnToAffectedActionsByIdentifiers(columnIdx, actionsToDoInColumnAfterExtendingColouredFieldsNearXInColumns);
                                this.nonogramState.increaseMadeSteps();

                                tmpLog = generateColourStepDescription(columnIdx, colourRowIdx, "extend coloured fields in sequence to top near X " +
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
        } while (rowIdx >= 0 && isFieldColoured(nonogramSolutionBoard, fieldBeforeColouredToCheck));
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

        for (int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(nonogramSolutionBoard, potentiallyColouredField)) {

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
                for (int rowsFromX = rangeLength(colouredSubsequenceRange); colouredSubsequenceRange.get(1) - rowsFromX >= 0 && rowsFromX < minimumPossibleLength; rowsFromX++) {
                    potentiallyXField = new Field(colouredSubsequenceRange.get(1) - rowsFromX, columnIdx);
                    if (isFieldWithX(nonogramSolutionBoard, potentiallyXField)) {
                        distanceFromX = rowsFromX;
                        break;
                    }
                }

                /* there could be situation when 'X' is f.e. too far from coloured sequence(distanceFromX not changed), check that case*/
                if (distanceFromX != 0) {
                    int mostBottomSequenceToExtendColumnIndex = colouredSubsequenceRange.get(1) - distanceFromX + minimumPossibleLength;
                    for (int colourRowIdx = colouredSubsequenceRange.get(1) + 1; colourRowIdx < this.getHeight() /*tmp cond*/ && colourRowIdx <= mostBottomSequenceToExtendColumnIndex; colourRowIdx++) {
                        try {
                            fieldToColour = new Field(colourRowIdx, columnIdx);
                            if (isFieldEmpty(nonogramSolutionBoard, fieldToColour)) {
                                this.colourFieldAtGivenPosition(fieldToColour, "--C-");
                                this.addRowToAffectedActionsByIdentifiers(colourRowIdx, actionsToDoInRowAfterExtendingColouredFieldsNearXInColumns);
                                this.addColumnToAffectedActionsByIdentifiers(columnIdx, actionsToDoInColumnAfterExtendingColouredFieldsNearXInColumns);
                                this.nonogramState.increaseMadeSteps();

                                tmpLog = generateColourStepDescription(columnIdx, colourRowIdx, "extend coloured fields in sequence to bottom near X " +
                                        "to length of shortest possible sequence in column");
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
        } while (rowIdx < this.getHeight() - 1 && isFieldColoured(nonogramSolutionBoard, fieldBeforeColouredToCheck));
        int lastColouredFieldRowIndexInSubsequence = --rowIdx;

        return List.of(firstColouredFieldRowIndexInSubsequence, lastColouredFieldRowIndexInSubsequence);
    }

    /**
     * PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS
     * @param columnIdx - place an "X" on fields which not belong to any column possible range
     */
    public void placeXsColumnAtUnreachableFields(int columnIdx) {

        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        int nonogramHeight = this.getHeight();
        boolean existRangeIncludingRow;
        List<Integer> fieldAsRange;
        Field fieldToExclude;

        for (int rowIdx = 0; rowIdx < nonogramHeight; rowIdx++) {
            fieldAsRange = List.of(rowIdx, rowIdx);
            existRangeIncludingRow = rangesListIncludingAnotherRange(columnSequencesRanges, fieldAsRange);

            if (!existRangeIncludingRow) {
                fieldToExclude = new Field(rowIdx, columnIdx);
                if (isFieldEmpty(nonogramSolutionBoard, fieldToExclude)) {
                    this.placeXAtGivenField(fieldToExclude);
                    this.addRowToAffectedActionsByIdentifiers(rowIdx, actionsToDoInRowAfterPlacingXsAtColumnsUnreachableFields);
                    this.addColumnToAffectedActionsByIdentifiers(columnIdx, actionsToDoInColumnAfterPlacingXsAtColumnsUnreachableFields);

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
     * PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES
     * @param columnIdx - the column index where you place an "X" around the longest possible coloured sequences in a given area
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

        for (int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(nonogramSolutionBoard, potentiallyColouredField)) {
                colouredSequenceRange = findColouredSequenceRangeInColumn(rowIdx, columnIdx);
                rowIdx = colouredSequenceRange.get(1); //start from end of current coloured sequence

                sequenceOnBoardLength = rangeLength(colouredSequenceRange);
                columnSequencesIndexesIncludingSequenceRange = new ArrayList<>();
                columnSequencesLengthsIncludingSequenceRange = new ArrayList<>();

                for (int seqNo = 0; seqNo < columnSequencesRanges.size(); seqNo++) {
                    columnSequenceRange = columnSequencesRanges.get(seqNo);
                    if (rangeInsideAnotherRange(colouredSequenceRange, columnSequenceRange)) {
                        columnSequencesIndexesIncludingSequenceRange.add(seqNo);
                        columnSequencesLengthsIncludingSequenceRange.add(columnSequencesLengths.get(seqNo));
                    }
                }

                xAtEdges = new ArrayList<>(Arrays.asList(colouredSequenceRange.get(0) - 1, colouredSequenceRange.get(1) + 1));

                /* given coloured sequence can be part of only one column sequence
                AND given coloured sequence has exactly same length as only matching column range */
                if (columnSequencesIndexesIncludingSequenceRange.size() == 1 && sequenceOnBoardLength == columnSequencesLengthsIncludingSequenceRange.get(0)) {
                    doStuffWhenPlacingXsAroundLongestSequence(columnIdx, xAtEdges, true);

                    updatedSequenceRange = Arrays.asList(xAtEdges.get(0) + 1, xAtEdges.get(1) - 1);
                    excludeColouredFieldsBetweenXs(columnIdx, updatedSequenceRange);
                    sequenceIdxToExclude = columnSequencesIndexesIncludingSequenceRange.get(0);
                    updateLogicAfterPlacingXsAroundOnlyOneMatchingSequenceInColumn(columnIdx, sequenceIdxToExclude, updatedSequenceRange);
                } else if (columnSequencesIndexesIncludingSequenceRange.size() > 1 && sequenceOnBoardLength == Collections.max(columnSequencesLengthsIncludingSequenceRange)) {
                    /* more than one column sequence fit in coloured range & length of coloured sequence is a max length of possible matching column sequences lengths */
                    doStuffWhenPlacingXsAroundLongestSequence(columnIdx, xAtEdges, false);
                }
            }
        }
    }

    private List<Integer> findColouredSequenceRangeInColumn(int startRowIdx, int columnIdx) {
        int rowIdx = startRowIdx;

        List<Integer> colouredSequenceRange = new ArrayList<>();
        colouredSequenceRange.add(rowIdx);
        while (rowIdx < this.getHeight() && isFieldColoured(nonogramSolutionBoard, new Field(rowIdx, columnIdx))) {
            rowIdx++;
        }
        colouredSequenceRange.add(rowIdx - 1);

        return colouredSequenceRange;
    }

    private void doStuffWhenPlacingXsAroundLongestSequence(int columnIdx, List<Integer> xAtEdges, boolean onlyMatching) {
        String logEndPart = onlyMatching ? "[only possible]" : "[sequence index not specified]";
        for (int currentRowIndex : xAtEdges) {
            Field fieldAtEdge = new Field(currentRowIndex, columnIdx);
            if (isRowIndexValid(currentRowIndex)) {
                if (isFieldEmpty(nonogramSolutionBoard, fieldAtEdge)) {
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

            this.addRowToAffectedActionsByIdentifiers(currentRowIndex, actionsToDoInRowAfterPlacingXsAroundLongestSequencesInColumns);
            this.addColumnToAffectedActionsByIdentifiers(columnIdx, actionsToDoInColumnAfterPlacingXsAroundLongestSequencesInColumns);

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
        for (int sequenceRowIdx : updatedSequenceRange) {
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
     * PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES
     * @param columnIdx - place an "X" at too short empty fields sequences in column with this index, when none of column sequences can fit in hole
     */
    public void placeXsColumnAtTooShortEmptySequences(int columnIdx) {

        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);
        List<Integer> columnsSequencesIdsNotToInclude = this.getColumnsSequencesIdsNotToInclude().get(columnIdx);

        Field potentiallyXPlacedField;
        Field fieldAfterXToCheck;

        List<Integer> columnSequencesIdsIncludingEmptyRange = new ArrayList<>();
        List<Integer> columnSequencesIdsIncludingEmptyRangeAndNotFitInIt = new ArrayList<>();

        int firstXIndex;
        int lastXIndex;
        int emptyFieldsSequenceLength;
        List<Integer> emptyFieldsRange;
        Field fieldToExclude;

        boolean onlyEmptyFieldsInSequence;

        for (int rowIdx = 0; rowIdx < this.getHeight() - 1; rowIdx++) {
            onlyEmptyFieldsInSequence = true;
            potentiallyXPlacedField = new Field(rowIdx, columnIdx);
            if (isFieldWithX(nonogramSolutionBoard, potentiallyXPlacedField)) {

                firstXIndex = rowIdx;
                fieldAfterXToCheck = new Field(++rowIdx, columnIdx);
                while(rowIdx < this.getHeight()) {
                    if (isFieldEmpty(nonogramSolutionBoard, fieldAfterXToCheck)) {
                        fieldAfterXToCheck = new Field(++rowIdx, columnIdx);
                    } else {
                        if (isFieldColoured(nonogramSolutionBoard, fieldAfterXToCheck)) {
                            onlyEmptyFieldsInSequence = false;
                        }
                        break;
                    }
                }

                lastXIndex = rowIdx;

                if (lastXIndex == firstXIndex + 1) {
                    rowIdx--;
                } else {
                    emptyFieldsRange = Arrays.asList(firstXIndex + 1, lastXIndex - 1);
                    emptyFieldsSequenceLength = rangeLength(emptyFieldsRange);

                    columnSequencesIdsIncludingEmptyRange.clear();
                    columnSequencesIdsIncludingEmptyRangeAndNotFitInIt.clear();

                    for (int columnSequenceId = 0; columnSequenceId < columnSequencesLengths.size(); columnSequenceId++) {
                        if (!columnsSequencesIdsNotToInclude.contains(columnSequenceId)
                                && rangeInsideAnotherRange(emptyFieldsRange, columnSequencesRanges.get(columnSequenceId))) {
                            columnSequencesIdsIncludingEmptyRange.add(columnSequenceId);
                            if (columnSequencesLengths.get(columnSequenceId) > emptyFieldsSequenceLength) {
                                columnSequencesIdsIncludingEmptyRangeAndNotFitInIt.add(columnSequenceId);
                            }
                        }
                    }

                    // TODO onlyEmptyFieldsInSequence/emptyFieldsSequenceLength - check earlier - if is there is no sense to check another conditions (similarly to row)
                    if (onlyEmptyFieldsInSequence && !columnSequencesIdsIncludingEmptyRange.isEmpty()
                            && (columnSequencesIdsIncludingEmptyRange.equals(columnSequencesIdsIncludingEmptyRangeAndNotFitInIt))
                    ) {
                        for (int emptyFieldRowIdx = emptyFieldsRange.get(0); emptyFieldRowIdx <= emptyFieldsRange.get(1); emptyFieldRowIdx++) {
                            fieldToExclude = new Field(emptyFieldRowIdx, columnIdx);
                            if (isFieldEmpty(nonogramSolutionBoard, fieldToExclude)) {
                                this.placeXAtGivenField(fieldToExclude);
                                this.addRowToAffectedActionsByIdentifiers(emptyFieldRowIdx, actionsToDoInRowAfterPlacingXsAtTooShortSequencesInColumns);

                                tmpLog = generatePlacingXStepDescription(columnIdx, emptyFieldRowIdx, "placing \"X\" inside too short empty fields sequence");
                                addLog(tmpLog);

                                this.nonogramState.increaseMadeSteps();
                            } else if (showRepetitions) {
                                System.out.println("X placed in too short column empty field sequence earlier!");
                            }
                        }
                    }
                    rowIdx--;
                }
            }
        }
    }

    /***
     * PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE
     * @param columnIdx - column index to check if O can't be placed on field because of creating too long possible coloured sequence
     * How it works:
     *               1. Find coloured fields indexes in column, f.e. for:
     *                  ["-", "-", "O", "O", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "X"]
     *                  it will be [ 2, 3, 7, 17, 18]
     *               2. Group fields into ranges (field column indexes that differs by one row)
     *                  [ 2, 3, 7, 17, 18] -> [ [2, 3], [7, 7], [17, 18] ]
     *               3. For every range create new coloured sequences ranges, simulating colour field before or after current range:
     *                  [2, 3]: before -> [1, 3], after [2, 4] -> [[1, 3], [2, 4]]
     *                  whole ranges array: [ [2, 3], [7, 7], [17, 18] ] -> [ [[1, 3], [2, 4]], [[6, 7], [7, 8]], [[16, 18], [17, 19]] ]
     *               4. For every coloured fields sequence (2.) check if this sequence:
     *                  - can be merged with previous coloured sequence
     *                     (f.e. for seqNo == 1: [2, 3] and [6, 7] (3. arr[1][0]) -> 3 + 1 != 6 -> can't be merged)
     *                  - can be merged with next coloured sequence
     *                     (f.e. for seqNo == 1: [7, 8] (3. arr[1][1]) and [17, 18] -> 8 + 1 != 17 -> can't be merged)
     *               5. If sequence can be merged when placing "O":
     *                  - validate created coloured sequence -> NonogramLogicUtils.colouredSequenceInColumnIsValid()
     *                  Else validate only current coloured sequence (when isn't merged with another after placing "O")
     *               6. If sequence is not valid, place "X" at field on which trying to place "O", in other case do nothing
     */
    public void placeXsColumnIfOWillMergeNearFieldsToTooLongColouredSequence(int columnIdx) {
        List<Integer> colouredFieldsIndexesInColumn = findColouredFieldsIndexesInColumn(nonogramSolutionBoard, columnIdx);

        List<List<Integer>> colouredSequencesRanges = groupConsecutiveIndices(colouredFieldsIndexesInColumn);

        int previousRowIndex;
        Field fieldWithPreviousRowColoured;
        int nextRowIndex;
        Field fieldWithNextRowColoured;

        List<List<List<Integer>>> colouredSequencesRangesWithColouredFieldAdded = createSequencesRangesWithColouredFieldAdded
                (colouredSequencesRanges);
        List<List<Integer>> currentColouredSequenceRangesWithColouredFieldAdded;

        List<Integer> sequenceWithFieldAddedBefore;
        List<Integer> mergedSequenceWithFieldAddedBefore;
        List<Integer> colouredSequenceRangeBeforeCurrent;

        List<Integer> mergedSequenceWithFieldAddedAfter;
        List<Integer> colouredSequenceRangeAfterCurrent;

        boolean colouredSequenceValid;

        for (int seqRangeIndex = 0; seqRangeIndex < colouredSequencesRanges.size(); seqRangeIndex++) {
            currentColouredSequenceRangesWithColouredFieldAdded = colouredSequencesRangesWithColouredFieldAdded.get(seqRangeIndex);

            sequenceWithFieldAddedBefore = currentColouredSequenceRangesWithColouredFieldAdded.get(0);

            if (seqRangeIndex > 0) {
                colouredSequenceRangeBeforeCurrent = colouredSequencesRanges.get(seqRangeIndex - 1);
                mergedSequenceWithFieldAddedBefore = tryToMergeColouredSequenceWithPrevious(
                        colouredSequenceRangeBeforeCurrent, sequenceWithFieldAddedBefore);
            } else {
                mergedSequenceWithFieldAddedBefore = sequenceWithFieldAddedBefore;
            }

            previousRowIndex = sequenceWithFieldAddedBefore.get(0);
            fieldWithPreviousRowColoured = new Field(previousRowIndex, columnIdx);
            colouredSequenceValid = colouredSequenceInColumnIsValid(mergedSequenceWithFieldAddedBefore, columnIdx, this);
            if (!colouredSequenceValid && isRowIndexValid(previousRowIndex) && isFieldEmpty(this.getNonogramSolutionBoard(), fieldWithPreviousRowColoured)) {
                this.placeXAtGivenField(fieldWithPreviousRowColoured);
                this.addRowToAffectedActionsByIdentifiers(previousRowIndex,
                        actionsToDoInRowAfterPlacingXInColumnIfOWillMergeNearFieldsToTooLongColouredSequence);
                this.addColumnToAffectedActionsByIdentifiers(columnIdx,
                        actionsToDoInColumnAfterPlacingXInColumnIfOWillMergeNearFieldsToTooLongColouredSequence);

                tmpLog = generatePlacingXStepDescription(columnIdx, previousRowIndex,
                        "placing \"X\" because \"O\" will create too long sequence");
                addLog(tmpLog);
                this.nonogramState.increaseMadeSteps();
            } else if (showRepetitions) {
                System.out.println("X because \"O\" will create too long sequence in column placed earlier!");
            }

            mergedSequenceWithFieldAddedAfter = currentColouredSequenceRangesWithColouredFieldAdded.get(1);

            nextRowIndex = mergedSequenceWithFieldAddedAfter.get(1);
            if (nextRowIndex != this.getHeight()) {

                fieldWithNextRowColoured = new Field(nextRowIndex, columnIdx);

                if (seqRangeIndex < colouredSequencesRanges.size() - 1) {
                    colouredSequenceRangeAfterCurrent = colouredSequencesRanges.get(seqRangeIndex + 1);
                    mergedSequenceWithFieldAddedAfter = tryToMergeColouredSequenceWithNext(
                            mergedSequenceWithFieldAddedAfter, colouredSequenceRangeAfterCurrent);
                }
                colouredSequenceValid = colouredSequenceInColumnIsValid(mergedSequenceWithFieldAddedAfter, columnIdx, this);
                if (!colouredSequenceValid && isFieldEmpty(this.getNonogramSolutionBoard(), fieldWithNextRowColoured)) {
                    this.placeXAtGivenField(fieldWithNextRowColoured);
                    this.addRowToAffectedActionsByIdentifiers(nextRowIndex,
                            actionsToDoInRowAfterPlacingXInColumnIfOWillMergeNearFieldsToTooLongColouredSequence);
                    this.addColumnToAffectedActionsByIdentifiers(columnIdx,
                            actionsToDoInColumnAfterPlacingXInColumnIfOWillMergeNearFieldsToTooLongColouredSequence);

                    tmpLog = generatePlacingXStepDescription(nextRowIndex, columnIdx,
                            "placing \"X\" because \"O\" will create too long sequence");
                    addLog(tmpLog);
                    this.nonogramState.increaseMadeSteps();
                }  else if (showRepetitions) {
                    System.out.println("X because \"O\" will create too long sequence in row placed earlier!");
                }
            }
        }
    }

    /**
     * PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE
     * @param columnIdx - TODO
     */
    public void placeXsColumnIfONearXWillBeginTooLongPossibleColouredSequence(int columnIdx) {

        Field fieldToCheckX;
        Field firstColouredField;
        List<Integer> emptyFieldsRange;
        List<Integer> colouredFieldsRange;
        int emptyFieldsRangeLength;
        int colouredFieldsRangeLength;

        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> sequencesIdsWhichWillBeginTooLongPossibleColoured;
        List<Integer> sequencesIdsWhichNotReachColouredField;

        for (int rowIdx = this.getHeight() - 1; rowIdx > 0; rowIdx--) {
            fieldToCheckX = new Field(rowIdx, columnIdx);
            if (isFieldWithX(this.getNonogramSolutionBoard(), fieldToCheckX)) {
                emptyFieldsRange = getEmptyFieldsRangeFromXToFirstColouredFieldOnTop(fieldToCheckX);

                if (!emptyFieldsRange.equals(NOT_FOUND_EMPTY_FIELDS_RANGE_VALUE)) {
                    firstColouredField = new Field(emptyFieldsRange.get(0) - 1, columnIdx);
                    colouredFieldsRange = getColouredFieldsRangeNearEmptySequenceOnTop(firstColouredField);

                    if (!colouredFieldsRange.equals(NOT_FOUND_COLOURED_FIELDS_RANGE_VALUE)) {
                        emptyFieldsRangeLength = rangeLength(emptyFieldsRange);
                        colouredFieldsRangeLength = rangeLength(colouredFieldsRange);
                        sequencesIdsWhichWillBeginTooLongPossibleColoured = new ArrayList<>();
                        sequencesIdsWhichNotReachColouredField = new ArrayList<>();
                        for (int seqNo = 0; seqNo < columnSequencesLengths.size(); seqNo++) {
                            int sequenceLength = columnSequencesLengths.get(seqNo);
                            if (rangeInsideAnotherRange(emptyFieldsRange, columnSequencesRanges.get(seqNo))
                                || columnSequenceCanFitAfterColouredField(columnIdx, seqNo, emptyFieldsRange)) {
                                if (emptyFieldsRangeLength <= sequenceLength) {
                                    sequencesIdsWhichWillBeginTooLongPossibleColoured.add(seqNo);
                                } else {
                                    sequencesIdsWhichNotReachColouredField.add(seqNo);
                                }
                            }
                        }
                        int mergedSequenceLength = emptyFieldsRangeLength + colouredFieldsRangeLength;
                        if (sequencesIdsWhichNotReachColouredField.isEmpty() &&
                                !sequencesIdsWhichWillBeginTooLongPossibleColoured.isEmpty() &&
                                sequencesIdsWhichWillBeginTooLongPossibleColoured.stream().allMatch(seqNo -> mergedSequenceLength > columnSequencesLengths.get(seqNo))) {
                            Field emptyFieldNearX = new Field(emptyFieldsRange.get(1), columnIdx);
                            if (isFieldEmpty(this.getNonogramSolutionBoard(), emptyFieldNearX)) {
                                this.placeXAtGivenField(emptyFieldNearX);
                                this.addRowToAffectedActionsByIdentifiers(emptyFieldNearX.getRowIdx(),
                                        actionsToDoInRowAfterPlacingXsInColumnIfONearWillBeginTooLongPossibleColouredSequence);

                                tmpLog = generatePlacingXStepDescription(columnIdx, rowIdx, "placing \"X\" when \"O\" near \"X\" will begin too long possible coloured sequence");
                                addLog(tmpLog);

                                this.nonogramState.increaseMadeSteps();
                            }
                        }
                    }
                }
            }
        }

        for (int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            fieldToCheckX = new Field(rowIdx, columnIdx);
            if (isFieldWithX(this.getNonogramSolutionBoard(), fieldToCheckX)) {
                emptyFieldsRange = getEmptyFieldsRangeFromXToFirstColouredFieldOnBottom(fieldToCheckX);
                if (!emptyFieldsRange.equals(NOT_FOUND_EMPTY_FIELDS_RANGE_VALUE)) {
                    firstColouredField = new Field(emptyFieldsRange.get(1) + 1, columnIdx);
                    colouredFieldsRange = getColouredFieldsRangeNearEmptySequenceOnBottom(firstColouredField);

                    if (!colouredFieldsRange.equals(NOT_FOUND_COLOURED_FIELDS_RANGE_VALUE)) {
                        emptyFieldsRangeLength = rangeLength(emptyFieldsRange);
                        colouredFieldsRangeLength = rangeLength(colouredFieldsRange);
                        sequencesIdsWhichWillBeginTooLongPossibleColoured = new ArrayList<>();
                        sequencesIdsWhichNotReachColouredField = new ArrayList<>();
                        for (int seqNo = 0; seqNo < columnSequencesLengths.size(); seqNo++) {
                            int sequenceLength = columnSequencesLengths.get(seqNo);
                            if (rangeInsideAnotherRange(emptyFieldsRange, columnSequencesRanges.get(seqNo))
                                    || columnSequenceCanFitBeforeColouredField(columnIdx, seqNo, emptyFieldsRange)) {
                                if (emptyFieldsRangeLength <= sequenceLength) {
                                    sequencesIdsWhichWillBeginTooLongPossibleColoured.add(seqNo);
                                } else {
                                    sequencesIdsWhichNotReachColouredField.add(seqNo);
                                }
                            }
                        }
                        int mergedSequenceLength = emptyFieldsRangeLength + colouredFieldsRangeLength;
                        if (sequencesIdsWhichNotReachColouredField.isEmpty() &&
                                !sequencesIdsWhichWillBeginTooLongPossibleColoured.isEmpty() &&
                                sequencesIdsWhichWillBeginTooLongPossibleColoured.stream().allMatch(seqNo -> mergedSequenceLength > columnSequencesLengths.get(seqNo))) {
                            Field emptyFieldNearX = new Field(emptyFieldsRange.get(0), columnIdx);
                            if (isFieldEmpty(this.getNonogramSolutionBoard(), emptyFieldNearX)) {
                                this.placeXAtGivenField(emptyFieldNearX);
                                this.addColumnToAffectedActionsByIdentifiers(emptyFieldNearX.getRowIdx(),
                                        actionsToDoInRowAfterPlacingXsInColumnIfONearWillBeginTooLongPossibleColouredSequence);

                                tmpLog = generatePlacingXStepDescription(columnIdx, rowIdx, "placing \"X\" when \"O\" near \"X\" will begin too long possible coloured sequence");
                                addLog(tmpLog);

                                this.nonogramState.increaseMadeSteps();
                            }

                        }
                    }
                }
            }
        }
    }

    private List<Integer> getEmptyFieldsRangeFromXToFirstColouredFieldOnTop(Field xField) {
        List<Integer> emptyFieldsRange = new ArrayList<>();
        Field fieldToCheckEmpty = new Field(xField.getRowIdx() - 1, xField.getColumnIdx());

        while (areFieldIndexesValid(fieldToCheckEmpty) && isFieldEmpty(this.getNonogramSolutionBoard(), fieldToCheckEmpty)) {
            if (emptyFieldsRange.isEmpty()) {
                emptyFieldsRange.add(fieldToCheckEmpty.getRowIdx());
            } else if (emptyFieldsRange.size() == 1) {
                emptyFieldsRange.add(0, fieldToCheckEmpty.getRowIdx());
            } else {
                emptyFieldsRange.set(0, fieldToCheckEmpty.getRowIdx());
            }
            fieldToCheckEmpty.setRowIdx(fieldToCheckEmpty.getRowIdx() - 1);
        }

        if (emptyFieldsRange.isEmpty()) {
            return List.of(-1, -1); // no empty fields (X or O just before X)
        } else if (emptyFieldsRange.size() == 1) {
            emptyFieldsRange.add(emptyFieldsRange.get(0)); // one empty field before X
        }

        return emptyFieldsRange;
    }

    private List<Integer> getColouredFieldsRangeNearEmptySequenceOnTop(Field firstSequenceField) {
        List<Integer> colouredFieldsRange = new ArrayList<>();
        Field fieldToCheckO = new Field(firstSequenceField.getRowIdx(), firstSequenceField.getColumnIdx());

        while (areFieldIndexesValid(fieldToCheckO) && isFieldColoured(this.getNonogramSolutionBoard(), fieldToCheckO)) {
            if (colouredFieldsRange.isEmpty()) {
                colouredFieldsRange.add(fieldToCheckO.getRowIdx());
            } else if (colouredFieldsRange.size() == 1) {
                colouredFieldsRange.add(0, fieldToCheckO.getRowIdx());
            } else {
                colouredFieldsRange.set(0, fieldToCheckO.getRowIdx());
            }
            fieldToCheckO.setRowIdx(fieldToCheckO.getRowIdx() - 1);
        }

        if (colouredFieldsRange.isEmpty()) {
            return List.of(-1, -1); // no coloured fields (X before - sequence)
        } else if (colouredFieldsRange.size() == 1) {
            colouredFieldsRange.add(colouredFieldsRange.get(0)); // one coloured field before empty sequence
        }

        return colouredFieldsRange;
    }

    private boolean  columnSequenceCanFitAfterColouredField(int columnIdx, int seqNo, List<Integer> emptyFieldsRange) {
        List<Integer> columnPossibleRange = this.getColumnsSequencesRanges().get(columnIdx).get(seqNo);
        int seqLength = this.getColumnsSequences().get(columnIdx).get(seqNo);
        List<Integer> lastRangeAfterColouredField = List.of(columnPossibleRange.get(0), columnPossibleRange.get(0) + seqLength - 1);

        return rangeInsideAnotherRange(lastRangeAfterColouredField, emptyFieldsRange);
    }

    private List<Integer> getEmptyFieldsRangeFromXToFirstColouredFieldOnBottom(Field xField) {
        List<Integer> emptyFieldsRange = new ArrayList<>();
        Field fieldToCheckEmpty = new Field(xField.getRowIdx() + 1, xField.getColumnIdx());

        while (areFieldIndexesValid(fieldToCheckEmpty) && isFieldEmpty(this.getNonogramSolutionBoard(), fieldToCheckEmpty)) {
            if (emptyFieldsRange.isEmpty()) {
                emptyFieldsRange.add(fieldToCheckEmpty.getRowIdx());
            } else if (emptyFieldsRange.size() == 1) {
                emptyFieldsRange.add(1, fieldToCheckEmpty.getRowIdx()); // -> direction, new column index higher than earlier
            } else {
                emptyFieldsRange.set(1, fieldToCheckEmpty.getRowIdx());
            }
            fieldToCheckEmpty.setRowIdx(fieldToCheckEmpty.getRowIdx() + 1);
        }

        if (emptyFieldsRange.isEmpty()) {
            return List.of(-1, -1); // no empty fields (X or O just before X)
        } else if (emptyFieldsRange.size() == 1) {
            emptyFieldsRange.add(emptyFieldsRange.get(0)); // one empty field after X
        }

        return emptyFieldsRange;
    }

    private List<Integer> getColouredFieldsRangeNearEmptySequenceOnBottom(Field firstSequenceField) {
        List<Integer> colouredFieldsRange = new ArrayList<>();
        Field fieldToCheckO = new Field(firstSequenceField.getRowIdx(), firstSequenceField.getColumnIdx());

        while (areFieldIndexesValid(fieldToCheckO) && isFieldColoured(this.getNonogramSolutionBoard(), fieldToCheckO)) {
            if (colouredFieldsRange.isEmpty()) {
                colouredFieldsRange.add(fieldToCheckO.getRowIdx());
            } else if (colouredFieldsRange.size() == 1) {
                colouredFieldsRange.add(1, fieldToCheckO.getRowIdx());
            } else {
                colouredFieldsRange.set(1, fieldToCheckO.getRowIdx());
            }
            fieldToCheckO.setRowIdx(fieldToCheckO.getRowIdx() + 1);
        }

        if (colouredFieldsRange.isEmpty()) {
            return List.of(-1, -1); // no coloured fields (X before - sequence)
        } else if (colouredFieldsRange.size() == 1) {
            colouredFieldsRange.add(colouredFieldsRange.get(0)); // one coloured field before empty sequence
        }

        return colouredFieldsRange;
    }

    private boolean columnSequenceCanFitBeforeColouredField(int columnIdx, int seqNo, List<Integer> emptyFieldsRange) {
        List<Integer> columnPossibleRange = this.getColumnsSequencesRanges().get(columnIdx).get(seqNo);
        int seqLength = this.getColumnsSequences().get(columnIdx).get(seqNo);
        List<Integer> lastRangeBeforeColouredField = List.of(columnPossibleRange.get(1) - seqLength + 1, columnPossibleRange.get(1));

        return rangeInsideAnotherRange(lastRangeBeforeColouredField, emptyFieldsRange);
    }

    public void preventExtendingColouredSequenceToExcessLengthInColumn(int columnIdx) {
        preventExtendingColouredSequenceToExcessLengthInColumnToTop(columnIdx);
        preventExtendingColouredSequenceToExcessLengthInColumnToBottom(columnIdx);
    }

    private void preventExtendingColouredSequenceToExcessLengthInColumnToTop(int columnIdx) {
        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);

        Field fieldToCheckX;

        int potentiallyColouredFieldRow;
        Field fieldToCheckColoured;

        List<Integer> sequencesIds;
        int maxSequenceLength;
        List<List<Integer>> colouredSequences;
        List<Integer> validSequenceIds;
        List<Integer> validSequenceLengths;

        for (int rowIdx = this.getHeight() - 1; rowIdx > 0; rowIdx--) {
            fieldToCheckX = new Field(rowIdx, columnIdx);
            if (isFieldWithX(this.getNonogramSolutionBoard(), fieldToCheckX)) {
                potentiallyColouredFieldRow = fieldToCheckX.getRowIdx() - 1;
                fieldToCheckColoured = new Field(potentiallyColouredFieldRow, fieldToCheckX.getColumnIdx());

                if (isFieldColoured(this.getNonogramSolutionBoard(), fieldToCheckColoured)) {
                    sequencesIds = sequencesIdsInColumnIncludingField(columnSequencesRanges, fieldToCheckColoured);

                    List<Integer> sequencesLengths = sequencesIds.stream().map(columnSequencesLengths::get).toList();

                    maxSequenceLength = Collections.max(sequencesLengths);

                    colouredSequences = getColouredSequencesRangesInColumnInRangeToTop(this.getNonogramSolutionBoard(), columnIdx, potentiallyColouredFieldRow, maxSequenceLength);

                    validSequenceIds = findValidSequencesIdsMergingToTop(sequencesIds, sequencesLengths, potentiallyColouredFieldRow, colouredSequences);

                    validSequenceLengths = validSequenceIds.stream()
                            .map(sequencesIds::indexOf)
                            .map(sequencesLengths::get)
                            .toList();

                    // only one length is valid
                    if (validSequenceLengths.stream().distinct().count() == 1) {
                        int sequenceLength = validSequenceLengths.get(0);
                        int colouredSequenceRowStartIdx = potentiallyColouredFieldRow - sequenceLength + 1;
                        Field fieldToColour;

                        for (int rowToColourIdx = colouredSequenceRowStartIdx; rowToColourIdx <= potentiallyColouredFieldRow; rowToColourIdx++) {
                            fieldToColour = new Field(rowToColourIdx, columnIdx);
                            if (isFieldEmpty(this.getNonogramSolutionBoard(), fieldToColour)) {
                                this.colourFieldAtGivenPosition(fieldToColour, "--C-");
                                this.addRowToAffectedActionsByIdentifiers(fieldToColour.getRowIdx(),
                                        actionsToDoInRowDuringColouringPartPreventingExcessLengthInColumns);
                                this.nonogramState.increaseMadeSteps();

                                tmpLog = generateColourStepDescription(columnIdx, rowToColourIdx, "extend coloured sequence to matching length to top near X (with placing X before)");
                            }
                        }

                        Field fieldToPlaceX = new Field(colouredSequenceRowStartIdx - 1, columnIdx);
                        this.placeXAtGivenField(fieldToPlaceX);
                        this.excludeFieldInRow(fieldToPlaceX);
                        this.excludeFieldInColumn(fieldToPlaceX);
                        this.addRowToAffectedActionsByIdentifiers(fieldToPlaceX.getRowIdx(),
                                actionsToDoInRowDuringPlacingXPartPreventingExcessLengthInColumns);

                        // moreover - only one id is valid -> can correct sequence range
                        if (validSequenceIds.size() == 1) {
                            int matchingSeqId = validSequenceIds.get(0);
                            List<Integer> oldRange = columnSequencesRanges.get(matchingSeqId);
                            List<Integer> updatedRange = new ArrayList<>(Arrays.asList(colouredSequenceRowStartIdx, potentiallyColouredFieldRow));

                            this.updateColumnSequenceRange(columnIdx, matchingSeqId, updatedRange);
                            this.addColumnToAffectedActionsByIdentifiers(columnIdx, actionsToDoInColumnAfterUpdateOnlyMatchingSequencePartPreventingExcessLengthInColumns);

                            this.nonogramState.increaseMadeSteps();
                            tmpLog = generateCorrectingColumnSequenceRangeStepDescription(columnIdx, matchingSeqId, oldRange, updatedRange, "update only matching sequence part preventing excess length to top");
                        }
                    }
                }
            }
        }
    }

    private void preventExtendingColouredSequenceToExcessLengthInColumnToBottom(int columnIdx) {
        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);

        Field fieldToCheckX;

        int potentiallyColouredFieldRowIndex;
        Field fieldToCheckColoured;

        List<Integer> sequencesIds;
        int maxSequenceLength;
        List<List<Integer>> colouredSequences;
        List<Integer> validSequencesIds;
        List<Integer> validSequenceLengths;

        for (int rowIdx = 0; rowIdx < this.getHeight() - 1; rowIdx++) {
            fieldToCheckX = new Field(rowIdx, columnIdx);
            if (isFieldWithX(this.getNonogramSolutionBoard(), fieldToCheckX)) {
                potentiallyColouredFieldRowIndex = fieldToCheckX.getRowIdx() + 1;
                fieldToCheckColoured = new Field(potentiallyColouredFieldRowIndex, fieldToCheckX.getColumnIdx());

                if (isFieldColoured(this.getNonogramSolutionBoard(), fieldToCheckColoured)) {
                    sequencesIds = sequencesIdsInColumnIncludingField(columnSequencesRanges, fieldToCheckColoured);

                    List<Integer> sequencesLengths = sequencesIds.stream().map(columnSequencesLengths::get).toList();

                    maxSequenceLength = Collections.max(sequencesLengths);

                    colouredSequences = getColouredSequencesRangesInColumnInRangeToBottom(this.getNonogramSolutionBoard(), columnIdx, potentiallyColouredFieldRowIndex, maxSequenceLength);

                    validSequencesIds = findValidSequencesIdsMergingToBottom(sequencesIds, sequencesLengths, potentiallyColouredFieldRowIndex, colouredSequences);

                    validSequenceLengths = validSequencesIds.stream()
                            .map(sequencesIds::indexOf)
                            .map(sequencesLengths::get)
                            .toList();

                    if (validSequenceLengths.stream().distinct().count() == 1) {
                        int sequenceLength = validSequenceLengths.get(0);
                        int colouredSequenceEndRowIndex = potentiallyColouredFieldRowIndex + sequenceLength - 1;
                        Field fieldToColour;

                        for (int rowToColourIdx = potentiallyColouredFieldRowIndex; rowToColourIdx <= colouredSequenceEndRowIndex; rowToColourIdx++) {
                            fieldToColour = new Field(rowToColourIdx, columnIdx);
                            if (isFieldEmpty(this.getNonogramSolutionBoard(), fieldToColour)) {
                                this.colourFieldAtGivenPosition(fieldToColour, "--C-");
                                this.addRowToAffectedActionsByIdentifiers(fieldToColour.getRowIdx(),
                                        actionsToDoInRowDuringColouringPartPreventingExcessLengthInColumns);
                                this.nonogramState.increaseMadeSteps();

                                tmpLog = generateColourStepDescription(columnIdx, rowToColourIdx, "extend coloured sequence to matching length to bottom near X (with placing X before)");
                                addLog(tmpLog);
                            }
                        }

                        Field fieldToPlaceX = new Field(colouredSequenceEndRowIndex + 1, columnIdx);
                        this.placeXAtGivenField(fieldToPlaceX);
                        this.excludeFieldInColumn(fieldToPlaceX);
                        this.excludeFieldInRow(fieldToPlaceX);
                        this.addRowToAffectedActionsByIdentifiers(fieldToPlaceX.getRowIdx(),
                                actionsToDoInRowDuringPlacingXPartPreventingExcessLengthInColumns);

                        if (validSequencesIds.size() == 1) {
                            int matchingSeqId = validSequencesIds.get(0);
                            List<Integer> oldRange = columnSequencesRanges.get(matchingSeqId);
                            List<Integer> updatedRange = new ArrayList<>(Arrays.asList(potentiallyColouredFieldRowIndex, colouredSequenceEndRowIndex));

                            this.updateColumnSequenceRange(columnIdx, matchingSeqId, updatedRange);
                            this.addColumnToAffectedActionsByIdentifiers(columnIdx, actionsToDoInColumnAfterUpdateOnlyMatchingSequencePartPreventingExcessLengthInColumns);

                            this.nonogramState.increaseMadeSteps();
                            tmpLog = generateCorrectingColumnSequenceRangeStepDescription(columnIdx, matchingSeqId, oldRange, updatedRange, "update only matching sequence part preventing excess length to bottom");
                        }
                    }
                }
            }
        }
    }

    /**
     * MARK_AVAILABLE_FIELDS_IN_COLUMN
     * @param columnIdx - column index on which mark fields with char sequences identifiers
     */
    public void markAvailableFieldsInColumn(int columnIdx) {
        Field potentiallyColouredField;
        List<Integer> colouredSequenceIndexes;
        int firstSequenceIndex;
        int lastSequenceIndex;
        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequenceRange;
        int matchingSequencesCount;
        int lastMatchingSequenceIndex = -1;
        int colouredSequenceLength;
        String sequenceMarker;

        List<Integer> onlyMatchingSequenceOldRange;
        List<Integer> newSequenceRange;

        for (int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(nonogramSolutionBoard, potentiallyColouredField)) {

                colouredSequenceIndexes = new ArrayList<>();
                colouredSequenceIndexes.add(rowIdx);

                // TODO - do while(?)
                while(rowIdx < this.getHeight() && isFieldColoured(nonogramSolutionBoard, potentiallyColouredField)) {
                    rowIdx++;
                    potentiallyColouredField = new Field(rowIdx, columnIdx);
                }

                colouredSequenceIndexes.add(rowIdx - 1);

                firstSequenceIndex = colouredSequenceIndexes.get(0);
                lastSequenceIndex = colouredSequenceIndexes.get(1);
                colouredSequenceLength = lastSequenceIndex - firstSequenceIndex  + 1;

                matchingSequencesCount = 0;

                for (int seqNo = 0; seqNo < columnSequencesLengths.size(); seqNo++) {
                    columnSequenceRange = columnSequencesRanges.get(seqNo);

                    if ( rangeInsideAnotherRange(colouredSequenceIndexes, columnSequenceRange)
                            && colouredSequenceLength <= columnSequencesLengths.get(seqNo)) {
                        matchingSequencesCount++;
                        lastMatchingSequenceIndex = seqNo;
                    }
                }

                if (matchingSequencesCount == 1) {

                    sequenceMarker = NonogramLogic.indexToSequenceCharMark(lastMatchingSequenceIndex);
                    for (int sequenceRowIdx = firstSequenceIndex; sequenceRowIdx <= lastSequenceIndex; sequenceRowIdx++) {
                        if (this.getNonogramSolutionBoardWithMarks().get(sequenceRowIdx).get(columnIdx).substring(3).equals(EMPTY_FIELD)) {
                            this.markColumnBoardField(sequenceRowIdx, columnIdx, sequenceMarker);
                            this.nonogramState.increaseMadeSteps();
                        } else if (this.showRepetitions) {
                            System.out.println("Column field was marked earlier.");
                        }
                    }

                    //correct sequence range if new range is shorter
                    onlyMatchingSequenceOldRange = columnSequencesRanges.get(lastMatchingSequenceIndex);
                    newSequenceRange = calculateNewMarkedRangeFromParameters(onlyMatchingSequenceOldRange, colouredSequenceIndexes,
                            columnSequencesLengths.get(lastMatchingSequenceIndex));

                    if (!rangesEqual(onlyMatchingSequenceOldRange, newSequenceRange)) {
                        this.changeColumnSequenceRange(columnIdx, lastMatchingSequenceIndex, newSequenceRange);
                        tmpLog = generateCorrectingColumnSequenceRangeStepDescription(columnIdx, lastMatchingSequenceIndex, onlyMatchingSequenceOldRange, newSequenceRange, CORRECT_COLUMN_SEQ_RANGE_MARKING_FIELD);
                        addLog(tmpLog);
                        addColumnToAffectedActionsByIdentifiers(columnIdx, actionsToDoAfterCorrectingRangesWhenMarkingSequencesInColumns);
                    }
                }
            }
        }
    }

    // TODO - extract
    private List<Integer> calculateNewMarkedRangeFromParameters(List<Integer> oldRange,
                                                                List<Integer> colouredSequenceIndexes,
                                                                int sequenceLength) {
        int newRangeBegin = Math.max(oldRange.get(0), colouredSequenceIndexes.get(1) - sequenceLength + 1);
        int newRangeEnd = Math.min(oldRange.get(1), colouredSequenceIndexes.get(0) + sequenceLength - 1);
        return List.of(newRangeBegin, newRangeEnd);
    }

    private boolean isRowRangeColoured(int columnIdx, List<Integer> rowRange) {
        Field potentiallyColouredField;
        for (Integer rowIdx : rowRange) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (!isFieldColoured(nonogramSolutionBoard, potentiallyColouredField)) {
                return false;
            }
        }

        return true;
    }

    public int minimumRowIndexWithoutX(int columnIdx, int lastSequenceRowIdx, int sequenceFullLength) {
        int minimumRowIndex = lastSequenceRowIdx;
        int minimumRowIndexLimit = Math.max(lastSequenceRowIdx - sequenceFullLength + 1, 0);
        Field fieldToCheck;

        for (; minimumRowIndex >= minimumRowIndexLimit; minimumRowIndex--) {
            fieldToCheck = new Field(minimumRowIndex, columnIdx);
            if (isFieldWithX(nonogramSolutionBoard, fieldToCheck)) {
                break;
            }
        }

        return minimumRowIndex + 1;
    }

    public int maximumRowIndexWithoutX(int columnIdx, int firstSequenceRowIdx, int sequenceFullLength) {
        int maximumRowIndex = firstSequenceRowIdx;
        int maximumRowIndexLimit = Math.min(firstSequenceRowIdx + sequenceFullLength - 1, this.getHeight() - 1);
        Field fieldToCheck;

        for (; maximumRowIndex <= maximumRowIndexLimit; maximumRowIndex++) {
            fieldToCheck = new Field(maximumRowIndex, columnIdx);
            if (isFieldWithX(nonogramSolutionBoard, fieldToCheck)) {
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
