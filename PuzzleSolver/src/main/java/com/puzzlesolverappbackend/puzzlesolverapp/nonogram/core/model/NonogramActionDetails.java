package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class NonogramActionDetails {
    private int index;

    private NonogramSolveAction actionName;

    private NonogramSolveAction triggeringActionName;

    private boolean changedState;

    public static NonogramActionDetails copy(NonogramActionDetails original) {
        return new NonogramActionDetails(
                original.getIndex(),
                original.getActionName(),
                original.getTriggeringActionName(),
                original.isChangedState()
        );
    }
}
