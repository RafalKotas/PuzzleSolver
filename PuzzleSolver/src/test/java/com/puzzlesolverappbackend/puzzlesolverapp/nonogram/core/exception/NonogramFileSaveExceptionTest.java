package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NonogramFileSaveExceptionTest {

    @Test
    @DisplayName("Constructor - set and get message and cause")
    void constructor_setsMessageAndCause() {
        // given
        String message = "Could not save file";
        Throwable cause = new RuntimeException("Disk full");

        // when
        NonogramFileSaveException exception = new NonogramFileSaveException(message, cause);

        // then
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}