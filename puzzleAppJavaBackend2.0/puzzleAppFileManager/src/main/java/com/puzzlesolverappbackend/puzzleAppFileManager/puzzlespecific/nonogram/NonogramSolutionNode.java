package com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram;


import com.google.gson.Gson;
import com.puzzlesolverappbackend.puzzleAppFileManager.payload.NonogramLogic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.NonogramParametersComparatorHelper.*;

@Getter
@Setter
@AllArgsConstructor
public class NonogramSolutionNode {

    private NonogramLogic nonogramLogic;
    private NonogramLogic nonogramSolution;
    private List<NonogramSolutionDecision> nonogramGuessDecisions;
    private List<NonogramSolutionDecision> nonogramRecursionDecisions;

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

    public void colourOrPlaceX() {
        //add chosen decision - colour field(O) or place (X)
        NonogramSolutionDecision lastDecision = this.nonogramGuessDecisions.get(this.nonogramGuessDecisions.size() - 1);

        if (lastDecision.getDecisionMarker().equals("X")) {
            this.getNonogramLogic().placeXAtGivenPosition(lastDecision.getRowIdx(), lastDecision.getColumnIdx());
            this.getNonogramLogic().addAffectedRowAndColumnAfterPlacingXAtField(lastDecision);
        } else {
            this.getNonogramLogic().colourFieldAtGivenPosition(lastDecision.getRowIdx(), lastDecision.getColumnIdx());
            this.getNonogramLogic().addAffectedRowAndColumnAfterColouringField(lastDecision);
        }
    }

    public void makeBasicSolverActions() {

        boolean sequencesRangesDiffered;
        boolean solutionBoardsDiffered;
        boolean sequenceIndexesNotToIncludeAddedCondition;

        boolean changesOccurred;

        do {
            NonogramLogic logicBeforeActionsMade = copyNonogramLogic();

            this.getNonogramLogic().basicSolve();

            NonogramLogic logicAfterActionsMade = copyNonogramLogic();

            this.nodeLogs = logicAfterActionsMade.getLogs();

            sequencesRangesDiffered = sequencesRangesAfterActionsMadeDiffers(logicBeforeActionsMade, logicAfterActionsMade);

            solutionBoardsDiffered = solutionBoardsDiffers(logicBeforeActionsMade.getNonogramSolutionBoard(),
                    logicAfterActionsMade.getNonogramSolutionBoard());

            sequenceIndexesNotToIncludeAddedCondition = sequenceIndexesNotToIncludeAdded(logicBeforeActionsMade, logicAfterActionsMade);

            changesOccurred = sequencesRangesDiffered || solutionBoardsDiffered || sequenceIndexesNotToIncludeAddedCondition;
        } while (
                (changesOccurred && !this.getNonogramLogic().getNonogramState().isInvalidSolution() && !this.getNonogramLogic().isSolved()) || affectedRowsOrColumnsLeft()
        );
    }

    private NonogramLogic copyNonogramLogic() {
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(this.getNonogramLogic()), NonogramLogic.class);
    }

    private boolean affectedRowsOrColumnsLeft() {
        return !this.getNonogramLogic().getActionsToDoList().isEmpty();
    }
}