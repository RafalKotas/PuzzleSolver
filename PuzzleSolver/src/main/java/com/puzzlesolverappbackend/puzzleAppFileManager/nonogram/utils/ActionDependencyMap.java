package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.enums.NonogramSolveAction;

import java.util.EnumMap;
import java.util.List;

public class ActionDependencyMap {
    public static final EnumMap<NonogramSolveAction, List<NonogramSolveAction>> actionDependencies = new EnumMap<>(NonogramSolveAction.class);

    // TODO test completeness of actions dependencies
    static {
        actionDependencies.put(NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // improvement o11684 -> test
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // improvement o07811 -> test
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, //no improvement,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // base
                NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // base
                NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // base
                NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // improvement_row
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // improvement_row
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // improvement o06011 -> test
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // base
                NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, //base
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, //base
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE,
                NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH //no improvement,
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // check-
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // improvement o07956  with CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE -> test
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // improvement o07956 with CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES  -> test
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // base
                NonogramSolveAction.COLOUR_FIELDS_IN_ROW_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE,
                NonogramSolveAction.COLOUR_FIELDS_IN_ROW_IF_X_CAUSES_ASSIGNMENT_CONFLICT, // TODO - NEW!! check for o08331
                NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // base
                NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES, // improvement o07584
                NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // base
                NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH // check-
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // improvement o10935
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // base
                NonogramSolveAction.COLOUR_FIELDS_IN_COLUMN_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE,
                NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, // base
                NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES, // improvement o07632
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // base
                NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH // improvement o05614
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // base
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // base
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // base
                NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // base
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW // base
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // base
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // base
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // base
                NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, // base
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN // base
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // base
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // base
                NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // base
                NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, // base
                NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW, // base
                NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // base
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // base
                NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, // base
                NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, // base (new) i bez tego
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN, // base
                NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // improvement o11686
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW // base
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // improvement o10173
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN // base
        ));

        actionDependencies.put(NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, List.of(
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW, // base

                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // base
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // base
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // base
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // base
                NonogramSolveAction.COLOUR_FIELDS_IN_COLUMN_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE,
                NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, // base
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // base
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN // base
        ));

        actionDependencies.put(NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, List.of(
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN, // base

                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // base
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // base
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // base
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // base
                NonogramSolveAction.COLOUR_FIELDS_IN_ROW_IF_X_CAUSES_ASSIGNMENT_CONFLICT, // new
                NonogramSolveAction.COLOUR_FIELDS_IN_ROW_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE,
                NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, // base
                NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // base
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW // base
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
                NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, // base
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW, // base

                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // base
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // base
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // base
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // base
                NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, // base
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // base
                NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // base
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN // base
        ));

        actionDependencies.put(NonogramSolveAction.COLOUR_FIELDS_IN_ROW_IF_X_CAUSES_ASSIGNMENT_CONFLICT, List.of(


                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES,
                NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, // base
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN, // base

                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // base
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // base
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // base
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // base
                NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, // base
                NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // base
                NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // o08542
                NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // base
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW // base
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // base

                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // base
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // // improvement o11684
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // base
                NonogramSolveAction.COLOUR_FIELDS_IN_COLUMN_IF_X_CAUSES_ASSIGNMENT_CONFLICT, // TODO - NEW!!! columnIdx == 4 check o11612
                NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES, // base
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE // base
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // base

                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // base
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // no improvement
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // base
                NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES, // base
                NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE // base
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // base
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // base
                NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // base

                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // base
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // base
                NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES, // base
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE // base
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // base
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // base
                NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, // base

                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // base
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // base
                NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES, // base
                NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE // base
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES // base
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES // base
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // base
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,

                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // base
                NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES, // base
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // base
                NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH // no improvement
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // base
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,

                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // base
                NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES, // base
                NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // base
                NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH // no improvement
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES // base
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES // base
        ));

        actionDependencies.put(NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // base
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // base
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY // base
        ));

        actionDependencies.put(NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY // base
        ));

        actionDependencies.put(NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART, List.of(
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART, List.of(
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // base
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // base
                NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS // base
        ));

        actionDependencies.put(NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN, List.of(
               NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // base
               NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // base
               NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS // base
        ));

        actionDependencies.put(NonogramSolveAction.COLOUR_FIELD_IN_TRIVIAL_ROW, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // base
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // base
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN // base
        ));

        actionDependencies.put(NonogramSolveAction.COLOUR_FIELD_IN_TRIVIAL_COLUMN, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // base
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // base
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW // base
        ));

        actionDependencies.put(NonogramSolveAction.PLACING_X_IN_TRIVIAL_ROW, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES // base
        ));

        actionDependencies.put(NonogramSolveAction.PLACING_X_IN_TRIVIAL_COLUMN, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // no improvement
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES // base
        ));
    }
}
