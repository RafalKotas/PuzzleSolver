package com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram;

public class NonogramConstants {
    public final static int LAST_ROW_ACTION_ENUM_VALUE = 8;

    public final static char MARKED_ROW_INDICATOR = 'R';

    public final static char MARKED_COLUMN_INDICATOR = 'C';

    // 4-char mark only for "X" (finally empty field) coloured field marked with "Ra--"/"RaCa" etc.
    public final static String X_FIELD_MARKED_BOARD = "XXXX";

    public final static String COLOURED_FIELD = "O";

    public final static String COLOURED_FIELD_MARKED_BOARD = "OOOO";

    public final static String X_FIELD = "X";

    public final static String EMPTY_FIELD = "-";

    public final static String EMPTY_FIELD_MARKED_BOARD = "----";

    public final static String EMPTY_LOG_WARNING = "Trying to add empty log!!!";
}
