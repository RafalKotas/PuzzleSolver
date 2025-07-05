package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.column;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.NonogramBoardAccessHelper;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.solve.NonogramFieldExclusionHelperBase;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class NonogramFieldExclusionHelperColumn extends NonogramFieldExclusionHelperBase {

    private final List<List<Integer>> columnsFieldsNotToInclude;

    public NonogramFieldExclusionHelperColumn(List<List<Integer>> columnsFieldsNotToInclude,
                                        NonogramBoardAccessHelper boardAccessHelper) {
        super(boardAccessHelper);
        this.columnsFieldsNotToInclude = columnsFieldsNotToInclude;
    }

    public void excludeFieldInColumn(Field fieldToExclude) {
        int col = fieldToExclude.getColumnIdx();
        int row = fieldToExclude.getRowIdx();

        if (this.getBoardAccessHelper().areFieldIndexesValid(fieldToExclude)
                && !columnsFieldsNotToInclude.get(col).contains(row)) {

            columnsFieldsNotToInclude.get(col).add(row);
            Collections.sort(columnsFieldsNotToInclude.get(col));
        }
    }
}
