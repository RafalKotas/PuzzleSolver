import { modes } from "../common"

export const emptyBoard = [
    [0, 0, 0, 0, 0, 0, 0, 0, 0], 
    [0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0]
]

export interface sudokuInformation {
    source : string,
    year?: string,
    month?: string,
    difficulty : number,
    filled: number,

    board: Array<Array<number>>,
    label : string,
    value : string
}

export const emptySudoku : sudokuInformation = {
    source : "",
    difficulty : 0.0,
    filled : 0,

	board: emptyBoard,

    label : "",
    value : ""    
}

export interface SudokuDataState {

    selectedSudoku: sudokuInformation,
    sudokusList : sudokuInformation[],
    
    createdSudoku: sudokuInformation,
    createdSudokusList : sudokuInformation[],

    sudokuCorrect: -1 | 0 | 1

    mode: modes,
    editMode: boolean,

    detailsSet: string[]
}

export const possibleSudokuOptions = ["source", "difficulty", "width", "height"]

export const COPY_SUDOKU_TO_CREATED_LIST = "COPY_SUDOKU_TO_CREATED_LIST"
export const COPY_SUDOKU_TO_SOLVER_LIST = "COPY_SUDOKU_TO_SOLVER_LIST"

export const SET_SELECTED_SUDOKU = "SET_SELECTED_SUDOKU"
export const SET_SUDOKUS_LIST = "SET_SUDOKUS_LIST"

export const SET_CREATED_SUDOKU = "SET_CREATED_SUDOKU"
export const ADD_SUDOKU_DETAIL = "ADD_SUDOKU_DETAIL"
export const REMOVE_SUDOKU_DETAIL = "REMOVE_SUDOKU_DETAIL"
export const CHANGE_SUDOKU_DETAIL = "CHANGE_SUDOKU_DETAIL"

export const ADD_TEMPORARY_SUDOKU = "ADD_TEMPORARY_SUDOKU"
export const SAVE_TEMPORARY_SUDOKU = "SAVE_TEMPORARY_SUDOKU"

export const CHECK_AND_SET_CORRECTNESS = "CHECK_AND_SET_CORRECTNESS"
export const SET_CORRECTNESS = "SET_CORRECTNESS"

interface CopySudokuToCreatedList {
    type: typeof COPY_SUDOKU_TO_CREATED_LIST,
    payload: {
        sudoku: sudokuInformation
    }
}

interface CopySudokuToSolverList {
    type: typeof COPY_SUDOKU_TO_SOLVER_LIST,
    payload: {
        sudoku: sudokuInformation
    }
}

interface SetSudokusList {
    type: typeof SET_SUDOKUS_LIST,
    payload: {
        sudokusList: sudokuInformation[]
    }
}

interface SetSelectedSudoku {
    type: typeof SET_SELECTED_SUDOKU,
    payload: {
        selectedSudoku: sudokuInformation
    }
}

interface AddSudokuDetail {
    type: typeof ADD_SUDOKU_DETAIL,
    payload: {
        detail : keyof sudokuInformation
    }
}

interface SetCreatedSudoku {
    type: typeof SET_CREATED_SUDOKU,
    payload: {
        createdSudoku: sudokuInformation
    }
}

interface RemoveSudokuDetail {
    type: typeof REMOVE_SUDOKU_DETAIL,
    payload: {
        detail : keyof sudokuInformation
    }
}

interface ChangeSudokuStringDetail {
    type: typeof CHANGE_SUDOKU_DETAIL,
    payload: {
        detail: keyof sudokuInformation,
        value: string
    }
}

interface SaveTemporarySudoku {
    type: typeof SAVE_TEMPORARY_SUDOKU
}

interface CheckAndSetCorrectness {
    type: typeof CHECK_AND_SET_CORRECTNESS,
    payload: {
        sudokuBoard: number[][]
    }
}

interface SetCorrectness {
    type: typeof SET_CORRECTNESS,
    payload: {
        correctness: -1 | 0 | 1
    }
}

export type SudokuDataActionTypes = CopySudokuToCreatedList | CopySudokuToSolverList |  
SetSudokusList | SetSelectedSudoku | SetCreatedSudoku | 
AddSudokuDetail | RemoveSudokuDetail | ChangeSudokuStringDetail | 
 SaveTemporarySudoku | CheckAndSetCorrectness | SetCorrectness