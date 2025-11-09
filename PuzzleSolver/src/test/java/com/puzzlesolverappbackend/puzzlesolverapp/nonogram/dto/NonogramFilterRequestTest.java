package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NonogramFilterRequestTest {

    @Test
    @DisplayName("should correctly construct NonogramFilterRequest and return values")
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
        assertEquals(List.of("user", "system"), request.getSources());
        assertEquals(List.of("2024"), request.getYears());
        assertEquals(List.of("07"), request.getMonths());
        assertEquals(1.0, request.getMinDifficulty());
        assertEquals(5.0, request.getMaxDifficulty());
        assertEquals(5, request.getMinHeight());
        assertEquals(20, request.getMaxHeight());
        assertEquals(5, request.getMinWidth());
        assertEquals(20, request.getMaxWidth());
    }
}