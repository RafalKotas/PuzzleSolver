package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogicParams;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.xplacement.NonogramFieldPlacingXHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed.SequenceRangeCorrectionWhenPlacingXsLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement.PlaceXsAroundLongestSequencesLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement.PlaceXsAtTooShortEmptySequencesLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement.PlaceXsAtUnreachableFieldsLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement.PlaceXsIfONearXWillBeginTooLongPossibleSequenceLogHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;

import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.util.NonogramLogicUtils.colouredSequenceInRowIsValid;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service.NonogramLogicService.rangesListIncludingAnotherRange;

@Slf4j
@Getter
@Setter
public class RowXPlacementHelperImpl implements RowXPlacementHelper, RefreshableRowHelper {

    private final NonogramRowLogic nonogramRowLogic;

    private final NonogramFieldPlacingXHelper nonogramFieldPlacingXHelper;

    private static final List<Integer> NOT_FOUND_EMPTY_FIELDS_RANGE_VALUE = List.of(-1, -1);

    private static final List<Integer> NOT_FOUND_COLOURED_FIELDS_RANGE_VALUE = List.of(-1, -1);

    public RowXPlacementHelperImpl(NonogramRowLogic nonogramRowLogic) {
        this.nonogramRowLogic = nonogramRowLogic;
        this.nonogramFieldPlacingXHelper = new NonogramFieldPlacingXHelper(
                this.nonogramRowLogic.getNonogramSolutionBoard(),
                this.nonogramRowLogic.getNonogramSolutionBoardWithMarks(),
                this.nonogramRowLogic.getBoardAccessHelper()
        );
    }

    @Override
    public void placeXsRowAtUnreachableFields(int rowIdx) {
        List<List<Integer>> rowSequencesRanges = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);

        List<String> initialState = nonogramRowLogic.getBoardAccessHelper().getRowCopy(rowIdx);
        List<List<Integer>> initialRanges = cloneAndMakeImmutable2DList(rowSequencesRanges);

        for (int columnIdx = 0; columnIdx < nonogramRowLogic.getNonogramRules().getWidth(); columnIdx++) {
            List<Integer> fieldAsRange = List.of(columnIdx, columnIdx);
            boolean isReachable = rangesListIncludingAnotherRange(rowSequencesRanges, fieldAsRange);

            if (!isReachable) {
                Field fieldToExclude = new Field(rowIdx, columnIdx);
                if (isFieldEmpty(nonogramRowLogic.getNonogramSolutionBoard(), fieldToExclude)) {
                    nonogramFieldPlacingXHelper.placeXAtGivenField(fieldToExclude);
                    nonogramRowLogic.getNonogramFieldExclusionHelper().excludeFieldInRow(fieldToExclude);
                    nonogramRowLogic.getActionScheduler().scheduleActionsBasedOnField(fieldToExclude, NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_ROW);
                    nonogramRowLogic.getNonogramState().increaseMadeSteps();
                } else if (NonogramLogicParams.SHOW_REPETITIONS) {
                    log.warn("X at unreachable field in row placed earlier!");
                }
            }
        }

        List<String> updatedState = nonogramRowLogic.getBoardAccessHelper().getRowCopy(rowIdx);
        if (!initialState.equals(updatedState)) {
            String tmpLog = PlaceXsAtUnreachableFieldsLogHelper.generateLog(
                    true,
                    rowIdx,
                    initialState,
                    updatedState,
                    initialRanges
            );
            nonogramRowLogic.setTmpLog(tmpLog);
            nonogramRowLogic.addLog();
        }
    }

    @Override
    public void placeXsAroundLongestSequencesInRow(int rowIdx) {
        int width = nonogramRowLogic.getNonogramRules().getWidth();
        int columnIdx = 1;

        while (columnIdx < width - 1) {
            Field field = new Field(rowIdx, columnIdx);

            if (isFieldColoured(nonogramRowLogic.getNonogramSolutionBoard(), field)) {
                List<Integer> colouredRange = findColouredSequenceRangeInRow(columnIdx, rowIdx);
                processColouredSequenceRangeInRow(rowIdx, colouredRange);
                columnIdx = colouredRange.get(1) + 1;
            } else {
                columnIdx++;
            }
        }
    }

    private List<Integer> findColouredSequenceRangeInRow(int startColumnIdx, int rowIdx) {
        int start = startColumnIdx;
        int end = startColumnIdx;
        int width = nonogramRowLogic.getNonogramRules().getWidth();

        int left = start - 1;
        while (left >= 0) {
            if (!isFieldColoured(nonogramRowLogic.getNonogramSolutionBoard(), new Field(rowIdx, left))) break;
            start = left;
            left--;
        }

        int right = end + 1;
        while (right < width) {
            if (!isFieldColoured(nonogramRowLogic.getNonogramSolutionBoard(), new Field(rowIdx, right))) break;
            end = right;
            right++;
        }

        return List.of(start, end);
    }

    private void processColouredSequenceRangeInRow(int rowIdx, List<Integer> colouredRange) {
        List<List<Integer>> rowRanges = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowLengths = nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx);

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
        List<String> initialRow = nonogramRowLogic.getBoardAccessHelper().getRowCopy(rowIdx);
        boolean anyXPlaced = false;

        for (int columnIdx : xEdges) {
            if (!nonogramRowLogic.getBoardAccessHelper().isColumnIndexValid(columnIdx)) continue;

            Field edgeField = new Field(rowIdx, columnIdx);
            if (isFieldEmpty(nonogramRowLogic.getNonogramSolutionBoard(), edgeField)) {
                nonogramFieldPlacingXHelper.placeXAtGivenField(edgeField);
                nonogramRowLogic.getNonogramFieldExclusionHelper().excludeFieldInRow(edgeField);
                nonogramRowLogic.getActionScheduler().scheduleActionsBasedOnField(
                        new Field(rowIdx, columnIdx), NonogramSolveAction.PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_ROW
                );
                nonogramRowLogic.getNonogramState().increaseMadeSteps();
                anyXPlaced = true;
            } else if (NonogramLogicParams.SHOW_REPETITIONS) {
                log.warn("X around longest sequence already placed at {}", edgeField);
            }
        }

        List<String> updatedRow = nonogramRowLogic.getBoardAccessHelper().getRowCopy(rowIdx);

        if (anyXPlaced) {
            String tmpLog = PlaceXsAroundLongestSequencesLogHelper.generateLog(
                    true,
                    rowIdx,
                    xEdges,
                    initialRow,
                    updatedRow,
                    onlyMatching
            );
            nonogramRowLogic.setTmpLog(tmpLog);
            nonogramRowLogic.addLog();
        }
    }

    private void excludeColouredFieldsBetweenXs(int rowIdx, List<Integer> range) {
        for (int columnIdx = range.get(0); columnIdx <= range.get(1); columnIdx++) {
            nonogramRowLogic.getNonogramFieldExclusionHelper().excludeFieldInRow(new Field(rowIdx, columnIdx));
            nonogramRowLogic.getNonogramState().increaseMadeSteps();
        }
    }

    private void updateLogicAfterXsPlacement(int rowIdx, int sequenceIndex, List<Integer> updatedRange) {
        List<Integer> oldRange = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx).get(sequenceIndex);
        if (!updatedRange.equals(oldRange)) {
            nonogramRowLogic.changeRowSequenceRange(rowIdx, sequenceIndex, updatedRange);

            List<Integer> rowSequencesLengths = nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
            List<String> boardRow = nonogramRowLogic.getBoardAccessHelper().getRowCopy(rowIdx);

            String tmpLog = SequenceRangeCorrectionWhenPlacingXsLogHelper.generateLog(
                    true,
                    rowIdx,
                    sequenceIndex,
                    oldRange,
                    updatedRange,
                    boardRow,
                    rowSequencesLengths
            );
            nonogramRowLogic.setTmpLog(tmpLog);
            nonogramRowLogic.addLog();

            nonogramRowLogic.excludeSequenceInRow(rowIdx, sequenceIndex);
        }
    }

    /**
     * Places Xs in a row between two existing Xs if the empty range is too short
     * for any allowed sequence. The logic follows:
     * <ol>
     *     <li>Identify candidate empty ranges between Xs in the row.</li>
     *     <li>For each such range, check whether only too-long sequences fit in it.</li>
     *     <li>If so, place Xs in all fields of that range, exclude those fields from logic,
     *     and schedule further solving actions.</li>
     *     <li>If any changes occurred, a log is generated.</li>
     * </ol>
     *
     * @param rowIdx the index of the row in which to attempt placing Xs
     */
    @Override
    public void placeXsRowAtTooShortEmptySequences(int rowIdx) {
        int width = nonogramRowLogic.getNonogramRules().getWidth();
        List<List<Integer>> sequenceRanges = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);
        List<Integer> sequencesLengths = nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<Integer> excludedSequenceIds = nonogramRowLogic.getRowsSequencesIdsNotToInclude().get(rowIdx);

        List<String> initialRow = nonogramRowLogic.getBoardAccessHelper().getRowCopy(rowIdx);

        List<List<Integer>> candidateRanges = findEmptyRangesBetweenXs(rowIdx, width);

        for (List<Integer> range : candidateRanges) {
            if (onlyTooLongSequencesFitInRange(sequenceRanges, sequencesLengths, excludedSequenceIds, range)) {
                markXsInRange(range, rowIdx);
            }
        }

        List<String> updatedRow = nonogramRowLogic.getBoardAccessHelper().getRowCopy(rowIdx);
        if (!initialRow.equals(updatedRow)) {
            String tmpLog = PlaceXsAtTooShortEmptySequencesLogHelper.generateLog(
                    true,
                    rowIdx,
                    initialRow,
                    updatedRow,
                    sequenceRanges,
                    sequencesLengths,
                    excludedSequenceIds
            );
            nonogramRowLogic.setTmpLog(tmpLog);
            nonogramRowLogic.addLog();
        }
    }

    /**
     * Finds all non-trivial empty ranges between two Xs in the given row.
     * A range is considered valid if:
     * <ul>
     *     <li>There is an X at the start and at the end of the range.</li>
     *     <li>The empty area between them has at least one cell.</li>
     * </ul>
     *
     * @param rowIdx the row index to scan
     * @param width the width of the board
     * @return list of empty ranges (as pairs of [start, end] column indices)
     */
    private List<List<Integer>> findEmptyRangesBetweenXs(int rowIdx, int width) {
        List<List<Integer>> emptyRanges = new ArrayList<>();
        int columnIdx = 0;

        while (columnIdx < width - 1) {
            columnIdx = findStartColumnWithX(columnIdx, rowIdx, width);
            if (columnIdx >= width - 1) break;

            int endColumnX = findEndColumnWithXAfterEmpty(columnIdx + 1, rowIdx, width);
            if (endColumnX != -1 && endColumnX > columnIdx + 1) {
                emptyRanges.add(List.of(columnIdx + 1, endColumnX - 1));
                columnIdx = endColumnX;
            } else {
                columnIdx = (endColumnX == -1) ? width : endColumnX;
            }
        }

        return emptyRanges;
    }

    /**
     * Finds the index of the first column at or after {@code startIdx} in the given row
     * that contains an X.
     *
     * @param startIdx the column index to begin the search from
     * @param rowIdx the row index to check
     * @param width the total width of the board
     * @return the index of the first column with an X, or {@code width} if none found
     */
    private int findStartColumnWithX(int startIdx, int rowIdx, int width) {
        while (startIdx < width && !isFieldWithX(nonogramRowLogic.getNonogramSolutionBoard(), new Field(rowIdx, startIdx))) {
            startIdx++;
        }
        return startIdx;
    }

    /**
     * Starting from {@code startIdx}, skips over empty fields in the specified row
     * and returns the index of the next column that contains an X.
     *
     * @param startIdx the column index wto start scanning from (after the initial X)
     * @param rowIdx the row index to check
     * @param width the total width of the board
     * @return the index of the column containing X or {@code -1} if not found
     */
    private int findEndColumnWithXAfterEmpty(int startIdx, int rowIdx, int width) {
        int cursor = startIdx;
        while (cursor < width && isFieldEmpty(nonogramRowLogic.getNonogramSolutionBoard(), new Field(rowIdx, cursor))) {
            cursor++;
        }

        if (cursor >= width || !isFieldWithX(nonogramRowLogic.getNonogramSolutionBoard(), new Field(rowIdx, cursor))) {
            return -1;
        }

        return cursor;
    }

    /**
     * Determines whether the given empty range can only fit sequences that are
     * too long for it. Used to decide whether the range should be filled with Xs.
     * <ul>
     *     <li>Filters out excluded sequences.</li>
     *     <li>Checks if any non-excluded sequence has a range containing this range.</li>
     *     <li>Verifies if all matching sequences are too long for the range.</li>
     * </ul>
     *
     * @param sequenceRanges list of allowed ranges for each sequence
     * @param sequenceLengths list of sequence lengths
     * @param excludedSequenceIds list of sequence indices that should be ignored
     * @param emptyRange the range of empty cells to evaluate
     * @return {@code true} if only too-long sequences match this range
     */
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

    /**
     * Places Xs in all empty cells of the given range in the specified row.
     * Also triggers necessary logic updates:
     * <ul>
     *     <li>Field is excluded from future row-based operations.</li>
     *     <li>Field-related solving actions are scheduled.</li>
     *     <li>Step counter is incremented.</li>
     * </ul>
     *
     * @param emptyRange the range of columns (as [start, end]) where Xs should be placed
     * @param rowIdx the row in which Xs should be placed
     */
    private void markXsInRange(List<Integer> emptyRange, int rowIdx) {
        for (int col = emptyRange.get(0); col <= emptyRange.get(1); col++) {
            Field field = new Field(rowIdx, col);
            if (!isFieldEmpty(nonogramRowLogic.getNonogramSolutionBoard(), field)) continue;

            nonogramFieldPlacingXHelper.placeXAtGivenField(field);
            nonogramRowLogic.getNonogramFieldExclusionHelper().excludeFieldInRow(field);
            nonogramRowLogic.getActionScheduler().scheduleActionsBasedOnField(field, NonogramSolveAction.PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES_IN_ROW);
            nonogramRowLogic.getNonogramState().increaseMadeSteps();
        }
    }

    @Override
    public void placeXsRowIfOWillMergeNearFieldsToTooLongColouredSequence(int rowIdx) {
        List<Integer> colouredFields = findColouredFieldsInRow(nonogramRowLogic.getNonogramSolutionBoard(), rowIdx);
        List<List<Integer>> colouredRanges = groupConsecutiveIndices(colouredFields);
        List<List<List<Integer>>> rangesWithExtras = createCandidateRangesAroundSequences(colouredRanges);

        List<String> initialRow = nonogramRowLogic.getBoardAccessHelper().getRowCopy(rowIdx);

        for (int i = 0; i < colouredRanges.size(); i++) {
            List<List<Integer>> currentWithExtras = rangesWithExtras.get(i);

            checkAndPlaceXBeforeInRow(colouredRanges, currentWithExtras.get(0), i, rowIdx);
            checkAndPlaceXAfterInRow(colouredRanges, currentWithExtras.get(1), i, rowIdx);
        }

        List<String> updatedRow = nonogramRowLogic.getBoardAccessHelper().getRowCopy(rowIdx);

        if (!initialRow.equals(updatedRow)) {
            // TODO - create log helper for this action
            String tmpLog = PlaceXsIfONearXWillBeginTooLongPossibleSequenceLogHelper.generateLog(
                    true, // isRow = true
                    rowIdx,
                    initialRow,
                    updatedRow,
                    nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx),
                    nonogramRowLogic.getRowsSequencesRanges().get(rowIdx)
            );
            nonogramRowLogic.setTmpLog(tmpLog);
            nonogramRowLogic.addLog();
        }
    }

    private void checkAndPlaceXBeforeInRow(List<List<Integer>> colouredRanges, List<Integer> rangeWithExtra, int idx, int rowIdx) {
        List<Integer> merged = (idx > 0)
                ? mergeWithPreviousIfAdjacent(colouredRanges.get(idx - 1), rangeWithExtra)
                : rangeWithExtra;

        int col = rangeWithExtra.get(0);
        Field field = new Field(rowIdx, col);

        if (shouldPlaceXInRow(rowIdx, col, field, merged)) {
            nonogramFieldPlacingXHelper.placeXAtGivenField(field);
            nonogramRowLogic.getNonogramFieldExclusionHelper().excludeFieldInRow(field);
            nonogramRowLogic.getActionScheduler().scheduleActionsBasedOnField(field, NonogramSolveAction.PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_ROW);
            nonogramRowLogic.getNonogramState().increaseMadeSteps();
        } else if (NonogramLogicParams.SHOW_REPETITIONS) {
            log.info("X because \"O\" will create too long sequence in row placed earlier!");
        }
    }

    private void checkAndPlaceXAfterInRow(List<List<Integer>> colouredRanges, List<Integer> rangeWithExtra, int idx, int rowIdx) {
        int nextCol = rangeWithExtra.get(1);
        if (nextCol == nonogramRowLogic.getNonogramRules().getWidth()) return;

        Field field = new Field(rowIdx, nextCol);
        List<Integer> merged = (idx < colouredRanges.size() - 1)
                ? mergeWithNextIfAdjacent(rangeWithExtra, colouredRanges.get(idx + 1))
                : rangeWithExtra;

        if (shouldPlaceXInRow(rowIdx, nextCol, field, merged)) {
            nonogramFieldPlacingXHelper.placeXAtGivenField(field);
            nonogramRowLogic.getNonogramFieldExclusionHelper().excludeFieldInRow(field);
            nonogramRowLogic.getActionScheduler().scheduleActionsBasedOnField(field, NonogramSolveAction.PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_ROW);
            nonogramRowLogic.getNonogramState().increaseMadeSteps();
        } else if (NonogramLogicParams.SHOW_REPETITIONS) {
            log.info("X because \"O\" will create too long sequence in row placed earlier!");
        }
    }

    private boolean shouldPlaceXInRow(int rowIdx, int columnIdx, Field field, List<Integer> range) {
        return nonogramRowLogic.getBoardAccessHelper().isColumnIndexValid(columnIdx)
                && isFieldEmpty(nonogramRowLogic.getNonogramSolutionBoard(), field)
                && !colouredSequenceInRowIsValid(range,
                nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx),
                nonogramRowLogic.getRowsSequencesRanges().get(rowIdx));
    }

    @Override
    public void placeXsRowIfONearXWillBeginTooLongPossibleColouredSequence(int rowIdx) {
        List<String> rowBefore = nonogramRowLogic.getBoardAccessHelper().getRowCopy(rowIdx);

        checkDirectionAndPlaceXsInRow(rowIdx, true);  // from left
        checkDirectionAndPlaceXsInRow(rowIdx, false); // from right

        List<String> rowAfter = nonogramRowLogic.getBoardAccessHelper().getRowCopy(rowIdx);

        if (!rowBefore.equals(rowAfter)) {
            String tmpLog = PlaceXsIfONearXWillBeginTooLongPossibleSequenceLogHelper.generateLog(
                    true, //isRow = true
                    rowIdx,
                    rowBefore,
                    rowAfter,
                    nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx),
                    nonogramRowLogic.getRowsSequencesRanges().get(rowIdx)
            );
            nonogramRowLogic.setTmpLog(tmpLog);
            nonogramRowLogic.addLog();
        }
    }

    private void checkDirectionAndPlaceXsInRow(int rowIdx, boolean fromLeft) {
        int start = fromLeft ? 0 : nonogramRowLogic.getNonogramRules().getWidth() - 1;
        int end = fromLeft ? nonogramRowLogic.getNonogramRules().getWidth() : -1;
        int step = fromLeft ? 1 : -1;

        for (int columnIdx = start; fromLeft ? columnIdx < end : columnIdx > end; columnIdx += step) {
            Field xField = new Field(rowIdx, columnIdx);
            if (!isFieldWithX(nonogramRowLogic.getNonogramSolutionBoard(), xField)) continue;

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
        return getFieldRange(
                new Field(xField.getRowIdx(), xField.getColumnIdx() + 1),
                i -> i + 1,
                f -> isFieldEmpty(nonogramRowLogic.getNonogramSolutionBoard(), f)
        );
    }

    private List<Integer> getEmptyFieldsRangeFromXToFirstColouredFieldToLeft(Field xField) {
        return getFieldRange(
                new Field(xField.getRowIdx(), xField.getColumnIdx() - 1),
                i -> i - 1,
                f -> isFieldEmpty(nonogramRowLogic.getNonogramSolutionBoard(), f)
        );
    }

    private List<Integer> getColouredFieldsRangeNearEmptySequenceFromLeft(Field startField) {
        return getFieldRange(
                new Field(startField.getRowIdx(), startField.getColumnIdx()),
                i -> i + 1,
                f -> isFieldColoured(nonogramRowLogic.getNonogramSolutionBoard(), f)
        );
    }

    private List<Integer> getColouredFieldsRangeNearEmptySequenceToLeft(Field startField) {
        return getFieldRange(
                new Field(startField.getRowIdx(), startField.getColumnIdx()),
                i -> i - 1,
                f -> isFieldColoured(nonogramRowLogic.getNonogramSolutionBoard(), f)
        );
    }

    private List<Integer> getFieldRange(Field startField,
                                        IntUnaryOperator directionFn,
                                        Predicate<Field> matchCondition) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        Field f = new Field(startField.getRowIdx(), startField.getColumnIdx());
        while (nonogramRowLogic.getBoardAccessHelper().areFieldIndexesValid(f) && matchCondition.test(f)) {
            int c = f.getColumnIdx();
            if (c < min) min = c;
            if (c > max) max = c;
            f.setColumnIdx(directionFn.applyAsInt(c));
        }

        if (min == Integer.MAX_VALUE && max == Integer.MIN_VALUE) return List.of(-1, -1);
        return List.of(min, max);
    }

    private void evaluateAndMaybePlaceX(int rowIdx, List<Integer> emptyRange, List<Integer> colouredRange, boolean isFromLeft) {
        List<Integer> lengths = nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> ranges = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);

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

            if (isFieldEmpty(nonogramRowLogic.getNonogramSolutionBoard(), field)) {
                nonogramFieldPlacingXHelper.placeXAtGivenField(field);
                nonogramRowLogic.getNonogramFieldExclusionHelper().excludeFieldInRow(field);
                nonogramRowLogic.getActionScheduler().scheduleActionsBasedOnField(field, NonogramSolveAction.PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_ROW);
                nonogramRowLogic.getNonogramState().increaseMadeSteps();
            }
        }
    }

    @Override
    public void refreshFrom(NonogramRowLogic logicToCopy) {
        nonogramRowLogic.setRowsSequencesRanges(logicToCopy.getRowsSequencesRanges());
        nonogramRowLogic.setRowsFieldsNotToInclude(logicToCopy.getRowsFieldsNotToInclude());
    }
}
