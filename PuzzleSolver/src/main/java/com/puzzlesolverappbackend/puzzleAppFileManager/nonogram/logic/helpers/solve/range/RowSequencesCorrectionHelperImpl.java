package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve.range;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.Field;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.range.*;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve.RangeCorrectionHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.NonogramRowLogic;

import java.util.*;
import java.util.stream.Collectors;

import static com.puzzlesolverappbackend.puzzleAppFileManager.common.ArrayUtils.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve.TooLongMergeFieldHelper.collectColouredSequencesRanges;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramBoardUtils.isFieldColoured;

public class RowSequencesCorrectionHelperImpl implements RowSequencesCorrectionHelper {

    private final NonogramRowLogic nonogramRowLogic;

    public RowSequencesCorrectionHelperImpl(NonogramRowLogic nonogramRowLogic) {
        this.nonogramRowLogic = nonogramRowLogic;
    }

    @Override
    public void correctRowSequencesRanges(int rowIdx) {
        List<List<Integer>> beforeRangesSnapshot = deepCopy(nonogramRowLogic.getRowsSequencesRanges().get(rowIdx));

        correctFromLeft(rowIdx);
        correctFromRight(rowIdx);

        List<List<Integer>> afterRangesSnapshot = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);

        if (ranglesListNotEqual(beforeRangesSnapshot, afterRangesSnapshot)) {
            nonogramRowLogic.getLogService().setTmpLog(SequenceRangeCorrectionLogHelper.generateLog(
                    rowIdx,
                    beforeRangesSnapshot,
                    afterRangesSnapshot,
                    nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx),
                    nonogramRowLogic.getRowsFieldsNotToInclude().get(rowIdx),
                    nonogramRowLogic.getRowsSequencesIdsNotToInclude().get(rowIdx),
                    true
            ));
            nonogramRowLogic.getLogService().addLog();
            nonogramRowLogic.getNonogramState().increaseMadeSteps();

            Field rowField = new Field(rowIdx, 0);
            nonogramRowLogic.getActionScheduler().scheduleActionsBasedOnField(rowField, NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES);
        }
    }

    private void correctFromLeft(int rowIdx) {
        List<Integer> lengths = nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> ranges = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);
        List<Integer> fieldsNotToInclude = nonogramRowLogic.getRowsFieldsNotToInclude().get(rowIdx);
        List<Integer> excludedIds = nonogramRowLogic.getRowsSequencesIdsNotToInclude().get(rowIdx);

        for (int seqIdx = 0; seqIdx < ranges.size() - 1; seqIdx++) {
            int nextIdx = seqIdx + 1;
            if (excludedIds.contains(nextIdx)) continue;

            List<Integer> updatedNext = excludedIds.contains(seqIdx)
                    ? SequenceRangeCorrectionHelper.calculateUpdatedNextSequenceRangeAfterExcludedSequence(ranges, fieldsNotToInclude, seqIdx, nextIdx)
                    : SequenceRangeCorrectionHelper.calculateUpdatedNextSequenceRangeAfterIncludedSequence(ranges, lengths, seqIdx, nextIdx);

            tryCorrectFromLeft(rowIdx, lengths, ranges.get(nextIdx), updatedNext, nextIdx);
        }
    }

    private void tryCorrectFromLeft(int rowIdx,
                                    List<Integer> lengths,
                                    List<Integer> oldRange,
                                    List<Integer> newRange,
                                    int idx) {
        if (!oldRange.get(0).equals(newRange.get(0))) {
            nonogramRowLogic.updateRowSequenceRange(rowIdx, idx, newRange);
            if (rangeLength(newRange) == lengths.get(idx) && nonogramRowLogic.getBoardAccessHelper().isRowRangeColoured(rowIdx, newRange)) {
                nonogramRowLogic.excludeSequenceInRow(rowIdx, idx);
            }
        }
    }

    private void correctFromRight(int rowIdx) {
        List<Integer> lengths = nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> ranges = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);
        List<Integer> fieldsNotToInclude = nonogramRowLogic.getRowsFieldsNotToInclude().get(rowIdx);
        List<Integer> excludedIds = nonogramRowLogic.getRowsSequencesIdsNotToInclude().get(rowIdx);

        for (int seqIdx = ranges.size() - 1; seqIdx > 0; seqIdx--) {
            int prevIdx = seqIdx - 1;
            if (excludedIds.contains(prevIdx)) continue;

            List<Integer> updatedPrev = excludedIds.contains(seqIdx)
                    ? SequenceRangeCorrectionHelper.calculateUpdatedPreviousSequenceRangeAfterExcludedSequence(ranges, fieldsNotToInclude, seqIdx, prevIdx)
                    : SequenceRangeCorrectionHelper.calculateUpdatedPreviousSequenceRangeAfterIncludedSequence(ranges, lengths, seqIdx, prevIdx);

            tryCorrectFromRight(rowIdx, lengths, ranges.get(prevIdx), updatedPrev, prevIdx);
        }
    }

    private void tryCorrectFromRight(int rowIdx,
                                     List<Integer> lengths,
                                     List<Integer> oldRange,
                                     List<Integer> newRange,
                                     int idx) {
        if (!oldRange.get(1).equals(newRange.get(1))) {
            nonogramRowLogic.updateRowSequenceRange(rowIdx, idx, newRange);
            if (rangeLength(newRange) == lengths.get(idx) && nonogramRowLogic.getBoardAccessHelper().isRowRangeColoured(rowIdx, newRange)) {
                nonogramRowLogic.excludeSequenceInRow(rowIdx, idx);
            }
        }
    }

    @Override
    public void correctRowSequencesRangesWhenMetColouredField(int rowIdx) {
        correctRowSequencesRangesWhenMetColouredFieldFromLeft(rowIdx);
        correctRowSequencesRangesWhenMetColouredFieldFromRight(rowIdx);
    }

    private void correctRowSequencesRangesWhenMetColouredFieldFromLeft(int rowIdx) {
        var ranges = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);
        var lengths = nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        boolean changed = false;

        int seqId = 0;
        int seqLength = lengths.get(seqId);

        for (int colIdx = 0; colIdx < nonogramRowLogic.getNonogramRules().getWidth(); colIdx++) {
            Field field = new Field(rowIdx, colIdx);
            if (!isFieldColoured(nonogramRowLogic.getNonogramSolutionBoard(), field)) continue;

            List<Integer> oldRange = ranges.get(seqId);
            List<Integer> updatedRange = RangeCorrectionHelper.updatedSequenceRangeWhenMetColouredField(
                    oldRange.get(0), oldRange.get(1), colIdx, seqLength, true);

            if (!updatedRange.equals(oldRange)) {
                nonogramRowLogic.getLogService().setTmpLog(SequenceRangeCorrectionWhenMetColouredFieldsLogHelper.generateLog(
                        rowIdx, seqId, ranges, updatedRange, nonogramRowLogic.getNonogramSolutionBoard().get(rowIdx), lengths, true, "fromLeft"
                ));
                nonogramRowLogic.getLogService().addLog();
                nonogramRowLogic.updateRowSequenceRange(rowIdx, seqId, updatedRange);

                if (rangeLength(updatedRange) == seqLength && nonogramRowLogic.getBoardAccessHelper().isRowRangeColoured(rowIdx, updatedRange)) {
                    nonogramRowLogic.excludeSequenceInRow(rowIdx, seqId);
                }

                changed = true;
            }

            colIdx += seqLength;
            if (++seqId >= lengths.size()) break;
            seqLength = lengths.get(seqId);
        }

        if (changed) {
            nonogramRowLogic.getNonogramState().increaseMadeSteps();
            nonogramRowLogic.getActionScheduler().scheduleActionsBasedOnField(new Field(rowIdx, 0), NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS);
        }
    }

    private void correctRowSequencesRangesWhenMetColouredFieldFromRight(int rowIdx) {
        var ranges = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);
        var lengths = nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        boolean changed = false;

        int seqId = lengths.size() - 1;
        int seqLength = lengths.get(seqId);

        for (int colIdx = nonogramRowLogic.getNonogramRules().getWidth() - 1; colIdx >= 0; colIdx--) {
            Field field = new Field(rowIdx, colIdx);
            if (!isFieldColoured(nonogramRowLogic.getNonogramSolutionBoard(), field)) continue;

            List<Integer> oldRange = ranges.get(seqId);
            List<Integer> updatedRange = RangeCorrectionHelper.updatedSequenceRangeWhenMetColouredField(
                    oldRange.get(0), oldRange.get(1), colIdx, seqLength, false);

            if (!updatedRange.equals(oldRange)) {
                nonogramRowLogic.getLogService().setTmpLog(SequenceRangeCorrectionWhenMetColouredFieldsLogHelper.generateLog(
                        rowIdx, seqId, ranges, updatedRange, nonogramRowLogic.getNonogramSolutionBoard().get(rowIdx), lengths, true, "fromRight"
                ));
                nonogramRowLogic.getLogService().addLog();
                nonogramRowLogic.updateRowSequenceRange(rowIdx, seqId, updatedRange);

                if (rangeLength(updatedRange) == seqLength && nonogramRowLogic.getBoardAccessHelper().isRowRangeColoured(rowIdx, updatedRange)) {
                    nonogramRowLogic.excludeSequenceInRow(rowIdx, seqId);
                }

                changed = true;
            }

            colIdx -= seqLength;
            if (--seqId < 0) break;
            seqLength = lengths.get(seqId);
        }

        if (changed) {
            nonogramRowLogic.getNonogramState().increaseMadeSteps();
            nonogramRowLogic.getActionScheduler().scheduleActionsBasedOnField(new Field(rowIdx, 0), NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS);
        }
    }

    @Override
    public void correctRowSequencesRangesIfXOnWay(int rowIdx, boolean changeLogicDetails) {
        boolean changed = false;

        List<Integer> lengths = nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> ranges = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);
        List<Integer> excluded = nonogramRowLogic.getRowsSequencesIdsNotToInclude().get(rowIdx);

        List<List<Integer>> beforeSnapshot = ranges.stream()
                .map(r -> List.of(r.get(0), r.get(1)))
                .collect(Collectors.toList());

        for (int seqIdx = 0; seqIdx < ranges.size(); seqIdx++) {
            if (excluded.contains(seqIdx)) continue;

            List<Integer> currentRange = ranges.get(seqIdx);
            int length = lengths.get(seqIdx);

            List<Integer> corrected = SequenceRangeCorrectionWhenMetXHelper.calculateCorrectedRangeWithoutX(
                    currentRange, length, rowIdx, false, nonogramRowLogic.getNonogramSolutionBoard());

            if (!currentRange.equals(corrected)) {
                changed = true;
                nonogramRowLogic.updateRowSequenceRange(rowIdx, seqIdx, corrected);

                if (changeLogicDetails && shouldExcludeSequence(rowIdx, corrected, length)) {
                    nonogramRowLogic.excludeSequenceInRow(rowIdx, seqIdx);
                }
            }
        }

        if (changed && changeLogicDetails) {
            List<List<Integer>> afterSnapshot = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);
            nonogramRowLogic.getLogService().setTmpLog(SequenceRangeCorrectionWhenMetXLogHelper.generateLog(
                    rowIdx, beforeSnapshot, afterSnapshot, lengths, excluded, true));
            nonogramRowLogic.getLogService().addLog();
            nonogramRowLogic.getNonogramState().increaseMadeSteps();
            nonogramRowLogic.getActionScheduler().scheduleActionsBasedOnField(
                    new Field(rowIdx, 0), NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY);
        }
    }

    private boolean shouldExcludeSequence(int rowIdx, List<Integer> range, int length) {
        return rangeLength(range) == length && nonogramRowLogic.getBoardAccessHelper().isRowRangeColoured(rowIdx, range);
    }

    @Override
    public void correctRowSequencesRangesWhenMatchingFieldsToSequences(int rowIdx) {
        List<List<Integer>> rowSequencesRanges =  nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths =  nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> colouredRanges = collectColouredSequencesRanges(nonogramRowLogic.getNonogramSolutionBoard(), rowIdx, true);

        boolean hasChangedGlobal = false;
        boolean hasChanged;

        do {
            hasChanged = false;

            Map<Integer, List<Integer>> colouredToSequencesLeft = new HashMap<>();
            for (int i = 0; i < colouredRanges.size(); i++) {
                List<Integer> coloured = colouredRanges.get(i);
                int colouredLen = rangeLength(coloured);
                List<Integer> possible = new ArrayList<>();

                for (int seqIdx = 0; seqIdx < rowSequencesRanges.size(); seqIdx++) {
                    List<Integer> seqRange = rowSequencesRanges.get(seqIdx);
                    int seqLen = rowSequencesLengths.get(seqIdx);
                    if (rangeInsideAnotherRange(coloured, seqRange) && seqLen >= colouredLen) {
                        possible.add(seqIdx);
                    }
                }

                colouredToSequencesLeft.put(i, possible);
            }

            int maxAssigned = -1;
            for (int i = 0; i < colouredRanges.size(); i++) {
                List<Integer> possible = colouredToSequencesLeft.get(i);
                if (possible == null || possible.isEmpty()) continue;

                List<Integer> filtered = new ArrayList<>();
                for (int seqId : possible) {
                    if (seqId >= maxAssigned) {
                        filtered.add(seqId);
                    }
                }

                colouredToSequencesLeft.put(i, filtered);
                if (filtered.size() == 1) {
                    maxAssigned = filtered.get(0);
                }
            }

            for (Map.Entry<Integer, List<Integer>> entry : colouredToSequencesLeft.entrySet()) {
                List<Integer> possible = entry.getValue();
                if (possible == null || possible.isEmpty()) continue;

                int seqIdx = Collections.min(possible);
                List<Integer> seqRange = rowSequencesRanges.get(seqIdx);
                int seqLength = rowSequencesLengths.get(seqIdx);
                List<Integer> coloured = colouredRanges.get(entry.getKey());

                int newStart = coloured.get(1) - seqLength + 1;
                int newEnd = coloured.get(0) + seqLength - 1;

                int oldStart = seqRange.get(0);
                int oldEnd = seqRange.get(1);

                boolean isCertain = possible.size() == 1;
                int updatedStart = isCertain ? Math.max(newStart, oldStart) : oldStart;
                int updatedEnd = Math.min(newEnd, oldEnd);

                if (rangeInsideAnotherRange(coloured, seqRange) && newStart <= newEnd
                        && (updatedStart != oldStart || updatedEnd != oldEnd)) {
                    seqRange.set(0, updatedStart);
                    seqRange.set(1, updatedEnd);
                    hasChanged = true;
                    hasChangedGlobal = true;
                }
            }

            Map<Integer, List<Integer>> colouredToSequencesRight = new HashMap<>();
            for (int i = colouredRanges.size() - 1; i >= 0; i--) {
                List<Integer> coloured = colouredRanges.get(i);
                int colouredLen = rangeLength(coloured);
                List<Integer> possible = new ArrayList<>();

                for (int seqIdx = rowSequencesRanges.size() - 1; seqIdx >= 0; seqIdx--) {
                    List<Integer> seqRange = rowSequencesRanges.get(seqIdx);
                    int seqLen = rowSequencesLengths.get(seqIdx);
                    if (rangeInsideAnotherRange(coloured, seqRange) && seqLen >= colouredLen) {
                        possible.add(seqIdx);
                    }
                }

                colouredToSequencesRight.put(i, possible);
            }

            int minAssigned = rowSequencesRanges.size();
            for (int i = colouredRanges.size() - 1; i >= 0; i--) {
                List<Integer> possible = colouredToSequencesRight.get(i);
                if (possible == null || possible.isEmpty()) continue;

                List<Integer> filtered = new ArrayList<>();
                for (int seqId : possible) {
                    if (seqId <= minAssigned) {
                        filtered.add(seqId);
                    }
                }

                colouredToSequencesRight.put(i, filtered);
                if (filtered.size() == 1) {
                    minAssigned = filtered.get(0);
                }
            }

            for (Map.Entry<Integer, List<Integer>> entry : colouredToSequencesRight.entrySet()) {
                List<Integer> possible = entry.getValue();
                if (possible == null || possible.isEmpty()) continue;

                int seqIdx = Collections.max(possible);
                List<Integer> seqRange = rowSequencesRanges.get(seqIdx);
                int seqLength = rowSequencesLengths.get(seqIdx);
                List<Integer> coloured = colouredRanges.get(entry.getKey());

                int newStart = coloured.get(1) - seqLength + 1;
                int newEnd = coloured.get(0) + seqLength - 1;

                int oldStart = seqRange.get(0);
                int oldEnd = seqRange.get(1);

                boolean isCertain = possible.size() == 1;
                int updatedEnd = isCertain ? Math.min(newEnd, oldEnd) : oldEnd;
                int updatedStart = Math.max(newStart, oldStart);

                if (rangeInsideAnotherRange(coloured, seqRange) && newStart <= newEnd
                        && (updatedStart != oldStart || updatedEnd != oldEnd)) {
                    seqRange.set(0, updatedStart);
                    seqRange.set(1, updatedEnd);
                    hasChanged = true;
                    hasChangedGlobal = true;
                }
            }

        } while (hasChanged);

        if (hasChangedGlobal) {
            Field rowField = new Field(rowIdx, 0);
            nonogramRowLogic.getActionScheduler().scheduleActionsBasedOnField(rowField, NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES);
            nonogramRowLogic.getNonogramState().increaseMadeSteps();
        }
    }

    @Override
    public void correctRowSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(int rowIdx) {
        boolean anyUpdated = false;

        List<List<Integer>> ranges = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);
        List<List<Integer>> before = deepCopy(ranges);

        for (int seqIdx = 0; seqIdx < ranges.size(); seqIdx++) {
            List<Integer> currentRange = ranges.get(seqIdx);

            List<Integer> updatedRange = RangeCorrectionHelper.adjustRangeIfColouredAtEdges(
                    currentRange,
                    rowIdx,
                    false, // isColumn == false → we're in row
                    nonogramRowLogic.getNonogramSolutionBoard(),
                    nonogramRowLogic.getNonogramRules().getWidth()
            );

            if (!updatedRange.equals(currentRange)) {
                nonogramRowLogic.updateRowSequenceRange(rowIdx, seqIdx, updatedRange);
                nonogramRowLogic.getNonogramState().increaseMadeSteps();
                anyUpdated = true;
            }
        }

        if (anyUpdated) {
            List<List<Integer>> after = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);
            List<Integer> lengths = nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
            List<String> line = nonogramRowLogic.getNonogramSolutionBoard().get(rowIdx);


            String tmpLog = SequenceRangeCorrectionFromColouredEdgesLogHelper.generateLog(
                    rowIdx,
                    before,
                    after,
                    lengths,
                    line,
                    true // isRow
            );
            nonogramRowLogic.getLogService().setTmpLog(tmpLog);
            nonogramRowLogic.getLogService().addLog();

            Field rowField = new Field(rowIdx, 0);
            nonogramRowLogic.getActionScheduler().scheduleActionsBasedOnField(rowField, NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE);
        }
    }
}

