package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.actions;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class ActionDependencyMapTest {

    @Test
    @DisplayName("ActionDependencyMap constructor should throw UnsupportedOperationException - reflect instantiation")
    void privateConstructorThrowsExpectedExceptionAndIsCovered() throws Exception {
        Constructor<ActionDependencyMap> constructor = ActionDependencyMap.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException ex = assertThrows(
                InvocationTargetException.class,
                constructor::newInstance
        );

        Throwable cause = ex.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("Should return correct dependencies for CORRECT_SEQUENCES_RANGES_IN_ROW")
    void returnsCorrectDependenciesForCorrectRowSequencesRanges() {
        // given && when
        List<NonogramSolveAction> dependencies = ActionDependencyMap.getDependenciesFor(NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_ROW);

        // then
        assertThat(dependencies)
                .isNotNull()
                .hasSize(9)
                .containsAll(
                        List.of(
                                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS_IN_ROW,
                                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_ROW,
                                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_ROW,
                                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE_IN_ROW,
                                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW,
                                NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_ROW,
                                NonogramSolveAction.PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_ROW,
                                NonogramSolveAction.PLACE_XS_IF_COLOURING_FIELD_WILL_CAUSE_ASSIGNMENT_CONFLICT_IN_ROW,
                                NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_IN_ROW
                        )
                );
    }

    @Test
    @DisplayName("Should return correct dependencies for CORRECT_SEQUENCES_RANGES_IN_COLUMN")
    void returnsCorrectDependenciesForCorrectColumnSequencesRanges() {
        // given && when
        List<NonogramSolveAction> dependencies = ActionDependencyMap.getDependenciesFor(NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_COLUMN);

        // then
        assertThat(dependencies)
                .isNotNull()
                .hasSize(9)
                .containsAll(
                        List.of(
                                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS_IN_COLUMN,
                                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_COLUMN,
                                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_COLUMN,
                                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE_IN_COLUMN,
                                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN,
                                NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_COLUMN,
                                NonogramSolveAction.PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_COLUMN,
                                NonogramSolveAction.PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_COLUMN,
                                NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_IN_COLUMN
                        )
                );
    }

    @Test
    @DisplayName("Should return empty dependencies for UNKNOWN action")
    void returnsEmptyListForActionWithNoDependencies() {
        // given && when
        List<NonogramSolveAction> dependencies = ActionDependencyMap.getDependenciesFor(NonogramSolveAction.UNKNOWN);

        // then
        assertNotNull(dependencies);
        assertTrue(dependencies.isEmpty());
    }

    @Test
    @DisplayName("Should return correct dependencies for COLOUR_OVERLAPPING_FIELDS_IN_ROW")
    void containsExpectedDependencyForColourOverlappingFieldsInRow() {
        // given && when
        List<NonogramSolveAction> dependencies = ActionDependencyMap.getDependenciesFor(NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW);

        // then
        assertThat(dependencies)
                .isNotNull()
                .hasSize(9)
                .containsAll(
                        List.of(
                                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW,

                                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS_IN_COLUMN,
                                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_COLUMN,
                                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE_IN_COLUMN,
                                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                                NonogramSolveAction.COLOUR_FIELDS_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE_IN_COLUMN,
                                NonogramSolveAction.PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_COLUMN,
                                NonogramSolveAction.PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_COLUMN,
                                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN
                        )
                );
    }

    @Test
    @DisplayName("Should return correct dependencies for COLOUR_OVERLAPPING_FIELDS_IN_ROW")
    void returnsExpectedDependenciesForMarkAvailableFieldsInRow() {
        // given && when
        List<NonogramSolveAction> dependencies = ActionDependencyMap.getDependenciesFor(NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW);

        // then
        assertThat(dependencies)
                .isNotNull()
                .hasSize(3)
                .containsAll(
                        List.of(
                                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_ROW,
                                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW,
                                NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_ROW
                        )
                );
    }
}