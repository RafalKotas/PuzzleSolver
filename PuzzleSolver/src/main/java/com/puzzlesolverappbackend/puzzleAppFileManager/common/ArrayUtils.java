package com.puzzlesolverappbackend.puzzleAppFileManager.common;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class ArrayUtils {

    /**
     * Return true if one range is inside range of another
     * @param rangeToCheckIfIsInsideAnother range to check if is inside another (externalRange)
     * @param externalRange potential external range for rangeToCheckIfIsInsideAnother
     * @return true if rangeToCheckIfIsInsideAnother is inside externalRange, false otherwise
     */
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

    public static int sumListElements(List<Integer> list) {
        return list.stream().mapToInt(Integer::intValue).sum();
    }

    public static List<String> get2dimArrayRow(List<List<String>> array, int rowIdx) {
        if (rowIdx >= 0 && rowIdx < array.size()) {
            return array.get(rowIdx);
        } else {
            return null;
        }
    }

    public static List<String> get2dimArrayColumn(List<List<String>> array, int columnIdx) {
        return array.stream()
                .map(row -> {
                    if (row != null && columnIdx >= 0 && columnIdx < row.size()) {
                        return row.get(columnIdx);
                    } else {
                        return null;
                    }
                })
                .collect(java.util.stream.Collectors.toList());
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

    public static <T> List<T> immutableCloneList(List<T> original) {
        return List.copyOf(original);
    }
}
