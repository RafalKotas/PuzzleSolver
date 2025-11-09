package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.actions;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramActionDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ActionDetailsGeneratorTest {

    @Test
    @DisplayName("Should generate all possible single action details")
    void generateAllPossibleSingleActionDetails_shouldReturnCorrectSizeAndValues() {
        // given
        int height = 2;
        int width = 2;

        // when
        List<NonogramActionDetails> result = ActionDetailsGenerator.generateAllPossibleSingleActionDetails(height, width);

        // then
        assertThat(result)
                .isNotEmpty()
                .allSatisfy(detail -> {
                    assertThat(detail.getIndex()).isNotNegative();
                    assertThat(detail.getActionName()).isNotNull();
                    assertThat(detail.isChangedState()).isFalse();
                });
    }

    @Test
    @DisplayName("Should generate NonogramActionDetails list in row")
    void generateRowActionDetails_shouldReturnExpectedNumberOfDetailsList() {
        // given
        int height = 1;

        // when
        List<NonogramActionDetails> rowDetails = ActionDetailsGenerator.generateRowActionDetailsList(height);

        // then
        assertThat(rowDetails)
                .isNotEmpty()
                .allSatisfy(detail -> {
                    assertThat(detail.getActionName().name()).contains("ROW");
                });
    }

    @Test
    @DisplayName("Should generate NonogramActionDetails list in column")
    void generateColumnActionDetails_shouldReturnExpectedNumberOfDetailsList() {
        // given
        int width = 1;

        // when
        List<NonogramActionDetails> colDetails = ActionDetailsGenerator.generateColumnActionDetailsList(width);

        // then
        assertThat(colDetails)
                .isNotEmpty()
                .allSatisfy(detail -> {
                    assertThat(detail.getActionName().name()).contains("COLUMN");
                });
    }

    @Test
    @DisplayName("ActionDetailsGenerator constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructorIsPrivateAndThrowsException() throws Exception {
        // given
        Constructor<ActionDetailsGenerator> constructor = ActionDetailsGenerator.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException thrown = assertThrows(
                InvocationTargetException.class,
                constructor::newInstance
        );

        // then
        Throwable cause = thrown.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }
}
