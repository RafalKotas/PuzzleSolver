import { SET_AKARI_CELL_SIZE } from "./types"

export const SetCellSize = (cellSize: number) => {
    return {
        type: SET_AKARI_CELL_SIZE,
        payload: {
            cellSize
        }
    }
}