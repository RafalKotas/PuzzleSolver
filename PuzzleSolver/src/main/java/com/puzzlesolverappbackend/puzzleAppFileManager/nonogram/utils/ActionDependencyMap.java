package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.enums.NonogramSolveAction;

import java.util.EnumMap;
import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.enums.NonogramSolveAction.*;

public class ActionDependencyMap {
    public static final EnumMap<NonogramSolveAction, List<NonogramSolveAction>> actionDependencies = new EnumMap<>(NonogramSolveAction.class);

    static class PreventingExcessLength {

        // ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART
        public static final List<NonogramSolveAction> actionsToDoInColumnDuringColouringPart = List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS
        );

        // COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART
        public static final List<NonogramSolveAction> actionsToDoInRowDuringColouringPart = List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS
        );

        // ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART
        public static final List<NonogramSolveAction> actionsToDoInColumnDuringPlacingXPart = List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY
        );

        // COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART
        public static final List<NonogramSolveAction> actionsToDoInRowDuringPlacingXPart = List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY
        );

        // ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART
        public static final List<NonogramSolveAction> actionsToDoInRowAfterCorrectingOnlyMatchingSequenceRangePart = List.of();

        // COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART
        public static final List<NonogramSolveAction> actionsToDoInColumnAfterCorrectingOnlyMatchingSequenceRangePart = List.of();
    }

    // TODO test completeness of actions dependencies
    static {
        // 30 -> 28 o11684 o07811
        actionDependencies.put(NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, List.of(
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES no improvement,
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // improvement o11684 -> test
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // improvement o07811 -> test
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, //no improvement,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // base
                //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW no improvement,
                NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // base
                //NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES no improvement,
                //NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES no improvement,
                NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // base
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE no improvement,
                NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW no improvement
        ));

        // 28 -> 27 o06011
        actionDependencies.put(NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, List.of(
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES no improvement,
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // improvement_row
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // improvement_row
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // improvement o06011 -> test
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // base
                //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN no improvement,
                NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, //base
                //NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES no improvement,
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES no improvement,
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, //base
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE no improvement,
                NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH //no improvement,
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN no improvement
        ));

        // 27 -> 25 o07956 o07584
        actionDependencies.put(NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // check-
                // NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // check-
                // NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // check-
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // improvement o07956  with CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE -> test
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // improvement o07956 with CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES  -> test
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // base
                NonogramSolveAction.COLOUR_FIELDS_IN_ROW_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE,
                //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // check-
                NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // base
                //NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, // check-
                NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES, // improvement o07584
                NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // base
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // check-
                NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH // check-
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW // check-
        ));

        // 25 -> 22 o10935 o07632 o05614
        actionDependencies.put(NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // improvement o10935
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // no improvement
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // base
                NonogramSolveAction.COLOUR_FIELDS_IN_COLUMN_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE,
                //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // no improvement
                NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, // base
                //NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, // no improvement
                NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES, // improvement o07632
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // base
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE // no improvement (25 with only this added)
                NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH // improvement o05614
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN // no improvement (25 with only this added)
        ));

        // 22 -> 22
        actionDependencies.put(NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // base
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // base
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // base
                NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // base
                //NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW // base
        ));


        // 22 -> 22
        actionDependencies.put(NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // base
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // base
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // base
                NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, // base
                //NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN // base
//                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES,
//                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN,
//                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
//                NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS,
//                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN
        ));

        // 22 -> 22
        actionDependencies.put(NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // base
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // base
                //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // no improvement
                NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // base
                //NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW // base
        ));

        // 22 -> 22
        actionDependencies.put(NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // base
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // base
                //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // no improvement
                NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, // base
                //NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN // base
        ));

        // 22 -> 21 o11686
        actionDependencies.put(NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // improvement o11686
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW // base
                //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE // no improvement
                //NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH // no improvement
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW // no improvement
        ));

        // 21 -> 20 o10173
        actionDependencies.put(NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // improvement o10173
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN // base
                //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE // no improvement
                //NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH // no improvement
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN // no improvement
        ));

        // 20 -> 20
        actionDependencies.put(NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, List.of(
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // no improvement
                //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW, // base

                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // no improvement
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // base
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // no improvement
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // base
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // base
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // no improvement
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // base
                NonogramSolveAction.COLOUR_FIELDS_IN_COLUMN_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE,
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, // no improvement
                NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, // base
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // base
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN // base
        ));

        // 20 -> 20
        actionDependencies.put(NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, List.of(
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // no improvement
                //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN, // base

                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // no improvement
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // base
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // no improvement
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // base
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // base
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // no improvement
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // base
                NonogramSolveAction.COLOUR_FIELDS_IN_ROW_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE,
                //NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // no improvement
                NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, // base
                //NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
                NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // base
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW // base
        ));

        actionDependencies.put(COLOUR_FIELDS_IN_ROW_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE,

                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN
        ));

        actionDependencies.put(COLOUR_FIELDS_IN_COLUMN_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE,

                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW
        ));

        // 20 -> 20
        actionDependencies.put(NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, List.of(
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // no improvement
                //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // no improvement
                NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, // base
                //NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW, // base

                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // no improvement
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // base
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // no improvement
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // base
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // base
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // no improvement
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // base
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, // no improvement
                NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, // base
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // base
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // base
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN // base
        ));

        // 20 -> 19 o08542
        actionDependencies.put(NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, List.of(
//                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // no improvement
//                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
//                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // no improvement
//                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
//                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
//                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // no improvement
//                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // no improvement
//                NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, // no improvement
                NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, // base
//                NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
//                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
//                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
//                NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN, // base

                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // no improvement
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // base
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // no improvement
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // base
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // base
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // no improvement
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // base
                //NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // no improvement
                NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, // base
                //NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
                NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // base
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // o08542
                NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // base
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW // base
        ));

        // - after check all again
        // 21 -> 20  o11684
        actionDependencies.put(NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // base
//                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
//                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // no improvement
//                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
//                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
//                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // no improvement
//                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // no improvement
                NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // base
//                NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, // no improvement
//                NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
//                NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
//                NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
//                NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
//                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW, // no improvement

//                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES,
//                NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS,

                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // base
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // // improvement o11684
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // base
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, // no improvement
                NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES, // base
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE // base
                //NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH // no improvement
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN // no improvement
        ));

        // 20 -> 20
        actionDependencies.put(NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // base
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // no improvement
                //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // no improvement
                NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, // base
                //NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN, // no improvement

                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // base
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // no improvement
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // base
                //NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, // no improvement
                NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES, // base
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE // base
                //NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW // no improvement
        ));

        // 20 -> 20
        actionDependencies.put(NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // base
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // no improvement
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // base
                NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // base
                //NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW, // no improvement

                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // base
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // no improvement
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // base
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, // no improvement
                NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES, // base
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE // base
                //NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN // no improvement
        ));

        // 20 -> 20
        actionDependencies.put(NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, List.of(
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // base
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // no improvement
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // base
                NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, // base
                //NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN, // no improvement

                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // base
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // no improvement
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // base
                //NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, // no improvement
                NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES, // base
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE // base
                //NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW // no improvement
        ));

        // 20 -> 20
        actionDependencies.put(NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES, List.of(
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // no improvement
                //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, // no improvement
                NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES // base
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN // no improvement
        ));

        // 20 -> 20
        actionDependencies.put(NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES, List.of(
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // no improvement
                //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, // no improvement
                NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES // base
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW // no improvement
        ));

        // 20 -> 20
        actionDependencies.put(NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, List.of(
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // base
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // no improvement
                //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
                NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // base
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW, // no improvement

                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // no improvement
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // base
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, // no improvement
                NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES, // base
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // base
                NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH // no improvement
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN // no improvement
        ));

        // 20 -> 20
        actionDependencies.put(NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, List.of(
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // base
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // no improvement
                //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
                NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // base
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN, // no improvement

                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // no improvement
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // base
                //NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, // no improvement
                NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES, // base
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // base
                NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH // no improvement
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW // no improvement
        ));

        // 20 -> 20
        actionDependencies.put(NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, List.of(
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // no improvement
                //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, // no improvement
                NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES // base
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN // no improvement
        ));

        // 20 -> 20
        actionDependencies.put(NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, List.of(
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // no improvement
                //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, // no improvement
                NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES // base
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW // no improvement
        ));

        // 20 -> 20
        actionDependencies.put(NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART, List.of(
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // no improvement
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // base
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // no improvement
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN // no improvement
        ));

        // 20 -> 20
        actionDependencies.put(NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART, List.of(
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // no improvement
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // base
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // no improvement
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW
                //NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW // no improvement
        ));

        // 20 -> 20
        actionDependencies.put(NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART, List.of(
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY // base
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // no improvement
                //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN // no improvement
        ));

        // 20 -> 20
        actionDependencies.put(NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART, List.of(
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES,  // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY // base
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // no improvement
                //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW // no improvement
        ));

        // 20 -> 20
        actionDependencies.put(NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART, List.of(
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // no improvement
                //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // check
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW // no improvement
        ));

        // 20 -> 20
        actionDependencies.put(NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART, List.of(
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // no improvement
                //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // check
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN // no improvement
        ));

        // 20 -> 20
        actionDependencies.put(NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // base
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // base
                //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // no improvement
                NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // base
                //NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW // base
        ));

        actionDependencies.put(NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN, List.of(
               NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // base
               //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
               //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // no improvement
               //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
               //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
               NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // base
               //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // no improvement
               NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, // base
               //NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, // no improvement
               //NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
               //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
               //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
               //NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
               NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN // base
        ));

        actionDependencies.put(COLOUR_FIELD_IN_TRIVIAL_ROW, List.of(
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // no improvement
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // base
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // no improvement
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // base
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN // base
        ));

        actionDependencies.put(COLOUR_FIELD_IN_TRIVIAL_COLUMN, List.of(
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // no improvement
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // base
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // no improvement
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // base
                //NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW // base
        ));

        actionDependencies.put(PLACING_X_IN_TRIVIAL_ROW, List.of(
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, // no improvement
                //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES, // no improvement
                NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES // base
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN // no improvement
        ));

        actionDependencies.put(PLACING_X_IN_TRIVIAL_COLUMN, List.of(
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS, // no improvement
                NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY, // base
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES, // no improvement
                //NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE, // no improvement
                //NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, // no improvement
                //NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES, // no improvement
                NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES // base
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE, // no improvement
                //NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH, // no improvement
                //NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW // no improvement
        ));
    }
}
