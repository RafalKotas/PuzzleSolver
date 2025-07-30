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
    }

    @Test
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
