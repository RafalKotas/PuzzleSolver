package com.puzzlesolverappbackend.puzzleAppFileManager.logicOperators;

public class LogicFunctions {

    public static boolean xor(boolean a, boolean b) {
        return (a && !b) || (!a && b);
    }
}
