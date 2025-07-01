package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.colouring.ExtendLogHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.colouring.OverlappingLogHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.colouring.TooLongMergeLogHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.exclusion.ExcludedSequenceLogHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.marking.MarkAvailableFieldsLogHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.range.*;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.xplacement.PlaceXsAroundLongestSequenceLogHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.xplacement.PlaceXsAtTooShortEmptySequencesLogHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.xplacement.PlaceXsAtUnreachableFieldsLogHelper;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.loggeneration.xplacement.PlaceXsIfOWillCreateTooLongSequenceLogHelper;
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
//            case "SEQUENCE_RANGE_CORRECTION_BY_MATCHING" ->
//                    Optional.of(SequenceRangeCorrectionByMatchingLogHelper.convertLogToTestArguments(log, solutionName));
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
        if (log.startsWith("OVERLAP_")) return "OVERLAP";
        if (log.startsWith("EXTEND_")) return "EXTEND";
        if (log.startsWith("TOO_LONG_MERGE_")) return "TOO_LONG_MERGE";
        if (log.startsWith("TRIVIAL_ROW_SEQUENCE:") || log.startsWith("TRIVIAL_COLUMN_SEQUENCE:")) return "TRIVIAL";
        if (log.startsWith("EXCLUDED_ROW_SEQUENCE:") || log.startsWith("EXCLUDED_COLUMN_SEQUENCE:")) return "EXCLUDED";
        if (log.startsWith("ROW_SEQUENCES_RANGES_CORRECTED:")
                || log.startsWith("COLUMN_SEQUENCES_RANGES_CORRECTED:"))
            return "SEQUENCES_RANGES_CORRECTION";
        if (log.startsWith("ROW_SEQUENCE_CORRECTION_WHEN_MET_COLOURED_FIELDS:")
                || log.startsWith("COLUMN_SEQUENCE_CORRECTION_WHEN_MET_COLOURED_FIELDS:"))
            return "SEQUENCE_CORRECTION_WHEN_MET_COLOURED_FIELDS";
        if (log.startsWith("ROW_SEQUENCE_RANGE_CORRECTION_WHEN_MARKING_FIELDS:")
                || log.startsWith("COLUMN_SEQUENCE_RANGE_CORRECTION_WHEN_MARKING_FIELDS:"))
            return "SEQUENCE_RANGE_CORRECTION_WHEN_MARKING_FIELDS";
        if (log.startsWith("ROW_CORRECTING_SEQUENCES_RANGES_FROM_COLOURED_EDGES:")
                || log.startsWith("COLUMN_CORRECTING_SEQUENCES_RANGES_FROM_COLOURED_EDGES:"))
            return "SEQUENCES_RANGES_CORRECTION_FROM_COLOURED_EDGES";
        if (log.startsWith("CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY:")
                || log.startsWith("CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY:"))
            return "SEQUENCES_RANGES_CORRECTION_IF_X_ON_WAY";
//        if (log.contains("correcting sequence when matching fields to only possible coloured sequences"))
//            return "SEQUENCE_RANGE_CORRECTION_BY_MATCHING";
        if (log.startsWith("PLACE_XS_ROW_AROUND_LONGEST_SEQUENCE:")
                || log.startsWith("PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCE:"))
            return "PLACE_XS_AROUND_LONGEST_SEQUENCE";
        if (log.startsWith("PLACE_XS_AT_UNREACHABLE_FIELDS_IN_ROW:")
                || log.startsWith("PLACE_XS_AT_UNREACHABLE_FIELDS_IN_COLUMN:"))
            return "PLACE_XS_AT_UNREACHABLE_FIELDS";
        if (log.startsWith("PLACE_XS_IN_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES:")
                || log.startsWith("PLACE_XS_IN_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES:"))
            return "PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES";
        if (log.startsWith("PLACE_X_IF_O_WILL_CREATE_TOO_LONG_SEQUENCE_IN_ROW:")
                || log.startsWith("PLACE_X_IF_O_WILL_CREATE_TOO_LONG_SEQUENCE_IN_COLUMN:"))
            return "PLACE_X_IF_O_WILL_CREATE_TOO_LONG_SEQUENCE";
        if (log.startsWith("MARK_AVAILABLE_FIELDS_IN_ROW:")
                || log.startsWith("MARK_AVAILABLE_FIELDS_IN_COLUMN:"))
            return "MARK_AVAILABLE_FIELDS";
        return "UNKNOWN";
    }
}
