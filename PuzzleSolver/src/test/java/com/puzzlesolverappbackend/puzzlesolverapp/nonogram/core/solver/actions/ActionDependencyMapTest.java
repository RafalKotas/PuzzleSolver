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
        List<NonogramSolveAction> dependencies = ActionDependencyMap.getDependenciesFor(NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_ROW);

        assertNotNull(dependencies);
        assertTrue(dependencies.contains(NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW));
        assertTrue(dependencies.contains(NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_IN_ROW));
        assertEquals(8, dependencies.size());
    }

    @Test
    void returnsCorrectDependenciesForCorrectColumnSequencesRanges() {
        List<NonogramSolveAction> dependencies = ActionDependencyMap.getDependenciesFor(NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_COLUMN);

        assertTrue(dependencies.contains(NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN));
        assertTrue(dependencies.contains(NonogramSolveAction.PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_COLUMN));
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

        assertTrue(dependencies.contains(NonogramSolveAction.PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_COLUMN));
    }

    @Test
    void returnsExpectedDependenciesForMarkAvailableFieldsInRow() {
        List<NonogramSolveAction> dependencies = ActionDependencyMap.getDependenciesFor(NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW);

        assertEquals(List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_ROW,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW,
                NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_ROW
        ), dependencies);
    }
}