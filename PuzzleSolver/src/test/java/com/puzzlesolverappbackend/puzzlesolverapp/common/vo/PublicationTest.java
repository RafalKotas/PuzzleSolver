package com.puzzlesolverappbackend.puzzlesolverapp.common.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PublicationTest {

    @Test
    void noArgsConstructor_shouldCreateNonNullInstance() {
        // when
        Publication publication = new Publication();

        // then
        assertNotNull(publication);
    }
}