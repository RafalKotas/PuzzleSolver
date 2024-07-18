package com.puzzlesolverappbackend.puzzleAppFileManager.payload;

import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.ActionEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.ActionEnum.*;

public class ActionsConstants {

    public final static ArrayList<ActionEnum> actionsToDoAfterCorrectingRowsSequences = new ArrayList<>(
            List.of(
                    CORRECT_ROWS_SEQUENCES_RANGES_IF_X_ON_WAY,
                    COLOUR_OVERLAPPING_FIELDS_IN_ROWS,
                    PLACE_XS_ROWS_AT_UNREACHABLE_FIELDS
            ));

    public final static ArrayList<ActionEnum> actionsToDoAfterCorrectingColumnsSequences = new ArrayList<>(
            List.of(
                    CORRECT_COLUMNS_SEQUENCES_RANGES_IF_X_ON_WAY,
                    COLOUR_OVERLAPPING_FIELDS_IN_COLUMNS,
                    PLACE_XS_COLUMNS_AT_UNREACHABLE_FIELDS
            ));

    public final static ArrayList<ActionEnum> actionsToDoAfterCorrectingRowsSequencesWhenMetColouredField = new ArrayList<>(
            Arrays.asList(
                    COLOUR_OVERLAPPING_FIELDS_IN_ROWS,
                    PLACE_XS_ROWS_AT_UNREACHABLE_FIELDS
            ));

    public final static ArrayList<ActionEnum> actionsToDoAfterCorrectingColumnsSequencesWhenMetColouredField = new ArrayList<>(
            Arrays.asList(
                    COLOUR_OVERLAPPING_FIELDS_IN_COLUMNS,
                    PLACE_XS_COLUMNS_AT_UNREACHABLE_FIELDS
            ));

    public final static ArrayList<ActionEnum> actionsToDoAfterCorrectingRowsSequencesIfXOnWay = new ArrayList<>(
            List.of(
                    CORRECT_ROWS_SEQUENCES_RANGES,
                    COLOUR_OVERLAPPING_FIELDS_IN_ROWS,
                    PLACE_XS_ROWS_AT_UNREACHABLE_FIELDS,
                    MARK_AVAILABLE_FIELDS_IN_ROWS
            ));

    public final static ArrayList<ActionEnum> actionsToDoAfterCorrectingColumnsSequencesIfXOnWay = new ArrayList<>(
            List.of(
                    CORRECT_COLUMNS_SEQUENCES_RANGES,
                    COLOUR_OVERLAPPING_FIELDS_IN_COLUMNS,
                    PLACE_XS_COLUMNS_AT_UNREACHABLE_FIELDS,
                    MARK_AVAILABLE_FIELDS_IN_COLUMNS
            ));

    public final static ArrayList<ActionEnum> actionsToDoAfterColouringOverlappingSequencesInRows = new ArrayList<>(
            Arrays.asList(
                    CORRECT_COLUMNS_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                    PLACE_XS_COLUMNS_AROUND_LONGEST_SEQUENCES,
                    MARK_AVAILABLE_FIELDS_IN_COLUMNS
            ));

    public final static ArrayList<ActionEnum> actionsToDoAfterColouringOverlappingSequencesInColumns = new ArrayList<>(
            Arrays.asList(
                    CORRECT_ROWS_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                    PLACE_XS_ROWS_AROUND_LONGEST_SEQUENCES,
                    MARK_AVAILABLE_FIELDS_IN_ROWS
            ));

    public final static ArrayList<ActionEnum> actionsToDoAfterExtendingColouredFieldsNearXInRows = new ArrayList<>(
            List.of());

    public final static ArrayList<ActionEnum> actionsToDoAfterExtendingColouredFieldsNearXInColumns = new ArrayList<>(
            List.of());

    public final static ArrayList<ActionEnum> actionsToDoAfterPlacingXsAtRowsUnreachableFields = new ArrayList<>(
            List.of(
                    CORRECT_COLUMNS_SEQUENCES_RANGES_IF_X_ON_WAY,
                    PLACE_XS_COLUMNS_AT_UNREACHABLE_FIELDS //+
            ));

    public final static ArrayList<ActionEnum> actionsToDoAfterPlacingXsAtColumnsUnreachableFields = new ArrayList<>(
            List.of(
                    CORRECT_COLUMNS_SEQUENCES_RANGES,
                    CORRECT_ROWS_SEQUENCES_RANGES_IF_X_ON_WAY,
                    PLACE_XS_COLUMNS_AT_UNREACHABLE_FIELDS,
                    PLACE_XS_ROWS_AT_TOO_SHORT_EMPTY_SEQUENCES //+
            ));

    public final static ArrayList<ActionEnum> actionsToDoAfterPlacingXsAroundLongestSequencesInRows = new ArrayList<>(
            List.of(
                    CORRECT_ROWS_SEQUENCES_RANGES, // TODO - check if can extract correcting range to another place (while only one possible coloured sequence matches)
                    CORRECT_COLUMNS_SEQUENCES_RANGES_IF_X_ON_WAY,
                    PLACE_XS_ROWS_AT_UNREACHABLE_FIELDS,
                    PLACE_XS_COLUMNS_AT_TOO_SHORT_EMPTY_SEQUENCES
            ));

    public final static ArrayList<ActionEnum> actionsToDoAfterPlacingXsAroundLongestSequencesInColumns = new ArrayList<>(
            List.of(
                    CORRECT_COLUMNS_SEQUENCES_RANGES, // TODO - check if can extract correcting range to another place (while only one possible coloured sequence matches)
                    CORRECT_ROWS_SEQUENCES_RANGES_IF_X_ON_WAY,
                    PLACE_XS_COLUMNS_AT_UNREACHABLE_FIELDS,
                    PLACE_XS_ROWS_AT_TOO_SHORT_EMPTY_SEQUENCES
            ));

    public final static ArrayList<ActionEnum> actionsToDoAfterPlacingXsAtTooShortEmptySequencesInRows = new ArrayList<>(
            List.of(
                    CORRECT_COLUMNS_SEQUENCES_RANGES_IF_X_ON_WAY
            ));

    public final static ArrayList<ActionEnum> actionsToDoAfterPlacingXsAtTooShortSequencesInColumns = new ArrayList<>(
            List.of(
                    CORRECT_ROWS_SEQUENCES_RANGES_IF_X_ON_WAY
            ));

    public final static ArrayList<ActionEnum> actionsToDoAfterColouringFieldInTrivialRow = new ArrayList<>(
            List.of(
                    CORRECT_COLUMNS_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMNS
            ));

    public final static ArrayList<ActionEnum> actionsToDoAfterColouringFieldInTrivialColumn = new ArrayList<>(
            List.of(
                    CORRECT_ROWS_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROWS
            ));
    public final static ArrayList<ActionEnum> actionsToDoAfterPlacingXInTrivialRow = new ArrayList<>(
            List.of(

                    CORRECT_COLUMNS_SEQUENCES_RANGES_IF_X_ON_WAY,
                    PLACE_XS_COLUMNS_AT_TOO_SHORT_EMPTY_SEQUENCES
            ));

    public final static ArrayList<ActionEnum> actionsToDoAfterPlacingXInTrivialColumn = new ArrayList<>(
            List.of(
                    CORRECT_ROWS_SEQUENCES_RANGES_IF_X_ON_WAY,
                    PLACE_XS_ROWS_AT_TOO_SHORT_EMPTY_SEQUENCES
            ));

    public final static ArrayList<ActionEnum> actionsToDoAfterCorrectingRangesWhenMarkingSequencesInRows = new ArrayList<>(
            List.of(
                    COLOUR_OVERLAPPING_FIELDS_IN_ROWS,
                    PLACE_XS_ROWS_AT_UNREACHABLE_FIELDS
            ));

    public final static ArrayList<ActionEnum> actionsToDoAfterCorrectingRangesWhenMarkingSequencesInColumns = new ArrayList<>(
            List.of(
                    COLOUR_OVERLAPPING_FIELDS_IN_COLUMNS,
                    PLACE_XS_COLUMNS_AT_UNREACHABLE_FIELDS
            ));
}
