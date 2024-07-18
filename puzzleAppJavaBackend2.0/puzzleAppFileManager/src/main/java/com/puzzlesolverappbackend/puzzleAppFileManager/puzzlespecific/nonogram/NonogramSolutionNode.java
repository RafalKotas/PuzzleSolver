package com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram;


import com.google.gson.Gson;
import com.puzzlesolverappbackend.puzzleAppFileManager.payload.NonogramLogic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.NonogramParametersComparatorHelper.*;

@Getter
@Setter
@AllArgsConstructor
public class NonogramSolutionNode {

    private NonogramLogic nonogramLogic;
    private NonogramLogic nonogramSolution;
    private List<NonogramSolutionDecision> nonogramGuessDecisions;
    private List<NonogramSolutionDecision> nonogramRecursionDecisions;

    private boolean influentActionPrinted = false;

    // log of taken actions (heuristics, guesses decisions, and in nodeHeight > 0 also recursion decisions)
    private List<String> nodeLogs;

    public NonogramSolutionNode(NonogramLogic nonogramLogic) {
        Gson gson = new Gson();
        this.nonogramLogic = gson.fromJson(gson.toJson(nonogramLogic), NonogramLogic.class);
        this.nonogramSolution = gson.fromJson(gson.toJson(nonogramLogic), NonogramLogic.class);
        this.nonogramGuessDecisions = new ArrayList<>();
        this.nonogramRecursionDecisions = new ArrayList<>();
        this.nodeLogs = new ArrayList<>();
    }

    public void addDecision(NonogramSolutionDecision decision) {
        this.nonogramGuessDecisions.add(decision);
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
        if(!this.nonogramGuessDecisions.isEmpty()) {
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
        System.out.println("Subsolution invalid? : " + this.getNonogramLogic().getNonogramState().isInvalidSolution());
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

        boolean sequencesRangesDiffered;
        boolean solutionBoardsDiffered;
        boolean sequenceIndexesNotToIncludeAddedCondition;

        boolean changesOccured;

        do {
            NonogramLogic logicBeforeActionsMade = copyNonogramLogic();

            for(ActionEnum action : EnumSet.allOf(ActionEnum.class)) {
                this.getNonogramLogic().basicSolve(action);
                logActionWhichLeadToCheckedStage(action);
            }

            NonogramLogic logicAfterActionsMade = copyNonogramLogic();

            this.nodeLogs = logicAfterActionsMade.getLogs();

            sequencesRangesDiffered = sequencesRangesAfterActionsMadeDiffers(logicBeforeActionsMade, logicAfterActionsMade);

            solutionBoardsDiffered = solutionBoardsDiffers(logicBeforeActionsMade.getNonogramSolutionBoard(),
                    logicAfterActionsMade.getNonogramSolutionBoard());

            sequenceIndexesNotToIncludeAddedCondition = sequenceIndexesNotToIncludeAdded(logicBeforeActionsMade, logicAfterActionsMade);

            changesOccured = sequencesRangesDiffered || solutionBoardsDiffered || sequenceIndexesNotToIncludeAddedCondition;
        } while (
                (changesOccured && !this.getNonogramLogic().getNonogramState().isInvalidSolution() && !this.getNonogramLogic().isSolved()) || affectedRowsOrColumnsLeft()
        );
    }

    private void logActionWhichLeadToCheckedStage(ActionEnum action) {
        if(!influentActionPrinted && this.getNonogramLogic().getNonogramSolutionBoard().get(4).get(5).equals("O")
                && this.getNonogramLogic().getNonogramSolutionBoard().get(4).get(6).equals("O")) {
            System.out.println("Checked stage after actionNo: " + action);
            influentActionPrinted = true;
        }
    }

    private NonogramLogic copyNonogramLogic() {
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(this.getNonogramLogic()), NonogramLogic.class);
    }

    private String getNonogramBoardField(int rowIdx, int columnIdx) {
        return this.getNonogramLogic().getNonogramSolutionBoard().get(rowIdx).get(columnIdx);
    }

    private boolean affectedRowsOrColumnsLeft() {
        boolean areAffectedRows = !this.getNonogramLogic().getAffectedRowsToCorrectSequencesRanges().isEmpty()
                || !this.getNonogramLogic().getAffectedRowsToCorrectSequencesRangesWhenMetColouredField().isEmpty()
                || !this.getNonogramLogic().getAffectedRowsToCorrectSequencesRangesIfXOnWay().isEmpty()
                || !this.getNonogramLogic().getAffectedRowsToFillOverlappingFields().isEmpty()
                || !this.getNonogramLogic().getAffectedRowsToExtendColouredFieldsNearX().isEmpty()
                || !this.getNonogramLogic().getAffectedRowsToPlaceXsAtUnreachableFields().isEmpty()
                || !this.getNonogramLogic().getAffectedRowsToPlaceXsAroundLongestSequences().isEmpty()
                || !this.getNonogramLogic().getAffectedRowsToPlaceXsAtTooShortEmptySequences().isEmpty()
                || !this.getNonogramLogic().getAffectedRowsToMarkAvailableSequences().isEmpty();

        boolean areAffectedColumns = !this.getNonogramLogic().getAffectedColumnsToCorrectSequencesRanges().isEmpty()
                || !this.getNonogramLogic().getAffectedColumnsToCorrectSequencesRangesWhenMetColouredField().isEmpty()
                || !this.getNonogramLogic().getAffectedColumnsToCorrectSequencesRangesIfXOnWay().isEmpty()
                || !this.getNonogramLogic().getAffectedColumnsToFillOverlappingFields().isEmpty()
                || !this.getNonogramLogic().getAffectedColumnsToExtendColouredFieldsNearX().isEmpty()
                || !this.getNonogramLogic().getAffectedColumnsToPlaceXsAtUnreachableFields().isEmpty()
                || !this.getNonogramLogic().getAffectedColumnsToPlaceXsAroundLongestSequences().isEmpty()
                || !this.getNonogramLogic().getAffectedColumnsToPlaceXsAtTooShortEmptySequences().isEmpty()
                || !this.getNonogramLogic().getAffectedColumnsToMarkAvailableSequences().isEmpty();

        return areAffectedRows || areAffectedColumns;
    }
}