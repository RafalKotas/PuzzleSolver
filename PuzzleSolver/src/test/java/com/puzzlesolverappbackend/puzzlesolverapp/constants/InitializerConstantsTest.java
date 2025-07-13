package com.puzzlesolverappbackend.puzzlesolverapp.constants;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class InitializerConstantsTest {

    @Test
    void constantsShouldHaveExpectedValues() {
        assertEquals("./FrontReact/public/resources/", InitializerConstants.PUZZLE_RELATIVE_PATH);
        assertTrue(InitializerConstants.PRINT_PUZZLE_STATUS_INFO);
        assertEquals("data/solutions/Nonograms/", InitializerConstants.NONOGRAM_SOLUTIONS_PATH);
        assertEquals("sword", InitializerConstants.PUZZLE_NAME);
    }

    @Test
    void puzzleMappingsShouldContainExpectedSuffixes() {
        assertEquals("Nonograms/", InitializerConstants.PuzzleMappings.NONOGRAM_PATH_SUFFIX);
        assertEquals("Akari/", InitializerConstants.PuzzleMappings.AKARI_PATH_SUFFIX);
        assertEquals("Architect/", InitializerConstants.PuzzleMappings.ARCHITECT_PATH_SUFFIX);
        assertEquals("Hitori/", InitializerConstants.PuzzleMappings.HITORI_PATH_SUFFIX);
        assertEquals("Slitherlink/", InitializerConstants.PuzzleMappings.SLITHERLINK_PATH_SUFFIX);
        assertEquals("Sudoku/", InitializerConstants.PuzzleMappings.SUDOKU_PATH_SUFFIX);
    }


    @Test
    void constructorIsPrivateAndThrowsException() throws Exception {
        Constructor<InitializerConstants> constructor = InitializerConstants.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(
                InvocationTargetException.class,
                constructor::newInstance
        );

        Throwable cause = thrown.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    void puzzleMappingsConstructorShouldThrowException() throws Exception {
        Constructor<InitializerConstants.PuzzleMappings> constructor = InitializerConstants.PuzzleMappings.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(
                InvocationTargetException.class,
                constructor::newInstance
        );

        Throwable cause = thrown.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }
}
