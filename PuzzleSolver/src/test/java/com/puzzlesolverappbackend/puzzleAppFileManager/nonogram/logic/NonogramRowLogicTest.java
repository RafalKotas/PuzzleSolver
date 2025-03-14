package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.NonogramActionDetails;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.enums.NonogramSolveAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.NonogramState.buildInitialEmptyNonogramState;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramCreatorUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;

class NonogramRowLogicTest {

    NonogramRowLogic nonogramRowLogic;

    @BeforeEach
    void setUp() {
        nonogramRowLogic = new NonogramRowLogic();
        nonogramRowLogic.setNonogramState(buildInitialEmptyNonogramState());
    }

    void prepareNonogramRowLogic(int HEIGHT, int WIDTH) {
        nonogramRowLogic.setNonogramSolutionBoard(generateEmptyBoard(HEIGHT, WIDTH));
        nonogramRowLogic.setNonogramSolutionBoardWithMarks(generateEmptyBoardWithMarks(HEIGHT, WIDTH));

        nonogramRowLogic.setColumnsSequences(generateEmptyColumnSequencesLengths(WIDTH));
        nonogramRowLogic.setColumnsFieldsNotToInclude(generateEmptyColumnsFieldsNotToInclude(WIDTH));
        nonogramRowLogic.setColumnsSequencesIdsNotToInclude(generateEmptyColumnsSequencesNotToInclude(WIDTH));

        nonogramRowLogic.setRowsSequences(generateEmptyRowSequencesLengths(HEIGHT));
        nonogramRowLogic.setRowsFieldsNotToInclude(generateEmptyRowsFieldsNotToInclude(HEIGHT));
        nonogramRowLogic.setRowsSequencesIdsNotToInclude(generateEmptyRowsSequencesNotToInclude(HEIGHT));

        nonogramRowLogic.setRowsSequencesRanges(
                generateEmptyRowSequencesRanges(HEIGHT)
        );
    }

    @Test
    @DisplayName(value = "Should place X too short empty sequences - o05765 row 25")
    void shouldPlaceXAtTooShortEmptySequences() {
        // given
        int ROW_TO_TEST = 25;
        int HEIGHT = 30;
        int WIDTH = 30;
        prepareNonogramRowLogic(HEIGHT, WIDTH);

        nonogramRowLogic.excludeSequenceInRow(ROW_TO_TEST, 0);

        nonogramRowLogic.setRowSequencesRanges(ROW_TO_TEST, List.of(
                List.of(3, 3), List.of(6, 11), List.of(12, 20), List.of(18, 29)
        ));
        nonogramRowLogic.setRowSequencesLengths(ROW_TO_TEST, List.of(1, 5, 1, 3));
        List<String> rowBeforeActionMade = List.of("X", "X", "X", "O", "X",
                "X", "-", "O", "O", "O",
                "O", "-", "-", "-", "-",
                "-", "-", "-", "-", "-",
                "O", "X", "-", "-", "X",
                "-", "-", "-", "-", "-");
        nonogramRowLogic.setNonogramSolutionBoardRow(ROW_TO_TEST, rowBeforeActionMade);

        // when
        nonogramRowLogic.placeXsRowAtTooShortEmptySequences(ROW_TO_TEST);

        // then
        List<String> expectedRowAfterActionMade = List.of("X", "X", "X", "O", "X",
                "X", "-", "O", "O", "O",
                "O", "-", "-", "-", "-",
                "-", "-", "-", "-", "-",
                "O", "X", "X", "X", "X",
                "-", "-", "-", "-", "-");
        assertThat(nonogramRowLogic.getNonogramSolutionBoard().get(ROW_TO_TEST)).isEqualTo(expectedRowAfterActionMade);
    }

    @Test
    @DisplayName(value = "Should place X if O will create too long coloured sequence [no changes] - o09983 row 10")
    void shouldPlaceXIfOWillCreateTooLongColumnColouredSequence() {
        // given
        int ROW_TO_TEST = 10;
        int HEIGHT = 35;
        int WIDTH = 25;
        prepareNonogramRowLogic(HEIGHT, WIDTH);
        nonogramRowLogic.setRowSequencesRanges(ROW_TO_TEST, List.of(
                List.of(0, 6), List.of(3, 8), List.of(10, 19), List.of(13, 21), List.of(15, 24)
        ));
        nonogramRowLogic.setRowSequencesLengths(ROW_TO_TEST, List.of(2, 6, 2, 1, 2));
        List<String> rowBeforeActionMade = List.of("-", "-", "X", "O", "O",
                "O", "O", "O", "O", "X",
                "-", "-", "-", "-", "-",
                "-", "-", "X", "-", "-",
                "-", "-", "-", "-", "-"
        );
        nonogramRowLogic.setNonogramSolutionBoardRow(ROW_TO_TEST, rowBeforeActionMade);

        // when
        nonogramRowLogic.placeXsRowIfONearXWillBeginTooLongPossibleColouredSequence(ROW_TO_TEST);

        // then
        List<String> expectedRowAfterActionMade = List.of("-", "-", "X", "O", "O",
                "O", "O", "O", "O", "X",
                "-", "-", "-", "-", "-",
                "-", "-", "X", "-", "-",
                "-", "-", "-", "-", "-"
        );
        assertThat(nonogramRowLogic.getNonogramSolutionBoard().get(ROW_TO_TEST)).isEqualTo(expectedRowAfterActionMade);
    }

    @Test
    @DisplayName(value = "Should match field at columnIdx 18 to seqId 3 when prevent extending to excess length and correct it range")
    void shouldPreventExtendingColouredSequenceToExcessLengthInRowToLeftWithUniqueSequenceMatch() {
        // given
        int ROW_TO_TEST = 8;
        int HEIGHT = 35;
        int WIDTH = 40;
        prepareNonogramRowLogic(HEIGHT, WIDTH);
        nonogramRowLogic.setRowSequencesRanges(8, new ArrayList<>(Arrays.asList(
                List.of(0, 13), List.of(2, 18), List.of(13, 27), List.of(18, 29), List.of(25, 33), List.of(37, 39)
        )));
        nonogramRowLogic.getRowsSequences().set(8, new ArrayList<>(Arrays.asList(1, 4, 3, 1, 3, 3)));
        List<String> rowBeforeActionMade = new ArrayList<>(Arrays.asList(
                "-", "-", "-", "-", "-",
                "-", "-", "-", "-", "-",
                "-", "-", "-", "-", "O",
                "O", "-", "-", "O", "X",
                "X", "X", "X", "X", "X",
                "O", "O", "O", "X", "-",
                "-", "-", "-", "-", "X",
                "X", "X", "O", "O", "O"
        ));
        nonogramRowLogic.setNonogramSolutionBoardRow(ROW_TO_TEST, rowBeforeActionMade);

        // when
        nonogramRowLogic.preventExtendingColouredSequenceToExcessLengthInRow(ROW_TO_TEST);

        // then
        List<String> expectedRowAfterActionMade = new ArrayList<>(Arrays.asList(
                "-", "-", "-", "-", "-",
                "-", "-", "-", "-", "-",
                "-", "-", "-", "-", "O",
                "O", "-", "X", "O", "X",
                "X", "X", "X", "X", "X",
                "O", "O", "O", "X", "-",
                "-", "-", "-", "-", "X",
                "X", "X", "O", "O", "O"
        ));
        List<List<Integer>> expectedRowSequencesRangesAfterActionMade = List.of(
                List.of(0, 13), List.of(2, 18), List.of(13, 27), List.of(18, 18), List.of(25, 33), List.of(37, 39)
        );
        assertThat(nonogramRowLogic.getNonogramSolutionBoard().get(ROW_TO_TEST)).isEqualTo(expectedRowAfterActionMade);
        assertThat(nonogramRowLogic.getRowsSequencesRanges().get(ROW_TO_TEST)).isEqualTo(expectedRowSequencesRangesAfterActionMade);
    }

    @Test
    @DisplayName(value = "Should match field at columnIdx 21 to seqId 2 when prevent extending to excess length and correct its range")
    void shouldPreventExtendingColouredSequenceToExcessLengthInRowToRightWithUniqueSequenceMatch() {
        // given
        int ROW_TO_TEST = 8;
        int HEIGHT = 35;
        int WIDTH = 40;
        prepareNonogramRowLogic(HEIGHT, WIDTH);
        nonogramRowLogic.setRowSequencesRanges(ROW_TO_TEST, new ArrayList<>(Arrays.asList(
                List.of(0, 2), List.of(6, 14), List.of(10, 21), List.of(12, 26), List.of(21, 37), List.of(26, 39)
        )));
        nonogramRowLogic.getRowsSequences().set(ROW_TO_TEST, new ArrayList<>(Arrays.asList(3, 3, 1, 3, 4, 1)));
        List<String> rowBeforeActionMade = new ArrayList<>(Arrays.asList(
                "O", "O", "O", "X", "X",
                "X", "-", "-", "-", "-",
                "-", "X", "O", "O", "O",
                "X", "X", "X", "X", "X",
                "X", "O", "-", "-", "O",
                "O", "-", "-", "-", "-",
                "-", "-", "-", "-", "-",
                "-", "-", "-", "-", "-"
        ));
        nonogramRowLogic.setNonogramSolutionBoardRow(ROW_TO_TEST, rowBeforeActionMade);

        // when
        nonogramRowLogic.preventExtendingColouredSequenceToExcessLengthInRow(ROW_TO_TEST);

        // then
        List<String> expectedRowAfterActionMade = new ArrayList<>(Arrays.asList(
                "O", "O", "O", "X", "X",
                "X", "-", "-", "-", "-",
                "-", "X", "O", "O", "O",
                "X", "X", "X", "X", "X",
                "X", "O", "X", "-", "O",
                "O", "-", "-", "-", "-",
                "-", "-", "-", "-", "-",
                "-", "-", "-", "-", "-"
        ));
        List<List<Integer>> expectedRowSequencesRangesAfterActionMade = List.of(
                List.of(0, 2), List.of(6, 14), List.of(21, 21), List.of(12, 26), List.of(21, 37), List.of(26, 39)
        );
        assertThat(nonogramRowLogic.getNonogramSolutionBoard().get(ROW_TO_TEST)).isEqualTo(expectedRowAfterActionMade);
        assertThat(nonogramRowLogic.getRowsSequencesRanges().get(ROW_TO_TEST)).isEqualTo(expectedRowSequencesRangesAfterActionMade);
    }

    @Test
    @DisplayName(value = "Should correct row 25th sequenceRange when start from field near coloured field nonogram o08004 even if X on way.")
    void correctRowSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence() {
        // given
        int ROW_TO_TEST = 25;
        int HEIGHT = 30;
        int WIDTH = 25;
        prepareNonogramRowLogic(HEIGHT, WIDTH);
        nonogramRowLogic.setRowSequencesRanges(ROW_TO_TEST, new ArrayList<>(Arrays.asList(
                List.of(0, 1), List.of(4, 6), List.of(6, 10), List.of(13, 14)
        )));
        nonogramRowLogic.getRowsSequences().set(ROW_TO_TEST, new ArrayList<>(Arrays.asList(2, 2, 2, 2)));
        List<String> rowBeforeActionMade = new ArrayList<>(Arrays.asList(
                "O", "O", "X", "X", "-",
                "O", "-", "X", "X", "-",
                "-", "X", "X", "O", "O",
                "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X"
        ));
        nonogramRowLogic.setNonogramSolutionBoardRow(ROW_TO_TEST, rowBeforeActionMade);

        // when
        nonogramRowLogic.correctRowSequencesRangesWhenStartFromEdgeIndexWillCreateTooLongSequence(ROW_TO_TEST);

        // then
        List<List<Integer>> expectedRowSequencesRangesAfterActionMade = List.of(
                List.of(0, 1), List.of(4, 6), List.of(7, 10), List.of(13, 14)
        );
        assertThat(nonogramRowLogic.getRowsSequencesRanges().get(ROW_TO_TEST)).isEqualTo(expectedRowSequencesRangesAfterActionMade);

        NonogramActionDetails expectedActionAdded = new NonogramActionDetails(anyInt(), NonogramSolveAction.CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY);
        assertThat(nonogramRowLogic.getActionsToDoList().contains(
                expectedActionAdded
        ));
    }
}