package com.puzzlesolverappbackend.puzzleAppFileManager.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.puzzlesolverappbackend.puzzleAppFileManager.NonogramSolutionDecision;
import com.puzzlesolverappbackend.puzzleAppFileManager.NonogramSolutionNode;
import com.puzzlesolverappbackend.puzzleAppFileManager.NonogramSolver;
import com.puzzlesolverappbackend.puzzleAppFileManager.config.NonogramConfig;
import com.puzzlesolverappbackend.puzzleAppFileManager.model.NonogramResult;
import com.puzzlesolverappbackend.puzzleAppFileManager.payload.NonogramLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.repository.NonogramRepository;
import com.puzzlesolverappbackend.puzzleAppFileManager.runners.InitializerConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Optional;

import static com.puzzlesolverappbackend.puzzleAppFileManager.services.CommonService.justifiedString;

@Service
public class NonogramLogicService {

    FileWriter fileWriter;
    Gson gson;

    boolean showRepetitions = false;

    double completionAfterHeuristics;
    double timeAfterHeuristics;
    double completionAfterTrialAndError;
    double timeAfterTrialAndError;
    double completionAfterRecursion;
    double timeAfterRecursion;
    int nonogramGuessDecisionsSize;

    String source;
    double difficulty;
    int height;
    int width;
    int area;

    int recursionDepth;

    public static String resourcesPath = "../../PuzzleSolver/FrontReact/public/resources/allNonogramsJSON";

    @Autowired
    NonogramRepository nonogramRepository;

    public void initGson(boolean pretty) {
        if(pretty) {
            gson = new GsonBuilder().setPrettyPrinting().create();
        } else {
            gson = new Gson();
        }
    }

    // iterations through all columns
    public NonogramLogic fillOverLappingFieldsInColumnsRange (NonogramLogic nonogramLogicObject, int columnBegin, int columnEnd) {
        NonogramLogic nonogramLogicObjectToChange = nonogramLogicObject;
        int nonogramWidth = nonogramLogicObject.getColumnsSequences().size();
        for(int columnIdx = columnBegin; columnIdx < nonogramWidth && columnIdx < columnEnd; columnIdx++) {
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

        for(int sequenceIdx = 0; sequenceIdx < sequencesInColumn.size(); sequenceIdx++) {
            sequenceLength = sequencesInColumn.get(sequenceIdx);
            rangeBeginIndex = sequencesInColumnRanges.get(sequenceIdx).get(0);
            rangeEndIndex = sequencesInColumnRanges.get(sequenceIdx).get(1);

            colourBeginRowIndex = rangeEndIndex - sequenceLength + 1;
            colourEndRowIndex = rangeBeginIndex + sequenceLength - 1;

            if(colourBeginRowIndex <= colourEndRowIndex) {
                sequenceCharMark = NonogramLogic.indexToSequenceCharMark(sequenceIdx);
                for (int rowIdx = colourBeginRowIndex; rowIdx <= colourEndRowIndex; rowIdx++) {
                    rowToChangeColumnBoardWithMarks = nonogramLogicObject.getNonogramSolutionBoardWithMarks().get(rowIdx);
                    elementToChangeInsideRowBoardWithMarks = rowToChangeColumnBoardWithMarks.get(columnIdx);


                    if(rowToChangeColumnBoardWithMarks.get(columnIdx).substring(2).equals("--")) {
                        nonogramLogicObject.increaseStepsMade();

                        rowToChangeColumnBoardWithMarks.set(columnIdx, elementToChangeInsideRowBoardWithMarks.substring(0, 2) + "C" + sequenceCharMark);
                        nonogramLogicObject.getNonogramSolutionBoardWithMarks().set(rowIdx, rowToChangeColumnBoardWithMarks);
                        nonogramLogicObject.setNewStepsMade(nonogramLogicObject.getNewStepsMade() + 1);

                        rowToChangeColumnBoard = nonogramLogicObject.getNonogramSolutionBoard().get(rowIdx);
                        if(rowToChangeColumnBoard.get(columnIdx).equals("-".repeat(4))) {
                            rowToChangeColumnBoard.set(columnIdx, "O".repeat(4));
                            nonogramLogicObject.getNonogramSolutionBoard().set(rowIdx, rowToChangeColumnBoard);
                        }
                    } else if (showRepetitions) {
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
        for(int rowIdx = rowBegin; rowIdx < nonogramHeight && rowIdx < rowEnd; rowIdx++) {
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

        for(int sequenceIdx = 0; sequenceIdx < sequencesInRow.size(); sequenceIdx++) {
            sequenceLength = sequencesInRow.get(sequenceIdx);
            rangeBeginIndex = sequencesInRowRanges.get(sequenceIdx).get(0);
            rangeEndIndex = sequencesInRowRanges.get(sequenceIdx).get(1);

            colourBeginColumnIndex = rangeEndIndex - sequenceLength + 1;
            colourEndColumnIndex = rangeBeginIndex + sequenceLength - 1;

            if(colourBeginColumnIndex <= colourEndColumnIndex) {

                sequenceCharMark = NonogramLogic.indexToSequenceCharMark(sequenceIdx);
                rowToChangeSolutionBoardWithMarks = nonogramLogicObject.getNonogramSolutionBoardWithMarks().get(rowIdx);

                for (int columnIdx = colourBeginColumnIndex; columnIdx <= colourEndColumnIndex; columnIdx++) {
                    elementToChangeInsideRowBoardWithMarks = rowToChangeSolutionBoardWithMarks.get(columnIdx);

                    if(rowToChangeSolutionBoardWithMarks.get(columnIdx).startsWith("--")) {
                        nonogramLogicObject.increaseStepsMade();
                        rowToChangeSolutionBoardWithMarks.set(columnIdx, "R" + sequenceCharMark + elementToChangeInsideRowBoardWithMarks.substring(2, 4));

                        rowToChangeSolutionBoard = nonogramLogicObject.getNonogramSolutionBoard().get(rowIdx);
                        if(rowToChangeSolutionBoard.get(columnIdx).equals("-".repeat(4))) {
                            rowToChangeSolutionBoard.set(columnIdx, "O".repeat(4));
                        }
                    } else if (showRepetitions) {
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

        for(int rowIdx = rowBegin; rowIdx < rowEnd; rowIdx++) {
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

        for(int columnIdx = 0; columnIdx < boardRow.size(); columnIdx++) {

            if(boardRow.get(columnIdx).equals("O")) {

                colouredSequenceIndexes = new ArrayList<>();
                colouredSequenceIndexes.add(columnIdx);

                //collect indexes of current coloured sequence
                while(columnIdx < boardRow.size() && boardRow.get(columnIdx).equals("O")) {
                    columnIdx++;
                }

                colouredSequenceIndexes.add(columnIdx - 1);

                firstSequenceIndex = colouredSequenceIndexes.get(0);
                lastSequenceIndex = colouredSequenceIndexes.get(colouredSequenceIndexes.size() - 1);
                colouredSequenceLength = lastSequenceIndex - firstSequenceIndex  + 1;

                matchingSequencesCount = 0;

                //check how many sequences matching conditions for mark
                for(int seqNo = 0; seqNo < rowSequences.size(); seqNo++) {

                    rowSequenceRange = rowSequencesRanges.get(seqNo);

                    if( rangeInsideAnotherRange(colouredSequenceIndexes, rowSequenceRange)
                            && colouredSequenceLength <= rowSequences.get(seqNo)) {
                        matchingSequencesCount++;
                        lastMatchingSequenceIndex = seqNo;
                    }
                }

                //NOTE!!! if matching count == 0 and there are sequences that are not fullfiled -> wrong solution
                if(matchingSequencesCount == 1) {

                    matchingSequenceLength = rowSequences.get(lastMatchingSequenceIndex);
                    sequenceEqualsRowSequenceLength = colouredSequenceLength == matchingSequenceLength;
                    rowSequenceRange = new ArrayList<>();

                    if(sequenceEqualsRowSequenceLength) {
                        rowSequenceRange.add(firstSequenceIndex);
                        rowSequenceRange.add(lastSequenceIndex);
                    } else {
                        oldRangeBeginIndex = nonogramLogicObject.getRowsSequencesRanges().get(rowIdx).get(lastMatchingSequenceIndex).get(0);
                        oldRangeEndIndex = nonogramLogicObject.getRowsSequencesRanges().get(rowIdx).get(lastMatchingSequenceIndex).get(1);

                        updatedRangeBeginIndex = Math.max(0,
                                nonogramLogicObject.getNonogramRowLogic().minimumColumnIndexWithoutX(rowIdx, lastSequenceIndex, matchingSequenceLength));
                        updatedRangeEndIndex = Math.min(nonogramLogicObject.getWidth() - 1,
                                nonogramLogicObject.maximumColumnIndexWithoutX(rowIdx, firstSequenceIndex, matchingSequenceLength));
                        rowSequenceRange.add( Math.max(oldRangeBeginIndex ,updatedRangeBeginIndex) );
                        rowSequenceRange.add( Math.min(oldRangeEndIndex, updatedRangeEndIndex) );

                    }
                    nonogramLogicObject.getNonogramRowLogic().updateRowSequenceRange(rowIdx, lastMatchingSequenceIndex, rowSequenceRange);

                    //mark sequence part
                    sequenceMarker = NonogramLogic.indexToSequenceCharMark(lastMatchingSequenceIndex);
                    for(int sequenceColumnIdx = firstSequenceIndex; sequenceColumnIdx <= lastSequenceIndex; sequenceColumnIdx++) {
                        if(nonogramLogicObject.getNonogramSolutionBoardWithMarks().get(rowIdx).get(sequenceColumnIdx).startsWith("--")) {
                            nonogramLogicObject.getNonogramRowLogic().markRowBoardField(rowIdx, sequenceColumnIdx, sequenceMarker);
                            nonogramLogicObject.increaseStepsMade();
                        } else if(showRepetitions) {
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

        for(int columnIdx = columnBegin; columnIdx < columnEnd; columnIdx++) {
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

        for(int rowIdx = 0; rowIdx < nonogramSolutionBoardWithMarks.size(); rowIdx++) {

            if(nonogramSolutionBoard.get(rowIdx).get(columnIdx).equals("O")) {

                colouredSequenceIndexes = new ArrayList<>();

                colouredSequenceIndexes.add(rowIdx);
                //collect indexes of current coloured sequence
                while(rowIdx < nonogramSolutionBoard.size() && nonogramSolutionBoard.get(rowIdx).get(columnIdx).equals("O")) {
                    rowIdx++;
                }

                colouredSequenceIndexes.add(rowIdx - 1);

                firstSequenceIndex = colouredSequenceIndexes.get(0);
                lastSequenceIndex = colouredSequenceIndexes.get(colouredSequenceIndexes.size() - 1);
                matchingSequencesCount = 0;
                colouredSequenceLength = lastSequenceIndex - firstSequenceIndex  + 1;
                //check how many sequences matching conditions for mark
                for(int seqNo = 0; seqNo < columnSequences.size(); seqNo++) {
                    columnSequenceRange = columnSequencesRanges.get(seqNo);

                    if( rangeInsideAnotherRange(colouredSequenceIndexes, columnSequenceRange)
                            && colouredSequenceLength <= columnSequences.get(seqNo)) {
                        matchingSequencesCount++;
                        lastMatchingSequenceIndex = seqNo;
                    }
                }

                //NOTE!!! if matching count == 0 and there are sequences that are not fullfiled -> wrong solution
                if(matchingSequencesCount == 1) {
                    matchingSequenceLength = columnSequences.get(lastMatchingSequenceIndex);
                    sequenceEqualsColumnSequenceLength = colouredSequenceLength == matchingSequenceLength;

                    if(sequenceEqualsColumnSequenceLength) {
                        nonogramLogicObject.getNonogramColumnLogic().updateColumnSequenceRange(columnIdx, lastMatchingSequenceIndex, colouredSequenceIndexes);
                        nonogramLogicObject.copyLogicFromNonogramColumnLogic();
                    } else {
                        oldRangeBeginIndex = nonogramLogicObject.getColumnsSequencesRanges().get(columnIdx).get(lastMatchingSequenceIndex).get(0);
                        oldRangeEndIndex = nonogramLogicObject.getColumnsSequencesRanges().get(columnIdx).get(lastMatchingSequenceIndex).get(1);

                        updatedRangeBeginIndex = Math.max(0,
                                nonogramLogicObject.getNonogramColumnLogic().minimumRowIndexWithoutX(columnIdx, lastSequenceIndex, matchingSequenceLength));

                        updatedRangeEndIndex = Math.min(nonogramLogicObject.getHeight() - 1,
                                nonogramLogicObject.getNonogramColumnLogic().maximumRowIndexWithoutX(columnIdx, firstSequenceIndex, matchingSequenceLength));

                        columnSequenceRange = new ArrayList<>();
                        columnSequenceRange.add( Math.max(oldRangeBeginIndex ,updatedRangeBeginIndex) );
                        columnSequenceRange.add( Math.min(oldRangeEndIndex, updatedRangeEndIndex) );

                        nonogramLogicObject.getNonogramColumnLogic().updateColumnSequenceRange(columnIdx, lastMatchingSequenceIndex, columnSequenceRange);
                        nonogramLogicObject.copyLogicFromNonogramColumnLogic();
                    }

                    //mark sequence part
                    sequenceMarker = NonogramLogic.indexToSequenceCharMark(lastMatchingSequenceIndex);
                    for(int sequenceRowIdx = firstSequenceIndex; sequenceRowIdx <= lastSequenceIndex; sequenceRowIdx++) {
                        if(nonogramLogicObject.getNonogramSolutionBoardWithMarks().get(sequenceRowIdx).get(columnIdx).substring(2).equals("--")) {
                            nonogramLogicObject.getNonogramColumnLogic().markColumnBoardField(sequenceRowIdx, columnIdx, sequenceMarker);
                            nonogramLogicObject.copyLogicFromNonogramColumnLogic();
                            nonogramLogicObject.increaseStepsMade();
                        } else if (showRepetitions) {
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

        for(int rowIdx = rowBegin; rowIdx < rowEnd; rowIdx++) {
            nonogramLogicDataToChange = placeXsAroundLongestSequencesInRow(nonogramLogicDataToChange, rowIdx);
        }

        return nonogramLogicDataToChange;
    }

    public NonogramLogic placeXsAroundLongestSequencesInRow(NonogramLogic nonogramLogicObject, int rowIdx) {

        NonogramLogic nonogramLogicDataToChange = nonogramLogicObject;
        List<String> nonogramSolutionBoardRow = nonogramLogicDataToChange.getNonogramSolutionBoard().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = nonogramLogicDataToChange.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths = nonogramLogicDataToChange.getRowsSequences().get(rowIdx);
        int width = nonogramLogicDataToChange.getWidth();

        List<Integer> colouredSequenceRange;
        List<Integer> rowSequenceRange;
        int sequenceOnBoardLength;

        List<Integer> rowSequencesIndexesIncludingSequenceRange;
        List<Integer> rowSequencesLengthsIncludingSequenceRange;

        int firstXIndex;
        int lastXIndex;

        int rowSequenceIdxNotToInclude;

        for(int columnIdx = 0; columnIdx < width; columnIdx++) {

            if(nonogramSolutionBoardRow.get(columnIdx).equals("O".repeat(4))) {

                colouredSequenceRange = new ArrayList<>();
                colouredSequenceRange.add(columnIdx);

                while(columnIdx < width && nonogramSolutionBoardRow.get(columnIdx).equals("O")) {
                    columnIdx++;
                }

                //when solutionBoard[rowIdx][columnIdx] != "O".repeat(4)
                colouredSequenceRange.add(columnIdx - 1);
                sequenceOnBoardLength = rangeLength(colouredSequenceRange);
                rowSequencesIndexesIncludingSequenceRange = new ArrayList<>();
                rowSequencesLengthsIncludingSequenceRange = new ArrayList<>();

                for(int seqNo = 0; seqNo < rowSequencesRanges.size(); seqNo++) {
                    rowSequenceRange = rowSequencesRanges.get(seqNo);
                    if(rangeInsideAnotherRange(colouredSequenceRange, rowSequenceRange)) {
                        rowSequencesIndexesIncludingSequenceRange.add(seqNo);
                        rowSequencesLengthsIncludingSequenceRange.add(rowSequencesLengths.get(seqNo));
                    }
                }


                firstXIndex = colouredSequenceRange.get(0) - 1;
                lastXIndex = colouredSequenceRange.get(1) + 1;

                if(rowSequencesIndexesIncludingSequenceRange.size() == 1 ) {

                    rowSequenceIdxNotToInclude = rowSequencesIndexesIncludingSequenceRange.get(0);

                    if(!nonogramLogicDataToChange.getRowsSequencesIdsNotToInclude().get(rowIdx).contains(rowSequenceIdxNotToInclude)) {

                        if(sequenceOnBoardLength == rowSequencesLengthsIncludingSequenceRange.get(0)) {
                            if((firstXIndex > -1 && nonogramLogicDataToChange.getNonogramSolutionBoard().get(rowIdx).get(firstXIndex).equals("-"))
                             || (lastXIndex < width && nonogramLogicDataToChange.getNonogramSolutionBoard().get(rowIdx).get(lastXIndex).equals("-")) ) {
                                nonogramLogicObject.increaseStepsMade();
                                    nonogramLogicDataToChange.getNonogramRowLogic()
                                            .placeXAtGivenPosition(rowIdx, firstXIndex)
                                            .placeXAtGivenPosition(rowIdx, lastXIndex)
                                            .addRowSequenceIdxToNotToInclude(rowIdx, rowSequencesIndexesIncludingSequenceRange.get(0))
                                            .addRowFieldToNotToInclude(rowIdx, firstXIndex)
                                            .addRowFieldToNotToInclude(rowIdx, lastXIndex)
                                            .addColumnFieldToNotToInclude(firstXIndex, rowIdx)
                                            .addColumnFieldToNotToInclude(lastXIndex, rowIdx)
                                            .updateRowSequenceRange(rowIdx, rowSequencesIndexesIncludingSequenceRange.get(0),
                                                    colouredSequenceRange);
                                    nonogramLogicDataToChange.copyLogicFromNonogramRowLogic();
                                    for(int sequenceColumnIdx = firstXIndex + 1; sequenceColumnIdx < lastXIndex; sequenceColumnIdx++) {
                                        nonogramLogicDataToChange = nonogramLogicDataToChange
                                                .addRowFieldToNotToInclude(rowIdx, sequenceColumnIdx);
                                    }
                            } else if(showRepetitions) {
                                System.out.println("Placed Xs around longest sequence in row before!");
                            }
                        }

                    }
                } else if(rowSequencesLengthsIncludingSequenceRange.size() > 0) {

                    //check if length of sequence == Max(foundSequences_lengths)
                    if(sequenceOnBoardLength == Collections.max(rowSequencesLengthsIncludingSequenceRange)) {

                        if(nonogramLogicDataToChange.getNonogramSolutionBoard().get(rowIdx).get(firstXIndex).equals("-")) {
                            nonogramLogicObject.increaseStepsMade();
                            nonogramLogicDataToChange = nonogramLogicDataToChange
                                    .placeXAtGivenPosittion(rowIdx, firstXIndex)
                                    .addRowFieldToNotToInclude(rowIdx, firstXIndex)
                                    .addColumnFieldToNotToInclude(firstXIndex, rowIdx);
                        } else if(showRepetitions) {
                            System.out.println("Longest sequence in row firstXIndex added before!");
                        }

                        if(nonogramLogicDataToChange.getNonogramSolutionBoard().get(rowIdx).get(lastXIndex).equals("-")) {
                            nonogramLogicObject.increaseStepsMade();
                            nonogramLogicDataToChange = nonogramLogicDataToChange
                                    .placeXAtGivenPosittion(rowIdx, lastXIndex)
                                    .addRowFieldToNotToInclude(rowIdx, lastXIndex)
                                    .addColumnFieldToNotToInclude(lastXIndex, rowIdx);
                        } else if(showRepetitions) {
                            System.out.println("Longest sequence in row lastXIndex added before!");
                        }
                    }
                }
            }
        }

        return nonogramLogicDataToChange;
    }

    // iterations through all columns
    public NonogramLogic placeXsAroundLongestSequencesInColumnsRange(NonogramLogic nonogramLogicObject, int columnBegin, int columnEnd) {

        NonogramLogic nonogramLogicDataToChange = nonogramLogicObject;

        for(int columnIdx = columnBegin; columnIdx < columnEnd; columnIdx++) {
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

        int columnSequenceIdxNotToInclude;

        for(int rowIdx = 0; rowIdx < height; rowIdx++) {

            if(nonogramLogicDataToChange.getNonogramSolutionBoard().get(rowIdx).get(columnIdx).equals("O")) {

                colouredSequenceRange = new ArrayList<>();
                colouredSequenceRange.add(rowIdx);

                while(rowIdx < height && nonogramLogicDataToChange.getNonogramSolutionBoard().get(rowIdx).get(columnIdx).equals("O")) {
                    rowIdx++;
                }

                //when solutionBoard[rowIdx][columnIdx] != "O".repeat(4)
                colouredSequenceRange.add(rowIdx - 1);
                sequenceLength = rangeLength(colouredSequenceRange);
                columnSequencesIndexesIncludingSequenceRange = new ArrayList<>();
                columnSequencesLengthsIncludingSequenceRange = new ArrayList<>();

                for(int seqNo = 0; seqNo < columnSequencesRanges.size(); seqNo++) {
                    columnSequenceRange = columnSequencesRanges.get(seqNo);
                    if(rangeInsideAnotherRange(colouredSequenceRange, columnSequenceRange)) {
                        columnSequencesIndexesIncludingSequenceRange.add(seqNo);
                        columnSequencesLengthsIncludingSequenceRange.add(columnSequencesLengths.get(seqNo));
                    }
                }

                firstXIndex = colouredSequenceRange.get(0) - 1;
                lastXIndex = colouredSequenceRange.get(1) + 1;

                if(columnSequencesIndexesIncludingSequenceRange.size() == 1 ) {

                    columnSequenceIdxNotToInclude = columnSequencesIndexesIncludingSequenceRange.get(0);

                    if(sequenceLength == columnSequencesLengthsIncludingSequenceRange.get(0)) {

                        if(!nonogramLogicDataToChange.getColumnsSequencesIdsNotToInclude().get(columnIdx).contains(columnSequenceIdxNotToInclude)) {
                            nonogramLogicDataToChange.getNonogramColumnLogic().addColumnSequenceIdxToNotToInclude(columnIdx, columnSequenceIdxNotToInclude);
                            nonogramLogicDataToChange.copyLogicToNonogramColumnLogic();

                            if(firstXIndex > -1) {
                                if (nonogramLogicDataToChange.getNonogramSolutionBoard().get(firstXIndex).get(columnIdx).equals("-")) {
                                    nonogramLogicObject.increaseStepsMade();
                                    nonogramLogicDataToChange = nonogramLogicDataToChange
                                            .placeXAtGivenPosittion(firstXIndex, columnIdx)
                                            .addRowFieldToNotToInclude(firstXIndex, columnIdx)
                                            .addColumnFieldToNotToInclude(columnIdx, firstXIndex);
                                } else if(showRepetitions) {
                                    System.out.println("Longest sequence in column firstXIndex added before!");
                                }
                            }

                            if (lastXIndex < height) {
                                if( nonogramLogicDataToChange.getNonogramSolutionBoard().get(lastXIndex).get(columnIdx).equals("-")) {
                                    nonogramLogicObject.increaseStepsMade();
                                    nonogramLogicDataToChange = nonogramLogicDataToChange
                                            .placeXAtGivenPosittion(lastXIndex, columnIdx)
                                            .addRowFieldToNotToInclude(lastXIndex, columnIdx)
                                            .addColumnFieldToNotToInclude(columnIdx, lastXIndex);
                                } else if(showRepetitions) {
                                    System.out.println("Longest sequence in column lastXIndex added before!");
                                }
                            }

                            for(int sequenceRowIdx = firstXIndex + 1; sequenceRowIdx < lastXIndex; sequenceRowIdx++) {
                                if (!nonogramLogicDataToChange.getColumnsFieldsNotToInclude().get(columnIdx).contains(sequenceRowIdx)) {
                                    nonogramLogicObject.increaseStepsMade();
                                    nonogramLogicDataToChange = nonogramLogicDataToChange.addColumnFieldToNotToInclude(columnIdx, sequenceRowIdx);
                                } else if(showRepetitions) {
                                    System.out.println("Field not to include in column has been inserted before");
                                }
                            }
                        }

                    }
                } else if(columnSequencesIndexesIncludingSequenceRange.size() > 0) {
                    //check if length of sequence == Max(foundSequences_lengths)
                    if(sequenceLength == Collections.max(columnSequencesLengthsIncludingSequenceRange)) {
                        if(nonogramLogicDataToChange.getNonogramSolutionBoard().get(firstXIndex).get(columnIdx).equals("-")) {
                            nonogramLogicDataToChange = nonogramLogicDataToChange
                                    .placeXAtGivenPosittion(firstXIndex, columnIdx)
                                    .addColumnFieldToNotToInclude(columnIdx, firstXIndex)
                                    .addRowFieldToNotToInclude(firstXIndex, columnIdx);
                            nonogramLogicObject.increaseStepsMade();
                        } else if (showRepetitions) {
                            System.out.println("Sequence with maximum length in area firstXIndex placed before!");
                        }
                        if(nonogramLogicDataToChange.getNonogramSolutionBoard().get(lastXIndex).get(columnIdx).equals("-")) {
                            nonogramLogicDataToChange = nonogramLogicDataToChange
                                    .placeXAtGivenPosittion(lastXIndex, columnIdx)
                                    .addColumnFieldToNotToInclude(columnIdx, lastXIndex)
                                    .addRowFieldToNotToInclude(lastXIndex, columnIdx);
                            nonogramLogicObject.increaseStepsMade();
                        } else if(showRepetitions) {
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

        for(int rowIdx = rowBegin; rowIdx < rowEnd; rowIdx++) {
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

        for(int columnIdx = 0; columnIdx < width; columnIdx++) {
            fieldAsRange = new ArrayList<>();
            fieldAsRange.add(columnIdx);
            fieldAsRange.add(columnIdx);
            existRangeIncludingColumn = rangesListIncludingAnotherRange(rowSequencesRanges ,fieldAsRange);

            if(!existRangeIncludingColumn) {
                if(nonogramLogicDataToChange.getNonogramSolutionBoard().get(rowIdx).get(columnIdx).equals("-")) {
                    nonogramLogicDataToChange = nonogramLogicDataToChange
                            .placeXAtGivenPosittion(rowIdx, columnIdx)
                            .addRowFieldToNotToInclude(rowIdx, columnIdx)
                            .addColumnFieldToNotToInclude(columnIdx, rowIdx);
                    nonogramLogicObject.increaseStepsMade();
                } else if(showRepetitions) {
                    System.out.println("X at unreachable field in row placed before!");
                }
            }
        }

        return nonogramLogicDataToChange;
    }

    // iterations through all columns
    public NonogramLogic placeXsAtUnreachableFieldsInColumnsRange(NonogramLogic nonogramLogicObject, int columnBegin, int columnEnd) {

        NonogramLogic nonogramLogicDataToChange = nonogramLogicObject;

        for(int columnIdx = columnBegin; columnIdx < columnEnd; columnIdx++) {
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

        for(int rowIdx = 0; rowIdx < height; rowIdx++) {
            fieldAsRange = new ArrayList<>();
            fieldAsRange.add(rowIdx);
            fieldAsRange.add(rowIdx);
            existRangeIncludingRow = rangesListIncludingAnotherRange(columnSequencesRanges, fieldAsRange);

            if(!existRangeIncludingRow) {
                if(nonogramLogicDataToChange.getNonogramSolutionBoard().get(rowIdx).get(columnIdx).equals("-")) {
                    nonogramLogicDataToChange = nonogramLogicDataToChange
                            .placeXAtGivenPosittion(rowIdx, columnIdx)
                            .addRowFieldToNotToInclude(rowIdx, columnIdx)
                            .addColumnFieldToNotToInclude(columnIdx, rowIdx);
                    nonogramLogicObject.increaseStepsMade();
                } else if(showRepetitions) {
                    System.out.println("X at unreachable field in column placed before!");
                }
            }
        }

        return nonogramLogicDataToChange;
    }

    // iterations through all rows
    public NonogramLogic correctRowsSequencesRanges (NonogramLogic nonogramLogicObject, int rowBegin, int rowEnd) {
        NonogramLogic nonogramLogicChanged = nonogramLogicObject;

        for(int rowIdx = rowBegin; rowIdx < rowEnd; rowIdx++) {
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
        if(!rowSequencesIdsNotToInclude.contains(0)) {
            int fieldIdx = 0;

            while(rowFieldsNotToInclude.contains(fieldIdx)) {
                fieldIdx++;
            }

            List<Integer> oldFirstSequenceRange = rowSequencesRanges.get(0);
            List<Integer> updatedFirstSequenceRange = new ArrayList<>();
            updatedFirstSequenceRange.add(fieldIdx);
            updatedFirstSequenceRange.add(oldFirstSequenceRange.get(1));

            nonogramLogicObject.getRowsSequencesRanges().get(rowIdx).set(0, updatedFirstSequenceRange);
        }

        //from left/start
        for(int sequenceIdx = 0; sequenceIdx < rowSequencesRanges.size() - 1; sequenceIdx++) {

            if(rowSequencesIdsNotToInclude.contains(sequenceIdx) && !rowSequencesIdsNotToInclude.contains(sequenceIdx + 1)) {

                fullSequenceRange = rowSequencesRanges.get(sequenceIdx);

                oldNextSequenceRange = rowSequencesRanges.get(sequenceIdx + 1);
                updatedNextSequenceBeginRangeColumnIndex = fullSequenceRange.get(0) + 2;

                while(rowFieldsNotToInclude.contains(updatedNextSequenceBeginRangeColumnIndex)) {
                    updatedNextSequenceBeginRangeColumnIndex++;
                }

                oldNextSequenceBeginRangeColumnIndex = oldNextSequenceRange.get(0);
                oldNextSequenceEndRangeColumnIndex = oldNextSequenceRange.get(1);

                updatedNextSequenceRange = new ArrayList<>();

                //experimental
                while(rowFieldsNotToInclude.contains(oldNextSequenceEndRangeColumnIndex)) {
                    oldNextSequenceEndRangeColumnIndex--;
                }

                updatedNextSequenceRange.add(Math.max(oldNextSequenceBeginRangeColumnIndex, updatedNextSequenceBeginRangeColumnIndex));
                updatedNextSequenceRange.add(oldNextSequenceEndRangeColumnIndex);

                nonogramLogicObject.getRowsSequencesRanges().get(rowIdx).set(sequenceIdx + 1, updatedNextSequenceRange);
            }

            //second range update
            updatedNextSequenceRange = new ArrayList<>();
            //rangeBegin
            int oldNextSequenceRangeBegin = rowSequencesRanges.get(sequenceIdx + 1 ).get(0);
            int currentSequenceRangeBegin = rowSequencesRanges.get(sequenceIdx).get(0);
            int currentSequenceLengthPlusX = (rowSequences.get(sequenceIdx) + 1);
            updatedNextSequenceRange.add(
                    Math.max( oldNextSequenceRangeBegin, currentSequenceRangeBegin + currentSequenceLengthPlusX )
            );
            //rangeEnd
            updatedNextSequenceRange.add(rowSequencesRanges.get(sequenceIdx + 1).get(1));

            nonogramLogicObject.getRowsSequencesRanges().get(rowIdx).set(sequenceIdx + 1, updatedNextSequenceRange);
        }

        List<Integer> oldPreviousSequenceRange;
        int oldPreviousSequenceBeginRangeColumnIndex;
        int oldPreviousSequenceEndRangeColumnIndex;
        int updatedPreviousSequenceEndRangeColumnIndex;
        List<Integer> updatedPreviousSequenceRange;

        //from right/end
        for(int sequenceIdx = rowSequencesRanges.size() - 1; sequenceIdx > 0; sequenceIdx--) {

            if(rowSequencesIdsNotToInclude.contains(sequenceIdx) && !rowSequencesIdsNotToInclude.contains(sequenceIdx - 1)) {

                fullSequenceRange = rowSequencesRanges.get(sequenceIdx);
                oldPreviousSequenceRange = rowSequencesRanges.get(sequenceIdx - 1);
                updatedPreviousSequenceEndRangeColumnIndex = fullSequenceRange.get(0) - 2;

                while(rowFieldsNotToInclude.contains(updatedPreviousSequenceEndRangeColumnIndex)) {
                    updatedPreviousSequenceEndRangeColumnIndex--;
                }

                oldPreviousSequenceBeginRangeColumnIndex = oldPreviousSequenceRange.get(0);
                oldPreviousSequenceEndRangeColumnIndex = oldPreviousSequenceRange.get(1);

                updatedPreviousSequenceRange = new ArrayList<>();

                //experimental
                while(rowFieldsNotToInclude.contains(oldPreviousSequenceBeginRangeColumnIndex)) {
                    oldPreviousSequenceBeginRangeColumnIndex++;
                }

                updatedPreviousSequenceRange.add(oldPreviousSequenceBeginRangeColumnIndex);
                int newEnd = Math.min(oldPreviousSequenceEndRangeColumnIndex, updatedPreviousSequenceEndRangeColumnIndex);
                updatedPreviousSequenceRange.add(newEnd);

                nonogramLogicObject.getRowsSequencesRanges().get(rowIdx).set(sequenceIdx - 1, updatedPreviousSequenceRange);
            }

            //second range update
            updatedPreviousSequenceRange = new ArrayList<>();
            //rangeBegin
            updatedPreviousSequenceRange.add(rowSequencesRanges.get(sequenceIdx - 1).get(0));

            int oldPreviousSequenceRangeEnd = rowSequencesRanges.get(sequenceIdx - 1 ).get(1);
            int currentSequenceRangeEnd = rowSequencesRanges.get(sequenceIdx).get(1);
            int currentSequenceLengthPlusX = (rowSequences.get(sequenceIdx) + 1);

            //rangeEnd
            updatedPreviousSequenceRange.add(
                    Math.min( oldPreviousSequenceRangeEnd, currentSequenceRangeEnd - currentSequenceLengthPlusX )
            );

            nonogramLogicObject.getRowsSequencesRanges().get(rowIdx).set(sequenceIdx - 1, updatedPreviousSequenceRange);
        }

        //for last sequence in row
        int width = nonogramLogicObject.getWidth();
        int lastRowSequenceIndex = rowSequencesRanges.size() - 1;

        if(!rowSequencesIdsNotToInclude.contains(lastRowSequenceIndex)) {
            int fieldIdx = width - 1;

            while(rowFieldsNotToInclude.contains(fieldIdx)) {
                fieldIdx--;
            }

            List<Integer> oldLastSequenceRange = rowSequencesRanges.get(lastRowSequenceIndex);
            List<Integer> updatedLastSequenceRange = new ArrayList<>();
            updatedLastSequenceRange.add(oldLastSequenceRange.get(0));
            updatedLastSequenceRange.add(fieldIdx);

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

        for(int columnIdx = 0; columnIdx < width; columnIdx++) {
            if(solutionBoardRow.get(columnIdx).equals("O".repeat(4))) {
                rowSequenceRangeStart = rowSequencesRanges.get(sequenceId).get(0);
                rowSequenceRangeEnd = rowSequencesRanges.get(sequenceId).get(1);
                maximumPossibleSequenceRangeEnd = columnIdx + sequenceLength - 1;

                updatedRange = new ArrayList<>();
                updatedRange.add(rowSequenceRangeStart);
                updatedRange.add(Math.min(rowSequenceRangeEnd, maximumPossibleSequenceRangeEnd));
                nonogramLogicObject.getRowsSequencesRanges().get(rowIdx).set(sequenceId, updatedRange);

                columnIdx = columnIdx + sequenceLength;
                sequenceId++;
                if(sequenceId < rowSequencesLengths.size()) {
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

        for(int columnIdx = width - 1; columnIdx >= 0; columnIdx--) {
            if(solutionBoardRow.get(columnIdx).equals("O".repeat(4))) {
                minimumPossibleSequenceRangeStart = columnIdx - sequenceLength + 1;
                rowSequenceRangeStart = rowSequencesRanges.get(sequenceId).get(0);
                rowSequenceRangeEnd = rowSequencesRanges.get(sequenceId).get(1);

                updatedRange = new ArrayList<>();
                updatedRange.add(Math.max(minimumPossibleSequenceRangeStart, rowSequenceRangeStart));
                updatedRange.add(rowSequenceRangeEnd);
                nonogramLogicObject.getRowsSequencesRanges().get(rowIdx).set(sequenceId, updatedRange);

                columnIdx = columnIdx - sequenceLength;
                sequenceId--;
                if(sequenceId > -1) {
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
        List<Integer> updatedRowRange = new ArrayList<>();

        for(int seqNo = 0; seqNo < nonogramRowSequencesRanges.size(); seqNo++) {
            if(!nonogramRowSequencesIdsNotToInclude.contains(seqNo)) {

                rowSequenceRange = nonogramRowSequencesRanges.get(seqNo);
                rowSequenceLength = rowSequences.get(seqNo);
                rowSequenceRangeStartIndex = rowSequenceRange.get(0);
                rowSequenceRangeEndIndex = rowSequenceRange.get(1);

                updatedRowRangeStartIndex = rowSequenceRangeStartIndex;

                for(int columnStartIndex = rowSequenceRangeStartIndex; columnStartIndex < (rowSequenceRangeEndIndex - rowSequenceLength + 1); columnStartIndex++) {
                    indexOk = true;
                    for(int columnIdx = columnStartIndex; columnIdx < columnStartIndex + rowSequenceLength; columnIdx++) {
                        if(nonogramBoard.get(rowIdx).get(columnIdx).equals("XXXX")) {
                            indexOk = false;
                            break;
                        }
                    }
                    if(indexOk) {
                        break;
                    } else {
                        updatedRowRangeStartIndex++;
                    }
                }

                updatedRowRangeEndIndex = rowSequenceRangeEndIndex;

                for(int columnEndIndex = rowSequenceRangeEndIndex; columnEndIndex > (rowSequenceRangeStartIndex + rowSequenceLength - 1); columnEndIndex--) {
                    indexOk = true;
                    for(int columnIdx = columnEndIndex; columnIdx > columnEndIndex - rowSequenceLength; columnIdx--) {
                        if(nonogramBoard.get(rowIdx).get(columnIdx).equals("XXXX")) {
                            indexOk = false;
                            break;
                        }
                    }
                    if(indexOk) {
                        break;
                    } else {
                        updatedRowRangeEndIndex--;
                    }
                }

                updatedRowRange.add(updatedRowRangeStartIndex);
                updatedRowRange.add(updatedRowRangeEndIndex);

                nonogramLogicObject.getNonogramRowLogic().updateRowSequenceRange(rowIdx, seqNo, updatedRowRange);
                nonogramLogicObject.copyLogicFromNonogramRowLogic();
            }
        }

        return nonogramLogicObject;
    }

    // iterations through all columns
    public NonogramLogic correctColumnsSequencesRanges (NonogramLogic nonogramLogicObject, int columnBegin, int columnEnd) {
        NonogramLogic nonogramLogicChanged = nonogramLogicObject;

        for(int columnIdx = columnBegin; columnIdx < columnEnd; columnIdx++) {
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
        List<Integer> updatedNextSequenceRange;

        List<Integer> columnSequencesIdsNotToInclude = nonogramLogicObject.getColumnsSequencesIdsNotToInclude().get(columnIdx);

        List<Integer> columnIndexesNotToInclude = nonogramLogicObject.getColumnsFieldsNotToInclude().get(columnIdx);

        //for first sequence in column
        if(!columnSequencesIdsNotToInclude.contains(0)) {
            int fieldIdx = 0;

            while(columnFieldsNotToInclude.contains(fieldIdx)) {
                fieldIdx++;
            }

            List<Integer> oldFirstSequenceRange = columnSequencesRanges.get(0);
            List<Integer> updatedFirstSequenceRange = new ArrayList<>();
            updatedFirstSequenceRange.add(Math.max(fieldIdx, oldFirstSequenceRange.get(0)));
            updatedFirstSequenceRange.add(oldFirstSequenceRange.get(1));

            nonogramLogicObject.getColumnsSequencesRanges().get(columnIdx).set(0, updatedFirstSequenceRange);
        }

        //from top - start
        for(int sequenceIdx = 0; sequenceIdx < columnSequencesRanges.size() - 1; sequenceIdx++) {

            if(columnSequencesIdsNotToInclude.contains(sequenceIdx) && !columnSequencesIdsNotToInclude.contains(sequenceIdx + 1)) {

                fullSequenceRange = columnSequencesRanges.get(sequenceIdx);

                oldNextSequenceRange = columnSequencesRanges.get(sequenceIdx + 1);
                updatedNextSequenceBeginRangeRowIndex = fullSequenceRange.get(0) + 2;

                while(columnFieldsNotToInclude.contains(updatedNextSequenceBeginRangeRowIndex)) {
                    updatedNextSequenceBeginRangeRowIndex++;
                }

                oldNextSequenceBeginRangeRowIndex = oldNextSequenceRange.get(0);
                oldNextSequenceEndRangeRowIndex = oldNextSequenceRange.get(1);

                updatedNextSequenceRange = new ArrayList<>();

                //experimental
                while(columnFieldsNotToInclude.contains(oldNextSequenceEndRangeRowIndex)) {
                    oldNextSequenceEndRangeRowIndex--;
                }

                updatedNextSequenceRange.add(Math.max(oldNextSequenceBeginRangeRowIndex, updatedNextSequenceBeginRangeRowIndex));
                updatedNextSequenceRange.add(oldNextSequenceEndRangeRowIndex);

                if(!columnIndexesNotToInclude.contains(columnIdx)) {
                    nonogramLogicObject.getColumnsSequencesRanges().get(columnIdx).set(sequenceIdx + 1, updatedNextSequenceRange);
                }
            }

            //second range update
            updatedNextSequenceRange = new ArrayList<>();

            //rangeBegin
            int oldNextSequenceRangeBegin = columnSequencesRanges.get(sequenceIdx + 1 ).get(0);
            int currentSequenceRangeBegin = columnSequencesRanges.get(sequenceIdx).get(0);
            int currentSequenceLengthPlusX = (columnSequences.get(sequenceIdx) + 1);
            updatedNextSequenceRange.add(
                    Math.max( oldNextSequenceRangeBegin, currentSequenceRangeBegin + currentSequenceLengthPlusX )
            );
            //rangeEnd
            updatedNextSequenceRange.add(columnSequencesRanges.get(sequenceIdx + 1).get(1));

            if(!columnIndexesNotToInclude.contains(columnIdx)) {
                nonogramLogicObject.getColumnsSequencesRanges().get(columnIdx).set(sequenceIdx + 1, updatedNextSequenceRange);
            }
        }

        List<Integer> oldPreviousSequenceRange;
        int oldPreviousSequenceBeginRangeRowIndex;
        int oldPreviousSequenceEndRangeRowIndex;
        int updatedPreviousSequenceEndRangeRowIndex;
        List<Integer> updatedPreviousSequenceRange;

        //from bottom - end
        for(int sequenceIdx = columnSequencesRanges.size() - 1; sequenceIdx > 0; sequenceIdx--) {

            if(columnSequencesIdsNotToInclude.contains(sequenceIdx) && !columnSequencesIdsNotToInclude.contains(sequenceIdx - 1)) {

                fullSequenceRange = columnSequencesRanges.get(sequenceIdx);
                oldPreviousSequenceRange = columnSequencesRanges.get(sequenceIdx - 1);
                updatedPreviousSequenceEndRangeRowIndex = fullSequenceRange.get(0) - 2;

                while(columnFieldsNotToInclude.contains(updatedPreviousSequenceEndRangeRowIndex)) {
                    updatedPreviousSequenceEndRangeRowIndex--;
                }

                oldPreviousSequenceBeginRangeRowIndex = oldPreviousSequenceRange.get(0);
                oldPreviousSequenceEndRangeRowIndex = oldPreviousSequenceRange.get(1);

                updatedPreviousSequenceRange = new ArrayList<>();

                //experimental
                while(columnFieldsNotToInclude.contains(oldPreviousSequenceBeginRangeRowIndex)) {
                    oldPreviousSequenceBeginRangeRowIndex++;
                }

                updatedPreviousSequenceRange.add(oldPreviousSequenceBeginRangeRowIndex);
                updatedPreviousSequenceRange.add(Math.min(oldPreviousSequenceEndRangeRowIndex, updatedPreviousSequenceEndRangeRowIndex));

                if(!columnIndexesNotToInclude.contains(columnIdx)) {
                    nonogramLogicObject.getColumnsSequencesRanges().get(columnIdx).set(sequenceIdx - 1, updatedPreviousSequenceRange);
                }
            }

            //second range update
            updatedPreviousSequenceRange = new ArrayList<>();
            //rangeBegin
            updatedPreviousSequenceRange.add(columnSequencesRanges.get(sequenceIdx - 1).get(0));

            int oldPreviousSequenceRangeEnd = columnSequencesRanges.get(sequenceIdx - 1 ).get(1);
            int currentSequenceRangeEnd = columnSequencesRanges.get(sequenceIdx).get(1);
            int currentSequenceLengthPlusX = (columnSequences.get(sequenceIdx) + 1);
            int possibleLowerPreviousSequenceRangeEnd = currentSequenceRangeEnd - currentSequenceLengthPlusX;

            //rangeEnd
            updatedPreviousSequenceRange.add(
                    Math.min( oldPreviousSequenceRangeEnd, possibleLowerPreviousSequenceRangeEnd )
            );

            if(!columnIndexesNotToInclude.contains(columnIdx)) {
                nonogramLogicObject.getColumnsSequencesRanges().get(columnIdx).set(sequenceIdx - 1, updatedPreviousSequenceRange);
            }
        }

        //for last sequence in column
        int height = nonogramLogicObject.getHeight();
        int lastColumnSequenceIndex = columnSequencesRanges.size() - 1;

        if(!columnSequencesIdsNotToInclude.contains(lastColumnSequenceIndex)) {
            int fieldIdx = height - 1;

            while(columnFieldsNotToInclude.contains(fieldIdx)) {
                fieldIdx--;
            }

            List<Integer> oldLastSequenceRange = columnSequencesRanges.get(lastColumnSequenceIndex);
            List<Integer> updatedLastSequenceRange = new ArrayList<>();
            updatedLastSequenceRange.add(oldLastSequenceRange.get(0));
            updatedLastSequenceRange.add(Math.min(fieldIdx, oldLastSequenceRange.get(1)));

            if(!columnIndexesNotToInclude.contains(columnIdx)) {
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
        List<Integer> updatedColumnSequenceRange = new ArrayList<>();

        for(int seqNo = 0; seqNo < nonogramColumnSequencesRanges.size(); seqNo++) {
            if(!nonogramColumnsSequencesIdsNotToInclude.contains(seqNo)) {

                columnSequenceRange = nonogramColumnSequencesRanges.get(seqNo);
                columnSequenceLength = columnSequences.get(seqNo);
                columnSequenceRangeStartIndex = columnSequenceRange.get(0);
                columnSequenceRangeEndIndex = columnSequenceRange.get(1);

                updatedColumnSequenceRangeStartIndex = columnSequenceRangeStartIndex;

                for(int rowStartIndex = columnSequenceRangeStartIndex; rowStartIndex < columnSequenceRangeEndIndex - columnSequenceLength + 1; rowStartIndex++) {
                    indexOk = true;
                    for(int rowIdx = rowStartIndex; rowIdx < rowStartIndex + columnSequenceLength; rowIdx++) {
                        if(nonogramBoard.get(rowIdx).get(columnIdx).equals("XXXX")) {
                            indexOk = false;
                            break;
                        }
                    }
                    if(indexOk) {
                        break;
                    } else {
                        updatedColumnSequenceRangeStartIndex++;
                    }
                }

                updatedColumnSequenceRangeEndIndex = columnSequenceRangeEndIndex;

                for(int rowEndIndex = columnSequenceRangeEndIndex; rowEndIndex > columnSequenceRangeStartIndex + columnSequenceLength - 1; rowEndIndex--) {
                    indexOk = true;
                    for(int rowIdx = rowEndIndex; rowIdx > rowEndIndex - columnSequenceLength; rowIdx--) {
                        if(nonogramBoard.get(rowIdx).get(columnIdx).equals("XXXX")) {
                            indexOk = false;
                            break;
                        }
                    }
                    if(indexOk) {
                        break;
                    } else {
                        updatedColumnSequenceRangeEndIndex--;
                    }
                }

                updatedColumnSequenceRange.add(updatedColumnSequenceRangeStartIndex);
                updatedColumnSequenceRange.add(updatedColumnSequenceRangeEndIndex);

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

        for(int rowIdx = 0; rowIdx < height; rowIdx++) {
            solutionBoardColumn.add(nonogramSolutionBoard.get(rowIdx).get(columnIdx));
        }

        int sequenceId = 0;
        int sequenceLength = columnSequencesLengths.get(0);

        for(int rowIdx = 0; rowIdx < height; rowIdx++) {
            if(solutionBoardColumn.get(rowIdx).equals("O".repeat(4))) {
                columnSequenceRangeStart = columnSequencesRanges.get(sequenceId).get(0);
                columnSequenceRangeEnd = columnSequencesRanges.get(sequenceId).get(1);
                maximumPossibleSequenceRangeEnd = rowIdx + sequenceLength - 1;

                updatedRange = new ArrayList<>();
                updatedRange.add(columnSequenceRangeStart);
                updatedRange.add(Math.min(columnSequenceRangeEnd, maximumPossibleSequenceRangeEnd));
                nonogramLogicObject.getColumnsSequencesRanges().get(columnIdx).set(sequenceId, updatedRange);

                rowIdx = rowIdx + sequenceLength;
                sequenceId++;
                if(sequenceId < columnSequencesLengths.size()) {
                    sequenceLength = columnSequencesLengths.get(sequenceId);
                } else {
                    break;
                }
            }
        }

        return nonogramLogicObject;
    }

    public boolean compareSubSolutionWithSolution(List<List<List<Integer>>> subSolutionRowsSequencesRanges, List<List<List<Integer>>> solutionRowsSequencesRanges,
                                                  List<List<List<Integer>>> subSolutionColumnsSequencesRanges, List<List<List<Integer>>> solutionColumnsSequencesRanges,
                                                  List<List<String>> subSolutionBoard, List<List<String>> solutionBoard) {
        List<List<Integer>> subSolutionRowRanges;
        List<List<Integer>> solutionRowRanges;

        for(int rowIdx = 0; rowIdx < subSolutionRowsSequencesRanges.size(); rowIdx++) {
            subSolutionRowRanges = subSolutionRowsSequencesRanges.get(rowIdx);
            solutionRowRanges = solutionRowsSequencesRanges.get(rowIdx);
            for(int sequenceNo = 0; sequenceNo < solutionRowRanges.size(); sequenceNo++) {
                if(!rangeInsideAnotherRange(solutionRowRanges.get(sequenceNo), subSolutionRowRanges.get(sequenceNo))) {
                    return false;
                }
            }
        }

        List<List<Integer>> subSolutionColumnRanges;
        List<List<Integer>> solutionColumnRanges;

        for(int columnIdx = 0; columnIdx < subSolutionColumnsSequencesRanges.size(); columnIdx++) {
            subSolutionColumnRanges = subSolutionColumnsSequencesRanges.get(columnIdx);
            solutionColumnRanges = solutionColumnsSequencesRanges.get(columnIdx);
            for(int sequenceNo = 0; sequenceNo < solutionColumnRanges.size(); sequenceNo++) {
                if(!rangeInsideAnotherRange(solutionColumnRanges.get(sequenceNo), subSolutionColumnRanges.get(sequenceNo))) {
                    System.out.println("columnIdx: " + columnIdx);
                    System.out.println("subSolutionColumnRanges: " + subSolutionColumnRanges);
                    System.out.println("solutionColumnRanges: " + solutionColumnRanges);
                    return false;
                }
            }

        }

        for (int rowIdx = 0; rowIdx < solutionRowsSequencesRanges.size(); rowIdx++) {
            for (int columnIdx = 0; columnIdx < solutionColumnsSequencesRanges.size(); columnIdx++) {

                if(subSolutionBoard.get(rowIdx).get(columnIdx).equals("XXXX") && !solutionBoard.get(rowIdx).get(columnIdx).equals("XXXX")) {
                    System.out.println("rowIdx: " + rowIdx + ", columnIdx: " + columnIdx + " inconsistency");
                    return false;
                }

                if(subSolutionBoard.get(rowIdx).get(columnIdx).equals("O".repeat(4)) && !solutionBoard.get(rowIdx).get(columnIdx).equals("O".repeat(4))) {
                    System.out.println("rowIdx: " + rowIdx + ", columnIdx: " + columnIdx + " inconsistency");
                    return false;
                }
            }
        }

        return true;
    }

    public static List<Integer> createRangeFromTwoIntegers(int rangeBegin, int rangeEnd) {
        List<Integer> range = new ArrayList<>();
        range.add(rangeBegin);
        range.add(rangeEnd);

        return range;
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

        for(int rangeNo = 0; rangeNo < ranges.size(); rangeNo++) {
            if(rangeInsideAnotherRange(rangeToInclude, ranges.get(rangeNo)) && lengths.get(rangeNo) >= rangeLength(rangeToInclude)) {
                filteredLengths.add(lengths.get(rangeNo));
            }
        }

        return filteredLengths;
    }

    public static boolean rangesEqual(List<Integer> firstRange, List<Integer> secondRange) {
        return (firstRange.size() == 2 && secondRange.size() == 2) ?
                (firstRange.get(0) == secondRange.get(0) && firstRange.get(1) == secondRange.get(1) ? true : false)
                : false;
    }

    public static int rangeLength(List<Integer> range) {
        int rangeStart = range.get(0);
        int rangeEnd = range.get(range.size() - 1);
        return rangeEnd - rangeStart + 1;
    }

    public static boolean rangeInsideAnotherRange(List<Integer> internalRange, List<Integer> externalRange) {
        if(internalRange.size() >= 1 && externalRange.size() == 2) {
            return (internalRange.get(0) >= externalRange.get(0)) && (internalRange.get(internalRange.size() - 1) <= externalRange.get(1));
        } else {
            return false;
        }
    }

    public NonogramLogic loadSolutionFromFile(String solutionFileName, NonogramLogic placeholder) {
        initGson(false);

        try (Reader reader = new FileReader(resourcesPath + "nonogramsSolutions/r" + solutionFileName + ".json")) {

            // Convert JSON File to Java Object

            return gson.fromJson(reader, NonogramLogic.class);

        } catch (IOException e) {
            return placeholder;
        }
    }

    public NonogramLogic runCustomSolverOperationWithCorrectnessCheck(NonogramLogic nonogramLogicObject, String fileName) throws CloneNotSupportedException {
        NonogramSolver nonogramSolver = new NonogramSolver(nonogramLogicObject, fileName);
        NonogramSolutionNode nonogramSolutionNode = new NonogramSolutionNode(nonogramLogicObject);

        nonogramSolver.runSolutionAtNode(nonogramSolutionNode);

        completionAfterHeuristics = nonogramSolver.getCompletionAfterHeuristics();
        timeAfterHeuristics = nonogramSolver.getTimeAfterHeuristics();
        completionAfterTrialAndError = nonogramSolver.getCompletionAfterTrialAndError();
        timeAfterTrialAndError = nonogramSolver.getTimeAfterTrialAndError();
        completionAfterRecursion = nonogramSolver.getCompletionAfterRecursion();
        timeAfterRecursion = nonogramSolver.getTimeAfterRecursion();
        nonogramGuessDecisionsSize = nonogramSolver.getSolutionNode().getNonogramGuessDecisions().size();


        height = nonogramLogicObject.getHeight();
        width = nonogramLogicObject.getWidth();
        area = nonogramLogicObject.getHeight() * nonogramLogicObject.getWidth();
        source = nonogramRepository.selectFileSource(fileName);
        difficulty = nonogramRepository.selectFileDifficulty(fileName);
        recursionDepth = nonogramSolver.getRecursionDepth();

        NonogramResult nonogramResult = new NonogramResult(
                source,
                fileName,
                height,
                width,
                difficulty,
                timeAfterHeuristics,
                completionAfterHeuristics,
                timeAfterTrialAndError,
                completionAfterTrialAndError,
                timeAfterRecursion,
                completionAfterRecursion,
                nonogramSolver.getSolutionNode().getNonogramGuessDecisions(),
                recursionDepth
        );

        try {;
            fileWriter = new FileWriter(getResultsPath()  + fileName + "-result.json");
            fileWriter.write(nonogramResult.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println( new StringBuilder()
                .append(String.format("%-" + 30 + "s", fileName))
                .append(String.format("%-" + 7 + "s", source))
                .append(String.format("%-" + ("difficulty".length() + 1) + "s", difficulty))
                .append(String.format("%-" + ("height".length() + 1) + "s", height))
                .append(String.format("%-" + ("width".length() + 1) + "s", width))
                .append(String.format("%-" + ("area".length() + 1) + "s", area))
                .append(String.format("%-" + ("TaH(s)".length() + 3) + "s", timeAfterHeuristics))
                .append(String.format("%-" + ("CaH(%)".length() + 1) + "s", completionAfterHeuristics))
                .append(String.format("%-" + ("heuristics_only".length() + 1) + "s", nonogramSolver.getCompletionAfterHeuristics() == 100.0 ? 1 : 0))
                .append(String.format("%-" + ("TaTAR(s)".length() + 3) + "s", timeAfterTrialAndError))
                .append(String.format("%-" + ("CaTAR(%)".length() + 1) + "s", completionAfterTrialAndError))
                .append(String.format("%-" + ("dec".length() + 1) + "s", nonogramGuessDecisionsSize))
                .append(String.format("%-" + ("heuristics_with_trial_and_error".length() + 1) + "s", nonogramSolver.getCompletionAfterTrialAndError() == 100.0 ? 1 : 0))
                .append(String.format("%-" + ("TaR(s)".length() + 3) + "s", timeAfterRecursion))
                .append(String.format("%-" + ("CaR(%)".length() + 1) + "s", completionAfterRecursion))
        );

        if(nonogramSolver.getSolutionNode().getNonogramGuessDecisions().size() > 0) {
            nonogramSolver.getSolutionNode().printNodeGuessDecisions();
        }

        return nonogramSolver.getSolutionLogic();
    }

    public static String getResultsPath() {
        String nonogramsPath = InitializerConstants.NONOGRAM_RESULTS_PATH;
        return nonogramsPath + NonogramConfig.getNonogramResultsOutputPath();
    }

    public static void printHeaders() {
        System.out.print(justifiedString(20,"filename"));
        System.out.print(justifiedString(7,"source"));
        System.out.print(justifiedString("difficulty".length() + 1,"difficulty"));
        System.out.print(justifiedString("height".length() + 1,"height"));
        System.out.print(justifiedString("width".length() + 1,"width"));
        System.out.print(justifiedString("area".length() + 1,"area"));
        System.out.print(justifiedString("TaH(s)".length() + 3,"TaH(s)"));
        System.out.print(justifiedString("CaH(%)".length() + 1,"CaH(%)"));
        System.out.print(justifiedString("heuristics_only".length() + 1, "heuristics_only"));
        System.out.print(justifiedString("TaTAR(s)".length() + 3,"TaTAR(s)"));
        System.out.print(justifiedString("CaTAR(%)".length() + 1,"CaTAR(%)"));
        System.out.print(justifiedString("dec".length() + 1,"dec"));
        System.out.print(justifiedString("heuristics_with_trial_and_error".length() + 1,
                "heuristics_with_trial_and_error"));
        System.out.print(justifiedString("TaR(s)".length() + 3,"TaR(s)"));
        System.out.println(justifiedString("CaR(%)".length() + 1,"CaR(%)"));
    }
}
