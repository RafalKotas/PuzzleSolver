package com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.NonogramState.buildInitialEmptyNonogramState;
import static com.puzzlesolverappbackend.puzzleAppFileManager.utils.NonogramCreatorUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

public class NonogramColumnLogicTest {

    NonogramColumnLogic nonogramColumnLogic;

    int COLUMN_TO_TEST = 4;
    int HEIGHT = 35;
    int WIDTH = 30;

    @BeforeEach
    void setUp() {
        nonogramColumnLogic = new NonogramColumnLogic();
        nonogramColumnLogic.setNonogramState(buildInitialEmptyNonogramState());

        nonogramColumnLogic.setNonogramSolutionBoard(generateEmptyBoard(HEIGHT, WIDTH));
        nonogramColumnLogic.setNonogramSolutionBoardWithMarks(generateEmptyBoardWithMarks(HEIGHT, WIDTH));

        nonogramColumnLogic.setColumnsSequences(generateEmptyColumnSequencesLengths(WIDTH));
        nonogramColumnLogic.setColumnsFieldsNotToInclude(generateEmptyColumnsFieldsNotToInclude(WIDTH));

        nonogramColumnLogic.setRowsSequences(generateEmptyRowSequencesLengths(HEIGHT));
        nonogramColumnLogic.setRowsFieldsNotToInclude(generateEmptyRowsFieldsNotToInclude(HEIGHT));

        nonogramColumnLogic.setColumnsSequencesRanges(
                generateEmptyColumnSequencesRanges(WIDTH)
        );
    }

    @Test
    @Disabled
    @DisplayName(value = "Should not place X too short empty sequences - o08398 column 4")
    void shouldNotPlaceXAtTooShortEmptySequences() {
        // given
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
        nonogramColumnLogic.setNonogramBoardColumn(COLUMN_TO_TEST, columnBeforeActionMade);

        // when
        nonogramColumnLogic.placeXsColumnAtTooShortEmptySequences(COLUMN_TO_TEST);

        // then
        assertThat(nonogramColumnLogic.getNonogramBoardColumn(COLUMN_TO_TEST)).isEqualTo(columnBeforeActionMade);
    }
}
