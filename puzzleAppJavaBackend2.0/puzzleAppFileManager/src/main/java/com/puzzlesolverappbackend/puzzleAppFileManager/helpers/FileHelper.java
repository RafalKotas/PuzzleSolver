package com.puzzlesolverappbackend.puzzleAppFileManager.helpers;

import com.puzzlesolverappbackend.puzzleAppFileManager.runners.InitializerConstants;

public class FileHelper {

    public static String generateSavePathForFilename(String filename) {
        return InitializerConstants.NONOGRAM_SOLUTIONS_PATH + "r" + filename + ".json";
    }
}
