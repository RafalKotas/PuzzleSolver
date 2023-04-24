// redux
import { Reducer } from "redux"
import { removeDigitOption, square3x3cellsIndices, addDigitOption, generateNumbersAllowedInCell, fillSudokuWithMarkedCells, calculateFilledFields, generateNumbersAllowedInEveryBoardCell } from "./logic"

import { ADD_MARKED_CELL, availableNumbersForEmptyBoard, availableNumbersForPreviewMode, CHANGE_CONSIDERED_INDEX, CHANGE_RANGES, FILL_MARKED_CELLS,
    INIT_SUDOKU_BOARD_EDIT, 
    INIT_SUDOKU_BOARD_SOLVER, 
    INSERT_DIGIT_EDIT_MODE, INSERT_DIGIT_INTO_CELL, MARK_ACTION, 
    REMOVE_DIGIT_EDIT_MODE, RESET_MARKED_CELLS, RESET_SUDOKU_EDIT_DATA, sudokuCellCoords, SudokuLogicActionTypes, SudokuLogicState, TOGGLE_EDIT_MODE, TOGGLE_PREVIEW_MODE } from "./types"

import commonFunctions from "../../../functions"
import { emptyBoard, emptySudoku } from "../../data/sudoku"

export const initialState: SudokuLogicState = {
    
    // solver-view purpose
    sudokuBoard: [
            [0, 0, 0, 0, 0, 0, 0, 0, 0], 
            [0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0]
    ],
    // edit-view purpose
    sudokuBoardEditMode: [
        [0, 0, 0, 0, 0, 0, 0, 0, 0], 
        [0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0]
    ],
    markedCells: [],
    consideredRows: [0, 0],
    consideredColumns: [-1, -1],
    filled: 0,
    createStep: "EDIT",
    markOption: "None",
    availableNumbers : availableNumbersForEmptyBoard
}

export const sudokuLogicReducer: Reducer<SudokuLogicState, SudokuLogicActionTypes> = (state = initialState, action : SudokuLogicActionTypes) => {
    switch (action.type) {
        case INIT_SUDOKU_BOARD_SOLVER:
            let sudokuBoardSolver = action.payload.sudokuBoard
            return {
                ...state,
                sudokuBoard: sudokuBoardSolver,
                filled: calculateFilledFields(sudokuBoardSolver)
            }
        case INSERT_DIGIT_INTO_CELL:
            let { rowIdx: insertRowIndex, columnIdx: insertColumnIndex } = action.payload.coords
            let digitToInsert = action.payload.digit
            let sudokuBoardWithNumberInserted = state.sudokuBoard

            sudokuBoardWithNumberInserted[insertRowIndex][insertColumnIndex] = digitToInsert

            let availableNumbersWithoutDigit = removeDigitOption(state.availableNumbers, digitToInsert, insertRowIndex, insertColumnIndex)
            availableNumbersWithoutDigit[insertRowIndex][insertColumnIndex] = []

            return {
                ...state,
                sudokuBoard: sudokuBoardWithNumberInserted,
                availableNumbers: availableNumbersWithoutDigit
            }
        case FILL_MARKED_CELLS:
            let sudokuBoardWithMarkedCellsFilled = fillSudokuWithMarkedCells(state.sudokuBoard, state.markedCells)
            let markedCellsCount = state.markedCells.length
            return {
                ...state,
                sudokuBoard: sudokuBoardWithMarkedCellsFilled,
                filled: state.filled + markedCellsCount,
                markedCells: []
            }
        case INIT_SUDOKU_BOARD_EDIT:
            let createdSudokuFromList = action.payload.sudokuBoard
            let availableNumbersForCurrentMode = state.createStep === "EDIT" ? 
                generateNumbersAllowedInEveryBoardCell(createdSudokuFromList) : availableNumbersForPreviewMode
            return {
                ...state,
                sudokuBoard: emptyBoard,
                sudokuBoardEditMode: createdSudokuFromList,
                availableNumbers: availableNumbersForCurrentMode,
                filled: calculateFilledFields(createdSudokuFromList)
            }
        case RESET_SUDOKU_EDIT_DATA:
            return {
                ...state,
                sudokuBoardEditMode: emptyBoard,
                sudokuBoard: emptyBoard,
                availableNumbers: availableNumbersForEmptyBoard
            }
        case INSERT_DIGIT_EDIT_MODE:
            let { rowIdx : insertEditModeRowIndex, columnIdx : insertEditModeColumnIndex } = action.payload.coords
            let digitToInsertEditMode = action.payload.digit
            let sudokuBoardEditModeWithNumberInserted = state.sudokuBoardEditMode

            sudokuBoardEditModeWithNumberInserted[ insertEditModeRowIndex][ insertEditModeColumnIndex ] = digitToInsertEditMode

            let availableNumbersWithoutDigitInsertMode = removeDigitOption(state.availableNumbers, digitToInsertEditMode, insertEditModeRowIndex, insertEditModeColumnIndex)
            availableNumbersWithoutDigitInsertMode[ insertEditModeRowIndex ][ insertEditModeColumnIndex ] = [digitToInsertEditMode]

            return {
                ...state,
                availableNumbers: generateNumbersAllowedInEveryBoardCell(state.sudokuBoardEditMode),
                sudokuBoardEditMode: sudokuBoardEditModeWithNumberInserted
            }
        case REMOVE_DIGIT_EDIT_MODE:
            let { rowIdx : removeRowIndex , columnIdx : removeColumnIndex } = action.payload.coords
            let digitToRemove = action.payload.digit

            let sudokuBoardWithDigitRemoved = state.sudokuBoardEditMode
            sudokuBoardWithDigitRemoved[ removeRowIndex ][ removeColumnIndex ] = 0

            let newAvailableNumbers = addDigitOption(state.availableNumbers, state.sudokuBoardEditMode,  digitToRemove, removeRowIndex, removeColumnIndex)

            newAvailableNumbers[ removeRowIndex ][ removeColumnIndex ] = generateNumbersAllowedInCell(removeRowIndex , removeColumnIndex, state.sudokuBoardEditMode)

            return {
                ...state,
                sudokuBoard: sudokuBoardWithDigitRemoved,
                availableNumbers: newAvailableNumbers
            }
        case TOGGLE_PREVIEW_MODE:
            return {
                ...state,
                availableNumbers: availableNumbersForPreviewMode,
                sudokuBoard: state.sudokuBoardEditMode,
                createStep: "PREVIEW"
            }
        case TOGGLE_EDIT_MODE:
            return {
                ...state,
                availableNumbers: generateNumbersAllowedInEveryBoardCell(action.payload.sudokuBoardForEditMode),//action.payload.availableNumbers,
                sudokuBoardEditMode: action.payload.sudokuBoardForEditMode,
                sudokuBoard: emptySudoku.board,
                createStep: "EDIT"

            }
        case ADD_MARKED_CELL:
            let { digit, cellCoords } = action.payload
            return {
                ...state,
                markedCells: [...state.markedCells, {digit, cellCoords}]
            }
        case RESET_MARKED_CELLS:
            return {
                ...state,
                markedCells: []
            }
        case CHANGE_CONSIDERED_INDEX:
            let {index, rowOrColumn, startOrEnd} = action.payload
            return {
                ...state,
                consideredRows: (rowOrColumn === "ROW" && commonFunctions.isValueInRange(index, [0, 8])) ? (startOrEnd === "START" ? [index, state.consideredRows[1]] : [state.consideredRows[0], index]) : state.consideredRows,
                consideredColumns: (rowOrColumn === "COLUMN" && commonFunctions.isValueInRange(index, [0, 8])) ? (startOrEnd === "START" ? [index, state.consideredColumns[1]] : [state.consideredColumns[0], index]) : state.consideredColumns
            }
        case CHANGE_RANGES:
            let {rowsRange : changedRowRanges, columnsRange: changedColumnRanges} = action.payload
            return {
                ...state,
                consideredRows: changedRowRanges,
                consideredColumns: changedColumnRanges
            }
        case MARK_ACTION:
            let {rowsAndColumnsRange, markOption} = action.payload
            return {
                ...state,
                markOption: markOption,
                consideredRows: rowsAndColumnsRange.rowsRange,
                consideredColumns: rowsAndColumnsRange.columnsRange
            }
        default:
            return state
      }
}

export const selectAvailableNumbersInCell = (state: SudokuLogicState, rowIdx: number, columnIdx : number) => state.availableNumbers[ rowIdx ][ columnIdx ]

export const selectOnlyAvailableInCell = (state: SudokuLogicState, rowIdx: number, columnIdx : number, digit: number) => {
    let available = state.availableNumbers[ rowIdx ][ columnIdx ]
    return available.length === 1 ? (available[0] === digit ? true : false) : false
}

export const selectOccurencesInRow = (state : SudokuLogicState, rowIdx: number, digit: number) => 
    state.availableNumbers[rowIdx].filter((availableInSquare) => { return availableInSquare.includes(digit)}).length

export const selectOccurencesInColumn = (state : SudokuLogicState, columnIdx: number, digit: number) =>  
    state.availableNumbers.reduce(
        (accumulator, curValue, index, array) => {
            return (accumulator + curValue.filter((availableNumbersInCell, columnIndex) => {
                return (availableNumbersInCell.includes(digit) && columnIndex === columnIdx)
            }).length)
        }, 0
)

export const selectOccurencesInSquare3x3 = (state : SudokuLogicState, coords: sudokuCellCoords, digit: number) => {
    let {rowIdx, columnIdx} = coords
    let squareCellIndices = square3x3cellsIndices(rowIdx, columnIdx)
    return squareCellIndices.reduce(
        (occurences, curValue, index, array) => {
            let [rowIdx, colIdx] = curValue
            return occurences + (state.availableNumbers[rowIdx][colIdx].includes(digit) ? 1 : 0)
        }, 0
    )
}

export const selectOccurencesInSquare3x3EditBoard = (state : SudokuLogicState, coords: sudokuCellCoords, digit: number) => {
    let {rowIdx, columnIdx} = coords
    let squareCellIndices = square3x3cellsIndices(rowIdx, columnIdx)
    return squareCellIndices.reduce(
        (includesDigit, curValue) => {
            let [squareRowIdx, squareColIdx] = curValue
            return state.sudokuBoardEditMode[ squareRowIdx ][ squareColIdx ] === digit || includesDigit
        }, false
    )
}
