export type puzzleNames = "nonogram" | "sudoku" | "slitherlink" | "akari" | "hitori" | "architect"

export interface CommonState {
    selectedPuzzleName: puzzleNames
}

interface SetPuzzleName {
    type: typeof SET_PUZZLE_NAME,
    payload: {
        puzzleName : puzzleNames
    }
}

export const SET_PUZZLE_NAME = "SET_PUZZLE_NAME"

export type CommonActionTypes = SetPuzzleName