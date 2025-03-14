package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.enums;

public enum NonogramCorrectnessIndicator {

    VALID,

    INVALID_DIMENSIONS_ROWS,
    INVALID_DIMENSIONS_COLUMNS,

    TOO_LONG_ROW_SEQUENCE,
    TOO_LONG_COLUMN_SEQUENCE,

    SUM_MISMATCH_ROWS_COLUMNS
}
