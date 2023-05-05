package com.puzzlesolverappbackend.puzzleAppFileManager;


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
    private NonogramSolutionNode parentNode;
    private NonogramSolutionNode leftNode;
    private NonogramSolutionNode rightNode;

    // log of taken actions (heuristics, guesses decisions, and in nodeHeight > 0 also recursion decisions)
    private List<NonogramNodeLog> nodeLogs;

    public NonogramSolutionNode(NonogramLogic nonogramLogic) {
        this.nonogramLogic = nonogramLogic;
        this.nonogramGuessDecisions = new ArrayList<>();
        this.nodeLogs = new ArrayList<>();
        this.parentNode = null;
        this.leftNode = null;
        this.rightNode = null;
    }

    public NonogramSolutionNode(NonogramLogic nonogramLogic, NonogramSolutionNode parentNode,
                                NonogramSolutionDecision nonogramSolutionDecision) {
        this.nonogramLogic = nonogramLogic;
        this.parentNode = parentNode;

        List<NonogramSolutionDecision> childDecisions = new ArrayList<>(parentNode.getNonogramGuessDecisions());
        childDecisions.add(nonogramSolutionDecision);
        this.nonogramGuessDecisions = childDecisions;

        this.leftNode = null;
        this.rightNode = null;
    }

    public NonogramSolutionNode(NonogramSolutionNode nodeToCopy, NonogramSolutionDecision decision) throws CloneNotSupportedException {
        NonogramSolutionNode tmpNode = (NonogramSolutionNode) nodeToCopy.clone();
        this.nonogramLogic = tmpNode.getNonogramLogic();
        this.parentNode = tmpNode;
        List<NonogramSolutionDecision> decisionList = new ArrayList<>(tmpNode.getNonogramGuessDecisions());
        decisionList.add(decision);
        this.nonogramGuessDecisions = decisionList;
        this.leftNode = null;
        this.rightNode = null;
    }

    public NonogramSolutionNode() {
        this.leftNode = null;
        this.rightNode = null;
    }

    public void setLeftNode(NonogramSolutionNode node, NonogramSolutionDecision decision) {
        this.leftNode = node;
        List<NonogramSolutionDecision> leftNodeSolutionDecisions = this.nonogramGuessDecisions;
        leftNodeSolutionDecisions.add(decision);
        this.leftNode.nonogramGuessDecisions = leftNodeSolutionDecisions;
    }

    public void setRightNode(NonogramSolutionNode node, NonogramSolutionDecision decision) {
        this.rightNode = node;
        List<NonogramSolutionDecision> rightNodeSolutionDecisions = this.nonogramGuessDecisions;
        rightNodeSolutionDecisions.add(decision);
        this.leftNode.nonogramGuessDecisions = rightNodeSolutionDecisions;
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
        NonogramSolutionDecision lastDecision = null;
        if(nonogramGuessDecisions.size() > 0) {
            lastDecision = nonogramGuessDecisions.get(nonogramGuessDecisions.size() - 1);
        }
        return "NonogramSolutionNode{" +
                ", lastDecision=" + lastDecision +
                ", invalidSolution=" + nonogramLogic.isSolutionInvalid() +
                '}';
    }

    public Optional<NonogramSolutionDecision> getLastDecision() {
        Optional<NonogramSolutionDecision> lastDecision = Optional.empty();
        if(this.nonogramGuessDecisions.size() > 0) {
            lastDecision = Optional.ofNullable(this.nonogramGuessDecisions.get(this.nonogramGuessDecisions.size() - 1));
        }
        return lastDecision;
    }

    public void makeBasicSolverActions() {

        int stepsMadeAtStart;
        int stepsMadeAtEnd;

        //conditions
        boolean stepsCountDiffered;
        boolean solutionInvalid;

        //System.out.println("Nonogram filename: " + this);

        do {
            stepsMadeAtStart = this.getNonogramLogic().getNewStepsMade();

            for(int actionNo = 1; actionNo < 19; actionNo++) {
                this.getNonogramLogic().basicSolve(actionNo);
            }

            stepsMadeAtEnd = this.getNonogramLogic().getNewStepsMade();

            stepsCountDiffered = stepsMadeAtStart != stepsMadeAtEnd;
            solutionInvalid = this.getNonogramLogic().isSolutionInvalid();
        } while (
                stepsCountDiffered && !solutionInvalid
        );
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
}
