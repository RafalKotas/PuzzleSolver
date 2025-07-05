package com.puzzlesolverappbackend.puzzlesolverapp.common;

public class LogicFunctions {

    public static boolean xor(boolean a, boolean b) {
        return (a && !b) || (!a && b);
    }
}
