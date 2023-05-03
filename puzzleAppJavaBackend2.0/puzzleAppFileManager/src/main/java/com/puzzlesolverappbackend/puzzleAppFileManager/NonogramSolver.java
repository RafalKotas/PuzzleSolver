package com.puzzlesolverappbackend.puzzleAppFileManager;

import com.google.gson.Gson;
import com.puzzlesolverappbackend.puzzleAppFileManager.logs.nonogram.NonogramGuessActionsLog;
import com.puzzlesolverappbackend.puzzleAppFileManager.logs.nonogram.NonogramNodeLog;
import com.puzzlesolverappbackend.puzzleAppFileManager.payload.NonogramLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.services.NonogramLogicService;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class NonogramSolver {

    final int maxTreeHeight = 2;

    boolean printNodeCompletionPercentage = false;

    NonogramLogicService nonogramLogicService = new NonogramLogicService();

    private boolean solved = false;

    // tree root
    private NonogramSolutionNode rootNode;
    // all fields coloured/x placed - setting in only one place inside recursion
    private NonogramSolutionNode solutionNode;
    // partial or full solution logic provided by solver
    private NonogramLogic finalSolutionLogic;

    // file with correct full solution (exists or not)
    private String solutionFileName;

    private NonogramLogic solutionLogic;

    private Gson gsonObj;

    public NonogramSolver(NonogramLogic nonogramLogic, String fileName) {
        gsonObj = new Gson();

        this.solutionNode = new NonogramSolutionNode(nonogramLogic);

        this.solutionNode.setNonogramLogic(nonogramLogic);

        this.rootNode = gsonObj.fromJson(gsonObj.toJson(this.solutionNode), NonogramSolutionNode.class);

        this.finalSolutionLogic = new NonogramLogic(nonogramLogic.getRowsSequences(),
                nonogramLogic.getColumnsSequences());
        // optional correct solution
        this.solutionFileName = "r" + fileName;
        System.out.println("solution filename: " + this.solutionFileName);
    }

    public void runSolutionAtNode(NonogramSolutionNode nonogramSubsolutionNode) {
        this.runHeuristicSolver(nonogramSubsolutionNode, true, false, 0, maxTreeHeight);
    }

    public void makeBasicSolverActions(NonogramSolutionNode nonogramSubsolutionNode) {

        boolean solutionInvalid = nonogramSubsolutionNode.getNonogramLogic().isSolutionInvalid();

        boolean sequencesRangesDiffered;
        boolean solutionBoardsDiffered;
        boolean sequenceIndexesNotToIncludeAddedCondition;

        do {

            NonogramSolutionNode nodeBeforeActionsMade = gsonObj.fromJson(gsonObj.toJson(nonogramSubsolutionNode), NonogramSolutionNode.class);

            for(int actionNo = 1; actionNo < 19; actionNo++) {
                //make action depending on actionNo
                nonogramSubsolutionNode.getNonogramLogic().basicSolve(actionNo);

                solutionInvalid = nonogramSubsolutionNode.getNonogramLogic().isSolutionInvalid(); //+
            }

            NonogramSolutionNode nodeAfterActionsMade = gsonObj.fromJson(gsonObj.toJson(nonogramSubsolutionNode), NonogramSolutionNode.class);

            sequencesRangesDiffered = sequencesRangesAfterActionsMadeDiffers(nodeBeforeActionsMade, nodeAfterActionsMade);

            solutionBoardsDiffered = solutionBoardsDiffers(nodeBeforeActionsMade.getNonogramLogic().getNonogramSolutionBoard(),
                    nodeAfterActionsMade.getNonogramLogic().getNonogramSolutionBoard());

            sequenceIndexesNotToIncludeAddedCondition = sequenceIndexesNotToIncludeAdded(nodeBeforeActionsMade, nodeAfterActionsMade);


            //solutionInvalid = nonogramSubsolutionNode.getNonogramLogic().isSolutionInvalid(); //-
        } while (
            //stepsCountDiffered && !solutionInvalid
                (sequencesRangesDiffered || solutionBoardsDiffered || sequenceIndexesNotToIncludeAddedCondition) && !solutionInvalid
        );
    }

    public boolean sequencesRangesAfterActionsMadeDiffers(NonogramSolutionNode nodeBeforeActionsMade, NonogramSolutionNode nodeAfterActionsMade) {

        //node rows sequences ranges

        List<List<List<Integer>>> nodeBeforeActionsMadeRowsSequencesRanges = nodeBeforeActionsMade.getNonogramLogic().getRowsSequencesRanges();
        List<List<List<Integer>>> nodeAfterActionsMadeRowsSequencesRanges = nodeAfterActionsMade.getNonogramLogic().getRowsSequencesRanges();

        Iterator<List<List<Integer>>> nodeBeforeActionsMadeRowsSequencesRangesIterator = nodeBeforeActionsMadeRowsSequencesRanges.iterator();
        Iterator<List<List<Integer>>> nodeAfterActionsMadeRowsSequencesRangesIterator = nodeAfterActionsMadeRowsSequencesRanges.iterator();

        while(nodeBeforeActionsMadeRowsSequencesRangesIterator.hasNext() && nodeAfterActionsMadeRowsSequencesRangesIterator.hasNext()) {

            List<List<Integer>> nodeBeforeActionsMadeRowSequencesRanges = nodeBeforeActionsMadeRowsSequencesRangesIterator.next();
            List<List<Integer>> nodeAfterActionsMadeRowSequencesRanges = nodeAfterActionsMadeRowsSequencesRangesIterator.next();

            if(!NonogramSolver.sequencesRangesEqual(nodeBeforeActionsMadeRowSequencesRanges, nodeAfterActionsMadeRowSequencesRanges)) {
                return true;
            }
        }

        // node columns sequences ranges
        List<List<List<Integer>>> nodeBeforeActionsMadeColumnsSequencesRanges = nodeBeforeActionsMade.getNonogramLogic().getColumnsSequencesRanges();
        List<List<List<Integer>>> nodeAfterActionsMadeColumnsSequencesRanges = nodeAfterActionsMade.getNonogramLogic().getColumnsSequencesRanges();

        Iterator<List<List<Integer>>> nodeBeforeActionsMadeColumnsSequencesRangesIterator = nodeBeforeActionsMadeColumnsSequencesRanges.iterator();
        Iterator<List<List<Integer>>> nodeAfterActionsMadeColumnsSequencesRangesIterator = nodeAfterActionsMadeColumnsSequencesRanges.iterator();

        while(nodeBeforeActionsMadeColumnsSequencesRangesIterator.hasNext() && nodeAfterActionsMadeColumnsSequencesRangesIterator.hasNext()) {

            List<List<Integer>> nodeBeforeActionsMadeColumnSequencesRanges = nodeBeforeActionsMadeColumnsSequencesRangesIterator.next();
            List<List<Integer>> nodeAfterActionsMadeColumnSequencesRanges = nodeAfterActionsMadeColumnsSequencesRangesIterator.next();

            if(!NonogramSolver.sequencesRangesEqual(nodeBeforeActionsMadeColumnSequencesRanges, nodeAfterActionsMadeColumnSequencesRanges)) {
                return true;
            }

        }

        return false;
    }

    public static boolean sequencesRangesEqual(List<List<Integer>> sequencesRanges_A, List<List<Integer>> sequenceRanges_B) {
        Iterator<List<Integer>> sequencesRanges_AIterator = sequencesRanges_A.iterator();
        Iterator<List<Integer>> sequenceRanges_BIterator = sequenceRanges_B.iterator();

        List<Integer> sequenceRange_A;
        List<Integer> sequenceRange_B;

        while(sequencesRanges_AIterator.hasNext() && sequenceRanges_BIterator.hasNext()) {
            sequenceRange_A = sequencesRanges_AIterator.next();
            sequenceRange_B = sequenceRanges_BIterator.next();
            if(!rangesEqual(sequenceRange_A, sequenceRange_B)) {
                return false;
            }
        }

        return true;
    }

    /***
     * range_A in format [A_1, A_2]
     * range_B in format [B_1, B_2]
     ***/
    public static boolean rangesEqual(List<Integer> range_A, List<Integer> range_B) {
        return Objects.equals(range_A.get(0), range_B.get(0)) && Objects.equals(range_A.get(1), range_B.get(1));
    }

    public boolean solutionBoardsDiffers(List<List<String>> solutionBoard_A, List<List<String>> solutionBoard_B) {
        Iterator<List<String>> solutionBoard_ARowsIterator = solutionBoard_A.iterator();
        Iterator<List<String>> solutionBoard_BRowsIterator = solutionBoard_B.iterator();

        List<String> solutionBoard_ARow;
        List<String> solutionBoard_BRow;

        while(solutionBoard_ARowsIterator.hasNext() && solutionBoard_BRowsIterator.hasNext()) {
            solutionBoard_ARow = solutionBoard_ARowsIterator.next();
            solutionBoard_BRow = solutionBoard_BRowsIterator.next();

            if(solutionBoardRowsDiffers(solutionBoard_ARow, solutionBoard_BRow)) {
                return true;
            }
        }

        return false;
    }

    public boolean sequenceIndexesNotToIncludeAdded(NonogramSolutionNode nodeBeforeActionsMade, NonogramSolutionNode nodeAfterActionsMade) {
        List<List<Integer>> rowsSequencesIdsNotToIncludeBeforeActionsMade = nodeBeforeActionsMade.getNonogramLogic().getRowsSequencesIdsNotToInclude();
        List<List<Integer>> rowsSequencesIdsNotToIncludeAfterActionsMade = nodeAfterActionsMade.getNonogramLogic().getRowsSequencesIdsNotToInclude();

        Iterator<List<Integer>> rowsSequencesIdsNotToIncludeBeforeActionsMadeIterator = rowsSequencesIdsNotToIncludeBeforeActionsMade.iterator();
        Iterator<List<Integer>> rowsSequencesIdsNotToIncludeAfterActionsMadeIterator = rowsSequencesIdsNotToIncludeAfterActionsMade.iterator();

        List<Integer> rowSequencesIdsNotToIncludeBeforeActionsMade;
        List<Integer> rowSequencesIdsNotToIncludeAfterActionsMade;

        while(rowsSequencesIdsNotToIncludeBeforeActionsMadeIterator.hasNext() && rowsSequencesIdsNotToIncludeAfterActionsMadeIterator.hasNext()) {
            rowSequencesIdsNotToIncludeBeforeActionsMade = rowsSequencesIdsNotToIncludeBeforeActionsMadeIterator.next();
            rowSequencesIdsNotToIncludeAfterActionsMade = rowsSequencesIdsNotToIncludeAfterActionsMadeIterator.next();

            // ids not to include differs only if size of Lists differs (ids are sorted ascending)
            if(!(rowSequencesIdsNotToIncludeBeforeActionsMade.size() == rowSequencesIdsNotToIncludeAfterActionsMade.size()) ) {
                return true;
            }

        }

        List<List<Integer>> columnsSequencesIdsNotToIncludeBeforeActionsMade = nodeBeforeActionsMade.getNonogramLogic().getColumnsSequencesIdsNotToInclude();
        List<List<Integer>> columnsSequencesIdsNotToIncludeAfterActionsMade = nodeAfterActionsMade.getNonogramLogic().getColumnsSequencesIdsNotToInclude();

        Iterator<List<Integer>> columnsSequencesIdsNotToIncludeBeforeActionsMadeIterator = columnsSequencesIdsNotToIncludeBeforeActionsMade.iterator();
        Iterator<List<Integer>> columnsSequencesIdsNotToIncludeAfterActionsMadeIterator = columnsSequencesIdsNotToIncludeAfterActionsMade.iterator();

        List<Integer> columnSequencesIdsNotToIncludeBeforeActionsMade;
        List<Integer> columnSequencesIdsNotToIncludeAfterActionsMade;

        while(columnsSequencesIdsNotToIncludeBeforeActionsMadeIterator.hasNext() && columnsSequencesIdsNotToIncludeAfterActionsMadeIterator.hasNext()) {
            columnSequencesIdsNotToIncludeBeforeActionsMade = columnsSequencesIdsNotToIncludeBeforeActionsMadeIterator.next();
            columnSequencesIdsNotToIncludeAfterActionsMade = columnsSequencesIdsNotToIncludeAfterActionsMadeIterator.next();

            // ids not to include differs only if size of Lists differs (ids are sorted ascending)
            if(!(columnSequencesIdsNotToIncludeBeforeActionsMade.size() == columnSequencesIdsNotToIncludeAfterActionsMade.size()) ) {
                return true;
            }
        }

        return false;
    }

    public boolean solutionBoardRowsDiffers(List<String> solutionBoard_ARow, List<String> solutionBoard_BRow) {
        Iterator<String> solutionBoard_ARowIterator = solutionBoard_ARow.iterator();
        Iterator<String> solutionBoard_BRowIterator = solutionBoard_BRow.iterator();

        String rowField_A;
        String rowField_B;

        while(solutionBoard_ARowIterator.hasNext() && solutionBoard_BRowIterator.hasNext()) {
            rowField_A = solutionBoard_ARowIterator.next();
            rowField_B = solutionBoard_BRowIterator.next();
            if(!rowField_A.equals(rowField_B)) {
                return true;
            }
        }

        return false;
    }

    public void runHeuristicSolver(NonogramSolutionNode nonogramSubsolutionNode,
                                   boolean guessModeEnabled, boolean recursionEnabled,
                                   int currentTreeHeight, int maxTreeHeight) {

        System.out.println("solver run!");

        // for classes clone
        Gson gson = new Gson();

        NonogramSolutionNode leftNodeO;
        NonogramSolutionNode rightNodeX;

        //TODO - set if all fields 'X' or 'O', (now partial solution not invalid)
        if(!nonogramSubsolutionNode.getNonogramLogic().isSolutionInvalid()) {
            this.solutionLogic = nonogramSubsolutionNode.getNonogramLogic();
        }

        NonogramSolutionDecision leftNodeDecision;
        NonogramSolutionDecision rightNodeDecision;
        Optional<NonogramSolutionDecision> correctDecision;
        //NonogramSolutionDecision bestDecisionCoefficientsMax = null;

        //solve without guesses
        makeBasicSolverActions(nonogramSubsolutionNode);

        // heuristic logs
        NonogramNodeLog nonogramNodeLog = new NonogramNodeLog(nonogramSubsolutionNode.getNonogramLogic().getLogs());
        // guess logs
        List<NonogramGuessActionsLog> guessesLogs = new ArrayList<>();
        // guess log
        NonogramGuessActionsLog nonogramGuessActionsLog;

        nonogramSubsolutionNode.getNonogramLogic().getNonogramPrinter().printLogs();

        System.out.println("HEURISTIC, TREE HEIGHT: " + currentTreeHeight + " filled: " + nonogramSubsolutionNode.getNonogramLogic().fieldsFilled() + " ENDS.");

        if(guessModeEnabled) {

            boolean oneOfTwoWrong;

            int wrongCount;

            do {
                nonogramSubsolutionNode.getNonogramLogic().updateCurrentAvailableChoices();
                nonogramSubsolutionNode.getNonogramLogic().clearLogs();

                correctDecision = Optional.empty();

                // two decisions are 'possibly' ok
                oneOfTwoWrong = false;

                wrongCount = -1;

                for(NonogramSolutionDecision decision : nonogramSubsolutionNode.getNonogramLogic().getAvailableChoices()) {

                    leftNodeO = copyNodeAndAddDecision(decision, "O", nonogramSubsolutionNode);
                    rightNodeX = copyNodeAndAddDecision(decision, "X", nonogramSubsolutionNode);
                    NonogramSolutionDecision tmpDecision = gson.fromJson(gson.toJson(decision), NonogramSolutionDecision.class);

                    if(!leftNodeO.getNonogramLogic().isSolutionInvalid()) {
                        if(rightNodeX.getNonogramLogic().isSolutionInvalid()) {
                            decision.setDecisionMarker("O");
                            correctDecision = Optional.of(decision);
                            nonogramSubsolutionNode = gson.fromJson(gson.toJson(leftNodeO), NonogramSolutionNode.class);
                            this.solutionLogic = nonogramSubsolutionNode.getNonogramLogic();
                            oneOfTwoWrong = true;
                            wrongCount = 1;
                            break;
                        } else {
                            wrongCount = 0;
                        }
                    } else { //leftNode0 solution invalid
                        if(rightNodeX.getNonogramLogic().isSolutionInvalid()) {
                            wrongCount = 2;
                        } else {
                            decision.setDecisionMarker("X");
                            correctDecision = Optional.of(decision);
                            nonogramSubsolutionNode = gson.fromJson(gson.toJson(rightNodeX), NonogramSolutionNode.class);
                            this.solutionLogic = nonogramSubsolutionNode.getNonogramLogic();
                            oneOfTwoWrong = true;
                            wrongCount = 1;
                        }
                        break;
                    }
                }

                //only guesses, not recursive - can replace solution with more overall completeness %
                if(currentTreeHeight == 0) {
                    // one of decision wrong -> another is correct
                    if(oneOfTwoWrong) {

                        this.replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);

                        nonogramGuessActionsLog = new NonogramGuessActionsLog(correctDecision.get(), this.solutionLogic.getLogs());
                        guessesLogs.add(nonogramGuessActionsLog);
                    }
                } else {
                    //fully completion while recursive solving
                    if(nonogramSubsolutionNode.getNonogramLogic().fieldsFilled() == nonogramSubsolutionNode.getNonogramLogic().area()) {
                        this.replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
                    }
                }
            } while (
                    wrongCount != 2 && oneOfTwoWrong
            );

            if(wrongCount == 0) {
                if(currentTreeHeight == 0) {
                    nonogramNodeLog.setGuessesLogs(guessesLogs);
                    this.solutionNode.addNodeLog(nonogramNodeLog);
                    this.solutionLogic = nonogramSubsolutionNode.getNonogramLogic();
                    nonogramSubsolutionNode.getNonogramLogic().getNonogramPrinter().printLogs();
                    System.out.println("Need to use recursion, filled at start: " + nonogramSubsolutionNode.getNonogramLogic().fieldsFilled());


                    NonogramSolutionDecision decisionCoefficientsMax = null;
                    int maxNextFilled = 0;
                    int leftNodeFilled;
                    int rightNodeFilled;
                    // choose one decision where both sub-solutions are possibly correct
                    for(NonogramSolutionDecision decision : nonogramSubsolutionNode.getNonogramLogic().getAvailableChoices()) {
                        //left node decision - place "O" at specific place
                        leftNodeO = copyNodeAndAddDecision(decision, "O", nonogramSubsolutionNode);
                        makeBasicSolverActions(leftNodeO);
                        leftNodeFilled = leftNodeO.getNonogramLogic().fieldsFilled();

                        //right node decision - place "X" at specific place
                        rightNodeX = copyNodeAndAddDecision(decision, "X", nonogramSubsolutionNode);
                        makeBasicSolverActions(rightNodeX);
                        rightNodeFilled = rightNodeX.getNonogramLogic().fieldsFilled();

                        if(maxNextFilled < Math.max(leftNodeFilled, rightNodeFilled)) {
                            decisionCoefficientsMax = gson.fromJson(gson.toJson(decision), NonogramSolutionDecision.class);
                            maxNextFilled = Math.max(leftNodeFilled, rightNodeFilled);
                            System.out.println("-".repeat(10));
                            System.out.printf("decision (rowIdx, columnIdx): (%d, %d)\n", decision.getRowIdx(), decision.getColumnIdx());
                            System.out.printf("leftNodeO filled: %d, rightNodeX filled: %d\n.", leftNodeFilled, rightNodeFilled);
                            System.out.println("-".repeat(10));
                        }
                    }
                    NonogramSolutionNode leftNodeRecursive = gson.fromJson(gson.toJson(nonogramSubsolutionNode), NonogramSolutionNode.class);
                    decisionCoefficientsMax.setDecisionMarker("O");
                    leftNodeRecursive.addDecision(decisionCoefficientsMax);
                    leftNodeRecursive.colourOrPlaceX();
                    makeBasicSolverActions(leftNodeRecursive);

                    NonogramSolutionNode rightNodeRecursive = gson.fromJson(gson.toJson(nonogramSubsolutionNode), NonogramSolutionNode.class);
                    decisionCoefficientsMax.setDecisionMarker("X");
                    rightNodeRecursive.addDecision(decisionCoefficientsMax);
                    rightNodeRecursive.colourOrPlaceX();
                    makeBasicSolverActions(rightNodeRecursive);

                    runHeuristicSolver(leftNodeRecursive, true, true, currentTreeHeight + 1, maxTreeHeight);
                    runHeuristicSolver(rightNodeRecursive, true, true, currentTreeHeight + 1, maxTreeHeight);
                } else if(recursionEnabled && currentTreeHeight < maxTreeHeight) {
                    if(nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage() == 100) {
                        this.solutionLogic = nonogramSubsolutionNode.getNonogramLogic();
                    } else {
                        NonogramSolutionDecision decisionCoefficientsMax = null;
                        int maxNextFilled = 0;
                        int leftNodeFilled;
                        int rightNodeFilled;
                        System.out.println("Need to use recursion, filled at start: " + nonogramSubsolutionNode.getNonogramLogic().fieldsFilled());

                        for(NonogramSolutionDecision decision : nonogramSubsolutionNode.getNonogramLogic().getAvailableChoices()) {
                            leftNodeO = copyNodeAndAddDecision(decision, "O", nonogramSubsolutionNode);
                            makeBasicSolverActions(leftNodeO);
                            leftNodeFilled = leftNodeO.getNonogramLogic().fieldsFilled();

                            rightNodeX = copyNodeAndAddDecision(decision, "X", nonogramSubsolutionNode);
                            makeBasicSolverActions(rightNodeX);
                            rightNodeFilled = rightNodeX.getNonogramLogic().fieldsFilled();


                            System.out.println("-".repeat(10));
                            System.out.printf("decision (rowIdx, columnIdx): (%d, %d)\n", decision.getRowIdx(), decision.getColumnIdx());
                            System.out.printf("leftNodeO filled: %d, rightNodeX filled: %d\n.", leftNodeFilled, rightNodeFilled);
                            System.out.println("-".repeat(10));

                            if(maxNextFilled < Math.max(leftNodeFilled, rightNodeFilled)) {
                                decisionCoefficientsMax = gson.fromJson(gson.toJson(decision), NonogramSolutionDecision.class);
                                maxNextFilled = Math.max(leftNodeFilled, rightNodeFilled);
                            }
                        }
                        NonogramSolutionNode leftNodeRecursive = copyNodeAndAddDecision(decisionCoefficientsMax, "O", nonogramSubsolutionNode);
                        NonogramSolutionNode rightNodeRecursive = copyNodeAndAddDecision(decisionCoefficientsMax, "X", nonogramSubsolutionNode);

                        runHeuristicSolver(leftNodeRecursive, true, true, currentTreeHeight + 1, maxTreeHeight);
                        runHeuristicSolver(rightNodeRecursive, true, true, currentTreeHeight + 1, maxTreeHeight);
                    }
                }
            } else if(wrongCount == 2) {
                System.out.println("Solver ends at node, both decisions wrong");
            }
        } else {
            System.out.println("currentTreeHeight: " + currentTreeHeight + " final result invalid decisions: (" + nonogramSubsolutionNode.getNonogramGuessDecisions() + ")");
        }
    }

    public NonogramSolutionNode copyNodeAndAddDecision(NonogramSolutionDecision decision, String decisionMarker, NonogramSolutionNode nodeToCopy) {
        Gson gson = new Gson();
        NonogramSolutionDecision nodeDecision = new NonogramSolutionDecision(decisionMarker, decision.getRowIdx(), decision.getColumnIdx());
        NonogramSolutionNode nodeToAddDecision = gson.fromJson(gson.toJson(nodeToCopy), NonogramSolutionNode.class);
        nodeToAddDecision.addDecision(nodeDecision);
        nodeToAddDecision.colourOrPlaceX();
        makeBasicSolverActions(nodeToAddDecision);
        return gson.fromJson(gson.toJson(nodeToAddDecision), NonogramSolutionNode.class);
    }

    //replace solutionNode with this with higher completion percentage
    public void replaceSolutionNodeWithMoreBeneficialSolution(NonogramSolutionNode nodeToCheck) {
        Gson gson = new Gson();

        double oldNodeCompletionPercentage = this.solutionNode.getNonogramLogic().getCompletionPercentage();
        double nodeToCheckCompletionPercentage = nodeToCheck.getNonogramLogic().getCompletionPercentage();

        if(printNodeCompletionPercentage) {
            System.out.println("old Node cp: " + oldNodeCompletionPercentage);
            System.out.println("current Node cp: "+ nodeToCheckCompletionPercentage);
        }

        this.solutionNode = nodeToCheckCompletionPercentage > oldNodeCompletionPercentage
                ? gson.fromJson(gson.toJson(nodeToCheck), NonogramSolutionNode.class)
                : gson.fromJson(gson.toJson(this.solutionNode), NonogramSolutionNode.class);
    }
}