package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/***
    0  - CORRECT_ROW_SEQUENCES_RANGES,
    1  - CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
    2  - CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY,
    3  - COLOUR_OVERLAPPING_FIELDS_IN_ROW,
    4  - EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW,
    5  - PLACE_XS_ROW_AT_UNREACHABLE_FIELDS,
    6  - PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES,
    7  - PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES,
    8  - MARK_AVAILABLE_FIELDS_IN_ROW,

    9  - CORRECT_COLUMN_SEQUENCES_RANGES,
    10 - CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS,
    11 - CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY,
    12 - COLOUR_OVERLAPPING_FIELDS_IN_COLUMN,
    13 - EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN,
    14 - PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS,
    15 - PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES,
    16 - PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES,
    17 - MARK_AVAILABLE_FIELDS_IN_COLUMN,

    TRTC - trivial rows, trivial columns
 ***/
class NonogramLogicOperationsTest {

    NonogramLogic o06005_difficulty_1;
    List<List<Integer>> o06005_rowsSequences = List.of(
            List.of(1, 1, 1),
            List.of(1, 1, 1, 1, 1),
            List.of(1, 5, 1),
            List.of(9),
            List.of(9),
            List.of(7, 1),
            List.of(9),
            List.of(10),
            List.of(1, 1),
            List.of(2, 2)
    );
    List<List<Integer>> o06005_columnsSequences = List.of(
            List.of(7),
            List.of(7),
            List.of(8, 1),
            List.of(6),
            List.of(8),
            List.of(6),
            List.of(10),
            List.of(2, 2, 1),
            List.of(7),
            List.of(1)
    );

    @BeforeEach
    void setUp() {
        o06005_difficulty_1 = new NonogramLogic(o06005_rowsSequences, o06005_columnsSequences, GuessMode.DISABLED);
    }

    @Test
    @DisplayName("o06005-TRTC+3+12+1")
    void should_TRTC_3_12_1() {
        // given
        List<List<List<Integer>>> rowsSequencesRangesBeforeCorrectingWhenMetColouredFields = o06005_difficulty_1.getRowsSequencesRanges();
        assertThat(rowsSequencesRangesBeforeCorrectingWhenMetColouredFields).isEqualTo(Arrays.asList(
                Arrays.asList(Arrays.asList(0, 5), Arrays.asList(2, 7), Arrays.asList(4, 9)),
                Arrays.asList(Arrays.asList(0, 1), Arrays.asList(2, 3), Arrays.asList(4, 5), Arrays.asList(6, 7), Arrays.asList(8, 9)),
                Arrays.asList(Arrays.asList(0, 1), Arrays.asList(2, 7), Arrays.asList(8, 9)),
                List.of(Arrays.asList(0, 9)),
                List.of(Arrays.asList(0, 9)),
                Arrays.asList(Arrays.asList(0, 7), Arrays.asList(8, 9)),
                List.of(Arrays.asList(0, 9)),
                List.of(Arrays.asList(0, 9)),
                Arrays.asList(Arrays.asList(0, 7), Arrays.asList(2, 9)),
                Arrays.asList(Arrays.asList(0, 6), Arrays.asList(3, 9))
        ));

        // when
        /* TRTC */
        o06005_difficulty_1.fillTrivialRowsAndColumns();
        /* 3 */
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().colourOverlappingFieldsInRow(rowIdx);
        }
        /* 12 */
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().colourOverlappingFieldsInColumn(columnIdx);
        }
        /* 1 */
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowSequencesRangesWhenMetColouredField(rowIdx);
        }

        // then
        List<List<List<Integer>>> rowsSequencesRangesAfterCorrectingWhenMetColouredFields = o06005_difficulty_1.getRowsSequencesRanges();
        assertThat(rowsSequencesRangesAfterCorrectingWhenMetColouredFields).isEqualTo(Arrays.asList(
                Arrays.asList(Arrays.asList(0, 2), Arrays.asList(2, 6), Arrays.asList(6, 9)),
                Arrays.asList(Arrays.asList(0, 1), Arrays.asList(2, 3), Arrays.asList(4, 5), Arrays.asList(6, 7), Arrays.asList(8, 9)),
                Arrays.asList(Arrays.asList(0, 1), Arrays.asList(2, 7), Arrays.asList(8, 9)),
                List.of(Arrays.asList(0, 8)),
                List.of(Arrays.asList(0, 8)),
                Arrays.asList(Arrays.asList(0, 6), Arrays.asList(8, 8)),
                List.of(Arrays.asList(0, 8)),
                List.of(Arrays.asList(0, 9)),
                Arrays.asList(Arrays.asList(0, 6), Arrays.asList(6, 9)),
                Arrays.asList(Arrays.asList(1, 3), Arrays.asList(5, 7))
        ));
    }

    @Test
    @DisplayName("o06005-TRTC+3+12+10")
    void should_TRTC_3_12_10() {
        // given
        List<List<List<Integer>>> columnsSequencesRangesBeforeCorrectingWhenMetColouredFields = o06005_difficulty_1.getColumnsSequencesRanges();
        assertThat(columnsSequencesRangesBeforeCorrectingWhenMetColouredFields).isEqualTo(Arrays.asList(
                List.of(Arrays.asList(0, 9)),
                List.of(Arrays.asList(0, 9)),
                Arrays.asList(Arrays.asList(0, 7), Arrays.asList(9, 9)),
                List.of(Arrays.asList(0, 9)),
                List.of(Arrays.asList(0, 9)),
                List.of(Arrays.asList(0, 9)),
                List.of(Arrays.asList(0, 9)),
                Arrays.asList(Arrays.asList(0, 4), Arrays.asList(3, 7), Arrays.asList(6, 9)),
                List.of(Arrays.asList(0, 9)),
                List.of(Arrays.asList(0, 9))
        ));

        // when
        /* TRTC */
        o06005_difficulty_1.fillTrivialRowsAndColumns();
        /* 3 */
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().colourOverlappingFieldsInRow(rowIdx);
        }
        /* 12 */
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().colourOverlappingFieldsInColumn(columnIdx);
        }
        /* 10 */
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().correctColumnSequencesWhenMetColouredField(columnIdx);
        }

        List<List<List<Integer>>> columnsSequencesRangesAfterCorrectingWhenMetColouredFields = o06005_difficulty_1.getColumnsSequencesRanges();
        assertThat(columnsSequencesRangesAfterCorrectingWhenMetColouredFields).isEqualTo(Arrays.asList(
                List.of(Arrays.asList(1, 9)),
                List.of(Arrays.asList(1, 9)),
                Arrays.asList(Arrays.asList(0, 7), Arrays.asList(9, 9)),
                List.of(Arrays.asList(2, 7)),
                List.of(Arrays.asList(0, 9)),
                List.of(Arrays.asList(2, 7)),
                List.of(Arrays.asList(0, 9)),
                Arrays.asList(Arrays.asList(0, 4), Arrays.asList(3, 7), Arrays.asList(7, 9)),
                List.of(Arrays.asList(1, 9)),
                List.of(Arrays.asList(7, 7))
        ));
    }

    @Test
    @DisplayName("o06005-TRTC+3+12+1+10+5")
    void should_TRTC_3_12_1_10_5() {
        // given
        List<List<String>> expectedBoard = NonogramBoards.expectedNonogramBoardTRTC_3_12_1_10_5;

        // when
        /* TRTC */
        o06005_difficulty_1.fillTrivialRowsAndColumns();

        /* 3 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().colourOverlappingFieldsInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 12 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().colourOverlappingFieldsInColumn(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 1 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowSequencesRangesWhenMetColouredField(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 10 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().correctColumnSequencesWhenMetColouredField(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 5 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().placeXsRowAtUnreachableFields(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        // then
        assertThat(o06005_difficulty_1.getNonogramSolutionBoard()).isEqualTo(expectedBoard);
    }

    @Test
    @DisplayName("o06005-TRTC+3+12+1+10+14")
    void should_TRTC_3_12_1_10_14() {
        // given
        List<List<String>> expectedBoard = NonogramBoards.expectedNonogramBoardTRTC_3_12_1_10_14;

        // when
        /* TRTC */
        o06005_difficulty_1.fillTrivialRowsAndColumns();

        /* 3 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().colourOverlappingFieldsInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 12 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().colourOverlappingFieldsInColumn(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 1 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowSequencesRangesWhenMetColouredField(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 10 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().correctColumnSequencesWhenMetColouredField(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 14 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().placeXsColumnAtUnreachableFields(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        // then
        assertThat(o06005_difficulty_1.getNonogramSolutionBoard()).isEqualTo(expectedBoard);
    }

    @Test
    @DisplayName("o06005-TRTC+3+12+1+10+5+14")
    void should_TRTC_3_12_1_10_5_14() {
        // given
        List<List<String>> expectedBoard = NonogramBoards.expectedNonogramBoardTRTC_3_12_1_10_5_14;

        // when
        /* TRTC */
        o06005_difficulty_1.fillTrivialRowsAndColumns();

        /* 3 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().colourOverlappingFieldsInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 12 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().colourOverlappingFieldsInColumn(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 1 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowSequencesRangesWhenMetColouredField(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 10 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().correctColumnSequencesWhenMetColouredField(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 5 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().placeXsRowAtUnreachableFields(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 14 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().placeXsColumnAtUnreachableFields(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        // then
        assertThat(o06005_difficulty_1.getNonogramSolutionBoard()).isEqualTo(expectedBoard);
    }

    @Test
    @DisplayName("o06005-TRTC+3+12+1+10+5+14+6")
    void should_TRTC_3_12_1_10_5_14_6() {
        // given
        List<List<String>> expectedBoard = NonogramBoards.expectedNonogramBoardTRTC_3_12_1_10_5_14_6;

        // when
        /* TRTC */
        o06005_difficulty_1.fillTrivialRowsAndColumns();

        /* 3 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().colourOverlappingFieldsInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 12 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().colourOverlappingFieldsInColumn(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 1 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowSequencesRangesWhenMetColouredField(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 10 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().correctColumnSequencesWhenMetColouredField(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 5 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().placeXsRowAtUnreachableFields(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 14 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().placeXsColumnAtUnreachableFields(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 6 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().placeXsAroundLongestSequencesInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        // then
        assertThat(o06005_difficulty_1.getNonogramSolutionBoard()).isEqualTo(expectedBoard);
    }


    @Test
    @DisplayName("o06005-TRTC+3+12+1+10+5+14+6+2")
    void should_TRTC_3_12_1_10_5_14_6_2() {
        // when
        /* TRTC */
        o06005_difficulty_1.fillTrivialRowsAndColumns();

        /* 3 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().colourOverlappingFieldsInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 12 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().colourOverlappingFieldsInColumn(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 1 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowSequencesRangesWhenMetColouredField(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 10 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().correctColumnSequencesWhenMetColouredField(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 5 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().placeXsRowAtUnreachableFields(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 14 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().placeXsColumnAtUnreachableFields(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 6 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().placeXsAroundLongestSequencesInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 2 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowRangeIndexesIfXOnWay(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        // then
        List<List<List<Integer>>> rowSequencesRangesAfterCorrectingWhenMetColouredFields = o06005_difficulty_1.getRowsSequencesRanges();
        assertThat(rowSequencesRangesAfterCorrectingWhenMetColouredFields).isEqualTo(Arrays.asList(
                Arrays.asList(Arrays.asList(2, 2), Arrays.asList(2, 6), Arrays.asList(6, 6)),
                Arrays.asList(Arrays.asList(0, 0), Arrays.asList(2, 2), Arrays.asList(4, 4),  Arrays.asList(6, 6), Arrays.asList(8, 8)),
                Arrays.asList(Arrays.asList(0, 0), Arrays.asList(2, 6), Arrays.asList(8, 8)),
                List.of(Arrays.asList(0, 8)),
                List.of(Arrays.asList(0, 8)),
                Arrays.asList(Arrays.asList(0, 6), Arrays.asList(8, 8)),
                List.of(Arrays.asList(0, 8)),
                List.of(Arrays.asList(0, 9)),
                Arrays.asList(Arrays.asList(0, 6), Arrays.asList(6, 8)),
                Arrays.asList(Arrays.asList(1, 2), Arrays.asList(6, 7))
        ));
    }

    @Test
    @DisplayName("o06005-TRTC+3+12+1+10+5+14+6+2+3")
    void should_TRTC_3_12_1_10_5_14_6_2_3() {
        // given
        List<List<String>> expectedBoard = NonogramBoards.expectedNonogramBoardTRTC_3_12_1_10_5_14_6_2_3;

        // when
        /* TRTC */
        o06005_difficulty_1.fillTrivialRowsAndColumns();

        /* 3 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().colourOverlappingFieldsInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 12 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().colourOverlappingFieldsInColumn(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 1 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowSequencesRangesWhenMetColouredField(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 10 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().correctColumnSequencesWhenMetColouredField(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 5 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().placeXsRowAtUnreachableFields(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 14 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().placeXsColumnAtUnreachableFields(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 6 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().placeXsAroundLongestSequencesInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 2 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowRangeIndexesIfXOnWay(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 3 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().colourOverlappingFieldsInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        // then
        assertThat(o06005_difficulty_1.getNonogramSolutionBoard()).isEqualTo(expectedBoard);
    }

    @Test
    @DisplayName("o06005-TRTC+3+12+1+10+5+14+6+2+3+10")
    void should_TRTC_3_12_1_10_5_14_6_2_3_10() {
        // when
        /* TRTC */
        o06005_difficulty_1.fillTrivialRowsAndColumns();

        /* 3 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().colourOverlappingFieldsInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 12 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().colourOverlappingFieldsInColumn(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 1 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowSequencesRangesWhenMetColouredField(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 10 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().correctColumnSequencesWhenMetColouredField(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 5 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().placeXsRowAtUnreachableFields(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 14 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().placeXsColumnAtUnreachableFields(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 6 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().placeXsAroundLongestSequencesInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 2 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowRangeIndexesIfXOnWay(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 3 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().colourOverlappingFieldsInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 10 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().correctColumnSequencesWhenMetColouredField(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        // then
        List<List<List<Integer>>> columnSequencesRangesAfterCorrectingWhenMetColouredFields = o06005_difficulty_1.getColumnsSequencesRanges();
        assertThat(columnSequencesRangesAfterCorrectingWhenMetColouredFields).isEqualTo(Arrays.asList(
                List.of(Arrays.asList(1, 7)),
                List.of(Arrays.asList(3, 9)),
                Arrays.asList(Arrays.asList(0, 7), Arrays.asList(9, 9)),
                List.of(Arrays.asList(2, 7)),
                List.of(Arrays.asList(0, 8)),
                List.of(Arrays.asList(2, 7)),
                List.of(Arrays.asList(0, 9)),
                Arrays.asList(Arrays.asList(3, 4), Arrays.asList(6, 7), Arrays.asList(9, 9)),
                List.of(Arrays.asList(1, 7)),
                List.of(Arrays.asList(7, 7))
        ));
    }

    @Test
    @DisplayName("o06005-TRTC+3+12+1+10+5+14+6+2+3+10+12")
    void should_TRTC_3_12_1_10_5_14_6_2_3_10_12() {
        // given
        List<List<String>> expectedBoard = NonogramBoards.expectedNonogramBoardTRTC_3_12_1_10_5_14_6_2_3_10_12;

        // when
        /* TRTC */
        o06005_difficulty_1.fillTrivialRowsAndColumns();

        /* 3 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().colourOverlappingFieldsInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 12 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().colourOverlappingFieldsInColumn(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 1 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowSequencesRangesWhenMetColouredField(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 10 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().correctColumnSequencesWhenMetColouredField(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 5 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().placeXsRowAtUnreachableFields(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 14 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().placeXsColumnAtUnreachableFields(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 6 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().placeXsAroundLongestSequencesInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 2 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowRangeIndexesIfXOnWay(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 3 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().colourOverlappingFieldsInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 10 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().correctColumnSequencesWhenMetColouredField(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 12 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().colourOverlappingFieldsInColumn(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        // then
        assertThat(o06005_difficulty_1.getNonogramSolutionBoard()).isEqualTo(expectedBoard);
    }

    @Test
    @DisplayName("o06005-TRTC+3+12+1+10+5+14+6+2+3+10+12+0")
    void should_TRTC_3_12_1_10_5_14_6_2_3_10_12_0() {
        // when
        /* TRTC */
        o06005_difficulty_1.fillTrivialRowsAndColumns();

        /* 3 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().colourOverlappingFieldsInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 12 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().colourOverlappingFieldsInColumn(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 1 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowSequencesRangesWhenMetColouredField(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 10 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().correctColumnSequencesWhenMetColouredField(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 5 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().placeXsRowAtUnreachableFields(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 14 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().placeXsColumnAtUnreachableFields(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 6 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().placeXsAroundLongestSequencesInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 2 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowRangeIndexesIfXOnWay(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 3 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().colourOverlappingFieldsInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 10 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().correctColumnSequencesWhenMetColouredField(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 12 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().colourOverlappingFieldsInColumn(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 0 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowSequencesRanges(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        // then
        List<List<List<Integer>>> rowsSequencesRangesAfterCorrectingRanges = o06005_difficulty_1.getRowsSequencesRanges();
        assertThat(rowsSequencesRangesAfterCorrectingRanges).isEqualTo(Arrays.asList(
                Arrays.asList(Arrays.asList(2, 2), Arrays.asList(4, 4), Arrays.asList(6, 6)),
                Arrays.asList(Arrays.asList(0, 0), Arrays.asList(2, 2), Arrays.asList(4, 4), Arrays.asList(6, 6), Arrays.asList(8, 8)),
                Arrays.asList(Arrays.asList(0, 0), Arrays.asList(2, 6), Arrays.asList(8, 8)),
                List.of(Arrays.asList(0, 8)),
                List.of(Arrays.asList(0, 8)),
                Arrays.asList(Arrays.asList(0, 6), Arrays.asList(8, 8)),
                List.of(Arrays.asList(0, 8)),
                List.of(Arrays.asList(0, 9)),
                Arrays.asList(Arrays.asList(0, 6), Arrays.asList(6, 8)),
                Arrays.asList(Arrays.asList(1, 2), Arrays.asList(6, 7))
        ));
    }

    @Test
    @DisplayName("o06005-TRTC+3+12+1+10+5+14+6+2+3+10+12+0+1")
    void should_TRTC_3_12_1_10_5_14_6_2_3_10_12_0_1() {
        // when
        /* TRTC */
        o06005_difficulty_1.fillTrivialRowsAndColumns();

        /* 3 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().colourOverlappingFieldsInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 12 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().colourOverlappingFieldsInColumn(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 1 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowSequencesRangesWhenMetColouredField(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 10 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().correctColumnSequencesWhenMetColouredField(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 5 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().placeXsRowAtUnreachableFields(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 14 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().placeXsColumnAtUnreachableFields(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 6 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().placeXsAroundLongestSequencesInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 2 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowRangeIndexesIfXOnWay(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 3 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().colourOverlappingFieldsInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 10 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().correctColumnSequencesWhenMetColouredField(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 12 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().colourOverlappingFieldsInColumn(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 0 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowSequencesRanges(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 1 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowSequencesRangesWhenMetColouredField(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        // then
        List<List<List<Integer>>> rowsSequencesRangesAfterCorrectingRanges = o06005_difficulty_1.getRowsSequencesRanges();
        assertThat(rowsSequencesRangesAfterCorrectingRanges).isEqualTo(Arrays.asList(
                Arrays.asList(Arrays.asList(2, 2), Arrays.asList(4, 4), Arrays.asList(6, 6)),
                Arrays.asList(Arrays.asList(0, 0), Arrays.asList(2, 2), Arrays.asList(4, 4), Arrays.asList(6, 6), Arrays.asList(8, 8)),
                Arrays.asList(Arrays.asList(0, 0), Arrays.asList(2, 6), Arrays.asList(8, 8)),
                List.of(Arrays.asList(0, 8)),
                List.of(Arrays.asList(0, 8)),
                Arrays.asList(Arrays.asList(0, 6), Arrays.asList(8, 8)),
                List.of(Arrays.asList(0, 8)),
                List.of(Arrays.asList(0, 9)),
                Arrays.asList(Arrays.asList(1, 1), Arrays.asList(6, 6)),
                Arrays.asList(Arrays.asList(1, 2), Arrays.asList(6, 7))
        ));
    }

    @Test
    @DisplayName("o06005-TRTC+3+12+1+10+5+14+6+2+3+10+12+0+1+3")
    void should_TRTC_3_12_1_10_5_14_6_2_3_10_12_0_1_3() {
        // given
        List<List<String>> expectedBoard = NonogramBoards.expectedNonogramBoardTRTC_3_12_1_10_5_14_6_2_3_10_12_0_1_3;

        // when
        /* TRTC */
        o06005_difficulty_1.fillTrivialRowsAndColumns();

        /* 3 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().colourOverlappingFieldsInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 12 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().colourOverlappingFieldsInColumn(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 1 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowSequencesRangesWhenMetColouredField(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 10 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().correctColumnSequencesWhenMetColouredField(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 5 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().placeXsRowAtUnreachableFields(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 14 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().placeXsColumnAtUnreachableFields(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 6 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().placeXsAroundLongestSequencesInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 2 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowRangeIndexesIfXOnWay(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 3 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().colourOverlappingFieldsInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 10 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().correctColumnSequencesWhenMetColouredField(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 12 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().colourOverlappingFieldsInColumn(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 0 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowSequencesRanges(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 1 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowSequencesRangesWhenMetColouredField(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 3 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().colourOverlappingFieldsInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        // then
        assertThat(o06005_difficulty_1.getNonogramSolutionBoard()).isEqualTo(expectedBoard);
    }

    @Test
    @DisplayName("o06005-TRTC+3+12+1+10+5+14+6+2+3+10+12+0+1+3+5-solved")
    void should_TRTC_3_12_1_10_5_14_6_2_3_10_12_0_1_3_5() {
        // given
        List<List<String>> expectedBoard = NonogramBoards.expectedNonogramBoardTRTC_3_12_1_10_5_14_6_2_3_10_12_0_1_3_5;

        // when
        /* TRTC */
        o06005_difficulty_1.fillTrivialRowsAndColumns();

        /* 3 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().colourOverlappingFieldsInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 12 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().colourOverlappingFieldsInColumn(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 1 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowSequencesRangesWhenMetColouredField(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 10 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().correctColumnSequencesWhenMetColouredField(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 5 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().placeXsRowAtUnreachableFields(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 14 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().placeXsColumnAtUnreachableFields(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 6 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().placeXsAroundLongestSequencesInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 2 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowRangeIndexesIfXOnWay(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 3 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().colourOverlappingFieldsInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 10 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().correctColumnSequencesWhenMetColouredField(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 12 */
        o06005_difficulty_1.copyLogicToNonogramColumnLogic();
        for (int columnIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getWidth() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramColumnLogic().colourOverlappingFieldsInColumn(columnIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramColumnLogic();

        /* 0 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowSequencesRanges(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 1 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().correctRowSequencesRangesWhenMetColouredField(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 3 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().colourOverlappingFieldsInRow(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        /* 5 */
        o06005_difficulty_1.copyLogicToNonogramRowLogic();
        for (int rowIdx : IntStream.rangeClosed(0, o06005_difficulty_1.getHeight() - 1).boxed().toList()) {
            o06005_difficulty_1.getNonogramRowLogic().placeXsRowAtUnreachableFields(rowIdx);
        }
        o06005_difficulty_1.copyLogicFromNonogramRowLogic();

        // then
        assertThat(o06005_difficulty_1.getNonogramSolutionBoard()).isEqualTo(expectedBoard);
        assertThat(o06005_difficulty_1.getCompletionPercentage()).isEqualTo(100.00);
    }

    static class NonogramBoards {
        // trivial rows/columns only
        static List<List<String>> expectedNonogramBoardTRTC = new ArrayList<>(Arrays.asList(
                List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"),
                List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"),
                List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"),
                List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"),
                List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"),
                List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"),
                List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O"),
                List.of("-", "-", "X", "-", "-", "-", "O", "-", "-", "-"),
                List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-")
        ));

        // trivial rows/columns + filling overlapping sequences in rows
        static List<List<String>> expectedNonogramBoardTRTC_3 = new ArrayList<>(Arrays.asList(
                List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"),
                List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"),
                List.of("-", "-", "O", "O", "O", "O", "O", "-", "-", "-"),
                List.of("-", "O", "O", "O", "O", "O", "O", "O", "O", "-"),
                List.of("-", "O", "O", "O", "O", "O", "O", "O", "O", "-"),
                List.of("-", "O", "O", "O", "O", "O", "O", "-", "-", "-"),
                List.of("-", "O", "O", "O", "O", "O", "O", "O", "O", "-"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O"),
                List.of("-", "-", "X", "-", "-", "-", "O", "-", "-", "-"),
                List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-")
        ));

        // trivial rows/columns + filling overlapping sequences in columns
        static List<List<String>> expectedNonogramBoardTRTC_12 = new ArrayList<>(Arrays.asList(
                List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"),
                List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"),
                List.of("-", "-", "O", "-", "O", "-", "O", "-", "-", "-"),
                List.of("O", "O", "O", "-", "O", "-", "O", "-", "O", "-"),
                List.of("O", "O", "O", "O", "O", "O", "O", "-", "O", "-"),
                List.of("O", "O", "O", "O", "O", "O", "O", "-", "O", "-"),
                List.of("O", "O", "O", "-", "O", "-", "O", "-", "O", "-"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O"),
                List.of("-", "-", "X", "-", "-", "-", "O", "-", "-", "-"),
                List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-")
        ));

        // trivial rows/columns + filling overlapping sequences in columns
        static List<List<String>> expectedNonogramBoardTRTC_3_12 = mergeBoards(expectedNonogramBoardTRTC_3, expectedNonogramBoardTRTC_12);

        static List<List<String>> expectedNonogramBoardTRTC_3_12_1_10_5 = new ArrayList<>(Arrays.asList(
                List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"),
                List.of("-", "-", "O", "-", "-", "-", "O", "-", "-", "-"),
                List.of("-", "-", "O", "O", "O", "O", "O", "-", "-", "-"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "X", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O"),
                List.of("-", "-", "X", "-", "-", "-", "O", "-", "-", "-"),
                List.of("X", "-", "O", "-", "X", "-", "O", "-", "X", "X")
        ));

        static List<List<String>> expectedNonogramBoardTRTC_3_12_1_10_14 = new ArrayList<>(Arrays.asList(
                List.of("X", "X", "O", "X", "-", "X", "O", "-", "X", "X"),
                List.of("-", "-", "O", "X", "-", "X", "O", "-", "-", "X"),
                List.of("-", "-", "O", "O", "O", "O", "O", "-", "-", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "-", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O"),
                List.of("-", "-", "X", "X", "-", "X", "O", "-", "-", "X"),
                List.of("-", "-", "O", "X", "-", "X", "O", "-", "-", "X")
        ));

        static List<List<String>> expectedNonogramBoardTRTC_3_12_1_10_5_14 = mergeBoards(expectedNonogramBoardTRTC_3_12_1_10_5, expectedNonogramBoardTRTC_3_12_1_10_14);

        static List<List<String>> expectedNonogramBoardTRTC_3_12_1_10_5_14_6 = new ArrayList<>(Arrays.asList(
                List.of("X", "X", "O", "X", "-", "X", "O", "X", "X", "X"),
                List.of("-", "X", "O", "X", "-", "X", "O", "X", "-", "X"),
                List.of("-", "X", "O", "O", "O", "O", "O", "X", "-", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "X", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O"),
                List.of("-", "-", "X", "X", "-", "X", "O", "X", "-", "X"),
                List.of("X", "-", "O", "X", "X", "X", "O", "-", "X", "X")
        ));

        static List<List<String>> expectedNonogramBoardTRTC_3_12_1_10_5_14_6_2_3 = new ArrayList<>(Arrays.asList(
                List.of("X", "X", "O", "X", "-", "X", "O", "X", "X", "X"),
                List.of("O", "X", "O", "X", "O", "X", "O", "X", "O", "X"),
                List.of("O", "X", "O", "O", "O", "O", "O", "X", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "X", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O"),
                List.of("-", "-", "X", "X", "-", "X", "O", "X", "-", "X"),
                List.of("X", "O", "O", "X", "X", "X", "O", "O", "X", "X")
        ));

        static List<List<String>> expectedNonogramBoardTRTC_3_12_1_10_5_14_6_2_3_10_12 = new ArrayList<>(Arrays.asList(
                List.of("X", "X", "O", "X", "-", "X", "O", "X", "X", "X"),
                List.of("O", "X", "O", "X", "O", "X", "O", "X", "O", "X"),
                List.of("O", "X", "O", "O", "O", "O", "O", "X", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "X", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O"),
                List.of("-", "O", "X", "X", "-", "X", "O", "X", "-", "X"),
                List.of("X", "O", "O", "X", "X", "X", "O", "O", "X", "X")
        ));

        static List<List<String>> expectedNonogramBoardTRTC_3_12_1_10_5_14_6_2_3_10_12_0_1_3 = new ArrayList<>(Arrays.asList(
                List.of("X", "X", "O", "X", "O", "X", "O", "X", "X", "X"),
                List.of("O", "X", "O", "X", "O", "X", "O", "X", "O", "X"),
                List.of("O", "X", "O", "O", "O", "O", "O", "X", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "X", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O"),
                List.of("-", "O", "X", "X", "-", "X", "O", "X", "-", "X"),
                List.of("X", "O", "O", "X", "X", "X", "O", "O", "X", "X")
                ));

        static List<List<String>> expectedNonogramBoardTRTC_3_12_1_10_5_14_6_2_3_10_12_0_1_3_5 = new ArrayList<>(Arrays.asList(
                List.of("X", "X", "O", "X", "O", "X", "O", "X", "X", "X"),
                List.of("O", "X", "O", "X", "O", "X", "O", "X", "O", "X"),
                List.of("O", "X", "O", "O", "O", "O", "O", "X", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "X", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "X"),
                List.of("O", "O", "O", "O", "O", "O", "O", "O", "O", "O"),
                List.of("X", "O", "X", "X", "X", "X", "O", "X", "X", "X"),
                List.of("X", "O", "O", "X", "X", "X", "O", "O", "X", "X")
        ));
    }

    public static List<List<String>> mergeBoards(List<List<String>> board_1, List<List<String>> board_2) {
        List<List<String>> mergedBoard = new ArrayList<>();

        for (int rowIdx = 0; rowIdx < board_1.size(); rowIdx++) {
            List<String> row1 = board_1.get(rowIdx);
            List<String> row2 = board_2.get(rowIdx);
            List<String> mergedRow = new ArrayList<>();

            for (int colIdx = 0; colIdx < row1.size(); colIdx++) {
                String field1 = row1.get(colIdx);
                String field2 = row2.get(colIdx);

                if (field1.equals(field2)) {
                    mergedRow.add(field1);
                } else if (field1.equals("-")) {
                    mergedRow.add(field2);
                } else if (field2.equals("-")) {
                    mergedRow.add(field1);
                } else {
                    mergedRow.add("-");
                }
            }

            mergedBoard.add(mergedRow);
        }

        return mergedBoard;
    }
}