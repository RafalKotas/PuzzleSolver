package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramActionDetails;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solutions.NonogramSolutionDecision;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solutions.NonogramSolutionNode;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.NonogramSolvePayload;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramState.buildInitialEmptyNonogramState;

@Component
public class NonogramLogicFactory {

    public NonogramLogic createFromPayload(NonogramSolvePayload payload) {

        NonogramLogic logic = new NonogramLogic();

        NonogramRules rules = new NonogramRules(
                payload.getRowSequences(),
                payload.getColumnSequences(),
                payload.getNonogramRules().getHeight(),
                payload.getNonogramRules().getWidth()
        );
        logic.setNonogramRules(rules);

        logic.setNonogramSolutionBoard(deepCopyStrings(payload.getNonogramSolutionBoard()));
        logic.setNonogramSolutionBoardWithMarks(deepCopyStrings(payload.getNonogramSolutionBoardWithMarks()));
        logic.setRowsSequencesRanges(deepCopy3DIntegers(payload.getRowsSequencesRanges()));
        logic.setColumnsSequencesRanges(deepCopy3DIntegers(payload.getColumnsSequencesRanges()));
        logic.setRowsFieldsNotToInclude(deepCopyIntegers(payload.getRowsFieldsNotToInclude()));
        logic.setColumnsFieldsNotToInclude(deepCopyIntegers(payload.getColumnsFieldsNotToInclude()));
        logic.setRowsSequencesIdsNotToInclude(deepCopyIntegers(payload.getRowsSequencesIdsNotToInclude()));
        logic.setColumnsSequencesIdsNotToInclude(deepCopyIntegers(payload.getColumnsSequencesIdsNotToInclude()));

        logic.setActionsToDoList(
                NonogramLogic.generateInitialActionsToDo(rules)
        );

        logic.setNonogramState(buildInitialEmptyNonogramState());

        logic.initializeHelpers();

        return logic;
    }

    public NonogramLogic copy(NonogramLogic original) {
        NonogramLogic logic = new NonogramLogic();
        logic.setNonogramRules(original.getNonogramRules());

        logic.setNonogramSolutionBoard(deepCopyStrings(original.getNonogramSolutionBoard()));
        logic.setNonogramSolutionBoardWithMarks(deepCopyStrings(original.getNonogramSolutionBoardWithMarks()));
        logic.setRowsSequencesRanges(deepCopy3DIntegers(original.getRowsSequencesRanges()));
        logic.setColumnsSequencesRanges(deepCopy3DIntegers(original.getColumnsSequencesRanges()));
        logic.setRowsFieldsNotToInclude(deepCopyIntegers(original.getRowsFieldsNotToInclude()));
        logic.setColumnsFieldsNotToInclude(deepCopyIntegers(original.getColumnsFieldsNotToInclude()));
        logic.setRowsSequencesIdsNotToInclude(deepCopyIntegers(original.getRowsSequencesIdsNotToInclude()));
        logic.setColumnsSequencesIdsNotToInclude(deepCopyIntegers(original.getColumnsSequencesIdsNotToInclude()));

        logic.setActionsToDoList(
                new ArrayList<>(original.getActionsToDoList().stream()
                        .map(NonogramActionDetails::copy)
                        .toList())
        );

        logic.setNonogramState(buildInitialEmptyNonogramState());

        logic.initializeHelpers();

        return logic;
    }

    // ======== DEEP COPY HELPERS ========= //

    private List<List<String>> deepCopyStrings(List<List<String>> original) {
        return original.stream()
                .map(inner -> inner.stream()
                        .map(String::new)
                        .collect(Collectors.toCollection(ArrayList::new)))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private List<List<Integer>> deepCopyIntegers(List<List<Integer>> original) {
        List<List<Integer>> copy = new ArrayList<>();

        for (List<Integer> inner : original) {
            copy.add(new ArrayList<>(inner));
        }

        return copy;
    }

    private List<List<List<Integer>>> deepCopy3DIntegers(List<List<List<Integer>>> original) {
        List<List<List<Integer>>> copy = new ArrayList<>();

        for (List<List<Integer>> innerList : original) {
            List<List<Integer>> innerCopy = new ArrayList<>();

            for (List<Integer> deepList : innerList) {
                innerCopy.add(new ArrayList<>(deepList));
            }

            copy.add(innerCopy);
        }

        return copy;
    }

    public NonogramSolutionNode copyNode(NonogramSolutionNode original) {
        NonogramLogic logicCopy = copy(original.getNonogramLogic());
        logicCopy.initializeHelpers();
        NonogramSolutionNode nodeCopy = new NonogramSolutionNode(logicCopy, this);

        nodeCopy.setNonogramGuessDecisions(deepCopyDecisions(original.getNonogramGuessDecisions()));
        nodeCopy.setNonogramRecursionDecisions(deepCopyDecisions(original.getNonogramRecursionDecisions()));

        return nodeCopy;
    }

    private List<NonogramSolutionDecision> deepCopyDecisions(List<NonogramSolutionDecision> originalList) {
        if (originalList == null) return new ArrayList<>();

        List<NonogramSolutionDecision> copiedList = new ArrayList<>();
        for (NonogramSolutionDecision decision : originalList) {
            Field field = decision.getDecisionField();
            Field copiedField = new Field(field.getRowIdx(), field.getColumnIdx());

            NonogramSolutionDecision copied = new NonogramSolutionDecision(decision.getDecisionMarker(), copiedField);
            copiedList.add(copied);
        }
        return copiedList;
    }
}
