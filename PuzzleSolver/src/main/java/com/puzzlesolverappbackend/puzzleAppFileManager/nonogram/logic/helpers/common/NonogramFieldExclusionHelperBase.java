package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.common;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve.NonogramBoardAccessHelper;
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
