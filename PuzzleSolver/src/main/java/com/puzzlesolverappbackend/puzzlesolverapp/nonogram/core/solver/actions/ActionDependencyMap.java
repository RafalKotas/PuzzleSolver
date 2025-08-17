package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.actions;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;
import lombok.experimental.UtilityClass;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ActionDependencyMap {

    private static final Map<NonogramSolveAction, List<NonogramSolveAction>> actionDependencies = new EnumMap<>(NonogramSolveAction.class);

    public static List<NonogramSolveAction> getDependenciesFor(NonogramSolveAction action) {
        return actionDependencies.getOrDefault(action, List.of());
    }

    // TODO test completeness of actions dependencies
    static {
        actionDependencies.put(NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_ROW, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS_IN_ROW,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_ROW,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_ROW,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE_IN_ROW,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW,
                NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_ROW,
                NonogramSolveAction.PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_ROW,
                NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_COLUMN, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS_IN_COLUMN,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_COLUMN,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_COLUMN,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE_IN_COLUMN,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_COLUMN,
                NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS_IN_ROW, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_ROW,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_ROW,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE_IN_ROW,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW,
                NonogramSolveAction.COLOUR_FIELDS_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE_IN_ROW,
                NonogramSolveAction.COLOUR_FIELDS_IF_X_CAUSES_ASSIGNMENT_CONFLICT_IN_ROW,
                NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_ROW,
                NonogramSolveAction.PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES_IN_ROW,
                NonogramSolveAction.PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_ROW,
                NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS_IN_COLUMN, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_COLUMN,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_COLUMN,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE_IN_COLUMN,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN,
                NonogramSolveAction.COLOUR_FIELDS_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_COLUMN,
                NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_ROW, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_ROW,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_ROW,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_COLUMN, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_COLUMN,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_COLUMN,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_ROW, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_ROW,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW,
                NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_ROW,
                NonogramSolveAction.PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_ROW,
                NonogramSolveAction.PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_ROW,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW,
                NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_COLUMN, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_COLUMN,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_COLUMN,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN,
                NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE_IN_ROW, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_ROW,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_ROW,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE_IN_COLUMN, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_COLUMN,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_COLUMN,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW, List.of(
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW,

                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS_IN_COLUMN,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_COLUMN,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE_IN_COLUMN,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                NonogramSolveAction.COLOUR_FIELDS_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_COLUMN,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN, List.of(
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN,

                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS_IN_ROW,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_ROW,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE_IN_ROW,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                NonogramSolveAction.COLOUR_FIELDS_IF_X_CAUSES_ASSIGNMENT_CONFLICT_IN_ROW, // new
                NonogramSolveAction.COLOUR_FIELDS_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE_IN_ROW,
                NonogramSolveAction.PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_ROW,
                NonogramSolveAction.PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_ROW,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.COLOUR_FIELDS_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE_IN_ROW, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE_IN_ROW,

                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS_IN_COLUMN,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_COLUMN,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.COLOUR_FIELDS_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE_IN_COLUMN, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE_IN_COLUMN,

                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS_IN_ROW,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE_IN_ROW,
                NonogramSolveAction.PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_ROW,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW, List.of(
                NonogramSolveAction.PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_ROW,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW,

                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS_IN_COLUMN,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_COLUMN,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE_IN_COLUMN,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_COLUMN,
                NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_IN_COLUMN,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.COLOUR_FIELDS_IF_X_CAUSES_ASSIGNMENT_CONFLICT_IN_ROW, List.of(


                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_COLUMN,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN,

                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS_IN_ROW,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_ROW,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE_IN_ROW,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                NonogramSolveAction.PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_ROW,
                NonogramSolveAction.PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_ROW,
                NonogramSolveAction.PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_ROW,
                NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_IN_ROW,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_ROW, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_ROW,

                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_COLUMN,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_COLUMN,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                NonogramSolveAction.COLOUR_FIELDS_IF_X_CAUSES_ASSIGNMENT_CONFLICT_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_COLUMN, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_COLUMN,

                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_ROW,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_ROW,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                NonogramSolveAction.PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES_IN_ROW,
                NonogramSolveAction.PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_ROW, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_ROW,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_ROW,

                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_COLUMN,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_COLUMN,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_COLUMN, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_COLUMN,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_COLUMN,

                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_ROW,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_ROW,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                NonogramSolveAction.PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES_IN_ROW,
                NonogramSolveAction.PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES_IN_ROW, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES_IN_COLUMN, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_ROW,
                NonogramSolveAction.PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_ROW, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_ROW,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_ROW,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,

                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_COLUMN,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_COLUMN,
                NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_COLUMN, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_COLUMN,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_COLUMN,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,

                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_ROW,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                NonogramSolveAction.PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES_IN_ROW,
                NonogramSolveAction.PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_ROW,
                NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_ROW, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_COLUMN, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_ROW,
                NonogramSolveAction.PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART_IN_ROW, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS_IN_COLUMN,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART_IN_COLUMN, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS_IN_ROW,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART_IN_ROW, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART_IN_COLUMN, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART_IN_ROW, List.of(
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART_IN_COLUMN, List.of(
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_ROW,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW,
                NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_COLUMN,
                NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.COLOUR_FIELD_IN_TRIVIAL_ROW, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS_IN_COLUMN,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.COLOUR_FIELD_IN_TRIVIAL_COLUMN, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS_IN_ROW,
                NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW
        ));

        actionDependencies.put(NonogramSolveAction.PLACING_X_IN_TRIVIAL_ROW, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_COLUMN,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_COLUMN,
                NonogramSolveAction.PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES_IN_COLUMN
        ));

        actionDependencies.put(NonogramSolveAction.PLACING_X_IN_TRIVIAL_COLUMN, List.of(
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_ROW,
                NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_ROW,
                NonogramSolveAction.PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES_IN_ROW
        ));
    }
}
