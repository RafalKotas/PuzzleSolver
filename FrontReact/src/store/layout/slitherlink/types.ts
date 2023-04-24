export const minEdgeWidth = 5
export const maxEdgeWidth = 15

interface SetBoardCellSize {
    type: typeof SET_SLITHERLINK_CELL_SIZE,
    payload: {
        cellSize: number
    }
}

export const SET_SLITHERLINK_CELL_SIZE = "SET_SLITHERLINK_CELL_SIZE"

export type SlitherlinkLayoutActionTypes = SetBoardCellSize

export interface SlitherlinkLayoutState {
    cellSize: number,
    edgeWidth: number
}