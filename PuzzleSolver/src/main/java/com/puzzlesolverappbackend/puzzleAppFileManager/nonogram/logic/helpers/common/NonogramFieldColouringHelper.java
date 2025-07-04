package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.common;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.Field;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve.NonogramBoardAccessHelper;

import java.util.List;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.model.NonogramConstants.COLOURED_FIELD;

public class NonogramFieldColouringHelper {

    private final List<List<String>> nonogramSolutionBoard;
    private final List<List<String>> nonogramSolutionBoardWithMarks;
    private final NonogramBoardAccessHelper boardAccessHelper;

    public NonogramFieldColouringHelper(List<List<String>> board,
                                        List<List<String>> boardWithMarks,
                                        NonogramBoardAccessHelper accessHelper) {
        this.nonogramSolutionBoard = board;
        this.nonogramSolutionBoardWithMarks = boardWithMarks;
        this.boardAccessHelper = accessHelper;
    }

    public void colourFieldAtGivenPosition(Field fieldToColour, String mask) {
        int rowIdx = fieldToColour.getRowIdx();
        int colIdx = fieldToColour.getColumnIdx();

        if (boardAccessHelper.areFieldIndexesValid(fieldToColour)) {
            String current = nonogramSolutionBoardWithMarks.get(rowIdx).get(colIdx);
            nonogramSolutionBoard.get(rowIdx).set(colIdx, COLOURED_FIELD);
            nonogramSolutionBoardWithMarks.get(rowIdx).set(colIdx, getUpdatedFieldWithMarks(current, mask));
        }
    }

    public String getUpdatedFieldWithMarks(String currentField, String mask) {
        StringBuilder updatedField = new StringBuilder();
        for (int i = 0; i < currentField.length(); i++) {
            if (currentField.charAt(i) == '-') {
                updatedField.append(mask.charAt(i));
            } else {
                updatedField.append(currentField.charAt(i));
            }
        }

        return updatedField.toString();
    }
}

