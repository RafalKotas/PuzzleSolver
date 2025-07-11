package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.mark;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.List;
import java.util.function.BiConsumer;

@AllArgsConstructor
@Getter
@Setter
public class SequencesContext {
    private final List<List<Integer>> sequencesLengths;
    private final List<List<List<Integer>>> sequencesRanges;
    private final TriConsumer<Integer, Integer, List<Integer>> updateRangeConsumer;
    private final BiConsumer<Integer, Integer> excludeSequenceConsumer;
}

