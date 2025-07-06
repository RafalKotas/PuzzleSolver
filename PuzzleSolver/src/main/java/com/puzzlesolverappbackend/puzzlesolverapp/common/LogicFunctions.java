package com.puzzlesolverappbackend.puzzlesolverapp.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LogicFunctions {

    public static boolean xor(boolean a, boolean b) {
        return (a && !b) || (!a && b);
    }
}
