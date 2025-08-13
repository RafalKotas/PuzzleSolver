package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.RangeCorrectionHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range.SequenceRangeCorrectionFromColouredEdgesLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range.SequenceRangeCorrectionLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range.SequenceRangeCorrectionWhenMetColouredFieldsLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range.SequenceRangeCorrectionWhenMetXLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.solve.SequenceRangeCorrectionHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.solve.SequenceRangeCorrectionWhenMetXHelper;

import java.util.*;

import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.isFieldColoured;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.TooLongMergeFieldHelper.collectColouredSequencesRanges;

public class ColumnSequencesCorrectionHelperImpl implements ColumnSequencesCorrectionHelper, RefreshableColumnHelper {

    private final NonogramColumnLogic logic;

    public ColumnSequencesCorrectionHelperImpl(NonogramColumnLogic logic) {
        this.logic = logic;
    }

    @Override
    public void correctColumnSequencesRanges(int columnIdx) {
        List<List<Integer>> beforeRangesSnapshot = deepCopy(logic.getColumnsSequencesRanges().get(columnIdx));

        correctFromTop(columnIdx);
        correctFromBottom(columnIdx);

        List<List<Integer>> afterRangesSnapshot = logic.getColumnsSequencesRanges().get(columnIdx);

        if (rangesListNotEqual(beforeRangesSnapshot, afterRangesSnapshot)) {
            logic.getLogService().setTmpLog(SequenceRangeCorrectionLogHelper.generateLog(
                    columnIdx,
                    beforeRangesSnapshot,
                    afterRangesSnapshot,
                    logic.getNonogramRules().getColumnSequencesLengths().get(columnIdx),
                    logic.getColumnsFieldsNotToInclude().get(columnIdx),
                    logic.getColumnsSequencesIdsNotToInclude().get(columnIdx),
                    false
            ));
            logic.getLogService().addLog();
            logic.getNonogramState().increaseMadeSteps();

            Field columnField = new Field(0, columnIdx);
            logic.getActionScheduler().scheduleActionsBasedOnField(columnField, NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES);
        }
    }

    private void correctFromTop(int columnIdx) {
        List<Integer> lengths = logic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> ranges = logic.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> fieldsNotToInclude = logic.getColumnsFieldsNotToInclude().get(columnIdx);
        List<Integer> excludedIds = logic.getColumnsSequencesIdsNotToInclude().get(columnIdx);

        for (int seqIdx = 0; seqIdx < ranges.size() - 1; seqIdx++) {
            int nextIdx = seqIdx + 1;
            if (excludedIds.contains(nextIdx)) continue;

            List<Integer> updatedNext = excludedIds.contains(seqIdx)
                    ? SequenceRangeCorrectionHelper.calculateUpdatedNextSequenceRangeAfterExcludedSequence(ranges, fieldsNotToInclude, seqIdx, nextIdx)
                    : SequenceRangeCorrectionHelper.calculateUpdatedNextSequenceRangeAfterIncludedSequence(ranges, lengths, seqIdx, nextIdx);

            tryCorrectFromTop(columnIdx, lengths, ranges.get(nextIdx), updatedNext, nextIdx);
        }
    }

    private void tryCorrectFromTop(int columnIdx,
                                   List<Integer> lengths,
                                   List<Integer> oldRange,
                                   List<Integer> newRange,
                                   int idx) {
        if (!oldRange.get(0).equals(newRange.get(0))) {
            logic.updateColumnSequenceRange(columnIdx, idx, newRange);
            if (rangeLength(newRange) == lengths.get(idx) && logic.getBoardAccessHelper().isColumnRangeColoured(columnIdx, newRange)) {
                logic.excludeSequenceInColumn(columnIdx, idx);
            }
        }
    }

    private void correctFromBottom(int columnIdx) {
        List<Integer> lengths = logic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> ranges = logic.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> fieldsNotToInclude = logic.getColumnsFieldsNotToInclude().get(columnIdx);
        List<Integer> excludedIds = logic.getColumnsSequencesIdsNotToInclude().get(columnIdx);

        for (int seqIdx = ranges.size() - 1; seqIdx > 0; seqIdx--) {
            int prevIdx = seqIdx - 1;
            if (excludedIds.contains(prevIdx)) continue;

            List<Integer> updatedPrev = excludedIds.contains(seqIdx)
                    ? SequenceRangeCorrectionHelper.calculateUpdatedPreviousSequenceRangeAfterExcludedSequence(ranges, fieldsNotToInclude, seqIdx, prevIdx)
                    : SequenceRangeCorrectionHelper.calculateUpdatedPreviousSequenceRangeAfterIncludedSequence(ranges, lengths, seqIdx, prevIdx);

            tryCorrectFromBottom(columnIdx, lengths, ranges.get(prevIdx), updatedPrev, prevIdx);
        }
    }

    private void tryCorrectFromBottom(int columnIdx,
                                      List<Integer> lengths,
                                      List<Integer> oldRange,
                                      List<Integer> newRange,
                                      int idx) {
        if (!oldRange.get(1).equals(newRange.get(1))) {
            logic.updateColumnSequenceRange(columnIdx, idx, newRange);
            if (rangeLength(newRange) == lengths.get(idx) && logic.getBoardAccessHelper().isColumnRangeColoured(columnIdx, newRange)) {
                logic.excludeSequenceInColumn(columnIdx, idx);
            }
        }
    }

    @Override
    public void correctColumnSequencesRangesWhenMetColouredField(int columnIdx) {
        correctColumnSequencesRangesWhenMetColouredFieldFromTop(columnIdx);
        correctColumnSequencesRangesWhenMetColouredFieldFromBottom(columnIdx);
    }

    private void correctColumnSequencesRangesWhenMetColouredFieldFromTop(int columnIdx) {
        var ranges = logic.getColumnsSequencesRanges().get(columnIdx);
        var lengths = logic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);

        boolean changed = false;
        int seqId = 0;
        int rowIdx = 0;

        while (rowIdx < logic.getNonogramRules().getHeight() && seqId < lengths.size()) {
            int seqLength = lengths.get(seqId);
            Field field = new Field(rowIdx, columnIdx);

            if (!isFieldColoured(logic.getNonogramSolutionBoard(), field)) {
                rowIdx++;
                continue;
            }

            List<Integer> oldRange = ranges.get(seqId);
            List<Integer> updatedRange = RangeCorrectionHelper.updatedSequenceRangeWhenMetColouredField(
                    oldRange.get(0), oldRange.get(1), rowIdx, seqLength, true);

            if (!updatedRange.equals(oldRange)) {
                logic.getLogService().setTmpLog(SequenceRangeCorrectionWhenMetColouredFieldsLogHelper.generateLog(
                        columnIdx, seqId, ranges, updatedRange,
                        logic.getBoardAccessHelper().getColumnCopy(columnIdx), lengths, "fromTop"
                ));
                logic.getLogService().addLog();
                logic.updateColumnSequenceRange(columnIdx, seqId, updatedRange);

                if (rangeLength(updatedRange) == seqLength &&
                        logic.getBoardAccessHelper().isColumnRangeColoured(columnIdx, updatedRange)) {
                    logic.excludeSequenceInColumn(columnIdx, seqId);
                }

                changed = true;
            }

            rowIdx += seqLength;
            seqId++;
        }

        if (changed) {
            logic.getNonogramState().increaseMadeSteps();
            logic.getActionScheduler().scheduleActionsBasedOnField(new Field(0, columnIdx),
                    NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS);
        }
    }

    private void correctColumnSequencesRangesWhenMetColouredFieldFromBottom(int columnIdx) {
        var ranges = logic.getColumnsSequencesRanges().get(columnIdx);
        var lengths = logic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);

        boolean changed = false;
        int seqId = lengths.size() - 1;
        int rowIdx = logic.getNonogramRules().getHeight() - 1;

        while (rowIdx >= 0 && seqId >= 0) {
            int seqLength = lengths.get(seqId);
            Field field = new Field(rowIdx, columnIdx);

            if (!isFieldColoured(logic.getNonogramSolutionBoard(), field)) {
                rowIdx--;
                continue;
            }

            List<Integer> oldRange = ranges.get(seqId);
            List<Integer> updatedRange = RangeCorrectionHelper.updatedSequenceRangeWhenMetColouredField(
                    oldRange.get(0), oldRange.get(1), rowIdx, seqLength, false);

            if (!updatedRange.equals(oldRange)) {
                logic.getLogService().setTmpLog(SequenceRangeCorrectionWhenMetColouredFieldsLogHelper.generateLog(
                        columnIdx, seqId, ranges, updatedRange,
                        logic.getBoardAccessHelper().getColumnCopy(columnIdx), lengths, "fromBottom"
                ));
                logic.getLogService().addLog();
                logic.updateColumnSequenceRange(columnIdx, seqId, updatedRange);

                if (rangeLength(updatedRange) == seqLength &&
                        logic.getBoardAccessHelper().isColumnRangeColoured(columnIdx, updatedRange)) {
                    logic.excludeSequenceInColumn(columnIdx, seqId);
                }

                changed = true;
            }

            rowIdx -= seqLength;
            seqId--;
        }

        if (changed) {
            logic.getNonogramState().increaseMadeSteps();
            logic.getActionScheduler().scheduleActionsBasedOnField(new Field(0, columnIdx),
                    NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS);
        }
    }

    @Override
    public void correctColumnSequencesRangesIfXOnWay(int columnIdx, boolean changeLogicDetails) {
        boolean changed = false;

        List<Integer> lengths = logic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> ranges = logic.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> excluded = logic.getColumnsSequencesIdsNotToInclude().get(columnIdx);

        List<List<Integer>> beforeSnapshot = ranges.stream()
                .map(range -> List.of(range.get(0), range.get(1)))
                .toList();

        for (int seqIdx = 0; seqIdx < ranges.size(); seqIdx++) {
            if (excluded.contains(seqIdx)) continue;

            List<Integer> currentRange = ranges.get(seqIdx);
            int length = lengths.get(seqIdx);

            List<Integer> corrected = SequenceRangeCorrectionWhenMetXHelper.calculateCorrectedRangeWithoutX(
                    currentRange, length, columnIdx, true, logic.getNonogramSolutionBoard());

            if (!currentRange.equals(corrected)) {
                changed = true;
                logic.updateColumnSequenceRange(columnIdx, seqIdx, corrected);

                if (changeLogicDetails && shouldExcludeSequence(columnIdx, corrected, length)) {
                    logic.excludeSequenceInColumn(columnIdx, seqIdx);
                }
            }
        }

        if (changed && changeLogicDetails) {
            List<List<Integer>> afterSnapshot = logic.getColumnsSequencesRanges().get(columnIdx);

            logic.getLogService().setTmpLog(SequenceRangeCorrectionWhenMetXLogHelper.generateLog(
                    columnIdx, beforeSnapshot, afterSnapshot, lengths, excluded, false));
            logic.getLogService().addLog();

            logic.getNonogramState().increaseMadeSteps();
            logic.getActionScheduler().scheduleActionsBasedOnField(
                    new Field(0, columnIdx), NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY);
        }
    }

    private boolean shouldExcludeSequence(int columnIdx, List<Integer> range, int length) {
        return rangeLength(range) == length && logic.getBoardAccessHelper().isColumnRangeColoured(columnIdx, range);
    }

    @Override
    public void correctColumnSequencesRangesWhenMatchingFieldsToSequences(int columnIdx) {
        List<List<Integer>> columnSequencesRanges = logic.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequencesLengths = logic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> colouredRanges = collectColouredSequencesRanges(logic.getNonogramSolutionBoard(), columnIdx, false);

        boolean hasChangedGlobal = false;
        boolean hasChanged;

        do {
            hasChanged = processDirection(colouredRanges, columnSequencesRanges, columnSequencesLengths, true); // TOP -> BOTTOM
            hasChanged |= processDirection(colouredRanges, columnSequencesRanges, columnSequencesLengths, false); // BOTTOM → TOP

            hasChangedGlobal |= hasChanged;
        } while (hasChanged);

        if (hasChangedGlobal) {
            Field columnField = new Field(0, columnIdx);
            logic.getActionScheduler().scheduleActionsBasedOnField(columnField, NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES);
            logic.getNonogramState().increaseMadeSteps();
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

        List<List<Integer>> ranges = logic.getColumnsSequencesRanges().get(columnIdx);
        List<List<Integer>> before = deepCopy(ranges);

        for (int seqIdx = 0; seqIdx < ranges.size(); seqIdx++) {
            List<Integer> currentRange = ranges.get(seqIdx);

            List<Integer> updatedRange = RangeCorrectionHelper.adjustRangeIfColouredAtEdges(
                    currentRange,
                    columnIdx,
                    true, // isColumn == true
                    logic.getNonogramSolutionBoard(),
                    logic.getNonogramRules().getHeight()
            );

            if (!updatedRange.equals(currentRange)) {
                logic.updateColumnSequenceRange(columnIdx, seqIdx, updatedRange);
                logic.getNonogramState().increaseMadeSteps();
                anyUpdated = true;
            }
        }

        if (anyUpdated) {
            List<List<Integer>> after = logic.getColumnsSequencesRanges().get(columnIdx);
            List<Integer> lengths = logic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
            List<String> line = logic.getNonogramBoardColumn(columnIdx);

            String tmpLog = SequenceRangeCorrectionFromColouredEdgesLogHelper.generateLog(
                    columnIdx,
                    before,
                    after,
                    lengths,
                    line,
                    false // isRow == false
            );
            logic.getLogService().setTmpLog(tmpLog);
            logic.getLogService().addLog();

            Field columnField = new Field(0, columnIdx);
            logic.getActionScheduler().scheduleActionsBasedOnField(columnField, NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE);
        }
    }

    @Override
    public void refreshFrom(NonogramColumnLogic logicToCopy) {
        logic.setColumnsSequencesRanges(logicToCopy.getColumnsSequencesRanges());
        logic.setColumnsFieldsNotToInclude(logicToCopy.getColumnsFieldsNotToInclude());
    }
}
