package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogicFactory;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramSolver;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solutions.NonogramSolutionNode;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.config.GuessMode;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.FinalNonogramSolutionDTO;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.NonogramInitializationRequest;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.NonogramSolutionSaveRequest;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row.NonogramRowLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.mapper.NonogramMapper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service.saver.NonogramSolutionSaver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.rangeInsideAnotherRange;
import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.rangeLength;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramConstants.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.mark.NonogramFieldMarkHelper.markColumnBoardField;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.mark.NonogramFieldMarkHelper.markRowBoardField;

@Service
@Slf4j
public class NonogramLogicService {

    private final NonogramLogicFactory logicFactory;

    private final NonogramSolutionSaver nonogramSolutionSaver;

    private static final boolean SHOW_REPETITIONS = false;

    public NonogramLogicService(NonogramSolutionSaver nonogramSolutionSaver,
                                NonogramLogicFactory logicFactory) {
        this.nonogramSolutionSaver = nonogramSolutionSaver;
        this.logicFactory = logicFactory;
    }

    public NonogramLogic initializeLogicFromRequest(NonogramInitializationRequest request) {
        NonogramRules rules = new NonogramRules(request.getRowSequences(), request.getColumnSequences(), request.getHeight(), request.getWidth());

        return new NonogramLogic(rules, GuessMode.DISABLED);
    }

    /**
     * Applies the overlapping field fill logic for each column in the given range.
     *
     * @param logic the {@link NonogramLogic} object representing the current puzzle state
     * @param columnBegin the index of the first column (inclusive)
     * @param columnEnd the index of the last column (exclusive)
     * @return the updated {@link NonogramLogic} with applied column overlap fills
     */
    public NonogramLogic fillOverlappingFieldsInColumnsRange(NonogramLogic logic, int columnBegin, int columnEnd) {
        for (int columnIdx = columnBegin; columnIdx < columnEnd; columnIdx++) {
            logic = fillOverlappingFieldsInColumn(logic, columnIdx);
        }
        return logic;
    }

    /**
     * <p>
     * Fills in overlapping fields in a column with coloured values and sequence marks.
     * </p>
     *
     * <p><strong>Steps:</strong></p>
     * <ol>
     *     <li>Get sequence lengths and ranges in the column.</li>
     *     <li>For each sequence, calculate overlapping (definitely coloured) range.</li>
     *     <li>If such a range exists, mark and colour all fields in it.</li>
     *     <li>Return updated logic object.</li>
     * </ol>
     *
     * @param logic the {@link NonogramLogic} object representing the current state of the nonogram
     * @param columnIdx the index of the column being processed
     * @return the updated {@link NonogramLogic} object with newly coloured and marked fields
     */
    public NonogramLogic fillOverlappingFieldsInColumn(NonogramLogic logic, int columnIdx) {
        List<Integer> sequenceLengths = logic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> sequenceRanges = logic.getColumnsSequencesRanges().get(columnIdx);

        for (int seqIdx = 0; seqIdx < sequenceLengths.size(); seqIdx++) {
            int seqLength = sequenceLengths.get(seqIdx);
            List<Integer> range = sequenceRanges.get(seqIdx);
            int overlapStart = range.get(1) - seqLength + 1;
            int overlapEnd = range.get(0) + seqLength - 1;

            if (overlapStart <= overlapEnd) {
                markOverlappingRangeInColumn(logic, columnIdx, seqIdx, overlapStart, overlapEnd);
            }
        }

        return logic;
    }

    /**
     * <p>
     * Marks and colours all fields in the specified overlapping range of a column for a given sequence.
     * </p>
     *
     * <p>
     * Steps:
     * <ol>
     *     <li>Retrieve the field mark for the sequence (e.g. 'a', 'b', etc.).</li>
     *     <li>Iterate over the range of rows where the overlapping fields are located.</li>
     *     <li>If a field is unmarked (has "--" suffix), update it in both the solution board and the marked board.</li>
     *     <li>Log a warning if the field was already marked (if repetition logging is enabled).</li>
     * </ol>
     * </p>
     *
     * @param logic the current state of the nonogram logic
     * @param colIdx the column index of the overlapping range
     * @param seqIdx the index of the sequence being processed
     * @param startRow the starting row index of the overlapping range (inclusive)
     * @param endRow the ending row index of the overlapping range (inclusive)
     */
    private void markOverlappingRangeInColumn(NonogramLogic logic, int colIdx, int seqIdx, int startRow, int endRow) {
        String marker = indexToSequenceCharMark(seqIdx);

        for (int rowIdx = startRow; rowIdx <= endRow; rowIdx++) {
            List<String> rowWithMarks = logic.getNonogramSolutionBoardWithMarks().get(rowIdx);
            String currentMark = rowWithMarks.get(colIdx);

            if (currentMark.substring(2).equals(EMPTY_PART_MARKED_BOARD)) {
                markAndColourFieldInColumn(logic, colIdx, rowIdx, currentMark, marker);
            } else if (SHOW_REPETITIONS) {
                log.warn("Column field was coloured before!");
            }
        }
    }

    /**
     * <p>
     * Updates a field in the column with the appropriate mark and colours it if not already coloured.
     * </p>
     *
     * <p>
     * Steps:
     * <ol>
     *     <li>Check if the field in the solution board is uncoloured (value equals {@code EMPTY_FIELD_MARKED_BOARD}).</li>
     *     <li>If so, replace it with {@code COLOURED_FIELD_MARKED_BOARD}.</li>
     *     <li>Update the field's mark in the board with marks by prepending {@code "C"} and the sequence marker.</li>
     *     <li>Increment the step counter in the logic state to reflect the modification.</li>
     * </ol>
     * </p>
     *
     * @param logic the current nonogram logic object
     * @param colIdx the column index of the field to be coloured and marked
     * @param rowIdx the row index of the field to be coloured and marked
     * @param currentMarker the current marker string at that position
     * @param marker the new marker to apply (e.g. "a", "b", etc.)
     */
    private void markAndColourFieldInColumn(NonogramLogic logic, int colIdx, int rowIdx, String currentMarker, String marker) {
        logic.getNonogramSolutionBoardWithMarks()
                .get(rowIdx)
                .set(colIdx, currentMarker.substring(0, 2) + "C" + marker);

        List<String> boardRow = logic.getNonogramSolutionBoard().get(rowIdx);
        if (boardRow.get(colIdx).equals(EMPTY_FIELD_MARKED_BOARD)) {
            boardRow.set(colIdx, COLOURED_FIELD_MARKED_BOARD);
        }

        logic.getNonogramState().increaseMadeSteps();
    }

    /**
     * Applies the overlapping field fill logic for each column in the given range.
     *
     * @param logic the {@link NonogramLogic} object representing the current puzzle state
     * @param rowBegin the index of the first row (inclusive)
     * @param rowEnd the index of the last row (exclusive)
     * @return the updated {@link NonogramLogic} with applied row overlap fills
     */
    public NonogramLogic fillOverLappingFieldsInRowsRange(NonogramLogic logic, int rowBegin, int rowEnd) {
        for (int rowIdx = rowBegin; rowIdx < rowEnd; rowIdx++) {
            logic = fillOverlappingFieldsInRow(logic, rowIdx);
        }
        return logic;
    }

    /**
     * <p>
     * Fills in overlapping fields in a row with coloured values and sequence marks.
     * </p>
     *
     * <p>
     * Steps:
     * <ol>
     *     <li>Get sequence lengths and ranges in the row.</li>
     *     <li>For each sequence, calculate overlapping (definitely coloured) range.</li>
     *     <li>If such a range exists, mark and colour all fields in it.</li>
     *     <li>Return updated logic object.</li>
     * </ol>
     * </p>
     *
     * @param logic the {@link NonogramLogic} object representing the current state of the nonogram
     * @param rowIdx the row index to apply the operation on
     * @return updated {@link NonogramLogic} with overlapping row fields filled
     */
    public NonogramLogic fillOverlappingFieldsInRow(NonogramLogic logic, int rowIdx) {
        List<Integer> sequenceLengths = logic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> sequenceRanges = logic.getRowsSequencesRanges().get(rowIdx);

        for (int seqIdx = 0; seqIdx < sequenceLengths.size(); seqIdx++) {
            int seqLength = sequenceLengths.get(seqIdx);
            List<Integer> range = sequenceRanges.get(seqIdx);
            int overlapStart = range.get(1) - seqLength + 1;
            int overlapEnd = range.get(0) + seqLength - 1;

            if (overlapStart <= overlapEnd) {
                markOverlappingRangeInRow(logic, rowIdx, seqIdx, overlapStart, overlapEnd);
            }
        }

        return logic;
    }

    /**
     * <p>
     * Marks and colours all fields in the specified overlapping range of a row for a given sequence.
     * </p>
     *
     * <p>
     * Steps:
     * <ol>
     *     <li>Retrieve the field mark for the sequence (e.g. 'a', 'b', etc.).</li>
     *     <li>Iterate over the range of columns where overlap occurs.</li>
     *     <li>If a field is unmarked (has "--" prefix), update it in both the solution board and marked board.</li>
     *     <li>Log a warning if the field was already marked (if repetition logging is enabled).</li>
     * </ol>
     * </p>
     *
     * @param logic the current state of the nonogram logic
     * @param rowIdx the row index of the overlapping range
     * @param seqIdx the index of the sequence being processed
     * @param startColumnIdx the starting column index of the overlapping range (inclusive)
     * @param endColumnIdx the ending column index of the overlapping range (inclusive)
     */
    private void markOverlappingRangeInRow(NonogramLogic logic, int rowIdx, int seqIdx, int startColumnIdx, int endColumnIdx) {
        String marker = indexToSequenceCharMark(seqIdx);
        List<String> rowMarks = logic.getNonogramSolutionBoardWithMarks().get(rowIdx);

        for (int colIdx = startColumnIdx; colIdx <= endColumnIdx; colIdx++) {
            String currentMark = rowMarks.get(colIdx);

            if (currentMark.startsWith(EMPTY_PART_MARKED_BOARD)) {
                markAndColourFieldInRow(logic, rowIdx, colIdx, currentMark, marker);
            } else if (SHOW_REPETITIONS) {
                log.warn("Row field was coloured before!");
            }
        }
    }

    /**
     * <p>
     * Updates a field in the row with the appropriate mark and colours it if not already coloured.
     * </p>
     *
     * <p>
     * Steps:
     * <ol>
     *     <li>Check if the field in the solution board is uncoloured (value equals {@code EMPTY_FIELD_MARKED_BOARD}).</li>
     *     <li>If so, replace it with {@code COLOURED_FIELD_MARKED_BOARD}.</li>
     *     <li>Update the field's mark in the board with marks by prepending {@code "R"} and the sequence marker.</li>
     *     <li>Increment the step counter in the logic state to reflect the modification.</li>
     * </ol>
     * </p>
     *
     * @param logic the current nonogram logic instance
     * @param rowIdx the row index of the field to be coloured and marked
     * @param colIdx the column index of the field to be coloured and marked
     * @param currentMarker the current marker string at that position
     * @param marker the new marker to apply (e.g. "a", "b", etc.)
     */
    private void markAndColourFieldInRow(NonogramLogic logic, int rowIdx, int colIdx, String currentMarker, String marker) {
        logic.getNonogramSolutionBoardWithMarks()
                .get(rowIdx)
                .set(colIdx, "R" + marker + currentMarker.substring(2, 4));

        List<String> boardRow = logic.getNonogramSolutionBoard().get(rowIdx);
        if (boardRow.get(colIdx).equals(EMPTY_FIELD_MARKED_BOARD)) {
            boardRow.set(colIdx, COLOURED_FIELD_MARKED_BOARD);
        }

        logic.getNonogramState().increaseMadeSteps();
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
        List<Integer> rowSequencesLengths = nonogramLogicObject.getNonogramRules().getRowSequencesLengths().get(rowIdx);
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

        int columnIdx = 0;
        while (columnIdx < boardRow.size()) {
            if (!boardRow.get(columnIdx).equals(COLOURED_FIELD)) {
                columnIdx++;
                continue;
            }

            colouredSequenceIndexes = new ArrayList<>();
            colouredSequenceIndexes.add(columnIdx);

            // collect indexes of current coloured sequence
            while (columnIdx < boardRow.size() && boardRow.get(columnIdx).equals(COLOURED_FIELD)) {
                columnIdx++;
            }

            colouredSequenceIndexes.add(columnIdx - 1);

            firstSequenceIndex = colouredSequenceIndexes.get(0);
            lastSequenceIndex = colouredSequenceIndexes.get(1);
            colouredSequenceLength = lastSequenceIndex - firstSequenceIndex + 1;

            matchingSequencesCount = 0;

            // check how many sequences match
            for (int seqNo = 0; seqNo < rowSequencesLengths.size(); seqNo++) {
                rowSequenceRange = rowSequencesRanges.get(seqNo);

                if (rangeInsideAnotherRange(colouredSequenceIndexes, rowSequenceRange)
                        && colouredSequenceLength <= rowSequencesLengths.get(seqNo)) {
                    matchingSequencesCount++;
                    lastMatchingSequenceIndex = seqNo;
                }
            }

            if (matchingSequencesCount == 1) {
                matchingSequenceLength = rowSequencesLengths.get(lastMatchingSequenceIndex);
                sequenceEqualsRowSequenceLength = colouredSequenceLength == matchingSequenceLength;

                if (sequenceEqualsRowSequenceLength) {
                    rowSequenceRange = List.of(firstSequenceIndex, lastSequenceIndex);
                } else {
                    oldRangeBeginIndex = rowSequencesRanges.get(lastMatchingSequenceIndex).get(0);
                    oldRangeEndIndex = rowSequencesRanges.get(lastMatchingSequenceIndex).get(1);

                    updatedRangeBeginIndex = Math.max(0,
                            nonogramLogicObject.getNonogramRowLogic().minimumColumnIndexWithoutX(rowIdx, lastSequenceIndex, matchingSequenceLength));
                    updatedRangeEndIndex = Math.min(nonogramLogicObject.getNonogramRules().getWidth() - 1,
                            nonogramLogicObject.getNonogramRowLogic().maximumColumnIndexWithoutX(rowIdx, firstSequenceIndex, matchingSequenceLength));

                    rowSequenceRange = List.of(
                            Math.max(oldRangeBeginIndex, updatedRangeBeginIndex),
                            Math.min(oldRangeEndIndex, updatedRangeEndIndex)
                    );
                }

                nonogramLogicObject.getNonogramRowLogic().updateRowSequenceRange(rowIdx, lastMatchingSequenceIndex, rowSequenceRange);

                sequenceMarker = indexToSequenceCharMark(lastMatchingSequenceIndex);
                for (int sequenceColumnIdx = firstSequenceIndex; sequenceColumnIdx <= lastSequenceIndex; sequenceColumnIdx++) {
                    String markCell = nonogramLogicObject.getNonogramSolutionBoardWithMarks().get(rowIdx).get(sequenceColumnIdx);
                    if (markCell.startsWith(EMPTY_PART_MARKED_BOARD)) {
                        markRowBoardField(nonogramLogicObject.getNonogramSolutionBoardWithMarks(), rowIdx, sequenceColumnIdx, sequenceMarker);
                        nonogramLogicObject.getNonogramState().increaseMadeSteps();
                    } else if (SHOW_REPETITIONS) {
                        log.warn("Row field was marked before.");
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
        List<Integer> columnSequencesLengths = nonogramLogicObject.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = nonogramLogicObject.getColumnsSequencesRanges().get(columnIdx);

        int rowIdx = 0;

        while (rowIdx < nonogramSolutionBoard.size()) {
            if (!nonogramSolutionBoard.get(rowIdx).get(columnIdx).equals(COLOURED_FIELD)) {
                rowIdx++;
                continue;
            }

            List<Integer> colouredSequenceIndexes = new ArrayList<>();
            colouredSequenceIndexes.add(rowIdx);

            while (rowIdx < nonogramSolutionBoard.size() &&
                    nonogramSolutionBoard.get(rowIdx).get(columnIdx).equals(COLOURED_FIELD)) {
                rowIdx++;
            }

            colouredSequenceIndexes.add(rowIdx - 1);

            int firstSequenceIndex = colouredSequenceIndexes.get(0);
            int lastSequenceIndex = colouredSequenceIndexes.get(1);
            int colouredSequenceLength = lastSequenceIndex - firstSequenceIndex + 1;

            int matchingSequencesCount = 0;
            int lastMatchingSequenceIndex = -1;

            for (int seqNo = 0; seqNo < columnSequencesLengths.size(); seqNo++) {
                List<Integer> columnSequenceRange = columnSequencesRanges.get(seqNo);

                if (rangeInsideAnotherRange(colouredSequenceIndexes, columnSequenceRange) &&
                        colouredSequenceLength <= columnSequencesLengths.get(seqNo)) {
                    matchingSequencesCount++;
                    lastMatchingSequenceIndex = seqNo;
                }
            }

            //NOTE!!! if matching count == 0 and there are sequences that are not fulfilled -> wrong solution
            if (matchingSequencesCount == 1) {
                int matchingSequenceLength = columnSequencesLengths.get(lastMatchingSequenceIndex);
                boolean sequenceEqualsColumnSequenceLength = colouredSequenceLength == matchingSequenceLength;

                List<Integer> columnSequenceRange;

                if (sequenceEqualsColumnSequenceLength) {
                    columnSequenceRange = List.of(firstSequenceIndex, lastSequenceIndex);
                } else {
                    int oldRangeBeginIndex = columnSequencesRanges.get(lastMatchingSequenceIndex).get(0);
                    int oldRangeEndIndex = columnSequencesRanges.get(lastMatchingSequenceIndex).get(1);

                    int updatedRangeBeginIndex = Math.max(0,
                            nonogramLogicObject.getNonogramColumnLogic().minimumRowIndexWithoutX(columnIdx, lastSequenceIndex, matchingSequenceLength));
                    int updatedRangeEndIndex = Math.min(
                            nonogramLogicObject.getNonogramRules().getHeight() - 1,
                            nonogramLogicObject.getNonogramColumnLogic().maximumRowIndexWithoutX(columnIdx, firstSequenceIndex, matchingSequenceLength)
                    );

                    columnSequenceRange = List.of(
                            Math.max(oldRangeBeginIndex, updatedRangeBeginIndex),
                            Math.min(oldRangeEndIndex, updatedRangeEndIndex)
                    );
                }

                nonogramLogicObject.getNonogramColumnLogic().updateColumnSequenceRange(columnIdx, lastMatchingSequenceIndex, columnSequenceRange);
                nonogramLogicObject.copyLogicFromNonogramColumnLogic();

                String sequenceMarker = indexToSequenceCharMark(lastMatchingSequenceIndex);
                for (int sequenceRowIdx = firstSequenceIndex; sequenceRowIdx <= lastSequenceIndex; sequenceRowIdx++) {
                    String markCell = nonogramSolutionBoardWithMarks.get(sequenceRowIdx).get(columnIdx);
                    if (markCell.substring(2).equals(EMPTY_PART_MARKED_BOARD)) {
                        markColumnBoardField(nonogramSolutionBoardWithMarks, sequenceRowIdx, columnIdx, sequenceMarker);
                        nonogramLogicObject.copyLogicFromNonogramColumnLogic();
                        nonogramLogicObject.getNonogramState().increaseMadeSteps();
                    } else if (SHOW_REPETITIONS) {
                        log.warn("Column field was marked before.");
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
        NonogramRowLogic rowLogic = nonogramLogicObject.getNonogramRowLogic();
        List<List<Integer>> rowSequencesRanges = rowLogic.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths = rowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        int width = rowLogic.getNonogramRules().getWidth();

        int columnIdx = 0;

        while (columnIdx < width) {
            Field field = new Field(rowIdx, columnIdx);

            if (!isFieldColoured(rowLogic.getNonogramSolutionBoard(), field)) {
                columnIdx++;
                continue;
            }

            List<Integer> colouredSequenceRange = new ArrayList<>();
            colouredSequenceRange.add(columnIdx);

            // znajdź koniec kolorowanej sekwencji
            while (columnIdx < width && isFieldColoured(rowLogic.getNonogramSolutionBoard(), new Field(rowIdx, columnIdx))) {
                columnIdx++;
            }
            colouredSequenceRange.add(columnIdx - 1);

            int sequenceOnBoardLength = rangeLength(colouredSequenceRange);

            List<Integer> matchingSeqIndexes = new ArrayList<>();
            List<Integer> matchingSeqLengths = new ArrayList<>();

            for (int seqNo = 0; seqNo < rowSequencesRanges.size(); seqNo++) {
                List<Integer> range = rowSequencesRanges.get(seqNo);
                if (rangeInsideAnotherRange(colouredSequenceRange, range)) {
                    matchingSeqIndexes.add(seqNo);
                    matchingSeqLengths.add(rowSequencesLengths.get(seqNo));
                }
            }

            int firstXCol = colouredSequenceRange.get(0) - 1;
            int lastXCol = colouredSequenceRange.get(1) + 1;

            if (matchingSeqIndexes.size() == 1) {
                int seqIdx = matchingSeqIndexes.get(0);
                int seqLen = matchingSeqLengths.get(0);

                if (!rowLogic.getRowsSequencesIdsNotToInclude().get(rowIdx).contains(seqIdx) &&
                        sequenceOnBoardLength == seqLen) {

                    if (firstXCol >= 0) {
                        Field xLeft = new Field(rowIdx, firstXCol);
                        if (isFieldEmpty(rowLogic.getNonogramSolutionBoard(), xLeft)) {
                            rowLogic.getRowXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(xLeft);
                            rowLogic.getNonogramFieldExclusionHelper().excludeFieldInRow(xLeft);
                            rowLogic.getNonogramState().increaseMadeSteps();
                        } else if (SHOW_REPETITIONS) {
                            log.warn("Longest sequence in row firstXColumnIndex added earlier!");
                        }
                    }

                    if ((firstXCol >= 0 && isFieldEmpty(rowLogic.getNonogramSolutionBoard(), new Field(rowIdx, firstXCol)))
                            || (lastXCol < width && rowLogic.getNonogramSolutionBoard().get(rowIdx).get(lastXCol).equals(EMPTY_FIELD))) {
                        Field xLeft = new Field(rowIdx, firstXCol);
                        Field xRight = new Field(rowIdx, lastXCol);
                        List<Field> fields = List.of(xLeft, xRight);

                        rowLogic.getRowXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenFields(fields);
                        rowLogic.excludeSequenceInRow(rowIdx, seqIdx);
                        rowLogic.excludeFieldsInRow(fields);
                        rowLogic.updateRowSequenceRange(rowIdx, seqIdx, colouredSequenceRange);
                        nonogramLogicObject.copyLogicFromNonogramRowLogic();
                        nonogramLogicObject.getNonogramState().increaseMadeSteps();
                    } else if (SHOW_REPETITIONS) {
                        log.warn("Placed Xs around longest sequence in row before!");
                    }
                }
            } else if (!matchingSeqLengths.isEmpty() &&
                    sequenceOnBoardLength == Collections.max(matchingSeqLengths)) {

                Field xLeft = new Field(rowIdx, firstXCol);
                if (isFieldEmpty(rowLogic.getNonogramSolutionBoard(), xLeft)) {
                    rowLogic.getRowXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(xLeft);
                    rowLogic.getNonogramFieldExclusionHelper().excludeFieldInRow(xLeft);
                    rowLogic.getNonogramState().increaseMadeSteps();
                } else if (SHOW_REPETITIONS) {
                    log.warn("Longest sequence in row firstXIndex added before!");
                }

                Field xRight = new Field(rowIdx, lastXCol);
                if (isFieldEmpty(rowLogic.getNonogramSolutionBoard(), xRight)) {
                    rowLogic.getRowXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(xRight);
                    rowLogic.getNonogramFieldExclusionHelper().excludeFieldInRow(xRight);
                    nonogramLogicObject.copyLogicFromNonogramRowLogic();
                    nonogramLogicObject.getNonogramState().increaseMadeSteps();
                } else if (SHOW_REPETITIONS) {
                    log.warn("Longest sequence in row lastXIndex added before!");
                }
            }
        }

        return nonogramLogicObject;
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
        NonogramLogic logic = nonogramLogicObject;

        List<List<Integer>> columnSequencesRanges = logic.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequencesLengths = logic.getNonogramRules().getRowSequencesLengths().get(columnIdx);
        int height = logic.getNonogramRules().getHeight();

        int rowIdx = 0;

        while (rowIdx < height) {
            if (!logic.getNonogramSolutionBoard().get(rowIdx).get(columnIdx).equals(COLOURED_FIELD)) {
                rowIdx++;
                continue;
            }

            List<Integer> colouredRange = new ArrayList<>();
            colouredRange.add(rowIdx);

            while (rowIdx < height && logic.getNonogramSolutionBoard().get(rowIdx).get(columnIdx).equals(COLOURED_FIELD)) {
                rowIdx++;
            }

            colouredRange.add(rowIdx - 1);
            int sequenceLength = rangeLength(colouredRange);
            int firstX = colouredRange.get(0) - 1;
            int lastX = colouredRange.get(1) + 1;

            List<Integer> matchingSeqIndexes = new ArrayList<>();
            List<Integer> matchingSeqLengths = new ArrayList<>();

            for (int seqNo = 0; seqNo < columnSequencesRanges.size(); seqNo++) {
                List<Integer> range = columnSequencesRanges.get(seqNo);
                if (rangeInsideAnotherRange(colouredRange, range)) {
                    matchingSeqIndexes.add(seqNo);
                    matchingSeqLengths.add(columnSequencesLengths.get(seqNo));
                }
            }

            if (matchingSeqIndexes.size() == 1) {
                int seqIdx = matchingSeqIndexes.get(0);
                int seqLen = matchingSeqLengths.get(0);

                if (sequenceLength == seqLen && !logic.getColumnsSequencesIdsNotToInclude().get(columnIdx).contains(seqIdx)) {
                    logic.getNonogramColumnLogic().excludeSequenceInColumn(columnIdx, seqIdx);
                    logic.copyLogicToNonogramColumnLogic();

                    if (firstX >= 0 && logic.getNonogramSolutionBoard().get(firstX).get(columnIdx).equals(EMPTY_FIELD)) {
                        Field f = new Field(firstX, columnIdx);
                        logic = logic.placeXAtGivenPosition(f).addColumnFieldToExcluded(f);
                        logic.getNonogramState().increaseMadeSteps();
                    } else if (SHOW_REPETITIONS && firstX >= 0) {
                        log.warn("Longest sequence in column firstXIndex added before!");
                    }

                    if (lastX < height && logic.getNonogramSolutionBoard().get(lastX).get(columnIdx).equals(EMPTY_FIELD)) {
                        Field f = new Field(lastX, columnIdx);
                        logic = logic.placeXAtGivenPosition(f).addColumnFieldToExcluded(f);
                        logic.getNonogramState().increaseMadeSteps();
                    } else if (SHOW_REPETITIONS && lastX < height) {
                        log.warn("Longest sequence in column lastXIndex added before!");
                    }

                    for (int i = firstX + 1; i < lastX; i++) {
                        if (!logic.getColumnsFieldsNotToInclude().get(columnIdx).contains(i)) {
                            Field f = new Field(i, columnIdx);
                            logic = logic.addColumnFieldToExcluded(f);
                            logic.getNonogramState().increaseMadeSteps();
                        } else if (SHOW_REPETITIONS) {
                            log.warn("Field not to include in column has been inserted before");
                        }
                    }
                }
            } else if (!matchingSeqLengths.isEmpty() && sequenceLength == Collections.max(matchingSeqLengths)) {
                if (firstX >= 0 && logic.getNonogramSolutionBoard().get(firstX).get(columnIdx).equals(EMPTY_FIELD)) {
                    Field f = new Field(firstX, columnIdx);
                    logic = logic.placeXAtGivenPosition(f).addColumnFieldToExcluded(f);
                    logic.getNonogramState().increaseMadeSteps();
                } else if (SHOW_REPETITIONS && firstX >= 0) {
                    log.warn("Sequence with maximum length in area firstXIndex placed before!");
                }

                if (lastX < height && logic.getNonogramSolutionBoard().get(lastX).get(columnIdx).equals(EMPTY_FIELD)) {
                    Field f = new Field(lastX, columnIdx);
                    logic = logic.placeXAtGivenPosition(f).addColumnFieldToExcluded(f);
                    logic.getNonogramState().increaseMadeSteps();
                } else if (SHOW_REPETITIONS && lastX < height) {
                    log.warn("Sequence with maximum length in area lastXIndex placed before!");
                }
            }
        }

        return logic;
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
        int width = nonogramLogicDataToChange.getNonogramRules().getWidth();
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
                            //.addRowFieldToExcluded(fieldToExclude); TODO - update after any place X in column action
                            .addColumnFieldToExcluded(fieldToExclude);
                    nonogramLogicObject.getNonogramState().increaseMadeSteps();
                } else if (SHOW_REPETITIONS) {
                    log.warn("X at unreachable field in row placed before!");
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
        int height = nonogramLogicDataToChange.getNonogramRules().getHeight();
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
                            //.addRowFieldToExcluded(fieldToExclude) // TODO - update after any place X in column action
                            .addColumnFieldToExcluded(fieldToExclude);
                    nonogramLogicObject.getNonogramState().increaseMadeSteps();
                } else if (SHOW_REPETITIONS) {
                    log.warn("X at unreachable field in column placed before!");
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
        List<Integer> rowSequencesLengths = nonogramLogicObject.getNonogramRules().getRowSequencesLengths().get(rowIdx);
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
            int currentSequenceLengthPlusX = (rowSequencesLengths.get(sequenceIdx) + 1);
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
            int currentSequenceLengthPlusX = (rowSequencesLengths.get(sequenceIdx) + 1);

            updatedPreviousSequenceRange = Arrays.asList(rowSequencesRanges.get(sequenceIdx - 1).get(0),
                    Math.min( oldPreviousSequenceRangeEnd, currentSequenceRangeEnd - currentSequenceLengthPlusX));

            nonogramLogicObject.getRowsSequencesRanges().get(rowIdx).set(sequenceIdx - 1, updatedPreviousSequenceRange);
        }

        //for last sequence in row
        int width = nonogramLogicObject.getNonogramRules().getWidth();
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

    public NonogramLogic correctRowSequencesWhenMetColouredFieldFromLeft(NonogramLogic nonogramLogicObject, int rowIdx) {
        List<List<Integer>> rowSequencesRanges = nonogramLogicObject.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths = nonogramLogicObject.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<String> solutionBoardRow = nonogramLogicObject.getNonogramSolutionBoard().get(rowIdx);
        int width = nonogramLogicObject.getNonogramRules().getWidth();

        int sequenceId = 0;
        int sequenceLength = rowSequencesLengths.get(sequenceId);
        int columnIdx = 0;

        while (columnIdx < width && sequenceId < rowSequencesLengths.size()) {
            if (solutionBoardRow.get(columnIdx).equals(COLOURED_FIELD_MARKED_BOARD)) {
                int rangeStart = rowSequencesRanges.get(sequenceId).get(0);
                int rangeEnd = rowSequencesRanges.get(sequenceId).get(1);
                int maxPossibleEnd = columnIdx + sequenceLength - 1;

                List<Integer> updatedRange = Arrays.asList(rangeStart, Math.min(rangeEnd, maxPossibleEnd));
                rowSequencesRanges.set(sequenceId, updatedRange);

                columnIdx += sequenceLength;
                sequenceId++;

                if (sequenceId < rowSequencesLengths.size()) {
                    sequenceLength = rowSequencesLengths.get(sequenceId);
                }
            } else {
                columnIdx++;
            }
        }

        return nonogramLogicObject;
    }

    public NonogramLogic correctRowSequencesWhenMetColouredFieldFromRight(NonogramLogic nonogramLogicObject, int rowIdx) {
        List<List<Integer>> rowSequencesRanges = nonogramLogicObject.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths = nonogramLogicObject.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<String> solutionBoardRow = nonogramLogicObject.getNonogramSolutionBoard().get(rowIdx);
        int width = nonogramLogicObject.getNonogramRules().getWidth();

        int sequenceId = rowSequencesLengths.size() - 1;
        int sequenceLength = rowSequencesLengths.get(sequenceId);
        int columnIdx = width - 1;

        while (columnIdx >= 0 && sequenceId >= 0) {
            if (solutionBoardRow.get(columnIdx).equals(COLOURED_FIELD_MARKED_BOARD)) {
                int minPossibleStart = columnIdx - sequenceLength + 1;
                List<Integer> currentRange = rowSequencesRanges.get(sequenceId);

                List<Integer> updatedRange = Arrays.asList(
                        Math.max(minPossibleStart, currentRange.get(0)),
                        currentRange.get(1)
                );

                rowSequencesRanges.set(sequenceId, updatedRange);

                columnIdx -= sequenceLength;
                sequenceId--;

                if (sequenceId >= 0) {
                    sequenceLength = rowSequencesLengths.get(sequenceId);
                }
            } else {
                columnIdx--;
            }
        }

        return nonogramLogicObject;
    }

    public NonogramLogic changeRowRangeIndexesIfXOnWay (NonogramLogic nonogramLogicObject, int rowIdx) {
        List<Integer> rowSequences = nonogramLogicObject.getNonogramRules().getRowSequencesLengths().get(rowIdx);
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
                        if (nonogramBoard.get(rowIdx).get(columnIdx).equals(X_FIELD.repeat(4))) {
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
                        if (nonogramBoard.get(rowIdx).get(columnIdx).equals(X_FIELD.repeat(4))) {
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

        List<Integer> columnSequencesLengths = nonogramLogicObject.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
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
            int currentSequenceLengthPlusX = (columnSequencesLengths.get(sequenceIdx) + 1);
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
            int currentSequenceLengthPlusX = (columnSequencesLengths.get(sequenceIdx) + 1);
            int possibleLowerPreviousSequenceRangeEnd = currentSequenceRangeEnd - currentSequenceLengthPlusX;

            updatedPreviousSequenceRange = Arrays.asList(columnSequencesRanges.get(sequenceIdx - 1).get(0),
                    Math.min( oldPreviousSequenceRangeEnd, possibleLowerPreviousSequenceRangeEnd));

            if (!columnIndexesNotToInclude.contains(columnIdx)) {
                nonogramLogicObject.getColumnsSequencesRanges().get(columnIdx).set(sequenceIdx - 1, updatedPreviousSequenceRange);
            }
        }

        //for last sequence in column
        int height = nonogramLogicObject.getNonogramRules().getHeight();
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
        List<Integer> columnSequencesLengths = nonogramLogicObject.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
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
                columnSequenceLength = columnSequencesLengths.get(seqNo);
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

    public NonogramLogic correctColumnSequencesWhenMetColouredField(NonogramLogic nonogramLogicObject, int columnIdx) {
        List<List<Integer>> columnSequencesRanges = nonogramLogicObject.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequencesLengths = nonogramLogicObject.getNonogramRules().getRowSequencesLengths().get(columnIdx);
        int height = nonogramLogicObject.getNonogramRules().getHeight();

        List<String> solutionBoardColumn = nonogramLogicObject.getNonogramBoardColumn(columnIdx);

        int sequenceId = 0;
        int sequenceLength = columnSequencesLengths.get(sequenceId);
        int rowIdx = 0;

        while (rowIdx < height && sequenceId < columnSequencesLengths.size()) {
            if (solutionBoardColumn.get(rowIdx).equals(COLOURED_FIELD_MARKED_BOARD)) {
                int currentStart = columnSequencesRanges.get(sequenceId).get(0);
                int currentEnd = columnSequencesRanges.get(sequenceId).get(1);
                int maxPossibleEnd = rowIdx + sequenceLength - 1;

                List<Integer> updatedRange = Arrays.asList(currentStart, Math.min(currentEnd, maxPossibleEnd));
                columnSequencesRanges.set(sequenceId, updatedRange);

                rowIdx += sequenceLength;
                sequenceId++;

                if (sequenceId < columnSequencesLengths.size()) {
                    sequenceLength = columnSequencesLengths.get(sequenceId);
                }
            } else {
                rowIdx++;
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

    public NonogramLogic runSolverWithCorrectnessCheck(NonogramLogic logic, String fileName) {
        log.info("Running heuristic solver with correctness check...");

        NonogramSolver solver = new NonogramSolver(logic, fileName, GuessMode.DISABLED, logicFactory);
        log.info("Initialized NonogramSolver: {}", solver);

        NonogramSolutionNode rootNode = new NonogramSolutionNode(logic, logicFactory);
        log.info("Initialized NonogramSolutionNode (decisions: {})", rootNode.getNonogramGuessDecisions().size());

        NonogramLogic solvedLogic = solver.runSolutionAtNode(rootNode);

        if (solvedLogic.isSolved()) {
            try {
                NonogramSolutionSaveRequest request = NonogramMapper.toSaveRequest(solvedLogic, fileName);
                saveIfCorrect(request);
                log.info("Solved nonogram was successfully saved.");
            } catch (IOException e) {
                log.error("Failed to save solved nonogram: {}", e.getMessage());
            }
        }

        return solvedLogic;
    }

    public FinalNonogramSolutionDTO saveIfCorrect(NonogramSolutionSaveRequest request) throws IOException {
        return nonogramSolutionSaver.saveIfCorrect(request);
    }
}
