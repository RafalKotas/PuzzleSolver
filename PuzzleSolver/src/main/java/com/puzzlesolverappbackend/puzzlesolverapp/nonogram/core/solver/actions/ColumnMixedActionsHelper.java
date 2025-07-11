package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.actions;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.isFieldColoured;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.isFieldWithX;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.MixedActionsHelper.wouldMergeTooLongBackward;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.MixedActionsHelper.wouldMergeTooLongForward;

@UtilityClass
public class ColumnMixedActionsHelper {

    private static final int DISTANCE_WITH_ONE_EMPTY_FIELD_TO_POSSIBLE_COLOURED = 2;

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

        List<List<Integer>> colouredSequencesRanges = new ArrayList<>();
        int minRowIdx = Math.max(potentiallyColouredFieldRowIndex - maxSequenceLength, 0);
        int maxRowIdx = potentiallyColouredFieldRowIndex - DISTANCE_WITH_ONE_EMPTY_FIELD_TO_POSSIBLE_COLOURED;

        boolean fieldWithXFound = false;
        int currentRowIdx = maxRowIdx;

        while (currentRowIdx >= minRowIdx && !fieldWithXFound) {

            Field field = new Field(currentRowIdx, columnIdx);

            if (isFieldColoured(solutionBoard, field)) {
                int startIdx = currentRowIdx;
                int endIdx = currentRowIdx;

                while (endIdx >= 0 && isFieldColoured(solutionBoard, new Field(endIdx, columnIdx))) {
                    endIdx--;
                }

                colouredSequencesRanges.add(List.of(startIdx, endIdx + 1));
                currentRowIdx = endIdx - 1;

                if (currentRowIdx < minRowIdx || isFieldWithX(solutionBoard, new Field(currentRowIdx, columnIdx))) {
                    fieldWithXFound = true;
                }

            } else {
                if (isFieldWithX(solutionBoard, field)) {
                    fieldWithXFound = true;
                }
                currentRowIdx--;
            }
        }

        return colouredSequencesRanges;
    }

    public static List<List<Integer>> getColouredSequencesRangesInColumnInRangeToBottom(List<List<String>> solutionBoard, int columnIdx, int potentiallyColouredFieldColumnIndex, int maxSequenceLength) {
        int height = solutionBoard.size();

        List<List<Integer>> colouredSequencesRangesInColumnNotFurtherThanMaxSequenceLength = new ArrayList<>();
        List<Integer> colouredSequenceRangeInColumnInRange;

        List<Integer> possibleColouredSequencesStartIndexesRange = Arrays.asList(potentiallyColouredFieldColumnIndex + DISTANCE_WITH_ONE_EMPTY_FIELD_TO_POSSIBLE_COLOURED,
                potentiallyColouredFieldColumnIndex + maxSequenceLength);

        boolean fieldWithXFound = false;
        int currentRowIdx = possibleColouredSequencesStartIndexesRange.get(0);
        int potentiallyColouredSequenceRowIdx;

        while (currentRowIdx < height && currentRowIdx <= possibleColouredSequencesStartIndexesRange.get(1)) {

            if (isFieldColoured(solutionBoard, new Field(currentRowIdx, columnIdx))) {
                potentiallyColouredSequenceRowIdx = currentRowIdx;
                while (potentiallyColouredSequenceRowIdx < height && isFieldColoured(solutionBoard, new Field(potentiallyColouredSequenceRowIdx, columnIdx))) {
                    potentiallyColouredSequenceRowIdx++;
                }
                colouredSequenceRangeInColumnInRange = new ArrayList<>(Arrays.asList(currentRowIdx, potentiallyColouredSequenceRowIdx - 1));
                colouredSequencesRangesInColumnNotFurtherThanMaxSequenceLength.add(colouredSequenceRangeInColumnInRange);

                currentRowIdx = potentiallyColouredSequenceRowIdx + 1; // field with this rowIdx is not coloured ("X"/"-")

                if (currentRowIdx > height - 1) {
                    break;
                }

                if (isFieldWithX(solutionBoard, new Field(currentRowIdx, columnIdx))) {
                    fieldWithXFound = true;
                }
            } else if (isFieldWithX(solutionBoard, new Field(currentRowIdx, columnIdx))) {
                fieldWithXFound = true;
            }

            if (fieldWithXFound) {
                break;
            }

            currentRowIdx++;
        }

        return colouredSequencesRangesInColumnNotFurtherThanMaxSequenceLength;
    }

    public static List<Integer> findValidSequencesIdsMergingToTop(List<Integer> sequenceIds, List<Integer> expectedLengths, int rowIndexBeforeX, List<List<Integer>> colouredSequences) {
        return IntStream.range(0, sequenceIds.size())
                .filter(i -> !wouldMergeTooLongBackward(expectedLengths.get(i), rowIndexBeforeX, colouredSequences))
                .mapToObj(sequenceIds::get)
                .toList();
    }

    public static List<Integer> findValidSequencesIdsMergingToBottom(List<Integer> sequenceIds, List<Integer> expectedLengths, int colouredRowIndexAfterX, List<List<Integer>> colouredSequences) {

        return IntStream.range(0, sequenceIds.size())
                .filter(i -> !wouldMergeTooLongForward(expectedLengths.get(i), colouredRowIndexAfterX, colouredSequences))
                .mapToObj(sequenceIds::get)
                .toList();
    }
}
