package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogicParams;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.xplacement.NonogramFieldPlacingXHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.common.CommonXPlacementHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed.SequenceRangeCorrectionWhenPlacingXsLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement.*;
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
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.util.NonogramLogicUtils.colouredSequenceInColumnIsValid;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service.NonogramLogicService.rangesListIncludingAnotherRange;

@Slf4j
@Getter
@Setter
public class ColumnXPlacementHelperImpl  extends CommonXPlacementHelper implements ColumnXPlacementHelper, RefreshableColumnHelper {

    private final NonogramColumnLogic nonogramColumnLogic;

    private final NonogramFieldPlacingXHelper nonogramFieldPlacingXHelper;

    private static final List<Integer> NOT_FOUND_EMPTY_FIELDS_RANGE_VALUE = List.of(-1, -1);
    private static final List<Integer> NOT_FOUND_COLOURED_FIELDS_RANGE_VALUE = List.of(-1, -1);

    public ColumnXPlacementHelperImpl(NonogramColumnLogic nonogramColumnLogic) {
        this.nonogramColumnLogic = new NonogramColumnLogic(nonogramColumnLogic);
        this.nonogramFieldPlacingXHelper = new NonogramFieldPlacingXHelper(
                this.nonogramColumnLogic.getNonogramSolutionBoard(),
                this.nonogramColumnLogic.getNonogramSolutionBoardWithMarks(),
                this.nonogramColumnLogic.getBoardAccessHelper()
        );
    }

    /**
     * Places Xs in all fields of a given column that cannot belong to any currently valid sequence range.
     * <p>
     * For each cell in the column, the method checks if its row index is included in any of the sequence ranges.
     * If the field is not reachable and currently empty, an X is placed in it, the field is excluded from
     * future processing, and appropriate actions are scheduled.
     * </p>
     * <p>
     * If changes to the column state were made, a log entry describing the action is recorded.
     * </p>
     *
     * @param columnIdx index of the column to process for unreachable field exclusion
     */
    @Override
    public void placeXsColumnAtUnreachableFields(int columnIdx) {
        List<String> initialColumn = nonogramColumnLogic.getBoardAccessHelper().getColumnCopy(columnIdx);
        List<List<Integer>> initialRanges = cloneAndMakeImmutable2DList(nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx));

        List<List<Integer>> colSequencesRanges = nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx);

        for (int rowIdx = 0; rowIdx < nonogramColumnLogic.getNonogramRules().getHeight(); rowIdx++) {
            List<Integer> fieldAsRange = List.of(rowIdx, rowIdx);
            boolean isReachable = rangesListIncludingAnotherRange(colSequencesRanges, fieldAsRange);

            if (!isReachable) {
                Field fieldToExclude = new Field(rowIdx, columnIdx);
                if (isFieldEmpty(nonogramColumnLogic.getNonogramSolutionBoard(), fieldToExclude)) {
                    nonogramFieldPlacingXHelper.placeXAtGivenField(fieldToExclude);

                    nonogramColumnLogic.getActionScheduler().scheduleActionsBasedOnField(fieldToExclude, NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_COLUMN);
                    nonogramColumnLogic.getNonogramState().increaseMadeSteps();
                } else if (NonogramLogicParams.SHOW_REPETITIONS) {
                    log.warn("X at unreachable field in column placed earlier!");
                }
            }
        }

        List<String> updatedColumn = nonogramColumnLogic.getBoardAccessHelper().getColumnCopy(columnIdx);
        if (!initialColumn.equals(updatedColumn)) {
            PlaceXBaseLogContext placeXBaseLogContext = new PlaceXBaseLogContext(
                    false,
                    columnIdx,
                    initialColumn,
                    updatedColumn
            );
            String tmpLog = PlaceXsAtUnreachableFieldsLogHelper.generateLog(
                    placeXBaseLogContext,
                    initialRanges
            );
            setAndAddLog(tmpLog);
        }
    }

    /**
     * Scans the specified column to identify coloured (filled) sequences and evaluates their alignment
     * with the expected sequence constraints.
     * <p>
     * For each continuous coloured segment found on the board, the method attempts to:
     * <ul>
     *     <li>Determine the full range of the coloured segment,</li>
     *     <li>Match it against known valid ranges and sequence lengths,</li>
     *     <li>If exactly one matching sequence exists and the length matches, place Xs around the segment and update its range,</li>
     *     <li>If multiple sequences exist and the segment matches the longest, place Xs around it without updating the range.</li>
     * </ul>
     * <p>
     * The method also logs all relevant actions and state changes for debugging or user feedback.
     *
     * @param columnIdx index of the column to be processed for placing Xs around longest matching coloured sequences
     */
    @Override
    public void placeXsAroundLongestSequencesInColumn(int columnIdx) {
        int height = nonogramColumnLogic.getNonogramRules().getHeight();
        int rowIdx = 0;

        while (rowIdx < height) {
            Field field = new Field(rowIdx, columnIdx);
            if (isFieldColoured(nonogramColumnLogic.getNonogramSolutionBoard(), field)) {
                List<Integer> colouredRange = findColouredSequenceRangeInColumn(rowIdx, columnIdx);
                processColouredSequenceRangeInColumn(columnIdx, colouredRange);
                rowIdx = colouredRange.get(1) + 1;
            } else {
                rowIdx++;
            }
        }
    }

    /**
     * Finds the full range of a continuous coloured (filled) sequence in the given column, starting from a specific row.
     * <p>
     * The method expands upward and downward from the provided start row index until it reaches the first
     * uncoloured (non-'O') cell or the edge of the board. It returns the bounds (inclusive) of the detected sequence.
     *
     * @param startRowIdx the row index where the coloured sequence is assumed to begin
     * @param columnIdx the index of the column to search in
     * @return a list containing two integers representing the start and end row indices of the coloured sequence
     */
    private List<Integer> findColouredSequenceRangeInColumn(int startRowIdx, int columnIdx) {
        int end = startRowIdx;

        while (end + 1 < nonogramColumnLogic.getNonogramRules().getHeight() &&
                isFieldColoured(nonogramColumnLogic.getNonogramSolutionBoard(), new Field(end + 1, columnIdx))) {
            end++;
        }

        return List.of(startRowIdx, end);
    }

    /**
     * Processes a detected coloured sequence in the specified column and attempts to place Xs around it
     * based on how well it matches expected column sequences.
     * <p>
     * If exactly one matching sequence range contains the coloured segment and the lengths match,
     * the method calls {@code placeXsAndUpdateSingleSequence} to place Xs and update logic.
     * If multiple sequences match the length of the segment and it equals the maximum expected length,
     * it places Xs around the longest without updating logic ({@code placeXsAroundLongestSequence}).
     *
     * @param columnIdx the index of the column containing the coloured sequence
     * @param colouredRange a list of two integers indicating the start and end of the coloured segment
     */
    private void processColouredSequenceRangeInColumn(int columnIdx, List<Integer> colouredRange) {
        List<List<Integer>> columnRanges = nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnLengths = nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);

        int lengthOnBoard = rangeLength(colouredRange);

        List<Integer> matchingIndices = new ArrayList<>();
        List<Integer> matchingLengths = new ArrayList<>();

        for (int seqIdx = 0; seqIdx < columnRanges.size(); seqIdx++) {
            if (rangeInsideAnotherRange(colouredRange, columnRanges.get(seqIdx))
                    && lengthOnBoard <= columnLengths.get(seqIdx)) {
                matchingIndices.add(seqIdx);
                matchingLengths.add(columnLengths.get(seqIdx));
            }
        }

        List<Integer> edgeXs = List.of(colouredRange.get(0) - 1, colouredRange.get(1) + 1);

        if (!nonogramColumnLogic.getColumnsSequencesIdsNotToInclude().get(columnIdx).contains(matchingIndices.get(0)) &&
                matchingIndices.size() == 1 && lengthOnBoard == matchingLengths.get(0)) {
            placeXsAndUpdateSingleSequence(columnIdx, edgeXs, matchingIndices.get(0));
        } else if (matchingLengths.size() > 1 && lengthOnBoard == Collections.max(matchingLengths)) {
            placeXsAroundLongestSequence(columnIdx, edgeXs, false);
        }
    }

    /**
     * Places Xs around a coloured sequence that exactly matches a single expected sequence and updates the sequence range.
     * <p>
     * This method is invoked when there is only one matching sequence whose length equals the length of a detected
     * coloured segment. It places Xs at both ends of the segment, updates the logic's sequence range, and excludes
     * the fields in the sequence from further consideration.
     *
     * @param columnIdx the index of the column being processed
     * @param xEdges a list containing two integers: the row indices where Xs should be placed (before and after the sequence)
     * @param seqIdx the index of the matching sequence to update and exclude
     */
    private void placeXsAndUpdateSingleSequence(int columnIdx, List<Integer> xEdges, int seqIdx) {
        placeXsAroundLongestSequence(columnIdx, xEdges, true);

        List<Integer> updatedRange = List.of(xEdges.get(0) + 1, xEdges.get(1) - 1);
        excludeColouredFieldsBetweenXs(columnIdx, updatedRange);

        updateLogicAfterXsPlacement(columnIdx, seqIdx, updatedRange);
    }

    /**
     * Places Xs at the edges of a coloured sequence in a column and records the action if any X is placed.
     * <p>
     * For each edge index (above and below the coloured segment), this method checks if it is a valid and empty cell.
     * If so, it places an X, excludes the field, schedules corresponding actions, and increments step count.
     * The change is logged if at least one X is placed.
     *
     * @param columnIdx the index of the column being processed
     * @param xEdges a list of two row indices indicating where to place Xs (typically one before and one after a coloured segment)
     * @param onlyMatching whether the X placement corresponds to a unique sequence match (affects logging semantics)
     */
    private void placeXsAroundLongestSequence(int columnIdx, List<Integer> xEdges, boolean onlyMatching) {
        List<String> columnBefore = nonogramColumnLogic.getBoardAccessHelper().getColumnCopy(columnIdx);
        boolean anyXPlaced = false;

        for (int rowIdx : xEdges) {
            if (!nonogramColumnLogic.getBoardAccessHelper().isRowIndexValid(rowIdx)) continue;

            Field edgeField = new Field(rowIdx, columnIdx);
            if (isFieldEmpty(nonogramColumnLogic.getNonogramSolutionBoard(), edgeField)) {
                nonogramFieldPlacingXHelper.placeXAtGivenField(edgeField);
                nonogramColumnLogic.getNonogramFieldExclusionHelper().excludeFieldInColumn(edgeField);
                nonogramColumnLogic.getActionScheduler().scheduleActionsBasedOnField(
                        new Field(rowIdx, columnIdx),
                        NonogramSolveAction.PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_COLUMN
                );
                nonogramColumnLogic.getNonogramState().increaseMadeSteps();
                anyXPlaced = true;
            } else if (NonogramLogicParams.SHOW_REPETITIONS) {
                log.warn("X around longest sequence already placed at {}", edgeField);
            }
        }

        List<String> columnAfter = nonogramColumnLogic.getBoardAccessHelper().getColumnCopy(columnIdx);

        PlaceXBaseLogContext placeXBaseLogContext = new PlaceXBaseLogContext(
                false,
                columnIdx,
                columnBefore,
                columnAfter
        );
        if (anyXPlaced) {
            String tmpLog = PlaceXsAroundLongestSequencesLogHelper.generateLog(
                    placeXBaseLogContext,
                    xEdges,
                    onlyMatching
            );
            setAndAddLog(tmpLog);
        }
    }

    /**
     * Excludes all coloured fields within a given range in the specified column from further logic processing.
     * <p>
     * This method is typically used after placing Xs at both ends of a coloured sequence to mark all fields
     * within the sequence as processed. Each excluded field increments the logic's step counter.
     *
     * @param columnIdx the index of the column being processed
     * @param range the range of row indices (inclusive) representing the coloured sequence
     */
    private void excludeColouredFieldsBetweenXs(int columnIdx, List<Integer> range) {
        for (int rowIdx = range.get(0); rowIdx <= range.get(1); rowIdx++) {
            nonogramColumnLogic.getNonogramFieldExclusionHelper().excludeFieldInColumn(new Field(rowIdx, columnIdx));
            nonogramColumnLogic.getNonogramState().increaseMadeSteps();
        }
    }

    /**
     * Updates the internal logic after Xs have been placed around a coloured sequence.
     * <p>
     * If the new sequence range differs from the previously stored one, it is updated and logged.
     * The sequence is also excluded from further solving, and actions are scheduled for adjacent cells.
     *
     * @param columnIdx the index of the column being processed
     * @param sequenceIndex the index of the sequence being updated
     * @param newRange the new detected range for the sequence after placing Xs
     */
    private void updateLogicAfterXsPlacement(int columnIdx, int sequenceIndex, List<Integer> newRange) {
        List<Integer> oldRange = nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx).get(sequenceIndex);
        if (!newRange.equals(oldRange)) {
            nonogramColumnLogic.changeColumnSequenceRange(columnIdx, sequenceIndex, newRange);

            List<Integer> sequencesLengths = nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
            List<String> columnState = nonogramColumnLogic.getBoardAccessHelper().getColumnCopy(columnIdx);

            String tmpLog = SequenceRangeCorrectionWhenPlacingXsLogHelper.generateLog(
                    false,
                    columnIdx,
                    sequenceIndex,
                    oldRange,
                    newRange,
                    columnState,
                    sequencesLengths
            );
            setAndAddLog(tmpLog);
        }

        nonogramColumnLogic.excludeSequenceInColumn(columnIdx, sequenceIndex);

        Field topEdge = new Field(newRange.get(0) - 1, columnIdx);
        Field bottomEdge = new Field(newRange.get(1) + 1, columnIdx);

        if (nonogramColumnLogic.getBoardAccessHelper().isRowIndexValid(topEdge.getRowIdx())) {
            nonogramColumnLogic.getActionScheduler().scheduleActionsBasedOnField(topEdge, NonogramSolveAction.PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_COLUMN);
        }

        if (nonogramColumnLogic.getBoardAccessHelper().isRowIndexValid(bottomEdge.getRowIdx())) {
            nonogramColumnLogic.getActionScheduler().scheduleActionsBasedOnField(bottomEdge, NonogramSolveAction.PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_COLUMN);
        }
    }

    /**
     * Places Xs in all empty fields between X-marked fields in a column,
     * if no sequence can possibly fit in the range except those that are too long.
     * <p>
     * Steps:
     * <ol>
     *   <li>Get all ranges of empty fields between pairs of Xs in the column.</li>
     *   <li>Filter out ranges that are too short to hold any valid sequence (i.e., endRow == startRow + 1).</li>
     *   <li>For each such range, check if only too-long sequences could fit.</li>
     *   <li>If so, mark Xs in the entire range.</li>
     *   <li>If any changes were made, generate and store a log entry.</li>
     * </ol>
     *
     * @param columnIdx index of the column to process
     */
    @Override
    public void placeXsColumnAtTooShortEmptySequences(int columnIdx) {
        int height = nonogramColumnLogic.getNonogramRules().getHeight();
        List<List<Integer>> sequencesRanges = nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> sequencesLengths = nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<Integer> excludedSequenceIndexes = nonogramColumnLogic.getColumnsSequencesIdsNotToInclude().get(columnIdx);

        List<String> initialColumn = nonogramColumnLogic.getBoardAccessHelper().getColumnCopy(columnIdx);

        List<List<Integer>> candidateRanges = findEmptyRangesBetweenXs(columnIdx, height);

        for (List<Integer> range : candidateRanges) {
            if (onlyTooLongSequencesFitInRange(sequencesRanges, sequencesLengths, excludedSequenceIndexes, range)) {
                markXsInRange(range, columnIdx);
            }
        }

        List<String> updatedColumn = nonogramColumnLogic.getBoardAccessHelper().getColumnCopy(columnIdx);
        if (!initialColumn.equals(updatedColumn)) {
            PlaceXBaseLogContext placeXBaseLogContext = new PlaceXBaseLogContext(
                    false,
                    columnIdx,
                    initialColumn,
                    updatedColumn
            );
            String tmpLog = PlaceXsAtTooShortEmptySequencesLogHelper.generateLog(
                    placeXBaseLogContext,
                    sequencesRanges,
                    sequencesLengths,
                    excludedSequenceIndexes);
            setAndAddLog(tmpLog);
        }
    }

    /**
     * Finds ranges of empty fields in a column that are located between two X-marked fields.
     * <p>
     * Iterates through the column from top to bottom, and:
     * <ol>
     *   <li>Searches for a row that contains an X (start boundary).</li>
     *   <li>Skips over empty fields until a non-empty field or another X is found (end boundary).</li>
     *   <li>If a valid pair of Xs is found and at least one empty field lies between them,
     *       creates a range representing those empty fields.</li>
     * </ol>
     * Only ranges of length at least 1 are returned.
     *
     * @param columnIdx the index of the column to search
     * @param height the total number of rows in the board
     * @return a list of empty ranges (each as [startRow, endRow]) between two Xs
     */
    private List<List<Integer>> findEmptyRangesBetweenXs(int columnIdx, int height) {
        List<List<Integer>> emptyRanges = new ArrayList<>();
        int rowIdx = 0;

        while (rowIdx < height - 1) {
            rowIdx = findStartRowWithX(rowIdx, columnIdx, height);
            if (rowIdx >= height - 1) break;

            int endRowX = findEndRowWithXAfterEmpty(rowIdx + 1, columnIdx, height);
            if (endRowX != -1 && endRowX > rowIdx + 1) {
                emptyRanges.add(List.of(rowIdx + 1, endRowX - 1));
                rowIdx = endRowX;
            } else {
                rowIdx = (endRowX == -1) ? height : endRowX;
            }
        }

        return emptyRanges;
    }

    /**
     * Finds the next row index starting from {@code startIdx} (inclusive)
     * that contains an X in the specified column.
     *
     * @param startIdx the row index to start the search from
     * @param colIdx   the column index to check
     * @param height   the total height of the board
     * @return the index of the next row containing an X, or {@code height} if none is found
     */
    private int findStartRowWithX(int startIdx, int colIdx, int height) {
        while (startIdx < height && !isFieldWithX(nonogramColumnLogic.getNonogramSolutionBoard(), new Field(startIdx, colIdx))) {
            startIdx++;
        }
        return startIdx;
    }

    /**
     * Starting from {@code startIdx}, skips over empty fields in the specified column
     * and returns the index of the next row that contains an X.
     *
     * @param startIdx the row index to start scanning from (after the initial X)
     * @param colIdx   the column index to check
     * @param height   the total height of the board
     * @return the index of the row with the next X, or {@code -1} if not found
     */
    private int findEndRowWithXAfterEmpty(int startIdx, int colIdx, int height) {
        int cursor = startIdx;
        while (cursor < height && isFieldEmpty(nonogramColumnLogic.getNonogramSolutionBoard(), new Field(cursor, colIdx))) {
            cursor++;
        }

        if (cursor >= height || !isFieldWithX(nonogramColumnLogic.getNonogramSolutionBoard(), new Field(cursor, colIdx))) {
            return -1;
        }

        return cursor;
    }

    /**
     * Marks all empty fields in the given column range with Xs.
     * <p>
     * Also:
     * <ul>
     *   <li>Excludes each field from further consideration in the column.</li>
     *   <li>Schedules solving actions based on the marked field.</li>
     *   <li>Increments the made steps counter.</li>
     * </ul>
     *
     * @param emptyRange the range of rows (inclusive) in which to place Xs
     * @param colIdx     the column index where Xs should be placed
     */
    private void markXsInRange(List<Integer> emptyRange, int colIdx) {
        for (int row = emptyRange.get(0); row <= emptyRange.get(1); row++) {
            Field field = new Field(row, colIdx);
            nonogramFieldPlacingXHelper.placeXAtGivenField(field);
            nonogramColumnLogic.getNonogramFieldExclusionHelper().excludeFieldInColumn(field);
            nonogramColumnLogic.getActionScheduler().scheduleActionsBasedOnField(field, NonogramSolveAction.PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES_IN_COLUMN);
            nonogramColumnLogic.getNonogramState().increaseMadeSteps();
        }
    }

    @Override
    public void placeXsColumnIfOWillMergeNearFieldsToTooLongColouredSequence(int columnIdx) {
        List<Integer> colouredFields = findColouredFieldsInColumn(nonogramColumnLogic.getNonogramSolutionBoard(), columnIdx);
        List<List<Integer>> colouredRanges = groupConsecutiveIndices(colouredFields);
        List<List<List<Integer>>> rangesWithExtras = createCandidateRangesAroundSequences(colouredRanges);

        List<String> columnBefore = nonogramColumnLogic.getBoardAccessHelper().getColumnCopy(columnIdx);

        for (int i = 0; i < colouredRanges.size(); i++) {
            List<List<Integer>> currentWithExtras = rangesWithExtras.get(i);

            checkAndPlaceXBefore(colouredRanges, currentWithExtras.get(0), i, columnIdx);
            checkAndPlaceXAfter(colouredRanges, currentWithExtras.get(1), i, columnIdx);
        }

        List<String> columnAfter = nonogramColumnLogic.getBoardAccessHelper().getColumnCopy(columnIdx);

        if (!columnBefore.equals(columnAfter)) {
            PlaceXBaseLogContext placeXBaseLogContext = new PlaceXBaseLogContext(
                    false,
                    columnIdx,
                    columnBefore,
                    columnAfter
            );
            String tmpLog = PlaceXsIfONearXWillBeginTooLongPossibleSequenceLogHelper.generateLog(
                    placeXBaseLogContext,
                    nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx),
                    nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx)
            );
            setAndAddLog(tmpLog);
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
            nonogramColumnLogic.getNonogramFieldExclusionHelper().excludeFieldInColumn(field);
            nonogramColumnLogic.getActionScheduler().scheduleActionsBasedOnField(field, NonogramSolveAction.PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_COLUMN);
            nonogramColumnLogic.getNonogramState().increaseMadeSteps();
        } else if (NonogramLogicParams.SHOW_REPETITIONS) {
            log.info("X because \"O\" will create too long sequence in column placed earlier!");
        }
    }

    private void checkAndPlaceXAfter(List<List<Integer>> colouredRanges, List<Integer> rangeWithExtra, int idx, int colIdx) {
        int nextRow = rangeWithExtra.get(1);
        if (nextRow == nonogramColumnLogic.getNonogramRules().getHeight()) {
            return;
        }

        Field field = new Field(nextRow, colIdx);
        List<Integer> merged = (idx < colouredRanges.size() - 1)
                ? mergeWithNextIfAdjacent(rangeWithExtra, colouredRanges.get(idx + 1))
                : rangeWithExtra;

        if (shouldPlaceX(nextRow, merged, colIdx, field)) {
            nonogramFieldPlacingXHelper.placeXAtGivenField(field);
            nonogramColumnLogic.getNonogramFieldExclusionHelper().excludeFieldInColumn(field);
            nonogramColumnLogic.getActionScheduler().scheduleActionsBasedOnField(field, NonogramSolveAction.PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_COLUMN);
            nonogramColumnLogic.getNonogramState().increaseMadeSteps();
        } else if (NonogramLogicParams.SHOW_REPETITIONS) {
            log.info("X because \"O\" will create too long sequence in column placed earlier!");
        }
    }

    private boolean shouldPlaceX(int rowIdx, List<Integer> range, int columnIdx, Field field) {
        return nonogramColumnLogic.getBoardAccessHelper().isRowIndexValid(rowIdx)
                && isFieldEmpty(nonogramColumnLogic.getNonogramSolutionBoard(), field)
                && !colouredSequenceInColumnIsValid(range,
                nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx),
                nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx));
    }

    @Override
    public void placeXsColumnIfONearXWillBeginTooLongPossibleColouredSequence(int columnIdx) {
        List<String> initialColumn = nonogramColumnLogic.getBoardAccessHelper().getColumnCopy(columnIdx);

        checkDirectionAndPlaceXs(columnIdx, true);
        checkDirectionAndPlaceXs(columnIdx, false);

        List<String> updatedColumn = nonogramColumnLogic.getBoardAccessHelper().getColumnCopy(columnIdx);

        if (!initialColumn.equals(updatedColumn)) {
            PlaceXBaseLogContext placeXBaseLogContext = new PlaceXBaseLogContext(
                    false,
                    columnIdx,
                    initialColumn,
                    updatedColumn
            );
            String tmpLog = PlaceXsIfONearXWillBeginTooLongPossibleSequenceLogHelper.generateLog(
                    placeXBaseLogContext,
                    nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx),
                    nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx)
            );
            setAndAddLog(tmpLog);
        }
    }

    private void checkDirectionAndPlaceXs(int columnIdx, boolean fromTop) {
        int start = fromTop ? 0 : nonogramColumnLogic.getNonogramRules().getHeight() - 1;
        int end = fromTop ? nonogramColumnLogic.getNonogramRules().getHeight() : -1;
        int step = fromTop ? 1 : -1;

        for (int rowIdx = start; fromTop ? rowIdx < end : rowIdx > end; rowIdx += step) {
            tryPlaceXNearExtremeInDirection(columnIdx, rowIdx, fromTop);
        }
    }

    private void tryPlaceXNearExtremeInDirection(int columnIdx, int rowIdx, boolean isFromTop) {
        Field xField = new Field(rowIdx, columnIdx);
        if (!isFieldWithX(nonogramColumnLogic.getNonogramSolutionBoard(), xField)) return;

        List<Integer> emptyRange = isFromTop
                ? getEmptyFieldsRangeFromXToFirstColouredFieldToBottom(xField)
                : getEmptyFieldsRangeFromXToFirstColouredFieldToTop(xField);

        if (emptyRange.equals(NOT_FOUND_EMPTY_FIELDS_RANGE_VALUE)) return;

        Field colouredStart = isFromTop
                ? new Field(emptyRange.get(1) + 1, columnIdx)
                : new Field(emptyRange.get(0) - 1, columnIdx);

        List<Integer> colouredRange = isFromTop
                ? getColouredFieldsRangeNearEmptySequenceToBottom(colouredStart)
                : getColouredFieldsRangeNearEmptySequenceToTop(colouredStart);

        if (colouredRange.equals(NOT_FOUND_COLOURED_FIELDS_RANGE_VALUE)) return;

        evaluateAndMaybePlaceX(columnIdx, emptyRange, colouredRange, isFromTop);
    }

    private List<Integer> getEmptyFieldsRangeFromXToFirstColouredFieldToBottom(Field xField) {
        return getFieldRange(
                new Field(xField.getRowIdx() + 1, xField.getColumnIdx()),
                i -> i + 1,
                f -> isFieldEmpty(nonogramColumnLogic.getNonogramSolutionBoard(), f)
        );
    }

    private List<Integer> getEmptyFieldsRangeFromXToFirstColouredFieldToTop(Field xField) {
        return getFieldRange(
                new Field(xField.getRowIdx() - 1, xField.getColumnIdx()),
                i -> i - 1,
                f -> isFieldEmpty(nonogramColumnLogic.getNonogramSolutionBoard(), f)
        );
    }

    private List<Integer> getColouredFieldsRangeNearEmptySequenceToBottom(Field startField) {
        return getFieldRange(
                new Field(startField.getRowIdx(), startField.getColumnIdx()),
                i -> i + 1,
                f -> isFieldColoured(nonogramColumnLogic.getNonogramSolutionBoard(), f)
        );
    }

    private List<Integer> getColouredFieldsRangeNearEmptySequenceToTop(Field startField) {
        return getFieldRange(
                new Field(startField.getRowIdx(), startField.getColumnIdx()),
                i -> i - 1,
                f -> isFieldColoured(nonogramColumnLogic.getNonogramSolutionBoard(), f)
        );
    }

    private List<Integer> getFieldRange(Field startField,
                                        IntUnaryOperator directionFn,
                                        Predicate<Field> matchCondition) {
        Range range = computeMinMaxRowRange(startField, directionFn, matchCondition);
        if (range.isEmpty()) {
            return List.of(-1, -1);
        }
        return List.of(range.min(), range.max());
    }

    private Range computeMinMaxRowRange(Field startField,
                                        IntUnaryOperator directionFn,
                                        Predicate<Field> matchCondition) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        Field f = new Field(startField.getRowIdx(), startField.getColumnIdx());
        while (nonogramColumnLogic.getBoardAccessHelper().areFieldIndexesValid(f) && matchCondition.test(f)) {
            int row = f.getRowIdx();
            min = Math.min(min, row);
            max = Math.max(max, row);
            f.setRowIdx(directionFn.applyAsInt(row));
        }

        return new Range(min, max);
    }

    private record Range(int min, int max) {
        boolean isEmpty() {
            return min == Integer.MAX_VALUE && max == Integer.MIN_VALUE;
        }
    }

    private void evaluateAndMaybePlaceX(int columnIdx, List<Integer> emptyRange, List<Integer> colouredRange, boolean isFromTop) {
        List<Integer> lengths = nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> ranges = nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx);

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

            boolean fitsInsideEmpty = possibleRangeLengthFitsInEmptyRange(possibleRange, emptyLen, range);
            boolean mergedAcceptable = mergedSequenceLengthIsAcceptable(possibleRange, emptyLen, totalLen, len, range);

            if (fitsInsideEmpty || mergedAcceptable) fitting.add(i);
        }

        if (fitting.isEmpty()) {
            int xRow = isFromTop ? emptyRange.get(0) : emptyRange.get(1);
            Field field = new Field(xRow, columnIdx);

            nonogramFieldPlacingXHelper.placeXAtGivenField(field);
            nonogramColumnLogic.getNonogramFieldExclusionHelper().excludeFieldInColumn(field);
            nonogramColumnLogic.getActionScheduler().scheduleActionsBasedOnField(field, NonogramSolveAction.PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_COLUMN);
            nonogramColumnLogic.getNonogramState().increaseMadeSteps();
        }
    }

    private boolean possibleRangeLengthFitsInEmptyRange(List<Integer> possibleRange, int emptyLen, List<Integer> range) {
        return rangeLength(possibleRange) < emptyLen && rangeInsideAnotherRange(possibleRange, range);
    }

    private boolean mergedSequenceLengthIsAcceptable(List<Integer> possibleRange,
                                                     int emptyLen,
                                                     int totalLength,
                                                     int length,
                                                     List<Integer> range) {
        return rangeLength(possibleRange) >= emptyLen && totalLength <= length && rangeInsideAnotherRange(possibleRange, range);
    }

    private void setAndAddLog(String tmpLog) {
        nonogramColumnLogic.setTmpLog(tmpLog);
        nonogramColumnLogic.addLog();
    }

    @Override
    public void refreshFrom(NonogramColumnLogic logicToCopy) {
        nonogramColumnLogic.setLogs(logicToCopy.getLogs());

        nonogramColumnLogic.setColumnsSequencesRanges(logicToCopy.getColumnsSequencesRanges());
        nonogramColumnLogic.setColumnsFieldsNotToInclude(logicToCopy.getColumnsFieldsNotToInclude());
    }
}
