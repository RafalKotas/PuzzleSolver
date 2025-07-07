package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogicParams;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.xplacement.NonogramFieldPlacingXHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range.SequenceRangeCorrectionWhenPlacingXsLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement.PlaceXsAroundLongestSequenceLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement.PlaceXsAtTooShortEmptySequencesLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement.PlaceXsAtUnreachableFieldsLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement.PlaceXsIfOWillCreateTooLongSequenceLogHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.util.NonogramLogicUtils.colouredSequenceInColumnIsValid;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service.NonogramLogicService.rangesListIncludingAnotherRange;

@Slf4j
@Getter
@Setter
public class ColumnXPlacementHelperImpl implements ColumnXPlacementHelper {

    private final NonogramColumnLogic logic;

    private final NonogramFieldPlacingXHelper nonogramFieldPlacingXHelper;

    private static final List<Integer> NOT_FOUND_EMPTY_FIELDS_RANGE_VALUE = List.of(-1, -1);
    private static final List<Integer> NOT_FOUND_COLOURED_FIELDS_RANGE_VALUE = List.of(-1, -1);

    public ColumnXPlacementHelperImpl(NonogramColumnLogic nonogramColumnLogic) {
        logic = nonogramColumnLogic;
        this.nonogramFieldPlacingXHelper = new NonogramFieldPlacingXHelper(
                logic.getNonogramSolutionBoard(),
                logic.getNonogramSolutionBoardWithMarks(),
                logic.getBoardAccessHelper()
        );
    }

    @Override
    public void placeXsColumnAtUnreachableFields(int columnIdx) {
        List<String> initialState = logic.getBoardAccessHelper().getColumnCopy(columnIdx);
        List<List<Integer>> initialRanges = cloneAndMakeImmutable2DList(logic.getColumnsSequencesRanges().get(columnIdx));

        List<List<Integer>> colSequencesRanges = logic.getColumnsSequencesRanges().get(columnIdx);

        for (int rowIdx = 0; rowIdx < logic.getNonogramRules().getHeight(); rowIdx++) {
            List<Integer> fieldAsRange = List.of(rowIdx, rowIdx);
            boolean isReachable = rangesListIncludingAnotherRange(colSequencesRanges, fieldAsRange);

            if (!isReachable) {
                Field fieldToExclude = new Field(rowIdx, columnIdx);
                if (isFieldEmpty(logic.getNonogramSolutionBoard(), fieldToExclude)) {
                    nonogramFieldPlacingXHelper.placeXAtGivenField(fieldToExclude);

                    logic.getActionScheduler().scheduleActionsBasedOnField(fieldToExclude, NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS);
                    logic.getNonogramState().increaseMadeSteps();
                } else if (NonogramLogicParams.SHOW_REPETITIONS) {
                    log.warn("X at unreachable field in column placed earlier!");
                }
            }
        }

        List<String> finalState = logic.getBoardAccessHelper().getColumnCopy(columnIdx);
        if (!initialState.equals(finalState)) {
            logic.getLogService().setTmpLog(PlaceXsAtUnreachableFieldsLogHelper.generateLog(
                    columnIdx,
                    initialState,
                    finalState,
                    initialRanges,
                    false
            ));
            logic.getLogService().addLog();
        }
    }

    @Override
    public void placeXsAroundLongestSequencesInColumn(int columnIdx) {
        int height = logic.getNonogramRules().getHeight();
        int rowIdx = 0;

        while (rowIdx < height) {
            Field field = new Field(rowIdx, columnIdx);
            if (isFieldColoured(logic.getNonogramSolutionBoard(), field)) {
                List<Integer> colouredRange = findColouredSequenceRangeInColumn(rowIdx, columnIdx);
                processColouredSequenceRangeInColumn(columnIdx, colouredRange);
                rowIdx = colouredRange.get(1) + 1;
            } else {
                rowIdx++;
            }
        }
    }

    private List<Integer> findColouredSequenceRangeInColumn(int startRowIdx, int columnIdx) {
        int start = startRowIdx;
        int end = startRowIdx;

        while (start > 0 && isFieldColoured(logic.getNonogramSolutionBoard(), new Field(start - 1, columnIdx))) {
            start--;
        }

        while (end + 1 < logic.getNonogramRules().getHeight() &&
                isFieldColoured(logic.getNonogramSolutionBoard(), new Field(end + 1, columnIdx))) {
            end++;
        }

        return List.of(start, end);
    }

    private void processColouredSequenceRangeInColumn(int columnIdx, List<Integer> colouredRange) {
        List<List<Integer>> columnRanges = logic.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnLengths = logic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);

        int lengthOnBoard = rangeLength(colouredRange);

        List<Integer> matchingIndices = new ArrayList<>();
        List<Integer> matchingLengths = new ArrayList<>();

        for (int i = 0; i < columnRanges.size(); i++) {
            if (rangeInsideAnotherRange(colouredRange, columnRanges.get(i))
                    && lengthOnBoard <= columnLengths.get(i)) {
                matchingIndices.add(i);
                matchingLengths.add(columnLengths.get(i));
            }
        }

        List<Integer> edgeXs = List.of(colouredRange.get(0) - 1, colouredRange.get(1) + 1);

        if (matchingIndices.size() == 1 && lengthOnBoard == matchingLengths.get(0)) {
            placeXsAndUpdateSingleSequence(columnIdx, edgeXs, matchingIndices.get(0));
        } else if (matchingLengths.size() > 1 && lengthOnBoard == Collections.max(matchingLengths)) {
            placeXsAroundLongestSequence(columnIdx, edgeXs, false);
        }
    }

    private void placeXsAndUpdateSingleSequence(int columnIdx, List<Integer> xEdges, int seqIdx) {
        placeXsAroundLongestSequence(columnIdx, xEdges, true);

        List<Integer> updatedRange = List.of(xEdges.get(0) + 1, xEdges.get(1) - 1);
        excludeColouredFieldsBetweenXs(columnIdx, updatedRange);

        updateLogicAfterXsPlacement(columnIdx, seqIdx, updatedRange);
    }

    private void placeXsAroundLongestSequence(int columnIdx, List<Integer> xEdges, boolean onlyMatching) {
        List<String> columnBefore = logic.getBoardAccessHelper().getColumnCopy(columnIdx);
        boolean anyXPlaced = false;

        for (int rowIdx : xEdges) {
            if (!logic.getBoardAccessHelper().isRowIndexValid(rowIdx)) continue;

            Field edgeField = new Field(rowIdx, columnIdx);
            if (isFieldEmpty(logic.getNonogramSolutionBoard(), edgeField)) {
                nonogramFieldPlacingXHelper.placeXAtGivenField(edgeField);
                logic.getNonogramFieldExclusionHelper().excludeFieldInColumn(edgeField);
                logic.getActionScheduler().scheduleActionsBasedOnField(
                        new Field(rowIdx, columnIdx),
                        NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES
                );
                logic.getNonogramState().increaseMadeSteps();
                anyXPlaced = true;
            } else if (NonogramLogicParams.SHOW_REPETITIONS) {
                log.warn("X around longest sequence already placed at {}", edgeField);
            }
        }

        List<String> columnAfter = logic.getBoardAccessHelper().getColumnCopy(columnIdx);

        if (anyXPlaced) {
            logic.getLogService().setTmpLog(PlaceXsAroundLongestSequenceLogHelper.generateLog(
                    columnIdx,
                    xEdges,
                    columnBefore,
                    columnAfter,
                    onlyMatching,
                    false
            ));
            logic.getLogService().addLog();
        }
    }

    private void excludeColouredFieldsBetweenXs(int columnIdx, List<Integer> range) {
        for (int rowIdx = range.get(0); rowIdx <= range.get(1); rowIdx++) {
            logic.getNonogramFieldExclusionHelper().excludeFieldInColumn(new Field(rowIdx, columnIdx));
            logic.getNonogramState().increaseMadeSteps();
        }
    }

    private void updateLogicAfterXsPlacement(int columnIdx, int seqIdx, List<Integer> newRange) {
        List<Integer> oldRange = logic.getColumnsSequencesRanges().get(columnIdx).get(seqIdx);
        if (!newRange.equals(oldRange)) {
            logic.changeColumnSequenceRange(columnIdx, seqIdx, newRange);

            List<List<Integer>> allRanges = logic.getColumnsSequencesRanges().get(columnIdx);
            List<Integer> lengths = logic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
            List<String> columnState = logic.getBoardAccessHelper().getColumnCopy(columnIdx);

            logic.getLogService().setTmpLog(SequenceRangeCorrectionWhenPlacingXsLogHelper.generateLog(
                    columnIdx, seqIdx, allRanges, newRange, columnState, lengths
            ));
            logic.getLogService().addLog();
        }

        logic.excludeSequenceInColumn(columnIdx, seqIdx);

        Field topEdge = new Field(newRange.get(0) - 1, columnIdx);
        Field bottomEdge = new Field(newRange.get(1) + 1, columnIdx);

        if (logic.getBoardAccessHelper().isRowIndexValid(topEdge.getRowIdx())) {
            logic.getActionScheduler().scheduleActionsBasedOnField(topEdge, NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES);
        }

        if (logic.getBoardAccessHelper().isRowIndexValid(bottomEdge.getRowIdx())) {
            logic.getActionScheduler().scheduleActionsBasedOnField(bottomEdge, NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES);
        }
    }

    @Override
    public void placeXsColumnAtTooShortEmptySequences(int columnIdx) {
        int height = logic.getNonogramRules().getHeight();
        List<List<Integer>> sequenceRanges = logic.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> sequenceLengths = logic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<Integer> excludedSequenceIds = logic.getColumnsSequencesIdsNotToInclude().get(columnIdx);

        List<String> columnBefore = logic.getBoardAccessHelper().getColumnCopy(columnIdx);

        int rowIdx = 0;
        while (rowIdx < height - 1) {
            if (!isFieldWithX(logic.getNonogramSolutionBoard(), new Field(rowIdx, columnIdx))) {
                rowIdx++;
                continue;
            }

            int startRowX = rowIdx;
            int cursor = rowIdx + 1;
            while (cursor < height && isFieldEmpty(logic.getNonogramSolutionBoard(), new Field(cursor, columnIdx))) {
                cursor++;
            }

            if (cursor >= height || !isFieldWithX(logic.getNonogramSolutionBoard(), new Field(cursor, columnIdx))) {
                rowIdx = cursor;
                continue;
            }

            int endRowX = cursor;
            if (endRowX <= startRowX + 1) {
                rowIdx = cursor;
                continue;
            }

            List<Integer> emptyRange = List.of(startRowX + 1, endRowX - 1);

            if (onlyTooLongSequencesFitInRange(sequenceRanges, sequenceLengths, excludedSequenceIds, emptyRange)) {
                for (int rowInRange = emptyRange.get(0); rowInRange <= emptyRange.get(1); rowInRange++) {
                    Field field = new Field(rowInRange, columnIdx);
                    if (!isFieldEmpty(logic.getNonogramSolutionBoard(), field)) continue;

                    nonogramFieldPlacingXHelper.placeXAtGivenField(field);
                    logic.getNonogramFieldExclusionHelper().excludeFieldInColumn(field);
                    logic.getActionScheduler().scheduleActionsBasedOnField(field, NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES);
                    logic.getNonogramState().increaseMadeSteps();
                }
            }

            rowIdx = endRowX;
        }

        List<String> columnAfter = logic.getBoardAccessHelper().getColumnCopy(columnIdx);
        if (!columnBefore.equals(columnAfter)) {
            logic.getLogService().setTmpLog(PlaceXsAtTooShortEmptySequencesLogHelper.generateLog(
                    columnIdx,
                    columnBefore,
                    columnAfter,
                    sequenceLengths,
                    excludedSequenceIds,
                    false // isRow
            ));
            logic.getLogService().addLog();
        }
    }

    private boolean onlyTooLongSequencesFitInRange(
            List<List<Integer>> sequenceRanges,
            List<Integer> sequenceLengths,
            List<Integer> excludedSequenceIds,
            List<Integer> emptyRange
    ) {
        int emptyRangeLength = rangeLength(emptyRange);
        List<Integer> fittingSequences = new ArrayList<>();
        List<Integer> tooLongSequences = new ArrayList<>();

        for (int seqIdx = 0; seqIdx < sequenceLengths.size(); seqIdx++) {
            if (excludedSequenceIds.contains(seqIdx)) continue;

            List<Integer> seqRange = sequenceRanges.get(seqIdx);
            if (rangeInsideAnotherRange(emptyRange, seqRange)) {
                fittingSequences.add(seqIdx);
                if (sequenceLengths.get(seqIdx) > emptyRangeLength) {
                    tooLongSequences.add(seqIdx);
                }
            }
        }

        return !fittingSequences.isEmpty() && fittingSequences.equals(tooLongSequences);
    }

    @Override
    public void placeXsColumnIfOWillMergeNearFieldsToTooLongColouredSequence(int columnIdx) {
        List<Integer> colouredFields = findColouredFieldsInColumn(logic.getNonogramSolutionBoard(), columnIdx);
        List<List<Integer>> colouredRanges = groupConsecutiveIndices(colouredFields);
        List<List<List<Integer>>> rangesWithExtras = createCandidateRangesAroundSequences(colouredRanges);

        List<String> columnBefore = logic.getBoardAccessHelper().getColumnCopy(columnIdx);

        for (int i = 0; i < colouredRanges.size(); i++) {
            List<List<Integer>> currentWithExtras = rangesWithExtras.get(i);

            checkAndPlaceXBefore(colouredRanges, currentWithExtras.get(0), i, columnIdx);
            checkAndPlaceXAfter(colouredRanges, currentWithExtras.get(1), i, columnIdx);
        }

        List<String> columnAfter = logic.getBoardAccessHelper().getColumnCopy(columnIdx);

        if (!columnBefore.equals(columnAfter)) {
            // TODO - create log helper for this action
            logic.getLogService().setTmpLog(PlaceXsIfOWillCreateTooLongSequenceLogHelper.generateLog(
                    columnIdx,
                    columnBefore,
                    columnAfter,
                    logic.getNonogramRules().getColumnSequencesLengths().get(columnIdx),
                    logic.getColumnsSequencesRanges().get(columnIdx),
                    false // isRow == false
            ));
            logic.getLogService().addLog();
        }
    }

    private void checkAndPlaceXBefore(List<List<Integer>> colouredRanges, List<Integer> rangeWithExtra, int idx, int colIdx) {
        List<Integer> merged = (idx > 0)
                ? mergeWithPreviousIfAdjacent(colouredRanges.get(idx - 1), rangeWithExtra)
                : rangeWithExtra;

        int row = rangeWithExtra.get(0);
        Field field = new Field(row, colIdx);

        if (shouldPlaceX(row, merged, colIdx, field)) {
            nonogramFieldPlacingXHelper.placeXAtGivenField(field);
            logic.getNonogramFieldExclusionHelper().excludeFieldInColumn(field);
            logic.getActionScheduler().scheduleActionsBasedOnField(field, NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE);
            logic.getNonogramState().increaseMadeSteps();
        } else if (NonogramLogicParams.SHOW_REPETITIONS) {
            log.info("X because \"O\" will create too long sequence in column placed earlier!");
        }
    }

    private void checkAndPlaceXAfter(List<List<Integer>> colouredRanges, List<Integer> rangeWithExtra, int idx, int colIdx) {
        int nextRow = rangeWithExtra.get(1);
        if (nextRow == logic.getNonogramRules().getHeight()) return;

        Field field = new Field(nextRow, colIdx);
        List<Integer> merged = (idx < colouredRanges.size() - 1)
                ? mergeWithNextIfAdjacent(rangeWithExtra, colouredRanges.get(idx + 1))
                : rangeWithExtra;

        if (shouldPlaceX(nextRow, merged, colIdx, field)) {
            nonogramFieldPlacingXHelper.placeXAtGivenField(field);
            logic.getNonogramFieldExclusionHelper().excludeFieldInColumn(field);
            logic.getActionScheduler().scheduleActionsBasedOnField(field, NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE);
            logic.getNonogramState().increaseMadeSteps();
        } else if (NonogramLogicParams.SHOW_REPETITIONS) {
            log.info("X because \"O\" will create too long sequence in column placed earlier!");
        }
    }

    private boolean shouldPlaceX(int rowIdx, List<Integer> range, int colIdx, Field field) {
        return logic.getBoardAccessHelper().isRowIndexValid(rowIdx)
                && isFieldEmpty(logic.getNonogramSolutionBoard(), field)
                && !colouredSequenceInColumnIsValid(range, colIdx, logic);
    }

    @Override
    public void placeXsColumnIfONearXWillBeginTooLongPossibleColouredSequence(int columnIdx) {
        List<String> columnBefore = logic.getBoardAccessHelper().getColumnCopy(columnIdx);

        checkDirectionAndPlaceXs(columnIdx, true);
        checkDirectionAndPlaceXs(columnIdx, false);

        List<String> columnAfter = logic.getBoardAccessHelper().getColumnCopy(columnIdx);

        logic.getLogService().setTmpLog(PlaceXsIfOWillCreateTooLongSequenceLogHelper.generateLog(
                columnIdx,
                columnBefore,
                columnAfter,
                logic.getNonogramRules().getColumnSequencesLengths().get(columnIdx),
                logic.getColumnsSequencesRanges().get(columnIdx),
                false // isRow = false
        ));
        logic.getLogService().addLog();
    }

    private void checkDirectionAndPlaceXs(int columnIdx, boolean fromTop) {
        int start = fromTop ? 0 : logic.getNonogramRules().getHeight() - 1;
        int end = fromTop ? logic.getNonogramRules().getHeight() : -1;
        int step = fromTop ? 1 : -1;

        for (int rowIdx = start; fromTop ? rowIdx < end : rowIdx > end; rowIdx += step) {
            Field xField = new Field(rowIdx, columnIdx);
            if (!isFieldWithX(logic.getNonogramSolutionBoard(), xField)) continue;

            List<Integer> emptyRange = fromTop
                    ? getEmptyFieldsRangeFromXToFirstColouredFieldFromTop(xField)
                    : getEmptyFieldsRangeFromXToFirstColouredFieldToTop(xField);

            if (emptyRange.equals(NOT_FOUND_EMPTY_FIELDS_RANGE_VALUE)) continue;

            Field colouredStart = fromTop
                    ? new Field(emptyRange.get(1) + 1, columnIdx)
                    : new Field(emptyRange.get(0) - 1, columnIdx);

            List<Integer> colouredRange = fromTop
                    ? getColouredFieldsRangeNearEmptySequenceFromTop(colouredStart)
                    : getColouredFieldsRangeNearEmptySequenceToTop(colouredStart);

            if (colouredRange.equals(NOT_FOUND_COLOURED_FIELDS_RANGE_VALUE)) continue;

            evaluateAndMaybePlaceX(columnIdx, emptyRange, colouredRange, fromTop);
        }
    }

    private List<Integer> getEmptyFieldsRangeFromXToFirstColouredFieldFromTop(Field xField) {
        List<Integer> range = new ArrayList<>();
        Field field = new Field(xField.getRowIdx() + 1, xField.getColumnIdx());

        while (logic.getBoardAccessHelper().areFieldIndexesValid(field) &&
                isFieldEmpty(logic.getNonogramSolutionBoard(), field)) {
            if (range.isEmpty()) {
                range.add(field.getRowIdx());
            } else {
                if (range.size() == 1) range.add(field.getRowIdx());
                else range.set(1, field.getRowIdx());
            }
            field.setRowIdx(field.getRowIdx() + 1);
        }

        if (range.isEmpty()) return List.of(-1, -1);
        if (range.size() == 1) range.add(range.get(0));
        return range;
    }

    private List<Integer> getEmptyFieldsRangeFromXToFirstColouredFieldToTop(Field xField) {
        List<Integer> range = new ArrayList<>();
        Field field = new Field(xField.getRowIdx() - 1, xField.getColumnIdx());

        while (logic.getBoardAccessHelper().areFieldIndexesValid(field) &&
                isFieldEmpty(logic.getNonogramSolutionBoard(), field)) {
            if (range.isEmpty()) {
                range.add(field.getRowIdx());
            } else {
                if (range.size() == 1) range.add(0, field.getRowIdx());
                else range.set(0, field.getRowIdx());
            }
            field.setRowIdx(field.getRowIdx() - 1);
        }

        if (range.isEmpty()) return List.of(-1, -1);
        if (range.size() == 1) range.add(range.get(0));
        return range;
    }

    private List<Integer> getColouredFieldsRangeNearEmptySequenceFromTop(Field startField) {
        List<Integer> range = new ArrayList<>();
        Field field = new Field(startField.getRowIdx(), startField.getColumnIdx());

        while (logic.getBoardAccessHelper().areFieldIndexesValid(field) &&
                isFieldColoured(logic.getNonogramSolutionBoard(), field)) {
            if (range.isEmpty()) {
                range.add(field.getRowIdx());
            } else {
                if (range.size() == 1) range.add(field.getRowIdx());
                else range.set(1, field.getRowIdx());
            }
            field.setRowIdx(field.getRowIdx() + 1);
        }

        if (range.isEmpty()) return List.of(-1, -1);
        if (range.size() == 1) range.add(range.get(0));
        return range;
    }

    private List<Integer> getColouredFieldsRangeNearEmptySequenceToTop(Field startField) {
        List<Integer> range = new ArrayList<>();
        Field field = new Field(startField.getRowIdx(), startField.getColumnIdx());

        while (logic.getBoardAccessHelper().areFieldIndexesValid(field) &&
                isFieldColoured(logic.getNonogramSolutionBoard(), field)) {
            if (range.isEmpty()) {
                range.add(field.getRowIdx());
            } else {
                if (range.size() == 1) range.add(0, field.getRowIdx());
                else range.set(0, field.getRowIdx());
            }
            field.setRowIdx(field.getRowIdx() - 1);
        }

        if (range.isEmpty()) return List.of(-1, -1);
        if (range.size() == 1) range.add(range.get(0));
        return range;
    }

    private void evaluateAndMaybePlaceX(int columnIdx, List<Integer> emptyRange, List<Integer> colouredRange, boolean isFromTop) {
        List<Integer> lengths = logic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> ranges = logic.getColumnsSequencesRanges().get(columnIdx);

        int emptyLen = rangeLength(emptyRange);
        int colouredLen = rangeLength(colouredRange);
        int totalLen = emptyLen + colouredLen;

        List<Integer> fitting = new ArrayList<>();

        for (int i = 0; i < lengths.size(); i++) {
            List<Integer> range = ranges.get(i);
            int len = lengths.get(i);

            List<Integer> possibleRange = isFromTop
                    ? List.of(emptyRange.get(0), emptyRange.get(0) + len - 1)
                    : List.of(emptyRange.get(1) - len + 1, emptyRange.get(1));

            boolean fitsInsideEmpty = rangeLength(possibleRange) < emptyLen && rangeInsideAnotherRange(possibleRange, range);
            boolean mergedAcceptable = rangeLength(possibleRange) >= emptyLen && totalLen <= len && rangeInsideAnotherRange(possibleRange, range);

            if (fitsInsideEmpty || mergedAcceptable) fitting.add(i);
        }

        if (fitting.isEmpty()) {
            int xRow = isFromTop ? emptyRange.get(0) : emptyRange.get(1);
            Field field = new Field(xRow, columnIdx);

            if (isFieldEmpty(logic.getNonogramSolutionBoard(), field)) {
                nonogramFieldPlacingXHelper.placeXAtGivenField(field);
                logic.getNonogramFieldExclusionHelper().excludeFieldInColumn(field);
                logic.getActionScheduler().scheduleActionsBasedOnField(field, NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE);
                logic.getNonogramState().increaseMadeSteps();
            }
        }
    }
}
