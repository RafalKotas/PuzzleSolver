import { SET_ARCHITECT_CELL_SIZE } from "./types"

export const SetCellSize = (cellSize: number) => {
    return {
        type: SET_ARCHITECT_CELL_SIZE,
        payload: {
            cellSize
        }
    }
}