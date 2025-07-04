package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.solutions;


import com.google.gson.Gson;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.logic.NonogramLogicFactory;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.model.NonogramSolution;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.model.NonogramConstants.X_FIELD;

@Getter
@Setter
@AllArgsConstructor
public class NonogramSolutionNode {

    private NonogramLogic nonogramLogic;
    private NonogramLogic nonogramSolution;
    private NonogramSolution fullSolutionBoard;
    private List<NonogramSolutionDecision> nonogramGuessDecisions;
    private List<NonogramSolutionDecision> nonogramRecursionDecisions;

    // log of taken actions (heuristics, guesses decisions, and in nodeHeight > 0 also recursion decisions)
    private List<String> nodeLogs;

    public NonogramSolutionNode(NonogramLogic baseLogic, NonogramLogicFactory factory) {
        this.nonogramLogic = factory.copy(baseLogic);
        this.nonogramSolution = factory.copy(baseLogic);
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

        if (lastDecision.getDecisionMarker().equals(X_FIELD)) {
            this.getNonogramLogic().placeXAtGivenPosition(lastDecision.getDecisionField());
            this.getNonogramLogic().addAffectedRowAndColumnAfterPlacingXAtField(lastDecision);
        } else {
            this.getNonogramLogic().colourFieldAtGivenPosition(lastDecision.getDecisionField());
            this.getNonogramLogic().addAffectedRowAndColumnAfterColouringField(lastDecision);
        }
    }

    public void makeBasicSolverActions() {
        this.getNonogramLogic().basicSolve();

        NonogramLogic logicAfterActionsMade = this.getNonogramLogic().deepCopy();
        this.nodeLogs = logicAfterActionsMade.getLogs();
    }

    private NonogramLogic copyNonogramLogic() {
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(this.getNonogramLogic()), NonogramLogic.class);
    }
}