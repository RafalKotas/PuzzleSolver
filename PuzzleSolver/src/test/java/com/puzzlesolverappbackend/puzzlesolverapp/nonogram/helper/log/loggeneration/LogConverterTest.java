package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.loggeneration;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.AssignmentConflictLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.ColouringFieldsIfXWouldForceTooLongColouredFieldsSequenceLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.ExtendLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.colouring.OverlappingLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.exclusion.ExcludedSequenceLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.marking.MarkAvailableFieldsLogHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.mixed.*;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.range.*;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.helper.log.xplacement.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

class LogConverterTest {

    private static final String LOG = "dummy log";
    private static final String SOL = "sol.json";
    private static final String OK = "OK";

    @Test
    @DisplayName("LogConverter constructor should throw UnsupportedOperationException - reflect instantiation")
    void constructor_throwsException_whenInstantiatedReflectively() throws Exception {
        // given
        Constructor<LogConverter> constructor = LogConverter.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // when
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // then
        Throwable cause = exception.getCause();
        assertInstanceOf(UnsupportedOperationException.class, cause);
    }

    @Nested
    class ConvertRouting {

        @Test @DisplayName("routes SEQUENCES_RANGES_CORRECTION")
        void routes_rangesCorrection() {
            try (MockedStatic<SequencesRangesCorrectionLogHelper> ms = mockStatic(SequencesRangesCorrectionLogHelper.class)) {
                ms.when(() -> SequencesRangesCorrectionLogHelper.convertLogToTestArguments(LOG, SOL)).thenReturn(OK);
                Optional<String> out = LogConverter.convertLogByAction(LOG, SOL, "SEQUENCES_RANGES_CORRECTION");
                assertThat(out).contains(OK);
                ms.verify(() -> SequencesRangesCorrectionLogHelper.convertLogToTestArguments(LOG, SOL));
            }
        }

        @Test @DisplayName("routes SEQUENCES_RANGES_CORRECTION_WHEN_MET_COLOURED_FIELDS")
        void routes_whenMetColoured() {
            try (MockedStatic<SequenceRangeCorrectionWhenMetColouredFieldsLogHelper> ms = mockStatic(SequenceRangeCorrectionWhenMetColouredFieldsLogHelper.class)) {
                ms.when(() -> SequenceRangeCorrectionWhenMetColouredFieldsLogHelper.convertLogToTestArguments(LOG, SOL)).thenReturn(OK);
                Optional<String> out = LogConverter.convertLogByAction(LOG, SOL, "SEQUENCES_RANGES_CORRECTION_WHEN_MET_COLOURED_FIELDS");
                assertThat(out).contains(OK);
            }
        }

        @Test @DisplayName("routes SEQUENCES_RANGES_CORRECTION_IF_X_ON_WAY")
        void routes_ifXOnWay() {
            try (MockedStatic<SequenceRangeCorrectionWhenMetXLogHelper> ms = mockStatic(SequenceRangeCorrectionWhenMetXLogHelper.class)) {
                ms.when(() -> SequenceRangeCorrectionWhenMetXLogHelper.convertLogToTestArguments(LOG, SOL)).thenReturn(OK);
                Optional<String> out = LogConverter.convertLogByAction(LOG, SOL, "SEQUENCES_RANGES_CORRECTION_IF_X_ON_WAY");
                assertThat(out).contains(OK);
            }
        }

        @Test @DisplayName("routes SEQUENCES_RANGES_CORRECTION_BY_MATCHING_FIELDS_TO_SEQUENCES")
        void routes_matchingFields() {
            try (MockedStatic<SequenceRangeCorrectionWhenMatchingFieldsToSequencesLogHelper> ms = mockStatic(SequenceRangeCorrectionWhenMatchingFieldsToSequencesLogHelper.class)) {
                ms.when(() -> SequenceRangeCorrectionWhenMatchingFieldsToSequencesLogHelper.convertLogToTestArguments(LOG, SOL)).thenReturn(OK);
                Optional<String> out = LogConverter.convertLogByAction(LOG, SOL, "SEQUENCES_RANGES_CORRECTION_BY_MATCHING_FIELDS_TO_SEQUENCES");
                assertThat(out).contains(OK);
            }
        }

        @Test @DisplayName("routes SEQUENCES_RANGES_CORRECTION_FROM_COLOURED_EDGES")
        void routes_fromEdges() {
            try (MockedStatic<SequenceRangeCorrectionFromColouredEdgesLogHelper> ms = mockStatic(SequenceRangeCorrectionFromColouredEdgesLogHelper.class)) {
                ms.when(() -> SequenceRangeCorrectionFromColouredEdgesLogHelper.convertLogToTestArguments(LOG, SOL)).thenReturn(OK);
                Optional<String> out = LogConverter.convertLogByAction(LOG, SOL, "SEQUENCES_RANGES_CORRECTION_FROM_COLOURED_EDGES");
                assertThat(out).contains(OK);
            }
        }

        @Test @DisplayName("routes SEQUENCES_RANGES_CORRECTION_WHEN_MARKING_FIELDS")
        void routes_whenMarking() {
            try (MockedStatic<SequenceRangeCorrectionWhenMarkingFieldsLogHelper> ms = mockStatic(SequenceRangeCorrectionWhenMarkingFieldsLogHelper.class)) {
                ms.when(() -> SequenceRangeCorrectionWhenMarkingFieldsLogHelper.convertLogToTestArguments(LOG, SOL)).thenReturn(OK);
                Optional<String> out = LogConverter.convertLogByAction(LOG, SOL, "SEQUENCES_RANGES_CORRECTION_WHEN_MARKING_FIELDS");
                assertThat(out).contains(OK);
            }
        }

        @Test @DisplayName("routes SEQUENCES_RANGES_CORRECTION_WHEN_PLACING_X")
        void routes_whenPlacingX() {
            try (MockedStatic<SequenceRangeCorrectionWhenPlacingXsLogHelper> ms = mockStatic(SequenceRangeCorrectionWhenPlacingXsLogHelper.class)) {
                ms.when(() -> SequenceRangeCorrectionWhenPlacingXsLogHelper.convertLogToTestArguments(LOG, SOL)).thenReturn(OK);
                Optional<String> out = LogConverter.convertLogByAction(LOG, SOL, "SEQUENCES_RANGES_CORRECTION_WHEN_PLACING_X");
                assertThat(out).contains(OK);
            }
        }

        @Test @DisplayName("routes COLOUR_OVERLAPPING_FIELDS")
        void routes_overlap() {
            try (MockedStatic<OverlappingLogHelper> ms = mockStatic(OverlappingLogHelper.class)) {
                ms.when(() -> OverlappingLogHelper.convertLogToTestArguments(LOG, SOL)).thenReturn(OK);
                Optional<String> out = LogConverter.convertLogByAction(LOG, SOL, "COLOUR_OVERLAPPING_FIELDS");
                assertThat(out).contains(OK);
            }
        }

        @Test @DisplayName("routes TOO_LONG_MERGE")
        void routes_tooLongMerge() {
            try (MockedStatic<ColouringFieldsIfXWouldForceTooLongColouredFieldsSequenceLogHelper> ms = mockStatic(ColouringFieldsIfXWouldForceTooLongColouredFieldsSequenceLogHelper.class)) {
                ms.when(() -> ColouringFieldsIfXWouldForceTooLongColouredFieldsSequenceLogHelper.convertLogToTestArguments(LOG, SOL)).thenReturn(OK);
                Optional<String> out = LogConverter.convertLogByAction(LOG, SOL, "TOO_LONG_MERGE");
                assertThat(out).contains(OK);
            }
        }

        @Test @DisplayName("routes EXTEND")
        void routes_extend() {
            try (MockedStatic<ExtendLogHelper> ms = mockStatic(ExtendLogHelper.class)) {
                ms.when(() -> ExtendLogHelper.convertLogToTestArguments(LOG, SOL)).thenReturn(OK);
                Optional<String> out = LogConverter.convertLogByAction(LOG, SOL, "EXTEND");
                assertThat(out).contains(OK);
            }
        }

        @Test @DisplayName("routes COLOUR_IF_X_CAUSES_ASSIGNMENT_CONFLICT")
        void routes_assignmentConflict() {
            try (MockedStatic<AssignmentConflictLogHelper> ms = mockStatic(AssignmentConflictLogHelper.class)) {
                ms.when(() -> AssignmentConflictLogHelper.convertLogToTestArguments(LOG, SOL)).thenReturn(OK);
                Optional<String> out = LogConverter.convertLogByAction(LOG, SOL, "COLOUR_IF_X_CAUSES_ASSIGNMENT_CONFLICT");
                assertThat(out).contains(OK);
            }
        }

        @Test @DisplayName("routes PLACE_XS_AT_UNREACHABLE_FIELDS")
        void routes_placeXsAtUnreachable() {
            try (MockedStatic<PlaceXsAtUnreachableFieldsLogHelper> ms = mockStatic(PlaceXsAtUnreachableFieldsLogHelper.class)) {
                ms.when(() -> PlaceXsAtUnreachableFieldsLogHelper.convertLogToTestArguments(LOG, SOL)).thenReturn(OK);
                Optional<String> out = LogConverter.convertLogByAction(LOG, SOL, "PLACE_XS_AT_UNREACHABLE_FIELDS");
                assertThat(out).contains(OK);
            }
        }

        @Test @DisplayName("routes PLACE_XS_AROUND_LONGEST_SEQUENCES")
        void routes_placeXsAroundLongest() {
            try (MockedStatic<PlaceXsAroundLongestSequencesLogHelper> ms = mockStatic(PlaceXsAroundLongestSequencesLogHelper.class)) {
                ms.when(() -> PlaceXsAroundLongestSequencesLogHelper.convertLogToTestArguments(LOG, SOL)).thenReturn(OK);
                Optional<String> out = LogConverter.convertLogByAction(LOG, SOL, "PLACE_XS_AROUND_LONGEST_SEQUENCES");
                assertThat(out).contains(OK);
            }
        }

        @Test @DisplayName("routes PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES")
        void routes_placeXsAtTooShort() {
            try (MockedStatic<PlaceXsAtTooShortEmptySequencesLogHelper> ms = mockStatic(PlaceXsAtTooShortEmptySequencesLogHelper.class)) {
                ms.when(() -> PlaceXsAtTooShortEmptySequencesLogHelper.convertLogToTestArguments(LOG, SOL)).thenReturn(OK);
                Optional<String> out = LogConverter.convertLogByAction(LOG, SOL, "PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES");
                assertThat(out).contains(OK);
            }
        }

        @Test @DisplayName("routes PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE")
        void routes_placeXIfONearXBeginTooLong() {
            try (MockedStatic<PlaceXsIfONearXWillBeginTooLongPossibleSequenceLogHelper> ms =
                         mockStatic(PlaceXsIfONearXWillBeginTooLongPossibleSequenceLogHelper.class)) {
                ms.when(() -> PlaceXsIfONearXWillBeginTooLongPossibleSequenceLogHelper.convertLogToTestArguments(LOG, SOL)).thenReturn(OK);
                Optional<String> out = LogConverter.convertLogByAction(LOG, SOL, "PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE");
                assertThat(out).contains(OK);
            }
        }

        @Test @DisplayName("routes PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE")
        void routes_placeXIfOWillMergeTooLong() {
            try (MockedStatic<PlaceXsIfONearXWillMergeNearFieldsToTooLongColouredSequenceLogHelper> ms =
                         mockStatic(PlaceXsIfONearXWillMergeNearFieldsToTooLongColouredSequenceLogHelper.class)) {
                ms.when(() -> PlaceXsIfONearXWillMergeNearFieldsToTooLongColouredSequenceLogHelper.convertLogToTestArguments(LOG, SOL)).thenReturn(OK);
                Optional<String> out = LogConverter.convertLogByAction(LOG, SOL, "PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE");
                assertThat(out).contains(OK);
            }
        }

        @Test @DisplayName("routes mixed: colouring/placeX/correctingRange parts")
        void routes_mixed_threeParts() {
            try (MockedStatic<PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper> m1 =
                         mockStatic(PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper.class);
                 MockedStatic<PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper> m2 =
                         mockStatic(PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper.class);
                 MockedStatic<PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelper> m3 =
                         mockStatic(PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelper.class)) {
                m1.when(() -> PreventExtendingColouredSequenceToExcessLengthColouringPartLogHelper.convertLogToTestArguments(LOG, SOL)).thenReturn(OK);
                m2.when(() -> PreventExtendingColouredSequenceToExcessLengthPlaceXPartLogHelper.convertLogToTestArguments(LOG, SOL)).thenReturn(OK);
                m3.when(() -> PreventExtendingColouredSequenceToExcessLengthCorrectingRangePartLogHelper.convertLogToTestArguments(LOG, SOL)).thenReturn(OK);

                assertThat(LogConverter.convertLogByAction(LOG, SOL, "PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART")).contains(OK);
                assertThat(LogConverter.convertLogByAction(LOG, SOL, "PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART")).contains(OK);
                assertThat(LogConverter.convertLogByAction(LOG, SOL, "PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART")).contains(OK);
            }
        }

        @Test @DisplayName("routes other: Mark/Trivial/Excluded")
        void routes_other() {
            try (MockedStatic<MarkAvailableFieldsLogHelper> m1 = mockStatic(MarkAvailableFieldsLogHelper.class);
                 MockedStatic<TrivialFillLogHelper> m2 = mockStatic(TrivialFillLogHelper.class);
                 MockedStatic<ExcludedSequenceLogHelper> m3 = mockStatic(ExcludedSequenceLogHelper.class)) {

                m1.when(() -> MarkAvailableFieldsLogHelper.convertLogToTestArguments(LOG, SOL)).thenReturn(OK);
                m2.when(() -> TrivialFillLogHelper.convertLogToTestArguments(LOG, SOL)).thenReturn(OK);
                m3.when(() -> ExcludedSequenceLogHelper.convertLogToTestArguments(LOG, SOL)).thenReturn(OK);

                assertThat(LogConverter.convertLogByAction(LOG, SOL, "MARK_AVAILABLE_FIELDS")).contains(OK);
                assertThat(LogConverter.convertLogByAction(LOG, SOL, "TRIVIAL")).contains(OK);
                assertThat(LogConverter.convertLogByAction(LOG, SOL, "EXCLUDED")).contains(OK);
            }
        }

        @Test @DisplayName("returns empty for unknown action")
        void returns_empty_forUnknown() {
            Optional<String> out = LogConverter.convertLogByAction(LOG, SOL, "SOMETHING_ELSE");
            assertThat(out).isEmpty();
        }
    }

    @Nested
    class Detection {

        @Test @DisplayName("detects all correction flavours")
        void detects_corrections() {
            assertThat(LogConverter.detectActionTypeFromRawLog("SEQUENCES_RANGES_CORRECTION_IN ..."))
                    .isEqualTo("SEQUENCES_RANGES_CORRECTION");
            assertThat(LogConverter.detectActionTypeFromRawLog("SEQUENCES_RANGES_CORRECTION_WHEN_MET_COLOURED_FIELDS ..."))
                    .isEqualTo("SEQUENCES_RANGES_CORRECTION_WHEN_MET_COLOURED_FIELDS");
            assertThat(LogConverter.detectActionTypeFromRawLog("SEQUENCES_RANGES_CORRECTION_IF_X_ON_WAY ..."))
                    .isEqualTo("SEQUENCES_RANGES_CORRECTION_IF_X_ON_WAY");
            assertThat(LogConverter.detectActionTypeFromRawLog("CORRECTING_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES ..."))
                    .isEqualTo("SEQUENCES_RANGES_CORRECTION_BY_MATCHING_FIELDS_TO_SEQUENCES");
            assertThat(LogConverter.detectActionTypeFromRawLog("CORRECTING_SEQUENCES_RANGES_FROM_COLOURED_EDGES ..."))
                    .isEqualTo("SEQUENCES_RANGES_CORRECTION_FROM_COLOURED_EDGES");
            assertThat(LogConverter.detectActionTypeFromRawLog("SEQUENCE_RANGE_CORRECTION_WHEN_MARKING_FIELDS ..."))
                    .isEqualTo("SEQUENCES_RANGES_CORRECTION_WHEN_MARKING_FIELDS");
            assertThat(LogConverter.detectActionTypeFromRawLog("SEQUENCE_CORRECTION_WHEN_PLACING_X ..."))
                    .isEqualTo("SEQUENCES_RANGES_CORRECTION_WHEN_PLACING_X");
        }

        @Test @DisplayName("detects colouring, x placement, mixed and other")
        void detects_otherFamilies() {
            assertThat(LogConverter.detectActionTypeFromRawLog("COLOUR_OVERLAPPING_FIELDS_IN_ROW ...")).isEqualTo("COLOUR_OVERLAPPING_FIELDS");
            assertThat(LogConverter.detectActionTypeFromRawLog("COLOURING_FIELDS_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE ...")).isEqualTo("TOO_LONG_MERGE");
            assertThat(LogConverter.detectActionTypeFromRawLog("EXTEND_ROW ...")).isEqualTo("EXTEND");
            assertThat(LogConverter.detectActionTypeFromRawLog("COLOUR_IF_X_CAUSES_ASSIGNMENT_CONFLICT_IN_ROW ...")).isEqualTo("COLOUR_FIELDS_IF_X_CAUSES_ASSIGNMENT_CONFLICT");

            assertThat(LogConverter.detectActionTypeFromRawLog("PLACE_XS_AT_UNREACHABLE_FIELDS_IN_ROW ...")).isEqualTo("PLACE_XS_AT_UNREACHABLE_FIELDS");
            assertThat(LogConverter.detectActionTypeFromRawLog("PLACE_XS_AROUND_LONGEST_SEQUENCES ...")).isEqualTo("PLACE_XS_AROUND_LONGEST_SEQUENCES");
            assertThat(LogConverter.detectActionTypeFromRawLog("PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES ...")).isEqualTo("PLACE_XS_AT_TOO_SHORT_EMPTY_SEQUENCES");
            assertThat(LogConverter.detectActionTypeFromRawLog("PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE_IN_ROW: ..."))
                    .isEqualTo("PLACE_XS_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE");

            // detector returns a different constant than converter expects – this flags the mismatch explicitly
            assertThat(LogConverter.detectActionTypeFromRawLog("PLACE_X_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE_IN_COLUMN ..."))
                    .isEqualTo("PLACE_XS_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE");

            assertThat(LogConverter.detectActionTypeFromRawLog("PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART_IN_ROW ..."))
                    .isEqualTo("PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_COLOURING_PART");
            assertThat(LogConverter.detectActionTypeFromRawLog("PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART_IN_COLUMN ..."))
                    .isEqualTo("PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_PLACE_X_PART");
            assertThat(LogConverter.detectActionTypeFromRawLog("PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART_IN_ROW ..."))
                    .isEqualTo("PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH_CORRECTING_RANGE_PART");

            assertThat(LogConverter.detectActionTypeFromRawLog("MARK_AVAILABLE_FIELDS_IN_ROW ...")).isEqualTo("MARK_AVAILABLE_FIELDS");
            assertThat(LogConverter.detectActionTypeFromRawLog("FILL_TRIVIAL_SEQUENCE_IN_COLUMN ...")).isEqualTo("TRIVIAL");
            assertThat(LogConverter.detectActionTypeFromRawLog("EXCLUSION_SEQUENCE_IN_ROW ...")).isEqualTo("EXCLUDED");
        }

        @Test @DisplayName("returns UNKNOWN for unrecognized log line")
        void detects_unknown() {
            assertThat(LogConverter.detectActionTypeFromRawLog("SOMETHING COMPLETELY DIFFERENT")).isEqualTo("UNKNOWN");
        }
    }

    @Test
    @DisplayName("mismatched detector key is not routable by converter")
    void mismatched_detector_key_fallsBackToEmpty() {
        Optional<String> out = LogConverter.convertLogByAction(LOG, SOL,
                "PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE");
        assertThat(out).isEmpty(); // this highlights the naming mismatch
    }
}