package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.features.common.clearing;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.solver.NonogramBoardAccessHelper;

import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.model.NonogramConstants.EMPTY_FIELD;
import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.model.NonogramConstants.EMPTY_FIELD_MARKED_BOARD;

public class NonogramFieldClearingHelper {

    private final List<List<String>> nonogramSolutionBoard;
    private final List<List<String>> nonogramSolutionBoardWithMarks;
    private final NonogramBoardAccessHelper boardAccessHelper;

    public NonogramFieldClearingHelper(List<List<String>> board,
                                       List<List<String>> boardWithMarks,
                                       NonogramBoardAccessHelper accessHelper) {
        this.nonogramSolutionBoard = board;
        this.nonogramSolutionBoardWithMarks = boardWithMarks;
        this.boardAccessHelper = accessHelper;
    }

    public void clearField(Field fieldToClear) {
        int fieldColIdx = fieldToClear.getColumnIdx();
        int fieldRowIdx = fieldToClear.getRowIdx();
        if (boardAccessHelper.areFieldIndexesValid(fieldToClear)) {
            this.nonogramSolutionBoard.get(fieldRowIdx).set(fieldColIdx, EMPTY_FIELD);
            this.nonogramSolutionBoardWithMarks.get(fieldRowIdx).set(fieldColIdx, EMPTY_FIELD_MARKED_BOARD);
        }
    }
}
