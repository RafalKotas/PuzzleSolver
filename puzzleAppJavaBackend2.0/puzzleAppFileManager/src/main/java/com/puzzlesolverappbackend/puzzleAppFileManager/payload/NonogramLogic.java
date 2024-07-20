package com.puzzlesolverappbackend.puzzleAppFileManager.payload;

import com.puzzlesolverappbackend.puzzleAppFileManager.logicOperators.LogicFunctions;
import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.ActionEnum;
import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.NonogramActionDefinition;
import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.NonogramSolutionDecision;
import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.NonogramState;
import com.puzzlesolverappbackend.puzzleAppFileManager.templates.nonogram.NonogramBoardTemplate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.puzzlesolverappbackend.puzzleAppFileManager.payload.ActionsConstants.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.ActionEnum.*;
import static com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.NonogramState.buildInitialEmptyNonogramState;
import static com.puzzlesolverappbackend.puzzleAppFileManager.services.NonogramLogicService.rangeInsideAnotherRange;
import static com.puzzlesolverappbackend.puzzleAppFileManager.services.NonogramLogicService.rangeLength;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NonogramLogic extends NonogramLogicParams {
    private static final Logger log = LoggerFactory.getLogger(NonogramLogic.class);

    private NonogramState nonogramState;

    private List<NonogramActionDefinition> actionsToDoList;

    private final static int LAST_ROW_ACTION_ENUM_VALUE = 8;

    private boolean guessMode;

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

    private List<NonogramActionDefinition> generateInitialActionsToDo(int height, int width) {

        List<NonogramActionDefinition> overlappingActionsAllRows = IntStream.range(0, height)
                .mapToObj(rowIdx -> new NonogramActionDefinition(rowIdx, COLOUR_OVERLAPPING_FIELDS_IN_ROW))
                .toList();
        List<NonogramActionDefinition> overlappingActionsAllColumns = IntStream.range(0, width)
                .mapToObj(columnIdx -> new NonogramActionDefinition(columnIdx, COLOUR_OVERLAPPING_FIELDS_IN_COLUMN))
                .toList();

        return Stream.concat(overlappingActionsAllRows.stream(), overlappingActionsAllColumns.stream())
                .toList();
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
                that.getNonogramState().isInvalidSolution());
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
                         boolean isSolutionInvalid) {
        this.logs = new ArrayList<>();

        this.rowsSequences = new ArrayList<>(rowsSequences);
        this.columnsSequences = new ArrayList<>(columnsSequences);
        this.rowsSequencesRanges = new ArrayList<>(rowsSequencesRanges);
        this.columnsSequencesRanges = new ArrayList<>(columnsSequencesRanges);
        this.rowsFieldsNotToInclude = new ArrayList<>(rowsFieldsNotToInclude);
        this.columnsFieldsNotToInclude = new ArrayList<>(columnsFieldsNotToInclude);
        this.rowsSequencesIdsNotToInclude = new ArrayList<>(rowsSequencesIdsNotToInclude);
        this.columnsSequencesIdsNotToInclude = new ArrayList<>(columnsSequencesIdsNotToInclude);

        this.nonogramSolutionBoard = new ArrayList<>(nonogramSolutionBoard);
        this.nonogramSolutionBoardWithMarks = new ArrayList<>(nonogramSolutionBoardWithMarks);

        this.availableChoices = new ArrayList<>();

        this.nonogramState = new NonogramState(0, isSolutionInvalid);
    }

    public void clearLogs() {
        this.logs.clear();
    }

    /***
     * @param height - height of initial solution board (with or without sequence idx marks)
     * @param width - width of initial solution board (with or without sequence idx marks)
     * @param emptyFieldCharRepeatCount - how many chars (every initial field - empty field) should have
     * @return empty board of given height, width with "-" on every field repeated emptyFieldCharRepeatCount times
     */
    private List<List<String>> generateEmptyBoard(int height, int width, int emptyFieldCharRepeatCount) {
        List<List<String>> emptyBoard = new ArrayList<>(height);
        for(int rowIdx = 0; rowIdx < height; rowIdx++) {
            List<String> boardRow = new ArrayList<>();
            for(int column = 0; column < width; column++) {
                boardRow.add("-".repeat(emptyFieldCharRepeatCount));
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
        for(int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            if(isRowTrivial(rowIdx)) {
                addAllRowSequencesIdxToNotToInclude(rowIdx);
                addFillTrivialRowLog(rowIdx);
                int seqNo = 0;
                int subsequentXs = 0;
                List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
                List<Integer> rowSequenceRange = rowSequencesRanges.get(seqNo);
                for(int columnIdx = 0; columnIdx < this.getWidth(); columnIdx++) {
                    if(rangeInsideAnotherRange(List.of(columnIdx), rowSequenceRange)) {
                        subsequentXs = 0;
                        fillTrivialRowField(rowIdx, columnIdx, seqNo);
                        addRowFieldToNotToInclude(rowIdx, columnIdx);
                        addColumnToAffectedActionsByIdentifiers(columnIdx, actionsToDoAfterColouringFieldInTrivialRow);
                    } else {
                        placeXAtGivenPosition(rowIdx, columnIdx);
                        addRowFieldToNotToInclude(rowIdx, columnIdx);
                        addColumnFieldToNotToInclude(columnIdx, rowIdx);
                        addColumnToAffectedActionsByIdentifiers(columnIdx, actionsToDoAfterPlacingXInTrivialRow);
                        subsequentXs += 1;
                        if(subsequentXs == 1 && seqNo + 1 < rowSequencesRanges.size()) {
                            rowSequenceRange = this.getRowsSequencesRanges().get(rowIdx).get(++seqNo);
                        } else {
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean isRowTrivial(int rowIdx) {
        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        for(int seqNo = 0; seqNo < rowSequencesLengths.size(); seqNo++) {
            if(rowSequencesLengths.get(seqNo) != rangeLength(rowSequencesRanges.get(seqNo))) {
                return false;
            }
        }

        return true;
    }

    private void addAllRowSequencesIdxToNotToInclude(int rowIdx) {
        IntStream.range(0, this.getRowsSequences().get(rowIdx).size()).boxed().forEach(seqNo -> this.addRowSequenceIdxToNotToInclude(rowIdx, seqNo));
    }

    public void addRowSequenceIdxToNotToInclude(int rowIdx, int seqIdx) {
        if(!this.rowsSequencesIdsNotToInclude.get(rowIdx).contains(seqIdx)) {
            tmpLog = generateAddingRowSequenceToNotToIncludeDescription(rowIdx, seqIdx);
            addLog(tmpLog);
            this.rowsSequencesIdsNotToInclude.get(rowIdx).add(seqIdx);
            Collections.sort(this.rowsSequencesIdsNotToInclude.get(rowIdx));
        }
    }

    public String generateAddingRowSequenceToNotToIncludeDescription(int rowIdx, int seqNo) {
        return String.format("ROW %d - SeqNo = %d added to not to include", rowIdx, seqNo);
    }

    private void addFillTrivialRowLog(int rowIdx) {
        tmpLog = String.format("ROW %d is trivial - filling whole", rowIdx);
        addLog(tmpLog);
    }

    private void fillTrivialRowField(int rowIdx, int columnIdx, int seqNo) {
        String seqMark = indexToSequenceCharMark(seqNo);
        colourFieldAtGivenPosition(rowIdx, columnIdx);
        updateNonogramBoardFieldWithMarksInRow(rowIdx, columnIdx, seqMark);
    }

    private void updateNonogramBoardFieldWithMarksInRow(int rowIdx, int columnIdx, String seqMark) {
        String updatedRowPartFieldWithMarks = "R" + seqMark;
        String columnPartFromFieldWithMarks = getColumnPartFromFieldWithMarks(rowIdx, columnIdx);
        String updatedFieldWithMarks = updatedRowPartFieldWithMarks + columnPartFromFieldWithMarks;

        this.getNonogramSolutionBoardWithMarks().get(rowIdx).set(columnIdx, updatedFieldWithMarks);
    }

    private String getColumnPartFromFieldWithMarks(int rowIdx, int columnIdx) {
        return this.getNonogramSolutionBoardWithMarks().get(rowIdx).get(columnIdx).substring(2);
    }

    private void fillTrivialColumns() {
        for(int columnIdx = 0; columnIdx < this.getWidth(); columnIdx++) {
            if(isColumnTrivial(columnIdx)) {
                addAllColumnSequencesIdxToNotToInclude(columnIdx);
                addFillTrivialColumnLog(columnIdx);
                int seqNo = 0;
                int subsequentXs = 0;
                List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
                List<Integer> columnSequenceRange = columnSequencesRanges.get(seqNo);
                for(int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
                    if(rangeInsideAnotherRange(List.of(rowIdx), columnSequenceRange)) {
                        subsequentXs = 0;
                        fillTrivialColumnField(columnIdx, rowIdx, seqNo);
                        addColumnFieldToNotToInclude(columnIdx, rowIdx);
                        addRowToAffectedActionsByIdentifiers(rowIdx, actionsToDoAfterColouringFieldInTrivialColumn);
                    } else {
                        placeXAtGivenPosition(rowIdx, columnIdx);
                        addColumnFieldToNotToInclude(columnIdx, rowIdx);
                        addRowFieldToNotToInclude(rowIdx, columnIdx);
                        addRowToAffectedActionsByIdentifiers(rowIdx, actionsToDoAfterPlacingXInTrivialColumn);
                        subsequentXs += 1;
                        if(subsequentXs == 1 && seqNo + 1 < columnSequencesRanges.size()) {
                            columnSequenceRange = columnSequencesRanges.get(++seqNo);
                        } else {
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean isColumnTrivial(int columnIdx) {
        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        for(int seqNo = 0; seqNo < columnSequencesLengths.size(); seqNo++) {
            if(columnSequencesLengths.get(seqNo) != rangeLength(columnSequencesRanges.get(seqNo))) {
                return false;
            }
        }

        return true;
    }

    private void addAllColumnSequencesIdxToNotToInclude(int columnIdx) {
        IntStream.range(0, this.getColumnsSequences().get(columnIdx).size()).boxed().forEach(seqNo -> this.addColumnSequenceIdxToNotToInclude(columnIdx, seqNo));
    }

    public void addColumnSequenceIdxToNotToInclude(int columnIdx, int seqIdx) {
        if(!this.columnsSequencesIdsNotToInclude.get(columnIdx).contains(seqIdx)) {
            tmpLog = generateAddingColumnSequenceToNotToIncludeDescription(columnIdx, seqIdx);
            addLog(tmpLog);
            this.columnsSequencesIdsNotToInclude.get(columnIdx).add(seqIdx);
            Collections.sort(this.columnsSequencesIdsNotToInclude.get(columnIdx));
        }
    }

    public String generateAddingColumnSequenceToNotToIncludeDescription(int columnIdx, int seqNo) {
        return String.format("COLUMN %d - SeqNo = %d added to not to include", columnIdx, seqNo);
    }

    private void addFillTrivialColumnLog(int columnIdx) {
        tmpLog = String.format("COLUMN %d is trivial - filling whole", columnIdx);
        addLog(tmpLog);
    }

    private void fillTrivialColumnField(int columnIdx, int rowIdx, int seqNo) {
        String seqMark = indexToSequenceCharMark(seqNo);
        colourFieldAtGivenPosition(rowIdx, columnIdx);
        updateNonogramBoardFieldWithMarksInColumn(columnIdx, rowIdx, seqMark);
    }

    private void updateNonogramBoardFieldWithMarksInColumn(int columnIdx, int rowIdx, String seqMark) {
        String rowPartFromFieldWithMarks = getRowPartFromFieldWithMarks(rowIdx, columnIdx);
        String updatedColumnPartFieldWithMarks = "C" + seqMark;
        String updatedFieldWithMarks = rowPartFromFieldWithMarks + updatedColumnPartFieldWithMarks;

        this.nonogramSolutionBoardWithMarks.get(rowIdx).set(columnIdx, updatedFieldWithMarks);
    }

    private String getRowPartFromFieldWithMarks(int rowIdx, int columnIdx) {
        return this.nonogramSolutionBoardWithMarks.get(rowIdx).get(columnIdx).substring(0, 2);
    }

    /**
     * @param rowIdx - field row index
     * @param columnIdx - field column index
     */
    public void colourFieldAtGivenPosition(int rowIdx, int columnIdx) {
        boolean rowValid = isRowIndexValid(rowIdx);
        boolean columnValid = isColumnIndexValid(columnIdx);
        if(rowValid && columnValid) {
            this.nonogramSolutionBoard.get(rowIdx).set(columnIdx, "O");
        }
    }

    /**
     * @param rowIdx - row index to validate if is in range <0, height)
     * @return true if row index is valid(in range) or false if not
     */
    private boolean isRowIndexValid (int rowIdx) {
        return rowIdx >= 0 && rowIdx < this.getHeight();
    }

    /**
     * @param columnIdx - column index to validate if is in range <0, width - 1>
     * @return true if column index is valid(in range) or false if not
     */
    private boolean isColumnIndexValid (int columnIdx) {
        return columnIdx >= 0 && columnIdx < this.getWidth();
    }

    /**
     * @param rowIdx - row index to exclude field ("X" or part of fully coloured and marked sequence)
     * @param columnIdx - field column index to exclude in given row
     * @return NonogramLogic object with field in given row excluded
     */
    public NonogramLogic addRowFieldToNotToInclude(int rowIdx, int columnIdx) {
        boolean rowValid = isRowIndexValid(rowIdx);
        boolean columnValid = isColumnIndexValid(columnIdx);
        List<Integer> fieldsNotToIncludeSorted;
        if(rowValid && columnValid && !this.rowsFieldsNotToInclude.get(rowIdx).contains(columnIdx)) {
            this.rowsFieldsNotToInclude.get(rowIdx).add(columnIdx);

            fieldsNotToIncludeSorted = this.rowsFieldsNotToInclude.get(rowIdx);
            Collections.sort(fieldsNotToIncludeSorted);
        }

        return this;
    }

    /**
     * @param columnIdx - column index to exclude field ("X" or part of fully coloured and marked sequence)
     * @param rowIdx - field row index to exclude in given column
     * @return NonogramLogic object with field in given column excluded
     */
    public NonogramLogic addColumnFieldToNotToInclude(int columnIdx, int rowIdx) {
        boolean rowValid = isRowIndexValid(rowIdx);
        boolean columnValid = isColumnIndexValid(columnIdx);
        if(rowValid && columnValid && !this.columnsFieldsNotToInclude.get(columnIdx).contains(rowIdx)) {
            this.columnsFieldsNotToInclude.get(columnIdx).add(rowIdx);
            Collections.sort(this.columnsFieldsNotToInclude.get(columnIdx));
        }

        return this;
    }

    public int maximumColumnIndexWithoutX(int rowIdx, int firstSequenceColumnIdx, int sequenceFullLength) {
        List<String> boardRow = this.getNonogramSolutionBoard().get(rowIdx);
        int maximumColumnIndex = firstSequenceColumnIdx;
        int maximumColumnIndexLimit = Math.min(firstSequenceColumnIdx + sequenceFullLength - 1, this.getWidth() - 1);
        String fieldMark;

        for(; maximumColumnIndex <= maximumColumnIndexLimit; maximumColumnIndex++) {
            fieldMark = boardRow.get(maximumColumnIndex);
            if(fieldMark.equals("X")) {
                break;
            }
        }

        return maximumColumnIndex - 1;
    }

    private List<List<List<Integer>>> inferInitialRowsSequencesRanges() {
        List<List<List<Integer>>> initialRowSequencesRanges = new ArrayList<>();

        for(int rowIdx = 0; rowIdx < rowsSequences.size(); rowIdx++) {
            initialRowSequencesRanges.add( inferInitialRowSequencesRanges( rowsSequences.get(rowIdx), rowIdx) );
        }

        return initialRowSequencesRanges;
    }

    /**
     * @param sequencesLengths - row sequences lengths
     * @param rowIdx - row index
     * @return - initial row sequences ranges
     */
    private List<List<Integer>> inferInitialRowSequencesRanges (List<Integer> sequencesLengths, int rowIdx) {

        List<String> arrayFilledFromStart = createRowArrayFromSequencesAndChars(rowIdx, sequencesLengths, false);
        // list filled from end and reversed to place the latest sequences on array end
        List<String> arrayFilledFromEnd = reverseList(createRowArrayFromSequencesAndChars(rowIdx, sequencesLengths, true));

        return inferRowSequencesRangesFromArrays(arrayFilledFromStart, arrayFilledFromEnd);
    }


    /**
     * @param arrayFilledFromStart - array filled with sequences from the earliest possible field
     * @param arrayFilledFromEnd - array filled with sequences from
     * @return sequences ranges in row calculated from sequences lengths
     */
    List<List<Integer>> inferRowSequencesRangesFromArrays (List<String> arrayFilledFromStart, List<String> arrayFilledFromEnd) {
        List<String> collectedSequences = new ArrayList<>();
        List<List<Integer>> sequencesRanges = new ArrayList<>();
        int rangeStartIndex;
        int rangeLastIndex;

        for(int idx = 0; idx < arrayFilledFromStart.size(); idx++) {
            String field = arrayFilledFromStart.get(idx);
            String fieldSequenceChar = field.substring(1,2);
            if(field.indexOf("R") == 0 && !collectedSequences.contains(fieldSequenceChar)) {
                collectedSequences.add(fieldSequenceChar);
                rangeStartIndex = idx;
                rangeLastIndex = findLastIndexContaining(arrayFilledFromEnd, "R" + fieldSequenceChar);
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

        if(reverse) {
            sequences = reverseList(sequences);
            charsNeeded = reverseList(charsNeeded);
        }

        int width = nonogramSolutionBoard.get(0).size();
        List<String> arrayFilledFromStart = createArrayOfEmptyFields(width);

        boolean canStartSequenceFromIndex;// = false;
        boolean writeSequenceMode = false;
        int currentSequenceIdx = 0;
        int sequencesFieldsFilled = 0;
        String charToWrite = charsNeeded.get(currentSequenceIdx);
        int sequenceLength = sequences.get(currentSequenceIdx);
        boolean breakX = true;

        for(int fieldIdx = 0; fieldIdx < width; fieldIdx++ ) {

            if(!writeSequenceMode && currentSequenceIdx < charsNeeded.size() && breakX) {
                canStartSequenceFromIndex = checkIfCanStartSequenceFromRowIndex(rowIdx, fieldIdx, sequenceLength);
                if(canStartSequenceFromIndex) {
                    writeSequenceMode = true; // start fill fields with sequence char mark
                }
            }
            if(writeSequenceMode) {
                arrayFilledFromStart.set(fieldIdx, "R" + charToWrite + nonogramSolutionBoardWithMarks.get(rowIdx).get(fieldIdx).substring(2, 4));

                sequencesFieldsFilled++;

                if(sequencesFieldsFilled == sequenceLength) {
                    sequencesFieldsFilled = 0;
                    currentSequenceIdx++;
                    if(currentSequenceIdx < charsNeeded.size()) {
                        charToWrite = charsNeeded.get( currentSequenceIdx );
                        sequenceLength = sequences.get( currentSequenceIdx );
                    }
                    writeSequenceMode = false;
                    breakX = false;
                }
            } else {
                arrayFilledFromStart.set( fieldIdx, "X".repeat(4));
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

        Predicate<String> fieldWithX = field -> field.equals("X".repeat(4));

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

        if(reverse) {
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

            if(!writeSequenceMode && currentSequenceIdx < charsNeeded.size() && breakX) {
                canStartSequenceFromIndex = checkIfCanStartSequenceFromColumnIndex(columnIdx, fieldIdx, sequenceLength);
                if(canStartSequenceFromIndex) {
                    writeSequenceMode = true; // start fill fields with sequence char mark
                }
            }
            if(writeSequenceMode) {

                arrayFilledFromStart.set(fieldIdx, nonogramSolutionBoardWithMarks.get(fieldIdx).get(columnIdx).substring(0, 2) + "C" + charToWrite);

                sequencesFieldsFilled++;

                if(sequencesFieldsFilled == sequenceLength) {
                    sequencesFieldsFilled = 0;
                    currentSequenceIdx++;
                    if(currentSequenceIdx < charsNeeded.size()) {
                        charToWrite = charsNeeded.get( currentSequenceIdx );
                        sequenceLength = sequences.get( currentSequenceIdx );
                    }
                    writeSequenceMode = false;
                    breakX = false;
                }
            } else {
                arrayFilledFromStart.set(fieldIdx, "X".repeat(4));
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

        Predicate<String> fieldWithX = field -> field.equals("X".repeat(4));

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
     * @param columnIdx - column index from solution board to select
     * @return selected column
     */
    List<String> getSolutionBoardColumn(int columnIdx) {
        List<String> boardColumn = new ArrayList<>();

        for (List<String> strings : nonogramSolutionBoard) {
            boardColumn.add(strings.get(columnIdx));
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
            String fieldSequenceChar = field.substring(3,4);
            if(field.indexOf("C") == 2 && !collectedSequences.contains(fieldSequenceChar)) {
                collectedSequences.add(fieldSequenceChar);
                rangeStartIndex = idx;
                rangeLastIndex = findLastIndexContaining(arrayFilledFromEnd, "C" + fieldSequenceChar);
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
            if(boardRow.get(columnIdx).equals("O".repeat(4))) {
                boardRowSequenceRange = new ArrayList<>();
                boardRowSequenceRange.add(columnIdx);
                while(columnIdx < this.getWidth() && boardRow.get(columnIdx).equals("O".repeat(4))) {
                    columnIdx++;
                }
                boardRowSequenceRange.add(columnIdx - 1);
                boardRowSequencesRanges.add(boardRowSequenceRange);
                sequenceNo++;
            }
            if(sequenceNo == boardRowSequencesLengths.size()) {
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
        List<String> boardColumn = this.getSolutionBoardColumn(columnIdx);
        List<List<Integer>> boardColumnSequencesRanges = new ArrayList<>();
        List<Integer> boardColumnSequencesLengths = this.getColumnsSequences().get(columnIdx);
        List<Integer> boardColumnSequenceRange;
        int sequenceNo = 0;

        for(int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            if(boardColumn.get(rowIdx).equals("O".repeat(4))) {
                boardColumnSequenceRange = new ArrayList<>();
                boardColumnSequenceRange.add(rowIdx);
                while(rowIdx < this.getHeight() && boardColumn.get(rowIdx).equals("O".repeat(4))) {
                    rowIdx++;
                }
                boardColumnSequenceRange.add(rowIdx - 1);
                boardColumnSequencesRanges.add(boardColumnSequenceRange);
                sequenceNo++;
            }
            if(sequenceNo == boardColumnSequencesLengths.size()) {
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
            if(array.get(arrIdx).contains(matchingSubstring)) {
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

        for(int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            rowFieldsNotToInclude = this.getRowsFieldsNotToInclude().get(rowIdx);
            if(!(rowFieldsNotToInclude.size() == this.getWidth())) {
                for(int columnIdx = 0; columnIdx < this.getWidth(); columnIdx++) {
                    if(!nonogramSolutionBoard.get(rowIdx).get(columnIdx).equals("O") && !nonogramSolutionBoard.get(rowIdx).get(columnIdx).equals("X")) {
                        decision = new NonogramSolutionDecision("X", rowIdx, columnIdx);
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
        int rowIdx = decision.getRowIdx();
        int columnIdx = decision.getColumnIdx();

        this.actionsToDoList.add(new NonogramActionDefinition(rowIdx, ActionEnum.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS));
        this.actionsToDoList.add(new NonogramActionDefinition(columnIdx, ActionEnum.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS));
        this.actionsToDoList.add(new NonogramActionDefinition(rowIdx, ActionEnum.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW));
        this.actionsToDoList.add(new NonogramActionDefinition(columnIdx, ActionEnum.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN));
        this.actionsToDoList.add(new NonogramActionDefinition(columnIdx, ActionEnum.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES));
        this.actionsToDoList.add(new NonogramActionDefinition(columnIdx, ActionEnum.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES));
        this.actionsToDoList.add(new NonogramActionDefinition(rowIdx, ActionEnum.MARK_AVAILABLE_FIELDS_IN_ROW));
        this.actionsToDoList.add(new NonogramActionDefinition(columnIdx, ActionEnum.MARK_AVAILABLE_FIELDS_IN_COLUMN));
    }

    /**
     * @param decision - taken decision, pointing to the row and column of the field where "X" has been placed
     */
    public void addAffectedRowAndColumnAfterPlacingXAtField(NonogramSolutionDecision decision) {
        int rowIdx = decision.getRowIdx();
        int columnIdx = decision.getColumnIdx();

        this.actionsToDoList.add(new NonogramActionDefinition(rowIdx, ActionEnum.CORRECT_ROW_SEQUENCES_RANGES));
        this.actionsToDoList.add(new NonogramActionDefinition(columnIdx, ActionEnum.CORRECT_COLUMN_SEQUENCES_RANGES));
        this.actionsToDoList.add(new NonogramActionDefinition(rowIdx, CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY));
        this.actionsToDoList.add(new NonogramActionDefinition(columnIdx, ActionEnum.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY));
        this.actionsToDoList.add(new NonogramActionDefinition(rowIdx, ActionEnum.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW));
        this.actionsToDoList.add(new NonogramActionDefinition(columnIdx, ActionEnum.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN));
        this.actionsToDoList.add(new NonogramActionDefinition(rowIdx, ActionEnum.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES));
        this.actionsToDoList.add(new NonogramActionDefinition(columnIdx, ActionEnum.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES));
    }

    public void basicSolve() {
        int actionListIndex = 0;

        while(actionListIndex <= actionsToDoList.size()) {
            NonogramActionDefinition nextNonogramActionDefinition = actionsToDoList.get(actionListIndex);
            if(nextNonogramActionDefinition.getAction().ordinal() <= LAST_ROW_ACTION_ENUM_VALUE) {
                this.copyLogicToNonogramRowLogic();
                makeProperActionInRow(nextNonogramActionDefinition.getIndex(), nextNonogramActionDefinition.getAction());
                this.copyLogicFromNonogramRowLogic();
            } else {
                this.copyLogicToNonogramColumnLogic();
                makeProperActionInColumn(nextNonogramActionDefinition.getIndex(), nextNonogramActionDefinition.getAction());
                this.copyLogicFromNonogramColumnLogic();
            }
            actionListIndex++;

            if(this.guessMode && this.nonogramState.isInvalidSolution()) {
                break;
            }
        }
    }

    public void makeProperActionInRow(int rowIdx, ActionEnum actionToDoInRow) {
        switch (actionToDoInRow) {
            case CORRECT_ROW_SEQUENCES_RANGES -> nonogramRowLogic.correctRowSequencesRanges(rowIdx);
            case CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS -> nonogramRowLogic.correctRowSequencesRangesWhenMetColouredField(rowIdx);
            case CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY -> nonogramRowLogic.correctRowRangeIndexesIfXOnWay(rowIdx);
            case COLOUR_OVERLAPPING_FIELDS_IN_ROW -> nonogramRowLogic.fillOverlappingFieldsInRow(rowIdx);
            case EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW -> {
                nonogramRowLogic.extendColouredFieldsToLeftNearXToMaximumPossibleLengthInRow(rowIdx);
                nonogramRowLogic.extendColouredFieldsToRightNearXToMaximumPossibleLengthInRow(rowIdx);
            }
            case PLACE_XS_ROW_AT_UNREACHABLE_FIELDS -> nonogramRowLogic.placeXsRowAtUnreachableFields(rowIdx);
            case PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES -> nonogramRowLogic.placeXsRowAroundLongestSequences(rowIdx);
            case PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES ->
                    nonogramRowLogic.placeXsRowAtTooShortEmptySequences(rowIdx);
            case MARK_AVAILABLE_FIELDS_IN_ROW -> nonogramRowLogic.markAvailableSequencesInRow(rowIdx);
            default -> {
            }
        }
    }

    public void makeProperActionInColumn(int columnIdx, ActionEnum actionToDoInColumn) {
        switch (actionToDoInColumn) {
            case CORRECT_COLUMN_SEQUENCES_RANGES -> {
                nonogramColumnLogic.correctColumnSequencesRanges(columnIdx);
                if(guessMode) {
                    invalidateSolutionIfColumnSequencesWrong(columnIdx);
                }
            }
            case CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS -> nonogramColumnLogic.correctColumnSequencesWhenMetColouredField(columnIdx);
            case CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY -> nonogramColumnLogic.correctColumnRangeIndexesIfXOnWay(columnIdx);
            case COLOUR_OVERLAPPING_FIELDS_IN_COLUMN -> nonogramColumnLogic.fillOverlappingFieldsInColumn(columnIdx);
            case EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN -> {
                nonogramColumnLogic.extendColouredFieldsToTopNearXToMaximumPossibleLengthInColumn(columnIdx);
                nonogramColumnLogic.extendColouredFieldsToBottomNearXToMaximumPossibleLengthInColumn(columnIdx);
            }
            case PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS -> nonogramColumnLogic.placeXsColumnAtUnreachableFields(columnIdx);
            case PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES -> nonogramColumnLogic.placeXsColumnAroundLongestSequences(columnIdx);
            case PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES -> nonogramColumnLogic.placeXsColumnAtTooShortEmptySequences(columnIdx);
            case MARK_AVAILABLE_FIELDS_IN_COLUMN -> nonogramColumnLogic.markAvailableSequencesInColumn(columnIdx);
            default -> {
            }
        }
    }

    private void invalidateSolutionIfColumnSequencesWrong(int columnIndex) {
        List<Integer> sequencesLengths = this.nonogramColumnLogic.getColumnsSequences().get(columnIndex);
        List<List<Integer>> columnSequencesRanges = this.nonogramColumnLogic.getColumnsSequencesRanges().get(columnIndex);

        if(!nonogramColumnLogic.emptyColumn(columnIndex)) {
            for(int seqNo = 0; seqNo < columnSequencesRanges.size(); seqNo++) {
                if(rangeLength(columnSequencesRanges.get(seqNo)) < sequencesLengths.get(seqNo)) {
                    this.nonogramColumnLogic.getNonogramState().setInvalidSolution(true);
                    break;
                }
            }
        }
    }

    public void copyLogicFromNonogramColumnLogic() {
        this.logs = this.nonogramColumnLogic.getLogs();
        this.nonogramSolutionBoardWithMarks = this.nonogramColumnLogic.getNonogramSolutionBoardWithMarks();
        this.nonogramSolutionBoard = this.nonogramColumnLogic.getNonogramSolutionBoard();

        this.rowsSequencesRanges = this.nonogramColumnLogic.getRowsSequencesRanges();
        this.columnsSequencesRanges = this.nonogramColumnLogic.getColumnsSequencesRanges();

        this.rowsFieldsNotToInclude = this.nonogramColumnLogic.getRowsFieldsNotToInclude();
        this.columnsFieldsNotToInclude = this.nonogramColumnLogic.getColumnsFieldsNotToInclude();
        this.rowsSequencesIdsNotToInclude = this.nonogramColumnLogic.getRowsSequencesIdsNotToInclude();
        this.columnsSequencesIdsNotToInclude = this.nonogramColumnLogic.getColumnsSequencesIdsNotToInclude();

        this.actionsToDoList = this.nonogramColumnLogic.getActionsToDoList();

        this.setNonogramState(this.nonogramColumnLogic.getNonogramState());
    }

    public void copyLogicToNonogramColumnLogic() {
        this.nonogramColumnLogic.setLogs(this.getLogs());
        this.nonogramColumnLogic.setNonogramSolutionBoardWithMarks(this.getNonogramSolutionBoardWithMarks());
        this.nonogramColumnLogic.setNonogramSolutionBoard(this.getNonogramSolutionBoard());

        this.nonogramColumnLogic.setRowsSequencesRanges(this.getRowsSequencesRanges());
        this.nonogramColumnLogic.setColumnsSequencesRanges(this.getColumnsSequencesRanges());

        this.nonogramColumnLogic.setRowsFieldsNotToInclude(this.getRowsFieldsNotToInclude());
        this.nonogramColumnLogic.setColumnsFieldsNotToInclude(this.getColumnsFieldsNotToInclude());
        this.nonogramColumnLogic.setRowsSequencesIdsNotToInclude(this.getRowsSequencesIdsNotToInclude());
        this.nonogramColumnLogic.setColumnsSequencesIdsNotToInclude(this.getColumnsSequencesIdsNotToInclude());

        this.nonogramColumnLogic.setActionsToDoList(this.getActionsToDoList());

        this.nonogramColumnLogic.setNonogramState(this.getNonogramState());
    }

    public void copyLogicFromNonogramRowLogic() {
        this.logs = this.nonogramRowLogic.getLogs();

        this.nonogramSolutionBoardWithMarks = this.nonogramRowLogic.getNonogramSolutionBoardWithMarks();
        this.nonogramSolutionBoard = this.nonogramRowLogic.getNonogramSolutionBoard();

        this.rowsSequencesRanges = this.nonogramRowLogic.getRowsSequencesRanges();
        this.columnsSequencesRanges = this.nonogramRowLogic.getColumnsSequencesRanges();

        this.rowsFieldsNotToInclude = this.nonogramRowLogic.getRowsFieldsNotToInclude();
        this.columnsFieldsNotToInclude = this.nonogramRowLogic.getColumnsFieldsNotToInclude();
        this.rowsSequencesIdsNotToInclude = this.nonogramRowLogic.getRowsSequencesIdsNotToInclude();
        this.columnsSequencesIdsNotToInclude = this.nonogramRowLogic.getColumnsSequencesIdsNotToInclude();

        this.actionsToDoList = this.nonogramRowLogic.getActionsToDoList();

        this.nonogramState = this.nonogramRowLogic.getNonogramState();
    }

    public void copyLogicToNonogramRowLogic() {
        this.nonogramRowLogic.setLogs(this.logs);

        this.nonogramRowLogic.setNonogramSolutionBoardWithMarks(this.nonogramSolutionBoardWithMarks);
        this.nonogramRowLogic.setNonogramSolutionBoard(this.nonogramSolutionBoard);

        this.nonogramRowLogic.setRowsSequencesRanges(this.rowsSequencesRanges);
        this.nonogramRowLogic.setColumnsSequencesRanges(this.columnsSequencesRanges);

        this.nonogramRowLogic.setRowsFieldsNotToInclude(this.rowsFieldsNotToInclude);
        this.nonogramRowLogic.setColumnsFieldsNotToInclude(this.columnsFieldsNotToInclude);
        this.nonogramRowLogic.setRowsSequencesIdsNotToInclude(this.rowsSequencesIdsNotToInclude);
        this.nonogramRowLogic.setColumnsSequencesIdsNotToInclude(this.columnsSequencesIdsNotToInclude);

        this.nonogramRowLogic.setActionsToDoList(this.getActionsToDoList());

        this.nonogramRowLogic.setNonogramState(this.nonogramState);
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
                if(subsolutionNonogramBoardField.equals("O")) {
                    if(!subsolutionNonogramBoardField.equals(solutionNonogramBoardField)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * @param rowIdx - field row index
     * @param columnIdx - field column index
     * @return NonogramLogic object with "X" placed on specified position
     */
    public NonogramLogic placeXAtGivenPosition(int rowIdx, int columnIdx) {
        boolean rowValid = isRowIndexValid(rowIdx);
        boolean columnValid = isColumnIndexValid(columnIdx);
        if(rowValid && columnValid) {
            this.nonogramSolutionBoard.get(rowIdx).set(columnIdx, "X");
            this.nonogramSolutionBoardWithMarks.get(rowIdx).set(columnIdx, "X".repeat(4));
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
        } else if(isNonogram2DSymmetrical()) {
            return "2 axis";
        } else if(isNonogram1DSymmetrical()) {
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

    public void printSolutionBoard() {
        for (List<String> solutionBoardRow : this.getNonogramSolutionBoard()) {
            System.out.println(solutionBoardRow);
        }
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

    private void addRowToAffectedActionsByIdentifiers(int rowIdx, List<ActionEnum> actions) {
        for(ActionEnum action : actions) {
            this.actionsToDoList.add(new NonogramActionDefinition(rowIdx, action));
        }
    }

    private void addColumnToAffectedActionsByIdentifiers(int columnIdx, List<ActionEnum> actions) {
        for(ActionEnum action : actions) {
            this.actionsToDoList.add(new NonogramActionDefinition(columnIdx, action));
        }
    }

    public void addLog(String log) {

        //String situationOccuredMarker = situationDetectionCondition()  ? "+" : "-";
        if (log.isEmpty()) {
            System.out.println("Trying to add empty log!!!");
        } else {
            this.logs.add(log/* + situationOccuredMarker*/);
        }
    }
}
