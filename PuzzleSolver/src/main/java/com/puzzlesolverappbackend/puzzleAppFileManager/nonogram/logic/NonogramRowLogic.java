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
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.NonogramConstants.MARKED_ROW_INDICATOR;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.NonogramParametersComparatorHelper.rangesEqual;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.NonogramLogicService.filterSequencesRangesIncludingAnotherAndReturnCorrespondingLengths;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.NonogramLogicService.rangesListIncludingAnotherRange;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.RowMixedActionsHelper.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.ActionsConstants.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramBoardUtils.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramLogicUtils.colouredSequenceInRowIsValid;
import static com.puzzlesolverappbackend.puzzleAppFileManager.utils.ArrayUtils.rangeInsideAnotherRange;
import static com.puzzlesolverappbackend.puzzleAppFileManager.utils.ArrayUtils.rangeLength;

@Setter
@Getter
@Slf4j
@NoArgsConstructor
public class NonogramRowLogic extends NonogramLogicParams {

    private final static String CORRECT_ROW_SEQ_RANGE_MARKING_FIELD = "correcting row sequence range when marking field";

    private final static String FILL_OVERLAPPING_FIELDS = "fill overlapping fields";

    private final static List<Integer> NOT_FOUND_EMPTY_FIELDS_RANGE_VALUE = List.of(-1, -1);

    private final static List<Integer> NOT_FOUND_COLOURED_FIELDS_RANGE_VALUE = List.of(-1, -1);

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

    // CORRECT_ROW_SEQUENCES_RANGES
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

        for (int sequenceIdx = 0; sequenceIdx < rowSequencesRanges.size() - 1; sequenceIdx++) {
            nextSequenceId = sequenceIdx + 1;
            if (!rowSequencesIdsNotToInclude.contains(nextSequenceId)) {
                if (rowSequencesIdsNotToInclude.contains(sequenceIdx)) {
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
                } else {
                    currentSequenceRange = rowSequencesRanges.get(sequenceIdx);
                    nextSequenceRange = rowSequencesRanges.get(nextSequenceId);

                    updatedNextSequenceBeginRangeColumnIndex = currentSequenceRange.get(0) + rowSequencesLengths.get(sequenceIdx) + 1;

                    nextSequenceOldBeginRangeColumnIndex = nextSequenceRange.get(0);

                    updatedStartIndex = Math.max(nextSequenceOldBeginRangeColumnIndex, updatedNextSequenceBeginRangeColumnIndex);
                    updatedNextSequenceRange = new ArrayList<>(Arrays.asList(updatedStartIndex, nextSequenceRange.get(1)));

                    correctRowRangeFromLeft(rowIdx, rowSequencesLengths, nextSequenceRange, updatedStartIndex, nextSequenceOldBeginRangeColumnIndex, nextSequenceId, updatedNextSequenceRange);
                }
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
            this.addRowToAffectedActionsByIdentifiers(rowIdx, actionsToDoAfterCorrectingRowsSequencesRanges);
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

        for (int sequenceIdx = rowSequencesRanges.size() - 1; sequenceIdx > 0; sequenceIdx--) {
            previousSequenceId = sequenceIdx - 1;
            if (!rowSequencesIdsNotToInclude.contains(previousSequenceId)) {
                if (rowSequencesIdsNotToInclude.contains(sequenceIdx)) {
                    fullSequenceRange = rowSequencesRanges.get(sequenceIdx);

                    oldPreviousSequenceRange = rowSequencesRanges.get(previousSequenceId);
                    previousSequenceUpdatedRangeEndColumnIndex = fullSequenceRange.get(0) - 2;

                    while(rowFieldsNotToInclude.contains(previousSequenceUpdatedRangeEndColumnIndex)) {
                        previousSequenceUpdatedRangeEndColumnIndex--;
                    }

                } else {
                    currentSequenceLength = rowSequencesLengths.get(sequenceIdx);
                    currentSequenceRange = rowSequencesRanges.get(sequenceIdx);
                    oldPreviousSequenceRange = rowSequencesRanges.get(previousSequenceId);

                    previousSequenceUpdatedRangeEndColumnIndex = currentSequenceRange.get(1) - currentSequenceLength - 1;

                }
                previousSequenceOldRangeEndColumnIndex = oldPreviousSequenceRange.get(1);
                updatedEndIndex = Math.min(previousSequenceOldRangeEndColumnIndex, previousSequenceUpdatedRangeEndColumnIndex);
                updatedPreviousSequenceRange = new ArrayList<>(Arrays.asList(oldPreviousSequenceRange.get(0), updatedEndIndex));
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
            this.addRowToAffectedActionsByIdentifiers(rowIdx, actionsToDoAfterCorrectingRowsSequencesRanges);

            if (rangeLength(updatedPreviousSequenceRange) == rowSequencesLengths.get(previousSequenceId) && isColumnRangeColoured(rowIdx, updatedPreviousSequenceRange)) {
                this.excludeSequenceInRow(rowIdx, previousSequenceId);
            }

            this.nonogramState.increaseMadeSteps();
            tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, previousSequenceId, oldPreviousSequenceRange, updatedPreviousSequenceRange, "correcting from right");
            addLog(tmpLog);
        }
    }

    /**
     * CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS
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

        for (int columnIdx = 0; columnIdx < this.getWidth(); columnIdx++) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(nonogramSolutionBoard, potentiallyColouredField)) {
                oldSequenceRange = rowSequencesRanges.get(sequenceId);
                rowSequenceRangeStart = oldSequenceRange.get(0);
                rowSequenceRangeEnd = oldSequenceRange.get(1);
                maximumPossibleSequenceRangeEnd = columnIdx + sequenceLength - 1;

                updatedRangeEndIndex = Math.min(rowSequenceRangeEnd, maximumPossibleSequenceRangeEnd);
                updatedSequenceRange = new ArrayList<>(Arrays.asList(rowSequenceRangeStart, updatedRangeEndIndex));

                if (updatedRangeEndIndex != rowSequenceRangeEnd) {
                    rowSequenceRangesChanged = true;
                    this.getRowsSequencesRanges().get(rowIdx).set(sequenceId, updatedSequenceRange);
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
            addRowToAffectedActionsByIdentifiers(rowIdx, actionsToDoAfterCorrectingRowsSequencesWhenMetColouredField);
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

        for (int columnIdx = this.getWidth() - 1; columnIdx >= 0; columnIdx--) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(nonogramSolutionBoard, potentiallyColouredField)) {
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
            addRowToAffectedActionsByIdentifiers(rowIdx, actionsToDoAfterCorrectingRowsSequencesWhenMetColouredField);
        }
    }

    /**
     * CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY
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

        for (int seqNo = 0; seqNo < rowSequencesRanges.size(); seqNo++) {
            if (!excludedRowSequences.contains(seqNo)) {

                rowSequenceRange = rowSequencesRanges.get(seqNo);
                rowSequenceLength = rowSequencesLengths.get(seqNo);

                updatedRowRangeStartIndex = getUpdatedRowRangeStartIndexBecauseXOnWay(rowIdx, rowSequenceRange, rowSequenceLength);
                updatedRowRangeEndIndex = getUpdatedRowRangeEndIndexBecauseXOnWay(rowIdx, rowSequenceRange, rowSequenceLength);
                updatedRange = new ArrayList<>(Arrays.asList(updatedRowRangeStartIndex, updatedRowRangeEndIndex));

                if (!rangesEqual(rowSequenceRange, updatedRange)) {
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
            addRowToAffectedActionsByIdentifiers(rowIdx, actionsToDoAfterCorrectingRowsSequencesIfXOnWay);
        }
    }

    private int getUpdatedRowRangeStartIndexBecauseXOnWay(int rowIdx,
                                                          List<Integer> rowSequenceRange,
                                                          int rowSequenceLength) {
        Field potentiallyXOnWayField;

        int updatedRowRangeStartIndex = rowSequenceRange.get(0);
        boolean indexOk;

        // TODO - optimization(?) -> if isFieldWithX on rowIdx then columnStartIndex := columnIdx + 1 (next near X)
        for (int columnStartIndex = rowSequenceRange.get(0); columnStartIndex <= rowSequenceRange.get(1) - rowSequenceLength + 1; columnStartIndex++) {
            indexOk = true;

            for (int columnIdx = columnStartIndex; columnIdx < columnStartIndex + rowSequenceLength; columnIdx++) {
                potentiallyXOnWayField = new Field(rowIdx, columnIdx);
                if (isFieldWithX(nonogramSolutionBoard, potentiallyXOnWayField)) {
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

        // TODO - optimization(?) -> if isFieldWithX on rowIdx then columnStartIndex := columnIdx - 1 (previous near X)
        for (int columnEndIndex = rowSequenceRange.get(1); columnEndIndex > rowSequenceRange.get(0) + rowSequenceLength - 1; columnEndIndex--) {
            indexOk = true;
            for (int columnIdx = columnEndIndex; columnIdx > columnEndIndex - rowSequenceLength; columnIdx--) {
                potentiallyXOnWayField = new Field(rowIdx, columnIdx);
                if (isFieldWithX(nonogramSolutionBoard, potentiallyXOnWayField)) {
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
     * CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES
     * @param rowIdx - row to correct sequence/s range/s when matching fields to corresponding sequences
     */
    public void correctRowSequencesRangesWhenMatchingFieldsToSequences(int rowIdx) {
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);

        Field fieldToCheckIfIsColoured;
        List<List<Integer>> colouredSequencesPartsRanges = new ArrayList<>();
        List<Integer> colouredSequencePartRange;

        // extract coloured sequences ranges from board
        for (int columnIdx = 0; columnIdx < this.getWidth(); columnIdx++) {
            fieldToCheckIfIsColoured = new Field(rowIdx, columnIdx);
            if (isFieldColoured(this.nonogramSolutionBoard, fieldToCheckIfIsColoured)) {
                colouredSequencePartRange = getColouredRangeInRowNearField(fieldToCheckIfIsColoured);
                colouredSequencesPartsRanges.add(colouredSequencePartRange);
                columnIdx = colouredSequencePartRange.get(1);
            }
        }

        List<List<Integer>> partsMaxRanges = new ArrayList<>();
        List<Integer> partMaxRange;

        // calculate max possible ranges for corresponding coloured sequences
        for (List<Integer> colouredSequencesPartsRange : colouredSequencesPartsRanges) {
            partMaxRange = getRowSequenceMaxPossibleRange(rowIdx, colouredSequencesPartsRange);
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
            for (int seqNo = minSeqNo; seqNo < rowSequencesRanges.size(); seqNo++) {
                if (rangeInsideAnotherRange(currentColouredSeqPart, rowSequencesRanges.get(seqNo)) &&
                        rowSequencesLengths.get(seqNo) <= partMaxLength) {
                    colouredSequencePartMatches.add(seqNo);
                }
            }
            colouredSequencesPartsMatches.add(colouredSequencePartMatches);
            if (seqPartNo < partsMaxRanges.size() - 1 &&
                    areXsBetweenColouredRangesInRow(rowIdx, currentColouredSeqPart, colouredSequencesPartsRanges.get(seqPartNo + 1))) {
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
                oldMatchingSequenceRange = rowSequencesRanges.get(matchingSeqId);
                matchingSequenceLength = rowSequencesLengths.get(matchingSeqId);

                firstColouredPossibleRowAccordingToSequenceRanges = colouredSequencesPartsRanges.get(matchedSeqNo).get(1) - matchingSequenceLength + 1;
                updatedMatchingSequenceRangeStartIndex = Math.max(oldMatchingSequenceRange.get(0), firstColouredPossibleRowAccordingToSequenceRanges);

                lastColouredPossibleRowAccordingToSequenceRanges = colouredSequencesPartsRanges.get(matchedSeqNo).get(0) + matchingSequenceLength - 1;
                updatedMatchingSequenceRangeEndIndex = Math.min(oldMatchingSequenceRange.get(1), lastColouredPossibleRowAccordingToSequenceRanges);

                updatedMatchingSequenceRange = new ArrayList<>(Arrays.asList(updatedMatchingSequenceRangeStartIndex, updatedMatchingSequenceRangeEndIndex));

                if (!rangesEqual(oldMatchingSequenceRange, updatedMatchingSequenceRange)) {
                    this.updateRowSequenceRange(rowIdx, matchingSeqId, updatedMatchingSequenceRange);
                    this.addRowToAffectedActionsByIdentifiers(rowIdx, actionsToDoInRowAfterCorrectingColumnsSequencesRangesWhenMatchingFieldsToSequences);
                    this.nonogramState.increaseMadeSteps();
                    tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, matchingSeqId, oldMatchingSequenceRange, updatedMatchingSequenceRange, "correcting sequence when matching fields to only possible coloured sequences");
                    addLog(tmpLog);

                    if (rangeLength(updatedMatchingSequenceRange) == rowSequencesLengths.get(matchingSeqId) && isColumnRangeColoured(rowIdx, updatedMatchingSequenceRange)) {
                        this.excludeSequenceInRow(rowIdx, matchingSeqId);
                    }
                }
            }
        }
    }

    private List<Integer> getColouredRangeInRowNearField(Field colouredField) {
        List<Integer> colouredSequenceRangeNearField = new ArrayList<>(List.of(colouredField.getColumnIdx()));
        int rowIdx = colouredField.getRowIdx();

        int columnToRight = colouredField.getColumnIdx() + 1;
        Field fieldRight = new Field(rowIdx, columnToRight);

        while (columnToRight < this.getWidth() && isFieldColoured(this.getNonogramSolutionBoard(), fieldRight)) {
            fieldRight = new Field(rowIdx, ++columnToRight);
        }
        fieldRight = new Field(rowIdx, --columnToRight);

        colouredSequenceRangeNearField.add(fieldRight.getColumnIdx());

        return colouredSequenceRangeNearField;
    }

    private List<Integer> getRowSequenceMaxPossibleRange(int rowIdx, List<Integer> colouredSequencePartRange) {
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

    private boolean areXsBetweenColouredRangesInRow(int rowIdx, List<Integer> firstRange, List<Integer> secondRange) {
        if (firstRange.size() != 2 || secondRange.size() != 2 || firstRange.get(1) >= secondRange.get(0)) {
            return false;
        }

        List<Integer> rangeBetweenColouredRanges = new ArrayList<>(Arrays.asList(firstRange.get(1) + 1, secondRange.get(0) - 1));

        Field fieldToCheck;

        for (int columnIdx : rangeBetweenColouredRanges) {
            fieldToCheck = new Field(rowIdx, columnIdx);
            if (isFieldWithX(this.getNonogramSolutionBoard(), fieldToCheck)) {
                return true;
            }
        }

        return false;
    }

    /**
     *  CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE
     * @param rowIdx - row to correct sequence/s range/s if sequence possible start/end index if one of 'edge indexes'
     *              f.e. in:
     *               rowSequencesRanges.get(0).get(3) = [23, 28] -> edge indexes : 23, 28
     *               and field with column index 22 or 29 is coloured -> then sequence which start at idx 23/28 will create too long coloured sequence
     */
    public void correctRowSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(int rowIdx) {
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequenceRange;
        int rangeStartIndex;
        int rangeEndIndex;

        List<Integer> updatedSequenceRange;

        boolean anySequenceWasUpdated = false;

        for (int seqNo = 0; seqNo < rowSequencesRanges.size(); seqNo++) {
            rowSequenceRange = rowSequencesRanges.get(seqNo);
            rangeStartIndex = rowSequenceRange.get(0);
            rangeEndIndex = rowSequenceRange.get(1);

            updatedSequenceRange = new ArrayList<>();
            if (rangeStartIndex >= 1 && isFieldColoured(this.nonogramSolutionBoard, new Field(rowIdx, rangeStartIndex - 1))) {
                updatedSequenceRange.add(rangeStartIndex + 1);
            } else {
                updatedSequenceRange.add(rangeStartIndex);
            }

            if (rangeEndIndex < this.getWidth() - 1 && isFieldColoured(this.nonogramSolutionBoard, new Field(rowIdx, rangeEndIndex + 1))) {
                updatedSequenceRange.add(rangeEndIndex - 1);
            } else {
                updatedSequenceRange.add(rangeEndIndex);
            }

            if (!updatedSequenceRange.equals(rowSequenceRange)) {
                anySequenceWasUpdated = true;
                this.updateRowSequenceRange(rowIdx, seqNo, updatedSequenceRange);
                this.nonogramState.increaseMadeSteps();
                tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, seqNo, rowSequenceRange, updatedSequenceRange, "correcting row sequence range when start from edge index will create too long sequence.");
                addLog(tmpLog);
            }
        }

        if (anySequenceWasUpdated) {
            this.addRowToAffectedActionsByIdentifiers(rowIdx, actionsToDoInRowAfterCorrectingRowSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence);
        }
    }

    /**
     * COLOUR_OVERLAPPING_FIELDS_IN_ROW
     * @param rowIdx - row in which action should be made
     * fill fields in row where ranges met specific condition (range_length < sequence_length * 2)
     */
    public void colourOverlappingFieldsInRow(int rowIdx) {
        List<Integer> sequencesInRowLengths = this.getRowsSequences().get(rowIdx);
        List<List<Integer>> sequencesInRowRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> range;

        int sequenceLength;
        int rangeBeginIndex;
        int rangeEndIndex;
        List<Integer> colouringRange;
        Field fieldToColour;

        for (int sequenceIdx = 0; sequenceIdx < sequencesInRowLengths.size(); sequenceIdx++) {
            sequenceLength = sequencesInRowLengths.get(sequenceIdx);
            range = sequencesInRowRanges.get(sequenceIdx);
            rangeBeginIndex = range.get(0);
            rangeEndIndex = range.get(1);

            colouringRange = IntStream.rangeClosed(rangeEndIndex - sequenceLength + 1, rangeBeginIndex + sequenceLength - 1)
                    .boxed()
                    .toList();

            for (int columnIdx : colouringRange) {
                fieldToColour = new Field(rowIdx, columnIdx);

                if (isFieldEmpty(nonogramSolutionBoard, fieldToColour)) {
                    this.colourFieldAtGivenPosition(fieldToColour, "R---");
                    tmpLog = generateColourStepDescription(rowIdx, columnIdx, FILL_OVERLAPPING_FIELDS);
                    addLog(tmpLog);
                    this.addColumnToAffectedActionsByIdentifiers(columnIdx, actionsToDoInColumnAfterColouringOverlappingSequencesInRows);
                    this.addRowToAffectedActionsByIdentifiers(rowIdx, actionsToDoInRowAfterColouringOverlappingSequencesInRows);
                    this.getNonogramState().increaseMadeSteps();
                } else if (showRepetitions) {
                    logger.warn("Row field was coloured earlier!");
                }

                // TODO - check if need to mark sequence before excluding
                if (rangeLength(colouringRange) == rowsSequences.get(rowIdx).get(sequenceIdx)) {
                    this.excludeSequenceInRow(rowIdx, sequenceIdx);
                }
            }
        }
    }

    /**
     * EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW
     * @param rowIdx - row in which action should be made
     * coloring the fields in row next to the x to the distance of the shortest possible sequence in a given area
     */
    public void extendColouredFieldsNearXToMaximumPossibleLengthInRow(int rowIdx) {
        extendColouredFieldsToLeftNearXToMaximumPossibleLengthInRow(rowIdx);
        extendColouredFieldsToRightNearXToMaximumPossibleLengthInRow(rowIdx);
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

        for (int columnIdx = this.getWidth() - 1; columnIdx >= 0; columnIdx--) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(nonogramSolutionBoard, potentiallyColouredField)) {
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
                for (int columnsToX = rangeLength(colouredSubsequenceRange); colouredSubsequenceRange.get(0) + columnsToX < this.getWidth() && columnsToX < minimumPossibleLength; columnsToX++) {
                    potentiallyXField = new Field(rowIdx, colouredSubsequenceRange.get(0) + columnsToX);
                    if (isFieldWithX(nonogramSolutionBoard, potentiallyXField)) {
                        distanceToX = columnsToX;
                        break;
                    }
                }

                /* there could be situation when 'X' is f.e. too far from coloured sequence(distanceToX not changed), check that case*/
                if (distanceToX != 0) {
                    int mostLeftSequenceToExtendColumnIndex = colouredSubsequenceRange.get(0) + distanceToX - minimumPossibleLength;
                    for (int colourColumnIdx = colouredSubsequenceRange.get(0); colourColumnIdx >= 0 /*tmp cond*/ && colourColumnIdx >= mostLeftSequenceToExtendColumnIndex; colourColumnIdx--) {
                        fieldToColour = new Field(rowIdx, colourColumnIdx);
                        try {
                            if (isFieldEmpty(nonogramSolutionBoard, fieldToColour)) {
                                this.colourFieldAtGivenPosition(fieldToColour, "R---");
                                this.addRowToAffectedActionsByIdentifiers(rowIdx, actionsToDoInRowAfterExtendingColouredFieldsNearXInRows);
                                this.addColumnToAffectedActionsByIdentifiers(colourColumnIdx, actionsToDoInColumnAfterExtendingColouredFieldsNearXInRows);
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
        } while (columnIdx >= 0 && isFieldColoured(nonogramSolutionBoard, fieldBeforeColouredToCheck));
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

        for (int columnIdx = 0; columnIdx < this.getWidth(); columnIdx++) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(nonogramSolutionBoard, potentiallyColouredField)) {

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
                for (int columnsFromX = rangeLength(colouredSubsequenceRange); colouredSubsequenceRange.get(1) - columnsFromX >= 0 && columnsFromX < minimumPossibleLength; columnsFromX++) {
                    potentiallyXField = new Field(rowIdx, colouredSubsequenceRange.get(1) - columnsFromX);
                    if (isFieldWithX(nonogramSolutionBoard, potentiallyXField)) {
                        distanceFromX = columnsFromX;
                        break;
                    }
                }

                /* there could be situation when 'X' is f.e. too far from coloured sequence(distanceFromX not changed), check that case*/
                if (distanceFromX != 0) {
                    int mostRightSequenceToExtendColumnIndex = colouredSubsequenceRange.get(1) - distanceFromX + minimumPossibleLength;
                    for (int colourColumnIdx = colouredSubsequenceRange.get(1) + 1; colourColumnIdx < this.getWidth() /*tmp cond*/ && colourColumnIdx <= mostRightSequenceToExtendColumnIndex; colourColumnIdx++) {
                        try {
                            fieldToColour = new Field(rowIdx, colourColumnIdx);
                            if (isFieldEmpty(nonogramSolutionBoard, fieldToColour)) {
                                this.colourFieldAtGivenPosition(fieldToColour, "R---");
                                this.addRowToAffectedActionsByIdentifiers(rowIdx, actionsToDoInRowAfterExtendingColouredFieldsNearXInRows);
                                this.addColumnToAffectedActionsByIdentifiers(colourColumnIdx, actionsToDoInColumnAfterExtendingColouredFieldsNearXInRows);
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
        } while (columnIdx < this.getWidth() - 1 && isFieldColoured(nonogramSolutionBoard, fieldBeforeColouredToCheck));
        int lastColouredFieldColumnIndexInSubsequence = --columnIdx;

        return List.of(firstColouredFieldColumnIndexInSubsequence, lastColouredFieldColumnIndexInSubsequence);
    }

    /**
     * @param rowIdx - place an "X" on fields which not belong to any row possible range
     * PLACE_XS_ROW_AT_UNREACHABLE_FIELDS
     */
    public void placeXsRowAtUnreachableFields(int rowIdx) {

        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        int nonogramWidth = this.getWidth();
        boolean existRangeIncludingColumn;
        List<Integer> fieldAsRange;
        Field fieldToExclude;

        for (int columnIdx = 0; columnIdx < nonogramWidth; columnIdx++) {
            fieldAsRange = List.of(columnIdx, columnIdx);
            existRangeIncludingColumn = rangesListIncludingAnotherRange(rowSequencesRanges, fieldAsRange);

            if (!existRangeIncludingColumn) {
                fieldToExclude = new Field(rowIdx, columnIdx);
                if (isFieldEmpty(nonogramSolutionBoard, fieldToExclude)) {
                    this.placeXAtGivenField(fieldToExclude);
                    this.addColumnToAffectedActionsByIdentifiers(columnIdx, actionsToDoInColumnAfterPlacingXsAtRowsUnreachableFields);
                    this.addRowToAffectedActionsByIdentifiers(rowIdx, actionsToDoInRowAfterPlacingXsAtRowsUnreachableFields);

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
     * PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES
     * @param rowIdx - the row index where you place an "X" around the longest possible coloured sequences in a given area
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

        for (int columnIdx = 0; columnIdx < this.getWidth(); columnIdx++) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(nonogramSolutionBoard, potentiallyColouredField)) {
                colouredSequenceRange = findColouredSequenceRangeInRow(columnIdx, rowIdx);
                columnIdx = colouredSequenceRange.get(1); //start from end of current coloured sequence

                sequenceOnBoardLength = rangeLength(colouredSequenceRange);
                rowSequencesIndexesIncludingSequenceRange = new ArrayList<>();
                rowSequencesLengthsIncludingSequenceRange = new ArrayList<>();

                for (int seqNo = 0; seqNo < rowSequencesRanges.size(); seqNo++) {
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
                    /* more than one row sequence fit in coloured range & length of coloured sequence is a max length of possible matching row sequences lengths */
                    doStuffWhenPlacingXsAroundLongestSequence(rowIdx, xAtEdges, false);
                }
            }
        }
    }

    private List<Integer> findColouredSequenceRangeInRow(int startColumnIdx, int rowIdx) {
        int columnIdx = startColumnIdx;

        List<Integer> colouredSequenceRange = new ArrayList<>();
        colouredSequenceRange.add(columnIdx);
        while (columnIdx < this.getWidth() && isFieldColoured(nonogramSolutionBoard, new Field(rowIdx, columnIdx))) {
            columnIdx++;
        }
        colouredSequenceRange.add(columnIdx - 1);

        return colouredSequenceRange;
    }

    private void doStuffWhenPlacingXsAroundLongestSequence(int rowIdx, List<Integer> xAtEdges, boolean onlyMatching) {
        String logEndPart = onlyMatching ? "[only possible]" : "[sequence index not specified]";
        for (int currentColumnIndex : xAtEdges) {
            Field fieldAtEdge = new Field(rowIdx, currentColumnIndex);
            if (isColumnIndexValid(currentColumnIndex)) {
                if (isFieldEmpty(nonogramSolutionBoard, fieldAtEdge)) {
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

            this.addColumnToAffectedActionsByIdentifiers(currentColumnIndex, actionsToDoInColumnAfterPlacingXsAroundLongestSequencesInRows);
            this.addRowToAffectedActionsByIdentifiers(rowIdx, actionsToDoInRowDoAfterPlacingXsAroundLongestSequencesInRows);

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
        for (int sequenceColumnIdx : updatedSequenceRange) {
            this.excludeFieldInRow(new Field(rowIdx, sequenceColumnIdx));
            this.nonogramState.increaseMadeSteps();
        }
    }

    private void updateLogicAfterPlacingXsAroundOnlyOneMatchingSequenceInRow(int rowIdx, int rowSequenceToExclude, List<Integer> updatedSequenceRange) {
        List<List<Integer>> rowsSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> oldSequenceRange = rowsSequencesRanges.get(rowSequenceToExclude); // old version: rowsSequencesRanges.get(rowSequenceToExclude), new: rowsSequencesRanges.get(rowIdx)
        this.changeRowSequenceRange(rowIdx, rowSequenceToExclude, updatedSequenceRange);
        tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, rowSequenceToExclude,
                oldSequenceRange, updatedSequenceRange, "correcting sequence while placing X before only matching coloured sequence");
        addLog(tmpLog);
        this.excludeSequenceInRow(rowIdx, rowSequenceToExclude);
    }

    /**
     * PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES
     * @param rowIdx - place an "X" at too short empty fields sequences in row with this index, when none of row sequences can fit in hole
     */
    public void placeXsRowAtTooShortEmptySequences(int rowIdx) {

        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);
        List<Integer> rowsSequencesIdsNotToInclude = this.getRowsSequencesIdsNotToInclude().get(rowIdx);

        Field potentiallyXPlacedField;
        Field fieldAfterXToCheck;

        List<Integer> rowSequencesIdsIncludingEmptyRange = new ArrayList<>();
        List<Integer> rowSequencesIdsIncludingEmptyRangeAndNotFitInIt = new ArrayList<>();

        int firstXIndex;
        int lastXIndex;
        int emptyFieldsSequenceLength;
        List<Integer> emptyFieldsRange;
        Field fieldToExclude;

        boolean onlyEmptyFieldsInSequence;

        for (int columnIdx = 0; columnIdx < this.getWidth() - 1; columnIdx++) {
            onlyEmptyFieldsInSequence = true;
            potentiallyXPlacedField = new Field(rowIdx, columnIdx);
            if (isFieldWithX(nonogramSolutionBoard, potentiallyXPlacedField)) {

                firstXIndex = columnIdx;
                fieldAfterXToCheck = new Field(rowIdx, ++columnIdx);
                while(columnIdx < this.getWidth()) {
                    if (isFieldEmpty(nonogramSolutionBoard, fieldAfterXToCheck)) {
                        fieldAfterXToCheck = new Field(rowIdx, ++columnIdx);
                    } else {
                        if (isFieldColoured(nonogramSolutionBoard, fieldAfterXToCheck)) {
                            onlyEmptyFieldsInSequence = false;
                        }
                        break;
                    }
                }

                lastXIndex = columnIdx;

                if (lastXIndex != firstXIndex + 1) {
                    emptyFieldsRange = Arrays.asList(firstXIndex + 1, lastXIndex - 1);
                    emptyFieldsSequenceLength = rangeLength(emptyFieldsRange);

                    rowSequencesIdsIncludingEmptyRange.clear();
                    rowSequencesIdsIncludingEmptyRangeAndNotFitInIt.clear();

                    for (int rowSequenceId = 0; rowSequenceId < rowSequencesLengths.size(); rowSequenceId++) {
                        if (!rowsSequencesIdsNotToInclude.contains(rowSequenceId)
                                && rangeInsideAnotherRange(emptyFieldsRange, rowSequencesRanges.get(rowSequenceId))) {
                            rowSequencesIdsIncludingEmptyRange.add(rowSequenceId);
                            if (rowSequencesLengths.get(rowSequenceId) > emptyFieldsSequenceLength) {
                                rowSequencesIdsIncludingEmptyRangeAndNotFitInIt.add(rowSequenceId);
                            }
                        }
                    }

                    // TODO onlyEmptyFieldsInSequence/emptyFieldsSequenceLength - check earlier - if is there is no sense to check another conditions
                    if (onlyEmptyFieldsInSequence && !rowSequencesIdsIncludingEmptyRange.isEmpty()
                            && (rowSequencesIdsIncludingEmptyRange.equals(rowSequencesIdsIncludingEmptyRangeAndNotFitInIt))
                    ) {
                        for (int emptyFieldColumnIdx = emptyFieldsRange.get(0); emptyFieldColumnIdx <= emptyFieldsRange.get(1); emptyFieldColumnIdx++) {
                            fieldToExclude = new Field(rowIdx, emptyFieldColumnIdx);
                            if (isFieldEmpty(nonogramSolutionBoard, fieldToExclude)) {
                                this.placeXAtGivenField(fieldToExclude);
                                this.addColumnToAffectedActionsByIdentifiers(emptyFieldColumnIdx, actionsToDoInColumnAfterPlacingXsAtTooShortEmptySequencesInRows);

                                tmpLog = generatePlacingXStepDescription(rowIdx, emptyFieldColumnIdx, "placing \"X\" inside too short empty fields sequence");
                                addLog(tmpLog);

                                this.nonogramState.increaseMadeSteps();
                            } else if (showRepetitions) {
                                System.out.println("X placed in too short row empty field sequence earlier!");
                            }
                        }
                    }
                }
                columnIdx--;
            }
        }
    }

    /***
     * PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE
     * @param rowIdx - row index to check if O can't be placed on field because of creating too long possible coloured sequence
     * How it works:
     *               1. Find coloured fields indexes in row, f.e. for:
     *                  ["-", "-", "O", "O", "-", "O", "-", "-", "-", "-", "-", "-", "-", "-", "O", "O", "-", "X"]
     *                  it will be [ 2, 3, 7, 17, 18]
     *               2. Group fields into ranges (field column indexes that differs by one column)
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
     *                  - validate created coloured sequence -> NonogramLogicUtils.colouredSequenceInRowIsValid()
     *                  Else validate only current coloured sequence (when isn't merged with another after placing "O")
     *               6. If sequence is not valid, place "X" at field on which trying to place "O", in other case do nothing
     */
    public void placeXsRowIfOWillMergeNearFieldsToTooLongColouredSequence(int rowIdx) {
        List<Integer> colouredFieldsIndexesInRow = findColouredFieldsIndexesInRow(nonogramSolutionBoard, rowIdx);

        List<List<Integer>> colouredSequencesRanges = groupConsecutiveIndices(colouredFieldsIndexesInRow);

        int previousColumnIndex;
        Field fieldWithPreviousColumnColoured;
        int nextColumnIndex;
        Field fieldWithNextColumnColoured;

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

            previousColumnIndex = sequenceWithFieldAddedBefore.get(0);
            fieldWithPreviousColumnColoured = new Field(rowIdx, previousColumnIndex);
            colouredSequenceValid = colouredSequenceInRowIsValid(mergedSequenceWithFieldAddedBefore, rowIdx, this);
            if (!colouredSequenceValid && isColumnIndexValid(previousColumnIndex) && isFieldEmpty(this.getNonogramSolutionBoard(), fieldWithPreviousColumnColoured)) {
                this.placeXAtGivenField(fieldWithPreviousColumnColoured);
                this.addRowToAffectedActionsByIdentifiers(rowIdx,
                        actionsToDoInRowAfterPlacingXInRowIfOWillMergeNearFieldsToTooLongColouredSequence);
                this.addColumnToAffectedActionsByIdentifiers(previousColumnIndex,
                        actionsToDoInColumnAfterPlacingXInRowIfOWillMergeNearFieldsToTooLongColouredSequence);

                tmpLog = generatePlacingXStepDescription(rowIdx, previousColumnIndex,
                        "placing \"X\" because \"O\" will create too long sequence");
                addLog(tmpLog);
                this.nonogramState.increaseMadeSteps();
            } else if (showRepetitions) {
                System.out.println("X because \"O\" will create too long sequence in row placed earlier!");
            }

            mergedSequenceWithFieldAddedAfter = currentColouredSequenceRangesWithColouredFieldAdded.get(1);

            nextColumnIndex = mergedSequenceWithFieldAddedAfter.get(1);
            if (nextColumnIndex != this.getWidth()) {

                if (seqRangeIndex < colouredSequencesRanges.size() - 1) {
                    colouredSequenceRangeAfterCurrent = colouredSequencesRanges.get(seqRangeIndex + 1);
                    mergedSequenceWithFieldAddedAfter = tryToMergeColouredSequenceWithNext(
                            mergedSequenceWithFieldAddedAfter, colouredSequenceRangeAfterCurrent);
                }

                fieldWithNextColumnColoured = new Field(rowIdx, nextColumnIndex);
                colouredSequenceValid = colouredSequenceInRowIsValid(mergedSequenceWithFieldAddedAfter, rowIdx, this);
                if (!colouredSequenceValid && isFieldEmpty(this.getNonogramSolutionBoard(), fieldWithNextColumnColoured)) {
                    this.placeXAtGivenField(fieldWithNextColumnColoured);
                    this.addRowToAffectedActionsByIdentifiers(rowIdx,
                            actionsToDoInRowAfterPlacingXInRowIfOWillMergeNearFieldsToTooLongColouredSequence);
                    this.addColumnToAffectedActionsByIdentifiers(nextColumnIndex,
                            actionsToDoInColumnAfterPlacingXInRowIfOWillMergeNearFieldsToTooLongColouredSequence);

                    tmpLog = generatePlacingXStepDescription(rowIdx, nextColumnIndex,
                            "placing \"X\" because \"O\" will create too long sequence");
                    addLog(tmpLog);
                    this.nonogramState.increaseMadeSteps();
                } else if (showRepetitions) {
                    System.out.println("X because \"O\" will create too long sequence in row placed earlier!");
                }
            }
        }
    }

    /**
     * PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE
     * @param rowIdx - TODO
     */
    public void placeXsRowIfONearXWillBeginTooLongPossibleColouredSequence(int rowIdx) {
        Field fieldToCheckX;
        Field firstColouredField;
        List<Integer> emptyFieldsRange;
        List<Integer> colouredFieldsRange;
        int emptyFieldsRangeLength;
        int colouredFieldsRangeLength;

        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> sequencesIdsWhichWillBeginTooLongPossibleColoured;
        List<Integer> sequencesIdsWhichNotReachColouredField;

        for (int columnIdx = this.getWidth() - 1; columnIdx > 0; columnIdx--) {
            fieldToCheckX = new Field(rowIdx, columnIdx);
            if (isFieldWithX(this.getNonogramSolutionBoard(), fieldToCheckX)) {
                emptyFieldsRange = getEmptyFieldsRangeFromXToFirstColouredFieldOnLeft(fieldToCheckX);

                if (!emptyFieldsRange.equals(NOT_FOUND_EMPTY_FIELDS_RANGE_VALUE)) {
                    firstColouredField = new Field(rowIdx, emptyFieldsRange.get(0) - 1);
                    colouredFieldsRange = getColouredFieldsRangeNearEmptySequenceOnLeft(firstColouredField);

                    if (!colouredFieldsRange.equals(NOT_FOUND_COLOURED_FIELDS_RANGE_VALUE)) {
                        emptyFieldsRangeLength = rangeLength(emptyFieldsRange);
                        colouredFieldsRangeLength = rangeLength(colouredFieldsRange);
                        sequencesIdsWhichWillBeginTooLongPossibleColoured = new ArrayList<>();
                        sequencesIdsWhichNotReachColouredField = new ArrayList<>();
                        for (int seqNo = 0; seqNo < rowSequencesLengths.size(); seqNo++) {
                            int sequenceLength = rowSequencesLengths.get(seqNo);
                            if (rangeInsideAnotherRange(emptyFieldsRange, rowSequencesRanges.get(seqNo))
                                || rowSequenceCanFitAfterColouredField(rowIdx, seqNo, emptyFieldsRange)) {
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
                                sequencesIdsWhichWillBeginTooLongPossibleColoured.stream().allMatch(seqNo -> mergedSequenceLength > rowSequencesLengths.get(seqNo))) {
                            Field emptyFieldNearX = new Field(rowIdx, emptyFieldsRange.get(1));
                            if (isFieldEmpty(this.getNonogramSolutionBoard(), emptyFieldNearX)) {
                                this.placeXAtGivenField(emptyFieldNearX);
                                this.addColumnToAffectedActionsByIdentifiers(emptyFieldNearX.getColumnIdx(),
                                        actionsToDoInColumnAfterPlacingXsInRowIfONearWillBeginTooLongPossibleColouredSequence);

                                tmpLog = generatePlacingXStepDescription(columnIdx, rowIdx, "placing \"X\" when \"O\" near \"X\" will begin too long possible coloured sequence");
                                addLog(tmpLog);

                                this.nonogramState.increaseMadeSteps();
                            }
                        }
                    }
                }
            }
        }

        for (int columnIdx = 0; columnIdx < this.getWidth(); columnIdx++) {
            fieldToCheckX = new Field(rowIdx, columnIdx);
            if (isFieldWithX(this.getNonogramSolutionBoard(), fieldToCheckX)) {
                emptyFieldsRange = getEmptyFieldsRangeFromXToFirstColouredFieldOnRight(fieldToCheckX);
                if (!emptyFieldsRange.equals(NOT_FOUND_EMPTY_FIELDS_RANGE_VALUE)) {
                    firstColouredField = new Field(rowIdx, emptyFieldsRange.get(1) + 1);
                    colouredFieldsRange = getColouredFieldsRangeNearEmptySequenceOnRight(firstColouredField);

                    if (!colouredFieldsRange.equals(NOT_FOUND_COLOURED_FIELDS_RANGE_VALUE)) {
                        emptyFieldsRangeLength = rangeLength(emptyFieldsRange);
                        colouredFieldsRangeLength = rangeLength(colouredFieldsRange);
                        sequencesIdsWhichWillBeginTooLongPossibleColoured = new ArrayList<>();
                        sequencesIdsWhichNotReachColouredField = new ArrayList<>();
                        for (int seqNo = 0; seqNo < rowSequencesLengths.size(); seqNo++) {
                            int sequenceLength = rowSequencesLengths.get(seqNo);
                            if (rangeInsideAnotherRange(emptyFieldsRange, rowSequencesRanges.get(seqNo))
                                    || rowSequenceCanFitBeforeColouredField(rowIdx, seqNo, emptyFieldsRange)) {
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
                                sequencesIdsWhichWillBeginTooLongPossibleColoured.stream().allMatch(seqNo -> mergedSequenceLength > rowSequencesLengths.get(seqNo))) {
                            Field emptyFieldNearX = new Field(rowIdx, emptyFieldsRange.get(0));
                            if (isFieldEmpty(this.getNonogramSolutionBoard(), emptyFieldNearX)) {
                                this.placeXAtGivenField(emptyFieldNearX);
                                this.addColumnToAffectedActionsByIdentifiers(emptyFieldNearX.getColumnIdx(),
                                        actionsToDoInColumnAfterPlacingXsInRowIfONearWillBeginTooLongPossibleColouredSequence);

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

    private List<Integer> getEmptyFieldsRangeFromXToFirstColouredFieldOnLeft(Field xField) {
        List<Integer> emptyFieldsRange = new ArrayList<>();
        Field fieldToCheckEmpty = new Field(xField.getRowIdx(), xField.getColumnIdx() - 1);

        while (areFieldIndexesValid(fieldToCheckEmpty) && isFieldEmpty(this.getNonogramSolutionBoard(), fieldToCheckEmpty)) {
            if (emptyFieldsRange.isEmpty()) {
               emptyFieldsRange.add(fieldToCheckEmpty.getColumnIdx());
            } else if (emptyFieldsRange.size() == 1) {
                emptyFieldsRange.add(0, fieldToCheckEmpty.getColumnIdx());
            } else {
                emptyFieldsRange.set(0, fieldToCheckEmpty.getColumnIdx());
            }
            fieldToCheckEmpty.setColumnIdx(fieldToCheckEmpty.getColumnIdx() - 1);
        }

        if (emptyFieldsRange.isEmpty()) {
            return List.of(-1, -1); // no empty fields (X or O just before X)
        } else if (emptyFieldsRange.size() == 1) {
            emptyFieldsRange.add(emptyFieldsRange.get(0)); // one empty field before X
        }

        return emptyFieldsRange;
    }

    private List<Integer> getColouredFieldsRangeNearEmptySequenceOnLeft(Field firstSequenceField) {
        List<Integer> colouredFieldsRange = new ArrayList<>();
        Field fieldToCheckO = new Field(firstSequenceField.getRowIdx(), firstSequenceField.getColumnIdx());

        while (areFieldIndexesValid(fieldToCheckO) && isFieldColoured(this.getNonogramSolutionBoard(), fieldToCheckO)) {
            if (colouredFieldsRange.isEmpty()) {
                colouredFieldsRange.add(fieldToCheckO.getColumnIdx());
            } else if (colouredFieldsRange.size() == 1) {
                colouredFieldsRange.add(0, fieldToCheckO.getColumnIdx());
            } else {
                colouredFieldsRange.set(0, fieldToCheckO.getColumnIdx());
            }
            fieldToCheckO.setColumnIdx(fieldToCheckO.getColumnIdx() - 1);
        }

        if (colouredFieldsRange.isEmpty()) {
            return List.of(-1, -1); // no coloured fields (X before - sequence)
        } else if (colouredFieldsRange.size() == 1) {
            colouredFieldsRange.add(colouredFieldsRange.get(0)); // one coloured field before empty sequence
        }

        return colouredFieldsRange;
    }

    private boolean rowSequenceCanFitAfterColouredField(int rowIdx, int seqNo, List<Integer> emptyFieldsRange) {
        List<Integer> rowPossibleRange = this.getRowsSequencesRanges().get(rowIdx).get(seqNo);
        int seqLength = this.getRowsSequences().get(rowIdx).get(seqNo);
        List<Integer> lastRangeAfterColouredField = List.of(rowPossibleRange.get(0), rowPossibleRange.get(0) + seqLength - 1);

        return rangeInsideAnotherRange(lastRangeAfterColouredField, emptyFieldsRange);
    }

    private List<Integer> getEmptyFieldsRangeFromXToFirstColouredFieldOnRight(Field xField) {
        List<Integer> emptyFieldsRange = new ArrayList<>();
        Field fieldToCheckEmpty = new Field(xField.getRowIdx(), xField.getColumnIdx() + 1);

        while (areFieldIndexesValid(fieldToCheckEmpty) && isFieldEmpty(this.getNonogramSolutionBoard(), fieldToCheckEmpty)) {
            if (emptyFieldsRange.isEmpty()) {
                emptyFieldsRange.add(fieldToCheckEmpty.getColumnIdx());
            } else if (emptyFieldsRange.size() == 1) {
                emptyFieldsRange.add(1, fieldToCheckEmpty.getColumnIdx()); // -> direction, new column index higher than earlier
            } else {
                emptyFieldsRange.set(1, fieldToCheckEmpty.getColumnIdx());
            }
            fieldToCheckEmpty.setColumnIdx(fieldToCheckEmpty.getColumnIdx() + 1);
        }

        if (emptyFieldsRange.isEmpty()) {
            return List.of(-1, -1); // no empty fields (X or O just before X)
        } else if (emptyFieldsRange.size() == 1) {
            emptyFieldsRange.add(emptyFieldsRange.get(0)); // one empty field after X
        }

        return emptyFieldsRange;
    }

    private List<Integer> getColouredFieldsRangeNearEmptySequenceOnRight(Field firstSequenceField) {
        List<Integer> colouredFieldsRange = new ArrayList<>();
        Field fieldToCheckO = new Field(firstSequenceField.getRowIdx(), firstSequenceField.getColumnIdx());

        while (areFieldIndexesValid(fieldToCheckO) && isFieldColoured(this.getNonogramSolutionBoard(), fieldToCheckO)) {
            if (colouredFieldsRange.isEmpty()) {
                colouredFieldsRange.add(fieldToCheckO.getColumnIdx());
            } else if (colouredFieldsRange.size() == 1) {
                colouredFieldsRange.add(1, fieldToCheckO.getColumnIdx());
            } else {
                colouredFieldsRange.set(1, fieldToCheckO.getColumnIdx());
            }
            fieldToCheckO.setColumnIdx(fieldToCheckO.getColumnIdx() + 1);
        }

        if (colouredFieldsRange.isEmpty()) {
            return List.of(-1, -1); // no coloured fields (X before - sequence)
        } else if (colouredFieldsRange.size() == 1) {
            colouredFieldsRange.add(colouredFieldsRange.get(0)); // one coloured field before empty sequence
        }

        return colouredFieldsRange;
    }

    private boolean rowSequenceCanFitBeforeColouredField(int rowIdx, int seqNo, List<Integer> emptyFieldsRange) {
        List<Integer> rowPossibleRange = this.getRowsSequencesRanges().get(rowIdx).get(seqNo);
        int seqLength = this.getRowsSequences().get(rowIdx).get(seqNo);
        List<Integer> lastRangeBeforeColouredField = List.of(rowPossibleRange.get(1) - seqLength + 1, rowPossibleRange.get(1));

        return rangeInsideAnotherRange(lastRangeBeforeColouredField, emptyFieldsRange);
    }

    public void preventExtendingColouredSequenceToExcessLengthInRow(int rowIdx) {
        preventExtendingColouredSequenceToExcessLengthInRowToLeft(rowIdx);
        preventExtendingColouredSequenceToExcessLengthInRowToRight(rowIdx);
    }

    /***
        EXAMPLE: case o10401 - start from column 18
          ids: [0, 1, 2, 3, 4 | 5, 6, 7, 8, 9 |10,11,12,13,14 |15,16,17,18,19 |20,21,22,23,24 |25,26,27,28,29 |30,31,32,33,34 |35,36,37,38,39 ]
        row 8: [-, -, -, -, - | -, -, -, -, - | -, -, -, -, O | O, -, -, O, X | X, X, X, X, X | O, O, O, X, - | X, -, -, -, X | X, X, O, O, O ]
        rowSequencesLengths [1      , 4(chk), 3(chk)  , 1(chk)  , 3       , 3       ]
          (4): [-, -, -, -, - | -, -, -, -, - | -, -, -, -, O | O, #, #, O, X | X, X, X, X, X | O, O, O, X, - | X, -, -, -, X | X, X, O, O, O ] - too long
          (3): [-, -, -, -, - | -, -, -, -, - | -, -, -, -, O | O, #, #, O, X | X, X, X, X, X | O, O, O, X, - | X, -, -, -, X | X, X, O, O, O ] - too long
          (1): [-, -, -, -, - | -, -, -, -, - | -, -, -, -, O | O, -, X, O, X | X, X, X, X, X | O, O, O, X, - | X, -, -, -, X | X, X, O, O, O ] - ok
        rowSequencesRanges [[0, 13], [2, 18], [13, 27], [18, 29], [25, 33], [37, 39]]

     |10,11,12,13,14 |15,16,17,18,19 |
     | -, -, -, -, O | O, -, -, O, X |
     Sequences which have ranges in area: [4, 3, 1] -> only possible is seq with length 1

     TODO - check also case with sequence with length more than 1 (colour and place X)
     ***/
    private void preventExtendingColouredSequenceToExcessLengthInRowToLeft(int rowIdx) {
        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);

        Field fieldToCheckX;

        int potentiallyColouredFieldColumn;
        Field fieldToCheckColoured;

        List<Integer> sequencesIds;
        int maxSequenceLength;
        List<List<Integer>> colouredSequences;
        List<Integer> validSequenceIds;
        List<Integer> validSequenceLengths;

        for (int columnIdx = this.getWidth() - 1; columnIdx > 0; columnIdx--) {
            fieldToCheckX = new Field(rowIdx, columnIdx);
            if (isFieldWithX(this.getNonogramSolutionBoard(), fieldToCheckX)) {
                potentiallyColouredFieldColumn = fieldToCheckX.getColumnIdx() - 1;
                fieldToCheckColoured = new Field(fieldToCheckX.getRowIdx(), potentiallyColouredFieldColumn);

                if (isFieldColoured(this.getNonogramSolutionBoard(), fieldToCheckColoured)) {
                    sequencesIds = sequencesIdsInRowIncludingField(rowSequencesRanges, fieldToCheckColoured);

                    List<Integer> sequencesLengths = sequencesIds.stream().map(rowSequencesLengths::get).toList();

                    maxSequenceLength = Collections.max(sequencesLengths);

                    colouredSequences = getColouredSequencesRangesInRowInRangeOnLeft(this.getNonogramSolutionBoard(), rowIdx, potentiallyColouredFieldColumn, maxSequenceLength);

                    validSequenceIds = findValidSequencesIdsMergingToLeft(sequencesIds, sequencesLengths, potentiallyColouredFieldColumn, colouredSequences);

                    validSequenceLengths = validSequenceIds.stream()
                            .map(sequencesIds::indexOf)
                            .map(sequencesLengths::get)
                            .toList();

                    // only one length is valid
                    if (validSequenceLengths.stream().distinct().count() == 1) {
                        int sequenceLength = validSequenceLengths.get(0);
                        int colouredSequenceColStartIdx = potentiallyColouredFieldColumn - sequenceLength + 1;
                        Field fieldToColour;

                        for (int columnToColourIdx = colouredSequenceColStartIdx; columnToColourIdx <= potentiallyColouredFieldColumn; columnToColourIdx++) {
                            fieldToColour = new Field(rowIdx, columnToColourIdx);
                            if (isFieldEmpty(this.getNonogramSolutionBoard(), fieldToColour)) {
                                this.colourFieldAtGivenPosition(fieldToColour, "R---");

                                this.nonogramState.increaseMadeSteps();

                                tmpLog = generateColourStepDescription(rowIdx, columnToColourIdx, "extend coloured sequence to matching length to left near X (with placing X before)");
                                addLog(tmpLog);

                                //this.addRowToAffectedActionsByIdentifiers(rowIdx, actionsToDoInRowDuringColouringPartPreventingExcessLengthInRows);
                                //this.addColumnToAffectedActionsByIdentifiers(columnToColourIdx, actionsToDoInColumnDuringColouringPartPreventingExcessLengthInRows);
                            }
                        }

                        Field fieldToPlaceX = new Field(rowIdx, colouredSequenceColStartIdx - 1);
                        this.placeXAtGivenField(fieldToPlaceX);
                        this.addColumnToAffectedActionsByIdentifiers(fieldToPlaceX.getColumnIdx(),
                                actionsToDoInColumnDuringColouringPartPreventingExcessLengthInRows);

                        // moreover - only one id is valid -> can correct sequence range
                        if (validSequenceIds.size() == 1) {
                            int matchingSeqId = validSequenceIds.get(0);
                            List<Integer> oldRange = rowSequencesRanges.get(matchingSeqId);
                            List<Integer> updatedRange = new ArrayList<>(Arrays.asList(colouredSequenceColStartIdx, potentiallyColouredFieldColumn));

                            this.updateRowSequenceRange(rowIdx, matchingSeqId, updatedRange);
                            this.addRowToAffectedActionsByIdentifiers(rowIdx, actionsToDoInRowAfterUpdateOnlyMatchingSequencePartPreventingExcessLengthInRows);

                            this.nonogramState.increaseMadeSteps();
                            tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, matchingSeqId, oldRange, updatedRange, "update only matching sequence part preventing excess length to left");
                        }
                    }
                }
            }
        }
    }

    private void preventExtendingColouredSequenceToExcessLengthInRowToRight(int rowIdx) {
        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);

        Field fieldToCheckX;

        int potentiallyColouredFieldColumnIndex;
        Field fieldToCheckColoured;

        List<Integer> sequencesIds;
        int maxSequenceLength;
        List<List<Integer>> colouredSequences;
        List<Integer> validSequencesIds;
        List<Integer> validSequenceLengths;

        for (int columnIdx = 0; columnIdx < this.getWidth() - 1; columnIdx++) {
            fieldToCheckX = new Field(rowIdx, columnIdx);
            if (isFieldWithX(this.getNonogramSolutionBoard(), fieldToCheckX)) {
                potentiallyColouredFieldColumnIndex = fieldToCheckX.getColumnIdx() + 1;
                fieldToCheckColoured = new Field(fieldToCheckX.getRowIdx(), potentiallyColouredFieldColumnIndex);

                if (isFieldColoured(this.getNonogramSolutionBoard(), fieldToCheckColoured)) {
                    sequencesIds = sequencesIdsInRowIncludingField(rowSequencesRanges, fieldToCheckColoured);

                    List<Integer> sequencesLengths = sequencesIds.stream().map(rowSequencesLengths::get).toList();

                    maxSequenceLength = Collections.max(sequencesLengths);

                    colouredSequences = getColouredSequencesRangesInRowInRangeOnRight(this.getNonogramSolutionBoard(), rowIdx, potentiallyColouredFieldColumnIndex, maxSequenceLength);

                    validSequencesIds = findValidSequencesIdsMergingToRight(sequencesIds, sequencesLengths, potentiallyColouredFieldColumnIndex, colouredSequences);

                    validSequenceLengths = validSequencesIds.stream()
                            .map(sequencesIds::indexOf)
                            .map(sequencesLengths::get)
                            .toList();

                    if (validSequenceLengths.stream().distinct().count() == 1) {
                        int sequenceLength = validSequenceLengths.get(0);
                        int colouredSequenceEndColumnIndex = potentiallyColouredFieldColumnIndex + sequenceLength - 1;
                        Field fieldToColour;

                        for (int columnToColourIdx = potentiallyColouredFieldColumnIndex; columnToColourIdx <= colouredSequenceEndColumnIndex; columnToColourIdx++) {
                            fieldToColour = new Field(rowIdx, columnToColourIdx);
                            if (isFieldEmpty(this.getNonogramSolutionBoard(), fieldToColour)) {
                                this.colourFieldAtGivenPosition(fieldToColour, "R---");

                                this.nonogramState.increaseMadeSteps();

                                tmpLog = generateColourStepDescription(rowIdx, columnToColourIdx, "extend coloured sequence to matching length to right near X (with placing X before)");
                                addLog(tmpLog);
                            }
                        }

                        Field fieldToPlaceX = new Field(rowIdx, colouredSequenceEndColumnIndex + 1);
                        this.placeXAtGivenField(fieldToPlaceX);
                        this.addColumnToAffectedActionsByIdentifiers(fieldToPlaceX.getColumnIdx(),
                                actionsToDoInColumnDuringColouringPartPreventingExcessLengthInRows);

                        if (validSequencesIds.size() == 1) {
                            int matchingSeqId = validSequencesIds.get(0);
                            List<Integer> oldRange = rowSequencesRanges.get(matchingSeqId);
                            List<Integer> updatedRange = new ArrayList<>(Arrays.asList(potentiallyColouredFieldColumnIndex, colouredSequenceEndColumnIndex));

                            this.updateRowSequenceRange(rowIdx, matchingSeqId, updatedRange);
                            this.addRowToAffectedActionsByIdentifiers(rowIdx, actionsToDoInRowAfterUpdateOnlyMatchingSequencePartPreventingExcessLengthInRows);

                            this.nonogramState.increaseMadeSteps();
                            tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, matchingSeqId, oldRange, updatedRange, "update only matching sequence part preventing excess length to right");
                        }
                    }
                }
            }
        }
    }

    /**
     * MARK_AVAILABLE_FIELDS_IN_ROW
     * @param rowIdx - row index on which mark fields with char sequences identifiers
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

        List<Integer> onlyMatchingSequenceOldRange;
        List<Integer> newSequenceRange;

        for (int columnIdx = 0; columnIdx < this.getWidth(); columnIdx++) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(nonogramSolutionBoard, potentiallyColouredField)) {

                colouredSequenceIndexes = new ArrayList<>();
                colouredSequenceIndexes.add(columnIdx);

                // TODO - do while(?)
                while(columnIdx < this.getWidth() && isFieldColoured(nonogramSolutionBoard, potentiallyColouredField)) {
                    columnIdx++;
                    potentiallyColouredField = new Field(rowIdx, columnIdx);
                }

                colouredSequenceIndexes.add(columnIdx - 1);

                firstSequenceIndex = colouredSequenceIndexes.get(0);
                lastSequenceIndex = colouredSequenceIndexes.get(1);
                colouredSequenceLength = rangeLength(colouredSequenceIndexes);

                matchingSequencesCount = 0;

                for (int seqNo = 0; seqNo < rowSequencesLengths.size(); seqNo++) {
                    rowSequenceRange = rowSequencesRanges.get(seqNo);

                    if ( rangeInsideAnotherRange(colouredSequenceIndexes, rowSequenceRange)
                            && colouredSequenceLength <= rowSequencesLengths.get(seqNo)) {
                        matchingSequencesCount++;
                        lastMatchingSequenceIndex = seqNo;
                    }
                }

                if (matchingSequencesCount == 1) {

                    sequenceMarker = NonogramLogic.indexToSequenceCharMark(lastMatchingSequenceIndex);
                    for (int sequenceColumnIdx = firstSequenceIndex; sequenceColumnIdx <= lastSequenceIndex; sequenceColumnIdx++) {
                        if (this.getNonogramSolutionBoardWithMarks().get(rowIdx).get(sequenceColumnIdx).startsWith(EMPTY_FIELD, 1)) {
                            this.markRowBoardField(rowIdx, sequenceColumnIdx, sequenceMarker);
                            this.nonogramState.increaseMadeSteps();
                        } else if (showRepetitions) {
                            System.out.println("Row field was marked earlier.");
                        }
                    }

                    //correct sequence range if new range is shorter
                    onlyMatchingSequenceOldRange = rowSequencesRanges.get(lastMatchingSequenceIndex);
                    newSequenceRange = calculateNewMarkedRangeFromParameters(onlyMatchingSequenceOldRange, colouredSequenceIndexes,
                            rowSequencesLengths.get(lastMatchingSequenceIndex));

                    if (!rangesEqual(onlyMatchingSequenceOldRange, newSequenceRange)) {
                        this.changeRowSequenceRange(rowIdx, lastMatchingSequenceIndex, newSequenceRange);
                        tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, lastMatchingSequenceIndex, onlyMatchingSequenceOldRange, newSequenceRange, CORRECT_ROW_SEQ_RANGE_MARKING_FIELD);
                        addLog(tmpLog);
                        addRowToAffectedActionsByIdentifiers(rowIdx, actionsToDoAfterCorrectingRangesWhenMarkingSequencesInRows);
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

    private boolean isColumnRangeColoured(int rowIdx, List<Integer> columnRange) {
        Field potentiallyColouredField;
        for (Integer columnIdx : columnRange) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (!isFieldColoured(nonogramSolutionBoard, potentiallyColouredField)) {
                return false;
            }
        }

        return true;
    }

    public int minimumColumnIndexWithoutX(int rowIdx, int lastSequenceColumnIdx, int sequenceFullLength) {
        int minimumColumnIndex = lastSequenceColumnIdx;
        int minimumColumnIndexLimit = Math.max(lastSequenceColumnIdx - sequenceFullLength + 1, 0);
        Field fieldToCheck;

        for (; minimumColumnIndex >= minimumColumnIndexLimit; minimumColumnIndex--) {
            fieldToCheck = new Field(rowIdx, minimumColumnIndex);
            if (isFieldWithX(nonogramSolutionBoard, fieldToCheck)) {
                break;
            }
        }

        return minimumColumnIndex + 1;
    }

    public int maximumColumnIndexWithoutX(int rowIdx, int firstSequenceColumnIdx, int sequenceFullLength) {
        int maximumColumnIndex = firstSequenceColumnIdx;
        int maximumColumnIndexLimit = Math.min(firstSequenceColumnIdx + sequenceFullLength - 1, this.getWidth() - 1);
        Field fieldToCheck;

        for (; maximumColumnIndex <= maximumColumnIndexLimit; maximumColumnIndex++) {
            fieldToCheck = new Field(rowIdx, maximumColumnIndex);
            if (isFieldWithX(nonogramSolutionBoard, fieldToCheck)) {
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
        this.nonogramSolutionBoardWithMarks.get(rowIdx).set(colIdx, MARKED_ROW_INDICATOR + marker + oldRowField.substring(2, 4));
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
