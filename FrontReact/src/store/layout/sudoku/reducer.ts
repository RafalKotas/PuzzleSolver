import { Reducer } from "redux"
import { SudokuLayoutActionTypes, SudokuLayoutState, SET_SUDOKU_CELL_SIZE } from "./types"

export const initialState: SudokuLayoutState = {

    cellSize: 45,
    cellBorder: 2

}

export const sudokuLayoutReducer: Reducer<SudokuLayoutState, SudokuLayoutActionTypes> = (state = initialState, action : SudokuLayoutActionTypes) => {
    switch (action.type) {
        case SET_SUDOKU_CELL_SIZE:
            return {
                ...state,
                cellSize: action.payload.cellSize
            }
        default:
            return state
      }
}