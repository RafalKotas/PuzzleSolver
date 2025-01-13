package com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram;

import com.puzzlesolverappbackend.puzzleAppFileManager.constants.NonogramSolveAction;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class NonogramActionDetails {
    private int index;

    private NonogramSolveAction actionName;
}
