import { SET_SUDOKU_CELL_SIZE } from "./types"

export const SetCellSize = (cellSize: number) => {
    return {
        type: SET_SUDOKU_CELL_SIZE,
        payload: {
            cellSize
        }
    }
}