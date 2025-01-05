package com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic;

import com.puzzlesolverappbackend.puzzleAppFileManager.logicOperators.LogicFunctions;
import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.NonogramActionDetails;
import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzleAppFileManager.templates.nonogram.NonogramBoardTemplate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.puzzlesolverappbackend.puzzleAppFileManager.constants.ActionsConstants.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.NonogramConstants.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.NonogramSolveAction.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.NonogramLogicService.rangeLength;
import static com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.NonogramState.buildInitialEmptyNonogramState;
import static com.puzzlesolverappbackend.puzzleAppFileManager.utils.ArrayUtils.rangeInsideAnotherRange;
import static com.puzzlesolverappbackend.puzzleAppFileManager.utils.NonogramBoardUtils.getSolutionBoardColumn;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class NonogramLogic extends NonogramLogicParams {

    protected boolean guessMode;

    protected NonogramColumnLogic nonogramColumnLogic;
    protected NonogramRowLogic nonogramRowLogic;

    @Override
    public String toString() {
        return "NonogramLogic{" +
                "rowsSequences=" + rowsSequences + "\n" +
                ", columnsSequences=" + columnsSequences + "\n" +
                ", nonogramSolutionBoardWithMarks=" + nonogramSolutionBoardWithMarks + "\n" +
                ", nonogramSolutionBoard=" + nonogramSolutionBoard + "\n" +
                ", rowsSequencesRanges=" + rowsSequencesRanges + "\n" +
                ", columnsSequencesRanges=" + columnsSequencesRanges + "\n" +
                ", rowsFieldsNotToInclude=" + rowsFieldsNotToInclude + "\n" +
                ", columnsFieldsNotToInclude=" + columnsFieldsNotToInclude + "\n" +
                ", rowsSequencesIdsNotToInclude=" + rowsSequencesIdsNotToInclude + "\n" +
                ", columnsSequencesIdsNotToInclude=" + columnsSequencesIdsNotToInclude + "\n" +
                '}';
    }

    public NonogramLogic(List<List<Integer>> rowsSequences, List<List<Integer>> columnsSequences, boolean guessMode) {
        this.logs = new ArrayList<>();

        this.rowsSequences = rowsSequences;
        this.columnsSequences = columnsSequences;

        int height = this.getHeight();
        int width = this.getWidth();

        this.actionsToDoList = generateInitialActionsToDo(height, width);

        this.nonogramSolutionBoard = generateEmptyBoard(height, width, 1);
        this.nonogramSolutionBoardWithMarks = generateEmptyBoard(height, width, 4);

        this.rowsFieldsNotToInclude = generateEmptyRowsNotToInclude();
        this.columnsFieldsNotToInclude = generateEmptyColumnsNotToInclude();

        this.rowsSequencesRanges = inferInitialRowsSequencesRanges();
        this.columnsSequencesRanges = inferInitialColumnsSequencesRanges();

        this.rowsSequencesIdsNotToInclude = generateEmptyRowsNotToInclude();
        this.columnsSequencesIdsNotToInclude = generateEmptyColumnsNotToInclude();

        this.nonogramState = buildInitialEmptyNonogramState();

        this.nonogramColumnLogic = new NonogramColumnLogic(this);
        this.nonogramRowLogic = new NonogramRowLogic(this);

        this.guessMode = guessMode;

        log.info("CREATED NonogramLogic object from rowSequences and columnSequences");
    }

    private List<NonogramActionDetails> generateInitialActionsToDo(int height, int width) {

        List<NonogramActionDetails> overlappingActionsAllRows = IntStream.range(0, height)
                .mapToObj(rowIdx -> new NonogramActionDetails(rowIdx, COLOUR_OVERLAPPING_FIELDS_IN_ROW))
                .collect(Collectors.toCollection(ArrayList::new));
        List<NonogramActionDetails> overlappingActionsAllColumns = IntStream.range(0, width)
                .mapToObj(columnIdx -> new NonogramActionDetails(columnIdx, COLOUR_OVERLAPPING_FIELDS_IN_COLUMN))
                .collect(Collectors.toCollection(ArrayList::new));

        return Stream.concat(overlappingActionsAllRows.stream(), overlappingActionsAllColumns.stream()).collect(Collectors.toCollection(ArrayList::new));
    }

    public NonogramLogic(List<List<Integer>> rowsSequences, List<List<Integer>> columnsSequences, List<List<String>> nonogramSolutionBoard) {

        this.logs = new ArrayList<>();
        //ok
        this.rowsSequences = rowsSequences;
        //ok
        this.columnsSequences = columnsSequences;
        //ok
        this.nonogramSolutionBoard = nonogramSolutionBoard;

        //ok
        int height = this.getHeight();
        //ok
        int width = this.getWidth();

        //ok - can be inferred from nonogramSolutionBoard
        this.nonogramSolutionBoardWithMarks = generateEmptyBoard(height, width, 4);

        //ok (all/none)
        this.rowsFieldsNotToInclude = generateEmptyRowsNotToInclude();
        //ok (all/none)
        this.columnsFieldsNotToInclude = generateEmptyColumnsNotToInclude();

        //ok?
        this.rowsSequencesRanges = inferRowsSequencesRangesFromSolutionBoard();
        this.columnsSequencesRanges = inferColumnsSequencesRangesFromSolutionBoard();

        this.rowsSequencesIdsNotToInclude = generateEmptyRowsNotToInclude();
        this.columnsSequencesIdsNotToInclude = generateEmptyColumnsNotToInclude();
    }

    public NonogramLogic(NonogramLogic that) {
        this(that.getRowsSequences(),
                that.getColumnsSequences(),
                that.getRowsSequencesRanges(),
                that.getColumnsSequencesRanges(),
                that.getRowsFieldsNotToInclude(),
                that.getColumnsFieldsNotToInclude(),
                that.getRowsSequencesIdsNotToInclude(),
                that.getColumnsFieldsNotToInclude(),
                that.getNonogramSolutionBoard(),
                that.getNonogramSolutionBoardWithMarks(),
                that.getNonogramState().isInvalidSolution(),
                that.isGuessMode());
    }

    public NonogramLogic(List<List<Integer>> rowsSequences,
                         List<List<Integer>> columnsSequences,
                         List<List<List<Integer>>> rowsSequencesRanges,
                         List<List<List<Integer>>> columnsSequencesRanges,
                         List<List<Integer>> rowsFieldsNotToInclude,
                         List<List<Integer>> columnsFieldsNotToInclude,
                         List<List<Integer>> rowsSequencesIdsNotToInclude,
                         List<List<Integer>> columnsSequencesIdsNotToInclude,
                         List<List<String>> nonogramSolutionBoard,
                         List<List<String>> nonogramSolutionBoardWithMarks,
                         boolean isSolutionInvalid,
                         boolean guessMode) {
        this.logs = new ArrayList<>();

        this.rowsSequences = new ArrayList<>(rowsSequences);
        this.rowsSequencesRanges = new ArrayList<>(rowsSequencesRanges);
        this.rowsSequencesIdsNotToInclude = new ArrayList<>(rowsSequencesIdsNotToInclude);
        this.rowsFieldsNotToInclude = new ArrayList<>(rowsFieldsNotToInclude);

        this.columnsSequences = new ArrayList<>(columnsSequences);
        this.columnsSequencesRanges = new ArrayList<>(columnsSequencesRanges);
        this.columnsSequencesIdsNotToInclude = new ArrayList<>(columnsSequencesIdsNotToInclude);
        this.columnsFieldsNotToInclude = new ArrayList<>(columnsFieldsNotToInclude);

        this.nonogramSolutionBoard = new ArrayList<>(nonogramSolutionBoard);
        this.nonogramSolutionBoardWithMarks = new ArrayList<>(nonogramSolutionBoardWithMarks);

        this.availableChoices = new ArrayList<>();

        this.nonogramState = new NonogramState(0, isSolutionInvalid);

        this.guessMode = guessMode;
    }

    public void clearLogs() {
        this.logs.clear();
    }

    /***
     * @param height - height of initial solution board (with or without sequence idx marks)
     * @param width - width of initial solution board (with or without sequence idx marks)
     * @param emptyFieldCharRepeatCount - how many chars (every initial field - empty field) should have
     * @return empty board of given height, width with EMPTY_FIELD on every field repeated emptyFieldCharRepeatCount times
     */
    private List<List<String>> generateEmptyBoard(int height, int width, int emptyFieldCharRepeatCount) {
        List<List<String>> emptyBoard = new ArrayList<>(height);
        for(int rowIdx = 0; rowIdx < height; rowIdx++) {
            List<String> boardRow = new ArrayList<>();
            for(int column = 0; column < width; column++) {
                boardRow.add(EMPTY_FIELD.repeat(emptyFieldCharRepeatCount));
            }
            emptyBoard.add(boardRow);
        }
        return emptyBoard;
    }

    /**
     * @return empty list of empty rows not to include when running solver
     */
    private List<List<Integer>> generateEmptyRowsNotToInclude() {
        return IntStream.range(0, this.getHeight())
                .mapToObj(row -> new ArrayList<Integer>())
                .collect(Collectors.toList());
    }

    private List<List<Integer>> generateEmptyColumnsNotToInclude() {
        return IntStream.range(0, this.getWidth())
                .mapToObj(row -> new ArrayList<Integer>())
                .collect(Collectors.toList());
    }

    public void fillTrivialRowsAndColumns() {
        fillTrivialRows();
        fillTrivialColumns();
    }

    private void fillTrivialRows() {
        Field rowField;
        for(int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            if (isRowTrivial(rowIdx)) {
                addFillTrivialRowLog(rowIdx);
                int seqNo = 0;
                int subsequentXs = 0;
                List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
                List<Integer> rowSequenceRange = rowSequencesRanges.get(seqNo);
                for(int columnIdx = 0; columnIdx < this.getWidth(); columnIdx++) {
                    rowField = new Field(rowIdx, columnIdx);
                    if (rangeInsideAnotherRange(List.of(columnIdx, columnIdx), rowSequenceRange)) {
                        subsequentXs = 0;
                        fillTrivialRowField(rowField, seqNo);
                        addRowFieldToExcluded(rowField);
                        addColumnToAffectedActionsByIdentifiers(columnIdx, actionsToDoAfterColouringFieldInTrivialRow);
                    } else {
                        placeXAtGivenPosition(rowField);
                        addRowFieldToExcluded(rowField);
                        addColumnFieldToExcluded(rowField);
                        addColumnToAffectedActionsByIdentifiers(columnIdx, actionsToDoAfterPlacingXInTrivialRow);
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
                for (int columnIdx = 0; columnIdx < this.getWidth(); columnIdx++) {
                    rowField = new Field(rowIdx, columnIdx);
                    placeXAtGivenPosition(rowField);
                    addRowFieldToExcluded(rowField);
                    addColumnFieldToExcluded(rowField);
                    addColumnToAffectedActionsByIdentifiers(columnIdx, actionsToDoAfterPlacingXInTrivialRow);
                }
                addAllRowSequencesIdxToNotToInclude(rowIdx);
            }
        }
    }

    private void addAllRowSequencesIdxToNotToInclude(int rowIdx) {
        IntStream.range(0, this.getRowsSequences().get(rowIdx).size()).boxed().forEach(seqNo -> this.addTrivialRowSequenceIdxToNotToInclude(rowIdx, seqNo));
    }

    public void addTrivialRowSequenceIdxToNotToInclude(int rowIdx, int seqIdx) {
        if (!this.rowsSequencesIdsNotToInclude.get(rowIdx).contains(seqIdx)) {
            tmpLog = generateAddingRowSequenceToNotToIncludeDescription(rowIdx, seqIdx);
            addLog(tmpLog);
            this.rowsSequencesIdsNotToInclude.get(rowIdx).add(seqIdx);
            Collections.sort(this.rowsSequencesIdsNotToInclude.get(rowIdx));
        }
    }

    private void addFillTrivialRowLog(int rowIdx) {
        tmpLog = String.format("ROW %d is trivial - filling whole", rowIdx);
        addLog(tmpLog);
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
        for(int columnIdx = 0; columnIdx < this.getWidth(); columnIdx++) {
            if (isColumnTrivial(columnIdx)) {
                addFillTrivialColumnLog(columnIdx);
                int seqNo = 0;
                int subsequentXs = 0;
                List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
                List<Integer> columnSequenceRange = columnSequencesRanges.get(seqNo);
                for(int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
                    columnField = new Field(rowIdx, columnIdx);
                    if (rangeInsideAnotherRange(List.of(rowIdx), columnSequenceRange)) {
                        subsequentXs = 0;
                        fillTrivialColumnField(columnField, seqNo);
                        addColumnFieldToExcluded(columnField);
                        addRowToAffectedActionsByIdentifiers(rowIdx, actionsToDoAfterColouringFieldInTrivialColumn);
                    } else {
                        placeXAtGivenPosition(columnField);
                        addColumnFieldToExcluded(columnField);
                        addRowFieldToExcluded(columnField);
                        addRowToAffectedActionsByIdentifiers(rowIdx, actionsToDoAfterPlacingXInTrivialColumn);
                        subsequentXs += 1;
                        if (subsequentXs == 1 && seqNo + 1 < columnSequencesRanges.size()) {
                            columnSequenceRange = columnSequencesRanges.get(++seqNo);
                        } else {
                            break;
                        }
                    }
                }
                addAllColumnSequencesIdxToNotToInclude(columnIdx);
            }
        }
    }

    private void addAllColumnSequencesIdxToNotToInclude(int columnIdx) {
        IntStream.range(0, this.getColumnsSequences().get(columnIdx).size()).boxed().forEach(seqNo -> this.addColumnSequenceIdxToNotToInclude(columnIdx, seqNo));
    }

    public void addColumnSequenceIdxToNotToInclude(int columnIdx, int seqIdx) {
        if (!this.columnsSequencesIdsNotToInclude.get(columnIdx).contains(seqIdx)) {
            generateAddingColumnSequenceToNotToIncludeDescription(columnIdx, seqIdx);
            this.columnsSequencesIdsNotToInclude.get(columnIdx).add(seqIdx);
            Collections.sort(this.columnsSequencesIdsNotToInclude.get(columnIdx));
        }
    }

    private void addFillTrivialColumnLog(int columnIdx) {
        tmpLog = String.format("COLUMN %d is trivial - filling whole", columnIdx);
        addLog(tmpLog);
    }

    private void generateAddingColumnSequenceToNotToIncludeDescription(int columnIdx, int seqNo) {
        tmpLog = String.format("COLUMN %d - seqNo = %d excluded", columnIdx, seqNo);
        addLog(tmpLog);
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

    /**
     * @param fieldToColour - field on which place COLOURED_FIELD_MARK
     */
    public void colourFieldAtGivenPosition(Field fieldToColour) {
        int fieldColIdx = fieldToColour.getColumnIdx();
        int fieldRowIdx = fieldToColour.getRowIdx();
        if (areFieldIndexesValid(fieldToColour)) {
            this.nonogramSolutionBoard.get(fieldRowIdx).set(fieldColIdx, COLOURED_FIELD);
        }
    }

    /**
     * @param fieldToExclude - field to not exclude - not consider in future
     * @return NonogramLogic object with field in given row excluded
     */
    protected NonogramLogic addRowFieldToExcluded(Field fieldToExclude) {
        int fieldRowIdx = fieldToExclude.getRowIdx();
        int fieldColIdx = fieldToExclude.getColumnIdx();
        if (areFieldIndexesValid(fieldToExclude) && !this.rowsFieldsNotToInclude.get(fieldRowIdx).contains(fieldColIdx)) {
            this.rowsFieldsNotToInclude.get(fieldRowIdx).add(fieldColIdx);
            Collections.sort(this.rowsFieldsNotToInclude.get(fieldRowIdx));
        }

        return this;
    }

    /**
     * @param fieldToAdd - field to exclude in its column
     * @return NonogramLogic object with field in given column excluded
     */
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

    private List<List<List<Integer>>> inferInitialRowsSequencesRanges() {
        List<List<List<Integer>>> initialRowSequencesRanges = new ArrayList<>();

        for(int rowIdx = 0; rowIdx < rowsSequences.size(); rowIdx++) {
            initialRowSequencesRanges.add(inferInitialRowSequencesRanges(rowsSequences.get(rowIdx), rowIdx));
        }

        return initialRowSequencesRanges;
    }

    /**
     * @param sequencesLengths - row sequences lengths
     * @param rowIdx - row index
     * @return - initial row sequences ranges
     */
    private List<List<Integer>> inferInitialRowSequencesRanges (List<Integer> sequencesLengths, int rowIdx) {

        if (sequencesLengths.size() == 1 && sequencesLengths.get(0) == 0) {
            return List.of(List.of(-1, -1));
        } else if (sequencesLengths.size() == 1 && sequencesLengths.get(0) == this.getWidth()) {
            return List.of(List.of(0, this.getWidth() - 1));
        } else {
            List<String> arrayFilledFromStart = createRowArrayFromSequencesAndChars(rowIdx, sequencesLengths, false);
            // list filled from end and reversed to place the latest sequences on array end
            List<String> arrayFilledFromEnd = reverseList(createRowArrayFromSequencesAndChars(rowIdx, sequencesLengths, true));

            return inferRowSequencesRangesFromArrays(arrayFilledFromStart, arrayFilledFromEnd);
        }
    }

    /**
     * @param arrayFilledFromStart - array filled with sequences from the earliest possible field
     * @param arrayFilledFromEnd - array filled with sequences from
     * @return sequences ranges in row calculated from sequences lengths
     */
    private List<List<Integer>> inferRowSequencesRangesFromArrays (List<String> arrayFilledFromStart, List<String> arrayFilledFromEnd) {
        List<String> collectedSequences = new ArrayList<>();
        List<List<Integer>> sequencesRanges = new ArrayList<>();
        int rangeStartIndex;
        int rangeLastIndex;

        for(int idx = 0; idx < arrayFilledFromStart.size(); idx++) {
            String field = arrayFilledFromStart.get(idx);
            String fieldSequenceChar = field.substring(1, 2);
            if (field.indexOf(MARKED_ROW_INDICATOR) == 0 && !collectedSequences.contains(fieldSequenceChar)) {
                collectedSequences.add(fieldSequenceChar);
                rangeStartIndex = idx;
                rangeLastIndex = findLastIndexContaining(arrayFilledFromEnd, MARKED_ROW_INDICATOR + fieldSequenceChar);
                List<Integer> sequenceRange = new ArrayList<>();
                sequenceRange.add(rangeStartIndex);
                sequenceRange.add(rangeLastIndex);
                sequencesRanges.add(sequenceRange);
            }
        }

        return sequencesRanges;
    }

    /**
     * @param rowIdx - row index to create array from sequences on
     * @param sequencesParam - row sequences lengths
     * @param reverse - true if start filling row with chars from the end of array
     * @return List with row filled with sequence identifiers
     */
    private List<String> createRowArrayFromSequencesAndChars (int rowIdx, List<Integer> sequencesParam, boolean reverse) {

        List<Integer> sequences = sequencesParam;
        List<String> charsNeeded = generateArrayOfSequenceMarks(sequences.size());

        if (reverse) {
            sequences = reverseList(sequences);
            charsNeeded = reverseList(charsNeeded);
        }

        List<String> arrayFilledFromStart = createArrayOfEmptyFields(this.getWidth());

        boolean canStartSequenceFromIndex;// = false;
        boolean writeSequenceMode = false;
        int currentSequenceIdx = 0;
        int sequencesFieldsFilled = 0;
        String charToWrite = charsNeeded.get(currentSequenceIdx);
        int sequenceLength = sequences.get(currentSequenceIdx);
        boolean breakX = true;

        for(int fieldIdx = 0; fieldIdx < this.getWidth(); fieldIdx++ ) {

            if (!writeSequenceMode && currentSequenceIdx < charsNeeded.size() && breakX) {
                canStartSequenceFromIndex = checkIfCanStartSequenceFromRowIndex(rowIdx, fieldIdx, sequenceLength);
                if (canStartSequenceFromIndex) {
                    writeSequenceMode = true; // start fill fields with sequence char mark
                }
            }
            if (writeSequenceMode) { /* Marking rows with sequences marks */
                arrayFilledFromStart.set(fieldIdx, MARKED_ROW_INDICATOR + charToWrite + nonogramSolutionBoardWithMarks.get(rowIdx).get(fieldIdx).substring(2, 4));

                sequencesFieldsFilled++;

                if (sequencesFieldsFilled == sequenceLength) {
                    sequencesFieldsFilled = 0;
                    currentSequenceIdx++;
                    if (currentSequenceIdx < charsNeeded.size()) {
                        charToWrite = charsNeeded.get( currentSequenceIdx );
                        sequenceLength = sequences.get( currentSequenceIdx );
                    }
                    writeSequenceMode = false;
                    breakX = false;
                }
            } else {
                arrayFilledFromStart.set(fieldIdx, X_FIELD_MARKED_BOARD);
                breakX = true;
            }
        }

        return arrayFilledFromStart;
    }

    /**
     * @param arrLength - length(size) of created array
     * @return list of <arrLength> elements filled with "----"(empty)
     */
    private List<String> createArrayOfEmptyFields (int arrLength) {
        List<String> emptyFields = new ArrayList<>();
        for(int i = 0; i < arrLength; i++) {
            emptyFields.add("----");
        }
        return emptyFields;
    }

    private boolean checkIfCanStartSequenceFromRowIndex (int rowIdx, int fieldIdx, int sequenceLength) {
        List<String> fieldsToCheck = nonogramSolutionBoardWithMarks.get(rowIdx).subList(fieldIdx, fieldIdx + sequenceLength);

        Predicate<String> fieldWithX = field -> field.equals(X_FIELD_MARKED_BOARD);

        List<String> xs = fieldsToCheck.stream().filter(fieldWithX).toList();

        return xs.isEmpty();
    }

    // columns

    private List<List<List<Integer>>> inferInitialColumnsSequencesRanges() {
        List<List<List<Integer>>> initialColumnsSequencesRanges = new ArrayList<>();

        for(int columnIdx = 0; columnIdx < columnsSequences.size(); columnIdx ++) {
            initialColumnsSequencesRanges.add( inferInitialColumnSequencesRanges( columnsSequences.get(columnIdx), columnIdx) );
        }

        return initialColumnsSequencesRanges;
    }

    /**
     * @param columnIdx - column index
     * @return - initial column sequences ranges
     */
    private List<List<Integer>> inferInitialColumnSequencesRanges (List<Integer> sequences, int columnIdx) {

        List<String> arrayFilledFromStart = createColumnArrayFromSequencesAndChars(columnIdx, sequences, false);
        List<String> arrayFilledFromEnd = reverseList( createColumnArrayFromSequencesAndChars(columnIdx, sequences, true) );

        return inferColumnSequencesRangesFromArrays(arrayFilledFromStart, arrayFilledFromEnd);
    }

    /**
     * @param columnIdx - column index to create array from sequences on
     * @param sequencesParam - column sequences lengths
     * @param reverse - true if start filling column with chars from the end of array (nonogram column)
     * @return List with column filled with sequence identifiers
     */
    private List<String> createColumnArrayFromSequencesAndChars (int columnIdx, List<Integer> sequencesParam, boolean reverse) {

        List<Integer> sequences = sequencesParam;
        List<String> charsNeeded =  generateArrayOfSequenceMarks(sequences.size());

        if (reverse) {
            sequences = reverseList(sequences);
            charsNeeded = reverseList(charsNeeded);
        }

        int height = nonogramSolutionBoardWithMarks.size();
        List<String> arrayFilledFromStart = createArrayOfEmptyFields(height);

        boolean canStartSequenceFromIndex;
        boolean writeSequenceMode = false;
        int currentSequenceIdx = 0;
        int sequencesFieldsFilled = 0;
        String charToWrite = charsNeeded.get(currentSequenceIdx);
        int sequenceLength = sequences.get(currentSequenceIdx);
        boolean breakX = true;

        for(int fieldIdx = 0; fieldIdx < height; fieldIdx++ ) {

            if (!writeSequenceMode && currentSequenceIdx < charsNeeded.size() && breakX) {
                canStartSequenceFromIndex = checkIfCanStartSequenceFromColumnIndex(columnIdx, fieldIdx, sequenceLength);
                if (canStartSequenceFromIndex) {
                    writeSequenceMode = true; // start fill fields with sequence char mark
                }
            }
            if (writeSequenceMode) {

                arrayFilledFromStart.set(fieldIdx, nonogramSolutionBoardWithMarks.get(fieldIdx).get(columnIdx).substring(0, 2) + MARKED_COLUMN_INDICATOR + charToWrite);

                sequencesFieldsFilled++;

                if (sequencesFieldsFilled == sequenceLength) {
                    sequencesFieldsFilled = 0;
                    currentSequenceIdx++;
                    if (currentSequenceIdx < charsNeeded.size()) {
                        charToWrite = charsNeeded.get( currentSequenceIdx );
                        sequenceLength = sequences.get( currentSequenceIdx );
                    }
                    writeSequenceMode = false;
                    breakX = false;
                }
            } else {
                arrayFilledFromStart.set(fieldIdx, X_FIELD_MARKED_BOARD);
                breakX = true;
            }
        }

        return arrayFilledFromStart;
    }

    /**
     * @param columnIdx - column index to check if it is possible to start colouring sequence
     * @param fieldIdx - row index to check if it is possible to start colouring sequence in column
     * @param sequenceLength - sequence to colour length
     * @return true if there are not "X" on way, false otherwise
     */
    private boolean checkIfCanStartSequenceFromColumnIndex (int columnIdx, int fieldIdx, int sequenceLength) {
        List<String> fieldsToCheck = getSolutionBoardWithMarksColumn(columnIdx).subList(fieldIdx, fieldIdx + sequenceLength);

        Predicate<String> fieldWithX = field -> field.equals(X_FIELD_MARKED_BOARD);

        List<String> xs = fieldsToCheck.stream().filter(fieldWithX).toList();

        return xs.isEmpty();
    }


    /**
     * @param columnIdx - column index from solution board with marks to select
     * @return selected column with sequences marks
     */
    List<String> getSolutionBoardWithMarksColumn(int columnIdx) {
        List<String> boardColumn = new ArrayList<>();

        for (List<String> nonogramSolutionBoardWithMark : nonogramSolutionBoardWithMarks) {
            boardColumn.add(nonogramSolutionBoardWithMark.get(columnIdx));
        }

        return boardColumn;
    }

    /**
     * @param arrayFilledFromStart - array filled with sequences from the earliest possible field
     * @param arrayFilledFromEnd - array filled with sequences from end
     * @return sequences ranges in column calculated from sequences lengths
     */
    private List<List<Integer>> inferColumnSequencesRangesFromArrays (List<String> arrayFilledFromStart, List<String> arrayFilledFromEnd) {
        List<String> collectedSequences = new ArrayList<>();
        List<List<Integer>> sequencesRanges = new ArrayList<>();
        int rangeStartIndex;
        int rangeLastIndex;

        for(int idx = 0; idx < arrayFilledFromStart.size(); idx++) {
            String field = arrayFilledFromStart.get(idx);
            String fieldSequenceChar = field.substring(3, 4);
            if (field.indexOf(MARKED_COLUMN_INDICATOR) == 2 && !collectedSequences.contains(fieldSequenceChar)) {
                collectedSequences.add(fieldSequenceChar);
                rangeStartIndex = idx;
                rangeLastIndex = findLastIndexContaining(arrayFilledFromEnd, MARKED_COLUMN_INDICATOR + fieldSequenceChar);
                List<Integer> sequenceRange = new ArrayList<>();
                sequenceRange.add(rangeStartIndex);
                sequenceRange.add(rangeLastIndex);
                sequencesRanges.add(sequenceRange);
            }
        }

        return sequencesRanges;
    }

    /**
     * @return row sequences ranges from nonogram board filled fields
     */
    private List<List<List<Integer>>> inferRowsSequencesRangesFromSolutionBoard() {
        List<List<List<Integer>>> inferredRowsSequencesRanges = new ArrayList<>();

        for(int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            inferredRowsSequencesRanges.add(inferRowSequencesRangesFromSolutionBoardRow(rowIdx));
        }

        return inferredRowsSequencesRanges;
    }


    /**
     * @param rowIdx - row to infer sequences ranges
     * @return row sequences ranges inferred from nonogram solution board
     */
    private List<List<Integer>> inferRowSequencesRangesFromSolutionBoardRow(int rowIdx) {
        List<String> boardRow = this.getNonogramSolutionBoard().get(rowIdx);
        List<List<Integer>> boardRowSequencesRanges = new ArrayList<>();
        List<Integer> boardRowSequencesLengths = this.getRowsSequences().get(rowIdx);
        List<Integer> boardRowSequenceRange;
        int sequenceNo = 0;

        for(int columnIdx = 0; columnIdx < this.getWidth(); columnIdx++) {
            if (boardRow.get(columnIdx).equals(COLOURED_FIELD)) {
                boardRowSequenceRange = new ArrayList<>();
                boardRowSequenceRange.add(columnIdx);
                while(columnIdx < this.getWidth() && boardRow.get(columnIdx).equals(COLOURED_FIELD)) {
                    columnIdx++;
                }
                boardRowSequenceRange.add(columnIdx - 1);
                boardRowSequencesRanges.add(boardRowSequenceRange);
                sequenceNo++;
            }
            if (sequenceNo == boardRowSequencesLengths.size()) {
                break;
            }
        }

        return boardRowSequencesRanges;
    }

    /**
     * @return column sequences ranges from nonogram board filled fields
     */
    private List<List<List<Integer>>> inferColumnsSequencesRangesFromSolutionBoard() {
        List<List<List<Integer>>> inferredColumnsSequencesRanges = new ArrayList<>();

        for(int columnIdx = 0; columnIdx < this.getWidth(); columnIdx++) {
            inferredColumnsSequencesRanges.add(inferColumnSequencesRangesFromSolutionBoardColumn(columnIdx));
        }

        return inferredColumnsSequencesRanges;
    }

    /**
     * @param columnIdx - column to infer sequences ranges
     * @return column sequences ranges inferred from nonogram solution board
     */
    private List<List<Integer>> inferColumnSequencesRangesFromSolutionBoardColumn(int columnIdx) {
        List<String> boardColumn = getSolutionBoardColumn(nonogramSolutionBoard, columnIdx);
        List<List<Integer>> boardColumnSequencesRanges = new ArrayList<>();
        List<Integer> boardColumnSequencesLengths = this.getColumnsSequences().get(columnIdx);
        List<Integer> boardColumnSequenceRange;
        int sequenceNo = 0;

        for(int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            if (boardColumn.get(rowIdx).equals(COLOURED_FIELD)) {
                boardColumnSequenceRange = new ArrayList<>();
                boardColumnSequenceRange.add(rowIdx);
                while(rowIdx < this.getHeight() && boardColumn.get(rowIdx).equals(COLOURED_FIELD)) {
                    rowIdx++;
                }
                boardColumnSequenceRange.add(rowIdx - 1);
                boardColumnSequencesRanges.add(boardColumnSequenceRange);
                sequenceNo++;
            }
            if (sequenceNo == boardColumnSequencesLengths.size()) {
                break;
            }
        }

        return boardColumnSequencesRanges;
    }

    // common functions

    /**
     * @param marksNo - number of sequences marks to create array
     * @return list of marks created from given marksNo (1 -> ["a"], 5 -> ["a", "b", "c", "d", "e"], etc...)
     */
    private List<String> generateArrayOfSequenceMarks (int marksNo) {
        List<String> arrayOfSequenceMarks = new ArrayList<>();

        for(int charIdx = 0; charIdx < marksNo; charIdx++) {
            arrayOfSequenceMarks.add( indexToSequenceCharMark(charIdx) );
        }

        return arrayOfSequenceMarks;
    }


    /**
     * @param index - index that would be converted into char (sequence char mark)
     * @return sequence character ("a", "b", "c", ... )
     */
    public static String indexToSequenceCharMark(int index) {
        return Character.toString((char) ((int) 'a' + index));
    }

    /**
     * @param list - list to reverse
     * @return reversed list
     * @param <T> - type of list to reverse
     */
    public static<T> List<T> reverseList(List<T> list) {
        List<T> reverse = new ArrayList<>(list);
        Collections.reverse(reverse);
        return reverse;
    }

    /**
     * @param array - array to search
     * @param matchingSubstring - substring to find
     * @return array last index element that contains matchingSubstring or -1 if there is not such index
     */
    private int findLastIndexContaining (List<String> array, String matchingSubstring) {

        for( int arrIdx = array.size() - 1; arrIdx >= 0; arrIdx--) {
            if (array.get(arrIdx).contains(matchingSubstring)) {
                return arrIdx;
            }
        }

        return -1;
    }

    /**
     * function updates available choices to solve nonogram using method "trial and error"(?)
     */
    public void updateCurrentAvailableChoices() {
        List<Integer> rowFieldsNotToInclude;
        this.availableChoices = new ArrayList<>();
        NonogramSolutionDecision decision;
        Field decisionField;

        for(int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            rowFieldsNotToInclude = this.getRowsFieldsNotToInclude().get(rowIdx);
            if (!(rowFieldsNotToInclude.size() == this.getWidth())) {
                for(int columnIdx = 0; columnIdx < this.getWidth(); columnIdx++) {
                    if (!nonogramSolutionBoard.get(rowIdx).get(columnIdx).equals(COLOURED_FIELD) && !nonogramSolutionBoard.get(rowIdx).get(columnIdx).equals(X_FIELD)) {
                        decisionField = new Field(rowIdx, columnIdx);
                        decision = new NonogramSolutionDecision(X_FIELD, decisionField);
                        availableChoices.add(decision);
                    }
                }
            }
        }
    }

    /**
     * @param decision - taken decision, pointing to the row and column of the colored field
     */
    public void addAffectedRowAndColumnAfterColouringField(NonogramSolutionDecision decision) {
        int rowIdx = decision.getDecisionField().getRowIdx();
        int columnIdx = decision.getDecisionField().getColumnIdx();

        this.actionsToDoList.add(new NonogramActionDetails(rowIdx, NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS));
        this.actionsToDoList.add(new NonogramActionDetails(columnIdx, NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS));
        this.actionsToDoList.add(new NonogramActionDetails(rowIdx, NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW));
        this.actionsToDoList.add(new NonogramActionDetails(columnIdx, NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN));
        this.actionsToDoList.add(new NonogramActionDetails(columnIdx, NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES));
        this.actionsToDoList.add(new NonogramActionDetails(columnIdx, NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES));
        this.actionsToDoList.add(new NonogramActionDetails(rowIdx, NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW));
        this.actionsToDoList.add(new NonogramActionDetails(columnIdx, NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN));
    }

    /**
     * @param decision - taken decision, pointing to the row and column of the field where "X" has been placed
     */
    public void addAffectedRowAndColumnAfterPlacingXAtField(NonogramSolutionDecision decision) {
        int rowIdx = decision.getDecisionField().getRowIdx();
        int columnIdx = decision.getDecisionField().getColumnIdx();

        this.actionsToDoList.add(new NonogramActionDetails(rowIdx, NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES));
        this.actionsToDoList.add(new NonogramActionDetails(columnIdx, NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES));
        this.actionsToDoList.add(new NonogramActionDetails(rowIdx, CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY));
        this.actionsToDoList.add(new NonogramActionDetails(columnIdx, NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY));
        this.actionsToDoList.add(new NonogramActionDetails(rowIdx, NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW));
        this.actionsToDoList.add(new NonogramActionDetails(columnIdx, NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN));
        this.actionsToDoList.add(new NonogramActionDetails(rowIdx, NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES));
        this.actionsToDoList.add(new NonogramActionDetails(columnIdx, NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES));
    }

    public void basicSolve() {
        int actionListIndex = 0;
        int nextActionRCIndex; // row or column index
        int stepsBefore;
        int stepsAfter;
        NonogramActionDetails currentActionDetails;

        while(actionListIndex < actionsToDoList.size()) {
            currentActionDetails = actionsToDoList.get(actionListIndex);
            nextActionRCIndex = currentActionDetails.getIndex();
            try {
                if (getRowSolveActions().contains(currentActionDetails.getActionName())) {
                    this.copyLogicToNonogramRowLogic();

                    stepsBefore = this.getNonogramState().getNewStepsMade();

                    makeProperActionInRow(nextActionRCIndex, currentActionDetails.getActionName());
                    stepsAfter = this.getNonogramState().getNewStepsMade();

                    if (stepsBefore != stepsAfter) {
                        logRowStateBefore(currentActionDetails, nextActionRCIndex);
                        logRowStateAfter(currentActionDetails, nextActionRCIndex);
                    }

                    this.copyLogicFromNonogramRowLogic();
                } else {
                    this.copyLogicToNonogramColumnLogic();

                    stepsBefore = this.getNonogramState().getNewStepsMade();

                    makeProperActionInColumn(nextActionRCIndex, currentActionDetails.getActionName());
                    stepsAfter = this.getNonogramState().getNewStepsMade();

                    if (stepsBefore != stepsAfter) {
                        logColumnStateBefore(currentActionDetails, nextActionRCIndex);
                        logColumnStateAfter(currentActionDetails, nextActionRCIndex);
                    }

                    this.copyLogicFromNonogramColumnLogic();
                }
                if (this.getNonogramSolutionBoard().get(10).get(6).equals("O")) {
                    System.out.println("Should place X if O will create too long possible sequence");
                    System.out.println("-".repeat(20));
                }
            } catch (Exception e) {
                // empty
            }

            actionListIndex++;

            if (this.guessMode && this.nonogramState.isInvalidSolution()) {
                break;
            }
        }
    }

//    private void stopConditionRow(NonogramActionDetails actionDetails) {
//        if (this.getNonogramSolutionBoard().get(15).get(17).equals("O")) {
//            System.out.println("Should place X if O will create too long possible sequence");
//            System.out.println("-".repeat(20));
//        }
//    }

    private void logRowStateBefore(NonogramActionDetails actionDetails, int nextActionRowIndex) {
        String elementToLog;

        if (NonogramSolveAction.getCorrectRowRangesSolveActions().contains(actionDetails.getActionName())) {
            elementToLog = this.getRowsSequencesRanges().get(nextActionRowIndex).toString();
        } else if (NonogramSolveAction.isMarkRowAction(actionDetails.getActionName())) {
            elementToLog = this.getNonogramSolutionBoardWithMarks().get(nextActionRowIndex).toString();
        } else {
            elementToLog = this.getNonogramSolutionBoard().get(nextActionRowIndex).toString();
        }

        log.info("Row {} before making action {}: {}", nextActionRowIndex, actionDetails.getActionName(), elementToLog);
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

        log.info("Row {} after  making action {}: {}", nextActionRowIndex, actionDetails.getActionName(), elementToLog);
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
                nonogramRowLogic.correctRowSequencesRanges(rowIdx);
                if (guessMode) {
                    invalidateSolutionIfRowSequencesWrong(rowIdx);
                }
            }
            case CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS -> {
                nonogramRowLogic.correctRowSequencesRangesWhenMetColouredField(rowIdx);
                if (guessMode) {
                    invalidateSolutionIfRowSequencesWrong(rowIdx);
                }
            }
            case CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY -> {
                nonogramRowLogic.correctRowRangeIndexesIfXOnWay(rowIdx);
                if (guessMode) {
                    invalidateSolutionIfRowSequencesWrong(rowIdx);
                }
            }
            case COLOUR_OVERLAPPING_FIELDS_IN_ROW -> nonogramRowLogic.fillOverlappingFieldsInRow(rowIdx);
            case EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW -> {
                nonogramRowLogic.extendColouredFieldsToLeftNearXToMaximumPossibleLengthInRow(rowIdx);
                nonogramRowLogic.extendColouredFieldsToRightNearXToMaximumPossibleLengthInRow(rowIdx);
            }
            case PLACE_XS_ROW_AT_UNREACHABLE_FIELDS -> nonogramRowLogic.placeXsRowAtUnreachableFields(rowIdx);
            case PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES -> nonogramRowLogic.placeXsAroundLongestSequencesInRow(rowIdx);
            case PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES ->
                    nonogramRowLogic.placeXsRowAtTooShortEmptySequences(rowIdx);
            case PLACE_XS_ROW_IF_O_WILL_CREATE_TOO_LONG_COLOURED_SEQUENCE ->
                    nonogramRowLogic.placeXsRowIfOWillCreateTooLongColouredSequence(rowIdx);
            case MARK_AVAILABLE_FIELDS_IN_ROW -> nonogramRowLogic.markAvailableFieldsInRow(rowIdx);
            default -> {
                // empty
            }
        }
    }

    public void makeProperActionInColumn(int columnIdx, NonogramSolveAction actionToDoInColumn) {
        switch (actionToDoInColumn) {
            case CORRECT_COLUMN_SEQUENCES_RANGES -> {
                nonogramColumnLogic.correctColumnSequencesRanges(columnIdx);
                if (guessMode) {
                    invalidateSolutionIfColumnSequencesWrong(columnIdx);
                }
            }
            case CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS -> {
                nonogramColumnLogic.correctColumnSequencesWhenMetColouredField(columnIdx);
                if (guessMode) {
                    invalidateSolutionIfColumnSequencesWrong(columnIdx);
                }
            }
            case CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY -> {
                nonogramColumnLogic.correctColumnRangeIndexesIfXOnWay(columnIdx);
                if (guessMode) {
                    invalidateSolutionIfColumnSequencesWrong(columnIdx);
                }
            }
            case COLOUR_OVERLAPPING_FIELDS_IN_COLUMN -> nonogramColumnLogic.fillOverlappingFieldsInColumn(columnIdx);
            case EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN -> {
                nonogramColumnLogic.extendColouredFieldsToTopNearXToMaximumPossibleLengthInColumn(columnIdx);
                nonogramColumnLogic.extendColouredFieldsToBottomNearXToMaximumPossibleLengthInColumn(columnIdx);
            }
            case PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS ->
                    nonogramColumnLogic.placeXsColumnAtUnreachableFields(columnIdx);
            case PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES ->
                    nonogramColumnLogic.placeXsAroundLongestSequencesInColumn(columnIdx);
            case PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES ->
                    nonogramColumnLogic.placeXsColumnAtTooShortEmptySequences(columnIdx);
//            case PLACE_XS_COLUMN_IF_O_WILL_CREATE_TOO_LONG_COLOURED_SEQUENCE ->
//                    nonogramColumnLogic.placeXsColumnIfOWillCreateTooLongSequence(columnIdx);
            case MARK_AVAILABLE_FIELDS_IN_COLUMN ->
                    nonogramColumnLogic.markAvailableFieldsInColumn(columnIdx);
            default -> {
                // empty
            }
        }
    }

    private void invalidateSolutionIfColumnSequencesWrong(int columnIndex) {
        List<Integer> sequencesLengths = this.nonogramColumnLogic.getColumnsSequences().get(columnIndex);
        List<List<Integer>> columnSequencesRanges = this.nonogramColumnLogic.getColumnsSequencesRanges().get(columnIndex);

        for(int seqNo = 0; seqNo < columnSequencesRanges.size(); seqNo++) {
            if (rangeLength(columnSequencesRanges.get(seqNo)) < sequencesLengths.get(seqNo)) {
                this.nonogramColumnLogic.getNonogramState().invalidateSolution();
                break;
            }
        }
    }

    private void invalidateSolutionIfRowSequencesWrong(int rowIndex) {
        List<Integer> sequencesLengths = this.getRowsSequences().get(rowIndex);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIndex);

        for(int seqNo = 0; seqNo < rowSequencesRanges.size(); seqNo++) {
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

        this.rowsSequencesRanges = copySequencesRanges(this.nonogramColumnLogic.getRowsSequencesRanges());
        this.columnsSequencesRanges = copySequencesRanges(this.nonogramColumnLogic.getColumnsSequencesRanges());

        this.rowsFieldsNotToInclude = copyTwoDeepList(this.nonogramColumnLogic.getRowsFieldsNotToInclude());
        this.columnsFieldsNotToInclude = copyTwoDeepList(this.nonogramColumnLogic.getColumnsFieldsNotToInclude());
        this.rowsSequencesIdsNotToInclude = copyTwoDeepList(this.nonogramColumnLogic.getRowsSequencesIdsNotToInclude());
        this.columnsSequencesIdsNotToInclude = copyTwoDeepList(this.nonogramColumnLogic.getColumnsSequencesIdsNotToInclude());

        this.actionsToDoList = this.nonogramColumnLogic.getActionsToDoList();

        this.nonogramState = this.nonogramColumnLogic.getNonogramState();
    }

    public void copyLogicToNonogramColumnLogic() {
        this.nonogramColumnLogic.setNonogramState(this.getNonogramState());
        this.nonogramColumnLogic.setLogs(this.getLogs());

        this.nonogramColumnLogic.setRowsSequencesRanges(copySequencesRanges(this.getRowsSequencesRanges()));
        this.nonogramColumnLogic.setRowsSequencesIdsNotToInclude(copyTwoDeepList(this.getRowsSequencesIdsNotToInclude()));
        this.nonogramColumnLogic.setRowsFieldsNotToInclude(copyTwoDeepList(this.getRowsFieldsNotToInclude()));

        this.nonogramColumnLogic.setColumnsSequencesRanges(copySequencesRanges(this.getColumnsSequencesRanges()));
        this.nonogramColumnLogic.setColumnsSequencesIdsNotToInclude(copyTwoDeepList(this.getColumnsSequencesIdsNotToInclude()));
        this.nonogramColumnLogic.setColumnsFieldsNotToInclude(copyTwoDeepList(this.getColumnsFieldsNotToInclude()));

        this.nonogramColumnLogic.setNonogramSolutionBoardWithMarks(copyTwoDeepList(this.getNonogramSolutionBoardWithMarks()));
        this.nonogramColumnLogic.setNonogramSolutionBoard(copyTwoDeepList(this.getNonogramSolutionBoard()));
        this.nonogramColumnLogic.setActionsToDoList(this.getActionsToDoList());
    }

    public void copyLogicFromNonogramRowLogic() {
        this.logs = this.nonogramRowLogic.getLogs();

        this.nonogramSolutionBoardWithMarks = copyTwoDeepList(this.nonogramRowLogic.getNonogramSolutionBoardWithMarks());
        this.nonogramSolutionBoard = copyTwoDeepList(this.nonogramRowLogic.getNonogramSolutionBoard());

        this.rowsSequencesRanges = copySequencesRanges(this.nonogramRowLogic.getRowsSequencesRanges());
        this.columnsSequencesRanges = copySequencesRanges(this.nonogramRowLogic.getColumnsSequencesRanges());

        this.rowsFieldsNotToInclude = copyTwoDeepList(this.nonogramRowLogic.getRowsFieldsNotToInclude());
        this.columnsFieldsNotToInclude = copyTwoDeepList(this.nonogramRowLogic.getColumnsFieldsNotToInclude());
        this.rowsSequencesIdsNotToInclude = copyTwoDeepList(this.nonogramRowLogic.getRowsSequencesIdsNotToInclude());
        this.columnsSequencesIdsNotToInclude = copyTwoDeepList(this.nonogramRowLogic.getColumnsSequencesIdsNotToInclude());

        this.actionsToDoList = this.nonogramRowLogic.getActionsToDoList();

        this.nonogramState = this.nonogramRowLogic.getNonogramState();
    }

    public void copyLogicToNonogramRowLogic() {
        this.nonogramRowLogic.setNonogramState(this.getNonogramState());
        this.nonogramRowLogic.setLogs(this.getLogs());

        this.nonogramRowLogic.setRowsSequencesRanges(copySequencesRanges(this.getRowsSequencesRanges()));
        this.nonogramRowLogic.setRowsSequencesIdsNotToInclude(copyTwoDeepList(this.getRowsSequencesIdsNotToInclude()));
        this.nonogramRowLogic.setRowsFieldsNotToInclude(copyTwoDeepList(this.getRowsFieldsNotToInclude()));

        this.nonogramRowLogic.setColumnsSequencesRanges(copySequencesRanges(this.getColumnsSequencesRanges()));
        this.nonogramRowLogic.setColumnsSequencesIdsNotToInclude(copyTwoDeepList(this.getColumnsSequencesIdsNotToInclude()));
        this.nonogramRowLogic.setColumnsFieldsNotToInclude(copyTwoDeepList(this.getColumnsFieldsNotToInclude()));

        this.nonogramRowLogic.setNonogramSolutionBoardWithMarks(copyTwoDeepList(this.getNonogramSolutionBoardWithMarks()));
        this.nonogramRowLogic.setNonogramSolutionBoard(copyTwoDeepList(this.getNonogramSolutionBoard()));
        this.nonogramRowLogic.setActionsToDoList(this.getActionsToDoList());
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


    /**
     * @param solutionFileName - String fileName with correct solution board
     * @return true if fields are colored correctly in the partial solution, false in other cases
     */
    public boolean subsolutionBoardCorrectComparisonWithSolutionBoard(String solutionFileName) {
        List<List<String>> subsolutionNonogramBoard = this.nonogramSolutionBoard;
        NonogramBoardTemplate solutionBoardTemplate = new NonogramBoardTemplate(solutionFileName);
        System.out.println("solution board template:");
        solutionBoardTemplate.printBoard();
        List<List<String>> solutionNonogramBoard = solutionBoardTemplate.getBoard();

        Iterator<List<String>> subsolutionNonogramBoardIterator = subsolutionNonogramBoard.iterator();
        Iterator<List<String>> solutionNonogramBoardIterator = solutionNonogramBoard.iterator();

        while(subsolutionNonogramBoardIterator.hasNext() && solutionNonogramBoardIterator.hasNext()) {

            List<String> subsolutionNonogramBoardRow = subsolutionNonogramBoardIterator.next();
            List<String> solutionNonogramBoardRow = solutionNonogramBoardIterator.next();

            System.out.println("rows (subsolution and solution)");
            System.out.println(subsolutionNonogramBoardRow);
            System.out.println(solutionNonogramBoardRow);

            Iterator<String> subsolutionNonogramBoardRowIterator = subsolutionNonogramBoardRow.iterator();
            Iterator<String> solutionNonogramBoardRowIterator = solutionNonogramBoardRow.iterator();

            while(subsolutionNonogramBoardRowIterator.hasNext() && solutionNonogramBoardRowIterator.hasNext()) {
                String subsolutionNonogramBoardField = subsolutionNonogramBoardRowIterator.next();
                String solutionNonogramBoardField = solutionNonogramBoardRowIterator.next();

                // "X" or "O"
                if (subsolutionNonogramBoardField.equals(COLOURED_FIELD)) {
                    if (!subsolutionNonogramBoardField.equals(solutionNonogramBoardField)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * @param x_field - field(rowIdx, columnIdx) on which place X
     * @return NonogramLogic object with "X" placed on specified position
     */
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
        for(int colIdx = 0; colIdx < this.getWidth(); colIdx++) {
            this.nonogramSolutionBoard.get(rowIdx).set(colIdx, boardRow.get(colIdx));
        }
        return this;
    }

    public boolean isNonogramRowSymmetrical() {
        return areOriginalAndReversedListIdentical( this.getRowsSequences() );
    }

    public boolean isNonogramColumnSymmetrical() {
        return areOriginalAndReversedListIdentical( this.getColumnsSequences() );
    }

    public static boolean areOriginalAndReversedListIdentical(List<List<Integer>> listOfIntegers) {

        List<List<Integer>> reversedList = new ArrayList<>(listOfIntegers);
        Collections.reverse(reversedList);

        return reversedList.equals(listOfIntegers);
    }

    /**
     * @return true if nonogram is row XOR column symmetrical (maximum one of these conditions)
     */
    public boolean isNonogram1DSymmetrical() {
        return LogicFunctions.xor(isNonogramRowSymmetrical(), isNonogramColumnSymmetrical());
    }

    /**
     * @return true if rows sequences and columns sequences are symmetrical (reversed identical with original),
     * but rows sequences list isn't identical with columns sequences list
     */
    public boolean isNonogram2DSymmetrical() {
        return isNonogramRowSymmetrical() && isNonogramColumnSymmetrical() && !areRowsSequencesIdenticalWithColumnsSequences();
    }

    /***
     * @return true if rows sequences are equal to columns sequences, and both are symmetrical
     * (reversed identical with original), false in other cases
     */
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
        if (this.rowsSequences.size() != this.columnsSequences.size()) {
            return false; // Różna liczba podlist - listy nie są identyczne
        }

        for (int i = 0; i < rowsSequences.size(); i++) {
            List<Integer> rowSequences = rowsSequences.get(i);
            List<Integer> columnSequences = columnsSequences.get(i);

            if (!rowSequences.equals(columnSequences)) {
                return false;
            }
        }

        return true;
    }

    public void printRowsSequencesRanges() {
        int rowIdx = 0;
        System.out.println("Rows sequences ranges:");
        for(List<List<Integer>> rowSequencesRanges : this.getRowsSequencesRanges()) {
            System.out.println(rowIdx + ": " + rowSequencesRanges);
            rowIdx++;
        }
    }

    public void printColumnsSequencesRanges() {
        int columnIdx = 0;
        System.out.println("Columns sequences ranges:");
        for(List<List<Integer>> columnSequencesRanges : this.getColumnsSequencesRanges()) {
            System.out.println(columnIdx + ": " + columnSequencesRanges);
            columnIdx++;
        }
    }
}
