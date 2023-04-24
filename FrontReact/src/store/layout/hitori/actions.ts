import { SET_HITORI_CELL_SIZE, SET_MAXIMUM_HITORI_CELL_SIZE, SET_MINIMUM_HITORI_CELL_SIZE } from "./types"

export const SetHitoriCellSize = (cellSize: number) => {
    return {
        type: SET_HITORI_CELL_SIZE,
        payload: {
            cellSize
        }
    }
}

export const SetMinimumHitoriCellsize = (cellSize: number) => {
    return {
        typye: SET_MINIMUM_HITORI_CELL_SIZE,
        payload: {
            cellSize
        }
    }
}

export const SetMaximumHitoriCellsize = (cellSize: number) => {
    return {
        typye: SET_MAXIMUM_HITORI_CELL_SIZE,
        payload: {
            cellSize
        }
    }
}