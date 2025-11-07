package com.puzzlesolverappbackend.puzzlesolverapp.common;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ArrayUtils {

    public static <T> List<List<T>> copyTwoDeepList(List<List<T>> nonogramBoard) {
        if (nonogramBoard == null) {
            return new ArrayList<>();
        }

        List<List<T>> copiedBoard = new ArrayList<>();
        for (List<T> boardRow : nonogramBoard) {
            if (boardRow != null) {
                copiedBoard.add(new ArrayList<>(boardRow));
            } else {
                copiedBoard.add(null);
            }
        }
        return copiedBoard;
    }

    public static List<List<List<Integer>>> copySequencesRanges(List<List<List<Integer>>> sequencesRanges) {

        List<List<List<Integer>>> sequencesRangesCopy = new ArrayList<>();

        for (List<List<Integer>> singleSequencesRanges : sequencesRanges) {
            List<List<Integer>> elementSequencesRangesCopy = new ArrayList<>();
            for (List<Integer> sequenceRange : singleSequencesRanges) {
                elementSequencesRangesCopy.add(new ArrayList<>(sequenceRange));
            }
            sequencesRangesCopy.add(elementSequencesRangesCopy);
        }

        return sequencesRangesCopy;
    }

    public static boolean rangeInsideAnotherRange(List<Integer> rangeToCheckIfIsInsideAnother, List<Integer> externalRange) {
        if (!rangeToCheckIfIsInsideAnother.isEmpty() && externalRange.size() >= 2) {
            return (externalRange.get(0) <= rangeToCheckIfIsInsideAnother.get(0)
                    && (rangeToCheckIfIsInsideAnother.get(rangeToCheckIfIsInsideAnother.size() - 1) <= externalRange.get(externalRange.size() - 1)));
        } else {
            return false;
        }
    }

    public static int rangeLength(List<Integer> range) {
        int rangeStart = range.get(0);
        int rangeEnd = range.get(range.size() - 1);
        return rangeEnd - rangeStart + 1;
    }

    public static List<List<Integer>> deepCopy(List<List<Integer>> original) {
        return original.stream()
                .map(ArrayList::new)
                .collect(Collectors.toList());
    }

    public static <T> List<T> copyList(List<T> original) {
        return new ArrayList<>(original);
    }

    public static boolean rangesListNotEqual(List<List<Integer>> a, List<List<Integer>> b) {
        if (a.size() != b.size()) return true;
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i))) return true;
        }
        return false;
    }

    public static int sumListElements(List<Integer> list) {
        return list.stream().mapToInt(Integer::intValue).sum();
    }


    public static <T> List<List<T>> cloneAndMakeImmutable2DList(List<List<T>> original) {
        List<List<T>> cloned = new ArrayList<>();
        for (List<T> innerList : original) {
            cloned.add(List.copyOf(innerList));
        }
        return Collections.unmodifiableList(cloned);
    }

    public static <T> List<List<T>> mutableClone2DList(List<List<T>> original) {
        List<List<T>> cloned = new ArrayList<>();
        for (List<T> innerList : original) {
            cloned.add(new ArrayList<>(innerList));
        }
        return cloned;
    }
}
