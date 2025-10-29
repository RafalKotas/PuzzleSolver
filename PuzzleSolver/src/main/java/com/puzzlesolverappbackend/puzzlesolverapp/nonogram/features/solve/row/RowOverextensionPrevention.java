package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.isFieldColoured;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.isFieldWithX;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.MixedActionsHelper.wouldMergeTooLongBackward;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.MixedActionsHelper.wouldMergeTooLongForward;

/**
 * prevent extending coloured sequence to excess length in row
 * (old name - RowPreventExtendingColouredSequenceToExcessLengthHelpers)
 */
public interface RowOverextensionPrevention {
    int DISTANCE_WITH_ONE_EMPTY_FIELD_TO_POSSIBLE_COLOURED = 2;

    static List<Integer> sequencesIdsInRowIncludingField(List<List<Integer>> rowSequencesRanges, Field field) {
        return IntStream.range(0, rowSequencesRanges.size())
                .filter(i -> {
                    int rangeStart = rowSequencesRanges.get(i).get(0);
                    int rangeEnd = rowSequencesRanges.get(i).get(1);
                    return field.getColumnIdx() >= rangeStart && field.getColumnIdx() <= rangeEnd;
                })
                .boxed()
                .toList();
    }

    static List<List<Integer>> getColouredSequencesRangesInRowInRangeOnLeft(List<List<String>> solutionBoard, int rowIdx, int potentiallyColouredFieldColumnIndex, int maxSequenceLength) {
        List<List<Integer>> colouredSequencesRangesInRowNotFurtherThanMaxSequenceLength = new ArrayList<>();
        List<Integer> colouredSequenceRangeInRow;

        List<Integer> possibleColouredSequencesEndIndexesRange = Arrays.asList(Math.max(potentiallyColouredFieldColumnIndex - maxSequenceLength, 0),
                potentiallyColouredFieldColumnIndex - DISTANCE_WITH_ONE_EMPTY_FIELD_TO_POSSIBLE_COLOURED);

        boolean fieldWithXFound = false;
        int currentColumnIdx = possibleColouredSequencesEndIndexesRange.get(1);
        int potentiallyColouredSequenceColumnIdx;

        while (currentColumnIdx >= possibleColouredSequencesEndIndexesRange.get(0) && !fieldWithXFound) {

            if (isFieldColoured(solutionBoard, new Field(rowIdx, currentColumnIdx))) {
                potentiallyColouredSequenceColumnIdx = currentColumnIdx;
                while (potentiallyColouredSequenceColumnIdx >= 0 && isFieldColoured(solutionBoard, new Field(rowIdx, potentiallyColouredSequenceColumnIdx))) {
                    potentiallyColouredSequenceColumnIdx--;
                }
                colouredSequenceRangeInRow = new ArrayList<>(Arrays.asList(potentiallyColouredSequenceColumnIdx + 1, currentColumnIdx));
                colouredSequencesRangesInRowNotFurtherThanMaxSequenceLength.add(colouredSequenceRangeInRow);

                currentColumnIdx = potentiallyColouredSequenceColumnIdx - 1; // field with this columnIdx is not coloured ("X"/"-")

                if (isFieldWithX(solutionBoard, new Field(rowIdx, currentColumnIdx))) {
                    fieldWithXFound = true;
                }
            } else if (isFieldWithX(solutionBoard, new Field(rowIdx, currentColumnIdx))) {
                fieldWithXFound = true;
            }

            currentColumnIdx--;
        }

        return colouredSequencesRangesInRowNotFurtherThanMaxSequenceLength;
    }

    static List<List<Integer>> getColouredSequencesRangesInRowInRangeOnRight(
            List<List<String>> solutionBoard,
            int rowIdx,
            int potentiallyColouredFieldColumnIndex,
            int maxSequenceLength
    ) {
        int width = solutionBoard.get(0).size();
        List<List<Integer>> colouredSequencesRangesNotFurtherThanMaxSequenceLength = new ArrayList<>();

        List<Integer> possibleColouredSequencesStartIndexesRange = Arrays.asList(
                potentiallyColouredFieldColumnIndex + DISTANCE_WITH_ONE_EMPTY_FIELD_TO_POSSIBLE_COLOURED,
                potentiallyColouredFieldColumnIndex + maxSequenceLength
        );

        boolean fieldWithXFound = false;
        int currentColumnIdx = possibleColouredSequencesStartIndexesRange.get(0);

        while (currentColumnIdx <= possibleColouredSequencesStartIndexesRange.get(1) && !fieldWithXFound) {

            if (isFieldColoured(solutionBoard, new Field(rowIdx, currentColumnIdx))) {
                int potentiallyColouredSequenceColumnIdx = currentColumnIdx;

                while (potentiallyColouredSequenceColumnIdx < width &&
                        isFieldColoured(solutionBoard, new Field(rowIdx, potentiallyColouredSequenceColumnIdx))) {
                    potentiallyColouredSequenceColumnIdx++;
                }

                List<Integer> colouredSequenceRangeInRowInRange = List.of(
                        currentColumnIdx,
                        potentiallyColouredSequenceColumnIdx - 1
                );
                colouredSequencesRangesNotFurtherThanMaxSequenceLength.add(colouredSequenceRangeInRowInRange);

                currentColumnIdx = potentiallyColouredSequenceColumnIdx + 1;

                if (currentColumnIdx < width &&
                        isFieldWithX(solutionBoard, new Field(rowIdx, currentColumnIdx))) {
                    fieldWithXFound = true;
                }
            } else if (isFieldWithX(solutionBoard, new Field(rowIdx, currentColumnIdx))) {
                fieldWithXFound = true;
            }

            currentColumnIdx++;
        }

        return colouredSequencesRangesNotFurtherThanMaxSequenceLength;
    }

    static List<Integer> findValidSequencesIdsMergingToLeft(List<Integer> sequenceIds, List<Integer> expectedLengths, int columnIndexBeforeX, List<List<Integer>> colouredSequences) {
        return IntStream.range(0, sequenceIds.size())
                .filter(i -> !wouldMergeTooLongBackward(expectedLengths.get(i), columnIndexBeforeX, colouredSequences))
                .mapToObj(sequenceIds::get)
                .toList();
    }

    static List<Integer> findValidSequencesIdsMergingToRight(List<Integer> sequenceIds, List<Integer> expectedLengths, int colouredColumnIndexAfterX, List<List<Integer>> colouredSequences) {

        return IntStream.range(0, sequenceIds.size())
                .filter(i -> !wouldMergeTooLongForward(expectedLengths.get(i), colouredColumnIndexAfterX, colouredSequences))
                .mapToObj(sequenceIds::get)
                .toList();
    }
}
