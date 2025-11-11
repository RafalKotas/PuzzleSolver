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
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.colour.*;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.correction.ColumnSequencesCorrectionHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.correction.ColumnSequencesCorrectionHelperImpl;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.mixed.ColumnMixedActionsHelperImpl;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column.xplacement.ColumnXPlacementHelperImpl;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.ColouringGenerateLogBaseContext;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.exclusion.ExcludedSequenceLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement.PlaceXIfOWillCauseAssignmentConflictLogHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.*;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.indexToSequenceCharMark;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.isFieldEmpty;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.TooLongMergeFieldHelper.collectColouredSequencesRanges;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.mark.NonogramFieldMarkHelper.markColumnBoardField;

@Getter
@Setter
@Slf4j
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NonogramColumnLogic extends NonogramLogicParams implements ColumnActions {

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
    @JsonIgnore private final ColumnMixedActionsHelperImpl columnMixedActionsHelper;
    @JsonIgnore private final NonogramFieldClearingHelper nonogramFieldClearingHelper;
    @JsonIgnore private final NonogramFieldExclusionHelperColumn nonogramFieldExclusionHelper;
    @JsonIgnore private final List<RefreshableColumnHelper> refreshables = new ArrayList<>();

    private static final boolean IS_ROW = false;

    // ------------------------------------------------------------------------
    // 🔹 Constructors
    // ------------------------------------------------------------------------

    public NonogramColumnLogic(NonogramLogic nonogramLogic,
                               NonogramBoardAccessHelper accessHelper,
                               NonogramActionScheduler actionScheduler) {
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

        this.actionScheduler = actionScheduler;
        this.boardAccessHelper = accessHelper;

        this.columnColouringHelper = new ColumnColouringHelperImpl(this);
        this.columnXPlacementHelper = new ColumnXPlacementHelperImpl(this);
        this.columnSequencesCorrectionHelper = new ColumnSequencesCorrectionHelperImpl(this);
        this.columnMixedActionsHelper = new ColumnMixedActionsHelperImpl(this);

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

    // ------------------------------------------------------------------------
    // 🔹 Column actions - correct ranges
    // ------------------------------------------------------------------------
    @Override
    public void correctColumnSequencesRanges(int columnIdx) {
        columnSequencesCorrectionHelper.correctColumnSequencesRanges(columnIdx);
    }

    @Override
    public void correctColumnSequencesRangesWhenMetColouredField(int columnIdx) {
        columnSequencesCorrectionHelper.correctColumnSequencesRangesWhenMetColouredField(columnIdx);
    }

    @Override
    public void correctColumnSequencesRangesIfXOnWay(int columnIdx) {
        columnSequencesCorrectionHelper.correctColumnSequencesRangesIfXOnWay(columnIdx);
    }

    @Override
    public void correctColumnSequencesRangesWhenMatchingFieldsToSequences(int columnIdx) {
        columnSequencesCorrectionHelper.correctColumnSequencesRangesWhenMatchingFieldsToSequences(columnIdx);
    }

    @Override
    public void correctColumnSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(int columnIdx) {
        columnSequencesCorrectionHelper.correctColumnSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(columnIdx);
    }

    // ------------------------------------------------------------------------
    // 🟦 Column actions - colouring
    // ------------------------------------------------------------------------
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

    // ------------------------------------------------------------------------
    // 🗙 Column actions - placing X
    // ------------------------------------------------------------------------
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

    // tmp start
    @Override
    public void placeXsColumnIfColouringFieldWillCauseAssignmentConflict(int columnIdx) {
        List<String> initialColumn = getColumnCopy(columnIdx);
        List<List<Integer>> initialSequencesRanges = deepCopy(columnsSequencesRanges.get(columnIdx));
        List<Integer> initialSequencesIdsNotToInclude = copyList(columnsSequencesIdsNotToInclude.get(columnIdx));
        int initialActionsToDoListSize = actionsToDoList.size();
        boolean placeXBecauseOfConflict;

        for (int rowIndex = 0; rowIndex < this.getNonogramRules().getWidth(); rowIndex++) {
            Field fieldToCheck = new Field(columnIdx, rowIndex);
            if (isFieldEmpty(this.getNonogramSolutionBoard(), fieldToCheck)) {
                this.getColumnColouringHelper().getColouringHelper().colourFieldAtGivenPosition(fieldToCheck, "--C-");

                placeXBecauseOfConflict = !checkIfRangesValidAfterCorrections(columnIdx) || !checkIfCanAssignAllColouredSequences(fieldToCheck);

                while (actionsToDoList.size() > initialActionsToDoListSize) {
                    actionsToDoList.remove(actionsToDoList.size() - 1);
                }

                if (!placeXBecauseOfConflict) {
                    nonogramFieldClearingHelper.clearField(fieldToCheck);
                } else {
                    this.getColumnXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(fieldToCheck);
                    List<String> updatedColumn = getColumnCopy(columnIdx);

                    actionScheduler.scheduleActionsBasedOnField(fieldToCheck, NonogramSolveAction.PLACE_XS_IF_COLOURING_FIELD_WILL_CAUSE_ASSIGNMENT_CONFLICT_IN_COLUMN);

                    ColouringGenerateLogBaseContext logContext = new ColouringGenerateLogBaseContext(
                            IS_ROW,
                            columnIdx,
                            initialColumn,
                            updatedColumn,
                            initialSequencesRanges,
                            getNonogramRules().getColumnSequencesLengths().get(columnIdx)
                    );
                    String tmpLog = PlaceXIfOWillCauseAssignmentConflictLogHelper.generateLog(
                            logContext
                    );
                    setAndAddLog(tmpLog);
                    this.nonogramState.increaseMadeSteps();
                }

                columnsSequencesRanges.set(columnIdx, new ArrayList<>(initialSequencesRanges));
                columnsSequencesIdsNotToInclude.set(columnIdx, new ArrayList<>(initialSequencesIdsNotToInclude));
            }
        }
    }

    private boolean checkIfRangesValidAfterCorrections(int columnIdx) {
        boolean allStepsCorrect = true;

        correctColumnSequencesRangesWhenMetColouredField(columnIdx);
        allStepsCorrect &= areRangesValidAfterCorrectingStep(columnIdx);

        correctColumnSequencesRangesWhenMatchingFieldsToSequences(columnIdx);
        allStepsCorrect &= areRangesValidAfterCorrectingStep(columnIdx);

        correctColumnSequencesRangesIfXOnWay(columnIdx);
        allStepsCorrect &= areRangesValidAfterCorrectingStep(columnIdx);

        correctColumnSequencesRanges(columnIdx);
        allStepsCorrect &= areRangesValidAfterCorrectingStep(columnIdx);

        correctColumnSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(columnIdx); // TODO - return false after first allStepsCorrect change
        allStepsCorrect &= areRangesValidAfterCorrectingStep(columnIdx);

        return allStepsCorrect;
    }

    public boolean checkIfCanAssignAllColouredSequences(Field fieldToCheck) {
        boolean canAssignAll = true;
        List<List<Integer>> colouredSequencesInRow = collectColouredSequencesRanges(
                this.getNonogramSolutionBoard(),
                fieldToCheck.getRowIdx(),
                IS_ROW
        );

        for (List<Integer> seq : colouredSequencesInRow) {
            boolean canAssign = colouredSequenceCanBeAssignedToAnyRange(seq, fieldToCheck.getColumnIdx());
            if (!canAssign) {
                this.getColumnXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(fieldToCheck);
                canAssignAll = false;
                actionScheduler.scheduleActionsBasedOnField(
                        fieldToCheck,
                        NonogramSolveAction.PLACE_XS_IF_COLOURING_FIELD_WILL_CAUSE_ASSIGNMENT_CONFLICT_IN_COLUMN
                );
                this.nonogramState.increaseMadeSteps();
                break;
            }
        }

        return canAssignAll;
    }

    private boolean areRangesValidAfterCorrectingStep(int columnIdx) {
        for (List<Integer> range : this.getColumnsSequencesRanges().get(columnIdx)) {
            int rangeStart = range.get(0);
            int rangeEnd = range.get(1);
            if (rangeStart > rangeEnd || rangeStart < 0) {
                return false;
            }
        }

        return true;
    }

    private boolean colouredSequenceCanBeAssignedToAnyRange(List<Integer> colouredSequence, int columnIdx) {
        return columnsSequencesRanges.get(columnIdx).stream().anyMatch(range -> rangeInsideAnotherRange(colouredSequence, range));
    }
    // tmp end

    // ------------------------------------------------------------------------
    // 🧩 Column actions - overextension prevention
    // ------------------------------------------------------------------------
    @Override
    public void preventExtendingColouredSequenceToExcessLengthInColumn(int columnIdx) {
        columnMixedActionsHelper.preventExtendingColouredSequenceToExcessLengthInColumn(columnIdx);
    }

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
                        this::updateColumnSequenceRange,
                        this::excludeSequenceInColumn),
                new MarkOperationContext(actionScheduler, nonogramState, this::addLog, this::setTmpLog)
        );

        NonogramFieldMarkHelper.markAvailableFieldsInLine(markContext);
    }

    // === SUPPORT METHODS ===

    public void excludeSequenceInColumn(int columnIdx, int seqIdx) {
        if (!this.getBoardAccessHelper().isColumnIndexValid(columnIdx)
                || this.columnsSequencesIdsNotToInclude.get(columnIdx).contains(seqIdx)) return;

        String marker = indexToSequenceCharMark(seqIdx);
        List<Integer> columnSeqRange = this.getColumnsSequencesRanges().get(columnIdx).get(seqIdx);

        IntStream.rangeClosed(columnSeqRange.get(0), columnSeqRange.get(1))
                .forEach(rowIdx -> markColumnBoardField(this.getNonogramSolutionBoardWithMarks(), rowIdx, columnIdx, marker));

        tmpLog = ExcludedSequenceLogHelper.generateLog(
                IS_ROW, columnIdx, seqIdx, this.getColumnCopy(columnIdx),
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

    private void setAndAddLog(String tmpLog) {
        setTmpLog(tmpLog);
        addLog();
    }
}