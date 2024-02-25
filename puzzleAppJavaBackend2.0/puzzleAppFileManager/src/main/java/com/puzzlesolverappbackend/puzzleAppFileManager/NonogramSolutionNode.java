package com.puzzlesolverappbackend.puzzleAppFileManager;


import com.google.gson.Gson;
import com.puzzlesolverappbackend.puzzleAppFileManager.logs.nonogram.NonogramNodeLog;
import com.puzzlesolverappbackend.puzzleAppFileManager.payload.NonogramLogic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
public class NonogramSolutionNode {

    private NonogramLogic nonogramLogic;
    private NonogramLogic nonogramSolution;
    private List<NonogramSolutionDecision> nonogramGuessDecisions;
    private List<NonogramSolutionDecision> nonogramRecursionDecisions;

    // log of taken actions (heuristics, guesses decisions, and in nodeHeight > 0 also recursion decisions)
    private List<NonogramNodeLog> nodeLogs;

    public NonogramSolutionNode(NonogramLogic nonogramLogic) {
        Gson gson = new Gson();
        this.nonogramLogic = gson.fromJson(gson.toJson(nonogramLogic), NonogramLogic.class);
        this.nonogramSolution = gson.fromJson(gson.toJson(nonogramLogic), NonogramLogic.class);
        this.nonogramGuessDecisions = new ArrayList<>();
        this.nonogramRecursionDecisions = new ArrayList<>();
        this.nodeLogs = new ArrayList<>();
    }

    public NonogramSolutionNode(NonogramLogic nonogramLogic, NonogramSolutionNode parentNode,
                                NonogramSolutionDecision nonogramSolutionDecision) {
        this.nonogramLogic = nonogramLogic;

        List<NonogramSolutionDecision> childDecisions = new ArrayList<>(parentNode.getNonogramGuessDecisions());
        childDecisions.add(nonogramSolutionDecision);
        this.nonogramGuessDecisions = childDecisions;
    }

    public NonogramSolutionNode(NonogramSolutionNode nodeToCopy, NonogramSolutionDecision decision) throws CloneNotSupportedException {
        NonogramSolutionNode tmpNode = (NonogramSolutionNode) nodeToCopy.clone();
        this.nonogramLogic = tmpNode.getNonogramLogic();
        List<NonogramSolutionDecision> decisionList = new ArrayList<>(tmpNode.getNonogramGuessDecisions());
        decisionList.add(decision);
        this.nonogramGuessDecisions = decisionList;
    }

    public void setLeftNode(NonogramSolutionNode node, NonogramSolutionDecision decision) {
        List<NonogramSolutionDecision> leftNodeSolutionDecisions = this.nonogramGuessDecisions;
        leftNodeSolutionDecisions.add(decision);
    }

    public void setRightNode(NonogramSolutionNode node, NonogramSolutionDecision decision) {
        List<NonogramSolutionDecision> rightNodeSolutionDecisions = this.nonogramGuessDecisions;
        rightNodeSolutionDecisions.add(decision);
    }

    public void addDecision(NonogramSolutionDecision decision) {
        this.nonogramGuessDecisions.add(decision);
    }

    public void removeLastDecision() {
        this.nonogramGuessDecisions.remove(this.nonogramGuessDecisions.size() - 1);
    }

    public void addNodeLog(NonogramNodeLog nonogramNodeLog) {
        this.nodeLogs.add(nonogramNodeLog);
    }

    @Override
    public String toString() {
        return "NonogramSolutionNode{" +
                "nonogramLogic=" + nonogramLogic +
                ", nonogramSolution=" + nonogramSolution +
                ", nonogramGuessDecisions=" + nonogramGuessDecisions +
                ", nonogramRecursionDecisions=" + nonogramRecursionDecisions +
                ", nodeLogs=" + nodeLogs +
                '}';
    }

    public Optional<NonogramSolutionDecision> getLastDecision() {
        Optional<NonogramSolutionDecision> lastDecision = Optional.empty();
        if(this.nonogramGuessDecisions.size() > 0) {
            lastDecision = Optional.ofNullable(this.nonogramGuessDecisions.get(this.nonogramGuessDecisions.size() - 1));
        }
        return lastDecision;
    }

    public void colourOrPlaceX() {
        //add chosen decision - colour field(O) or place (X)
        NonogramSolutionDecision lastDecision = this.nonogramGuessDecisions.get(this.nonogramGuessDecisions.size() - 1);

        if (lastDecision.getDecisionMarker().equals("X")) {
            this.getNonogramLogic().placeXAtGivenPosittion(lastDecision.getRowIdx(), lastDecision.getColumnIdx());
            this.getNonogramLogic().addAffectedRowAndColumnAfterPlacingXAtField(lastDecision);
        } else {
            this.getNonogramLogic().colourFieldAtGivenPosition(lastDecision.getRowIdx(), lastDecision.getColumnIdx());
            this.getNonogramLogic().addAffectedRowAndColumnAfterColouringField(lastDecision);
        }
    }

    public void printNodeFinalResult() {
        System.out.println("-".repeat(30));
        System.out.println("Total decisions made: " + this.getNonogramGuessDecisions().size());
        printNodeDecisions();
        System.out.println("Stats: ");
        //this.getNonogramLogic().getNonogramPrinter().printStatsAfterBasicActionsMade(this.getNonogramLogic());
        System.out.println("Subsolution invalid? : " + this.getNonogramLogic().isSolutionInvalid());
        System.out.println("-".repeat(30));
    }

    public void printNodeDecisions() {
        int decisionNo = 1;
        for(NonogramSolutionDecision decision : this.getNonogramGuessDecisions()) {
            System.out.println(decisionNo + " : " + decision);
            decisionNo++;
        }
    }

    public void makeBasicSolverActions() {

        boolean solutionInvalid;

        boolean sequencesRangesDiffered;
        boolean solutionBoardsDiffered;
        boolean sequenceIndexesNotToIncludeAddedCondition;

        do {
            NonogramLogic logicBeforeActionsMade = copyNonogramLogic();

            for(int actionNo = 1; actionNo <= 18; actionNo++) {
                this.getNonogramLogic().basicSolve(actionNo);
            }

            solutionInvalid = this.getNonogramLogic().isSolutionInvalid();

            NonogramLogic logicAfterActionsMade = copyNonogramLogic();

            System.out.println(logicBeforeActionsMade.getRowsSequencesRanges());
            System.out.println(logicAfterActionsMade.getRowsSequencesRanges());
            System.out.println(logicBeforeActionsMade.getColumnsSequencesRanges());
            System.out.println(logicAfterActionsMade.getColumnsSequencesRanges());
            sequencesRangesDiffered = sequencesRangesAfterActionsMadeDiffers(logicBeforeActionsMade, logicAfterActionsMade);

            solutionBoardsDiffered = solutionBoardsDiffers(logicBeforeActionsMade.getNonogramSolutionBoard(),
                    logicAfterActionsMade.getNonogramSolutionBoard());

            sequenceIndexesNotToIncludeAddedCondition = sequenceIndexesNotToIncludeAdded(logicBeforeActionsMade, logicAfterActionsMade);
            System.out.println("sequencesRangesDiffered: " + sequencesRangesDiffered + " \n solutionBoardDiffered: " + solutionBoardsDiffered +
                    " \n sequenceIndexesNotToIncludeAddedCondition: " + sequenceIndexesNotToIncludeAddedCondition + " \n solutionInvalid: " + solutionInvalid
                    + " \n completionPercentage: " + this.getNonogramLogic().getCompletionPercentage());
        } while (
                (sequencesRangesDiffered || solutionBoardsDiffered || sequenceIndexesNotToIncludeAddedCondition)
                        && !solutionInvalid && this.getNonogramLogic().getCompletionPercentage()!=100
        );
    }

    private boolean sequencesRangesAfterActionsMadeDiffers(NonogramLogic logicBeforeActionsMade, NonogramLogic logicAfterActionsMade) {
        boolean sequencesRangesAfterActionsMadeDiffersInRows = sequencesRangesAfterActionsMadeDiffersInSection(
                logicBeforeActionsMade.getRowsSequencesRanges(),
                logicAfterActionsMade.getRowsSequencesRanges()
        );
        System.out.println("differs in rows: " + sequencesRangesAfterActionsMadeDiffersInRows);
        boolean sequencesRangesAfterActionsMadeDiffersInColumns = sequencesRangesAfterActionsMadeDiffersInSection(
                logicBeforeActionsMade.getColumnsSequencesRanges(),
                logicAfterActionsMade.getColumnsSequencesRanges()
        );
        System.out.println("differs in columns: " + sequencesRangesAfterActionsMadeDiffersInColumns);
        return sequencesRangesAfterActionsMadeDiffersInRows || sequencesRangesAfterActionsMadeDiffersInColumns;
    }

    private boolean sequencesRangesAfterActionsMadeDiffersInSection(List<List<List<Integer>>> sequencesRangesBefore, List<List<List<Integer>>> sequencesRangesAfter) {

        Iterator<List<List<Integer>>> logicBeforeActionsMadeRowsSequencesRangesIterator = sequencesRangesBefore.iterator();
        Iterator<List<List<Integer>>> logicAfterActionsMadeRowsSequencesRangesIterator = sequencesRangesAfter.iterator();

        while(logicBeforeActionsMadeRowsSequencesRangesIterator.hasNext() && logicAfterActionsMadeRowsSequencesRangesIterator.hasNext()) {

            List<List<Integer>> logicBeforeActionsMadeRowSequencesRanges = logicBeforeActionsMadeRowsSequencesRangesIterator.next();
            List<List<Integer>> logicAfterActionsMadeRowSequencesRanges = logicAfterActionsMadeRowsSequencesRangesIterator.next();

            if(!NonogramSolutionNode.sequencesRangesEqual(logicBeforeActionsMadeRowSequencesRanges, logicAfterActionsMadeRowSequencesRanges)) {
                return true;
            }
        }

        return false;
    }

    private static boolean sequencesRangesEqual(List<List<Integer>> sequencesRanges_A, List<List<Integer>> sequenceRanges_B) {
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

    public boolean sequenceIndexesNotToIncludeAdded(NonogramLogic logicBeforeActionsMade, NonogramLogic logicAfterActionsMade) {
        boolean sequencesIndexesSizeNotToIncludeAddedInRowsChanged = sequenceIndexesNotToIncludeAddedOneDimension(
                logicBeforeActionsMade.getRowsSequencesIdsNotToInclude(), logicAfterActionsMade.getRowsSequencesIdsNotToInclude());
        boolean sequencesIndexesSizeNotToIncludeAddedInColumnsChanged = sequenceIndexesNotToIncludeAddedOneDimension(
                logicBeforeActionsMade.getColumnsSequencesIdsNotToInclude(), logicAfterActionsMade.getColumnsSequencesIdsNotToInclude());

        return sequencesIndexesSizeNotToIncludeAddedInRowsChanged || sequencesIndexesSizeNotToIncludeAddedInColumnsChanged;
    }

    public boolean sequenceIndexesNotToIncludeAddedOneDimension(List<List<Integer>> sequencesIdsNotToIncludeBefore, List<List<Integer>> sequencesIdsNotToIncludeAfter) {

        Iterator<List<Integer>> sequencesIdsNotToIncludeBeforeActionsMadeIterator = sequencesIdsNotToIncludeBefore.iterator();
        Iterator<List<Integer>> sequencesIdsNotToIncludeAfterActionsMadeIterator = sequencesIdsNotToIncludeAfter.iterator();

        List<Integer> sequencesIdsNotToIncludeBeforeActionsMade;
        List<Integer> sequencesIdsNotToIncludeAfterActionsMade;

        while(sequencesIdsNotToIncludeBeforeActionsMadeIterator.hasNext() && sequencesIdsNotToIncludeAfterActionsMadeIterator.hasNext()) {
            sequencesIdsNotToIncludeBeforeActionsMade = sequencesIdsNotToIncludeBeforeActionsMadeIterator.next();
            sequencesIdsNotToIncludeAfterActionsMade = sequencesIdsNotToIncludeAfterActionsMadeIterator.next();

            // ids not to include differs only if size of Lists differs (ids are sorted ascending)
            if(!(sequencesIdsNotToIncludeBeforeActionsMade.size() == sequencesIdsNotToIncludeAfterActionsMade.size()) ) {
                return true;
            }
        }

        return false;
    }

    /***
     * range_A in format [A_1, A_2]
     * range_B in format [B_1, B_2]
     ***/
    public static boolean rangesEqual(List<Integer> range_A, List<Integer> range_B) {
        return Objects.equals(range_A.get(0), range_B.get(0)) && Objects.equals(range_A.get(1), range_B.get(1));
    }

    private NonogramLogic copyNonogramLogic() {
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(this.getNonogramLogic()), NonogramLogic.class);
    }
}