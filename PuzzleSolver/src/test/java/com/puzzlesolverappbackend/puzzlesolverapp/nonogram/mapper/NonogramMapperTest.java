package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.mapper;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.NonogramLogicResponse;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto.NonogramSolutionSaveRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class NonogramMapperTest {

    @Test
    @DisplayName("NonogramMapper constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<NonogramMapper> constructor = NonogramMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("should map NonogramLogic to NonogramSolutionSaveRequest correctly")
    void shouldMapToSaveRequest() {
        // given
        NonogramLogic logic = TestDataFactory.minimalNonogramLogicForSaveRequest();
        String fileName = "o06005";

        // when
        NonogramSolutionSaveRequest result = NonogramMapper.toSaveRequest(logic, fileName);

        // then
        assertEquals(fileName, result.getFileName());
        assertEquals(logic.getNonogramSolutionBoard(), result.getBoard());
        assertEquals(logic.getNonogramRules().getRowSequencesLengths(), result.getRowSequences());
        assertEquals(logic.getNonogramRules().getColumnSequencesLengths(), result.getColumnSequences());
    }

    @Test
    @DisplayName("should map NonogramLogic to NonogramLogicResponse correctly o06005")
    void shouldMapToResponse() {
        // given
        NonogramLogic logic = TestDataFactory.minimalNonogramLogicForResponse();

        // when
        NonogramLogicResponse result = NonogramMapper.toResponse(logic);

        // then
        assertEquals(logic.getNonogramSolutionBoard(), result.getNonogramSolutionBoard());
        assertEquals(logic.getNonogramSolutionBoardWithMarks(), result.getNonogramSolutionBoardWithMarks());
        assertEquals(logic.getRowsSequencesRanges(), result.getRowsSequencesRanges());
        assertEquals(logic.getColumnsSequencesRanges(), result.getColumnsSequencesRanges());
        assertEquals(logic.getRowsFieldsNotToInclude(), result.getRowsFieldsNotToInclude());
        assertEquals(logic.getColumnsFieldsNotToInclude(), result.getColumnsFieldsNotToInclude());
        assertEquals(logic.getRowsSequencesIdsNotToInclude(), result.getRowsSequencesIdsNotToInclude());
        assertEquals(logic.getColumnsSequencesIdsNotToInclude(), result.getColumnsSequencesIdsNotToInclude());
        assertEquals(logic.getNonogramRules(), result.getNonogramRules());
        assertEquals(logic.getNonogramState(), result.getNonogramState());
    }

}