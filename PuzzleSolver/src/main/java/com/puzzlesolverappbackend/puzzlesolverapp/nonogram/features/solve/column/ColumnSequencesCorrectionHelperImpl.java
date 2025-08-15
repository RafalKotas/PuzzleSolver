package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.RangeCorrectionHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range.*;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.solve.SequenceRangeCorrectionHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.solve.SequenceRangeCorrectionWhenMetXHelper;

import java.util.*;

import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.isFieldColoured;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.TooLongMergeFieldHelper.collectColouredSequencesRanges;

public class ColumnSequencesCorrectionHelperImpl implements ColumnSequencesCorrectionHelper, RefreshableColumnHelper {

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
            nonogramColumnLogic.getActionScheduler().scheduleActionsBasedOnField(columnField, NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES);

            String tmpLog = SequencesRangesCorrectionLogHelper.generateLog(
                    false, // isRow
                    columnIdx,
                    nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx),
                    nonogramColumnLogic.getColumnsFieldsNotToInclude().get(columnIdx),
                    nonogramColumnLogic.getColumnsSequencesIdsNotToInclude().get(columnIdx),
                    initialRanges,
                    updatedRanges
            );
            nonogramColumnLogic.getLogService().setTmpLog(tmpLog);
            nonogramColumnLogic.getLogService().addLog();
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
                    NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS);

            String tmpLog = SequenceRangeCorrectionWhenMetColouredFieldsLogHelper.generateLog(
                    false, // isRow
                    columnIdx,
                    nonogramColumnLogic.getNonogramBoardColumn(columnIdx),
                    nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx),
                    initialRanges,
                    updatedRanges
            );
            nonogramColumnLogic.getLogService().setTmpLog(tmpLog);
            nonogramColumnLogic.getLogService().addLog();
        }
    }

    private boolean correctColumnSequencesRangesWhenMetColouredFieldFromTop(int columnIdx) {
        var ranges = nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx);
        var lengths = nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        boolean changed = false;

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
    public void correctColumnSequencesRangesIfXOnWay(int columnIdx, boolean changeLogicDetails) {
        var ranges = nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx);
        var lengths = nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        var excluded = nonogramColumnLogic.getColumnsSequencesIdsNotToInclude().get(columnIdx);

        var column = nonogramColumnLogic.getNonogramBoardColumn(columnIdx);

        boolean changed = false;

        List<List<Integer>> initialRanges = deepCopy(ranges);

        for (int seqIdx = 0; seqIdx < ranges.size(); seqIdx++) {
            if (excluded.contains(seqIdx)) continue;

            List<Integer> currentRange = ranges.get(seqIdx);
            int length = lengths.get(seqIdx);

            List<Integer> corrected = SequenceRangeCorrectionWhenMetXHelper.calculateCorrectedRangeWithoutX(
                    currentRange, length, columnIdx, true, nonogramColumnLogic.getNonogramSolutionBoard());

            if (!currentRange.equals(corrected)) {
                nonogramColumnLogic.updateColumnSequenceRange(columnIdx, seqIdx, corrected);

                if (changeLogicDetails && shouldExcludeSequence(columnIdx, corrected, length)) {
                    nonogramColumnLogic.excludeSequenceInColumn(columnIdx, seqIdx);
                }

                changed = true;
            }
        }

        if (changed && changeLogicDetails) {
            List<List<Integer>> updatedRanges = nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx);

            nonogramColumnLogic.getLogService().setTmpLog(SequenceRangeCorrectionWhenMetXLogHelper.generateLog(
                    columnIdx,
                    column,
                    initialRanges,
                    updatedRanges,
                    lengths,
                    excluded,
                    false));
            nonogramColumnLogic.getLogService().addLog();

            nonogramColumnLogic.getNonogramState().increaseMadeSteps();
            nonogramColumnLogic.getActionScheduler().scheduleActionsBasedOnField(
                    new Field(0, columnIdx), NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY);
        }
    }

    private boolean shouldExcludeSequence(int columnIdx, List<Integer> range, int length) {
        return rangeLength(range) == length && nonogramColumnLogic.getBoardAccessHelper().isColumnRangeColoured(columnIdx, range);
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
            nonogramColumnLogic.getLogService().setTmpLog(tmpLog);
            nonogramColumnLogic.getLogService().addLog();

            nonogramColumnLogic.getNonogramState().increaseMadeSteps();
            Field columnField = new Field(0, columnIdx);
            nonogramColumnLogic.getActionScheduler().scheduleActionsBasedOnField(columnField, NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES);
        }
    }

    private boolean processDirection(List<List<Integer>> colouredRanges, List<List<Integer>> columnSequencesRanges,
                                     List<Integer> columnSequencesLengths, boolean fromTop) {

        Map<Integer, List<Integer>> colouredToSeqs = collectMatchingSequences(colouredRanges, columnSequencesRanges, columnSequencesLengths, fromTop);
        filterSequences(colouredToSeqs, columnSequencesRanges.size(), fromTop);

        return updateRanges(colouredToSeqs, colouredRanges, columnSequencesRanges, columnSequencesLengths);
    }

    private Map<Integer, List<Integer>> collectMatchingSequences(List<List<Integer>> colouredRanges, List<List<Integer>> sequenceRanges,
                                                                 List<Integer> sequenceLengths, boolean fromTop) {
        Map<Integer, List<Integer>> result = new HashMap<>();
        int start = fromTop ? 0 : colouredRanges.size() - 1;
        int end = fromTop ? colouredRanges.size() : -1;
        int step = fromTop ? 1 : -1;

        for (int i = start; i != end; i += step) {
            List<Integer> coloured = colouredRanges.get(i);
            int colouredLen = rangeLength(coloured);

            List<Integer> possible = new ArrayList<>();
            for (int seqIdx = 0; seqIdx < sequenceRanges.size(); seqIdx++) {
                List<Integer> seqRange = sequenceRanges.get(seqIdx);
                int seqLen = sequenceLengths.get(seqIdx);

                if (rangeInsideAnotherRange(coloured, seqRange) && seqLen >= colouredLen) {
                    possible.add(seqIdx);
                }
            }

            result.put(i, possible);
        }

        return result;
    }

    private void filterSequences(Map<Integer, List<Integer>> colouredToSeqs, int totalSequences, boolean fromTop) {
        int boundary = fromTop ? -1 : totalSequences;
        List<Integer> keys = new ArrayList<>(colouredToSeqs.keySet());
        keys.sort(fromTop ? Comparator.naturalOrder() : Comparator.reverseOrder());

        for (int i : keys) {
            List<Integer> possible = colouredToSeqs.get(i);
            if (possible == null || possible.isEmpty()) continue;

            List<Integer> filtered = filterByBoundary(possible, boundary, fromTop);
            colouredToSeqs.put(i, filtered);

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

    private boolean updateRanges(Map<Integer, List<Integer>> colouredToSeqs, List<List<Integer>> colouredRanges,
                                 List<List<Integer>> sequenceRanges, List<Integer> sequenceLengths) {
        boolean hasChanged = false;

        for (Map.Entry<Integer, List<Integer>> entry : colouredToSeqs.entrySet()) {
            List<Integer> possible = entry.getValue();
            if (possible == null || possible.isEmpty()) continue;

            int seqIdx = possible.size() == 1 ? possible.get(0)
                    : possible.stream().min(Comparator.naturalOrder()).orElse(possible.get(0));  // fallback

            List<Integer> seqRange = sequenceRanges.get(seqIdx);
            int seqLen = sequenceLengths.get(seqIdx);
            List<Integer> coloured = colouredRanges.get(entry.getKey());

            int newStart = coloured.get(1) - seqLen + 1;
            int newEnd = coloured.get(0) + seqLen - 1;

            int updatedStart = possible.size() == 1 ? Math.max(newStart, seqRange.get(0)) : seqRange.get(0);
            int updatedEnd = possible.size() == 1 ? Math.min(newEnd, seqRange.get(1)) : seqRange.get(1);

            boolean inside = rangeInsideAnotherRange(coloured, seqRange);
            boolean valid = newStart <= newEnd;

            if (inside && valid && (updatedStart != seqRange.get(0) || updatedEnd != seqRange.get(1))) {
                seqRange.set(0, updatedStart);
                seqRange.set(1, updatedEnd);
                hasChanged = true;
            }
        }

        return hasChanged;
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
                    columnIdx,
                    before,
                    after,
                    lengths,
                    line,
                    false // isRow == false
            );
            nonogramColumnLogic.getLogService().setTmpLog(tmpLog);
            nonogramColumnLogic.getLogService().addLog();

            Field columnField = new Field(0, columnIdx);
            nonogramColumnLogic.getActionScheduler().scheduleActionsBasedOnField(columnField, NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE);
        }
    }

    @Override
    public void refreshFrom(NonogramColumnLogic logicToCopy) {
        nonogramColumnLogic.setColumnsSequencesRanges(logicToCopy.getColumnsSequencesRanges());
        nonogramColumnLogic.setColumnsFieldsNotToInclude(logicToCopy.getColumnsFieldsNotToInclude());
    }
}
