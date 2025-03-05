package com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.rowactions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.NonogramCreatorUtils.generateEmptyBoard;
import static com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.rowactions.MixedActionsHelper.findValidSequencesIdsMergingToRight;
import static com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.rowactions.MixedActionsHelper.getColouredSequencesRangesInRowInRangeOnRight;
import static org.assertj.core.api.Assertions.assertThat;

class MixedActionsHelperTest {

    @Test
    @DisplayName("Should collect coloured ranges(from test columnIdx 21 to seqId 2 in NonogramRowLogicTest)")
    void getColouredSequencesRangesInRowInRangeOnRightExample() {
        // given
        List<List<String>> board = generateEmptyBoard(35, 40);
        int ROW_TO_TEST = 8;
        int colouredColumnAfterX = 21;
        int maxSequenceLength = 4;
        List<String> boardRowToTest = new ArrayList<>(Arrays.asList(
                "O", "O", "O", "X", "X",
                "X", "-", "-", "-", "-",
                "-", "X", "O", "O", "O",
                "X", "X", "X", "X", "X",
                "X", "O", "-", "-", "O",
                "O", "-", "-", "-", "-",
                "-", "-", "-", "-", "-",
                "-", "-", "-", "-", "-"
        ));
        board.set(ROW_TO_TEST, boardRowToTest);

        // when
        List<List<Integer>> colouredSequencesRangesInRowInRangeOnRight = getColouredSequencesRangesInRowInRangeOnRight(board,
                ROW_TO_TEST, colouredColumnAfterX, maxSequenceLength);


        // then
        assertThat(colouredSequencesRangesInRowInRangeOnRight).isEqualTo(List.of(List.of(24, 25)));
    }

    @Test
    @DisplayName("Should find valid sequencesIds that could merge to right")
    void findValidSequencesIdsMergingToRightExample() {
        // given
        List<Integer> sequencesIds = List.of(2, 3, 4);
        List<Integer> expectedLengths = List.of(1, 3, 4);
        int colouredColumnAfterX = 21;
        List<List<Integer>> coloredSequences = List.of(List.of(24, 25));

        // when
        List<Integer> validSequencesIdsMergingToRight = findValidSequencesIdsMergingToRight(sequencesIds, expectedLengths, colouredColumnAfterX, coloredSequences);


        // then
        assertThat(validSequencesIdsMergingToRight).isNotEmpty()
                .hasSize(1)
                .containsExactly(2);
    }
}