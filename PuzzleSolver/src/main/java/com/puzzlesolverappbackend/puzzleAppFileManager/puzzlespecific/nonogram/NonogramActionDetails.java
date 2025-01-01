package com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram;

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
