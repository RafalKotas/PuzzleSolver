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

    final int maxTreeHeight = 1;

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

        this.finalSolutionLogic = new NonogramLogic(nonogramLogic.getRowsSequencesLengths(),
                nonogramLogic.getColumnsSequencesLengths());

        // optional correct solution
        this.solutionFileName = "r" + fileName;
        System.out.println("solution filename: " + this.solutionFileName);
        this.solutionLogic = null;
    }

    public void runSolutionAtNode(NonogramSolutionNode nonogramSubsolutionNode) throws CloneNotSupportedException {
        this.runHeuristicSolver(nonogramSubsolutionNode, 0, maxTreeHeight, false);
    }

    /*private int getNonogramHeight() {
        return nonogramLogic.getHeight();
    }*/

    public void makeBasicSolverActions(NonogramSolutionNode nonogramSubsolutionNode) {

        int stepsMadeAtStart;
        int stepsMadeAtEnd;

        //conditions
        boolean stepsCountDiffered;
        boolean solutionInvalid = nonogramSubsolutionNode.getNonogramLogic().isSolutionInvalid();

        //conditions - ver 2
        boolean sequencesRangesDiffered;
        boolean solutionBoardsDiffered;
        boolean sequenceIndexesNotToIncludeAddedCondition;

        /* condition - do while:
            a) ranges changing +?
            b) board marks changing
        */
        do {
            stepsMadeAtStart = nonogramSubsolutionNode.getNonogramLogic().getNewStepsMade();

            NonogramSolutionNode nodeBeforeActionsMade = gsonObj.fromJson(gsonObj.toJson(nonogramSubsolutionNode), NonogramSolutionNode.class);

            for(int actionNo = 1; actionNo < 19; actionNo++) {
                //make action depending on actionNo
                nonogramSubsolutionNode.getNonogramLogic().basicSolve(actionNo);

                solutionInvalid = nonogramSubsolutionNode.getNonogramLogic().isSolutionInvalid(); //+
            }

            stepsMadeAtEnd = nonogramSubsolutionNode.getNonogramLogic().getNewStepsMade();

            stepsCountDiffered = stepsMadeAtStart != stepsMadeAtEnd;

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
        return range_A.get(0) == range_B.get(0) && range_A.get(1) == range_B.get(1);
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

    public void runHeuristicSolver(NonogramSolutionNode nonogramSubsolutionNode, int currentTreeHeight, int maxTreeHeight, boolean recursive) throws CloneNotSupportedException {

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
        Optional<NonogramSolutionNode> wrongNode;
        //NonogramSolutionDecision bestDecisionCoeffsMax = null;

        //solve without guesses
        makeBasicSolverActions(nonogramSubsolutionNode);

        // heuristic logs
        NonogramNodeLog nonogramNodeLog = new NonogramNodeLog(nonogramSubsolutionNode.getNonogramLogic().getLogs());
        // guess logs
        List<NonogramGuessActionsLog> guessesLogs = new ArrayList<>();
        // guess log
        NonogramGuessActionsLog nonogramGuessActionsLog;

        nonogramSubsolutionNode.getNonogramLogic().printLogs();

        System.out.println("HEURISTIC, TREE HEIGHT: " + currentTreeHeight + " filled: " + nonogramSubsolutionNode.getNonogramLogic().fieldsFilled() + " ENDS.");
        //nonogramSubsolutionNode.getNonogramLogic().printNonogramBoard();
        //nonogramSubsolutionNode.printNodeFinalResult();
        //System.out.println("-".repeat(31));

        boolean guessModeEnabled = true; //currentTreeHeight <= maxTreeHeight && !nonogramSubsolutionNode.getNonogramLogic().isSolutionInvalid()

        if(guessModeEnabled) {

            boolean oneOfTwoWrong;

            do {
                nonogramSubsolutionNode.getNonogramLogic().updateCurrentAvailableChoices();
                nonogramSubsolutionNode.getNonogramLogic().clearLogs();

                correctDecision = Optional.empty();

                // two decissions are 'possibly' ok
                oneOfTwoWrong = false;

                //look for field, where:
                // colouring is not wrong decision and placing X is wrong decision
                // OR
                // colouring is wrong decision and placing X is not wrong decision
                for(NonogramSolutionDecision decision : nonogramSubsolutionNode.getNonogramLogic().getAvailableChoices()) {

                    //left node decision - place "O" at specific place
                    leftNodeDecision = new NonogramSolutionDecision("O", decision.getRowIdx(), decision.getColumnIdx());
                    NonogramSolutionNode leftNodeToAddDecision = gson.fromJson(gson.toJson(nonogramSubsolutionNode), NonogramSolutionNode.class);
                    leftNodeToAddDecision.addDecision(leftNodeDecision);
                    leftNodeToAddDecision.colourOrPlaceX();
                    leftNodeO = gson.fromJson(gson.toJson(leftNodeToAddDecision), NonogramSolutionNode.class);

                    makeBasicSolverActions(leftNodeO);

                    //right node decision - place "X" at specific place
                    rightNodeDecision = new NonogramSolutionDecision("X", decision.getRowIdx(), decision.getColumnIdx());
                    NonogramSolutionNode rightNodeToAddDecision = gson.fromJson(gson.toJson(nonogramSubsolutionNode), NonogramSolutionNode.class);
                    rightNodeToAddDecision.addDecision(rightNodeDecision);
                    rightNodeToAddDecision.colourOrPlaceX();
                    rightNodeX = gson.fromJson(gson.toJson(rightNodeToAddDecision), NonogramSolutionNode.class);

                    makeBasicSolverActions(rightNodeX);

                    int columnDecisionToCheck = 1;
                    int rowDecisionToCheck = 13;


                    NonogramSolutionNode nodeToCheck = gson.fromJson(gson.toJson(leftNodeO), NonogramSolutionNode.class);
                    boolean printDecisionStats = true;
                    if(printDecisionStats && leftNodeO.getNonogramLogic().fieldsFilled() > 170  && nonogramSubsolutionNode.getNonogramLogic().fieldsFilled()  == 148) {
                        System.out.println("-".repeat(20) + "DECISION" + decision + " STATS START" + "-".repeat(20));
                        System.out.println("filled after: " + nodeToCheck.getNonogramLogic().fieldsFilled() + "(difference=" +
                                (leftNodeO.getNonogramLogic().fieldsFilled() - nonogramSubsolutionNode.getNonogramLogic().fieldsFilled()) + ")");
                        System.out.println("before: ");
                        nonogramSubsolutionNode.getNonogramLogic().printNonogramBoard();
                        //nonogramSubsolutionNode.getNonogramLogic().printNonogramBoardWithMarks();
                        System.out.println("after: ");
                        nodeToCheck.getNonogramLogic().printNonogramBoard();
                        //nodeToCheck.getNonogramLogic().printNonogramBoardWithMarks();
                        //System.out.println("logs: ");
                        //nodeToCheck.getNonogramLogic().printLogs();
                        //System.out.println("Rows sequences ranges: ");
                        //nodeToCheck.getNonogramLogic().printRowsSequencesRanges();
                        //System.out.println("Columns sequences ranges: ");
                        //nodeToCheck.getNonogramLogic().printColumnsSequencesRanges();
                        //System.out.println("invalid? :" + nodeToCheck.getNonogramLogic().isSolutionInvalid());
                        System.out.println("-".repeat(20) + "DECISION" + decision + " STATS END" + "-".repeat(20));
                    }

                    // check correctness in both nodes or 100% - completness
                    if(!leftNodeO.getNonogramLogic().isSolutionInvalid() && rightNodeX.getNonogramLogic().isSolutionInvalid()) {
                        correctDecision = Optional.of(leftNodeDecision);
                        nonogramSubsolutionNode = gson.fromJson(gson.toJson(leftNodeO), NonogramSolutionNode.class);
                        this.solutionLogic = nonogramSubsolutionNode.getNonogramLogic();
                        oneOfTwoWrong = true;
                        break;
                    } else if(leftNodeO.getNonogramLogic().isSolutionInvalid() && !rightNodeX.getNonogramLogic().isSolutionInvalid()) {
                        correctDecision = Optional.of(rightNodeDecision);
                        nonogramSubsolutionNode = gson.fromJson(gson.toJson(rightNodeX), NonogramSolutionNode.class);
                        this.solutionLogic = nonogramSubsolutionNode.getNonogramLogic();
                        oneOfTwoWrong = true;
                        break;
                    }
                }

                //only guesses, not recursive - can replace solution with more overall completness
                if(currentTreeHeight == 0) {

                    // one of decision wrong -> another is correct
                    if(oneOfTwoWrong) {

                        this.replaceSolutionNodeWithMoreBenefitialSolution(nonogramSubsolutionNode);

                        nonogramGuessActionsLog = new NonogramGuessActionsLog(correctDecision.get(), this.solutionLogic.getLogs());
                        guessesLogs.add(nonogramGuessActionsLog);
                    } /*else {
                        for(NonogramSolutionDecision decision : nonogramSubsolutionNode.getNonogramLogic().getAvailableChoices()) {

                            nonogramSubsolutionNode.getNonogramLogic().clearLogs();

                            //left node decision - place "O" at specific place
                            leftNodeDecision = new NonogramSolutionDecision("O", decision.getRowIdx(), decision.getColumnIdx());
                            NonogramSolutionNode leftNodeToAddDecision = gson.fromJson(gson.toJson(nonogramSubsolutionNode), NonogramSolutionNode.class);
                            leftNodeToAddDecision.addDecision(leftNodeDecision);
                            leftNodeToAddDecision.colourOrPlaceX();
                            leftNodeO = gson.fromJson(gson.toJson(leftNodeToAddDecision), NonogramSolutionNode.class);

                            makeBasicSolverActions(leftNodeO);

                            //right node decision - place "X" at specific place
                            rightNodeDecision = new NonogramSolutionDecision("X", decision.getRowIdx(), decision.getColumnIdx());
                            NonogramSolutionNode rightNodeToAddDecision = gson.fromJson(gson.toJson(nonogramSubsolutionNode), NonogramSolutionNode.class);
                            rightNodeToAddDecision.addDecision(rightNodeDecision);
                            rightNodeToAddDecision.colourOrPlaceX();
                            rightNodeX = gson.fromJson(gson.toJson(rightNodeToAddDecision), NonogramSolutionNode.class);

                            makeBasicSolverActions(rightNodeX);
                        }
                    }*/

                } else { // recursion
                    System.out.println("tree height: " + currentTreeHeight);
                }
            } while (
                    oneOfTwoWrong
            );



            nonogramNodeLog.setGuessesLogs(guessesLogs);
            this.solutionNode.addNodeLog(nonogramNodeLog);
            // after all possible guesses
            this.solutionLogic = nonogramSubsolutionNode.getNonogramLogic();
            nonogramSubsolutionNode.getNonogramLogic().printLogs();
            //r3c1 (Akkordeon)
            System.out.println(this.solutionNode.getNonogramGuessDecisions());
            System.out.println("Rows Sequences Ranges: ");
            solutionLogic.printRowsSequencesRanges();
            System.out.println("Columns Sequences Ranges");
            solutionLogic.printColumnsSequencesRanges();
            solutionNode.printNodeFinalResult();
        } else {
            System.out.println("currentTreeHeight: " + currentTreeHeight + " final result invalid decisions: (" + nonogramSubsolutionNode.getNonogramGuessDecisions() + ")");
        }
    }

    //replace solutionNode with this with higher completion percentage
    public void replaceSolutionNodeWithMoreBenefitialSolution(NonogramSolutionNode nodeToCheck) {
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
