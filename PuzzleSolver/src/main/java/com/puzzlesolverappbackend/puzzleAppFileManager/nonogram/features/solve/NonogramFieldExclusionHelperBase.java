package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.features.solve;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.core.solver.NonogramBoardAccessHelper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NonogramFieldExclusionHelperBase {

    private final NonogramBoardAccessHelper boardAccessHelper;

    public NonogramFieldExclusionHelperBase(NonogramBoardAccessHelper boardAccessHelper) {
        this.boardAccessHelper = boardAccessHelper;
    }
}
