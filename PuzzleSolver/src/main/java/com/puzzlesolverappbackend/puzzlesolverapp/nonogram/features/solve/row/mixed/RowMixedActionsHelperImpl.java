package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row.mixed;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.colouring.NonogramFieldColouringHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.xplacement.NonogramFieldPlacingXHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row.NonogramRowLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row.RefreshableRowHelper;
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

public class RowMixedActionsHelperImpl implements RowMixedActionsHelper, RefreshableRowHelper {

    private final NonogramRowLogic nonogramRowLogic;

    private final NonogramFieldColouringHelper colouringHelper;

    private final NonogramFieldPlacingXHelper fieldPlacingXHelper;

    public final static boolean IS_ROW = true;

    public RowMixedActionsHelperImpl(NonogramRowLogic nonogramRowLogic) {
        this.nonogramRowLogic = nonogramRowLogic;
        this.colouringHelper = new NonogramFieldColouringHelper(
                this.nonogramRowLogic.getNonogramSolutionBoard(),
                this.nonogramRowLogic.getNonogramSolutionBoardWithMarks(),
                this.nonogramRowLogic.getBoardAccessHelper()
        );
        this.fieldPlacingXHelper = new NonogramFieldPlacingXHelper(
                this.nonogramRowLogic.getNonogramSolutionBoard(),
                this.nonogramRowLogic.getNonogramSolutionBoardWithMarks(),
                this.nonogramRowLogic.getBoardAccessHelper()
        );
    }

    @Override
    public void preventExtendingColouredSequenceToExcessLengthInRow(int rowIdx) {
        preventExtendingColouredSequenceToExcessLengthInRowToLeft(rowIdx);
        preventExtendingColouredSequenceToExcessLengthInRowToRight(rowIdx);
    }

    private void preventExtendingColouredSequenceToExcessLengthInRowToLeft(int rowIdx) {
        List<String> initialRow = nonogramRowLogic.getRowCopy(rowIdx);
        List<List<Integer>> initialSequencesRanges = getRowSequencesRangesCopy(rowIdx);

        List<Integer> rowSequencesLengths = nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);

        for (int columnIdx = nonogramRowLogic.getNonogramRules().getWidth() - 1; columnIdx > 0; columnIdx--) {
            Field fieldToCheckX = new Field(rowIdx, columnIdx);
            if (!isFieldWithX(nonogramRowLogic.getNonogramSolutionBoard(), fieldToCheckX)) continue;

            int potentiallyColouredFieldColumn = fieldToCheckX.getColumnIdx() - 1;
            Field fieldToCheckColoured = new Field(fieldToCheckX.getRowIdx(), potentiallyColouredFieldColumn);

            if (!isFieldColoured(nonogramRowLogic.getNonogramSolutionBoard(), fieldToCheckColoured)) continue;

            RowOverextensionContext context = analyzeLeftOverextensionCandidatesInRow(
                    rowIdx, potentiallyColouredFieldColumn, rowSequencesLengths, rowSequencesRanges
            );

            if (context == null || context.validSequenceLengths().isEmpty()) continue;

            handleValidLeftOverextensionCase(context, initialRow, rowSequencesRanges, rowSequencesLengths);
            updateSequenceRangeIfNeededLeft(context, rowIdx, initialRow, initialSequencesRanges);
        }
    }

    private List<List<Integer>> getRowSequencesRangesCopy(int rowIdx) {
        return nonogramRowLogic.getRowsSequencesRanges().get(rowIdx).stream()
                .map(ArrayList::new)
                .collect(Collectors.toList());
    }

    private record RowOverextensionContext(
            int rowIdx,
            int potentiallyColouredFieldColumn,
            List<Integer> validSequenceIds,
            List<Integer> validSequenceLengths,
            List<Integer> sequencesLengths,
            List<List<Integer>> rowSequencesRanges
    ) {}

    private RowOverextensionContext analyzeLeftOverextensionCandidatesInRow(
            int rowIdx,
            int potentiallyColouredFieldColumn,
            List<Integer> rowSequencesLengths,
            List<List<Integer>> rowSequencesRanges
    ) {
        List<Integer> sequencesIds = RowOverextensionPrevention.sequencesIdsInRowIncludingField(
                rowSequencesRanges, new Field(rowIdx, potentiallyColouredFieldColumn)
        );
        if (sequencesIds.isEmpty()) return null;

        List<Integer> sequencesLengths = sequencesIds.stream().map(rowSequencesLengths::get).toList();
        int maxSequenceLength = Collections.max(sequencesLengths);

        List<List<Integer>> colouredSequences = RowOverextensionPrevention
                .getColouredSequencesRangesInRowInRangeOnLeft(
                        nonogramRowLogic.getNonogramSolutionBoard(),
                        rowIdx,
                        potentiallyColouredFieldColumn,
                        maxSequenceLength);

        List<Integer> validSequenceIds = RowOverextensionPrevention.findValidSequencesIdsMergingToLeft(
                sequencesIds, sequencesLengths, potentiallyColouredFieldColumn, colouredSequences
        );

        List<Integer> validSequenceLengths = validSequenceIds.stream()
                .map(sequencesIds::indexOf)
                .map(sequencesLengths::get)
                .toList();

        return new RowOverextensionContext(rowIdx, potentiallyColouredFieldColumn,
                validSequenceIds, validSequenceLengths, sequencesLengths, rowSequencesRanges);
    }

    private void handleValidLeftOverextensionCase(
            RowOverextensionContext context,
            List<String> initialRow,
            List<List<Integer>> rowSequencesRanges,
            List<Integer> rowSequencesLengths
    ) {
        if (context.validSequenceLengths().stream().distinct().count() != 1) return;

        int sequenceLength = context.validSequenceLengths().get(0);
        int colouredSequenceColStartIdx = context.potentiallyColouredFieldColumn() - sequenceLength + 1;

        for (int columnIdx = colouredSequenceColStartIdx; columnIdx <= context.potentiallyColouredFieldColumn(); columnIdx++) {
            processFieldToColourWhenPreventExtendingSequenceToExcessLength(
                    context,
                    columnIdx,
                    initialRow,
                    rowSequencesRanges,
                    rowSequencesLengths,
                    "left"
            );
        }

        Field fieldToPlaceX = new Field(context.rowIdx(), colouredSequenceColStartIdx - 1);
        if (fieldToPlaceX.getColumnIdx() >= 0 && isFieldEmpty(nonogramRowLogic.getNonogramSolutionBoard(), fieldToPlaceX)) {
            nonogramRowLogic.getRowXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(fieldToPlaceX);
            nonogramRowLogic.getNonogramState().increaseMadeSteps();

            PlaceXGenerateLogBaseContext placeXGenerateLogBaseContext = new PlaceXGenerateLogBaseContext(
                    IS_ROW,
                    context.rowIdx(),
                    initialRow,
                    nonogramRowLogic.getRowCopy(context.rowIdx())
            );
            String tmpLog = PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper.generateLog(
                    placeXGenerateLogBaseContext,
                    "left",
                    context.validSequenceIds.get(0),
                    rowSequencesLengths,
                    rowSequencesRanges
            );
            nonogramRowLogic.setTmpLog(tmpLog);
            nonogramRowLogic.addLog();

            nonogramRowLogic.getNonogramFieldExclusionHelper().excludeFieldInRow(fieldToPlaceX);
            nonogramRowLogic.getActionScheduler().scheduleActionsBasedOnField(fieldToPlaceX,
                    NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART_IN_ROW);
        }
    }

    private void updateSequenceRangeIfNeededLeft(
            RowOverextensionContext context,
            int rowIdx,
            List<String> initialRow,
            List<List<Integer>> initialSequencesRanges
    ) {
        if (context.validSequenceIds().size() != 1) return;

        int seqId = context.validSequenceIds().get(0);
        int len   = context.validSequenceLengths().get(0);
        int end   = context.potentiallyColouredFieldColumn();
        int start = end - len + 1;

        applyRowRangeUpdate(rowIdx, seqId, start, end, context, initialRow, initialSequencesRanges);
    }

    private void preventExtendingColouredSequenceToExcessLengthInRowToRight(int rowIdx) {
        List<String> initialRow = nonogramRowLogic.getRowCopy(rowIdx);
        List<List<Integer>> initialSequencesRanges = getRowSequencesRangesCopy(rowIdx);
        List<Integer> rowSequencesLengths = nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx);

        for (int columnIdx = 0; columnIdx < nonogramRowLogic.getNonogramRules().getWidth() - 1; columnIdx++) {
            Field fieldToCheckX = new Field(rowIdx, columnIdx);
            if (!isFieldWithX(nonogramRowLogic.getNonogramSolutionBoard(), fieldToCheckX)) continue;

            int potentiallyColouredFieldColumn = fieldToCheckX.getColumnIdx() + 1;
            Field fieldToCheckColoured = new Field(fieldToCheckX.getRowIdx(), potentiallyColouredFieldColumn);
            if (!isFieldColoured(nonogramRowLogic.getNonogramSolutionBoard(), fieldToCheckColoured)) continue;

            // --- step 1: analyze potential overextension candidates ---
            RowOverextensionContext context = analyzeRightOverextensionCandidatesInRow(
                    rowIdx, potentiallyColouredFieldColumn, rowSequencesLengths, rowSequencesRanges
            );
            if (context == null || context.validSequenceLengths().isEmpty()) continue;

            // --- step 2: handle detected case ---
            handleValidRightOverextensionCase(context, initialRow, rowSequencesRanges, rowSequencesLengths);

            // --- step 3: update sequence ranges ---
            updateSequenceRangeIfNeededRight(context, rowIdx, initialRow, initialSequencesRanges);
        }
    }

    private RowOverextensionContext analyzeRightOverextensionCandidatesInRow(
            int rowIdx,
            int potentiallyColouredFieldColumn,
            List<Integer> rowSequencesLengths,
            List<List<Integer>> rowSequencesRanges
    ) {
        List<Integer> sequencesIds = RowOverextensionPrevention
                .sequencesIdsInRowIncludingField(
                        rowSequencesRanges,
                        new Field(rowIdx, potentiallyColouredFieldColumn)
                );
        if (sequencesIds.isEmpty()) return null;

        List<Integer> sequencesLengths = sequencesIds.stream().map(rowSequencesLengths::get).toList();
        int maxSequenceLength = Collections.max(sequencesLengths);

        List<List<Integer>> colouredSequences = RowOverextensionPrevention
                .getColouredSequencesRangesInRowInRangeOnRight(
                        nonogramRowLogic.getNonogramSolutionBoard(),
                        rowIdx,
                        potentiallyColouredFieldColumn,
                        maxSequenceLength
                );

        List<Integer> validSequenceIds = RowOverextensionPrevention
                .findValidSequencesIdsMergingToRight(sequencesIds, sequencesLengths, potentiallyColouredFieldColumn, colouredSequences);

        List<Integer> validSequenceLengths = validSequenceIds.stream()
                .map(sequencesIds::indexOf)
                .map(sequencesLengths::get)
                .toList();

        return new RowOverextensionContext(rowIdx, potentiallyColouredFieldColumn,
                validSequenceIds, validSequenceLengths, sequencesLengths, rowSequencesRanges);
    }

    private void handleValidRightOverextensionCase(
            RowOverextensionContext context,
            List<String> initialRow,
            List<List<Integer>> rowSequencesRanges,
            List<Integer> rowSequencesLengths
    ) {
        if (context.validSequenceLengths().stream().distinct().count() != 1) return;

        int sequenceLength = context.validSequenceLengths().get(0);
        int colouredSequenceEndColumnIdx = context.potentiallyColouredFieldColumn() + sequenceLength - 1;

        // Colour fields on the right
        for (int columnIdx = context.potentiallyColouredFieldColumn(); columnIdx <= colouredSequenceEndColumnIdx; columnIdx++) {
            Field f = new Field(context.rowIdx(), columnIdx);
            if (isFieldEmpty(nonogramRowLogic.getNonogramSolutionBoard(), f)) {
                processFieldToColourWhenPreventExtendingSequenceToExcessLength(
                        context,
                        columnIdx,
                        initialRow,
                        rowSequencesRanges,
                        rowSequencesLengths,
                        "right"
                );
            }
        }

        // Place X after sequence
        Field fieldToPlaceX = new Field(context.rowIdx(), colouredSequenceEndColumnIdx + 1);
        if (fieldToPlaceX.getColumnIdx() < nonogramRowLogic.getNonogramRules().getWidth() &&
                isFieldEmpty(nonogramRowLogic.getNonogramSolutionBoard(), fieldToPlaceX)) {

            nonogramRowLogic.getRowXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(fieldToPlaceX);
            nonogramRowLogic.getNonogramState().increaseMadeSteps();

            PlaceXGenerateLogBaseContext placeXGenerateLogBaseContext = new PlaceXGenerateLogBaseContext(
                    IS_ROW,
                    context.rowIdx(),
                    initialRow,
                    nonogramRowLogic.getRowCopy(context.rowIdx())
            );

            String tmpLog = PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper.generateLog(
                    placeXGenerateLogBaseContext,
                    "right",
                    context.validSequenceIds.get(0),
                    rowSequencesLengths,
                    rowSequencesRanges
            );
            nonogramRowLogic.setTmpLog(tmpLog);
            nonogramRowLogic.addLog();

            nonogramRowLogic.getNonogramFieldExclusionHelper().excludeFieldInRow(fieldToPlaceX);
            nonogramRowLogic.getActionScheduler().scheduleActionsBasedOnField(fieldToPlaceX,
                    NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART_IN_ROW);
        }
    }

    private void updateSequenceRangeIfNeededRight(
            RowOverextensionContext context,
            int rowIdx,
            List<String> initialRow,
            List<List<Integer>> initialSequencesRanges
    ) {
        if (context.validSequenceIds().size() != 1) return;

        int seqId = context.validSequenceIds().get(0);
        int len   = context.validSequenceLengths().get(0);
        int start = context.potentiallyColouredFieldColumn();
        int end   = start + len - 1;

        applyRowRangeUpdate(rowIdx, seqId, start, end, context, initialRow, initialSequencesRanges);
    }

    private void applyRowRangeUpdate(
            int rowIdx,
            int seqId,
            int start,
            int end,
            RowOverextensionContext context,
            List<String> initialRow,
            List<List<Integer>> initialSequencesRanges
    ) {
        List<Integer> oldRange = context.rowSequencesRanges().get(seqId);
        List<Integer> updated  = List.of(start, end);
        if (!rangesNotEqual(oldRange, updated)) return;

        nonogramRowLogic.updateRowSequenceRange(rowIdx, seqId, updated);
        nonogramRowLogic.getNonogramState().increaseMadeSteps();

        if (sequenceShouldBeExcluded(rowIdx, seqId)) {
            nonogramRowLogic.excludeSequenceInRow(rowIdx, seqId);
            nonogramRowLogic.getNonogramState().increaseMadeSteps();
        }

        Field rowField = new Field(rowIdx, 0);
        nonogramRowLogic.getActionScheduler().scheduleActionsBasedOnField(rowField,
                NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART_IN_ROW);

        String tmpLog = PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelper.generateLog(
                IS_ROW,
                rowIdx,
                context.sequencesLengths(),
                initialRow,
                nonogramRowLogic.getRowCopy(rowIdx),
                initialSequencesRanges,
                nonogramRowLogic.getRowsSequencesRanges().get(rowIdx)
        );
        nonogramRowLogic.setTmpLog(tmpLog);
        nonogramRowLogic.addLog();
    }

    void processFieldToColourWhenPreventExtendingSequenceToExcessLength(
            RowOverextensionContext context,
            int columnIdx,
            List<String> initialRow,
            List<List<Integer>> rowSequencesRanges,
            List<Integer> rowSequencesLengths,
            String direction) {
        Field f = new Field(context.rowIdx(), columnIdx);

        if (isFieldEmpty(nonogramRowLogic.getNonogramSolutionBoard(), f)) {
            nonogramRowLogic.getRowColouringHelper().getColouringHelper().colourFieldAtGivenPosition(f, "R---");
            nonogramRowLogic.getActionScheduler().scheduleActionsBasedOnField(f, NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART_IN_ROW);
            nonogramRowLogic.getNonogramState().increaseMadeSteps();

            ColouringGenerateLogBaseContext colouringGenerateLogBaseContext = new ColouringGenerateLogBaseContext(
                    IS_ROW,
                    context.rowIdx(),
                    initialRow,
                    nonogramRowLogic.getRowCopy(context.rowIdx()),
                    rowSequencesRanges,
                    rowSequencesLengths
            );
            String tmpLog = PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper.generateLog(
                    colouringGenerateLogBaseContext,
                    direction
            );
            nonogramRowLogic.setTmpLog(tmpLog);
            nonogramRowLogic.addLog();
        }
    }

    private boolean sequenceShouldBeExcluded(int rowIdx, int sequenceIdx) {
        int sequenceLength = nonogramRowLogic.getNonogramRules().getRowSequencesLengths().get(rowIdx).get(sequenceIdx);
        List<Integer> sequenceRange = nonogramRowLogic.getRowsSequencesRanges().get(rowIdx).get(sequenceIdx);
        return rangeLength(sequenceRange) == sequenceLength
                && allFieldsAreColouredInColumnRange(rowIdx, sequenceRange, nonogramRowLogic.getNonogramSolutionBoard());
    }

    @Override
    public void refreshFrom(NonogramRowLogic logicToCopy) {
        nonogramRowLogic.setNonogramSolutionBoard(logicToCopy.getNonogramSolutionBoard());

        nonogramRowLogic.setRowsSequencesRanges(logicToCopy.getRowsSequencesRanges());
        nonogramRowLogic.setRowsFieldsNotToInclude(logicToCopy.getRowsFieldsNotToInclude());
    }
}
