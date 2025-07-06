package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NonogramConstants {

    public static final char MARKED_ROW_INDICATOR = 'R';

    public static final char MARKED_COLUMN_INDICATOR = 'C';

    // 4-char mark only for "X" (finally empty field) coloured field marked with "Ra--"/"RaCa" etc.
    public static final String X_FIELD_MARKED_BOARD = "XXXX";

    public static final String COLOURED_FIELD = "O";

    public static final String COLOURED_FIELD_MARKED_BOARD = "OOOO";

    public static final String X_FIELD = "X";

    public static final String EMPTY_FIELD = "-";

    public static final String EMPTY_PART_MARKED_BOARD = "--";

    public static final String EMPTY_FIELD_MARKED_BOARD = "----";
}
