package com.puzzlesolverappbackend.puzzlesolverapp.common.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DimensionsTest {

    @Test
    void noArgsConstructor_shouldCreateNonNullInstance() {
        // when
        Dimensions dimensions = new Dimensions();

        // then
        assertNotNull(dimensions);
    }
}