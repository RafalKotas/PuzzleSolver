package com.puzzlesolverappbackend.puzzleAppFileManager.runners;

public enum InitializerConstants {;
    public static final String PUZZLE_RELATIVE_PATH = "../../FrontReact/public/resources/";
    public static final boolean PRINT_PUZZLE_STATUS_INFO = false;

    public static final String NONOGRAM_RESULTS_PATH = "./src/main/resources/decisions/";
    public static final String NONOGRAM_SOLUTIONS_PATH = "./src/main/resources/solutions/nonograms/";

    public static final String PUZZLE_NAME = "sword";

    public enum PuzzleMappings {;
        public static final String NONOGRAM_PATH_SUFFIX = "allNonogramsJSON/";
        public static final String AKARI_PATH_SUFFIX = "Akari/";
        public static final String ARCHITECT_PATH_SUFFIX = "Architect/";
        public static final String HITORI_PATH_SUFFIX = "Hitori/";
        public static final String SLITHERLINK_PATH_SUFFIX = "Slitherlink/";
        public static final String SUDOKU_PATH_SUFFIX = "Sudoku/";
    }
}
