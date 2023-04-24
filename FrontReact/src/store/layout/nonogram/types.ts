type dimensions = {
    width: number,
    height: number
}

export const maxCellSize = 24
export const minCellSize = 12

interface SetBoardCellSize {
    type: typeof SET_NONOGRAM_CELL_SIZE,
    payload: {
        cellSize: number
    }
}

export const SET_NONOGRAM_CELL_SIZE = "SET_NONOGRAM_CELL_SIZE"

export type NonogramLayoutActionTypes = SetBoardCellSize

export interface NonogramLayoutState {
    cellSize: number,
    cellBorder: number,
    bigSquareAdditionalBorder: number,
    boardDimensions: dimensions,
    sequencesSectionDimensions: dimensions
}