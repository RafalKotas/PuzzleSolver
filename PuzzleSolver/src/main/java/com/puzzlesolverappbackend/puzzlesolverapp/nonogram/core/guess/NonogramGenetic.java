package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.guess;

import com.google.gson.Gson;
import com.puzzlesolverappbackend.puzzlesolverapp.constants.InitializerConstants;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramConstants.COLOURED_FIELD;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramConstants.X_FIELD;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.BoardUtils.getColumn;

@Getter
@Setter
@Slf4j
public class NonogramGenetic {

    private boolean solutionFound;
    private Random randomIntGenerator;
    private List<Integer> sortedListOfIntegers = new ArrayList<>(Arrays.asList(197, 183, 172, 161, 160, 159, 157, 155, 153, 150));
    private int firstIntToFind = 165;
    private int secondIntToFind = 149;

    private NonogramLogic nonogramObject;
    private List<NonogramLogic> currentPopulation;
    private List<NonogramLogic> nextPopulation;

    private List<Integer> maxCorrectFieldsInColumnsSums;

    private List<List<String>> finalSolutionBoard;

    private static final int POPULATION_COUNT = 120;
    private List<List<Integer>> populationColumnsMaximumCorrectIndexFromTop;
    private List<List<Integer>> populationColumnsMaximumCorrectIndexFromBottom;
    private static final double MUTATION_PROBABILITY = 0.05;

    private static final int ITERATIONS_LIMIT = 10000;

    private static final int TIME_SECONDS_LIMIT = 100;

    private Gson gson;

    public NonogramGenetic(NonogramLogic nonogramObject) {
        gson = new Gson();
        this.nonogramObject = gson.fromJson(gson.toJson(nonogramObject), NonogramLogic.class);
        this.randomIntGenerator = new Random();
        this.solutionFound = false;
    }

    public void solve() {
        generateInitialPopulation();
        for (int iterationNo = 0; iterationNo < 20; iterationNo++) {
            generateNextPopulation();
            if (solutionFound) {
                log.info("Solution found!!!, iterationNo: {}", iterationNo);
                break;
            }
        }
    }

    public void generateInitialPopulation() {
        NonogramLogic populationMember;
        List<String> nonogramBoardRow;
        currentPopulation = new ArrayList<>();

        populationColumnsMaximumCorrectIndexFromTop = new ArrayList<>();
        populationColumnsMaximumCorrectIndexFromBottom = new ArrayList<>();

        int generated = 0;

        while (generated < POPULATION_COUNT) {
            populationMember = gson.fromJson(gson.toJson(nonogramObject), NonogramLogic.class);

            for (int rowIdx = 0; rowIdx < nonogramObject.getNonogramRules().getHeight(); rowIdx++) {
                nonogramBoardRow = generateRandomRowArray(populationMember, rowIdx);
                populationMember = populationMember.setNonogramBoardRow(rowIdx, nonogramBoardRow);
            }

            if (boardInPopulationUnique(populationMember.getNonogramSolutionBoard(), currentPopulation)) {
                currentPopulation.add(populationMember);
                generated++;
            }
        }
    }

    public void generateNextPopulation() {
        initializeNextPopulation();
        log.info("Current population size: {}", currentPopulation.size());

        this.setSolutionFound(false);

        for (int i = 0; i < POPULATION_COUNT && !isSolutionFound(); i++) {
            NonogramLogic first = currentPopulation.get(i);

            for (int j = i + 1; j < POPULATION_COUNT && !isSolutionFound(); j++) {
                NonogramLogic second = currentPopulation.get(j);

                handleCrossover(first, second);
            }
        }
    }

    private void initializeNextPopulation() {
        randomIntGenerator = new Random();
        nextPopulation = new ArrayList<>();
        maxCorrectFieldsInColumnsSums = new ArrayList<>();
    }

    private void handleCrossover(NonogramLogic first, NonogramLogic second) {
        int rowIndex = chooseCrossoverRow(first, second);

        NonogramLogic child1 = gson.fromJson(gson.toJson(first), NonogramLogic.class);
        NonogramLogic child2 = gson.fromJson(gson.toJson(second), NonogramLogic.class);

        List<String> row1 = first.getNonogramSolutionBoard().get(rowIndex);
        List<String> row2 = second.getNonogramSolutionBoard().get(rowIndex);

        child2.setNonogramBoardRow(rowIndex, row1);
        child1.setNonogramBoardRow(rowIndex, shouldMutate() ? generateRandomRowArray(child1, rowIndex) : row2);
        child2.setNonogramBoardRow(rowIndex, shouldMutate() ? generateRandomRowArray(child2, rowIndex) : row1);

        evaluateAndInsert(child1, first, "first");
        evaluateAndInsert(child2, second, "second");
    }

    private int chooseCrossoverRow(NonogramLogic a, NonogramLogic b) {
        int topMin = Math.min(
                getMin(generateColumnsMaximumCorrectIndexesFromTop(a)),
                getMin(generateColumnsMaximumCorrectIndexesFromTop(b))
        );
        int bottomMin = Math.min(
                getMin(generateColumnsMaximumCorrectIndexesFromBottom(a)),
                getMin(generateColumnsMaximumCorrectIndexesFromBottom(b))
        );

        boolean chooseTop = randomIntGenerator.nextBoolean();

        if (chooseTop) {
            return randomIntGenerator.nextInt(topMin + 1);
        } else {
            int height = nonogramObject.getNonogramRules().getHeight();
            int minIdx = height - 1 - bottomMin;
            return randomIntGenerator.nextInt(height - minIdx) + minIdx;
        }
    }

    private int getMin(List<Integer> values) {
        return values.stream().mapToInt(i -> i).min().orElseThrow(NoSuchElementException::new);
    }

    private boolean shouldMutate() {
        return randomIntGenerator.nextInt(100) + 1 > 95;
    }

    private void evaluateAndInsert(NonogramLogic child, NonogramLogic parent, String label) {
        List<Integer> top = generateColumnsMaximumCorrectIndexesFromTop(child);
        List<Integer> bottom = generateColumnsMaximumCorrectIndexesFromBottom(parent);
        int score = top.stream().mapToInt(Integer::intValue).sum() + bottom.stream().mapToInt(Integer::intValue).sum();

        int idx = findFirstLessOrEqualElementIndex(score);
        if (idx != POPULATION_COUNT) {
            nextPopulation.add(idx, child);
            maxCorrectFieldsInColumnsSums.add(idx, score);
            if (nextPopulation.size() > POPULATION_COUNT) {
                nextPopulation = nextPopulation.subList(0, POPULATION_COUNT);
                maxCorrectFieldsInColumnsSums = maxCorrectFieldsInColumnsSums.subList(0, POPULATION_COUNT);
            }
            if ("first".equals(label) && child.subSolutionBoardCorrectComparisonWithSolutionBoard("r" + InitializerConstants.PUZZLE_NAME)) {
                setSolutionFound(true);
            }
        } else if (nextPopulation.isEmpty()) {
            nextPopulation.add(child);
            maxCorrectFieldsInColumnsSums.add(score);
            log.info("added {} element to next population/max sums", label);
        }
    }

    public int findFirstLessOrEqualElementIndex(int elementToFind) {
        OptionalInt firstIndex = IntStream.range(0, maxCorrectFieldsInColumnsSums.size())
                .filter(i -> maxCorrectFieldsInColumnsSums.get(i) <= elementToFind)
                .findFirst();

        if (firstIndex.isPresent()) {
            return firstIndex.getAsInt();
        } else {
            return POPULATION_COUNT;
        }
    }

    public List<Integer> generateColumnsMaximumCorrectIndexesFromTop(NonogramLogic populationMember) {
        int width = populationMember.getNonogramRules().getWidth();
        List<Integer> result = new ArrayList<>(width);

        for (int col = 0; col < width; col++) {
            List<String> column = getColumn(populationMember.getNonogramSolutionBoard(), col);
            List<Integer> sequences = populationMember.getNonogramRules().getColumnSequencesLengths().get(col);

            int maxCorrect = evaluateColumnFromTop(column, sequences);
            result.add(maxCorrect);
        }

        return result;
    }

    private int evaluateColumnFromTop(List<String> column, List<Integer> sequences) {
        int seqIdx = 0;
        int currentLength = sequences.get(seqIdx);
        int coloured = 0;
        int maxCorrect = 0;

        for (int row = 0; row < column.size(); row++) {
            String cell = column.get(row);

            if (X_FIELD.equals(cell)) {
                if (isSequenceComplete(coloured, currentLength)) {
                    seqIdx++;
                    if (seqIdx < sequences.size()) {
                        currentLength = sequences.get(seqIdx);
                    }
                } else if (coloured > 0) {
                    return row - 1;
                }

                coloured = 0;

                if (!hasEnoughSpace(row, seqIdx, sequences, column.size())) {
                    return row - 1;
                }

                maxCorrect = row;

            } else {
                coloured++;

                if (isOverflow(coloured, currentLength, seqIdx, sequences.size())) {
                    return row - 1;
                }

                if (row == column.size() - 1) {
                    maxCorrect = row;
                }
            }
        }

        return maxCorrect;
    }

    private boolean isSequenceComplete(int coloured, int currentLength) {
        return coloured == currentLength;
    }

    private boolean hasEnoughSpace(int currentRow, int sequenceIndex, List<Integer> sequences, int columnHeight) {
        int fieldsLeft = columnHeight - 1 - currentRow;
        int fieldsNeeded = calculateFieldsNeeded(sequenceIndex, sequences, "fromTop");
        return fieldsLeft >= fieldsNeeded;
    }

    private boolean isOverflow(int coloured, int currentLength, int seqIdx, int totalSequences) {
        return coloured > currentLength || seqIdx >= totalSequences;
    }

    public List<Integer> generateColumnsMaximumCorrectIndexesFromBottom(NonogramLogic populationMember) {
        int width = populationMember.getNonogramRules().getWidth();
        List<Integer> result = new ArrayList<>(width);

        for (int col = 0; col < width; col++) {
            List<String> column = getColumn(populationMember.getNonogramSolutionBoard(), col);
            List<Integer> sequences = populationMember.getNonogramRules().getColumnSequencesLengths().get(col);

            int maxCorrect = evaluateColumnFromBottom(column, sequences, populationMember.getNonogramRules().getHeight());
            result.add(maxCorrect);
        }

        return result;
    }

    private int evaluateColumnFromBottom(List<String> column, List<Integer> sequences, int height) {
        int seqIdx = sequences.size() - 1;
        int currentLength = sequences.get(seqIdx);
        int coloured = 0;
        int maxCorrect = 0;

        for (int row = height - 1; row >= 0; row--) {
            String cell = column.get(row);

            if (X_FIELD.equals(cell)) {
                if (isSequenceComplete(coloured, currentLength)) {
                    seqIdx--;
                    if (seqIdx >= 0) {
                        currentLength = sequences.get(seqIdx);
                    }
                } else if (coloured > 0) {
                    return rowIndexFromBottom(height, row);
                }

                coloured = 0;

                if (!hasEnoughSpaceFromBottom(row, seqIdx, sequences)) {
                    return rowIndexFromBottom(height, row);
                }

                maxCorrect = height - 1 - row;

            } else {
                coloured++;

                if (isOverflowFromBottom(coloured, currentLength)) {
                    return rowIndexFromBottom(height, row);
                }

                if (row == 0) {
                    maxCorrect = rowIndexFromBottom(height, row);
                }
            }
        }

        return maxCorrect;
    }

    private int rowIndexFromBottom(int height, int row) {
        return height - (row + 1);
    }

    private boolean isOverflowFromBottom(int coloured, int currentLength) {
        return coloured > currentLength;
    }

    private boolean hasEnoughSpaceFromBottom(int currentRow, int sequenceIndex, List<Integer> sequences) {
        int fieldsNeeded = calculateFieldsNeeded(sequenceIndex, sequences, "fromBottom");
        return currentRow >= fieldsNeeded;
    }

    public int calculateFieldsNeeded(int currentSeqNo, List<Integer> sequencesLengths, String direction) {
        int sequencesLengthsSum = 0;
        int sequencesCount = 0;
        if (direction.equals("fromTop")) {
            for (int seqNo = 0; seqNo < sequencesLengths.size(); seqNo++) {
                if (seqNo >= currentSeqNo) {
                    sequencesCount++;
                    sequencesLengthsSum += sequencesLengths.get(seqNo);
                }
            }
        } else {
            for (int seqNo = sequencesLengths.size() - 1; seqNo >= 0; seqNo--) {
                if (seqNo <= currentSeqNo) {
                    sequencesCount++;
                    sequencesLengthsSum += sequencesLengths.get(seqNo);
                }
            }
        }

        return sequencesLengthsSum + sequencesCount - 1;
    }

    public List<String> generateRandomRowArray(NonogramLogic populationMember, int rowIdx) {
        List<String> rowArray = new ArrayList<>();

        NonogramLogic nonogramLogicTmp = gson.fromJson(gson.toJson(populationMember), NonogramLogic.class);
        List<List<Integer>> rowSequencesRanges = nonogramLogicTmp.getRowsSequencesRanges().get(rowIdx);
        List<Integer> rowSequencesLengths = nonogramLogicTmp.getNonogramRules().getRowSequencesLengths().get(rowIdx);

        List<Integer> sequenceRange;
        Integer sequenceLength;
        List<Integer> startPointPossibleIndexes;
        int randomStartPointIndex;
        int chosenStartPointIndex;
        List<Integer> updatedRange;

        for (int rowSequence = 0; rowSequence < rowSequencesRanges.size(); rowSequence++) {
            sequenceRange = rowSequencesRanges.get(rowSequence);
            sequenceLength = rowSequencesLengths.get(rowSequence);

                startPointPossibleIndexes = generateStartIndexesForSequence(sequenceRange, sequenceLength);

                if (startPointPossibleIndexes.size() != 1) {
                    randomStartPointIndex = randomIntGenerator.nextInt(startPointPossibleIndexes.size());
                    chosenStartPointIndex = startPointPossibleIndexes.get(randomStartPointIndex);
                } else {
                    chosenStartPointIndex = startPointPossibleIndexes.get(0);
                }

                updatedRange = new ArrayList<>();
                updatedRange.add(chosenStartPointIndex);
                updatedRange.add(chosenStartPointIndex + sequenceLength - 1);

                rowSequencesRanges = setRowSequenceRange(rowSequencesRanges, rowSequence, updatedRange);

                if (rowSequence + 1 < rowSequencesRanges.size()) {
                    updatedRange = new ArrayList<>();
                    updatedRange.add(chosenStartPointIndex + sequenceLength + 1);
                    updatedRange.add(rowSequencesRanges.get(rowSequence + 1).get(1));
                    rowSequencesRanges = setRowSequenceRange(rowSequencesRanges, rowSequence + 1, updatedRange);
                }
        }

        List<List<Integer>> indexesToFillRanges = new ArrayList<>();

        for (List<Integer> rowSequencesRange : rowSequencesRanges) {
            indexesToFillRanges.add(findAllIntegersInRange(rowSequencesRange));
        }

        List<Integer> indexesToColour = flattenArray(indexesToFillRanges);

        for (int j = 0; j < this.getNonogramObject().getNonogramRules().getWidth(); j++) {
            if (indexesToColour.contains(j)) {
                rowArray.add(COLOURED_FIELD);
            } else {
                rowArray.add(X_FIELD);
            }
        }

        return rowArray;
    }
    
    public List<Integer> flattenArray(List<List<Integer>> rangesIndexes) {
        List<Integer> flattenedArray = new ArrayList<>();
        List<Integer> rangeIndexes;
        for (List<Integer> rangesIndex : rangesIndexes) {
            rangeIndexes = rangesIndex;

            flattenedArray.addAll(rangeIndexes);
        }

        return flattenedArray;
    } 

    public List<List<Integer>> setRowSequenceRange(List<List<Integer>> sequencesRanges, int sequenceIdx, List<Integer> updatedRange) {
        sequencesRanges.set(sequenceIdx, updatedRange);

        return sequencesRanges;
    }

    public List<Integer> generateStartIndexesForSequence(List<Integer> sequenceRange, Integer sequenceLength) {
        List<Integer> startIndexRange = new ArrayList<>();
        startIndexRange.add(sequenceRange.get(0));
        startIndexRange.add(sequenceRange.get(1) - sequenceLength + 1);

        return findAllIntegersInRange(startIndexRange);
    }

    public List<Integer> findAllIntegersInRange(List<Integer> range) {
        List<Integer> integersInRange = new ArrayList<>();

        for (int startIndex = range.get(0); startIndex <= range.get(1); startIndex++) {
            integersInRange.add(startIndex);
        }

        return integersInRange;
    }

    public boolean boardInPopulationUnique(List<List<String>> boardToCheck, List<NonogramLogic> population) {
        List<List<String>> populationBoard;

        if (population.isEmpty()) {
            return true;
        } else {
            for (NonogramLogic populationMember : population) {
                populationBoard = populationMember.getNonogramSolutionBoard();
                if (areBoardsIdentical(boardToCheck, populationBoard)) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean areBoardsIdentical(List<List<String>> boardToCheck, List<List<String>> populationBoard) {
        List<String> boardToCheckRow;
        List<String> populationBoardRow;

        for (int rowIdx = 0; rowIdx < boardToCheck.size(); rowIdx++) {
            boardToCheckRow = boardToCheck.get(rowIdx);
            populationBoardRow = populationBoard.get(rowIdx);

            for (int colIdx = 0; colIdx < boardToCheckRow.size(); colIdx++) {
                if (!boardToCheckRow.get(colIdx).equals(populationBoardRow.get(colIdx))) {
                    return false;
                }
            }
        }

        return true;
    }
}
