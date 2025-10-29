package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.RangeCorrectionHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.common.CommonRangeCorrectionHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range.*;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.solve.SequenceRangeCorrectionHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.solve.SequenceRangeCorrectionWhenMetXHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.deepCopy;
import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.rangeLength;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.isFieldColoured;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.TooLongMergeFieldHelper.collectColouredSequencesRanges;

public class ColumnSequencesCorrectionHelperImpl extends CommonRangeCorrectionHelper implements ColumnSequencesCorrectionHelper, RefreshableColumnHelper {

    private final NonogramColumnLogic nonogramColumnLogic;

    public ColumnSequencesCorrectionHelperImpl(NonogramColumnLogic nonogramColumnLogic) {
        this.nonogramColumnLogic = nonogramColumnLogic;
    }

    @Override
    public void correctColumnSequencesRanges(int columnIdx) {
        List<List<Integer>> initialRanges = deepCopy(nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx));

        boolean anyUpdated = false;

        anyUpdated |= correctFromTop(columnIdx);
        anyUpdated |= correctFromBottom(columnIdx);

        List<List<Integer>> updatedRanges = nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx);

        if (anyUpdated) {
            nonogramColumnLogic.getNonogramState().increaseMadeSteps();

            Field columnField = new Field(0, columnIdx);
            nonogramColumnLogic.getActionScheduler().scheduleActionsBasedOnField(columnField, NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_COLUMN);

            String tmpLog = SequencesRangesCorrectionLogHelper.generateLog(
                    false, // isRow
                    columnIdx,
                    nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx),
                    nonogramColumnLogic.getColumnsFieldsNotToInclude().get(columnIdx),
                    nonogramColumnLogic.getColumnsSequencesIdsNotToInclude().get(columnIdx),
                    initialRanges,
                    updatedRanges
            );
            nonogramColumnLogic.setTmpLog(tmpLog);
            nonogramColumnLogic.addLog();
        }
    }

    private boolean correctFromTop(int columnIdx) {
        List<Integer> lengths = nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> ranges = nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> fieldsNotToInclude = nonogramColumnLogic.getColumnsFieldsNotToInclude().get(columnIdx);
        List<Integer> excludedIds = nonogramColumnLogic.getColumnsSequencesIdsNotToInclude().get(columnIdx);

        boolean anyUpdated = false;

        for (int seqIdx = 0; seqIdx < ranges.size() - 1; seqIdx++) {
            int nextIdx = seqIdx + 1;
            if (excludedIds.contains(nextIdx)) continue;

            List<Integer> updatedNext = excludedIds.contains(seqIdx)
                    ? SequenceRangeCorrectionHelper.calculateUpdatedNextSequenceRangeAfterExcludedSequence(ranges, fieldsNotToInclude, seqIdx, nextIdx)
                    : SequenceRangeCorrectionHelper.calculateUpdatedNextSequenceRangeAfterIncludedSequence(ranges, lengths, seqIdx, nextIdx);

            anyUpdated |= tryCorrectFromTop(columnIdx, lengths, ranges.get(nextIdx), updatedNext, nextIdx);
        }

        return anyUpdated;
    }

    private boolean tryCorrectFromTop(int columnIdx,
                                   List<Integer> lengths,
                                   List<Integer> oldRange,
                                   List<Integer> newRange,
                                   int idx) {
        if (!oldRange.get(0).equals(newRange.get(0))) {
            nonogramColumnLogic.updateColumnSequenceRange(columnIdx, idx, newRange);
            if (rangeLength(newRange) == lengths.get(idx) && nonogramColumnLogic.getBoardAccessHelper().isColumnRangeColoured(columnIdx, newRange)) {
                nonogramColumnLogic.excludeSequenceInColumn(columnIdx, idx);
            }

            return true;
        }

        return false;
    }

    private boolean correctFromBottom(int columnIdx) {
        List<Integer> lengths = nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> ranges = nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> fieldsNotToInclude = nonogramColumnLogic.getColumnsFieldsNotToInclude().get(columnIdx);
        List<Integer> excludedIds = nonogramColumnLogic.getColumnsSequencesIdsNotToInclude().get(columnIdx);

        boolean anyUpdated = false;

        for (int seqIdx = ranges.size() - 1; seqIdx > 0; seqIdx--) {
            int prevIdx = seqIdx - 1;
            if (excludedIds.contains(prevIdx)) continue;

            List<Integer> updatedPrev = excludedIds.contains(seqIdx)
                    ? SequenceRangeCorrectionHelper.calculateUpdatedPreviousSequenceRangeAfterExcludedSequence(ranges, fieldsNotToInclude, seqIdx, prevIdx)
                    : SequenceRangeCorrectionHelper.calculateUpdatedPreviousSequenceRangeAfterIncludedSequence(ranges, lengths, seqIdx, prevIdx);

            anyUpdated |= tryCorrectFromBottom(columnIdx, lengths, ranges.get(prevIdx), updatedPrev, prevIdx);
        }

        return anyUpdated;
    }

    private boolean tryCorrectFromBottom(int columnIdx,
                                      List<Integer> lengths,
                                      List<Integer> oldRange,
                                      List<Integer> newRange,
                                      int sequenceIdx) {
        if (!oldRange.get(1).equals(newRange.get(1))) {
            nonogramColumnLogic.updateColumnSequenceRange(columnIdx, sequenceIdx, newRange);
            if (rangeLength(newRange) == lengths.get(sequenceIdx) && nonogramColumnLogic.getBoardAccessHelper().isColumnRangeColoured(columnIdx, newRange)) {
                nonogramColumnLogic.excludeSequenceInColumn(columnIdx, sequenceIdx);
            }

            return true;
        }

        return false;
    }

    @Override
    public void correctColumnSequencesRangesWhenMetColouredField(int columnIdx) {
        List<List<Integer>> initialRanges = deepCopy(nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx));

        boolean anyUpdated = false;

        anyUpdated |= correctColumnSequencesRangesWhenMetColouredFieldFromTop(columnIdx);
        anyUpdated |= correctColumnSequencesRangesWhenMetColouredFieldFromBottom(columnIdx);

        if (anyUpdated) {
            List<List<Integer>> updatedRanges = deepCopy(nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx));
            nonogramColumnLogic.getNonogramState().increaseMadeSteps();

            Field columnField = new Field(0, columnIdx);
            nonogramColumnLogic.getActionScheduler().scheduleActionsBasedOnField(columnField,
                    NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS_IN_COLUMN);

            String tmpLog = SequenceRangeCorrectionWhenMetColouredFieldsLogHelper.generateLog(
                    false, // isRow
                    columnIdx,
                    nonogramColumnLogic.getNonogramBoardColumn(columnIdx),
                    nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx),
                    initialRanges,
                    updatedRanges
            );
            nonogramColumnLogic.setTmpLog(tmpLog);
            nonogramColumnLogic.addLog();
        }
    }

    private boolean correctColumnSequencesRangesWhenMetColouredFieldFromTop(int columnIdx) {
        var ranges = nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx);
        var lengths = nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        boolean changed = false;

        if (lengths.isEmpty()) return false;
        if (ranges.isEmpty())  return false;

        int seqIdx = 0;
        int seqLength = lengths.get(seqIdx);
        int rowIdx = 0;

        while (rowIdx < nonogramColumnLogic.getNonogramRules().getHeight() && seqIdx < lengths.size()) {
            Field field = new Field(rowIdx, columnIdx);

            if (!isFieldColoured(nonogramColumnLogic.getNonogramSolutionBoard(), field)) {
                rowIdx++;
                continue;
            }

            List<Integer> oldRange = ranges.get(seqIdx);
            List<Integer> updatedRange = RangeCorrectionHelper.updatedSequenceRangeWhenMetColouredField(
                    oldRange.get(0), oldRange.get(1), rowIdx, seqLength, true);

            if (!updatedRange.equals(oldRange)) {
                nonogramColumnLogic.updateColumnSequenceRange(columnIdx, seqIdx, updatedRange);

                if (rangeLength(updatedRange) == seqLength &&
                        nonogramColumnLogic.getBoardAccessHelper().isColumnRangeColoured(columnIdx, updatedRange)) {
                    nonogramColumnLogic.excludeSequenceInColumn(columnIdx, seqIdx);
                    nonogramColumnLogic.getNonogramState().increaseMadeSteps();
                }

                changed = true;
            }

            rowIdx += seqLength;
            seqIdx++;
            if (seqIdx != lengths.size()) {
                seqLength = lengths.get(seqIdx);
            }
        }

        return changed;
    }

    private boolean correctColumnSequencesRangesWhenMetColouredFieldFromBottom(int columnIdx) {
        var ranges = nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx);
        var lengths = nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        boolean changed = false;

        int seqIdx = lengths.size() - 1;
        int seqLength = lengths.get(seqIdx);
        int rowIdx = nonogramColumnLogic.getNonogramRules().getHeight() - 1;

        while (rowIdx >= 0 && seqIdx >= 0) {
            Field field = new Field(rowIdx, columnIdx);

            if (!isFieldColoured(nonogramColumnLogic.getNonogramSolutionBoard(), field)) {
                rowIdx--;
                continue;
            }

            List<Integer> oldRange = ranges.get(seqIdx);
            List<Integer> updatedRange = RangeCorrectionHelper.updatedSequenceRangeWhenMetColouredField(
                    oldRange.get(0), oldRange.get(1), rowIdx, seqLength, false);

            if (!updatedRange.equals(oldRange)) {
                nonogramColumnLogic.updateColumnSequenceRange(columnIdx, seqIdx, updatedRange);

                if (rangeLength(updatedRange) == seqLength &&
                        nonogramColumnLogic.getBoardAccessHelper().isColumnRangeColoured(columnIdx, updatedRange)) {
                    nonogramColumnLogic.excludeSequenceInColumn(columnIdx, seqIdx);
                    nonogramColumnLogic.getNonogramState().increaseMadeSteps();
                }

                changed = true;
            }

            rowIdx -= seqLength;
            seqIdx--;
            if (seqIdx > -1) {
                seqLength = lengths.get(seqIdx);
            }
        }

        return changed;
    }

    @Override
    public void correctColumnSequencesRangesIfXOnWay(int columnIndex, boolean changeLogicDetails) {
        var ranges = nonogramColumnLogic.getColumnsSequencesRanges().get(columnIndex);
        var lengths = nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIndex);
        var excluded = nonogramColumnLogic.getColumnsSequencesIdsNotToInclude().get(columnIndex);

        var column = nonogramColumnLogic.getNonogramBoardColumn(columnIndex);

        boolean changed = false;

        List<List<Integer>> initialRanges = deepCopy(ranges);

        for (int sequenceIndex = 0; sequenceIndex < ranges.size(); sequenceIndex++) {
            if (excluded.contains(sequenceIndex)) continue;

            List<Integer> currentRange = ranges.get(sequenceIndex);
            int length = lengths.get(sequenceIndex);

            List<Integer> updatedRange = SequenceRangeCorrectionWhenMetXHelper.calculateCorrectedRangeWithoutX(
                    true, columnIndex, currentRange, length, nonogramColumnLogic.getNonogramSolutionBoard());

            if (!currentRange.equals(updatedRange)) {
                nonogramColumnLogic.updateColumnSequenceRange(columnIndex, sequenceIndex, updatedRange);

                if (changeLogicDetails && shouldExcludeSequence(columnIndex, updatedRange, length)) {
                    nonogramColumnLogic.excludeSequenceInColumn(columnIndex, sequenceIndex);
                }

                changed = true;
            }
        }

        if (changed && changeLogicDetails) {
            List<List<Integer>> updatedRanges = nonogramColumnLogic.getColumnsSequencesRanges().get(columnIndex);

            String tmpLog = SequenceRangeCorrectionWhenMetXLogHelper.generateLog(
                    false,
                    columnIndex,
                    column,
                    initialRanges,
                    updatedRanges,
                    lengths,
                    excluded);
            nonogramColumnLogic.setTmpLog(tmpLog);
            nonogramColumnLogic.addLog();

            nonogramColumnLogic.getNonogramState().increaseMadeSteps();
            nonogramColumnLogic.getActionScheduler().scheduleActionsBasedOnField(
                    new Field(0, columnIndex), NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_COLUMN);
        }
    }

    private boolean shouldExcludeSequence(int columnIdx, List<Integer> range, int length) {
        return rangeLength(range) == length &&
                nonogramColumnLogic.getBoardAccessHelper().isColumnRangeColoured(columnIdx, range);
    }

    @Override
    public void correctColumnSequencesRangesWhenMatchingFieldsToSequences(int columnIdx) {
        List<List<Integer>> columnSequencesRanges = nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequencesLengths = nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> colouredRanges = collectColouredSequencesRanges(nonogramColumnLogic.getNonogramSolutionBoard(), columnIdx, false);

        List<List<Integer>> initialRanges = deepCopy(columnSequencesRanges);

        boolean hasChangedGlobal = false;
        boolean hasChanged;

        do {
            hasChanged = processDirection(colouredRanges, columnSequencesRanges, columnSequencesLengths, true); // TOP -> BOTTOM
            hasChanged |= processDirection(colouredRanges, columnSequencesRanges, columnSequencesLengths, false); // BOTTOM → TOP

            hasChangedGlobal |= hasChanged;
        } while (hasChanged);

        if (hasChangedGlobal) {
            List<List<Integer>> updatedRanges = deepCopy(columnSequencesRanges);

            String tmpLog = SequenceRangeCorrectionWhenMatchingFieldsToSequencesLogHelper.generateLog(
                    false, // isRow
                    columnIdx,
                    columnSequencesLengths,
                    nonogramColumnLogic.getNonogramBoardColumn(columnIdx),
                    initialRanges,
                    updatedRanges
            );
            nonogramColumnLogic.setTmpLog(tmpLog);
            nonogramColumnLogic.addLog();

            nonogramColumnLogic.getNonogramState().increaseMadeSteps();
            Field columnField = new Field(0, columnIdx);
            nonogramColumnLogic.getActionScheduler().scheduleActionsBasedOnField(columnField, NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_COLUMN);
        }
    }

    private boolean processDirection(List<List<Integer>> colouredRanges, List<List<Integer>> columnSequencesRanges,
                                     List<Integer> columnSequencesLengths, boolean fromTop) {

        Map<Integer, List<Integer>> colouredToSequences = collectMatchingSequences(colouredRanges, columnSequencesRanges, columnSequencesLengths, fromTop);
        filterSequences(colouredToSequences, columnSequencesRanges.size(), fromTop);

        return updateRanges(colouredToSequences, colouredRanges, columnSequencesRanges, columnSequencesLengths);
    }

    private void filterSequences(Map<Integer, List<Integer>> colouredToSequences, int totalSequences, boolean fromTop) {
        int boundary = fromTop ? -1 : totalSequences;
        List<Integer> keys = new ArrayList<>(colouredToSequences.keySet());
        keys.sort(fromTop ? Comparator.naturalOrder() : Comparator.reverseOrder());

        for (int i : keys) {
            List<Integer> possible = colouredToSequences.get(i);
            if (possible == null || possible.isEmpty()) continue;

            List<Integer> filtered = filterByBoundary(possible, boundary, fromTop);
            colouredToSequences.put(i, filtered);

            if (filtered.size() == 1) {
                boundary = filtered.get(0);
            }
        }
    }

    private List<Integer> filterByBoundary(List<Integer> sequenceIds, int boundary, boolean fromTop) {
        return sequenceIds.stream()
                .filter(seqId -> fromTop ? seqId >= boundary : seqId <= boundary)
                .toList();
    }

    @Override
    public void correctColumnSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(int columnIdx) {
        boolean anyUpdated = false;

        List<List<Integer>> ranges = nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx);
        List<List<Integer>> before = deepCopy(ranges);

        for (int seqIdx = 0; seqIdx < ranges.size(); seqIdx++) {
            List<Integer> currentRange = ranges.get(seqIdx);

            List<Integer> updatedRange = RangeCorrectionHelper.adjustRangeIfColouredAtEdges(
                    currentRange,
                    columnIdx,
                    true, // isColumn == true
                    nonogramColumnLogic.getNonogramSolutionBoard(),
                    nonogramColumnLogic.getNonogramRules().getHeight()
            );

            if (!updatedRange.equals(currentRange)) {
                nonogramColumnLogic.updateColumnSequenceRange(columnIdx, seqIdx, updatedRange);
                nonogramColumnLogic.getNonogramState().increaseMadeSteps();
                anyUpdated = true;
            }
        }

        if (anyUpdated) {
            List<List<Integer>> after = nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx);
            List<Integer> lengths = nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
            List<String> line = nonogramColumnLogic.getNonogramBoardColumn(columnIdx);

            String tmpLog = SequenceRangeCorrectionFromColouredEdgesLogHelper.generateLog(
                    false, // isRow
                    columnIdx,
                    before,
                    after,
                    lengths,
                    line
            );
            nonogramColumnLogic.setTmpLog(tmpLog);
            nonogramColumnLogic.addLog();

            nonogramColumnLogic.getNonogramState().increaseMadeSteps();
            Field columnField = new Field(0, columnIdx);
            nonogramColumnLogic.getActionScheduler().scheduleActionsBasedOnField(columnField, NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE_IN_COLUMN);
        }
    }

    @Override
    public void refreshFrom(NonogramColumnLogic logicToCopy) {
        nonogramColumnLogic.setLogs(logicToCopy.getLogs());

        nonogramColumnLogic.setNonogramSolutionBoard(logicToCopy.getNonogramSolutionBoard());
        nonogramColumnLogic.setColumnsSequencesRanges(logicToCopy.getColumnsSequencesRanges());
        nonogramColumnLogic.setColumnsFieldsNotToInclude(logicToCopy.getColumnsFieldsNotToInclude());
    }
}
