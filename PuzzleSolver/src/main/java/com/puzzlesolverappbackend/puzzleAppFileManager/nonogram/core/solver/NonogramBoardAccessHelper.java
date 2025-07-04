package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.solver;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.model.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.solver.BoardUtils.isFieldColoured;

public class NonogramBoardAccessHelper {

    private final NonogramLogic logic;

    public NonogramBoardAccessHelper(NonogramLogic logic) {
        this.logic = logic;
    }

    public NonogramBoardAccessHelper(List<List<String>> board) {
        this.logic = new NonogramLogic();
        logic.setNonogramSolutionBoard(board);
    }

    public List<String> getColumn(int columnIdx) {
        List<String> column = new ArrayList<>();
        for (List<String> row : logic.getNonogramSolutionBoard()) {
            column.add(row.get(columnIdx));
        }
        return column;
    }

    public List<String> getColumnCopy(int columnIdx) {
        return new ArrayList<>(getColumn(columnIdx));
    }

    public List<String> getRowCopy(int rowIdx) {
        return new ArrayList<>(logic.getNonogramSolutionBoard().get(rowIdx));
    }

    public String getField(int rowIdx, int colIdx) {
        return logic.getNonogramSolutionBoard().get(rowIdx).get(colIdx);
    }

    public boolean isRowIndexValid(int rowIdx) {
        return rowIdx >= 0 && rowIdx < logic.getNonogramSolutionBoard().size();
    }

    public boolean isColumnIndexValid (int columnIdx) {
        return columnIdx >= 0 && (!logic.getNonogramSolutionBoard().isEmpty() && columnIdx < logic.getNonogramSolutionBoard().get(0).size());
    }

    public boolean areFieldIndexesValid (Field fieldToValidate) {
        int fieldRowIdx = fieldToValidate.getRowIdx();
        int fieldColIdx = fieldToValidate.getColumnIdx();
        return isRowIndexValid(fieldRowIdx) && isColumnIndexValid(fieldColIdx);
    }

    public boolean isRowRangeColoured(int rowIdx, List<Integer> columnRange) {
        int start = columnRange.get(0);
        int end = columnRange.get(1);

        return IntStream.rangeClosed(start, end)
                .mapToObj(colIdx -> new Field(rowIdx, colIdx))
                .allMatch(field -> isFieldColoured(logic.getNonogramSolutionBoard(), field));
    }

    public boolean isColumnRangeColoured(int columnIdx, List<Integer> rowRange) {
        int start = rowRange.get(0);
        int end = rowRange.get(1);

        return IntStream.rangeClosed(start, end)
                .mapToObj(rowIdx -> new Field(rowIdx, columnIdx))
                .allMatch(field -> isFieldColoured(logic.getNonogramSolutionBoard(), field));
    }
}

