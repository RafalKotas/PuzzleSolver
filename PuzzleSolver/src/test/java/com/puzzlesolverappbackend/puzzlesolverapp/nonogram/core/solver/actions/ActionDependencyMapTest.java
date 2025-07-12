package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.actions;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ActionDependencyMapTest {

    @Test
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
    void returnsCorrectDependenciesForCorrectRowSequencesRanges() {
        List<NonogramSolveAction> dependencies = ActionDependencyMap.getDependenciesFor(NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES);

        assertNotNull(dependencies);
        assertTrue(dependencies.contains(NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW));
        assertTrue(dependencies.contains(NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH));
        assertEquals(8, dependencies.size());
    }

    @Test
    void returnsCorrectDependenciesForCorrectColumnSequencesRanges() {
        List<NonogramSolveAction> dependencies = ActionDependencyMap.getDependenciesFor(NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES);

        assertTrue(dependencies.contains(NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN));
        assertTrue(dependencies.contains(NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE));
        assertEquals(9, dependencies.size());
    }

    @Test
    void returnsEmptyListForActionWithNoDependencies() {
        List<NonogramSolveAction> dependencies = ActionDependencyMap.getDependenciesFor(NonogramSolveAction.UNKNOWN);

        assertNotNull(dependencies);
        assertTrue(dependencies.isEmpty());
    }

    @Test
    void containsExpectedDependencyForColourOverlappingFieldsInRow() {
        List<NonogramSolveAction> dependencies = ActionDependencyMap.getDependenciesFor(NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW);

        assertTrue(dependencies.contains(NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES));
    }

    @Test
    void returnsExpectedDependenciesForMarkAvailableFieldsInRow() {
        List<NonogramSolveAction> dependencies = ActionDependencyMap.getDependenciesFor(NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW);

        assertEquals(List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW,
                NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS
        ), dependencies);
    }
}