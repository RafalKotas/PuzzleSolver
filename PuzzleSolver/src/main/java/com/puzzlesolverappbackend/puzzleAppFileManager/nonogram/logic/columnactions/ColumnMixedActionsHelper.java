package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.Field;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramBoardUtils.isFieldColoured;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramBoardUtils.isFieldWithX;

@UtilityClass
public class ColumnMixedActionsHelper {

    private final static int DISTANCE_WITH_ONE_EMPTY_FIELD_TO_POSSIBLE_COLOURED = 2;

    public static List<Integer> sequencesIdsInColumnIncludingField(List<List<Integer>> columnSequencesRanges, Field field) {

        return IntStream.range(0, columnSequencesRanges.size())
                .filter(columnIdx -> {
                    int rangeStart = columnSequencesRanges.get(columnIdx).get(0);
                    int rangeEnd = columnSequencesRanges.get(columnIdx).get(1);
                    return field.getRowIdx() >= rangeStart && field.getRowIdx() <= rangeEnd;
                })
                .boxed()
                .collect(Collectors.toList());
    }

    public List<List<Integer>> getColouredSequencesRangesInColumnInRangeToTop(List<List<String>> solutionBoard, int columnIdx, int potentiallyColouredFieldRowIndex, int maxSequenceLength) {
        List<List<Integer>> colouredSequencesRangesInColumnNotFurtherThanMaxSequenceLength = new ArrayList<>();
        List<Integer> colouredSequenceRangeInColumn;

        List<Integer> possibleColouredSequencesEndIndexesRange = Arrays.asList(Math.max(potentiallyColouredFieldRowIndex - maxSequenceLength, 0),
                potentiallyColouredFieldRowIndex - DISTANCE_WITH_ONE_EMPTY_FIELD_TO_POSSIBLE_COLOURED);

        boolean fieldWithXFound = false;
        int currentRowIdx = possibleColouredSequencesEndIndexesRange.get(1);
        int potentiallyColouredSequenceRowIdx;

        while (currentRowIdx >= possibleColouredSequencesEndIndexesRange.get(0)) {

            if (isFieldColoured(solutionBoard, new Field(currentRowIdx, columnIdx))) {
                potentiallyColouredSequenceRowIdx = currentRowIdx;
                while (potentiallyColouredSequenceRowIdx >= 0 && isFieldColoured(solutionBoard, new Field(potentiallyColouredSequenceRowIdx, columnIdx))) {
                    potentiallyColouredSequenceRowIdx--;
                }
                colouredSequenceRangeInColumn = new ArrayList<>(Arrays.asList(currentRowIdx, potentiallyColouredSequenceRowIdx + 1));
                colouredSequencesRangesInColumnNotFurtherThanMaxSequenceLength.add(colouredSequenceRangeInColumn);

                currentRowIdx = potentiallyColouredSequenceRowIdx - 1; // field with this columnIdx is not coloured ("X"/"-")

                if (potentiallyColouredSequenceRowIdx < 0) {
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

            currentRowIdx--;
        }

        return colouredSequencesRangesInColumnNotFurtherThanMaxSequenceLength;
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

        while (currentRowIdx <= possibleColouredSequencesStartIndexesRange.get(1)) {

            if (isFieldColoured(solutionBoard, new Field(currentRowIdx, columnIdx))) {
                potentiallyColouredSequenceRowIdx = currentRowIdx;
                while (potentiallyColouredSequenceRowIdx < height && isFieldColoured(solutionBoard, new Field(potentiallyColouredSequenceRowIdx, columnIdx))) {
                    potentiallyColouredSequenceRowIdx++;
                }
                colouredSequenceRangeInColumnInRange = new ArrayList<>(Arrays.asList(currentRowIdx, potentiallyColouredSequenceRowIdx - 1));
                colouredSequencesRangesInColumnNotFurtherThanMaxSequenceLength.add(colouredSequenceRangeInColumnInRange);

                currentRowIdx = potentiallyColouredSequenceRowIdx + 1; // field with this rowIdx is not coloured ("X"/"-")

                if (currentRowIdx > height) {
                    break;
                }

                if(isFieldWithX(solutionBoard, new Field(currentRowIdx, columnIdx))) {
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
                .filter(i -> !wouldMergeTooLongToTop(expectedLengths.get(i), rowIndexBeforeX, colouredSequences))
                .mapToObj(sequenceIds::get)
                .collect(Collectors.toList());
    }

    // TODO(?) - same as wouldMergeTooLongToTop at RowMixedActionsHelper
    public static boolean wouldMergeTooLongToTop(int expectedLength, int rowIndexBeforeX, List<List<Integer>> colouredSequences) {
        int contactIndex = rowIndexBeforeX - expectedLength + 1;

        return colouredSequences.stream().anyMatch(colouredSequenceRange -> {
            int colouredSequenceStart = colouredSequenceRange.get(0);
            int colouredSequenceEnd = colouredSequenceRange.get(1);

            if (contactIndex <= colouredSequenceEnd + 1) {
                int firstPathEndIndex = contactIndex - 1;
                int firstPartLength = Math.max(0, firstPathEndIndex  - colouredSequenceStart + 1);
                int secondPartLength = rowIndexBeforeX - contactIndex + 1;
                return firstPartLength + secondPartLength > expectedLength;
            }
            return false;
        });
    }

    public static List<Integer> findValidSequencesIdsMergingToBottom(List<Integer> sequenceIds, List<Integer> expectedLengths, int colouredRowIndexAfterX, List<List<Integer>> colouredSequences) {

        return IntStream.range(0, sequenceIds.size())
                .filter(i -> !wouldMergeTooLongToBottom(expectedLengths.get(i), colouredRowIndexAfterX, colouredSequences))
                .mapToObj(sequenceIds::get)
                .collect(Collectors.toList());
    }

    // TODO(?) - same as wouldMergeTooLongToRight at RowMixedActionsHelper
    public static boolean wouldMergeTooLongToBottom(int expectedLength, int colouredRowIndexAfterX, List<List<Integer>> colouredSequences) {
        int contactIndex = colouredRowIndexAfterX + expectedLength - 1;

        return colouredSequences.stream().anyMatch(colouredSequenceRange -> {
            int colouredSequenceStart = colouredSequenceRange.get(0);
            int colouredSequenceEnd = colouredSequenceRange.get(1);

            if (contactIndex >= colouredSequenceStart - 1) {
                int firstPartLength = contactIndex - colouredRowIndexAfterX + 1;
                int secondPartStartIndex = contactIndex + 1;
                int secondPartLength = contactIndex == colouredSequenceEnd ? 0 : Math.max(0, colouredSequenceEnd - secondPartStartIndex + 1);
                return firstPartLength + secondPartLength > expectedLength;
            }
            return false;
        });
    }

}
