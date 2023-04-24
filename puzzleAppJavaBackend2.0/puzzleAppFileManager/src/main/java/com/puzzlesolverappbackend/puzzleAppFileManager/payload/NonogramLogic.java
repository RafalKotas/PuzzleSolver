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
import static com.puzzlesolverappbackend.puzzleAppFileManager.services.NonogramLogicService.rangeInsideAnotherRange;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NonogramLogic {

    boolean showRepetitions = false;

    private List<String> logs;

    private String tmpLog;

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

    // Sets (to prevent rows/columns repetitions)
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
        this.logs.clear();;
    }

    private List<List<String>> generateEmptyBoard(int height, int width, int emptyFieldRepeatCount) {
        List<List<String>> emptyBoard = new ArrayList<>(height);
        for(int rowIdx = 0; rowIdx < height; rowIdx++) {
            List<String> boardRow = new ArrayList<>();
            for(int column = 0; column < width; column++) {
                boardRow.add("-".repeat(emptyFieldRepeatCount));
            }
            emptyBoard.add(boardRow);
        }
        return emptyBoard;
    }

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

    public void printNonogramBoard() {
        for(List<String> boardRow : this.getNonogramSolutionBoard()) {
            System.out.println(boardRow);
        }
    }

    public void printNonogramBoardWithMarks() {
        for(List<String> boardRow : this.getNonogramSolutionBoardWithMarks()) {
            System.out.println(boardRow);
        }
    }

    public void printRowsSequencesRanges() {
        int rowIdx = 0;
        for(List<List<Integer>> rowSequencesRanges : this.getRowsSequencesRanges()) {
            System.out.println(rowIdx + " " + rowSequencesRanges);
            rowIdx++;
        }
    }

    public void printColumnsSequencesRanges() {
        int colIdx = 0;
        for(List<List<Integer>> colSequencesRanges : this.getColumnsSequencesRanges()) {
            System.out.println(colIdx + " " + colSequencesRanges);
            colIdx++;
        }
    }

    public void printLogs() {
        for(String log : this.logs) {
            System.out.println(log);
        }
    }

    public void updateRowSequenceRange(int rowIdx, int sequenceIdx, List<Integer> updatedRange) {
        this.rowsSequencesRanges.get(rowIdx).set(sequenceIdx, updatedRange);

    }

    public void updateColumnSequenceRange(int columnIdx, int sequenceIdx, List<Integer> updatedRange) {
        this.columnsSequencesRanges.get(columnIdx).set(sequenceIdx, updatedRange);

    }

    public void markRowBoardField(int rowIdx, int colIdx, String marker) {
        String oldRowField = this.nonogramSolutionBoardWithMarks.get(rowIdx).get(colIdx);
        this.nonogramSolutionBoardWithMarks.get(rowIdx).set(colIdx, "R" + marker + oldRowField.substring(2, 4));

    }

    public void markColumnBoardField(int rowIdx, int colIdx, String marker) {
        String oldRowField = this.nonogramSolutionBoardWithMarks.get(rowIdx).get(colIdx);
        this.nonogramSolutionBoardWithMarks.get(rowIdx).set(colIdx, oldRowField.substring(0, 2) + "C" + marker);

    }

    public NonogramLogic placeXAtGivenPosittion(int rowIdx, int columnIdx) {
        boolean rowValid = isRowIndexValid(rowIdx);
        boolean columnValid = isColumnIdxValid(columnIdx);
        if(rowValid && columnValid) {
            this.nonogramSolutionBoard.get(rowIdx).set(columnIdx, "X");
            this.nonogramSolutionBoardWithMarks.get(rowIdx).set(columnIdx, "XXXX");
        }

        return this;
    }

    public NonogramLogic colourFieldAtGivenPosittion(int rowIdx, int columnIdx) {
        boolean rowValid = isRowIndexValid(rowIdx);
        boolean columnValid = isColumnIdxValid(columnIdx);
        if(rowValid && columnValid) {
            this.nonogramSolutionBoard.get(rowIdx).set(columnIdx, "O");
        }

        return this;
    }

    private boolean isRowIndexValid (int rowIdx) {
        return rowIdx >= 0 && rowIdx < this.getHeight();
    }

    private boolean isColumnIdxValid (int columnIdx) {
        return columnIdx >= 0 && columnIdx < this.getWidth();
    }

    public NonogramLogic addRowFieldToNotToInclude(int rowIdx, int columnIdx) {
        boolean rowValid = isRowIndexValid(rowIdx);
        boolean columnValid = isColumnIdxValid(columnIdx);
        List<Integer> fieldsNotToIncludeSorted;
        if(rowValid && columnValid && !this.rowsFieldsNotToInclude.get(rowIdx).contains(columnIdx)) {
            this.rowsFieldsNotToInclude.get(rowIdx).add(columnIdx);

            fieldsNotToIncludeSorted = this.rowsFieldsNotToInclude.get(rowIdx);
            Collections.sort(fieldsNotToIncludeSorted);
        }

        return this;
    }

    public NonogramLogic addRowSequenceIdxToNotToInclude(int rowIdx, int seqIdx) {
        boolean rowValid = isRowIndexValid(rowIdx);
        if(rowValid && !this.rowsSequencesIdsNotToInclude.get(rowIdx).contains(seqIdx)) {
            this.rowsSequencesIdsNotToInclude.get(rowIdx).add(seqIdx);
            Collections.sort(this.rowsSequencesIdsNotToInclude.get(rowIdx));
        }

        return this;
    }

    public NonogramLogic addColumnFieldToNotToInclude(int columnIdx, int rowIdx) {
        boolean rowValid = isRowIndexValid(rowIdx);
        boolean columnValid = isColumnIdxValid(columnIdx);
        if(rowValid && columnValid && !this.columnsFieldsNotToInclude.get(columnIdx).contains(rowIdx) && rowIdx < this.getHeight() && rowIdx >= 0) {
            this.columnsFieldsNotToInclude.get(columnIdx).add(rowIdx);
            Collections.sort(this.columnsFieldsNotToInclude.get(columnIdx));
        }

        return this;
    }

    public NonogramLogic addColumnSequenceIdxToNotToInclude(int columnIdx, int seqIdx) {
        if(!this.columnsSequencesIdsNotToInclude.get(columnIdx).contains(seqIdx)) {
            this.columnsSequencesIdsNotToInclude.get(columnIdx).add(seqIdx);
            Collections.sort(this.columnsSequencesIdsNotToInclude.get(columnIdx));
        }

        return this;
    }

    public NonogramLogic changeRowSequenceRange(int rowIdx, int sequenceIdx, int rangeStart, int rangeEnd) {
        this.rowsSequencesRanges.get(rowIdx).get(sequenceIdx).set(0, rangeStart);
        this.rowsSequencesRanges.get(rowIdx).get(sequenceIdx).set(1, rangeEnd);

        return this;
    }

    public NonogramLogic changeColumnSequenceRange(int columnIdx, int sequenceIdx, int rangeStart, int rangeEnd) {
        this.columnsSequencesRanges.get(columnIdx).get(sequenceIdx).set(0, rangeStart);
        this.columnsSequencesRanges.get(columnIdx).get(sequenceIdx).set(1, rangeEnd);

        return this;
    }

    public int minimumColumnIndexWithoutX(int rowIdx, int lastSequenceColumnIdx, int sequenceFullLength) {
        List<String> boardRow = this.getNonogramSolutionBoard().get(rowIdx);
        int minimumColumnIndex = lastSequenceColumnIdx;
        int minimumColumnIndexLimit = Math.max(lastSequenceColumnIdx - sequenceFullLength + 1, 0);
        String fieldMark;

        for(; minimumColumnIndex >= minimumColumnIndexLimit; minimumColumnIndex--) {
            fieldMark = boardRow.get(minimumColumnIndex);
            if(fieldMark.equals("X")) {
                break;
            }
        }

        return minimumColumnIndex + 1;
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

    public int minimumRowIndexWithoutX(int columnIdx, int lastSequenceRowIdx, int sequenceFullLength) {

        int minimumRowIndex = lastSequenceRowIdx;
        int minimumRowIndexLimit = Math.max(lastSequenceRowIdx - sequenceFullLength + 1, 0);
        String fieldMark;

        for(; minimumRowIndex >= minimumRowIndexLimit; minimumRowIndex--) {
            fieldMark = this.nonogramSolutionBoard.get(minimumRowIndex).get(columnIdx);
            if(fieldMark.equals("X")) {
                break;
            }
        }

        return minimumRowIndex + 1;
    }

    public int maximumRowIndexWithoutX(int columnIdx, int firstSequenceRowIdx, int sequenceFullLength) {

        int maximumRowIndex = firstSequenceRowIdx;
        int maximumRowIndexLimit = Math.min(firstSequenceRowIdx + sequenceFullLength - 1, this.getHeight() - 1);
        String fieldMark;

        for(; maximumRowIndex <= maximumRowIndexLimit; maximumRowIndex++) {
            fieldMark = nonogramSolutionBoard.get(maximumRowIndex).get(columnIdx);
            if(fieldMark.equals("X")) {
                break;
            }
        }

        return maximumRowIndex - 1;
    }

    public int getWidth() {
        return this.columnsSequences.size();
    }

    public int getHeight() {
        return this.rowsSequences.size();
    }

    // some logic

    //rows

    private List<List<List<Integer>>> inferInitialRowsSequencesRanges() {
        List<List<List<Integer>>> initialRowSequencesRanges = new ArrayList<>();

        for(int rowIdx = 0; rowIdx < rowsSequences.size(); rowIdx ++) {
            initialRowSequencesRanges.add( inferInitialRowSequencesRanges( rowsSequences.get(rowIdx), rowIdx) );
        }

        return initialRowSequencesRanges;
    }

    private List<List<Integer>> inferInitialRowSequencesRanges (List<Integer> sequences, int rowIdx) {

        List<String> arrayFilledFromStart = createRowArrayFromSequencesAndChars(rowIdx, sequences, false);
        List<String> arrayFilledFromEnd = reverseList(createRowArrayFromSequencesAndChars(rowIdx, sequences, true));

        return inferRowSequencesRangesFromArrays(arrayFilledFromStart, arrayFilledFromEnd);
    }

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


                rangeLastIndex = findLastIndexContainingRow(arrayFilledFromEnd, "R" + fieldSequenceChar);
                List<Integer> sequenceRange = new ArrayList<>();
                sequenceRange.add(rangeStartIndex);
                sequenceRange.add(rangeLastIndex);
                sequencesRanges.add(sequenceRange);
            }
        }

        return sequencesRanges;
    }

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

    private List<List<Integer>> inferInitialColumnSequencesRanges (List<Integer> sequences, int columnIdx) {

        List<String> arrayFilledFromStart = createColumnArrayFromSequencesAndChars(columnIdx, sequences, false);
        List<String> arrayFilledFromEnd = reverseList( createColumnArrayFromSequencesAndChars(columnIdx, sequences, true) );

        return inferColumnSequencesRangesFromArrays(arrayFilledFromStart, arrayFilledFromEnd);
    }

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

    private boolean checkIfCanStartSequenceFromColumnIndex (int columnIdx, int fieldIdx, int sequenceLength) {
        List<String> fieldsToCheck = getSolutionBoardWithMarksColumn(columnIdx).subList(fieldIdx, fieldIdx + sequenceLength);

        Predicate<String> fieldWithX = field -> field.equals("XXXX");

        List<String> xs = fieldsToCheck.stream().filter(fieldWithX).collect(Collectors.toList());

        return xs.size() == 0;
    }

    List<String> getSolutionBoardWithMarksColumn(int columnIdx) {
        List<String> boardColumn = new ArrayList<>();

        for (List<String> nonogramSolutionBoardWithMark : nonogramSolutionBoardWithMarks) {
            boardColumn.add(nonogramSolutionBoardWithMark.get(columnIdx));
        }

        return boardColumn;
    }

    List<String> getSolutionBoardColumn(int columnIdx) {
        List<String> boardColumn = new ArrayList<>();

        for (List<String> strings : nonogramSolutionBoard) {
            boardColumn.add(strings.get(columnIdx));
        }

        return boardColumn;
    }

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

    private List<List<List<Integer>>> inferRowsSequencesRangesFromSolutionBoard() {
        List<List<List<Integer>>> inferredRowsSequencesRanges = new ArrayList<>();

        for(int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            inferredRowsSequencesRanges.add(inferRowSequencesRangesFromSolutionBoardRow(rowIdx));
        }

        return inferredRowsSequencesRanges;
    }

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

    private List<List<List<Integer>>> inferColumnsSequencesRangesFromSolutionBoard() {
        List<List<List<Integer>>> inferredColumnsSequencesRanges = new ArrayList<>();

        for(int columnIdx = 0; columnIdx < this.getWidth(); columnIdx++) {
            inferredColumnsSequencesRanges.add(inferColumnSequencesRangesFromSolutionBoardColumn(columnIdx));
        }

        return inferredColumnsSequencesRanges;
    }

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

    private List<String> generateArrayOfSequenceMarks (int marks) {
        List<String> arrayOfSequenceMarks = new ArrayList<>();

        for(int charIdx = 0; charIdx < marks; charIdx++) {
            arrayOfSequenceMarks.add( indexToSequenceCharMark(charIdx) );
        }

        return arrayOfSequenceMarks;
    }

    public static String indexToSequenceCharMark(int index) {
        return Character.toString((char) ((int) 'a' + index));
    }

    public static<T> List<T> reverseList(List<T> list) {
        List<T> reverse = new ArrayList<>(list);
        Collections.reverse(reverse);
        return reverse;
    }

    private int findLastIndexContaining (List<String> array, String matchingSubstring) {

        for( int arrIdx = array.size() - 1; arrIdx >= 0; arrIdx--) {
            if(array.get(arrIdx).contains(matchingSubstring)) {
                return arrIdx;
            }
        }

        return -1;
    }

    private int findLastIndexContainingRow (List<String> array, String matchingSubstring) {

        for( int arrIdx = array.size() - 1; arrIdx >= 0; arrIdx--) {
            if(array.get(arrIdx).contains(matchingSubstring)) {
                return arrIdx;
            }
        }

        return -1;
    }

    public NonogramLogic increaseStepsMade() {
        this.newStepsMade = this.newStepsMade + 1;
        return this;
    }

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

    public int fieldsFilled() {
        return this.fieldsWithXPlaced() + this.fieldsColoured();
    }

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

    public int fieldsToPlaceXTotal() {
        return (this.getHeight() * this.getWidth()) - fieldsToColourTotal();
    }

    public double fieldsWithXPlacedPercent() {
        return Math.round(((double)this.fieldsWithXPlaced() / (double)this.fieldsToPlaceXTotal()) * 10000)/ 100.0;
    }

    public double fieldsColouredPercent() {
        return Math.round(((double)this.fieldsColoured() / (double)this.fieldsToColourTotal()) * 10000) / 100.0;
    }

    /**
     * @return completionPercentage of nonogram in given nonogramLogic node
     */
    public double getCompletionPercentage() {
        int xPlaced = this.fieldsWithXPlaced();
        int coloured = this.fieldsColoured();

        double completionPercentage = Math.round(((double)(xPlaced + coloured) / this.area()) * 10000 ) / 100.0;

        return completionPercentage;
    }

    public void printStatsAfterBasicActionsMade() {
        //printNonogramBoard();
        int fieldsXPlaced = this.fieldsWithXPlaced();
        int fieldsColoured = this.fieldsColoured();
        int fieldsXTotal  = this.fieldsToPlaceXTotal();
        int colourFieldsTotal = this.fieldsToColourTotal();
        this.printNonogramBoard();
        /*System.out.println("X placed: " + fieldsXPlaced + "/" + fieldsXTotal);
        System.out.println("O placed: " + fieldsColoured + "/" + colourFieldsTotal);
        System.out.println("X(percent): " + this.fieldsWithXPlacedPercent());
        System.out.println("O(percent): " + this.fieldsColouredPercent());*/
        System.out.println(String.format("%-12s %-12s %-12s | %-12s %-12s %-12s",
                "'X' placed", "'X' total", "'X' percent",
                "'O' placed", "'O' total", "'O' percent"));
        System.out.println(String.format("%-12s %-12s %-12s | %-12s %-12s %-12s",
                fieldsXPlaced, fieldsXTotal, this.fieldsWithXPlacedPercent(),
                fieldsColoured, fieldsToColourTotal(), this.fieldsColouredPercent()));
        //System.out.println(String.format("%-12s %-12s %-12s", "'O' placed", "'O' total", "'O' percent"));
        //System.out.println(String.format("%-12s %-12s %-12s", "'O' placed", "'O' total", "'O' percent"));

        System.out.println("Overall completion percentage: " + this.getCompletionPercentage() + "%");
        System.out.println("newStepsMade: " + this.newStepsMade);
        System.out.println("possible fields to make decision: " + this.availableChoices);
    }

    public double getColouredFieldPercent() {
        return (this.fieldsColoured() / this.fieldsToColourTotal()) * 100;
    }

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

    /*public static boolean compareTwoRowsSequencesRangesArrays(List<List<List<Integer>>> firstRowsSequencesRanges,
                                                              List<List<List<Integer>>> secondRowsSequencesRanges) {
        boolean areIdentical = true;
        List<List<Integer>> firstSequencesRowRanges;
        List<List<Integer>> secondSequencesRowRanges;
        List<Integer> firstRowSequenceRange;
        List<Integer> secondRowSequenceRange;
        for(int idx = 0; idx < firstRowsSequencesRanges.size(); idx++) {
            firstSequencesRowRanges = firstRowsSequencesRanges.get(idx);
            secondSequencesRowRanges = secondRowsSequencesRanges.get(idx);

            for(int seqIdx = 0; seqIdx < firstSequencesRowRanges.size(); seqIdx++) {
                firstRowSequenceRange = firstSequencesRowRanges.get(seqIdx);
                secondRowSequenceRange = secondSequencesRowRanges.get(seqIdx);
                if(firstRowSequenceRange.get(0).equals(secondRowSequenceRange.get(0)) || firstRowSequenceRange.get(1).equals(secondRowSequenceRange.get(1)) ) {
                    System.out.println("rowIdx: " + idx + " seq: " + seqIdx + " change recorded!");
                    areIdentical = false;
                }
            }
        }

        return areIdentical;
    }*/

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

    public void basicSolve(int actionNo) {
        switch (actionNo) {
            case 1:
                fillOverlappingFieldsInRows(); //print+
                setAffectedRowsToFillOverlappingFields(new HashSet<>());
                break;
            case 2:
                markAvailableSequencesInColumns();
                setAffectedColumnsToMarkAvailableSequences(new HashSet<>());
                break;
            case 3:
                fillOverlappingFieldsInColumns(); //print+
                setAffectedColumnsToFillOverlappingFields(new HashSet<>());
                break;
            case 4:
                markAvailableSequencesInRows();
                setAffectedRowsToMarkAvailableSequences(new HashSet<>());
                break;
            case 5:
                correctRowsSequencesRanges();
                setAffectedRowsToCorrectSequencesRanges(new HashSet<>());
                break;
            case 6:
                correctRowsSequencesRangesWhenMetColouredFields();
                setAffectedRowsToCorrectSequencesRangesWhenMetColouredField(new HashSet<>());
                break;
            case 7:
                correctRowsSequencesRangesIndexesIfXOnWay();
                setAffectedRowsToChangeSequencesRangeIfXOnWay(new HashSet<>());
                break;
            case 8:
                correctColumnsSequencesRanges();
                setAffectedColumnsToCorrectSequencesRanges(new HashSet<>());
                break;
            case 9:
                correctColumnsSequencesWhenMetColouredField();
                setAffectedColumnsToCorrectSequencesRangesWhenMetColouredField(new HashSet<>());
                break;
            case 10:
                correctColumnsSequencesIfXOnWay();
                setAffectedColumnsToCorrectSequencesRangesIfXOnWay(new HashSet<>());
                break;
            case 11:
                placeXsRowsAtUnreachableFields(); //print+
                setAffectedRowsToPlaceXsAtUnreachableFields(new HashSet<>());
                break;
            case 12:
                placeXsColumnsAtUnreachableFields(); //print+
                setAffectedColumnsToPlaceXsAtUnreachableFields(new HashSet<>());
                break;
            case 13:
                placeXsRowsAroundLongestSequences(); //print+
                setAffectedRowsToPlaceXsAroundLongestSequences(new HashSet<>());
                break;
            case 14:
                placeXsColumnsAroundLongestSequences(); //print+
                setAffectedColumnsToPlaceXsAroundLongestSequences(new HashSet<>());
                break;
            case 15:
                placeXsRowsAtTooShortEmptySequences();
                setAffectedRowsToPlaceXsAtTooShortEmptySequences(new HashSet<>());
                break;
            case 16:
                placeXsColumnsAtTooShortEmptySequences();
                setAffectedColumnsToPlaceXsAtTooShortEmptySequences(new HashSet<>());
                break;
            case 17:
                extendColouredFieldsNearXToMinimumPossibleLengthInRows();
                setAffectedRowsToExtendColouredFieldsNearX(new HashSet<>());
                break;
            case 18:
                extendColouredFieldsNearXToMinimumPossibleLengthInColumns();
                setAffectedColumnsToExtendColouredFieldsNearX(new HashSet<>());
                break;
        }

        /*List<List<Integer>> row13ranges = this.getRowsSequencesRanges().get(13);

        if(row13ranges.get(0).get(0) == 1 && row13ranges.get(0).get(1) == 2) {
            tmpLog = "Changed row 13 seqNo==0 range to [1, 2], after actionNo: " + actionNo;
            this.logs.add(tmpLog);
        }*/

        /*if(this.getNonogramBoardColumn(2).get(6).equals("O")) {
            tmpLog = " coloured 6th row field in coloumn 2, after actionNo: " + actionNo;
            this.logs.add(tmpLog);
        }*/

        /*if(this.affectedRowsToPlaceXsAtTooShortEmptySequences.contains(14)) {
            System.out.println("actionNo: " + actionNo + " row 14 added, WRONG!!!!!");
        }*/

        /*if(this.rowsSequencesIdsNotToInclude.get(13).contains(0)) {
            tmpLog = "Row 13 sequences not to include contains 0, after actionNo: " + actionNo;
            this.logs.add(tmpLog);
        }*/
    }

    // iterations through all rows
    public void fillOverlappingFieldsInRows() {

        for (Integer rowIndex : this.getAffectedRowsToFillOverlappingFields()) {
            fillOverlappingFieldsInRow(rowIndex);
        }

    }

    public void fillOverlappingFieldsInRow (int rowIdx) {
        List<Integer> sequencesInRow = this.getRowsSequences().get(rowIdx);
        List<List<Integer>> sequencesInRowRanges = this.getRowsSequencesRanges().get(rowIdx);

        int sequenceLength;
        int rangeBeginIndex;
        int rangeEndIndex;
        int colourBeginColumnIndex;
        int colourEndColumnIndex;

        String sequenceCharMark;

        List<String> rowToChangeSolutionBoard;
        List<String> rowToChangeSolutionBoardWithMarks;

        String elementToChangeInsideRowBoardWithMarks;

        for(int sequenceIdx = 0; sequenceIdx < sequencesInRow.size(); sequenceIdx++) {
            sequenceLength = sequencesInRow.get(sequenceIdx);
            rangeBeginIndex = sequencesInRowRanges.get(sequenceIdx).get(0);
            rangeEndIndex = sequencesInRowRanges.get(sequenceIdx).get(1);

            // the range in which we can color the fields from sequence <cBCI, cECI>
            colourBeginColumnIndex = rangeEndIndex - sequenceLength + 1;
            colourEndColumnIndex = rangeBeginIndex + sequenceLength - 1;

            if(colourBeginColumnIndex <= colourEndColumnIndex) {

                sequenceCharMark = NonogramLogic.indexToSequenceCharMark(sequenceIdx);
                rowToChangeSolutionBoardWithMarks = this.getNonogramSolutionBoardWithMarks().get(rowIdx);

                for (int columnIdx = colourBeginColumnIndex; columnIdx <= colourEndColumnIndex; columnIdx++) {
                    elementToChangeInsideRowBoardWithMarks = rowToChangeSolutionBoardWithMarks.get(columnIdx);

                    if(rowToChangeSolutionBoardWithMarks.get(columnIdx).startsWith("--")) {
                        this.increaseStepsMade();
                        rowToChangeSolutionBoardWithMarks.set(columnIdx, "R" + sequenceCharMark + elementToChangeInsideRowBoardWithMarks.substring(2, 4));

                        //2
                        this.getAffectedColumnsToMarkAvailableSequences().add(columnIdx);
                        //9
                        this.getAffectedColumnsToCorrectSequencesRangesWhenMetColouredField().add(columnIdx);
                        //14
                        this.getAffectedColumnsToPlaceXsAroundLongestSequences().add(columnIdx);
                        //18
                        this.getAffectedColumnsToExtendColouredFieldsNearX().add(columnIdx);

                        rowToChangeSolutionBoard = this.getNonogramSolutionBoard().get(rowIdx);
                        if(rowToChangeSolutionBoard.get(columnIdx).equals("-")) {
                            this.colourFieldAtGivenPosittion(rowIdx, columnIdx);
                            tmpLog = "O placed, row: " + rowIdx + " , col: " + columnIdx + " (overlapping fields in row).";
                            this.logs.add(tmpLog);
                        }
                    } else if (showRepetitions) {
                        System.out.println("Row field was coloured earlier!");
                    }
                }
            }
        }

    }

    public void fillOverlappingFieldsInColumns () {
        for (Integer columnIndex : this.getAffectedColumnsToFillOverlappingFields()) {
            fillOverlappingFieldsInColumn(columnIndex);
        }
    }

    public void fillOverlappingFieldsInColumn (int columnIdx) {
        List<Integer> sequencesInColumnLengths = this.getColumnsSequences().get(columnIdx);
        List<List<Integer>> sequencesInColumnRanges = this.getColumnsSequencesRanges().get(columnIdx);

        int sequenceLength;
        int rangeBeginIndex;
        int rangeEndIndex;
        int colourBeginRowIndex;
        int colourEndRowIndex;

        String sequenceCharMark;

        List<String> rowToChangeColumnBoard;

        List<String> rowToChangeColumnBoardWithMarks;
        String elementToChangeInsideRowBoardWithMarks;

        for(int sequenceIdx = 0; sequenceIdx < sequencesInColumnLengths.size(); sequenceIdx++) {
            sequenceLength = sequencesInColumnLengths.get(sequenceIdx);
            rangeBeginIndex = sequencesInColumnRanges.get(sequenceIdx).get(0);
            rangeEndIndex = sequencesInColumnRanges.get(sequenceIdx).get(1);

            colourBeginRowIndex = rangeEndIndex - sequenceLength + 1;
            colourEndRowIndex = rangeBeginIndex + sequenceLength - 1;

            if(colourBeginRowIndex <= colourEndRowIndex) {

                sequenceCharMark = NonogramLogic.indexToSequenceCharMark(sequenceIdx);

                if(colourEndRowIndex - colourBeginRowIndex + 1 == sequencesInColumnLengths.get(sequenceIdx)) {
                    this.addColumnSequenceIdxToNotToInclude(columnIdx, sequenceIdx);
                }

                for (int rowIdx = colourBeginRowIndex; rowIdx <= colourEndRowIndex; rowIdx++) {
                    try {
                        rowToChangeColumnBoardWithMarks = this.getNonogramSolutionBoardWithMarks().get(rowIdx);
                        elementToChangeInsideRowBoardWithMarks = rowToChangeColumnBoardWithMarks.get(columnIdx);


                        if(rowToChangeColumnBoardWithMarks.get(columnIdx).substring(2).equals("--")) {
                            this.increaseStepsMade();

                            //4
                            this.getAffectedRowsToMarkAvailableSequences().add(rowIdx);
                            //6
                            this.getAffectedRowsToCorrectSequencesRangesWhenMetColouredField().add(rowIdx);
                            //13
                            this.getAffectedRowsToPlaceXsAroundLongestSequences().add(rowIdx);
                            //17
                            this.getAffectedRowsToExtendColouredFieldsNearX().add(rowIdx);

                            rowToChangeColumnBoardWithMarks.set(columnIdx, elementToChangeInsideRowBoardWithMarks.substring(0, 2) + "C" + sequenceCharMark);
                            this.getNonogramSolutionBoardWithMarks().set(rowIdx, rowToChangeColumnBoardWithMarks);
                            this.increaseStepsMade();

                            rowToChangeColumnBoard = this.getNonogramSolutionBoard().get(rowIdx);
                            if(rowToChangeColumnBoard.get(columnIdx).equals("-")) {
                                rowToChangeColumnBoard.set(columnIdx, "O");

                                tmpLog = "O placed, col: " + columnIdx + " , row: " + rowIdx + " (overlapping fields in column).";
                                this.logs.add(tmpLog);

                                this.getNonogramSolutionBoard().set(rowIdx, rowToChangeColumnBoard);
                            }
                        } else if (showRepetitions) {
                            System.out.println("Column field was coloured earlier!");
                        }
                    } catch (IndexOutOfBoundsException e) {
                        this.setSolutionInvalid(true);
                    }
                }
            }
        }

    }

    public void extendColouredFieldsNearXToMinimumPossibleLengthInRows() {

        for (Integer rowIndex : this.getAffectedRowsToExtendColouredFieldsNearX()) {
            extendColouredFieldsToLeftNearXToMinimumPossibleLengthInRow(rowIndex);
            extendColouredFieldsToRightNearXToMinimumPossibleLengthInRow(rowIndex);
        }

    }

    public void extendColouredFieldsToLeftNearXToMinimumPossibleLengthInRow(int rowIdx) {

        List<String> nonogramSolutionBoardRow = this.getNonogramSolutionBoard().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);

        List<Integer> possibleSequenceLengths;
        int minimumPossibleLength;
        List<Integer> firstColouredSubsequence;
        int sequenceStartIndex;
        int distanceToX;

        int nonogramWidth = this.getWidth();

        int firstColouredFieldIndexInSubsequence;

        for(int columnIdx = 0; columnIdx < nonogramWidth; columnIdx++) {

            if(nonogramSolutionBoardRow.get(columnIdx).equals("O")) {
                firstColouredSubsequence = new ArrayList<>();

                firstColouredFieldIndexInSubsequence = columnIdx;
                while(firstColouredFieldIndexInSubsequence >= 0 &&
                        nonogramSolutionBoardRow.get(firstColouredFieldIndexInSubsequence).equals("O")) {
                    firstColouredFieldIndexInSubsequence--;
                }
                firstColouredFieldIndexInSubsequence++;

                // create range from coloured subsequence
                firstColouredSubsequence.add(firstColouredFieldIndexInSubsequence);
                firstColouredSubsequence.add(columnIdx);

                possibleSequenceLengths = filterSequencesRangesIncludingAnotherAndReturnCorrespondingLengths (
                        rowSequencesRanges, firstColouredSubsequence, rowSequencesLengths);

                if(possibleSequenceLengths.size() == 0) {
                    this.setSolutionInvalid(true);
                    break;
                }

                minimumPossibleLength = Collections.min(possibleSequenceLengths);

                sequenceStartIndex = columnIdx;

                distanceToX = 0;

                for(int columnsToX = 1; columnsToX < minimumPossibleLength && columnIdx + columnsToX < nonogramWidth; columnsToX++) {
                    if(nonogramSolutionBoardRow.get(columnIdx + columnsToX).equals("X")) {
                        distanceToX = columnsToX;
                        break;
                    }
                }

                int extendedSequenceEndIndex = sequenceStartIndex + distanceToX - minimumPossibleLength;

                if(distanceToX != 0) {
                    for(int colourColumnIdx = sequenceStartIndex;
                        colourColumnIdx >= 0 /*tmp cond*/
                                && colourColumnIdx >= extendedSequenceEndIndex;
                        colourColumnIdx--) {
                        if(this.getNonogramSolutionBoard().get(rowIdx).get(colourColumnIdx).equals("-")) {

                            this.increaseStepsMade();
                            this.colourFieldAtGivenPosittion(rowIdx, colourColumnIdx);

                            tmpLog = "O placed, rowIdx: " + rowIdx + " , columnIdx: " + colourColumnIdx + " (extend coloured fields in sequence to left near X to minimum available length in row).";
                            this.logs.add(tmpLog);

                            //2
                            this.getAffectedColumnsToMarkAvailableSequences().add(colourColumnIdx);
                            //8
                            this.getAffectedColumnsToCorrectSequencesRanges().add(colourColumnIdx);
                            //9
                            this.getAffectedColumnsToCorrectSequencesRanges().add(colourColumnIdx);
                            //10
                            this.getAffectedColumnsToPlaceXsAroundLongestSequences().add(colourColumnIdx);
                            //14
                            this.getAffectedColumnsToExtendColouredFieldsNearX().add(colourColumnIdx);
                        } else if(showRepetitions) {
                            System.out.println("Row field was coloured earlier (extending to minimum required - to left).");
                        }
                    }
                }
            }

        }
    }

    public void extendColouredFieldsToRightNearXToMinimumPossibleLengthInRow(int rowIdx) {

        List<String> nonogramSolutionBoardRow = this.getNonogramSolutionBoard().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);

        List<Integer> possibleSequenceLengths;
        int minimumPossibleLength;
        List<Integer> lastColouredSubsequence;
        int sequenceStartIndex;
        int distanceFromX;

        int nonogramWidth = this.getWidth();

        int lastColouredFieldInRange;

        for(int columnIdx = nonogramWidth - 1; columnIdx >= 0; columnIdx--) {

            if(nonogramSolutionBoardRow.get(columnIdx).equals("O")) {
                lastColouredSubsequence = new ArrayList<>();

                lastColouredFieldInRange = columnIdx;
                while(lastColouredFieldInRange < nonogramWidth &&
                        nonogramSolutionBoardRow.get(lastColouredFieldInRange).equals("O")) {
                    lastColouredFieldInRange++;
                }
                lastColouredFieldInRange--;

                // create range from coloured subsequence
                lastColouredSubsequence.add(columnIdx);
                lastColouredSubsequence.add(lastColouredFieldInRange);

                possibleSequenceLengths = filterSequencesRangesIncludingAnotherAndReturnCorrespondingLengths (
                        rowSequencesRanges, lastColouredSubsequence, rowSequencesLengths);

                if(possibleSequenceLengths.size() == 0) {
                    this.setSolutionInvalid(true);
                    break;
                }

                minimumPossibleLength = Collections.min(possibleSequenceLengths);

                sequenceStartIndex = columnIdx;

                distanceFromX = 0;

                for(int columnsFromX = 1; columnsFromX < minimumPossibleLength && columnIdx - columnsFromX >= 0; columnsFromX++) {
                    if(nonogramSolutionBoardRow.get(columnIdx - columnsFromX).equals("X")) {
                        distanceFromX = columnsFromX;
                        break;
                    }
                }

                if(distanceFromX != 0) {
                    for(int colourColumnIdx = sequenceStartIndex;
                        colourColumnIdx < this.getWidth() /*tmp cond*/ && colourColumnIdx <= sequenceStartIndex - distanceFromX + minimumPossibleLength;
                        colourColumnIdx++) {
                        if(this.getNonogramSolutionBoard().get(rowIdx).get(colourColumnIdx).equals("-")) {
                            this.increaseStepsMade();
                            this.colourFieldAtGivenPosittion(rowIdx, colourColumnIdx);

                            tmpLog = "O placed, rowIdx: " + rowIdx + " , columnIdx: " + colourColumnIdx + " (extend coloured fields in sequence to right near X to minimum available length in row).";
                            this.logs.add(tmpLog);


                            //2
                            this.getAffectedColumnsToMarkAvailableSequences().add(colourColumnIdx);
                            //8
                            this.getAffectedColumnsToCorrectSequencesRanges().add(colourColumnIdx);
                            //9
                            this.getAffectedColumnsToCorrectSequencesRangesWhenMetColouredField().add(colourColumnIdx);
                            //10
                            this.getAffectedColumnsToPlaceXsAroundLongestSequences().add(colourColumnIdx);
                            //14
                            this.getAffectedColumnsToExtendColouredFieldsNearX().add(colourColumnIdx);

                        } else if (showRepetitions) {
                            System.out.println("Row field was coloured earlier (extending to minimum required - to right).");
                        }
                    }
                }
            }

        }
    }

    public void extendColouredFieldsNearXToMinimumPossibleLengthInColumns() {
        for (Integer columnIndex : this.getAffectedColumnsToExtendColouredFieldsNearX()) {
            extendColouredFieldsToTopNearXToMinimumPossibleLengthInColumn(columnIndex);
            extendColouredFieldsToBottomNearXToMinimumPossibleLengthInColumn(columnIndex);
        }
    }

    public void extendColouredFieldsToTopNearXToMinimumPossibleLengthInColumn(int columnIdx) {


        List<String> nonogramSolutionBoardColumn = new ArrayList<>();
        int nonogramHeight = this.getHeight();

        // save column to list (from top to bottom) - OK
        for(int rowIdx = 0; rowIdx < nonogramHeight; rowIdx++) {
            nonogramSolutionBoardColumn.add(this.getNonogramSolutionBoard().get(rowIdx).get(columnIdx));
        }

        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);

        List<Integer> possibleSequenceLengths;
        int minimumPossibleLength;
        List<Integer> firstColouredSubsequence;
        int sequenceStartIndex;
        int distanceToX;

        int firstColouredFieldInRange;

        for(int rowIdx = 0; rowIdx < nonogramHeight; rowIdx++) {

            if(nonogramSolutionBoardColumn.get(rowIdx).equals("O")) {

                firstColouredSubsequence = new ArrayList<>();

                firstColouredFieldInRange = rowIdx;
                while(firstColouredFieldInRange >= 0 && nonogramSolutionBoardColumn.get(firstColouredFieldInRange).equals("O")) {
                    firstColouredFieldInRange--;
                }
                firstColouredFieldInRange++; // back to last coloured field (after while index is on "-" (or "X" ???) field

                firstColouredSubsequence.add(firstColouredFieldInRange);
                firstColouredSubsequence.add(rowIdx);

                possibleSequenceLengths = filterSequencesRangesIncludingAnotherAndReturnCorrespondingLengths (
                        columnSequencesRanges, firstColouredSubsequence, columnSequencesLengths);

                if((possibleSequenceLengths.size() == 0)) {
                    this.setSolutionInvalid(true);
                    break;
                }

                minimumPossibleLength = Collections.min(possibleSequenceLengths);

                sequenceStartIndex = rowIdx;

                distanceToX = 0;

                for(int rowToX = 1; rowIdx + rowToX < nonogramHeight && rowToX < minimumPossibleLength; rowToX++) {
                    if(nonogramSolutionBoardColumn.get(rowIdx + rowToX).equals("X")) {
                        distanceToX = rowToX;
                        break;
                    }
                }

                if(distanceToX != 0) {
                    int sequenceEndIndex = sequenceStartIndex + distanceToX - minimumPossibleLength;
                    for(int colourRowIdx = sequenceStartIndex; colourRowIdx >= sequenceEndIndex; colourRowIdx--) {
                        try {
                            if(this.getNonogramSolutionBoard().get(colourRowIdx).get(columnIdx).equals("-")) {
                                this.colourFieldAtGivenPosittion(colourRowIdx, columnIdx);

                                tmpLog = "O placed, columnIdx: " + columnIdx + " , rowIdx: " + colourRowIdx + " (extend coloured fields in sequence to top near X to minimum available length in column).";
                                this.logs.add(tmpLog);

                                //2
                                this.getAffectedColumnsToMarkAvailableSequences().add(columnIdx);
                                //4
                                this.getAffectedRowsToMarkAvailableSequences().add(colourRowIdx);
                                //6
                                this.getAffectedRowsToCorrectSequencesRangesWhenMetColouredField().add(colourRowIdx);
                                //13
                                this.getAffectedRowsToPlaceXsAroundLongestSequences().add(colourRowIdx);
                                //17
                                this.getAffectedRowsToExtendColouredFieldsNearX().add(colourRowIdx);

                                this.increaseStepsMade();
                            } else if(showRepetitions) {
                                System.out.println("Column field was coloured earlier (extending to minimum required - to top).");
                            }
                        } catch (IndexOutOfBoundsException e) {
                            this.setSolutionInvalid(true);
                        }
                    }
                }
            }

        }

    }

    //from to to bottom
    /*  [3, 3]
        X    X
        -    -
        O -> O
        -    O  , etc
     */
    public void extendColouredFieldsToBottomNearXToMinimumPossibleLengthInColumn(int columnIdx) {

        List<String> nonogramSolutionBoardColumn = new ArrayList<>();
        int nonogramHeight = this.getHeight();

        for(int rowIdx = 0; rowIdx < nonogramHeight; rowIdx++) {
            nonogramSolutionBoardColumn.add(this.getNonogramSolutionBoard().get(rowIdx).get(columnIdx));
        }

        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);

        List<Integer> possibleSequenceLengths;
        int minimumPossibleLength;
        List<Integer> firstColouredSubsequence;
        int sequenceStartIndex;
        int distanceFromX;

        int lastColouredFieldInRange;

        for(int rowIdx = nonogramHeight - 1; rowIdx >= 0; rowIdx--) {

            if(nonogramSolutionBoardColumn.get(rowIdx).equals("O")) {
                firstColouredSubsequence = new ArrayList<>();

                lastColouredFieldInRange = rowIdx;
                while(lastColouredFieldInRange < nonogramHeight &&
                        nonogramSolutionBoardColumn.get(lastColouredFieldInRange).equals("O")) {
                    lastColouredFieldInRange++;
                }
                lastColouredFieldInRange--;


                // create range from coloured subsequence
                firstColouredSubsequence.add(rowIdx);
                firstColouredSubsequence.add(lastColouredFieldInRange);

                possibleSequenceLengths = filterSequencesRangesIncludingAnotherAndReturnCorrespondingLengths (
                        columnSequencesRanges, firstColouredSubsequence, columnSequencesLengths);

                if(possibleSequenceLengths.size() == 0) {
                    this.setSolutionInvalid(true);
                    break;
                }

                minimumPossibleLength = Collections.min(possibleSequenceLengths);

                sequenceStartIndex = rowIdx;

                distanceFromX = 0;

                for(int rowsFromX = 1; rowIdx - rowsFromX > -1 && rowsFromX < minimumPossibleLength; rowsFromX++) {
                    if(nonogramSolutionBoardColumn.get(rowIdx - rowsFromX).equals("X")) {
                        distanceFromX = rowsFromX;
                        break;
                    }
                }

                if(distanceFromX != 0) {
                    for(int colourRowIdx = sequenceStartIndex;
                        colourRowIdx <= sequenceStartIndex - distanceFromX + minimumPossibleLength; colourRowIdx++) {

                        try {
                            if(this.getNonogramSolutionBoard().get(colourRowIdx).get(columnIdx).equals("-")) {
                                this.colourFieldAtGivenPosittion(colourRowIdx, columnIdx);

                                tmpLog = "O placed, colIdx: " + columnIdx + " , rowIdx: " + colourRowIdx + " (extend coloured fields in sequence to bottom near X to minimum available length in column). Ranges: " + columnSequencesRanges;
                                this.logs.add(tmpLog);

                                //2
                                this.getAffectedColumnsToMarkAvailableSequences().add(columnIdx);
                                //4
                                this.getAffectedRowsToMarkAvailableSequences().add(colourRowIdx);
                                //6
                                this.getAffectedRowsToCorrectSequencesRangesWhenMetColouredField().add(colourRowIdx);
                                //13
                                this.getAffectedRowsToPlaceXsAroundLongestSequences().add(colourRowIdx);
                                //17
                                this.getAffectedRowsToExtendColouredFieldsNearX().add(colourRowIdx);

                                this.increaseStepsMade();
                            } else if(showRepetitions) {
                                System.out.println("Column field was coloured earlier (extending to minimum required - to bottom).");
                            }
                        } catch (IndexOutOfBoundsException e) {
                            this.setSolutionInvalid(true);
                        }

                    }
                }
            }

        }

    }

    // mark iterations through all rows
    public void markAvailableSequencesInRows() {
        for (Integer rowIndex : this.getAffectedRowsToMarkAvailableSequences()) {
            markAvailableSequencesInRow(rowIndex);
        }
    }

    public void markAvailableSequencesInRow(int rowIdx) {

        List<String> boardRow = this.getNonogramSolutionBoard().get(rowIdx);
        List<Integer> colouredSequenceIndexes;
        int firstSequenceIndex;
        int lastSequenceIndex;
        List<Integer> rowSequences = this.getRowsSequences().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequenceRange;
        int matchingSequencesCount;
        int lastMatchingSequenceIndex = -1;
        int colouredSequenceLength;
        int matchingSequenceLength;
        boolean sequenceEqualsRowSequenceLength;
        String sequenceMarker;

        int oldRangeBeginIndex;
        int oldRangeEndIndex;
        int updatedRangeBeginIndex;
        int updatedRangeEndIndex;

        for(int columnIdx = 0; columnIdx < boardRow.size(); columnIdx++) {

            if(boardRow.get(columnIdx).equals("O")) {

                colouredSequenceIndexes = new ArrayList<>();
                colouredSequenceIndexes.add(columnIdx);

                //collect indexes of current coloured sequence
                while(columnIdx < boardRow.size() && boardRow.get(columnIdx).equals("O")) {
                    columnIdx++;
                }

                colouredSequenceIndexes.add(columnIdx - 1);

                firstSequenceIndex = colouredSequenceIndexes.get(0);
                lastSequenceIndex = colouredSequenceIndexes.get(colouredSequenceIndexes.size() - 1);
                colouredSequenceLength = lastSequenceIndex - firstSequenceIndex  + 1;

                matchingSequencesCount = 0;

                //check how many sequences matching conditions for mark
                for(int seqNo = 0; seqNo < rowSequences.size(); seqNo++) {

                    rowSequenceRange = rowSequencesRanges.get(seqNo);

                    if( rangeInsideAnotherRange(colouredSequenceIndexes, rowSequenceRange)
                            && colouredSequenceLength <= rowSequences.get(seqNo)) {
                        matchingSequencesCount++;
                        lastMatchingSequenceIndex = seqNo;
                    }
                }

                //NOTE!!! if matching count == 0 and there are sequences that are not fulfilled -> wrong solution
                if(matchingSequencesCount == 1) {

                    matchingSequenceLength = rowSequences.get(lastMatchingSequenceIndex);
                    sequenceEqualsRowSequenceLength = colouredSequenceLength == matchingSequenceLength;
                    rowSequenceRange = new ArrayList<>();

                    if(sequenceEqualsRowSequenceLength) {
                        rowSequenceRange.add(firstSequenceIndex);
                        rowSequenceRange.add(lastSequenceIndex);
                        this.addRowSequenceIdxToNotToInclude(rowIdx, lastMatchingSequenceIndex);
                    } else {
                        oldRangeBeginIndex = this.getRowsSequencesRanges().get(rowIdx).get(lastMatchingSequenceIndex).get(0);
                        oldRangeEndIndex = this.getRowsSequencesRanges().get(rowIdx).get(lastMatchingSequenceIndex).get(1);

                        updatedRangeBeginIndex = Math.max(0,
                                this.minimumColumnIndexWithoutX(rowIdx, lastSequenceIndex, matchingSequenceLength));
                        updatedRangeEndIndex = Math.min(this.getWidth() - 1,
                                this.maximumColumnIndexWithoutX(rowIdx, firstSequenceIndex, matchingSequenceLength));

                        rowSequenceRange.add( Math.max(oldRangeBeginIndex ,updatedRangeBeginIndex) );
                        rowSequenceRange.add( Math.min(oldRangeEndIndex, updatedRangeEndIndex) );
                    }



                    this.updateRowSequenceRange(rowIdx, lastMatchingSequenceIndex, rowSequenceRange);

                    //1
                    this.getAffectedRowsToFillOverlappingFields().add(rowIdx);
                    //5
                    this.getAffectedRowsToCorrectSequencesRanges().add(rowIdx);
                    //11
                    this.getAffectedRowsToPlaceXsAtUnreachableFields().add(rowIdx);
                    //13
                    this.getAffectedRowsToPlaceXsAroundLongestSequences().add(rowIdx);
                    //15
                    this.getAffectedRowsToPlaceXsAtTooShortEmptySequences().add(rowIdx);
                    //17
                    this.getAffectedRowsToExtendColouredFieldsNearX().add(rowIdx);
                    //mark sequence part
                    sequenceMarker = NonogramLogic.indexToSequenceCharMark(lastMatchingSequenceIndex);
                    for(int sequenceColumnIdx = firstSequenceIndex; sequenceColumnIdx <= lastSequenceIndex; sequenceColumnIdx++) {
                        if(this.getNonogramSolutionBoardWithMarks().get(rowIdx).get(sequenceColumnIdx).startsWith("--")) {
                            this.markRowBoardField(rowIdx, sequenceColumnIdx, sequenceMarker);

                            this.increaseStepsMade();
                        } else if(showRepetitions) {
                            System.out.println("Row field was marked earlier.");
                        }
                    }
                }
            }
        }
    }

    //2
    public void markAvailableSequencesInColumns() {
        for (Integer columnIndex : this.getAffectedColumnsToMarkAvailableSequences()) {
            markAvailableSequencesInColumn(columnIndex);
        }
    }

    //2
    public void markAvailableSequencesInColumn(int columnIdx) {

        List<List<String>> nonogramSolutionBoardWithMarks = this.getNonogramSolutionBoardWithMarks();
        List<List<String>> nonogramSolutionBoard = this.getNonogramSolutionBoard();
        List<Integer> colouredSequenceIndexes;
        int firstSequenceIndex;
        int lastSequenceIndex;
        List<Integer> columnSequences = this.getColumnsSequences().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequenceRange;
        List<Integer> updatedColumnSequenceRange;
        int matchingSequencesCount;
        int lastMatchingSequenceIndex = -1;
        int colouredSequenceLength;
        int matchingSequenceLength;
        boolean sequenceEqualsColumnSequenceLength;
        String sequenceMarker;

        int oldRangeBeginIndex;
        int oldRangeEndIndex;
        int updatedRangeBeginIndex;
        int updatedRangeEndIndex;

        for(int rowIdx = 0; rowIdx < nonogramSolutionBoardWithMarks.size(); rowIdx++) {

            //TODO - exclude fieldsNotToInclude

            if(nonogramSolutionBoard.get(rowIdx).get(columnIdx).equals("O")) {

                colouredSequenceIndexes = new ArrayList<>();

                colouredSequenceIndexes.add(rowIdx);
                //collect indexes of current coloured sequence
                while(rowIdx < nonogramSolutionBoard.size() && nonogramSolutionBoard.get(rowIdx).get(columnIdx).equals("O")) {
                    rowIdx++;
                }

                colouredSequenceIndexes.add(rowIdx - 1);

                firstSequenceIndex = colouredSequenceIndexes.get(0);
                lastSequenceIndex = colouredSequenceIndexes.get(colouredSequenceIndexes.size() - 1);
                matchingSequencesCount = 0;
                colouredSequenceLength = lastSequenceIndex - firstSequenceIndex  + 1;

                /*check how many sequences matching conditions for mark:
                    a)coloured sequence range inside sequenceRange(seqNo)
                    b)coloured sequence range length less or equal than sequenceRange(seqNo)
                 */
                for(int seqNo = 0; seqNo < columnSequences.size(); seqNo++) {
                    columnSequenceRange = columnSequencesRanges.get(seqNo);

                    if( rangeInsideAnotherRange(colouredSequenceIndexes, columnSequenceRange)
                            && colouredSequenceLength <= columnSequences.get(seqNo)) {
                        matchingSequencesCount++;
                        lastMatchingSequenceIndex = seqNo;
                    }
                }

                //NOTE!!! if matching count == 0 and there are sequences that are not fulfilled -> wrong solution
                //marking possible only if matching sequence count is 1
                if(matchingSequencesCount == 1) {

                    //3
                    this.getAffectedColumnsToFillOverlappingFields().add(columnIdx);
                    //8
                    this.getAffectedColumnsToCorrectSequencesRanges().add(columnIdx);
                    //12
                    this.getAffectedColumnsToPlaceXsAtUnreachableFields().add(columnIdx);
                    //14
                    this.getAffectedColumnsToPlaceXsAroundLongestSequences().add(columnIdx);
                    //16
                    this.getAffectedColumnsToPlaceXsAtTooShortEmptySequences().add(columnIdx);
                    //18
                    this.getAffectedColumnsToExtendColouredFieldsNearX().add(columnIdx);

                    matchingSequenceLength = columnSequences.get(lastMatchingSequenceIndex);
                    sequenceEqualsColumnSequenceLength = colouredSequenceLength == matchingSequenceLength;

                    if(sequenceEqualsColumnSequenceLength &&
                            (!columnSequencesRanges.get(lastMatchingSequenceIndex).get(0).equals(colouredSequenceIndexes.get(0)) ||
                                    !columnSequencesRanges.get(lastMatchingSequenceIndex).get(1).equals(colouredSequenceIndexes.get(1)))) {
                        tmpLog = "Updated column sequence range when marking coloured field(whole sequence) (columnIdx="+ columnIdx + "), seqNo: "+ lastMatchingSequenceIndex +
                                ", from " + columnSequencesRanges.get(lastMatchingSequenceIndex) + " to " + colouredSequenceIndexes;
                        this.logs.add(tmpLog);
                        this.updateColumnSequenceRange(columnIdx, lastMatchingSequenceIndex, colouredSequenceIndexes);
                    } else {
                        //current range bounds
                        oldRangeBeginIndex = this.getColumnsSequencesRanges().get(columnIdx).get(lastMatchingSequenceIndex).get(0);
                        oldRangeEndIndex = this.getColumnsSequencesRanges().get(columnIdx).get(lastMatchingSequenceIndex).get(1);

                        //updated marked sequence range basing on lastSequenceIndex(searching first 'X' going to top)
                        updatedRangeBeginIndex = Math.max(oldRangeBeginIndex,
                                this.minimumRowIndexWithoutX(columnIdx, lastSequenceIndex, matchingSequenceLength));

                        //updated marked sequence range basing on firstSequenceIndex(searching first 'X' going to bottom)
                        updatedRangeEndIndex = Math.min(oldRangeEndIndex,
                                this.maximumRowIndexWithoutX(columnIdx, firstSequenceIndex, matchingSequenceLength));

                        updatedColumnSequenceRange = new ArrayList<>();

                        updatedColumnSequenceRange.add( updatedRangeBeginIndex );
                        updatedColumnSequenceRange.add( updatedRangeEndIndex );

                        if(!columnSequencesRanges.get(lastMatchingSequenceIndex).get(0).equals(updatedColumnSequenceRange.get(0)) ||
                                !columnSequencesRanges.get(lastMatchingSequenceIndex).get(1).equals(updatedColumnSequenceRange.get(1))) {
                            tmpLog = "Updated column sequence range when marking coloured field (columnIdx="+ columnIdx + "), seqNo: "+ lastMatchingSequenceIndex +
                                    ", from " + columnSequencesRanges.get(lastMatchingSequenceIndex) + " to " + updatedColumnSequenceRange;
                            this.logs.add(tmpLog);
                            //lastMatching is here only one that matching (from if cond earlier)
                            this.updateColumnSequenceRange(columnIdx, lastMatchingSequenceIndex, updatedColumnSequenceRange);
                        }
                    }


                    //mark sequence part
                    sequenceMarker = NonogramLogic.indexToSequenceCharMark(lastMatchingSequenceIndex);

                    for(int sequenceRowIdx = firstSequenceIndex; sequenceRowIdx <= lastSequenceIndex; sequenceRowIdx++) {
                        if(this.getNonogramSolutionBoardWithMarks().get(sequenceRowIdx).get(columnIdx).substring(2).equals("--")) {
                            tmpLog = "Sequence marked in column (seqIdx=" + lastMatchingSequenceIndex + ", mark="
                                    + sequenceMarker + "), column: " + columnIdx + " , row: " + sequenceRowIdx;
                            this.logs.add(tmpLog);
                            this.markColumnBoardField(sequenceRowIdx, columnIdx, sequenceMarker);
                            this.increaseStepsMade();
                        } else if (showRepetitions) {
                            System.out.println("Column field was marked earlier.");
                        }
                    }
                }
            }
        }

    }

    // iterations through all rows
    public void placeXsRowsAroundLongestSequences() {
        for (Integer rowIndex : this.getAffectedRowsToPlaceXsAroundLongestSequences()) {
            placeXsRowAroundLongestSequences(rowIndex);
        }
    }

    public void placeXsRowAroundLongestSequences(int rowIdx) {

        List<String> nonogramSolutionBoardRow = this.getNonogramSolutionBoard().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);
        int nonogramWidth = this.getWidth();

        List<Integer> colouredSequenceRange;
        List<Integer> rowSequenceRange;
        int sequenceOnBoardLength;

        List<Integer> rowSequencesIndexesIncludingSequenceRange;
        List<Integer> rowSequencesLengthsIncludingSequenceRange;

        int firstXIndex;
        int lastXIndex;

        int rowSequenceIdxNotToInclude;

        for(int columnIdx = 0; columnIdx < nonogramWidth; columnIdx++) {

            if(nonogramSolutionBoardRow.get(columnIdx).equals("O")) {

                colouredSequenceRange = new ArrayList<>();
                colouredSequenceRange.add(columnIdx);

                while(columnIdx < nonogramWidth && nonogramSolutionBoardRow.get(columnIdx).equals("O")) {
                    columnIdx++;
                }

                //when solutionBoard[rowIdx][columnIdx] != "O"
                colouredSequenceRange.add(columnIdx - 1);
                sequenceOnBoardLength = rangeLength(colouredSequenceRange);
                rowSequencesIndexesIncludingSequenceRange = new ArrayList<>();
                rowSequencesLengthsIncludingSequenceRange = new ArrayList<>();

                for(int seqNo = 0; seqNo < rowSequencesRanges.size(); seqNo++) {
                    rowSequenceRange = rowSequencesRanges.get(seqNo);
                    if(rangeInsideAnotherRange(colouredSequenceRange, rowSequenceRange)) {
                        rowSequencesIndexesIncludingSequenceRange.add(seqNo);
                        rowSequencesLengthsIncludingSequenceRange.add(rowSequencesLengths.get(seqNo));
                    }
                }

                firstXIndex = colouredSequenceRange.get(0) - 1;
                lastXIndex = colouredSequenceRange.get(1) + 1;

                if(rowSequencesIndexesIncludingSequenceRange.size() >= 1) {
                    if(rowSequencesIndexesIncludingSequenceRange.size() == 1 ) {

                        rowSequenceIdxNotToInclude = rowSequencesIndexesIncludingSequenceRange.get(0);

                        if(sequenceOnBoardLength == rowSequencesLengthsIncludingSequenceRange.get(0)) {

                            if(!this.getRowsSequencesIdsNotToInclude().get(rowIdx).contains(rowSequenceIdxNotToInclude)) {

                                this.addRowSequenceIdxToNotToInclude(rowIdx, rowSequenceIdxNotToInclude);

                                if(firstXIndex > -1) {
                                    if (this.getNonogramSolutionBoard().get(rowIdx).get(firstXIndex).equals("-")) {

                                        tmpLog = "X placed, columnIdx: " + columnIdx + " rowIdx: " + firstXIndex + " (Placing 'X' before sequence in row - only possible sequence).";
                                        this.logs.add(tmpLog);

                                        this.increaseStepsMade().placeXAtGivenPosittion(rowIdx, firstXIndex)
                                                .addRowFieldToNotToInclude(rowIdx, firstXIndex)
                                                .addColumnFieldToNotToInclude(firstXIndex, rowIdx);
                                    } else if (showRepetitions) {
                                        System.out.println("Placed Xs before longest sequence in row earlier!");
                                    }
                                }
                                if(lastXIndex < this.getWidth() ) {
                                    if(this.getNonogramSolutionBoard().get(rowIdx).get(lastXIndex).equals("-")) {

                                        tmpLog = "X placed, rowIdx: " + rowIdx + " colIdx: " + lastXIndex + " (Placing 'X' after sequence in row - only possible sequence).";
                                        this.logs.add(tmpLog);

                                        this.increaseStepsMade().placeXAtGivenPosittion(rowIdx, lastXIndex)
                                                .addRowFieldToNotToInclude(rowIdx, lastXIndex)
                                                .addColumnFieldToNotToInclude(lastXIndex, rowIdx);
                                    }

                                } else if(showRepetitions) {
                                    System.out.println("Placed Xs after longest sequence in row earlier!");
                                }
                            }

                            for(int sequenceColumnIdx = firstXIndex + 1; sequenceColumnIdx < lastXIndex; sequenceColumnIdx++) {
                                this.addRowFieldToNotToInclude(rowIdx, sequenceColumnIdx);
                            }

                            this.changeRowSequenceRange(rowIdx, rowSequencesIndexesIncludingSequenceRange.get(0),
                                    colouredSequenceRange.get(0), colouredSequenceRange.get(1));
                        }

                    } else if(rowSequencesLengthsIncludingSequenceRange.size() > 1) {

                        //check if length of sequence == Max(foundSequences_lengths)
                        if(sequenceOnBoardLength == Collections.max(rowSequencesLengthsIncludingSequenceRange)) {

                            if(this.getNonogramSolutionBoard().get(rowIdx).get(firstXIndex).equals("-")) {
                                this.increaseStepsMade().placeXAtGivenPosittion(rowIdx, firstXIndex)
                                        .addRowFieldToNotToInclude(rowIdx, firstXIndex)
                                        .addColumnFieldToNotToInclude(firstXIndex, rowIdx);

                                tmpLog = "X placed, rowIdx: " + rowIdx + " colIdx: " + firstXIndex + " (Placing 'X' before longest sequence in row - sequence index not specified).";
                                this.logs.add(tmpLog);

                                //10
                                this.getAffectedColumnsToCorrectSequencesRangesIfXOnWay().add(firstXIndex);
                                //16
                                this.getAffectedColumnsToPlaceXsAtTooShortEmptySequences().add(firstXIndex);
                                //18
                                this.getAffectedColumnsToExtendColouredFieldsNearX().add(firstXIndex);

                            } else if(showRepetitions) {
                                System.out.println("Longest sequence in row firstXIndex added earlier!");
                            }

                            if(this.getNonogramSolutionBoard().get(rowIdx).get(lastXIndex).equals("-")) {
                                this.increaseStepsMade().placeXAtGivenPosittion(rowIdx, lastXIndex)
                                        .addRowFieldToNotToInclude(rowIdx, lastXIndex)
                                        .addColumnFieldToNotToInclude(lastXIndex, rowIdx);

                                tmpLog = "X placed, rowIdx: " + rowIdx + " colIdx: " + lastXIndex + " (Placing 'X' after longest sequence in row - sequence index not specified).";
                                this.logs.add(tmpLog);

                                //10
                                this.getAffectedColumnsToCorrectSequencesRangesIfXOnWay().add(lastXIndex);
                                //16
                                this.getAffectedColumnsToPlaceXsAtTooShortEmptySequences().add(lastXIndex);
                                //18
                                this.getAffectedColumnsToExtendColouredFieldsNearX().add(lastXIndex);
                            } else if(showRepetitions) {
                                System.out.println("Longest sequence in row lastXIndex added earlier!");
                            }
                        }
                    }
                }
            }
        }
    }

    // iterations through all columns
    public void placeXsColumnsAroundLongestSequences() {

        for (Integer columnIndex : this.getAffectedColumnsToPlaceXsAroundLongestSequences()) {
            placeXsColumnAroundLongestSequences(columnIndex);
        }
    }

    public void placeXsColumnAroundLongestSequences(int columnIdx) {

        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);

        int nonogramHeight = this.getHeight();

        List<Integer> colouredSequenceRange;
        List<Integer> columnSequenceRange;
        int sequenceLength;

        List<Integer> columnSequencesIndexesIncludingSequenceRange;
        List<Integer> columnSequencesLengthsIncludingSequenceRange;

        int firstXIndex;
        int lastXIndex;

        int columnSequenceIdxNotToInclude;

        List<Integer> oldSequenceRange;
        List<Integer> updatedSequenceRange;

        for(int rowIdx = 0; rowIdx < nonogramHeight; rowIdx++) {

            if(this.getNonogramSolutionBoard().get(rowIdx).get(columnIdx).equals("O")) {

                colouredSequenceRange = new ArrayList<>();
                colouredSequenceRange.add(rowIdx);

                while(rowIdx < nonogramHeight && this.getNonogramSolutionBoard().get(rowIdx).get(columnIdx).equals("O")) {
                    rowIdx++;
                }

                //when solutionBoard[rowIdx][columnIdx] != "O"
                colouredSequenceRange.add(rowIdx - 1);
                sequenceLength = rangeLength(colouredSequenceRange);
                columnSequencesIndexesIncludingSequenceRange = new ArrayList<>();
                columnSequencesLengthsIncludingSequenceRange = new ArrayList<>();

                for(int seqNo = 0; seqNo < columnSequencesRanges.size(); seqNo++) {
                    columnSequenceRange = columnSequencesRanges.get(seqNo);
                    if(rangeInsideAnotherRange(colouredSequenceRange, columnSequenceRange)) {
                        columnSequencesIndexesIncludingSequenceRange.add(seqNo);
                        columnSequencesLengthsIncludingSequenceRange.add(columnSequencesLengths.get(seqNo));
                    }
                }

                firstXIndex = colouredSequenceRange.get(0) - 1;
                lastXIndex = colouredSequenceRange.get(1) + 1;

                if(columnSequencesIndexesIncludingSequenceRange.size() >= 1) {
                    if(columnSequencesIndexesIncludingSequenceRange.size() == 1 ) {

                        columnSequenceIdxNotToInclude = columnSequencesIndexesIncludingSequenceRange.get(0);

                        if(sequenceLength == columnSequencesLengthsIncludingSequenceRange.get(0)) {

                            if(!this.getColumnsSequencesIdsNotToInclude().get(columnIdx).contains(columnSequenceIdxNotToInclude)) {

                                this.addColumnSequenceIdxToNotToInclude(columnIdx, columnSequenceIdxNotToInclude);

                                if(firstXIndex > -1) {
                                    if (this.getNonogramSolutionBoard().get(firstXIndex).get(columnIdx).equals("-")) {

                                        tmpLog = "X placed, columnIdx: " + columnIdx + " rowIdx: " + firstXIndex + " (Placing 'X' before sequence in column - only possible sequence).";
                                        this.logs.add(tmpLog);

                                        this.increaseStepsMade().placeXAtGivenPosittion(firstXIndex, columnIdx)
                                                .addRowFieldToNotToInclude(firstXIndex, columnIdx)
                                                .addColumnFieldToNotToInclude(columnIdx, firstXIndex);
                                    } else if(showRepetitions) {
                                        System.out.println("Longest sequence in column firstXIndex added earlier!");
                                    }

                                    oldSequenceRange = columnSequencesRanges.get(columnSequenceIdxNotToInclude);

                                    updatedSequenceRange = new ArrayList<>();
                                    updatedSequenceRange.add(firstXIndex + 1);
                                    updatedSequenceRange.add(oldSequenceRange.get(1));
                                    this.changeColumnSequenceRange(columnIdx, columnSequenceIdxNotToInclude,
                                            updatedSequenceRange.get(0), updatedSequenceRange.get(1));
                                }

                                if (lastXIndex < this.getHeight()) {
                                    if( this.getNonogramSolutionBoard().get(lastXIndex).get(columnIdx).equals("-")) {

                                        tmpLog = "X placed, columnIdx: " + columnIdx + " rowIdx: " + lastXIndex + " (Placing 'X' after longest sequence in column).";
                                        this.logs.add(tmpLog);

                                        this.increaseStepsMade().placeXAtGivenPosittion(lastXIndex, columnIdx)
                                                .addColumnFieldToNotToInclude(columnIdx, lastXIndex)
                                                .addRowFieldToNotToInclude(lastXIndex, columnIdx);
                                        oldSequenceRange = columnSequencesRanges.get(columnSequenceIdxNotToInclude);

                                        updatedSequenceRange = new ArrayList<>();
                                        updatedSequenceRange.add(oldSequenceRange.get(0));
                                        updatedSequenceRange.add(lastXIndex - 1);

                                        this.changeColumnSequenceRange(columnIdx, columnSequenceIdxNotToInclude,
                                                updatedSequenceRange.get(0), updatedSequenceRange.get(1));

                                    } else if(showRepetitions) {
                                        System.out.println("Longest sequence in column lastXIndex added earlier!");
                                    }
                                }

                                for(int sequenceRowIdx = firstXIndex + 1; sequenceRowIdx < lastXIndex; sequenceRowIdx++) {
                                    if (!this.getColumnsFieldsNotToInclude().get(columnIdx).contains(sequenceRowIdx)) {
                                        this.increaseStepsMade().addColumnFieldToNotToInclude(columnIdx, sequenceRowIdx);
                                    } else if(showRepetitions) {
                                        System.out.println("Field not to include in column has been inserted earlier");
                                    }
                                }
                            }

                        }

                    } else if(columnSequencesIndexesIncludingSequenceRange.size() > 0) {
                        //check if length of sequence == Max(foundSequences_lengths)
                        if(sequenceLength == Collections.max(columnSequencesLengthsIncludingSequenceRange)) {
                            if(this.getNonogramSolutionBoard().get(firstXIndex).get(columnIdx).equals("-")) {
                                this.placeXAtGivenPosittion(firstXIndex, columnIdx)
                                        .addColumnFieldToNotToInclude(columnIdx, firstXIndex)
                                        .addRowFieldToNotToInclude(firstXIndex, columnIdx);

                                tmpLog = "X placed, columnIdx: " + columnIdx + " rowIdx: " + firstXIndex +
                                        " (Placing 'X' before longest sequence in column - sequence index not specified).";
                                this.logs.add(tmpLog);

                                this.increaseStepsMade();
                            } else if (showRepetitions) {
                                System.out.println("Sequence with maximum length in area firstXIndex placed earlier!");
                            }
                            if(this.getNonogramSolutionBoard().get(lastXIndex).get(columnIdx).equals("-")) {
                                this.placeXAtGivenPosittion(lastXIndex, columnIdx)
                                        .addColumnFieldToNotToInclude(columnIdx, lastXIndex)
                                        .addRowFieldToNotToInclude(lastXIndex, columnIdx);

                                tmpLog = "X placed, columnIdx: " + columnIdx + " rowIdx: " + lastXIndex +
                                        " (Placing 'X' after longest sequence in column - sequence index not specified).";
                                this.logs.add(tmpLog);

                                this.increaseStepsMade();
                            } else if(showRepetitions) {
                                System.out.println("Sequence with maximum length in area lastXIndex placed earlier!");
                            }

                        }
                    }

                    //2
                    this.getAffectedColumnsToMarkAvailableSequences().add(columnIdx);
                    //7
                    this.getAffectedColumnsToCorrectSequencesRanges().add(columnIdx);

                    if(firstXIndex > -1 ) {
                        //7
                        this.getAffectedRowsToChangeSequencesRangeIfXOnWay().add(firstXIndex);
                        //11
                        this.getAffectedRowsToPlaceXsAtTooShortEmptySequences().add(firstXIndex);
                        //13
                        this.getAffectedRowsToExtendColouredFieldsNearX().add(firstXIndex);
                    }

                    if(lastXIndex < this.getHeight()) {
                        //7
                        this.getAffectedRowsToChangeSequencesRangeIfXOnWay().add(lastXIndex);
                        //11
                        this.getAffectedRowsToPlaceXsAtTooShortEmptySequences().add(lastXIndex);
                        //13
                        this.getAffectedRowsToExtendColouredFieldsNearX().add(lastXIndex);
                    }

                }
            }
        }

    }

    // iterations through all rows
    public void placeXsRowsAtUnreachableFields() {

        for (Integer rowIndex : this.getAffectedRowsToPlaceXsAtUnreachableFields()) {
            placeXsRowAtUnreachableFields(rowIndex);
        }
    }

    public void placeXsRowAtUnreachableFields(int rowIdx) {

        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        int nonogramWidth = this.getWidth();
        boolean existRangeIncludingColumn;
        List<Integer> fieldAsRange;

        for(int columnIdx = 0; columnIdx < nonogramWidth; columnIdx++) {
            fieldAsRange = new ArrayList<>();
            fieldAsRange.add(columnIdx);
            fieldAsRange.add(columnIdx);
            existRangeIncludingColumn = rangesListIncludingAnotherRange(rowSequencesRanges ,fieldAsRange);

            if(!existRangeIncludingColumn) {
                if(this.getNonogramSolutionBoard().get(rowIdx).get(columnIdx).equals("-")) {
                    this.placeXAtGivenPosittion(rowIdx, columnIdx)
                            .addRowFieldToNotToInclude(rowIdx, columnIdx)
                            .addColumnFieldToNotToInclude(columnIdx, rowIdx);

                    tmpLog = "X placed, rowIdx: " + rowIdx + " , columnIdx: " + columnIdx + " (place X at unreachable fields in row). Ranges: " + rowSequencesRanges;
                    this.logs.add(tmpLog);

                    //10
                    this.getAffectedColumnsToCorrectSequencesRangesIfXOnWay().add(columnIdx);
                    //16
                    this.getAffectedColumnsToPlaceXsAtTooShortEmptySequences().add(columnIdx);
                    //18
                    this.getAffectedColumnsToExtendColouredFieldsNearX().add(columnIdx);

                    this.increaseStepsMade();
                } else if(showRepetitions) {
                    System.out.println("X at unreachable field in row placed earlier!");
                }
            }
        }

    }

    // iterations through all columns
    public void placeXsColumnsAtUnreachableFields() {

        for (Integer columnIndex : this.getAffectedColumnsToPlaceXsAtUnreachableFields()) {
            placeXsColumnAtUnreachableFields(columnIndex);
        }
    }

    public void placeXsColumnAtUnreachableFields(int columnIdx) {

        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        int nonogramHeight = this.getHeight();
        boolean existRangeIncludingRow;
        List<Integer> fieldAsRange;

        for(int rowIdx = 0; rowIdx < nonogramHeight; rowIdx++) {
            fieldAsRange = new ArrayList<>();
            fieldAsRange.add(rowIdx);
            fieldAsRange.add(rowIdx);
            existRangeIncludingRow = rangesListIncludingAnotherRange(columnSequencesRanges, fieldAsRange);

            if(!existRangeIncludingRow) {
                if(this.getNonogramSolutionBoard().get(rowIdx).get(columnIdx).equals("-")) {
                    this.placeXAtGivenPosittion(rowIdx, columnIdx)
                            .addRowFieldToNotToInclude(rowIdx, columnIdx)
                            .addColumnFieldToNotToInclude(columnIdx, rowIdx);

                    tmpLog = "X placed, columnIdx: " + columnIdx + " , rowIdx: " + rowIdx + " (place X at unreachable fields in column).";
                    this.logs.add(tmpLog);

                    //7
                    this.getAffectedRowsToChangeSequencesRangeIfXOnWay().add(rowIdx);
                    //15
                    this.getAffectedRowsToPlaceXsAtTooShortEmptySequences().add(rowIdx);
                    //17
                    this.getAffectedRowsToExtendColouredFieldsNearX().add(rowIdx);
                } else if(showRepetitions) {
                    System.out.println("X at unreachable field in column placed earlier!");
                }
            }
        }
    }

    // iterations through all rows
    public void placeXsRowsAtTooShortEmptySequences() {

        for (Integer rowIndex : this.getAffectedRowsToPlaceXsAtTooShortEmptySequences()) {
            placeXsRowAtTooShortEmptySequences(rowIndex);
        }
    }

    public void placeXsRowAtTooShortEmptySequences(int rowIdx) {

        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequenceRange;

        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);
        int rowSequenceLength;

        List<String> solutionBoardRow = this.getNonogramSolutionBoard().get(rowIdx);

        List<Integer> sequencesWhichNotFit;
        List<Integer> sequencesWithinRange;

        int firstXIndex;
        int lastXIndex;
        int emptyFieldsSequenceLength;
        List<Integer> emptyFieldsRange;
        int nonogramWidth = this.getWidth();

        boolean colouredFieldInFieldsSequence;

        for(int columnIdx = 1; columnIdx < nonogramWidth - 1; columnIdx++) {
            if(solutionBoardRow.get(columnIdx).equals("X")) {

                sequencesWhichNotFit = new ArrayList<>();
                sequencesWithinRange = new ArrayList<>();

                firstXIndex = columnIdx;
                if(solutionBoardRow.get(columnIdx + 1).equals("-")) {
                    colouredFieldInFieldsSequence = false;
                    columnIdx++;
                } else {
                    continue;
                }

                while(columnIdx < nonogramWidth && (solutionBoardRow.get(columnIdx).equals("-") ||  solutionBoardRow.get(columnIdx).equals("O")) ) {
                    if (solutionBoardRow.get(columnIdx).equals("O")) {
                        colouredFieldInFieldsSequence = true;
                        break;
                    }
                    columnIdx++;
                }

                if(!colouredFieldInFieldsSequence) {
                    lastXIndex = columnIdx;
                    columnIdx--;

                    emptyFieldsRange = createRangeFromTwoIntegers(firstXIndex + 1, lastXIndex - 1);

                    emptyFieldsSequenceLength = rangeLength(emptyFieldsRange);


                    for(int seqNo = 0; seqNo < rowSequencesLengths.size(); seqNo++) {
                        rowSequenceRange = rowSequencesRanges.get(seqNo);
                        rowSequenceLength = rowSequencesLengths.get(seqNo);

                        if(rangeInsideAnotherRange(emptyFieldsRange, rowSequenceRange) || emptyFieldsSequenceLength > rowSequenceLength) {
                            sequencesWithinRange.add(seqNo);
                            if(emptyFieldsSequenceLength < rowSequenceLength) {
                                sequencesWhichNotFit.add(seqNo);
                            }
                        }
                    }

                    // if there not any sequence with length equal or less than emptyFieldSequenceLength
                    if(sequencesWhichNotFit.size() == sequencesWithinRange.size() && emptyFieldsSequenceLength > 0) {
                        for(int emptyFieldColumnIdx = emptyFieldsRange.get(0); emptyFieldColumnIdx <= emptyFieldsRange.get(1); emptyFieldColumnIdx++) {
                            this.addRowFieldToNotToInclude(rowIdx, emptyFieldColumnIdx);
                            this.addColumnFieldToNotToInclude(emptyFieldColumnIdx, rowIdx);

                            if(this.getNonogramSolutionBoard().get(rowIdx).get(emptyFieldColumnIdx).equals("-")) {
                                this.placeXAtGivenPosittion(rowIdx, emptyFieldColumnIdx);

                                tmpLog = "X placed, row: " + rowIdx + " , col: " + emptyFieldColumnIdx + " (too short empty fields in row for sequence).";
                                this.logs.add(tmpLog);

                                //10
                                this.getAffectedColumnsToCorrectSequencesRangesIfXOnWay().add(emptyFieldColumnIdx);
                                //16
                                this.getAffectedColumnsToPlaceXsAtTooShortEmptySequences().add(emptyFieldColumnIdx);
                                //18
                                this.getAffectedColumnsToExtendColouredFieldsNearX().add(emptyFieldColumnIdx);

                                this.increaseStepsMade();
                            } else if(showRepetitions) {
                                System.out.println("X placed in too short row empty field sequence earlier!");
                            }
                        }
                    }
                }
            }
        }

    }

    // iterations through all columns
    public void placeXsColumnsAtTooShortEmptySequences() {

        for (Integer columnIndex : this.getAffectedColumnsToPlaceXsAtTooShortEmptySequences()) {
            placeXsColumnAtTooShortEmptySequences(columnIndex);
        }

    }

    public void placeXsColumnAtTooShortEmptySequences(int columnIdx) {

        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequenceRange;

        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);
        int columnSequenceLength;

        List<String> solutionBoardColumn = getNonogramBoardColumn(columnIdx);

        List<Integer> sequencesWhichNotFit;
        List<Integer> sequencesWithinRange;

        //first "X" index (rowIndex) before empty fields sequence in column
        int firstXIndex;
        //last "X" index (rowIndex) after empty fields sequence in column
        int lastXIndex;
        // empty field sequence between "X"s
        int emptyFieldsSequenceLength;
        // empty field sequence range [firstXIndex + 1, lastXIndex - 1]
        List<Integer> emptyFieldsRange;

        // condition true if coloured field exists in emptyFieldsRange (can't place Xs)
        boolean colouredFieldInFieldsSequence;

        //
        for(int rowIdx = 1; rowIdx < this.getHeight() - 1; rowIdx++) {
            //start from first rowIdx(not rowIdx==1), because if rowIdx==0 other method does stuff (correctWhenMetXOnWay)
            if(solutionBoardColumn.get(rowIdx).equals("X")) {

                sequencesWhichNotFit = new ArrayList<>();
                sequencesWithinRange = new ArrayList<>();

                firstXIndex = rowIdx;

                if(solutionBoardColumn.get(rowIdx + 1).equals("-")) {
                    colouredFieldInFieldsSequence = false; // check if in sequence of fields is coloured field (only place 'X' consecutive "-")
                    rowIdx++;
                } else {
                    continue;
                }

                //iterate to the next "X" in column -> find last field of "-" sequence
                while(rowIdx < this.getHeight() && !solutionBoardColumn.get(rowIdx).equals("X")) {
                    //if "O" met, can't place 'X' between potential too short sequence of "-"
                    if (solutionBoardColumn.get(rowIdx).equals("O")) {
                        colouredFieldInFieldsSequence = true;
                        break;
                    }
                    rowIdx++;
                }

                // condition - only empty fields, coloured fields in at least one of consectuive fields not allowed
                if(!colouredFieldInFieldsSequence) {
                    lastXIndex = rowIdx; // index of "X" after empty fields sequence
                    rowIdx--;

                    //range doesn't include leading and trailing "X"s
                    emptyFieldsRange = createRangeFromTwoIntegers(firstXIndex + 1, lastXIndex - 1);
                    //empty sequence length
                    emptyFieldsSequenceLength = rangeLength(emptyFieldsRange);


                    for(int seqNo = 0; seqNo < columnSequencesLengths.size(); seqNo++) {
                        columnSequenceRange = columnSequencesRanges.get(seqNo);
                        columnSequenceLength = columnSequencesLengths.get(seqNo);

                        //check how many sequences ranges includes empty fields sequence range TODO think about second condition
                        if(rangeInsideAnotherRange(emptyFieldsRange, columnSequenceRange) || emptyFieldsSequenceLength > columnSequenceLength) {
                            sequencesWithinRange.add(seqNo);

                            //if empty fields sequence is too short for current columnSequence
                            if(emptyFieldsSequenceLength < columnSequenceLength) {
                                sequencesWhichNotFit.add(seqNo);
                            }
                        }
                    }

                    // if there not any sequence with length equal or less than emptyFieldSequenceLength
                    if(sequencesWhichNotFit.size() == sequencesWithinRange.size() && emptyFieldsSequenceLength > 0) {
                        for(int emptyFieldRowIdx = emptyFieldsRange.get(0); emptyFieldRowIdx <= emptyFieldsRange.get(1); emptyFieldRowIdx++) {
                            // always true? (empty fields sequence)
                            if(this.getNonogramSolutionBoard().get(emptyFieldRowIdx).get(columnIdx).equals("-")) {
                                this.placeXAtGivenPosittion(emptyFieldRowIdx, columnIdx);

                                tmpLog = "X placed, column: " + columnIdx + " , row: " + emptyFieldRowIdx + " (too short empty fields in column for sequence). rowSequencesIdsNotToInclude: " + this.getRowsSequencesIdsNotToInclude().get(emptyFieldRowIdx);
                                this.logs.add(tmpLog);

                                this.addRowFieldToNotToInclude(emptyFieldRowIdx, columnIdx);
                                this.addColumnFieldToNotToInclude(columnIdx, emptyFieldRowIdx);

                                //7
                                this.getAffectedRowsToChangeSequencesRangeIfXOnWay().add(emptyFieldRowIdx);
                                //15
                                this.getAffectedRowsToPlaceXsAtTooShortEmptySequences().add(emptyFieldRowIdx);
                                //17
                                this.getAffectedRowsToExtendColouredFieldsNearX().add(emptyFieldRowIdx);

                                this.increaseStepsMade();
                            }  else if (showRepetitions) {
                                System.out.println("X placed in too short column empty field sequence earlier!");
                            }
                        }
                    }
                }
            }
        }

    }

    // iterations through all rows
    public void correctRowsSequencesRanges () {

        for (Integer rowIndex : this.getAffectedRowsToCorrectSequencesRanges()) {
            correctRowSequencesRanges(rowIndex);

            List<Integer> sequencesLengths = this.getRowsSequences().get(rowIndex);
            List<List<Integer>> sequencesRanges = this.getRowsSequencesRanges().get(rowIndex);

            if(!emptyRow(rowIndex)) {
                for(int seqNo = 0; seqNo < this.getRowsSequencesRanges().get(rowIndex).size(); seqNo++) {
                    if(rangeLength(sequencesRanges.get(seqNo)) < sequencesLengths.get(seqNo)) {
                        this.logs.add("ERR correct row sequences ranges!!!");
                        this.logs.add("row ranges: " + sequencesRanges + "   ,  row sequences lengths: " + sequencesLengths);
                        this.setSolutionInvalid(true);
                        break;
                    }
                }
            }

            if(solutionInvalid) {
                break;
            }
        }

    }

    public void correctRowSequencesRanges (int rowIdx) {

        boolean rowSequenceRangesChanged = false;

        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);
        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowFieldsNotToInclude = this.getRowsFieldsNotToInclude().get(rowIdx);

        List<Integer> fullSequenceRange;
        List<Integer> currentSequenceRange;
        List<Integer> nextSequenceRange;
        int updatedNextSequenceBeginRangeColumnIndex;
        int updatedStartIndex;
        int updatedEndIndex;
        int oldNextSequenceBeginRangeColumnIndex;
        List<Integer> updatedNextSequenceRange;

        List<Integer> rowSequencesIdsNotToInclude = this.getRowsSequencesIdsNotToInclude().get(rowIdx);

        //from left - start
        for(int sequenceIdx = 0; sequenceIdx < rowSequencesRanges.size() - 1; sequenceIdx++) {

            // row sequences not to include should be marked and rangeLength==sequenceLength
            if(rowSequencesIdsNotToInclude.contains(sequenceIdx) && !rowSequencesIdsNotToInclude.contains(sequenceIdx + 1)) {
                fullSequenceRange = rowSequencesRanges.get(sequenceIdx);

                nextSequenceRange = rowSequencesRanges.get(sequenceIdx + 1);
                //XXXffffXu---- (f - full sequence that position and mark is known, u -> the minimum start index of next sequence)
                updatedNextSequenceBeginRangeColumnIndex = fullSequenceRange.get(1) + 2;

                while(rowFieldsNotToInclude.contains(updatedNextSequenceBeginRangeColumnIndex)) {
                    updatedNextSequenceBeginRangeColumnIndex++;
                }

                oldNextSequenceBeginRangeColumnIndex = nextSequenceRange.get(0);

                updatedNextSequenceRange = new ArrayList<>();

                updatedStartIndex = Math.max(oldNextSequenceBeginRangeColumnIndex, updatedNextSequenceBeginRangeColumnIndex);
                updatedNextSequenceRange.add(updatedStartIndex);
                updatedNextSequenceRange.add(nextSequenceRange.get(1));

                if(updatedNextSequenceBeginRangeColumnIndex != oldNextSequenceBeginRangeColumnIndex) {
                    rowSequenceRangesChanged = true;
                    this.increaseStepsMade();
                    tmpLog = "Changed row sequence range (rowIdx=" + rowIdx + "), seqNo: " + (sequenceIdx + 1)
                            + ", from " + nextSequenceRange + " to " + updatedNextSequenceRange;
                    this.logs.add(tmpLog);
                }

                this.getRowsSequencesRanges().get(rowIdx).set(sequenceIdx + 1, updatedNextSequenceRange);
            }   else if(!rowSequencesIdsNotToInclude.contains(sequenceIdx) && !rowSequencesIdsNotToInclude.contains(sequenceIdx + 1)) {
                currentSequenceRange = rowSequencesRanges.get(sequenceIdx);

                nextSequenceRange = rowSequencesRanges.get(sequenceIdx + 1);
                //get the possible new miniumum start index of next sequence checking where is the earliest index that current sequence can end (+1 for 'X' after sequence)
                updatedNextSequenceBeginRangeColumnIndex = currentSequenceRange.get(0) + rowSequencesLengths.get(sequenceIdx) + 1;

                oldNextSequenceBeginRangeColumnIndex = nextSequenceRange.get(0);

                updatedNextSequenceRange = new ArrayList<>();

                updatedStartIndex = Math.max(oldNextSequenceBeginRangeColumnIndex, updatedNextSequenceBeginRangeColumnIndex);
                updatedNextSequenceRange.add(updatedStartIndex);
                updatedNextSequenceRange.add(nextSequenceRange.get(1));

                if(updatedNextSequenceBeginRangeColumnIndex != oldNextSequenceBeginRangeColumnIndex) {
                    rowSequenceRangesChanged = true;
                    this.increaseStepsMade();
                    tmpLog = "Changed row sequence range (rowIdx=" + rowIdx + "), seqNo: " + sequenceIdx
                            + ", from " + nextSequenceRange + " to " + updatedNextSequenceRange;
                    this.logs.add(tmpLog);
                }

                this.getRowsSequencesRanges().get(rowIdx).set(sequenceIdx + 1, updatedNextSequenceRange);
            }
        }

        List<Integer> previousSequenceRange;
        int previousSequenceBeginRangeColumnIndex;
        int previousSequenceEndRangeColumnIndex;
        int updatedPreviousSequenceEndRangeColumnIndex;
        List<Integer> updatedPreviousSequenceRange;

        int currentSequenceLength;

        //from left - end
        for(int sequenceIdx = rowSequencesRanges.size() - 1; sequenceIdx > 0; sequenceIdx--) {

            //currentSequenceRange is marked with row seqNo and fully coloured
            if(rowSequencesIdsNotToInclude.contains(sequenceIdx) && !rowSequencesIdsNotToInclude.contains(sequenceIdx - 1)) {

                fullSequenceRange = rowSequencesRanges.get(sequenceIdx);
                previousSequenceRange = rowSequencesRanges.get(sequenceIdx - 1);

                updatedPreviousSequenceEndRangeColumnIndex = fullSequenceRange.get(0) - 2;

                while(rowFieldsNotToInclude.contains(updatedPreviousSequenceEndRangeColumnIndex)) {
                    updatedPreviousSequenceEndRangeColumnIndex--;
                }

                previousSequenceBeginRangeColumnIndex = previousSequenceRange.get(0);
                previousSequenceEndRangeColumnIndex = previousSequenceRange.get(1);

                updatedPreviousSequenceRange = new ArrayList<>();

                updatedPreviousSequenceRange.add(previousSequenceBeginRangeColumnIndex);
                updatedEndIndex = Math.min(previousSequenceEndRangeColumnIndex, updatedPreviousSequenceEndRangeColumnIndex);
                updatedPreviousSequenceRange.add(updatedEndIndex);

                if(updatedEndIndex != previousSequenceEndRangeColumnIndex) {
                    rowSequenceRangesChanged = true;
                    this.increaseStepsMade();
                    tmpLog = "Changed row sequence range (rowIdx=" + rowIdx + "), seqNo: " + sequenceIdx
                            + ", from " + previousSequenceRange + " to " + updatedPreviousSequenceRange;
                    this.logs.add(tmpLog);
                }

                this.getRowsSequencesRanges().get(rowIdx).set(sequenceIdx - 1, updatedPreviousSequenceRange);
            } else if(!rowSequencesIdsNotToInclude.contains(sequenceIdx) && !rowSequencesIdsNotToInclude.contains(sequenceIdx - 1)) {
                currentSequenceLength = rowSequencesLengths.get(sequenceIdx);
                currentSequenceRange = rowSequencesRanges.get(sequenceIdx);
                previousSequenceRange = rowSequencesRanges.get(sequenceIdx - 1);

                updatedPreviousSequenceEndRangeColumnIndex = currentSequenceRange.get(1) - currentSequenceLength - 1;

                previousSequenceBeginRangeColumnIndex = previousSequenceRange.get(0);
                previousSequenceEndRangeColumnIndex = previousSequenceRange.get(1);

                updatedPreviousSequenceRange = new ArrayList<>();

                updatedPreviousSequenceRange.add(previousSequenceBeginRangeColumnIndex);
                updatedEndIndex = Math.min(previousSequenceEndRangeColumnIndex, updatedPreviousSequenceEndRangeColumnIndex);
                updatedPreviousSequenceRange.add(updatedEndIndex);

                if(updatedEndIndex != previousSequenceEndRangeColumnIndex) {
                    this.getRowsSequencesRanges().get(rowIdx).set(sequenceIdx - 1, updatedPreviousSequenceRange);
                    rowSequenceRangesChanged = true;
                }
            }

        }

        if(rowSequenceRangesChanged) {
            this.increaseStepsMade();
            //1
            this.getAffectedRowsToFillOverlappingFields().add(rowIdx);
            //4
            this.getAffectedRowsToMarkAvailableSequences().add(rowIdx);
            //7
            this.getAffectedRowsToChangeSequencesRangeIfXOnWay().add(rowIdx);
            //11
            this.getAffectedRowsToPlaceXsAtUnreachableFields().add(rowIdx);
            //13
            this.getAffectedRowsToPlaceXsAroundLongestSequences().add(rowIdx);
            //15
            this.getAffectedRowsToPlaceXsAtTooShortEmptySequences().add(rowIdx);
            //17
            this.getAffectedRowsToExtendColouredFieldsNearX().add(rowIdx);
        }

    }

    private boolean emptyRow(int rowIdx) {
        if(rowIdx < this.getHeight()) {
            if(this.getRowsSequences().get(rowIdx).size() == 1) {
                if(this.getRowsSequences().get(rowIdx).get(0) == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean emptyColumn(int columnIdx) {
        if(columnIdx < this.getWidth()) {
            if(this.getColumnsSequences().get(columnIdx).size() == 1) {
                if(this.getColumnsSequences().get(columnIdx).get(0) == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    // iterations through all rows
    public void correctRowsSequencesRangesWhenMetColouredFields() {
        for (Integer rowIndex : this.getAffectedRowsToCorrectSequencesRangesWhenMetColouredField()) {
            correctRowSequencesRangesWhenMetColouredField(rowIndex);

            List<Integer> sequencesLengths = this.getRowsSequences().get(rowIndex);
            List<List<Integer>> sequencesRanges = this.getRowsSequencesRanges().get(rowIndex);
            for(int seqNo = 0; seqNo < this.getRowsSequencesRanges().get(rowIndex).size(); seqNo++) {
                if(rangeLength(sequencesRanges.get(seqNo)) < sequencesLengths.get(seqNo)) {
                    this.setSolutionInvalid(true);
                    break;
                }
            }
            if(solutionInvalid) {
                break;
            }
        }
    }

    public void correctRowSequencesRangesWhenMetColouredField (int rowIdx) {
        correctRowSequencesRangesWhenMetColouredFieldFromLeft(rowIdx);
        correctRowSequencesRangesWhenMetColouredFieldFromRight(rowIdx);
    }

    public void correctRowSequencesRangesWhenMetColouredFieldFromLeft (int rowIdx) {
        boolean rowSequenceRangesChanged = false;

        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);
        List<Integer> rowSequenceRange;
        int rowSequenceRangeStart;
        int rowSequenceRangeEnd;
        int maximumPossibleSequenceRangeEnd;
        List<Integer> updatedRange;
        int updatedRangeEnd;
        List<String> solutionBoardRow = this.getNonogramSolutionBoard().get(rowIdx);
        int nonogramWidth = this.getWidth();
        int sequenceId = 0;
        int sequenceLength = rowSequencesLengths.get(0);

        for(int columnIdx = 0; columnIdx < nonogramWidth; columnIdx++) {

            //look for first coloured field from left to right
            if(solutionBoardRow.get(columnIdx).equals("O")) {
                rowSequenceRange = rowSequencesRanges.get(sequenceId);
                rowSequenceRangeStart = rowSequenceRange.get(0);
                rowSequenceRangeEnd = rowSequenceRange.get(1);

                // new maximum end index cannot be higher than: (first coloured field index + sequence length - 1), 1 stands for already coloured field
                maximumPossibleSequenceRangeEnd = columnIdx + sequenceLength - 1;

                updatedRange = new ArrayList<>();
                // start column index doesn't change
                updatedRange.add(rowSequenceRangeStart);

                // new end index is minimum of two values - old end index and new calculated when met coloured field
                // (sequence can be far away from start so we can check if coloured field is close enough to set new minimum end range index
                updatedRangeEnd = Math.min(rowSequenceRangeEnd, maximumPossibleSequenceRangeEnd);
                updatedRange.add(updatedRangeEnd);

                if(updatedRangeEnd != rowSequenceRangeEnd) {
                    rowSequenceRangesChanged = true;
                    tmpLog = "Changed row sequence when met coloured field(left)(rowIdx=" + rowIdx + "), seqNo: " + sequenceId + ", from " + rowSequenceRange +  " to [" + updatedRange.get(0) + "," + updatedRange.get(1) + "]";
                    this.logs.add(tmpLog);
                }

                this.getRowsSequencesRanges().get(rowIdx).set(sequenceId, updatedRange);

                columnIdx = columnIdx + sequenceLength;
                sequenceId++;
                // check if met enough coloured fields in certain intervals (not go to sequenceId > rowSequences.size())
                if(sequenceId < rowSequencesLengths.size()) {
                    sequenceLength = rowSequencesLengths.get(sequenceId);
                } else {
                    break;
                }
            }
        }

        if(rowSequenceRangesChanged) {
            //1
            this.getAffectedRowsToFillOverlappingFields().add(rowIdx);
            //4
            this.getAffectedRowsToMarkAvailableSequences().add(rowIdx);
            //11
            this.getAffectedRowsToPlaceXsAtUnreachableFields().add(rowIdx);
            //13
            this.getAffectedRowsToPlaceXsAroundLongestSequences().add(rowIdx);
            //15
            this.getAffectedRowsToPlaceXsAtTooShortEmptySequences().add(rowIdx);
            //17
            this.getAffectedRowsToExtendColouredFieldsNearX().add(rowIdx);
        }
    }

    public void correctRowSequencesRangesWhenMetColouredFieldFromRight (int rowIdx) {
        boolean rowSequenceRangesChanged = false;

        List<List<Integer>> rowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths = this.getRowsSequences().get(rowIdx);
        List<Integer> rowSequenceRange;
        int rowSequenceRangeStart;
        int rowSequenceRangeEnd;
        int updatedRangeStart;
        int minimumPossibleSequenceRangeStart;
        List<Integer> updatedRange;
        List<String> solutionBoardRow = this.getNonogramSolutionBoard().get(rowIdx);
        int nonogramWidth = this.getWidth();
        int sequenceId = rowSequencesLengths.size() - 1;
        int sequenceLength = rowSequencesLengths.get(sequenceId);

        for(int columnIdx = nonogramWidth - 1; columnIdx >= 0; columnIdx--) {
            if(solutionBoardRow.get(columnIdx).equals("O")) {
                minimumPossibleSequenceRangeStart = columnIdx - sequenceLength + 1;
                rowSequenceRange = rowSequencesRanges.get(sequenceId);
                rowSequenceRangeStart = rowSequenceRange.get(0);
                rowSequenceRangeEnd = rowSequenceRange.get(1);

                updatedRange = new ArrayList<>();
                updatedRangeStart = Math.max(minimumPossibleSequenceRangeStart, rowSequenceRangeStart);
                updatedRange.add(updatedRangeStart);

                updatedRange.add(rowSequenceRangeEnd);

                if(updatedRangeStart != rowSequenceRangeStart) {
                    rowSequenceRangesChanged = true;
                    tmpLog = "Changed row sequence when met coloured field(right)(rowIdx=" + rowIdx + "), seqNo: " + sequenceId + " , from" + rowSequenceRange  + " to [" + updatedRange.get(0) + "," + updatedRange.get(1) + "]";
                    this.logs.add(tmpLog);
                    this.getRowsSequencesRanges().get(rowIdx).set(sequenceId, updatedRange);
                }

                //go back - make step of length of current sequence, columnIdx-- will work as placing imaginary "X" before coloured sequence
                columnIdx = columnIdx - sequenceLength;
                sequenceId--;
                if(sequenceId > -1) {
                    sequenceLength = rowSequencesLengths.get(sequenceId);
                } else {
                    break;
                }
            }
        }

        if(rowSequenceRangesChanged) {
            //1
            this.getAffectedRowsToFillOverlappingFields().add(rowIdx);
            //4
            this.getAffectedRowsToMarkAvailableSequences().add(rowIdx);
            //11
            this.getAffectedRowsToPlaceXsAtUnreachableFields().add(rowIdx);
            //13
            this.getAffectedRowsToPlaceXsAroundLongestSequences().add(rowIdx);
            //15
            this.getAffectedRowsToPlaceXsAtTooShortEmptySequences().add(rowIdx);
            //17
            this.getAffectedRowsToExtendColouredFieldsNearX().add(rowIdx);
        }
    }

    // iterations through all rows
    public void correctRowsSequencesRangesIndexesIfXOnWay() {
        for(Integer rowIndex : this.getAffectedRowsToChangeSequencesRangeIfXOnWay()) {
            correctRowRangeIndexesIfXOnWay(rowIndex);

            List<Integer> sequencesLengths = this.getRowsSequences().get(rowIndex);
            List<List<Integer>> sequencesRanges = this.getRowsSequencesRanges().get(rowIndex);
            for(int seqNo = 0; seqNo < this.getRowsSequencesRanges().get(rowIndex).size(); seqNo++) {
                if(rangeLength(sequencesRanges.get(seqNo)) < sequencesLengths.get(seqNo)) {
                    this.setSolutionInvalid(true);
                    break;
                }
            }
            if(solutionInvalid) {
                break;
            }
        }
    }

    public void correctRowRangeIndexesIfXOnWay (int rowIdx) {

        boolean rowSequenceRangesChanged = false;

        List<Integer> rowSequences = this.getRowsSequences().get(rowIdx);
        List<List<Integer>> nonogramRowSequencesRanges = this.getRowsSequencesRanges().get(rowIdx);
        List<Integer> nonogramRowSequencesIdsNotToInclude = this.getRowsSequencesIdsNotToInclude().get(rowIdx);
        List<List<String>> nonogramBoard = this.getNonogramSolutionBoard();
        int oldRowSequenceRangeStartIndex;
        int oldRowSequenceRangeEndIndex;
        int rowSequenceLength;
        List<Integer> rowSequenceRange;

        boolean indexOk;
        int updatedRowRangeStartIndex;
        int updatedRowRangeEndIndex;
        List<Integer> updatedRange;

        for(int seqNo = 0; seqNo < nonogramRowSequencesRanges.size(); seqNo++) {
            if(!nonogramRowSequencesIdsNotToInclude.contains(seqNo)) {

                rowSequenceRange = nonogramRowSequencesRanges.get(seqNo);
                rowSequenceLength = rowSequences.get(seqNo);
                oldRowSequenceRangeStartIndex = rowSequenceRange.get(0);
                oldRowSequenceRangeEndIndex = rowSequenceRange.get(1);

                updatedRowRangeStartIndex = oldRowSequenceRangeStartIndex;

                for(int columnStartIndex = oldRowSequenceRangeStartIndex; columnStartIndex <= (oldRowSequenceRangeEndIndex - rowSequenceLength + 1); columnStartIndex++) {
                    indexOk = true;
                    for(int columnIdx = columnStartIndex; columnIdx < columnStartIndex + rowSequenceLength; columnIdx++) {
                        if(nonogramBoard.get(rowIdx).get(columnIdx).equals("X")) {
                            indexOk = false;
                            break;
                        }
                    }
                    if(indexOk) {
                        break;
                    } else {
                        updatedRowRangeStartIndex++;
                    }
                }

                updatedRowRangeEndIndex = oldRowSequenceRangeEndIndex;

                for(int columnEndIndex = oldRowSequenceRangeEndIndex; columnEndIndex > (oldRowSequenceRangeStartIndex + rowSequenceLength - 1); columnEndIndex--) {
                    indexOk = true;
                    for(int columnIdx = columnEndIndex; columnIdx > columnEndIndex - rowSequenceLength; columnIdx--) {
                        if(nonogramBoard.get(rowIdx).get(columnIdx).equals("X")) {
                            indexOk = false;
                            break;
                        }
                    }
                    if(indexOk) {
                        break;
                    } else {
                        updatedRowRangeEndIndex--;
                    }
                }

                if((updatedRowRangeStartIndex != oldRowSequenceRangeStartIndex || updatedRowRangeEndIndex != oldRowSequenceRangeEndIndex)) {
                    rowSequenceRangesChanged = true;
                }

                updatedRange = new ArrayList<>();
                updatedRange.add(updatedRowRangeStartIndex);
                updatedRange.add(updatedRowRangeEndIndex);
                this.logs.add(tmpLog);

                this.changeRowSequenceRange(rowIdx, seqNo, updatedRowRangeStartIndex, updatedRowRangeEndIndex);
            }
        }

        if(rowSequenceRangesChanged) {
            this.increaseStepsMade();
            //1
            this.getAffectedRowsToFillOverlappingFields().add(rowIdx);
            //4
            this.getAffectedRowsToMarkAvailableSequences().add(rowIdx);
            //5
            this.getAffectedRowsToCorrectSequencesRanges().add(rowIdx);
            //11
            this.getAffectedRowsToPlaceXsAtUnreachableFields().add(rowIdx);
            //13
            this.getAffectedRowsToPlaceXsAroundLongestSequences().add(rowIdx);
            //15
            this.getAffectedRowsToPlaceXsAtTooShortEmptySequences().add(rowIdx);
            //17
            this.getAffectedRowsToExtendColouredFieldsNearX().add(rowIdx);
        }

    }

    // iterations through all columns
    public void correctColumnsSequencesRanges () {

        for (Integer columnIndex : this.getAffectedColumnsToCorrectSequencesRanges()) {
            correctColumnSequencesRanges(columnIndex);

            List<Integer> sequencesLengths = this.getColumnsSequences().get(columnIndex);
            List<List<Integer>> sequencesRanges = this.getColumnsSequencesRanges().get(columnIndex);

            if(!emptyColumn(columnIndex)) {
                for(int seqNo = 0; seqNo < sequencesRanges.size(); seqNo++) {
                    if(rangeLength(sequencesRanges.get(seqNo)) < sequencesLengths.get(seqNo)) {
                        this.logs.add("ERR correct column sequences ranges!!!");
                        this.logs.add("column ranges: " + sequencesRanges + "   ,  column sequences lengths: " + sequencesLengths);
                        this.setSolutionInvalid(true);
                        break;
                    }
                }
            }

            if(solutionInvalid) {
                break;
            }
        }
    }

    public void correctColumnSequencesRanges (int columnIdx) {

        boolean columnSequenceRangesChanged = false;

        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);
        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnFieldsNotToInclude = this.getColumnsFieldsNotToInclude().get(columnIdx);

        List<Integer> fullSequenceRange;
        List<Integer> currentSequenceRange;
        List<Integer> nextSequenceRange;
        int updatedNextSequenceBeginRangeRowIndex;
        int updatedStartIndex;
        int updatedEndIndex;
        int oldNextSequenceBeginRangeRowIndex;
        List<Integer> updatedNextSequenceRange;

        List<Integer> columnSequencesIdsNotToInclude = this.getColumnsSequencesIdsNotToInclude().get(columnIdx);

        //from top - start
        for(int sequenceIdx = 0; sequenceIdx < columnSequencesRanges.size() - 1; sequenceIdx++) {

            if(columnSequencesIdsNotToInclude.contains(sequenceIdx) && !columnSequencesIdsNotToInclude.contains(sequenceIdx + 1)) {
                fullSequenceRange = columnSequencesRanges.get(sequenceIdx);

                nextSequenceRange = columnSequencesRanges.get(sequenceIdx + 1);
                //XXXffffXu---- (f - full sequence that position and mark is known, u -> the minimum start index of next sequence)
                updatedNextSequenceBeginRangeRowIndex = fullSequenceRange.get(1) + 2;

                while(columnFieldsNotToInclude.contains(updatedNextSequenceBeginRangeRowIndex)) {
                    updatedNextSequenceBeginRangeRowIndex++;
                }

                oldNextSequenceBeginRangeRowIndex = nextSequenceRange.get(0);

                updatedNextSequenceRange = new ArrayList<>();

                updatedStartIndex = Math.max(oldNextSequenceBeginRangeRowIndex, updatedNextSequenceBeginRangeRowIndex);
                updatedNextSequenceRange.add(updatedStartIndex);
                updatedNextSequenceRange.add(nextSequenceRange.get(1));

                if(updatedNextSequenceBeginRangeRowIndex != oldNextSequenceBeginRangeRowIndex) {
                    this.getColumnsSequencesRanges().get(columnIdx).set(sequenceIdx + 1, updatedNextSequenceRange);
                    columnSequenceRangesChanged = true;
                    this.increaseStepsMade();
                    tmpLog = "Changed column sequence range(previous seqId not to include) from top (columnIdx=" + columnIdx + "), seqNo: " + (sequenceIdx + 1)
                            + ", from " + nextSequenceRange + " to " + updatedNextSequenceRange;
                    this.logs.add(tmpLog);
                }
            }   else if(!columnSequencesIdsNotToInclude.contains(sequenceIdx) && !columnSequencesIdsNotToInclude.contains(sequenceIdx + 1)) {

                currentSequenceRange = columnSequencesRanges.get(sequenceIdx);

                nextSequenceRange = columnSequencesRanges.get(sequenceIdx + 1);
                //get the miniumum start index of next sequence checking where is the earliest index that current sequence can end (+1 for 'X' after sequence)
                updatedNextSequenceBeginRangeRowIndex = currentSequenceRange.get(0) + columnSequencesLengths.get(sequenceIdx) + 1;

                oldNextSequenceBeginRangeRowIndex = nextSequenceRange.get(0); //6

                updatedNextSequenceRange = new ArrayList<>();

                updatedStartIndex = Math.max(oldNextSequenceBeginRangeRowIndex, updatedNextSequenceBeginRangeRowIndex);

                updatedNextSequenceRange.add(updatedStartIndex);  //6
                updatedNextSequenceRange.add(nextSequenceRange.get(1)); //8

                if(updatedStartIndex != oldNextSequenceBeginRangeRowIndex) {
                    columnSequenceRangesChanged = true;
                    this.increaseStepsMade();
                    tmpLog = "Changed column sequence range(previous seqId to include) from top (columnIdx=" + columnIdx + "), seqNo: " + (sequenceIdx + 1)
                            + ", from " + nextSequenceRange + " to " + updatedNextSequenceRange;
                    this.logs.add(tmpLog);
                }

                this.getColumnsSequencesRanges().get(columnIdx).set(sequenceIdx + 1, updatedNextSequenceRange);
            }
        }

        List<Integer> previousSequenceRange;
        int previousSequenceBeginRangeRowIndex;
        int previousSequenceEndRangeRowIndex;
        int updatedPreviousSequenceEndRangeRowIndex;
        List<Integer> updatedPreviousSequenceRange;

        int currentSequenceLength;

        //from bottom - end
        for(int sequenceIdx = columnSequencesRanges.size() - 1; sequenceIdx > 0; sequenceIdx--) {

            // f.e. [[0, 10], [2, 12]] (lengths: [1, 2], docelowo: [[0, 9], [10, 12]]
            if(columnSequencesIdsNotToInclude.contains(sequenceIdx) && !columnSequencesIdsNotToInclude.contains(sequenceIdx - 1)) {

                //currentSequenceRange is marked with column seqNo and fully coloured
                if(columnSequencesIdsNotToInclude.contains(sequenceIdx)) {
                    currentSequenceRange = columnSequencesRanges.get(sequenceIdx);
                    previousSequenceRange = columnSequencesRanges.get(sequenceIdx - 1);

                    updatedPreviousSequenceEndRangeRowIndex = currentSequenceRange.get(0) - 2;

                    while(columnFieldsNotToInclude.contains(updatedPreviousSequenceEndRangeRowIndex)) {
                        updatedPreviousSequenceEndRangeRowIndex--;
                    }

                    previousSequenceBeginRangeRowIndex = previousSequenceRange.get(0);
                    previousSequenceEndRangeRowIndex = previousSequenceRange.get(1);

                    updatedPreviousSequenceRange = new ArrayList<>();

                    updatedPreviousSequenceRange.add(previousSequenceBeginRangeRowIndex);
                    updatedEndIndex = Math.min(previousSequenceEndRangeRowIndex, updatedPreviousSequenceEndRangeRowIndex);
                    updatedPreviousSequenceRange.add(updatedEndIndex);

                    if(updatedEndIndex != previousSequenceEndRangeRowIndex) {
                        columnSequenceRangesChanged = true;
                        this.increaseStepsMade();
                        tmpLog = "Changed column sequence range from bottom - end (columnIdx=" + columnIdx + "), seqNo: " + (sequenceIdx - 1)
                                + ", from " + previousSequenceRange + " to " + updatedPreviousSequenceRange + " ranges: "
                                + columnSequencesRanges + " column: " + this.getNonogramBoardColumn(columnIdx)
                        + " column fields not to include: " + columnFieldsNotToInclude;
                        this.logs.add(tmpLog);
                    }

                    this.getColumnsSequencesRanges().get(columnIdx).set(sequenceIdx - 1, updatedPreviousSequenceRange);
                }
            } else if(!columnSequencesIdsNotToInclude.contains(sequenceIdx) && !columnSequencesIdsNotToInclude.contains(sequenceIdx - 1)) {
                currentSequenceLength = columnSequencesLengths.get(sequenceIdx);
                currentSequenceRange = columnSequencesRanges.get(sequenceIdx);
                previousSequenceRange = columnSequencesRanges.get(sequenceIdx - 1);

                updatedPreviousSequenceEndRangeRowIndex = currentSequenceRange.get(1) - currentSequenceLength - 1;

                previousSequenceBeginRangeRowIndex = previousSequenceRange.get(0);
                previousSequenceEndRangeRowIndex = previousSequenceRange.get(1);

                updatedPreviousSequenceRange = new ArrayList<>();

                updatedPreviousSequenceRange.add(previousSequenceBeginRangeRowIndex);
                updatedEndIndex = Math.min(previousSequenceEndRangeRowIndex, updatedPreviousSequenceEndRangeRowIndex);
                updatedPreviousSequenceRange.add(updatedEndIndex);

                if(updatedEndIndex != previousSequenceEndRangeRowIndex) {
                    this.getColumnsSequencesRanges().get(columnIdx).set(sequenceIdx - 1, updatedPreviousSequenceRange);
                    columnSequenceRangesChanged = true;
                }
            }
        }

        if(columnSequenceRangesChanged) {
            //this.increaseStepsMade();
            //2
            this.getAffectedColumnsToMarkAvailableSequences().add(columnIdx);
            //3
            this.getAffectedColumnsToFillOverlappingFields().add(columnIdx);
            //10
            this.getAffectedColumnsToCorrectSequencesRangesIfXOnWay().add(columnIdx);
            //12
            this.getAffectedColumnsToPlaceXsAtUnreachableFields().add(columnIdx);
            //14
            this.getAffectedColumnsToPlaceXsAroundLongestSequences().add(columnIdx);
            //16
            this.getAffectedColumnsToPlaceXsAtTooShortEmptySequences().add(columnIdx);
            //18
            this.getAffectedColumnsToExtendColouredFieldsNearX().add(columnIdx);
        }
    }

    // iterations through all columns
    public void correctColumnsSequencesWhenMetColouredField () {

        for (Integer columnIndex : this.getAffectedColumnsToCorrectSequencesRangesWhenMetColouredField()) {
            correctColumnSequencesWhenMetColouredField(columnIndex);
        }
    }

    public void correctColumnSequencesWhenMetColouredField (int columnIdx) {


        boolean columnSequencesRangesChanged = false;

        List<List<Integer>> columnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> columnSequencesLengths = this.getColumnsSequences().get(columnIdx);
        int columnSequenceRangeStart;
        int columnSequenceRangeEnd;
        List<Integer> oldRange;
        List<Integer> updatedRange;

        List<String> solutionBoardColumn = getNonogramBoardColumn(columnIdx);

        int sequenceId = 0;
        int sequenceLength = columnSequencesLengths.get(0);
        int updatedEndIndex;
        int maximumPossibleSequenceRangeEnd;

        for(int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            if(solutionBoardColumn.get(rowIdx).equals("O")) {
                oldRange = columnSequencesRanges.get(sequenceId);
                columnSequenceRangeStart = oldRange.get(0);
                columnSequenceRangeEnd = oldRange.get(1);
                maximumPossibleSequenceRangeEnd = rowIdx + sequenceLength - 1;

                updatedRange = new ArrayList<>();
                updatedRange.add(columnSequenceRangeStart);
                updatedEndIndex = Math.min(columnSequenceRangeEnd, maximumPossibleSequenceRangeEnd);
                updatedRange.add(updatedEndIndex);
                if(updatedEndIndex != columnSequenceRangeEnd) {
                    columnSequencesRangesChanged = true;
                    this.getColumnsSequencesRanges().get(columnIdx).set(sequenceId, updatedRange);
                    tmpLog = "Changed column sequence range from top (columnIdx="+ columnIdx + "), seqNo: " + (sequenceId - 1) +
                            ", from " + oldRange + " to " + updatedRange + " ranges: " + columnSequencesRanges;
                    this.logs.add(tmpLog);
                }

                rowIdx = rowIdx + sequenceLength;
                sequenceId++;
                if(sequenceId < columnSequencesLengths.size()) {
                    sequenceLength = columnSequencesLengths.get(sequenceId);
                } else {
                    break;
                }
            }
        }

        int updatedStartIndex;
        int minimumPossibleSequenceRangeStart;

        sequenceId = columnSequencesLengths.size() - 1;
        sequenceLength = columnSequencesLengths.get(sequenceId);

        for(int rowIdx = this.getHeight() - 1; rowIdx >= 0 ; rowIdx--) {
            if(solutionBoardColumn.get(rowIdx).equals("O")) {
                oldRange = columnSequencesRanges.get(sequenceId);
                columnSequenceRangeStart = oldRange.get(0);
                columnSequenceRangeEnd = oldRange.get(1);
                minimumPossibleSequenceRangeStart = rowIdx - sequenceLength + 1;

                updatedRange = new ArrayList<>();
                updatedStartIndex = Math.max(columnSequenceRangeStart, minimumPossibleSequenceRangeStart);
                updatedRange.add(updatedStartIndex);
                updatedRange.add(columnSequenceRangeEnd);
                if(updatedStartIndex != columnSequenceRangeStart) {
                    columnSequencesRangesChanged = true;
                    this.getColumnsSequencesRanges().get(columnIdx).set(sequenceId, updatedRange);
                    tmpLog = "Changed column sequence range from bottom (columnIdx="+ columnIdx + "), seqNo: " + (sequenceId - 1) +
                            ", from " + oldRange + " to " + updatedRange + " ranges: " + columnSequencesRanges;
                    this.logs.add(tmpLog);
                }

                rowIdx = rowIdx - sequenceLength;
                sequenceId--;
                if(sequenceId >= 0) {
                    sequenceLength = columnSequencesLengths.get(sequenceId);
                } else {
                    break;
                }
            }
        }

        if(columnSequencesRangesChanged) {
            //2
            this.getAffectedColumnsToMarkAvailableSequences().add(columnIdx);
            //3
            this.getAffectedColumnsToFillOverlappingFields().add(columnIdx);
            //12
            this.getAffectedColumnsToPlaceXsAtUnreachableFields().add(columnIdx);
            //14
            this.getAffectedColumnsToPlaceXsAroundLongestSequences().add(columnIdx);
            //18
            this.getAffectedColumnsToExtendColouredFieldsNearX().add(columnIdx);
        }
    }

    // iterations through all columns
    public void correctColumnsSequencesIfXOnWay () {
        for (Integer columnIndex : this.getAffectedColumnsToCorrectSequencesRangesIfXOnWay()) {
            correctColumnRangeIndexesIfXOnWay(columnIndex);
        }
    }

    // iterations through all columns
    public void correctColumnRangeIndexesIfXOnWay (int columnIdx) {

        boolean columnSequencesRangesChanged = false;

        List<Integer> columnSequences = this.getColumnsSequences().get(columnIdx);
        List<List<Integer>> nonogramColumnSequencesRanges = this.getColumnsSequencesRanges().get(columnIdx);
        List<Integer> nonogramColumnsSequencesIdsNotToInclude = this.getColumnsSequencesIdsNotToInclude().get(columnIdx);
        List<List<String>> nonogramBoard = this.getNonogramSolutionBoard();
        int columnSequenceRangeStartIndex;
        int columnSequenceRangeEndIndex;
        int columnSequenceLength;
        List<Integer> columnSequenceRange;

        boolean indexOk;
        int updatedColumnSequenceRangeStartIndex;
        int updatedColumnSequenceRangeEndIndex;

        for(int seqNo = 0; seqNo < nonogramColumnSequencesRanges.size(); seqNo++) {
            if(!nonogramColumnsSequencesIdsNotToInclude.contains(seqNo)) {

                columnSequenceRange = nonogramColumnSequencesRanges.get(seqNo);
                columnSequenceLength = columnSequences.get(seqNo);
                columnSequenceRangeStartIndex = columnSequenceRange.get(0);
                columnSequenceRangeEndIndex = columnSequenceRange.get(1);

                updatedColumnSequenceRangeStartIndex = columnSequenceRangeStartIndex;

                for(int rowStartIndex = columnSequenceRangeStartIndex; rowStartIndex < columnSequenceRangeEndIndex - columnSequenceLength + 1; rowStartIndex++) {
                    indexOk = true;
                    for(int rowIdx = rowStartIndex; rowIdx < rowStartIndex + columnSequenceLength; rowIdx++) {
                        if(nonogramBoard.get(rowIdx).get(columnIdx).equals("X")) {
                            indexOk = false;
                            break;
                        }
                    }
                    if(indexOk) {
                        break;
                    } else {
                        updatedColumnSequenceRangeStartIndex++;
                    }
                }

                updatedColumnSequenceRangeEndIndex = columnSequenceRangeEndIndex;

                for(int rowEndIndex = columnSequenceRangeEndIndex; rowEndIndex > columnSequenceRangeStartIndex + columnSequenceLength - 1; rowEndIndex--) {
                    indexOk = true;
                    for(int rowIdx = rowEndIndex; rowIdx > rowEndIndex - columnSequenceLength; rowIdx--) {
                        if(nonogramBoard.get(rowIdx).get(columnIdx).equals("X")) {
                            indexOk = false;
                            break;
                        }
                    }
                    if(indexOk) {
                        break;
                    } else {
                        updatedColumnSequenceRangeEndIndex--;
                    }
                }

                if(updatedColumnSequenceRangeStartIndex != columnSequenceRangeStartIndex || updatedColumnSequenceRangeEndIndex != columnSequenceRangeEndIndex) {
                    columnSequencesRangesChanged = true;
                }

                this.changeColumnSequenceRange(columnIdx, seqNo,
                        updatedColumnSequenceRangeStartIndex, updatedColumnSequenceRangeEndIndex);

                if(updatedColumnSequenceRangeEndIndex - updatedColumnSequenceRangeStartIndex + 1 == columnSequenceLength) {
                    this.addColumnSequenceIdxToNotToInclude(columnIdx, seqNo);
                }
            }
        }

        if(columnSequencesRangesChanged) {
            //2
            this.getAffectedColumnsToMarkAvailableSequences().add(columnIdx);
            //3
            this.getAffectedColumnsToFillOverlappingFields().add(columnIdx);
            //8
            this.getAffectedColumnsToCorrectSequencesRanges().add(columnIdx);
            //10
            this.getAffectedColumnsToCorrectSequencesRangesIfXOnWay().add(columnIdx);
            //12
            this.getAffectedColumnsToPlaceXsAtUnreachableFields().add(columnIdx);
            //14
            this.getAffectedColumnsToPlaceXsAroundLongestSequences().add(columnIdx);
            //16
            this.getAffectedColumnsToPlaceXsAtTooShortEmptySequences().add(columnIdx);
            //18
            this.getAffectedColumnsToExtendColouredFieldsNearX().add(columnIdx);
        }
    }

    public int area() {
        return this.getWidth() * this.getHeight();
    }

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

    public List<String> getNonogramBoardColumn(int columnIdx) {
        List<String> solutionBoardColumn = new ArrayList<>();

        for(int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            solutionBoardColumn.add(nonogramSolutionBoard.get(rowIdx).get(columnIdx));
        }

        return solutionBoardColumn;
    }

    public boolean isSolved() {
        return this.fieldsColoured() + this.fieldsWithXPlaced() == this.area();
    }
}
