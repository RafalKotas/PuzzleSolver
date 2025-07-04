package com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.helpers.solve;

import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.enums.NonogramSolveAction;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.logic.Field;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.model.NonogramActionDetails;
import com.puzzlesolverappbackend.puzzleAppFileManager.nonogram.utils.ActionDependencyMap;

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

        if (actionsToDo == null) {
            System.out.println("abc");
        }

        for (NonogramSolveAction actionToDo : actionsToDo) {
            if (actionToDo.isRowAction()) {
                actionsToDoList.add(new NonogramActionDetails(rowIdx, actionToDo, actionTriggered, false));
            } else {
                actionsToDoList.add(new NonogramActionDetails(columnIdx, actionToDo, actionTriggered, false));
            }
        }
    }

    public void scheduleActionsBasedOnColumn(int columnIdx, NonogramSolveAction actionTriggered, NonogramBoardAccessHelper boardAccessHelper) {
        List<NonogramSolveAction> actionsToDo = ActionDependencyMap.actionDependencies.get(actionTriggered);

        if (!boardAccessHelper.isColumnIndexValid(columnIdx)) return;

        for (NonogramSolveAction actionToDo : actionsToDo) {
            if (!actionToDo.isRowAction()) {
                actionsToDoList.add(new NonogramActionDetails(columnIdx, actionToDo, actionTriggered, false));
            }
        }
    }

}
