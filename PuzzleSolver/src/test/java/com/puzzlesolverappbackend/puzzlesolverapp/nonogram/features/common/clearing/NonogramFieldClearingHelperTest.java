package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.clearing;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.NonogramBoardAccessHelper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramConstants.EMPTY_FIELD;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramConstants.EMPTY_FIELD_MARKED_BOARD;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NonogramFieldClearingHelperTest {

    @Test
    void shouldClearFieldInBoardAndBoardWithMarks() {

        // given
        List<List<String>> board = createBoardWithSingleColouredField();
        List<List<String>> boardWithMarks = createBoardWithSingleMark();
        NonogramBoardAccessHelper accessHelper = new NonogramBoardAccessHelper(board);
        NonogramFieldClearingHelper clearingHelper = new NonogramFieldClearingHelper(board, boardWithMarks, accessHelper);

        Field fieldToClear = new Field(0, 0);

        // when
        clearingHelper.clearField(fieldToClear);

        // then
        assertEquals(EMPTY_FIELD, board.get(0).get(0));
        assertEquals(EMPTY_FIELD_MARKED_BOARD, boardWithMarks.get(0).get(0));
    }

    @Test
    void shouldNotClearFieldIfIndexesAreInvalid() {
        // given
        List<List<String>> board = createBoardWithSingleColouredField();
        List<List<String>> boardWithMarks = createBoardWithSingleMark();
        NonogramBoardAccessHelper accessHelper = new NonogramBoardAccessHelper(board);
        NonogramFieldClearingHelper clearingHelper = new NonogramFieldClearingHelper(board, boardWithMarks, accessHelper);

        Field invalidField = new Field(5, 5);

        String originalBoardValue = board.get(0).get(0);
        String originalMarkValue = boardWithMarks.get(0).get(0);

        // when
        clearingHelper.clearField(invalidField);

        // then
        assertEquals(originalBoardValue, board.get(0).get(0));
        assertEquals(originalMarkValue, boardWithMarks.get(0).get(0));
    }

    private List<List<String>> createBoardWithSingleColouredField() {
        List<List<String>> board = new ArrayList<>();
        board.add(new ArrayList<>(List.of("X")));
        return board;
    }

    private List<List<String>> createBoardWithSingleMark() {
        List<List<String>> boardWithMarks = new ArrayList<>();
        boardWithMarks.add(new ArrayList<>(List.of("--C-")));
        return boardWithMarks;
    }
}