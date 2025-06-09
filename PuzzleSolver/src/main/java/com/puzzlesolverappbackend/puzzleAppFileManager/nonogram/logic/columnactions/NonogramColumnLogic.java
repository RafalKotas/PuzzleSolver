package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.Field;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.NonogramLogicParams;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.*;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramHelper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

import static com.puzzlesolverappbackend.puzzleAppFileManager.common.ArrayUtils.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.ColumnCorrectSequencesRangesHelper.reduceColouredSequenceMatches;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.ColumnCorrectSequencesRangesHelper.sequenceAssignmentAppearsAsFirstLater;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.ColumnMixedActionsHelper.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.ExcludedSequenceLogHelper.generateExcludedSequenceLog;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.OverlappingLogHelper.generateOverlappingSequenceLog;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.TooLongMergeFieldHelper.collectColouredSequencesRanges;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.model.NonogramConstants.EMPTY_FIELD;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.model.NonogramConstants.MARKED_COLUMN_INDICATOR;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.service.logic.NonogramLogicService.rangesListIncludingAnotherRange;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramBoardUtils.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramLogicUtils.colouredSequenceInColumnIsValid;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramParametersComparatorHelper.rangesEqual;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NonogramColumnLogic extends NonogramLogicParams implements ColumnActions {

    private final static String CORRECT_COLUMN_SEQ_RANGE_MARKING_FIELD = "correcting column sequence range when marking field";

    private final static String FILL_OVERLAPPING_FIELDS = "fill overlapping fields";

    private final static List<Integer> NOT_FOUND_EMPTY_FIELDS_RANGE_VALUE = List.of(-1, -1);

    private final static List<Integer> NOT_FOUND_COLOURED_FIELDS_RANGE_VALUE = List.of(-1, -1);

    protected List<List<List<Integer>>> columnsSequencesRanges;

    protected List<List<Integer>> columnsFieldsNotToInclude;

    protected List<List<Integer>> columnsSequencesIdsNotToInclude;

    public NonogramColumnLogic(NonogramLogic nonogramLogic) {
        super(
                nonogramLogic.getNonogramRules(),
                nonogramLogic.getNonogramSolutionBoard(),
                nonogramLogic.getNonogramSolutionBoardWithMarks(),
                nonogramLogic.getActionsToDoList(),
                nonogramLogic.getNonogramState(),
                nonogramLogic.getLogs()
        );

        this.columnsSequencesRanges = nonogramLogic.getColumnsSequencesRanges();
        this.columnsSequencesIdsNotToInclude = nonogramLogic.getColumnsSequencesIdsNotToInclude();
        this.columnsFieldsNotToInclude = nonogramLogic.getColumnsFieldsNotToInclude();

        this.nonogramSolutionBoardWithMarks = nonogramLogic.getNonogramSolutionBoardWithMarks();
        this.nonogramSolutionBoard = nonogramLogic.getNonogramSolutionBoard();

        this.actionsToDoList = nonogramLogic.getActionsToDoList();
    }

    public void setColumnSequencesRanges(int columnIdx, List<List<Integer>> ranges) {
        this.getColumnsSequencesRanges().set(columnIdx, ranges);
    }

    public void setColumnSequencesLengths(int columnIdx, List<Integer> lengths) {
        this.getNonogramRules().getColumnSequencesLengths().set(columnIdx, lengths);
    }

    @Override
    public void correctColumnSequencesRanges(int columnIdx) {
        List<List<Integer>> beforeRangesSnapshot = deepCopy(getColumnsSequencesRanges().get(columnIdx));

        correctSequencesRangesInColumnFromTop(columnIdx);
        correctSequencesRangesInColumnFromBottom(columnIdx);

        List<List<Integer>> afterRangesSnapshot = getColumnsSequencesRanges().get(columnIdx);

        if (!rangesListEqual(beforeRangesSnapshot, afterRangesSnapshot)) {
            tmpLog = SequenceRangeCorrectionLogHelper.generateLog(
                    columnIdx,
                    beforeRangesSnapshot,
                    afterRangesSnapshot,
                    getNonogramRules().getColumnSequencesLengths().get(columnIdx),
                    getColumnsFieldsNotToInclude().get(columnIdx),
                    getColumnsSequencesIdsNotToInclude().get(columnIdx),
                    false
            );
            addLog();
            addColumnToAffectedActionsByIdentifiers(columnIdx, NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES);
        }
    }

    private void correctSequencesRangesInColumnFromTop(int columnIdx) {
        List<Integer> colSeqLengths = getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> colSeqRanges = getColumnsSequencesRanges().get(columnIdx);
        List<Integer> colFieldsNotToInclude = getColumnsFieldsNotToInclude().get(columnIdx);
        List<Integer> colSeqIdsNotToInclude = getColumnsSequencesIdsNotToInclude().get(columnIdx);

        for (int seqIdx = 0; seqIdx < colSeqRanges.size() - 1; seqIdx++) {
            int nextSeqIdx = seqIdx + 1;
            if (colSeqIdsNotToInclude.contains(nextSeqIdx)) continue;

            List<Integer> updatedNextRange = colSeqIdsNotToInclude.contains(seqIdx)
                    ? SequenceRangeCorrectionHelper.calculateUpdatedNextSequenceRangeAfterExcludedSequence(colSeqRanges, colFieldsNotToInclude, seqIdx, nextSeqIdx)
                    : SequenceRangeCorrectionHelper.calculateUpdatedNextSequenceRangeAfterIncludedSequence(colSeqRanges, colSeqLengths, seqIdx, nextSeqIdx);

            tryToCorrectColumnRangeFromTop(columnIdx, colSeqLengths, colSeqRanges.get(nextSeqIdx), updatedNextRange, nextSeqIdx);
        }
    }

    private void tryToCorrectColumnRangeFromTop(int colIdx,
                                                List<Integer> colSeqLengths,
                                                List<Integer> oldNextRange,
                                                List<Integer> updatedNextRange,
                                                int nextSeqIdx) {
        if (!oldNextRange.get(0).equals(updatedNextRange.get(0))) {
            updateColumnSequenceRange(colIdx, nextSeqIdx, updatedNextRange);
            markColumnAsChanged(colIdx);

            if (rangeLength(updatedNextRange) == colSeqLengths.get(nextSeqIdx)
                    && isRowRangeColoured(colIdx, updatedNextRange)) {
                excludeSequenceInColumn(colIdx, nextSeqIdx);
            }
        }
    }

    public void correctSequencesRangesInColumnFromBottom(int columnIdx) {
        List<Integer> colSeqLengths = getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> colSeqRanges = getColumnsSequencesRanges().get(columnIdx);
        List<Integer> colFieldsNotToInclude = getColumnsFieldsNotToInclude().get(columnIdx);
        List<Integer> colSeqIdsNotToInclude = getColumnsSequencesIdsNotToInclude().get(columnIdx);

        for (int seqIdx = colSeqRanges.size() - 1; seqIdx > 0; seqIdx--) {
            int prevSeqIdx = seqIdx - 1;
            if (colSeqIdsNotToInclude.contains(prevSeqIdx)) continue;

            List<Integer> updatedPrevRange = colSeqIdsNotToInclude.contains(seqIdx)
                    ? SequenceRangeCorrectionHelper.calculateUpdatedPreviousSequenceRangeAfterExcludedSequence(colSeqRanges, colFieldsNotToInclude, seqIdx, prevSeqIdx)
                    : SequenceRangeCorrectionHelper.calculateUpdatedPreviousSequenceRangeAfterIncludedSequence(colSeqRanges, colSeqLengths, seqIdx, prevSeqIdx);

            tryToCorrectColumnRangeFromBottom(columnIdx, colSeqLengths, colSeqRanges.get(prevSeqIdx), updatedPrevRange, prevSeqIdx);
        }
    }

    private void tryToCorrectColumnRangeFromBottom(int columnIdx,
                                                   List<Integer> colSeqLengths,
                                                   List<Integer> oldPrevRange,
                                                   List<Integer> updatedPrevRange,
                                                   int prevSeqIdx) {
        if (!oldPrevRange.get(1).equals(updatedPrevRange.get(1))) {
            updateColumnSequenceRange(columnIdx, prevSeqIdx, updatedPrevRange);
            markColumnAsChanged(columnIdx);

            if (rangeLength(updatedPrevRange) == colSeqLengths.get(prevSeqIdx)
                    && isRowRangeColoured(columnIdx, updatedPrevRange)) {
                excludeSequenceInColumn(columnIdx, prevSeqIdx);
            }
        }
    }

    private void markColumnAsChanged(int columnIdx) {
        this.addColumnToAffectedActionsByIdentifiers(columnIdx, NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES);
        this.nonogramState.increaseMadeSteps();
    }

    @Override
    public void correctColumnSequencesRangesWhenMetColouredField (int columnIdx) {
        correctColumnSequencesRangesWhenMetColouredFieldFromTop(columnIdx);
        correctColumnSequencesRangesWhenMetColouredFieldFromBottom(columnIdx);
    }

    public void correctColumnSequencesRangesWhenMetColouredFieldFromTop(int columnIdx) {
        List<List<Integer>> colSeqRanges = getColumnsSequencesRanges().get(columnIdx);
        List<Integer> colSeqLengths = getNonogramRules().getColumnSequencesLengths().get(columnIdx);

        boolean changed = false;
        int seqId = 0;
        int seqLength = colSeqLengths.get(seqId);

        for (int rowIdx = 0; rowIdx < getNonogramRules().getHeight(); rowIdx++) {
            Field field = new Field(rowIdx, columnIdx);
            if (isFieldColoured(nonogramSolutionBoard, field)) {
                List<Integer> oldRange = colSeqRanges.get(seqId);
                List<Integer> updatedRange = RangeCorrectionHelper.updatedSequenceRangeWhenMetColouredField(
                        oldRange.get(0), oldRange.get(1), rowIdx, seqLength, true);

                if (!updatedRange.equals(oldRange)) {
                    this.tmpLog = SequenceCorrectionWhenMetColouredFieldsLogHelper.generateLog(
                            columnIdx,
                            seqId,
                            colSeqRanges,
                            updatedRange,
                            this.getNonogramBoardColumn(columnIdx),
                            colSeqLengths,
                            false,
                            "fromTop"
                    );
                    addLog();
                    updateColumnSequenceRange(columnIdx, seqId, updatedRange);

                    if (rangeLength(updatedRange) == seqLength && isRowRangeColoured(columnIdx, updatedRange)) {
                        excludeSequenceInColumn(columnIdx, seqId);
                    }
                    changed = true;
                }

                rowIdx += seqLength;
                seqId++;
                if (seqId >= colSeqLengths.size()) break;
                seqLength = colSeqLengths.get(seqId);
            }
        }

        if (changed) {
            nonogramState.increaseMadeSteps();
            addColumnToAffectedActionsByIdentifiers(columnIdx, NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS);
        }
    }

    public void correctColumnSequencesRangesWhenMetColouredFieldFromBottom(int columnIdx) {
        List<List<Integer>> colSeqRanges = getColumnsSequencesRanges().get(columnIdx);
        List<Integer> colSeqLengths = getNonogramRules().getColumnSequencesLengths().get(columnIdx);

        boolean changed = false;
        int seqId = colSeqLengths.size() - 1;
        int seqLength = colSeqLengths.get(seqId);

        for (int rowIdx = getNonogramRules().getHeight() - 1; rowIdx >= 0; rowIdx--) {
            Field field = new Field(rowIdx, columnIdx);
            if (isFieldColoured(nonogramSolutionBoard, field)) {
                List<Integer> oldRange = colSeqRanges.get(seqId);
                List<Integer> updatedRange = RangeCorrectionHelper.updatedSequenceRangeWhenMetColouredField(
                        oldRange.get(0), oldRange.get(1), rowIdx, seqLength, false);

                if (!updatedRange.equals(oldRange)) {
                    this.tmpLog = SequenceCorrectionWhenMetColouredFieldsLogHelper.generateLog(
                            columnIdx,
                            seqId,
                            colSeqRanges,
                            updatedRange,
                            this.getNonogramBoardColumn(columnIdx),
                            colSeqLengths,
                            false,
                            "fromBottom"
                    );
                    addLog();
                    updateColumnSequenceRange(columnIdx, seqId, updatedRange);

                    if (rangeLength(updatedRange) == seqLength && isRowRangeColoured(columnIdx, updatedRange)) {
                        excludeSequenceInColumn(columnIdx, seqId);
                    }
                    changed = true;
                }

                rowIdx -= seqLength;
                seqId--;
                if (seqId < 0) break;
                seqLength = colSeqLengths.get(seqId);
            }
        }

        if (changed) {
            nonogramState.increaseMadeSteps();
            addColumnToAffectedActionsByIdentifiers(columnIdx, NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS);
        }
    }

    @Override
    public void correctColumnSequencesRangesIfXOnWay(int columnIdx, boolean changeLogicDetails) {
        boolean madeAnyCorrection = false;

        List<Integer> lengths = getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> ranges = getColumnsSequencesRanges().get(columnIdx);
        List<Integer> excluded = getColumnsSequencesIdsNotToInclude().get(columnIdx);

        List<List<Integer>> initialRangesSnapshot = ranges.stream()
                .map(range -> List.of(range.get(0), range.get(1)))
                .collect(Collectors.toList());

        for (int seqIdx = 0; seqIdx < ranges.size(); seqIdx++) {
            if (excluded.contains(seqIdx)) continue;

            List<Integer> currentRange = ranges.get(seqIdx);
            int length = lengths.get(seqIdx);

            List<Integer> newRange = SequenceRangeCorrectionWhenMetXHelper.calculateCorrectedRangeWithoutX(
                    currentRange, length, columnIdx, true, nonogramSolutionBoard
            );

            if (!rangesEqual(currentRange, newRange)) {
                madeAnyCorrection = true;
                changeColumnSequenceRange(columnIdx, seqIdx, newRange);

                if (changeLogicDetails && shouldExcludeSequence(newRange, length, columnIdx)) {
                    excludeSequenceInColumn(columnIdx, seqIdx);
                }
            }
        }

        if (madeAnyCorrection && changeLogicDetails) {
            List<List<Integer>> updatedRangesSnapshot = getColumnsSequencesRanges().get(columnIdx);

            tmpLog = SequenceRangeCorrectionWhenMetXLogHelper.generateLog(
                    columnIdx,
                    initialRangesSnapshot,
                    updatedRangesSnapshot,
                    lengths,
                    excluded,
                    false  // isRow
            );
            addLog();

            nonogramState.increaseMadeSteps();
            addColumnToAffectedActionsByIdentifiers(columnIdx, NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY);
        }
    }

    private boolean shouldExcludeSequence(List<Integer> newRange, int length, int columnIdx) {
        return rangeLength(newRange) == length && isRowRangeColoured(columnIdx, newRange);
    }

    @Override
    public void correctColumnSequencesRangesWhenMatchingFieldsToSequences(int columnIdx) {
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequencesLengths = this.getNonogramRules().getColumnSequencesLengths().get(columnIdx);

        Field fieldToCheckIfIsColoured;
        List<List<Integer>> colouredSequencesPartsRanges = new ArrayList<>();
        List<Integer> colouredSequencePartRange;

        // extract coloured sequences ranges from board
        for (int rowIdx = 0; rowIdx < this.getNonogramRules().getHeight(); rowIdx++) {
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

        List<List<Integer>> reducedMatches = reduceColouredSequenceMatches(
                columnSequencesLengths,
                colouredSequencesPartsRanges,
                colouredSequencesPartsMatches
        );


        for (int i = 0; i < reducedMatches.size(); i++) {
            List<Integer> matchedSeqs = reducedMatches.get(i);
            int partStart = colouredSequencesPartsRanges.get(i).get(0);
            int partEnd = colouredSequencesPartsRanges.get(i).get(1);

            if (matchedSeqs.size() == 1) {
                int seqId = matchedSeqs.get(0);
                int length = columnSequencesLengths.get(seqId);
                int newStart = partEnd - length + 1;
                int newEnd = partStart + length - 1;

                List<Integer> oldRange = columnSequencesRanges.get(seqId);
                List<Integer> newRange = Arrays.asList(
                        Math.max(newStart, oldRange.get(0)),
                        Math.min(newEnd, oldRange.get(1))
                );

                if (!rangesEqual(oldRange, newRange)) {
                    this.updateColumnSequenceRange(columnIdx, seqId, newRange);
                    this.addColumnToAffectedActionsByIdentifiers(columnIdx, NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES);
                    this.nonogramState.increaseMadeSteps();
                    this.tmpLog = generateCorrectingColumnSequenceRangeStepDescription(columnIdx, seqId, oldRange, newRange, "correcting sequence when matching fields to only possible coloured sequences");
                    addLog();

                    if (rangeLength(newRange) == columnSequencesLengths.get(seqId) && isRowRangeColoured(columnIdx, newRange)) {
                        this.excludeSequenceInColumn(columnIdx, seqId);
                    }
                }
            } else {
                if (matchedSeqs.isEmpty()) continue;

                // We focus only on the first sequence in the match
                int primarySeqId = matchedSeqs.get(0);

                // Skip updating if this sequence will appear as the first one later
                if (sequenceAssignmentAppearsAsFirstLater(reducedMatches, i, primarySeqId)) {
                    continue; // Skip processing this sequence because it will appear as first later
                }

                // Get the range of the coloured sequence part for this match
                int length = columnSequencesLengths.get(primarySeqId);

                // Get the old range for the sequence
                List<Integer> oldRange = columnSequencesRanges.get(primarySeqId);

                // Calculate the new end of the range based on the starting position and the length of the sequence
                int newEnd = partStart + length - 1;

                // Create the new range by limiting the upper bound
                List<Integer> newRange = Arrays.asList(oldRange.get(0), Math.min(newEnd, oldRange.get(1)));

                // Check if the range has actually changed
                if (!rangesEqual(oldRange, newRange)) {
                    // Update the column sequence range with the new limited range
                    this.updateColumnSequenceRange(columnIdx, primarySeqId, newRange);

                    // Mark the column as affected and log the action
                    this.addColumnToAffectedActionsByIdentifiers(columnIdx, NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES);
                    this.nonogramState.increaseMadeSteps();

                    // Generate a log description for the change
                    this.tmpLog = generateCorrectingColumnSequenceRangeStepDescription(
                            columnIdx, primarySeqId, oldRange, newRange,
                            "correcting sequence when matching fields to only possible coloured sequences"
                    );
                    addLog();

                    // If the new range matches the length of the sequence and is coloured correctly, exclude the sequence
                    if (rangeLength(newRange) == columnSequencesLengths.get(primarySeqId) && isRowRangeColoured(columnIdx, newRange)) {
                        this.excludeSequenceInColumn(columnIdx, primarySeqId);
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

        while (rowToBottom < this.getNonogramRules().getHeight() && isFieldColoured(this.getNonogramSolutionBoard(), fieldBottom)) {
            fieldBottom = new Field(++rowToBottom, columnIdx);
        }
        fieldBottom = new Field(--rowToBottom, columnIdx);

        colouredSequenceRangeNearField.add(fieldBottom.getRowIdx());

        return colouredSequenceRangeNearField;
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

    @Override
    public void correctColumnSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(int columnIdx) {
        boolean anyUpdated = false;

        List<List<Integer>> ranges = getColumnsSequencesRanges().get(columnIdx);

        List<List<Integer>> before = deepCopy(ranges);
        for (int seqIdx = 0; seqIdx < ranges.size(); seqIdx++) {
            List<Integer> currentRange = ranges.get(seqIdx);
            List<Integer> updatedRange = RangeCorrectionHelper.adjustRangeIfColouredAtEdges(
                    currentRange, columnIdx, true, nonogramSolutionBoard, getNonogramRules().getHeight()
            );

            if (!updatedRange.equals(currentRange)) {
                anyUpdated = true;
                updateColumnSequenceRange(columnIdx, seqIdx, updatedRange);
                nonogramState.increaseMadeSteps();
            }
        }

        if (anyUpdated) {
            List<List<Integer>> after = getColumnsSequencesRanges().get(columnIdx);
            List<Integer> lengths = getNonogramRules().getColumnSequencesLengths().get(columnIdx);
            List<String> line = getNonogramBoardColumn(columnIdx);

            tmpLog = SequenceRangeCorrectionFromColouredEdgesLogHelper.generateLog(
                    columnIdx,
                    before,
                    after,
                    lengths,
                    line,
                    false
            );
            addLog();

            addColumnToAffectedActionsByIdentifiers(columnIdx, NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE);
        }
    }

    @Override
    public void colourOverlappingFieldsInColumn(int columnIdx) {
        List<String> columnBefore = getColumnCopy(columnIdx);
        boolean anyFieldColoured = false;

        List<Integer> sequenceLengths = getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> sequenceRanges = getColumnsSequencesRanges().get(columnIdx);

        for (int sequenceIdx = 0; sequenceIdx < sequenceLengths.size(); sequenceIdx++) {
            int sequenceLength = sequenceLengths.get(sequenceIdx);
            List<Integer> range = sequenceRanges.get(sequenceIdx);

            List<Integer> overlapRange = ColouringHelper.calculateOverlappingRange(range, sequenceLength);
            boolean coloured = colourAllEmptyFieldsInRangeForColumn(columnIdx, overlapRange, sequenceIdx);
            anyFieldColoured |= coloured;
        }

        if (anyFieldColoured) {
            List<String> columnAfter = getColumnCopy(columnIdx);
            tmpLog = generateOverlappingSequenceLog(
                    columnIdx,
                    false,
                    columnBefore,
                    getColumnsSequencesRanges().get(columnIdx),
                    getNonogramRules().getColumnSequencesLengths().get(columnIdx),
                    columnAfter
            );
            addLog();
        }
    }

    private boolean colourAllEmptyFieldsInRangeForColumn(int columnIdx, List<Integer> rows, int sequenceIdx) {
        if (rows.isEmpty()) return false;

        int sequenceLength = getNonogramRules().getColumnSequencesLengths().get(columnIdx).get(sequenceIdx);
        boolean anyFieldColoured = false;

        for (int rowIdx : rows) {
            Field field = new Field(rowIdx, columnIdx);

            if (isFieldEmpty(nonogramSolutionBoard, field)) {
                colourFieldAtGivenPosition(field, "--C-");
                anyFieldColoured = true;
                addRowAndColumnToAffectedByIdentifiers(field, NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN);
                nonogramState.increaseMadeSteps();
            } else if (SHOW_REPETITIONS) {
                logger.warn("Column field was coloured earlier (overlap).");
            }
        }

        if (rows.size() == sequenceLength) {
            excludeSequenceInColumn(columnIdx, sequenceIdx);
        }

        return anyFieldColoured;
    }

    @Override
    public void colourFieldsInColumnIfXWouldForceTooLongColouredFieldsSequence(int columnIdx) {
        List<String> columnBefore = getColumnCopy(columnIdx);
        boolean anyFieldColoured = false;

        List<Integer> sequencesLengths = this.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> originalRanges = cloneAndMakeImmutable2DList(getColumnsSequencesRanges().get(columnIdx));
        List<List<Integer>> colouredSequences = collectColouredSequencesRanges(getNonogramSolutionBoard(),
                columnIdx,
                false);

        anyFieldColoured |= handleTopMergeScenarios(columnIdx, sequencesLengths, originalRanges, colouredSequences);
        anyFieldColoured |= handleBottomMergeScenarios(columnIdx, sequencesLengths, originalRanges, colouredSequences);

        if (anyFieldColoured) {
            List<String> columnAfter = getColumnCopy(columnIdx);
            tmpLog = TooLongMergeLogHelper.generateTooLongMergeSequenceLog(
                    columnIdx,
                    false,
                    columnBefore,
                    getColumnsSequencesRanges().get(columnIdx),
                    getNonogramRules().getColumnSequencesLengths().get(columnIdx),
                    columnAfter
            );
            addLog();
        }
    }

    private boolean handleTopMergeScenarios(
            int columnIdx,
            List<Integer> seqLens,
            List<List<Integer>> originalRanges,
            List<List<Integer>> colouredSeqs
    ) {
        boolean anyFieldColoured = false;

        for (int i = 0; i < colouredSeqs.size() - 1; i++) {
            List<Integer> first = colouredSeqs.get(i);
            List<Integer> second = colouredSeqs.get(i + 1);

            int mergeStart = first.get(0);
            int mergePoint = second.get(0) - 1;
            int mergeEnd = second.get(1);

            Field tempX = new Field(mergeStart - 1, columnIdx);
            if (isFieldEmpty(nonogramSolutionBoard, tempX)) {
                placeXAtGivenField(tempX, false);
                correctColumnSequencesRangesIfXOnWay(columnIdx, false);
                clearField(tempX);
            }

            boolean shouldSkip = mergedSequenceViolatesConstraints(columnIdx, first, List.of(mergeStart, mergeEnd), mergePoint, seqLens);

            if (!shouldSkip) {
                Field toColour = new Field(mergeStart - 1, columnIdx);
                if (isFieldEmpty(nonogramSolutionBoard, toColour)) {
                    colourFieldAtGivenPosition(toColour, "--C-");
                    addRowAndColumnToAffectedByIdentifiers(toColour, NonogramSolveAction.COLOUR_FIELDS_IN_COLUMN_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE);
                    nonogramState.increaseMadeSteps();
                    anyFieldColoured = true;
                }
            }

            setColumnSequencesRanges(columnIdx, mutableClone2DList(originalRanges));
        }

        return anyFieldColoured;
    }

    private boolean handleBottomMergeScenarios(
            int columnIdx,
            List<Integer> seqLens,
            List<List<Integer>> originalRanges,
            List<List<Integer>> colouredSeqs
    ) {
        boolean anyFieldColoured = false;

        for (int i = colouredSeqs.size() - 1; i > 0; i--) {
            List<Integer> second = colouredSeqs.get(i);
            List<Integer> first = colouredSeqs.get(i - 1);

            int mergeStart = first.get(0);
            int mergePoint = first.get(1) + 1;
            int mergeEnd = second.get(1);

            Field tempX = new Field(mergeEnd + 1, columnIdx);
            if (isFieldEmpty(nonogramSolutionBoard, tempX)) {
                placeXAtGivenField(tempX, false);
                correctColumnSequencesRangesIfXOnWay(columnIdx, false);
                clearField(tempX);
            }

            boolean shouldSkip = mergedSequenceViolatesConstraints(columnIdx, second, List.of(mergeStart, mergeEnd), mergePoint, seqLens);

            if (!shouldSkip) {
                Field toColour = new Field(mergeEnd + 1, columnIdx);
                if (isFieldEmpty(nonogramSolutionBoard, toColour)) {
                    colourFieldAtGivenPosition(toColour, "--C-");
                    addRowAndColumnToAffectedByIdentifiers(toColour, NonogramSolveAction.COLOUR_FIELDS_IN_COLUMN_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE);
                    nonogramState.increaseMadeSteps();
                    anyFieldColoured = true;
                }
            }

            setColumnSequencesRanges(columnIdx, mutableClone2DList(originalRanges));
        }

        return anyFieldColoured;
    }

    private boolean mergedSequenceViolatesConstraints(int columnIdx, List<Integer> colouredPart,
                                                      List<Integer> mergedRange, int mergePoint,
                                                      List<Integer> seqLens) {
        Map<List<Integer>, List<Integer>> mapping = TooLongMergeFieldHelper.matchColouredSequencesToPossibleSeqIDs(
                collectColouredSequencesRanges(nonogramSolutionBoard, columnIdx, false),
                getColumnsSequencesRanges().get(columnIdx)
        );

        List<Integer> possibleSeqIds = mapping.get(colouredPart);
        if (possibleSeqIds == null) return true;

        for (int seqId : possibleSeqIds) {
            int length = seqLens.get(seqId);

            if ((mergedRange.get(0) + length - 1 < mergePoint) ||
                    (mergedRange.get(1) - length + 1 > mergePoint)) {
                return true;
            }

            List<Integer> range = getColumnsSequencesRanges().get(columnIdx).get(seqId);
            if (rangeInsideAnotherRange(mergedRange, range) && rangeLength(mergedRange) <= length) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void extendColouredFieldsNearXToMaximumPossibleLengthInColumn(int columnIdx) {
        extendColouredFieldsToTopNearXToMaximumPossibleLengthInColumn(columnIdx);
        extendColouredFieldsToBottomNearXToMaximumPossibleLengthInColumn(columnIdx);
    }

    public void extendColouredFieldsToTopNearXToMaximumPossibleLengthInColumn(int columnIdx) {
        List<String> columnBefore = getColumnCopy(columnIdx);
        boolean anyGlobalFieldColoured = false;

        for (int rowIdx = this.getNonogramRules().getHeight() - 1; rowIdx >= 0; rowIdx--) {
            Field currentField = new Field(rowIdx, columnIdx);

            if (isFieldColoured(this.nonogramSolutionBoard, currentField)) {
                List<Integer> colouredRange = findColouredSequenceRangeTop(columnIdx, rowIdx);
                List<Integer> possibleSequenceLenghts = findPossibleSequenceLengths(
                        this.getColumnsSequencesRanges().get(columnIdx),
                        colouredRange,
                        this.getNonogramRules().getColumnSequencesLengths().get(columnIdx)
                );

                if (possibleSequenceLenghts.isEmpty()) {
                    this.nonogramState.invalidateSolution();
                    break;
                }

                int minSequenceLength = Collections.min(possibleSequenceLenghts);
                int distanceFromX = findDistanceFromBottomX(columnIdx, colouredRange, minSequenceLength);

                if (distanceFromX > 0) {
                    int minExtensionIdx = colouredRange.get(0) + distanceFromX - minSequenceLength;
                    boolean extended = extendToTop(columnIdx, colouredRange.get(0) - 1, minExtensionIdx);

                    if (extended) {
                        anyGlobalFieldColoured = true;
                    }
                }

                rowIdx = colouredRange.get(0) - 1;
            }
        }

        if (anyGlobalFieldColoured) {
            List<String> columnAfter = getColumnCopy(columnIdx);
            tmpLog = ExtendLogHelper.generateExtendSequenceLog(
                    columnIdx,
                    "toTop",
                    columnBefore,
                    this.getColumnsSequencesRanges().get(columnIdx),
                    this.getNonogramRules().getColumnSequencesLengths().get(columnIdx),
                    columnAfter,
                    false
            );
            addLog();
        }
    }

    private List<Integer> findColouredSequenceRangeTop(int columnIdx, int startRowIdx) {
        int start = startRowIdx;
        while (start - 1 >= 0 && isFieldColoured(this.nonogramSolutionBoard, new Field(start - 1, columnIdx))) {
            start--;
        }
        return List.of(start, startRowIdx);
    }

    private int findDistanceFromBottomX(int columnIdx, List<Integer> colouredRange, int maxDist) {
        for (int offset = rangeLength(colouredRange); offset < maxDist; offset++) {
            int idxToCheck = colouredRange.get(0) + offset;
            if (idxToCheck >= this.getNonogramRules().getHeight()) break;

            Field fieldToCheck = new Field(idxToCheck, columnIdx);
            if (isFieldWithX(this.nonogramSolutionBoard, fieldToCheck)) {
                return offset;
            }
        }
        return 0;
    }

    private boolean extendToTop(int columnIdx, int fromInclusive, int toInclusive) {
        boolean anyFieldColoured = false;

        for (int row = fromInclusive; row >= toInclusive && row >= 0; row--) {
            Field field = new Field(row, columnIdx);
            try {
                if (isFieldEmpty(this.nonogramSolutionBoard, field)) {
                    this.colourFieldAtGivenPosition(field, "--C-");
                    this.addRowAndColumnToAffectedByIdentifiers(field, NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN);
                    this.nonogramState.increaseMadeSteps();
                    anyFieldColoured = true;
                } else if (SHOW_REPETITIONS) {
                    System.out.println("Column field already coloured.");
                }
            } catch (IndexOutOfBoundsException e) {
                this.nonogramState.invalidateSolution();
            }
        }

        return anyFieldColoured;
    }

    public void extendColouredFieldsToBottomNearXToMaximumPossibleLengthInColumn(int columnIdx) {
        List<String> columnBefore = getColumnCopy(columnIdx);
        boolean anyGlobalFieldColoured = false;

        for (int rowIdx = 0; rowIdx < this.getNonogramRules().getHeight(); rowIdx++) {
            Field currentField = new Field(rowIdx, columnIdx);

            if (isFieldColoured(this.nonogramSolutionBoard, currentField)) {
                List<Integer> colouredRange = findColouredSequenceRangeBottom(columnIdx, rowIdx);
                List<Integer> possibleSequenceLengths = findPossibleSequenceLengths(
                        this.getColumnsSequencesRanges().get(columnIdx),
                        colouredRange,
                        this.getNonogramRules().getColumnSequencesLengths().get(columnIdx)
                );

                if (possibleSequenceLengths.isEmpty()) {
                    this.nonogramState.invalidateSolution();
                    break;
                }

                int minSequenceLength = Collections.min(possibleSequenceLengths);
                int distanceFromX = findDistanceFromTopX(columnIdx, colouredRange, minSequenceLength);

                if (distanceFromX > 0) {
                    int maxExtensionIdx = colouredRange.get(1) - distanceFromX + minSequenceLength;
                    boolean extended = extendToBottom(columnIdx, colouredRange.get(1) + 1, maxExtensionIdx);
                    if (extended) {
                        anyGlobalFieldColoured = true;
                    }
                }

                rowIdx = colouredRange.get(1) + 1;
            }
        }

        if (anyGlobalFieldColoured) {
            List<String> columnAfter = getColumnCopy(columnIdx);
            tmpLog = ExtendLogHelper.generateExtendSequenceLog(
                    columnIdx,
                    "toBottom",
                    columnBefore,
                    this.getColumnsSequencesRanges().get(columnIdx),
                    this.getNonogramRules().getColumnSequencesLengths().get(columnIdx),
                    columnAfter,
                    false
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

    private List<Integer> findColouredSequenceRangeBottom(int columnIdx, int startRowIdx) {
        int endRowIdx = startRowIdx;
        while (endRowIdx + 1 < this.getNonogramRules().getHeight()
                && isFieldColoured(this.nonogramSolutionBoard, new Field(endRowIdx + 1, columnIdx))) {
            endRowIdx++;
        }
        return List.of(startRowIdx, endRowIdx);
    }

    private int findDistanceFromTopX(int columnIdx, List<Integer> colouredRange, int maxDist) {
        for (int offset = rangeLength(colouredRange); offset < maxDist; offset++) {
            int idxToCheck = colouredRange.get(1) - offset;
            if (idxToCheck < 0) break;

            Field fieldToCheck = new Field(idxToCheck, columnIdx);
            if (isFieldWithX(this.nonogramSolutionBoard, fieldToCheck)) {
                return offset;
            }
        }
        return 0;
    }

    private boolean extendToBottom(int columnIdx, int fromInclusive, int toInclusive) {
        boolean anyFieldColoured = false;

        for (int row = fromInclusive; row <= toInclusive && row < this.getNonogramRules().getHeight(); row++) {
            Field field = new Field(row, columnIdx);
            try {
                if (isFieldEmpty(this.nonogramSolutionBoard, field)) {
                    this.colourFieldAtGivenPosition(field, "--C-");
                    this.addRowAndColumnToAffectedByIdentifiers(field, NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN);
                    this.nonogramState.increaseMadeSteps();
                    anyFieldColoured = true;
                } else if (SHOW_REPETITIONS) {
                    System.out.println("Column field already coloured.");
                }
            } catch (IndexOutOfBoundsException e) {
                this.nonogramState.invalidateSolution();
            }
        }

        return anyFieldColoured;
    }

    @Override
    public void colourFieldsInColumnIfXCausesAssignmentConflict(int columnIdx) {

    }

    @Override
    public void placeXsColumnAtUnreachableFields(int columnIdx) {
        List<String> initialState = getColumnCopy(columnIdx);
        List<List<Integer>> initialRanges = cloneAndMakeImmutable2DList(getColumnsSequencesRanges().get(columnIdx));

        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        boolean existRangeIncludingRow;
        List<Integer> fieldAsRange;
        Field fieldToExclude;

        for (int rowIdx = 0; rowIdx < this.getNonogramRules().getHeight(); rowIdx++) {
            fieldAsRange = List.of(rowIdx, rowIdx);
            existRangeIncludingRow = rangesListIncludingAnotherRange(columnSequencesRanges, fieldAsRange);

            if (!existRangeIncludingRow) {
                fieldToExclude = new Field(rowIdx, columnIdx);
                if (isFieldEmpty(this.nonogramSolutionBoard, fieldToExclude)) {
                    this.placeXAtGivenField(fieldToExclude, true);
                    this.addRowAndColumnToAffectedByIdentifiers(fieldToExclude, NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS);
                    this.nonogramState.increaseMadeSteps();
                } else if (this.SHOW_REPETITIONS) {
                    System.out.println("X at unreachable field in column placed earlier!");
                }
            }
        }

        List<String> finalState = getColumnCopy(columnIdx);
        if (!initialState.equals(finalState)) {
            tmpLog = PlaceXsAtUnreachableFieldsLogHelper.generateLog(
                    columnIdx,
                    initialState,
                    finalState,
                    initialRanges,
                    false
            );
            addLog();
        }
    }

    @Override
    public void placeXsAroundLongestSequencesInColumn(int columnIdx) {
        int height = this.getNonogramRules().getHeight();

        for (int rowIdx = 0; rowIdx < height; rowIdx++) {
            Field field = new Field(rowIdx, columnIdx);
            if (isFieldColoured(this.nonogramSolutionBoard, field)) {
                List<Integer> colouredRange = findColouredSequenceRangeInColumn(rowIdx, columnIdx);
                rowIdx = colouredRange.get(1);

                processColouredSequenceRange(rowIdx, colouredRange);
            }
        }
    }

    private List<Integer> findColouredSequenceRangeInColumn(int startRowIdx, int columnIdx) {
        int rowIdx = startRowIdx;

        List<Integer> colouredSequenceRange = new ArrayList<>();
        colouredSequenceRange.add(rowIdx);
        while (rowIdx < this.getNonogramRules().getHeight() && isFieldColoured(this.nonogramSolutionBoard, new Field(rowIdx, columnIdx))) {
            rowIdx++;
        }
        colouredSequenceRange.add(rowIdx - 1);

        return colouredSequenceRange;
    }

    private void processColouredSequenceRange(int columnIdx, List<Integer> colouredRange) {
        List<List<Integer>> columnRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnLengths = this.getNonogramRules().getColumnSequencesLengths().get(columnIdx);

        int lengthOnBoard = rangeLength(colouredRange);

        List<Integer> matchingIndices = new ArrayList<>();
        List<Integer> matchingLengths = new ArrayList<>();

        for (int i = 0; i < columnRanges.size(); i++) {
            if (rangeInsideAnotherRange(colouredRange, columnRanges.get(i))) {
                matchingIndices.add(i);
                matchingLengths.add(columnLengths.get(i));
            }
        }

        List<Integer> edgeXs = List.of(colouredRange.get(0) - 1, colouredRange.get(1) + 1);

        if (matchingIndices.size() == 1 && lengthOnBoard == matchingLengths.get(0)) {
            placeXsAndUpdateSingleSequence(columnIdx, edgeXs, matchingIndices.get(0), colouredRange);
        } else if (matchingLengths.size() > 1 && lengthOnBoard == Collections.max(matchingLengths)) {
            placeXsAroundLongestSequence(columnIdx, edgeXs, false);
        }
    }

    private void placeXsAndUpdateSingleSequence(int columnIdx, List<Integer> xEdges, int seqIdx, List<Integer> colouredRange) {
        placeXsAroundLongestSequence(columnIdx, xEdges, true);

        List<Integer> updatedRange = List.of(xEdges.get(0) + 1, xEdges.get(1) - 1);
        excludeColouredFieldsBetweenXs(columnIdx, updatedRange);

        updateLogicAfterXsPlacement(columnIdx, seqIdx, colouredRange, updatedRange);
    }

    private void placeXsAroundLongestSequence(int columnIdx, List<Integer> xEdges, boolean onlyMatching) {
        List<String> columnBefore = getColumnCopy(columnIdx);
        String logTag = onlyMatching ? "[only possible]" : "[sequence index not specified]";
        boolean anyXPlaced = false;

        for (int rowIdx : xEdges) {
            if (!isColumnIndexValid(rowIdx)) continue;

            Field edgeField = new Field(rowIdx, columnIdx);
            if (isFieldEmpty(this.nonogramSolutionBoard, edgeField)) {
                this.placeXAtGivenField(edgeField, true);
                this.addRowAndColumnToAffectedByIdentifiers(edgeField, NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES);
                this.nonogramState.increaseMadeSteps();
                anyXPlaced = true;
            } else if (this.SHOW_REPETITIONS) {
                log.warn("X around longest sequence already placed at {} {}", edgeField, logTag);
            }
        }

        List<String> columnAfter = getColumnCopy(columnIdx);

        if (anyXPlaced) {
            tmpLog = PlaceXsAroundLongestSequenceLogHelper.generateLog(columnIdx,
                    xEdges,
                    columnBefore,
                    columnAfter,
                    onlyMatching,
                    false);
            addLog();
        }
    }

    private void excludeColouredFieldsBetweenXs(int columnIdx, List<Integer> range) {
        for (int rowIdx = range.get(0); rowIdx <= range.get(1); rowIdx++) {
            this.excludeFieldInColumn(new Field(rowIdx, columnIdx));
            this.nonogramState.increaseMadeSteps();
        }
    }

    private void updateLogicAfterXsPlacement(int columnIdx, int seqIdx, List<Integer> oldRange, List<Integer> newRange) {
        updateColumnSequenceAndExclude(columnIdx, seqIdx, oldRange, newRange, true);

        Field topEdge = new Field(columnIdx, newRange.get(0) - 1);
        Field bottomEdge = new Field(columnIdx, newRange.get(1) + 1);

        if (isColumnIndexValid(topEdge.getColumnIdx())) {
            this.addRowAndColumnToAffectedByIdentifiers(topEdge, NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES);
        }
        if (isColumnIndexValid(bottomEdge.getColumnIdx())) {
            this.addRowAndColumnToAffectedByIdentifiers(bottomEdge, NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES);
        }
    }

    private void updateColumnSequenceAndExclude(int columnIdx,
                                             int seqIdx,
                                             List<Integer> oldRange,
                                             List<Integer> newRange,
                                             boolean triggeredByPlacingXs) {
        if (!newRange.equals(oldRange)) {
            this.changeColumnSequenceRange(columnIdx, seqIdx, newRange);

            if (triggeredByPlacingXs) {
                List<List<Integer>> allRanges = getColumnsSequencesRanges().get(columnIdx);
                List<Integer> lengths = getNonogramRules().getColumnSequencesLengths().get(columnIdx);
                List<String> columnState = getColumnCopy(columnIdx);

                tmpLog = SequenceCorrectionWhenPlacingXsLogHelper.generateLog(columnIdx, seqIdx, allRanges, newRange, columnState, lengths);
                addLog();
            }
        }

        this.excludeSequenceInColumn(columnIdx, seqIdx);
    }

    @Override
    public void placeXsColumnAtTooShortEmptySequences(int columnIdx) {

        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequencesLengths = this.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
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

        for (int rowIdx = 0; rowIdx < this.getNonogramRules().getHeight() - 1; rowIdx++) {
            onlyEmptyFieldsInSequence = true;
            potentiallyXPlacedField = new Field(rowIdx, columnIdx);
            if (isFieldWithX(this.nonogramSolutionBoard, potentiallyXPlacedField)) {

                firstXIndex = rowIdx;
                fieldAfterXToCheck = new Field(++rowIdx, columnIdx);
                while (rowIdx < this.getNonogramRules().getHeight()) {
                    if (isFieldEmpty(this.nonogramSolutionBoard, fieldAfterXToCheck)) {
                        fieldAfterXToCheck = new Field(++rowIdx, columnIdx);
                    } else {
                        if (isFieldColoured(this.nonogramSolutionBoard, fieldAfterXToCheck)) {
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
                            if (isFieldEmpty(this.nonogramSolutionBoard, fieldToExclude)) {
                                this.placeXAtGivenField(fieldToExclude, true);
                                this.addRowToAffectedActionsByIdentifiers(emptyFieldRowIdx, NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES);

                                this.tmpLog = generatePlacingXStepDescription(columnIdx, emptyFieldRowIdx, "placing \"X\" inside too short empty fields sequence");
                                addLog();

                                this.nonogramState.increaseMadeSteps();
                            } else if (this.SHOW_REPETITIONS) {
                                System.out.println("X placed in too short column empty field sequence earlier!");
                            }
                        }
                    }
                    rowIdx--;
                }
            }
        }
    }

    @Override
    public void placeXsColumnIfOWillMergeNearFieldsToTooLongColouredSequence(int columnIdx) {
        List<Integer> colouredFields = findColouredFieldsIndexesInColumn(nonogramSolutionBoard, columnIdx);
        List<List<Integer>> colouredRanges = groupConsecutiveIndices(colouredFields);
        List<List<List<Integer>>> rangesWithExtraFields = createSequencesRangesWithColouredFieldAdded(colouredRanges);

        List<String> columnBefore = getColumnCopy(columnIdx);
        for (int i = 0; i < colouredRanges.size(); i++) {
            List<List<Integer>> currentWithExtras = rangesWithExtraFields.get(i);

            checkAndPlaceXBefore(colouredRanges, currentWithExtras.get(0), i, columnIdx);
            checkAndPlaceXAfter(colouredRanges, currentWithExtras.get(1), i, columnIdx);
        }
        List<String> columnAfter = getColumnCopy(columnIdx);

        if (!columnBefore.equals(columnAfter)) {
            tmpLog = PlaceXsIfOWillCreateTooLongSequenceLogHelper.generateLog(
                    columnIdx,
                    columnBefore,
                    columnAfter,
                    getNonogramRules().getColumnSequencesLengths().get(columnIdx),
                    getColumnsSequencesRanges().get(columnIdx),
                    true
            );
            addLog();
        }
    }

    private void checkAndPlaceXBefore(List<List<Integer>> colouredRanges, List<Integer> rangeWithExtra, int idx, int colIdx) {
        List<Integer> merged = (idx > 0)
                ? tryToMergeColouredSequenceWithPrevious(colouredRanges.get(idx - 1), rangeWithExtra)
                : rangeWithExtra;

        int row = rangeWithExtra.get(0);
        Field field = new Field(row, colIdx);

        if (shouldPlaceX(row, merged, colIdx, field)) {
            placeXAtGivenField(field, true);
            addRowAndColumnToAffectedByIdentifiers(field, NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE);
            nonogramState.increaseMadeSteps();
        } else if (SHOW_REPETITIONS) {
            System.out.println("X because \"O\" will create too long sequence in column placed earlier!");
        }
    }

    private void checkAndPlaceXAfter(List<List<Integer>> colouredRanges, List<Integer> rangeWithExtra, int idx, int colIdx) {
        int nextRow = rangeWithExtra.get(1);
        if (nextRow == getNonogramRules().getHeight()) return;

        Field field = new Field(nextRow, colIdx);
        List<Integer> merged = (idx < colouredRanges.size() - 1)
                ? tryToMergeColouredSequenceWithNext(rangeWithExtra, colouredRanges.get(idx + 1))
                : rangeWithExtra;

        if (shouldPlaceX(nextRow, merged, colIdx, field)) {
            placeXAtGivenField(field, true);
            addRowAndColumnToAffectedByIdentifiers(field, NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE);
            nonogramState.increaseMadeSteps();
        } else if (SHOW_REPETITIONS) {
            System.out.println("X because \"O\" will create too long sequence in row placed earlier!");
        }
    }

    private boolean shouldPlaceX(int rowIdx, List<Integer> range, int colIdx, Field field) {
        return isRowIndexValid(rowIdx)
                && isFieldEmpty(nonogramSolutionBoard, field)
                && !colouredSequenceInColumnIsValid(range, colIdx, this);
    }

    @Override
    public void placeXsColumnIfONearXWillBeginTooLongPossibleColouredSequence(int columnIdx) {

        Field fieldToCheckX;
        Field firstColouredField;
        List<Integer> emptyFieldsRange;
        List<Integer> colouredFieldsRange;
        int emptyFieldsRangeLength;
        int colouredFieldsRangeLength;

        List<Integer> columnSequencesLengths = this.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> sequencesIdsWhichWillBeginTooLongPossibleColoured;
        List<Integer> sequencesIdsWhichNotReachColouredField;

        for (int rowIdx = this.getNonogramRules().getHeight() - 1; rowIdx > 0; rowIdx--) {
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
                            if (isFieldEmpty(this.nonogramSolutionBoard, emptyFieldNearX)) {
                                this.placeXAtGivenField(emptyFieldNearX, true);
                                this.addRowToAffectedActionsByIdentifiers(emptyFieldNearX.getRowIdx(), NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE);

                                this.tmpLog = generatePlacingXStepDescription(columnIdx, rowIdx, "placing \"X\" when \"O\" near \"X\" will begin too long possible coloured sequence");
                                addLog();

                                this.nonogramState.increaseMadeSteps();
                            }
                        }
                    }
                }
            }
        }

        for (int rowIdx = 0; rowIdx < this.getNonogramRules().getHeight(); rowIdx++) {
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
                            if (isFieldEmpty(this.nonogramSolutionBoard, emptyFieldNearX)) {
                                this.placeXAtGivenField(emptyFieldNearX, true);
                                this.addRowToAffectedActionsByIdentifiers(emptyFieldNearX.getRowIdx(), NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE);

                                this.tmpLog = generatePlacingXStepDescription(columnIdx, rowIdx, "placing \"X\" when \"O\" near \"X\" will begin too long possible coloured sequence");
                                addLog();

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

        while (areFieldIndexesValid(fieldToCheckEmpty) && isFieldEmpty(this.nonogramSolutionBoard, fieldToCheckEmpty)) {
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
        int seqLength = this.getNonogramRules().getColumnSequencesLengths().get(columnIdx).get(seqNo);
        List<Integer> lastRangeAfterColouredField = List.of(columnPossibleRange.get(0), columnPossibleRange.get(0) + seqLength - 1);

        return rangeInsideAnotherRange(lastRangeAfterColouredField, emptyFieldsRange);
    }

    private List<Integer> getEmptyFieldsRangeFromXToFirstColouredFieldOnBottom(Field xField) {
        List<Integer> emptyFieldsRange = new ArrayList<>();
        Field fieldToCheckEmpty = new Field(xField.getRowIdx() + 1, xField.getColumnIdx());

        while (areFieldIndexesValid(fieldToCheckEmpty) && isFieldEmpty(this.nonogramSolutionBoard, fieldToCheckEmpty)) {
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
        int seqLength = this.getNonogramRules().getColumnSequencesLengths().get(columnIdx).get(seqNo);
        List<Integer> lastRangeBeforeColouredField = List.of(columnPossibleRange.get(1) - seqLength + 1, columnPossibleRange.get(1));

        return rangeInsideAnotherRange(lastRangeBeforeColouredField, emptyFieldsRange);
    }

    @Override
    public void preventExtendingColouredSequenceToExcessLengthInColumn(int columnIdx) {
        preventExtendingColouredSequenceToExcessLengthInColumnToTop(columnIdx);
        preventExtendingColouredSequenceToExcessLengthInColumnToBottom(columnIdx);
    }

    private void preventExtendingColouredSequenceToExcessLengthInColumnToTop(int columnIdx) {
        List<Integer> columnSequencesLengths = this.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);

        Field fieldToCheckX;

        int potentiallyColouredFieldRow;
        Field fieldToCheckColoured;

        List<Integer> sequencesIds;
        int maxSequenceLength;
        List<List<Integer>> colouredSequences;
        List<Integer> validSequenceIds;
        List<Integer> validSequenceLengths;

        for (int rowIdx = this.getNonogramRules().getHeight() - 1; rowIdx > 0; rowIdx--) {
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
                            if (isFieldEmpty(this.nonogramSolutionBoard, fieldToColour)) {
                                this.colourFieldAtGivenPosition(fieldToColour, "--C-");
                                this.addRowToAffectedActionsByIdentifiers(fieldToColour.getRowIdx(), NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART);

                                this.nonogramState.increaseMadeSteps();
                                this.tmpLog = generateColourStepDescription(columnIdx, rowToColourIdx, "extend coloured sequence to matching length to top near X (with placing X before)");
                            }
                        }


                        Field fieldToPlaceX = new Field(colouredSequenceRowStartIdx - 1, columnIdx);
                        if (isFieldEmpty(this.nonogramSolutionBoard, fieldToPlaceX)) {
                            this.placeXAtGivenField(fieldToPlaceX, true);
                            this.addRowToAffectedActionsByIdentifiers(fieldToPlaceX.getRowIdx(), NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART);

                            this.nonogramState.increaseMadeSteps();
                            // TODO - add log
                        }

                        // moreover - only one id is valid -> can correct sequence range
                        if (validSequenceIds.size() == 1) {
                            int matchingSeqId = validSequenceIds.get(0);
                            List<Integer> oldRange = columnSequencesRanges.get(matchingSeqId);
                            List<Integer> updatedRange = new ArrayList<>(Arrays.asList(colouredSequenceRowStartIdx, potentiallyColouredFieldRow));

                            if (!rangesEqual(oldRange, updatedRange)) {
                                this.updateColumnSequenceRange(columnIdx, matchingSeqId, updatedRange);
                                this.addColumnToAffectedActionsByIdentifiers(columnIdx, NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART);

                                this.nonogramState.increaseMadeSteps();
                                this.tmpLog = generateCorrectingColumnSequenceRangeStepDescription(columnIdx, matchingSeqId, oldRange, updatedRange, "update only matching sequence part preventing excess length to top");
                                addLog();
                            }

                        }
                    }
                }
            }
        }
    }

    private void preventExtendingColouredSequenceToExcessLengthInColumnToBottom(int columnIdx) {
        List<Integer> columnSequencesLengths = this.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);

        Field fieldToCheckX;

        int potentiallyColouredFieldRowIndex;
        Field fieldToCheckColoured;

        List<Integer> sequencesIds;
        int maxSequenceLength;
        List<List<Integer>> colouredSequences;
        List<Integer> validSequencesIds;
        List<Integer> validSequenceLengths;

        for (int rowIdx = 0; rowIdx < this.getNonogramRules().getHeight() - 1; rowIdx++) {
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
                            if (isFieldEmpty(this.nonogramSolutionBoard, fieldToColour)) {
                                this.colourFieldAtGivenPosition(fieldToColour, "--C-");
                                this.addRowToAffectedActionsByIdentifiers(fieldToColour.getRowIdx(),
                                         NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART);
                                this.nonogramState.increaseMadeSteps();

                                this.tmpLog = generateColourStepDescription(columnIdx, rowToColourIdx, "extend coloured sequence to matching length to bottom near X (with placing X before)");
                                addLog();
                            }
                        }

                        Field fieldToPlaceX = new Field(colouredSequenceEndRowIndex + 1, columnIdx);
                        if (isFieldEmpty(this.nonogramSolutionBoard, fieldToPlaceX)) {
                            this.placeXAtGivenField(fieldToPlaceX, true);
                            this.addRowToAffectedActionsByIdentifiers(fieldToPlaceX.getRowIdx(), NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART);
                        }


                        if (validSequencesIds.size() == 1) {
                            int matchingSeqId = validSequencesIds.get(0);
                            List<Integer> oldRange = columnSequencesRanges.get(matchingSeqId);
                            List<Integer> updatedRange = new ArrayList<>(Arrays.asList(potentiallyColouredFieldRowIndex, colouredSequenceEndRowIndex));

                            this.updateColumnSequenceRange(columnIdx, matchingSeqId, updatedRange);
                            this.addColumnToAffectedActionsByIdentifiers(columnIdx, NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART);

                            this.nonogramState.increaseMadeSteps();
                            this.tmpLog = generateCorrectingColumnSequenceRangeStepDescription(columnIdx, matchingSeqId, oldRange, updatedRange, "update only matching sequence part preventing excess length to bottom");
                            addLog();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void markAvailableFieldsInColumn(int columnIdx) {
        List<Integer> columnSequencesLengths = this.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);

        for (int rowIdx = 0; rowIdx < this.getNonogramRules().getHeight(); rowIdx++) {
            Field field = new Field(rowIdx, columnIdx);
            if (!isFieldColoured(this.nonogramSolutionBoard, field)) continue;

            List<Integer> colouredRange = findColouredSequenceRangeInColumn(rowIdx, columnIdx);
            int colouredLength = rangeLength(colouredRange);

            int matchingCount = 0;
            int matchedSeqIdx = -1;

            for (int seqIdx = 0; seqIdx < columnSequencesLengths.size(); seqIdx++) {
                List<Integer> range = columnSequencesRanges.get(seqIdx);
                if (rangeInsideAnotherRange(colouredRange, range) && colouredLength <= columnSequencesLengths.get(seqIdx)) {
                    matchingCount++;
                    matchedSeqIdx = seqIdx;
                }
            }

            if (matchingCount == 1) {
                String marker = NonogramHelper.indexToSequenceCharMark(matchedSeqIdx);
                List<String> beforeMarking = new ArrayList<>(this.getNonogramBoardColumnWithMarks(columnIdx));

                for (int i = colouredRange.get(0); i <= colouredRange.get(1); i++) {
                    String cell = this.getNonogramSolutionBoardWithMarks().get(rowIdx).get(i);
                    if (cell.startsWith(EMPTY_FIELD, 1)) {
                        this.markColumnBoardField(columnIdx, i, marker);
                        this.nonogramState.increaseMadeSteps();
                    } else if (this.SHOW_REPETITIONS) {
                        System.out.println("Column field was marked earlier.");
                    }
                }

                List<String> afterMarking = new ArrayList<>(this.getNonogramBoardColumnWithMarks(columnIdx));
                if (!beforeMarking.equals(afterMarking)) {
                    tmpLog = MarkAvailableFieldsLogHelper.generateLog(
                            columnIdx, beforeMarking, afterMarking, matchedSeqIdx, marker, false
                    );
                    addLog();
                }

                List<Integer> oldRange = columnSequencesRanges.get(matchedSeqIdx);
                List<Integer> newRange = calculateNewMarkedRangeFromParameters(
                        oldRange, colouredRange, columnSequencesLengths.get(matchedSeqIdx)
                );

                if (!rangesEqual(oldRange, newRange)) {
                    tmpLog = SequenceRangeCorrectionWhenMarkingFieldsLogHelper.generateLog(
                            columnIdx,
                            matchedSeqIdx,
                            columnSequencesRanges,
                            newRange,
                            columnSequencesLengths,
                            false
                    );
                    addLog();
                    this.changeColumnSequenceRange(columnIdx, matchedSeqIdx, newRange);
                    addColumnToAffectedActionsByIdentifiers(columnIdx, NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN);
                }
            }
        }
    }

    @Override
    protected void excludeFieldLogicSpecific(Field field) {
        this.excludeFieldInColumn(field);
    }

    public void excludeSequenceInColumn(int columnIdx, int seqIdx) {
        if (isColumnIndexValid(columnIdx) && !this.columnsSequencesIdsNotToInclude.get(columnIdx).contains(seqIdx)) {
            tmpLog =  generateExcludedSequenceLog(
                    columnIdx,
                    seqIdx,
                    false,
                    this.getColumnCopy(columnIdx),
                    this.getNonogramRules().getColumnSequencesLengths().get(columnIdx),
                    this.getColumnsSequencesRanges().get(columnIdx)
            );
            addLog();
            this.columnsSequencesIdsNotToInclude.get(columnIdx).add(seqIdx);
            Collections.sort(this.columnsSequencesIdsNotToInclude.get(columnIdx));
        }
    }

    public void excludeFieldsInColumn(List<Field> fieldsToExclude) {
        fieldsToExclude.forEach(this::excludeFieldInColumn);
    }

    protected void excludeFieldInColumn(Field fieldToExclude) {
        int fieldColIdx = fieldToExclude.getColumnIdx();
        int fieldRowIdx = fieldToExclude.getRowIdx();
        if (areFieldIndexesValid(fieldToExclude) && !this.columnsFieldsNotToInclude.get(fieldColIdx).contains(fieldRowIdx)) {
            this.columnsFieldsNotToInclude.get(fieldColIdx).add(fieldRowIdx);
            Collections.sort(this.columnsFieldsNotToInclude.get(fieldColIdx));
        }
    }

    public void updateColumnSequenceRange(int columnIdx, int sequenceIdx, List<Integer> updatedRange) {
        this.columnsSequencesRanges.get(columnIdx).set(sequenceIdx, updatedRange);
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
            if (!isFieldColoured(this.nonogramSolutionBoard, potentiallyColouredField)) {
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
            if (isFieldWithX(this.nonogramSolutionBoard, fieldToCheck)) {
                break;
            }
        }

        return minimumRowIndex + 1;
    }

    public int maximumRowIndexWithoutX(int columnIdx, int firstSequenceRowIdx, int sequenceFullLength) {
        int maximumRowIndex = firstSequenceRowIdx;
        int maximumRowIndexLimit = Math.min(firstSequenceRowIdx + sequenceFullLength - 1, this.getNonogramRules().getHeight() - 1);
        Field fieldToCheck;

        for (; maximumRowIndex <= maximumRowIndexLimit; maximumRowIndex++) {
            fieldToCheck = new Field(maximumRowIndex, columnIdx);
            if (isFieldWithX(this.nonogramSolutionBoard, fieldToCheck)) {
                break;
            }
        }

        return maximumRowIndex - 1;
    }

    public void markColumnBoardField(int colIdx, int rowIdx, String colSeqMark) {
        String oldRowField = this.nonogramSolutionBoardWithMarks.get(rowIdx).get(colIdx);
        this.nonogramSolutionBoardWithMarks.get(rowIdx).set(colIdx, oldRowField.substring(0, 2) + MARKED_COLUMN_INDICATOR + colSeqMark);
    }

    public void changeColumnSequenceRange(int columnIndex, int sequenceIndex, List<Integer> updatedRange) {
        this.columnsSequencesRanges.get(columnIndex).set(sequenceIndex, updatedRange);
    }

    public void setNonogramSolutionBoardColumn(int columnIdx, List<String> boardColumn) {
        for (int rowIdx = 0; rowIdx < this.getNonogramRules().getHeight(); rowIdx++) {
            this.nonogramSolutionBoard.get(rowIdx).set(columnIdx, boardColumn.get(rowIdx));
        }
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
