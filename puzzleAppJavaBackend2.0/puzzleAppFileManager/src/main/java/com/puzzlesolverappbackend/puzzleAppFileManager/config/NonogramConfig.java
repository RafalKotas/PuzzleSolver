package com.puzzlesolverappbackend.puzzleAppFileManager.config;

public class NonogramConfig {

    public final static String DECISIONS_OUTPUT_DIR = "FROM_FIRST";
    public final static String RECURSIVE_OUTPUT_DIR = "RECURSION_MAX_FROM_BOTH_NODES";

    public final static String getNonogramResultsOutputPath() {
        return DECISIONS_OUTPUT_DIR + "/" + RECURSIVE_OUTPUT_DIR + "/";
    }
}
