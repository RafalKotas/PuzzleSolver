package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.actions;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramActionDetails;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class ActionDetailsGeneratorTest {

    @Test
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
                    assertThat(detail.getIndex()).isNotNull();
                    assertThat(detail.getActionName()).isNotNull();
                    assertThat(detail.isChangedState()).isFalse();
                });
    }

    @Test
    void generateRowActionDetails_shouldReturnExpectedNumberOfDetails() {
        // given
        int height = 1;

        // when
        List<NonogramActionDetails> rowDetails = ActionDetailsGenerator.generateRowActionDetails(height);

        // then
        assertThat(rowDetails)
                .isNotEmpty()
                .allSatisfy(detail -> {
                    assertThat(detail.getActionName().name()).contains("ROW");
                });
    }

    @Test
    void generateColumnActionDetails_shouldReturnExpectedNumberOfDetails() {
        // given
        int width = 1;

        // when
        List<NonogramActionDetails> colDetails = ActionDetailsGenerator.generateColumnActionDetails(width);

        // then
        assertThat(colDetails)
                .isNotEmpty()
                .allSatisfy(detail -> {
                    assertThat(detail.getActionName().name()).contains("COLUMN");
                });
    }

    @Test
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
