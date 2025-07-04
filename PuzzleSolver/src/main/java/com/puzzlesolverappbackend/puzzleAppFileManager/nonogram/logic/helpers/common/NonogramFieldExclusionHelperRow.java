package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.common;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.Field;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve.NonogramBoardAccessHelper;
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
}
