package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.features.common.xplacement;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.solver.NonogramBoardAccessHelper;

import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.model.NonogramConstants.X_FIELD;

public class NonogramFieldPlacingXHelper {

    private final List<List<String>> nonogramSolutionBoard;
    private final List<List<String>> nonogramSolutionBoardWithMarks;
    private final NonogramBoardAccessHelper boardAccessHelper;

    public NonogramFieldPlacingXHelper(List<List<String>> board,
                                        List<List<String>> boardWithMarks,
                                        NonogramBoardAccessHelper accessHelper) {
        this.nonogramSolutionBoard = board;
        this.nonogramSolutionBoardWithMarks = boardWithMarks;
        this.boardAccessHelper = accessHelper;
    }

    public void placeXAtGivenField(Field fieldToPlaceX, boolean exclude) {

        int fieldRowIdx = fieldToPlaceX.getRowIdx();
        int fieldColumnIdx = fieldToPlaceX.getColumnIdx();

        if (boardAccessHelper.areFieldIndexesValid(fieldToPlaceX)) {
            this.nonogramSolutionBoard.get(fieldRowIdx).set(fieldColumnIdx, X_FIELD);
            this.nonogramSolutionBoardWithMarks.get(fieldRowIdx).set(fieldColumnIdx, X_FIELD.repeat(4));

//            if (exclude) {
//                excludeFieldLogicSpecific(fieldToPlaceX);
//            }
        }
    }

    public void placeXAtGivenFields(List<Field> x_fields) {
        x_fields.forEach(field -> placeXAtGivenField(field, true));
    }
}
