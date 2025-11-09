package com.puzzlesolverappbackend.puzzlesolverapp.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class LogicFunctionsTest {

    @Test
    @DisplayName("LogicFunctions constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<LogicFunctions> constructor = LogicFunctions.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }

    @Test
    @DisplayName("xor - should return true if only 1st value from 2 is true")
    void xor_returnsTrue_whenOnlyFirstArgumentIsTrue() {
        // given
        boolean a = true;
        boolean b = false;

        // when
        boolean result = LogicFunctions.xor(a, b);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("xor - should return true if only 2nd value from 2 is true")
    void xor_returnsTrue_whenOnlySecondArgumentIsTrue() {
        // given
        boolean a = false;
        boolean b = true;

        // when
        boolean result = LogicFunctions.xor(a, b);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("xor - should return false if both values are true")
    void xor_returnsFalse_whenBothArgumentsAreTrue() {
        // given
        boolean a = true;
        boolean b = true;

        // when
        boolean result = LogicFunctions.xor(a, b);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("xor - should return false if both values are false")
    void xor_returnsFalse_whenBothArgumentsAreFalse() {
        // given
        boolean a = false;
        boolean b = false;

        // when
        boolean result = LogicFunctions.xor(a, b);

        // then
        assertFalse(result);
    }
}
