package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.ExtendLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.OverlappingLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.TooLongMergeLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.exclusion.ExcludedSequenceLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.marking.MarkAvailableFieldsLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range.*;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement.PlaceXsAroundLongestSequenceLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement.PlaceXsAtTooShortEmptySequencesLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement.PlaceXsAtUnreachableFieldsLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement.PlaceXsIfOWillCreateTooLongSequenceLogHelper;
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
            case "OVERLAP" -> Optional.of(OverlappingLogHelper.convertLogToTestArguments(log, solutionName, logic));
            case "EXTEND" -> Optional.of(ExtendLogHelper.convertLogToTestArguments(log, solutionName, logic));
            case "TOO_LONG_MERGE" -> Optional.of(TooLongMergeLogHelper.convertLogToTestArguments(log, solutionName, logic));
            case "TRIVIAL" -> Optional.of(TrivialFillLogHelper.convertLogToTestArguments(log, solutionName, logic));
            case "EXCLUDED" -> Optional.of(ExcludedSequenceLogHelper.convertLogToTestArguments(log, solutionName, logic));
            case "SEQUENCES_RANGES_CORRECTION" ->
                    Optional.of(SequenceRangeCorrectionLogHelper.convertLogToTestArguments(log, solutionName));
            case "SEQUENCE_CORRECTION_WHEN_MET_COLOURED_FIELDS" ->
                    Optional.of(SequenceRangeCorrectionWhenMetColouredFieldsLogHelper.convertLogToTestArguments(log, solutionName, logic));
            case "SEQUENCE_RANGE_CORRECTION_WHEN_MARKING_FIELDS" ->
                    Optional.of(SequenceRangeCorrectionWhenMarkingFieldsLogHelper.convertLogToTestArguments(log, solutionName, logic));
            case "SEQUENCES_RANGES_CORRECTION_FROM_COLOURED_EDGES" ->
                    Optional.of(SequenceRangeCorrectionFromColouredEdgesLogHelper.convertLogToTestArguments(log, solutionName));
            case "SEQUENCES_RANGES_CORRECTION_IF_X_ON_WAY" ->
                    Optional.of(SequenceRangeCorrectionWhenMetXLogHelper.convertLogToTestArguments(log, solutionName, logic));
            // SEQUENCE_RANGE_CORRECTION_BY_MATCHING  SequenceRangeCorrectionByMatchingLogHelper ...
            case "PLACE_XS_AROUND_LONGEST_SEQUENCE" ->
                    Optional.of(PlaceXsAroundLongestSequenceLogHelper.convertLogToTestArguments(log, solutionName));
            case "PLACE_XS_AT_UNREACHABLE_FIELDS" ->
                    Optional.of(PlaceXsAtUnreachableFieldsLogHelper.convertLogToTestArguments(log, solutionName));
            case "PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES" ->
                    Optional.of(PlaceXsAtTooShortEmptySequencesLogHelper.convertLogToTestArguments(log, solutionName, logic));
            case "PLACE_X_IF_O_WILL_CREATE_TOO_LONG_SEQUENCE" ->
                    Optional.of(PlaceXsIfOWillCreateTooLongSequenceLogHelper.convertLogToTestArguments(log, solutionName, logic));
            case "MARK_AVAILABLE_FIELDS" ->
                    Optional.of(MarkAvailableFieldsLogHelper.convertLogToTestArguments(log, solutionName, logic));
            default -> Optional.empty();
        };
    }
    public static String detectActionTypeFromRawLog(String log) {
        if (isOverlap(log)) return "OVERLAP";
        if (isExtend(log)) return "EXTEND";
        if (isTooLongMerge(log)) return "TOO_LONG_MERGE";
        if (isTrivial(log)) return "TRIVIAL";
        if (isExcluded(log)) return "EXCLUDED";
        if (isRangeCorrection(log)) return "SEQUENCES_RANGES_CORRECTION";
        if (isCorrectionWhenMetColoured(log)) return "SEQUENCE_CORRECTION_WHEN_MET_COLOURED_FIELDS";
        if (isRangeCorrectionWhenMarking(log)) return "SEQUENCE_RANGE_CORRECTION_WHEN_MARKING_FIELDS";
        if (isCorrectionFromEdges(log)) return "SEQUENCES_RANGES_CORRECTION_FROM_COLOURED_EDGES";
        if (isCorrectionIfXOnWay(log)) return "SEQUENCES_RANGES_CORRECTION_IF_X_ON_WAY";
        if (isMatchingSequenceCorrection(log)) return "SEQUENCE_RANGE_CORRECTION_BY_MATCHING";
        if (isPlaceXsAroundLongest(log)) return "PLACE_XS_AROUND_LONGEST_SEQUENCE";
        if (isPlaceXsAtUnreachable(log)) return "PLACE_XS_AT_UNREACHABLE_FIELDS";
        if (isPlaceXsAtTooShort(log)) return "PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES";
        if (isPlaceXIfOWouldBeTooLong(log)) return "PLACE_X_IF_O_WILL_CREATE_TOO_LONG_SEQUENCE";
        if (isMarkAvailableFields(log)) return "MARK_AVAILABLE_FIELDS";

        return "UNKNOWN";
    }

    private static boolean isOverlap(String log) {
        return log.startsWith("OVERLAP_");
    }

    private static boolean isExtend(String log) {
        return log.startsWith("EXTEND_");
    }

    private static boolean isTooLongMerge(String log) {
        return log.startsWith("TOO_LONG_MERGE_");
    }

    private static boolean isTrivial(String log) {
        return log.startsWith("TRIVIAL_ROW_SEQUENCE:") || log.startsWith("TRIVIAL_COLUMN_SEQUENCE:");
    }

    private static boolean isExcluded(String log) {
        return log.startsWith("EXCLUDED_ROW_SEQUENCE:") || log.startsWith("EXCLUDED_COLUMN_SEQUENCE:");
    }

    private static boolean isRangeCorrection(String log) {
        return log.startsWith("ROW_SEQUENCES_RANGES_CORRECTED:") ||
                log.startsWith("COLUMN_SEQUENCES_RANGES_CORRECTED:");
    }

    private static boolean isCorrectionWhenMetColoured(String log) {
        return log.startsWith("ROW_SEQUENCE_CORRECTION_WHEN_MET_COLOURED_FIELDS:") ||
                log.startsWith("COLUMN_SEQUENCE_CORRECTION_WHEN_MET_COLOURED_FIELDS:");
    }

    private static boolean isRangeCorrectionWhenMarking(String log) {
        return log.startsWith("ROW_SEQUENCE_RANGE_CORRECTION_WHEN_MARKING_FIELDS:") ||
                log.startsWith("COLUMN_SEQUENCE_RANGE_CORRECTION_WHEN_MARKING_FIELDS:");
    }

    private static boolean isCorrectionFromEdges(String log) {
        return log.startsWith("ROW_CORRECTING_SEQUENCES_RANGES_FROM_COLOURED_EDGES:") ||
                log.startsWith("COLUMN_CORRECTING_SEQUENCES_RANGES_FROM_COLOURED_EDGES:");
    }

    private static boolean isCorrectionIfXOnWay(String log) {
        return log.startsWith("CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY:") ||
                log.startsWith("CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY:");
    }

    private static boolean isMatchingSequenceCorrection(String log) {
        return log.contains("correcting sequence when matching fields to only possible coloured sequences");
    }

    private static boolean isPlaceXsAroundLongest(String log) {
        return log.startsWith("PLACE_XS_ROW_AROUND_LONGEST_SEQUENCE:") ||
                log.startsWith("PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCE:");
    }

    private static boolean isPlaceXsAtUnreachable(String log) {
        return log.startsWith("PLACE_XS_AT_UNREACHABLE_FIELDS_IN_ROW:") ||
                log.startsWith("PLACE_XS_AT_UNREACHABLE_FIELDS_IN_COLUMN:");
    }

    private static boolean isPlaceXsAtTooShort(String log) {
        return log.startsWith("PLACE_XS_IN_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES:") ||
                log.startsWith("PLACE_XS_IN_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES:");
    }

    private static boolean isPlaceXIfOWouldBeTooLong(String log) {
        return log.startsWith("PLACE_X_IF_O_WILL_CREATE_TOO_LONG_SEQUENCE_IN_ROW:") ||
                log.startsWith("PLACE_X_IF_O_WILL_CREATE_TOO_LONG_SEQUENCE_IN_COLUMN:");
    }

    private static boolean isMarkAvailableFields(String log) {
        return log.startsWith("MARK_AVAILABLE_FIELDS_IN_ROW:") ||
                log.startsWith("MARK_AVAILABLE_FIELDS_IN_COLUMN:");
    }
}
