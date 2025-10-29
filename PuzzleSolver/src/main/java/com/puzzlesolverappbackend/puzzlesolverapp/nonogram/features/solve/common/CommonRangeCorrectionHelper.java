package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.common;

import java.util.*;

import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.rangeInsideAnotherRange;
import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.rangeLength;

public class CommonRangeCorrectionHelper {

    protected boolean updateRanges(Map<Integer, List<Integer>> colouredToSequences, List<List<Integer>> colouredRanges,
                                 List<List<Integer>> sequenceRanges, List<Integer> sequenceLengths) {
        boolean hasChanged = false;

        for (Map.Entry<Integer, List<Integer>> entry : colouredToSequences.entrySet()) {
            List<Integer> possible = entry.getValue();
            if (possible == null || possible.isEmpty()) continue;

            int seqIdx = possible.size() == 1 ? possible.get(0)
                    : possible.stream().min(Comparator.naturalOrder()).orElse(possible.get(0));  // fallback

            List<Integer> seqRange = sequenceRanges.get(seqIdx);
            int seqLen = sequenceLengths.get(seqIdx);
            List<Integer> coloured = colouredRanges.get(entry.getKey());

            int newStart = coloured.get(1) - seqLen + 1;
            int newEnd = coloured.get(0) + seqLen - 1;

            int updatedStart = possible.size() == 1 ? Math.max(newStart, seqRange.get(0)) : seqRange.get(0);
            int updatedEnd = possible.size() == 1 ? Math.min(newEnd, seqRange.get(1)) : seqRange.get(1);

            boolean inside = rangeInsideAnotherRange(coloured, seqRange);
            boolean valid = newStart <= newEnd;

            if (inside && valid && (updatedStart != seqRange.get(0) || updatedEnd != seqRange.get(1))) {
                seqRange.set(0, updatedStart);
                seqRange.set(1, updatedEnd);
                hasChanged = true;
            }
        }

        return hasChanged;
    }

    protected Map<Integer, List<Integer>> collectMatchingSequences(List<List<Integer>> colouredRanges, List<List<Integer>> sequenceRanges,
                                                                 List<Integer> sequenceLengths, boolean fromRight) {
        Map<Integer, List<Integer>> result = new HashMap<>();
        int start = fromRight ? 0 : colouredRanges.size() - 1;
        int end = fromRight ? colouredRanges.size() : -1;
        int step = fromRight ? 1 : -1;

        for (int i = start; i != end; i += step) {
            List<Integer> coloured = colouredRanges.get(i);
            int colouredLen = rangeLength(coloured);

            List<Integer> possible = new ArrayList<>();
            for (int seqIdx = 0; seqIdx < sequenceRanges.size(); seqIdx++) {
                List<Integer> seqRange = sequenceRanges.get(seqIdx);
                int seqLen = sequenceLengths.get(seqIdx);

                if (rangeInsideAnotherRange(coloured, seqRange) && seqLen >= colouredLen) {
                    possible.add(seqIdx);
                }
            }

            result.put(i, possible);
        }

        return result;
    }
}
