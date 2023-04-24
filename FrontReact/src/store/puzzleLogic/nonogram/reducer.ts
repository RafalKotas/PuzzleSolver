// redux
import { Reducer } from "redux"
import { inferInitialColumnsSequencesRanges } from "./logic-columns"

// logic
import { inferInitialRowsSequencesRanges } from "./logic-rows"

// store
import { COLOUR_FIELDS_IN_COLUMNS_RANGE, createEmptyNonogramBoard, emptyColumnFieldsNotToInclude, emptyColumnsSequencesIdsNotToInclude, emptyRowsFieldsNotToInclude, emptyRowsSequencesIdsNotToInclude, FILL_BOARD_SQUARE, INIT_SOLVER_DATA, NonogramLogicActionTypes, NonogramLogicState, PLACE_X_BOARD_SQUARE, RESET_LOGIC_DATA_INITIALIZED, SET_CURRENT_NONOGRAM_MARK, SET_NONOGRAM_LOGIC_DATA } from "./types"

export const initialState: NonogramLogicState = {
    nonogramRelatedData: {
        rowsSequences: [],
        columnsSequences: [],
        affectedRowsIndexes: [],
        affectedColumnsIndexes: [],
        nonogramSolutionBoardWithMarks: [],
        nonogramSolutionBoard: [],
        rowsSequencesRanges: [],
        columnsSequencesRanges: [],
        rowsFieldsNotToInclude: [],
        columnsFieldsNotToInclude: [],
        rowsSequencesIdsNotToInclude: [],
        columnsSequencesIdsNotToInclude: []
    },
    logicDataInitialized: false,
    currentMark: "O"
}

export const nonogramLogicReducer: Reducer<NonogramLogicState, NonogramLogicActionTypes> = (state = initialState, action : NonogramLogicActionTypes) => {
    switch (action.type) {
        case INIT_SOLVER_DATA:
            let { rowsSequences, columnsSequences } = action.payload
            let [height, width] = [rowsSequences.length, columnsSequences.length]
            let emptyNonogramBoard = createEmptyNonogramBoard(height, width, 1)
            let emptyNonogramBoardWithMarks = createEmptyNonogramBoard(height, width, 4)
            return {
                ...state,
                nonogramRelatedData : {
                    rowsSequences: rowsSequences,
                    columnsSequences: columnsSequences,
                    nonogramSolutionBoardWithMarks: emptyNonogramBoardWithMarks,
                    nonogramSolutionBoard: emptyNonogramBoard,
                    affectedRowsIndexes: Array.from({length: height}, (_, rowIdx) => rowIdx),
                    affectedColumnsIndexes: Array.from({length: width}, (_, columnIdx) => columnIdx),
                    rowsSequencesRanges: inferInitialRowsSequencesRanges(rowsSequences, emptyNonogramBoard),
                    columnsSequencesRanges: inferInitialColumnsSequencesRanges(columnsSequences, emptyNonogramBoard),
                    rowsFieldsNotToInclude: emptyRowsFieldsNotToInclude(height),
                    columnsFieldsNotToInclude: emptyColumnFieldsNotToInclude(width),
                    rowsSequencesIdsNotToInclude: emptyRowsSequencesIdsNotToInclude(height),
                    columnsSequencesIdsNotToInclude: emptyColumnsSequencesIdsNotToInclude(width)
                },
                logicDataInitialized: true
            }
        case RESET_LOGIC_DATA_INITIALIZED:
            return {
                ...state,
                logicDataInitialized: false
            }
        case SET_NONOGRAM_LOGIC_DATA:
            return {
                ...state,
                nonogramRelatedData: action.payload.nonogramLogicData
            }
        case FILL_BOARD_SQUARE:
            let { rowIdx, columnIdx } = action.payload
            return {
               ...state,
               nonogramRelatedData: {
                    ...state.nonogramRelatedData,
                    nonogramSolutionBoard: state.nonogramRelatedData.nonogramSolutionBoard.map((boardRow, r_idx) => {
                        return boardRow.map((boardField, c_idx) => {
                            return (r_idx === rowIdx && c_idx === columnIdx) ? 
                                ((boardField === "-" || boardField === "X") ? "O" : "-") : boardField
                        })
                   })
               }
            }
        case PLACE_X_BOARD_SQUARE:
            let { rowIdx : rowForX, columnIdx : columnForX } = action.payload
            return {
                ...state,
                nonogramRelatedData: {
                    ...state.nonogramRelatedData,
                    nonogramSolutionBoard: state.nonogramRelatedData.nonogramSolutionBoard.map((boardRow, r_idx) => {
                        return boardRow.map((boardField, c_idx) => {
                            return (r_idx === rowForX && c_idx === columnForX) ? 
                                ((boardField === "-" || boardField === "O") ? "X" : "-") : boardField
                        })
                    })
                }
            }
        case SET_CURRENT_NONOGRAM_MARK:
            let { mark }  = action.payload
            return {
                ...state,
                currentMark: mark
            }
        case COLOUR_FIELDS_IN_COLUMNS_RANGE:
            return state
        default:
            return state
      }
}

export const selectBoardSquare = (state: NonogramLogicState, rowIdx : number, columnIdx : number) =>  {
    if(state && state.nonogramRelatedData && state.nonogramRelatedData.nonogramSolutionBoard 
            && state.nonogramRelatedData.nonogramSolutionBoard[rowIdx] && state.nonogramRelatedData.nonogramSolutionBoard[rowIdx][columnIdx]) {
        return state.nonogramRelatedData.nonogramSolutionBoard[rowIdx][columnIdx]
    } else {
        return "-"
    }
}

export const selectBoardSquareMark = (state: NonogramLogicState, rowIdx : number, columnIdx : number) =>  {
    if(state && state.nonogramRelatedData && state.nonogramRelatedData.nonogramSolutionBoard 
            && state.nonogramRelatedData.nonogramSolutionBoardWithMarks[rowIdx] && 
            state.nonogramRelatedData.nonogramSolutionBoardWithMarks[rowIdx][columnIdx]) {
        return state.nonogramRelatedData.nonogramSolutionBoardWithMarks[rowIdx][columnIdx]
    } else {
        return "----"
    }
}

const selectSequenceInRowFulfiledConditionMet = (logicState: NonogramLogicState, sequenceNo: number, rowIdx: number) => {
    let condition = logicState && logicState.nonogramRelatedData && logicState.nonogramRelatedData.rowsSequencesIdsNotToInclude 
        && logicState.nonogramRelatedData.rowsSequencesIdsNotToInclude[rowIdx] && logicState.nonogramRelatedData.rowsSequencesIdsNotToInclude[rowIdx].includes(sequenceNo)
    return condition
}

const selectSequenceInColumnFulfiledConditionMet = (logicState: NonogramLogicState, sequenceNo: number, columnIdx: number) => {
    let condition = logicState && logicState.nonogramRelatedData && logicState.nonogramRelatedData.columnsSequencesIdsNotToInclude 
        && logicState.nonogramRelatedData.columnsSequencesIdsNotToInclude[columnIdx] && logicState.nonogramRelatedData.columnsSequencesIdsNotToInclude[columnIdx].includes(sequenceNo)
    return condition
}

export const selectSequenceFulFilledConditionMet = (logicState: NonogramLogicState, sequenceNo: number, idx: number, sectionName: string) => {
    if(sectionName === "rowSequences") {
        return selectSequenceInRowFulfiledConditionMet(logicState, sequenceNo, idx)
    } else {
        return selectSequenceInColumnFulfiledConditionMet(logicState, sequenceNo, idx)
    }
}