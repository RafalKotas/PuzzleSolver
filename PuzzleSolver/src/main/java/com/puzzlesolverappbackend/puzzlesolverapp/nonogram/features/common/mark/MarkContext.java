package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.features.common.mark;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MarkContext {
    private final BoardContext board;
    private final SequencesContext sequences;
    private final MarkOperationContext ops;
}


