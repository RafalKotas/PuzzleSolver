import { sudokuInformation, SET_SUDOKUS_LIST, SET_SELECTED_SUDOKU, ADD_SUDOKU_DETAIL, REMOVE_SUDOKU_DETAIL, CHANGE_SUDOKU_DETAIL, SAVE_TEMPORARY_SUDOKU, CHECK_AND_SET_CORRECTNESS, SET_CORRECTNESS, SET_CREATED_SUDOKU, COPY_SUDOKU_TO_CREATED_LIST, COPY_SUDOKU_TO_SOLVER_LIST } from "./types"

export const CopySudokuToCreatedList = (sudoku: sudokuInformation) => {
    return {
        type: COPY_SUDOKU_TO_CREATED_LIST,
        payload: {
            sudoku
        }
    }
}

export const CopySudokuToSolverList = (sudoku: sudokuInformation) => {
    return {
        type: COPY_SUDOKU_TO_SOLVER_LIST,
        payload: {
            sudoku
        }
    }
}

export const SetSudokusList = (sudokusList : sudokuInformation[]) => {
    return {
        type: SET_SUDOKUS_LIST,
        payload: {
            sudokusList
        }
    }
}

export const SetSelectedSudoku = (selectedSudoku : sudokuInformation) => {
    return {
        type: SET_SELECTED_SUDOKU,
        payload: {
            selectedSudoku
        }
    }
}

export const SetCreatedSudoku = (createdSudoku : sudokuInformation) => {
    return {
        type: SET_CREATED_SUDOKU,
        payload: {
            createdSudoku
        }
    }
}

export const AddSudokuDetail = (detail : keyof sudokuInformation) => {
    return {
        type: ADD_SUDOKU_DETAIL,
        payload: {
            detail
        }
    }
}

export const RemoveSudokuDetail = (detail : keyof sudokuInformation) => {
    return {
        type: REMOVE_SUDOKU_DETAIL,
        payload: {
            detail
        }
    }
}

export const ChangeSudokuDetail = (detail : keyof sudokuInformation, value : string | number | number[] | number[][]) => {
    return {
        type: CHANGE_SUDOKU_DETAIL,
        payload: {
            detail,
            value
        }
    }
}

export const SaveTemporarySudoku = () => {
    return {
        type: SAVE_TEMPORARY_SUDOKU
    }
}

export const CheckAndSetCorrectness = (sudokuBoard: number[][]) => {
    return {
        type: CHECK_AND_SET_CORRECTNESS,
        payload: {
            sudokuBoard
        }
    }
}

export const SetCorrectness = (correctness : -1 | 0 | 1) => {
    return {
        type: SET_CORRECTNESS,
        payload: {
            correctness
        }
    }
}