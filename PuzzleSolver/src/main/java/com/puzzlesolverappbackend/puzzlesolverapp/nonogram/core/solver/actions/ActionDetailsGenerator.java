package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.actions;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramActionDetails;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ActionDetailsGenerator {

    static List<NonogramActionDetails> generateAllPossibleSingleActionDetails(int height, int width) {
        List<NonogramActionDetails> result = new ArrayList<>();
        int index = 0;

        List<NonogramActionDetails> rowActions = generateRowActionDetails(height);
        for (NonogramActionDetails action : rowActions) {
            action.setIndex(index++);
            result.add(action);
        }

        List<NonogramActionDetails> columnActions = generateColumnActionDetails(width);
        for (NonogramActionDetails action : columnActions) {
            action.setIndex(index++);
            result.add(action);
        }

        return result;
    }

    static List<NonogramActionDetails> generateRowActionDetails(int height) {
        List<NonogramActionDetails> result = new ArrayList<>();
        for (int rowIdx = 0; rowIdx < height; rowIdx++) {
            for (NonogramSolveAction action : List.of(
                    NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_ROW,
                    NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS_IN_ROW,
                    NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_ROW,
                    NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_ROW,
                    NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE_IN_ROW,
                    NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW,
                    NonogramSolveAction.COLOUR_FIELDS_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE_IN_ROW,
                    NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                    NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_ROW,
                    NonogramSolveAction.PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_ROW,
                    NonogramSolveAction.PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES_IN_ROW,
                    NonogramSolveAction.PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_ROW,
                    NonogramSolveAction.PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_ROW,
                    NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_IN_ROW,
                    NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW
            )) {
                NonogramActionDetails detail = new NonogramActionDetails();
                detail.setActionName(action);
                detail.setTriggeringActionName(null);
                detail.setChangedState(false);
                result.add(detail);
            }
        }
        return result;
    }

    static List<NonogramActionDetails> generateColumnActionDetails(int width) {
        List<NonogramActionDetails> result = new ArrayList<>();
        for (int colIdx = 0; colIdx < width; colIdx++) {
            for (NonogramSolveAction action : List.of(
                    NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IN_COLUMN,
                    NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS_IN_COLUMN,
                    NonogramSolveAction.CORRECT_SEQUENCES_RANGES_IF_X_ON_WAY_IN_COLUMN,
                    NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES_IN_COLUMN,
                    NonogramSolveAction.CORRECT_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE_IN_COLUMN,
                    NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN,
                    NonogramSolveAction.COLOUR_FIELDS_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE_IN_COLUMN,
                    NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                    NonogramSolveAction.PLACE_XS_AT_UNREACHABLE_FIELDS_IN_COLUMN,
                    NonogramSolveAction.PLACE_XS_AROUND_LONGEST_SEQUENCES_IN_COLUMN,
                    NonogramSolveAction.PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES_IN_COLUMN,
                    NonogramSolveAction.PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_COLUMN,
                    NonogramSolveAction.PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_COLUMN,
                    NonogramSolveAction.PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_IN_COLUMN,
                    NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN
            )) {
                NonogramActionDetails detail = new NonogramActionDetails();
                detail.setActionName(action);
                detail.setTriggeringActionName(null);
                detail.setChangedState(false);
                result.add(detail);
            }
        }
        return result;
    }
}
