package com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.NonogramState.buildInitialEmptyNonogramState;
import static com.puzzlesolverappbackend.puzzleAppFileManager.utils.NonogramCreatorUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

public class NonogramColumnLogicTest {

    NonogramColumnLogic nonogramColumnLogic;

    @BeforeEach
    void setUp() {
        nonogramColumnLogic = new NonogramColumnLogic();
        nonogramColumnLogic.setNonogramState(buildInitialEmptyNonogramState());
    }

    void prepareNonogramColumnLogic(int HEIGHT, int WIDTH) {
        nonogramColumnLogic.setNonogramSolutionBoard(generateEmptyBoard(HEIGHT, WIDTH));
        nonogramColumnLogic.setNonogramSolutionBoardWithMarks(generateEmptyBoardWithMarks(HEIGHT, WIDTH));

        nonogramColumnLogic.setColumnsSequences(generateEmptyColumnSequencesLengths(WIDTH));
        nonogramColumnLogic.setColumnsFieldsNotToInclude(generateEmptyColumnsFieldsNotToInclude(WIDTH));
        nonogramColumnLogic.setColumnsSequencesIdsNotToInclude(generateEmptyColumnsSequencesNotToInclude(WIDTH));

        nonogramColumnLogic.setRowsSequences(generateEmptyRowSequencesLengths(HEIGHT));
        nonogramColumnLogic.setRowsFieldsNotToInclude(generateEmptyRowsFieldsNotToInclude(HEIGHT));

        nonogramColumnLogic.setColumnsSequencesRanges(
                generateEmptyColumnSequencesRanges(WIDTH)
        );
    }

    @Test
    @DisplayName(value = "Should not place X too short empty sequences - o08398 column 4")
    void shouldNotPlaceXAtTooShortEmptySequences() {
        // given
        int COLUMN_TO_TEST = 4;
        int HEIGHT = 35;
        int WIDTH = 30;
        prepareNonogramColumnLogic(HEIGHT, WIDTH);

        nonogramColumnLogic.excludeSequenceInColumn(COLUMN_TO_TEST, 0);
        nonogramColumnLogic.excludeSequenceInColumn(COLUMN_TO_TEST, 1);
        nonogramColumnLogic.excludeSequenceInColumn(COLUMN_TO_TEST, 2);
        nonogramColumnLogic.excludeSequenceInColumn(COLUMN_TO_TEST, 3);
        nonogramColumnLogic.excludeSequenceInColumn(COLUMN_TO_TEST, 4);
        nonogramColumnLogic.excludeSequenceInColumn(COLUMN_TO_TEST, 5);
        nonogramColumnLogic.excludeSequenceInColumn(COLUMN_TO_TEST, 6);

        nonogramColumnLogic.setColumnSequencesRanges(COLUMN_TO_TEST, List.of(
                List.of(0, 1), List.of(3, 15), List.of(17, 21), List.of(23, 23), List.of(25, 25), List.of(26, 27), List.of(29, 34)
        ));
        nonogramColumnLogic.setColumnSequencesLengths(COLUMN_TO_TEST, List.of(1, 13, 5, 1, 1, 1, 6));
        List<String> columnBeforeActionMade = List.of("-", "-", "X", "O", "O",
                "O", "O", "O", "O", "O",
                "O", "O", "O", "O", "O",
                "O", "X", "O", "O", "O",
                "O", "O", "X", "O", "X",
                "-", "-", "-", "X", "O",
                "O", "O", "O", "O", "O");
        nonogramColumnLogic.setNonogramSolutionBoardColumn(COLUMN_TO_TEST, columnBeforeActionMade);

        // when
        nonogramColumnLogic.placeXsColumnAtTooShortEmptySequences(COLUMN_TO_TEST);

        // then
        assertThat(nonogramColumnLogic.getNonogramBoardColumn(COLUMN_TO_TEST)).isEqualTo(columnBeforeActionMade);
    }

    @Test
    @DisplayName(value = "Should place X if O will create too long coloured sequence - o07959 column 6")
    void shouldPlaceXIfOWillCreateTooLongColumnColouredSequence() {
        // given
        int COLUMN_TO_TEST = 7;
        int HEIGHT = 20;
        int WIDTH = 20;
        prepareNonogramColumnLogic(HEIGHT, WIDTH);
        nonogramColumnLogic.setColumnSequencesRanges(COLUMN_TO_TEST, List.of(
                List.of(0, 5), List.of(4, 8), List.of(10, 15), List.of(18, 19)
        ));
        nonogramColumnLogic.setColumnSequencesLengths(COLUMN_TO_TEST, List.of(3, 2, 6, 2));
        List<String> columnBeforeActionMade = List.of("-", "-", "-", "-", "O",
                "O", "-", "-", "-", "X",
                "O", "O", "O", "O", "O",
                "O", "X", "X", "O", "O");
        nonogramColumnLogic.setNonogramSolutionBoardColumn(COLUMN_TO_TEST, columnBeforeActionMade);

        // when
        nonogramColumnLogic.placeXsColumnIfOWillMergeNearFieldsToTooLongColouredSequence(COLUMN_TO_TEST);

        // then
        List<String> expectedColumnAfterActionMade = List.of("-", "-", "-", "-", "O",
                "O", "X", "-", "-", "X",
                "O", "O", "O", "O", "O",
                "O", "X", "X", "O", "O");
        assertThat(nonogramColumnLogic.getNonogramBoardColumn(COLUMN_TO_TEST)).isEqualTo(expectedColumnAfterActionMade);
    }
}
