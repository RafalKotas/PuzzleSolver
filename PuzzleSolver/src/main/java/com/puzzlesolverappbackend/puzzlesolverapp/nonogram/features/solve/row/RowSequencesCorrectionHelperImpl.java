package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row;

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

public class RowSequencesCorrectionHelperImpl extends CommonRangeCorrectionHelper implements RowSequencesCorrectionHelper, RefreshableRowHelper {

    private final NonogramRowLogic nonogramRowLogic;

    public RowSequencesCorrectionHelperImpl(NonogramRowLogic nonogramRowLogic) {
        this.nonogramRowLogic = nonogramRowLogic;
    }

    @Override
    public void correctRowSequencesRanges(int rowIdx) {
        List<List<Integer>> initialRanges = deepCopy(nonogramRowLogic.getRowsSequencesRanges().get(rowIdx));

        boolean anyUpdated = false;

        anyUpdated |= correctFromLeft(rowIdx);
        anyUpdated |= correctFromRight(rowIdx);

        List<List<Integer>> finalRanges = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);

        if (anyUpdated) {
            nonogramRowLogic.getNonogramState().increaseMadeSteps();

            Field rowField = new Field(rowIdx, 0);
            nonogramRowLogic.getActionScheduler().scheduleActionsBasedOnField(rowField, NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_ROW);

            String tmpLog = SequencesRangesCorrectionLogHelper.generateLog(
                    true,
                    rowIdx,
                    nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx),
                    nonogramRowLogic.getRowsFieldsNotToInclude().get(rowIdx),
                    nonogramRowLogic.getRowsSequencesIdsNotToInclude().get(rowIdx),
                    initialRanges,
                    finalRanges
            );
            nonogramRowLogic.setTmpLog(tmpLog);
            nonogramRowLogic.addLog();
        }
    }

    private boolean correctFromLeft(int rowIdx) {
        List<Integer> lengths = nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> ranges = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);
        List<Integer> fieldsNotToInclude = nonogramRowLogic.getRowsFieldsNotToInclude().get(rowIdx);
        List<Integer> excludedIds = nonogramRowLogic.getRowsSequencesIdsNotToInclude().get(rowIdx);

        boolean anyUpdated = false;

        for (int seqIdx = 0; seqIdx < ranges.size() - 1; seqIdx++) {
            int nextIdx = seqIdx + 1;
            if (excludedIds.contains(nextIdx)) continue;

            List<Integer> updatedNext = excludedIds.contains(seqIdx)
                    ? SequenceRangeCorrectionHelper.calculateUpdatedNextSequenceRangeAfterExcludedSequence(ranges, fieldsNotToInclude, seqIdx, nextIdx)
                    : SequenceRangeCorrectionHelper.calculateUpdatedNextSequenceRangeAfterIncludedSequence(ranges, lengths, seqIdx, nextIdx);

            anyUpdated |= tryCorrectFromLeft(rowIdx, lengths, ranges.get(nextIdx), updatedNext, nextIdx);
        }

        return anyUpdated;
    }

    private boolean tryCorrectFromLeft(int rowIdx,
                                    List<Integer> lengths,
                                    List<Integer> oldRange,
                                    List<Integer> newRange,
                                    int idx) {
        if (!oldRange.get(0).equals(newRange.get(0))) {
            nonogramRowLogic.updateRowSequenceRange(rowIdx, idx, newRange);
            if (rangeLength(newRange) == lengths.get(idx) && nonogramRowLogic.getBoardAccessHelper().isRowRangeColoured(rowIdx, newRange)) {
                nonogramRowLogic.excludeSequenceInRow(rowIdx, idx);
            }

            return true;
        }

        return false;
    }

    private boolean correctFromRight(int rowIdx) {
        List<Integer> lengths = nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> ranges = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);
        List<Integer> fieldsNotToInclude = nonogramRowLogic.getRowsFieldsNotToInclude().get(rowIdx);
        List<Integer> excludedIds = nonogramRowLogic.getRowsSequencesIdsNotToInclude().get(rowIdx);

        boolean anyUpdated = false;

        for (int seqIdx = ranges.size() - 1; seqIdx > 0; seqIdx--) {
            int prevIdx = seqIdx - 1;
            if (excludedIds.contains(prevIdx)) continue;

            List<Integer> updatedPrev = excludedIds.contains(seqIdx)
                    ? SequenceRangeCorrectionHelper.calculateUpdatedPreviousSequenceRangeAfterExcludedSequence(ranges, fieldsNotToInclude, seqIdx, prevIdx)
                    : SequenceRangeCorrectionHelper.calculateUpdatedPreviousSequenceRangeAfterIncludedSequence(ranges, lengths, seqIdx, prevIdx);

            anyUpdated |= tryCorrectFromRight(rowIdx, lengths, ranges.get(prevIdx), updatedPrev, prevIdx);
        }

        return anyUpdated;
    }

    private boolean tryCorrectFromRight(int rowIdx,
                                     List<Integer> lengths,
                                     List<Integer> oldRange,
                                     List<Integer> newRange,
                                     int sequenceIdx) {
        if (!oldRange.get(1).equals(newRange.get(1))) {
            nonogramRowLogic.updateRowSequenceRange(rowIdx, sequenceIdx, newRange);
            if (rangeLength(newRange) == lengths.get(sequenceIdx) && nonogramRowLogic.getBoardAccessHelper().isRowRangeColoured(rowIdx, newRange)) {
                nonogramRowLogic.excludeSequenceInRow(rowIdx, sequenceIdx);
            }

            return true;
        }

        return false;
    }

    @Override
    public void correctRowSequencesRangesWhenMetColouredField(int rowIdx) {
        List<List<Integer>> initialRanges = deepCopy(nonogramRowLogic.getRowsSequencesRanges().get(rowIdx));

        boolean anyUpdated = false;

        anyUpdated |= correctRowSequencesRangesWhenMetColouredFieldFromLeft(rowIdx);
        anyUpdated |= correctRowSequencesRangesWhenMetColouredFieldFromRight(rowIdx);

        if (anyUpdated) {
            List<List<Integer>> updatedRanges = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);
            nonogramRowLogic.getNonogramState().increaseMadeSteps();

            Field rowField = new Field(rowIdx, 0);
            nonogramRowLogic.getActionScheduler().scheduleActionsBasedOnField(rowField,
                    NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS_IN_ROW);

            String tmpLog = SequenceRangeCorrectionWhenMetColouredFieldsLogHelper.generateLog(
                    true,
                    rowIdx,
                    nonogramRowLogic.getNonogramBoardRow(rowIdx),
                    nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx),
                    initialRanges,
                    updatedRanges
            );
            nonogramRowLogic.setTmpLog(tmpLog);
            nonogramRowLogic.addLog();
        }
    }

    private boolean correctRowSequencesRangesWhenMetColouredFieldFromLeft(int rowIdx) {
        var ranges = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);
        var lengths = nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        boolean changed = false;

        if (lengths.isEmpty()) return false;
        if (ranges.isEmpty())  return false;

        int seqIdx = 0;
        int seqLength = lengths.get(seqIdx);
        int columnIdx = 0;

        while (columnIdx < nonogramRowLogic.getNonogramRules().getWidth() && seqIdx < lengths.size()) {
            Field field = new Field(rowIdx, columnIdx);

            if (!isFieldColoured(nonogramRowLogic.getNonogramSolutionBoard(), field)) {
                columnIdx++;
                continue;
            }

            List<Integer> oldRange = List.copyOf(ranges.get(seqIdx));
            List<Integer> updatedRange = RangeCorrectionHelper.updatedSequenceRangeWhenMetColouredField(
                    oldRange.get(0), oldRange.get(1), columnIdx, seqLength, true);

            if (!updatedRange.equals(oldRange)) {
                nonogramRowLogic.updateRowSequenceRange(rowIdx, seqIdx, updatedRange);

                if (rangeLength(updatedRange) == seqLength &&
                        nonogramRowLogic.getBoardAccessHelper().isRowRangeColoured(rowIdx, updatedRange)) {
                    nonogramRowLogic.excludeSequenceInRow(rowIdx, seqIdx);
                    nonogramRowLogic.getNonogramState().increaseMadeSteps();
                }

                changed = true;
            }

            columnIdx += seqLength;
            seqIdx++;
            if (seqIdx != lengths.size()) {
                seqLength = lengths.get(seqIdx);
            }
        }

        return changed;
    }

    private boolean correctRowSequencesRangesWhenMetColouredFieldFromRight(int rowIdx) {
        var ranges = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);
        var lengths = nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        boolean changed = false;

        if (lengths.isEmpty()) return false;
        if (ranges.isEmpty())  return false;

        int seqIdx = lengths.size() - 1;
        int seqLength = lengths.get(seqIdx);
        int columnIdx = nonogramRowLogic.getNonogramRules().getWidth() - 1;

        while (columnIdx >= 0 && seqIdx >= 0) {
            Field field = new Field(rowIdx, columnIdx);

            if (!isFieldColoured(nonogramRowLogic.getNonogramSolutionBoard(), field)) {
                columnIdx--;
                continue;
            }

            List<Integer> oldRange = ranges.get(seqIdx);
            List<Integer> updatedRange = RangeCorrectionHelper.updatedSequenceRangeWhenMetColouredField(
                    oldRange.get(0), oldRange.get(1), columnIdx, seqLength, false
            );

            if (!updatedRange.equals(oldRange)) {
                nonogramRowLogic.updateRowSequenceRange(rowIdx, seqIdx, updatedRange);

                if (rangeLength(updatedRange) == seqLength &&
                        nonogramRowLogic.getBoardAccessHelper().isRowRangeColoured(rowIdx, updatedRange)) {
                    nonogramRowLogic.excludeSequenceInRow(rowIdx, seqIdx);
                    nonogramRowLogic.getNonogramState().increaseMadeSteps();
                }

                changed = true;
            }

            columnIdx -= seqLength;
            seqIdx--;
            if (seqIdx > -1) {
                seqLength = lengths.get(seqIdx);
            }
        }

        return changed;
    }

    @Override
    public void correctRowSequencesRangesIfXOnWay(int rowIndex) {
        var sequencesRanges = nonogramRowLogic.getRowsSequencesRanges().get(rowIndex);
        var sequencesLengths = nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIndex);
        var excluded = nonogramRowLogic.getRowsSequencesIdsNotToInclude().get(rowIndex);

        var boardRow = nonogramRowLogic.getNonogramSolutionBoard().get(rowIndex);

        boolean changed = false;

        List<List<Integer>> initialRanges = deepCopy(sequencesRanges);

        for (int sequenceIdx = 0; sequenceIdx < sequencesRanges.size(); sequenceIdx++) {
            if (excluded.contains(sequenceIdx)) continue;

            List<Integer> currentRange = sequencesRanges.get(sequenceIdx);
            int length = sequencesLengths.get(sequenceIdx);

            List<Integer> corrected = SequenceRangeCorrectionWhenMetXHelper.calculateCorrectedRangeWithoutX(
                    false, rowIndex, currentRange, length,  nonogramRowLogic.getNonogramSolutionBoard());

            if (!currentRange.equals(corrected)) {
                nonogramRowLogic.updateRowSequenceRange(rowIndex, sequenceIdx, corrected);

                if (shouldExcludeSequence(rowIndex, corrected, length)) {
                    nonogramRowLogic.excludeSequenceInRow(rowIndex, sequenceIdx);
                }

                changed = true;
            }
        }

        if (changed) {
            List<List<Integer>> updatedRanges = nonogramRowLogic.getRowsSequencesRanges().get(rowIndex);

            String tmpLog = SequenceRangeCorrectionWhenMetXLogHelper.generateLog(
                    true,
                    rowIndex,
                    boardRow,
                    initialRanges,
                    updatedRanges,
                    sequencesLengths,
                    excluded);
            nonogramRowLogic.setTmpLog(tmpLog);
            nonogramRowLogic.addLog();

            nonogramRowLogic.getNonogramState().increaseMadeSteps();
            nonogramRowLogic.getActionScheduler().scheduleActionsBasedOnField(
                    new Field(rowIndex, 0), NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_ROW);
        }
    }

    private boolean shouldExcludeSequence(int rowIdx, List<Integer> sequenceRange, int sequenceLength) {
        return rangeLength(sequenceRange) == sequenceLength &&
                nonogramRowLogic.getBoardAccessHelper().isRowRangeColoured(rowIdx, sequenceRange);
    }

    @Override
    public void correctRowSequencesRangesWhenMatchingFieldsToSequences(int rowIdx) {
        List<List<Integer>> rowSequencesRanges = deepCopy(nonogramRowLogic.getRowsSequencesRanges().get(rowIdx));
        List<Integer> rowSequencesLengths = nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> colouredRanges = collectColouredSequencesRanges(nonogramRowLogic.getNonogramSolutionBoard(), rowIdx, true);

        List<List<Integer>> initialSequencesRanges = deepCopy(rowSequencesRanges);

        boolean hasChangedGlobal = false;
        boolean hasChanged;

        do {
            hasChanged = processDirection(colouredRanges, rowSequencesRanges, rowSequencesLengths, true);
            hasChanged |= processDirection(colouredRanges, rowSequencesRanges, rowSequencesLengths, false);

            hasChangedGlobal |= hasChanged;
        } while (hasChanged);

        if (hasChangedGlobal) {
            List<List<Integer>> updatedSequencesRanges = deepCopy(rowSequencesRanges);

            String tmpLog = SequenceRangeCorrectionWhenMatchingFieldsToSequencesLogHelper.generateLog(
                    true,
                    rowIdx,
                    rowSequencesLengths,
                    nonogramRowLogic.getNonogramSolutionBoard().get(rowIdx),
                    initialSequencesRanges,
                    updatedSequencesRanges
            );
            nonogramRowLogic.setTmpLog(tmpLog);
            nonogramRowLogic.addLog();

            nonogramRowLogic.getNonogramState().increaseMadeSteps();
            Field rowField = new Field(rowIdx, 0);
            nonogramRowLogic.getActionScheduler().scheduleActionsBasedOnField(rowField, NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_ROW);
        }
    }

    private boolean processDirection(List<List<Integer>> colouredRanges, List<List<Integer>> rowSequencesRanges,
                                     List<Integer> rowSequencesLengths, boolean fromRight) {

        Map<Integer, List<Integer>> colouredToSeqs = collectMatchingSequences(colouredRanges, rowSequencesRanges, rowSequencesLengths, fromRight);
        filterSequences(colouredToSeqs, rowSequencesRanges.size(), fromRight);

        return updateRanges(colouredToSeqs, colouredRanges, rowSequencesRanges, rowSequencesLengths);
    }

    private void filterSequences(Map<Integer, List<Integer>> colouredToSeqs, int totalSequences, boolean fromRight) {
        int boundary = fromRight ? -1 : totalSequences;
        List<Integer> keys = new ArrayList<>(colouredToSeqs.keySet());
        keys.sort(fromRight ? Comparator.naturalOrder() : Comparator.reverseOrder());

        for (int i : keys) {
            List<Integer> possible = colouredToSeqs.get(i);
            if (possible == null || possible.isEmpty()) continue;

            List<Integer> filtered = filterByBoundary(possible, boundary, fromRight);
            colouredToSeqs.put(i, filtered);

            if (filtered.size() == 1) {
                boundary = filtered.get(0);
            }
        }
    }

    private List<Integer> filterByBoundary(List<Integer> sequenceIds, int boundary, boolean fromRight) {
        return sequenceIds.stream()
                .filter(seqId -> fromRight ? seqId >= boundary : seqId <= boundary)
                .toList();
    }

    @Override
    public void correctRowSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(int rowIdx) {
        boolean anyUpdated = false;

        List<List<Integer>> rowSequencesRanges = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);
        List<List<Integer>> initialSequencesRanges = deepCopy(rowSequencesRanges);

        for (int seqIdx = 0; seqIdx < rowSequencesRanges.size(); seqIdx++) {
            List<Integer> currentRange = rowSequencesRanges.get(seqIdx);

            List<Integer> updatedRange = RangeCorrectionHelper.adjustRangeIfColouredAtEdges(
                    currentRange,
                    rowIdx,
                    false,
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
            List<List<Integer>> updatedSequencesRanges = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);
            List<Integer> sequencesLengths = nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
            List<String> line = nonogramRowLogic.getNonogramSolutionBoard().get(rowIdx);

            String tmpLog = SequenceRangeCorrectionFromColouredEdgesLogHelper.generateLog(
                    true,
                    rowIdx,
                    initialSequencesRanges,
                    updatedSequencesRanges,
                    sequencesLengths,
                    line
            );
            nonogramRowLogic.setTmpLog(tmpLog);
            nonogramRowLogic.addLog();

            nonogramRowLogic.getNonogramState().increaseMadeSteps();
            Field rowField = new Field(rowIdx, 0);
            nonogramRowLogic.getActionScheduler().scheduleActionsBasedOnField(rowField, NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE_IN_ROW);
        }
    }

    @Override
    public void refreshFrom(NonogramRowLogic logicToCopy) {
        nonogramRowLogic.setLogs(logicToCopy.getLogs());

        nonogramRowLogic.setNonogramSolutionBoard(logicToCopy.getNonogramSolutionBoard());
        nonogramRowLogic.setRowsSequencesRanges(logicToCopy.getRowsSequencesRanges());
        nonogramRowLogic.setRowsFieldsNotToInclude(logicToCopy.getRowsFieldsNotToInclude());
    }
}

