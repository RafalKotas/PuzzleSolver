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

    //final boolean setWithoutCheckingCorrectness = true;

    final int maxTreeHeight = 50;
    long start;
    long end;
    long timeElapsed;

    final int secondTimeLimitForNode = 100;
    final int secondTimeLimitTrialAndError = 100;

    boolean printNodeCompletionPercentage = false;

    NonogramLogicService nonogramLogicService = new NonogramLogicService();

    private boolean solved = false;

    private boolean oneOfTwoWrong;

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

        this.solutionLogic = new NonogramLogic(nonogramLogic.getRowsSequences(), nonogramLogic.getColumnsSequences());
        this.finalSolutionLogic = new NonogramLogic(nonogramLogic.getRowsSequences(),
                nonogramLogic.getColumnsSequences());
        // optional correct solution
        this.solutionFileName = "r" + fileName;
        //System.out.println("solution filename: " + this.solutionFileName);
    }

    public void runSolutionAtNode(NonogramSolutionNode nonogramSubsolutionNode) {
        this.runHeuristicSolver(nonogramSubsolutionNode, true, true, 0, maxTreeHeight);
    }

    public void makeBasicSolverActions(NonogramSolutionNode nonogramSubsolutionNode) {

        boolean solutionInvalid = nonogramSubsolutionNode.getNonogramLogic().isSolutionInvalid();

        boolean sequencesRangesDiffered;
        boolean solutionBoardsDiffered;
        boolean sequenceIndexesNotToIncludeAddedCondition;
        boolean rangeChanged = false;

        do {

            NonogramSolutionNode nodeBeforeActionsMade = gsonObj.fromJson(gsonObj.toJson(nonogramSubsolutionNode), NonogramSolutionNode.class);

            for(int actionNo = 1; actionNo < 19; actionNo++) {
                //make action depending on actionNo
                nonogramSubsolutionNode.getNonogramLogic().basicSolve(actionNo);

                solutionInvalid = nonogramSubsolutionNode.getNonogramLogic().isSolutionInvalid(); //+
                //added 25.06.2023
                if(solutionInvalid) {
                    break;
                }

            }

            NonogramSolutionNode nodeAfterActionsMade = gsonObj.fromJson(gsonObj.toJson(nonogramSubsolutionNode), NonogramSolutionNode.class);

            sequencesRangesDiffered = sequencesRangesAfterActionsMadeDiffers(nodeBeforeActionsMade, nodeAfterActionsMade);

            solutionBoardsDiffered = solutionBoardsDiffers(nodeBeforeActionsMade.getNonogramLogic().getNonogramSolutionBoard(),
                    nodeAfterActionsMade.getNonogramLogic().getNonogramSolutionBoard());

            sequenceIndexesNotToIncludeAddedCondition = sequenceIndexesNotToIncludeAdded(nodeBeforeActionsMade, nodeAfterActionsMade);

        } while (
                (sequencesRangesDiffered || solutionBoardsDiffered || sequenceIndexesNotToIncludeAddedCondition) && !solutionInvalid
        );
    }

    public boolean sequencesRangesAfterActionsMadeDiffers(NonogramSolutionNode nodeBeforeActionsMade, NonogramSolutionNode nodeAfterActionsMade) {

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

        // for classes clone
        Gson gson = new Gson();

        NonogramSolutionNode leftNodeO;
        NonogramSolutionNode rightNodeX;

        if(!nonogramSubsolutionNode.getNonogramLogic().isSolutionInvalid()) {
            if(currentTreeHeight == 0) {
                replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
            } else if(nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage() == 100) {
                System.out.println("Solution found, recursion depth: " + currentTreeHeight);
                nonogramSubsolutionNode.getNonogramLogic().getNonogramPrinter().printNonogramBoard();
                replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
            }
        }

        Optional<NonogramSolutionDecision> correctDecision;

        //solve without guesses - only heuristics
        makeBasicSolverActions(nonogramSubsolutionNode);

        // heuristic logs
        NonogramNodeLog nonogramNodeLog = new NonogramNodeLog(nonogramSubsolutionNode.getNonogramLogic().getLogs());
        // guess logs
        List<NonogramGuessActionsLog> guessesLogs = new ArrayList<>();
        // guess log
        NonogramGuessActionsLog nonogramGuessActionsLog;

        if(currentTreeHeight == 0) {
            replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
        }

        //nonogramSubsolutionNode.getNonogramLogic().getNonogramPrinter().printLogs();

        if(guessModeEnabled && nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage() != 100) {
            System.out.println("Guess mode needed!");

            int wrongCount;

            NonogramSolutionDecision nodeDecision;

            do {
                nonogramSubsolutionNode.getNonogramLogic().updateCurrentAvailableChoices();
                nonogramSubsolutionNode.getNonogramLogic().clearLogs();

                correctDecision = Optional.empty();

                // two decisions are 'possibly' ok
                oneOfTwoWrong = false;

                wrongCount = -1;

                List<NonogramSolutionDecision> availableChoices = nonogramSubsolutionNode.getNonogramLogic().getAvailableChoices();
                start = System.currentTimeMillis();
                for(NonogramSolutionDecision decision : availableChoices) {

                    gson = new Gson();
                    nodeDecision = new NonogramSolutionDecision("O", decision.getRowIdx(), decision.getColumnIdx());
                    leftNodeO = gson.fromJson(gson.toJson(nonogramSubsolutionNode), NonogramSolutionNode.class);
                    leftNodeO.addDecision(nodeDecision);
                    leftNodeO.colourOrPlaceX();
                    makeBasicSolverActions(leftNodeO);

                    nodeDecision = new NonogramSolutionDecision("X", decision.getRowIdx(), decision.getColumnIdx());
                    rightNodeX = gson.fromJson(gson.toJson(nonogramSubsolutionNode), NonogramSolutionNode.class);
                    rightNodeX.addDecision(nodeDecision);
                    rightNodeX.colourOrPlaceX();
                    makeBasicSolverActions(rightNodeX);

                    if(!leftNodeO.getNonogramLogic().isSolutionInvalid()) {
                        if(rightNodeX.getNonogramLogic().isSolutionInvalid()) {
                            decision.setDecisionMarker("O");
                            correctDecision = Optional.of(decision);
                            nonogramSubsolutionNode = gson.fromJson(gson.toJson(leftNodeO), NonogramSolutionNode.class);
                            if(currentTreeHeight == 0) {
                                replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
                            }
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
                            if(currentTreeHeight == 0) {
                                replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
                            }
                            oneOfTwoWrong = true;
                            wrongCount = 1;
                        }
                        break;
                    }

                    end = System.currentTimeMillis();
                    timeElapsed = end - start;
                    if(currentTreeHeight > 0 && timeElapsed > secondTimeLimitTrialAndError && recursionEnabled
                            && ((double) timeElapsed / 1000.0 > secondTimeLimitForNode)) {
                        break;
                    }
                }

                //only guesses, not recursive - can replace solution with more overall completeness %
                if(currentTreeHeight == 0) {
                    // one of decision wrong -> another is correct, or full solution on currentTreeHeight == 0
                    if(correctDecision.isPresent()) {
                        nonogramGuessActionsLog = new NonogramGuessActionsLog(correctDecision.get(), nonogramSubsolutionNode.getNonogramLogic().getLogs());
                        guessesLogs.add(nonogramGuessActionsLog);
                    }
                    if(wrongCount == -1) {
                        //System.out.println("Replace with new solution, available choices: " + nonogramSubsolutionNode.getNonogramLogic().getAvailableChoices().size());
                        this.replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
                    }
                } else {
                    //fully completion while recursive solving
                    if(nonogramSubsolutionNode.getNonogramLogic().fieldsFilled() == nonogramSubsolutionNode.getNonogramLogic().area()) {
                        //System.out.printf("Recursive solved on tree height: %d\n", currentTreeHeight);
                        this.replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
                        System.out.println("Completed, recursion depth: " + currentTreeHeight);
                    }
                }
            } while (
                    guessModeContinueDecision("oneOfTwoWrong")//this.solutionNode.getNonogramGuessDecisions().size() < 1//!this.solutionNode.getNonogramLogic().isSolutionInvalid()
            );

            if(currentTreeHeight == 0) {
                replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
//                System.out.printf("After trial and error method(treeHeight == %d, wrongCount: %d): \n",
//                        currentTreeHeight, wrongCount);
                //nonogramSubsolutionNode.getNonogramLogic().getNonogramPrinter().printStats();
                //this.solutionLogic.getNonogramPrinter().printStats();

                System.out.println("Completion percentage after trial and error(subsolution node, solution logic): ("
                + nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage() +
                        " decisionsLength: " + nonogramSubsolutionNode.getNonogramGuessDecisions().size());
                //+ ", " + this.solutionNode.getNonogramLogic().getCompletionPercentage() + ") wrongCount: " + wrongCount);
            }

            if(wrongCount == 0) {
                if(currentTreeHeight == 0) {
                    nonogramNodeLog.setGuessesLogs(guessesLogs);
                    replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);

                    if(nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage() != 100.00) {
                        System.out.println("Need to use recursion, completion percentage at start: " + nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage());

                        this.replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);

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
                            /*System.out.println("-".repeat(10));
                            System.out.printf("decision (rowIdx, columnIdx): (%d, %d)\n", decision.getRowIdx(), decision.getColumnIdx());
                            System.out.printf("leftNodeO filled: %d, rightNodeX filled: %d\n.", leftNodeFilled, rightNodeFilled);
                            System.out.println("-".repeat(10));*/
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
                    } else {
                        System.out.println("Don't need to use recursion. Nonogram solved at ");
                    }
                } else if(recursionEnabled && currentTreeHeight <= maxTreeHeight) {
                    if(nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage() == 100) {
                        System.out.printf("Solution found recursion (tree height: %d)!!!\n", currentTreeHeight);
                        replaceSolutionNodeWithMoreBeneficialSolution(nonogramSubsolutionNode);
                    } else {
                        NonogramSolutionDecision decisionCoefficientsMax = null;
                        int maxNextFilled = 0;
                        int leftNodeFilled;
                        int rightNodeFilled;
                        //System.out.println("Need to use recursion, filled at start: " + nonogramSubsolutionNode.getNonogramLogic().fieldsFilled());

                        start = System.currentTimeMillis();
                        for(NonogramSolutionDecision decision : nonogramSubsolutionNode.getNonogramLogic().getAvailableChoices()) {
                            leftNodeO = copyNodeAndAddDecision(decision, "O", nonogramSubsolutionNode);
                            makeBasicSolverActions(leftNodeO);
                            leftNodeFilled = leftNodeO.getNonogramLogic().fieldsFilled();

                            rightNodeX = copyNodeAndAddDecision(decision, "X", nonogramSubsolutionNode);
                            makeBasicSolverActions(rightNodeX);
                            rightNodeFilled = rightNodeX.getNonogramLogic().fieldsFilled();


                            /*System.out.println("-".repeat(10));
                            System.out.printf("decision (rowIdx, columnIdx): (%d, %d)\n", decision.getRowIdx(), decision.getColumnIdx());
                            System.out.printf("leftNodeO filled: %d, rightNodeX filled: %d\n.", leftNodeFilled, rightNodeFilled);
                            System.out.println("-".repeat(10));*/

                            if(maxNextFilled < Math.max(leftNodeFilled, rightNodeFilled)) {
                                decisionCoefficientsMax = gson.fromJson(gson.toJson(decision), NonogramSolutionDecision.class);
                                maxNextFilled = Math.max(leftNodeFilled, rightNodeFilled);
                            }
                            end = System.currentTimeMillis();
                            timeElapsed = end - start;
                            if((double) timeElapsed / 1000.0 > secondTimeLimitForNode) {
                                break;
                            }
                        }
                        NonogramSolutionNode leftNodeRecursive = copyNodeAndAddDecision(decisionCoefficientsMax, "O", nonogramSubsolutionNode);
                        NonogramSolutionNode rightNodeRecursive = copyNodeAndAddDecision(decisionCoefficientsMax, "X", nonogramSubsolutionNode);

                        runHeuristicSolver(leftNodeRecursive, true, true, currentTreeHeight + 1, maxTreeHeight);
                        runHeuristicSolver(rightNodeRecursive, true, true, currentTreeHeight + 1, maxTreeHeight);
                    }
                }
            } else if(wrongCount == 2) {
                //System.out.printf("Solver ends at node, both decisions wrong (treeHeight: %d, completeness: %,.2f).\n",
                //        currentTreeHeight, nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage());
            }
        } else {
            System.out.println("full solution:");
            try {
                nonogramSubsolutionNode.getNonogramLogic().getNonogramPrinter().printNonogramBoard();
                nonogramSubsolutionNode.getLeftNode().getNonogramLogic().getNonogramPrinter().printNonogramBoard();
                nonogramSubsolutionNode.getRightNode().getNonogramLogic().getNonogramPrinter().printNonogramBoard();
            } catch (Exception e) {
                System.out.println("Node/node logic not exists!!!");
            }
            System.out.println(".".repeat(50));
            System.out.println("currentTreeHeight: " + currentTreeHeight + " , completion percentage without guess enabled: " + nonogramSubsolutionNode.getNonogramLogic().getCompletionPercentage());
        }

        nonogramSubsolutionNode.getNonogramLogic().getNonogramPrinter().printLogs();
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

        if(nodeToCheckCompletionPercentage > oldNodeCompletionPercentage) {
            //System.out.println("completion: " + nodeToCheckCompletionPercentage);
            this.solutionNode = gson.fromJson(gson.toJson(nodeToCheck), NonogramSolutionNode.class);
            this.solutionLogic = nodeToCheck.getNonogramLogic();
            this.solutionLogic.getNonogramPrinter().setNonogramSolutionBoard(this.solutionLogic.getNonogramSolutionBoard());
        } else {
            this.solutionNode = gson.fromJson(gson.toJson(this.solutionNode), NonogramSolutionNode.class);
        }
    }

    private boolean guessModeContinueDecision (String wordExpression) {
        //this.solutionNode.getNonogramGuessDecisions().size() < 1//!this.solutionNode.getNonogramLogic().isSolutionInvalid()
        switch (wordExpression) {
            case "oneOfTwoWrong":
                return this.oneOfTwoWrong;
            case "toFirstDecision":
                return this.solutionNode.getNonogramGuessDecisions().size() < 1;
            case "toInvalidSolution":
                return !this.solutionNode.getNonogramLogic().isSolutionInvalid();
            default:
                return this.oneOfTwoWrong;
        }
    }
}
