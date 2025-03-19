package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils;

import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public class NonogramSequenceReducer {
    public static List<List<Integer>> reduceMatches(List<List<Integer>> colouredMatches, List<Boolean> differentSequencesId) {
        boolean changed;

        do {
            changed = false;
            List<List<Integer>> newMatches = new ArrayList<>();

            for (int i = 0; i < colouredMatches.size(); i++) {
                List<Integer> current = colouredMatches.get(i);

                if (i < colouredMatches.size() - 1 && differentSequencesId.get(i)) {
                    List<Integer> next = colouredMatches.get(i + 1);

                    Set<Integer> union = new HashSet<>(current);
                    union.addAll(next);

                    if ((current.size() > 1 || next.size() > 1) && union.size() == current.size() + next.size() - countOverlap(current, next)) {
                        if (union.size() == 2) {
                            Iterator<Integer> it = union.iterator();
                            newMatches.add(List.of(it.next()));
                            newMatches.add(List.of(it.next()));
                            i++;
                            changed = true;
                            continue;
                        }
                    }
                }

                newMatches.add(current);
            }

            colouredMatches = newMatches;
        } while (changed);

        return colouredMatches;
    }

    private static int countOverlap(List<Integer> a, List<Integer> b) {
        Set<Integer> setA = new HashSet<>(a);
        int count = 0;
        for (Integer val : b) {
            if (setA.contains(val)) count++;
        }
        return count;
    }

}
