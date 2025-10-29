package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.common;

import java.util.ArrayList;
import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.rangeInsideAnotherRange;
import static com.puzzlesolverappbackend.puzzlesolverapp.common.ArrayUtils.rangeLength;

public class CommonXPlacementHelper {

    /**
     * Determines whether the given empty range can only fit sequences that are
     * too long for it. Used to decide whether the range should be filled with Xs.
     * <ul>
     *     <li>Filters out excluded sequences.</li>
     *     <li>Checks if any non-excluded sequence has a range containing this range.</li>
     *     <li>Verifies if all matching sequences are too long for the range.</li>
     * </ul>
     *
     * @param sequenceRanges list of allowed ranges for each sequence
     * @param sequenceLengths list of sequence lengths
     * @param excludedSequenceIds list of sequence indices that should be ignored
     * @param emptyRange the range of empty cells to evaluate
     * @return {@code true} if only too-long sequences match this range
     */
    protected boolean onlyTooLongSequencesFitInRange(
            List<List<Integer>> sequenceRanges,
            List<Integer> sequenceLengths,
            List<Integer> excludedSequenceIds,
            List<Integer> emptyRange
    ) {
        int emptyRangeLength = rangeLength(emptyRange);
        List<Integer> fittingSequences = new ArrayList<>();
        List<Integer> tooLongSequences = new ArrayList<>();

        for (int seqIdx = 0; seqIdx < sequenceLengths.size(); seqIdx++) {
            if (excludedSequenceIds.contains(seqIdx)) continue;

            List<Integer> seqRange = sequenceRanges.get(seqIdx);
            if (rangeInsideAnotherRange(emptyRange, seqRange)) {
                fittingSequences.add(seqIdx);
                if (sequenceLengths.get(seqIdx) > emptyRangeLength) {
                    tooLongSequences.add(seqIdx);
                }
            }
        }

        return !fittingSequences.isEmpty() && fittingSequences.equals(tooLongSequences);
    }
}
