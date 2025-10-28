package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column;

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
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.exclusion.ExcludedSequenceLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed.PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed.PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service.NonogramLogService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.rangeLength;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.actions.ColumnMixedActionsHelper.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.util.NonogramParametersComparatorHelper.rangesNotEqual;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.mark.NonogramFieldMarkHelper.markColumnBoardField;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed.PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelper.generateLog;

@Getter
@Setter
@Slf4j
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NonogramColumnLogic extends NonogramLogicParams implements ColumnActions {

    // ------------------------------------------------------------------------
    // 🔹 Constants
    // ------------------------------------------------------------------------
    private static final String CORRECT_COLUMN_SEQ_RANGE_MARKING_FIELD = "correcting column sequence range when marking field";
    private static final String FILL_OVERLAPPING_FIELDS = "fill overlapping fields";

    private static final List<Integer> NOT_FOUND_EMPTY_FIELDS_RANGE_VALUE = List.of(-1, -1);
    private static final List<Integer> NOT_FOUND_COLOURED_FIELDS_RANGE_VALUE = List.of(-1, -1);

    // ------------------------------------------------------------------------
    // 🔹 Fields
    // ------------------------------------------------------------------------
    protected List<List<List<Integer>>> columnsSequencesRanges;
    protected List<List<Integer>> columnsFieldsNotToInclude;
    protected List<List<Integer>> columnsSequencesIdsNotToInclude;

    @JsonIgnore private final NonogramActionScheduler actionScheduler;
    @JsonIgnore private final NonogramBoardAccessHelper boardAccessHelper;
    @JsonIgnore private final ColumnColouringHelperImpl columnColouringHelper;
    @JsonIgnore private final ColumnXPlacementHelperImpl columnXPlacementHelper;
    @JsonIgnore private final ColumnSequencesCorrectionHelperImpl columnSequencesCorrectionHelper;
    @JsonIgnore private final NonogramFieldClearingHelper nonogramFieldClearingHelper;
    @JsonIgnore private final NonogramFieldExclusionHelperColumn nonogramFieldExclusionHelper;
    @JsonIgnore private final List<RefreshableColumnHelper> refreshables = new ArrayList<>();
    @JsonIgnore private final NonogramLogService logService;

    // ------------------------------------------------------------------------
    // 🔹 Constructors
    // ------------------------------------------------------------------------

    public NonogramColumnLogic(NonogramLogic nonogramLogic) {
        super(
                nonogramLogic.getNonogramRules(),
                nonogramLogic.getNonogramSolutionBoard(),
                nonogramLogic.getNonogramSolutionBoardWithMarks(),
                nonogramLogic.getActionsToDoList(),
                nonogramLogic.getNonogramState(),
                nonogramLogic.getLogs()
        );

        this.columnsSequencesRanges = nonogramLogic.getColumnsSequencesRanges();
        this.columnsSequencesIdsNotToInclude = nonogramLogic.getColumnsSequencesIdsNotToInclude();
        this.columnsFieldsNotToInclude = nonogramLogic.getColumnsFieldsNotToInclude();

        this.nonogramSolutionBoardWithMarks = nonogramLogic.getNonogramSolutionBoardWithMarks();
        this.nonogramSolutionBoard = nonogramLogic.getNonogramSolutionBoard();
        this.actionsToDoList = nonogramLogic.getActionsToDoList();

        this.actionScheduler = new NonogramActionScheduler(this.getActionsToDoList());
        this.boardAccessHelper = new NonogramBoardAccessHelper(this.getNonogramSolutionBoard());
        this.columnColouringHelper = new ColumnColouringHelperImpl(this);
        this.columnXPlacementHelper = new ColumnXPlacementHelperImpl(this);
        this.columnSequencesCorrectionHelper = new ColumnSequencesCorrectionHelperImpl(this);
        this.nonogramFieldClearingHelper = new NonogramFieldClearingHelper(
                this.getNonogramSolutionBoard(),
                this.getNonogramSolutionBoardWithMarks(),
                this.getBoardAccessHelper()
        );
        this.nonogramFieldExclusionHelper = new NonogramFieldExclusionHelperColumn(
                this.columnsFieldsNotToInclude,
                boardAccessHelper
        );

        refreshables.add(columnColouringHelper);
        refreshables.add(columnXPlacementHelper);
        refreshables.add(columnSequencesCorrectionHelper);

        this.logService = new NonogramLogService();
    }

    public NonogramColumnLogic(NonogramLogic logic,
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

        this.columnsSequencesRanges = logic.getColumnsSequencesRanges();
        this.columnsSequencesIdsNotToInclude = logic.getColumnsSequencesIdsNotToInclude();
        this.columnsFieldsNotToInclude = logic.getColumnsFieldsNotToInclude();

        this.nonogramSolutionBoardWithMarks = logic.getNonogramSolutionBoardWithMarks();
        this.nonogramSolutionBoard = logic.getNonogramSolutionBoard();
        this.actionsToDoList = logic.getActionsToDoList();

        this.actionScheduler = actionScheduler;
        this.boardAccessHelper = accessHelper;
        this.columnColouringHelper = new ColumnColouringHelperImpl(this);
        this.columnXPlacementHelper = new ColumnXPlacementHelperImpl(this);
        this.columnSequencesCorrectionHelper = new ColumnSequencesCorrectionHelperImpl(this);
        this.nonogramFieldClearingHelper = new NonogramFieldClearingHelper(
                this.getNonogramSolutionBoard(),
                this.getNonogramSolutionBoardWithMarks(),
                this.getBoardAccessHelper()
        );
        this.nonogramFieldExclusionHelper = new NonogramFieldExclusionHelperColumn(
                this.columnsFieldsNotToInclude,
                boardAccessHelper
        );

        refreshables.add(columnColouringHelper);
        refreshables.add(columnXPlacementHelper);
        refreshables.add(columnSequencesCorrectionHelper);

        this.logService = new NonogramLogService();
    }

    public NonogramColumnLogic(NonogramColumnLogic original) {
        super(
                original.getNonogramRules(),
                original.getNonogramSolutionBoard(),
                original.getNonogramSolutionBoardWithMarks(),
                original.getActionsToDoList(),
                original.getNonogramState(),
                original.getLogs()
        );

        this.columnsSequencesRanges = original.getColumnsSequencesRanges();
        this.columnsSequencesIdsNotToInclude = original.getColumnsSequencesIdsNotToInclude();
        this.columnsFieldsNotToInclude = original.getColumnsFieldsNotToInclude();

        this.nonogramSolutionBoard = original.getNonogramSolutionBoard();
        this.nonogramSolutionBoardWithMarks = original.getNonogramSolutionBoardWithMarks();
        this.actionsToDoList = original.getActionsToDoList();

        this.actionScheduler = new NonogramActionScheduler(this.getActionsToDoList());
        this.boardAccessHelper = new NonogramBoardAccessHelper(this.getNonogramSolutionBoard());
        this.columnColouringHelper = original.getColumnColouringHelper();
        this.columnXPlacementHelper = original.getColumnXPlacementHelper();
        this.columnSequencesCorrectionHelper = new ColumnSequencesCorrectionHelperImpl(this);
        this.nonogramFieldClearingHelper = new NonogramFieldClearingHelper(
                this.getNonogramSolutionBoard(),
                this.getNonogramSolutionBoardWithMarks(),
                this.getBoardAccessHelper()
        );
        this.nonogramFieldExclusionHelper = new NonogramFieldExclusionHelperColumn(
                this.columnsFieldsNotToInclude,
                boardAccessHelper
        );

        refreshables.add(columnColouringHelper);
        refreshables.add(columnXPlacementHelper);
        refreshables.add(columnSequencesCorrectionHelper);

        this.logService = new NonogramLogService();
    }

    // ------------------------------------------------------------------------
    // 🔹 Public methods
    // ------------------------------------------------------------------------

    public void refreshHelpers() {
        for (RefreshableColumnHelper r : refreshables) {
            r.refreshFrom(this);
        }
    }

    public void setColumnSequencesRanges(int columnIdx, List<List<Integer>> ranges) {
        this.getColumnsSequencesRanges().set(columnIdx, ranges);
    }

    // -------------------- Corrections --------------------
    @Override
    public void correctColumnSequencesRanges(int columnIdx) {
        columnSequencesCorrectionHelper.correctColumnSequencesRanges(columnIdx);
    }

    @Override
    public void correctColumnSequencesRangesWhenMetColouredField(int columnIdx) {
        columnSequencesCorrectionHelper.correctColumnSequencesRangesWhenMetColouredField(columnIdx);
    }

    @Override
    public void correctColumnSequencesRangesIfXOnWay(int columnIdx, boolean changeLogicDetails) {
        columnSequencesCorrectionHelper.correctColumnSequencesRangesIfXOnWay(columnIdx, changeLogicDetails);
    }

    @Override
    public void correctColumnSequencesRangesWhenMatchingFieldsToSequences(int columnIdx) {
        columnSequencesCorrectionHelper.correctColumnSequencesRangesWhenMatchingFieldsToSequences(columnIdx);
    }

    @Override
    public void correctColumnSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(int columnIdx) {
        columnSequencesCorrectionHelper.correctColumnSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(columnIdx);
    }

    // -------------------- Colouring --------------------
    @Override
    public void colourOverlappingFieldsInColumn(int columnIdx) {
        columnColouringHelper.colourOverlappingFieldsInColumn(columnIdx);
    }

    @Override
    public void colourFieldsInColumnIfXWouldForceTooLongColouredFieldsSequence(int columnIdx) {
        columnColouringHelper.colourFieldsInColumnIfXWouldForceTooLongColouredFieldsSequence(columnIdx);
    }

    @Override
    public void extendColouredFieldsNearXToMaximumPossibleLengthInColumn(int columnIdx) {
        columnColouringHelper.extendColouredFieldsNearXToMaximumPossibleLengthInColumn(columnIdx);
    }

    // -------------------- X Placement --------------------
    @Override
    public void placeXsColumnAtUnreachableFields(int columnIdx) {
        columnXPlacementHelper.placeXsColumnAtUnreachableFields(columnIdx);
    }

    @Override
    public void placeXsAroundLongestSequencesInColumn(int columnIdx) {
        columnXPlacementHelper.placeXsAroundLongestSequencesInColumn(columnIdx);
    }

    @Override
    public void placeXsColumnAtTooShortEmptySequences(int columnIdx) {
        columnXPlacementHelper.placeXsColumnAtTooShortEmptySequences(columnIdx);
    }

    @Override
    public void placeXsColumnIfOWillMergeNearFieldsToTooLongColouredSequence(int columnIdx) {
        columnXPlacementHelper.placeXsColumnIfOWillMergeNearFieldsToTooLongColouredSequence(columnIdx);
    }

    @Override
    public void placeXsColumnIfONearXWillBeginTooLongPossibleColouredSequence(int columnIdx) {
        columnXPlacementHelper.placeXsColumnIfONearXWillBeginTooLongPossibleColouredSequence(columnIdx);
    }

    // ------------------------------------------------------------------------
    // 🧩 Overextension Prevention
    // ------------------------------------------------------------------------

    @Override
    public void preventExtendingColouredSequenceToExcessLengthInColumn(int columnIdx) {
        preventExtendingColouredSequenceToExcessLengthInColumnToTop(columnIdx);
        preventExtendingColouredSequenceToExcessLengthInColumnToBottom(columnIdx);
    }

    // --- toTop() ---
    private void preventExtendingColouredSequenceToExcessLengthInColumnToTop(int columnIdx) {
        List<String> columnBefore = getColumnCopy(columnIdx);
        List<List<Integer>> columnRangesBefore = getColumnSequencesRangesCopy(columnIdx);

        List<Integer> columnSequencesLengths = this.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);

        for (int rowIdx = this.getNonogramRules().getHeight() - 1; rowIdx > 0; rowIdx--) {
            Field fieldToCheckX = new Field(rowIdx, columnIdx);
            if (!isFieldWithX(this.getNonogramSolutionBoard(), fieldToCheckX)) continue;

            int potentiallyColouredFieldRow = fieldToCheckX.getRowIdx() - 1;
            Field fieldToCheckColoured = new Field(potentiallyColouredFieldRow, fieldToCheckX.getColumnIdx());

            if (!isFieldColoured(this.getNonogramSolutionBoard(), fieldToCheckColoured)) continue;

            OverextensionColumnContext context = analyzeTopOverextensionCandidatesInColumn(
                    columnIdx, potentiallyColouredFieldRow, columnSequencesLengths, columnSequencesRanges
            );

            if (context == null || context.validSequenceLengths().isEmpty()) continue;

            handleValidTopOverextensionCase(context, columnBefore, columnSequencesRanges, columnSequencesLengths);
            updateColumnSequenceRangeIfNeededTop(context, columnIdx, columnBefore, columnRangesBefore);
        }
    }

    // --- toBottom() ---
    private void preventExtendingColouredSequenceToExcessLengthInColumnToBottom(int columnIdx) {
        List<String> columnBefore = getColumnCopy(columnIdx);
        List<List<Integer>> columnRangesBefore = getColumnSequencesRangesCopy(columnIdx);

        List<Integer> columnSequencesLengths = this.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);

        for (int rowIdx = 0; rowIdx < this.getNonogramRules().getHeight() - 1; rowIdx++) {
            Field fieldToCheckX = new Field(rowIdx, columnIdx);
            if (!isFieldWithX(this.getNonogramSolutionBoard(), fieldToCheckX)) continue;

            int potentiallyColouredFieldRow = fieldToCheckX.getRowIdx() + 1;
            Field fieldToCheckColoured = new Field(potentiallyColouredFieldRow, fieldToCheckX.getColumnIdx());

            if (!isFieldColoured(this.getNonogramSolutionBoard(), fieldToCheckColoured)) continue;

            OverextensionColumnContext context = analyzeBottomOverextensionCandidatesInColumn(
                    columnIdx, potentiallyColouredFieldRow, columnSequencesLengths, columnSequencesRanges
            );

            if (context == null || context.validSequenceLengths().isEmpty()) continue;

            handleValidBottomOverextensionCase(context, columnBefore, columnSequencesRanges, columnSequencesLengths);
            updateColumnSequenceRangeIfNeededBottom(context, columnIdx, columnBefore, columnRangesBefore);
        }
    }

    // ------------------------------------------------------------------------
    // 🧠 Analysis methods
    // ------------------------------------------------------------------------

    private OverextensionColumnContext analyzeTopOverextensionCandidatesInColumn(
            int columnIdx, int potentiallyColouredFieldRow,
            List<Integer> columnSequencesLengths, List<List<Integer>> columnSequencesRanges) {

        List<Integer> sequencesIds = sequencesIdsInColumnIncludingField(columnSequencesRanges, new Field(potentiallyColouredFieldRow, columnIdx));
        if (sequencesIds.isEmpty()) return null;

        List<Integer> sequencesLengths = sequencesIds.stream().map(columnSequencesLengths::get).toList();
        int maxSequenceLength = Collections.max(sequencesLengths);

        List<List<Integer>> colouredSequences = getColouredSequencesRangesInColumnInRangeToTop(
                this.getNonogramSolutionBoard(), columnIdx, potentiallyColouredFieldRow, maxSequenceLength);

        List<Integer> validSequenceIds = findValidSequencesIdsMergingToTop(
                sequencesIds, sequencesLengths, potentiallyColouredFieldRow, colouredSequences);

        List<Integer> validSequenceLengths = validSequenceIds.stream()
                .map(sequencesIds::indexOf)
                .map(sequencesLengths::get)
                .toList();

        return new OverextensionColumnContext(columnIdx, potentiallyColouredFieldRow,
                validSequenceIds, validSequenceLengths, sequencesLengths, columnSequencesRanges);
    }

    private OverextensionColumnContext analyzeBottomOverextensionCandidatesInColumn(
            int columnIdx, int potentiallyColouredFieldRow,
            List<Integer> columnSequencesLengths, List<List<Integer>> columnSequencesRanges) {

        List<Integer> sequencesIds = sequencesIdsInColumnIncludingField(columnSequencesRanges, new Field(potentiallyColouredFieldRow, columnIdx));
        if (sequencesIds.isEmpty()) return null;

        List<Integer> sequencesLengths = sequencesIds.stream().map(columnSequencesLengths::get).toList();
        int maxSequenceLength = Collections.max(sequencesLengths);

        List<List<Integer>> colouredSequences = getColouredSequencesRangesInColumnInRangeToBottom(
                this.getNonogramSolutionBoard(), columnIdx, potentiallyColouredFieldRow, maxSequenceLength);

        List<Integer> validSequenceIds = findValidSequencesIdsMergingToBottom(
                sequencesIds, sequencesLengths, potentiallyColouredFieldRow, colouredSequences);

        List<Integer> validSequenceLengths = validSequenceIds.stream()
                .map(sequencesIds::indexOf)
                .map(sequencesLengths::get)
                .toList();

        return new OverextensionColumnContext(columnIdx, potentiallyColouredFieldRow,
                validSequenceIds, validSequenceLengths, sequencesLengths, columnSequencesRanges);
    }

    // ------------------------------------------------------------------------
    // 🎨 Handling valid overextension cases
    // ------------------------------------------------------------------------

    private void handleValidTopOverextensionCase(OverextensionColumnContext context,
                                                 List<String> columnBefore,
                                                 List<List<Integer>> columnSequencesRanges,
                                                 List<Integer> columnSequencesLengths) {
        if (context.validSequenceLengths().stream().distinct().count() != 1) return;

        int sequenceLength = context.validSequenceLengths().get(0);
        int colouredSequenceRowStartIdx = context.potentiallyColouredFieldRow() - sequenceLength + 1;

        for (int row = colouredSequenceRowStartIdx; row <= context.potentiallyColouredFieldRow(); row++) {
            Field f = new Field(row, context.columnIdx());
            if (isFieldEmpty(this.nonogramSolutionBoard, f)) {
                this.getColumnColouringHelper().getColouringHelper().colourFieldAtGivenPosition(f, "--C-");
                this.nonogramState.increaseMadeSteps();
                actionScheduler.scheduleActionsBasedOnField(f,
                        NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART_IN_COLUMN);

                tmpLog = PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper.generateLog(
                        false, context.columnIdx(), "top",
                        columnSequencesLengths, columnSequencesRanges, columnBefore, getColumnCopy(context.columnIdx()));
                addLog();
            }
        }

        Field fieldToPlaceX = new Field(colouredSequenceRowStartIdx - 1, context.columnIdx());
        if (fieldToPlaceX.getRowIdx() >= 0 && isFieldEmpty(this.nonogramSolutionBoard, fieldToPlaceX)) {
            this.getColumnXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(fieldToPlaceX);
            this.nonogramState.increaseMadeSteps();

            tmpLog = PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper.generateLog(
                    false, context.columnIdx(), "top",
                    columnSequencesLengths, columnSequencesRanges, columnBefore, getColumnCopy(context.columnIdx()));
            addLog();

            this.getNonogramFieldExclusionHelper().excludeFieldInColumn(fieldToPlaceX);
            actionScheduler.scheduleActionsBasedOnField(fieldToPlaceX,
                    NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART_IN_COLUMN);
        }
    }

    private void handleValidBottomOverextensionCase(OverextensionColumnContext context,
                                                    List<String> columnBefore,
                                                    List<List<Integer>> columnSequencesRanges,
                                                    List<Integer> columnSequencesLengths) {
        if (context.validSequenceLengths().stream().distinct().count() != 1) return;

        int sequenceLength = context.validSequenceLengths().get(0);
        int colouredSequenceEndRowIdx = context.potentiallyColouredFieldRow() + sequenceLength - 1;

        for (int row = context.potentiallyColouredFieldRow(); row <= colouredSequenceEndRowIdx; row++) {
            Field f = new Field(row, context.columnIdx());
            if (isFieldEmpty(this.nonogramSolutionBoard, f)) {
                this.getColumnColouringHelper().getColouringHelper().colourFieldAtGivenPosition(f, "--C-");
                this.nonogramState.increaseMadeSteps();
                actionScheduler.scheduleActionsBasedOnField(f,
                        NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART_IN_COLUMN);

                tmpLog = PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper.generateLog(
                        false, context.columnIdx(), "bottom",
                        columnSequencesLengths, columnSequencesRanges, columnBefore, getColumnCopy(context.columnIdx()));
                addLog();
            }
        }

        Field fieldToPlaceX = new Field(colouredSequenceEndRowIdx + 1, context.columnIdx());
        if (fieldToPlaceX.getRowIdx() < this.getNonogramRules().getHeight()
                && isFieldEmpty(this.nonogramSolutionBoard, fieldToPlaceX)) {

            this.getColumnXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(fieldToPlaceX);
            this.nonogramState.increaseMadeSteps();

            tmpLog = PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper.generateLog(
                    false, context.columnIdx(), "bottom",
                    columnSequencesLengths, columnSequencesRanges, columnBefore, getColumnCopy(context.columnIdx()));
            addLog();

            this.getNonogramFieldExclusionHelper().excludeFieldInColumn(fieldToPlaceX);
            actionScheduler.scheduleActionsBasedOnField(fieldToPlaceX,
                    NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART_IN_COLUMN);
        }
    }

    // ------------------------------------------------------------------------
    // 🧾 Sequence range update
    // ------------------------------------------------------------------------

    // TOP
    private void updateColumnSequenceRangeIfNeededTop(OverextensionColumnContext ctx, int colIdx,
                                                      List<String> colBefore, List<List<Integer>> rangesBefore) {
        if (ctx.validSequenceIds().size() != 1) return;

        int seqId = ctx.validSequenceIds().get(0);
        int len   = ctx.validSequenceLengths().get(0);
        int end   = ctx.potentiallyColouredFieldRow();
        int start = end - len + 1;

        applyColumnRangeUpdate(colIdx, seqId, start, end, ctx, colBefore, rangesBefore);
    }

    // BOTTOM
    private void updateColumnSequenceRangeIfNeededBottom(OverextensionColumnContext ctx, int colIdx,
                                                         List<String> colBefore, List<List<Integer>> rangesBefore) {
        if (ctx.validSequenceIds().size() != 1) return;

        int seqId = ctx.validSequenceIds().get(0);
        int len   = ctx.validSequenceLengths().get(0);
        int start = ctx.potentiallyColouredFieldRow();
        int end   = start + len - 1;

        applyColumnRangeUpdate(colIdx, seqId, start, end, ctx, colBefore, rangesBefore);
    }

    private void applyColumnRangeUpdate(int colIdx, int seqId, int start, int end,
                                        OverextensionColumnContext ctx,
                                        List<String> colBefore, List<List<Integer>> rangesBefore) {
        List<Integer> oldRange = ctx.columnSequencesRanges().get(seqId);
        List<Integer> updated  = List.of(start, end);

        if (!rangesNotEqual(oldRange, updated)) return;

        updateColumnSequenceRange(colIdx, seqId, updated);
        nonogramState.increaseMadeSteps();

        if (sequenceShouldBeExcluded(colIdx, seqId)) {
            excludeSequenceInColumn(colIdx, seqId);
            nonogramState.increaseMadeSteps();
        }

        Field colField = new Field(0, colIdx);
        actionScheduler.scheduleActionsBasedOnField(colField,
                NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART_IN_COLUMN);

        tmpLog = generateLog(false, colIdx, ctx.sequencesLengths(),
                colBefore, getColumnCopy(colIdx), rangesBefore, getColumnsSequencesRanges().get(colIdx));
        addLog();
    }

    // ------------------------------------------------------------------------
    // 🧩 Helper record
    // ------------------------------------------------------------------------
    private record OverextensionColumnContext(
            int columnIdx,
            int potentiallyColouredFieldRow,
            List<Integer> validSequenceIds,
            List<Integer> validSequenceLengths,
            List<Integer> sequencesLengths,
            List<List<Integer>> columnSequencesRanges
    ) {}

    // ------------------------------------------------------------------------
    // 🏷️ Field marking & exclusion
    // ------------------------------------------------------------------------

    @Override
    public void markAvailableFieldsInColumn(int columnIdx) {
        MarkContext markContext = new MarkContext(
                new BoardContext(columnIdx, false,
                        getNonogramRules(),
                        getNonogramSolutionBoard(),
                        getNonogramSolutionBoardWithMarks()),
                new SequencesContext(getNonogramRules().getColumnSequencesLengths(),
                        this.getColumnsSequencesRanges(),
                        this::changeColumnSequenceRange,
                        this::excludeSequenceInColumn),
                new MarkOperationContext(actionScheduler, nonogramState, this::addLog, this::setTmpLog)
        );

        NonogramFieldMarkHelper.markAvailableFieldsInLine(markContext);
    }

    public void excludeSequenceInColumn(int columnIdx, int seqIdx) {
        if (!this.getBoardAccessHelper().isColumnIndexValid(columnIdx)
                || this.columnsSequencesIdsNotToInclude.get(columnIdx).contains(seqIdx)) return;

        String marker = indexToSequenceCharMark(seqIdx);
        List<Integer> columnSeqRange = this.getColumnsSequencesRanges().get(columnIdx).get(seqIdx);

        IntStream.rangeClosed(columnSeqRange.get(0), columnSeqRange.get(1))
                .forEach(rowIdx -> markColumnBoardField(this.getNonogramSolutionBoardWithMarks(), rowIdx, columnIdx, marker));

        tmpLog = ExcludedSequenceLogHelper.generateLog(
                false, columnIdx, seqIdx, this.getColumnCopy(columnIdx),
                this.getNonogramRules().getColumnSequencesLengths().get(columnIdx),
                this.getColumnsSequencesRanges().get(columnIdx)
        );
        addLog();

        this.columnsSequencesIdsNotToInclude.get(columnIdx).add(seqIdx);
        Collections.sort(this.columnsSequencesIdsNotToInclude.get(columnIdx));
    }

    public void updateColumnSequenceRange(int columnIdx, int sequenceIdx, List<Integer> updatedRange) {
        this.columnsSequencesRanges.get(columnIdx).set(sequenceIdx, updatedRange);
    }

    private boolean sequenceShouldBeExcluded(int columnIdx, int sequenceIdx) {
        int sequenceLength = nonogramRules.getColumnSequencesLengths().get(columnIdx).get(sequenceIdx);
        List<Integer> sequenceRange = columnsSequencesRanges.get(columnIdx).get(sequenceIdx);

        return rangeLength(sequenceRange) == sequenceLength
                && allFieldsAreColouredInRowRange(columnIdx, sequenceRange, nonogramSolutionBoard);
    }

    public void changeColumnSequenceRange(int columnIndex, int sequenceIndex, List<Integer> updatedRange) {
        this.columnsSequencesRanges.get(columnIndex).set(sequenceIndex, updatedRange);
    }

    protected List<List<Integer>> getColumnSequencesRangesCopy(int columnIdx) {
        return this.getColumnsSequencesRanges().get(columnIdx).stream()
                .map(ArrayList::new)
                .collect(Collectors.toList());
    }
}