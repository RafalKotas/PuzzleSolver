package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solutions.NonogramSolutionDecision;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solutions.NonogramSolutionNode;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.config.GuessMode;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.NonogramSolvePayload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NonogramLogicFactoryTest {

    @Test
    @DisplayName("shouldCreateProperLogicFromPayload")
    void shouldCreateProperLogicFromPayload() {
        // given
        NonogramSolvePayload payload = TestDataFactory.prepareFullNonogramPayload();

        NonogramLogicFactory factory = new NonogramLogicFactory();

        // when
        NonogramLogic result = factory.createFromPayload(payload);

        // then
        assertNotNull(result);
        assertNotNull(result.getNonogramRules());
        assertEquals(payload.getRowSequences(), result.getNonogramRules().getRowSequencesLengths());
        assertEquals(payload.getColumnSequences(), result.getNonogramRules().getColumnSequencesLengths());
        assertEquals(payload.getNonogramRules().getHeight(), result.getNonogramRules().getHeight());
        assertEquals(payload.getNonogramRules().getWidth(), result.getNonogramRules().getWidth());

        assertEquals(payload.getNonogramSolutionBoard(), result.getNonogramSolutionBoard());
        assertEquals(payload.getNonogramSolutionBoardWithMarks(), result.getNonogramSolutionBoardWithMarks());

        assertEquals(payload.getRowsSequencesRanges(), result.getRowsSequencesRanges());
        assertEquals(payload.getColumnsSequencesRanges(), result.getColumnsSequencesRanges());

        assertEquals(payload.getRowsFieldsNotToInclude(), result.getRowsFieldsNotToInclude());
        assertEquals(payload.getColumnsFieldsNotToInclude(), result.getColumnsFieldsNotToInclude());
        assertEquals(payload.getRowsSequencesIdsNotToInclude(), result.getRowsSequencesIdsNotToInclude());
        assertEquals(payload.getColumnsSequencesIdsNotToInclude(), result.getColumnsSequencesIdsNotToInclude());

        assertNotNull(result.getActionsToDoList());
        assertEquals(40, result.getActionsToDoList().size()); // 20 rows + 20 columns

        assertNotNull(result.getNonogramState());
        assertNotNull(result.getBoardAccessHelper());
        assertNotNull(result.getActionScheduler());
        assertNotNull(result.getFieldClearingHelper());

        // logic row/col wrappers
        assertNotNull(result.getNonogramRowLogic());
        assertNotNull(result.getNonogramColumnLogic());

        assertEquals(result.getNonogramRules(), result.getNonogramRowLogic().getNonogramRules());
        assertEquals(result.getNonogramRules(), result.getNonogramColumnLogic().getNonogramRules());
    }

    @Test
    @DisplayName("copyNode should clone logic with recursion list=null and copied guessDecisions list")
    void copyNode_deepCopies_logic_and_decisions() {
        // given
        NonogramLogicFactory factory = new NonogramLogicFactory();
        NonogramSolutionNode original = new NonogramSolutionNode(logic3x3(), factory);

        // add some decisions
        original.setNonogramGuessDecisions(new ArrayList<>(
                List.of(
                        new NonogramSolutionDecision("X", new Field(0, 1)),
                        new NonogramSolutionDecision("O", new Field(2, 2))
                )
        ));
        original.setNonogramRecursionDecisions(null);

        // tweak logic so we can check independence later
        original.getNonogramLogic().getNonogramSolutionBoard().get(0).set(0, "O");

        // when
        NonogramSolutionNode copy = factory.copyNode(original);

        // then: node object is different
        assertThat(copy).isNotSameAs(original);

        // logic is a deep clone (different instance, same state)
        assertThat(copy.getNonogramLogic()).isNotSameAs(original.getNonogramLogic());
        assertThat(copy.getNonogramLogic().getNonogramSolutionBoard())
                .isEqualTo(original.getNonogramLogic().getNonogramSolutionBoard());

        // decisions lists are deep-copied (different lists)
        assertThat(copy.getNonogramGuessDecisions())
                .isNotSameAs(original.getNonogramGuessDecisions())
                .hasSize(2);

        // mutate original lists – copy must remain unchanged
        original.getNonogramGuessDecisions().add(
                new NonogramSolutionDecision("O", new Field(1, 1)));
        original.getNonogramLogic().getNonogramSolutionBoard().get(0).set(0, "X");

        assertThat(copy.getNonogramGuessDecisions()).hasSize(2);
        assertThat(copy.getNonogramLogic().getNonogramSolutionBoard().get(0).get(0)).isEqualTo("O");
    }

    private NonogramRules rules3x3() {
        List<List<Integer>> rows = List.of(List.of(3), List.of(1), List.of(3));
        List<List<Integer>> cols = List.of(List.of(3), List.of(1), List.of(3));
        return new NonogramRules(rows, cols, 3, 3);
    }

    private NonogramLogic logic3x3() {
        return new NonogramLogic(rules3x3(), GuessMode.DISABLED);
    }
}