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

    public static boolean wouldMergeTooLongBackward(int expectedLength,
                                                    int indexBeforeX,
                                                    List<List<Integer>> colouredSequences) {
        int startNew = indexBeforeX - expectedLength + 1;

        return colouredSequences.stream().anyMatch(range -> {
            int start = range.get(0);
            int end   = range.get(1);

            boolean touchesOrOverlaps = startNew <= end + 1;
            boolean extendsAboveStart = start < startNew;

//            if (!touchesOrOverlaps && !extendsAboveStart) {
//                System.out.println("40 not covered");
//            } else if (!touchesOrOverlaps && extendsAboveStart) {
//                System.out.println("42 not covered");
//            } else if (touchesOrOverlaps && !extendsAboveStart) {
//                System.out.println("44 not covered");
//            } else if (touchesOrOverlaps && extendsAboveStart) {
//                System.out.println("46 not covered");
//            }

            return touchesOrOverlaps && extendsAboveStart;
        });
    }
}
