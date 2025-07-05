package com.puzzlesolverappbackend.puzzlesolverapp.common;

import com.puzzlesolverappbackend.puzzlesolverapp.constants.InitializerConstants;

public class FileHelper {

    private static final String SOLUTION_PREFIX = "r";

    public static String nonogramSolutionSavePathForFilename(String solutionFileName) {
        return InitializerConstants.NONOGRAM_SOLUTIONS_PATH + SOLUTION_PREFIX + solutionFileName;
    }

    public static String nonogramSolutionLoadPathForFilename(String solutionFileName) {
        return InitializerConstants.NONOGRAM_SOLUTIONS_PATH + solutionFileName;
    }
}
