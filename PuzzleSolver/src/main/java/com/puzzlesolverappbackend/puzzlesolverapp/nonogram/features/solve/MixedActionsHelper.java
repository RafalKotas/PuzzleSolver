package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class MixedActionsHelper {

    public static boolean wouldMergeTooLongForward(int expectedLength, int indexAfterX, List<List<Integer>> colouredSequences) {
        int contactIndex = indexAfterX + expectedLength - 1;

        return colouredSequences.stream().anyMatch(colouredSequenceRange -> {
            int start = colouredSequenceRange.get(0);
            int end = colouredSequenceRange.get(1);

            if (contactIndex >= start - 1) {
                int firstPartLength = contactIndex - indexAfterX + 1;
                int secondPartStart = contactIndex + 1;
                int secondPartLength = contactIndex == end ? 0 : Math.max(0, end - secondPartStart + 1);
                return firstPartLength + secondPartLength > expectedLength;
            }
            return false;
        });
    }

    public static boolean wouldMergeTooLongBackward(int expectedLength, int indexBeforeX, List<List<Integer>> colouredSequences) {
        int contactIndex = indexBeforeX - expectedLength + 1;

        return colouredSequences.stream().anyMatch(colouredSequenceRange -> {
            int start = colouredSequenceRange.get(0);
            int end = colouredSequenceRange.get(1);

            if (contactIndex <= end + 1) {
                int firstPartEnd = contactIndex - 1;
                int firstPartLength = Math.max(0, firstPartEnd - start + 1);
                int secondPartLength = indexBeforeX - contactIndex + 1;
                return firstPartLength + secondPartLength > expectedLength;
            }
            return false;
        });
    }
}
