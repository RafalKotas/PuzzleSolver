package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.base.NonogramState;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.NonogramRowLogic.prepareNonogramRowLogic;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.rowactions.RowColourFieldsIfXWouldForceTooLongColouredFieldsSequenceHelpers.collectColouredSequencesRangesInRow;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.model.NonogramConstants.COLOURED_FIELD;
import static org.assertj.core.api.Assertions.assertThat;

class RowColourFieldsIfXWouldForceTooLongColouredFieldsSequenceHelpersTest {

    private NonogramRowLogic nonogramRowLogic;

    @Test
    void collectColouredSequencesRangesInRowTest() {
        // given
        int ROW_TO_TEST = 10;
        int HEIGHT = 30;
        int WIDTH = 25;
        nonogramRowLogic = prepareNonogramRowLogic(HEIGHT, WIDTH);
        nonogramRowLogic.setNonogramState(NonogramState.buildInitialEmptyNonogramState());

        nonogramRowLogic.setRowSequencesRanges(ROW_TO_TEST, List.of(
                List.of(0, 6), List.of(3, 9), List.of(6, 14), List.of(11, 21), List.of(16, 24)
        ));
        nonogramRowLogic.setRowSequencesLengths(ROW_TO_TEST, List.of(2, 2, 4, 4, 2));
        List<String> rowOnSolutionBoard = new ArrayList<>(Collections.nCopies(25, "-"));
        rowOnSolutionBoard.set(12, COLOURED_FIELD);
        rowOnSolutionBoard.set(16, COLOURED_FIELD);
        rowOnSolutionBoard.set(17, COLOURED_FIELD);
        nonogramRowLogic.setNonogramSolutionBoardRow(ROW_TO_TEST, rowOnSolutionBoard);

        // when
        List<List<Integer>> colouredSequences = collectColouredSequencesRangesInRow(nonogramRowLogic.getNonogramSolutionBoard(), ROW_TO_TEST);

        // then
        List<List<Integer>> expectedColouredSequences = List.of(List.of(12, 12), List.of(16, 17));
        assertThat(colouredSequences).isEqualTo(expectedColouredSequences);
    }
}