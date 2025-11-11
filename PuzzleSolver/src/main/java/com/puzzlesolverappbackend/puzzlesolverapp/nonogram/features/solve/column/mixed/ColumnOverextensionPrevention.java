package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.mixed;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.isFieldColoured;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.isFieldWithX;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.mixed.MixedActionsHelper.wouldMergeTooLongBackward;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.mixed.MixedActionsHelper.wouldMergeTooLongForward;

public interface ColumnOverextensionPrevention {
    int DISTANCE_WITH_ONE_EMPTY_FIELD_TO_POSSIBLE_COLOURED = 2;

    static List<Integer> sequencesIdsInColumnIncludingField(List<List<Integer>> columnSequencesRanges, Field field) {
        return IntStream.range(0, columnSequencesRanges.size())
                .filter(i -> {
                    int rangeStart = columnSequencesRanges.get(i).get(0);
                    int rangeEnd = columnSequencesRanges.get(i).get(1);
                    return field.getRowIdx() >= rangeStart && field.getRowIdx() <= rangeEnd;
                })
                .boxed()
                .toList();
    }

    static List<List<Integer>> getColouredSequencesRangesInColumnInRangeToTop(List<List<String>> solutionBoard, int columnIdx, int potentiallyColouredFieldRowIndex, int maxSequenceLength) {
        List<List<Integer>> colouredSequencesRangesInColumnNotFurtherThanMaxSequenceLength = new ArrayList<>();
        List<Integer> colouredSequenceRangeInColumn;

        List<Integer> possibleColouredSequencesEndIndexesRange = Arrays.asList(Math.max(potentiallyColouredFieldRowIndex - maxSequenceLength, 0),
                potentiallyColouredFieldRowIndex - DISTANCE_WITH_ONE_EMPTY_FIELD_TO_POSSIBLE_COLOURED);

        boolean fieldWithXFound = false;
        int currentRowIdx = possibleColouredSequencesEndIndexesRange.get(1);
        int potentiallyColouredSequenceRowIdx;
        int minRowIdx = Math.max(possibleColouredSequencesEndIndexesRange.get(0), 0);

        while (currentRowIdx >= minRowIdx && !fieldWithXFound) {

            if (isFieldColoured(solutionBoard, new Field(currentRowIdx, columnIdx))) {
                potentiallyColouredSequenceRowIdx = currentRowIdx;
                while (potentiallyColouredSequenceRowIdx >= 0 && isFieldColoured(solutionBoard, new Field(potentiallyColouredSequenceRowIdx, columnIdx))) {
                    potentiallyColouredSequenceRowIdx--;
                }
                colouredSequenceRangeInColumn = new ArrayList<>(Arrays.asList(potentiallyColouredSequenceRowIdx + 1, currentRowIdx));
                colouredSequencesRangesInColumnNotFurtherThanMaxSequenceLength.add(colouredSequenceRangeInColumn);

                currentRowIdx = potentiallyColouredSequenceRowIdx - 1; // field with this columnIdx is not coloured ("X"/"-")

                if (currentRowIdx >= minRowIdx && isFieldWithX(solutionBoard, new Field(currentRowIdx, columnIdx))) {
                    fieldWithXFound = true;
                }
            } else if (isFieldWithX(solutionBoard, new Field(currentRowIdx, columnIdx))) {
                fieldWithXFound = true;
            }

            currentRowIdx--;
        }

        return colouredSequencesRangesInColumnNotFurtherThanMaxSequenceLength;
    }

    static List<List<Integer>> getColouredSequencesRangesInColumnInRangeToBottom(
            List<List<String>> solutionBoard,
            int columnIdx,
            int potentiallyColouredFieldRowIndex,
            int maxSequenceLength
    ) {
        int height = solutionBoard.size();
        List<List<Integer>> colouredSequencesRangesInColumnNotFurtherThanMaxSequenceLength = new ArrayList<>();

        List<Integer> possibleColouredSequencesStartIndexesRange = Arrays.asList(
                potentiallyColouredFieldRowIndex + DISTANCE_WITH_ONE_EMPTY_FIELD_TO_POSSIBLE_COLOURED,
                potentiallyColouredFieldRowIndex + maxSequenceLength
        );

        boolean fieldWithXFound = false;
        int currentRowIdx = possibleColouredSequencesStartIndexesRange.get(0);
        int maxRowIdx = Math.min(possibleColouredSequencesStartIndexesRange.get(1), height - 1);

        while (currentRowIdx <= maxRowIdx && !fieldWithXFound) {

            if (isFieldColoured(solutionBoard, new Field(currentRowIdx, columnIdx))) {
                int potentiallyColouredSequenceRowIdx = currentRowIdx;

                while (potentiallyColouredSequenceRowIdx < height &&
                        isFieldColoured(solutionBoard, new Field(potentiallyColouredSequenceRowIdx, columnIdx))) {
                    potentiallyColouredSequenceRowIdx++;
                }

                List<Integer> colouredSequenceRangeInColumnInRange = List.of(
                        currentRowIdx,
                        potentiallyColouredSequenceRowIdx - 1
                );
                colouredSequencesRangesInColumnNotFurtherThanMaxSequenceLength.add(colouredSequenceRangeInColumnInRange);

                currentRowIdx = potentiallyColouredSequenceRowIdx + 1;

                if (currentRowIdx <= maxRowIdx && isFieldWithX(solutionBoard, new Field(currentRowIdx, columnIdx))) {
                    fieldWithXFound = true;
                }
            } else if (isFieldWithX(solutionBoard, new Field(currentRowIdx, columnIdx))) {
                fieldWithXFound = true;
            }

            currentRowIdx++;
        }

        return colouredSequencesRangesInColumnNotFurtherThanMaxSequenceLength;
    }

    static List<Integer> findValidSequencesIdsMergingToTop(List<Integer> sequenceIds,
                                                           List<Integer> expectedLengths,
                                                           int rowIndexBeforeX,
                                                           List<List<Integer>> colouredSequences) {
        return IntStream.range(0, sequenceIds.size())
                .filter(i -> !wouldMergeTooLongBackward(expectedLengths.get(i), rowIndexBeforeX, colouredSequences))
                .mapToObj(sequenceIds::get)
                .toList();
    }

    static List<Integer> findValidSequencesIdsMergingToBottom(List<Integer> sequenceIds,
                                                             List<Integer> expectedLengths,
                                                             int colouredRowIndexAfterX,
                                                             List<List<Integer>> colouredSequences) {

        return IntStream.range(0, sequenceIds.size())
                .filter(i -> !wouldMergeTooLongForward(expectedLengths.get(i), colouredRowIndexAfterX, colouredSequences))
                .mapToObj(sequenceIds::get)
                .toList();
    }
}
