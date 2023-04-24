import { SET_SLITHERLINK_CELL_SIZE } from "./types"

export const SetCellSize = (cellSize: number) => {
    return {
        type: SET_SLITHERLINK_CELL_SIZE,
        payload: {
            cellSize
        }
    }
}