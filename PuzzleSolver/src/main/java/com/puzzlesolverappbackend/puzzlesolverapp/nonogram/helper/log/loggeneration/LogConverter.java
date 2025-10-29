package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.ColouringFieldsIfXWouldForceTooLongColouredFieldsSequenceLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.ExtendLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.OverlappingLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.exclusion.ExcludedSequenceLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.marking.MarkAvailableFieldsLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed.*;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range.*;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement.PlaceXsAroundLongestSequencesLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement.PlaceXsAtTooShortEmptySequencesLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement.PlaceXsAtUnreachableFieldsLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement.PlaceXsIfONearXWillBeginTooLongPossibleSequenceLogHelper;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class LogConverter {

    private static final String SEQUENCES_RANGES_CORRECTION = "SEQUENCES_RANGES_CORRECTION";

    private static final String SEQUENCES_RANGES_CORRECTION_WHEN_MET_COLOURED_FIELDS = "SEQUENCES_RANGES_CORRECTION_WHEN_MET_COLOURED_FIELDS";

    private static final String SEQUENCES_RANGES_CORRECTION_IF_X_ON_WAY = "SEQUENCES_RANGES_CORRECTION_IF_X_ON_WAY";

    private static final String COLOUR_OVERLAPPING_FIELDS = "COLOUR_OVERLAPPING_FIELDS";

    private static final String PLACE_XS_AT_UNREACHABLE_FIELDS = "PLACE_XS_AT_UNREACHABLE_FIELDS"; // start

    private static final String PLACE_XS_AROUND_LONGEST_SEQUENCES = "PLACE_XS_AROUND_LONGEST_SEQUENCES";

    private static final String PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES = "PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES";

    private static final String PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE = "PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE";

    private static final String PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART = "PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART";

    private static final String PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART = "PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART";

    private static final String PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART = "PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART";

    private static final String MARK_AVAILABLE_FIELDS = "MARK_AVAILABLE_FIELDS";

    private static final String SEQUENCES_RANGES_CORRECTION_BY_MATCHING_FIELDS_TO_SEQUENCES = "SEQUENCES_RANGES_CORRECTION_BY_MATCHING_FIELDS_TO_SEQUENCES";

    private static final String SEQUENCES_RANGES_CORRECTION_WHEN_MARKING_FIELDS = "SEQUENCES_RANGES_CORRECTION_WHEN_MARKING_FIELDS";

    private static final String SEQUENCES_RANGES_CORRECTION_WHEN_PLACING_X = "SEQUENCES_RANGES_CORRECTION_WHEN_PLACING_X";

    private static final String SEQUENCES_RANGES_CORRECTION_FROM_COLOURED_EDGES = "SEQUENCES_RANGES_CORRECTION_FROM_COLOURED_EDGES";

    private static final String PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE = "PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE";

    private static final String UNKNOWN = "UNKNOWN";

    public static Optional<String> convertLogByAction(
            String log,
            String solutionName,
            String actionType
    ) {
        return switch (actionType) {
            // correction
            case SEQUENCES_RANGES_CORRECTION ->
                    Optional.of(SequencesRangesCorrectionLogHelper.convertLogToTestArguments(log, solutionName));
            case SEQUENCES_RANGES_CORRECTION_WHEN_MET_COLOURED_FIELDS ->
                    Optional.of(SequenceRangeCorrectionWhenMetColouredFieldsLogHelper.convertLogToTestArguments(log, solutionName));
            case SEQUENCES_RANGES_CORRECTION_IF_X_ON_WAY ->
                    Optional.of(SequenceRangeCorrectionWhenMetXLogHelper.convertLogToTestArguments(log, solutionName));
            case SEQUENCES_RANGES_CORRECTION_BY_MATCHING_FIELDS_TO_SEQUENCES -> // TODO - implementation methods
                    Optional.of(SequenceRangeCorrectionWhenMatchingFieldsToSequencesLogHelper.convertLogToTestArguments(log, solutionName));
            case SEQUENCES_RANGES_CORRECTION_FROM_COLOURED_EDGES ->
                    Optional.of(SequenceRangeCorrectionFromColouredEdgesLogHelper.convertLogToTestArguments(log, solutionName));
            case SEQUENCES_RANGES_CORRECTION_WHEN_MARKING_FIELDS ->
                    Optional.of(SequenceRangeCorrectionWhenMarkingFieldsLogHelper.convertLogToTestArguments(log, solutionName));
            case SEQUENCES_RANGES_CORRECTION_WHEN_PLACING_X ->
                    Optional.of(SequenceRangeCorrectionWhenPlacingXsLogHelper.convertLogToTestArguments(log, solutionName));

            // colour
            case COLOUR_OVERLAPPING_FIELDS -> Optional.of(OverlappingLogHelper.convertLogToTestArguments(log, solutionName));
            case "TOO_LONG_MERGE" -> Optional.of(ColouringFieldsIfXWouldForceTooLongColouredFieldsSequenceLogHelper.convertLogToTestArguments(log, solutionName));
            case "EXTEND" -> Optional.of(ExtendLogHelper.convertLogToTestArguments(log, solutionName));
            // TODO - implement methods
            // case "COLOUR_IF_X_CAUSES_ASSIGNMENT_CONFLICT" -> Optional.of(AssignmentConflictLogHelper.convertLogToTestArguments(log, solutionName));

            // x placing
            case PLACE_XS_AT_UNREACHABLE_FIELDS ->
                    Optional.of(PlaceXsAtUnreachableFieldsLogHelper.convertLogToTestArguments(log, solutionName));
            case PLACE_XS_AROUND_LONGEST_SEQUENCES ->
                    Optional.of(PlaceXsAroundLongestSequencesLogHelper.convertLogToTestArguments(log, solutionName));
            case PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES ->
                    Optional.of(PlaceXsAtTooShortEmptySequencesLogHelper.convertLogToTestArguments(log, solutionName));
            case PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE ->
                    Optional.of(PlaceXsIfONearXWillBeginTooLongPossibleSequenceLogHelper.convertLogToTestArguments(log, solutionName));
            // TODO - implement methods
            /* case PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE ->
                    Optional.of(PlaceXsIfONearXWillMergeNearFieldsToTooLongColouredSequenceLogHelper.convertLogToTestArguments(log, solutionName)); */

            // mixed
            case PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART ->
                    Optional.of(PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper.convertLogToTestArguments(log, solutionName));
            case PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART ->
                    Optional.of(PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper.convertLogToTestArguments(log, solutionName));
            case PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART ->
                    Optional.of(PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelper.convertLogToTestArguments(log, solutionName));

            // other
            case MARK_AVAILABLE_FIELDS ->
                    Optional.of(MarkAvailableFieldsLogHelper.convertLogToTestArguments(log, solutionName));

            case "TRIVIAL" -> Optional.of(TrivialFillLogHelper.convertLogToTestArguments(log, solutionName));
            case "EXCLUDED" -> Optional.of(ExcludedSequenceLogHelper.convertLogToTestArguments(log, solutionName));
            default -> Optional.empty();
        };
    }

    public static String detectActionTypeFromRawLog(String log) {
        String correctionType = detectCorrectionAction(log);
        if (!correctionType.equals(UNKNOWN)) return correctionType;

        String colourType = detectColourAction(log);
        if (!colourType.equals(UNKNOWN)) return colourType;

        String placeXType = detectPlaceXAction(log);
        if (!placeXType.equals(UNKNOWN)) return placeXType;

        String mixedType = detectMixedAction(log);
        if (!mixedType.equals(UNKNOWN)) return mixedType;

        String otherType = detectOtherAction(log);
        if (!otherType.equals(UNKNOWN)) return otherType;

        return UNKNOWN;
    }

    private static String detectCorrectionAction(String log) {
        if (isRangeCorrection(log)) return SEQUENCES_RANGES_CORRECTION;
        if (isCorrectionWhenMetColoured(log)) return SEQUENCES_RANGES_CORRECTION_WHEN_MET_COLOURED_FIELDS;
        if (isCorrectionIfXOnWay(log)) return SEQUENCES_RANGES_CORRECTION_IF_X_ON_WAY;
        if (isMatchingSequenceCorrection(log)) return SEQUENCES_RANGES_CORRECTION_BY_MATCHING_FIELDS_TO_SEQUENCES;
        if (isCorrectionFromEdges(log)) return SEQUENCES_RANGES_CORRECTION_FROM_COLOURED_EDGES;
        if (isRangeCorrectionWhenMarking(log)) return SEQUENCES_RANGES_CORRECTION_WHEN_MARKING_FIELDS;
        if (isRangeCorrectionWhenPlacingX(log)) return SEQUENCES_RANGES_CORRECTION_WHEN_PLACING_X;
        return UNKNOWN;
    }

    private static String detectColourAction(String log) {
        if (isOverlap(log)) return COLOUR_OVERLAPPING_FIELDS;
        if (isTooLongMerge(log)) return "TOO_LONG_MERGE";
        if (isExtend(log)) return "EXTEND";
        if (isColourFieldsIfXCausesAssignmentConflict(log))
            return "COLOUR_FIELDS_IF_X_CAUSES_ASSIGNMENT_CONFLICT";
        return UNKNOWN;
    }

    private static String detectPlaceXAction(String log) {
        if (isPlaceXsAtUnreachableFields(log)) return PLACE_XS_AT_UNREACHABLE_FIELDS;
        if (isPlaceXsAroundLongestSequences(log)) return PLACE_XS_AROUND_LONGEST_SEQUENCES;
        if (isPlaceXsAtTooShortEmptySequences(log)) return PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES;
        if (isPlaceXIfOWillMergeNearFieldsToTooLongColouredSequence(log))
            return PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE;
        if (isPlaceXIfONearXWillBeginTooLongPossibleColouredSequence(log))
            return PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE;
        return UNKNOWN;
    }

    private static String detectMixedAction(String log) {
        if (isPreventExtendingColouredSequenceToExcessLengthColouringPart(log))
            return PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART;
        if (isPreventExtendingColouredSequenceToExcessLengthPlaceXPart(log))
            return PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART;
        if (isPreventExtendingColouredSequenceToExcessLengthCorrectingRangePart(log))
            return PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART;
        return UNKNOWN;
    }

    private static String detectOtherAction(String log) {
        if (isMarkAvailableFields(log)) return MARK_AVAILABLE_FIELDS;
        if (isTrivial(log)) return "TRIVIAL";
        if (isExcluded(log)) return "EXCLUDED";
        return UNKNOWN;
    }

    // correction

    private static boolean isRangeCorrection(String log) {
        return log.startsWith("SEQUENCES_RANGES_CORRECTION_IN");
    }

    private static boolean isCorrectionWhenMetColoured(String log) {
        return log.startsWith(SEQUENCES_RANGES_CORRECTION_WHEN_MET_COLOURED_FIELDS);
    }

    private static boolean isCorrectionIfXOnWay(String log) {
        return log.startsWith(SEQUENCES_RANGES_CORRECTION_IF_X_ON_WAY);
    }

    private static boolean isMatchingSequenceCorrection(String log) {
        return log.startsWith("CORRECTING_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES");
    }

    private static boolean isCorrectionFromEdges(String log) {
        return log.startsWith("CORRECTING_SEQUENCES_RANGES_FROM_COLOURED_EDGES");
    }

    private static boolean isRangeCorrectionWhenMarking(String log) {
        return log.startsWith("SEQUENCE_RANGE_CORRECTION_WHEN_MARKING_FIELDS");
    }

    private static boolean isRangeCorrectionWhenPlacingX(String log) {
        return log.contains("SEQUENCE_CORRECTION_WHEN_PLACING_X");
    }

    // colour

    private static boolean isOverlap(String log) {
        return log.startsWith(COLOUR_OVERLAPPING_FIELDS);
    }

    private static boolean isTooLongMerge(String log) {
        return log.startsWith("COLOURING_FIELDS_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE");
    }

    private static boolean isExtend(String log) {
        return log.startsWith("EXTEND_");
    }

    private static boolean isColourFieldsIfXCausesAssignmentConflict(String log) {
        return log.startsWith("COLOUR_IF_X_CAUSES_ASSIGNMENT_CONFLICT_IN_");
    }

    // x placing

    private static boolean isPlaceXsAtUnreachableFields(String log) {
        return log.startsWith(PLACE_XS_AT_UNREACHABLE_FIELDS);
    }

    private static boolean isPlaceXsAroundLongestSequences(String log) {
        return log.startsWith(PLACE_XS_AROUND_LONGEST_SEQUENCES);
    }

    private static boolean isPlaceXsAtTooShortEmptySequences(String log) {
        return log.startsWith(PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES);
    }

    private static boolean isPlaceXIfOWillMergeNearFieldsToTooLongColouredSequence(String log) {
        return log.startsWith("PLACE_X_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE");
    }

    private static boolean isPlaceXIfONearXWillBeginTooLongPossibleColouredSequence(String log) {
        return log.startsWith(PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE);
    }

    // mixed
    private static boolean isPreventExtendingColouredSequenceToExcessLengthColouringPart(String log) {
        return log.startsWith(PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART);
    }

    private static boolean isPreventExtendingColouredSequenceToExcessLengthPlaceXPart(String log) {
        return log.startsWith(PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART);
    }

    private static boolean isPreventExtendingColouredSequenceToExcessLengthCorrectingRangePart(String log) {
        return log.startsWith(PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART);
    }

    // other

    private static boolean isMarkAvailableFields(String log) {
        return log.startsWith(MARK_AVAILABLE_FIELDS);
    }

    private static boolean isTrivial(String log) {
        return log.startsWith("FILL_TRIVIAL_SEQUENCE");
    }

    private static boolean isExcluded(String log) {
        return log.startsWith("EXCLUSION_SEQUENCE");
    }
}
