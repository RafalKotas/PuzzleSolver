package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.Field;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.NonogramLogicParams;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.RangeCorrectionHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramHelper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static com.puzzlesolverappbackend.puzzleAppFileManager.common.ArrayUtils.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.ColouringHelper.calculateOverlappingRange;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.RowColourFieldsIfXWouldForceTooLongColouredFieldsSequenceHelpers.collectColouredSequencesRangesInRow;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.RowColourFieldsIfXWouldForceTooLongColouredFieldsSequenceHelpers.matchColouredSequencesToPossibleSeqIDs;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.RowPreventExtendingColouredSequenceToExcessLengthHelpers.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.model.NonogramConstants.EMPTY_FIELD;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.model.NonogramConstants.MARKED_ROW_INDICATOR;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.service.logic.NonogramLogicService.rangesListIncludingAnotherRange;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramBoardUtils.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramCreatorUtils.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramLogicUtils.colouredSequenceInRowIsValid;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramParametersComparatorHelper.rangesEqual;

@Setter
@Getter
@Slf4j
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NonogramRowLogic extends NonogramLogicParams implements RowActions {

    private final static String CORRECTING_ROW_SEQ_RANGE_MARKING_FIELD = "correcting row sequence range when marking field";

    private final static String FILL_OVERLAPPING_FIELDS = "fill overlapping fields";

    private final static List<Integer> NOT_FOUND_EMPTY_FIELDS_RANGE_VALUE = List.of(-1, -1);

    private final static List<Integer> NOT_FOUND_COLOURED_FIELDS_RANGE_VALUE = List.of(-1, -1);

    protected List<List<List<Integer>>> rowsSequencesRanges;
    protected List<List<Integer>> rowsFieldsNotToInclude;
    protected List<List<Integer>> rowsSequencesIdsNotToInclude;

    public NonogramRowLogic(NonogramLogic logic) {
        super(
                logic.getNonogramRules(),
                logic.getNonogramSolutionBoard(),
                logic.getNonogramSolutionBoardWithMarks(),
                logic.getActionsToDoList(),
                logic.getNonogramState(),
                logic.getLogs()
        );

        this.rowsSequencesRanges = logic.getRowsSequencesRanges();
        this.rowsSequencesIdsNotToInclude = logic.getRowsSequencesIdsNotToInclude();
        this.rowsFieldsNotToInclude = logic.getRowsFieldsNotToInclude();

        this.nonogramSolutionBoardWithMarks = logic.getNonogramSolutionBoardWithMarks();
        this.nonogramSolutionBoard = logic.getNonogramSolutionBoard();

        this.actionsToDoList = logic.getActionsToDoList();
    }

    public void setRowSequencesLengths(int rowIdx, List<Integer> sequencesLengths) {
        this.nonogramRules.getRowSequencesLengths().set(rowIdx, sequencesLengths);
    }

    public void setRowSequencesRanges(int rowIdx, List<List<Integer>> rowSequencesRanges) {
        this.getRowsSequencesRanges().set(rowIdx, rowSequencesRanges);
    }

    // TODO - same at NonogramColumLogic + move to tests both
    public static NonogramRowLogic prepareNonogramRowLogic(int HEIGHT, int WIDTH) {
        NonogramRowLogic nonogramRowLogic = new NonogramRowLogic();

        nonogramRowLogic.setNonogramSolutionBoard(generateEmptyBoard(HEIGHT, WIDTH));
        nonogramRowLogic.setNonogramSolutionBoardWithMarks(generateEmptyBoardWithMarks(HEIGHT, WIDTH));

        nonogramRowLogic.setRowsFieldsNotToInclude(generateEmptyRowsFieldsNotToInclude(HEIGHT));
        nonogramRowLogic.setRowsSequencesIdsNotToInclude(generateEmptyRowsSequencesNotToInclude(HEIGHT));

        nonogramRowLogic.setRowsSequencesRanges(
                generateEmptyRowSequencesRanges(HEIGHT)
        );

        return nonogramRowLogic;
    }

    @Override
    public void correctRowSequencesRanges(int rowIdx) {
        correctSequencesRangesInRowFromLeft(rowIdx);
        correctSequencesRangesInRowFromRight(rowIdx);
    }

    private void correctSequencesRangesInRowFromLeft(int rowIdx) {
        List<Integer> rowSequencesLengths = getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowFieldsNotToInclude = getRowsFieldsNotToInclude().get(rowIdx);
        List<Integer> rowSequencesIdsNotToInclude = getRowsSequencesIdsNotToInclude().get(rowIdx);

        for (int seqIdx = 0; seqIdx < rowSequencesRanges.size() - 1; seqIdx++) {
            int nextSeqIdx = seqIdx + 1;
            if (rowSequencesIdsNotToInclude.contains(nextSeqIdx)) continue;

            List<Integer> updatedNextRange = rowSequencesIdsNotToInclude.contains(seqIdx)
                    ? RangeCorrectionHelper.calculateUpdatedNextSequenceRangeAfterExcludedSequence(rowSequencesRanges, rowFieldsNotToInclude, seqIdx, nextSeqIdx)
                    : RangeCorrectionHelper.calculateUpdatedNextSequenceRangeAfterIncludedSequence(rowSequencesRanges, rowSequencesLengths, seqIdx, nextSeqIdx);

            tryToCorrectRowRangeFromLeft(rowIdx, rowSequencesLengths, rowSequencesRanges.get(nextSeqIdx), updatedNextRange, nextSeqIdx);
        }
    }

    private void tryToCorrectRowRangeFromLeft(int rowIdx,
                                              List<Integer> rowSequencesLengths,
                                              List<Integer> oldNextRange,
                                              List<Integer> updatedNextRange,
                                              int nextSeqIdx) {
        if (!oldNextRange.get(0).equals(updatedNextRange.get(0))) {
            updateRowSequenceRange(rowIdx, nextSeqIdx, updatedNextRange);
            logRowSequenceCorrection(rowIdx, nextSeqIdx, oldNextRange, updatedNextRange, "correcting from left");
            markRowAsChanged(rowIdx);

            if (rangeLength(updatedNextRange) == rowSequencesLengths.get(nextSeqIdx)
                    && isColumnRangeColoured(rowIdx, updatedNextRange)) {
                excludeSequenceInRow(rowIdx, nextSeqIdx);
            }
        }
    }

    public void correctSequencesRangesInRowFromRight(int rowIdx) {
        List<Integer> rowSequencesLengths = getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowFieldsNotToInclude = getRowsFieldsNotToInclude().get(rowIdx);
        List<Integer> rowSequencesIdsNotToInclude = getRowsSequencesIdsNotToInclude().get(rowIdx);

        for (int seqIdx = rowSequencesRanges.size() - 1; seqIdx > 0; seqIdx--) {
            int prevSeqIdx = seqIdx - 1;
            if (rowSequencesIdsNotToInclude.contains(prevSeqIdx)) continue;

            List<Integer> updatedPrevRange = rowSequencesIdsNotToInclude.contains(seqIdx)
                    ? RangeCorrectionHelper.calculateUpdatedPreviousSequenceRangeAfterExcludedSequence(rowSequencesRanges, rowFieldsNotToInclude, seqIdx, prevSeqIdx)
                    : RangeCorrectionHelper.calculateUpdatedPreviousSequenceRangeAfterIncludedSequence(rowSequencesRanges, rowSequencesLengths, seqIdx, prevSeqIdx);

            tryToCorrectRowRangeFromRight(rowIdx, rowSequencesLengths, rowSequencesRanges.get(prevSeqIdx), updatedPrevRange, prevSeqIdx);
        }
    }

    private void tryToCorrectRowRangeFromRight(int rowIdx,
                                               List<Integer> rowSequencesLengths,
                                               List<Integer> oldPrevRange,
                                               List<Integer> updatedPrevRange,
                                               int prevSeqIdx) {
        if (!oldPrevRange.get(1).equals(updatedPrevRange.get(1))) {
            updateRowSequenceRange(rowIdx, prevSeqIdx, updatedPrevRange);
            logRowSequenceCorrection(rowIdx, prevSeqIdx, oldPrevRange, updatedPrevRange, "correcting from right");
            markRowAsChanged(rowIdx);

            if (rangeLength(updatedPrevRange) == rowSequencesLengths.get(prevSeqIdx)
                    && isColumnRangeColoured(rowIdx, updatedPrevRange)) {
                excludeSequenceInRow(rowIdx, prevSeqIdx);
            }
        }
    }

    private void logRowSequenceCorrection(int rowIdx, int sequenceId,
                                             List<Integer> oldRange, List<Integer> newRange, String action) {
        tmpLog = generateCorrectingRowSequenceRangeStepDescription(
                rowIdx, sequenceId, oldRange, newRange, action);
        addLog();
    }

    private void markRowAsChanged(int rowIdx) {
        addRowToAffectedActionsByIdentifiers(rowIdx, NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES);
        nonogramState.increaseMadeSteps();
    }

    @Override
    public void correctRowSequencesRangesWhenMetColouredField (int rowIdx) {
        correctRowSequencesRangesWhenMetColouredFieldFromLeft(rowIdx);
        correctRowSequencesRangesWhenMetColouredFieldFromRight(rowIdx);
    }

    public void correctRowSequencesRangesWhenMetColouredFieldFromLeft(int rowIdx) {
        List<List<Integer>> rowSeqRanges = getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSeqLengths = getNonogramRules().getRowSequencesLengths().get(rowIdx);

        boolean changed = false;
        int seqId = 0;
        int seqLength = rowSeqLengths.get(seqId);

        for (int colIdx = 0; colIdx < getNonogramRules().getWidth(); colIdx++) {
            Field field = new Field(rowIdx, colIdx);
            if (isFieldColoured(nonogramSolutionBoard, field)) {
                List<Integer> oldRange = rowSeqRanges.get(seqId);
                List<Integer> updatedRange = RangeCorrectionHelper.updatedSequenceRangeWhenMetColouredField(
                        oldRange.get(0), oldRange.get(1), colIdx, seqLength, true);

                if (!updatedRange.equals(oldRange)) {
                    rowSeqRanges.set(seqId, updatedRange);
                    logRowSequenceCorrection(rowIdx, seqId, oldRange, updatedRange, "met coloured field from left side");
                    changed = true;
                }

                colIdx += seqLength;
                seqId++;
                if (seqId >= rowSeqLengths.size()) break;
                seqLength = rowSeqLengths.get(seqId);
            }
        }

        if (changed) {
            nonogramState.increaseMadeSteps();
            addRowToAffectedActionsByIdentifiers(rowIdx, NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS);
        }
    }

    public void correctRowSequencesRangesWhenMetColouredFieldFromRight(int rowIdx) {
        List<List<Integer>> rowSeqRanges = getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSeqLengths = getNonogramRules().getRowSequencesLengths().get(rowIdx);

        boolean changed = false;
        int seqId = rowSeqLengths.size() - 1;
        int seqLength = rowSeqLengths.get(seqId);

        for (int colIdx = getNonogramRules().getWidth() - 1; colIdx >= 0; colIdx--) {
            Field field = new Field(rowIdx, colIdx);
            if (isFieldColoured(nonogramSolutionBoard, field)) {
                List<Integer> oldRange = rowSeqRanges.get(seqId);
                List<Integer> updatedRange = RangeCorrectionHelper.updatedSequenceRangeWhenMetColouredField(
                        oldRange.get(0), oldRange.get(1), colIdx, seqLength, false);

                if (!updatedRange.equals(oldRange)) {
                    updateRowSequenceRange(rowIdx, seqId, updatedRange);
                    logRowSequenceCorrection(rowIdx, seqId, oldRange, updatedRange, "met coloured field from right side");

                    if (rangeLength(updatedRange) == rowSeqLengths.get(seqId)
                            && isColumnRangeColoured(rowIdx, updatedRange)) {
                        excludeSequenceInRow(rowIdx, seqId);
                    }

                    changed = true;
                }

                colIdx -= seqLength;
                seqId--;
                if (seqId < 0) break;
                seqLength = rowSeqLengths.get(seqId);
            }
        }

        if (changed) {
            nonogramState.increaseMadeSteps();
            addRowToAffectedActionsByIdentifiers(rowIdx, NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS);
        }
    }

    @Override
    public void correctRowSequencesRangesIfXOnWay(int rowIdx, boolean changeLogicDetails) {
        boolean changed = false;

        List<Integer> lengths = getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> ranges = getRowsSequencesRanges().get(rowIdx);
        List<Integer> excluded = getRowsSequencesIdsNotToInclude().get(rowIdx);

        for (int seqIdx = 0; seqIdx < ranges.size(); seqIdx++) {
            if (excluded.contains(seqIdx)) continue;

            List<Integer> currentRange = ranges.get(seqIdx);
            int length = lengths.get(seqIdx);

            int newStart = RangeCorrectionHelper.findFirstValidSequenceStartIndexWithoutX(
                    currentRange.get(0), currentRange.get(1), length, rowIdx, false, nonogramSolutionBoard
            );

            int newEnd = RangeCorrectionHelper.findLastValidSequenceEndIndexWithoutX(
                    currentRange.get(0), currentRange.get(1), length, rowIdx, false, nonogramSolutionBoard
            );

            List<Integer> newRange = List.of(newStart, newEnd);

            if (!rangesEqual(currentRange, newRange)) {
                changed = true;
                updateRowSequenceRange(rowIdx, seqIdx, newRange);
                tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, seqIdx, currentRange, newRange, "\"X\" on way");
                addLog();

                if (changeLogicDetails &&
                        rangeLength(newRange) == length &&
                        isColumnRangeColoured(rowIdx, newRange)) {
                    excludeSequenceInRow(rowIdx, seqIdx);
                }
            }
        }

        if (changed && changeLogicDetails) {
            nonogramState.increaseMadeSteps();
            addRowToAffectedActionsByIdentifiers(rowIdx, NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY);
        }
    }

    @Override
    public void correctRowSequencesRangesWhenMatchingFieldsToSequences(int rowIdx) {
        List<String> boardRow = this.getNonogramSolutionBoard().get(rowIdx);

        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);

        List<Integer> rowSequencesLengths = this.getNonogramRules().getRowSequencesLengths().get(rowIdx);

        List<List<Integer>> colouredSequencesPartsRanges = collectColouredSequencesRangesInRow(this.getNonogramSolutionBoard(), rowIdx);

        List<List<Integer>> colouredSequencesPartsMaxRanges = getColouredSequencesPartsMaxRanges(rowIdx, colouredSequencesPartsRanges);

        List<List<Integer>> colouredSequencesPartsMatches = new ArrayList<>();

        for (List<Integer> colouredRange : colouredSequencesPartsRanges) {
            List<Integer> matches = new ArrayList<>();

            for (int seqIdx = 0; seqIdx < rowSequencesRanges.size(); seqIdx++) {
                List<Integer> seqRange = rowSequencesRanges.get(seqIdx);
                int seqLength = rowSequencesLengths.get(seqIdx);
                int colouredLength = rangeLength(colouredRange);

                if (rangeInsideAnotherRange(colouredRange, seqRange)
                        && colouredLength <= seqLength) {
                    matches.add(seqIdx);
                }
            }

            colouredSequencesPartsMatches.add(matches);
        }

        log.debug("Analyzing row {}", rowIdx);
        log.debug("Board row: {}", boardRow);
        log.debug("Row sequence ranges: {}", rowSequencesRanges);
        log.debug("Row sequence lengths: {}", rowSequencesLengths);
        log.debug("Coloured sequences ranges: {}", colouredSequencesPartsRanges);
        log.debug("Max coloured ranges: {}", colouredSequencesPartsMaxRanges);
        log.debug("Matches: {}", colouredSequencesPartsMatches);
    }

    @Override
    public void correctRowSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(int rowIdx) {
        boolean anyUpdated = false;

        List<List<Integer>> ranges = getRowsSequencesRanges().get(rowIdx);

        for (int seqIdx = 0; seqIdx < ranges.size(); seqIdx++) {
            List<Integer> currentRange = ranges.get(seqIdx);
            List<Integer> updatedRange = RangeCorrectionHelper.adjustRangeIfColouredAtEdges(
                    currentRange, rowIdx, false, nonogramSolutionBoard, getNonogramRules().getWidth()
            );

            if (!updatedRange.equals(currentRange)) {
                anyUpdated = true;
                updateRowSequenceRange(rowIdx, seqIdx, updatedRange);
                nonogramState.increaseMadeSteps();

                tmpLog = generateCorrectingRowSequenceRangeStepDescription(
                        rowIdx, seqIdx, currentRange, updatedRange,
                        "correcting row sequence range when start from edge index will create too long sequence"
                );
                addLog();
            }
        }

        if (anyUpdated) {
            addRowToAffectedActionsByIdentifiers(rowIdx, NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE);
        }
    }

    @Override
    public void colourOverlappingFieldsInRow(int rowIdx) {
        List<Integer> sequenceLengths = getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> sequenceRanges = getRowsSequencesRanges().get(rowIdx);

        for (int sequenceIdx = 0; sequenceIdx < sequenceLengths.size(); sequenceIdx++) {
            int sequenceLength = sequenceLengths.get(sequenceIdx);
            List<Integer> range = sequenceRanges.get(sequenceIdx);

            List<Integer> overlapRange = calculateOverlappingRange(range, sequenceLength);
            colourAllEmptyFieldsInRange(rowIdx, overlapRange, sequenceIdx);
        }
    }

    private void colourAllEmptyFieldsInRange(int rowIdx, List<Integer> columns, int sequenceIdx) {
        if (columns.isEmpty()) return;

        int sequenceLength = getNonogramRules().getRowSequencesLengths().get(rowIdx).get(sequenceIdx);

        for (int colIdx : columns) {
            Field field = new Field(rowIdx, colIdx);

            if (isFieldEmpty(nonogramSolutionBoard, field)) {
                this.colourFieldAtGivenPosition(field, "R---");
                tmpLog = generateColourStepDescription(rowIdx, colIdx, FILL_OVERLAPPING_FIELDS);
                addLog();
                addRowAndColumnToAffectedByIdentifiers(field, NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW);
                nonogramState.increaseMadeSteps();
            } else if (SHOW_REPETITIONS) {
                logger.warn("Row field was coloured earlier (overlap).");
            }
        }

        if (columns.size() == sequenceLength) {
            excludeSequenceInRow(rowIdx, sequenceIdx);
        }
    }

    @Override
    public void colourFieldsIfInRowXWouldForceTooLongColouredFieldsSequence(int rowIdx) {
        List<Integer> rowSequencesLengths = this.getNonogramRules().getRowSequencesLengths().get(rowIdx);

        List<List<Integer>> oldRowSequencesRanges = cloneAndMakeImmutable2DList(this.getRowsSequencesRanges().get(rowIdx));

        List<List<Integer>> colouredSequencesRanges = collectColouredSequencesRangesInRow(this.getNonogramSolutionBoard(), rowIdx);

        Map<List<Integer>, List<Integer>> possibleColouredSequencesRangesSequencesId;

        int mergedSequenceStartColumnIdx;
        int mergePointColumnIdx;
        int mergedSequenceEndColumnIdx;
        List<Integer> mergedSequenceRange;
        boolean onePossibleSequencesNotMergeToTooLongColouredFieldsSequence;
        int currentSequenceLength;

        List<Integer> rowSequenceRange;
        int seqContainingMergedLen;


        List<Integer> possibleSeqIdsMatchedToFirstColouredSequence;

        Field fieldWithTemporaryX;

        for (int colouredSeqPartId = 0; colouredSeqPartId < colouredSequencesRanges.size() - 1; colouredSeqPartId++) {
            List<Integer> first = colouredSequencesRanges.get(colouredSeqPartId);
            List<Integer> second = colouredSequencesRanges.get(colouredSeqPartId + 1);

            mergedSequenceStartColumnIdx = first.get(0);
            mergePointColumnIdx = second.get(0) - 1;
            mergedSequenceEndColumnIdx = second.get(1);

            fieldWithTemporaryX = new Field(rowIdx, mergedSequenceStartColumnIdx - 1);
            if (isFieldEmpty(this.getNonogramSolutionBoard(), fieldWithTemporaryX)) {
                placeXAtGivenField(fieldWithTemporaryX, false);
                correctRowSequencesRangesIfXOnWay(rowIdx, false);
                clearField(fieldWithTemporaryX);
            }

            possibleColouredSequencesRangesSequencesId = matchColouredSequencesToPossibleSeqIDs(colouredSequencesRanges, this.getRowsSequencesRanges().get(rowIdx));

            possibleSeqIdsMatchedToFirstColouredSequence = possibleColouredSequencesRangesSequencesId.get(first);

            onePossibleSequencesNotMergeToTooLongColouredFieldsSequence = false;
            for (int seqId : possibleSeqIdsMatchedToFirstColouredSequence) {
                currentSequenceLength = rowSequencesLengths.get(seqId);
                if (mergedSequenceStartColumnIdx + currentSequenceLength - 1 < mergePointColumnIdx) {
                    onePossibleSequencesNotMergeToTooLongColouredFieldsSequence = true;
                    break;
                } else {
                    mergedSequenceRange = List.of(mergedSequenceStartColumnIdx, mergedSequenceEndColumnIdx);
                    for (int seqIdToCheckIfCanContainMergedRange : possibleSeqIdsMatchedToFirstColouredSequence) {
                        rowSequenceRange = this.getRowsSequencesRanges().get(rowIdx).get(seqIdToCheckIfCanContainMergedRange);
                        seqContainingMergedLen = rowSequencesLengths.get(seqIdToCheckIfCanContainMergedRange);
                        if (rangeInsideAnotherRange(mergedSequenceRange, rowSequenceRange) && rangeLength(mergedSequenceRange) <= seqContainingMergedLen) {
                            onePossibleSequencesNotMergeToTooLongColouredFieldsSequence = true;
                            break;
                        }
                    }
                }
            }

            int fieldColumnIdx = mergedSequenceStartColumnIdx - 1;
            Field fieldToColour = new Field(rowIdx, fieldColumnIdx);
            if (!onePossibleSequencesNotMergeToTooLongColouredFieldsSequence) {
                this.colourFieldAtGivenPosition(fieldToColour, "R---");
                this.addRowAndColumnToAffectedByIdentifiers(fieldToColour, NonogramSolveAction.COLOUR_FIELDS_IN_ROW_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE);
                this.nonogramState.increaseMadeSteps();
                tmpLog = generateColourStepDescription(rowIdx, fieldColumnIdx,
                        "colour field if X would force too long coloured fields sequence (on left) in row");
                addLog();
            }

            setRowSequencesRanges(rowIdx, mutableClone2DList(oldRowSequencesRanges));
        }

        List<Integer> possibleSeqIdsMatchedToSecondColouredSequence;

        for (int colouredSeqPartId = colouredSequencesRanges.size() - 1; colouredSeqPartId > 0; colouredSeqPartId--) {
            List<Integer> second = colouredSequencesRanges.get(colouredSeqPartId);
            List<Integer> first = colouredSequencesRanges.get(colouredSeqPartId - 1);

            mergedSequenceStartColumnIdx = first.get(0);
            mergedSequenceEndColumnIdx = second.get(1);
            mergePointColumnIdx = first.get(1) + 1;

            fieldWithTemporaryX = new Field(rowIdx, mergedSequenceEndColumnIdx + 1);
            if (isFieldEmpty(this.getNonogramSolutionBoard(), fieldWithTemporaryX)) {
                placeXAtGivenField(fieldWithTemporaryX, false);
                correctRowSequencesRangesIfXOnWay(rowIdx, false);
                clearField(fieldWithTemporaryX);
            }

            possibleColouredSequencesRangesSequencesId = matchColouredSequencesToPossibleSeqIDs(colouredSequencesRanges, this.getRowsSequencesRanges().get(rowIdx));

            possibleSeqIdsMatchedToSecondColouredSequence = possibleColouredSequencesRangesSequencesId.get(second);

            onePossibleSequencesNotMergeToTooLongColouredFieldsSequence = false;
            for (int seqId : possibleSeqIdsMatchedToSecondColouredSequence) {
                currentSequenceLength = rowSequencesLengths.get(seqId);
                if (mergedSequenceEndColumnIdx - currentSequenceLength + 1 > mergePointColumnIdx) {
                    onePossibleSequencesNotMergeToTooLongColouredFieldsSequence = true;
                    break;
                } else {
                    mergedSequenceRange = List.of(mergedSequenceStartColumnIdx, mergedSequenceEndColumnIdx);
                    for (int seqIdToCheckIfCanContainMergedRange : possibleSeqIdsMatchedToSecondColouredSequence) {
                        rowSequenceRange = this.getRowsSequencesRanges().get(rowIdx).get(seqIdToCheckIfCanContainMergedRange);
                        seqContainingMergedLen = rowSequencesLengths.get(seqIdToCheckIfCanContainMergedRange);
                        if (rangeInsideAnotherRange(mergedSequenceRange, rowSequenceRange) && rangeLength(mergedSequenceRange) <= seqContainingMergedLen) {
                            onePossibleSequencesNotMergeToTooLongColouredFieldsSequence = true;
                            break;
                        }
                    }
                }
            }

            int fieldColumnIdx = mergedSequenceEndColumnIdx + 1;
            Field fieldToColour = new Field(rowIdx, fieldColumnIdx);
            if (!onePossibleSequencesNotMergeToTooLongColouredFieldsSequence) {
                this.colourFieldAtGivenPosition(fieldToColour, "R---");
                this.addRowAndColumnToAffectedByIdentifiers(fieldToColour, NonogramSolveAction.COLOUR_FIELDS_IN_ROW_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE);
                this.nonogramState.increaseMadeSteps();

                tmpLog = generateColourStepDescription(rowIdx, fieldColumnIdx,
                        "colour field if X would force too long coloured fields sequence (on right) in row");
                addLog();
            }

            setRowSequencesRanges(rowIdx, mutableClone2DList(oldRowSequencesRanges));
        }
    }

    @Override
    public void extendColouredFieldsNearXToMaximumPossibleLengthInRow(int rowIdx) {
        extendColouredFieldsToLeftNearXToMaximumPossibleLengthInRow(rowIdx);
        extendColouredFieldsToRightNearXToMaximumPossibleLengthInRow(rowIdx);
    }

    public void extendColouredFieldsToLeftNearXToMaximumPossibleLengthInRow(int rowIdx) {
        List<String> rowBefore = getRowCopy(rowIdx);
        boolean anyGlobalFieldColoured = false;

        for (int columnIdx = this.getNonogramRules().getWidth() - 1; columnIdx >= 0; columnIdx--) {
            Field currentField = new Field(rowIdx, columnIdx);

            if (isFieldColoured(this.nonogramSolutionBoard, currentField)) {
                List<Integer> colouredRange = findColouredSequenceRangeLeft(rowIdx, columnIdx);
                List<Integer> possibleSequenceLengths = findPossibleSequenceLengths(
                        this.getRowsSequencesRanges().get(rowIdx),
                        colouredRange,
                        this.getNonogramRules().getRowSequencesLengths().get(rowIdx)
                );

                if (possibleSequenceLengths.isEmpty()) {
                    this.nonogramState.invalidateSolution();
                    break;
                }

                int minSequenceLength = Collections.min(possibleSequenceLengths);
                int distanceFromX = findDistanceFromRightX(rowIdx, colouredRange, minSequenceLength);

                if (distanceFromX > 0) {
                    int minExtensionIdx = colouredRange.get(0) + distanceFromX - minSequenceLength;
                    boolean extended = extendToLeft(rowIdx, colouredRange.get(0) - 1, minExtensionIdx);

                    if (extended) {
                        anyGlobalFieldColoured = true;
                    }
                }

                columnIdx = colouredRange.get(0) - 1;
            }
        }

        if (anyGlobalFieldColoured) {
            List<String> rowAfter = getRowCopy(rowIdx);
            tmpLog = generateExtendSequenceInRow(
                    rowIdx,
                    "toLeft",
                    rowBefore,
                    this.getRowsSequencesRanges().get(rowIdx),
                    this.getNonogramRules().getRowSequencesLengths().get(rowIdx),
                    rowAfter
            );
            addLog();
        }
    }

    private List<Integer> findColouredSequenceRangeLeft(int rowIdx, int startColIdx) {
        int start = startColIdx;
        while (start - 1 >= 0 && isFieldColoured(this.nonogramSolutionBoard, new Field(rowIdx, start - 1))) {
            start--;
        }
        return List.of(start, startColIdx);
    }

    private int findDistanceFromRightX(int rowIdx, List<Integer> colouredRange, int maxDist) {
        for (int offset = rangeLength(colouredRange); offset < maxDist; offset++) {
            int idxToCheck = colouredRange.get(0) + offset;
            if (idxToCheck >= this.getNonogramRules().getWidth()) break;

            Field fieldToCheck = new Field(rowIdx, idxToCheck);
            if (isFieldWithX(this.nonogramSolutionBoard, fieldToCheck)) {
                return offset;
            }
        }
        return 0;
    }

    private boolean extendToLeft(int rowIdx, int fromInclusive, int toInclusive) {
        boolean anyFieldColoured = false;

        for (int col = fromInclusive; col >= toInclusive && col >= 0; col--) {
            Field field = new Field(rowIdx, col);
            try {
                if (isFieldEmpty(this.nonogramSolutionBoard, field)) {
                    this.colourFieldAtGivenPosition(field, "R---");
                    this.addRowAndColumnToAffectedByIdentifiers(field, NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW);
                    this.nonogramState.increaseMadeSteps();
                    anyFieldColoured = true;
                } else if (SHOW_REPETITIONS) {
                    System.out.println("Row field already coloured.");
                }
            } catch (IndexOutOfBoundsException e) {
                this.nonogramState.invalidateSolution();
            }
        }

        return anyFieldColoured;
    }

    public void extendColouredFieldsToRightNearXToMaximumPossibleLengthInRow(int rowIdx) {
        List<String> rowBefore = getRowCopy(rowIdx);
        boolean anyGlobalFieldColoured = false;

        for (int columnIdx = 0; columnIdx < this.getNonogramRules().getWidth(); columnIdx++) {
            Field currentField = new Field(rowIdx, columnIdx);

            if (isFieldColoured(this.nonogramSolutionBoard, currentField)) {
                List<Integer> colouredRange = findColouredSequenceRangeRight(rowIdx, columnIdx);
                List<Integer> possibleSequenceLengths = findPossibleSequenceLengths(
                        this.getRowsSequencesRanges().get(rowIdx),
                        colouredRange,
                        this.getNonogramRules().getRowSequencesLengths().get(rowIdx)
                );

                if (possibleSequenceLengths.isEmpty()) {
                    this.nonogramState.invalidateSolution();
                    break;
                }

                int minSequenceLength = Collections.min(possibleSequenceLengths);
                int distanceFromX = findDistanceFromLeftX(rowIdx, colouredRange, minSequenceLength);

                if (distanceFromX > 0) {
                    int maxExtensionIdx = colouredRange.get(1) - distanceFromX + minSequenceLength;
                    boolean extended = extendToRight(rowIdx, colouredRange.get(1) + 1, maxExtensionIdx);
                    if (extended) {
                        anyGlobalFieldColoured = true;
                    }
                }

                columnIdx = colouredRange.get(1) + 1;
            }
        }

        if (anyGlobalFieldColoured) {
            List<String> rowAfter = getRowCopy(rowIdx);
            tmpLog = generateExtendSequenceInRow(
                    rowIdx,
                    "toRight",
                    rowBefore,
                    this.getRowsSequencesRanges().get(rowIdx),
                    this.getNonogramRules().getRowSequencesLengths().get(rowIdx),
                    rowAfter
            );
            addLog();
        }
    }

    private List<Integer> findPossibleSequenceLengths(
            List<List<Integer>> ranges,
            List<Integer> colouredRange,
            List<Integer> lengths
    ) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < ranges.size(); i++) {
            if (rangeInsideAnotherRange(colouredRange, ranges.get(i))
                    && lengths.get(i) >= rangeLength(colouredRange)) {
                result.add(lengths.get(i));
            }
        }
        return result;
    }

    private List<Integer> findColouredSequenceRangeRight(int rowIdx, int startColIdx) {
        int endColIdx = startColIdx;
        while (endColIdx + 1 < this.getNonogramRules().getWidth()
                && isFieldColoured(this.nonogramSolutionBoard, new Field(rowIdx, endColIdx + 1))) {
            endColIdx++;
        }
        return List.of(startColIdx, endColIdx);
    }

    private int findDistanceFromLeftX(int rowIdx, List<Integer> colouredRange, int maxDist) {
        for (int offset = rangeLength(colouredRange); offset < maxDist; offset++) {
            int idxToCheck = colouredRange.get(1) - offset;
            if (idxToCheck < 0) break;

            Field fieldToCheck = new Field(rowIdx, idxToCheck);
            if (isFieldWithX(this.nonogramSolutionBoard, fieldToCheck)) {
                return offset;
            }
        }
        return 0;
    }

    private boolean extendToRight(int rowIdx, int fromInclusive, int toInclusive) {
        boolean anyFieldColoured = false;

        for (int col = fromInclusive; col <= toInclusive && col < this.getNonogramRules().getWidth(); col++) {
            Field field = new Field(rowIdx, col);
            try {
                if (isFieldEmpty(this.nonogramSolutionBoard, field)) {
                    this.colourFieldAtGivenPosition(field, "R---");
                    this.addRowAndColumnToAffectedByIdentifiers(field, NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW);
                    this.nonogramState.increaseMadeSteps();
                    anyFieldColoured = true;
                } else if (SHOW_REPETITIONS) {
                    System.out.println("Row field already coloured.");
                }
            } catch (IndexOutOfBoundsException e) {
                this.nonogramState.invalidateSolution();
            }
        }

        return anyFieldColoured;
    }

    private List<String> getRowCopy(int rowIdx) {
        return new ArrayList<>(this.nonogramSolutionBoard.get(rowIdx));
    }

    @Override
    public void colourFieldsInRowIfXCausesAssignmentConflict(int rowIdx) {
        List<String> boardRow = this.getNonogramSolutionBoard().get(rowIdx);
        List<List<Integer>> oldRowSequencesRanges = cloneAndMakeImmutable2DList(this.getRowsSequencesRanges().get(rowIdx));
        List<List<Integer>> colouredFieldsInRowRanges = collectColouredSequencesRangesInRow(this.getNonogramSolutionBoard(), rowIdx);

        // no X fields between ranges to check if placement is wrong
        if (colouredFieldsInRowRanges.size() < 2) return;

        for (int i = 0; i < colouredFieldsInRowRanges.size() - 1; i++) {
            int leftEnd = colouredFieldsInRowRanges.get(i).get(1);
            int rightStart = colouredFieldsInRowRanges.get(i + 1).get(0);

            for (int betweenColumnIdx = leftEnd + 1; betweenColumnIdx < rightStart; betweenColumnIdx++) {
                if (!Objects.equals(boardRow.get(betweenColumnIdx), EMPTY_FIELD)) continue;

                Field fieldToCheck = new Field(rowIdx, betweenColumnIdx);
                if (isFieldEmpty(this.getNonogramSolutionBoard(), fieldToCheck)) {
                    placeXAtGivenField(fieldToCheck, false);
                    correctRowSequencesRangesIfXOnWay(rowIdx, false);
                    clearField(fieldToCheck);
                }
                List<List<Integer>> updatedRanges = this.getRowsSequencesRanges().get(rowIdx);

                boolean conflict = false;

                // check if any coloured sequence part not matches into any range
                for (List<Integer> colouredRange : colouredFieldsInRowRanges) {
                    boolean matchesAny = false;
                    for (List<Integer> updatedRange : updatedRanges) {
                        if (rangeInsideAnotherRange(colouredRange, updatedRange)) {
                            matchesAny = true;
                            break;
                        }
                    }
                    if (!matchesAny) {
                        conflict = true;
                        break;
                    }
                }

                // restore old row ranges
                setRowSequencesRanges(rowIdx, mutableClone2DList(oldRowSequencesRanges));

                if (conflict) {
                    this.colourFieldAtGivenPosition(fieldToCheck, "R---");
                    addColumnToAffectedActionsByIdentifiers(fieldToCheck.getColumnIdx(),
                            NonogramSolveAction.COLOUR_FIELDS_IN_ROW_IF_X_CAUSES_ASSIGNMENT_CONFLICT);
                    this.nonogramState.increaseMadeSteps();
                    tmpLog = "Field at (" + rowIdx + ", " + betweenColumnIdx + ") must be 'O' because 'X' would cause a conflict in sequences.";
                    addLog();
                }
            }
        }
    }

    @Override
    public void placeXsRowAtUnreachableFields(int rowIdx) {
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        boolean existRangeIncludingColumn;
        List<Integer> fieldAsRange;
        Field fieldToExclude;

        for (int columnIdx = 0; columnIdx < this.getNonogramRules().getWidth(); columnIdx++) {
            fieldAsRange = List.of(columnIdx, columnIdx);
            existRangeIncludingColumn = rangesListIncludingAnotherRange(rowSequencesRanges, fieldAsRange);

            if (!existRangeIncludingColumn) {
                fieldToExclude = new Field(rowIdx, columnIdx);
                if (isFieldEmpty(this.nonogramSolutionBoard, fieldToExclude)) {
                    this.placeXAtGivenField(fieldToExclude, true);
                    this.addRowAndColumnToAffectedByIdentifiers(fieldToExclude, NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS);

                    tmpLog = generatePlacingXStepDescription(rowIdx, columnIdx, "placing \"X\" at unreachable field");
                    addLog();

                    this.nonogramState.increaseMadeSteps();
                } else if (this.SHOW_REPETITIONS) {
                    System.out.println("X at unreachable field in row placed earlier!");
                }
            }
        }
    }

    @Override
    public void placeXsAroundLongestSequencesInRow(int rowIdx) {
        int width = this.getNonogramRules().getWidth();

        for (int colIdx = 0; colIdx < width; colIdx++) {
            Field field = new Field(rowIdx, colIdx);
            if (isFieldColoured(this.nonogramSolutionBoard, field)) {
                List<Integer> colouredRange = findColouredSequenceRangeInRow(colIdx, rowIdx);
                colIdx = colouredRange.get(1);

                processColouredSequenceRange(rowIdx, colouredRange);
            }
        }
    }

    private List<Integer> findColouredSequenceRangeInRow(int startColumnIdx, int rowIdx) {
        int columnIdx = startColumnIdx;

        List<Integer> colouredSequenceRange = new ArrayList<>();
        colouredSequenceRange.add(columnIdx);
        while (columnIdx < this.getNonogramRules().getWidth() && isFieldColoured(this.nonogramSolutionBoard, new Field(rowIdx, columnIdx))) {
            columnIdx++;
        }
        colouredSequenceRange.add(columnIdx - 1);

        return colouredSequenceRange;
    }

    private void processColouredSequenceRange(int rowIdx, List<Integer> colouredRange) {
        List<List<Integer>> rowRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowLengths = this.getNonogramRules().getRowSequencesLengths().get(rowIdx);

        int lengthOnBoard = rangeLength(colouredRange);

        List<Integer> matchingIndices = new ArrayList<>();
        List<Integer> matchingLengths = new ArrayList<>();

        for (int i = 0; i < rowRanges.size(); i++) {
            if (rangeInsideAnotherRange(colouredRange, rowRanges.get(i))) {
                matchingIndices.add(i);
                matchingLengths.add(rowLengths.get(i));
            }
        }

        List<Integer> edgeXs = List.of(colouredRange.get(0) - 1, colouredRange.get(1) + 1);

        if (matchingIndices.size() == 1 && lengthOnBoard == matchingLengths.get(0)) {
            placeXsAndUpdateSingleSequence(rowIdx, edgeXs, matchingIndices.get(0), colouredRange);
        } else if (matchingLengths.size() > 1 && lengthOnBoard == Collections.max(matchingLengths)) {
            placeXsAroundLongestSequence(rowIdx, edgeXs, false);
        }
    }

    private void placeXsAndUpdateSingleSequence(int rowIdx, List<Integer> xEdges, int seqIdx, List<Integer> colouredRange) {
        placeXsAroundLongestSequence(rowIdx, xEdges, true);

        List<Integer> updatedRange = List.of(xEdges.get(0) + 1, xEdges.get(1) - 1);
        excludeColouredFieldsBetweenXs(rowIdx, updatedRange);

        updateLogicAfterXsPlacement(rowIdx, seqIdx, colouredRange, updatedRange);
    }

    private void placeXsAroundLongestSequence(int rowIdx, List<Integer> xEdges, boolean onlyMatching) {
        String logTag = onlyMatching ? "[only possible]" : "[sequence index not specified]";
        for (int col : xEdges) {
            if (!isColumnIndexValid(col)) continue;

            Field edgeField = new Field(rowIdx, col);
            if (isFieldEmpty(this.nonogramSolutionBoard, edgeField)) {
                logPlacingX(rowIdx, col, xEdges, logTag);
                this.placeXAtGivenField(edgeField, true);
                this.addRowAndColumnToAffectedByIdentifiers(edgeField, NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES);
                this.nonogramState.increaseMadeSteps();
            } else if (this.SHOW_REPETITIONS) {
                log.warn("X around longest sequence already placed at {} {}", edgeField, logTag);
            }
        }
    }

    private void excludeColouredFieldsBetweenXs(int rowIdx, List<Integer> range) {
        for (int col = range.get(0); col <= range.get(1); col++) {
            this.excludeFieldInRow(new Field(rowIdx, col));
            this.nonogramState.increaseMadeSteps();
        }
    }

    private void updateLogicAfterXsPlacement(int rowIdx, int seqIdx, List<Integer> oldRange, List<Integer> newRange) {
        this.changeRowSequenceRange(rowIdx, seqIdx, newRange);
        tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, seqIdx, oldRange, newRange,
                "correcting sequence while placing X before only matching coloured sequence");
        addLog();
        this.excludeSequenceInRow(rowIdx, seqIdx);

        Field leftEdge = new Field(rowIdx, newRange.get(0) - 1);
        Field rightEdge = new Field(rowIdx, newRange.get(1) + 1);

        if (isColumnIndexValid(leftEdge.getColumnIdx())) {
            this.addRowAndColumnToAffectedByIdentifiers(leftEdge, NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES);
        }
        if (isColumnIndexValid(rightEdge.getColumnIdx())) {
            this.addRowAndColumnToAffectedByIdentifiers(rightEdge, NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES);
        }
    }

    private void logPlacingX(int rowIdx, int colIdx, List<Integer> xEdges, String tag) {
        String part = (colIdx == xEdges.get(0)) ? "before" : "after";
        String message = "placing \"X\" " + part + " longest sequence in row " + tag;
        tmpLog = generatePlacingXStepDescription(rowIdx, colIdx, message);
        addLog();
    }

    @Override
    public void placeXsRowAtTooShortEmptySequences(int rowIdx) {

        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths = this.getNonogramRules().getRowSequencesLengths().get(rowIdx);
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

        for (int columnIdx = 0; columnIdx < this.getNonogramRules().getWidth() - 1; columnIdx++) {
            onlyEmptyFieldsInSequence = true;
            potentiallyXPlacedField = new Field(rowIdx, columnIdx);
            if (isFieldWithX(this.nonogramSolutionBoard, potentiallyXPlacedField)) {

                firstXIndex = columnIdx;
                fieldAfterXToCheck = new Field(rowIdx, ++columnIdx);
                while(columnIdx < this.getNonogramRules().getWidth()) {
                    if (isFieldEmpty(this.nonogramSolutionBoard, fieldAfterXToCheck)) {
                        fieldAfterXToCheck = new Field(rowIdx, ++columnIdx);
                    } else {
                        if (isFieldColoured(this.nonogramSolutionBoard, fieldAfterXToCheck)) {
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
                            if (isFieldEmpty(this.nonogramSolutionBoard, fieldToExclude)) {
                                this.placeXAtGivenField(fieldToExclude, true);
                                this.addColumnToAffectedActionsByIdentifiers(emptyFieldColumnIdx, NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES);

                                tmpLog = generatePlacingXStepDescription(rowIdx, emptyFieldColumnIdx, "placing \"X\" inside too short empty fields sequence");
                                addLog();

                                this.nonogramState.increaseMadeSteps();
                            } else if (this.SHOW_REPETITIONS) {
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
    @Override
    public void placeXsRowIfOWillMergeNearFieldsToTooLongColouredSequence(int rowIdx) {
        List<Integer> colouredFieldsIndexesInRow = findColouredFieldsIndexesInRow(this.nonogramSolutionBoard, rowIdx);

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
                this.placeXAtGivenField(fieldWithPreviousColumnColoured, true);
                this.addRowAndColumnToAffectedByIdentifiers(fieldWithPreviousColumnColoured, NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE);

                tmpLog = generatePlacingXStepDescription(rowIdx, previousColumnIndex,
                        "placing \"X\" because \"O\" will create too long sequence");
                addLog();
                this.nonogramState.increaseMadeSteps();
            } else if (this.SHOW_REPETITIONS) {
                System.out.println("X because \"O\" will create too long sequence in row placed earlier!");
            }

            mergedSequenceWithFieldAddedAfter = currentColouredSequenceRangesWithColouredFieldAdded.get(1);

            nextColumnIndex = mergedSequenceWithFieldAddedAfter.get(1);
            if (nextColumnIndex != this.getNonogramRules().getWidth()) {

                if (seqRangeIndex < colouredSequencesRanges.size() - 1) {
                    colouredSequenceRangeAfterCurrent = colouredSequencesRanges.get(seqRangeIndex + 1);
                    mergedSequenceWithFieldAddedAfter = tryToMergeColouredSequenceWithNext(
                            mergedSequenceWithFieldAddedAfter, colouredSequenceRangeAfterCurrent);
                }

                fieldWithNextColumnColoured = new Field(rowIdx, nextColumnIndex);
                colouredSequenceValid = colouredSequenceInRowIsValid(mergedSequenceWithFieldAddedAfter, rowIdx, this);
                if (!colouredSequenceValid && isFieldEmpty(this.getNonogramSolutionBoard(), fieldWithNextColumnColoured)) {
                    this.placeXAtGivenField(fieldWithNextColumnColoured, true);
                    this.addRowAndColumnToAffectedByIdentifiers(fieldWithNextColumnColoured, NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE);

                    tmpLog = generatePlacingXStepDescription(rowIdx, nextColumnIndex,
                            "placing \"X\" because \"O\" will create too long sequence");
                    addLog();
                    this.nonogramState.increaseMadeSteps();
                } else if (this.SHOW_REPETITIONS) {
                    System.out.println("X because \"O\" will create too long sequence in row placed earlier!");
                }
            }
        }
    }

    @Override
    public void placeXsRowIfONearXWillBeginTooLongPossibleColouredSequence(int rowIdx) {
        Field fieldToCheckX;
        Field firstColouredField;
        List<Integer> emptyFieldsRange;
        List<Integer> colouredFieldsRange;
        int emptyFieldsRangeLength;
        int colouredFieldsRangeLength;

        List<Integer> rowSequencesLengths = this.getNonogramRules().getColumnSequencesLengths().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> sequencesIdsWhichWillBeginTooLongPossibleColoured;
        List<Integer> sequencesIdsWhichNotReachColouredField;

        for (int columnIdx = this.getNonogramRules().getWidth() - 1; columnIdx > 0; columnIdx--) {
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
                                this.placeXAtGivenField(emptyFieldNearX, true);
                                this.addColumnToAffectedActionsByIdentifiers(emptyFieldNearX.getColumnIdx(), NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE);

                                tmpLog = generatePlacingXStepDescription(columnIdx, rowIdx, "placing \"X\" when \"O\" near \"X\" will begin too long possible coloured sequence");
                                addLog();

                                this.nonogramState.increaseMadeSteps();
                            }
                        }
                    }
                }
            }
        }

        for (int columnIdx = 0; columnIdx < this.getNonogramRules().getWidth(); columnIdx++) {
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
                                this.placeXAtGivenField(emptyFieldNearX, true);
                                this.addColumnToAffectedActionsByIdentifiers(emptyFieldNearX.getColumnIdx(), NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE);

                                tmpLog = generatePlacingXStepDescription(columnIdx, rowIdx, "placing \"X\" when \"O\" near \"X\" will begin too long possible coloured sequence");
                                addLog();

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
        int seqLength = this.getNonogramRules().getRowSequencesLengths().get(rowIdx).get(seqNo);
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
        int seqLength = this.getNonogramRules().getRowSequencesLengths().get(rowIdx).get(seqNo);
        List<Integer> lastRangeBeforeColouredField = List.of(rowPossibleRange.get(1) - seqLength + 1, rowPossibleRange.get(1));

        return rangeInsideAnotherRange(lastRangeBeforeColouredField, emptyFieldsRange);
    }

    @Override
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
        List<Integer> rowSequencesLengths = this.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);

        Field fieldToCheckX;

        int potentiallyColouredFieldColumn;
        Field fieldToCheckColoured;

        List<Integer> sequencesIds;
        int maxSequenceLength;
        List<List<Integer>> colouredSequences;
        List<Integer> validSequenceIds;
        List<Integer> validSequenceLengths;

        for (int columnIdx = this.getNonogramRules().getWidth() - 1; columnIdx > 0; columnIdx--) {
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
                                this.addRowToAffectedActionsByIdentifiers(fieldToColour.getColumnIdx(), NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH);
                                this.nonogramState.increaseMadeSteps();

                                tmpLog = generateColourStepDescription(rowIdx, columnToColourIdx, "extend coloured sequence to matching length to left near X (with placing X before)");
                                addLog();
                            }
                        }

                        Field fieldToPlaceX = new Field(rowIdx, colouredSequenceColStartIdx - 1);
                        this.placeXAtGivenField(fieldToPlaceX, true);
                        this.addColumnToAffectedActionsByIdentifiers(fieldToPlaceX.getColumnIdx(), NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART);

                        // moreover - only one id is valid -> can correct sequence range
                        if (validSequenceIds.size() == 1) {
                            int matchingSeqId = validSequenceIds.get(0);
                            List<Integer> oldRange = rowSequencesRanges.get(matchingSeqId);
                            List<Integer> updatedRange = new ArrayList<>(Arrays.asList(colouredSequenceColStartIdx, potentiallyColouredFieldColumn));

                            if (!rangesEqual(oldRange, updatedRange)) {
                                this.updateRowSequenceRange(rowIdx, matchingSeqId, updatedRange);
                                this.addRowToAffectedActionsByIdentifiers(rowIdx, NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART);

                                this.nonogramState.increaseMadeSteps();
                                tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, matchingSeqId, oldRange, updatedRange, "update only matching sequence part preventing excess length to left");
                                addLog();
                            }
                        }
                    }
                }
            }
        }
    }

    private void preventExtendingColouredSequenceToExcessLengthInRowToRight(int rowIdx) {
        List<Integer> rowSequencesLengths = this.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);

        Field fieldToCheckX;

        int potentiallyColouredFieldColumnIndex;
        Field fieldToCheckColoured;

        List<Integer> sequencesIds;
        int maxSequenceLength;
        List<List<Integer>> colouredSequences;
        List<Integer> validSequencesIds;
        List<Integer> validSequenceLengths;

        for (int columnIdx = 0; columnIdx < this.getNonogramRules().getWidth() - 1; columnIdx++) {
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
                                this.addRowToAffectedActionsByIdentifiers(fieldToColour.getColumnIdx(), NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART);
                                this.nonogramState.increaseMadeSteps();

                                tmpLog = generateColourStepDescription(rowIdx, columnToColourIdx, "extend coloured sequence to matching length to right near X (with placing X before)");
                                addLog();
                            }
                        }

                        Field fieldToPlaceX = new Field(rowIdx, colouredSequenceEndColumnIndex + 1);
                        this.placeXAtGivenField(fieldToPlaceX, true);
                        this.addColumnToAffectedActionsByIdentifiers(fieldToPlaceX.getColumnIdx(), NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART);

                        if (validSequencesIds.size() == 1) {
                            int matchingSeqId = validSequencesIds.get(0);
                            List<Integer> oldRange = rowSequencesRanges.get(matchingSeqId);
                            List<Integer> updatedRange = new ArrayList<>(Arrays.asList(potentiallyColouredFieldColumnIndex, colouredSequenceEndColumnIndex));

                            this.updateRowSequenceRange(rowIdx, matchingSeqId, updatedRange);
                            this.addRowToAffectedActionsByIdentifiers(rowIdx, NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART);

                            this.nonogramState.increaseMadeSteps();
                            tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, matchingSeqId, oldRange, updatedRange, "update only matching sequence part preventing excess length to right");
                            addLog();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void markAvailableFieldsInRow(int rowIdx) {
        Field potentiallyColouredField;
        List<Integer> colouredSequenceIndexes;
        int firstSequenceIndex;
        int lastSequenceIndex;
        List<Integer> rowSequencesLengths = this.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequenceRange;
        int matchingSequencesCount;
        int lastMatchingSequenceIndex = -1;
        int colouredSequenceLength;
        String sequenceMarker;

        List<Integer> onlyMatchingSequenceOldRange;
        List<Integer> newSequenceRange;

        for (int columnIdx = 0; columnIdx < this.getNonogramRules().getWidth(); columnIdx++) {
            potentiallyColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(this.nonogramSolutionBoard, potentiallyColouredField)) {

                colouredSequenceIndexes = new ArrayList<>();
                colouredSequenceIndexes.add(columnIdx);

                // TODO - do while(?)
                while(columnIdx < this.getNonogramRules().getWidth() && isFieldColoured(this.nonogramSolutionBoard, potentiallyColouredField)) {
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

                    sequenceMarker = NonogramHelper.indexToSequenceCharMark(lastMatchingSequenceIndex);
                    for (int sequenceColumnIdx = firstSequenceIndex; sequenceColumnIdx <= lastSequenceIndex; sequenceColumnIdx++) {
                        if (this.getNonogramSolutionBoardWithMarks().get(rowIdx).get(sequenceColumnIdx).startsWith(EMPTY_FIELD, 1)) {
                            this.markRowBoardField(rowIdx, sequenceColumnIdx, sequenceMarker);
                            this.nonogramState.increaseMadeSteps();
                        } else if (this.SHOW_REPETITIONS) {
                            System.out.println("Row field was marked earlier.");
                        }
                    }

                    //correct sequence range if new range is shorter
                    onlyMatchingSequenceOldRange = rowSequencesRanges.get(lastMatchingSequenceIndex);
                    newSequenceRange = calculateNewMarkedRangeFromParameters(onlyMatchingSequenceOldRange, colouredSequenceIndexes,
                            rowSequencesLengths.get(lastMatchingSequenceIndex)); // ([12, 28], [13, 13], 16)

                    if (!rangesEqual(onlyMatchingSequenceOldRange, newSequenceRange)) {
                        this.changeRowSequenceRange(rowIdx, lastMatchingSequenceIndex, newSequenceRange);
                        tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, lastMatchingSequenceIndex, onlyMatchingSequenceOldRange, newSequenceRange, CORRECTING_ROW_SEQ_RANGE_MARKING_FIELD);
                        addLog();
                        addRowToAffectedActionsByIdentifiers(rowIdx, NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW);
                    }
                }
            }
        }
    }

    @Override
    protected void excludeFieldLogicSpecific(Field field) {
        this.excludeFieldInRow(field);
    }

    public void excludeSequenceInRow(int rowIdx, int seqIdx) {
        boolean rowValid = isRowIndexValid(rowIdx);
        if (rowValid && !this.rowsSequencesIdsNotToInclude.get(rowIdx).contains(seqIdx)) {
            tmpLog = generateAddingRowSequenceToNotToIncludeDescription(rowIdx, seqIdx);
            addLog();
            this.rowsSequencesIdsNotToInclude.get(rowIdx).add(seqIdx);
            Collections.sort(this.rowsSequencesIdsNotToInclude.get(rowIdx));
        }
    }

    public void excludeFieldsInRow(List<Field> fieldsToExclude) {
        fieldsToExclude.forEach(this::excludeFieldInRow);
    }

    protected void excludeFieldInRow(Field fieldToExclude) {
        int fieldRowIdx = fieldToExclude.getRowIdx();
        int fieldColIdx = fieldToExclude.getColumnIdx();
        if (areFieldIndexesValid(fieldToExclude) && !this.rowsFieldsNotToInclude.get(fieldRowIdx).contains(fieldColIdx)) {
            this.rowsFieldsNotToInclude.get(fieldRowIdx).add(fieldColIdx);
            Collections.sort(this.rowsFieldsNotToInclude.get(fieldRowIdx));
        }
    }

    public void updateRowSequenceRange(int rowIdx, int sequenceIdx, List<Integer> updatedRange) {
        this.rowsSequencesRanges.get(rowIdx).set(sequenceIdx, updatedRange);
    }

    // TODO - extract
    // ([12, 28], [13, 13], 16)
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
            if (!isFieldColoured(this.nonogramSolutionBoard, potentiallyColouredField)) {
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
            if (isFieldWithX(this.nonogramSolutionBoard, fieldToCheck)) {
                break;
            }
        }

        return minimumColumnIndex + 1;
    }

    public int maximumColumnIndexWithoutX(int rowIdx, int firstSequenceColumnIdx, int sequenceFullLength) {
        int maximumColumnIndex = firstSequenceColumnIdx;
        int maximumColumnIndexLimit = Math.min(firstSequenceColumnIdx + sequenceFullLength - 1, this.getNonogramRules().getWidth() - 1);
        Field fieldToCheck;

        for (; maximumColumnIndex <= maximumColumnIndexLimit; maximumColumnIndex++) {
            fieldToCheck = new Field(rowIdx, maximumColumnIndex);
            if (isFieldWithX(this.nonogramSolutionBoard, fieldToCheck)) {
                break;
            }
        }

        return maximumColumnIndex - 1;
    }

    public void markRowBoardField(int rowIdx, int colIdx, String marker) {
        String oldRowField = this.nonogramSolutionBoardWithMarks.get(rowIdx).get(colIdx);
        this.nonogramSolutionBoardWithMarks.get(rowIdx).set(colIdx, MARKED_ROW_INDICATOR + marker + oldRowField.substring(2, 4));
    }

    public void changeRowSequenceRange(int rowIndex, int sequenceIndex, List<Integer> updatedRange) {
        this.rowsSequencesRanges.get(rowIndex).set(sequenceIndex, updatedRange);
    }

    public void setNonogramSolutionBoardRow(int rowIdx, List<String> boardRow) {
        for (int columnIdx = 0; columnIdx < this.getNonogramRules().getWidth(); columnIdx++) {
            this.nonogramSolutionBoard.get(rowIdx).set(columnIdx, boardRow.get(columnIdx));
        }
    }

    public String generateExtendSequenceInRow(
            int rowIndex,
            String direction,
            List<String> initialRowState,
            List<List<Integer>> rowSequenceRanges,
            List<Integer> rowSequenceLengths,
            List<String> finalRowState
    ) {
        return String.format(
                "EXTEND_ROW_SEQUENCE: row=%d, dir=%s\n" +
                        "initial=%s\n" +
                        "ranges=%s\n" +
                        "lengths=%s\n" +
                        "final=%s\n",
                rowIndex,
                direction,
                initialRowState.toString(),
                rowSequenceRanges.toString(),
                rowSequenceLengths.toString(),
                finalRowState.toString()
        );
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
