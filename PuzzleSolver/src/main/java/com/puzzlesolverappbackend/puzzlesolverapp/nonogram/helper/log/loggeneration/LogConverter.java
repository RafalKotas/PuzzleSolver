package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.AssignmentConflictLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.ExtendLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.OverlappingLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.TooLongMergeLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.exclusion.ExcludedSequenceLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.marking.MarkAvailableFieldsLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed.PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed.PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed.PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range.*;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement.*;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class LogConverter {

    public static Optional<String> convertLogByAction(
            String log,
            String solutionName,
            NonogramLogic logic,
            String actionType
    ) {
        return switch (actionType) {
            // correction
            case "SEQUENCES_RANGE_CORRECTION" ->
                    Optional.of(SequenceRangeCorrectionLogHelper.convertLogToTestArguments(log, solutionName));
            case "SEQUENCE_RANGE_CORRECTION_WHEN_MET_COLOURED_FIELDS" ->
                    Optional.of(SequenceRangeCorrectionWhenMetColouredFieldsLogHelper.convertLogToTestArguments(log, solutionName, logic));
            case "SEQUENCES_RANGE_CORRECTION_IF_X_ON_WAY" ->
                    Optional.of(SequenceRangeCorrectionWhenMetXLogHelper.convertLogToTestArguments(log, solutionName, logic));
            case "SEQUENCES_RANGE_CORRECTION_BY_MATCHING_FIELDS_TO_SEQUENCES" -> // TODO - implementation methods
                    Optional.of(SequenceRangeCorrectionByMatchingLogHelper.convertLogToTestArguments(log, solutionName, logic));
            case "SEQUENCES_RANGE_CORRECTION_FROM_COLOURED_EDGES" ->
                    Optional.of(SequenceRangeCorrectionFromColouredEdgesLogHelper.convertLogToTestArguments(log, solutionName));
            case "SEQUENCE_RANGE_CORRECTION_WHEN_MARKING_FIELDS" ->
                    Optional.of(SequenceRangeCorrectionWhenMarkingFieldsLogHelper.convertLogToTestArguments(log, solutionName, logic));

            // colour
            case "OVERLAP" -> Optional.of(OverlappingLogHelper.convertLogToTestArguments(log, solutionName, logic));
            case "TOO_LONG_MERGE" -> Optional.of(TooLongMergeLogHelper.convertLogToTestArguments(log, solutionName, logic));
            case "EXTEND" -> Optional.of(ExtendLogHelper.convertLogToTestArguments(log, solutionName, logic));
            // TODO - implementation methods
            case "COLOUR_IF_X_CAUSES_ASSIGNMENT_CONFLICT" -> Optional.of(AssignmentConflictLogHelper.convertLogToTestArguments(log, solutionName, logic));

            // x placing
            case "PLACE_XS_AT_UNREACHABLE_FIELDS" ->
                    Optional.of(PlaceXsAtUnreachableFieldsLogHelper.convertLogToTestArguments(log, solutionName));
            case "PLACE_XS_AROUND_LONGEST_SEQUENCE" ->
                    Optional.of(PlaceXsAroundLongestSequenceLogHelper.convertLogToTestArguments(log, solutionName));
            case "PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES" ->
                    Optional.of(PlaceXsAtTooShortEmptySequencesLogHelper.convertLogToTestArguments(log, solutionName, logic));
            case "PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE" ->
                    Optional.of(PlaceXsIfONearXWillBeginTooLongPossibleSequenceLogHelper.convertLogToTestArguments(log, solutionName, logic));
            case "PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_SEQUENCE" -> // TODO - implementation methods
                    Optional.of(PlaceXsIfONearXWillMergeNearFieldsToTooLongColouredSequenceLogHelper.convertLogToTestArguments(log, solutionName, logic));

            // mixed
            case "PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART" ->
                    Optional.of(PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper.convertLogToTestArguments(log, solutionName, logic));
            case "PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART" ->
                    Optional.of(PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper.convertLogToTestArguments(log, solutionName, logic));
            case "PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART" ->
                    Optional.of(PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelper.convertLogToTestArguments(log, solutionName, logic));

            // other
            case "MARK_AVAILABLE_FIELDS" ->
                    Optional.of(MarkAvailableFieldsLogHelper.convertLogToTestArguments(log, solutionName, logic));

            case "TRIVIAL" -> Optional.of(TrivialFillLogHelper.convertLogToTestArguments(log, solutionName, logic));
            case "EXCLUDED" -> Optional.of(ExcludedSequenceLogHelper.convertLogToTestArguments(log, solutionName, logic));
            default -> Optional.empty();
        };
    }
    public static String detectActionTypeFromRawLog(String log) {
        // correction
        if (isRangeCorrection(log)) return "SEQUENCES_RANGE_CORRECTION";
        if (isCorrectionWhenMetColoured(log)) return "SEQUENCE_RANGE_CORRECTION_WHEN_MET_COLOURED_FIELDS";
        if (isCorrectionIfXOnWay(log)) return "SEQUENCES_RANGE_CORRECTION_IF_X_ON_WAY";
        if (isMatchingSequenceCorrection(log)) return "SEQUENCES_RANGE_CORRECTION_BY_MATCHING_FIELDS_TO_SEQUENCES";
        if (isCorrectionFromEdges(log)) return "SEQUENCES_RANGE_CORRECTION_FROM_COLOURED_EDGES";
        // SUB-ACTION of "MARK_AVAILABLE_FIELDS"
        if (isRangeCorrectionWhenMarking(log)) return "SEQUENCE_RANGE_CORRECTION_WHEN_MARKING_FIELDS";

        // colour
        if (isOverlap(log)) return "OVERLAP";
        if (isTooLongMerge(log)) return "TOO_LONG_MERGE";
        if (isExtend(log)) return "EXTEND";
        if(isColourFieldsIfXCausesAssignmentConflict(log)) return "COLOUR_FIELDS_IF_X_CAUSES_ASSIGNMENT_CONFLICT";

        // x placing
        if (isPlaceXsAtUnreachable(log)) return "PLACE_XS_AT_UNREACHABLE_FIELDS";
        if (isPlaceXsAroundLongest(log)) return "PLACE_XS_AROUND_LONGEST_SEQUENCE";
        if (isPlaceXsAtTooShort(log)) return "PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES";
        if (isPlaceXIfOWillMergeNearFieldsToTooLongColouredSequence(log)) return "PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE";
        if (isPlaceXIfONearXWillBeginTooLongSeq(log)) return "PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE";

        // mixed
        if (isPreventExtendingColouredSequenceToExcessLengthColouringPart(log))
            return "PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART";
        if (isPreventExtendingColouredSequenceToExcessLengthPlaceXPart(log))
            return "PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART";
        if (isPreventExtendingColouredSequenceToExcessLengthCorrectingRangePart(log))
            return "PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART";

        // other
        if (isMarkAvailableFields(log)) return "MARK_AVAILABLE_FIELDS";

        if (isTrivial(log)) return "TRIVIAL";
        if (isExcluded(log)) return "EXCLUDED";

        return "UNKNOWN";
    }

    // correction

    private static boolean isRangeCorrection(String log) {
        return log.startsWith("ROW_SEQUENCES_RANGES_CORRECTED:") ||
                log.startsWith("COLUMN_SEQUENCES_RANGES_CORRECTED:");
    }

    private static boolean isCorrectionWhenMetColoured(String log) {
        return log.startsWith("ROW_SEQUENCE_CORRECTION_WHEN_MET_COLOURED_FIELDS:") ||
                log.startsWith("COLUMN_SEQUENCE_CORRECTION_WHEN_MET_COLOURED_FIELDS:");
    }

    private static boolean isCorrectionIfXOnWay(String log) {
        return log.startsWith("CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY:") ||
                log.startsWith("CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY:");
    }

    private static boolean isMatchingSequenceCorrection(String log) {
        return log.startsWith("ROW_CORRECTING_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES") ||
                log.startsWith("COLUMN_CORRECTING_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES");
    }

    private static boolean isCorrectionFromEdges(String log) {
        return log.startsWith("ROW_CORRECTING_SEQUENCES_RANGES_FROM_COLOURED_EDGES:") ||
                log.startsWith("COLUMN_CORRECTING_SEQUENCES_RANGES_FROM_COLOURED_EDGES:");
    }

    private static boolean isRangeCorrectionWhenMarking(String log) {
        return log.startsWith("ROW_SEQUENCE_RANGE_CORRECTION_WHEN_MARKING_FIELDS:") ||
                log.startsWith("COLUMN_SEQUENCE_RANGE_CORRECTION_WHEN_MARKING_FIELDS:");
    }

    // colour

    private static boolean isOverlap(String log) {
        return log.startsWith("OVERLAP_");
    }

    private static boolean isTooLongMerge(String log) {
        return log.startsWith("TOO_LONG_MERGE_");
    }

    private static boolean isExtend(String log) {
        return log.startsWith("EXTEND_");
    }

    private static boolean isColourFieldsIfXCausesAssignmentConflict(String log) {
        return log.startsWith("COLOUR_IF_X_CAUSES_ASSIGNMENT_CONFLICT_IN_");
    }

    // x placing

    private static boolean isPlaceXsAtUnreachable(String log) {
        return log.startsWith("PLACE_XS_AT_UNREACHABLE_FIELDS_IN_ROW:") ||
                log.startsWith("PLACE_XS_AT_UNREACHABLE_FIELDS_IN_COLUMN:");
    }

    private static boolean isPlaceXsAroundLongest(String log) {
        return log.startsWith("PLACE_XS_ROW_AROUND_LONGEST_SEQUENCE:") ||
                log.startsWith("PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCE:");
    }

    private static boolean isPlaceXsAtTooShort(String log) {
        return log.startsWith("PLACE_XS_IN_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES:") ||
                log.startsWith("PLACE_XS_IN_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES:");
    }

    private static boolean isPlaceXIfOWillMergeNearFieldsToTooLongColouredSequence(String log) {
        return log.startsWith("PLACE_X_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_ROW:") ||
                log.startsWith("PLACE_X_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_COLUMN:");
    }

    private static boolean isPlaceXIfONearXWillBeginTooLongSeq(String log) {
        return log.startsWith("PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_ROW:") ||
                log.startsWith("PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_COLUMN:");
    }

    // mixed
    private static boolean isPreventExtendingColouredSequenceToExcessLengthColouringPart(String log) {
        return log.startsWith("PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART_IN_ROW") ||
                log.startsWith("PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART_IN_COLUMN");
    }

    private static boolean isPreventExtendingColouredSequenceToExcessLengthPlaceXPart(String log) {
        return log.startsWith("PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART_IN_ROW") ||
                log.startsWith("PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART_IN_COLUMN");
    }

    private static boolean isPreventExtendingColouredSequenceToExcessLengthCorrectingRangePart(String log) {
        return log.startsWith("ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART") ||
                log.startsWith("COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART");
    }

    // other

    private static boolean isMarkAvailableFields(String log) {
        return log.startsWith("MARK_AVAILABLE_FIELDS_IN_ROW:") || log.startsWith("MARK_AVAILABLE_FIELDS_IN_COLUMN:");
    }

    private static boolean isTrivial(String log) {
        return log.startsWith("TRIVIAL_ROW_SEQUENCE:") || log.startsWith("TRIVIAL_COLUMN_SEQUENCE:");
    }

    private static boolean isExcluded(String log) {
        return log.startsWith("EXCLUDED_ROW_SEQUENCE:") || log.startsWith("EXCLUDED_COLUMN_SEQUENCE:");
    }
}
