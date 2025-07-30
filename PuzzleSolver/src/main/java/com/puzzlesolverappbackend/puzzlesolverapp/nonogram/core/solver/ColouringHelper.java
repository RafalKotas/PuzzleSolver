package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.colouring.NonogramFieldColouringHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.NonogramColumnLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row.NonogramRowLogic;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.rangeInsideAnotherRange;
import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.rangeLength;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.*;


@UtilityClass
public final class ColouringHelper {

    public static List<Integer> calculateOverlappingRange(List<Integer> range, int sequenceLength) {
        int start = range.get(1) - sequenceLength + 1;
        int end = range.get(0) + sequenceLength - 1;

        if (start > end) return List.of();

        return new ArrayList<>(IntStream.rangeClosed(start, end)
                .boxed()
                .toList());
    }

    public static List<Integer> findPossibleSequenceLengths(List<List<Integer>> ranges, List<Integer> colouredRange, List<Integer> lengths) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < ranges.size(); i++) {
            if (rangeInsideAnotherRange(colouredRange, ranges.get(i))
                    && lengths.get(i) >= rangeLength(colouredRange)) {
                result.add(lengths.get(i));
            }
        }
        return result;
    }

    // TODO: Skip if whole column is already filled
    public static List<Integer> findColouredSequenceRangeTop(List<List<String>> board, int columnIdx, int startRowIdx) {
        int start = startRowIdx;
        while (start > 0) {
            int above = start - 1;
            if (isFieldColoured(board, new Field(above, columnIdx))) {
                start--;
            } else {
                break;
            }
        }
        return List.of(start, startRowIdx);
    }

    public static List<Integer> findColouredSequenceRangeBottom(List<List<String>> board, int columnIdx, int startRowIdx) {
        int end = startRowIdx;
        while (end < board.size() - 1) {
            int below = end + 1;
            if (isFieldColoured(board, new Field(below, columnIdx))) {
                end++;
            } else {
                break;
            }
        }
        return List.of(startRowIdx, end);
    }

    public static int findDistanceFromTopX(List<List<String>> board, int columnIdx, List<Integer> colouredRange, int maxDist) {
        for (int offset = rangeLength(colouredRange); offset < maxDist; offset++) {
            int rowIdxToCheck = colouredRange.get(1) - offset;
            if (rowIdxToCheck < 0) break;

            Field fieldToCheck = new Field(rowIdxToCheck, columnIdx);
            if (isFieldWithX(board, fieldToCheck)) {
                return offset;
            }
        }
        return 0;
    }

    public static int findDistanceFromBottomX(List<List<String>> board, int columnIdx, List<Integer> colouredRange, int maxDist) {
        for (int offset = rangeLength(colouredRange); offset < maxDist; offset++) {
            int idxToCheck = colouredRange.get(0) + offset;
            if (idxToCheck >= board.size()) break;

            Field fieldToCheck = new Field(idxToCheck, columnIdx);
            if (isFieldWithX(board, fieldToCheck)) {
                return offset;
            }
        }
        return 0;
    }

    private static boolean extend(
            IntPredicate terminationCondition,
            IntUnaryOperator stepFn,
            IntFunction<Field> fieldSupplier,
            Predicate<Field> isEmptyPredicate,
            Consumer<Field> colouringAction,
            Consumer<Field> schedulingAction,
            Runnable onStepMade,
            Runnable onInvalidation,
            int startIdx
    ) {
        boolean anyFieldColoured = false;

        for (int idx = startIdx; terminationCondition.test(idx); idx = stepFn.applyAsInt(idx)) {
            Field field = fieldSupplier.apply(idx);
            try {
                if (isEmptyPredicate.test(field)) {
                    colouringAction.accept(field);
                    schedulingAction.accept(field);
                    onStepMade.run();
                    anyFieldColoured = true;
                }
            } catch (IndexOutOfBoundsException e) {
                onInvalidation.run();
            }
        }

        return anyFieldColoured;
    }

    public static boolean extendToTop(NonogramColumnLogic logic,
                                      NonogramFieldColouringHelper colouringHelper,
                                      NonogramActionScheduler scheduler,
                                      int columnIdx, int fromInclusive, int toInclusive) {

        return extend(
                rowIdx -> rowIdx >= toInclusive && rowIdx >= 0,
                rowIdx -> rowIdx - 1,
                rowIdx -> new Field(rowIdx, columnIdx),
                f -> isFieldEmpty(logic.getNonogramSolutionBoard(), f),
                f -> colouringHelper.colourFieldAtGivenPosition(f, "--C-"),
                f -> scheduler.scheduleActionsBasedOnField(f, NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN),
                () -> logic.getNonogramState().increaseMadeSteps(),
                () -> logic.getNonogramState().invalidateSolution(),
                fromInclusive
        );
    }

    public static boolean extendToBottom(NonogramColumnLogic logic,
                                         NonogramFieldColouringHelper colouringHelper,
                                         NonogramActionScheduler scheduler,
                                         int columnIdx, int fromInclusive, int toInclusive) {

        int height = logic.getNonogramRules().getHeight();
        return extend(
                rowIdx -> rowIdx <= toInclusive && rowIdx < height,
                rowIdx -> rowIdx + 1,
                rowIdx -> new Field(rowIdx, columnIdx),
                f -> isFieldEmpty(logic.getNonogramSolutionBoard(), f),
                f -> colouringHelper.colourFieldAtGivenPosition(f, "--C-"),
                f -> scheduler.scheduleActionsBasedOnField(f, NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN),
                () -> logic.getNonogramState().increaseMadeSteps(),
                () -> logic.getNonogramState().invalidateSolution(),
                fromInclusive
        );
    }

    public static boolean extendToRight(NonogramRowLogic logic,
                                        NonogramFieldColouringHelper colouringHelper,
                                        NonogramActionScheduler scheduler,
                                        int rowIdx, int fromInclusive, int toInclusive) {

        int width = logic.getNonogramRules().getWidth();
        return extend(
                colIdx -> colIdx <= toInclusive && colIdx < width,
                colIdx -> colIdx + 1,
                colIdx -> new Field(rowIdx, colIdx),
                f -> isFieldEmpty(logic.getNonogramSolutionBoard(), f),
                f -> colouringHelper.colourFieldAtGivenPosition(f, "R---"),
                f -> scheduler.scheduleActionsBasedOnField(f, NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW),
                () -> logic.getNonogramState().increaseMadeSteps(),
                () -> logic.getNonogramState().invalidateSolution(),
                fromInclusive
        );
    }

    public static boolean extendToLeft(NonogramRowLogic logic,
                                       NonogramFieldColouringHelper colouringHelper,
                                       NonogramActionScheduler scheduler,
                                       int rowIdx, int fromInclusive, int toInclusive) {

        return extend(
                colIdx -> colIdx >= toInclusive && colIdx >= 0,
                colIdx -> colIdx - 1,
                colIdx -> new Field(rowIdx, colIdx),
                f -> isFieldEmpty(logic.getNonogramSolutionBoard(), f),
                f -> colouringHelper.colourFieldAtGivenPosition(f, "R---"),
                f -> scheduler.scheduleActionsBasedOnField(f, NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW),
                () -> logic.getNonogramState().increaseMadeSteps(),
                () -> logic.getNonogramState().invalidateSolution(),
                fromInclusive
        );
    }

    public static int findDistanceFromLeftX(List<List<String>> board, int rowIdx, List<Integer> colouredRange, int maxDist) {
        for (int offset = rangeLength(colouredRange); offset < maxDist; offset++) {
            int columnIdxToCheck = colouredRange.get(1) - offset;
            if (columnIdxToCheck < 0) break;

            Field fieldToCheck = new Field(rowIdx, columnIdxToCheck);
            if (isFieldWithX(board, fieldToCheck)) {
                return offset;
            }
        }
        return 0;
    }

    public static int findDistanceFromRightX(List<List<String>> board, int rowIdx, List<Integer> colouredRange, int maxDist) {
        for (int offset = rangeLength(colouredRange); offset < maxDist; offset++) {
            int columnIdxToCheck = colouredRange.get(0) + offset;
            if (!board.get(0).isEmpty() && columnIdxToCheck >= board.get(0).size()) break;

            Field fieldToCheck = new Field(rowIdx, columnIdxToCheck);
            if (isFieldWithX(board, fieldToCheck)) {
                return offset;
            }
        }
        return 0;
    }

    public static List<Integer> findColouredSequenceRangeLeft(List<List<String>> board, int rowIdx, int startColIdx) {
        int start = startColIdx;
        while (start - 1 >= 0 && isFieldColoured(board, new Field(rowIdx, start - 1))) {
            start--;
        }
        return List.of(start, startColIdx);
    }

    public static List<Integer> findColouredSequenceRangeRight(List<List<String>> board, int rowIdx, int startColumnIdx) {
        int endColumnIdx = startColumnIdx;
        while (endColumnIdx + 1 < board.get(0).size() && isFieldColoured(board, new Field(rowIdx, endColumnIdx + 1))) {
            endColumnIdx++;
        }
        return List.of(startColumnIdx, endColumnIdx);
    }
}

