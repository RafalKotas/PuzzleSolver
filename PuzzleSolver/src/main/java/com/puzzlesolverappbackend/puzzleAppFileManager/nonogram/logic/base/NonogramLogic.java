package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.puzzlesolverappbackend.puzzleAppFileManager.common.LogicFunctions;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.*;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.NonogramColumnLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.NonogramRowLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.solutions.NonogramSolutionDecision;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.model.NonogramActionDetails;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.model.NonogramBoardTemplate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.puzzlesolverappbackend.puzzleAppFileManager.common.ArrayUtils.rangeInsideAnotherRange;
import static com.puzzlesolverappbackend.puzzleAppFileManager.common.ArrayUtils.rangeLength;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.enums.NonogramSolveAction.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.NonogramStructureFactory.generateEmptyColumns;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.NonogramStructureFactory.generateEmptyRows;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramState.buildInitialEmptyNonogramState;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.ColumnColourFieldsIfXWouldForceTooLongColouredFieldsSequenceHelpers.collectColouredSequencesRangesInColumn;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.RowColourFieldsIfXWouldForceTooLongColouredFieldsSequenceHelpers.collectColouredSequencesRangesInRow;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.model.NonogramConstants.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramBoardUtils.isFieldEmpty;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramHelper.indexToSequenceCharMark;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramParametersComparatorHelper.sequencesRangesEqual;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
public class NonogramLogic extends NonogramLogicParams {

    private List<List<String>> correctSolutionBoard;
    private List<List<List<Integer>>> correctRowRanges;
    private List<List<List<Integer>>> correctColumnRanges;

    private GuessMode guessMode;

    private List<List<Integer>> rowsFieldsNotToInclude;
    private List<List<Integer>> columnsFieldsNotToInclude;

    private List<List<Integer>> rowsSequencesIdsNotToInclude;
    private List<List<Integer>> columnsSequencesIdsNotToInclude;

    private List<List<List<Integer>>> rowsSequencesRanges;
    private List<List<List<Integer>>> columnsSequencesRanges;

    private NonogramRowLogic nonogramRowLogic;
    private NonogramColumnLogic nonogramColumnLogic;

    private boolean LOG_CHANGES = false;

    @JsonIgnore
    private NonogramPrinter printer;

    public NonogramLogic(NonogramRules rules, GuessMode guessMode) {
        this.nonogramRules = rules;
        this.guessMode = guessMode;
        this.logs = new ArrayList<>();

        int height = rules.getHeight();
        int width = rules.getWidth();

        this.actionsToDoList = generateInitialActionsToDo();

        this.nonogramSolutionBoard = generateEmptyBoard(height, width, 1);
        this.nonogramSolutionBoardWithMarks = generateEmptyBoard(height, width, 4);

        this.rowsFieldsNotToInclude = generateEmptyRows(height);
        this.columnsFieldsNotToInclude = generateEmptyColumns(width);

        NonogramSequenceRangeInferer rangeInferer = new NonogramSequenceRangeInferer(
                this.nonogramRules,
                this.nonogramSolutionBoardWithMarks
        );

        this.rowsSequencesRanges = rangeInferer.inferInitialRowsSequencesRanges();
        this.columnsSequencesRanges = rangeInferer.inferInitialColumnsSequencesRanges();

        this.rowsSequencesIdsNotToInclude = generateEmptyRows(height);
        this.columnsSequencesIdsNotToInclude = generateEmptyColumns(width);

        this.nonogramState = buildInitialEmptyNonogramState();

        this.nonogramRowLogic = new NonogramRowLogic(this);
        this.nonogramColumnLogic = new NonogramColumnLogic(this);

        if (LOG_CHANGES) {
            log.info("CREATED NonogramLogic object from rules and guessMode");
        }

        this.printer = new NonogramPrinter(this);
    }

    public NonogramLogic(List<List<Integer>> rowSequencesLengths,
                         List<List<Integer>> columnsSequencesLengths,
                         List<List<String>> nonogramSolutionBoard) {
        this.printer = new NonogramPrinter(this);
    }

    private List<NonogramActionDetails> generateInitialActionsToDo() {

        List<NonogramActionDetails> overlappingActionsAllRows = IntStream.range(0, this.getNonogramRules().getHeight())
                .mapToObj(rowIdx -> new NonogramActionDetails(rowIdx, COLOUR_OVERLAPPING_FIELDS_IN_ROW, null, false))
                .collect(Collectors.toCollection(ArrayList::new));
        List<NonogramActionDetails> overlappingActionsAllColumns = IntStream.range(0, this.getNonogramRules().getWidth())
                .mapToObj(columnIdx -> new NonogramActionDetails(columnIdx, COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, null, false))
                .collect(Collectors.toCollection(ArrayList::new));

        return Stream.concat(overlappingActionsAllRows.stream(), overlappingActionsAllColumns.stream()).collect(Collectors.toCollection(ArrayList::new));
    }

    public void clearLogs() {
        this.logs.clear();
    }

    private List<List<String>> generateEmptyBoard(int height, int width, int emptyFieldCharRepeatCount) {
        List<List<String>> emptyBoard = new ArrayList<>(height);
        for (int rowIdx = 0; rowIdx < height; rowIdx++) {
            List<String> boardRow = new ArrayList<>();
            for (int column = 0; column < width; column++) {
                boardRow.add(EMPTY_FIELD.repeat(emptyFieldCharRepeatCount));
            }
            emptyBoard.add(boardRow);
        }
        return emptyBoard;
    }

    public void fillTrivialRowsAndColumns() {
        fillTrivialRows();
        fillTrivialColumns();
    }

    private void fillTrivialRows() {
        Field rowField;
        int width = this.getNonogramRules().getWidth();
        for (int rowIdx = 0; rowIdx < this.getNonogramRules().getHeight(); rowIdx++) {
            if (isRowTrivial(rowIdx)) {
                addFillTrivialRowLog(rowIdx);
                int seqNo = 0;
                int subsequentXs = 0;
                List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
                List<Integer> rowSequenceRange = rowSequencesRanges.get(seqNo);
                for (int columnIdx = 0; columnIdx < width; columnIdx++) {
                    rowField = new Field(rowIdx, columnIdx);
                    if (rangeInsideAnotherRange(List.of(columnIdx, columnIdx), rowSequenceRange)) {
                        subsequentXs = 0;
                        fillTrivialRowField(rowField, seqNo);
                        addRowFieldToExcluded(rowField);
                        addColumnToAffectedActionsByIdentifiers(columnIdx, NonogramSolveAction.COLOUR_FIELD_IN_TRIVIAL_ROW);
                    } else {
                        placeXAtGivenPosition(rowField);
                        addRowFieldToExcluded(rowField);
                        addColumnFieldToExcluded(rowField);
                        addColumnToAffectedActionsByIdentifiers(columnIdx, NonogramSolveAction.PLACING_X_IN_TRIVIAL_ROW);
                        subsequentXs += 1;
                        if (subsequentXs == 1 && seqNo + 1 < rowSequencesRanges.size()) {
                            rowSequenceRange = this.getRowsSequencesRanges().get(rowIdx).get(++seqNo);
                        } else {
                            break;
                        }
                    }
                }
                addAllRowSequencesIdxToNotToInclude(rowIdx);
            } else if (isRowEmpty(rowIdx)) {
                for (int columnIdx = 0; columnIdx < width; columnIdx++) {
                    rowField = new Field(rowIdx, columnIdx);
                    placeXAtGivenPosition(rowField);
                    addRowFieldToExcluded(rowField);
                    addColumnFieldToExcluded(rowField);
                    addColumnToAffectedActionsByIdentifiers(columnIdx, NonogramSolveAction.PLACING_X_IN_TRIVIAL_ROW);
                }
                addAllRowSequencesIdxToNotToInclude(rowIdx);
            }
        }
    }

    protected boolean isRowEmpty(int rowIdx) {
        return sequencesRangesEqual(this.getRowsSequencesRanges().get(rowIdx), List.of(List.of(-1, -1)));
    }

    protected boolean isRowTrivial(int rowIdx) {
        List<Integer> rowSequencesLengths = this.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        for (int seqNo = 0; seqNo < rowSequencesLengths.size(); seqNo++) {
            if (rowSequencesLengths.get(seqNo) != rangeLength(rowSequencesRanges.get(seqNo))) {
                return false;
            }
        }

        return true;
    }

    private void addAllRowSequencesIdxToNotToInclude(int rowIdx) {
        IntStream.range(0, this.getNonogramRules().getRowSequencesLengths().get(rowIdx).size()).boxed().forEach(seqNo -> this.addTrivialRowSequenceIdxToNotToInclude(rowIdx, seqNo));
    }

    public void addTrivialRowSequenceIdxToNotToInclude(int rowIdx, int seqIdx) {
        if (!this.rowsSequencesIdsNotToInclude.get(rowIdx).contains(seqIdx)) {
            this.tmpLog = generateExcludedSequenceLog(
                    rowIdx,
                    seqIdx,
                    true,
                    this.getRowCopy(rowIdx),
                    this.getNonogramRules().getRowSequencesLengths().get(rowIdx),
                    this.getRowsSequencesRanges().get(rowIdx)
            );
            addLog();
            this.rowsSequencesIdsNotToInclude.get(rowIdx).add(seqIdx);
            Collections.sort(this.rowsSequencesIdsNotToInclude.get(rowIdx));
        }
    }

    private void addFillTrivialRowLog(int rowIdx) {
        this.tmpLog = String.format("ROW %d is trivial - filling whole", rowIdx);
        addLog();
    }

    private void fillTrivialRowField(Field trivialRowField, int seqNo) {
        String seqMark = indexToSequenceCharMark(seqNo);
        colourFieldAtGivenPosition(trivialRowField);
        updateNonogramBoardFieldWithMarksInRow(trivialRowField, seqMark);
    }

    private void updateNonogramBoardFieldWithMarksInRow(Field field, String seqMark) {
        int fieldRowIdx = field.getRowIdx();
        int fieldColIdx = field.getColumnIdx();
        String updatedRowPartFieldWithMarks = MARKED_ROW_INDICATOR + seqMark;
        String columnPartFromFieldWithMarks = getColumnPartFromFieldWithMarks(field);
        String updatedFieldWithMarks = updatedRowPartFieldWithMarks + columnPartFromFieldWithMarks;

        this.getNonogramSolutionBoardWithMarks().get(fieldRowIdx).set(fieldColIdx, updatedFieldWithMarks);
    }

    private String getColumnPartFromFieldWithMarks(Field field) {
        int fieldRowIdx = field.getRowIdx();
        int fieldColIdx = field.getColumnIdx();
        return this.getNonogramSolutionBoardWithMarks().get(fieldRowIdx).get(fieldColIdx).substring(2);
    }

    private void fillTrivialColumns() {
        Field columnField;
        int height = this.getNonogramRules().getHeight();
        for (int columnIdx = 0; columnIdx < this.getNonogramRules().getWidth(); columnIdx++) {
            if (isColumnTrivial(columnIdx)) {
                addFillTrivialColumnLog(columnIdx);
                int seqNo = 0;
                int subsequentXs = 0;
                List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
                List<Integer> columnSequenceRange = columnSequencesRanges.get(seqNo);
                for (int rowIdx = 0; rowIdx < height; rowIdx++) {
                    columnField = new Field(rowIdx, columnIdx);
                    if (rangeInsideAnotherRange(List.of(rowIdx), columnSequenceRange)) {
                        subsequentXs = 0;
                        fillTrivialColumnField(columnField, seqNo);
                        addColumnFieldToExcluded(columnField);
                        addRowToAffectedActionsByIdentifiers(rowIdx, NonogramSolveAction.COLOUR_FIELD_IN_TRIVIAL_COLUMN);
                    } else {
                        placeXAtGivenPosition(columnField);
                        addColumnFieldToExcluded(columnField);
                        addRowFieldToExcluded(columnField);
                        addRowToAffectedActionsByIdentifiers(rowIdx, NonogramSolveAction.PLACING_X_IN_TRIVIAL_COLUMN);
                        subsequentXs += 1;
                        if (subsequentXs == 1 && seqNo + 1 < columnSequencesRanges.size()) {
                            columnSequenceRange = columnSequencesRanges.get(++seqNo);
                        } else {
                            break;
                        }
                    }
                }
                addAllColumnSequencesIdxToNotToInclude(columnIdx);
            } else if (isColumnEmpty(columnIdx)) {
                for (int rowIdx = 0; rowIdx < height; rowIdx++) {
                    columnField = new Field(rowIdx, columnIdx);
                    placeXAtGivenPosition(columnField);
                    addRowFieldToExcluded(columnField);
                    addColumnFieldToExcluded(columnField);
                    addColumnToAffectedActionsByIdentifiers(columnIdx, NonogramSolveAction.PLACING_X_IN_TRIVIAL_COLUMN);
                }
                addAllColumnSequencesIdxToNotToInclude(columnIdx);
            }
        }
    }

    protected boolean isColumnEmpty(int columnIdx) {
        return sequencesRangesEqual(this.getColumnsSequencesRanges().get(columnIdx), List.of(List.of(-1, -1)));
    }

    protected boolean isColumnTrivial(int columnIdx) {
        List<Integer> columnSequencesLengths = this.getNonogramRules().getColumnSequencesLengths().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        for (int seqNo = 0; seqNo < columnSequencesLengths.size(); seqNo++) {
            if (columnSequencesLengths.get(seqNo) != rangeLength(columnSequencesRanges.get(seqNo))) {
                return false;
            }
        }

        return true;
    }

    private void addAllColumnSequencesIdxToNotToInclude(int columnIdx) {
        IntStream.range(0, this.getNonogramRules().getColumnSequencesLengths().get(columnIdx).size()).boxed().forEach(seqNo -> this.addColumnSequenceIdxToNotToInclude(columnIdx, seqNo));
    }

    public void addColumnSequenceIdxToNotToInclude(int columnIdx, int seqIdx) {
        if (!this.columnsSequencesIdsNotToInclude.get(columnIdx).contains(seqIdx)) {
            this.tmpLog = generateExcludedSequenceLog(
                    columnIdx,
                    seqIdx,
                    true,
                    this.getColumnCopy(columnIdx),
                    this.getNonogramRules().getColumnSequencesLengths().get(columnIdx),
                    this.getColumnsSequencesRanges().get(columnIdx)
            );
            addLog();
            this.columnsSequencesIdsNotToInclude.get(columnIdx).add(seqIdx);
            Collections.sort(this.columnsSequencesIdsNotToInclude.get(columnIdx));
        }
    }

    private void addFillTrivialColumnLog(int columnIdx) {
        this.tmpLog = String.format("COLUMN %d is trivial - filling whole", columnIdx);
        addLog();
    }

    private void fillTrivialColumnField(Field trivialColumnField, int seqNo) {
        String seqMark = indexToSequenceCharMark(seqNo);
        colourFieldAtGivenPosition(trivialColumnField);
        updateNonogramBoardFieldWithMarksInColumn(trivialColumnField, seqMark);
    }

    private void updateNonogramBoardFieldWithMarksInColumn(Field columnField, String seqMark) {
        int fieldRowIdx = columnField.getRowIdx();
        int fieldColIdx = columnField.getColumnIdx();
        String rowPartFromFieldWithMarks = getRowPartFromFieldWithMarks(columnField);
        String updatedColumnPartFieldWithMarks = MARKED_COLUMN_INDICATOR + seqMark;
        String updatedFieldWithMarks = rowPartFromFieldWithMarks + updatedColumnPartFieldWithMarks;

        this.nonogramSolutionBoardWithMarks.get(fieldRowIdx).set(fieldColIdx, updatedFieldWithMarks);
    }

    private String getRowPartFromFieldWithMarks(Field field) {
        int fieldRowIdx = field.getRowIdx();
        int fieldColIdx = field.getColumnIdx();
        return this.nonogramSolutionBoardWithMarks.get(fieldRowIdx).get(fieldColIdx).substring(0, 2);
    }

    public void colourFieldAtGivenPosition(Field fieldToColour) {
        int fieldColIdx = fieldToColour.getColumnIdx();
        int fieldRowIdx = fieldToColour.getRowIdx();
        if (areFieldIndexesValid(fieldToColour)) {
            this.nonogramSolutionBoard.get(fieldRowIdx).set(fieldColIdx, COLOURED_FIELD);
        }
    }

    protected NonogramLogic addRowFieldToExcluded(Field fieldToExclude) {
        int fieldRowIdx = fieldToExclude.getRowIdx();
        int fieldColIdx = fieldToExclude.getColumnIdx();
        if (areFieldIndexesValid(fieldToExclude) && !this.rowsFieldsNotToInclude.get(fieldRowIdx).contains(fieldColIdx)) {
            this.rowsFieldsNotToInclude.get(fieldRowIdx).add(fieldColIdx);
            Collections.sort(this.rowsFieldsNotToInclude.get(fieldRowIdx));
        }

        return this;
    }

    public NonogramLogic addColumnFieldToExcluded(Field fieldToAdd) {
        int fieldColIdx = fieldToAdd.getColumnIdx();
        int fieldRowIdx = fieldToAdd.getRowIdx();
        if (areFieldIndexesValid(fieldToAdd) && !this.columnsFieldsNotToInclude.get(fieldColIdx)
                .contains(fieldRowIdx)) {
            this.columnsFieldsNotToInclude.get(fieldColIdx).add(fieldRowIdx);
            Collections.sort(this.columnsFieldsNotToInclude.get(fieldColIdx));
        }

        return this;
    }

    public void updateCurrentAvailableChoices() {
        List<Integer> rowFieldsNotToInclude;
        this.availableChoices = new ArrayList<>();
        NonogramSolutionDecision decision;
        Field decisionField;

        for (int rowIdx = 0; rowIdx < this.getNonogramRules().getHeight(); rowIdx++) {
            rowFieldsNotToInclude = this.getRowsFieldsNotToInclude().get(rowIdx);
            if (!(rowFieldsNotToInclude.size() == this.getNonogramRules().getWidth())) {
                for (int columnIdx = 0; columnIdx < this.getNonogramRules().getWidth(); columnIdx++) {
                    decisionField = new Field(rowIdx, columnIdx);
                    if (isFieldEmpty(this.nonogramSolutionBoard, decisionField)) {
                        decision = new NonogramSolutionDecision(X_FIELD, decisionField);
                        this.availableChoices.add(decision);
                    }
                }
            }
        }
    }

    public void addAffectedRowAndColumnAfterColouringField(NonogramSolutionDecision decision) {
        int rowIdx = decision.getDecisionField().getRowIdx();
        int columnIdx = decision.getDecisionField().getColumnIdx();

        this.actionsToDoList.add(new NonogramActionDetails(rowIdx,
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, COLOUR_FIELD_GUESS_OR_RECURSIVE, false));
        this.actionsToDoList.add(new NonogramActionDetails(columnIdx,
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, COLOUR_FIELD_GUESS_OR_RECURSIVE, false));
        this.actionsToDoList.add(new NonogramActionDetails(rowIdx,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, COLOUR_FIELD_GUESS_OR_RECURSIVE, false));
        this.actionsToDoList.add(new NonogramActionDetails(columnIdx,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, COLOUR_FIELD_GUESS_OR_RECURSIVE, false));
        this.actionsToDoList.add(new NonogramActionDetails(columnIdx,
                NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, COLOUR_FIELD_GUESS_OR_RECURSIVE, false));
        this.actionsToDoList.add(new NonogramActionDetails(columnIdx,
                NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, COLOUR_FIELD_GUESS_OR_RECURSIVE, false));
        this.actionsToDoList.add(new NonogramActionDetails(rowIdx,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW, COLOUR_FIELD_GUESS_OR_RECURSIVE, false));
        this.actionsToDoList.add(new NonogramActionDetails(columnIdx,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN, COLOUR_FIELD_GUESS_OR_RECURSIVE, false));
    }

    public void addAffectedRowAndColumnAfterPlacingXAtField(NonogramSolutionDecision decision) {
        int rowIdx = decision.getDecisionField().getRowIdx();
        int columnIdx = decision.getDecisionField().getColumnIdx();

        this.actionsToDoList.add(new NonogramActionDetails(rowIdx, NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES,
                PLACE_X_FIELD_GUESS_OR_RECURSIVE, false));
        this.actionsToDoList.add(new NonogramActionDetails(columnIdx, NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES,
                PLACE_X_FIELD_GUESS_OR_RECURSIVE, false));
        this.actionsToDoList.add(new NonogramActionDetails(rowIdx, NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY,
                PLACE_X_FIELD_GUESS_OR_RECURSIVE, false));
        this.actionsToDoList.add(new NonogramActionDetails(columnIdx, NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY,
                PLACE_X_FIELD_GUESS_OR_RECURSIVE, false));
        this.actionsToDoList.add(new NonogramActionDetails(rowIdx, NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                PLACE_X_FIELD_GUESS_OR_RECURSIVE, false));
        this.actionsToDoList.add(new NonogramActionDetails(columnIdx, NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                PLACE_X_FIELD_GUESS_OR_RECURSIVE, false));
        this.actionsToDoList.add(new NonogramActionDetails(rowIdx, NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES,
                PLACE_X_FIELD_GUESS_OR_RECURSIVE, false));
        this.actionsToDoList.add(new NonogramActionDetails(columnIdx, NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES,
                PLACE_X_FIELD_GUESS_OR_RECURSIVE, false));
    }

    public void basicSolve() {

        int actionListIndex = 0;
        int currentActionRCIndex; // row or column index
        NonogramActionDetails currentActionDetails;
        NonogramSolveAction nonogramSolveAction;

        while (actionListIndex < this.actionsToDoList.size()) {
            currentActionDetails = this.actionsToDoList.get(actionListIndex);
            currentActionRCIndex = currentActionDetails.getIndex();
            nonogramSolveAction = currentActionDetails.getActionName();

            try {
                if (getRowSolveActions().contains(nonogramSolveAction)) {
                    executeRowAction(currentActionRCIndex, currentActionDetails);
                } else {
                    executeColumnAction(currentActionRCIndex, currentActionDetails);
                }
            } catch (Exception e) {
                // empty
            }

            if (!validateAgainstCorrectSolution(actionListIndex, currentActionDetails)) {
                break;
            }

            if (this.guessMode == GuessMode.ENABLED && this.nonogramState.isInvalidSolution()) {
                break;
            }

            actionListIndex++;
        }
    }

    private void executeRowAction(int rowIdx, NonogramActionDetails actionDetails) {
        copyLogicToNonogramRowLogic();

        int stepsBefore = nonogramState.getNewStepsMade();

        makeProperActionInRow(rowIdx, actionDetails.getActionName());

        int stepsAfter = nonogramState.getNewStepsMade();

        if (LOG_CHANGES && stepsBefore != stepsAfter) {
            logRowStateBefore(actionDetails, rowIdx);
            logRowStateAfter(actionDetails, rowIdx);
        }

        copyLogicFromNonogramRowLogic();
    }

    private void executeColumnAction(int columnIdx, NonogramActionDetails actionDetails) {
        copyLogicToNonogramColumnLogic();

        int stepsBefore = nonogramState.getNewStepsMade();

        makeProperActionInColumn(columnIdx, actionDetails.getActionName());

        int stepsAfter = nonogramState.getNewStepsMade();

        if (false && LOG_CHANGES && stepsBefore != stepsAfter) {
            logColumnStateBefore(actionDetails, columnIdx);
            logColumnStateAfter(actionDetails, columnIdx);
        }

        copyLogicFromNonogramColumnLogic();
    }

    public boolean validateAgainstCorrectSolution(int actionIndex, NonogramActionDetails currentActionDetails) {
        if (correctSolutionBoard == null || correctRowRanges == null || correctColumnRanges == null) {
            return true;
        }

        boolean boardCorrect = NonogramSolverUtils.partialBoardMatchesSolution(
                getNonogramSolutionBoard(), correctSolutionBoard
        );
        boolean rowRangesCorrect = NonogramSolverUtils.rangesContainCorrectRanges(
                correctRowRanges, getRowsSequencesRanges()
        );
        boolean columnRangesCorrect = NonogramSolverUtils.rangesContainCorrectRanges(
                correctColumnRanges, getColumnsSequencesRanges()
        );

        List<String> errors = new ArrayList<>();

        if (!boardCorrect) {
            errors.add("Board not correct after action %d: %s"
                    .formatted(actionIndex, currentActionDetails));
        }

        if (!rowRangesCorrect) {
            errors.add("Correct row ranges are not contained in current row ranges");
        }

        if (!columnRangesCorrect) {
            errors.add("Correct column ranges are not contained in current column ranges");
        }

        if (!errors.isEmpty()) {
            log.error("Validation failed at action {}: {}", actionIndex, currentActionDetails);
            errors.forEach(log::error);

            getNonogramState().setInvalidSolution(true);
            return false;
        }

        return true;
    }

    private void logRowStateBefore(NonogramActionDetails actionDetails, int nextActionRowIndex) {
        String elementToLog;

        if (NonogramSolveAction.getCorrectRowRangesSolveActions().contains(actionDetails.getActionName())) {
            elementToLog = this.getRowsSequencesRanges().get(nextActionRowIndex).toString();
        } else if (NonogramSolveAction.isMarkRowAction(actionDetails.getActionName())) {
            elementToLog = this.getNonogramSolutionBoardWithMarks().get(nextActionRowIndex).toString();
        } else {
            elementToLog = this.getNonogramSolutionBoard().get(nextActionRowIndex).toString();
        }

        String rangesLog = "";
        String lengthsLog = "";

        if (!actionDetails.getActionName().toString().contains("CORRECT")) {
            rangesLog = this.getRowsSequencesRanges().get(nextActionRowIndex).toString();
            lengthsLog = this.nonogramRules.getRowSequencesLengths().get(nextActionRowIndex).toString();
        }

        if (actionDetails.getActionName().toString().contains("EXTEND")) {
            if (!rangesLog.isEmpty()) {
                log.info("Row {} before making action {}: {} (ranges: {}, lengths: {})", nextActionRowIndex, actionDetails.getActionName(), elementToLog, rangesLog, lengthsLog);
            } else {
                log.info("Row {} before making action {}: {}", nextActionRowIndex, actionDetails.getActionName(), elementToLog);
            }
        }
    }

    private void logRowStateAfter(NonogramActionDetails actionDetails, int nextActionRowIndex) {
        String elementToLog;

        if (NonogramSolveAction.getCorrectRowRangesSolveActions().contains(actionDetails.getActionName())) {
            elementToLog = this.getNonogramRowLogic().getRowsSequencesRanges().get(nextActionRowIndex).toString();
        } else if (NonogramSolveAction.isMarkRowAction(actionDetails.getActionName())) {
            elementToLog = this.getNonogramRowLogic().getNonogramSolutionBoardWithMarks().get(nextActionRowIndex).toString();
        } else {
            elementToLog = this.getNonogramRowLogic().getNonogramSolutionBoard().get(nextActionRowIndex).toString();
        }

        if (actionDetails.getActionName().toString().contains("EXTEND")) {
            log.info("Row {} after  making action {}: {}", nextActionRowIndex, actionDetails.getActionName(), elementToLog);
        }
    }

    private void logColumnStateBefore(NonogramActionDetails actionDetails, int nextActionColumnIndex) {
        String elementToLog;

        if (NonogramSolveAction.getCorrectColumnRangesSolveActions().contains(actionDetails.getActionName())) {
            elementToLog = this.getColumnsSequencesRanges().get(nextActionColumnIndex).toString();
        } else if (NonogramSolveAction.isMarkColumnAction(actionDetails.getActionName())) {
            elementToLog = this.getNonogramBoardColumnWithMarks(nextActionColumnIndex).toString();
        } else {
            elementToLog = this.getNonogramBoardColumn(nextActionColumnIndex).toString();
        }

        log.info("Column {} before making action {}: {}", nextActionColumnIndex, actionDetails.getActionName(), elementToLog);
    }

    private void logColumnStateAfter(NonogramActionDetails actionDetails, int nextActionColumnIndex) {
        String elementToLog;

        if (NonogramSolveAction.getCorrectColumnRangesSolveActions().contains(actionDetails.getActionName())) {
            elementToLog = this.getNonogramColumnLogic().getColumnsSequencesRanges().get(nextActionColumnIndex).toString();
        } else if (NonogramSolveAction.isMarkColumnAction(actionDetails.getActionName())) {
            elementToLog = this.getNonogramColumnLogic().getNonogramBoardColumnWithMarks(nextActionColumnIndex).toString();
        } else {
            elementToLog = this.getNonogramColumnLogic().getNonogramBoardColumn(nextActionColumnIndex).toString();
        }

        log.info("Column {} after  making action {}: {}", nextActionColumnIndex, actionDetails.getActionName(), elementToLog);
    }

    public void makeProperActionInRow(int rowIdx, NonogramSolveAction actionToDoInRow) {
        switch (actionToDoInRow) {
            case CORRECT_ROW_SEQUENCES_RANGES -> {
                this.nonogramRowLogic.correctRowSequencesRanges(rowIdx);
                if (this.guessMode == GuessMode.ENABLED) {
                    invalidateSolutionIfRowSequencesWrong(rowIdx);
                }
            }
            case CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS -> {
                this.nonogramRowLogic.correctRowSequencesRangesWhenMetColouredField(rowIdx);
                if (this.guessMode == GuessMode.ENABLED) {
                    invalidateSolutionIfRowSequencesWrong(rowIdx);
                }
            }
            case CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY -> {
                this.nonogramRowLogic.correctRowSequencesRangesIfXOnWay(rowIdx, true);
                if (this.guessMode == GuessMode.ENABLED) {
                    invalidateSolutionIfRowSequencesWrong(rowIdx);
                }
            }
            case CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES ->
                    this.nonogramRowLogic.correctRowSequencesRangesWhenMatchingFieldsToSequences(rowIdx);
            case CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE ->
                    this.nonogramRowLogic.correctRowSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(rowIdx);
            case COLOUR_OVERLAPPING_FIELDS_IN_ROW -> this.nonogramRowLogic.colourOverlappingFieldsInRow(rowIdx);
            case COLOUR_FIELDS_IN_ROW_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE -> this.nonogramRowLogic.colourFieldsIfInRowXWouldForceTooLongColouredFieldsSequence(rowIdx);
            case EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW -> this.nonogramRowLogic.extendColouredFieldsNearXToMaximumPossibleLengthInRow(rowIdx);
            //case COLOUR_FIELDS_IN_ROW_IF_X_CAUSES_ASSIGNMENT_CONFLICT -> this.nonogramRowLogic.colourFieldsInRowIfXCausesAssignmentConflict(rowIdx);
            case PLACE_XS_ROW_AT_UNREACHABLE_FIELDS -> this.nonogramRowLogic.placeXsRowAtUnreachableFields(rowIdx);
            case PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES -> this.nonogramRowLogic.placeXsAroundLongestSequencesInRow(rowIdx);
            case PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES -> this.nonogramRowLogic.placeXsRowAtTooShortEmptySequences(rowIdx);
            case PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE -> this.nonogramRowLogic.placeXsRowIfOWillMergeNearFieldsToTooLongColouredSequence(rowIdx);
            //case PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE -> this.nonogramRowLogic.placeXsRowIfONearXWillBeginTooLongPossibleColouredSequence(rowIdx);
            //case ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH -> this.nonogramRowLogic.preventExtendingColouredSequenceToExcessLengthInRow(rowIdx);
            case MARK_AVAILABLE_FIELDS_IN_ROW -> this.nonogramRowLogic.markAvailableFieldsInRow(rowIdx);
            default -> {
                // empty
            }
        }
    }

    public void makeProperActionInColumn(int columnIdx, NonogramSolveAction actionToDoInColumn) {
        switch (actionToDoInColumn) {
            case CORRECT_COLUMN_SEQUENCES_RANGES -> {
                this.nonogramColumnLogic.correctColumnSequencesRanges(columnIdx);
                if (this.guessMode == GuessMode.ENABLED) {
                    invalidateSolutionIfColumnSequencesWrong(columnIdx);
                }
            }
            case CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS -> {
                this.nonogramColumnLogic.correctColumnSequencesRangesWhenMetColouredField(columnIdx);
                if (this.guessMode == GuessMode.ENABLED) {
                    invalidateSolutionIfColumnSequencesWrong(columnIdx);
                }
            }
            case CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY -> {
                this.nonogramColumnLogic.correctColumnSequencesRangesIfXOnWay(columnIdx, true);
                if (this.guessMode == GuessMode.ENABLED) {
                    invalidateSolutionIfColumnSequencesWrong(columnIdx);
                }
            }
            case CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES ->
                    this.nonogramColumnLogic.correctColumnSequencesRangesWhenMatchingFieldsToSequences(columnIdx);
            case CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE ->
                    this.nonogramColumnLogic.correctColumnSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(columnIdx);
            case COLOUR_OVERLAPPING_FIELDS_IN_COLUMN -> this.nonogramColumnLogic.colourOverlappingFieldsInColumn(columnIdx);
            case COLOUR_FIELDS_IN_COLUMN_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE -> this.nonogramColumnLogic.colourFieldsInColumnIfXWouldForceTooLongColouredFieldsSequence(columnIdx);
            case EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN -> this.nonogramColumnLogic.extendColouredFieldsNearXToMaximumPossibleLengthInColumn(columnIdx);
            //case COLOUR_FIELDS_IN_COLUMN_IF_X_CAUSES_ASSIGNMENT_CONFLICT -> this.nonogramColumnLogic.colourFieldsInColumnIfXCausesAssignmentConflict(columnIdx);
            case PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS -> this.nonogramColumnLogic.placeXsColumnAtUnreachableFields(columnIdx);
            case PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES -> this.nonogramColumnLogic.placeXsAroundLongestSequencesInColumn(columnIdx);
            case PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES -> this.nonogramColumnLogic.placeXsColumnAtTooShortEmptySequences(columnIdx);
            case PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE -> this.nonogramColumnLogic.placeXsColumnIfOWillMergeNearFieldsToTooLongColouredSequence(columnIdx);
            // case PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE -> this.nonogramColumnLogic.placeXsColumnIfONearXWillBeginTooLongPossibleColouredSequence(columnIdx);
            //case COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH -> this.nonogramColumnLogic.preventExtendingColouredSequenceToExcessLengthInColumn(columnIdx);
            case MARK_AVAILABLE_FIELDS_IN_COLUMN -> this.nonogramColumnLogic.markAvailableFieldsInColumn(columnIdx);
            default -> {
                // empty
            }
        }
    }

    private void invalidateSolutionIfColumnSequencesWrong(int columnIndex) {
        List<Integer> sequencesLengths = this.getNonogramRules().getRowSequencesLengths().get(columnIndex);
        List<List<Integer>> columnSequencesRanges = this.nonogramColumnLogic.getColumnsSequencesRanges().get(columnIndex);

        for (int seqNo = 0; seqNo < columnSequencesRanges.size(); seqNo++) {
            if (rangeLength(columnSequencesRanges.get(seqNo)) < sequencesLengths.get(seqNo)) {
                this.nonogramColumnLogic.getNonogramState().invalidateSolution();
                break;
            }
        }
    }

    private void invalidateSolutionIfRowSequencesWrong(int rowIndex) {
        List<Integer> sequencesLengths = this.getNonogramRules().getRowSequencesLengths().get(rowIndex);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIndex);

        for (int seqNo = 0; seqNo < rowSequencesRanges.size(); seqNo++) {
            if (rangeLength(rowSequencesRanges.get(seqNo)) < sequencesLengths.get(seqNo)) {
                this.nonogramRowLogic.getNonogramState().invalidateSolution();
                break;
            }
        }
    }

    public void copyLogicFromNonogramColumnLogic() {
        this.logs = this.nonogramColumnLogic.getLogs();

        this.nonogramSolutionBoardWithMarks = copyTwoDeepList(this.nonogramColumnLogic.getNonogramSolutionBoardWithMarks());
        this.nonogramSolutionBoard = copyTwoDeepList(this.nonogramColumnLogic.getNonogramSolutionBoard());

        this.columnsSequencesRanges = copySequencesRanges(this.nonogramColumnLogic.getColumnsSequencesRanges());
        this.columnsFieldsNotToInclude = copyTwoDeepList(this.nonogramColumnLogic.getColumnsFieldsNotToInclude());
        this.columnsSequencesIdsNotToInclude = copyTwoDeepList(this.nonogramColumnLogic.getColumnsSequencesIdsNotToInclude());

        this.actionsToDoList = this.nonogramColumnLogic.getActionsToDoList();
        this.nonogramState = this.nonogramColumnLogic.getNonogramState();
    }

    public void copyLogicFromNonogramRowLogic() {
        this.logs = this.nonogramRowLogic.getLogs();

        this.nonogramSolutionBoardWithMarks = copyTwoDeepList(this.nonogramRowLogic.getNonogramSolutionBoardWithMarks());
        this.nonogramSolutionBoard = copyTwoDeepList(this.nonogramRowLogic.getNonogramSolutionBoard());

        this.rowsSequencesRanges = copySequencesRanges(this.nonogramRowLogic.getRowsSequencesRanges());
        this.rowsFieldsNotToInclude = copyTwoDeepList(this.nonogramRowLogic.getRowsFieldsNotToInclude());
        this.rowsSequencesIdsNotToInclude = copyTwoDeepList(this.nonogramRowLogic.getRowsSequencesIdsNotToInclude());

        this.actionsToDoList = this.nonogramRowLogic.getActionsToDoList();
        this.nonogramState = this.nonogramRowLogic.getNonogramState();
    }

    public void copyLogicToNonogramColumnLogic() {
        this.nonogramColumnLogic.setNonogramState(this.getNonogramState());
        this.nonogramColumnLogic.setLogs(this.getLogs());

        this.nonogramColumnLogic.setColumnsSequencesRanges(copySequencesRanges(this.getColumnsSequencesRanges()));
        this.nonogramColumnLogic.setColumnsSequencesIdsNotToInclude(copyTwoDeepList(this.getColumnsSequencesIdsNotToInclude()));

        this.nonogramColumnLogic.setColumnsFieldsNotToInclude(copyTwoDeepList(this.getColumnsFieldsNotToInclude()));

        this.nonogramColumnLogic.setNonogramSolutionBoardWithMarks(copyTwoDeepList(this.getNonogramSolutionBoardWithMarks()));
        this.nonogramColumnLogic.setNonogramSolutionBoard(copyTwoDeepList(this.getNonogramSolutionBoard()));
        this.nonogramColumnLogic.setActionsToDoList(this.getActionsToDoList());
    }

    public void copyLogicToNonogramRowLogic() {
        this.nonogramRowLogic.setNonogramState(this.getNonogramState());
        this.nonogramRowLogic.setLogs(this.getLogs());

        this.nonogramRowLogic.setRowsSequencesRanges(copySequencesRanges(this.getRowsSequencesRanges()));
        this.nonogramRowLogic.setRowsSequencesIdsNotToInclude(copyTwoDeepList(this.getRowsSequencesIdsNotToInclude()));

        this.nonogramRowLogic.setRowsFieldsNotToInclude(this.getRowsFieldsNotToInclude());

        this.nonogramRowLogic.setNonogramSolutionBoardWithMarks(copyTwoDeepList(this.getNonogramSolutionBoardWithMarks()));
        this.nonogramRowLogic.setNonogramSolutionBoard(copyTwoDeepList(this.getNonogramSolutionBoard()));
        this.nonogramRowLogic.setActionsToDoList(this.getActionsToDoList());
    }

    private void syncFieldsNotToIncludeFromBoard() {
        int height = this.nonogramRules.getHeight();
        int width = this.nonogramRules.getWidth();

        for (int rowIdx = 0; rowIdx < height; rowIdx++) {
            for (int colIdx = 0; colIdx < width; colIdx++) {
                String field = this.nonogramSolutionBoard.get(rowIdx).get(colIdx);
                if ("X".equals(field)) {
                    if (!this.rowsFieldsNotToInclude.get(rowIdx).contains(colIdx)) {
                        this.rowsFieldsNotToInclude.get(rowIdx).add(colIdx);
                    }
                    if (!this.columnsFieldsNotToInclude.get(colIdx).contains(rowIdx)) {
                        this.columnsFieldsNotToInclude.get(colIdx).add(rowIdx);
                    }
                }
            }
        }
    }

    private <T> List<List<T>> copyTwoDeepList(List<List<T>> nonogramBoard) {
        if (nonogramBoard == null) {
            return null;
        }

        List<List<T>> copiedBoard = new ArrayList<>();
        for (List<T> boardRow : nonogramBoard) {
            if (boardRow != null) {
                copiedBoard.add(new ArrayList<>(boardRow));
            } else {
                copiedBoard.add(null);
            }
        }
        return copiedBoard;
    }

    private List<List<List<Integer>>> copySequencesRanges(List<List<List<Integer>>> sequencesRanges) {

        List<List<List<Integer>>> sequencesRangesCopy = new ArrayList<>();

        for (List<List<Integer>> singleSequencesRanges : sequencesRanges) {
            List<List<Integer>> elementSequencesRangesCopy = new ArrayList<>();
            for (List<Integer> sequenceRange : singleSequencesRanges) {
                elementSequencesRangesCopy.add(new ArrayList<>(sequenceRange));
            }
            sequencesRangesCopy.add(elementSequencesRangesCopy);
        }

        return sequencesRangesCopy;
    }

    public boolean subSolutionBoardCorrectComparisonWithSolutionBoard(String solutionFileName) {
        List<List<String>> subSolutionNonogramBoard = this.nonogramSolutionBoard;
        NonogramBoardTemplate solutionBoardTemplate = new NonogramBoardTemplate(solutionFileName);
        System.out.println("solution board template:");
        solutionBoardTemplate.printBoard();
        List<List<String>> solutionNonogramBoard = solutionBoardTemplate.getBoard();

        Iterator<List<String>> subsolutionNonogramBoardIterator = subSolutionNonogramBoard.iterator();
        Iterator<List<String>> solutionNonogramBoardIterator = solutionNonogramBoard.iterator();

        while(subsolutionNonogramBoardIterator.hasNext() && solutionNonogramBoardIterator.hasNext()) {

            List<String> subSolutionNonogramBoardRow = subsolutionNonogramBoardIterator.next();
            List<String> solutionNonogramBoardRow = solutionNonogramBoardIterator.next();

            System.out.println("rows (subSolution and solution)");
            System.out.println(subSolutionNonogramBoardRow);
            System.out.println(solutionNonogramBoardRow);

            Iterator<String> subsolutionNonogramBoardRowIterator = subSolutionNonogramBoardRow.iterator();
            Iterator<String> solutionNonogramBoardRowIterator = solutionNonogramBoardRow.iterator();

            while(subsolutionNonogramBoardRowIterator.hasNext() && solutionNonogramBoardRowIterator.hasNext()) {
                String subSolutionNonogramBoardField = subsolutionNonogramBoardRowIterator.next();
                String solutionNonogramBoardField = solutionNonogramBoardRowIterator.next();

                // "X" or "O"
                if (subSolutionNonogramBoardField.equals(COLOURED_FIELD)) {
                    if (!subSolutionNonogramBoardField.equals(solutionNonogramBoardField)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public NonogramLogic placeXAtGivenPosition(Field x_field) {
        int fieldColIdx = x_field.getColumnIdx();
        int fieldRowIdx = x_field.getRowIdx();
        if (areFieldIndexesValid(x_field)) {
            this.nonogramSolutionBoard.get(fieldRowIdx).set(fieldColIdx, X_FIELD);
            this.nonogramSolutionBoardWithMarks.get(fieldRowIdx).set(fieldColIdx, X_FIELD_MARKED_BOARD);
        }

        return this;
    }

    public NonogramLogic setNonogramBoardRow(int rowIdx, List<String> boardRow) {
        for (int colIdx = 0; colIdx < this.getNonogramRules().getWidth(); colIdx++) {
            this.nonogramSolutionBoard.get(rowIdx).set(colIdx, boardRow.get(colIdx));
        }
        return this;
    }

    public boolean isNonogramRowSymmetrical() {
        return areOriginalAndReversedListIdentical( this.nonogramRules.getRowSequencesLengths() );
    }

    public boolean isNonogramColumnSymmetrical() {
        return areOriginalAndReversedListIdentical( this.getNonogramRules().getColumnSequencesLengths() );
    }

    public static boolean areOriginalAndReversedListIdentical(List<List<Integer>> listOfIntegers) {

        List<List<Integer>> reversedList = new ArrayList<>(listOfIntegers);
        Collections.reverse(reversedList);

        return reversedList.equals(listOfIntegers);
    }

    public boolean isNonogram1DSymmetrical() {
        return LogicFunctions.xor(isNonogramRowSymmetrical(), isNonogramColumnSymmetrical());
    }

    public boolean isNonogram2DSymmetrical() {
        return isNonogramRowSymmetrical() && isNonogramColumnSymmetrical() && !areRowsSequencesIdenticalWithColumnsSequences();
    }

    public boolean isNonogram3DSymmetrical() {
        return isNonogramRowSymmetrical() && isNonogramColumnSymmetrical() && areRowsSequencesIdenticalWithColumnsSequences();
    }

    public String nonogramSymmetricalGrade() {
        if (isNonogram3DSymmetrical()) {
            return "4 axis";
        } else if (isNonogram2DSymmetrical()) {
            return "2 axis";
        } else if (isNonogram1DSymmetrical()) {
            return "1 axis";
        } else {
            return "None";
        }
    }

    public boolean areRowsSequencesIdenticalWithColumnsSequences() {
        if (this.getNonogramRules().getRowSequencesLengths().size() != this.getNonogramRules().getColumnSequencesLengths().size()) {
            return false;
        }

        for (int i = 0; i < this.getNonogramRules().getRowSequencesLengths().size(); i++) {
            List<Integer> rowSequences = this.getNonogramRules().getRowSequencesLengths().get(i);
            List<Integer> columnSequences = this.getNonogramRules().getColumnSequencesLengths().get(i);

            if (!rowSequences.equals(columnSequences)) {
                return false;
            }
        }

        return true;
    }

    public boolean nonogramIsFullyAndCorrectSolved() {
        if (fieldsFilled() != super.nonogramAreaInFieldsCount()) {
            return false;
        }

        for (int rowIdx = 0; rowIdx < this.getNonogramRules().getHeight(); rowIdx++) {
            List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
            List<Integer> rowSequencesLengths = this.getNonogramRules().getRowSequencesLengths().get(rowIdx);

            List<List<Integer>> colouredSequencesInRow = collectColouredSequencesRangesInRow(this.getNonogramSolutionBoard(), rowIdx);
            List<Integer> currentColouredSequence;

            if (rowSequencesLengths.equals(List.of(0)) && colouredSequencesInRow.isEmpty()) {
                continue;
            }

            if (colouredSequencesInRow.size() != rowSequencesLengths.size()) {
                return false;
            } else {
                for (int colouredSeqNo = 0; colouredSeqNo < colouredSequencesInRow.size(); colouredSeqNo++) {
                    currentColouredSequence = colouredSequencesInRow.get(colouredSeqNo);
                    if (!currentColouredSequence.equals(rowSequencesRanges.get(colouredSeqNo))
                        || rangeLength(rowSequencesRanges.get(colouredSeqNo)) != (rowSequencesLengths.get(colouredSeqNo))) {
                        return false;
                    }
                }
            }
        }

        for (int columnIdx = 0; columnIdx < this.getNonogramRules().getWidth(); columnIdx++) {
            List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
            List<Integer> columnSequencesLengths = this.getNonogramRules().getColumnSequencesLengths().get(columnIdx);

            List<List<Integer>> colouredSequencesInColumn = collectColouredSequencesRangesInColumn(this.getNonogramSolutionBoard(), columnIdx);
            List<Integer> currentColouredSequence;

            if (colouredSequencesInColumn.size() != columnSequencesLengths.size()) {
                return false;
            } else {
                for (int colouredSeqNo = 0; colouredSeqNo < colouredSequencesInColumn.size(); colouredSeqNo++) {
                    currentColouredSequence = colouredSequencesInColumn.get(colouredSeqNo);
                    if (!currentColouredSequence.equals(columnSequencesRanges.get(colouredSeqNo))
                            || rangeLength(columnSequencesRanges.get(colouredSeqNo)) != columnSequencesLengths.get(colouredSeqNo)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void printRowsSequencesRanges() {
        int rowIdx = 0;
        System.out.println("Rows sequences ranges:");
        for (List<List<Integer>> rowSequencesRanges : this.getRowsSequencesRanges()) {
            System.out.println(rowIdx + ": " + rowSequencesRanges);
            rowIdx++;
        }
    }

    public void printColumnsSequencesRanges() {
        int columnIdx = 0;
        System.out.println("Columns sequences ranges:");
        for (List<List<Integer>> columnSequencesRanges : this.getColumnsSequencesRanges()) {
            System.out.println(columnIdx + ": " + columnSequencesRanges);
            columnIdx++;
        }
    }
}
