package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.NonogramSolvePayload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NonogramLogicFactoryTest {

    @Test
    @DisplayName("shouldCreateProperLogicFromPayload")
    void shouldCreateProperLogicFromPayload() {
        // given
        NonogramSolvePayload payload = TestDataFactory.prepareFullNonogramPayload(); // zakładamy, że masz builder lub statyczną metodę

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
        assertEquals(40, result.getActionsToDoList().size()); // 20 wierszy + 20 kolumn

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
}