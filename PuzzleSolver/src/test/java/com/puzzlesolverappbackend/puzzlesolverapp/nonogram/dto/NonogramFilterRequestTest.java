package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NonogramFilterRequestTest {

    @DisplayName("should correctly construct NonogramFilterRequest and return values")
    @Test
    void shouldConstructNonogramFilterRequest() {
        // given
        List<String> sources = List.of("user", "system");
        List<String> years = List.of("2024");
        List<String> months = List.of("07");
        Double minDifficulty = 1.0;
        Double maxDifficulty = 5.0;
        Integer minHeight = 5;
        Integer maxHeight = 20;
        Integer minWidth = 5;
        Integer maxWidth = 20;

        // when
        NonogramFilterRequest request = new NonogramFilterRequest(
                sources, years, months,
                minDifficulty, maxDifficulty,
                minHeight, maxHeight, minWidth, maxWidth
        );

        // then
        assertEquals(sources, request.getSources());
        assertEquals(years, request.getYears());
        assertEquals(months, request.getMonths());
        assertEquals(minDifficulty, request.getMinDifficulty());
        assertEquals(maxDifficulty, request.getMaxDifficulty());
        assertEquals(minHeight, request.getMinHeight());
        assertEquals(maxHeight, request.getMaxHeight());
        assertEquals(minWidth, request.getMinWidth());
        assertEquals(maxWidth, request.getMaxWidth());
    }
}