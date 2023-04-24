export interface SudokuLogicState {
    sudokuBoard: Array<Array<number>>,
    sudokuBoardEditMode: Array<Array<number>>,
    availableNumbers: Array<Array<Array<number>>>,
    markedCells: Array<markedCell>
    filled: number,
    createStep: string,
    consideredRows: number[],
    consideredColumns: number[],
    markOption: markOptions
}

export interface rowsAndColumnsRange {
    rowsRange: number[],
    columnsRange: number[]
}

export const nonIncludeRange = [-1, -1]

export type markOptions = "None" | "One possible option in cell" | "One available place for digit in row" | "One available place for digit in column" | "One available place for digit in square 3x3"

export interface sudokuCellCoords {
    rowIdx: number,
    columnIdx: number
}

export interface markedCell {
    digit: number,
    cellCoords: sudokuCellCoords
}

export const availableNumbersForEmptyBoard = Array.from({ length: 9 }, (_, rowIdx) => (
    Array.from({length: 9}, (__, colIdx) => (
        Array.from({length: 9}, (___, numIdx) => (
            numIdx + 1
        ))
    ))
))

export const availableNumbersForPreviewMode = Array.from({ length: 9 }, (_, rowIdx) => (
    Array.from({length: 9}, (__, colIdx) => (
        Array.from({length: 9}, (___, numIdx) => (
            0
        ))
    ))
))

// FOR SOLVER
export const INIT_SUDOKU_BOARD_SOLVER = "INIT_SUDOKU_BOARD_SOLVER"
export const MARK_ONLY_POSSIBLE_OPTION = "MARK_ONLY_POSSIBLE_OPTION"
export const MARK_ONLY_PLACE_IN_ROWS = "MARK_ONLY_PLACE_IN_ROWS"
export const MARK_ONLY_PLACE_IN_COLUMNS = "MARK_ONLY_PLACE_IN_COLUMNS"
export const MARK_ONLY_PLACE_IN_3X3SQUARES = "MARK_ONLY PLACE_IN_3X3SQUARES"
export const MARK_ACTION = "MARK_ACTION"
export const CHANGE_RANGES = "CHANGE_RANGES"
export const CHANGE_CONSIDERED_INDEX = "CHANGE_CONSIDERED_INDEX"
export const INSERT_DIGIT_INTO_CELL = "INSERT_DIGIT_INTO_CELL"

interface InitializeSudokuBoardSolver {
    type: typeof INIT_SUDOKU_BOARD_SOLVER,
    payload: {
        sudokuBoard: Array<Array<number>>
    }
}

interface MarkOnlyPossibleOption {
    type: typeof MARK_ONLY_POSSIBLE_OPTION,
    payload: rowsAndColumnsRange
}

interface MarkOnlyPlaceInRows {
    type: typeof MARK_ONLY_PLACE_IN_ROWS,
    payload: rowsAndColumnsRange
}

interface MarkOnlyPlaceInColumns {
    type: typeof MARK_ONLY_PLACE_IN_COLUMNS,
    payload: rowsAndColumnsRange
}

interface MarkOnlyPlaceIn3x3Squares {
    type: typeof MARK_ONLY_PLACE_IN_3X3SQUARES,
    payload: rowsAndColumnsRange
}

interface MarkAction {
    type: typeof MARK_ACTION,
    payload: {
        rowsAndColumnsRange :  rowsAndColumnsRange,
        markOption: markOptions
    }
}

interface ChangeRanges {
    type: typeof CHANGE_RANGES,
    payload: {
        rowsRange : number[],
        columnsRange : number[]
    }
}

interface ChangeConsideredIndex {
    type: typeof CHANGE_CONSIDERED_INDEX,
    payload: {
        index : number,
        rowOrColumn: "ROW" | "COLUMN",
        startOrEnd: "START" | "END" 
    }
}

interface InsertDigitIntoCell {
    type: typeof INSERT_DIGIT_INTO_CELL,
    payload: {
        digit: number,
        coords: {
            rowIdx : number,
            columnIdx: number
        }
    }
}

type SolverActionTypes = InitializeSudokuBoardSolver | MarkOnlyPossibleOption | MarkOnlyPlaceInRows | MarkOnlyPlaceInColumns | MarkOnlyPlaceIn3x3Squares | 
MarkAction | ChangeRanges | ChangeConsideredIndex | InsertDigitIntoCell

// FOR EDIT
export const INIT_SUDOKU_BOARD_EDIT = "INIT_SUDOKU_BOARD_EDIT"
export const INSERT_DIGIT_EDIT_MODE = "INSERT_DIGIT_EDIT_MODE"
export const REMOVE_DIGIT_EDIT_MODE = "REMOVE_DIGIT_EDIT_MODE"
export const ADD_MARKED_CELL = "ADD_MARKED_CELL"
export const FILL_MARKED_CELLS = "FILL_MARKED_CELLS"
export const RESET_MARKED_CELLS = "RESET_MARKED_CELLS"
export const RESET_SUDOKU_EDIT_DATA = "RESET_SUDOKU_EDIT_DATA"
export const TOGGLE_PREVIEW_MODE = "TOGGLE_PREVIEW_MODE"
export const TOGGLE_EDIT_MODE = "TOGGLE_EDIT_MODE"

type EditActionTypes = InitializeSudokuBoardEdit | InsertDigitIntoCellEditMode | RemoveDigitFromCellEditMode | AddMarkedCell | 
FillMarkedCells | ResetMarkedCells | ResetSudokuEditData | TogglePreviewMode | ToggleEditMode

interface InitializeSudokuBoardEdit {
    type: typeof INIT_SUDOKU_BOARD_EDIT,
    payload: {
        sudokuBoard: Array<Array<number>>
    }
}

interface InsertDigitIntoCellEditMode {
    type: typeof INSERT_DIGIT_EDIT_MODE,
    payload: {
        digit: number,
        coords: {
            rowIdx : number,
            columnIdx: number
        }
    }
}

interface RemoveDigitFromCellEditMode {
    type: typeof REMOVE_DIGIT_EDIT_MODE,
    payload: {
        digit: number,
        coords: {
            rowIdx : number,
            columnIdx: number
        }
    }
}

interface AddMarkedCell {
    type: typeof ADD_MARKED_CELL,
    payload: {
        digit: number,
        cellCoords: sudokuCellCoords
    }
}

interface FillMarkedCells {
    type: typeof FILL_MARKED_CELLS
}

interface ResetMarkedCells {
    type: typeof RESET_MARKED_CELLS
}

interface ResetSudokuEditData {
    type: typeof RESET_SUDOKU_EDIT_DATA
}

interface TogglePreviewMode {
    type: typeof TOGGLE_PREVIEW_MODE
}

interface ToggleEditMode {
    type: typeof TOGGLE_EDIT_MODE,
    payload: {
        sudokuBoardForEditMode: number[][]
    }
}

export type SudokuLogicActionTypes = SolverActionTypes | EditActionTypes