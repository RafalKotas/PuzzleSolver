package com.puzzlesolverappbackend.puzzleAppFileManager.payload;

import java.util.ArrayList;
import java.util.Arrays;

public class ActionsConstants {
    public final static int CORRECT_ROWS_SEQUENCES_RANGES = 1;
    public final static int CORRECT_COLUMNS_SEQUENCES_RANGES = 2;
    public final static int CORRECT_ROWS_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS = 3;
    public final static int CORRECT_COLUMNS_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS = 4;
    public final static int CORRECT_ROWS_SEQUENCES_RANGES_IF_X_ON_WAY = 5;
    public final static int CORRECT_COLUMNS_SEQUENCES_RANGES_IF_X_ON_WAY = 6;
    public final static int COLOUR_OVERLAPPING_FIELDS_IN_ROWS = 7;
    public final static int COLOUR_OVERLAPPING_FIELDS_IN_COLUMNS = 8;
    public final static int EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROWS = 9;
    public final static int EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMNS = 10;
    public final static int PLACE_XS_ROWS_AT_UNREACHABLE_FIELDS = 11;
    public final static int PLACE_XS_COLUMNS_AT_UNREACHABLE_FIELDS = 12;
    public final static int PLACE_XS_ROWS_AROUND_LONGEST_SEQUENCES = 13;
    public final static int PLACE_XS_COLUMNS_AROUND_LONGEST_SEQUENCES = 14;
    public final static int PLACE_XS_ROWS_AT_TOO_SHORT_EMPTY_SEQUENCES = 15;
    public final static int PLACE_XS_COLUMNS_AT_TOO_SHORT_EMPTY_SEQUENCES = 16;
    public final static int MARK_AVAILABLE_FIELDS_IN_ROWS = 17;
    public final static int MARK_AVAILABLE_FIELDS_IN_COLUMNS = 18;

    public final static ArrayList<Integer> actionsToDoAfterCorrectingRowsSequences = new ArrayList<>(
            Arrays. asList(
                    CORRECT_ROWS_SEQUENCES_RANGES_IF_X_ON_WAY,
                    COLOUR_OVERLAPPING_FIELDS_IN_ROWS,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROWS,
                    PLACE_XS_ROWS_AT_UNREACHABLE_FIELDS,
                    PLACE_XS_ROWS_AROUND_LONGEST_SEQUENCES,
                    PLACE_XS_ROWS_AT_TOO_SHORT_EMPTY_SEQUENCES,

                    MARK_AVAILABLE_FIELDS_IN_ROWS));

    public final static ArrayList<Integer> actionsToDoAfterCorrectingColumnsSequences = new ArrayList<>(
            Arrays. asList(
                    CORRECT_COLUMNS_SEQUENCES_RANGES_IF_X_ON_WAY,
                    COLOUR_OVERLAPPING_FIELDS_IN_COLUMNS,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMNS,
                    PLACE_XS_COLUMNS_AT_UNREACHABLE_FIELDS,
                    PLACE_XS_COLUMNS_AROUND_LONGEST_SEQUENCES,
                    PLACE_XS_COLUMNS_AT_TOO_SHORT_EMPTY_SEQUENCES,

                    MARK_AVAILABLE_FIELDS_IN_COLUMNS));

    public final static ArrayList<Integer> actionsToDoAfterCorrectingRowsSequencesWhenMetColouredField = new ArrayList<>(
            Arrays. asList(
                    CORRECT_ROWS_SEQUENCES_RANGES,
                    COLOUR_OVERLAPPING_FIELDS_IN_ROWS,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROWS,
                    PLACE_XS_ROWS_AT_UNREACHABLE_FIELDS,
                    PLACE_XS_ROWS_AROUND_LONGEST_SEQUENCES,
                    PLACE_XS_ROWS_AT_TOO_SHORT_EMPTY_SEQUENCES,

                    MARK_AVAILABLE_FIELDS_IN_ROWS));

    public final static ArrayList<Integer> actionsToDoAfterCorrectingColumnsSequencesWhenMetColouredField = new ArrayList<>(
            Arrays. asList(
                    CORRECT_COLUMNS_SEQUENCES_RANGES,
                    COLOUR_OVERLAPPING_FIELDS_IN_COLUMNS,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMNS,
                    PLACE_XS_COLUMNS_AT_UNREACHABLE_FIELDS,
                    PLACE_XS_COLUMNS_AROUND_LONGEST_SEQUENCES,
                    PLACE_XS_COLUMNS_AT_TOO_SHORT_EMPTY_SEQUENCES,

                    MARK_AVAILABLE_FIELDS_IN_COLUMNS));

    public final static ArrayList<Integer> actionsToDoAfterCorrectingRowsSequencesIfXOnWay = new ArrayList<>(
            Arrays. asList(
                    CORRECT_ROWS_SEQUENCES_RANGES,
                    COLOUR_OVERLAPPING_FIELDS_IN_ROWS,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROWS,
                    PLACE_XS_ROWS_AT_UNREACHABLE_FIELDS,
                    PLACE_XS_ROWS_AROUND_LONGEST_SEQUENCES,
                    PLACE_XS_ROWS_AT_TOO_SHORT_EMPTY_SEQUENCES,

                    MARK_AVAILABLE_FIELDS_IN_ROWS));

    public final static ArrayList<Integer> actionsToDoAfterCorrectingColumnsSequencesIfXOnWay = new ArrayList<>(
            Arrays. asList(
                    CORRECT_COLUMNS_SEQUENCES_RANGES,
                    COLOUR_OVERLAPPING_FIELDS_IN_COLUMNS,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMNS,
                    PLACE_XS_COLUMNS_AT_UNREACHABLE_FIELDS,
                    PLACE_XS_COLUMNS_AROUND_LONGEST_SEQUENCES,
                    PLACE_XS_COLUMNS_AT_TOO_SHORT_EMPTY_SEQUENCES,

                    MARK_AVAILABLE_FIELDS_IN_COLUMNS));

    public final static ArrayList<Integer> actionsToDoAfterColouringOverlappingSequencesInRows = new ArrayList<>(
            Arrays. asList(
                    CORRECT_COLUMNS_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMNS,
                    PLACE_XS_COLUMNS_AROUND_LONGEST_SEQUENCES,

                    MARK_AVAILABLE_FIELDS_IN_COLUMNS));

    public final static ArrayList<Integer> actionsToDoAfterColouringOverlappingSequencesInColumns = new ArrayList<>(
            Arrays. asList(
                    CORRECT_ROWS_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROWS,
                    PLACE_XS_ROWS_AROUND_LONGEST_SEQUENCES,

                    MARK_AVAILABLE_FIELDS_IN_ROWS));

    public final static ArrayList<Integer> actionsToDoAfterExtendingColouredFieldsNearXInRows = new ArrayList<>(
            Arrays. asList(
                    //CORRECT_COLUMNS_SEQUENCES_RANGES,+
                    CORRECT_COLUMNS_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                    //CORRECT_COLUMNS_SEQUENCES_RANGES_IF_X_ON_WAY,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMNS,
                    PLACE_XS_COLUMNS_AROUND_LONGEST_SEQUENCES,

                    MARK_AVAILABLE_FIELDS_IN_COLUMNS));

    public final static ArrayList<Integer> actionsToDoAfterExtendingColouredFieldsNearXInColumns = new ArrayList<>(
            Arrays. asList(
                    //CORRECT_ROWS_SEQUENCES_RANGES,+
                    CORRECT_ROWS_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                    //CORRECT_ROWS_SEQUENCES_RANGES_IF_X_ON_WAY,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROWS,
                    PLACE_XS_ROWS_AROUND_LONGEST_SEQUENCES,

                    MARK_AVAILABLE_FIELDS_IN_ROWS));

    public final static ArrayList<Integer> actionsToDoAfterPlacingXsAtRowsUnreachableFields = new ArrayList<>(
            Arrays. asList(
                    CORRECT_COLUMNS_SEQUENCES_RANGES_IF_X_ON_WAY,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMNS,
                    PLACE_XS_COLUMNS_AT_TOO_SHORT_EMPTY_SEQUENCES));

    public final static ArrayList<Integer> actionsToDoAfterPlacingXsAtColumnsUnreachableFields = new ArrayList<>(
            Arrays. asList(
                            CORRECT_ROWS_SEQUENCES_RANGES_IF_X_ON_WAY,
                            EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROWS,
                            PLACE_XS_ROWS_AT_TOO_SHORT_EMPTY_SEQUENCES));

    public final static ArrayList<Integer> actionsToDoAfterPlacingXsAroundLongestSequencesInRows = new ArrayList<>(
            Arrays. asList(
                    CORRECT_COLUMNS_SEQUENCES_RANGES_IF_X_ON_WAY,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMNS,
                    PLACE_XS_COLUMNS_AT_TOO_SHORT_EMPTY_SEQUENCES));

    public final static ArrayList<Integer> actionsToDoAfterPlacingXsAroundLongestSequencesInColumns = new ArrayList<>(
            Arrays. asList(
                    CORRECT_ROWS_SEQUENCES_RANGES_IF_X_ON_WAY,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROWS,
                    PLACE_XS_ROWS_AT_TOO_SHORT_EMPTY_SEQUENCES));

    public final static ArrayList<Integer> actionsToDoAfterPlacingXsAtTooShortEmptySequencesInRows = new ArrayList<>(
            Arrays.asList(
                    CORRECT_COLUMNS_SEQUENCES_RANGES_IF_X_ON_WAY,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMNS,
                    PLACE_XS_COLUMNS_AT_TOO_SHORT_EMPTY_SEQUENCES));

    public final static ArrayList<Integer> actionsToDoAfterPlacingXsAtTooShortSequencesInColumns = new ArrayList<>(
            Arrays.asList(
                    CORRECT_ROWS_SEQUENCES_RANGES_IF_X_ON_WAY,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROWS,
                    PLACE_XS_ROWS_AT_TOO_SHORT_EMPTY_SEQUENCES));

    public final static ArrayList<Integer> actionsToDoAfterMarkingSequencesInRows = new ArrayList<>(
            Arrays.asList(
                    CORRECT_ROWS_SEQUENCES_RANGES,
                    COLOUR_OVERLAPPING_FIELDS_IN_ROWS,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROWS,
                    PLACE_XS_ROWS_AT_UNREACHABLE_FIELDS,
                    PLACE_XS_ROWS_AROUND_LONGEST_SEQUENCES,
                    PLACE_XS_ROWS_AT_TOO_SHORT_EMPTY_SEQUENCES));

    public final static ArrayList<Integer> actionsToDoAfterMarkingSequencesInColumns = new ArrayList<>(
            Arrays. asList(
                    CORRECT_COLUMNS_SEQUENCES_RANGES,
                    COLOUR_OVERLAPPING_FIELDS_IN_COLUMNS,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMNS,
                    PLACE_XS_COLUMNS_AT_UNREACHABLE_FIELDS,
                    PLACE_XS_COLUMNS_AROUND_LONGEST_SEQUENCES,
                    PLACE_XS_COLUMNS_AT_TOO_SHORT_EMPTY_SEQUENCES));
}
