import { puzzleNames, SET_PUZZLE_NAME } from "./types"

export const SetPuzzleName = (puzzleName: puzzleNames) => {
    return {
        type: SET_PUZZLE_NAME,
        payload: {
            puzzleName
        }
    }
}