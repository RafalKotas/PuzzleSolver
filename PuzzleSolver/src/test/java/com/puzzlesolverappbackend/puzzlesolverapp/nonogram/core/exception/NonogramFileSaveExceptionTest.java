package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NonogramFileSaveExceptionTest {

    @Test
    void constructor_setsMessageAndCause() {
        String message = "Could not save file";
        Throwable cause = new RuntimeException("Disk full");

        NonogramFileSaveException exception = new NonogramFileSaveException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}