// redux
import { Reducer } from "redux"
import { DisplayState } from "../../display"
import { calculateFilledFields } from "../../puzzleLogic/sudoku/logic"
import { SET_CORRECTNESS } from "../nonogram"
import { listIncludesFileName } from "./functions"

// sudokuData types
import { SudokuDataActionTypes, SudokuDataState, emptySudoku, 
    SET_SUDOKUS_LIST, SET_SELECTED_SUDOKU, ADD_SUDOKU_DETAIL, 
    REMOVE_SUDOKU_DETAIL, CHANGE_SUDOKU_DETAIL, SAVE_TEMPORARY_SUDOKU, 
    CHECK_AND_SET_CORRECTNESS, 
    SET_CREATED_SUDOKU,
    emptyBoard,
    COPY_SUDOKU_TO_CREATED_LIST,
    COPY_SUDOKU_TO_SOLVER_LIST} from "./types"

export const initialState: SudokuDataState = {
    
    selectedSudoku: {
        "source": "Logi",
        "year" : "2022",
        "month" : "06_logi-Mix",
        "difficulty" : 1,
        "filled" : 26,
        "board": [
            [0, 0, 0, 0, 0, 0, 7, 0, 0], 
            [0, 0, 0, 8, 0, 0, 0, 0, 1],
            [4, 0, 0, 7, 6, 1, 9, 8, 0],
            [0, 0, 0, 0, 0, 0, 2, 0, 4],
            [0, 5, 0, 2, 1, 0, 0, 7, 0],
            [3, 0, 0, 0, 0, 0, 5, 9, 0],
            [7, 8, 0, 0, 0, 5, 0, 0, 0],
            [5, 0, 0, 0, 0, 0, 3, 0, 0],
            [0, 0, 3, 0, 0, 6, 0, 0, 8]
        ],
        "label": "s02200.json",
        "value": "s02200.json",
    },
    sudokusList : [],
    
    createdSudoku: emptySudoku,
    createdSudokusList : [],

    detailsSet: [],

    sudokuCorrect: 0,

    mode: "READ",
    editMode: false
}

export const sudokuDataReducer: Reducer<SudokuDataState, SudokuDataActionTypes> = (state = initialState, action : SudokuDataActionTypes) => {
    switch (action.type) {
        case COPY_SUDOKU_TO_CREATED_LIST:
            let copyToCreatedSuccessfull = !listIncludesFileName(state.createdSudokusList, action.payload.sudoku) 
            return {
                ...state,
                createdSudokusList: copyToCreatedSuccessfull ? [...state.createdSudokusList, action.payload.sudoku] : state.createdSudokusList
            }
        case COPY_SUDOKU_TO_SOLVER_LIST:
            let copySuccessfull = !listIncludesFileName(state.sudokusList, action.payload.sudoku)
            return {
                ...state,
                sudokusList: copySuccessfull ? [...state.sudokusList, action.payload.sudoku] : state.sudokusList
            }
        case SET_SUDOKUS_LIST:
            return {
                ...state,
                sudokusList: action.payload.sudokusList
            }
        case SET_SELECTED_SUDOKU:
            return {
                ...state,
                selectedSudoku: action.payload.selectedSudoku
            }
        case SET_CREATED_SUDOKU:
            return {
                ...state,
                createdSudoku: action.payload.createdSudoku
            }
        case SAVE_TEMPORARY_SUDOKU:
            let sudokuToSave = state.createdSudoku
            sudokuToSave.board = emptyBoard
            sudokuToSave.filled = 0
            return {
                ...state,
                createdSudokusList: [...state.createdSudokusList, {
                    ...sudokuToSave,
                    label: "TemplateNo" + (state.createdSudokusList.length + 1) + ".json",
                    value: "TemplateNo" + (state.createdSudokusList.length + 1) + ".json"
                }]
            }
        case ADD_SUDOKU_DETAIL:
            return {
                ...state,
                detailsSet: state.detailsSet.includes(action.payload.detail) ? state.detailsSet : [...state.detailsSet, action.payload.detail]
            }
        case REMOVE_SUDOKU_DETAIL:
            return {
                ...state,
                detailsSet: state.detailsSet.filter((detail) => detail !== action.payload.detail)
            }
        case CHANGE_SUDOKU_DETAIL:
            return {
                ...state,
                createdSudoku: {
                    ...state.createdSudoku,
                    [action.payload.detail]: action.payload.value
                }
            }
        case CHECK_AND_SET_CORRECTNESS:
            // minimum digits to make sudoku solvable and deterministic
            let correctOrNot : 1 | 0 = (calculateFilledFields(action.payload.sudokuBoard) >= 17) ? 1 : 0
            return {
                ...state,
                sudokuCorrect: correctOrNot
            }
        case SET_CORRECTNESS:
            return {
                ...state,
                sudokuCorrect: action.payload.correctness
            }
        default:
            return state
      }
}

export const selectListFromMode = (displayState : DisplayState, sudokuDataState: SudokuDataState) => {
    return displayState.mode === "READ" ? sudokuDataState.sudokusList : sudokuDataState.createdSudokusList
}