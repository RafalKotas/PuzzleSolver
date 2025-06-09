package com.puzzlesolverappbackend.puzzleAppFileManager.common;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ArrayUtils {

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

    public static List<List<Integer>> deepCopy(List<List<Integer>> list) {
        return list.stream()
                .map(ArrayList::new)
                .collect(Collectors.toList());
    }

    public static boolean rangesListEqual(List<List<Integer>> a, List<List<Integer>> b) {
        if (a.size() != b.size()) return false;
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i))) return false;
        }
        return true;
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
