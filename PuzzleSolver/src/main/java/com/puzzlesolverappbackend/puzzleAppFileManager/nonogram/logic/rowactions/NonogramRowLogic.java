package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.Field;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.NonogramLogicParams;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.common.NonogramFieldClearingHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.common.NonogramFieldExclusionHelperRow;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve.NonogramActionScheduler;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve.NonogramBoardAccessHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve.colouring.RowColouringHelperImpl;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve.mark.NonogramFieldMarkHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve.range.RowSequencesCorrectionHelperImpl;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve.xplacement.RowXPlacementHelperImpl;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.service.NonogramLogService;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.exclusion.ExcludedSequenceLogHelper.generateExcludedSequenceLog;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve.mark.NonogramFieldMarkHelper.markRowBoardField;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.RowPreventExtendingColouredSequenceToExcessLengthHelpers.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramBoardUtils.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramParametersComparatorHelper.rangesNotEqual;

@Setter
@Getter
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
public class NonogramRowLogic extends NonogramLogicParams implements RowActions {

    private final static String CORRECTING_ROW_SEQ_RANGE_MARKING_FIELD = "correcting row sequence range when marking field";

    private final static String FILL_OVERLAPPING_FIELDS = "fill overlapping fields";

    private final static List<Integer> NOT_FOUND_EMPTY_FIELDS_RANGE_VALUE = List.of(-1, -1);

    private final static List<Integer> NOT_FOUND_COLOURED_FIELDS_RANGE_VALUE = List.of(-1, -1);

    protected List<List<List<Integer>>> rowsSequencesRanges;
    protected List<List<Integer>> rowsFieldsNotToInclude;
    protected List<List<Integer>> rowsSequencesIdsNotToInclude;

    @JsonIgnore
    private final NonogramActionScheduler actionScheduler;

    @JsonIgnore
    private final NonogramBoardAccessHelper boardAccessHelper;

    @JsonIgnore
    private final RowColouringHelperImpl rowColouringHelper;

    @JsonIgnore
    private final RowXPlacementHelperImpl rowXPlacementHelper;

    @JsonIgnore
    private final RowSequencesCorrectionHelperImpl rowSequencesCorrectionHelper;

    @JsonIgnore
    private final NonogramFieldClearingHelper nonogramFieldClearingHelper;

    @JsonIgnore
    private final NonogramFieldExclusionHelperRow nonogramFieldExclusionHelper;

    @JsonIgnore
    private final NonogramLogService logService;

    public NonogramRowLogic() {
        this.actionScheduler = new NonogramActionScheduler(this.getActionsToDoList());
        this.boardAccessHelper = new NonogramBoardAccessHelper(this.getNonogramSolutionBoard());
        this.rowColouringHelper = new RowColouringHelperImpl(this);
        this.rowXPlacementHelper = new RowXPlacementHelperImpl(this);
        this.rowSequencesCorrectionHelper = new RowSequencesCorrectionHelperImpl(this);
        this.nonogramFieldClearingHelper = new NonogramFieldClearingHelper(this.getNonogramSolutionBoard(),
                this.getNonogramSolutionBoardWithMarks(),
                this.getBoardAccessHelper());
        this.nonogramFieldExclusionHelper = new NonogramFieldExclusionHelperRow(
                this.rowsFieldsNotToInclude,
                boardAccessHelper
        );

        this.logService = new NonogramLogService();
    }

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
        this.nonogramFieldClearingHelper = new NonogramFieldClearingHelper(this.getNonogramSolutionBoard(),
                this.getNonogramSolutionBoardWithMarks(),
                this.getBoardAccessHelper());
        this.nonogramFieldExclusionHelper = new NonogramFieldExclusionHelperRow(
                this.rowsFieldsNotToInclude,
                boardAccessHelper
        );

        this.logService = new NonogramLogService();
    }

    public NonogramRowLogic(NonogramLogic logic, NonogramBoardAccessHelper accessHelper) {
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

        this.actionScheduler = new NonogramActionScheduler(this.getActionsToDoList());
        this.boardAccessHelper = accessHelper;
        this.rowColouringHelper = new RowColouringHelperImpl(this);
        this.rowXPlacementHelper = new RowXPlacementHelperImpl(this);
        this.rowSequencesCorrectionHelper = new RowSequencesCorrectionHelperImpl(this);
        this.nonogramFieldClearingHelper = new NonogramFieldClearingHelper(this.getNonogramSolutionBoard(),
                this.getNonogramSolutionBoardWithMarks(),
                this.getBoardAccessHelper());
        this.nonogramFieldExclusionHelper = new NonogramFieldExclusionHelperRow(
                this.rowsFieldsNotToInclude,
                boardAccessHelper
        );

        this.logService = new NonogramLogService();
    }

    public void setRowSequencesRanges(int rowIdx, List<List<Integer>> rowSequencesRanges) {
        this.getRowsSequencesRanges().set(rowIdx, rowSequencesRanges);
    }

    @Override
    public void correctRowSequencesRanges(int rowIdx) {
        rowSequencesCorrectionHelper.correctRowSequencesRanges(rowIdx);
    }

    @Override
    public void correctRowSequencesRangesWhenMetColouredField (int rowIdx) {
        rowSequencesCorrectionHelper.correctRowSequencesRangesWhenMetColouredField(rowIdx);
    }

    @Override
    public void correctRowSequencesRangesIfXOnWay(int rowIdx, boolean changeLogicDetails) {
        rowSequencesCorrectionHelper.correctRowSequencesRangesIfXOnWay(rowIdx, changeLogicDetails);
    }

    @Override
    public void correctRowSequencesRangesWhenMatchingFieldsToSequences(int rowIdx) {
        rowSequencesCorrectionHelper.correctRowSequencesRangesWhenMatchingFieldsToSequences(rowIdx);
    }

    @Override
    public void correctRowSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(int rowIdx) {
        rowSequencesCorrectionHelper.correctRowSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(rowIdx);
    }

    @Override
    public void colourOverlappingFieldsInRow(int rowIdx) {
        rowColouringHelper.colourOverlappingFieldsInRow(rowIdx);
    }

    @Override
    public void colourFieldsIfInRowXWouldForceTooLongColouredFieldsSequence(int rowIdx) {
        rowColouringHelper.colourFieldsIfXWouldForceTooLongColouredFieldsSequence(rowIdx);
    }

    @Override
    public void extendColouredFieldsNearXToMaximumPossibleLengthInRow(int rowIdx) {
        rowColouringHelper.extendColouredFieldsNearXToMaximumPossibleLengthInRow(rowIdx);
    }

//    @Override
//    public void colourFieldsInRowIfXCausesAssignmentConflict(int rowIdx) {
//        List<String> boardRow = this.getNonogramSolutionBoard().get(rowIdx);
//        List<List<Integer>> oldRowSequencesRanges = cloneAndMakeImmutable2DList(this.getRowsSequencesRanges().get(rowIdx));
//        List<List<Integer>> colouredFieldsInRowRanges = collectColouredSequencesRanges(this.getNonogramSolutionBoard(),
//                rowIdx,
//                true
//        );
//
//        // no X fields between ranges to check if placement is wrong
//        if (colouredFieldsInRowRanges.size() < 2) return;
//
//        for (int i = 0; i < colouredFieldsInRowRanges.size() - 1; i++) {
//            int leftEnd = colouredFieldsInRowRanges.get(i).get(1);
//            int rightStart = colouredFieldsInRowRanges.get(i + 1).get(0);
//
//            for (int betweenColumnIdx = leftEnd + 1; betweenColumnIdx < rightStart; betweenColumnIdx++) {
//                if (!Objects.equals(boardRow.get(betweenColumnIdx), EMPTY_FIELD)) continue;
//
//                Field fieldToCheck = new Field(rowIdx, betweenColumnIdx);
//                if (isFieldEmpty(this.getNonogramSolutionBoard(), fieldToCheck)) {
//                    this.getRowXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(fieldToCheck, false);
//                    correctRowSequencesRangesIfXOnWay(rowIdx, false);
//                    clearField(fieldToCheck);
//                }
//                List<List<Integer>> updatedRanges = this.getRowsSequencesRanges().get(rowIdx);
//
//                boolean conflict = false;
//
//                // check if any coloured sequence part not matches into any range
//                for (List<Integer> colouredRange : colouredFieldsInRowRanges) {
//                    boolean matchesAny = false;
//                    for (List<Integer> updatedRange : updatedRanges) {
//                        if (rangeInsideAnotherRange(colouredRange, updatedRange)) {
//                            matchesAny = true;
//                            break;
//                        }
//                    }
//                    if (!matchesAny) {
//                        conflict = true;
//                        break;
//                    }
//                }
//
//                // restore old row ranges
//                setRowSequencesRanges(rowIdx, mutableClone2DList(oldRowSequencesRanges));
//
//                if (conflict) {
//                    this.getRowColouringHelper().getColouringHelper().colourFieldAtGivenPosition(fieldToCheck, "R---");
//                    actionScheduler.scheduleActionsBasedOnField(fieldToCheck,
//                            NonogramSolveAction.COLOUR_FIELDS_IN_ROW_IF_X_CAUSES_ASSIGNMENT_CONFLICT);
//                    this.nonogramState.increaseMadeSteps();
//                    tmpLog = "Field at (" + rowIdx + ", " + betweenColumnIdx + ") must be 'O' because 'X' would cause a conflict in sequences.";
//                    addLog();
//                }
//            }
//        }
//    }

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
    public void preventExtendingColouredSequenceToExcessLengthInRow(int rowIdx) {
        preventExtendingColouredSequenceToExcessLengthInRowToLeft(rowIdx);
        preventExtendingColouredSequenceToExcessLengthInRowToRight(rowIdx);
    }

    /***
     EXAMPLE: case o10401 - start from column 18
     ids: [0, 1, 2, 3, 4 | 5, 6, 7, 8, 9 |10,11,12,13,14 |15,16,17,18,19 |20,21,22,23,24 |25,26,27,28,29 |30,31,32,33,34 |35,36,37,38,39 ]
     row 8: [-, -, -, -, - | -, -, -, -, - | -, -, -, -, O | O, -, -, O, X | X, X, X, X, X | O, O, O, X, - | X, -, -, -, X | X, X, O, O, O ]
     rowSequencesLengths [1      , 4(chk), 3(chk)  , 1(chk)  , 3       , 3       ]
     (4): [-, -, -, -, - | -, -, -, -, - | -, -, -, -, O | O, #, #, O, X | X, X, X, X, X | O, O, O, X, - | X, -, -, -, X | X, X, O, O, O ] - too long
     (3): [-, -, -, -, - | -, -, -, -, - | -, -, -, -, O | O, #, #, O, X | X, X, X, X, X | O, O, O, X, - | X, -, -, -, X | X, X, O, O, O ] - too long
     (1): [-, -, -, -, - | -, -, -, -, - | -, -, -, -, O | O, -, X, O, X | X, X, X, X, X | O, O, O, X, - | X, -, -, -, X | X, X, O, O, O ] - ok
     rowSequencesRanges [[0, 13], [2, 18], [13, 27], [18, 29], [25, 33], [37, 39]]

     |10,11,12,13,14 |15,16,17,18,19 |
     | -, -, -, -, O | O, -, -, O, X |
     Sequences which have ranges in area: [4, 3, 1] -> only possible is seq with length 1

     TODO - check also case with sequence with length more than 1 (colour and place X)
     ***/
    private void preventExtendingColouredSequenceToExcessLengthInRowToLeft(int rowIdx) {
        List<Integer> rowSequencesLengths = this.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);

        Field fieldToCheckX;

        int potentiallyColouredFieldColumn;
        Field fieldToCheckColoured;

        List<Integer> sequencesIds;
        int maxSequenceLength;
        List<List<Integer>> colouredSequences;
        List<Integer> validSequenceIds;
        List<Integer> validSequenceLengths;

        for (int columnIdx = this.getNonogramRules().getWidth() - 1; columnIdx > 0; columnIdx--) {
            fieldToCheckX = new Field(rowIdx, columnIdx);
            if (isFieldWithX(this.getNonogramSolutionBoard(), fieldToCheckX)) {
                potentiallyColouredFieldColumn = fieldToCheckX.getColumnIdx() - 1;
                fieldToCheckColoured = new Field(fieldToCheckX.getRowIdx(), potentiallyColouredFieldColumn);

                if (isFieldColoured(this.getNonogramSolutionBoard(), fieldToCheckColoured)) {
                    sequencesIds = sequencesIdsInRowIncludingField(rowSequencesRanges, fieldToCheckColoured);

                    List<Integer> sequencesLengths = sequencesIds.stream().map(rowSequencesLengths::get).toList();

                    maxSequenceLength = Collections.max(sequencesLengths);

                    colouredSequences = getColouredSequencesRangesInRowInRangeOnLeft(this.getNonogramSolutionBoard(), rowIdx, potentiallyColouredFieldColumn, maxSequenceLength);

                    validSequenceIds = findValidSequencesIdsMergingToLeft(sequencesIds, sequencesLengths, potentiallyColouredFieldColumn, colouredSequences);

                    validSequenceLengths = validSequenceIds.stream()
                            .map(sequencesIds::indexOf)
                            .map(sequencesLengths::get)
                            .toList();

                    // only one length is valid
                    if (validSequenceLengths.stream().distinct().count() == 1) {
                        int sequenceLength = validSequenceLengths.get(0);
                        int colouredSequenceColStartIdx = potentiallyColouredFieldColumn - sequenceLength + 1;
                        Field fieldToColour;

                        for (int columnToColourIdx = colouredSequenceColStartIdx; columnToColourIdx <= potentiallyColouredFieldColumn; columnToColourIdx++) {
                            fieldToColour = new Field(rowIdx, columnToColourIdx);
                            if (isFieldEmpty(this.getNonogramSolutionBoard(), fieldToColour)) {
                                this.getRowColouringHelper().getColouringHelper().colourFieldAtGivenPosition(fieldToColour, "R---");
                                actionScheduler.scheduleActionsBasedOnField(fieldToColour, NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART);

                                this.nonogramState.increaseMadeSteps();
                                tmpLog = generateColourStepDescription(rowIdx, columnToColourIdx, "extend coloured sequence to matching length to left near X (with placing X before)");
                                addLog();
                            }
                        }

                        Field fieldToPlaceX = new Field(rowIdx, colouredSequenceColStartIdx - 1);
                        this.getRowXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(fieldToPlaceX, true);
                        actionScheduler.scheduleActionsBasedOnField(fieldToPlaceX, NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART);

                        // moreover - only one id is valid -> can correct sequence range
                        if (validSequenceIds.size() == 1) {
                            int matchingSeqId = validSequenceIds.get(0);
                            List<Integer> oldRange = rowSequencesRanges.get(matchingSeqId);
                            List<Integer> updatedRange = new ArrayList<>(Arrays.asList(colouredSequenceColStartIdx, potentiallyColouredFieldColumn));

                            if (rangesNotEqual(oldRange, updatedRange)) {
                                this.updateRowSequenceRange(rowIdx, matchingSeqId, updatedRange);
                                Field rowField = new Field(rowIdx, 0);
                                actionScheduler.scheduleActionsBasedOnField(rowField, NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART);

                                this.nonogramState.increaseMadeSteps();
                                tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, matchingSeqId, oldRange, updatedRange, "update only matching sequence part preventing excess length to left");
                                addLog();
                            }
                        }
                    }
                }
            }
        }
    }

    private void preventExtendingColouredSequenceToExcessLengthInRowToRight(int rowIdx) {
        List<Integer> rowSequencesLengths = this.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);

        Field fieldToCheckX;

        int potentiallyColouredFieldColumnIndex;
        Field fieldToCheckColoured;

        List<Integer> sequencesIds;
        int maxSequenceLength;
        List<List<Integer>> colouredSequences;
        List<Integer> validSequencesIds;
        List<Integer> validSequenceLengths;

        for (int columnIdx = 0; columnIdx < this.getNonogramRules().getWidth() - 1; columnIdx++) {
            fieldToCheckX = new Field(rowIdx, columnIdx);
            if (isFieldWithX(this.getNonogramSolutionBoard(), fieldToCheckX)) {
                potentiallyColouredFieldColumnIndex = fieldToCheckX.getColumnIdx() + 1;
                fieldToCheckColoured = new Field(fieldToCheckX.getRowIdx(), potentiallyColouredFieldColumnIndex);

                if (isFieldColoured(this.getNonogramSolutionBoard(), fieldToCheckColoured)) {
                    sequencesIds = sequencesIdsInRowIncludingField(rowSequencesRanges, fieldToCheckColoured);

                    List<Integer> sequencesLengths = sequencesIds.stream().map(rowSequencesLengths::get).toList();

                    maxSequenceLength = Collections.max(sequencesLengths);

                    colouredSequences = getColouredSequencesRangesInRowInRangeOnRight(this.getNonogramSolutionBoard(), rowIdx, potentiallyColouredFieldColumnIndex, maxSequenceLength);

                    validSequencesIds = findValidSequencesIdsMergingToRight(sequencesIds, sequencesLengths, potentiallyColouredFieldColumnIndex, colouredSequences);

                    validSequenceLengths = validSequencesIds.stream()
                            .map(sequencesIds::indexOf)
                            .map(sequencesLengths::get)
                            .toList();

                    if (validSequenceLengths.stream().distinct().count() == 1) {
                        int sequenceLength = validSequenceLengths.get(0);
                        int colouredSequenceEndColumnIndex = potentiallyColouredFieldColumnIndex + sequenceLength - 1;
                        Field fieldToColour;

                        for (int columnToColourIdx = potentiallyColouredFieldColumnIndex; columnToColourIdx <= colouredSequenceEndColumnIndex; columnToColourIdx++) {
                            fieldToColour = new Field(rowIdx, columnToColourIdx);
                            if (isFieldEmpty(this.getNonogramSolutionBoard(), fieldToColour)) {
                                this.getRowColouringHelper().getColouringHelper().colourFieldAtGivenPosition(fieldToColour, "R---");
                                actionScheduler.scheduleActionsBasedOnField(fieldToColour, NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART);

                                this.nonogramState.increaseMadeSteps();

                                tmpLog = generateColourStepDescription(rowIdx, columnToColourIdx, "extend coloured sequence to matching length to right near X (with placing X before)");
                                addLog();
                            }
                        }

                        Field fieldToPlaceX = new Field(rowIdx, colouredSequenceEndColumnIndex + 1);
                        if (fieldToPlaceX.getColumnIdx() < this.getNonogramRules().getWidth() && isFieldEmpty(this.nonogramSolutionBoard, fieldToPlaceX)) {
                            this.getRowXPlacementHelper().getNonogramFieldPlacingXHelper().placeXAtGivenField(fieldToPlaceX, true);
                            actionScheduler.scheduleActionsBasedOnField(fieldToPlaceX, NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART);
                        }

                        if (validSequencesIds.size() == 1) {
                            int matchingSeqId = validSequencesIds.get(0);
                            List<Integer> oldRange = rowSequencesRanges.get(matchingSeqId);
                            List<Integer> updatedRange = new ArrayList<>(Arrays.asList(potentiallyColouredFieldColumnIndex, colouredSequenceEndColumnIndex));

                            this.updateRowSequenceRange(rowIdx, matchingSeqId, updatedRange);
                            Field rowField = new Field(rowIdx, 0);
                            actionScheduler.scheduleActionsBasedOnField(rowField, NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART);

                            this.nonogramState.increaseMadeSteps();
                            tmpLog = generateCorrectingRowSequenceRangeStepDescription(rowIdx, matchingSeqId, oldRange, updatedRange, "update only matching sequence part preventing excess length to right");
                            addLog();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void markAvailableFieldsInRow(int rowIdx) {
        NonogramFieldMarkHelper.markAvailableFieldsInLine(
                rowIdx,
                true, // isRow
                getNonogramRules(),
                getNonogramSolutionBoard(),
                getNonogramSolutionBoardWithMarks(),
                this.getNonogramRules().getRowSequencesLengths(),
                getRowsSequencesRanges(),
                getRowsSequencesIdsNotToInclude(),
                this::changeRowSequenceRange,
                this::excludeSequenceInRow,
                actionScheduler,
                nonogramState,
                this::addLog,
                this::setTmpLog
        );
    }

    public void excludeSequenceInRow(int rowIdx, int seqIdx) {
        if (!this.getBoardAccessHelper().isRowIndexValid(rowIdx) || this.rowsSequencesIdsNotToInclude.get(rowIdx).contains(seqIdx)) return;

        String marker = NonogramHelper.indexToSequenceCharMark(seqIdx);
        List<Integer> rowSeqRange = this.getRowsSequencesRanges().get(rowIdx).get(seqIdx);

        IntStream.rangeClosed(rowSeqRange.get(0), rowSeqRange.get(1))
                .forEach(columnIdx -> markRowBoardField(this.getNonogramSolutionBoardWithMarks(), rowIdx, columnIdx, marker));

        tmpLog = generateExcludedSequenceLog(
                rowIdx,
                seqIdx,
                false,
                this.getRowCopy(rowIdx),
                this.getNonogramRules().getRowSequencesLengths().get(rowIdx),
                this.getRowsSequencesRanges().get(rowIdx)
        );
        addLog();

        this.rowsSequencesIdsNotToInclude.get(rowIdx).add(seqIdx);
        Collections.sort(this.rowsSequencesIdsNotToInclude.get(rowIdx));
    }

    public void excludeFieldsInRow(List<Field> fieldsToExclude) {
        fieldsToExclude.forEach(this::excludeFieldInRow);
    }

    protected void excludeFieldInRow(Field fieldToExclude) {
        int fieldRowIdx = fieldToExclude.getRowIdx();
        int fieldColIdx = fieldToExclude.getColumnIdx();
        if (this.getBoardAccessHelper().areFieldIndexesValid(fieldToExclude) && !this.rowsFieldsNotToInclude.get(fieldRowIdx).contains(fieldColIdx)) {
            this.rowsFieldsNotToInclude.get(fieldRowIdx).add(fieldColIdx);
            Collections.sort(this.rowsFieldsNotToInclude.get(fieldRowIdx));
        }
    }

    public void updateRowSequenceRange(int rowIdx, int sequenceIdx, List<Integer> updatedRange) {
        this.rowsSequencesRanges.get(rowIdx).set(sequenceIdx, updatedRange);
    }

    public int minimumColumnIndexWithoutX(int rowIdx, int lastSequenceColumnIdx, int sequenceFullLength) {
        int minimumColumnIndex = lastSequenceColumnIdx;
        int minimumColumnIndexLimit = Math.max(lastSequenceColumnIdx - sequenceFullLength + 1, 0);
        Field fieldToCheck;

        for (; minimumColumnIndex >= minimumColumnIndexLimit; minimumColumnIndex--) {
            fieldToCheck = new Field(rowIdx, minimumColumnIndex);
            if (isFieldWithX(this.nonogramSolutionBoard, fieldToCheck)) {
                break;
            }
        }

        return minimumColumnIndex + 1;
    }

    public int maximumColumnIndexWithoutX(int rowIdx, int firstSequenceColumnIdx, int sequenceFullLength) {
        int maximumColumnIndex = firstSequenceColumnIdx;
        int maximumColumnIndexLimit = Math.min(firstSequenceColumnIdx + sequenceFullLength - 1, this.getNonogramRules().getWidth() - 1);
        Field fieldToCheck;

        for (; maximumColumnIndex <= maximumColumnIndexLimit; maximumColumnIndex++) {
            fieldToCheck = new Field(rowIdx, maximumColumnIndex);
            if (isFieldWithX(this.nonogramSolutionBoard, fieldToCheck)) {
                break;
            }
        }

        return maximumColumnIndex - 1;
    }

    public void changeRowSequenceRange(int rowIndex, int sequenceIndex, List<Integer> updatedRange) {
        this.rowsSequencesRanges.get(rowIndex).set(sequenceIndex, updatedRange);
    }

    public String generateColourStepDescription(int rowIndex, int columnIndex, String actionType) {
        return String.format("ROW %d, COLUMN %d - field colouring - %s.", rowIndex, columnIndex, actionType);
    }

    public String generatePlacingXStepDescription(int rowIndex, int columnIndex, String actionType) {
        return String.format("ROW %d, COLUMN %d - X placing - %s.", rowIndex, columnIndex, actionType);
    }

    public String generateCorrectingRowSequenceRangeStepDescription(int rowIndex, int sequenceIndex, List<Integer> oldRange, List<Integer> correctedRange, String actionType) {
        return String.format("ROW %d, SEQUENCE %d - range correcting - from [%d, %d] to [%d, %d] - %s", rowIndex, sequenceIndex,
                oldRange.get(0), oldRange.get(1), correctedRange.get(0), correctedRange.get(1), actionType);
    }
}
