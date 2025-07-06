package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver;

import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Field;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramActionDetails;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.actions.ActionDependencyMap;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.enums.NonogramSolveAction;

import java.util.List;

public class NonogramActionScheduler {

    private final List<NonogramActionDetails> actionsToDoList;

    public NonogramActionScheduler(List<NonogramActionDetails> actionsToDoList) {
        this.actionsToDoList = actionsToDoList;
    }

    public void scheduleActionsBasedOnField(Field field, NonogramSolveAction actionTriggered) {
        List<NonogramSolveAction> actionsToDo = ActionDependencyMap.actionDependencies.get(actionTriggered);

        int rowIdx = field.getRowIdx();
        int columnIdx = field.getColumnIdx();

        for (NonogramSolveAction actionToDo : actionsToDo) {
            if (actionToDo.isRowAction()) {
                actionsToDoList.add(new NonogramActionDetails(rowIdx, actionToDo, actionTriggered, false));
            } else {
                actionsToDoList.add(new NonogramActionDetails(columnIdx, actionToDo, actionTriggered, false));
            }
        }
    }

}
