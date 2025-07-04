package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.solver.actions;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.model.NonogramActionDetails;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.enums.NonogramSolveAction;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ActionDetailsGenerator {

    public static List<NonogramActionDetails> generateAllPossibleSingleActionDetails(int height, int width) {
        List<NonogramActionDetails> result = new ArrayList<>();
        int index = 0;

        // Wiersze
        for (int rowIdx = 0; rowIdx < height; rowIdx++) {
            for (NonogramSolveAction action : List.of(
                    NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES,
                    NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                    NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY,
                    NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES,
                    NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE,
                    NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_ROW,
                    NonogramSolveAction.COLOUR_FIELDS_IN_ROW_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE,
                    NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
                    NonogramSolveAction.PLACE_XS_ROW_AT_UNREACHABLE_FIELDS,
                    NonogramSolveAction.PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES,
                    NonogramSolveAction.PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES,
                    NonogramSolveAction.PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE,
                    NonogramSolveAction.ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH,
                    NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_ROW
            )) {
                NonogramActionDetails detail = new NonogramActionDetails();
                detail.setIndex(index++);
                detail.setActionName(action);
                detail.setTriggeringActionName(null);
                detail.setChangedState(false);
                result.add(detail);
            }
        }

        // Kolumny
        for (int colIdx = 0; colIdx < width; colIdx++) {
            for (NonogramSolveAction action : List.of(
                    NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES,
                    NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
                    NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY,
                    NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES,
                    NonogramSolveAction.CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE,
                    NonogramSolveAction.COLOUR_OVERLAPPING_FIELDS_IN_COLUMN,
                    NonogramSolveAction.COLOUR_FIELDS_IN_COLUMN_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE,
                    NonogramSolveAction.EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
                    NonogramSolveAction.PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS,
                    NonogramSolveAction.PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES,
                    NonogramSolveAction.PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES,
                    NonogramSolveAction.PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE,
                    NonogramSolveAction.PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE,
                    NonogramSolveAction.COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH,
                    NonogramSolveAction.MARK_AVAILABLE_FIELDS_IN_COLUMN
            )) {
                NonogramActionDetails detail = new NonogramActionDetails();
                detail.setIndex(index++);
                detail.setActionName(action);
                detail.setTriggeringActionName(null);
                detail.setChangedState(false);
                result.add(detail);
            }
        }

        return result;
    }

}
