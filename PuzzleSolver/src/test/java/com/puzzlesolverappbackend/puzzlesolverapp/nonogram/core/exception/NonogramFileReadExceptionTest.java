package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NonogramFileReadExceptionTest {

    @Test
    void constructor_setsMessageAndCause() {
        String message = "Could not read file";
        Throwable cause = new RuntimeException("File not found");

        NonogramFileReadException exception = new NonogramFileReadException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}