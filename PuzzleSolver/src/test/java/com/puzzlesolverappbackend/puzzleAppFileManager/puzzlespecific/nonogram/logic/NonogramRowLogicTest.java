package com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.NonogramState.buildInitialEmptyNonogramState;
import static com.puzzlesolverappbackend.puzzleAppFileManager.utils.NonogramCreatorUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

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
    @DisplayName(value = "Should place X if O will create too long coloured sequence - o09983 row 10")
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
}