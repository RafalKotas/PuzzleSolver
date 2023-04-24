export const minCellSize = 28
export const maxCellSize = 42

interface SetBoardCellSize {
    type: typeof SET_ARCHITECT_CELL_SIZE,
    payload: {
        cellSize: number
    }
}

export const SET_ARCHITECT_CELL_SIZE = "SET_ARCHITECT_CELL_SIZE"

export type ArchitectLayoutActionTypes = SetBoardCellSize

export interface ArchitectLayoutState {
    cellSize: number,
    cellBorder: number
}