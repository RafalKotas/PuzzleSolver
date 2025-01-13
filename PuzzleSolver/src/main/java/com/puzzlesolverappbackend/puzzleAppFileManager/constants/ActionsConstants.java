package com.puzzlesolverappbackend.puzzleAppFileManager.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.constants.NonogramSolveAction.*;

public class ActionsConstants {

    public final static ArrayList<NonogramSolveAction> actionsToDoAfterCorrectingRowsSequencesRanges = new ArrayList<>(
            List.of(
                    CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY,
                    COLOUR_OVERLAPPING_FIELDS_IN_ROW,
                    PLACE_XS_ROW_AT_UNREACHABLE_FIELDS,
                    PLACE_XS_ROW_IF_O_WILL_CREATE_TOO_LONG_COLOURED_SEQUENCE
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoAfterCorrectingColumnsSequencesRanges = new ArrayList<>(
            List.of(
                    CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY,
                    COLOUR_OVERLAPPING_FIELDS_IN_COLUMN,
                    PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS,
                    PLACE_XS_COLUMN_IF_O_WILL_CREATE_TOO_LONG_COLOURED_SEQUENCE
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoAfterCorrectingRowsSequencesWhenMetColouredField = new ArrayList<>(
            Arrays.asList(
                    COLOUR_OVERLAPPING_FIELDS_IN_ROW,
                    PLACE_XS_ROW_AT_UNREACHABLE_FIELDS
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoAfterCorrectingColumnsSequencesWhenMetColouredField = new ArrayList<>(
            Arrays.asList(
                    COLOUR_OVERLAPPING_FIELDS_IN_COLUMN,
                    PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoAfterCorrectingRowsSequencesIfXOnWay = new ArrayList<>(
            List.of(
                    CORRECT_ROW_SEQUENCES_RANGES,
                    COLOUR_OVERLAPPING_FIELDS_IN_ROW,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                    PLACE_XS_ROW_AT_UNREACHABLE_FIELDS,
                    MARK_AVAILABLE_FIELDS_IN_ROW
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoAfterCorrectingColumnsSequencesIfXOnWay = new ArrayList<>(
            List.of(
                    CORRECT_COLUMN_SEQUENCES_RANGES,
                    COLOUR_OVERLAPPING_FIELDS_IN_COLUMN,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                    PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS,
                    MARK_AVAILABLE_FIELDS_IN_COLUMN
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoInColumnAfterColouringOverlappingSequencesInRows = new ArrayList<>(
            Arrays.asList(
                    CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                    PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES,
                    MARK_AVAILABLE_FIELDS_IN_COLUMN,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                    PLACE_XS_COLUMN_IF_O_WILL_CREATE_TOO_LONG_COLOURED_SEQUENCE
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoInRowAfterColouringOverlappingSequencesInRows = new ArrayList<>(
            Arrays.asList(
                    MARK_AVAILABLE_FIELDS_IN_ROW
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoInRowAfterColouringOverlappingSequencesInColumns = new ArrayList<>(
            Arrays.asList(
                    CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                    PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES,
                    MARK_AVAILABLE_FIELDS_IN_ROW,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                    PLACE_XS_ROW_IF_O_WILL_CREATE_TOO_LONG_COLOURED_SEQUENCE
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoInColumnAfterColouringOverlappingSequencesInColumns = new ArrayList<>(
            Arrays.asList(
                    MARK_AVAILABLE_FIELDS_IN_COLUMN
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoInRowAfterExtendingColouredFieldsNearXInRows = new ArrayList<>(
            List.of(
                    PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES,
                    MARK_AVAILABLE_FIELDS_IN_ROW
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoInColumnAfterExtendingColouredFieldsNearXInRows = new ArrayList<>(
            List.of(
                    CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                    PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES,
                    MARK_AVAILABLE_FIELDS_IN_COLUMN,
                    PLACE_XS_COLUMN_IF_O_WILL_CREATE_TOO_LONG_COLOURED_SEQUENCE
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoInColumnAfterExtendingColouredFieldsNearXInColumns = new ArrayList<>(
            List.of(
                    PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES,
                    MARK_AVAILABLE_FIELDS_IN_COLUMN
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoInRowAfterExtendingColouredFieldsNearXInColumns = new ArrayList<>(
            List.of(
                    CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                    PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES,
                    MARK_AVAILABLE_FIELDS_IN_ROW,
                    PLACE_XS_ROW_IF_O_WILL_CREATE_TOO_LONG_COLOURED_SEQUENCE
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoInRowAfterPlacingXsAtRowsUnreachableFields = new ArrayList<>(
            List.of(
                    CORRECT_ROW_SEQUENCES_RANGES,
                    PLACE_XS_ROW_AT_UNREACHABLE_FIELDS
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoInColumnAfterPlacingXsAtRowsUnreachableFields = new ArrayList<>(
            List.of(
                    CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                    PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoInColumnAfterPlacingXsAtColumnsUnreachableFields = new ArrayList<>(
            List.of(
                    CORRECT_COLUMN_SEQUENCES_RANGES,
                    PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoInRowAfterPlacingXsAtColumnsUnreachableFields = new ArrayList<>(
            List.of(
                    CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                    PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoInRowDoAfterPlacingXsAroundLongestSequencesInRows = new ArrayList<>(
            List.of(
                    CORRECT_ROW_SEQUENCES_RANGES, // TODO - check if can extract correcting range to another place (while only one possible coloured sequence matches)
                    PLACE_XS_ROW_AT_UNREACHABLE_FIELDS
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoInColumnAfterPlacingXsAroundLongestSequencesInRows = new ArrayList<>(
            List.of(
                    CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY,
                    PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoInColumnAfterPlacingXsAroundLongestSequencesInColumns = new ArrayList<>(
            List.of(
                    CORRECT_COLUMN_SEQUENCES_RANGES, // TODO - check if can extract correcting range to another place (while only one possible coloured sequence matches)
                    PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS

            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoInRowAfterPlacingXsAroundLongestSequencesInColumns = new ArrayList<>(
            List.of(
                    CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY,
                    PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES,
                    PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoInColumnAfterPlacingXsAtTooShortEmptySequencesInRows = new ArrayList<>(
            List.of(
                    CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY,
                    PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoInRowAfterPlacingXsAtTooShortSequencesInColumns = new ArrayList<>(
            List.of(
                    CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY,
                    PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoAfterColouringFieldInTrivialRow = new ArrayList<>(
            List.of(
                    CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                    MARK_AVAILABLE_FIELDS_IN_COLUMN
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoAfterColouringFieldInTrivialColumn = new ArrayList<>(
            List.of(
                    CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                    MARK_AVAILABLE_FIELDS_IN_ROW
            ));
    public final static ArrayList<NonogramSolveAction> actionsToDoAfterPlacingXInTrivialRow = new ArrayList<>(
            List.of(

                    CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY,
                    PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoAfterPlacingXInTrivialColumn = new ArrayList<>(
            List.of(
                    CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY,
                    PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoAfterCorrectingRangesWhenMarkingSequencesInRows = new ArrayList<>(
            List.of(
                    CORRECT_ROW_SEQUENCES_RANGES,
                    COLOUR_OVERLAPPING_FIELDS_IN_ROW,
                    PLACE_XS_ROW_AT_UNREACHABLE_FIELDS,
                    MARK_AVAILABLE_FIELDS_IN_ROW
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoAfterCorrectingRangesWhenMarkingSequencesInColumns = new ArrayList<>(
            List.of(
                    CORRECT_COLUMN_SEQUENCES_RANGES,
                    COLOUR_OVERLAPPING_FIELDS_IN_COLUMN,
                    PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS,
                    MARK_AVAILABLE_FIELDS_IN_COLUMN
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoInRowAfterPlacingXInRowIfColouringWillCreateTooLongSequence = new ArrayList<>(
            List.of(
                    CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY,
                    PLACE_XS_ROW_IF_O_WILL_CREATE_TOO_LONG_COLOURED_SEQUENCE
            )
    );

    public final static ArrayList<NonogramSolveAction> actionsToDoInColumnAfterPlacingXInRowIfColouringWillCreateTooLongSequence = new ArrayList<>(
            List.of(
                    CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                    PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES
            )
    );

    public final static ArrayList<NonogramSolveAction> actionsToDoInColumnAfterPlacingXInColumnIfColouringWillCreateTooLongSequence = new ArrayList<>(
            List.of(
                    CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY,
                    PLACE_XS_COLUMN_IF_O_WILL_CREATE_TOO_LONG_COLOURED_SEQUENCE
            )
    );

    public final static ArrayList<NonogramSolveAction> actionsToDoInRowAfterPlacingXInColumnIfColouringWillCreateTooLongSequence = new ArrayList<>(
            List.of(
                    CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                    PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES
            )
    );

    public final static ArrayList<NonogramSolveAction> actionsToDoInColumnAfterPlacingXsIfONearWillBeginTooLongPossibleColouredSequence = new ArrayList<>(
      List.of(
              CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY
      )
    );
}
