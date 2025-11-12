package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.mixed;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.colouring.NonogramFieldColouringHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.xplacement.NonogramFieldPlacingXHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.NonogramColumnLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.RefreshableColumnHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.ColouringGenerateLogBaseContext;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed.PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed.PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement.PlaceXGenerateLogBaseContext;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement.PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.rangeLength;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.util.NonogramParametersComparatorHelper.rangesNotEqual;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.mixed.ColumnOverextensionPrevention.*;

public class ColumnMixedActionsHelperImpl implements ColumnMixedActionsHelper, RefreshableColumnHelper {

    private final NonogramColumnLogic nonogramColumnLogic;

    private final NonogramFieldColouringHelper colouringHelper;

    private final NonogramFieldPlacingXHelper fieldPlacingXHelper;

    public final static boolean IS_ROW = false;

    public ColumnMixedActionsHelperImpl(NonogramColumnLogic nonogramColumnLogic) {
        this.nonogramColumnLogic = nonogramColumnLogic;
        this.colouringHelper = new NonogramFieldColouringHelper(
                this.nonogramColumnLogic.getNonogramSolutionBoard(),
                this.nonogramColumnLogic.getNonogramSolutionBoardWithMarks(),
                this.nonogramColumnLogic.getBoardAccessHelper()
        );
        this.fieldPlacingXHelper = new NonogramFieldPlacingXHelper(
                this.nonogramColumnLogic.getNonogramSolutionBoard(),
                this.nonogramColumnLogic.getNonogramSolutionBoardWithMarks(),
                this.nonogramColumnLogic.getBoardAccessHelper()
        );
    }

    @Override
    public void preventExtendingColouredSequenceToExcessLengthInColumn(int columnIdx) {
        preventExtendingColouredSequenceToExcessLengthInColumnToTop(columnIdx);
        preventExtendingColouredSequenceToExcessLengthInColumnToBottom(columnIdx);
    }

    private void preventExtendingColouredSequenceToExcessLengthInColumnToTop(int columnIdx) {
        List<String> initialColumn = nonogramColumnLogic.getColumnCopy(columnIdx);
        List<List<Integer>> initialSequencesRanges = getColumnSequencesRangesCopy(columnIdx);

        List<Integer> columnSequencesLengths = nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx);

        for (int rowIdx = nonogramColumnLogic.getNonogramRules().getWidth() - 1; rowIdx > 0; rowIdx--) {
            Field fieldToCheckX = new Field(rowIdx, columnIdx);
            if (!isFieldWithX(nonogramColumnLogic.getNonogramSolutionBoard(), fieldToCheckX)) continue;

            int potentiallyColouredFieldRow = fieldToCheckX.getRowIdx() - 1;
            Field fieldToCheckColoured = new Field(potentiallyColouredFieldRow, fieldToCheckX.getColumnIdx());

            if (!isFieldColoured(nonogramColumnLogic.getNonogramSolutionBoard(), fieldToCheckColoured)) continue;

            ColumnOverextensionContext context = analyzeTopOverextensionCandidatesInColumn(
                    columnIdx, potentiallyColouredFieldRow, columnSequencesLengths, columnSequencesRanges
            );

            if (context == null || context.validSequenceLengths().isEmpty()) continue;

            handleValidTopOverextensionCase(context, initialColumn, columnSequencesRanges, columnSequencesLengths);
            updateSequenceRangeIfNeededTop(context, columnIdx, initialColumn, initialSequencesRanges);
        }
    }

    private List<List<Integer>> getColumnSequencesRangesCopy(int columnIdx) {
        return nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx).stream()
                .map(ArrayList::new)
                .collect(Collectors.toList());
    }

    private record ColumnOverextensionContext(
            int columnIdx,
            int potentiallyColouredFieldRow,
            List<Integer> validSequenceIds,
            List<Integer> validSequenceLengths,
            List<Integer> sequencesLengths,
            List<List<Integer>> columnSequencesRanges
    ) {}

    private ColumnOverextensionContext analyzeTopOverextensionCandidatesInColumn(
            int columnIdx,
            int potentiallyColouredFieldRow,
            List<Integer> columnSequencesLengths,
            List<List<Integer>> columnSequencesRanges
    ) {
        List<Integer> sequencesIds = sequencesIdsInColumnIncludingField(
                columnSequencesRanges, new Field(potentiallyColouredFieldRow, columnIdx)
        );
        if (sequencesIds.isEmpty()) return null;

        List<Integer> sequencesLengths = sequencesIds.stream().map(columnSequencesLengths::get).toList();
        int maxSequenceLength = Collections.max(sequencesLengths);

        List<List<Integer>> colouredSequences = getColouredSequencesRangesInColumnInRangeToTop(
                        nonogramColumnLogic.getNonogramSolutionBoard(),
                        columnIdx,
                        potentiallyColouredFieldRow,
                        maxSequenceLength);

        List<Integer> validSequenceIds = findValidSequencesIdsMergingToTop(
                sequencesIds, sequencesLengths, potentiallyColouredFieldRow, colouredSequences
        );

        List<Integer> validSequenceLengths = validSequenceIds.stream()
                .map(sequencesIds::indexOf)
                .map(sequencesLengths::get)
                .toList();

        return new ColumnOverextensionContext(columnIdx, potentiallyColouredFieldRow,
                validSequenceIds, validSequenceLengths, sequencesLengths, columnSequencesRanges);
    }

    private void handleValidTopOverextensionCase(
            ColumnOverextensionContext context,
            List<String> initialColumn,
            List<List<Integer>> columnSequencesRanges,
            List<Integer> columnSequencesLengths
    ) {
        if (context.validSequenceLengths().stream().distinct().count() != 1) return;

        int sequenceLength = context.validSequenceLengths().get(0);
        int colouredSequenceRowStartIdx = context.potentiallyColouredFieldRow() - sequenceLength + 1;

        for (int rowIdx = colouredSequenceRowStartIdx; rowIdx <= context.potentiallyColouredFieldRow(); rowIdx++) {
            processFieldToColourWhenPreventExtendingSequenceToExcessLength(
                    context,
                    rowIdx,
                    initialColumn,
                    columnSequencesRanges,
                    columnSequencesLengths,
                    "top"
            );
        }

        Field fieldToPlaceX = new Field(colouredSequenceRowStartIdx - 1, context.columnIdx());
        if (fieldToPlaceX.getColumnIdx() >= 0 && isFieldEmpty(nonogramColumnLogic.getNonogramSolutionBoard(), fieldToPlaceX)) {
            nonogramColumnLogic.getColumnXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(fieldToPlaceX);
            nonogramColumnLogic.getNonogramState().increaseMadeSteps();

            PlaceXGenerateLogBaseContext placeXGenerateLogBaseContext = new PlaceXGenerateLogBaseContext(
                    IS_ROW,
                    context.columnIdx(),
                    initialColumn,
                    nonogramColumnLogic.getColumnCopy(context.columnIdx())
            );
            String tmpLog = PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper.generateLog(
                    placeXGenerateLogBaseContext,
                    "top",
                    context.validSequenceIds.get(0),
                    columnSequencesLengths,
                    columnSequencesRanges
            );
            nonogramColumnLogic.setTmpLog(tmpLog);
            nonogramColumnLogic.addLog();

            nonogramColumnLogic.getNonogramFieldExclusionHelper().excludeFieldInColumn(fieldToPlaceX);
            nonogramColumnLogic.getActionScheduler().scheduleActionsBasedOnField(fieldToPlaceX,
                    NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART_IN_COLUMN);
        }
    }

    private void updateSequenceRangeIfNeededTop(
            ColumnOverextensionContext context,
            int columnIdx,
            List<String> initialColumn,
            List<List<Integer>> initialSequencesRanges
    ) {
        if (context.validSequenceIds().size() != 1) return;

        int seqId = context.validSequenceIds().get(0);
        int len   = context.validSequenceLengths().get(0);
        int end   = context.potentiallyColouredFieldRow();
        int start = end - len + 1;

        applyColumnRangeUpdate(columnIdx, seqId, start, end, context, initialColumn, initialSequencesRanges);
    }

    private void preventExtendingColouredSequenceToExcessLengthInColumnToBottom(int columnIdx) {
        List<String> initialColumn = nonogramColumnLogic.getRowCopy(columnIdx);
        List<List<Integer>> initialSequencesRanges = getColumnSequencesRangesCopy(columnIdx);
        List<Integer> columnSequencesLengths = nonogramColumnLogic.getNonogramRules().getRowSequencesLengths().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx);

        for (int rowIdx = 0; rowIdx < nonogramColumnLogic.getNonogramRules().getHeight() - 1; rowIdx++) {
            Field fieldToCheckX = new Field(rowIdx, rowIdx);
            if (!isFieldWithX(nonogramColumnLogic.getNonogramSolutionBoard(), fieldToCheckX)) continue;

            int potentiallyColouredFieldRow = fieldToCheckX.getRowIdx() + 1;
            Field fieldToCheckColoured = new Field(fieldToCheckX.getRowIdx(), potentiallyColouredFieldRow);
            if (!isFieldColoured(nonogramColumnLogic.getNonogramSolutionBoard(), fieldToCheckColoured)) continue;

            // --- step 1: analyze potential overextension candidates ---
            ColumnOverextensionContext context = analyzeBottomOverextensionCandidatesInColumn(
                    columnIdx, potentiallyColouredFieldRow, columnSequencesLengths, columnSequencesRanges
            );
            if (context == null || context.validSequenceLengths().isEmpty()) continue;

            // --- step 2: handle detected case ---
            handleValidBottomOverextensionCase(context, initialColumn, columnSequencesRanges, columnSequencesLengths);

            // --- step 3: update sequence ranges ---
            updateSequenceRangeIfNeededBottom(context, columnIdx, initialColumn, initialSequencesRanges);
        }
    }

    private ColumnOverextensionContext analyzeBottomOverextensionCandidatesInColumn(
            int columnIdx,
            int potentiallyColouredFieldRow,
            List<Integer> columnSequencesLengths,
            List<List<Integer>> columnSequencesRanges
    ) {
        List<Integer> sequencesIds = sequencesIdsInColumnIncludingField(
                        columnSequencesRanges,
                        new Field(columnIdx, potentiallyColouredFieldRow)
                );
        if (sequencesIds.isEmpty()) return null;

        List<Integer> sequencesLengths = sequencesIds.stream().map(columnSequencesLengths::get).toList();
        int maxSequenceLength = Collections.max(sequencesLengths);

        List<List<Integer>> colouredSequences = ColumnOverextensionPrevention
                .getColouredSequencesRangesInColumnInRangeToBottom(
                        nonogramColumnLogic.getNonogramSolutionBoard(),
                        columnIdx,
                        potentiallyColouredFieldRow,
                        maxSequenceLength
                );

        List<Integer> validSequenceIds = ColumnOverextensionPrevention
                .findValidSequencesIdsMergingToBottom(sequencesIds, sequencesLengths, potentiallyColouredFieldRow, colouredSequences);

        List<Integer> validSequenceLengths = validSequenceIds.stream()
                .map(sequencesIds::indexOf)
                .map(sequencesLengths::get)
                .toList();

        return new ColumnOverextensionContext(columnIdx, potentiallyColouredFieldRow,
                validSequenceIds, validSequenceLengths, sequencesLengths, columnSequencesRanges);
    }

    private void handleValidBottomOverextensionCase(
            ColumnOverextensionContext context,
            List<String> initialColumn,
            List<List<Integer>> columnSequencesRanges,
            List<Integer> columnSequencesLengths
    ) {
        if (context.validSequenceLengths().stream().distinct().count() != 1) return;

        int sequenceLength = context.validSequenceLengths().get(0);
        int colouredSequenceEndRowIdx = context.potentiallyColouredFieldRow() + sequenceLength - 1;

        // Colour fields to the bottom
        for (int rowIdx = context.potentiallyColouredFieldRow(); rowIdx <= colouredSequenceEndRowIdx; rowIdx++) {
            Field f = new Field(rowIdx, context.columnIdx());
            if (isFieldEmpty(nonogramColumnLogic.getNonogramSolutionBoard(), f)) {
                processFieldToColourWhenPreventExtendingSequenceToExcessLength(
                        context,
                        rowIdx,
                        initialColumn,
                        columnSequencesRanges,
                        columnSequencesLengths,
                        "bottom"
                );
            }
        }

        // Place X after sequence
        Field fieldToPlaceX = new Field(colouredSequenceEndRowIdx + 1, context.columnIdx());
        if (fieldToPlaceX.getRowIdx() < nonogramColumnLogic.getNonogramRules().getHeight() &&
                isFieldEmpty(nonogramColumnLogic.getNonogramSolutionBoard(), fieldToPlaceX)) {

            nonogramColumnLogic.getColumnXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(fieldToPlaceX);
            nonogramColumnLogic.getNonogramState().increaseMadeSteps();

            PlaceXGenerateLogBaseContext placeXGenerateLogBaseContext = new PlaceXGenerateLogBaseContext(
                    IS_ROW,
                    context.columnIdx(),
                    initialColumn,
                    nonogramColumnLogic.getColumnCopy(context.columnIdx())
            );

            String tmpLog = PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper.generateLog(
                    placeXGenerateLogBaseContext,
                    "bottom",
                    context.validSequenceIds.get(0),
                    columnSequencesLengths,
                    columnSequencesRanges
            );
            nonogramColumnLogic.setTmpLog(tmpLog);
            nonogramColumnLogic.addLog();

            nonogramColumnLogic.getNonogramFieldExclusionHelper().excludeFieldInColumn(fieldToPlaceX);
            nonogramColumnLogic.getActionScheduler().scheduleActionsBasedOnField(fieldToPlaceX,
                    NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART_IN_COLUMN);
        }
    }

    private void updateSequenceRangeIfNeededBottom(
            ColumnOverextensionContext context,
            int columnIdx,
            List<String> initialColumn,
            List<List<Integer>> initialSequencesRanges
    ) {
        if (context.validSequenceIds().size() != 1) return;

        int seqId = context.validSequenceIds().get(0);
        int len   = context.validSequenceLengths().get(0);
        int start = context.potentiallyColouredFieldRow();
        int end   = start + len - 1;

        applyColumnRangeUpdate(columnIdx, seqId, start, end, context, initialColumn, initialSequencesRanges);
    }

    private void applyColumnRangeUpdate(
            int columnIdx,
            int seqId,
            int start,
            int end,
            ColumnOverextensionContext context,
            List<String> initialColumn,
            List<List<Integer>> initialSequencesRanges
    ) {
        List<Integer> oldRange = context.columnSequencesRanges().get(seqId);
        List<Integer> updated  = List.of(start, end);
        if (!rangesNotEqual(oldRange, updated)) return;

        nonogramColumnLogic.updateColumnSequenceRange(columnIdx, seqId, updated);
        nonogramColumnLogic.getNonogramState().increaseMadeSteps();

        if (sequenceShouldBeExcluded(columnIdx, seqId)) {
            nonogramColumnLogic.excludeSequenceInColumn(columnIdx, seqId);
            nonogramColumnLogic.getNonogramState().increaseMadeSteps();
        }

        Field rowField = new Field(0, columnIdx);
        nonogramColumnLogic.getActionScheduler().scheduleActionsBasedOnField(rowField,
                NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART_IN_COLUMN);

        String tmpLog = PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelper.generateLog(
                IS_ROW,
                columnIdx,
                context.sequencesLengths(),
                initialColumn,
                nonogramColumnLogic.getRowCopy(columnIdx),
                initialSequencesRanges,
                nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx)
        );
        nonogramColumnLogic.setTmpLog(tmpLog);
        nonogramColumnLogic.addLog();
    }

    void processFieldToColourWhenPreventExtendingSequenceToExcessLength(
            ColumnOverextensionContext context,
            int rowIdx,
            List<String> initialColumn,
            List<List<Integer>> columnSequencesRanges,
            List<Integer> columnSequencesLengths,
            String direction) {
        Field f = new Field(rowIdx, context.columnIdx());

        if (isFieldEmpty(nonogramColumnLogic.getNonogramSolutionBoard(), f)) {
            nonogramColumnLogic.getColumnColouringHelper().getColouringHelper().colourFieldAtGivenPosition(f, "--C-");
            nonogramColumnLogic.getActionScheduler().scheduleActionsBasedOnField(f, NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART_IN_COLUMN);
            nonogramColumnLogic.getNonogramState().increaseMadeSteps();

            ColouringGenerateLogBaseContext colouringGenerateLogBaseContext = new ColouringGenerateLogBaseContext(
                    IS_ROW,
                    context.columnIdx(),
                    initialColumn,
                    nonogramColumnLogic.getColumnCopy(context.columnIdx()),
                    columnSequencesRanges,
                    columnSequencesLengths
            );
            String tmpLog = PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper.generateLog(
                    colouringGenerateLogBaseContext,
                    direction
            );
            nonogramColumnLogic.setTmpLog(tmpLog);
            nonogramColumnLogic.addLog();
        }
    }

    private boolean sequenceShouldBeExcluded(int columnIdx, int sequenceIdx) {
        int sequenceLength = nonogramColumnLogic.getNonogramRules().getColumnSequencesLengths().get(columnIdx).get(sequenceIdx);
        List<Integer> sequenceRange = nonogramColumnLogic.getColumnsSequencesRanges().get(columnIdx).get(sequenceIdx);
        return rangeLength(sequenceRange) == sequenceLength
                && allFieldsAreColouredInRowRange(columnIdx, sequenceRange, nonogramColumnLogic.getNonogramSolutionBoard());
    }

    @Override
    public void refreshFrom(NonogramColumnLogic logicToCopy) {
        nonogramColumnLogic.setNonogramSolutionBoard(logicToCopy.getNonogramSolutionBoard());

        nonogramColumnLogic.setColumnsSequencesRanges(logicToCopy.getColumnsSequencesRanges());
        nonogramColumnLogic.setColumnsFieldsNotToInclude(logicToCopy.getColumnsFieldsNotToInclude());
    }
}
