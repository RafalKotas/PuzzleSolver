package com.puzzlesolverappbackend.puzzleAppFileManager.payload;

import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.NonogramSolveAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.NonogramSolveAction.*;

public class ActionsConstants {

    public final static ArrayList<NonogramSolveAction> actionsToDoAfterCorrectingRowsSequences = new ArrayList<>(
            List.of(
                    CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY,
                    COLOUR_OVERLAPPING_FIELDS_IN_ROW,
                    PLACE_XS_ROW_AT_UNREACHABLE_FIELDS
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoAfterCorrectingColumnsSequences = new ArrayList<>(
            List.of(
                    CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY,
                    COLOUR_OVERLAPPING_FIELDS_IN_COLUMN,
                    PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS
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
                    PLACE_XS_ROW_AT_UNREACHABLE_FIELDS,
                    MARK_AVAILABLE_FIELDS_IN_ROW
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoAfterCorrectingColumnsSequencesIfXOnWay = new ArrayList<>(
            List.of(
                    CORRECT_COLUMN_SEQUENCES_RANGES,
                    COLOUR_OVERLAPPING_FIELDS_IN_COLUMN,
                    PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS,
                    MARK_AVAILABLE_FIELDS_IN_COLUMN
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoInColumnAfterColouringOverlappingSequencesInRows = new ArrayList<>(
            Arrays.asList(
                    CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                    PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES,
                    MARK_AVAILABLE_FIELDS_IN_COLUMN,
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN
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
                    EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoInColumnAfterColouringOverlappingSequencesInColumns = new ArrayList<>(
            Arrays.asList(
                    MARK_AVAILABLE_FIELDS_IN_COLUMN
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoAfterExtendingColouredFieldsNearXInRows = new ArrayList<>(
            List.of(
                    MARK_AVAILABLE_FIELDS_IN_COLUMN
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoAfterExtendingColouredFieldsNearXInColumns = new ArrayList<>(
            List.of(
                    MARK_AVAILABLE_FIELDS_IN_ROW
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoAfterPlacingXsAtRowsUnreachableFields = new ArrayList<>(
            List.of(
                    CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY,
                    PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS //+
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoAfterPlacingXsAtColumnsUnreachableFields = new ArrayList<>(
            List.of(
                    CORRECT_COLUMN_SEQUENCES_RANGES,
                    CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY,
                    PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS,
                    PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES //+
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoAfterPlacingXsAroundLongestSequencesInRows = new ArrayList<>(
            List.of(
                    CORRECT_ROW_SEQUENCES_RANGES, // TODO - check if can extract correcting range to another place (while only one possible coloured sequence matches)
                    CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY,
                    PLACE_XS_ROW_AT_UNREACHABLE_FIELDS,
                    PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES
            ));


    public final static ArrayList<NonogramSolveAction> actionsToDoInRowAfterPlacingXsAroundLongestSequencesInColumns = new ArrayList<>(
            List.of(
                    CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY,
                    PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES
            ));


    public final static ArrayList<NonogramSolveAction> actionsToDoInColumnAfterPlacingXsAroundLongestSequencesInColumns = new ArrayList<>(
            List.of(
                    CORRECT_COLUMN_SEQUENCES_RANGES, // TODO - check if can extract correcting range to another place (while only one possible coloured sequence matches)
                    PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoAfterPlacingXsAtTooShortEmptySequencesInRows = new ArrayList<>(
            List.of(
                    CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoAfterPlacingXsAtTooShortSequencesInColumns = new ArrayList<>(
            List.of(
                    CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY
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
                    PLACE_XS_ROW_AT_UNREACHABLE_FIELDS
            ));

    public final static ArrayList<NonogramSolveAction> actionsToDoAfterCorrectingRangesWhenMarkingSequencesInColumns = new ArrayList<>(
            List.of(
                    CORRECT_COLUMN_SEQUENCES_RANGES,
                    COLOUR_OVERLAPPING_FIELDS_IN_COLUMN,
                    PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS
            ));
}
