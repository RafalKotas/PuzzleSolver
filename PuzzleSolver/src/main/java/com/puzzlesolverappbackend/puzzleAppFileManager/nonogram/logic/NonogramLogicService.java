package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.enums.NonogramSolveAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.NonogramConstants.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.utils.ArrayUtils.rangeInsideAnotherRange;
import static com.puzzlesolverappbackend.puzzleAppFileManager.utils.ArrayUtils.rangeLength;

@Service
@Slf4j
public class NonogramLogicService {

    private final boolean showRepetitions = false;

    // iterations through all columns
    public NonogramLogic fillOverLappingFieldsInColumnsRange (NonogramLogic nonogramLogicObject, int columnBegin, int columnEnd) {
        NonogramLogic nonogramLogicObjectToChange = nonogramLogicObject;
        int nonogramWidth = nonogramLogicObject.getColumnsSequences().size();
        for (int columnIdx = columnBegin; columnIdx < nonogramWidth && columnIdx < columnEnd; columnIdx++) {
            nonogramLogicObjectToChange = fillOverlappingFieldsInColumn(nonogramLogicObjectToChange, columnIdx);
        }
        return nonogramLogicObjectToChange;
    }


    public NonogramLogic fillOverlappingFieldsInColumn (NonogramLogic nonogramLogicObject, int columnIdx) {
        List<Integer> sequencesInColumn = nonogramLogicObject.getColumnsSequences().get(columnIdx);
        List<List<Integer>> sequencesInColumnRanges = nonogramLogicObject.getColumnsSequencesRanges().get(columnIdx);

        int sequenceLength;
        int rangeBeginIndex;
        int rangeEndIndex;
        int colourBeginRowIndex;
        int colourEndRowIndex;

        String sequenceCharMark;

        List<String> rowToChangeColumnBoard;

        List<String> rowToChangeColumnBoardWithMarks;
        String elementToChangeInsideRowBoardWithMarks;

        for (int sequenceIdx = 0; sequenceIdx < sequencesInColumn.size(); sequenceIdx++) {
            sequenceLength = sequencesInColumn.get(sequenceIdx);
            rangeBeginIndex = sequencesInColumnRanges.get(sequenceIdx).get(0);
            rangeEndIndex = sequencesInColumnRanges.get(sequenceIdx).get(1);

            colourBeginRowIndex = rangeEndIndex - sequenceLength + 1;
            colourEndRowIndex = rangeBeginIndex + sequenceLength - 1;

            if (colourBeginRowIndex <= colourEndRowIndex) {
                sequenceCharMark = NonogramLogic.indexToSequenceCharMark(sequenceIdx);
                for (int rowIdx = colourBeginRowIndex; rowIdx <= colourEndRowIndex; rowIdx++) {
                    rowToChangeColumnBoardWithMarks = nonogramLogicObject.getNonogramSolutionBoardWithMarks().get(rowIdx);
                    elementToChangeInsideRowBoardWithMarks = rowToChangeColumnBoardWithMarks.get(columnIdx);


                    if (rowToChangeColumnBoardWithMarks.get(columnIdx).substring(2).equals(EMPTY_PART_MARKED_BOARD)) {
                        nonogramLogicObject.getNonogramState().increaseMadeSteps();

                        rowToChangeColumnBoardWithMarks.set(columnIdx, elementToChangeInsideRowBoardWithMarks.substring(0, 2) + "C" + sequenceCharMark);
                        nonogramLogicObject.getNonogramSolutionBoardWithMarks().set(rowIdx, rowToChangeColumnBoardWithMarks);
                        nonogramLogicObject.getNonogramState().increaseMadeSteps();

                        rowToChangeColumnBoard = nonogramLogicObject.getNonogramSolutionBoard().get(rowIdx);
                        if (rowToChangeColumnBoard.get(columnIdx).equals(EMPTY_FIELD_MARKED_BOARD)) {
                            rowToChangeColumnBoard.set(columnIdx, COLOURED_FIELD_MARKED_BOARD);
                            nonogramLogicObject.getNonogramSolutionBoard().set(rowIdx, rowToChangeColumnBoard);
                        }
                    } else if (this.showRepetitions) {
                        System.out.println("Column field was coloured before!");
                    }
                }
            }
        }

        return nonogramLogicObject;
    }

    // iterations through all rows
    public NonogramLogic fillOverLappingFieldsInRowsRange(NonogramLogic nonogramLogicObject, int rowBegin, int rowEnd) {
        NonogramLogic nonogramLogicObjectToChange = nonogramLogicObject;
        int nonogramHeight = nonogramLogicObject.getRowsSequences().size();
        for (int rowIdx = rowBegin; rowIdx < nonogramHeight && rowIdx < rowEnd; rowIdx++) {
            nonogramLogicObjectToChange = fillOverlappingFieldsInRow(nonogramLogicObjectToChange, rowIdx);
        }
        return nonogramLogicObjectToChange;
    }

    public NonogramLogic fillOverlappingFieldsInRow (NonogramLogic nonogramLogicObject, int rowIdx) {
        List<Integer> sequencesInRow = nonogramLogicObject.getRowsSequences().get(rowIdx);
        List<List<Integer>> sequencesInRowRanges = nonogramLogicObject.getRowsSequencesRanges().get(rowIdx);

        int sequenceLength;
        int rangeBeginIndex;
        int rangeEndIndex;
        int colourBeginColumnIndex;
        int colourEndColumnIndex;

        String sequenceCharMark;

        List<String> rowToChangeSolutionBoard;
        List<String> rowToChangeSolutionBoardWithMarks;

        String elementToChangeInsideRowBoardWithMarks;

        for (int sequenceIdx = 0; sequenceIdx < sequencesInRow.size(); sequenceIdx++) {
            sequenceLength = sequencesInRow.get(sequenceIdx);
            rangeBeginIndex = sequencesInRowRanges.get(sequenceIdx).get(0);
            rangeEndIndex = sequencesInRowRanges.get(sequenceIdx).get(1);

            colourBeginColumnIndex = rangeEndIndex - sequenceLength + 1;
            colourEndColumnIndex = rangeBeginIndex + sequenceLength - 1;

            if (colourBeginColumnIndex <= colourEndColumnIndex) {

                sequenceCharMark = NonogramLogic.indexToSequenceCharMark(sequenceIdx);
                rowToChangeSolutionBoardWithMarks = nonogramLogicObject.getNonogramSolutionBoardWithMarks().get(rowIdx);

                for (int columnIdx = colourBeginColumnIndex; columnIdx <= colourEndColumnIndex; columnIdx++) {
                    elementToChangeInsideRowBoardWithMarks = rowToChangeSolutionBoardWithMarks.get(columnIdx);

                    if (rowToChangeSolutionBoardWithMarks.get(columnIdx).startsWith(EMPTY_PART_MARKED_BOARD)) {
                        nonogramLogicObject.getNonogramState().increaseMadeSteps();
                        rowToChangeSolutionBoardWithMarks.set(columnIdx, "R" + sequenceCharMark + elementToChangeInsideRowBoardWithMarks.substring(2, 4));

                        rowToChangeSolutionBoard = nonogramLogicObject.getNonogramSolutionBoard().get(rowIdx);
                        if (rowToChangeSolutionBoard.get(columnIdx).equals(EMPTY_FIELD_MARKED_BOARD)) {
                            rowToChangeSolutionBoard.set(columnIdx, COLOURED_FIELD_MARKED_BOARD);
                        }
                    } else if (this.showRepetitions) {
                        System.out.println("Row field was coloured before!");
                    }
                }
            }
        }

        return nonogramLogicObject;
    }

    // mark iterations through all rows
    public NonogramLogic markAvailableSequencesInRows(NonogramLogic nonogramLogicObject, int rowBegin, int rowEnd) {
        NonogramLogic nonogramLogicDataToChange = nonogramLogicObject;

        for (int rowIdx = rowBegin; rowIdx < rowEnd; rowIdx++) {
            nonogramLogicDataToChange = markAvailableSequencesInRow(nonogramLogicDataToChange, rowIdx);
        }

        return nonogramLogicDataToChange;
    }

    public NonogramLogic markAvailableSequencesInRow(NonogramLogic nonogramLogicObject, int rowIdx) {

        List<String> boardRow = nonogramLogicObject.getNonogramSolutionBoard().get(rowIdx);
        List<Integer> colouredSequenceIndexes;
        int firstSequenceIndex;
        int lastSequenceIndex;
        List<Integer> rowSequences = nonogramLogicObject.getRowsSequences().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = nonogramLogicObject.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequenceRange;
        int matchingSequencesCount;
        int lastMatchingSequenceIndex = -1;
        int colouredSequenceLength;
        int matchingSequenceLength;
        boolean sequenceEqualsRowSequenceLength;
        String sequenceMarker;

        int oldRangeBeginIndex;
        int oldRangeEndIndex;
        int updatedRangeBeginIndex;
        int updatedRangeEndIndex;

        for (int columnIdx = 0; columnIdx < boardRow.size(); columnIdx++) {

            if (boardRow.get(columnIdx).equals(COLOURED_FIELD)) {

                colouredSequenceIndexes = new ArrayList<>();
                colouredSequenceIndexes.add(columnIdx);

                //collect indexes of current coloured sequence
                while(columnIdx < boardRow.size() && boardRow.get(columnIdx).equals(COLOURED_FIELD)) {
                    columnIdx++;
                }

                colouredSequenceIndexes.add(columnIdx - 1);

                firstSequenceIndex = colouredSequenceIndexes.get(0);
                lastSequenceIndex = colouredSequenceIndexes.get(colouredSequenceIndexes.size() - 1);
                colouredSequenceLength = lastSequenceIndex - firstSequenceIndex  + 1;

                matchingSequencesCount = 0;

                //check how many sequences matching conditions for mark
                for (int seqNo = 0; seqNo < rowSequences.size(); seqNo++) {

                    rowSequenceRange = rowSequencesRanges.get(seqNo);

                    if ( rangeInsideAnotherRange(colouredSequenceIndexes, rowSequenceRange)
                            && colouredSequenceLength <= rowSequences.get(seqNo)) {
                        matchingSequencesCount++;
                        lastMatchingSequenceIndex = seqNo;
                    }
                }

                //NOTE!!! if matching count == 0 and there are sequences that are not fulfilled -> wrong solution
                if (matchingSequencesCount == 1) {

                    matchingSequenceLength = rowSequences.get(lastMatchingSequenceIndex);
                    sequenceEqualsRowSequenceLength = colouredSequenceLength == matchingSequenceLength;

                    if (sequenceEqualsRowSequenceLength) {
                        rowSequenceRange = Arrays.asList(firstSequenceIndex, lastSequenceIndex);
                    } else {
                        oldRangeBeginIndex = nonogramLogicObject.getRowsSequencesRanges().get(rowIdx).get(lastMatchingSequenceIndex).get(0);
                        oldRangeEndIndex = nonogramLogicObject.getRowsSequencesRanges().get(rowIdx).get(lastMatchingSequenceIndex).get(1);

                        updatedRangeBeginIndex = Math.max(0,
                                nonogramLogicObject.getNonogramRowLogic().minimumColumnIndexWithoutX(rowIdx, lastSequenceIndex, matchingSequenceLength));
                        updatedRangeEndIndex = Math.min(nonogramLogicObject.getWidth() - 1,
                                nonogramLogicObject.getNonogramRowLogic().maximumColumnIndexWithoutX(rowIdx, firstSequenceIndex, matchingSequenceLength));
                        rowSequenceRange = Arrays.asList(Math.max(oldRangeBeginIndex, updatedRangeBeginIndex),
                                Math.min(oldRangeEndIndex, updatedRangeEndIndex));
                    }
                    nonogramLogicObject.getNonogramRowLogic().updateRowSequenceRange(rowIdx, lastMatchingSequenceIndex, rowSequenceRange);

                    //mark sequence part
                    sequenceMarker = NonogramLogic.indexToSequenceCharMark(lastMatchingSequenceIndex);
                    for (int sequenceColumnIdx = firstSequenceIndex; sequenceColumnIdx <= lastSequenceIndex; sequenceColumnIdx++) {
                        if (nonogramLogicObject.getNonogramSolutionBoardWithMarks().get(rowIdx).get(sequenceColumnIdx).startsWith(EMPTY_PART_MARKED_BOARD)) {
                            nonogramLogicObject.getNonogramRowLogic().markRowBoardField(rowIdx, sequenceColumnIdx, sequenceMarker);
                            nonogramLogicObject.getNonogramState().increaseMadeSteps();
                        } else if (this.showRepetitions) {
                            System.out.println("Row field was marked before.");
                        }
                    }
                }
            }
        }

        return nonogramLogicObject;
    }

    // iterations through all columns
    public NonogramLogic markAvailableSequencesInColumns(NonogramLogic nonogramLogicObject, int columnBegin, int columnEnd) {
        NonogramLogic nonogramLogicDataToChange = nonogramLogicObject;

        for (int columnIdx = columnBegin; columnIdx < columnEnd; columnIdx++) {
            nonogramLogicDataToChange = markAvailableSequencesInColumn(nonogramLogicDataToChange, columnIdx);
        }

        return nonogramLogicDataToChange;
    }

    public NonogramLogic markAvailableSequencesInColumn(NonogramLogic nonogramLogicObject, int columnIdx) {

        List<List<String>> nonogramSolutionBoardWithMarks = nonogramLogicObject.getNonogramSolutionBoardWithMarks();
        List<List<String>> nonogramSolutionBoard = nonogramLogicObject.getNonogramSolutionBoard();
        List<Integer> colouredSequenceIndexes;
        int firstSequenceIndex;
        int lastSequenceIndex;
        List<Integer> columnSequences = nonogramLogicObject.getColumnsSequences().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = nonogramLogicObject.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequenceRange;
        int matchingSequencesCount;
        int lastMatchingSequenceIndex = -1;
        int colouredSequenceLength;
        int matchingSequenceLength;
        boolean sequenceEqualsColumnSequenceLength;
        String sequenceMarker;

        int oldRangeBeginIndex;
        int oldRangeEndIndex;
        int updatedRangeBeginIndex;
        int updatedRangeEndIndex;

        for (int rowIdx = 0; rowIdx < nonogramSolutionBoardWithMarks.size(); rowIdx++) {

            if (nonogramSolutionBoard.get(rowIdx).get(columnIdx).equals(COLOURED_FIELD)) {

                colouredSequenceIndexes = new ArrayList<>();

                colouredSequenceIndexes.add(rowIdx);
                //collect indexes of current coloured sequence
                while(rowIdx < nonogramSolutionBoard.size() && nonogramSolutionBoard.get(rowIdx).get(columnIdx).equals(COLOURED_FIELD)) {
                    rowIdx++;
                }

                colouredSequenceIndexes.add(rowIdx - 1);

                firstSequenceIndex = colouredSequenceIndexes.get(0);
                lastSequenceIndex = colouredSequenceIndexes.get(colouredSequenceIndexes.size() - 1);
                matchingSequencesCount = 0;
                colouredSequenceLength = lastSequenceIndex - firstSequenceIndex  + 1;
                //check how many sequences matching conditions for mark
                for (int seqNo = 0; seqNo < columnSequences.size(); seqNo++) {
                    columnSequenceRange = columnSequencesRanges.get(seqNo);

                    if ( rangeInsideAnotherRange(colouredSequenceIndexes, columnSequenceRange)
                            && colouredSequenceLength <= columnSequences.get(seqNo)) {
                        matchingSequencesCount++;
                        lastMatchingSequenceIndex = seqNo;
                    }
                }

                //NOTE!!! if matching count == 0 and there are sequences that are not fulfilled -> wrong solution
                if (matchingSequencesCount == 1) {
                    matchingSequenceLength = columnSequences.get(lastMatchingSequenceIndex);
                    sequenceEqualsColumnSequenceLength = colouredSequenceLength == matchingSequenceLength;

                    if (sequenceEqualsColumnSequenceLength) {
                        nonogramLogicObject.getNonogramColumnLogic().updateColumnSequenceRange(columnIdx, lastMatchingSequenceIndex, colouredSequenceIndexes);
                        nonogramLogicObject.copyLogicFromNonogramColumnLogic();
                    } else {
                        oldRangeBeginIndex = nonogramLogicObject.getColumnsSequencesRanges().get(columnIdx).get(lastMatchingSequenceIndex).get(0);
                        oldRangeEndIndex = nonogramLogicObject.getColumnsSequencesRanges().get(columnIdx).get(lastMatchingSequenceIndex).get(1);

                        updatedRangeBeginIndex = Math.max(0,
                                nonogramLogicObject.getNonogramColumnLogic().minimumRowIndexWithoutX(columnIdx, lastSequenceIndex, matchingSequenceLength));

                        updatedRangeEndIndex = Math.min(nonogramLogicObject.getHeight() - 1,
                                nonogramLogicObject.getNonogramColumnLogic().maximumRowIndexWithoutX(columnIdx, firstSequenceIndex, matchingSequenceLength));

                        columnSequenceRange = Arrays.asList(Math.max(oldRangeBeginIndex, updatedRangeBeginIndex), Math.min(oldRangeEndIndex, updatedRangeEndIndex));

                        nonogramLogicObject.getNonogramColumnLogic().updateColumnSequenceRange(columnIdx, lastMatchingSequenceIndex, columnSequenceRange);
                        nonogramLogicObject.copyLogicFromNonogramColumnLogic();
                    }

                    //mark sequence part
                    sequenceMarker = NonogramLogic.indexToSequenceCharMark(lastMatchingSequenceIndex);
                    for (int sequenceRowIdx = firstSequenceIndex; sequenceRowIdx <= lastSequenceIndex; sequenceRowIdx++) {
                        if (nonogramLogicObject.getNonogramSolutionBoardWithMarks().get(sequenceRowIdx).get(columnIdx).substring(2).equals(EMPTY_PART_MARKED_BOARD)) {
                            nonogramLogicObject.getNonogramColumnLogic().markColumnBoardField(sequenceRowIdx, columnIdx, sequenceMarker);
                            nonogramLogicObject.copyLogicFromNonogramColumnLogic();
                            nonogramLogicObject.getNonogramState().increaseMadeSteps();
                        } else if (this.showRepetitions) {
                            System.out.println("Column field was marked before.");
                        }
                    }
                }
            }
        }

        return nonogramLogicObject;
    }

    // iterations through all rows
    public NonogramLogic placeXsAroundLongestSequencesInRowsRange(NonogramLogic nonogramLogicObject, int rowBegin, int rowEnd) {

        NonogramLogic nonogramLogicDataToChange = nonogramLogicObject;

        for (int rowIdx = rowBegin; rowIdx < rowEnd; rowIdx++) {
            nonogramLogicDataToChange = placeXsAroundLongestSequencesInRow(nonogramLogicDataToChange, rowIdx);
        }

        return nonogramLogicDataToChange;
    }

    public NonogramLogic placeXsAroundLongestSequencesInRow(NonogramLogic nonogramLogicObject, int rowIdx) {

        NonogramRowLogic nonogramRowLogicDataToChange = nonogramLogicObject.getNonogramRowLogic();

        List<List<Integer>> rowSequencesRanges = nonogramRowLogicDataToChange.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths = nonogramRowLogicDataToChange.getRowsSequences().get(rowIdx);

        int width = nonogramRowLogicDataToChange.getWidth();

        List<Integer> colouredSequenceRange;
        List<Integer> rowSequenceRange;
        int sequenceOnBoardLength;

        List<Integer> rowSequencesIndexesIncludingSequenceRange;
        List<Integer> rowSequencesLengthsIncludingSequenceRange;

        int firstXColumnIndex;
        int lastXColumnIndex;
        Field firstXFieldToExclude;
        Field colouredFieldInSequence;
        Field lastXFieldToExclude;

        int rowSequenceIdxNotToInclude;

        for (int columnIdx = 0; columnIdx < width; columnIdx++) {
            Field potentialColouredField = new Field(rowIdx, columnIdx);
            if (isFieldColoured(nonogramRowLogicDataToChange, potentialColouredField)) {

                colouredSequenceRange = new ArrayList<>(List.of(columnIdx));

                while(columnIdx < width && isFieldColoured(nonogramRowLogicDataToChange, potentialColouredField)) {
                    columnIdx++;
                    potentialColouredField = new Field(rowIdx, columnIdx);
                }

                //when solutionBoard[rowIdx][columnIdx] != COLOURED_FIELD_MARKED_BOARD
                colouredSequenceRange.add(columnIdx - 1);
                sequenceOnBoardLength = rangeLength(colouredSequenceRange);

                rowSequencesIndexesIncludingSequenceRange = new ArrayList<>();
                rowSequencesLengthsIncludingSequenceRange = new ArrayList<>();

                for (int seqNo = 0; seqNo < rowSequencesRanges.size(); seqNo++) {
                    rowSequenceRange = rowSequencesRanges.get(seqNo);
                    if (rangeInsideAnotherRange(colouredSequenceRange, rowSequenceRange)) {
                        rowSequencesIndexesIncludingSequenceRange.add(seqNo);
                        rowSequencesLengthsIncludingSequenceRange.add(rowSequencesLengths.get(seqNo));
                    }
                }

                firstXColumnIndex = colouredSequenceRange.get(0) - 1;
                lastXColumnIndex = colouredSequenceRange.get(1) + 1;

                if (rowSequencesIndexesIncludingSequenceRange.size() == 1) {

                    rowSequenceIdxNotToInclude = rowSequencesIndexesIncludingSequenceRange.get(0);

                    if (!nonogramRowLogicDataToChange.getRowsSequencesIdsNotToInclude().get(rowIdx).contains(rowSequenceIdxNotToInclude)) {

                        if (sequenceOnBoardLength == rowSequencesLengthsIncludingSequenceRange.get(0)) {
                            firstXFieldToExclude = new Field(rowIdx, firstXColumnIndex);
                            if (firstXColumnIndex >= 0) {
                                if (isFieldEmpty(nonogramRowLogicDataToChange, firstXFieldToExclude)) {
                                    nonogramRowLogicDataToChange.placeXAtGivenField(firstXFieldToExclude);
                                    nonogramRowLogicDataToChange.excludeFieldInRow(firstXFieldToExclude);
                                    nonogramRowLogicDataToChange.excludeFieldInColumn(firstXFieldToExclude);

                                    nonogramRowLogicDataToChange.getNonogramState().increaseMadeSteps();

                                    nonogramRowLogicDataToChange.addRowAndColumnToAffectedByIdentifiers(firstXFieldToExclude, NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES);
                                } else if (this.showRepetitions) {
                                    System.out.println("Longest sequence in row firstXColumnIndex added earlier!");
                                }
                            }

                            if ((firstXColumnIndex >= 0 && isFieldEmpty(nonogramRowLogicDataToChange, firstXFieldToExclude))
                             || (lastXColumnIndex < width && nonogramRowLogicDataToChange.getNonogramSolutionBoard().get(rowIdx).get(lastXColumnIndex).equals(EMPTY_FIELD)) ) {
                                List<Field> rowFieldsToExclude = List.of(new Field(rowIdx, firstXColumnIndex), new Field(rowIdx, lastXColumnIndex));
                                List<Field> columnFieldsToExclude = List.of(new Field(firstXColumnIndex, rowIdx), new Field(lastXColumnIndex, rowIdx));
                                nonogramLogicObject.getNonogramState().increaseMadeSteps();

                                nonogramRowLogicDataToChange.placeXAtGivenFields(rowFieldsToExclude);
                                nonogramRowLogicDataToChange.excludeSequenceInRow(rowIdx,
                                        rowSequencesIndexesIncludingSequenceRange.get(0));
                                nonogramRowLogicDataToChange.excludeFieldsInRow(rowFieldsToExclude);
                                nonogramRowLogicDataToChange.excludeFieldsInColumn(columnFieldsToExclude);
                                nonogramRowLogicDataToChange.updateRowSequenceRange(rowIdx,
                                        rowSequencesIndexesIncludingSequenceRange.get(0), colouredSequenceRange);

                                nonogramLogicObject.copyLogicFromNonogramRowLogic();
                                    for (int sequenceColumnIdx = firstXColumnIndex + 1; sequenceColumnIdx < lastXColumnIndex; sequenceColumnIdx++) {
                                        colouredFieldInSequence = new Field(rowIdx, sequenceColumnIdx);
                                        nonogramLogicObject = nonogramLogicObject
                                                .addRowFieldToExcluded(colouredFieldInSequence);
                                    }
                            } else if (this.showRepetitions) {
                                System.out.println("Placed Xs around longest sequence in row before!");
                            }
                        }

                    }
                } else if (!rowSequencesLengthsIncludingSequenceRange.isEmpty()) {

                    //check if length of sequence == Max(foundSequences_lengths)
                    if (sequenceOnBoardLength == Collections.max(rowSequencesLengthsIncludingSequenceRange)) {
                        firstXFieldToExclude = new Field(rowIdx, firstXColumnIndex);
                        if (isFieldEmpty(nonogramRowLogicDataToChange, firstXFieldToExclude)) {
                            nonogramRowLogicDataToChange.placeXAtGivenField(firstXFieldToExclude);
                            nonogramRowLogicDataToChange.excludeFieldInRow(firstXFieldToExclude);
                            nonogramRowLogicDataToChange.excludeFieldInColumn(firstXFieldToExclude);

                            nonogramLogicObject.getNonogramState().increaseMadeSteps();
                        } else if (this.showRepetitions) {
                            System.out.println("Longest sequence in row firstXIndex added before!");
                        }

                        lastXFieldToExclude = new Field(rowIdx, lastXColumnIndex);
                        if (isFieldEmpty(nonogramRowLogicDataToChange, lastXFieldToExclude)) {
                            nonogramRowLogicDataToChange.placeXAtGivenField(lastXFieldToExclude);
                            nonogramRowLogicDataToChange.excludeFieldInRow(lastXFieldToExclude);
                            nonogramRowLogicDataToChange.excludeFieldInColumn(lastXFieldToExclude);
                            nonogramLogicObject.copyLogicFromNonogramRowLogic();
                            nonogramLogicObject.getNonogramState().increaseMadeSteps();
                        } else if (this.showRepetitions) {
                            System.out.println("Longest sequence in row lastXIndex added before!");
                        }
                    }
                }
            }
        }

        return nonogramLogicObject;
    }

    private boolean isFieldColoured(NonogramRowLogic nonogramRowLogic, Field field) {
        return nonogramRowLogic.getNonogramSolutionBoard().get(field.getRowIdx()).get(field.getColumnIdx()).equals(COLOURED_FIELD);
    }

    private boolean isFieldEmpty(NonogramRowLogic nonogramRowLogic, Field field) {
        return nonogramRowLogic.getNonogramSolutionBoard().get(field.getRowIdx()).get(field.getColumnIdx()).equals(EMPTY_FIELD);
    }

    // iterations through all columns
    public NonogramLogic placeXsAroundLongestSequencesInColumnsRange(NonogramLogic nonogramLogicObject, int columnBegin, int columnEnd) {

        NonogramLogic nonogramLogicDataToChange = nonogramLogicObject;

        for (int columnIdx = columnBegin; columnIdx < columnEnd; columnIdx++) {
            nonogramLogicDataToChange = placeXsAroundLongestSequencesInColumn(nonogramLogicDataToChange, columnIdx);
        }

        return nonogramLogicDataToChange;
    }

    public NonogramLogic placeXsAroundLongestSequencesInColumn(NonogramLogic nonogramLogicObject, int columnIdx) {

        NonogramLogic nonogramLogicDataToChange = nonogramLogicObject;

        List<List<Integer>> columnSequencesRanges = nonogramLogicDataToChange.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequencesLengths = nonogramLogicDataToChange.getColumnsSequences().get(columnIdx);

        int height = nonogramLogicDataToChange.getHeight();

        List<Integer> colouredSequenceRange;
        List<Integer> columnSequenceRange;
        int sequenceLength;

        List<Integer> columnSequencesIndexesIncludingSequenceRange;
        List<Integer> columnSequencesLengthsIncludingSequenceRange;

        int firstXIndex;
        int lastXIndex;
        Field fieldToExclude;

        int columnSequenceIdxNotToInclude;

        for (int rowIdx = 0; rowIdx < height; rowIdx++) {

            if (nonogramLogicDataToChange.getNonogramSolutionBoard().get(rowIdx).get(columnIdx).equals(COLOURED_FIELD)) {

                colouredSequenceRange = new ArrayList<>();
                colouredSequenceRange.add(rowIdx);

                while(rowIdx < height && nonogramLogicDataToChange.getNonogramSolutionBoard().get(rowIdx).get(columnIdx).equals(COLOURED_FIELD)) {
                    rowIdx++;
                }

                //when solutionBoard[rowIdx][columnIdx] != COLOURED_FIELD_MARKED_BOARD
                colouredSequenceRange.add(rowIdx - 1);
                sequenceLength = rangeLength(colouredSequenceRange);
                firstXIndex = colouredSequenceRange.get(0) - 1;
                lastXIndex = colouredSequenceRange.get(1) + 1;

                columnSequencesIndexesIncludingSequenceRange = new ArrayList<>();
                columnSequencesLengthsIncludingSequenceRange = new ArrayList<>();

                for (int seqNo = 0; seqNo < columnSequencesRanges.size(); seqNo++) {
                    columnSequenceRange = columnSequencesRanges.get(seqNo);
                    if (rangeInsideAnotherRange(colouredSequenceRange, columnSequenceRange)) {
                        columnSequencesIndexesIncludingSequenceRange.add(seqNo);
                        columnSequencesLengthsIncludingSequenceRange.add(columnSequencesLengths.get(seqNo));
                    }
                }

                if (columnSequencesIndexesIncludingSequenceRange.size() == 1 ) {

                    columnSequenceIdxNotToInclude = columnSequencesIndexesIncludingSequenceRange.get(0);

                    if (sequenceLength == columnSequencesLengthsIncludingSequenceRange.get(0)) {

                        if (!nonogramLogicDataToChange.getColumnsSequencesIdsNotToInclude().get(columnIdx).contains(columnSequenceIdxNotToInclude)) {
                            nonogramLogicDataToChange.getNonogramColumnLogic().excludeSequenceInColumn(columnIdx, columnSequenceIdxNotToInclude);
                            nonogramLogicDataToChange.copyLogicToNonogramColumnLogic();

                            if (firstXIndex >= 0) {
                                if (nonogramLogicDataToChange.getNonogramSolutionBoard().get(firstXIndex).get(columnIdx).equals(EMPTY_FIELD)) {
                                    fieldToExclude = new Field(firstXIndex, columnIdx);
                                    nonogramLogicDataToChange = nonogramLogicDataToChange
                                            .placeXAtGivenPosition(fieldToExclude)
                                            .addRowFieldToExcluded(fieldToExclude)
                                            .addColumnFieldToExcluded(fieldToExclude);
                                    nonogramLogicObject.getNonogramState().increaseMadeSteps();
                                } else if (this.showRepetitions) {
                                    System.out.println("Longest sequence in column firstXIndex added before!");
                                }
                            }

                            if (lastXIndex < height) {
                                if ( nonogramLogicDataToChange.getNonogramSolutionBoard().get(lastXIndex).get(columnIdx).equals(EMPTY_FIELD)) {
                                    fieldToExclude = new Field(lastXIndex, columnIdx);
                                    nonogramLogicDataToChange = nonogramLogicDataToChange
                                            .placeXAtGivenPosition(fieldToExclude)
                                            .addRowFieldToExcluded(fieldToExclude)
                                            .addColumnFieldToExcluded(fieldToExclude);
                                    nonogramLogicObject.getNonogramState().increaseMadeSteps();
                                } else if (this.showRepetitions) {
                                    System.out.println("Longest sequence in column lastXIndex added before!");
                                }
                            }

                            for (int sequenceRowIdx = firstXIndex + 1; sequenceRowIdx < lastXIndex; sequenceRowIdx++) {
                                if (!nonogramLogicDataToChange.getColumnsFieldsNotToInclude().get(columnIdx).contains(sequenceRowIdx)) {
                                    fieldToExclude = new Field(sequenceRowIdx, columnIdx);
                                    nonogramLogicDataToChange = nonogramLogicDataToChange.addColumnFieldToExcluded(fieldToExclude);
                                    nonogramLogicObject.getNonogramState().increaseMadeSteps();
                                } else if (this.showRepetitions) {
                                    System.out.println("Field not to include in column has been inserted before");
                                }
                            }
                        }

                    }
                } else if (!columnSequencesIndexesIncludingSequenceRange.isEmpty()) {
                    //check if length of sequence == Max(foundSequences_lengths)
                    if (sequenceLength == Collections.max(columnSequencesLengthsIncludingSequenceRange)) {
                        if (nonogramLogicDataToChange.getNonogramSolutionBoard().get(firstXIndex).get(columnIdx).equals(EMPTY_FIELD)) {
                            fieldToExclude = new Field(firstXIndex, columnIdx);
                            nonogramLogicDataToChange = nonogramLogicDataToChange
                                    .placeXAtGivenPosition(fieldToExclude)
                                    .addColumnFieldToExcluded(fieldToExclude)
                                    .addRowFieldToExcluded(fieldToExclude);
                            nonogramLogicObject.getNonogramState().increaseMadeSteps();
                        } else if (this.showRepetitions) {
                            System.out.println("Sequence with maximum length in area firstXIndex placed before!");
                        }
                        if (nonogramLogicDataToChange.getNonogramSolutionBoard().get(lastXIndex).get(columnIdx).equals(EMPTY_FIELD)) {
                            fieldToExclude = new Field(lastXIndex, columnIdx);
                            nonogramLogicDataToChange = nonogramLogicDataToChange
                                    .placeXAtGivenPosition(fieldToExclude)
                                    .addColumnFieldToExcluded(fieldToExclude)
                                    .addRowFieldToExcluded(fieldToExclude);
                            nonogramLogicObject.getNonogramState().increaseMadeSteps();
                        } else if (this.showRepetitions) {
                            System.out.println("Sequence with maximum length in area lastXIndex placed before!");
                        }
                    }
                }
            }
        }
        return nonogramLogicDataToChange;
    }

    // iterations through all rows
    public NonogramLogic placeXsAtUnreachableFieldsInRowsRange(NonogramLogic nonogramLogicObject, int rowBegin, int rowEnd) {

        NonogramLogic nonogramLogicDataToChange = nonogramLogicObject;

        for (int rowIdx = rowBegin; rowIdx < rowEnd; rowIdx++) {
            nonogramLogicDataToChange = placeXsAtUnreachableFieldsInRow(nonogramLogicDataToChange, rowIdx);
        }

        return nonogramLogicDataToChange;
    }

    public NonogramLogic placeXsAtUnreachableFieldsInRow(NonogramLogic nonogramLogicObject, int rowIdx) {

        NonogramLogic nonogramLogicDataToChange = nonogramLogicObject;
        List<List<Integer>> rowSequencesRanges = nonogramLogicDataToChange.getRowsSequencesRanges().get(rowIdx);
        int width = nonogramLogicDataToChange.getWidth();
        boolean existRangeIncludingColumn;
        List<Integer> fieldAsRange;
        Field fieldToExclude;

        for (int columnIdx = 0; columnIdx < width; columnIdx++) {
            fieldAsRange = Arrays.asList(columnIdx, columnIdx);
            existRangeIncludingColumn = rangesListIncludingAnotherRange(rowSequencesRanges, fieldAsRange);

            if (!existRangeIncludingColumn) {
                if (nonogramLogicDataToChange.getNonogramSolutionBoard().get(rowIdx).get(columnIdx).equals(EMPTY_FIELD)) {
                    fieldToExclude = new Field(rowIdx, columnIdx);
                    nonogramLogicDataToChange = nonogramLogicDataToChange
                            .placeXAtGivenPosition(fieldToExclude)
                            .addRowFieldToExcluded(fieldToExclude)
                            .addColumnFieldToExcluded(fieldToExclude);
                    nonogramLogicObject.getNonogramState().increaseMadeSteps();
                } else if (this.showRepetitions) {
                    System.out.println("X at unreachable field in row placed before!");
                }
            }
        }

        return nonogramLogicDataToChange;
    }

    // iterations through all columns
    public NonogramLogic placeXsAtUnreachableFieldsInColumnsRange(NonogramLogic nonogramLogicObject, int columnBegin, int columnEnd) {

        NonogramLogic nonogramLogicDataToChange = nonogramLogicObject;

        for (int columnIdx = columnBegin; columnIdx < columnEnd; columnIdx++) {
            nonogramLogicDataToChange = placeXsAtUnreachableFieldsInColumn(nonogramLogicDataToChange, columnIdx);
        }

        return nonogramLogicDataToChange;
    }

    public NonogramLogic placeXsAtUnreachableFieldsInColumn(NonogramLogic nonogramLogicObject, int columnIdx) {

        NonogramLogic nonogramLogicDataToChange = nonogramLogicObject;
        List<List<Integer>> columnSequencesRanges = nonogramLogicDataToChange.getColumnsSequencesRanges().get(columnIdx);
        int height = nonogramLogicDataToChange.getHeight();
        boolean existRangeIncludingRow;
        List<Integer> fieldAsRange;
        Field fieldToExclude;

        for (int rowIdx = 0; rowIdx < height; rowIdx++) {
            fieldAsRange = List.of(rowIdx, rowIdx);
            existRangeIncludingRow = rangesListIncludingAnotherRange(columnSequencesRanges, fieldAsRange);

            if (!existRangeIncludingRow) {
                if (nonogramLogicDataToChange.getNonogramSolutionBoard().get(rowIdx).get(columnIdx).equals(EMPTY_FIELD)) {
                    fieldToExclude = new Field(rowIdx, columnIdx);
                    nonogramLogicDataToChange = nonogramLogicDataToChange
                            .placeXAtGivenPosition(fieldToExclude)
                            .addRowFieldToExcluded(fieldToExclude)
                            .addColumnFieldToExcluded(fieldToExclude);
                    nonogramLogicObject.getNonogramState().increaseMadeSteps();
                } else if (this.showRepetitions) {
                    System.out.println("X at unreachable field in column placed before!");
                }
            }
        }

        return nonogramLogicDataToChange;
    }

    // iterations through all rows
    public NonogramLogic correctRowsSequencesRanges (NonogramLogic nonogramLogicObject, int rowBegin, int rowEnd) {
        NonogramLogic nonogramLogicChanged = nonogramLogicObject;

        for (int rowIdx = rowBegin; rowIdx < rowEnd; rowIdx++) {
            nonogramLogicChanged = correctRowSequencesRanges(nonogramLogicChanged, rowIdx);
            nonogramLogicChanged = correctRowSequencesWhenMetColouredFieldFromLeft(nonogramLogicChanged, rowIdx);
            nonogramLogicChanged = correctRowSequencesWhenMetColouredFieldFromRight(nonogramLogicChanged, rowIdx);
            nonogramLogicChanged = changeRowRangeIndexesIfXOnWay(nonogramLogicChanged, rowIdx);
        }

        return  nonogramLogicChanged;
    }

    public NonogramLogic correctRowSequencesRanges (NonogramLogic nonogramLogicObject, int rowIdx) {
        List<Integer> rowSequences = nonogramLogicObject.getRowsSequences().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = nonogramLogicObject.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowFieldsNotToInclude = nonogramLogicObject.getRowsFieldsNotToInclude().get(rowIdx);

        List<Integer> fullSequenceRange;
        List<Integer> oldNextSequenceRange;
        int updatedNextSequenceBeginRangeColumnIndex;
        int oldNextSequenceBeginRangeColumnIndex;
        int oldNextSequenceEndRangeColumnIndex;
        List<Integer> updatedNextSequenceRange;

        List<Integer> rowSequencesIdsNotToInclude = nonogramLogicObject.getRowsSequencesIdsNotToInclude().get(rowIdx);

        //for first sequence in row
        if (!rowSequencesIdsNotToInclude.contains(0)) {
            int fieldIdx = 0;

            while(rowFieldsNotToInclude.contains(fieldIdx)) {
                fieldIdx++;
            }

            List<Integer> oldFirstSequenceRange = rowSequencesRanges.get(0);
            List<Integer> updatedFirstSequenceRange = new ArrayList<>(Arrays.asList(fieldIdx, oldFirstSequenceRange.get(1)));

            nonogramLogicObject.getRowsSequencesRanges().get(rowIdx).set(0, updatedFirstSequenceRange);
        }

        //from left/start
        for (int sequenceIdx = 0; sequenceIdx < rowSequencesRanges.size() - 1; sequenceIdx++) {

            if (rowSequencesIdsNotToInclude.contains(sequenceIdx) && !rowSequencesIdsNotToInclude.contains(sequenceIdx + 1)) {

                fullSequenceRange = rowSequencesRanges.get(sequenceIdx);

                oldNextSequenceRange = rowSequencesRanges.get(sequenceIdx + 1);
                updatedNextSequenceBeginRangeColumnIndex = fullSequenceRange.get(0) + 2;

                while(rowFieldsNotToInclude.contains(updatedNextSequenceBeginRangeColumnIndex)) {
                    updatedNextSequenceBeginRangeColumnIndex++;
                }

                oldNextSequenceBeginRangeColumnIndex = oldNextSequenceRange.get(0);
                oldNextSequenceEndRangeColumnIndex = oldNextSequenceRange.get(1);

                //experimental
                while(rowFieldsNotToInclude.contains(oldNextSequenceEndRangeColumnIndex)) {
                    oldNextSequenceEndRangeColumnIndex--;
                }

                updatedNextSequenceRange = Arrays.asList(Math.max(oldNextSequenceBeginRangeColumnIndex, updatedNextSequenceBeginRangeColumnIndex),
                        oldNextSequenceEndRangeColumnIndex);

                nonogramLogicObject.getRowsSequencesRanges().get(rowIdx).set(sequenceIdx + 1, updatedNextSequenceRange);
            }

            //second range update

            //rangeBegin
            int oldNextSequenceRangeBegin = rowSequencesRanges.get(sequenceIdx + 1).get(0);
            int currentSequenceRangeBegin = rowSequencesRanges.get(sequenceIdx).get(0);
            int currentSequenceLengthPlusX = (rowSequences.get(sequenceIdx) + 1);
            updatedNextSequenceRange = Arrays.asList(Math.max( oldNextSequenceRangeBegin, currentSequenceRangeBegin + currentSequenceLengthPlusX),
                    rowSequencesRanges.get(sequenceIdx + 1).get(1));

            nonogramLogicObject.getRowsSequencesRanges().get(rowIdx).set(sequenceIdx + 1, updatedNextSequenceRange);
        }

        List<Integer> oldPreviousSequenceRange;
        int oldPreviousSequenceBeginRangeColumnIndex;
        int oldPreviousSequenceEndRangeColumnIndex;
        int updatedPreviousSequenceEndRangeColumnIndex;
        List<Integer> updatedPreviousSequenceRange;

        //from right/end
        for (int sequenceIdx = rowSequencesRanges.size() - 1; sequenceIdx > 0; sequenceIdx--) {

            if (rowSequencesIdsNotToInclude.contains(sequenceIdx) && !rowSequencesIdsNotToInclude.contains(sequenceIdx - 1)) {

                fullSequenceRange = rowSequencesRanges.get(sequenceIdx);
                oldPreviousSequenceRange = rowSequencesRanges.get(sequenceIdx - 1);
                updatedPreviousSequenceEndRangeColumnIndex = fullSequenceRange.get(0) - 2;

                while(rowFieldsNotToInclude.contains(updatedPreviousSequenceEndRangeColumnIndex)) {
                    updatedPreviousSequenceEndRangeColumnIndex--;
                }

                oldPreviousSequenceBeginRangeColumnIndex = oldPreviousSequenceRange.get(0);
                oldPreviousSequenceEndRangeColumnIndex = oldPreviousSequenceRange.get(1);

                //experimental
                while(rowFieldsNotToInclude.contains(oldPreviousSequenceBeginRangeColumnIndex)) {
                    oldPreviousSequenceBeginRangeColumnIndex++;
                }

                int newEndIndex = Math.min(oldPreviousSequenceEndRangeColumnIndex, updatedPreviousSequenceEndRangeColumnIndex);
                updatedPreviousSequenceRange = Arrays.asList(oldPreviousSequenceBeginRangeColumnIndex, newEndIndex);

                nonogramLogicObject.getRowsSequencesRanges().get(rowIdx).set(sequenceIdx - 1, updatedPreviousSequenceRange);
            }

            int oldPreviousSequenceRangeEnd = rowSequencesRanges.get(sequenceIdx - 1 ).get(1);
            int currentSequenceRangeEnd = rowSequencesRanges.get(sequenceIdx).get(1);
            int currentSequenceLengthPlusX = (rowSequences.get(sequenceIdx) + 1);

            updatedPreviousSequenceRange = Arrays.asList(rowSequencesRanges.get(sequenceIdx - 1).get(0),
                    Math.min( oldPreviousSequenceRangeEnd, currentSequenceRangeEnd - currentSequenceLengthPlusX));

            nonogramLogicObject.getRowsSequencesRanges().get(rowIdx).set(sequenceIdx - 1, updatedPreviousSequenceRange);
        }

        //for last sequence in row
        int width = nonogramLogicObject.getWidth();
        int lastRowSequenceIndex = rowSequencesRanges.size() - 1;

        if (!rowSequencesIdsNotToInclude.contains(lastRowSequenceIndex)) {
            int fieldIdx = width - 1;

            while(rowFieldsNotToInclude.contains(fieldIdx)) {
                fieldIdx--;
            }

            List<Integer> oldLastSequenceRange = rowSequencesRanges.get(lastRowSequenceIndex);
            List<Integer> updatedLastSequenceRange = Arrays.asList(oldLastSequenceRange.get(0), fieldIdx);

            nonogramLogicObject.getRowsSequencesRanges().get(rowIdx).set(lastRowSequenceIndex, updatedLastSequenceRange);
        }

        return nonogramLogicObject;
    }

    public NonogramLogic correctRowSequencesWhenMetColouredFieldFromLeft (NonogramLogic nonogramLogicObject, int rowIdx) {
        List<List<Integer>> rowSequencesRanges = nonogramLogicObject.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths = nonogramLogicObject.getRowsSequences().get(rowIdx);
        int rowSequenceRangeStart;
        int rowSequenceRangeEnd;
        int maximumPossibleSequenceRangeEnd;
        List<Integer> updatedRange;
        List<String> solutionBoardRow = nonogramLogicObject.getNonogramSolutionBoard().get(rowIdx);
        int width = nonogramLogicObject.getWidth();
        int sequenceId = 0;
        int sequenceLength = rowSequencesLengths.get(0);

        for (int columnIdx = 0; columnIdx < width; columnIdx++) {
            if (solutionBoardRow.get(columnIdx).equals(COLOURED_FIELD_MARKED_BOARD)) {
                rowSequenceRangeStart = rowSequencesRanges.get(sequenceId).get(0);
                rowSequenceRangeEnd = rowSequencesRanges.get(sequenceId).get(1);
                maximumPossibleSequenceRangeEnd = columnIdx + sequenceLength - 1;

                updatedRange = Arrays.asList(rowSequenceRangeStart, Math.min(rowSequenceRangeEnd, maximumPossibleSequenceRangeEnd));
                nonogramLogicObject.getRowsSequencesRanges().get(rowIdx).set(sequenceId, updatedRange);

                columnIdx = columnIdx + sequenceLength;
                sequenceId++;
                if (sequenceId < rowSequencesLengths.size()) {
                    sequenceLength = rowSequencesLengths.get(sequenceId);
                } else {
                    break;
                }
            }
        }

        return nonogramLogicObject;
    }

    public NonogramLogic correctRowSequencesWhenMetColouredFieldFromRight (NonogramLogic nonogramLogicObject, int rowIdx) {
        List<List<Integer>> rowSequencesRanges = nonogramLogicObject.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths = nonogramLogicObject.getRowsSequences().get(rowIdx);
        int rowSequenceRangeStart;
        int rowSequenceRangeEnd;
        int minimumPossibleSequenceRangeStart;
        List<Integer> updatedRange;
        List<String> solutionBoardRow = nonogramLogicObject.getNonogramSolutionBoard().get(rowIdx);
        int width = nonogramLogicObject.getWidth();
        int sequenceId = rowSequencesLengths.size() - 1;
        int sequenceLength = rowSequencesLengths.get(sequenceId);

        for (int columnIdx = width - 1; columnIdx >= 0; columnIdx--) {
            if (solutionBoardRow.get(columnIdx).equals(COLOURED_FIELD_MARKED_BOARD)) {
                minimumPossibleSequenceRangeStart = columnIdx - sequenceLength + 1;
                rowSequenceRangeStart = rowSequencesRanges.get(sequenceId).get(0);
                rowSequenceRangeEnd = rowSequencesRanges.get(sequenceId).get(1);

                updatedRange = Arrays.asList(Math.max(minimumPossibleSequenceRangeStart, rowSequenceRangeStart), rowSequenceRangeEnd);
                nonogramLogicObject.getRowsSequencesRanges().get(rowIdx).set(sequenceId, updatedRange);

                columnIdx = columnIdx - sequenceLength;
                sequenceId--;
                if (sequenceId >= 0) {
                    sequenceLength = rowSequencesLengths.get(sequenceId);
                } else {
                    break;
                }
            }
        }

        return nonogramLogicObject;
    }

    public NonogramLogic changeRowRangeIndexesIfXOnWay (NonogramLogic nonogramLogicObject, int rowIdx) {
        List<Integer> rowSequences = nonogramLogicObject.getRowsSequences().get(rowIdx);
        List<List<Integer>> nonogramRowSequencesRanges = nonogramLogicObject.getRowsSequencesRanges().get(rowIdx);
        List<Integer> nonogramRowSequencesIdsNotToInclude = nonogramLogicObject.getRowsSequencesIdsNotToInclude().get(rowIdx);
        List<List<String>> nonogramBoard = nonogramLogicObject.getNonogramSolutionBoard();
        int rowSequenceRangeStartIndex;
        int rowSequenceRangeEndIndex;
        int rowSequenceLength;
        List<Integer> rowSequenceRange;

        boolean indexOk;
        int updatedRowRangeStartIndex;
        int updatedRowRangeEndIndex;
        List<Integer> updatedRowRange;

        for (int seqNo = 0; seqNo < nonogramRowSequencesRanges.size(); seqNo++) {
            if (!nonogramRowSequencesIdsNotToInclude.contains(seqNo)) {

                rowSequenceRange = nonogramRowSequencesRanges.get(seqNo);
                rowSequenceLength = rowSequences.get(seqNo);
                rowSequenceRangeStartIndex = rowSequenceRange.get(0);
                rowSequenceRangeEndIndex = rowSequenceRange.get(1);

                updatedRowRangeStartIndex = rowSequenceRangeStartIndex;

                for (int columnStartIndex = rowSequenceRangeStartIndex; columnStartIndex < (rowSequenceRangeEndIndex - rowSequenceLength + 1); columnStartIndex++) {
                    indexOk = true;
                    for (int columnIdx = columnStartIndex; columnIdx < columnStartIndex + rowSequenceLength; columnIdx++) {
                        if (nonogramBoard.get(rowIdx).get(columnIdx).equals("X".repeat(4))) {
                            indexOk = false;
                            break;
                        }
                    }
                    if (indexOk) {
                        break;
                    } else {
                        updatedRowRangeStartIndex++;
                    }
                }

                updatedRowRangeEndIndex = rowSequenceRangeEndIndex;

                for (int columnEndIndex = rowSequenceRangeEndIndex; columnEndIndex > (rowSequenceRangeStartIndex + rowSequenceLength - 1); columnEndIndex--) {
                    indexOk = true;
                    for (int columnIdx = columnEndIndex; columnIdx > columnEndIndex - rowSequenceLength; columnIdx--) {
                        if (nonogramBoard.get(rowIdx).get(columnIdx).equals("X".repeat(4))) {
                            indexOk = false;
                            break;
                        }
                    }
                    if (indexOk) {
                        break;
                    } else {
                        updatedRowRangeEndIndex--;
                    }
                }

                updatedRowRange = Arrays.asList(updatedRowRangeStartIndex, updatedRowRangeEndIndex);

                nonogramLogicObject.getNonogramRowLogic().updateRowSequenceRange(rowIdx, seqNo, updatedRowRange);
                nonogramLogicObject.copyLogicFromNonogramRowLogic();
            }
        }

        return nonogramLogicObject;
    }

    // iterations through all columns
    public NonogramLogic correctColumnsSequencesRanges (NonogramLogic nonogramLogicObject, int columnBegin, int columnEnd) {
        NonogramLogic nonogramLogicChanged = nonogramLogicObject;

        for (int columnIdx = columnBegin; columnIdx < columnEnd; columnIdx++) {
            nonogramLogicChanged = correctColumnSequencesRanges(nonogramLogicChanged, columnIdx);
            nonogramLogicChanged = changeColumnRangeIndexesIfXOnWay(nonogramLogicChanged, columnIdx);
            nonogramLogicChanged = correctColumnSequencesWhenMetColouredField(nonogramLogicChanged, columnIdx);
        }

        return  nonogramLogicChanged;
    }

    public NonogramLogic correctColumnSequencesRanges (NonogramLogic nonogramLogicObject, int columnIdx) {

        List<Integer> columnSequences = nonogramLogicObject.getColumnsSequences().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = nonogramLogicObject.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnFieldsNotToInclude = nonogramLogicObject.getColumnsFieldsNotToInclude().get(columnIdx);

        List<Integer> fullSequenceRange;
        List<Integer> oldNextSequenceRange;
        int updatedNextSequenceBeginRangeRowIndex;
        int oldNextSequenceBeginRangeRowIndex;
        int oldNextSequenceEndRangeRowIndex;

        List<Integer> columnSequencesIdsNotToInclude = nonogramLogicObject.getColumnsSequencesIdsNotToInclude().get(columnIdx);

        List<Integer> columnIndexesNotToInclude = nonogramLogicObject.getColumnsFieldsNotToInclude().get(columnIdx);

        //for first sequence in column
        List<Integer> updatedFirstSequenceRange;
        if (!columnSequencesIdsNotToInclude.contains(0)) {
            int fieldIdx = 0;

            while(columnFieldsNotToInclude.contains(fieldIdx)) {
                fieldIdx++;
            }

            List<Integer> oldFirstSequenceRange = columnSequencesRanges.get(0);
            updatedFirstSequenceRange = Arrays.asList(Math.max(fieldIdx, oldFirstSequenceRange.get(0)), oldFirstSequenceRange.get(1));

            nonogramLogicObject.getColumnsSequencesRanges().get(columnIdx).set(0, updatedFirstSequenceRange);
        }

        //from top - start
        List<Integer> updatedNextSequenceRange;
        for (int sequenceIdx = 0; sequenceIdx < columnSequencesRanges.size() - 1; sequenceIdx++) {

            if (columnSequencesIdsNotToInclude.contains(sequenceIdx) && !columnSequencesIdsNotToInclude.contains(sequenceIdx + 1)) {

                fullSequenceRange = columnSequencesRanges.get(sequenceIdx);

                oldNextSequenceRange = columnSequencesRanges.get(sequenceIdx + 1);
                updatedNextSequenceBeginRangeRowIndex = fullSequenceRange.get(0) + 2;

                while(columnFieldsNotToInclude.contains(updatedNextSequenceBeginRangeRowIndex)) {
                    updatedNextSequenceBeginRangeRowIndex++;
                }

                oldNextSequenceBeginRangeRowIndex = oldNextSequenceRange.get(0);
                oldNextSequenceEndRangeRowIndex = oldNextSequenceRange.get(1);

                //experimental
                while(columnFieldsNotToInclude.contains(oldNextSequenceEndRangeRowIndex)) {
                    oldNextSequenceEndRangeRowIndex--;
                }

                updatedNextSequenceRange = Arrays.asList(Math.max(oldNextSequenceBeginRangeRowIndex, updatedNextSequenceBeginRangeRowIndex),
                        oldNextSequenceEndRangeRowIndex);

                if (!columnIndexesNotToInclude.contains(columnIdx)) {
                    nonogramLogicObject.getColumnsSequencesRanges().get(columnIdx).set(sequenceIdx + 1, updatedNextSequenceRange);
                }
            }

            //rangeBegin
            int oldNextSequenceRangeBegin = columnSequencesRanges.get(sequenceIdx + 1 ).get(0);
            int currentSequenceRangeBegin = columnSequencesRanges.get(sequenceIdx).get(0);
            int currentSequenceLengthPlusX = (columnSequences.get(sequenceIdx) + 1);
            updatedNextSequenceRange = Arrays.asList(
                    Math.max( oldNextSequenceRangeBegin, currentSequenceRangeBegin + currentSequenceLengthPlusX),
                    columnSequencesRanges.get(sequenceIdx + 1).get(1)
            );

            if (!columnIndexesNotToInclude.contains(columnIdx)) {
                nonogramLogicObject.getColumnsSequencesRanges().get(columnIdx).set(sequenceIdx + 1, updatedNextSequenceRange);
            }
        }

        List<Integer> oldPreviousSequenceRange;
        int oldPreviousSequenceBeginRangeRowIndex;
        int oldPreviousSequenceEndRangeRowIndex;
        int updatedPreviousSequenceEndRangeRowIndex;
        List<Integer> updatedPreviousSequenceRange;

        //from bottom - end
        for (int sequenceIdx = columnSequencesRanges.size() - 1; sequenceIdx > 0; sequenceIdx--) {

            if (columnSequencesIdsNotToInclude.contains(sequenceIdx) && !columnSequencesIdsNotToInclude.contains(sequenceIdx - 1)) {

                fullSequenceRange = columnSequencesRanges.get(sequenceIdx);
                oldPreviousSequenceRange = columnSequencesRanges.get(sequenceIdx - 1);
                updatedPreviousSequenceEndRangeRowIndex = fullSequenceRange.get(0) - 2;

                while(columnFieldsNotToInclude.contains(updatedPreviousSequenceEndRangeRowIndex)) {
                    updatedPreviousSequenceEndRangeRowIndex--;
                }

                oldPreviousSequenceBeginRangeRowIndex = oldPreviousSequenceRange.get(0);
                oldPreviousSequenceEndRangeRowIndex = oldPreviousSequenceRange.get(1);

                //experimental
                while(columnFieldsNotToInclude.contains(oldPreviousSequenceBeginRangeRowIndex)) {
                    oldPreviousSequenceBeginRangeRowIndex++;
                }

                updatedPreviousSequenceRange = Arrays.asList(oldPreviousSequenceBeginRangeRowIndex,
                        Math.min(oldPreviousSequenceEndRangeRowIndex, updatedPreviousSequenceEndRangeRowIndex));

                if (!columnIndexesNotToInclude.contains(columnIdx)) {
                    nonogramLogicObject.getColumnsSequencesRanges().get(columnIdx).set(sequenceIdx - 1, updatedPreviousSequenceRange);
                }
            }

            int oldPreviousSequenceRangeEnd = columnSequencesRanges.get(sequenceIdx - 1 ).get(1);
            int currentSequenceRangeEnd = columnSequencesRanges.get(sequenceIdx).get(1);
            int currentSequenceLengthPlusX = (columnSequences.get(sequenceIdx) + 1);
            int possibleLowerPreviousSequenceRangeEnd = currentSequenceRangeEnd - currentSequenceLengthPlusX;

            updatedPreviousSequenceRange = Arrays.asList(columnSequencesRanges.get(sequenceIdx - 1).get(0),
                    Math.min( oldPreviousSequenceRangeEnd, possibleLowerPreviousSequenceRangeEnd));

            if (!columnIndexesNotToInclude.contains(columnIdx)) {
                nonogramLogicObject.getColumnsSequencesRanges().get(columnIdx).set(sequenceIdx - 1, updatedPreviousSequenceRange);
            }
        }

        //for last sequence in column
        int height = nonogramLogicObject.getHeight();
        int lastColumnSequenceIndex = columnSequencesRanges.size() - 1;

        if (!columnSequencesIdsNotToInclude.contains(lastColumnSequenceIndex)) {
            int fieldIdx = height - 1;

            while(columnFieldsNotToInclude.contains(fieldIdx)) {
                fieldIdx--;
            }

            List<Integer> oldLastSequenceRange = columnSequencesRanges.get(lastColumnSequenceIndex);
            List<Integer> updatedLastSequenceRange = Arrays.asList(oldLastSequenceRange.get(0), Math.min(fieldIdx, oldLastSequenceRange.get(1)));

            if (!columnIndexesNotToInclude.contains(columnIdx)) {
                nonogramLogicObject.getColumnsSequencesRanges().get(columnIdx).set(lastColumnSequenceIndex, updatedLastSequenceRange);
            }
        }

        return nonogramLogicObject;
    }

    // iterations through all columns
    public NonogramLogic changeColumnRangeIndexesIfXOnWay (NonogramLogic nonogramLogicObject, int columnIdx) {
        List<Integer> columnSequences = nonogramLogicObject.getColumnsSequences().get(columnIdx);
        List<List<Integer>> nonogramColumnSequencesRanges = nonogramLogicObject.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> nonogramColumnsSequencesIdsNotToInclude = nonogramLogicObject.getColumnsSequencesIdsNotToInclude().get(columnIdx);
        List<List<String>> nonogramBoard = nonogramLogicObject.getNonogramSolutionBoard();
        int columnSequenceRangeStartIndex;
        int columnSequenceRangeEndIndex;
        int columnSequenceLength;
        List<Integer> columnSequenceRange;

        boolean indexOk;
        int updatedColumnSequenceRangeStartIndex;
        int updatedColumnSequenceRangeEndIndex;
        List<Integer> updatedColumnSequenceRange;

        for (int seqNo = 0; seqNo < nonogramColumnSequencesRanges.size(); seqNo++) {
            if (!nonogramColumnsSequencesIdsNotToInclude.contains(seqNo)) {

                columnSequenceRange = nonogramColumnSequencesRanges.get(seqNo);
                columnSequenceLength = columnSequences.get(seqNo);
                columnSequenceRangeStartIndex = columnSequenceRange.get(0);
                columnSequenceRangeEndIndex = columnSequenceRange.get(1);

                updatedColumnSequenceRangeStartIndex = columnSequenceRangeStartIndex;

                for (int rowStartIndex = columnSequenceRangeStartIndex; rowStartIndex < columnSequenceRangeEndIndex - columnSequenceLength + 1; rowStartIndex++) {
                    indexOk = true;
                    for (int rowIdx = rowStartIndex; rowIdx < rowStartIndex + columnSequenceLength; rowIdx++) {
                        if (nonogramBoard.get(rowIdx).get(columnIdx).equals(X_FIELD_MARKED_BOARD)) {
                            indexOk = false;
                            break;
                        }
                    }
                    if (indexOk) {
                        break;
                    } else {
                        updatedColumnSequenceRangeStartIndex++;
                    }
                }

                updatedColumnSequenceRangeEndIndex = columnSequenceRangeEndIndex;

                for (int rowEndIndex = columnSequenceRangeEndIndex; rowEndIndex > columnSequenceRangeStartIndex + columnSequenceLength - 1; rowEndIndex--) {
                    indexOk = true;
                    for (int rowIdx = rowEndIndex; rowIdx > rowEndIndex - columnSequenceLength; rowIdx--) {
                        if (nonogramBoard.get(rowIdx).get(columnIdx).equals(X_FIELD_MARKED_BOARD)) {
                            indexOk = false;
                            break;
                        }
                    }
                    if (indexOk) {
                        break;
                    } else {
                        updatedColumnSequenceRangeEndIndex--;
                    }
                }

                updatedColumnSequenceRange = Arrays.asList(updatedColumnSequenceRangeStartIndex, updatedColumnSequenceRangeEndIndex);

                nonogramLogicObject.getNonogramColumnLogic().updateColumnSequenceRange(columnIdx, seqNo,
                        updatedColumnSequenceRange);
                nonogramLogicObject.copyLogicFromNonogramColumnLogic();
            }
        }

        return nonogramLogicObject;
    }

    public NonogramLogic correctColumnSequencesWhenMetColouredField (NonogramLogic nonogramLogicObject, int columnIdx) {
        List<List<Integer>> columnSequencesRanges = nonogramLogicObject.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequencesLengths = nonogramLogicObject.getColumnsSequences().get(columnIdx);
        List<List<String>> nonogramSolutionBoard = nonogramLogicObject.getNonogramSolutionBoard();
        int columnSequenceRangeStart;
        int columnSequenceRangeEnd;
        int maximumPossibleSequenceRangeEnd;
        List<Integer> updatedRange;
        int height = nonogramLogicObject.getHeight();
        List<String> solutionBoardColumn = new ArrayList<>();

        for (int rowIdx = 0; rowIdx < height; rowIdx++) {
            solutionBoardColumn.add(nonogramSolutionBoard.get(rowIdx).get(columnIdx));
        }

        int sequenceId = 0;
        int sequenceLength = columnSequencesLengths.get(0);

        for (int rowIdx = 0; rowIdx < height; rowIdx++) {
            if (solutionBoardColumn.get(rowIdx).equals(COLOURED_FIELD_MARKED_BOARD)) {
                columnSequenceRangeStart = columnSequencesRanges.get(sequenceId).get(0);
                columnSequenceRangeEnd = columnSequencesRanges.get(sequenceId).get(1);
                maximumPossibleSequenceRangeEnd = rowIdx + sequenceLength - 1;

                updatedRange = Arrays.asList(columnSequenceRangeStart, Math.min(columnSequenceRangeEnd, maximumPossibleSequenceRangeEnd));
                nonogramLogicObject.getColumnsSequencesRanges().get(columnIdx).set(sequenceId, updatedRange);

                rowIdx = rowIdx + sequenceLength;
                sequenceId++;
                if (sequenceId < columnSequencesLengths.size()) {
                    sequenceLength = columnSequencesLengths.get(sequenceId);
                } else {
                    break;
                }
            }
        }

        return nonogramLogicObject;
    }

    public static boolean rangesListIncludingAnotherRange (List<List<Integer>> listOfRanges, List<Integer> range) {
        for (List<Integer> listOfRange : listOfRanges) {
            if (rangeInsideAnotherRange(range, listOfRange)) {
                return true;
            }
        }

        return false;
    }


    public static List<Integer> filterSequencesRangesIncludingAnotherAndReturnCorrespondingLengths(List<List<Integer>> ranges,
                                                                                                         List<Integer> rangeToInclude,
                                                                                                         List<Integer> lengths) {
        List<Integer> filteredLengths = new ArrayList<>();

        for (int rangeNo = 0; rangeNo < ranges.size(); rangeNo++) {
            if (rangeInsideAnotherRange(rangeToInclude, ranges.get(rangeNo)) && lengths.get(rangeNo) >= rangeLength(rangeToInclude)) {
                filteredLengths.add(lengths.get(rangeNo));
            }
        }

        return filteredLengths;
    }

    public NonogramLogic runSolverWithCorrectnessCheck(NonogramLogic nonogramLogicObject, String solutionFileName) {
        log.info("RUN SOLVER WITH CORRECTNESS CHECK");
        NonogramSolver nonogramSolver = new NonogramSolver(nonogramLogicObject, solutionFileName);
        log.info("INITIALIZED nonogramSolver {}!", nonogramSolver);
        NonogramSolutionNode nonogramSolutionNode = new NonogramSolutionNode(nonogramLogicObject);
        log.info("INITIALIZED nonogramSolutionNode (DEC SIZE : {})! GO TO nonogramSolver.runSolutionAtNode()", nonogramSolutionNode.getNonogramGuessDecisions().size());
        return nonogramSolver.runSolutionAtNode(nonogramSolutionNode);
    }
}
