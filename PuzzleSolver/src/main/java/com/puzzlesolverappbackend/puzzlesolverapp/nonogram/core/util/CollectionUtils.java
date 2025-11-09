package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.util;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class CollectionUtils {

    // TODO - unit test
    public static <T> List<T> reverseList(List<T> list) {
        List<T> reversed = new ArrayList<>(list);
        Collections.reverse(reversed);
        return reversed;
    }
}
