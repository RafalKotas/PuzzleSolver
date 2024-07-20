package com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NonogramActionDefinition {
    private int index;

    private ActionEnum action;
}
