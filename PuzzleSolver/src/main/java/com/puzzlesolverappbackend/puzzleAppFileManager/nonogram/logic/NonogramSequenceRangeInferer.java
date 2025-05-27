package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramRules;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.model.NonogramConstants.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.CollectionUtils.reverseList;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramHelper.createArrayOfEmptyFields;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramHelper.generateArrayOfSequenceMarks;

@Getter
@Setter
@AllArgsConstructor
public class NonogramSequenceRangeInferer {

    private final NonogramRules nonogramRules;
    private final List<List<String>> nonogramSolutionBoardWithMarks;

    public List<List<List<Integer>>> inferInitialRowsSequencesRanges() {
        List<List<List<Integer>>> initialRowSequencesRanges = new ArrayList<>();

        for (int rowIdx = 0; rowIdx < this.getNonogramRules().getRowSequencesLengths().size(); rowIdx++) {
            initialRowSequencesRanges.add(inferInitialRowSequencesRanges(rowIdx));
        }

        return initialRowSequencesRanges;
    }

    private List<List<Integer>> inferInitialRowSequencesRanges (int rowIdx) {
        List<Integer> sequencesLengths = this.getNonogramRules().getRowSequencesLengths().get(rowIdx);

        if (sequencesLengths.size() == 1 && sequencesLengths.get(0) == 0) {
            return List.of(List.of(-1, -1));
        } else if (sequencesLengths.size() == 1 && sequencesLengths.get(0) == this.getNonogramRules().getWidth()) {
            return List.of(List.of(0, this.getNonogramRules().getWidth() - 1));
        } else {
            List<String> arrayFilledFromStart = createRowArrayFromSequencesAndChars(rowIdx, sequencesLengths, false);
            List<String> arrayFilledFromEnd = reverseList(createRowArrayFromSequencesAndChars(rowIdx, sequencesLengths, true));

            return inferRowSequencesRangesFromArrays(arrayFilledFromStart, arrayFilledFromEnd);
        }
    }

    private List<String> createRowArrayFromSequencesAndChars (int rowIdx, List<Integer> sequencesParam, boolean reverse) {

        List<Integer> sequences = sequencesParam;
        List<String> charsNeeded = generateArrayOfSequenceMarks(sequences.size());

        if (reverse) {
            sequences = reverseList(sequences);
            charsNeeded = reverseList(charsNeeded);
        }

        List<String> arrayFilledFromStart = createArrayOfEmptyFields(this.getNonogramRules().getWidth());

        boolean canStartSequenceFromIndex;
        boolean writeSequenceMode = false;
        int currentSequenceIdx = 0;
        int sequencesFieldsFilled = 0;
        String charToWrite = charsNeeded.get(currentSequenceIdx);
        int sequenceLength = sequences.get(currentSequenceIdx);
        boolean breakX = true;
        Field fieldToCheck;

        for (int columnIdx = 0; columnIdx < this.getNonogramRules().getWidth(); columnIdx++ ) {

            if (!writeSequenceMode && currentSequenceIdx < charsNeeded.size() && breakX) {
                fieldToCheck = new Field(rowIdx, columnIdx);
                canStartSequenceFromIndex = checkIfCanStartSequenceFromField(fieldToCheck, sequenceLength);
                if (canStartSequenceFromIndex) {
                    writeSequenceMode = true; // start fill fields with sequence char mark
                }
            }
            if (writeSequenceMode) { /* Marking rows with sequences marks */
                arrayFilledFromStart.set(columnIdx, MARKED_ROW_INDICATOR + charToWrite + nonogramSolutionBoardWithMarks.get(rowIdx).get(columnIdx).substring(2, 4));

                sequencesFieldsFilled++;

                if (sequencesFieldsFilled == sequenceLength) {
                    sequencesFieldsFilled = 0;
                    currentSequenceIdx++;
                    if (currentSequenceIdx < charsNeeded.size()) {
                        charToWrite = charsNeeded.get( currentSequenceIdx );
                        sequenceLength = sequences.get( currentSequenceIdx );
                    }
                    writeSequenceMode = false;
                    breakX = false;
                }
            } else {
                arrayFilledFromStart.set(columnIdx, X_FIELD_MARKED_BOARD);
                breakX = true;
            }
        }

        return arrayFilledFromStart;
    }

    private boolean checkIfCanStartSequenceFromField(Field field, int sequenceLength) {
        List<String> row = getNonogramBoardRowWithMarks(field.getRowIdx());
        if (field.getColumnIdx() + sequenceLength > row.size()) {
            return false;
        }
        List<String> fieldsToCheckSubList = row.subList(field.getColumnIdx(), field.getColumnIdx() + sequenceLength);

        Predicate<String> fieldWithX = fieldValue -> fieldValue.equals(X_FIELD_MARKED_BOARD);
        List<String> xs = fieldsToCheckSubList.stream().filter(fieldWithX).toList();

        return xs.isEmpty();
    }

    private List<String> getNonogramBoardRowWithMarks(int rowIdx) {
        return new ArrayList<>(this.nonogramSolutionBoardWithMarks.get(rowIdx));
    }

    private List<List<Integer>> inferRowSequencesRangesFromArrays (List<String> arrayFilledFromStart, List<String> arrayFilledFromEnd) {
        List<String> collectedSequences = new ArrayList<>();
        List<List<Integer>> sequencesRanges = new ArrayList<>();
        int rangeStartIndex;
        int rangeLastIndex;

        for (int idx = 0; idx < arrayFilledFromStart.size(); idx++) {
            String field = arrayFilledFromStart.get(idx);
            String fieldSequenceChar = field.substring(1, 2);
            if (field.indexOf(MARKED_ROW_INDICATOR) == 0 && !collectedSequences.contains(fieldSequenceChar)) {
                collectedSequences.add(fieldSequenceChar);
                rangeStartIndex = idx;
                rangeLastIndex = findLastIndexContaining(arrayFilledFromEnd, MARKED_ROW_INDICATOR + fieldSequenceChar);
                List<Integer> sequenceRange = new ArrayList<>();
                sequenceRange.add(rangeStartIndex);
                sequenceRange.add(rangeLastIndex);
                sequencesRanges.add(sequenceRange);
            }
        }

        return sequencesRanges;
    }

    private int findLastIndexContaining (List<String> array, String matchingSubstring) {

        for ( int arrIdx = array.size() - 1; arrIdx >= 0; arrIdx--) {
            if (array.get(arrIdx).contains(matchingSubstring)) {
                return arrIdx;
            }
        }

        return -1;
    }

    public List<List<List<Integer>>> inferInitialColumnsSequencesRanges() {
        List<List<List<Integer>>> initialColumnsSequencesRanges = new ArrayList<>();

        for (int columnIdx = 0; columnIdx < this.getNonogramRules().getColumnSequencesLengths().size(); columnIdx ++) {
            initialColumnsSequencesRanges.add(inferInitialColumnSequencesRanges(columnIdx) );
        }

        return initialColumnsSequencesRanges;
    }

    private List<List<Integer>> inferInitialColumnSequencesRanges(int columnIdx) {
        List<Integer> columnSequencesLengths = this.nonogramRules.getColumnSequencesLengths().get(columnIdx);

        if (columnSequencesLengths.size() == 1 && columnSequencesLengths.get(0) == 0) {
            return List.of(List.of(-1, -1));
        } else if (columnSequencesLengths.size() == 1 && columnSequencesLengths.get(0) == this.getNonogramRules().getHeight()) {
            return List.of(List.of(0, this.getNonogramRules().getHeight() - 1));
        } else {
            List<String> arrayFilledFromStart = createColumnArrayFromSequencesAndChars(columnIdx, columnSequencesLengths, false);
            List<String> arrayFilledFromEnd = reverseList( createColumnArrayFromSequencesAndChars(columnIdx, columnSequencesLengths, true) );

            return inferColumnSequencesRangesFromArrays(arrayFilledFromStart, arrayFilledFromEnd);
        }
    }

    private List<String> createColumnArrayFromSequencesAndChars(int columnIdx, List<Integer> sequencesParam, boolean reverse) {

        List<Integer> sequences = sequencesParam;
        List<String> charsNeeded = generateArrayOfSequenceMarks(sequences.size());

        if (reverse) {
            sequences = reverseList(sequences);
            charsNeeded = reverseList(charsNeeded);
        }

        int height = this.nonogramSolutionBoardWithMarks.size();
        List<String> arrayFilledFromStart = createArrayOfEmptyFields(height);

        boolean canStartSequenceFromIndex;
        boolean writeSequenceMode = false;
        int currentSequenceIdx = 0;
        int sequencesFieldsFilled = 0;
        String charToWrite = charsNeeded.get(currentSequenceIdx);
        int sequenceLength = sequences.get(currentSequenceIdx);
        boolean breakX = true;

        for (int fieldIdx = 0; fieldIdx < height; fieldIdx++ ) {

            if (!writeSequenceMode && currentSequenceIdx < charsNeeded.size() && breakX) {
                canStartSequenceFromIndex = checkIfCanStartSequenceFromColumnIndex(columnIdx, fieldIdx, sequenceLength);
                if (canStartSequenceFromIndex) {
                    writeSequenceMode = true; // start fill fields with sequence char mark
                }
            }
            if (writeSequenceMode) {

                arrayFilledFromStart.set(fieldIdx, this.nonogramSolutionBoardWithMarks.get(fieldIdx).get(columnIdx).substring(0, 2) + MARKED_COLUMN_INDICATOR + charToWrite);

                sequencesFieldsFilled++;

                if (sequencesFieldsFilled == sequenceLength) {
                    sequencesFieldsFilled = 0;
                    currentSequenceIdx++;
                    if (currentSequenceIdx < charsNeeded.size()) {
                        charToWrite = charsNeeded.get( currentSequenceIdx );
                        sequenceLength = sequences.get( currentSequenceIdx );
                    }
                    writeSequenceMode = false;
                    breakX = false;
                }
            } else {
                arrayFilledFromStart.set(fieldIdx, X_FIELD_MARKED_BOARD);
                breakX = true;
            }
        }

        return arrayFilledFromStart;
    }

    private boolean checkIfCanStartSequenceFromColumnIndex (int columnIdx, int fieldIdx, int sequenceLength) {
        List<String> fieldsToCheck = getSolutionBoardWithMarksColumn(columnIdx).subList(fieldIdx, fieldIdx + sequenceLength);

        Predicate<String> fieldWithX = field -> field.equals(X_FIELD_MARKED_BOARD);

        List<String> xs = fieldsToCheck.stream().filter(fieldWithX).toList();

        return xs.isEmpty();
    }

    List<String> getSolutionBoardWithMarksColumn(int columnIdx) {
        List<String> boardColumn = new ArrayList<>();

        for (List<String> nonogramSolutionBoardWithMark : this.nonogramSolutionBoardWithMarks) {
            boardColumn.add(nonogramSolutionBoardWithMark.get(columnIdx));
        }

        return boardColumn;
    }

    private List<List<Integer>> inferColumnSequencesRangesFromArrays (List<String> arrayFilledFromStart, List<String> arrayFilledFromEnd) {
        List<String> collectedSequences = new ArrayList<>();
        List<List<Integer>> sequencesRanges = new ArrayList<>();
        int rangeStartIndex;
        int rangeLastIndex;

        for (int idx = 0; idx < arrayFilledFromStart.size(); idx++) {
            String field = arrayFilledFromStart.get(idx);
            String fieldSequenceChar = field.substring(3, 4);
            if (field.indexOf(MARKED_COLUMN_INDICATOR) == 2 && !collectedSequences.contains(fieldSequenceChar)) {
                collectedSequences.add(fieldSequenceChar);
                rangeStartIndex = idx;
                rangeLastIndex = findLastIndexContaining(arrayFilledFromEnd, MARKED_COLUMN_INDICATOR + fieldSequenceChar);
                List<Integer> sequenceRange = new ArrayList<>();
                sequenceRange.add(rangeStartIndex);
                sequenceRange.add(rangeLastIndex);
                sequencesRanges.add(sequenceRange);
            }
        }

        return sequencesRanges;
    }
}
