package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NonogramStructureFactoryTest {

    @Test
    @DisplayName("NonogramStructureFactory constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<NonogramStructureFactory> constructor = NonogramStructureFactory.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException ex = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = ex.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("Should generate 3 empty rows")
    void shouldGenerateCorrectNumberOfEmptyRows() {
        // given
        int height = 3;

        // when
        List<List<Integer>> rows = NonogramStructureFactory.generateEmptyRows(height);

        // then
        assertEquals(height, rows.size());
        rows.forEach(row -> assertTrue(row.isEmpty()));
    }

    @Test
    @DisplayName("Should generate 4 empty columns")
    void shouldGenerateCorrectNumberOfEmptyColumns() {
        // given
        int width = 4;

        // when
        List<List<Integer>> columns = NonogramStructureFactory.generateEmptyColumns(width);

        // then
        assertEquals(width, columns.size());
        columns.forEach(col -> assertTrue(col.isEmpty()));
    }
}