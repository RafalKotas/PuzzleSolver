package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionUtils {

    public static <T> List<T> reverseList(List<T> list) {
        List<T> reversed = new ArrayList<>(list);
        Collections.reverse(reversed);
        return reversed;
    }
}
