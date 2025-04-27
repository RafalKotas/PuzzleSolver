package com.puzzlesolverappbackend.puzzleAppFileManager.common;

import com.puzzlesolverappbackend.puzzleAppFileManager.constants.InitializerConstants;

import static com.puzzlesolverappbackend.puzzleAppFileManager.constants.SharedConsts.JSON_EXTENSION;

public class FileHelper {

    private static final String SOLUTION_PREFIX = "r";

    public static String nonogramSolutionSavePathForFilename(String filename) {
        return InitializerConstants.NONOGRAM_SOLUTIONS_PATH + SOLUTION_PREFIX + filename + JSON_EXTENSION;
    }

    public static String nonogramSolutionLoadPathForFilename(String solutionFileName) {
        return InitializerConstants.NONOGRAM_SOLUTIONS_PATH + solutionFileName + JSON_EXTENSION;
    }
}
