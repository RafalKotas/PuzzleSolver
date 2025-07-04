package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.features.solve.row;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.logic.NonogramLogicParams;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.features.common.xplacement.NonogramFieldPlacingXHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.helper.log.range.SequenceRangeCorrectionWhenPlacingXsLogHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.helper.log.xplacement.PlaceXsAroundLongestSequenceLogHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.helper.log.xplacement.PlaceXsAtTooShortEmptySequencesLogHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.helper.log.xplacement.PlaceXsAtUnreachableFieldsLogHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.helper.log.xplacement.PlaceXsIfOWillCreateTooLongSequenceLogHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.common.ArrayUtils.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.solver.BoardUtils.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.util.NonogramLogicUtils.colouredSequenceInRowIsValid;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.service.NonogramLogicService.rangesListIncludingAnotherRange;

@Slf4j
@Getter
@Setter
public class RowXPlacementHelperImpl implements RowXPlacementHelper {

    private final NonogramRowLogic logic;

    private final NonogramFieldPlacingXHelper nonogramFieldPlacingXHelper;

    private final static List<Integer> NOT_FOUND_EMPTY_FIELDS_RANGE_VALUE = List.of(-1, -1);

    private final static List<Integer> NOT_FOUND_COLOURED_FIELDS_RANGE_VALUE = List.of(-1, -1);

    public RowXPlacementHelperImpl(NonogramRowLogic nonogramRowLogic) {
        logic = nonogramRowLogic;
        this.nonogramFieldPlacingXHelper = new NonogramFieldPlacingXHelper(
                logic.getNonogramSolutionBoard(),
                logic.getNonogramSolutionBoardWithMarks(),
                logic.getBoardAccessHelper()
        );
    }

    @Override
    public void placeXsRowAtUnreachableFields(int rowIdx) {
        List<String> initialState = logic.getBoardAccessHelper().getRowCopy(rowIdx);
        List<List<Integer>> initialRanges = cloneAndMakeImmutable2DList(logic.getRowsSequencesRanges().get(rowIdx));

        List<List<Integer>> rowSequencesRanges = logic.getRowsSequencesRanges().get(rowIdx);

        for (int columnIdx = 0; columnIdx < logic.getNonogramRules().getWidth(); columnIdx++) {
            List<Integer> fieldAsRange = List.of(columnIdx, columnIdx);
            boolean isReachable = rangesListIncludingAnotherRange(rowSequencesRanges, fieldAsRange);

            if (!isReachable) {
                Field fieldToExclude = new Field(rowIdx, columnIdx);
                if (isFieldEmpty(logic.getNonogramSolutionBoard(), fieldToExclude)) {
                    nonogramFieldPlacingXHelper.placeXAtGivenField(fieldToExclude, true);
                    logic.getActionScheduler().scheduleActionsBasedOnField(fieldToExclude, NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS);
                    logic.getNonogramState().increaseMadeSteps();
                } else if (NonogramLogicParams.SHOW_REPETITIONS) {
                    System.out.println("X at unreachable field in row placed earlier!");
                }
            }
        }

        List<String> finalState = logic.getBoardAccessHelper().getRowCopy(rowIdx);
        if (!initialState.equals(finalState)) {
            logic.getLogService().setTmpLog(PlaceXsAtUnreachableFieldsLogHelper.generateLog(
                    rowIdx,
                    initialState,
                    finalState,
                    initialRanges,
                    true
            ));
            logic.getLogService().addLog();
        }
    }

    @Override
    public void placeXsAroundLongestSequencesInRow(int rowIdx) {
        int width = logic.getNonogramRules().getWidth();

        for (int columnIdx = 0; columnIdx < width; columnIdx++) {
            Field field = new Field(rowIdx, columnIdx);
            if (isFieldColoured(logic.getNonogramSolutionBoard(), field)) {
                List<Integer> colouredRange = findColouredSequenceRangeInRow(columnIdx, rowIdx);
                columnIdx = colouredRange.get(1);

                processColouredSequenceRangeInRow(rowIdx, colouredRange);
            }
        }
    }

    private List<Integer> findColouredSequenceRangeInRow(int startColumnIdx, int rowIdx) {
        int start = startColumnIdx;
        int end = startColumnIdx;

        while (start > 0 && isFieldColoured(logic.getNonogramSolutionBoard(), new Field(rowIdx, start - 1))) {
            start--;
        }

        while (end + 1 < logic.getNonogramRules().getWidth()
                && isFieldColoured(logic.getNonogramSolutionBoard(), new Field(rowIdx, end + 1))) {
            end++;
        }

        return List.of(start, end);
    }

    private void processColouredSequenceRangeInRow(int rowIdx, List<Integer> colouredRange) {
        List<List<Integer>> rowRanges = logic.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowLengths = logic.getNonogramRules().getRowSequencesLengths().get(rowIdx);

        int lengthOnBoard = rangeLength(colouredRange);
        List<Integer> matchingIndices = new ArrayList<>();
        List<Integer> matchingLengths = new ArrayList<>();

        for (int i = 0; i < rowRanges.size(); i++) {
            if (rangeInsideAnotherRange(colouredRange, rowRanges.get(i))
                    && lengthOnBoard <= rowLengths.get(i)) {
                matchingIndices.add(i);
                matchingLengths.add(rowLengths.get(i));
            }
        }

        List<Integer> edgeXs = List.of(colouredRange.get(0) - 1, colouredRange.get(1) + 1);

        if (matchingIndices.size() == 1 && lengthOnBoard == matchingLengths.get(0)) {
            placeXsAndUpdateSingleSequence(rowIdx, edgeXs, matchingIndices.get(0));
        } else if (matchingLengths.size() > 1 && lengthOnBoard == Collections.max(matchingLengths)) {
            placeXsAroundLongestSequence(rowIdx, edgeXs, false);
        }
    }

    private void placeXsAndUpdateSingleSequence(int rowIdx, List<Integer> xEdges, int seqIdx) {
        placeXsAroundLongestSequence(rowIdx, xEdges, true);

        List<Integer> updatedRange = List.of(xEdges.get(0) + 1, xEdges.get(1) - 1);
        excludeColouredFieldsBetweenXs(rowIdx, updatedRange);

        updateLogicAfterXsPlacement(rowIdx, seqIdx, updatedRange);
    }

    private void placeXsAroundLongestSequence(int rowIdx, List<Integer> xEdges, boolean onlyMatching) {
        List<String> rowBefore = logic.getBoardAccessHelper().getRowCopy(rowIdx);
        boolean anyXPlaced = false;

        for (int columnIdx : xEdges) {
            if (!logic.getBoardAccessHelper().isColumnIndexValid(columnIdx)) continue;

            Field edgeField = new Field(rowIdx, columnIdx);
            if (isFieldEmpty(logic.getNonogramSolutionBoard(), edgeField)) {
                nonogramFieldPlacingXHelper.placeXAtGivenField(edgeField, true);
                logic.getActionScheduler().scheduleActionsBasedOnField(
                        new Field(rowIdx, columnIdx), NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES
                );
                logic.getNonogramState().increaseMadeSteps();
                anyXPlaced = true;
            } else if (NonogramLogicParams.SHOW_REPETITIONS) {
                System.out.println("X around longest sequence already placed at " + edgeField);
            }
        }

        List<String> rowAfter = logic.getBoardAccessHelper().getRowCopy(rowIdx);

        if (anyXPlaced) {
            logic.getLogService().setTmpLog(PlaceXsAroundLongestSequenceLogHelper.generateLog(
                    rowIdx,
                    xEdges,
                    rowBefore,
                    rowAfter,
                    onlyMatching,
                    true
            ));
            logic.getLogService().addLog();
        }
    }

    private void excludeColouredFieldsBetweenXs(int rowIdx, List<Integer> range) {
        for (int columnIdx = range.get(0); columnIdx <= range.get(1); columnIdx++) {
            logic.getNonogramFieldExclusionHelper().excludeFieldInRow(new Field(rowIdx, columnIdx));
            logic.getNonogramState().increaseMadeSteps();
        }
    }

    private void updateLogicAfterXsPlacement(int rowIdx, int seqIdx, List<Integer> newRange) {
        List<Integer> oldRange = logic.getRowsSequencesRanges().get(rowIdx).get(seqIdx);
        if (!newRange.equals(oldRange)) {
            logic.changeRowSequenceRange(rowIdx, seqIdx, newRange);

            List<List<Integer>> allRanges = logic.getRowsSequencesRanges().get(rowIdx);
            List<Integer> lengths = logic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
            List<String> rowState = logic.getBoardAccessHelper().getRowCopy(rowIdx);

            logic.getLogService().setTmpLog(SequenceRangeCorrectionWhenPlacingXsLogHelper.generateLog(
                    rowIdx, seqIdx, allRanges, newRange, rowState, lengths
            ));
            logic.getLogService().addLog();
        }

        logic.excludeSequenceInRow(rowIdx, seqIdx);

        Field leftEdge = new Field(rowIdx, newRange.get(0) - 1);
        Field rightEdge = new Field(rowIdx, newRange.get(1) + 1);

        if (logic.getBoardAccessHelper().isColumnIndexValid(leftEdge.getColumnIdx())) {
            logic.getActionScheduler().scheduleActionsBasedOnField(leftEdge, NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES);
        }
        if (logic.getBoardAccessHelper().isColumnIndexValid(rightEdge.getColumnIdx())) {
            logic.getActionScheduler().scheduleActionsBasedOnField(rightEdge, NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES);
        }
    }

    @Override
    public void placeXsRowAtTooShortEmptySequences(int rowIdx) {
        int width = logic.getNonogramRules().getWidth();
        List<List<Integer>> sequenceRanges = logic.getRowsSequencesRanges().get(rowIdx);
        List<Integer> sequenceLengths = logic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<Integer> excludedSequenceIds = logic.getRowsSequencesIdsNotToInclude().get(rowIdx);

        List<String> rowBefore = logic.getBoardAccessHelper().getRowCopy(rowIdx);

        for (int colIdx = 0; colIdx < width - 1; colIdx++) {
            if (!isFieldWithX(logic.getNonogramSolutionBoard(), new Field(rowIdx, colIdx))) continue;

            int startColX = colIdx;
            int cursor = colIdx + 1;

            while (cursor < width && isFieldEmpty(logic.getNonogramSolutionBoard(), new Field(rowIdx, cursor))) {
                cursor++;
            }

            if (!(cursor < width) || isFieldColoured(logic.getNonogramSolutionBoard(), new Field(rowIdx, cursor))) {
                continue;
            }

            int endColX = cursor;

            if (endColX <= startColX + 1) {
                colIdx = cursor - 1;
                continue;
            }

            List<Integer> emptyRange = List.of(startColX + 1, endColX - 1);
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

            if (!fittingSequences.isEmpty() && fittingSequences.equals(tooLongSequences)) {
                for (int columnIdx = emptyRange.get(0); columnIdx <= emptyRange.get(1); columnIdx++) {
                    Field field = new Field(rowIdx, columnIdx);
                    if (!isFieldEmpty(logic.getNonogramSolutionBoard(), field)) continue;

                    nonogramFieldPlacingXHelper.placeXAtGivenField(field, true);
                    logic.getActionScheduler().scheduleActionsBasedOnField(field, NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES);
                    logic.getNonogramState().increaseMadeSteps();
                }
            }

            colIdx = endColX - 1;
        }

        List<String> rowAfter = logic.getBoardAccessHelper().getRowCopy(rowIdx);

        if (!rowBefore.equals(rowAfter)) {
            logic.getLogService().setTmpLog(PlaceXsAtTooShortEmptySequencesLogHelper.generateLog(
                    rowIdx,
                    rowBefore,
                    rowAfter,
                    sequenceLengths,
                    excludedSequenceIds,
                    true // isRow
            ));
            logic.getLogService().addLog();
        }
    }


    @Override
    public void placeXsRowIfOWillMergeNearFieldsToTooLongColouredSequence(int rowIdx) {
        List<Integer> colouredFields = findColouredFieldsInRow(logic.getNonogramSolutionBoard(), rowIdx);
        List<List<Integer>> colouredRanges = groupConsecutiveIndices(colouredFields);
        List<List<List<Integer>>> rangesWithExtras = createCandidateRangesAroundSequences(colouredRanges);

        List<String> rowBefore = logic.getBoardAccessHelper().getRowCopy(rowIdx);

        for (int i = 0; i < colouredRanges.size(); i++) {
            List<List<Integer>> currentWithExtras = rangesWithExtras.get(i);

            checkAndPlaceXBeforeInRow(colouredRanges, currentWithExtras.get(0), i, rowIdx);
            checkAndPlaceXAfterInRow(colouredRanges, currentWithExtras.get(1), i, rowIdx);
        }

        List<String> rowAfter = logic.getBoardAccessHelper().getRowCopy(rowIdx);

        if (!rowBefore.equals(rowAfter)) {
            // TODO - create log helper for this action
            logic.getLogService().setTmpLog(PlaceXsIfOWillCreateTooLongSequenceLogHelper.generateLog(
                    rowIdx,
                    rowBefore,
                    rowAfter,
                    logic.getNonogramRules().getRowSequencesLengths().get(rowIdx),
                    logic.getRowsSequencesRanges().get(rowIdx),
                    true
            ));
            logic.getLogService().addLog();
        }
    }

    private void checkAndPlaceXBeforeInRow(List<List<Integer>> colouredRanges, List<Integer> rangeWithExtra, int idx, int rowIdx) {
        List<Integer> merged = (idx > 0)
                ? mergeWithPreviousIfAdjacent(colouredRanges.get(idx - 1), rangeWithExtra)
                : rangeWithExtra;

        int col = rangeWithExtra.get(0);
        Field field = new Field(rowIdx, col);

        if (shouldPlaceXInRow(rowIdx, col, field, merged)) {
            nonogramFieldPlacingXHelper.placeXAtGivenField(field, true);
            logic.getActionScheduler().scheduleActionsBasedOnField(field, NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE);
            logic.getNonogramState().increaseMadeSteps();
        } else if (NonogramLogicParams.SHOW_REPETITIONS) {
            System.out.println("X because \"O\" will create too long sequence in row placed earlier!");
        }
    }

    private void checkAndPlaceXAfterInRow(List<List<Integer>> colouredRanges, List<Integer> rangeWithExtra, int idx, int rowIdx) {
        int nextCol = rangeWithExtra.get(1);
        if (nextCol == logic.getNonogramRules().getWidth()) return;

        Field field = new Field(rowIdx, nextCol);
        List<Integer> merged = (idx < colouredRanges.size() - 1)
                ? mergeWithNextIfAdjacent(rangeWithExtra, colouredRanges.get(idx + 1))
                : rangeWithExtra;

        if (shouldPlaceXInRow(rowIdx, nextCol, field, merged)) {
            nonogramFieldPlacingXHelper.placeXAtGivenField(field, true);
            logic.getActionScheduler().scheduleActionsBasedOnField(field, NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE);
            logic.getNonogramState().increaseMadeSteps();
        } else if (NonogramLogicParams.SHOW_REPETITIONS) {
            System.out.println("X because \"O\" will create too long sequence in row placed earlier!");
        }
    }

    private boolean shouldPlaceXInRow(int rowIdx, int colIdx, Field field, List<Integer> range) {
        return logic.getBoardAccessHelper().isColumnIndexValid(colIdx)
                && isFieldEmpty(logic.getNonogramSolutionBoard(), field)
                && !colouredSequenceInRowIsValid(range, rowIdx, logic);
    }

    @Override
    public void placeXsRowIfONearXWillBeginTooLongPossibleColouredSequence(int rowIdx) {
        List<String> rowBefore = logic.getBoardAccessHelper().getRowCopy(rowIdx);

        checkDirectionAndPlaceXsInRow(rowIdx, true);  // from left
        checkDirectionAndPlaceXsInRow(rowIdx, false); // from right

        List<String> rowAfter = logic.getBoardAccessHelper().getRowCopy(rowIdx);

        if (!rowBefore.equals(rowAfter)) {
            logic.getLogService().setTmpLog(PlaceXsIfOWillCreateTooLongSequenceLogHelper.generateLog(
                    rowIdx,
                    rowBefore,
                    rowAfter,
                    logic.getNonogramRules().getRowSequencesLengths().get(rowIdx),
                    logic.getRowsSequencesRanges().get(rowIdx),
                    true
            ));
            logic.getLogService().addLog();
        }
    }

    private void checkDirectionAndPlaceXsInRow(int rowIdx, boolean fromLeft) {
        int start = fromLeft ? 0 : logic.getNonogramRules().getWidth() - 1;
        int end = fromLeft ? logic.getNonogramRules().getWidth() : -1;
        int step = fromLeft ? 1 : -1;

        for (int columnIdx = start; fromLeft ? columnIdx < end : columnIdx > end; columnIdx += step) {
            Field xField = new Field(rowIdx, columnIdx);
            if (!isFieldWithX(logic.getNonogramSolutionBoard(), xField)) continue;

            List<Integer> emptyRange = fromLeft
                    ? getEmptyFieldsRangeFromXToFirstColouredFieldFromLeft(xField)
                    : getEmptyFieldsRangeFromXToFirstColouredFieldToLeft(xField);

            if (emptyRange.equals(NOT_FOUND_EMPTY_FIELDS_RANGE_VALUE)) continue;

            Field colouredStart = fromLeft
                    ? new Field(rowIdx, emptyRange.get(1) + 1)
                    : new Field(rowIdx, emptyRange.get(0) - 1);

            List<Integer> colouredRange = fromLeft
                    ? getColouredFieldsRangeNearEmptySequenceFromLeft(colouredStart)
                    : getColouredFieldsRangeNearEmptySequenceToLeft(colouredStart);

            if (colouredRange.equals(NOT_FOUND_COLOURED_FIELDS_RANGE_VALUE)) continue;

            evaluateAndMaybePlaceX(rowIdx, emptyRange, colouredRange, fromLeft);
        }
    }

    private List<Integer> getEmptyFieldsRangeFromXToFirstColouredFieldFromLeft(Field xField) {
        List<Integer> range = new ArrayList<>();
        Field field = new Field(xField.getRowIdx(), xField.getColumnIdx() + 1);

        while (logic.getBoardAccessHelper().areFieldIndexesValid(field) &&
                isFieldEmpty(logic.getNonogramSolutionBoard(), field)) {
            if (range.isEmpty()) {
                range.add(field.getColumnIdx());
            } else if (range.size() == 1) {
                range.add(field.getColumnIdx());
            } else {
                range.set(1, field.getColumnIdx());
            }
            field.setColumnIdx(field.getColumnIdx() + 1);
        }

        if (range.isEmpty()) return List.of(-1, -1);
        if (range.size() == 1) range.add(range.get(0));
        return range;
    }

    private List<Integer> getEmptyFieldsRangeFromXToFirstColouredFieldToLeft(Field xField) {
        List<Integer> range = new ArrayList<>();
        Field field = new Field(xField.getRowIdx(), xField.getColumnIdx() - 1);

        while (logic.getBoardAccessHelper().areFieldIndexesValid(field) &&
                isFieldEmpty(logic.getNonogramSolutionBoard(), field)) {
            if (range.isEmpty()) {
                range.add(field.getColumnIdx());
            } else if (range.size() == 1) {
                range.add(0, field.getColumnIdx());
            } else {
                range.set(0, field.getColumnIdx());
            }
            field.setColumnIdx(field.getColumnIdx() - 1);
        }

        if (range.isEmpty()) return List.of(-1, -1);
        if (range.size() == 1) range.add(range.get(0));
        return range;
    }

    private List<Integer> getColouredFieldsRangeNearEmptySequenceFromLeft(Field startField) {
        List<Integer> range = new ArrayList<>();
        Field field = new Field(startField.getRowIdx(), startField.getColumnIdx());

        while (logic.getBoardAccessHelper().areFieldIndexesValid(field) &&
                isFieldColoured(logic.getNonogramSolutionBoard(), field)) {
            if (range.isEmpty()) {
                range.add(field.getColumnIdx());
            } else if (range.size() == 1) {
                range.add(field.getColumnIdx());
            } else {
                range.set(1, field.getColumnIdx());
            }
            field.setColumnIdx(field.getColumnIdx() + 1);
        }

        if (range.isEmpty()) return List.of(-1, -1);
        if (range.size() == 1) range.add(range.get(0));
        return range;
    }

    private List<Integer> getColouredFieldsRangeNearEmptySequenceToLeft(Field startField) {
        List<Integer> range = new ArrayList<>();
        Field field = new Field(startField.getRowIdx(), startField.getColumnIdx());

        while (logic.getBoardAccessHelper().areFieldIndexesValid(field) &&
                isFieldColoured(logic.getNonogramSolutionBoard(), field)) {
            if (range.isEmpty()) {
                range.add(field.getColumnIdx());
            } else if (range.size() == 1) {
                range.add(0, field.getColumnIdx());
            } else {
                range.set(0, field.getColumnIdx());
            }
            field.setColumnIdx(field.getColumnIdx() - 1);
        }

        if (range.isEmpty()) return List.of(-1, -1);
        if (range.size() == 1) range.add(range.get(0));
        return range;
    }

    private void evaluateAndMaybePlaceX(int rowIdx, List<Integer> emptyRange, List<Integer> colouredRange, boolean isFromLeft) {
        List<Integer> lengths = logic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> ranges = logic.getRowsSequencesRanges().get(rowIdx);

        int emptyLen = rangeLength(emptyRange);
        int colouredLen = rangeLength(colouredRange);
        int totalLen = emptyLen + colouredLen;

        List<Integer> fitting = new ArrayList<>();

        for (int i = 0; i < lengths.size(); i++) {
            List<Integer> seqRange = ranges.get(i);
            int len = lengths.get(i);

            List<Integer> possibleRange = isFromLeft
                    ? List.of(emptyRange.get(0), emptyRange.get(0) + len - 1)
                    : List.of(emptyRange.get(1) - len + 1, emptyRange.get(1));

            boolean fitsInsideEmpty = rangeLength(possibleRange) < emptyLen && rangeInsideAnotherRange(possibleRange, seqRange);
            boolean mergedAcceptable = rangeLength(possibleRange) >= emptyLen && totalLen <= len && rangeInsideAnotherRange(possibleRange, seqRange);

            if (fitsInsideEmpty || mergedAcceptable) fitting.add(i);
        }

        if (fitting.isEmpty()) {
            int xCol = isFromLeft ? emptyRange.get(0) : emptyRange.get(1);
            Field field = new Field(rowIdx, xCol);

            if (isFieldEmpty(logic.getNonogramSolutionBoard(), field)) {
                nonogramFieldPlacingXHelper.placeXAtGivenField(field, true);
                logic.getActionScheduler().scheduleActionsBasedOnField(field, NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE);
                logic.getNonogramState().increaseMadeSteps();
            }
        }
    }
}
