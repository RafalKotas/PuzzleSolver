package com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic;

import com.google.gson.Gson;
import com.puzzlesolverappbackend.puzzleAppFileManager.runners.InitializerConstants;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*TODO 26.05
For every population member:
1.(+) Generate arrays (top and bottom) of maximumPossibleCorrect fields
2.(+) Find minimum from top and bottom arrays (point no 1)
----------------------------------------------------------------DONE UP
3. (+) Iterate through every pair of nonograms and shuffle selected row (rows in both nonograms have same index):
a) from top: random from [0, maximumPossibleCorrect - 1]
b) from bottom: random from [height - 1, height - 1 - maximumPossibleCorrect]
----------------------------------------------------------------DONE UP
TODO 27/28.05 - generate new population
4. Choose those nonograms from population which have the best score (sum of maximumPossibleCorrectFields bottom and top)
*/

@Getter
@Setter
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

    private final int populationCount = 120;
    private List<List<Integer>> populationColumnsMaximumCorrectIndexFromTop;
    private List<List<Integer>> populationColumnsMaximumCorrectIndexFromBottom;
    private final double mutationProbability = 0.05;

    private final int iterationsLimit = 10000;

    private final int timeSecondsLimit = 100;

    private Gson gson;

    public NonogramGenetic(NonogramLogic nonogramObject) {
        gson = new Gson();
        this.nonogramObject = gson.fromJson(gson.toJson(nonogramObject), NonogramLogic.class);
        this.randomIntGenerator = new Random();
        this.solutionFound = false;
    }

    public void solve() {
        generateInitialPopulation();
        for(int iterationNo = 0; iterationNo < 20; iterationNo++) {
            generateNextPopulation(iterationNo);
            if(solutionFound) {
                System.out.println("Solution found!!!, iterationNo: " + iterationNo);
                break;
            }
        }
    }

    public void generateInitialPopulation() {
        NonogramLogic populationMember;
        List<String> nonogramBoardRow;
        currentPopulation = new ArrayList<>();
        int repetitionCount = 0;

        populationColumnsMaximumCorrectIndexFromTop = new ArrayList<>();
        populationColumnsMaximumCorrectIndexFromBottom = new ArrayList<>();

        for(int index = 0; index < populationCount; index++) {
            populationMember = gson.fromJson(gson.toJson(nonogramObject), NonogramLogic.class);

            for(int rowIdx = 0; rowIdx < nonogramObject.getHeight(); rowIdx++) {
                nonogramBoardRow =  generateRandomRowArray(populationMember, rowIdx);

                populationMember = populationMember.setNonogramBoardRow(rowIdx, nonogramBoardRow);
            }

            if(boardInPopulationUnique(populationMember.getNonogramSolutionBoard(), currentPopulation)) {
                currentPopulation.add(populationMember);
            } else {
                repetitionCount++;
                index--;
            }
        }
    }

    public void generateNextPopulation(int iterationNo) {
        randomIntGenerator = new Random();
        nextPopulation = new ArrayList<>();
        maxCorrectFieldsInColumnsSums = new ArrayList<>();

        System.out.println("Current population size: " + currentPopulation.size());

        List<Integer> firstMemberColumnsMaximumCorrectIndexFromTop;
        List<Integer> firstMemberColumnsMaximumCorrectIndexFromBottom;

        List<Integer> secondMemberColumnsMaximumCorrectIndexFromTop;
        List<Integer> secondMemberColumnsMaximumCorrectIndexFromBottom;

        NonogramLogic firstPopulationMember;
        NonogramLogic secondPopulationMember;

        int topMinimum;
        int bottomMinimum;
        int choosenSide; // 0 - top, 1 - bottom
        int chosenRowIndex;

        List<String> firstMemberRowToSwap;
        List<String> secondMemberRowToSwap;
        NonogramLogic firstChild;
        NonogramLogic secondChild;

        List<Integer> firstChildColumnsMaximumCorrectIndexFromTop;
        List<Integer> firstChildColumnsMaximumCorrectIndexFromBottom;
        int firstChildMaxPossibleCorrectFieldsSum;

        List<Integer> secondChildColumnsMaximumCorrectIndexFromTop;
        List<Integer> secondChildColumnsMaximumCorrectIndexFromBottom;
        int secondChildMaxPossibleCorrectFieldsSum;

        int crossingCount = 0;
        this.setSolutionFound(false);
            // crossing
        for(int firstPopulationMemberIndex = 0; firstPopulationMemberIndex < populationCount; firstPopulationMemberIndex++) {
            firstPopulationMember = currentPopulation.get(firstPopulationMemberIndex);
            for(int secondPopulationMemberIndex = 0; secondPopulationMemberIndex < populationCount; secondPopulationMemberIndex++) {
                secondPopulationMember = currentPopulation.get(secondPopulationMemberIndex);
                //System.out.println("Population indexes: " + firstPopulationMemberIndex + " and " + secondPopulationMemberIndex);

                //do crossing - fit function parameters (not same members && cross two members only once)
                if(firstPopulationMemberIndex < secondPopulationMemberIndex) {
                    crossingCount++;

                    int firstMemberTopMinimum;
                    int firstMemberBottomMinimum;

                    firstMemberColumnsMaximumCorrectIndexFromTop = generateColumnsMaximumCorrectIndexesFromTop(firstPopulationMember);
                    firstMemberColumnsMaximumCorrectIndexFromBottom = generateColumnsMaximumCorrectIndexesFromBottom(firstPopulationMember);

                    // common minimum from maximum possible column field, f.e. [4, 5, 3, 7, 9, 10] -> 3
                    firstMemberTopMinimum = firstMemberColumnsMaximumCorrectIndexFromTop
                            .stream()
                            .mapToInt(v -> v)
                            .min().orElseThrow(NoSuchElementException::new);
                    firstMemberBottomMinimum = firstMemberColumnsMaximumCorrectIndexFromBottom
                            .stream()
                            .mapToInt(v -> v)
                            .min().orElseThrow(NoSuchElementException::new);

                    int secondMemberTopMinimum;
                    int secondMemberBottomMinimum;

                    secondMemberColumnsMaximumCorrectIndexFromTop = generateColumnsMaximumCorrectIndexesFromTop(secondPopulationMember);
                    secondMemberColumnsMaximumCorrectIndexFromBottom = generateColumnsMaximumCorrectIndexesFromBottom(secondPopulationMember);

                    secondMemberTopMinimum = secondMemberColumnsMaximumCorrectIndexFromTop
                            .stream()
                            .mapToInt(v -> v)
                            .min().orElseThrow(NoSuchElementException::new);
                    secondMemberBottomMinimum = secondMemberColumnsMaximumCorrectIndexFromBottom
                            .stream()
                            .mapToInt(v -> v)
                            .min().orElseThrow(NoSuchElementException::new);

                    topMinimum = Math.min(firstMemberTopMinimum, secondMemberTopMinimum);
                    bottomMinimum = Math.min(firstMemberBottomMinimum, secondMemberBottomMinimum);

                    //choose side top(0) or bottom(1)
                    choosenSide = randomIntGenerator.nextInt(2);
                    if(choosenSide == 0) {
                        chosenRowIndex = randomIntGenerator.nextInt(topMinimum + 1);
                    } else {
                        //chosenRowIndex = ThreadLocalRandom.current().nextInt(nonogramObject.getHeight() - 1 - bottomMinimum, nonogramObject.getHeight() - 1);
                        int minimumBottomIndex = nonogramObject.getHeight() - 1 - bottomMinimum;
                        int maximimumBottomIndex = nonogramObject.getHeight() - 1;
                        chosenRowIndex = randomIntGenerator.nextInt(maximimumBottomIndex - minimumBottomIndex + 1) + minimumBottomIndex;
                    }


                    // row chosen, now select rows and do crossing
                    firstMemberRowToSwap = firstPopulationMember.getNonogramSolutionBoard().get(chosenRowIndex);
                    secondMemberRowToSwap = secondPopulationMember.getNonogramSolutionBoard().get(chosenRowIndex);

                    //copy population members to change them further
                    firstChild = gson.fromJson(gson.toJson(firstPopulationMember), NonogramLogic.class);
                    secondChild = gson.fromJson(gson.toJson(secondPopulationMember), NonogramLogic.class);

                    //insert new rows - create new population members
                    secondChild.setNonogramBoardRow(chosenRowIndex, firstMemberRowToSwap);

                    int randNumTo10 = randomIntGenerator.nextInt(100) + 1;

                    if(randNumTo10 > 95) {
                        //mutate
                        firstChild.setNonogramBoardRow(chosenRowIndex, generateRandomRowArray(firstChild, chosenRowIndex));
                    } else {
                        firstChild.setNonogramBoardRow(chosenRowIndex, secondMemberRowToSwap);
                    }

                    randNumTo10 = randomIntGenerator.nextInt(100) + 1;
                    if(randNumTo10 > 95) {
                        //mutate
                        secondChild.setNonogramBoardRow(chosenRowIndex, generateRandomRowArray(secondChild, chosenRowIndex));
                    } else {
                        secondChild.setNonogramBoardRow(chosenRowIndex, firstMemberRowToSwap);
                    }

                    //
                    firstChildColumnsMaximumCorrectIndexFromTop = generateColumnsMaximumCorrectIndexesFromTop(firstChild);
                    firstChildColumnsMaximumCorrectIndexFromBottom = generateColumnsMaximumCorrectIndexesFromBottom(firstPopulationMember);

                    firstChildMaxPossibleCorrectFieldsSum = firstChildColumnsMaximumCorrectIndexFromTop.stream().reduce(0, Integer::sum);
                    firstChildMaxPossibleCorrectFieldsSum = firstChildColumnsMaximumCorrectIndexFromBottom.stream().reduce(firstChildMaxPossibleCorrectFieldsSum, Integer::sum);

                    secondChildColumnsMaximumCorrectIndexFromTop = generateColumnsMaximumCorrectIndexesFromTop(secondChild);
                    secondChildColumnsMaximumCorrectIndexFromBottom = generateColumnsMaximumCorrectIndexesFromBottom(secondPopulationMember);

                    secondChildMaxPossibleCorrectFieldsSum = secondChildColumnsMaximumCorrectIndexFromTop.stream().reduce(0, Integer::sum);
                    secondChildMaxPossibleCorrectFieldsSum = secondChildColumnsMaximumCorrectIndexFromBottom.stream().reduce(secondChildMaxPossibleCorrectFieldsSum, Integer::sum);

                    //first child
                    if(!nextPopulation.isEmpty()) {
                        int firstLessOrEqualElementIndex = findFirstLessOrEqualElementIndex(firstChildMaxPossibleCorrectFieldsSum);

                        if(firstLessOrEqualElementIndex != populationCount) {
                            nextPopulation.add(firstLessOrEqualElementIndex, firstChild);
                            if(firstChild.subsolutionBoardCorrectComparisonWithSolutionBoard("r" + InitializerConstants.PUZZLE_NAME)) {
                                setSolutionFound(true);
                            }
                            maxCorrectFieldsInColumnsSums.add(firstLessOrEqualElementIndex, firstChildMaxPossibleCorrectFieldsSum);
                            if(nextPopulation.size() > populationCount) {
                                nextPopulation = nextPopulation.stream().limit(populationCount).collect(Collectors.toList());
                                maxCorrectFieldsInColumnsSums = maxCorrectFieldsInColumnsSums.stream().limit(populationCount).collect(Collectors.toList());
                            }
                        }
                    } else {
                        // just add
                        nextPopulation.add(firstChild);
                        maxCorrectFieldsInColumnsSums.add(0, firstChildMaxPossibleCorrectFieldsSum);
                        System.out.println("added first element to next population/max sums");
                    }

                    int firstLessOrEqualElementIndex = findFirstLessOrEqualElementIndex(secondChildMaxPossibleCorrectFieldsSum);

                    if(firstLessOrEqualElementIndex != populationCount) {
                        nextPopulation.add(firstLessOrEqualElementIndex, secondChild);
                        maxCorrectFieldsInColumnsSums.add(firstLessOrEqualElementIndex, secondChildMaxPossibleCorrectFieldsSum);
                        if(nextPopulation.size() > populationCount) {
                            nextPopulation = nextPopulation.stream().limit(populationCount).collect(Collectors.toList());
                            maxCorrectFieldsInColumnsSums = maxCorrectFieldsInColumnsSums.stream().limit(populationCount).collect(Collectors.toList());
                        }
                    }
                }
                if (isSolutionFound()) break;
            }
            if (isSolutionFound()) break;
        }

        System.out.println("next population size: " + nextPopulation.size());
        System.out.println("maxsums size: " + maxCorrectFieldsInColumnsSums.size());
        System.out.println("MIN: " + maxCorrectFieldsInColumnsSums.get(maxCorrectFieldsInColumnsSums.size() - 1));
        System.out.println("MAX: " + maxCorrectFieldsInColumnsSums.get(0));

        if(!solutionFound) {
            selectNewPopulation();
            System.out.println("iterationNo: " + iterationNo + " solution not found...");
        } else {
            System.out.println("Solution found: ");
        }
    }

    public void selectNewPopulation() {
        this.currentPopulation = new ArrayList<>();
        this.currentPopulation.addAll(nextPopulation);
    }

    public int findFirstLessOrEqualElementIndex(int elementToFind) {
        OptionalInt firstIndex = IntStream.range(0, maxCorrectFieldsInColumnsSums.size())
                .filter(i -> maxCorrectFieldsInColumnsSums.get(i) <= elementToFind)
                .findFirst();

        if(firstIndex.isPresent()) {
            return firstIndex.getAsInt();
        } else {
            return populationCount;
        }
    }

    /***
     * @param populationMember - nonogramLogic object - nonogram representation
     * @return list of maximum possible correct filled (or not) fields in each nonogram column
     */
    public List<Integer> generateColumnsMaximumCorrectIndexesFromTop(NonogramLogic populationMember) {

        List<Integer> columnsMaximumCorrectIndexFromTop = new ArrayList<>();
        List<String> boardColumn;
        List<Integer> columnSequencesLengths;

        // from top
        int currentSequenceNo;
        int currentSequenceLength;
        int colouredInRow;
        int fieldsLeft;
        int fieldsNeeded;
        int maxProbablyCorrect;

        for(int columnIdx = 0; columnIdx < populationMember.getWidth(); columnIdx++) {
            boardColumn = populationMember.getSolutionBoardColumn(columnIdx);
            columnSequencesLengths = populationMember.getColumnsSequences().get(columnIdx);

            // from top
            currentSequenceNo = 0;
            currentSequenceLength = columnSequencesLengths.get(0);
            colouredInRow = 0;
            maxProbablyCorrect = 0;

            for(int rowIdx = 0;  rowIdx < populationMember.getHeight(); rowIdx++) {
                if(boardColumn.get(rowIdx).equals("X")) {
                    if(colouredInRow == currentSequenceLength) {
                        currentSequenceNo++;
                        if(currentSequenceNo < columnSequencesLengths.size()) {
                            currentSequenceLength = columnSequencesLengths.get(currentSequenceNo);
                        }
                    } else if (colouredInRow > 0 && colouredInRow < currentSequenceLength) {
                        maxProbablyCorrect = rowIdx - 1;
                        break;
                    }
                    colouredInRow = 0;
                    fieldsLeft = nonogramObject.getHeight() - 1 - rowIdx;
                    fieldsNeeded = calculateFieldsNeeded(currentSequenceNo, columnSequencesLengths, "fromTop");
                    if(fieldsLeft < fieldsNeeded && rowIdx > 0) {
                        maxProbablyCorrect = rowIdx - 1;
                        break;
                    } else {
                        maxProbablyCorrect = rowIdx;
                    }
                } else {
                    colouredInRow++;
                    if(colouredInRow > currentSequenceLength || currentSequenceNo >= columnSequencesLengths.size()) {
                        maxProbablyCorrect = rowIdx - 1;
                        break;
                    } else if(rowIdx == populationMember.getHeight() - 1) {
                        maxProbablyCorrect = rowIdx;
                    }
                }
            }

            columnsMaximumCorrectIndexFromTop.add(maxProbablyCorrect);
        }
        return columnsMaximumCorrectIndexFromTop;
    }

    public List<Integer> generateColumnsMaximumCorrectIndexesFromBottom(NonogramLogic populationMember) {

        List<Integer> columnsMaximumCorrectIndexFromBottom = new ArrayList<>();
        List<String> boardColumn;
        List<Integer> columnSequencesLengths;

        // from top
        int currentSequenceNo;
        int currentSequenceLength;
        int colouredInRow;
        int fieldsLeft;
        int fieldsNeeded;
        int maxProbablyCorrect;

        for(int columnIdx = 0; columnIdx < populationMember.getWidth(); columnIdx++) {
            boardColumn = populationMember.getSolutionBoardColumn(columnIdx);
            columnSequencesLengths = populationMember.getColumnsSequences().get(columnIdx);

            //from bottom - analogous
            currentSequenceNo = columnSequencesLengths.size() - 1;
            currentSequenceLength = columnSequencesLengths.get(currentSequenceNo);
            colouredInRow = 0;
            maxProbablyCorrect = 0;

            for(int rowIdx = populationMember.getHeight() - 1;  rowIdx >= 0; rowIdx--) {
                if(boardColumn.get(rowIdx).equals("X")) {
                    if(colouredInRow == currentSequenceLength) {
                        currentSequenceNo--;
                        if(currentSequenceNo >= 0) {
                            currentSequenceLength = columnSequencesLengths.get(currentSequenceNo);
                        }
                    } else if (colouredInRow > 0 && colouredInRow < currentSequenceLength) {
                        maxProbablyCorrect = nonogramObject.getHeight() - (rowIdx + 1);
                        break;
                    }
                    colouredInRow = 0;
                    fieldsLeft = rowIdx;
                    fieldsNeeded = calculateFieldsNeeded(currentSequenceNo, columnSequencesLengths, "fromBottom");
                    if(fieldsLeft < fieldsNeeded && rowIdx > 0) {
                        maxProbablyCorrect = nonogramObject.getHeight() - (rowIdx + 1);
                        break;
                    } else {
                        maxProbablyCorrect = (nonogramObject.getHeight() - 1 - rowIdx);
                    }
                } else {
                    colouredInRow++;
                    if(colouredInRow > currentSequenceLength) {
                        maxProbablyCorrect = nonogramObject.getHeight() - (rowIdx + 1);
                        break;
                    }  else if(rowIdx == 0) {
                        maxProbablyCorrect = nonogramObject.getHeight() - (rowIdx + 1);
                    }
                }
            }
            columnsMaximumCorrectIndexFromBottom.add(maxProbablyCorrect);
        }

        return columnsMaximumCorrectIndexFromBottom;
    }

    public int calculateFieldsNeeded(int currentSeqNo, List<Integer> sequencesLengths, String direction) {
        int sequencesLengthsSum = 0;
        int sequencesCount = 0;
        if(direction.equals("fromTop")) {
            for(int seqNo = 0; seqNo < sequencesLengths.size(); seqNo++) {
                if(seqNo >= currentSeqNo) {
                    sequencesCount++;
                    sequencesLengthsSum += sequencesLengths.get(seqNo);
                }
            }
        } else {
            for(int seqNo = sequencesLengths.size() - 1; seqNo >= 0; seqNo--) {
                if(seqNo <= currentSeqNo) {
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
        List<Integer> rowSequencesLengths = nonogramLogicTmp.getRowsSequences().get(rowIdx);

        List<Integer> sequenceRange;
        Integer sequenceLength;
        List<Integer> startPointPossibleIndexes;
        int randomStartPointIndex;
        int choosenStartPointIndex;
        List<Integer> updatedRange;

        for(int rowSequence = 0; rowSequence < rowSequencesRanges.size(); rowSequence++) {
            sequenceRange = rowSequencesRanges.get(rowSequence);
            sequenceLength = rowSequencesLengths.get(rowSequence);

                startPointPossibleIndexes = generateStartIndexesForSequence(sequenceRange, sequenceLength);

                if(startPointPossibleIndexes.size() != 1) {
                    //randomStartPointIndex =  randomIntGenerator.nextInt(0, startPointPossibleIndexes.size() - 1);
                    randomStartPointIndex = randomIntGenerator.nextInt(startPointPossibleIndexes.size());
                    choosenStartPointIndex = startPointPossibleIndexes.get(randomStartPointIndex);
                } else { //only one possible start index
                    choosenStartPointIndex = startPointPossibleIndexes.get(0);
                }

                updatedRange = new ArrayList<>();
                updatedRange.add(choosenStartPointIndex);
                updatedRange.add(choosenStartPointIndex + sequenceLength - 1);

                rowSequencesRanges = setRowSequenceRange(rowSequencesRanges, rowSequence, updatedRange);

                if(rowSequence + 1 < rowSequencesRanges.size()) {
                    updatedRange = new ArrayList<>();
                    updatedRange.add(choosenStartPointIndex + sequenceLength + 1);
                    updatedRange.add(rowSequencesRanges.get(rowSequence + 1).get(1));
                    rowSequencesRanges = setRowSequenceRange(rowSequencesRanges, rowSequence + 1, updatedRange);
                }
        }

        List<List<Integer>> indexesToFillRanges = new ArrayList<>();

        for (List<Integer> rowSequencesRange : rowSequencesRanges) {
            indexesToFillRanges.add(findAllIntegersInRange(rowSequencesRange));
        }

        List<Integer> indexesToColour = flattenArray(indexesToFillRanges);

        for(int j = 0; j < this.getNonogramObject().getWidth(); j++) {
            if(indexesToColour.contains(j)) {
                rowArray.add("O");
            } else {
                rowArray.add("X");
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
        List<Integer> IntegersInRange = new ArrayList<>();

        for(int startIndex = range.get(0); startIndex <= range.get(1); startIndex++) {
            IntegersInRange.add(startIndex);
        }

        return IntegersInRange;
    }

    /***
     * @param boardToCheck - new board to add to population
     * @param population - population of nonogramLogic with different solutionBoards
     * @return true if boardIsUnique, false in other case
     */
    public boolean boardInPopulationUnique(List<List<String>> boardToCheck, List<NonogramLogic> population) {
        List<List<String>> populationBoard;

        if(population.isEmpty()) {
            return true;
        } else {
            for (NonogramLogic populationMember : population) {
                populationBoard = populationMember.getNonogramSolutionBoard();
                if(areBoardsIdentical(boardToCheck, populationBoard)) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean areBoardsIdentical(List<List<String>> boardToCheck, List<List<String>> populationBoard) {
        List<String> boardToCheckRow;
        List<String> populationBoardRow;

        for(int rowIdx = 0; rowIdx < boardToCheck.size(); rowIdx++) {
            boardToCheckRow = boardToCheck.get(rowIdx);
            populationBoardRow = populationBoard.get(rowIdx);

            for(int colIdx = 0; colIdx < boardToCheckRow.size(); colIdx++) {
                if(!boardToCheckRow.get(colIdx).equals(populationBoardRow.get(colIdx))) {
                    return false;
                }
            }
        }

        return true;
    }
}
