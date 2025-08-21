package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.mark;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.marking.MarkAvailableFieldsLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range.SequenceRangeCorrectionWhenMarkingFieldsLogHelper;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.rangeInsideAnotherRange;
import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.rangeLength;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramConstants.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.*;

@UtilityClass
public class NonogramFieldMarkHelper {

    /**
     * Iteratively scans a single row or column and attempts to mark all fully identified sequences.
     * <p>
     * The method continues as long as any progress is made (i.e., any sequence is successfully marked).
     * It uses coloured fields on the board to detect completed sequences and match them with
     * declared sequence ranges and lengths.
     *
     * @param ctx The marking context containing board state, rules, and sequences metadata.
     */
    public static void markAvailableFieldsInLine(MarkContext ctx) {
        BoardContext board = ctx.getBoard();

        boolean progress;
        do {
            progress = false;
            int lineSize = board.isRow() ? board.getRules().getWidth() : board.getRules().getHeight();

            for (int i = 0; i < lineSize; i++) {
                Field field = resolveField(board, i);

                if (isFieldColoured(board.getSolutionBoard(), field)) {
                    boolean changed = tryMarkField(ctx, field);
                    progress |= changed;
                }
            }
        } while (progress);
    }

    /**
     * Resolves a {@link Field} object based on the current line index and direction (row or column).
     * <p>
     * Used in the context of iterating over a single row or column depending on the board orientation.
     *
     * @param board The board context providing direction and index.
     * @param i     The position in the current line (column if row-wise, row if column-wise).
     * @return The resolved field in 2D board coordinates.
     */
    private static Field resolveField(BoardContext board, int i) {
        return board.isRow()
                ? new Field(board.getLineIdx(), i)
                : new Field(i, board.getLineIdx());
    }

    /**
     * Attempts to identify and mark a matching sequence for a given coloured field.
     * <p>
     * It finds the full coloured range around the field, checks whether this range matches
     * any sequence based on length and allowed range, and if so, applies marking to it.
     *
     * @param ctx   The full solving context with board and sequence data.
     * @param field The field from which to attempt sequence recognition.
     * @return {@code true} if a sequence was successfully matched and marked, {@code false} otherwise.
     */
    private static boolean tryMarkField(MarkContext ctx, Field field) {
        BoardContext board = ctx.getBoard();
        SequencesContext sequences = ctx.getSequences();

        List<Integer> colouredRange = findColouredSequenceRange(
                board.getSolutionBoard(),
                field,
                board.isRow(),
                board.getRules()
        );

        int lineIdx = board.getLineIdx();
        List<Integer> sequenceLengths = sequences.getSequencesLengths().get(lineIdx);
        List<List<Integer>> sequenceRanges = sequences.getSequencesRanges().get(lineIdx);

        int matchIdx = findMatchingSequenceIdx(colouredRange, sequenceLengths, sequenceRanges);
        return matchIdx != -1 && markMatchedSequence(ctx, colouredRange, matchIdx);
    }

    private static int findMatchingSequenceIdx(
            List<Integer> colouredRange,
            List<Integer> lengths,
            List<List<Integer>> ranges
    ) {
        int matchCount = 0;
        int matchedIdx = -1;

        for (int seqIdx = 0; seqIdx < lengths.size(); seqIdx++) {
            if (rangeInsideAnotherRange(colouredRange, ranges.get(seqIdx)) &&
                    rangeLength(colouredRange) <= lengths.get(seqIdx)) {
                matchCount++;
                matchedIdx = seqIdx;
            }
        }

        return matchCount == 1 ? matchedIdx : -1;
    }

    private static boolean markMatchedSequence(MarkContext ctx, List<Integer> colouredRange, int matchedIdx) {
        BoardContext board = ctx.getBoard();
        SequencesContext sequences = ctx.getSequences();
        MarkOperationContext ops = ctx.getOps();

        String marker = indexToSequenceCharMark(matchedIdx);
        List<String> before = copyLine(board.getBoardWithMarks(), board);

        boolean changed = markAllFieldsInRange(board, ops, colouredRange, marker);

        List<String> after = copyLine(board.getBoardWithMarks(), board);
        if (!before.equals(after)) {
            logMarkChange(board, ops, matchedIdx, marker, before, after);
        }

        return updateRangeIfNecessary(board, sequences, ops, matchedIdx, colouredRange) || changed;
    }

    private static List<String> copyLine(List<List<String>> boardWithMarks, BoardContext board) {
        return board.isRow()
                ? new ArrayList<>(boardWithMarks.get(board.getLineIdx()))
                : getColumnCopy(boardWithMarks, board.getLineIdx());
    }

    private static List<String> getColumnCopy(List<List<String>> board, int columnIdx) {
        List<String> column = new ArrayList<>();
        for (List<String> row : board) {
            column.add(row.get(columnIdx));
        }
        return column;
    }

    private static boolean markAllFieldsInRange(BoardContext board, MarkOperationContext ops,
                                                List<Integer> range, String marker) {
        boolean changed = false;
        for (int j = range.get(0); j <= range.get(1); j++) {
            int row = board.isRow() ? board.getLineIdx() : j;
            int col = board.isRow() ? j : board.getLineIdx();
            String cell = board.getBoardWithMarks().get(row).get(col);

            boolean isEmptyMark = board.isRow()
                    ? cell.startsWith(EMPTY_FIELD, 1)
                    : cell.charAt(3) == EMPTY_FIELD.charAt(0);

            if (isEmptyMark) {
                markField(board.getBoardWithMarks(), row, col, marker, board.isRow());
                ops.getState().increaseMadeSteps();
                changed = true;
            }
        }
        return changed;
    }

    private static void markField(List<List<String>> boardWithMarks, int row, int col, String marker, boolean isRow) {
        if (isRow) {
            markRowBoardField(boardWithMarks, row, col, marker);
        } else {
            markColumnBoardField(boardWithMarks, row, col, marker);
        }
    }

    private static void logMarkChange(BoardContext board, MarkOperationContext ops,
                                      int seqIdx, String marker, List<String> before, List<String> after) {
        String log = MarkAvailableFieldsLogHelper.generateLog(
                board.isRow(),
                board.getLineIdx(),
                seqIdx,
                marker,
                before,
                after
        );
        ops.getSetTmpLogConsumer().accept(log);
        ops.getAddLogRunnable().run();
    }

    private static boolean updateRangeIfNecessary(BoardContext board, SequencesContext sequences,
                                                  MarkOperationContext ops, int seqIdx, List<Integer> colouredRange) {
        List<Integer> oldRange = sequences.getSequencesRanges().get(board.getLineIdx()).get(seqIdx);
        int seqLength = sequences.getSequencesLengths().get(board.getLineIdx()).get(seqIdx);

        List<Integer> updatedRange = calculateNewMarkedRange(oldRange, colouredRange, seqLength);
        if (oldRange.equals(updatedRange)) return false;

        String log = SequenceRangeCorrectionWhenMarkingFieldsLogHelper.generateLog(
                board.isRow(),
                board.getLineIdx(),
                seqIdx,
                sequences.getSequencesRanges().get(board.getLineIdx()),
                sequences.getSequencesLengths().get(board.getLineIdx()),
                updatedRange
        );
        ops.getSetTmpLogConsumer().accept(log);
        ops.getAddLogRunnable().run();

        sequences.getUpdateRangeConsumer().accept(board.getLineIdx(), seqIdx, updatedRange);

        if (rangeLength(updatedRange) == seqLength) {
            sequences.getExcludeSequenceConsumer().accept(board.getLineIdx(), seqIdx);
        }

        Field trigger = board.isRow()
                ? new Field(board.getLineIdx(), 0)
                : new Field(0, board.getLineIdx());

        ops.getScheduler().scheduleActionsBasedOnField(trigger,
                board.isRow() ? NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW
                        : NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN);

        return true;
    }

    public static void markRowBoardField(List<List<String>> boardWithMarks, int rowIdx, int columnIdx, String rowSeqMark) {
        String currentField = boardWithMarks.get(rowIdx).get(columnIdx);
        String updatedField = MARKED_ROW_INDICATOR + rowSeqMark + currentField.substring(2);
        boardWithMarks.get(rowIdx).set(columnIdx, updatedField);
    }

    public static void markColumnBoardField(List<List<String>> boardWithMarks, int rowIdx, int columnIdx, String columnSeqMark) {
        String currentField = boardWithMarks.get(rowIdx).get(columnIdx);
        String updatedField = currentField.substring(0, 2) + MARKED_COLUMN_INDICATOR + columnSeqMark;
        boardWithMarks.get(rowIdx).set(columnIdx, updatedField);
    }
}
