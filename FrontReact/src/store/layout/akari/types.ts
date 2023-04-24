export const minCellSize = 16
export const maxCellSize = 70

interface SetBoardCellSize {
    type: typeof SET_AKARI_CELL_SIZE,
    payload: {
        cellSize: number
    }
}

export const SET_AKARI_CELL_SIZE = "SET_AKARI_CELL_SIZE"

export type AkariLayoutActionTypes = SetBoardCellSize

export interface AkariLayoutState {
    cellSize: number,
    cellBorder: number
}