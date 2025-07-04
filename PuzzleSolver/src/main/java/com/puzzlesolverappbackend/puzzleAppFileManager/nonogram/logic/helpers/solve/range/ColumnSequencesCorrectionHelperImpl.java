package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve.range;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.Field;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.NonogramColumnLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.range.*;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve.RangeCorrectionHelper;

import java.util.*;
import java.util.stream.Collectors;

import static com.puzzlesolverappbackend.puzzleAppFileManager.common.ArrayUtils.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve.TooLongMergeFieldHelper.collectColouredSequencesRanges;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramBoardUtils.isFieldColoured;

public class ColumnSequencesCorrectionHelperImpl implements ColumnSequencesCorrectionHelper {

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

        if (ranglesListNotEqual(beforeRangesSnapshot, afterRangesSnapshot)) {
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
        int seqLength = lengths.get(seqId);

        for (int rowIdx = 0; rowIdx < logic.getNonogramRules().getHeight(); rowIdx++) {
            Field field = new Field(rowIdx, columnIdx);
            if (!isFieldColoured(logic.getNonogramSolutionBoard(), field)) continue;

            List<Integer> oldRange = ranges.get(seqId);
            List<Integer> updatedRange = RangeCorrectionHelper.updatedSequenceRangeWhenMetColouredField(
                    oldRange.get(0), oldRange.get(1), rowIdx, seqLength, true);

            if (!updatedRange.equals(oldRange)) {
                logic.getLogService().setTmpLog(SequenceRangeCorrectionWhenMetColouredFieldsLogHelper.generateLog(
                        columnIdx, seqId, ranges, updatedRange, logic.getBoardAccessHelper().getColumnCopy(columnIdx),
                        lengths, false, "fromTop"
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
            if (++seqId >= lengths.size()) break;
            seqLength = lengths.get(seqId);
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
        int seqLength = lengths.get(seqId);

        for (int rowIdx = logic.getNonogramRules().getHeight() - 1; rowIdx >= 0; rowIdx--) {
            Field field = new Field(rowIdx, columnIdx);
            if (!isFieldColoured(logic.getNonogramSolutionBoard(), field)) continue;

            List<Integer> oldRange = ranges.get(seqId);
            List<Integer> updatedRange = RangeCorrectionHelper.updatedSequenceRangeWhenMetColouredField(
                    oldRange.get(0), oldRange.get(1), rowIdx, seqLength, false);

            if (!updatedRange.equals(oldRange)) {
                logic.getLogService().setTmpLog(SequenceRangeCorrectionWhenMetColouredFieldsLogHelper.generateLog(
                        columnIdx, seqId, ranges, updatedRange, logic.getBoardAccessHelper().getColumnCopy(columnIdx),
                        lengths, false, "fromBottom"
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
            if (--seqId < 0) break;
            seqLength = lengths.get(seqId);
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
                .collect(Collectors.toList());

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
            hasChanged = false;

            // TOP → BOTTOM
            Map<Integer, List<Integer>> colouredToSeqsTop = new HashMap<>();
            for (int i = 0; i < colouredRanges.size(); i++) {
                List<Integer> coloured = colouredRanges.get(i);
                int colouredLen = rangeLength(coloured);

                List<Integer> possible = new ArrayList<>();
                for (int seqIdx = 0; seqIdx < columnSequencesRanges.size(); seqIdx++) {
                    List<Integer> seqRange = columnSequencesRanges.get(seqIdx);
                    int seqLen = columnSequencesLengths.get(seqIdx);

                    if (rangeInsideAnotherRange(coloured, seqRange) && seqLen >= colouredLen) {
                        possible.add(seqIdx);
                    }
                }
                colouredToSeqsTop.put(i, possible);
            }

            int maxAssigned = -1;
            for (int i = 0; i < colouredRanges.size(); i++) {
                List<Integer> possible = colouredToSeqsTop.get(i);
                if (possible == null || possible.isEmpty()) continue;

                List<Integer> filtered = new ArrayList<>();
                for (int seqId : possible) {
                    if (seqId >= maxAssigned) {
                        filtered.add(seqId);
                    }
                }

                colouredToSeqsTop.put(i, filtered);
                if (filtered.size() == 1) {
                    maxAssigned = filtered.get(0);
                }
            }

            for (Map.Entry<Integer, List<Integer>> entry : colouredToSeqsTop.entrySet()) {
                List<Integer> possible = entry.getValue();
                if (possible == null || possible.isEmpty()) continue;

                int seqIdx = Collections.min(possible);
                List<Integer> seqRange = columnSequencesRanges.get(seqIdx);
                int seqLen = columnSequencesLengths.get(seqIdx);

                List<Integer> coloured = colouredRanges.get(entry.getKey());
                int newStart = coloured.get(1) - seqLen + 1;
                int newEnd = coloured.get(0) + seqLen - 1;

                int oldStart = seqRange.get(0);
                int oldEnd = seqRange.get(1);

                boolean certain = possible.size() == 1;
                int updatedStart = certain ? Math.max(newStart, oldStart) : oldStart;
                int updatedEnd = Math.min(newEnd, oldEnd);

                boolean inside = rangeInsideAnotherRange(coloured, seqRange);
                boolean valid = newStart <= newEnd;

                if (inside && valid && (updatedStart != oldStart || updatedEnd != oldEnd)) {
                    seqRange.set(0, updatedStart);
                    seqRange.set(1, updatedEnd);
                    hasChanged = true;
                    hasChangedGlobal = true;
                }
            }

            // BOTTOM -> TOP
            Map<Integer, List<Integer>> colouredToSeqsBottom = new HashMap<>();
            for (int i = colouredRanges.size() - 1; i >= 0; i--) {
                List<Integer> coloured = colouredRanges.get(i);
                int colouredLen = rangeLength(coloured);

                List<Integer> possible = new ArrayList<>();
                for (int seqIdx = columnSequencesRanges.size() - 1; seqIdx >= 0; seqIdx--) {
                    List<Integer> seqRange = columnSequencesRanges.get(seqIdx);
                    int seqLen = columnSequencesLengths.get(seqIdx);

                    if (rangeInsideAnotherRange(coloured, seqRange) && seqLen >= colouredLen) {
                        possible.add(seqIdx);
                    }
                }
                colouredToSeqsBottom.put(i, possible);
            }

            int minAssigned = columnSequencesRanges.size();
            for (int i = colouredRanges.size() - 1; i >= 0; i--) {
                List<Integer> possible = colouredToSeqsBottom.get(i);
                if (possible == null || possible.isEmpty()) continue;

                List<Integer> filtered = new ArrayList<>();
                for (int seqId : possible) {
                    if (seqId <= minAssigned) {
                        filtered.add(seqId);
                    }
                }

                colouredToSeqsBottom.put(i, filtered);
                if (filtered.size() == 1) {
                    minAssigned = filtered.get(0);
                }
            }

            for (Map.Entry<Integer, List<Integer>> entry : colouredToSeqsBottom.entrySet()) {
                List<Integer> possible = entry.getValue();
                if (possible == null || possible.isEmpty()) continue;

                int seqIdx = Collections.max(possible);
                List<Integer> seqRange = columnSequencesRanges.get(seqIdx);
                int seqLen = columnSequencesLengths.get(seqIdx);

                List<Integer> coloured = colouredRanges.get(entry.getKey());
                int newStart = coloured.get(1) - seqLen + 1;
                int newEnd = coloured.get(0) + seqLen - 1;

                int oldStart = seqRange.get(0);
                int oldEnd = seqRange.get(1);

                boolean certain = possible.size() == 1;
                int updatedEnd = certain ? Math.min(newEnd, oldEnd) : oldEnd;
                int updatedStart = Math.max(newStart, oldStart);

                boolean inside = rangeInsideAnotherRange(coloured, seqRange);
                boolean valid = newStart <= newEnd;

                if (inside && valid && (updatedStart != oldStart || updatedEnd != oldEnd)) {
                    seqRange.set(0, updatedStart);
                    seqRange.set(1, updatedEnd);
                    hasChanged = true;
                    hasChangedGlobal = true;
                }
            }

        } while (hasChanged);

        if (hasChangedGlobal) {
            Field columnField = new Field(0, columnIdx);
            logic.getActionScheduler().scheduleActionsBasedOnField(columnField, NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES);
            logic.getNonogramState().increaseMadeSteps();
        }
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

}
