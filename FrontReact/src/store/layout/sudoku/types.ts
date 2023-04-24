export const minCellSize = 16
export const maxCellSize = 70

interface SetBoardCellSize {
    type: typeof SET_SUDOKU_CELL_SIZE,
    payload: {
        cellSize: number
    }
}

export const SET_SUDOKU_CELL_SIZE = "SET_SUDOKU_CELL_SIZE"

export type SudokuLayoutActionTypes = SetBoardCellSize

export interface SudokuLayoutState {
    cellSize: number,
    cellBorder: number
}