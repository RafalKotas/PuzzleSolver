package com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.rowactions;

import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.Field;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzleAppFileManager.utils.NonogramBoardUtils.isFieldColoured;
import static com.puzzlesolverappbackend.puzzleAppFileManager.utils.NonogramBoardUtils.isFieldWithX;

@UtilityClass
public class MixedActionsHelper {

    public static int DISTANCE_WITH_ONE_EMPTY_FIELD_TO_POSSIBLE_COLOURED = 2;

    public static List<Integer> sequencesIdsInRowIncludingField(List<List<Integer>> rowSequencesRanges, Field field) {

        return IntStream.range(0, rowSequencesRanges.size())
                .filter(i -> {
                    int start = rowSequencesRanges.get(i).get(0);
                    int end = rowSequencesRanges.get(i).get(1);
                    return field.getColumnIdx() >= start && field.getColumnIdx() <= end;
                })
                .boxed()
                .collect(Collectors.toList());
    }

    public List<List<Integer>> getColouredSequencesRangesInRowInRangeOnLeft(List<List<String>> solutionBoard, int rowIdx, int potentiallyColouredFieldColumn, int maxSequenceLength) {
        int DISTANCE_WITH_ONE_EMPTY_FIELD_TO_POSSIBLE_COLOURED = 2;
        List<List<Integer>> colouredSequencesRangesInRowInRange = new ArrayList<>();
        List<Integer> colouredSequenceRangeInRowInRange;

        List<Integer> possibleColouredSequencesEndIndexesRange = Arrays.asList(Math.max(potentiallyColouredFieldColumn - maxSequenceLength, 0),
                potentiallyColouredFieldColumn - DISTANCE_WITH_ONE_EMPTY_FIELD_TO_POSSIBLE_COLOURED);

        boolean fieldWithXFound = false;
        int currentColumnIdx = possibleColouredSequencesEndIndexesRange.get(1);
        int potentiallyColouredSequenceColumnIdx;

        while (currentColumnIdx >= possibleColouredSequencesEndIndexesRange.get(0)) {

            if (isFieldColoured(solutionBoard, new Field(rowIdx, currentColumnIdx))) {
                potentiallyColouredSequenceColumnIdx = currentColumnIdx;
                while (potentiallyColouredSequenceColumnIdx >= 0 && isFieldColoured(solutionBoard, new Field(rowIdx, potentiallyColouredSequenceColumnIdx))) {
                    potentiallyColouredSequenceColumnIdx--;
                }
                colouredSequenceRangeInRowInRange = new ArrayList<>(Arrays.asList(potentiallyColouredSequenceColumnIdx + 1, currentColumnIdx));
                colouredSequencesRangesInRowInRange.add(colouredSequenceRangeInRowInRange);

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

        return colouredSequencesRangesInRowInRange;
    }

    public List<List<Integer>> getColouredSequencesRangesInRowInRangeOnRight(List<List<String>> solutionBoard, int rowIdx, int potentiallyColouredFieldColumn, int maxSequenceLength) {
        int width = solutionBoard.get(0).size();

        List<List<Integer>> colouredSequencesRangesInRowInRange = new ArrayList<>();
        List<Integer> colouredSequenceRangeInRowInRange;

        List<Integer> possibleColouredSequencesStartIndexesRange = Arrays.asList(potentiallyColouredFieldColumn + DISTANCE_WITH_ONE_EMPTY_FIELD_TO_POSSIBLE_COLOURED,
                potentiallyColouredFieldColumn + maxSequenceLength);

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
                colouredSequencesRangesInRowInRange.add(colouredSequenceRangeInRowInRange);

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

        return colouredSequencesRangesInRowInRange;
    }

    public static List<Integer> findValidSequencesIdsMergingToLeft(List<Integer> sequenceIds, List<Integer> expectedLengths, int columnIndexBeforeX, List<List<Integer>> coloredSequences) {
        return IntStream.range(0, sequenceIds.size())
                .filter(i -> !wouldMergeTooLongToLeft(expectedLengths.get(i), columnIndexBeforeX, coloredSequences))
                .mapToObj(sequenceIds::get)
                .collect(Collectors.toList());
    }

    public static boolean wouldMergeTooLongToLeft(int expectedLength, int columnIndexBeforeX, List<List<Integer>> coloredSequences) {
        int contactIndex = columnIndexBeforeX - expectedLength + 1;

        return coloredSequences.stream().anyMatch(colouredSequenceRange -> {
            int coloredSequenceStart = colouredSequenceRange.get(0);
            int coloredSequenceEnd = colouredSequenceRange.get(1);

            if (contactIndex <= coloredSequenceEnd + 1) {
                int firstPathEndIndex = contactIndex - 1;
                int firstPartLength = Math.max(0, firstPathEndIndex  - coloredSequenceStart + 1);
                int secondPartLength = columnIndexBeforeX - contactIndex + 1;
                return firstPartLength + secondPartLength > expectedLength;
            }
            return false;
        });
    }

    public static List<Integer> findValidSequencesIdsMergingToRight(List<Integer> sequenceIds, List<Integer> expectedLengths, int colouredColumnIndexAfterX, List<List<Integer>> coloredSequences) {

        return IntStream.range(0, sequenceIds.size())
                .filter(i -> !wouldMergeTooLongToRight(expectedLengths.get(i), colouredColumnIndexAfterX, coloredSequences))
                .mapToObj(sequenceIds::get)
                .collect(Collectors.toList());
    }

    // TODO - check inversed case o10401 - start from column 18
    public static boolean wouldMergeTooLongToRight(int expectedLength, int colouredColumnIndexAfterX, List<List<Integer>> coloredSequences) {
        int contactIndex = colouredColumnIndexAfterX + expectedLength - 1;

        return coloredSequences.stream().anyMatch(colouredSequenceRange -> {
            int coloredSequenceStart = colouredSequenceRange.get(0);
            int coloredSequenceEnd = colouredSequenceRange.get(1);

            if (contactIndex >= coloredSequenceStart - 1) {
                int firstPartLength = contactIndex - colouredColumnIndexAfterX + 1;
                int secondPartStartIndex = contactIndex + 1;
                int secondPartLength = contactIndex == coloredSequenceEnd ? 0 : Math.max(0, secondPartStartIndex  - coloredSequenceEnd + 1);
                return firstPartLength + secondPartLength > expectedLength;
            }
            return false;
        });
    }
}
