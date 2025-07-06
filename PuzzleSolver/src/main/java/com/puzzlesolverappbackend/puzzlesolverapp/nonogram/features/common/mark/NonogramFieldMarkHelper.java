package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.mark;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramState;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.NonogramActionScheduler;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.marking.MarkAvailableFieldsLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range.SequenceRangeCorrectionWhenMarkingFieldsLogHelper;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.util.BiConsumer;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.rangeInsideAnotherRange;
import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.rangeLength;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramConstants.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.*;

@UtilityClass
public class NonogramFieldMarkHelper {

    public static void markAvailableFieldsInLine(
            int lineIdx,
            boolean isRow,
            NonogramRules rules,
            List<List<String>> solutionBoard,
            List<List<String>> boardWithMarks,
            List<List<Integer>> sequencesLengths,
            List<List<List<Integer>>> sequencesRanges,
            TriConsumer<Integer, Integer, List<Integer>> updateRangeConsumer,
            BiConsumer<Integer, Integer> excludeSequenceConsumer,
            NonogramActionScheduler scheduler,
            NonogramState state,
            Runnable addLogRunnable,
            Consumer<String> setTmpLogConsumer
    ) {
        boolean progress;
        do {
            progress = false;

            int width = isRow ? rules.getWidth() : rules.getHeight();
            List<Integer> lengths = sequencesLengths.get(lineIdx);
            List<List<Integer>> ranges = sequencesRanges.get(lineIdx);

            for (int i = 0; i < width; i++) {
                Field field = isRow ? new Field(lineIdx, i) : new Field(i, lineIdx);
                if (!isFieldColoured(solutionBoard, field)) continue;

                List<Integer> colouredRange = findColouredSequenceRange(solutionBoard, field, isRow, rules);
                int colouredLength = rangeLength(colouredRange);

                int matchCount = 0;
                int matchedIdx = -1;

                for (int seqIdx = 0; seqIdx < lengths.size(); seqIdx++) {
                    List<Integer> range = ranges.get(seqIdx);
                    if (rangeInsideAnotherRange(colouredRange, range) && colouredLength <= lengths.get(seqIdx)) {
                        matchCount++;
                        matchedIdx = seqIdx;
                    }
                }

                if (matchCount == 1) {
                    String marker = indexToSequenceCharMark(matchedIdx);
                    List<String> before = isRow
                            ? new ArrayList<>(boardWithMarks.get(lineIdx))
                            : getColumnCopy(boardWithMarks, lineIdx);


                    for (int j = colouredRange.get(0); j <= colouredRange.get(1); j++) {
                        int row = isRow ? lineIdx : j;
                        int col = isRow ? j : lineIdx;
                        String cell = boardWithMarks.get(row).get(col);
                        boolean isEmptyMark = isRow
                                ? cell.startsWith(EMPTY_FIELD, 1)
                                : cell.charAt(3) == EMPTY_FIELD.charAt(0);

                        if (isEmptyMark) {
                            markField(boardWithMarks, row, col, marker, isRow);
                            state.increaseMadeSteps();
                            progress = true;
                        }
                    }

                    List<String> after = isRow
                            ? new ArrayList<>(boardWithMarks.get(lineIdx))
                            : getColumnCopy(boardWithMarks, lineIdx);
                    if (!before.equals(after)) {
                        String log = MarkAvailableFieldsLogHelper.generateLog(
                                lineIdx, before, after, matchedIdx, marker, isRow
                        );
                        setTmpLogConsumer.accept(log); // <-- poprawione
                        addLogRunnable.run();
                    }

                    List<Integer> oldRange = ranges.get(matchedIdx);
                    List<Integer> newRange = calculateNewMarkedRange(
                            oldRange, colouredRange, lengths.get(matchedIdx)
                    );

                    if (!oldRange.equals(newRange)) {
                        String log = SequenceRangeCorrectionWhenMarkingFieldsLogHelper.generateLog(
                                lineIdx, matchedIdx, ranges, newRange, lengths, isRow
                        );
                        setTmpLogConsumer.accept(log);
                        addLogRunnable.run();

                        updateRangeConsumer.accept(lineIdx, matchedIdx, newRange);
                        progress = true;

                        if (rangeLength(newRange) == lengths.get(matchedIdx)) {
                            excludeSequenceConsumer.accept(lineIdx, matchedIdx);
                        }

                        Field triggerField = isRow ? new Field(lineIdx, 0) : new Field(0, lineIdx);
                        scheduler.scheduleActionsBasedOnField(triggerField, isRow
                                ? NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW
                                : NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN);
                    }
                }
            }

        } while (progress);
    }

    private static void markField(List<List<String>> boardWithMarks, int row, int col, String marker, boolean isRow) {
        if (isRow) {
            markRowBoardField(boardWithMarks, row, col, marker);
        } else {
            markColumnBoardField(boardWithMarks, row, col, marker);
        }
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

    private static List<String> getColumnCopy(List<List<String>> board, int columnIdx) {
        List<String> column = new ArrayList<>();
        for (List<String> row : board) {
            column.add(row.get(columnIdx));
        }
        return column;
    }

    public static String getUpdatedFieldWithMarks(String currentField, String mask) {
        StringBuilder updatedField = new StringBuilder();
        for (int i = 0; i < currentField.length(); i++) {
            if (currentField.charAt(i) == '-') {
                updatedField.append(mask.charAt(i));
            } else {
                updatedField.append(currentField.charAt(i));
            }
        }

        return updatedField.toString();
    }
}
