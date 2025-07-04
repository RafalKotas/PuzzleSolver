package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import com.puzzlesolverappbackend.puzzleAppFileManager.common.LogicFunctions;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.dto.NonogramSolvePayload;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.*;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.columnactions.NonogramColumnLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.common.NonogramFieldClearingHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.TrivialFillLogHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve.NonogramActionScheduler;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve.NonogramBoardAccessHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.NonogramRowLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.solutions.NonogramSolutionDecision;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.model.NonogramActionDetails;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.model.NonogramBoardTemplate;
import jakarta.persistence.Transient;
import lombok.*;
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
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramSolverUtils.actualRangesDoNotContainCorrectRanges;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramState.buildInitialEmptyNonogramState;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.generators.ActionDetailsGenerator.generateAllPossibleSingleActionDetails;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.exclusion.ExcludedSequenceLogHelper.generateExcludedSequenceLog;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve.TooLongMergeFieldHelper.collectColouredSequencesRanges;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.model.NonogramConstants.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramBoardUtils.isFieldEmpty;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramHelper.indexToSequenceCharMark;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramParametersComparatorHelper.sequencesRangesEqual;


@Data
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

    @Expose(serialize = false)
    @Transient
    @JsonIgnore
    private NonogramRowLogic nonogramRowLogic;

    @Expose(serialize = false)
    @Transient
    @JsonIgnore
    private NonogramColumnLogic nonogramColumnLogic;

    @Expose(serialize = false)
    @Transient
    @JsonIgnore
    private NonogramActionScheduler actionScheduler;

    @Expose(serialize = false)
    @Transient
    @JsonIgnore
    private NonogramBoardAccessHelper boardAccessHelper;

    @Expose(serialize = false)
    @Transient
    @JsonIgnore
    private NonogramFieldClearingHelper fieldClearingHelper;

    private boolean LOG_CHANGES = false;

    @Expose(serialize = false)
    @Transient
    @JsonIgnore
    private NonogramPrinter printer;

    public NonogramLogic(NonogramRules rules, GuessMode guessMode) {
        this.nonogramRules = rules;
        this.actionsToDoList = generateInitialActionsToDo(rules);
        this.actionScheduler = new NonogramActionScheduler(this.getActionsToDoList());

        this.guessMode = guessMode;
        this.logs = new ArrayList<>();

        int height = rules.getHeight();
        int width = rules.getWidth();

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

        this.nonogramRowLogic = new NonogramRowLogic(this, boardAccessHelper, actionScheduler);

        this.nonogramColumnLogic = new NonogramColumnLogic(this);

        this.boardAccessHelper = new NonogramBoardAccessHelper(this.getNonogramSolutionBoard());

        if (LOG_CHANGES) {
            log.info("CREATED NonogramLogic object from rules and guessMode");
        }

        this.printer = new NonogramPrinter(this);
    }

    public NonogramLogic(NonogramRules rules, NonogramSolvePayload payload) {
        this.nonogramRules = rules;
        this.guessMode = GuessMode.DISABLED;
        this.logs = new ArrayList<>();

        int height = rules.getHeight();
        int width = rules.getWidth();

        this.actionsToDoList = generateInitialActionsToDo(rules);
        this.actionScheduler = new NonogramActionScheduler(this.actionsToDoList);

        this.nonogramSolutionBoard = generateEmptyBoard(height, width, 1);
        this.nonogramSolutionBoardWithMarks = payload.getNonogramSolutionBoardWithMarks();

        this.rowsFieldsNotToInclude = getRowsFieldsNotToInclude();
        this.columnsFieldsNotToInclude = getColumnsFieldsNotToInclude();

        this.rowsSequencesRanges = payload.getRowsSequencesRanges();
        this.columnsSequencesRanges = payload.getColumnsSequencesRanges();

        this.rowsSequencesIdsNotToInclude = payload.getRowsSequencesIdsNotToInclude();
        this.columnsSequencesIdsNotToInclude = getColumnsSequencesIdsNotToInclude();

        this.nonogramState = buildInitialEmptyNonogramState();

        this.boardAccessHelper = new NonogramBoardAccessHelper(this);

        this.nonogramRowLogic = new NonogramRowLogic(this, boardAccessHelper, actionScheduler);
        this.nonogramColumnLogic = new NonogramColumnLogic(this, boardAccessHelper, actionScheduler);

        if (LOG_CHANGES) {
            log.info("CREATED NonogramLogic object from rules and payload");
        }

        this.printer = new NonogramPrinter(this);
    }

    public NonogramLogic deepCopy() {
        NonogramLogic original = this;

        NonogramRules copiedRules = new NonogramRules(
                original.getNonogramRules().getRowSequencesLengths(),
                original.getNonogramRules().getColumnSequencesLengths(),
                original.getNonogramRules().getHeight(),
                original.getNonogramRules().getWidth());

        NonogramLogic copy = new NonogramLogic(copiedRules, original.getGuessMode());

        copy.setNonogramSolutionBoard(deepCopyBoard(original.getNonogramSolutionBoard()));
        copy.setNonogramSolutionBoardWithMarks(deepCopyBoard(original.getNonogramSolutionBoardWithMarks()));

        copy.setRowsFieldsNotToInclude(deepCopyIntegerListList(original.getRowsFieldsNotToInclude()));
        copy.setColumnsFieldsNotToInclude(deepCopyIntegerListList(original.getColumnsFieldsNotToInclude()));
        copy.setRowsSequencesIdsNotToInclude(deepCopyIntegerListList(original.getRowsSequencesIdsNotToInclude()));
        copy.setColumnsSequencesIdsNotToInclude(deepCopyIntegerListList(original.getColumnsSequencesIdsNotToInclude()));

        copy.setRowsSequencesRanges(deepCopyIntegerListListList(original.getRowsSequencesRanges()));
        copy.setColumnsSequencesRanges(deepCopyIntegerListListList(original.getColumnsSequencesRanges()));

        copy.setActionsToDoList(original.getActionsToDoList());
        copy.setLogs(original.getLogs());
        copy.setNonogramState(new NonogramState(original.getNonogramState().getNewStepsMade(),
                original.getNonogramState().isInvalidSolution()));

        copy.initializeHelpers();

        return copy;
    }

    public void initializeHelpers() {
        NonogramBoardAccessHelper accessHelper = new NonogramBoardAccessHelper(this);
        NonogramFieldClearingHelper clearingHelper = new NonogramFieldClearingHelper(
                this.getNonogramSolutionBoard(),
                this.getNonogramSolutionBoardWithMarks(),
                accessHelper
        );
        NonogramActionScheduler scheduler = new NonogramActionScheduler(this.getActionsToDoList());

        NonogramRowLogic rowLogic = new NonogramRowLogic(this, accessHelper, scheduler);
        NonogramColumnLogic columnLogic = new NonogramColumnLogic(this, accessHelper, scheduler);

        this.setBoardAccessHelper(accessHelper);
        this.setNonogramRowLogic(rowLogic);
        this.setNonogramColumnLogic(columnLogic);
        this.setFieldClearingHelper(clearingHelper);
        this.setActionScheduler(scheduler);
    }


    private List<List<String>> deepCopyBoard(List<List<String>> board) {
        List<List<String>> copy = new ArrayList<>();
        for (List<String> row : board) {
            copy.add(new ArrayList<>(row));
        }
        return copy;
    }

    private List<List<Integer>> deepCopyIntegerListList(List<List<Integer>> original) {
        List<List<Integer>> copy = new ArrayList<>();
        for (List<Integer> inner : original) {
            copy.add(new ArrayList<>(inner));
        }
        return copy;
    }

    private List<List<List<Integer>>> deepCopyIntegerListListList(List<List<List<Integer>>> original) {
        List<List<List<Integer>>> copy = new ArrayList<>();
        for (List<List<Integer>> innerList : original) {
            List<List<Integer>> innerCopy = new ArrayList<>();
            for (List<Integer> inner : innerList) {
                innerCopy.add(new ArrayList<>(inner));
            }
            copy.add(innerCopy);
        }
        return copy;
    }


    public static List<NonogramActionDetails> generateInitialActionsToDo(NonogramRules nonogramRules) {

        List<NonogramActionDetails> overlappingActionsAllRows = IntStream.range(0, nonogramRules.getHeight())
                .mapToObj(rowIdx -> new NonogramActionDetails(rowIdx, COLOUR_OVERLAPPING_FIELDS_IN_ROW, null, false))
                .collect(Collectors.toCollection(ArrayList::new));
        List<NonogramActionDetails> overlappingActionsAllColumns = IntStream.range(0, nonogramRules.getWidth())
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
        int width = getNonogramRules().getWidth();

        for (int rowIdx = 0; rowIdx < getNonogramRules().getHeight(); rowIdx++) {
            List<String> rowBefore = getRowCopy(rowIdx);
            boolean changed = false;

            if (isRowTrivial(rowIdx)) {
                fillTrivialRow(rowIdx, width);
                changed = true;
            } else if (isRowEmpty(rowIdx)) {
                fillEmptyRow(rowIdx, width);
                changed = true;
            }

            if (changed) {
                List<String> rowAfter = getRowCopy(rowIdx);
                List<Integer> sequenceLengths = getNonogramRules().getRowSequencesLengths().get(rowIdx);
                List<List<Integer>> sequenceRanges = getRowsSequencesRanges().get(rowIdx);

                this.tmpLog = TrivialFillLogHelper.generateTrivialLineLog(
                        rowIdx,
                        true,
                        rowBefore,
                        rowAfter,
                        sequenceLengths,
                        sequenceRanges
                );
                addLog();
            }
        }
    }

    private boolean isRowTrivial(int rowIdx) {
        List<Integer> rowSequencesLengths = this.getNonogramRules().getRowSequencesLengths().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        for (int seqNo = 0; seqNo < rowSequencesLengths.size(); seqNo++) {
            if (rowSequencesLengths.get(seqNo) != rangeLength(rowSequencesRanges.get(seqNo))) {
                return false;
            }
        }

        return true;
    }

    private void fillTrivialRow(int rowIdx, int width) {
        int seqNo = 0;
        int subsequentXs = 0;

        List<List<Integer>> rowSequencesRanges = getRowsSequencesRanges().get(rowIdx);
        List<Integer> currentSequenceRange = rowSequencesRanges.get(seqNo);

        for (int colIdx = 0; colIdx < width; colIdx++) {
            Field field = new Field(rowIdx, colIdx);
            boolean inRange = rangeInsideAnotherRange(List.of(colIdx, colIdx), currentSequenceRange);

            if (inRange) {
                subsequentXs = 0;
                fillTrivialRowField(field, seqNo);
                addRowFieldToExcluded(field);
                actionScheduler.scheduleActionsBasedOnField(field, NonogramSolveAction.COLOUR_FIELD_IN_TRIVIAL_ROW);
            } else {
                placeXAtGivenPosition(field);
                addRowFieldToExcluded(field);
                addColumnFieldToExcluded(field);
                actionScheduler.scheduleActionsBasedOnField(field, NonogramSolveAction.PLACING_X_IN_TRIVIAL_ROW);

                subsequentXs++;
                if (subsequentXs == 1 && seqNo + 1 < rowSequencesRanges.size()) {
                    currentSequenceRange = rowSequencesRanges.get(++seqNo);
                } else if (seqNo + 1 >= rowSequencesRanges.size()) {
                    break;
                }
            }
        }

        addAllRowSequencesIdxToNotToInclude(rowIdx);
    }

    private boolean isRowEmpty(int rowIdx) {
        return sequencesRangesEqual(this.getRowsSequencesRanges().get(rowIdx), List.of(List.of(-1, -1)));
    }

    private void fillEmptyRow(int rowIdx, int width) {
        for (int colIdx = 0; colIdx < width; colIdx++) {
            Field field = new Field(rowIdx, colIdx);
            placeXAtGivenPosition(field);
            addRowFieldToExcluded(field);
            addColumnFieldToExcluded(field);

            actionScheduler.scheduleActionsBasedOnField(field, NonogramSolveAction.PLACING_X_IN_TRIVIAL_ROW);
        }

        addAllRowSequencesIdxToNotToInclude(rowIdx);
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
        int height = getNonogramRules().getHeight();

        for (int columnIdx = 0; columnIdx < getNonogramRules().getWidth(); columnIdx++) {
            List<String> columnBefore = getColumnCopy(columnIdx);
            boolean changed = false;

            if (isColumnTrivial(columnIdx)) {
                fillTrivialColumn(columnIdx, height);
                changed = true;
            } else if (isColumnEmpty(columnIdx)) {
                fillEmptyColumn(columnIdx, height);
                changed = true;
            }

            if (changed) {
                List<String> columnAfter = getColumnCopy(columnIdx);
                List<Integer> sequenceLengths = getNonogramRules().getColumnSequencesLengths().get(columnIdx);
                List<List<Integer>> sequenceRanges = getColumnsSequencesRanges().get(columnIdx);

                this.tmpLog = TrivialFillLogHelper.generateTrivialLineLog(
                        columnIdx,
                        false,
                        columnBefore,
                        columnAfter,
                        sequenceLengths,
                        sequenceRanges
                );
                addLog();
            }
        }
    }

    private void fillTrivialColumn(int columnIdx, int height) {
        int seqNo = 0;
        int subsequentXs = 0;

        List<List<Integer>> colSeqRanges = getColumnsSequencesRanges().get(columnIdx);
        List<Integer> currentRange = colSeqRanges.get(seqNo);

        for (int rowIdx = 0; rowIdx < height; rowIdx++) {
            Field field = new Field(rowIdx, columnIdx);
            boolean inRange = rangeInsideAnotherRange(List.of(rowIdx, rowIdx), currentRange);

            if (inRange) {
                subsequentXs = 0;
                fillTrivialColumnField(field, seqNo);
                addColumnFieldToExcluded(field);
                actionScheduler.scheduleActionsBasedOnField(field, NonogramSolveAction.COLOUR_FIELD_IN_TRIVIAL_COLUMN);
            } else {
                placeXAtGivenPosition(field);
                addColumnFieldToExcluded(field);
                addRowFieldToExcluded(field);
                actionScheduler.scheduleActionsBasedOnField(field, NonogramSolveAction.PLACING_X_IN_TRIVIAL_COLUMN);

                subsequentXs++;
                if (subsequentXs == 1 && seqNo + 1 < colSeqRanges.size()) {
                    currentRange = colSeqRanges.get(++seqNo);
                } else if (seqNo + 1 >= colSeqRanges.size()) {
                    break;
                }
            }
        }

        addAllColumnSequencesIdxToNotToInclude(columnIdx);
    }

    private void fillEmptyColumn(int columnIdx, int height) {
        for (int rowIdx = 0; rowIdx < height; rowIdx++) {
            Field field = new Field(rowIdx, columnIdx);
            placeXAtGivenPosition(field);
            addRowFieldToExcluded(field);
            addColumnFieldToExcluded(field);
            actionScheduler.scheduleActionsBasedOnField(field, NonogramSolveAction.PLACING_X_IN_TRIVIAL_COLUMN);
        }

        addAllColumnSequencesIdxToNotToInclude(columnIdx);
    }

    private boolean isColumnEmpty(int columnIdx) {
        return sequencesRangesEqual(this.getColumnsSequencesRanges().get(columnIdx), List.of(List.of(-1, -1)));
    }

    private boolean isColumnTrivial(int columnIdx) {
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
        if (boardAccessHelper.areFieldIndexesValid(fieldToColour)) {
            this.nonogramSolutionBoard.get(fieldRowIdx).set(fieldColIdx, COLOURED_FIELD);
        }
    }

    protected NonogramLogic addRowFieldToExcluded(Field fieldToExclude) {
        int fieldRowIdx = fieldToExclude.getRowIdx();
        int fieldColIdx = fieldToExclude.getColumnIdx();
        if (boardAccessHelper.areFieldIndexesValid(fieldToExclude) && !this.rowsFieldsNotToInclude.get(fieldRowIdx).contains(fieldColIdx)) {
            this.rowsFieldsNotToInclude.get(fieldRowIdx).add(fieldColIdx);
            Collections.sort(this.rowsFieldsNotToInclude.get(fieldRowIdx));
        }

        return this;
    }

    public NonogramLogic addColumnFieldToExcluded(Field fieldToAdd) {
        int fieldColIdx = fieldToAdd.getColumnIdx();
        int fieldRowIdx = fieldToAdd.getRowIdx();
        if (boardAccessHelper.areFieldIndexesValid(fieldToAdd) && !this.columnsFieldsNotToInclude.get(fieldColIdx)
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

        while (actionListIndex < this.actionsToDoList.size()) {
            NonogramActionDetails currentAction = this.actionsToDoList.get(actionListIndex);



            if (!executeSingleActionWithValidation(actionListIndex, currentAction)) {
                break;
            }

            if (this.guessMode == GuessMode.ENABLED && this.nonogramState.isInvalidSolution()) {
                break;
            }

//            if (isFieldWithX(this.getNonogramSolutionBoard(), new Field(4, 6))
//            && isFieldWithX(this.getNonogramSolutionBoard(), new Field(4, 8))
//            && !rangeInsideAnotherRange(List.of(7, 7), this.getRowsSequencesRanges().get(4).get(0)) ) {
//                System.out.println("po tym można postawić X w za małych pustych sekwencjach między Xsami");
//            }

            actionListIndex++;
        }
    }

    private void makeExtraActions() {
        List<NonogramActionDetails> nonogramExtraActions =
                generateAllPossibleSingleActionDetails(this.getNonogramRules().getHeight(), this.getNonogramRules().getWidth());

        for (NonogramActionDetails extraAction : nonogramExtraActions) {
            int currentIndex = extraAction.getIndex();
            NonogramSolveAction extraActionName = extraAction.getActionName();

            try {
                if (getRowSolveActions().contains(extraActionName)) {
                    executeRowAction(currentIndex, extraAction);
                } else {
                    executeColumnAction(currentIndex, extraAction);
                }
            } catch (Exception e) {
                // empty
            }

            if (!validateAgainstCorrectSolution(currentIndex, extraAction)) {
                break;
            }

            if (this.guessMode == GuessMode.ENABLED && this.nonogramState.isInvalidSolution()) {
                break;
            }
        }
    }

    private boolean executeSingleActionWithValidation(int actionIndex, NonogramActionDetails action) {
        int index = action.getIndex();
        NonogramSolveAction actionName = action.getActionName();

        try {
            if (getRowSolveActions().contains(actionName)) {
                executeRowAction(index, action);
            } else {
                executeColumnAction(index, action);
            }
        } catch (Exception e) {
            log.warn("Exception during action execution: {}", actionName, e);
        }

        return validateAgainstCorrectSolution(actionIndex, action);
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

        if (LOG_CHANGES && stepsBefore != stepsAfter) {
            logColumnStateBefore(actionDetails, columnIdx);
            logColumnStateAfter(actionDetails, columnIdx);
        }

        copyLogicFromNonogramColumnLogic();
    }

    public boolean validateAgainstCorrectSolution(int actionIndex, NonogramActionDetails currentActionDetails) {
        if (correctSolutionBoard == null || correctRowRanges == null || correctColumnRanges == null) {
            return true;
        }

        List<String> errors = new ArrayList<>();

        for (int row = 0; row < getNonogramSolutionBoard().size(); row++) {
            List<String> currentRow = getNonogramSolutionBoard().get(row);
            List<String> correctRow = correctSolutionBoard.get(row);
            for (int col = 0; col < currentRow.size(); col++) {
                String current = currentRow.get(col);
                String correct = correctRow.get(col);
                if (!current.equals("-") && !current.equals(correct)) {
                    errors.add("Mismatch at (" + row + "," + col + ") - found: " + current + ", expected: " + correct);
                }
            }
        }

        List<List<List<Integer>>> currentRowRanges = getRowsSequencesRanges();
        for (int rowIdx = 0; rowIdx < currentRowRanges.size(); rowIdx++) {
            List<List<Integer>> expectedRanges = correctRowRanges.get(rowIdx);
            List<List<Integer>> actualRanges = currentRowRanges.get(rowIdx);
            if (actualRangesDoNotContainCorrectRanges(expectedRanges, actualRanges)) {
                errors.add("Row range mismatch at row " + rowIdx);
            }
        }

        List<List<List<Integer>>> currentColRanges = getColumnsSequencesRanges();
        for (int colIdx = 0; colIdx < currentColRanges.size(); colIdx++) {
            List<List<Integer>> expectedRanges = correctColumnRanges.get(colIdx);
            List<List<Integer>> actualRanges = currentColRanges.get(colIdx);
            if (actualRangesDoNotContainCorrectRanges(expectedRanges, actualRanges)) {
                errors.add("Column range mismatch at column " + colIdx);
            }
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
        if (this.getNonogramRowLogic().getBoardAccessHelper().isRowIndexValid(rowIdx)) {
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
                case PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE -> this.nonogramRowLogic.placeXsRowIfONearXWillBeginTooLongPossibleColouredSequence(rowIdx);
                case ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH -> this.nonogramRowLogic.preventExtendingColouredSequenceToExcessLengthInRow(rowIdx);
                case MARK_AVAILABLE_FIELDS_IN_ROW -> this.nonogramRowLogic.markAvailableFieldsInRow(rowIdx);
                default -> {
                    // empty
                }
            }
        }
    }

    public void makeProperActionInColumn(int columnIdx, NonogramSolveAction actionToDoInColumn) {
        if (this.getNonogramRowLogic().getBoardAccessHelper().isColumnIndexValid(columnIdx)) {
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
                case PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE -> this.nonogramColumnLogic.placeXsColumnIfONearXWillBeginTooLongPossibleColouredSequence(columnIdx);
                case COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH -> this.nonogramColumnLogic.preventExtendingColouredSequenceToExcessLengthInColumn(columnIdx);
                case MARK_AVAILABLE_FIELDS_IN_COLUMN -> this.nonogramColumnLogic.markAvailableFieldsInColumn(columnIdx);
                default -> {
                    // empty
                }
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

        this.nonogramSolutionBoardWithMarks = this.nonogramColumnLogic.getNonogramSolutionBoardWithMarks();
        this.nonogramSolutionBoard = this.nonogramColumnLogic.getNonogramSolutionBoard();

        this.columnsSequencesRanges = copySequencesRanges(this.nonogramColumnLogic.getColumnsSequencesRanges());
        this.columnsFieldsNotToInclude = copyTwoDeepList(this.nonogramColumnLogic.getColumnsFieldsNotToInclude());
        this.columnsSequencesIdsNotToInclude = copyTwoDeepList(this.nonogramColumnLogic.getColumnsSequencesIdsNotToInclude());

        List<NonogramActionDetails> copiedActions = new ArrayList<>(this.nonogramColumnLogic.getActionsToDoList());

        this.actionsToDoList.clear();
        this.actionsToDoList.addAll(copiedActions);

        this.nonogramState = this.nonogramColumnLogic.getNonogramState();
    }

    public void copyLogicFromNonogramRowLogic() {
        this.logs = this.nonogramRowLogic.getLogs();

        this.nonogramSolutionBoardWithMarks = this.nonogramRowLogic.getNonogramSolutionBoardWithMarks();
        this.nonogramSolutionBoard = this.nonogramRowLogic.getNonogramSolutionBoard();

        this.rowsSequencesRanges = copySequencesRanges(this.nonogramRowLogic.getRowsSequencesRanges());
        this.rowsFieldsNotToInclude = copyTwoDeepList(this.nonogramRowLogic.getRowsFieldsNotToInclude());
        this.rowsSequencesIdsNotToInclude = copyTwoDeepList(this.nonogramRowLogic.getRowsSequencesIdsNotToInclude());

        List<NonogramActionDetails> copiedActions = new ArrayList<>(this.nonogramRowLogic.getActionsToDoList());

        this.actionsToDoList.clear();
        this.actionsToDoList.addAll(copiedActions);

        this.nonogramState = this.nonogramRowLogic.getNonogramState();
    }

    public void copyLogicToNonogramColumnLogic() {
        this.nonogramColumnLogic.setNonogramState(this.getNonogramState());
        this.nonogramColumnLogic.setLogs(this.getLogs());

        this.nonogramColumnLogic.setColumnsSequencesRanges(copySequencesRanges(this.getColumnsSequencesRanges()));
        this.nonogramColumnLogic.setColumnsSequencesIdsNotToInclude(copyTwoDeepList(this.getColumnsSequencesIdsNotToInclude()));

        this.nonogramColumnLogic.setColumnsFieldsNotToInclude(copyTwoDeepList(this.getColumnsFieldsNotToInclude()));

        this.nonogramColumnLogic.setNonogramSolutionBoardWithMarks(this.getNonogramSolutionBoardWithMarks());
        this.nonogramColumnLogic.setNonogramSolutionBoard(this.getNonogramSolutionBoard());
    }

    public void copyLogicToNonogramRowLogic() {
        this.nonogramRowLogic.setNonogramState(this.getNonogramState());
        this.nonogramRowLogic.setLogs(this.getLogs());

        this.nonogramRowLogic.setRowsSequencesRanges(copySequencesRanges(this.getRowsSequencesRanges()));
        this.nonogramRowLogic.setRowsSequencesIdsNotToInclude(copyTwoDeepList(this.getRowsSequencesIdsNotToInclude()));

        this.nonogramRowLogic.setRowsFieldsNotToInclude(copyTwoDeepList(this.getRowsFieldsNotToInclude()));

        this.nonogramRowLogic.setNonogramSolutionBoardWithMarks(this.getNonogramSolutionBoardWithMarks());
        this.nonogramRowLogic.setNonogramSolutionBoard(this.getNonogramSolutionBoard());
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
        if (boardAccessHelper.areFieldIndexesValid(x_field)) {
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

            List<List<Integer>> colouredSequencesInRow = collectColouredSequencesRanges(this.getNonogramSolutionBoard(),
                    rowIdx,
                    true);
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

            List<List<Integer>> colouredSequencesInColumn = collectColouredSequencesRanges(
                    this.getNonogramSolutionBoard(),
                    columnIdx,
                    false);
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
