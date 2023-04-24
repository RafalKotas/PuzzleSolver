import {markOptions, rowsAndColumnsRange, sudokuCellCoords, 
    CHANGE_CONSIDERED_INDEX, CHANGE_RANGES, INSERT_DIGIT_EDIT_MODE, INSERT_DIGIT_INTO_CELL, 
    MARK_ACTION, MARK_ONLY_PLACE_IN_3X3SQUARES, MARK_ONLY_PLACE_IN_COLUMNS, MARK_ONLY_PLACE_IN_ROWS, MARK_ONLY_POSSIBLE_OPTION, 
    REMOVE_DIGIT_EDIT_MODE, RESET_SUDOKU_EDIT_DATA, ADD_MARKED_CELL, FILL_MARKED_CELLS, RESET_MARKED_CELLS, TOGGLE_EDIT_MODE, TOGGLE_PREVIEW_MODE, INIT_SUDOKU_BOARD_SOLVER, INIT_SUDOKU_BOARD_EDIT} from "./types"

export const InitializeSudokuBoardSolver = (sudokuBoard : Array<Array<number>>) => {
    return {
        type: INIT_SUDOKU_BOARD_SOLVER,
        payload: {
            sudokuBoard
        }
    }
}

export const InitializeSudokuBoardEdit = (sudokuBoard : Array<Array<number>>) => {
    return {
        type: INIT_SUDOKU_BOARD_EDIT,
        payload: {
            sudokuBoard
        }
    }
}

export const ResetSudokuEditData = () => {
    return {
        type: RESET_SUDOKU_EDIT_DATA
    }
}

export const InsertDigitIntoCell = (digit : number, coords : sudokuCellCoords) => {
    return {
        type: INSERT_DIGIT_INTO_CELL,
        payload: {
            digit,
            coords
        }
    }
}

export const InsertDigitIntoCellEditMode = (digit : number, coords : sudokuCellCoords) => {
    return {
        type: INSERT_DIGIT_EDIT_MODE,
        payload: {
            digit,
            coords
        }
    }
}

export const RemoveDigitFromCellEditMode = (digit : number, coords : sudokuCellCoords) => {
    return {
        type: REMOVE_DIGIT_EDIT_MODE,
        payload: {
            digit,
            coords
        }
    }
}

export const TogglePreviewMode = () => {
    return {
        type: TOGGLE_PREVIEW_MODE
    }
}

export const ToggleEditMode = (sudokuBoardForEditMode: number[][]) => {
    return {
        type: TOGGLE_EDIT_MODE,
        payload: {
            sudokuBoardForEditMode
        }
    }
}

export const ResetMarkedCells = () => {
    return {
        type: RESET_MARKED_CELLS
    }
}

export const AddMarkedCell = (digit: number, cellCoords: sudokuCellCoords) => {
    return {
        type: ADD_MARKED_CELL,
        payload: {
            digit,
            cellCoords
        }
    }
}

export const FillMarkedCells = () => {
    return {
        type: FILL_MARKED_CELLS
    }
}

export const MarkAction = (rowsAndColumnsRange: rowsAndColumnsRange, markOption : markOptions) => {
    return {
        type: MARK_ACTION,
        payload: {
            rowsAndColumnsRange,
            markOption
        }
    }
}

export const MarkCellsWithOnlyOnePossibleOption = (rowsRange: number[], columnsRange: number[]) => {
    return {
        type: MARK_ONLY_POSSIBLE_OPTION,
        payload: {
            rowsRange,
            columnsRange
        }
    }
}

export const MarkCellsWithOnlyOneAvailablePlaceForDigitInRows = (rowsRange: number[], columnsRange: number[]) => {
    return {
        type: MARK_ONLY_PLACE_IN_ROWS,
        payload: {
            rowsRange,
            columnsRange
        }
    }
}

export const MarkCellsWithOnlyOneAvailablePlaceForDigitInColumns = (rowsRange: number[], columnsRange: number[]) => {
    return {
        type: MARK_ONLY_PLACE_IN_COLUMNS,
        payload: {
            rowsRange,
            columnsRange
        }
    }
}

export const MarkCellsWithOnlyOneAvailablePlaceForDigitIn3x3Squares = (rowsRange: number[], columnsRange: number[]) => {
    return {
        type: MARK_ONLY_PLACE_IN_3X3SQUARES,
        payload: {
            rowsRange,
            columnsRange
        }
    }
}

export const ChangeRowStartConsideredIndex = (updatedRowStartIndex : number) => {
    return {
        type: CHANGE_CONSIDERED_INDEX,
        payload: {
            index: updatedRowStartIndex,
            rowOrColumn: "ROW",
            startOrEnd: "START"
        }
    }
}

export const ChangeRowEndConsideredIndex = (updatedRowEndIndex : number) => {
    return {
        type: CHANGE_CONSIDERED_INDEX,
        payload: {
            index: updatedRowEndIndex,
            rowOrColumn: "ROW",
            startOrEnd: "END"
        }
    }
}

export const ChangeColumnStartConsideredIndex = (updatedColumnStartIndex : number) => {
    return {
        type: CHANGE_CONSIDERED_INDEX,
        payload: {
            index: updatedColumnStartIndex,
            rowOrColumn: "COLUMN",
            startOrEnd: "START"
        }
    }
}

export const ChangeColumnEndConsideredIndex = (updatedColumnEndIndex : number) => {
    return {
        type: CHANGE_CONSIDERED_INDEX,
        payload: {
            index: updatedColumnEndIndex,
            rowOrColumn: "COLUMN",
            startOrEnd: "END"
        }
    }
}

export const SetRowsAndColumnsRanges = (rowsRange : number[], columnsRange : number[]) => {
    return {
        type: CHANGE_RANGES,
        payload: {
            rowsRange,
            columnsRange
        }
    }
}