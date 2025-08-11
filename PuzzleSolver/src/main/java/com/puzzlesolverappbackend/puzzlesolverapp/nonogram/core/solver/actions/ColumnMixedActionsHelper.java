package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.actions;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.isFieldColoured;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.isFieldWithX;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.MixedActionsHelper.wouldMergeTooLongBackward;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.MixedActionsHelper.wouldMergeTooLongForward;

@UtilityClass
public class ColumnMixedActionsHelper {

    public static List<Integer> sequencesIdsInColumnIncludingField(List<List<Integer>> columnSequencesRanges, Field field) {

        return IntStream.range(0, columnSequencesRanges.size())
                .filter(columnIdx -> {
                    int rangeStart = columnSequencesRanges.get(columnIdx).get(0);
                    int rangeEnd = columnSequencesRanges.get(columnIdx).get(1);
                    return field.getRowIdx() >= rangeStart && field.getRowIdx() <= rangeEnd;
                })
                .boxed()
                .toList();
    }

    public List<List<Integer>> getColouredSequencesRangesInColumnInRangeToTop(
            List<List<String>> solutionBoard,
            int columnIdx,
            int potentiallyColouredFieldRowIndex,
            int maxSequenceLength) {

        List<List<Integer>> ranges = new ArrayList<>();

        int minRow = Math.max(potentiallyColouredFieldRowIndex - maxSequenceLength, 0);
        int currentRowIdx = potentiallyColouredFieldRowIndex;

        do {
            Field currentField = new Field(currentRowIdx, columnIdx);

            if (isFieldWithX(solutionBoard, currentField)) break;

            if (isFieldColoured(solutionBoard, currentField)) {
                int bottomSequenceRowIdx = currentRowIdx;

                int topSequenceRowIdx = currentRowIdx;
                int prev = topSequenceRowIdx - 1;
                while (prev >= 0 && isFieldColoured(solutionBoard, new Field(prev, columnIdx))) {
                    topSequenceRowIdx = prev;
                    prev = topSequenceRowIdx - 1;
                }

                ranges.add(0, List.of(topSequenceRowIdx, bottomSequenceRowIdx));
                currentRowIdx = topSequenceRowIdx - 1;
            } else {
                currentRowIdx--;
            }

        } while (currentRowIdx >= minRow);

        return ranges;
    }

    public static List<List<Integer>> getColouredSequencesRangesInColumnInRangeToBottom(
            List<List<String>> solutionBoard,
            int columnIdx,
            int potentiallyColouredFieldRowIndex,
            int maxSequenceLength
    ) {
        List<List<Integer>> ranges = new ArrayList<>();

        int height = solutionBoard.size();
        int maxRow = Math.min(potentiallyColouredFieldRowIndex + maxSequenceLength, height - 1);
        int currentRowIdx = potentiallyColouredFieldRowIndex;

        do {
            Field currentField = new Field(currentRowIdx, columnIdx);

            if (isFieldWithX(solutionBoard, currentField)) break;

            if (isFieldColoured(solutionBoard, currentField)) {
                int bottomSequenceRowIdx = currentRowIdx;

                int topSequenceRowIdx = currentRowIdx;
                int next = bottomSequenceRowIdx + 1;
                while (next < height && isFieldColoured(solutionBoard, new Field(next, columnIdx))) {
                    bottomSequenceRowIdx = next;
                    next = bottomSequenceRowIdx + 1;
                }

                ranges.add(List.of(topSequenceRowIdx, bottomSequenceRowIdx));
                currentRowIdx = bottomSequenceRowIdx + 1;
            } else {
                currentRowIdx++;
            }
        } while (currentRowIdx <= maxRow);

        return ranges;
    }

    public static List<Integer> findValidSequencesIdsMergingToTop(List<Integer> sequenceIds,
                                                                  List<Integer> expectedLengths,
                                                                  int rowIndexBeforeX,
                                                                  List<List<Integer>> colouredSequences) {
        return IntStream.range(0, sequenceIds.size())
                .filter(i -> !wouldMergeTooLongBackward(expectedLengths.get(i), rowIndexBeforeX, colouredSequences))
                .mapToObj(sequenceIds::get)
                .toList();
    }

    public static List<Integer> findValidSequencesIdsMergingToBottom(List<Integer> sequenceIds,
                                                                     List<Integer> expectedLengths,
                                                                     int colouredRowIndexAfterX,
                                                                     List<List<Integer>> colouredSequences) {
        return IntStream.range(0, sequenceIds.size())
                .filter(i -> !wouldMergeTooLongForward(expectedLengths.get(i), colouredRowIndexAfterX, colouredSequences))
                .mapToObj(sequenceIds::get)
                .toList();
    }
}
