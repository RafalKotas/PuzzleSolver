package com.puzzlesolverappbackend.puzzleAppFileManager.utils;

import lombok.experimental.UtilityClass;

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

    public static boolean allElementsLowerThanValue(List<Integer> elements, int value) {
        return elements.stream().allMatch(columnSequence -> columnSequence < value);
    }
}
