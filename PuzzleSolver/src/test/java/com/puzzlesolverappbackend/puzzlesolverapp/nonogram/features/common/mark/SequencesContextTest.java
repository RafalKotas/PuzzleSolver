package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.mark;

import org.apache.logging.log4j.util.TriConsumer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SequencesContextTest {

    @Test
    @DisplayName("AllArgsConstructor should create SequencesContext with all fields")
    void shouldCreateContextWithAllFields() {
        // given
        List<List<Integer>> lengths = List.of(List.of(1, 2), List.of(3));
        List<List<List<Integer>>> ranges = List.of(
                List.of(List.of(0, 6), List.of(2, 9)),
                List.of(List.of(0, 9))
        );
        TriConsumer<Integer, Integer, List<Integer>> updateConsumer = (a, b, c) -> {};
        BiConsumer<Integer, Integer> excludeConsumer = (a, b) -> {};

        // when
        SequencesContext context = new SequencesContext(lengths, ranges, updateConsumer, excludeConsumer);

        // then
        assertEquals(lengths, context.getSequencesLengths());
        assertEquals(ranges, context.getSequencesRanges());
        assertEquals(updateConsumer, context.getUpdateRangeConsumer());
        assertEquals(excludeConsumer, context.getExcludeSequenceConsumer());
    }
}