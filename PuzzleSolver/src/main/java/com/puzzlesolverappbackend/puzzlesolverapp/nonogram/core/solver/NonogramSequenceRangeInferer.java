package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramConstants.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.createEmptyMarkedLine;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.generateSequenceMarks;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.util.CollectionUtils.reverseList;

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

    private List<String> createRowArrayFromSequencesAndChars(int rowIdx, List<Integer> sequencesParam, boolean reverse) {
        List<Integer> sequences = new ArrayList<>(sequencesParam);
        List<String> charsNeeded = generateSequenceMarks(sequences.size());

        if (reverse) {
            sequences = reverseList(sequences);
            charsNeeded = reverseList(charsNeeded);
        }

        List<String> arrayFilled = createEmptyMarkedLine(this.getNonogramRules().getWidth());

        int currentSequenceIdx = 0;
        int filledInSequence = 0;
        boolean writingSequence = false;
        boolean allowStart = true;

        String currentChar = charsNeeded.get(currentSequenceIdx);
        int currentLength = sequences.get(currentSequenceIdx);

        for (int colIdx = 0; colIdx < arrayFilled.size(); colIdx++) {
            if (!writingSequence && currentSequenceIdx < charsNeeded.size() && allowStart && canStartSequence(rowIdx, colIdx, currentLength)) {
                writingSequence = true;
            }

            if (writingSequence) {
                applyMarking(rowIdx, colIdx, currentChar, arrayFilled);
                filledInSequence++;

                if (filledInSequence == currentLength) {
                    filledInSequence = 0;
                    currentSequenceIdx++;
                    if (currentSequenceIdx < charsNeeded.size()) {
                        currentChar = charsNeeded.get(currentSequenceIdx);
                        currentLength = sequences.get(currentSequenceIdx);
                    }
                    writingSequence = false;
                    allowStart = false;
                }
            } else {
                arrayFilled.set(colIdx, X_FIELD_MARKED_BOARD);
                allowStart = true;
            }
        }

        return arrayFilled;
    }

    private boolean canStartSequence(int rowIdx, int colIdx, int sequenceLength) {
        Field field = new Field(rowIdx, colIdx);
        return checkIfCanStartSequenceFromField(field, sequenceLength);
    }

    private void applyMarking(int rowIdx, int colIdx, String charToWrite, List<String> array) {
        String suffix = nonogramSolutionBoardWithMarks.get(rowIdx).get(colIdx).substring(2, 4);
        array.set(colIdx, MARKED_ROW_INDICATOR + charToWrite + suffix);
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
        List<Integer> sequences = reverse ? reverseList(sequencesParam) : sequencesParam;
        List<String> charsNeeded = reverse ? reverseList(generateSequenceMarks(sequences.size())) : generateSequenceMarks(sequences.size());

        int height = this.nonogramSolutionBoardWithMarks.size();
        List<String> arrayFilledFromStart = createEmptyMarkedLine(height);

        boolean writeSequenceMode = false;
        int currentSequenceIdx = 0;
        int sequencesFieldsFilled = 0;
        String charToWrite = charsNeeded.get(currentSequenceIdx);
        int sequenceLength = sequences.get(currentSequenceIdx);
        boolean breakX = true;

        for (int fieldIdx = 0; fieldIdx < height; fieldIdx++) {
            if (shouldStartSequence(writeSequenceMode, currentSequenceIdx, charsNeeded.size(), breakX)
                    && checkIfCanStartSequenceFromColumnIndex(columnIdx, fieldIdx, sequenceLength)) {
                writeSequenceMode = true;
            }

            if (writeSequenceMode) {
                markSequenceField(arrayFilledFromStart, fieldIdx, columnIdx, charToWrite);
                sequencesFieldsFilled++;

                if (sequencesFieldsFilled == sequenceLength) {
                    writeSequenceMode = false;
                    breakX = false;
                    sequencesFieldsFilled = 0;
                    currentSequenceIdx++;

                    if (currentSequenceIdx < charsNeeded.size()) {
                        charToWrite = charsNeeded.get(currentSequenceIdx);
                        sequenceLength = sequences.get(currentSequenceIdx);
                    }
                }
            } else {
                arrayFilledFromStart.set(fieldIdx, X_FIELD_MARKED_BOARD);
                breakX = true;
            }
        }

        return arrayFilledFromStart;
    }

    private boolean shouldStartSequence(boolean writeSequenceMode, int currentIdx, int total, boolean breakX) {
        return !writeSequenceMode && currentIdx < total && breakX;
    }

    private void markSequenceField(List<String> line, int fieldIdx, int columnIdx, String markChar) {
        String base = this.nonogramSolutionBoardWithMarks.get(fieldIdx).get(columnIdx).substring(0, 2);
        line.set(fieldIdx, base + MARKED_COLUMN_INDICATOR + markChar);
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
