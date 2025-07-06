package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.actions;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;

import java.util.EnumMap;
import java.util.List;

public class ActionDependencyMap {
    public static final EnumMap<NonogramSolveAction, List<NonogramSolveAction>> actionDependencies = new EnumMap<>(NonogramSolveAction.class);

    // TODO test completeness of actions dependencies
    static {
        actionDependencies.put(NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY,
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES,
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW,
                NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS,
                NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE,
                NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY,
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES,
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS,
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE,
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE,
                NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES,
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES,
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW,
                NonogramSolveAction.COLOUR_FIELDS_IN_ROW_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE,
                NonogramSolveAction.COLOUR_FIELDS_IN_ROW_IF_X_CAUSES_ASSIGNMENT_CONFLICT,
                NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS,
                NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES,
                NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE,
                NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES,
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES,
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN,
                NonogramSolveAction.COLOUR_FIELDS_IN_COLUMN_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE,
                NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS,
                NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES,
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE,
                NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW,
                NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS,
                NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES,
                NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW,
                NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS,
                NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES,
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN,
                NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES,
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES,
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, List.of(
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW,

                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES,
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                NonogramSolveAction.COLOUR_FIELDS_IN_COLUMN_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE,
                NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES,
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, List.of(
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN,

                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES,
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                NonogramSolveAction.COLOUR_FIELDS_IN_ROW_IF_X_CAUSES_ASSIGNMENT_CONFLICT, // new
                NonogramSolveAction.COLOUR_FIELDS_IN_ROW_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE,
                NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES,
                NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.COLOUR_FIELDS_IN_ROW_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE,

                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE,
                NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.COLOUR_FIELDS_IN_COLUMN_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE,

                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE,
                NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, List.of(
                NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW,

                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES,
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES,
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE,
                NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.COLOUR_FIELDS_IN_ROW_IF_X_CAUSES_ASSIGNMENT_CONFLICT, List.of(


                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES,
                NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN,

                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES,
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES,
                NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE,
                NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE,
                NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES,

                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY,
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                NonogramSolveAction.COLOUR_FIELDS_IN_COLUMN_IF_X_CAUSES_ASSIGNMENT_CONFLICT,
                NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES,
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES,

                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY,
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES,
                NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS,

                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY,
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES,
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS,

                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY,
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES,
                NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY,
                NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY,
                NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY,
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,

                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES,
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE,
                NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY,
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,

                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES,
                NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE,
                NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY,
                NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY,
                NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES
        ));

        actionDependencies.put(NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY
        ));

        actionDependencies.put(NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY
        ));

        actionDependencies.put(NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART, List.of(
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART, List.of(
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW,
                NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS
        ));

        actionDependencies.put(NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN, List.of(
               NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES,
               NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN,
               NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS
        ));

        actionDependencies.put(NonogramSolveAction.COLOUR_FIELD_IN_TRIVIAL_ROW, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.COLOUR_FIELD_IN_TRIVIAL_COLUMN, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.PLACING_X_IN_TRIVIAL_ROW, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES,
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY,
                NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES
        ));

        actionDependencies.put(NonogramSolveAction.PLACING_X_IN_TRIVIAL_COLUMN, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES,
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY,
                NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES
        ));
    }
}
