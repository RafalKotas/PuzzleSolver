package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.Field;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.NonogramColumnLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.common.NonogramFieldColouringHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.NonogramRowLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzleAppFileManager.common.ArrayUtils.rangeInsideAnotherRange;
import static com.puzzlesolverappbackend.puzzleAppFileManager.common.ArrayUtils.rangeLength;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramBoardUtils.*;

public final class ColouringHelper {

    public static List<Integer> calculateOverlappingRange(List<Integer> range, int sequenceLength) {
        int start = range.get(1) - sequenceLength + 1;
        int end = range.get(0) + sequenceLength - 1;

        if (start > end) return List.of();

        return IntStream.rangeClosed(start, end)
                .boxed()
                .collect(Collectors.toList());
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



    public static List<Integer> findColouredSequenceRangeTop(List<List<String>> board, int columnIdx, int startRowIdx) {
        int start = startRowIdx;
        while (start - 1 >= 0 && isFieldColoured(board, new Field(start - 1, columnIdx))) {
            start--;
        }
        return List.of(start, startRowIdx);
    }

    public static List<Integer> findColouredSequenceRangeBottom(List<List<String>> board, int columnIdx, int startRowIdx) {
        int endRowIdx = startRowIdx;
        while (endRowIdx + 1 < board.size() && isFieldColoured(board, new Field(endRowIdx + 1, columnIdx))) {
            endRowIdx++;
        }
        return List.of(startRowIdx, endRowIdx);
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

    public static boolean extendToTop(
            NonogramColumnLogic logic,
            NonogramFieldColouringHelper colouringHelper,
            NonogramActionScheduler scheduler,
            int columnIdx,
            int fromInclusive,
            int toInclusive
    ) {
        boolean anyFieldColoured = false;

        for (int rowIdx = fromInclusive; rowIdx >= toInclusive && rowIdx >= 0; rowIdx--) {
            Field field = new Field(rowIdx, columnIdx);
            try {
                if (isFieldEmpty(logic.getNonogramSolutionBoard(), field)) {
                    colouringHelper.colourFieldAtGivenPosition(field, "--C-");
                    scheduler.scheduleActionsBasedOnField(field, NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN);
                    logic.getNonogramState().increaseMadeSteps();
                    anyFieldColoured = true;
                }
            } catch (IndexOutOfBoundsException e) {
                logic.getNonogramState().invalidateSolution();
            }
        }

        return anyFieldColoured;
    }

    public static boolean extendToBottom(
            NonogramColumnLogic logic,
            NonogramFieldColouringHelper colouringHelper,
            NonogramActionScheduler scheduler,
            int columnIdx,
            int fromInclusive,
            int toInclusive
    ) {
        boolean anyFieldColoured = false;

        for (int rowIdx = fromInclusive; rowIdx <= toInclusive && rowIdx < logic.getNonogramRules().getHeight(); rowIdx++) {
            Field field = new Field(rowIdx, columnIdx);
            try {
                if (isFieldEmpty(logic.getNonogramSolutionBoard(), field)) {
                    colouringHelper.colourFieldAtGivenPosition(field, "--C-");
                    scheduler.scheduleActionsBasedOnField(field, NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN);
                    logic.getNonogramState().increaseMadeSteps();
                    anyFieldColoured = true;
                }
            } catch (IndexOutOfBoundsException e) {
                logic.getNonogramState().invalidateSolution();
            }
        }

        return anyFieldColoured;
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

    public static boolean extendToLeft(
            NonogramRowLogic logic,
            NonogramFieldColouringHelper colouringHelper,
            NonogramActionScheduler scheduler,
            int rowIdx,
            int fromInclusive,
            int toInclusive
    ) {
        boolean anyFieldColoured = false;

        for (int columnIdx = fromInclusive; columnIdx >= toInclusive && columnIdx >= 0; columnIdx--) {
            Field field = new Field(rowIdx, columnIdx);
            try {
                if (isFieldEmpty(logic.getNonogramSolutionBoard(), field)) {
                    colouringHelper.colourFieldAtGivenPosition(field, "--C-");
                    scheduler.scheduleActionsBasedOnField(field, NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW);
                    logic.getNonogramState().increaseMadeSteps();
                    anyFieldColoured = true;
                }
            } catch (IndexOutOfBoundsException e) {
                logic.getNonogramState().invalidateSolution();
            }
        }
        return anyFieldColoured;
    }

    public static boolean extendToRight(
            NonogramRowLogic logic,
            NonogramFieldColouringHelper colouringHelper,
            NonogramActionScheduler scheduler,
            int rowIdx,
            int fromInclusive,
            int toInclusive
    ) {
        boolean anyFieldColoured = false;

        for (int columnIdx = fromInclusive; columnIdx <= toInclusive && columnIdx < logic.getNonogramRules().getWidth(); columnIdx++) {
            Field field = new Field(rowIdx, columnIdx);
            try {
                if (isFieldEmpty(logic.getNonogramSolutionBoard(), field)) {
                    colouringHelper.colourFieldAtGivenPosition(field, "R---");
                    scheduler.scheduleActionsBasedOnField(field, NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW);
                    logic.getNonogramState().increaseMadeSteps();
                    anyFieldColoured = true;
                }
            } catch (IndexOutOfBoundsException e) {
                logic.getNonogramState().invalidateSolution();
            }
        }

        return anyFieldColoured;
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

