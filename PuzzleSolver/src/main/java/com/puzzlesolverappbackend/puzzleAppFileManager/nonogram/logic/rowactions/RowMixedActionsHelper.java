package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions;

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
public class RowMixedActionsHelper {

    private final static int DISTANCE_WITH_ONE_EMPTY_FIELD_TO_POSSIBLE_COLOURED = 2;

    public static List<Integer> sequencesIdsInRowIncludingField(List<List<Integer>> rowSequencesRanges, Field field) {

        return IntStream.range(0, rowSequencesRanges.size())
                .filter(i -> {
                    int rangeStart = rowSequencesRanges.get(i).get(0);
                    int rangeEnd = rowSequencesRanges.get(i).get(1);
                    return field.getColumnIdx() >= rangeStart && field.getColumnIdx() <= rangeEnd;
                })
                .boxed()
                .collect(Collectors.toList());
    }

    public List<List<Integer>> getColouredSequencesRangesInRowInRangeOnLeft(List<List<String>> solutionBoard, int rowIdx, int potentiallyColouredFieldColumnIndex, int maxSequenceLength) {
        List<List<Integer>> colouredSequencesRangesInRowNotFurtherThanMaxSequenceLength = new ArrayList<>();
        List<Integer> colouredSequenceRangeInRow;

        List<Integer> possibleColouredSequencesEndIndexesRange = Arrays.asList(Math.max(potentiallyColouredFieldColumnIndex - maxSequenceLength, 0),
                potentiallyColouredFieldColumnIndex - DISTANCE_WITH_ONE_EMPTY_FIELD_TO_POSSIBLE_COLOURED);

        boolean fieldWithXFound = false;
        int currentColumnIdx = possibleColouredSequencesEndIndexesRange.get(1);
        int potentiallyColouredSequenceColumnIdx;

        while (currentColumnIdx >= possibleColouredSequencesEndIndexesRange.get(0)) {

            if (isFieldColoured(solutionBoard, new Field(rowIdx, currentColumnIdx))) {
                potentiallyColouredSequenceColumnIdx = currentColumnIdx;
                while (potentiallyColouredSequenceColumnIdx >= 0 && isFieldColoured(solutionBoard, new Field(rowIdx, potentiallyColouredSequenceColumnIdx))) {
                    potentiallyColouredSequenceColumnIdx--;
                }
                colouredSequenceRangeInRow = new ArrayList<>(Arrays.asList(potentiallyColouredSequenceColumnIdx + 1, currentColumnIdx));
                colouredSequencesRangesInRowNotFurtherThanMaxSequenceLength.add(colouredSequenceRangeInRow);

                currentColumnIdx = potentiallyColouredSequenceColumnIdx - 1; // field with this columnIdx is not coloured ("X"/"-")

                if (potentiallyColouredSequenceColumnIdx < 0) {
                    break;
                }

                if (isFieldWithX(solutionBoard, new Field(rowIdx, currentColumnIdx))) {
                    fieldWithXFound = true;
                }
            } else if (isFieldWithX(solutionBoard, new Field(rowIdx, currentColumnIdx))) {
                fieldWithXFound = true;
            }

            if (fieldWithXFound) {
                break;
            }

            currentColumnIdx--;
        }

        return colouredSequencesRangesInRowNotFurtherThanMaxSequenceLength;
    }

    public static List<List<Integer>> getColouredSequencesRangesInRowInRangeOnRight(List<List<String>> solutionBoard, int rowIdx, int potentiallyColouredFieldColumnIndex, int maxSequenceLength) {
        int width = solutionBoard.get(0).size();

        List<List<Integer>> colouredSequencesRangesNotFurtherThanMaxSequenceLength = new ArrayList<>();
        List<Integer> colouredSequenceRangeInRowInRange;

        List<Integer> possibleColouredSequencesStartIndexesRange = Arrays.asList(potentiallyColouredFieldColumnIndex + DISTANCE_WITH_ONE_EMPTY_FIELD_TO_POSSIBLE_COLOURED,
                potentiallyColouredFieldColumnIndex + maxSequenceLength);

        boolean fieldWithXFound = false;
        int currentColumnIdx = possibleColouredSequencesStartIndexesRange.get(0);
        int potentiallyColouredSequenceColumnIdx;

        while (currentColumnIdx <= possibleColouredSequencesStartIndexesRange.get(1)) {

            if (isFieldColoured(solutionBoard, new Field(rowIdx, currentColumnIdx))) {
                potentiallyColouredSequenceColumnIdx = currentColumnIdx;
                while (potentiallyColouredSequenceColumnIdx < width && isFieldColoured(solutionBoard, new Field(rowIdx, potentiallyColouredSequenceColumnIdx))) {
                    potentiallyColouredSequenceColumnIdx++;
                }
                colouredSequenceRangeInRowInRange = new ArrayList<>(Arrays.asList(currentColumnIdx, potentiallyColouredSequenceColumnIdx - 1));
                colouredSequencesRangesNotFurtherThanMaxSequenceLength.add(colouredSequenceRangeInRowInRange);

                currentColumnIdx = potentiallyColouredSequenceColumnIdx + 1; // field with this columnIdx is not coloured ("X"/"-")

                if (currentColumnIdx > width) {
                    break;
                }

                if(isFieldWithX(solutionBoard, new Field(rowIdx, currentColumnIdx))) {
                    fieldWithXFound = true;
                }
            } else if (isFieldWithX(solutionBoard, new Field(rowIdx, currentColumnIdx))) {
                fieldWithXFound = true;
            }

            if (fieldWithXFound) {
                break;
            }

            currentColumnIdx++;
        }

        return colouredSequencesRangesNotFurtherThanMaxSequenceLength;
    }

    public static List<Integer> findValidSequencesIdsMergingToLeft(List<Integer> sequenceIds, List<Integer> expectedLengths, int columnIndexBeforeX, List<List<Integer>> colouredSequences) {
        return IntStream.range(0, sequenceIds.size())
                .filter(i -> !wouldMergeTooLongToLeft(expectedLengths.get(i), columnIndexBeforeX, colouredSequences))
                .mapToObj(sequenceIds::get)
                .collect(Collectors.toList());
    }

    // TODO(?) - same as wouldMergeTooLongToTop at ColumnMixedActionsHelper
    public static boolean wouldMergeTooLongToLeft(int expectedLength, int columnIndexBeforeX, List<List<Integer>> colouredSequences) {
        int contactIndex = columnIndexBeforeX - expectedLength + 1;

        return colouredSequences.stream().anyMatch(colouredSequenceRange -> {
            int colouredSequenceStart = colouredSequenceRange.get(0);
            int colouredSequenceEnd = colouredSequenceRange.get(1);

            if (contactIndex <= colouredSequenceEnd + 1) {
                int firstPathEndIndex = contactIndex - 1;
                int firstPartLength = Math.max(0, firstPathEndIndex  - colouredSequenceStart + 1);
                int secondPartLength = columnIndexBeforeX - contactIndex + 1;
                return firstPartLength + secondPartLength > expectedLength;
            }
            return false;
        });
    }

    public static List<Integer> findValidSequencesIdsMergingToRight(List<Integer> sequenceIds, List<Integer> expectedLengths, int colouredColumnIndexAfterX, List<List<Integer>> colouredSequences) {

        return IntStream.range(0, sequenceIds.size())
                .filter(i -> !wouldMergeTooLongToRight(expectedLengths.get(i), colouredColumnIndexAfterX, colouredSequences))
                .mapToObj(sequenceIds::get)
                .collect(Collectors.toList());
    }

    // TODO - check inversed case o10401 - start from column 18
    // TODO(?) - same as wouldMergeTooLongToBottom at ColumnMixedActionsHelper
    public static boolean wouldMergeTooLongToRight(int expectedLength, int colouredColumnIndexAfterX, List<List<Integer>> colouredSequences) {
        int contactIndex = colouredColumnIndexAfterX + expectedLength - 1;

        return colouredSequences.stream().anyMatch(colouredSequenceRange -> {
            int colouredSequenceStart = colouredSequenceRange.get(0);
            int colouredSequenceEnd = colouredSequenceRange.get(1);

            if (contactIndex >= colouredSequenceStart - 1) {
                int firstPartLength = contactIndex - colouredColumnIndexAfterX + 1;
                int secondPartStartIndex = contactIndex + 1;
                int secondPartLength = contactIndex == colouredSequenceEnd ? 0 : Math.max(0, colouredSequenceEnd - secondPartStartIndex + 1);
                return firstPartLength + secondPartLength > expectedLength;
            }
            return false;
        });
    }
}
