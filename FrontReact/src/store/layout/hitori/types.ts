export const minCellSizeBiggestPuzzle = 20
export const maxCellSizeBiggestPuzzle = 36

interface SetBoardCellSize {
    type: typeof SET_HITORI_CELL_SIZE,
    payload: {
        cellSize: number
    }
}

interface SetMinimumHitoriCellSize {
    type: typeof SET_MINIMUM_HITORI_CELL_SIZE,
    payload: {
        cellSize: number
    }
}

interface SetMaximumHitoriCellSize {
    type: typeof SET_MAXIMUM_HITORI_CELL_SIZE,
    payload: {
        cellSize: number
    }
}

export const SET_HITORI_CELL_SIZE = "SET_HITORI_CELL_SIZE"
export const SET_MINIMUM_HITORI_CELL_SIZE = "SET_MINIMUM_HITORI_CELL_SIZE"
export const SET_MAXIMUM_HITORI_CELL_SIZE = "SET_MAXIMUM_HITORI_CELL_SIZE"

export type HitoriLayoutActionTypes = SetBoardCellSize | SetMinimumHitoriCellSize | SetMaximumHitoriCellSize

export interface HitoriLayoutState {
    cellSize: number,
    cellBorder: number,
    frameBorder: number,
    minCellSize: number,
    maxCellSize: number
}