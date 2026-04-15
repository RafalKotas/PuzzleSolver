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
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row.colour.RowColouringHelperImpl;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row.correction.RowSequencesCorrectionHelperImpl;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row.mixed.RowMixedActionsHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row.mixed.RowMixedActionsHelperImpl;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row.xplacement.RowXPlacementHelperImpl;
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
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.mark.NonogramFieldMarkHelper.markRowBoardField;

@Setter
@Getter
@Slf4j
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NonogramRowLogic extends NonogramLogicParams implements RowActions {

    // ------------------------------------------------------------------------
    // 🔹 Fields
    // ------------------------------------------------------------------------
    protected List<List<List<Integer>>> rowsSequencesRanges;
    protected List<List<Integer>> rowsFieldsNotToInclude;
    protected List<List<Integer>> rowsSequencesIdsNotToInclude;

    @JsonIgnore private final NonogramActionScheduler actionScheduler;
    @JsonIgnore private final NonogramBoardAccessHelper boardAccessHelper;
    @JsonIgnore private final RowColouringHelperImpl rowColouringHelper;
    @JsonIgnore private final RowXPlacementHelperImpl rowXPlacementHelper;
    @JsonIgnore private final RowSequencesCorrectionHelperImpl rowSequencesCorrectionHelper;
    @JsonIgnore private final RowMixedActionsHelper rowMixedActionsHelper;
    @JsonIgnore private final NonogramFieldClearingHelper nonogramFieldClearingHelper;
    @JsonIgnore private final NonogramFieldExclusionHelperRow nonogramFieldExclusionHelper;
    @JsonIgnore private final List<RefreshableRowHelper> refreshables = new ArrayList<>();

    private static final boolean IS_ROW = true;

    // ------------------------------------------------------------------------
    // 🔹 Constructors
    // ------------------------------------------------------------------------

    public NonogramRowLogic(NonogramLogic nonogramLogic,
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

        this.rowsSequencesRanges = nonogramLogic.getRowsSequencesRanges();
        this.rowsSequencesIdsNotToInclude = nonogramLogic.getRowsSequencesIdsNotToInclude();
        this.rowsFieldsNotToInclude = nonogramLogic.getRowsFieldsNotToInclude();

        this.nonogramSolutionBoardWithMarks = nonogramLogic.getNonogramSolutionBoardWithMarks();
        this.nonogramSolutionBoard = nonogramLogic.getNonogramSolutionBoard();
        this.actionsToDoList = nonogramLogic.getActionsToDoList();

        this.actionScheduler = actionScheduler;
        this.boardAccessHelper = accessHelper;

        this.rowColouringHelper = new RowColouringHelperImpl(this);
        this.rowXPlacementHelper = new RowXPlacementHelperImpl(this);
        this.rowSequencesCorrectionHelper = new RowSequencesCorrectionHelperImpl(this);
        this.rowMixedActionsHelper = new RowMixedActionsHelperImpl(this);

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

    // ------------------------------------------------------------------------
    // 🔹 Public methods
    // ------------------------------------------------------------------------

    public void refreshHelpers() {
        for (RefreshableRowHelper r : refreshables) {
            r.refreshFrom(this);
        }
    }

    public void setRowSequencesRanges(int rowIdx, List<List<Integer>> rowSequencesRanges) {
        this.getRowsSequencesRanges().set(rowIdx, rowSequencesRanges);
    }

    // ------------------------------------------------------------------------
    // 🔹 Row actions - correct ranges
    // ------------------------------------------------------------------------
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

    // ------------------------------------------------------------------------
    // 🟦 Row actions - colouring
    // ------------------------------------------------------------------------
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

    // ------------------------------------------------------------------------
    // 🗙 Row actions - placing X
    // ------------------------------------------------------------------------
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
        List<String> initialRow = getRowCopy(rowIdx);
        List<List<Integer>> initialSequencesRanges = deepCopy(rowsSequencesRanges.get(rowIdx));
        List<Integer> initialSequencesIdsNotToInclude = copyList(rowsSequencesIdsNotToInclude.get(rowIdx));
        int initialActionsToDoListSize = actionsToDoList.size();
        boolean placeXBecauseOfConflict;

        for (int columnIndex = 0; columnIndex < this.getNonogramRules().getWidth(); columnIndex++) {
            Field fieldToCheck = new Field(rowIdx, columnIndex);
            if (isFieldEmpty(this.getNonogramSolutionBoard(), fieldToCheck)) {
                this.getRowColouringHelper().getColouringHelper().colourFieldAtGivenPosition(fieldToCheck, "R---");

                placeXBecauseOfConflict = !checkIfRangesValidAfterCorrections(rowIdx) || !checkIfCanAssignAllColouredSequences(fieldToCheck);

                while (actionsToDoList.size() > initialActionsToDoListSize) {
                    actionsToDoList.remove(actionsToDoList.size() - 1);
                }

                if (!placeXBecauseOfConflict) {
                    nonogramFieldClearingHelper.clearField(fieldToCheck);
                } else {
                    this.getRowXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(fieldToCheck);
                    List<String> updatedRow = getRowCopy(rowIdx);

                    actionScheduler.scheduleActionsBasedOnField(fieldToCheck, NonogramSolveAction.PLACE_XS_IF_COLOURING_FIELD_WILL_CAUSE_ASSIGNMENT_CONFLICT_IN_ROW);
                    initialActionsToDoListSize = actionsToDoList.size();

                    ColouringGenerateLogBaseContext logContext = new ColouringGenerateLogBaseContext(
                            IS_ROW,
                            rowIdx,
                            initialRow,
                            updatedRow,
                            initialSequencesRanges,
                            getNonogramRules().getRowSequencesLengths().get(rowIdx)
                    );
                    String tmpLog = PlaceXIfOWillCauseAssignmentConflictLogHelper.generateLog(
                            logContext
                    );
                    setAndAddLog(tmpLog);
                    this.nonogramState.increaseMadeSteps();
                }

                rowsSequencesRanges.set(rowIdx, new ArrayList<>(initialSequencesRanges));
                rowsSequencesIdsNotToInclude.set(rowIdx, new ArrayList<>(initialSequencesIdsNotToInclude));
            }
        }
    }

    private boolean checkIfRangesValidAfterCorrections(int rowIdx) {
        boolean allStepsCorrect = true;

        correctRowSequencesRangesWhenMetColouredField(rowIdx);
        allStepsCorrect &= areRangesValidAfterCorrectingStep(rowIdx);

        correctRowSequencesRangesWhenMatchingFieldsToSequences(rowIdx);
        allStepsCorrect &= areRangesValidAfterCorrectingStep(rowIdx);

        correctRowSequencesRangesIfXOnWay(rowIdx);
        allStepsCorrect &= areRangesValidAfterCorrectingStep(rowIdx);

        correctRowSequencesRanges(rowIdx);
        allStepsCorrect &= areRangesValidAfterCorrectingStep(rowIdx);

        correctRowSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(rowIdx); // TODO - return false after first allStepsCorrect change
        allStepsCorrect &= areRangesValidAfterCorrectingStep(rowIdx);

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
            boolean canAssign = colouredSequenceCanBeAssignedToAnyRange(seq, fieldToCheck.getRowIdx());
            if (!canAssign) {
                this.getRowXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(fieldToCheck);
                canAssignAll = false;
                actionScheduler.scheduleActionsBasedOnField(
                        fieldToCheck,
                        NonogramSolveAction.PLACE_XS_IF_COLOURING_FIELD_WILL_CAUSE_ASSIGNMENT_CONFLICT_IN_ROW
                );
                this.nonogramState.increaseMadeSteps();
                break;
            }
        }

        return canAssignAll;
    }

    private boolean areRangesValidAfterCorrectingStep(int rowIdx) {
        for (List<Integer> range : this.getRowsSequencesRanges().get(rowIdx)) {
            int rangeStart = range.get(0);
            int rangeEnd = range.get(1);
            if (rangeStart > rangeEnd || rangeStart < 0) {
                return false;
            }
        }

        return true;
    }

    private boolean colouredSequenceCanBeAssignedToAnyRange(List<Integer> colouredSequence, int rowIdx) {
        return rowsSequencesRanges.get(rowIdx).stream().anyMatch(range -> rangeInsideAnotherRange(colouredSequence, range));
    }

    // ------------------------------------------------------------------------
    // 🧩 Row actions - overextension prevention
    // ------------------------------------------------------------------------
    @Override
    public void preventExtendingColouredSequenceToExcessLengthInRow(int rowIdx) {
        rowMixedActionsHelper.preventExtendingColouredSequenceToExcessLengthInRow(rowIdx);
    }

    // ------------------------------------------------------------------------
    // 🏷️ Field marking & exclusion
    // ------------------------------------------------------------------------

    @Override
    public void markAvailableFieldsInRow(int rowIdx) {
        MarkContext markContext = new MarkContext(
                new BoardContext(rowIdx, true,
                        getNonogramRules(),
                        getNonogramSolutionBoard(),
                        getNonogramSolutionBoardWithMarks()),
                new SequencesContext(getNonogramRules().getRowSequencesLengths(),
                        getRowsSequencesRanges(),
                        this::updateRowSequenceRange,
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
                IS_ROW, rowIdx, seqIdx, this.getRowCopy(rowIdx),
                this.getNonogramRules().getRowSequencesLengths().get(rowIdx),
                this.getRowsSequencesRanges().get(rowIdx));
        addLog();

        this.rowsSequencesIdsNotToInclude.get(rowIdx).add(seqIdx);
        Collections.sort(this.rowsSequencesIdsNotToInclude.get(rowIdx));
    }

    public void updateRowSequenceRange(int rowIdx, int sequenceIdx, List<Integer> updatedRange) {
        this.rowsSequencesRanges.get(rowIdx).set(sequenceIdx, new ArrayList<>(updatedRange));
    }

    private void setAndAddLog(String tmpLog) {
        setTmpLog(tmpLog);
        addLog();
    }
}