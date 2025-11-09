package com.puzzlesolverappbackend.puzzlesolverapp.constants;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class SharedConstantsTest {

    @Test
    @DisplayName("SharedConstants constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructorShouldThrowException() throws Exception {
        // given
        Constructor<SharedConstants> constructor = SharedConstants.class.getDeclaredConstructor();
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

    @Test
    @DisplayName("constants should have expected values")
    void constantsShouldHaveExpectedValues() {
        assertEquals(".json", SharedConstants.JSON_EXTENSION);
        assertEquals(5, SharedConstants.JSON_EXTENSION_LENGTH);
    }
}