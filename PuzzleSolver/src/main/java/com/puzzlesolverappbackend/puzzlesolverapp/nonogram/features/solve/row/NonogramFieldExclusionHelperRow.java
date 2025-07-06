package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.row;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.NonogramBoardAccessHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.NonogramFieldExclusionHelperBase;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class NonogramFieldExclusionHelperRow extends NonogramFieldExclusionHelperBase {

    private final List<List<Integer>> rowsFieldsNotToInclude;

    public NonogramFieldExclusionHelperRow(List<List<Integer>> rowsFieldsNotToInclude,
                                        NonogramBoardAccessHelper boardAccessHelper) {
        super(boardAccessHelper);
        this.rowsFieldsNotToInclude = rowsFieldsNotToInclude;
    }

    public void excludeFieldInRow(Field fieldToExclude) {
        int fieldRowIdx = fieldToExclude.getRowIdx();
        int fieldColIdx = fieldToExclude.getColumnIdx();
        if (this.getBoardAccessHelper().areFieldIndexesValid(fieldToExclude) &&
                !rowsFieldsNotToInclude.get(fieldRowIdx).contains(fieldColIdx)) {
            this.rowsFieldsNotToInclude.get(fieldRowIdx).add(fieldColIdx);
            Collections.sort(this.rowsFieldsNotToInclude.get(fieldRowIdx));
        }
    }

    public void removeFieldFromExcludedInRow(Field field) {
        int col = field.getColumnIdx();
        int row = field.getRowIdx();

        if (this.getBoardAccessHelper().areFieldIndexesValid(field)
                && rowsFieldsNotToInclude.get(row).contains(col)) {

            List<Integer> colsToRemove = rowsFieldsNotToInclude.get(row);
            colsToRemove.removeIf(value -> value == col);
        }
    }
}
