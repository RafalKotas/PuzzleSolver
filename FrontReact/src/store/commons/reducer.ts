import { Reducer } from "redux"
import { CommonActionTypes, CommonState, SET_PUZZLE_NAME } from "./types"

export const initialCommonState: CommonState = {
    selectedPuzzleName: "nonogram"
}

export const commonReducer: Reducer<CommonState, CommonActionTypes> = (state = initialCommonState, action : CommonActionTypes) => {
    switch (action.type) {
        case SET_PUZZLE_NAME:
            return {
                ...state,
                selectedPuzzleName: action.payload.puzzleName
            }
        default:
            return state
      }
}

export const selectedPuzzleName = (state: CommonState) => state.selectedPuzzleName