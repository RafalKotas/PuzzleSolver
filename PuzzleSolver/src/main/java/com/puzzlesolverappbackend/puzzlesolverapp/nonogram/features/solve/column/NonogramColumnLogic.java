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
import java.util.Arrays;
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

    private static final String CORRECT_COLUMN_SEQ_RANGE_MARKING_FIELD = "correcting column sequence range when marking field";

    private static final String FILL_OVERLAPPING_FIELDS = "fill overlapping fields";

    private static final List<Integer> NOT_FOUND_EMPTY_FIELDS_RANGE_VALUE = List.of(-1, -1);

    private static final List<Integer> NOT_FOUND_COLOURED_FIELDS_RANGE_VALUE = List.of(-1, -1);

    protected List<List<List<Integer>>> columnsSequencesRanges;

    protected List<List<Integer>> columnsFieldsNotToInclude;

    protected List<List<Integer>> columnsSequencesIdsNotToInclude;

    @JsonIgnore
    private final NonogramActionScheduler actionScheduler;

    @JsonIgnore
    private final NonogramBoardAccessHelper boardAccessHelper;

    @JsonIgnore
    private final ColumnColouringHelperImpl columnColouringHelper;

    @JsonIgnore
    private final ColumnXPlacementHelperImpl columnXPlacementHelper;

    @JsonIgnore
    private final ColumnSequencesCorrectionHelperImpl columnSequencesCorrectionHelper;

    @JsonIgnore
    private final NonogramFieldClearingHelper nonogramFieldClearingHelper;

    @JsonIgnore
    private final NonogramFieldExclusionHelperColumn nonogramFieldExclusionHelper;

    @JsonIgnore
    private final List<RefreshableColumnHelper> refreshables = new ArrayList<>();

    @JsonIgnore
    private final NonogramLogService logService;

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
        this.nonogramFieldClearingHelper = new NonogramFieldClearingHelper(this.getNonogramSolutionBoard(),
                this.getNonogramSolutionBoardWithMarks(),
                this.getBoardAccessHelper());
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
        this.nonogramFieldClearingHelper = new NonogramFieldClearingHelper(this.getNonogramSolutionBoard(),
                this.getNonogramSolutionBoardWithMarks(),
                this.getBoardAccessHelper());
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

    public void refreshHelpers() {
        for (RefreshableColumnHelper r : refreshables) {
            r.refreshFrom(this);
        }
    }

    public void setColumnSequencesRanges(int columnIdx, List<List<Integer>> ranges) {
        this.getColumnsSequencesRanges().set(columnIdx, ranges);
    }

    @Override
    public void correctColumnSequencesRanges(int columnIdx) {
        columnSequencesCorrectionHelper.correctColumnSequencesRanges(columnIdx);
    }

    @Override
    public void correctColumnSequencesRangesWhenMetColouredField (int columnIdx) {
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

    // colourFieldsInColumnIfXCausesAssignmentConflict

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

    @Override
    public void preventExtendingColouredSequenceToExcessLengthInColumn(int columnIdx) {
        preventExtendingColouredSequenceToExcessLengthInColumnToTop(columnIdx);
        preventExtendingColouredSequenceToExcessLengthInColumnToBottom(columnIdx);
    }

    private void preventExtendingColouredSequenceToExcessLengthInColumnToTop(int columnIdx) {
        List<String> columnBefore = getColumnCopy(columnIdx);
        List<List<Integer>> columnRangesBefore = getColumnSequencesRangesCopy(columnIdx);

        List<Integer> columnSequencesLengths = this.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);

        Field fieldToCheckX;

        int potentiallyColouredFieldRow;
        Field fieldToCheckColoured;

        List<Integer> sequencesIds;
        int maxSequenceLength;
        List<List<Integer>> colouredSequences;
        List<Integer> validSequencesIds;
        List<Integer> validSequenceLengths;

        for (int rowIdx = this.getNonogramRules().getHeight() - 1; rowIdx > 0; rowIdx--) {
            fieldToCheckX = new Field(rowIdx, columnIdx);
            if (isFieldWithX(this.getNonogramSolutionBoard(), fieldToCheckX)) {
                potentiallyColouredFieldRow = fieldToCheckX.getRowIdx() - 1;
                fieldToCheckColoured = new Field(potentiallyColouredFieldRow, fieldToCheckX.getColumnIdx());

                if (isFieldColoured(this.getNonogramSolutionBoard(), fieldToCheckColoured)) {
                    sequencesIds = sequencesIdsInColumnIncludingField(columnSequencesRanges, fieldToCheckColoured);

                    List<Integer> sequencesLengths = sequencesIds.stream().map(columnSequencesLengths::get).toList();

                    maxSequenceLength = Collections.max(sequencesLengths);

                    colouredSequences = getColouredSequencesRangesInColumnInRangeToTop(this.getNonogramSolutionBoard(), columnIdx, potentiallyColouredFieldRow, maxSequenceLength);

                    validSequencesIds = findValidSequencesIdsMergingToTop(sequencesIds, sequencesLengths, potentiallyColouredFieldRow, colouredSequences);

                    validSequenceLengths = validSequencesIds.stream()
                            .map(sequencesIds::indexOf)
                            .map(sequencesLengths::get)
                            .toList();

                    if (validSequenceLengths.stream().distinct().count() == 1) {
                        int sequenceLength = validSequenceLengths.get(0);
                        int colouredSequenceRowStartIdx = potentiallyColouredFieldRow - sequenceLength + 1;
                        Field fieldToColour;

                        List<String> columnAfterColouring;
                        List<String> columnAfterXPlacing;
                        List<String> columnAfterUpdate;

                        for (int rowToColourIdx = colouredSequenceRowStartIdx; rowToColourIdx <= potentiallyColouredFieldRow; rowToColourIdx++) {
                            fieldToColour = new Field(rowToColourIdx, columnIdx);
                            if (isFieldEmpty(this.nonogramSolutionBoard, fieldToColour)) {
                                this.getColumnColouringHelper().getColouringHelper().colourFieldAtGivenPosition(fieldToColour, "--C-");
                                actionScheduler.scheduleActionsBasedOnField(fieldToColour, NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART_IN_COLUMN);

                                this.nonogramState.increaseMadeSteps();

                                columnAfterColouring = getColumnCopy(columnIdx);
                                tmpLog = PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper
                                        .generateLog(
                                                false,
                                                columnIdx,
                                                "top",
                                                sequencesLengths,
                                                columnSequencesRanges,
                                                columnBefore,
                                                columnAfterColouring
                                        );
                                addLog();
                            }
                        }

                        Field fieldToPlaceX = new Field(colouredSequenceRowStartIdx - 1, columnIdx);
                        if (fieldToPlaceX.getRowIdx() >= 0 && isFieldEmpty(this.nonogramSolutionBoard, fieldToPlaceX)) {
                            this.getColumnXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(fieldToPlaceX);
                            this.nonogramState.increaseMadeSteps();

                            columnAfterXPlacing = getColumnCopy(columnIdx);
                            tmpLog = PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper
                                    .generateLog(
                                            false,
                                            columnIdx,
                                            "top",
                                            sequencesLengths,
                                            columnSequencesRanges,
                                            columnBefore,
                                            columnAfterXPlacing
                                    );
                            addLog();

                            this.getNonogramFieldExclusionHelper().excludeFieldInColumn(fieldToPlaceX);
                            actionScheduler.scheduleActionsBasedOnField(fieldToPlaceX, NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART_IN_COLUMN);
                        }

                        // moreover - only one id is valid -> can correct sequence range
                        if (validSequencesIds.size() == 1) {
                            int matchingSeqId = validSequencesIds.get(0);
                            List<Integer> oldRange = columnSequencesRanges.get(matchingSeqId);
                            List<Integer> updatedRange = new ArrayList<>(Arrays.asList(colouredSequenceRowStartIdx, potentiallyColouredFieldRow));

                            if (rangesNotEqual(oldRange, updatedRange)) {
                                this.updateColumnSequenceRange(columnIdx, matchingSeqId, updatedRange);
                                this.nonogramState.increaseMadeSteps();
                                List<List<Integer>> columnRangesAfter = this.getColumnsSequencesRanges().get(columnIdx);

                                if (sequenceShouldBeExcluded(columnIdx, matchingSeqId)) {
                                    excludeSequenceInColumn(columnIdx, matchingSeqId);
                                    this.nonogramState.increaseMadeSteps();
                                }

                                Field columnField = new Field(0, columnIdx);
                                actionScheduler.scheduleActionsBasedOnField(columnField, NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART_IN_COLUMN);

                                columnAfterUpdate = getColumnCopy(columnIdx);
                                tmpLog = generateLog(
                                        false,
                                        columnIdx,
                                        sequencesLengths,
                                        columnBefore,
                                        columnAfterUpdate,
                                        columnRangesBefore,
                                        columnRangesAfter
                                );
                                addLog();
                            }

                        }
                    }
                }
            }
        }
    }

    private void preventExtendingColouredSequenceToExcessLengthInColumnToBottom(int columnIdx) {
        List<String> columnBefore = getRowCopy(columnIdx);
        List<List<Integer>> columnRangesBefore = getColumnSequencesRangesCopy(columnIdx);

        List<Integer> columnSequencesLengths = this.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);

        Field fieldToCheckX;

        int potentiallyColouredFieldRowIndex;
        Field fieldToCheckColoured;

        List<Integer> sequencesIds;
        int maxSequenceLength;
        List<List<Integer>> colouredSequences;
        List<Integer> validSequencesIds;
        List<Integer> validSequenceLengths;

        for (int rowIdx = 0; rowIdx < this.getNonogramRules().getHeight() - 1; rowIdx++) {
            fieldToCheckX = new Field(rowIdx, columnIdx);

            if (isFieldWithX(this.getNonogramSolutionBoard(), fieldToCheckX)) {
                potentiallyColouredFieldRowIndex = fieldToCheckX.getRowIdx() + 1;
                fieldToCheckColoured = new Field(potentiallyColouredFieldRowIndex, fieldToCheckX.getColumnIdx());

                if (isFieldColoured(this.getNonogramSolutionBoard(), fieldToCheckColoured)) {
                    sequencesIds = sequencesIdsInColumnIncludingField(columnSequencesRanges, fieldToCheckColoured);

                    List<Integer> sequencesLengths = sequencesIds.stream().map(columnSequencesLengths::get).toList();

                    maxSequenceLength = Collections.max(sequencesLengths);

                    colouredSequences = getColouredSequencesRangesInColumnInRangeToBottom(this.getNonogramSolutionBoard(), columnIdx, potentiallyColouredFieldRowIndex, maxSequenceLength);

                    validSequencesIds = findValidSequencesIdsMergingToBottom(sequencesIds, sequencesLengths, potentiallyColouredFieldRowIndex, colouredSequences);

                    validSequenceLengths = validSequencesIds.stream()
                            .map(sequencesIds::indexOf)
                            .map(sequencesLengths::get)
                            .toList();

                    if (validSequenceLengths.stream().distinct().count() == 1) {
                        int sequenceLength = validSequenceLengths.get(0);
                        int colouredSequenceEndRowIndex = potentiallyColouredFieldRowIndex + sequenceLength - 1;
                        Field fieldToColour;

                        List<String> columnAfterColouring;
                        List<String> columnAfterXPlacing;
                        List<String> columnAfterUpdate;

                        for (int rowToColourIdx = potentiallyColouredFieldRowIndex; rowToColourIdx <= colouredSequenceEndRowIndex; rowToColourIdx++) {
                            fieldToColour = new Field(rowToColourIdx, columnIdx);
                            if (isFieldEmpty(this.nonogramSolutionBoard, fieldToColour)) {
                                this.getColumnColouringHelper().getColouringHelper().colourFieldAtGivenPosition(fieldToColour, "--C-");
                                this.nonogramState.increaseMadeSteps();
                                actionScheduler.scheduleActionsBasedOnField(fieldToColour, NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART_IN_COLUMN);

                                columnAfterColouring = getColumnCopy(columnIdx);
                                tmpLog = PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper
                                        .generateLog(
                                                false,
                                                columnIdx,
                                                "bottom",
                                                sequencesLengths,
                                                columnSequencesRanges,
                                                columnBefore,
                                                columnAfterColouring
                                        );
                                addLog();
                            }
                        }

                        Field fieldToPlaceX = new Field(colouredSequenceEndRowIndex + 1, columnIdx);
                        if (fieldToPlaceX.getRowIdx() < this.getNonogramRules().getHeight() && isFieldEmpty(this.nonogramSolutionBoard, fieldToPlaceX)) { // TODO - temp condition fieldToPlaceX.getRowIdx() < this.getNonogramRules().getHeight()
                            this.getColumnXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(fieldToPlaceX);
                            this.nonogramState.increaseMadeSteps();
                            columnAfterXPlacing = getRowCopy(rowIdx);
                            tmpLog = PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper
                                    .generateLog(
                                            false,
                                            columnIdx,
                                            "bottom",
                                            sequencesLengths,
                                            columnSequencesRanges,
                                            columnBefore,
                                            columnAfterXPlacing
                                    );
                            addLog();

                            this.getNonogramFieldExclusionHelper().excludeFieldInColumn(fieldToPlaceX);
                            actionScheduler.scheduleActionsBasedOnField(fieldToPlaceX, NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART_IN_COLUMN);
                        }

                        // moreover - only one id is valid -> can correct sequence range
                        if (validSequencesIds.size() == 1) {
                            int matchingSeqId = validSequencesIds.get(0);
                            List<Integer> oldRange = columnSequencesRanges.get(matchingSeqId);
                            List<Integer> updatedRange = new ArrayList<>(Arrays.asList(potentiallyColouredFieldRowIndex, colouredSequenceEndRowIndex));

                            if (rangesNotEqual(oldRange, updatedRange)) {
                                this.updateColumnSequenceRange(columnIdx, matchingSeqId, updatedRange);
                                this.nonogramState.increaseMadeSteps();
                                List<List<Integer>> columnRangesAfter = this.getColumnsSequencesRanges().get(columnIdx);

                                if (sequenceShouldBeExcluded(columnIdx, matchingSeqId)) {
                                    excludeSequenceInColumn(columnIdx, matchingSeqId);
                                }
                                Field columnField = new Field(0, columnIdx);
                                actionScheduler.scheduleActionsBasedOnField(columnField, NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART_IN_COLUMN);

                                columnAfterUpdate = getColumnCopy(columnIdx);
                                tmpLog = generateLog(
                                        false,
                                        columnIdx,
                                        sequencesLengths,
                                        columnBefore,
                                        columnAfterUpdate,
                                        columnRangesBefore,
                                        columnRangesAfter
                                );
                                addLog();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void markAvailableFieldsInColumn(int columnIdx) {
        MarkContext markContext = new MarkContext(
                new BoardContext(columnIdx,
                        false,
                        getNonogramRules(),
                        getNonogramSolutionBoard(),
                        getNonogramSolutionBoardWithMarks()),
                new SequencesContext(getNonogramRules().getColumnSequencesLengths(),
                        this.getColumnsSequencesRanges(),
                        this::changeColumnSequenceRange,
                        this::excludeSequenceInColumn),
                new MarkOperationContext(actionScheduler,
                        nonogramState,
                        this::addLog,
                        this::setTmpLog)
        );

        NonogramFieldMarkHelper.markAvailableFieldsInLine(markContext);
    }

    public void excludeSequenceInColumn(int columnIdx, int seqIdx) {
        if (!this.getBoardAccessHelper().isColumnIndexValid(columnIdx) || this.columnsSequencesIdsNotToInclude.get(columnIdx).contains(seqIdx)) return;

        String marker = indexToSequenceCharMark(seqIdx);
        List<Integer> columnSeqRange = this.getColumnsSequencesRanges().get(columnIdx).get(seqIdx);

        IntStream.rangeClosed(columnSeqRange.get(0), columnSeqRange.get(1))
                .forEach(rowIdx -> markColumnBoardField(this.getNonogramSolutionBoardWithMarks(), rowIdx, columnIdx, marker));

        tmpLog = ExcludedSequenceLogHelper.generateLog(
                false,
                columnIdx,
                seqIdx,
                this.getColumnCopy(columnIdx),
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

    public int minimumRowIndexWithoutX(int columnIdx, int lastSequenceRowIdx, int sequenceFullLength) {
        int minimumRowIndex = lastSequenceRowIdx;
        int minimumRowIndexLimit = Math.max(lastSequenceRowIdx - sequenceFullLength + 1, 0);
        Field fieldToCheck;

        for (; minimumRowIndex >= minimumRowIndexLimit; minimumRowIndex--) {
            fieldToCheck = new Field(minimumRowIndex, columnIdx);
            if (isFieldWithX(this.nonogramSolutionBoard, fieldToCheck)) {
                break;
            }
        }

        return minimumRowIndex + 1;
    }

    public int maximumRowIndexWithoutX(int columnIdx, int firstSequenceRowIdx, int sequenceFullLength) {
        int maximumRowIndex = firstSequenceRowIdx;
        int maximumRowIndexLimit = Math.min(firstSequenceRowIdx + sequenceFullLength - 1, this.getNonogramRules().getHeight() - 1);
        Field fieldToCheck;

        for (; maximumRowIndex <= maximumRowIndexLimit; maximumRowIndex++) {
            fieldToCheck = new Field(maximumRowIndex, columnIdx);
            if (isFieldWithX(this.nonogramSolutionBoard, fieldToCheck)) {
                break;
            }
        }

        return maximumRowIndex - 1;
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

    private String generateColourStepDescription(int columnIndex, int rowIndex, String actionType) {
        return String.format("COLUMN %d, ROW %d - field colouring - %s.", columnIndex, rowIndex, actionType);
    }

    private String generateCorrectingColumnSequenceRangeStepDescription(int columnIndex, int sequenceIndex, List<Integer> oldRange, List<Integer> correctedRange, String actionType) {
        return String.format("COLUMN %d, SEQUENCE %d - range correcting - from [%d, %d] to [%d, %d] - %s", columnIndex, sequenceIndex,
                oldRange.get(0), oldRange.get(1), correctedRange.get(0), correctedRange.get(1), actionType);
    }

    protected List<List<Integer>> getColumnSequencesRangesCopy(int columnIdx) {
        return this.getColumnsSequencesRanges().get(columnIdx).stream()
                .map(ArrayList::new)
                .collect(Collectors.toList());
    }
}
