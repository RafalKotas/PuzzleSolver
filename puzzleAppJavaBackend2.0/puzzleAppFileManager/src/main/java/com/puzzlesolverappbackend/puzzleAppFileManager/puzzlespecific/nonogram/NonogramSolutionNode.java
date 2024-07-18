package com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram;


import com.google.gson.Gson;
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

        boolean solutionInvalid;

        boolean sequencesRangesDiffered;
        boolean solutionBoardsDiffered;
        boolean sequenceIndexesNotToIncludeAddedCondition;

        boolean changesOccured;

        do {
            NonogramLogic logicBeforeActionsMade = copyNonogramLogic();

            for(int actionNo = 1; actionNo <= 18; actionNo++) {
                this.getNonogramLogic().basicSolve(actionNo);
                logActionWhichLeadToCheckedStage(actionNo);
            }

            solutionInvalid = this.getNonogramLogic().getNonogramState().isInvalidSolution();

            NonogramLogic logicAfterActionsMade = copyNonogramLogic();

            this.nodeLogs = logicAfterActionsMade.getLogs();

            sequencesRangesDiffered = sequencesRangesAfterActionsMadeDiffers(logicBeforeActionsMade, logicAfterActionsMade);

            solutionBoardsDiffered = solutionBoardsDiffers(logicBeforeActionsMade.getNonogramSolutionBoard(),
                    logicAfterActionsMade.getNonogramSolutionBoard());

            sequenceIndexesNotToIncludeAddedCondition = sequenceIndexesNotToIncludeAdded(logicBeforeActionsMade, logicAfterActionsMade);

            changesOccured = sequencesRangesDiffered || solutionBoardsDiffered || sequenceIndexesNotToIncludeAddedCondition;
        } while (
                (changesOccured && !solutionInvalid && !this.getNonogramLogic().isSolved()) || affectedRowsOrColumnsLeft()
        );
    }

    private void logActionWhichLeadToCheckedStage(int actionNo) {
        if(!influentActionPrinted && this.getNonogramLogic().getNonogramSolutionBoard().get(4).get(5).equals("O")
                && this.getNonogramLogic().getNonogramSolutionBoard().get(4).get(6).equals("O")) {
            System.out.println("Checked stage after actionNo: " + actionNo);
            influentActionPrinted = true;
        }
    }

    private boolean sequencesRangesAfterActionsMadeDiffers(NonogramLogic logicBeforeActionsMade, NonogramLogic logicAfterActionsMade) {
        boolean sequencesRangesAfterActionsMadeDiffersInRows = sequencesRangesAfterActionsMadeDiffersInSection(
                logicBeforeActionsMade.getRowsSequencesRanges(),
                logicAfterActionsMade.getRowsSequencesRanges()
        );
        boolean sequencesRangesAfterActionsMadeDiffersInColumns = sequencesRangesAfterActionsMadeDiffersInSection(
                logicBeforeActionsMade.getColumnsSequencesRanges(),
                logicAfterActionsMade.getColumnsSequencesRanges()
        );
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