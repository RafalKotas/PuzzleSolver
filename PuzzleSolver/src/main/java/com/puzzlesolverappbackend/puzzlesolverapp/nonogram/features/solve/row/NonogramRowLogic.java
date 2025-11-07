package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogicParams;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.NonogramActionScheduler;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.NonogramBoardAccessHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.clearing.NonogramFieldClearingHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.mark.*;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.ColouringGenerateLogBaseContext;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.exclusion.ExcludedSequenceLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed.PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement.PlaceXGenerateLogBaseContext;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement.PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.rangeLength;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.util.NonogramParametersComparatorHelper.rangesNotEqual;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.mark.NonogramFieldMarkHelper.markRowBoardField;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed.PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelper.generateLog;

@Setter
@Getter
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
public class NonogramRowLogic extends NonogramLogicParams implements RowActions {

    // === DATA STRUCTURES ===
    protected List<List<List<Integer>>> rowsSequencesRanges;
    protected List<List<Integer>> rowsFieldsNotToInclude;
    protected List<List<Integer>> rowsSequencesIdsNotToInclude;

    // === HELPERS AND SERVICES ===
    @JsonIgnore private final NonogramActionScheduler actionScheduler;
    @JsonIgnore private final NonogramBoardAccessHelper boardAccessHelper;
    @JsonIgnore private final RowColouringHelperImpl rowColouringHelper;
    @JsonIgnore private final RowXPlacementHelperImpl rowXPlacementHelper;
    @JsonIgnore private final RowSequencesCorrectionHelperImpl rowSequencesCorrectionHelper;
    @JsonIgnore private final NonogramFieldClearingHelper nonogramFieldClearingHelper;
    @JsonIgnore private final NonogramFieldExclusionHelperRow nonogramFieldExclusionHelper;
    @JsonIgnore private final List<RefreshableRowHelper> refreshables = new ArrayList<>();

    // === CONSTRUCTORS ===

    public NonogramRowLogic(NonogramLogic logic,
                            NonogramBoardAccessHelper accessHelper,
                            NonogramActionScheduler actionScheduler) {
        super(
                logic.getNonogramRules(),
                logic.getNonogramSolutionBoard(),
                logic.getNonogramSolutionBoardWithMarks(),
                logic.getActionsToDoList(),
                logic.getNonogramState(),
                logic.getLogs()
        );

        this.rowsSequencesRanges = logic.getRowsSequencesRanges();
        this.rowsSequencesIdsNotToInclude = logic.getRowsSequencesIdsNotToInclude();
        this.rowsFieldsNotToInclude = logic.getRowsFieldsNotToInclude();

        this.nonogramSolutionBoardWithMarks = logic.getNonogramSolutionBoardWithMarks();
        this.nonogramSolutionBoard = logic.getNonogramSolutionBoard();
        this.actionsToDoList = logic.getActionsToDoList();

        this.actionScheduler = actionScheduler;
        this.boardAccessHelper = accessHelper;

        this.rowColouringHelper = new RowColouringHelperImpl(this);
        this.rowXPlacementHelper = new RowXPlacementHelperImpl(this);
        this.rowSequencesCorrectionHelper = new RowSequencesCorrectionHelperImpl(this);

        this.nonogramFieldClearingHelper = new NonogramFieldClearingHelper(
                this.getNonogramSolutionBoard(),
                this.getNonogramSolutionBoardWithMarks(),
                this.getBoardAccessHelper()
        );

        this.nonogramFieldExclusionHelper = new NonogramFieldExclusionHelperRow(
                this.rowsFieldsNotToInclude,
                boardAccessHelper
        );

        refreshables.add(rowColouringHelper);
        refreshables.add(rowXPlacementHelper);
        refreshables.add(rowSequencesCorrectionHelper);
    }

    // === PUBLIC METHODS (API) ===

    public void refreshHelpers() {
        for (RefreshableRowHelper r : refreshables) {
            r.refreshFrom(this);
        }
    }

    public void setRowSequencesRanges(int rowIdx, List<List<Integer>> rowSequencesRanges) {
        this.getRowsSequencesRanges().set(rowIdx, rowSequencesRanges);
    }

    // === ROW ACTIONS: CORRECTION ===

    @Override
    public void correctRowSequencesRanges(int rowIdx) {
        rowSequencesCorrectionHelper.correctRowSequencesRanges(rowIdx);
    }

    @Override
    public void correctRowSequencesRangesWhenMetColouredField(int rowIdx) {
        rowSequencesCorrectionHelper.correctRowSequencesRangesWhenMetColouredField(rowIdx);
    }

    @Override
    public void correctRowSequencesRangesIfXOnWay(int rowIdx) {
        rowSequencesCorrectionHelper.correctRowSequencesRangesIfXOnWay(rowIdx);
    }

    @Override
    public void correctRowSequencesRangesWhenMatchingFieldsToSequences(int rowIdx) {
        rowSequencesCorrectionHelper.correctRowSequencesRangesWhenMatchingFieldsToSequences(rowIdx);
    }

    @Override
    public void correctRowSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(int rowIdx) {
        rowSequencesCorrectionHelper.correctRowSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(rowIdx);
    }

    // === ROW ACTIONS: COLOURING ===

    @Override
    public void colourOverlappingFieldsInRow(int rowIdx) {
        rowColouringHelper.colourOverlappingFieldsInRow(rowIdx);
    }

    @Override
    public void colourFieldsIfInRowXWouldForceTooLongColouredFieldsSequence(int rowIdx) {
        rowColouringHelper.colourFieldsInRowIfXWouldForceTooLongColouredFieldsSequence(rowIdx);
    }

    @Override
    public void extendColouredFieldsNearXToMaximumPossibleLengthInRow(int rowIdx) {
        rowColouringHelper.extendColouredFieldsNearXToMaximumPossibleLengthInRow(rowIdx);
    }

    // === ROW ACTIONS: X PLACEMENT ===

    @Override
    public void placeXsRowAtUnreachableFields(int rowIdx) {
        rowXPlacementHelper.placeXsRowAtUnreachableFields(rowIdx);
    }

    @Override
    public void placeXsAroundLongestSequencesInRow(int rowIdx) {
        rowXPlacementHelper.placeXsAroundLongestSequencesInRow(rowIdx);
    }

    @Override
    public void placeXsRowAtTooShortEmptySequences(int rowIdx) {
        rowXPlacementHelper.placeXsRowAtTooShortEmptySequences(rowIdx);
    }

    @Override
    public void placeXsRowIfOWillMergeNearFieldsToTooLongColouredSequence(int rowIdx) {
        rowXPlacementHelper.placeXsRowIfOWillMergeNearFieldsToTooLongColouredSequence(rowIdx);
    }

    @Override
    public void placeXsRowIfONearXWillBeginTooLongPossibleColouredSequence(int rowIdx) {
        rowXPlacementHelper.placeXsRowIfONearXWillBeginTooLongPossibleColouredSequence(rowIdx);
    }

    @Override
    public void placeXsRowIfColouringFieldWillCauseAssignmentConflict(int rowIdx) {
        // The original ranges before any correction
        List<List<Integer>> initialRanges = deepCopy(rowsSequencesRanges.get(rowIdx));
        List<Integer> initialSequencesIdsNotToInclude = copyList(rowsSequencesIdsNotToInclude.get(rowIdx));
        int initialActionsToDoListSize = actionsToDoList.size();
        boolean xPlaceBecauseOfConflict = false;

        for (int columnIndex = 0; columnIndex < this.getNonogramRules().getWidth(); columnIndex++) {
            Field fieldToCheck = new Field(rowIdx, columnIndex);
            if (isFieldEmpty(this.getNonogramSolutionBoard(), fieldToCheck)) {
                // 1. Colour field
                this.getRowColouringHelper().getColouringHelper().colourFieldAtGivenPosition(fieldToCheck, "R---");

                // 2. Apply the series of corrections to the row sequences ranges.
                correctRowSequencesRangesWhenMetColouredField(rowIdx);
                correctRowSequencesRangesWhenMatchingFieldsToSequences(rowIdx);
                correctRowSequencesRangesIfXOnWay(rowIdx);
                correctRowSequencesRanges(rowIdx);
                correctRowSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(rowIdx);

                // 3. Remove the actions added by the scope correction
                while (actionsToDoList.size() > initialActionsToDoListSize) {
                    actionsToDoList.remove(actionsToDoList.size() - 1);
                }

                // 4. Collect all coloured sequences of fields in row.
                List<List<Integer>> colouredSequencesInRow = collectColouredSequencesRanges(this.getNonogramSolutionBoard(), rowIdx, true);

                // 5. Check if for every sequence from 3. we can assign it to any of the possibleSequencesRanges.
                for (List<Integer> seq : colouredSequencesInRow) {
                    boolean canAssign = checkIfColouredSequenceCanBeAssignedToAnyRange(seq, rowIdx);
                    if (!canAssign) {
                        // 6. if (!canAssign) {placeXAtField(rowIdx, columnIndex)}
                        Field fieldToPlaceX = new Field(rowIdx, columnIndex);
                        this.getRowXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(fieldToPlaceX);
                        xPlaceBecauseOfConflict = true;
                        actionScheduler.scheduleActionsBasedOnField(fieldToPlaceX, NonogramSolveAction.PLACE_XS_IF_COLOURING_FIELD_WILL_CAUSE_ASSIGNMENT_CONFLICT_IN_ROW);
                        this.nonogramState.increaseMadeSteps();
                        break;
                    }
                }

                // 7 Empty field
                if (!xPlaceBecauseOfConflict) {
                    nonogramFieldClearingHelper.clearField(fieldToCheck);
                }

                // 8. Set possibleSequencesRanges before correcting/updating (before action method calls)
                rowsSequencesRanges.set(rowIdx, new ArrayList<>(initialRanges));
                rowsSequencesIdsNotToInclude.set(rowIdx, new ArrayList<>(initialSequencesIdsNotToInclude));
                xPlaceBecauseOfConflict = false;
            }
        }
    }

    private boolean checkIfColouredSequenceCanBeAssignedToAnyRange(List<Integer> colouredSequence, int rowIdx) {
        return rowsSequencesRanges.get(rowIdx).stream().anyMatch(range -> rangeInsideAnotherRange(colouredSequence, range));
    }

    // === ROW ACTIONS: OVEREXTENSION PREVENTION ===

    @Override
    public void preventExtendingColouredSequenceToExcessLengthInRow(int rowIdx) {
        preventExtendingColouredSequenceToExcessLengthInRowToLeft(rowIdx);
        preventExtendingColouredSequenceToExcessLengthInRowToRight(rowIdx);
    }

    // === PRIVATE HELPERS: OVEREXTENSION LEFT ===

    private void preventExtendingColouredSequenceToExcessLengthInRowToLeft(int rowIdx) {
        List<String> rowBefore = getRowCopy(rowIdx);
        List<List<Integer>> rowRangesBefore = getRowSequencesRangesCopy(rowIdx);
        List<Integer> rowSequencesLengths = this.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);

        for (int columnIdx = this.getNonogramRules().getWidth() - 1; columnIdx > 0; columnIdx--) {
            Field fieldToCheckX = new Field(rowIdx, columnIdx);
            if (!isFieldWithX(this.getNonogramSolutionBoard(), fieldToCheckX)) continue;

            int potentiallyColouredFieldColumn = fieldToCheckX.getColumnIdx() - 1;
            Field fieldToCheckColoured = new Field(fieldToCheckX.getRowIdx(), potentiallyColouredFieldColumn);
            if (!isFieldColoured(this.getNonogramSolutionBoard(), fieldToCheckColoured)) continue;

            // --- step 1: analyze potential overextension candidates ---
            OverextensionContext context = analyzeLeftOverextensionCandidatesInRow(
                    rowIdx, potentiallyColouredFieldColumn, rowSequencesLengths, rowSequencesRanges
            );
            if (context == null || context.validSequenceLengths().isEmpty()) continue;

            // --- step 2: handle detected case ---
            handleValidOverextensionCase(context, rowBefore, rowSequencesRanges, rowSequencesLengths);

            // --- step 3: update sequence ranges ---
            updateSequenceRangeIfNeededLeft(context, rowIdx, rowBefore, rowRangesBefore);
        }
    }

    private OverextensionContext analyzeLeftOverextensionCandidatesInRow(
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
                .getColouredSequencesRangesInRowInRangeOnLeft(this.getNonogramSolutionBoard(), rowIdx, potentiallyColouredFieldColumn, maxSequenceLength);

        List<Integer> validSequenceIds = RowOverextensionPrevention.findValidSequencesIdsMergingToLeft(
                sequencesIds, sequencesLengths, potentiallyColouredFieldColumn, colouredSequences
        );

        List<Integer> validSequenceLengths = validSequenceIds.stream()
                .map(sequencesIds::indexOf)
                .map(sequencesLengths::get)
                .toList();

        return new OverextensionContext(rowIdx, potentiallyColouredFieldColumn,
                validSequenceIds, validSequenceLengths, sequencesLengths, rowSequencesRanges);
    }

    private void handleValidOverextensionCase(
            OverextensionContext context,
            List<String> initialRow,
            List<List<Integer>> rowSequencesRanges,
            List<Integer> rowSequencesLengths
    ) {
        if (context.validSequenceLengths().stream().distinct().count() != 1) return;

        int sequenceLength = context.validSequenceLengths().get(0);
        int colouredSequenceColStartIdx = context.potentiallyColouredFieldColumn() - sequenceLength + 1;

        // Colour fields on the left
        for (int col = colouredSequenceColStartIdx; col <= context.potentiallyColouredFieldColumn(); col++) {
            Field f = new Field(context.rowIdx(), col);
            if (isFieldEmpty(this.getNonogramSolutionBoard(), f)) {
                this.getRowColouringHelper().getColouringHelper().colourFieldAtGivenPosition(f, "R---");
                actionScheduler.scheduleActionsBasedOnField(f, NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART_IN_ROW);
                this.nonogramState.increaseMadeSteps();

                ColouringGenerateLogBaseContext colouringGenerateLogBaseContext = new ColouringGenerateLogBaseContext(
                        true,
                        context.rowIdx(),
                        initialRow,
                        getRowCopy(context.rowIdx()),
                        rowSequencesRanges,
                        rowSequencesLengths
                );
                tmpLog = PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper.generateLog(
                        colouringGenerateLogBaseContext,
                        "left"
                );
                addLog();
            }
        }

        // Place X before sequence
        Field fieldToPlaceX = new Field(context.rowIdx(), colouredSequenceColStartIdx - 1);
        if (fieldToPlaceX.getColumnIdx() >= 0 && isFieldEmpty(this.nonogramSolutionBoard, fieldToPlaceX)) {
            this.getRowXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(fieldToPlaceX);
            this.nonogramState.increaseMadeSteps();

            PlaceXGenerateLogBaseContext placeXGenerateLogBaseContext = new PlaceXGenerateLogBaseContext(
                    true,
                    context.rowIdx(),
                    initialRow,
                    getRowCopy(context.rowIdx())
            );
            tmpLog = PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper.generateLog(
                    placeXGenerateLogBaseContext,
                    "left",
                    context.validSequenceIds.get(0),
                    rowSequencesLengths,
                    rowSequencesRanges
            );
            addLog();

            this.getNonogramFieldExclusionHelper().excludeFieldInRow(fieldToPlaceX);
            actionScheduler.scheduleActionsBasedOnField(fieldToPlaceX,
                    NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART_IN_ROW);
        }
    }

    // === PRIVATE HELPERS: OVEREXTENSION RIGHT ===

    private void preventExtendingColouredSequenceToExcessLengthInRowToRight(int rowIdx) {
        List<String> rowBefore = getRowCopy(rowIdx);
        List<List<Integer>> rowRangesBefore = getRowSequencesRangesCopy(rowIdx);
        List<Integer> rowSequencesLengths = this.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);

        for (int columnIdx = 0; columnIdx < this.getNonogramRules().getWidth() - 1; columnIdx++) {
            Field fieldToCheckX = new Field(rowIdx, columnIdx);
            if (!isFieldWithX(this.getNonogramSolutionBoard(), fieldToCheckX)) continue;

            int potentiallyColouredFieldColumn = fieldToCheckX.getColumnIdx() + 1;
            Field fieldToCheckColoured = new Field(fieldToCheckX.getRowIdx(), potentiallyColouredFieldColumn);
            if (!isFieldColoured(this.getNonogramSolutionBoard(), fieldToCheckColoured)) continue;

            // --- step 1: analyze potential overextension candidates ---
            OverextensionContext context = analyzeRightOverextensionCandidatesInRow(
                    rowIdx, potentiallyColouredFieldColumn, rowSequencesLengths, rowSequencesRanges
            );
            if (context == null || context.validSequenceLengths().isEmpty()) continue;

            // --- step 2: handle detected case ---
            handleValidRightOverextensionCase(context, rowBefore, rowSequencesRanges, rowSequencesLengths);

            // --- step 3: update sequence ranges ---
            updateSequenceRangeIfNeededRight(context, rowIdx, rowBefore, rowRangesBefore);
        }
    }

    private OverextensionContext analyzeRightOverextensionCandidatesInRow(
            int rowIdx,
            int potentiallyColouredFieldColumn,
            List<Integer> rowSequencesLengths,
            List<List<Integer>> rowSequencesRanges
    ) {
        List<Integer> sequencesIds = RowOverextensionPrevention
                .sequencesIdsInRowIncludingField(rowSequencesRanges, new Field(rowIdx, potentiallyColouredFieldColumn));
        if (sequencesIds.isEmpty()) return null;

        List<Integer> sequencesLengths = sequencesIds.stream().map(rowSequencesLengths::get).toList();
        int maxSequenceLength = Collections.max(sequencesLengths);

        List<List<Integer>> colouredSequences = RowOverextensionPrevention
                .getColouredSequencesRangesInRowInRangeOnRight(this.getNonogramSolutionBoard(), rowIdx, potentiallyColouredFieldColumn, maxSequenceLength);

        List<Integer> validSequenceIds = RowOverextensionPrevention
                .findValidSequencesIdsMergingToRight(sequencesIds, sequencesLengths, potentiallyColouredFieldColumn, colouredSequences);

        List<Integer> validSequenceLengths = validSequenceIds.stream()
                .map(sequencesIds::indexOf)
                .map(sequencesLengths::get)
                .toList();

        return new OverextensionContext(rowIdx, potentiallyColouredFieldColumn,
                validSequenceIds, validSequenceLengths, sequencesLengths, rowSequencesRanges);
    }

    private void handleValidRightOverextensionCase(
            OverextensionContext context,
            List<String> initialRow,
            List<List<Integer>> rowSequencesRanges,
            List<Integer> rowSequencesLengths
    ) {
        if (context.validSequenceLengths().stream().distinct().count() != 1) return;

        int sequenceLength = context.validSequenceLengths().get(0);
        int colouredSequenceEndColumnIdx = context.potentiallyColouredFieldColumn() + sequenceLength - 1;

        // Colour fields on the right
        for (int col = context.potentiallyColouredFieldColumn(); col <= colouredSequenceEndColumnIdx; col++) {
            Field f = new Field(context.rowIdx(), col);
            if (isFieldEmpty(this.getNonogramSolutionBoard(), f)) {
                this.getRowColouringHelper().getColouringHelper().colourFieldAtGivenPosition(f, "R---");
                actionScheduler.scheduleActionsBasedOnField(f,
                        NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART_IN_ROW);
                this.nonogramState.increaseMadeSteps();

                ColouringGenerateLogBaseContext colouringGenerateLogBaseContext = new ColouringGenerateLogBaseContext(
                        true,
                        context.rowIdx(),
                        initialRow,
                        getRowCopy(context.rowIdx()),
                        rowSequencesRanges,
                        rowSequencesLengths
                );
                tmpLog = PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper.generateLog(
                        colouringGenerateLogBaseContext,
                        "right"
                );
                addLog();
            }
        }

        // Place X after sequence
        Field fieldToPlaceX = new Field(context.rowIdx(), colouredSequenceEndColumnIdx + 1);
        if (fieldToPlaceX.getColumnIdx() < this.getNonogramRules().getWidth() &&
                isFieldEmpty(this.nonogramSolutionBoard, fieldToPlaceX)) {

            this.getRowXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(fieldToPlaceX);
            this.nonogramState.increaseMadeSteps();

            PlaceXGenerateLogBaseContext placeXGenerateLogBaseContext = new PlaceXGenerateLogBaseContext(
                    true,
                    context.rowIdx(),
                    initialRow,
                    getRowCopy(context.rowIdx())
            );
            tmpLog = PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper.generateLog(
                    placeXGenerateLogBaseContext,
                    "right",
                    context.validSequenceIds.get(0),
                    rowSequencesLengths,
                    rowSequencesRanges
            );
            addLog();

            this.getNonogramFieldExclusionHelper().excludeFieldInRow(fieldToPlaceX);
            actionScheduler.scheduleActionsBasedOnField(fieldToPlaceX,
                    NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART_IN_ROW);
        }
    }

    // === RANGES UPDATES: LEFT & RIGHT ===

    // LEFT  → [start = col - len + 1, end = col]
    private void updateSequenceRangeIfNeededLeft(
            OverextensionContext context,
            int rowIdx,
            List<String> rowBefore,
            List<List<Integer>> rowRangesBefore
    ) {
        if (context.validSequenceIds().size() != 1) return;

        int seqId = context.validSequenceIds().get(0);
        int len   = context.validSequenceLengths().get(0);
        int end   = context.potentiallyColouredFieldColumn();
        int start = end - len + 1;

        applyRowRangeUpdate(rowIdx, seqId, start, end, context, rowBefore, rowRangesBefore);
    }

    // RIGHT → [start = col, end = col + len - 1]
    private void updateSequenceRangeIfNeededRight(
            OverextensionContext context,
            int rowIdx,
            List<String> rowBefore,
            List<List<Integer>> rowRangesBefore
    ) {
        if (context.validSequenceIds().size() != 1) return;

        int seqId = context.validSequenceIds().get(0);
        int len   = context.validSequenceLengths().get(0);
        int start = context.potentiallyColouredFieldColumn();
        int end   = start + len - 1;

        applyRowRangeUpdate(rowIdx, seqId, start, end, context, rowBefore, rowRangesBefore);
    }

    private void applyRowRangeUpdate(
            int rowIdx,
            int seqId,
            int start,
            int end,
            OverextensionContext context,
            List<String> rowBefore,
            List<List<Integer>> rowRangesBefore
    ) {
        List<Integer> oldRange = context.rowSequencesRanges().get(seqId);
        List<Integer> updated  = List.of(start, end);
        if (!rangesNotEqual(oldRange, updated)) return;

        updateRowSequenceRange(rowIdx, seqId, updated);
        nonogramState.increaseMadeSteps();

        if (sequenceShouldBeExcluded(rowIdx, seqId)) {
            excludeSequenceInRow(rowIdx, seqId);
            nonogramState.increaseMadeSteps();
        }

        Field rowField = new Field(rowIdx, 0);
        actionScheduler.scheduleActionsBasedOnField(rowField,
                NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART_IN_ROW);

        tmpLog = generateLog(
                true,
                rowIdx,
                context.sequencesLengths(),
                rowBefore,
                getRowCopy(rowIdx),
                rowRangesBefore,
                getRowsSequencesRanges().get(rowIdx)
        );
        addLog();
    }

    // === ROW ACTIONS: MARKING ===

    @Override
    public void markAvailableFieldsInRow(int rowIdx) {
        MarkContext markContext = new MarkContext(
                new BoardContext(rowIdx, true,
                        getNonogramRules(),
                        getNonogramSolutionBoard(),
                        getNonogramSolutionBoardWithMarks()),
                new SequencesContext(getNonogramRules().getRowSequencesLengths(),
                        getRowsSequencesRanges(),
                        this::changeRowSequenceRange,
                        this::excludeSequenceInRow),
                new MarkOperationContext(actionScheduler,
                        nonogramState,
                        this::addLog,
                        this::setTmpLog)
        );

        NonogramFieldMarkHelper.markAvailableFieldsInLine(markContext);
    }

    // === SUPPORT METHODS ===

    public void excludeSequenceInRow(int rowIdx, int seqIdx) {
        if (!this.getBoardAccessHelper().isRowIndexValid(rowIdx)
                || this.rowsSequencesIdsNotToInclude.get(rowIdx).contains(seqIdx))
            return;

        String marker = indexToSequenceCharMark(seqIdx);
        List<Integer> rowSeqRange = this.getRowsSequencesRanges().get(rowIdx).get(seqIdx);

        IntStream.rangeClosed(rowSeqRange.get(0), rowSeqRange.get(1))
                .forEach(columnIdx ->
                        markRowBoardField(this.getNonogramSolutionBoardWithMarks(), rowIdx, columnIdx, marker));

        tmpLog = ExcludedSequenceLogHelper.generateLog(
                true, rowIdx, seqIdx, this.getRowCopy(rowIdx),
                this.getNonogramRules().getRowSequencesLengths().get(rowIdx),
                this.getRowsSequencesRanges().get(rowIdx));
        addLog();

        this.rowsSequencesIdsNotToInclude.get(rowIdx).add(seqIdx);
        Collections.sort(this.rowsSequencesIdsNotToInclude.get(rowIdx));
    }

    public void updateRowSequenceRange(int rowIdx, int sequenceIdx, List<Integer> updatedRange) {
        this.rowsSequencesRanges.get(rowIdx).set(sequenceIdx, updatedRange);
    }

    private boolean sequenceShouldBeExcluded(int rowIdx, int sequenceIdx) {
        int sequenceLength = nonogramRules.getRowSequencesLengths().get(rowIdx).get(sequenceIdx);
        List<Integer> sequenceRange = rowsSequencesRanges.get(rowIdx).get(sequenceIdx);
        return rangeLength(sequenceRange) == sequenceLength
                && allFieldsAreColouredInColumnRange(rowIdx, sequenceRange, nonogramSolutionBoard);
    }

    public void changeRowSequenceRange(int rowIndex, int sequenceIndex, List<Integer> updatedRange) {
        this.rowsSequencesRanges.get(rowIndex).set(sequenceIndex, updatedRange);
    }

    protected List<List<Integer>> getRowSequencesRangesCopy(int rowIdx) {
        return this.getRowsSequencesRanges().get(rowIdx).stream()
                .map(ArrayList::new)
                .collect(Collectors.toList());
    }

    // === INTERNAL RECORDS ===

    private record OverextensionContext(
            int rowIdx,
            int potentiallyColouredFieldColumn,
            List<Integer> validSequenceIds,
            List<Integer> validSequenceLengths,
            List<Integer> sequencesLengths,
            List<List<Integer>> rowSequencesRanges
    ) {}
}