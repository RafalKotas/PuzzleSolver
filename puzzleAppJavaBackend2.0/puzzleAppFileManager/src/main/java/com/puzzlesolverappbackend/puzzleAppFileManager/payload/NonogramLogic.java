package com.puzzlesolverappbackend.puzzleAppFileManager.payload;

import com.puzzlesolverappbackend.puzzleAppFileManager.NonogramSolutionDecision;
import com.puzzlesolverappbackend.puzzleAppFileManager.templates.nonogram.NonogramBoardTemplate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzleAppFileManager.services.NonogramLogicService.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NonogramLogic {

    boolean showRepetitions = false;

    private List<String> logs;

    private String tmpLog;
    private NonogramColumnLogic nonogramColumnLogic;
    private NonogramRowLogic nonogramRowLogic;

    private List<List<Integer>> rowsSequences;
    private List<List<Integer>> columnsSequences;
    private List<List<String>> nonogramSolutionBoardWithMarks;
    private List<List<String>> nonogramSolutionBoard;
    private List<List<List<Integer>>> rowsSequencesRanges;
    private List<List<List<Integer>>> columnsSequencesRanges;

    private List<List<Integer>> rowsFieldsNotToInclude;
    private List<List<Integer>> columnsFieldsNotToInclude;
    private List<List<Integer>> rowsSequencesIdsNotToInclude;
    private List<List<Integer>> columnsSequencesIdsNotToInclude;

    private List<NonogramSolutionDecision> availableChoices;

    //1
    private Set<Integer> affectedRowsToFillOverlappingFields;
    //2
    private Set<Integer> affectedColumnsToMarkAvailableSequences;
    //3
    private Set<Integer> affectedColumnsToFillOverlappingFields;
    //4
    private Set<Integer> affectedRowsToMarkAvailableSequences;
    //5
    private Set<Integer> affectedRowsToCorrectSequencesRanges;
    //6
    private Set<Integer> affectedRowsToCorrectSequencesRangesWhenMetColouredField;
    //7
    private Set<Integer> affectedRowsToChangeSequencesRangeIfXOnWay;
    //8
    private Set<Integer> affectedColumnsToCorrectSequencesRanges;
    //9
    private Set<Integer> affectedColumnsToCorrectSequencesRangesWhenMetColouredField;
    //10
    private Set<Integer> affectedColumnsToCorrectSequencesRangesIfXOnWay;
    //11
    private Set<Integer> affectedRowsToPlaceXsAtUnreachableFields;
    //12
    private Set<Integer> affectedColumnsToPlaceXsAtUnreachableFields;
    //13
    private Set<Integer> affectedRowsToPlaceXsAroundLongestSequences;
    //14
    private Set<Integer> affectedColumnsToPlaceXsAroundLongestSequences;
    //15
    private Set<Integer> affectedRowsToPlaceXsAtTooShortEmptySequences;
    //16
    private Set<Integer> affectedColumnsToPlaceXsAtTooShortEmptySequences;
    //17
    private Set<Integer> affectedRowsToExtendColouredFieldsNearX;
    //18
    private Set<Integer> affectedColumnsToExtendColouredFieldsNearX;

    private int newStepsMade;

    private boolean solutionInvalid;

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
                ", affectedRowsToFillOverlappingFields=" + affectedRowsToFillOverlappingFields + "\n" +
                ", affectedColumnsToMarkAvailableSequences=" + affectedColumnsToMarkAvailableSequences + "\n" +
                ", affectedColumnsToColourOverlappingFields=" + affectedColumnsToFillOverlappingFields + "\n" +
                ", affectedRowsToMarkAvailableSequences=" + affectedRowsToMarkAvailableSequences + "\n" +
                ", affectedRowsToCorrectSequencesRanges=" + affectedRowsToCorrectSequencesRanges + "\n" +
                ", affectedColumnsToCorrectSequencesRanges=" + affectedColumnsToCorrectSequencesRanges + "\n" +
                ", affectedRowsToPlaceXsAtUnreachableFields=" + affectedRowsToPlaceXsAtUnreachableFields + "\n" +
                ", affectedColumnsToPlaceXsAtUnreachableFields=" + affectedColumnsToPlaceXsAtUnreachableFields + "\n" +
                ", affectedRowsToPlaceXsAroundLongestSequences=" + affectedRowsToPlaceXsAroundLongestSequences + "\n" +
                ", affectedColumnsToPlaceXsAroundLongestSequences=" + affectedColumnsToPlaceXsAroundLongestSequences + "\n" +
                ", affectedRowsToPlaceXsAtTooShortEmptySequences=" + affectedRowsToPlaceXsAtTooShortEmptySequences + "\n" +
                ", affectedColumnsToPlaceXsAtTooShortEmptySequences=" + affectedColumnsToPlaceXsAtTooShortEmptySequences + "\n" +
                ", affectedRowsToExtendColouredFieldsNearX=" + affectedRowsToExtendColouredFieldsNearX + "\n" +
                ", affectedColumnsToExtendColouredFieldsNearX=" + affectedColumnsToExtendColouredFieldsNearX + "\n" +
                ", newStepsMade=" + newStepsMade + "\n" +
                '}';
    }

    public NonogramLogic(List<List<Integer>> rowsSequences, List<List<Integer>> columnsSequences) {
        this.logs = new ArrayList<>();

        this.rowsSequences = rowsSequences;
        this.columnsSequences = columnsSequences;

        int height = this.getHeight();
        int width = this.getWidth();

        //1
        this.affectedRowsToFillOverlappingFields = IntStream.range(0, height)
                .boxed()
                .collect(Collectors.toSet());
        //2
        this.affectedColumnsToMarkAvailableSequences = new HashSet<>();
        //3
        this.affectedColumnsToFillOverlappingFields = IntStream.range(0, width)
                .boxed()
                .collect(Collectors.toSet());
        //4
        this.affectedRowsToMarkAvailableSequences = new HashSet<>();
        //5
        this.affectedRowsToCorrectSequencesRanges = new HashSet<>();
        //6
        this.affectedRowsToCorrectSequencesRangesWhenMetColouredField = new HashSet<>();
        //7
        this.affectedRowsToChangeSequencesRangeIfXOnWay = new HashSet<>();
        //8
        this.affectedColumnsToCorrectSequencesRanges = new HashSet<>();
        //9
        this.affectedColumnsToCorrectSequencesRangesWhenMetColouredField = new HashSet<>();
        //10
        this.affectedColumnsToCorrectSequencesRangesIfXOnWay = new HashSet<>();
        //11
        this.affectedRowsToPlaceXsAtUnreachableFields = new HashSet<>();
        //12
        this.affectedColumnsToPlaceXsAtUnreachableFields = new HashSet<>();
        //13
        this.affectedRowsToPlaceXsAroundLongestSequences = new HashSet<>();
        //14
        this.affectedColumnsToPlaceXsAroundLongestSequences = new HashSet<>();
        //15
        this.affectedRowsToPlaceXsAtTooShortEmptySequences = new HashSet<>();
        //16
        this.affectedColumnsToPlaceXsAtTooShortEmptySequences = new HashSet<>();
        //17
        this.affectedRowsToExtendColouredFieldsNearX = new HashSet<>();
        //18
        this.affectedColumnsToExtendColouredFieldsNearX = new HashSet<>();

        this.nonogramSolutionBoard = generateEmptyBoard(height, width, 1);
        this.nonogramSolutionBoardWithMarks = generateEmptyBoard(height, width, 4);

        this.rowsFieldsNotToInclude = generateEmptyRowsNotToInclude();
        this.columnsFieldsNotToInclude = generateEmptyColumnsNotToInclude();

        this.rowsSequencesRanges = inferInitialRowsSequencesRanges();
        this.columnsSequencesRanges = inferInitialColumnsSequencesRanges();

        this.rowsSequencesIdsNotToInclude = generateEmptyRowsNotToInclude();
        this.columnsSequencesIdsNotToInclude = generateEmptyColumnsNotToInclude();

        this.newStepsMade = 0;

        this.nonogramColumnLogic = new NonogramColumnLogic(this);
        this.nonogramRowLogic = new NonogramRowLogic(this);
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

        //ok (all/none)
        this.rowsSequencesIdsNotToInclude = generateEmptyRowsNotToInclude();
        //ok (all/none)
        this.columnsSequencesIdsNotToInclude = generateEmptyColumnsNotToInclude();
    }

    public NonogramLogic(NonogramLogic that) {
        this(that.getRowsSequences(), that.getColumnsSequences(),
                that.getRowsSequencesRanges(), that.getColumnsSequencesRanges(),
                that.getRowsFieldsNotToInclude(), that.getColumnsFieldsNotToInclude(),
                that.getRowsSequencesIdsNotToInclude(), that.getColumnsFieldsNotToInclude(),
                that.getNonogramSolutionBoard(), that.getNonogramSolutionBoardWithMarks(),
                that.isSolutionInvalid());
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

        this.affectedRowsToFillOverlappingFields = new HashSet<>();
        this.affectedColumnsToMarkAvailableSequences = new HashSet<>();
        this.affectedColumnsToFillOverlappingFields = new HashSet<>();
        this.affectedRowsToMarkAvailableSequences = new HashSet<>();
        this.affectedRowsToCorrectSequencesRanges = new HashSet<>();
        this.affectedRowsToCorrectSequencesRangesWhenMetColouredField = new HashSet<>();
        this.affectedRowsToChangeSequencesRangeIfXOnWay = new HashSet<>();
        this.affectedColumnsToCorrectSequencesRanges = new HashSet<>();
        this.affectedColumnsToCorrectSequencesRangesWhenMetColouredField = new HashSet<>();
        this.affectedColumnsToCorrectSequencesRangesIfXOnWay = new HashSet<>();
        this.affectedRowsToPlaceXsAtUnreachableFields = new HashSet<>();
        this.affectedColumnsToPlaceXsAtUnreachableFields = new HashSet<>();
        this.affectedRowsToPlaceXsAroundLongestSequences = new HashSet<>();
        this.affectedColumnsToPlaceXsAroundLongestSequences = new HashSet<>();
        this.affectedRowsToPlaceXsAtTooShortEmptySequences = new HashSet<>();
        this.affectedColumnsToPlaceXsAtTooShortEmptySequences = new HashSet<>();
        this.affectedRowsToExtendColouredFieldsNearX = new HashSet<>();
        this.affectedColumnsToExtendColouredFieldsNearX = new HashSet<>();

        this.setSolutionInvalid(solutionInvalid);

        this.newStepsMade = 0;
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
        int height = this.getHeight();
        List<List<Integer>> emptyRowsFieldsNotToInclude = new ArrayList<>(height);
        List<Integer> emptyRowFieldsNotToInclude;
        for(int rowIdx = 0; rowIdx < height; rowIdx++) {
            emptyRowFieldsNotToInclude = new ArrayList<>();
            emptyRowsFieldsNotToInclude.add(emptyRowFieldsNotToInclude);
        }
        return emptyRowsFieldsNotToInclude;
    }

    /**
     * @return empty list of empty columns not to include when running solver
     */
    private List<List<Integer>> generateEmptyColumnsNotToInclude() {
        int width = this.getWidth();
        List<List<Integer>> emptyColumnsFieldsNotToInclude = new ArrayList<>(width);
        List<Integer> emptyColumnFieldsNotToInclude;
        for(int rowIdx = 0; rowIdx < width; rowIdx++) {
            emptyColumnFieldsNotToInclude = new ArrayList<>();
            emptyColumnsFieldsNotToInclude.add(emptyColumnFieldsNotToInclude);
        }
        return emptyColumnsFieldsNotToInclude;
    }

    /**
     * prints nonogramSolutionBoard(only "X"/"O"/"-") in readable format
     */
    public void printNonogramBoard() {
        for(List<String> boardRow : this.getNonogramSolutionBoard()) {
            System.out.println(boardRow);
        }
    }

    /**
     * prints nonogramSolutionBoardWithMarks(fields like "RxCy"/"Rx--"/"--Cx"/"----") in readable format
     */
    public void printNonogramBoardWithMarks() {
        for(List<String> boardRow : this.getNonogramSolutionBoardWithMarks()) {
            System.out.println(boardRow);
        }
    }

    /**
     * prints current rows sequences ranges line by line
     */
    public void printRowsSequencesRanges() {
        int rowIdx = 0;
        for(List<List<Integer>> rowSequencesRanges : this.getRowsSequencesRanges()) {
            System.out.println(rowIdx + " " + rowSequencesRanges);
            rowIdx++;
        }
    }

    /**
     * prints current columns sequences ranges line by line
     */
    public void printColumnsSequencesRanges() {
        int colIdx = 0;
        for(List<List<Integer>> colSequencesRanges : this.getColumnsSequencesRanges()) {
            System.out.println(colIdx + " " + colSequencesRanges);
            colIdx++;
        }
    }

    /**
     * print solver logs line by line
     */
    public void printLogs() {
        for(String log : this.logs) {
            System.out.println(log);
        }
    }


    /**
     * @param rowIdx - field row index
     * @param columnIdx - field column index
     * @return NonogramLogic object with "X" placed on specified position
     */
    public NonogramLogic placeXatGivenPosition(int rowIdx, int columnIdx) {
        boolean rowValid = isRowIndexValid(rowIdx);
        boolean columnValid = isColumnIndexValid(columnIdx);
        if(rowValid && columnValid) {
            this.nonogramSolutionBoard.get(rowIdx).set(columnIdx, "X");
            this.nonogramSolutionBoardWithMarks.get(rowIdx).set(columnIdx, "XXXX");
        }

        return this;
    }

    /**
     * @param rowIdx - field row index
     * @param columnIdx - field column index
     * @return NonogramLogic object with "O" placed on specified position (field coloured)
     */
    public NonogramLogic colourFieldAtGivenPosittion(int rowIdx, int columnIdx) {
        boolean rowValid = isRowIndexValid(rowIdx);
        boolean columnValid = isColumnIndexValid(columnIdx);
        if(rowValid && columnValid) {
            this.nonogramSolutionBoard.get(rowIdx).set(columnIdx, "O");
        }

        return this;
    }

    /**
     * @param rowIdx - row index to validate if is in range <0, height)
     * @return true if row index is valid(in range) or false if not
     */
    private boolean isRowIndexValid (int rowIdx) {
        return rowIdx >= 0 && rowIdx < this.getHeight();
    }

    /**
     * @param columnIdx - column index to validate if is in range <0, width)
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
        if(rowValid && columnValid && !this.columnsFieldsNotToInclude.get(columnIdx).contains(rowIdx) && rowIdx < this.getHeight() && rowIdx >= 0) {
            this.columnsFieldsNotToInclude.get(columnIdx).add(rowIdx);
            Collections.sort(this.columnsFieldsNotToInclude.get(columnIdx));
        }

        return this;
    }

    /**
     * @param columnIdx - column index to change one of its sequences range
     * @param sequenceIdx - sequence index to change range
     * @param updatedRange - column sequence updatedRange
     * @return
     */
    public NonogramLogic changeColumnSequenceRange(int columnIdx, int sequenceIdx, List<Integer> updatedRange) {
        this.columnsSequencesRanges.get(columnIdx).set(sequenceIdx, updatedRange);
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


    /**
     * @return nonogram width based on columnsSequences size
     */
    public int getWidth() {
        return this.columnsSequences.size();
    }

    /**
     * @return nonogram width based on rowSequences size
     */
    public int getHeight() {
        return this.rowsSequences.size();
    }

    private List<List<List<Integer>>> inferInitialRowsSequencesRanges() {
        List<List<List<Integer>>> initialRowSequencesRanges = new ArrayList<>();

        for(int rowIdx = 0; rowIdx < rowsSequences.size(); rowIdx ++) {
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
     * @param arrayFilledFromStart - array filled with sequences from earliest possible field
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
     * @param reverse
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
                arrayFilledFromStart.set( fieldIdx, "XXXX");
                breakX = true;
            }
        }

        return arrayFilledFromStart;
    }

    /**
     * @param arrLength - length(size) of created array
     * @return array of <arrLength> elements filled with "----"(empty)
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

        Predicate<String> fieldWithX = field -> field.equals("XXXX");

        List<String> xs = fieldsToCheck.stream().filter(fieldWithX).collect(Collectors.toList());

        return xs.size() == 0;
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
     * @param reverse
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
                arrayFilledFromStart.set(fieldIdx, "XXXX");
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

        Predicate<String> fieldWithX = field -> field.equals("XXXX");

        List<String> xs = fieldsToCheck.stream().filter(fieldWithX).collect(Collectors.toList());

        return xs.size() == 0;
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
            if(boardRow.get(columnIdx).equals("OOOO")) {
                boardRowSequenceRange = new ArrayList<>();
                boardRowSequenceRange.add(columnIdx);
                while(columnIdx < this.getWidth() && boardRow.get(columnIdx).equals("OOOO")) {
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
            if(boardColumn.get(rowIdx).equals("OOOO")) {
                boardColumnSequenceRange = new ArrayList<>();
                boardColumnSequenceRange.add(rowIdx);
                while(rowIdx < this.getHeight() && boardColumn.get(rowIdx).equals("OOOO")) {
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
     * @return array of marks created from given marksNo (1 -> ["a"], 5 -> ["a", "b", "c", "d", "e"], etc...)
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
     * @return NonogramLogic after increasing steps made
     */
    public NonogramLogic increaseStepsMade() {
        this.newStepsMade = this.newStepsMade + 1;
        return this;
    }

    /**
     * @return "X" count on nonogram solution board
     */
    public int fieldsWithXPlaced() {
        int fieldsWithXOnBoard = 0;
        for(int rowIndex = 0; rowIndex < this.getHeight(); rowIndex++) {
            for(int columnIndex = 0; columnIndex < this.getWidth(); columnIndex++) {
                if(this.getNonogramSolutionBoard().get(rowIndex).get(columnIndex).equals("X")) {
                    fieldsWithXOnBoard++;
                }
            }
        }
        return fieldsWithXOnBoard;
    }

    /**
     * @return "O" count on nonogram solution board
     */
    public int fieldsColoured() {
        int colouredFieldsOnBoard = 0;
        for(int rowIndex = 0; rowIndex < this.getHeight(); rowIndex++) {
            for(int columnIndex = 0; columnIndex < this.getWidth(); columnIndex++) {
                if(this.getNonogramSolutionBoard().get(rowIndex).get(columnIndex).equals("O")) {
                    colouredFieldsOnBoard++;
                }
            }
        }
        return colouredFieldsOnBoard;
    }

    /**
     * @return "X" and "O" count(sum of occurrences) on nonogram solution board
     */
    public int fieldsFilled() {
        return this.fieldsWithXPlaced() + this.fieldsColoured();
    }

    /**
     * @return fields that need to be proper coloured to solve nonogram
     */
    public int fieldsToColourTotal() {
        int fieldsToColourOnBoard = 0;
        int fieldsToColourInRow;

        for(int rowIndex = 0; rowIndex < this.getHeight(); rowIndex++) {
            fieldsToColourInRow = this.getRowsSequences()
                    .get(rowIndex)
                    .stream()
                    .reduce(0, Integer::sum);
            fieldsToColourOnBoard += fieldsToColourInRow;
        }

        return fieldsToColourOnBoard;
    }


    /**
     * @return fields to proper place "X" to solve nonogram
     */
    public int fieldsToPlaceXTotal() {
        return this.area() - fieldsToColourTotal();
    }

    /**
     * @return percent of "X" placed from all required
     */
    public double fieldsWithXPlacedPercent() {
        return getPercent(this.fieldsWithXPlaced(), this.fieldsToPlaceXTotal());
    }

    /**
     * @return percent of "O" placed (fields coloured) from all required
     */
    public double fieldsColouredPercent() {
        return getPercent(this.fieldsColoured(), this.fieldsToColourTotal());
    }

    /**
     * @return completionPercentage of nonogram in given nonogramLogic node
     */
    public double getCompletionPercentage() {
        int xPlaced = this.fieldsWithXPlaced();
        int coloured = this.fieldsColoured();

        return getPercent(xPlaced + coloured, this.area());
    }


    /**
     * @param part - part of whole thing
     * @param whole - total amount
     * @return percentage of the total
     */
    public double getPercent(int part, int whole) {
        return Math.round(((double)(part) / whole) * 10000 ) / 100.0;
    }

    /**
     * function meant to print stats after using only heuristics to solve nonogram
     */
    public void printStatsAfterBasicActionsMade() {
        //printNonogramBoard();
        int fieldsXPlaced = this.fieldsWithXPlaced();
        int fieldsColoured = this.fieldsColoured();
        int fieldsXTotal  = this.fieldsToPlaceXTotal();
        this.printNonogramBoard();
        System.out.println(String.format("%-12s %-12s %-12s | %-12s %-12s %-12s",
                "'X' placed", "'X' total", "'X' percent",
                "'O' placed", "'O' total", "'O' percent"));
        System.out.println(String.format("%-12s %-12s %-12s | %-12s %-12s %-12s",
                fieldsXPlaced, fieldsXTotal, this.fieldsWithXPlacedPercent(),
                fieldsColoured, fieldsToColourTotal(), this.fieldsColouredPercent()));
        System.out.println("Overall completion percentage: " + this.getCompletionPercentage() + "%");
        System.out.println("newStepsMade: " + this.newStepsMade);
        System.out.println("possible fields to make decision: " + this.availableChoices);
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
            if(rowFieldsNotToInclude.size() == this.getWidth()) {
                //System.out.println("rowIdx: " + rowIdx + " fullfilled.");
            } else {
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
        //2
        this.affectedColumnsToMarkAvailableSequences = new HashSet<>();
        this.affectedColumnsToMarkAvailableSequences.add(columnIdx);
        //4
        this.affectedRowsToMarkAvailableSequences = new HashSet<>();
        this.affectedRowsToMarkAvailableSequences.add(rowIdx);
        //6
        this.affectedRowsToCorrectSequencesRangesWhenMetColouredField = new HashSet<>();
        this.affectedRowsToCorrectSequencesRangesWhenMetColouredField.add(rowIdx);
        //9
        this.affectedColumnsToCorrectSequencesRangesWhenMetColouredField = new HashSet<>();
        this.affectedColumnsToCorrectSequencesRangesWhenMetColouredField.add(columnIdx);
        //14
        this.affectedColumnsToPlaceXsAroundLongestSequences = new HashSet<>();
        this.affectedColumnsToPlaceXsAroundLongestSequences.add(columnIdx);
        //17
        this.affectedRowsToExtendColouredFieldsNearX = new HashSet<>();
        this.affectedRowsToExtendColouredFieldsNearX.add(rowIdx);
        //18
        this.affectedColumnsToExtendColouredFieldsNearX = new HashSet<>();
        this.affectedColumnsToExtendColouredFieldsNearX.add(columnIdx);
    }

    /**
     * @param decision - taken decision, pointing to the row and column of the field where "X" has been placed
     */
    public void addAffectedRowAndColumnAfterPlacingXAtField(NonogramSolutionDecision decision) {
        int rowIdx = decision.getRowIdx();
        int columnIdx = decision.getColumnIdx();

        //5
        this.affectedRowsToCorrectSequencesRanges = new HashSet<>();
        this.affectedRowsToCorrectSequencesRanges.add(rowIdx);
        //7
        this.affectedRowsToChangeSequencesRangeIfXOnWay = new HashSet<>();
        this.affectedRowsToChangeSequencesRangeIfXOnWay.add(rowIdx);
        //8
        this.affectedColumnsToCorrectSequencesRanges = new HashSet<>();
        this.affectedColumnsToCorrectSequencesRanges.add(columnIdx);
        //10
        this.affectedColumnsToCorrectSequencesRangesIfXOnWay = new HashSet<>();
        this.affectedColumnsToCorrectSequencesRangesIfXOnWay.add(columnIdx);
        //15
        this.affectedRowsToPlaceXsAtTooShortEmptySequences = new HashSet<>();
        this.affectedRowsToPlaceXsAtTooShortEmptySequences.add(rowIdx);
        //16
        this.affectedColumnsToPlaceXsAtTooShortEmptySequences = new HashSet<>();
        this.affectedColumnsToPlaceXsAtTooShortEmptySequences.add(columnIdx);
        //17
        this.affectedRowsToExtendColouredFieldsNearX = new HashSet<>();
        this.affectedRowsToExtendColouredFieldsNearX.add(rowIdx);
        //18
        this.affectedColumnsToExtendColouredFieldsNearX = new HashSet<>();
        this.affectedColumnsToExtendColouredFieldsNearX.add(columnIdx);
    }


    /**
     * prints affected rows and columns at current time
     */
    public void printAffectedRowsAndColumns() {
        //1
        int affectedRowsToColourOverlappingFieldsCount = affectedRowsToFillOverlappingFields.size();
        //2
        int affectedColumnsToMarkAvailableSequencesCount = affectedColumnsToMarkAvailableSequences.size();
        //3
        int affectedColumnsToColourOverlappingFieldsCount = affectedColumnsToFillOverlappingFields.size();
        //4
        int affectedRowsToMarkAvailableSequencesCount = affectedRowsToMarkAvailableSequences.size();
        //5
        int affectedRowsToCorrectSequencesRangesCount = affectedRowsToCorrectSequencesRanges.size();
        //6
        int affectedRowsToCorrectSequencesWhenMetColouredFieldCount = affectedRowsToCorrectSequencesRangesWhenMetColouredField.size();
        //7
        int affectedRowsToCorrectSequencesIfXOnWayCount = affectedRowsToChangeSequencesRangeIfXOnWay.size();
        //8
        int affectedColumnsToCorrectSequencesRangesCount = affectedColumnsToCorrectSequencesRanges.size();
        //9
        int affectedColumnsToCorrectSequencesWhenMetColouredFieldCount = affectedColumnsToCorrectSequencesRangesWhenMetColouredField.size();
        //10
        int affectedColumnsToCorrectSequencesIfXOnWayCount = affectedColumnsToCorrectSequencesRangesIfXOnWay.size();
        //11
        int affectedRowsToPlaceXsAtUnreachableFieldsCount = affectedRowsToPlaceXsAtUnreachableFields.size();
        //12
        int affectedColumnsToPlaceXsAtUnreachableFieldsCount = affectedColumnsToPlaceXsAtUnreachableFields.size();
        //13
        int affectedRowsToPlaceXsAroundLongestSequencesCount = affectedRowsToPlaceXsAroundLongestSequences.size();
        //14
        int affectedColumnsToPlaceXsAroundLongestSequencesCount = affectedColumnsToPlaceXsAroundLongestSequences.size();
        //15
        int affectedRowsToPlaceXsAtTooShortEmptySequencesCount = affectedRowsToPlaceXsAtTooShortEmptySequences.size();
        //16
        int affectedColumnsToPlaceXsAtTooShortEmptySequencesCount = affectedColumnsToPlaceXsAtTooShortEmptySequences.size();
        //17
        int affectedRowsToExtendColouredFieldsNearXCount = affectedRowsToExtendColouredFieldsNearX.size();
        //18
        int affectedColumnsToExtendColouredFieldsNearXCount = affectedColumnsToExtendColouredFieldsNearX.size();

        int sumOfAffectedRows = affectedRowsToColourOverlappingFieldsCount + affectedRowsToMarkAvailableSequencesCount +
                affectedRowsToCorrectSequencesRangesCount + affectedRowsToCorrectSequencesWhenMetColouredFieldCount +
                affectedRowsToCorrectSequencesIfXOnWayCount + affectedRowsToPlaceXsAtUnreachableFieldsCount +
                affectedRowsToPlaceXsAroundLongestSequencesCount + affectedRowsToPlaceXsAtTooShortEmptySequencesCount
                + affectedRowsToExtendColouredFieldsNearXCount;

        int sumOfAffectedColumns = affectedColumnsToColourOverlappingFieldsCount + affectedColumnsToMarkAvailableSequencesCount +
                affectedColumnsToCorrectSequencesRangesCount + affectedColumnsToCorrectSequencesWhenMetColouredFieldCount +
                affectedColumnsToCorrectSequencesIfXOnWayCount + affectedColumnsToPlaceXsAtUnreachableFieldsCount +
                affectedColumnsToPlaceXsAroundLongestSequencesCount + affectedColumnsToPlaceXsAtTooShortEmptySequencesCount
                + affectedColumnsToExtendColouredFieldsNearXCount;


        boolean printCondition = true;

        if(printCondition) {
            System.out.println("-".repeat(50));
            System.out.println("Rows affected: ");
            System.out.println("affectedRowsToColourOverlappingFields=" + affectedRowsToFillOverlappingFields);
            System.out.println("affectedRowsToMarkAvailableSequences=" + affectedRowsToMarkAvailableSequences);
            System.out.println("affectedRowsToCorrectSequencesRanges=" + affectedRowsToCorrectSequencesRanges);
            System.out.println("affectedRowsToCorrectSequencesRangesWhenMetColouredField=" + affectedRowsToCorrectSequencesRangesWhenMetColouredField);
            System.out.println("affectedRowsToChangeSequencesRangeIfXOnWay=" + affectedRowsToChangeSequencesRangeIfXOnWay);
            System.out.println("affectedRowsToPlaceXsAtUnreachableFields=" + affectedRowsToPlaceXsAtUnreachableFields);
            System.out.println("affectedRowsToPlaceXsAroundLongestSequences=" + affectedRowsToPlaceXsAroundLongestSequences);
            System.out.println("affectedRowsToPlaceXsAtTooShortEmptySequences=" + affectedRowsToPlaceXsAtTooShortEmptySequences);
            System.out.println("affectedRowsToExtendColouredFieldsNearX=" + affectedRowsToExtendColouredFieldsNearX);
            System.out.println("-".repeat(50));
            System.out.println("Columns affected: ");
            System.out.println("affectedColumnsToColourOverlappingFields=" + affectedColumnsToFillOverlappingFields);
            System.out.println("affectedColumnsToMarkAvailableSequences=" + affectedColumnsToMarkAvailableSequences);
            System.out.println("affectedColumnsToCorrectSequencesRanges=" + affectedColumnsToCorrectSequencesRanges);
            System.out.println("affectedColumnsToCorrectSequencesRangesWhenMetColouredField=" + affectedColumnsToCorrectSequencesRangesWhenMetColouredField);
            System.out.println("affectedColumnsToChangeSequencesRangeIfXOnWay=" + affectedColumnsToCorrectSequencesRangesIfXOnWay);
            System.out.println("affectedColumnsToPlaceXsAtUnreachableFields=" + affectedColumnsToPlaceXsAtUnreachableFields);
            System.out.println("affectedColumnsToPlaceXsAroundLongestSequences=" + affectedColumnsToPlaceXsAroundLongestSequences);
            System.out.println("affectedColumnsToPlaceXsAtTooShortEmptySequences=" + affectedColumnsToPlaceXsAtTooShortEmptySequences);
            System.out.println("affectedColumnsToExtendColouredFieldsNearX=" + affectedColumnsToExtendColouredFieldsNearX);
            System.out.println("-".repeat(50));
        }
    }


    /**
     * @param actionNo - action identifier to make
     */
    public void basicSolve(int actionNo) {
        switch (actionNo) {
            case 1:
                this.copyLogicToNonogramRowLogic();
                nonogramRowLogic.fillOverlappingFieldsInRows();
                this.copyLogicFromNonogramRowLogic();
                setAffectedRowsToFillOverlappingFields(new HashSet<>());
                break;
            case 2:
                this.copyLogicToNonogramColumnLogic();
                nonogramColumnLogic.markAvailableSequencesInColumns();
                this.copyLogicFromNonogramColumnLogic();
                setAffectedColumnsToMarkAvailableSequences(new HashSet<>());
                break;
            case 3:
                this.copyLogicToNonogramColumnLogic();
                nonogramColumnLogic.fillOverlappingFieldsInColumns();
                this.copyLogicFromNonogramColumnLogic();
                setAffectedColumnsToFillOverlappingFields(new HashSet<>());
                break;
            case 4:
                this.copyLogicToNonogramRowLogic();
                nonogramRowLogic.markAvailableSequencesInRows();
                this.copyLogicFromNonogramRowLogic();
                setAffectedRowsToMarkAvailableSequences(new HashSet<>());
                break;
            case 5:
                this.copyLogicToNonogramRowLogic();
                nonogramRowLogic.correctSequencesRangesInRows();
                this.copyLogicFromNonogramRowLogic();
                setAffectedRowsToCorrectSequencesRanges(new HashSet<>());
                break;
            case 6:
                this.copyLogicToNonogramRowLogic();
                nonogramRowLogic.correctRowsSequencesRangesWhenMetColouredFields();
                this.copyLogicFromNonogramRowLogic();
                setAffectedRowsToCorrectSequencesRangesWhenMetColouredField(new HashSet<>());
                break;
            case 7:
                this.copyLogicToNonogramRowLogic();
                nonogramRowLogic.correctRowsSequencesRangesIndexesIfXOnWay();
                this.copyLogicFromNonogramRowLogic();
                setAffectedRowsToChangeSequencesRangeIfXOnWay(new HashSet<>());
                break;
            case 8:
                this.copyLogicToNonogramColumnLogic();
                nonogramColumnLogic.correctColumnsSequencesRanges();
                this.copyLogicFromNonogramColumnLogic();
                setAffectedColumnsToCorrectSequencesRanges(new HashSet<>());
                break;
            case 9:
                this.copyLogicToNonogramColumnLogic();
                nonogramColumnLogic.correctColumnsSequencesWhenMetColouredField();
                this.copyLogicFromNonogramColumnLogic();
                setAffectedColumnsToCorrectSequencesRangesWhenMetColouredField(new HashSet<>());
                break;
            case 10:
                this.copyLogicToNonogramColumnLogic();
                nonogramColumnLogic.correctColumnsSequencesIfXOnWay();;
                this.copyLogicFromNonogramColumnLogic();
                setAffectedColumnsToCorrectSequencesRangesIfXOnWay(new HashSet<>());
                break;
            case 11:
                this.copyLogicToNonogramRowLogic();
                nonogramRowLogic.placeXsRowsAtUnreachableFields();
                this.copyLogicFromNonogramRowLogic();
                setAffectedRowsToPlaceXsAtUnreachableFields(new HashSet<>());
                break;
            case 12:
                this.copyLogicToNonogramColumnLogic();
                nonogramColumnLogic.placeXsColumnsAtUnreachableFields();
                this.copyLogicFromNonogramColumnLogic();
                setAffectedColumnsToPlaceXsAtUnreachableFields(new HashSet<>());
                break;
            case 13:
                this.copyLogicToNonogramRowLogic();
                nonogramRowLogic.placeXsRowsAroundLongestSequences();
                this.copyLogicFromNonogramRowLogic();
                setAffectedRowsToPlaceXsAroundLongestSequences(new HashSet<>());
                break;
            case 14:
                this.copyLogicToNonogramColumnLogic();
                nonogramColumnLogic.placeXsColumnsAroundLongestSequences();
                this.copyLogicFromNonogramColumnLogic();
                setAffectedColumnsToPlaceXsAroundLongestSequences(new HashSet<>());
                break;
            case 15:
                this.copyLogicToNonogramRowLogic();
                nonogramRowLogic.placeXsRowsAtTooShortEmptySequences();
                this.copyLogicFromNonogramRowLogic();
                setAffectedRowsToPlaceXsAtTooShortEmptySequences(new HashSet<>());
                break;
            case 16:
                this.copyLogicToNonogramColumnLogic();
                nonogramColumnLogic.placeXsColumnsAtTooShortEmptySequences();;
                this.copyLogicFromNonogramColumnLogic();
                setAffectedColumnsToPlaceXsAtTooShortEmptySequences(new HashSet<>());
                break;
            case 17:
                this.copyLogicToNonogramRowLogic();
                nonogramRowLogic.extendColouredFieldsNearXToMaximumPossibleLengthInRows();
                this.copyLogicFromNonogramRowLogic();
                setAffectedRowsToExtendColouredFieldsNearX(new HashSet<>());
                break;
            case 18:
                this.copyLogicToNonogramColumnLogic();
                nonogramColumnLogic.extendColouredFieldsNearXToMaximumPossibleLengthInColumns();
                this.copyLogicFromNonogramColumnLogic();
                setAffectedColumnsToExtendColouredFieldsNearX(new HashSet<>());
                break;
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
        this.affectedRowsToFillOverlappingFields = this.nonogramColumnLogic.getAffectedRowsToFillOverlappingFields();
        this.affectedColumnsToMarkAvailableSequences = this.nonogramColumnLogic.getAffectedColumnsToMarkAvailableSequences();
        this.affectedColumnsToFillOverlappingFields = this.nonogramColumnLogic.getAffectedColumnsToFillOverlappingFields();
        this.affectedRowsToMarkAvailableSequences = this.nonogramColumnLogic.getAffectedRowsToMarkAvailableSequences();
        this.affectedRowsToCorrectSequencesRanges = this.nonogramColumnLogic.getAffectedRowsToCorrectSequencesRanges();
        this.affectedRowsToCorrectSequencesRangesWhenMetColouredField = this.nonogramColumnLogic.getAffectedRowsToCorrectSequencesRangesWhenMetColouredField();
        this.affectedRowsToChangeSequencesRangeIfXOnWay = this.nonogramColumnLogic.getAffectedRowsToChangeSequencesRangeIfXOnWay();
        this.affectedColumnsToCorrectSequencesRanges = this.nonogramColumnLogic.getAffectedColumnsToCorrectSequencesRanges();
        this.affectedColumnsToCorrectSequencesRangesWhenMetColouredField = this.nonogramColumnLogic.getAffectedColumnsToCorrectSequencesRangesWhenMetColouredField();
        this.affectedColumnsToCorrectSequencesRangesIfXOnWay = this.nonogramColumnLogic.getAffectedColumnsToCorrectSequencesRangesIfXOnWay();
        this.affectedRowsToPlaceXsAtUnreachableFields = this.nonogramColumnLogic.getAffectedRowsToPlaceXsAtUnreachableFields();
        this.affectedColumnsToPlaceXsAtUnreachableFields = this.nonogramColumnLogic.getAffectedColumnsToPlaceXsAtUnreachableFields();
        this.affectedRowsToPlaceXsAroundLongestSequences = this.nonogramColumnLogic.getAffectedRowsToPlaceXsAroundLongestSequences();
        this.affectedColumnsToPlaceXsAroundLongestSequences = this.nonogramColumnLogic.getAffectedColumnsToPlaceXsAroundLongestSequences();
        this.affectedRowsToPlaceXsAtTooShortEmptySequences = this.nonogramColumnLogic.getAffectedRowsToPlaceXsAtTooShortEmptySequences();
        this.affectedColumnsToPlaceXsAtTooShortEmptySequences = this.nonogramColumnLogic.getAffectedColumnsToPlaceXsAtTooShortEmptySequences();
        this.affectedRowsToExtendColouredFieldsNearX = this.nonogramColumnLogic.getAffectedRowsToExtendColouredFieldsNearX();
        this.affectedColumnsToExtendColouredFieldsNearX = this.nonogramColumnLogic.getAffectedColumnsToExtendColouredFieldsNearX();
        this.newStepsMade = this.nonogramColumnLogic.getNewStepsMade();
        this.solutionInvalid = this.nonogramColumnLogic.isSolutionInvalid();
    }

    public void copyLogicToNonogramColumnLogic() {
        this.nonogramColumnLogic.setLogs(this.logs);
        this.nonogramColumnLogic.setNonogramSolutionBoardWithMarks(this.nonogramSolutionBoardWithMarks);
        this.nonogramColumnLogic.setNonogramSolutionBoard(this.nonogramSolutionBoard);
        this.nonogramColumnLogic.setRowsSequencesRanges(this.rowsSequencesRanges);
        this.nonogramColumnLogic.setColumnsSequencesRanges(this.columnsSequencesRanges);
        this.nonogramColumnLogic.setRowsFieldsNotToInclude(this.rowsFieldsNotToInclude);
        this.nonogramColumnLogic.setColumnsFieldsNotToInclude(this.columnsFieldsNotToInclude);
        this.nonogramColumnLogic.setRowsSequencesIdsNotToInclude(this.rowsSequencesIdsNotToInclude);
        this.nonogramColumnLogic.setColumnsSequencesIdsNotToInclude(this.columnsSequencesIdsNotToInclude);
        this.nonogramColumnLogic.setAffectedRowsToFillOverlappingFields(this.affectedRowsToFillOverlappingFields);
        this.nonogramColumnLogic.setAffectedColumnsToMarkAvailableSequences(this.affectedColumnsToMarkAvailableSequences);
        this.nonogramColumnLogic.setAffectedColumnsToFillOverlappingFields(this.affectedColumnsToFillOverlappingFields);
        this.nonogramColumnLogic.setAffectedRowsToMarkAvailableSequences(this.affectedRowsToMarkAvailableSequences);
        this.nonogramColumnLogic.setAffectedRowsToCorrectSequencesRanges(this.affectedRowsToCorrectSequencesRanges);
        this.nonogramColumnLogic.setAffectedRowsToCorrectSequencesRangesWhenMetColouredField(this.affectedRowsToCorrectSequencesRangesWhenMetColouredField);
        this.nonogramColumnLogic.setAffectedRowsToChangeSequencesRangeIfXOnWay(this.affectedRowsToChangeSequencesRangeIfXOnWay);
        this.nonogramColumnLogic.setAffectedColumnsToCorrectSequencesRanges(this.affectedColumnsToCorrectSequencesRanges);
        this.nonogramColumnLogic.setAffectedColumnsToCorrectSequencesRangesWhenMetColouredField(this.affectedColumnsToCorrectSequencesRangesWhenMetColouredField);
        this.nonogramColumnLogic.setAffectedColumnsToCorrectSequencesRangesIfXOnWay(this.affectedColumnsToCorrectSequencesRangesIfXOnWay);
        this.nonogramColumnLogic.setAffectedRowsToPlaceXsAtUnreachableFields(this.affectedRowsToPlaceXsAtUnreachableFields);
        this.nonogramColumnLogic.setAffectedColumnsToPlaceXsAtUnreachableFields(this.affectedColumnsToPlaceXsAtUnreachableFields);
        this.nonogramColumnLogic.setAffectedRowsToPlaceXsAroundLongestSequences(this.affectedRowsToPlaceXsAroundLongestSequences);
        this.nonogramColumnLogic.setAffectedColumnsToPlaceXsAroundLongestSequences(this.affectedColumnsToPlaceXsAroundLongestSequences);
        this.nonogramColumnLogic.setAffectedRowsToPlaceXsAtTooShortEmptySequences(this.affectedRowsToPlaceXsAtTooShortEmptySequences);
        this.nonogramColumnLogic.setAffectedColumnsToPlaceXsAtTooShortEmptySequences(this.affectedColumnsToPlaceXsAtTooShortEmptySequences);
        this.nonogramColumnLogic.setAffectedRowsToExtendColouredFieldsNearX(this.affectedRowsToExtendColouredFieldsNearX);
        this.nonogramColumnLogic.setAffectedColumnsToExtendColouredFieldsNearX(this.affectedColumnsToExtendColouredFieldsNearX);
        this.nonogramColumnLogic.setNewStepsMade(this.newStepsMade);
        this.nonogramColumnLogic.setSolutionInvalid(this.solutionInvalid);
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
        this.affectedRowsToFillOverlappingFields = this.nonogramRowLogic.getAffectedRowsToFillOverlappingFields();
        this.affectedColumnsToMarkAvailableSequences = this.nonogramRowLogic.getAffectedColumnsToMarkAvailableSequences();
        this.affectedColumnsToFillOverlappingFields = this.nonogramRowLogic.getAffectedColumnsToFillOverlappingFields();
        this.affectedRowsToMarkAvailableSequences = this.nonogramRowLogic.getAffectedRowsToMarkAvailableSequences();
        this.affectedRowsToCorrectSequencesRanges = this.nonogramRowLogic.getAffectedRowsToCorrectSequencesRanges();
        this.affectedRowsToCorrectSequencesRangesWhenMetColouredField = this.nonogramRowLogic.getAffectedRowsToCorrectSequencesRangesWhenMetColouredField();
        this.affectedRowsToChangeSequencesRangeIfXOnWay = this.nonogramRowLogic.getAffectedRowsToChangeSequencesRangeIfXOnWay();
        this.affectedColumnsToCorrectSequencesRanges = this.nonogramRowLogic.getAffectedColumnsToCorrectSequencesRanges();
        this.affectedColumnsToCorrectSequencesRangesWhenMetColouredField = this.nonogramRowLogic.getAffectedColumnsToCorrectSequencesRangesWhenMetColouredField();
        this.affectedColumnsToCorrectSequencesRangesIfXOnWay = this.nonogramRowLogic.getAffectedColumnsToCorrectSequencesRangesIfXOnWay();
        this.affectedRowsToPlaceXsAtUnreachableFields = this.nonogramRowLogic.getAffectedRowsToPlaceXsAtUnreachableFields();
        this.affectedColumnsToPlaceXsAtUnreachableFields = this.nonogramRowLogic.getAffectedColumnsToPlaceXsAtUnreachableFields();
        this.affectedRowsToPlaceXsAroundLongestSequences = this.nonogramRowLogic.getAffectedRowsToPlaceXsAroundLongestSequences();
        this.affectedColumnsToPlaceXsAroundLongestSequences = this.nonogramRowLogic.getAffectedColumnsToPlaceXsAroundLongestSequences();
        this.affectedRowsToPlaceXsAtTooShortEmptySequences = this.nonogramRowLogic.getAffectedRowsToPlaceXsAtTooShortEmptySequences();
        this.affectedColumnsToPlaceXsAtTooShortEmptySequences = this.nonogramRowLogic.getAffectedColumnsToPlaceXsAtTooShortEmptySequences();
        this.affectedRowsToExtendColouredFieldsNearX = this.nonogramRowLogic.getAffectedRowsToExtendColouredFieldsNearX();
        this.affectedColumnsToExtendColouredFieldsNearX = this.nonogramRowLogic.getAffectedColumnsToExtendColouredFieldsNearX();
        this.newStepsMade = this.nonogramRowLogic.getNewStepsMade();
        this.solutionInvalid = this.nonogramRowLogic.isSolutionInvalid();
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
        this.nonogramRowLogic.setAffectedRowsToFillOverlappingFields(this.affectedRowsToFillOverlappingFields);
        this.nonogramRowLogic.setAffectedColumnsToMarkAvailableSequences(this.affectedColumnsToMarkAvailableSequences);
        this.nonogramRowLogic.setAffectedColumnsToFillOverlappingFields(this.affectedColumnsToFillOverlappingFields);
        this.nonogramRowLogic.setAffectedRowsToMarkAvailableSequences(this.affectedRowsToMarkAvailableSequences);
        this.nonogramRowLogic.setAffectedRowsToCorrectSequencesRanges(this.affectedRowsToCorrectSequencesRanges);
        this.nonogramRowLogic.setAffectedRowsToCorrectSequencesRangesWhenMetColouredField(this.affectedRowsToCorrectSequencesRangesWhenMetColouredField);
        this.nonogramRowLogic.setAffectedRowsToChangeSequencesRangeIfXOnWay(this.affectedRowsToChangeSequencesRangeIfXOnWay);
        this.nonogramRowLogic.setAffectedColumnsToCorrectSequencesRanges(this.affectedColumnsToCorrectSequencesRanges);
        this.nonogramRowLogic.setAffectedColumnsToCorrectSequencesRangesWhenMetColouredField(this.affectedColumnsToCorrectSequencesRangesWhenMetColouredField);
        this.nonogramRowLogic.setAffectedColumnsToCorrectSequencesRangesIfXOnWay(this.affectedColumnsToCorrectSequencesRangesIfXOnWay);
        this.nonogramRowLogic.setAffectedRowsToPlaceXsAtUnreachableFields(this.affectedRowsToPlaceXsAtUnreachableFields);
        this.nonogramRowLogic.setAffectedColumnsToPlaceXsAtUnreachableFields(this.affectedColumnsToPlaceXsAtUnreachableFields);
        this.nonogramRowLogic.setAffectedRowsToPlaceXsAroundLongestSequences(this.affectedRowsToPlaceXsAroundLongestSequences);
        this.nonogramRowLogic.setAffectedColumnsToPlaceXsAroundLongestSequences(this.affectedColumnsToPlaceXsAroundLongestSequences);
        this.nonogramRowLogic.setAffectedRowsToPlaceXsAtTooShortEmptySequences(this.affectedRowsToPlaceXsAtTooShortEmptySequences);
        this.nonogramRowLogic.setAffectedColumnsToPlaceXsAtTooShortEmptySequences(this.affectedColumnsToPlaceXsAtTooShortEmptySequences);
        this.nonogramRowLogic.setAffectedRowsToExtendColouredFieldsNearX(this.affectedRowsToExtendColouredFieldsNearX);
        this.nonogramRowLogic.setAffectedColumnsToExtendColouredFieldsNearX(this.affectedColumnsToExtendColouredFieldsNearX);
        this.nonogramRowLogic.setNewStepsMade(this.newStepsMade);
        this.nonogramRowLogic.setSolutionInvalid(this.solutionInvalid);
    }

    /**
     * @return total area of nonogram (fields to fill)
     */
    public int area() {
        return this.getWidth() * this.getHeight();
    }


    /**
     * @param solutionFileName - String fileName with correct solution board
     * @return true if fields are colored correctly in the partial solution, false in other cases
     */
    public boolean subsolutionBoardCorrectComparisonWithSolutionBoard(String solutionFileName) {
        List<List<String>> subsolutionNonogramBoard = this.nonogramSolutionBoard;
        NonogramBoardTemplate solutionBoardTemplate = new NonogramBoardTemplate(solutionFileName);
        List<List<String>> solutionNonogramBoard = solutionBoardTemplate.getBoard();

        Iterator<List<String>> subsolutionNonogramBoardIterator = subsolutionNonogramBoard.iterator();
        Iterator<List<String>> solutionNonogramBoardIterator = solutionNonogramBoard.iterator();

        while(subsolutionNonogramBoardIterator.hasNext() && solutionNonogramBoardIterator.hasNext()) {

            List<String> subsolutionNonogramBoardRow = subsolutionNonogramBoardIterator.next();
            List<String> solutionNonogramBoardRow = solutionNonogramBoardIterator.next();

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
     * @param columnIdx - column index to exclude sequence (fully coloured and marked sequence, sequence range == sequence length)
     * @param seqIdx - sequence index to exclude in specified column
     * @return NonogramLogic object with sequence index in specified column excluded
     */
    public NonogramLogic addColumnSequenceIdxToNotToInclude(int columnIdx, int seqIdx) {
        if(!this.columnsSequencesIdsNotToInclude.get(columnIdx).contains(seqIdx)) {
            this.columnsSequencesIdsNotToInclude.get(columnIdx).add(seqIdx);
            Collections.sort(this.columnsSequencesIdsNotToInclude.get(columnIdx));
        }

        return this;
    }

    /**
     * @param rowIdx - field row index
     * @param columnIdx - field column index
     * @return NonogramLogic object with "X" placed on specified position
     */
    public NonogramLogic placeXAtGivenPosittion(int rowIdx, int columnIdx) {
        boolean rowValid = isRowIndexValid(rowIdx);
        boolean columnValid = isColumnIndexValid(columnIdx);
        if(rowValid && columnValid) {
            this.nonogramSolutionBoard.get(rowIdx).set(columnIdx, "X");
            this.nonogramSolutionBoardWithMarks.get(rowIdx).set(columnIdx, "XXXX");
        }

        return this;
    }

    public boolean isSolved() {
        return this.fieldsColoured() + this.fieldsWithXPlaced() == this.area();
    }
}
