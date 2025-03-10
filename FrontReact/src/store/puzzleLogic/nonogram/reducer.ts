// redux
import { Reducer } from "redux"
import { inferInitialColumnsSequencesRanges } from "./logic-columns"

// logic
import { inferInitialRowsSequencesRanges } from "./logic-rows"

// store
import { createEmptyNonogramBoard, 
    emptyColumnFieldsNotToInclude, 
    emptyColumnsSequencesIdsNotToInclude, 
    emptyRowsFieldsNotToInclude, 
    emptyRowsSequencesIdsNotToInclude,
    NonogramLogicActionTypes, 
    NonogramLogicState, 
    COLOUR_FIELDS_IN_COLUMNS_RANGE, 
    SET_NONOGRAM_LOGIC_DATA,
    RESET_NONOGRAM_BOARD,
    FILL_BOARD_SQUARE, 
    INIT_SOLVER_DATA, 
    PLACE_X_BOARD_SQUARE, 
    SET_CURRENT_NONOGRAM_MARK, 
} from "./types"

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
    currentMark: "O"
}

const createEmptyList = (length: number): any[] => {
    return Array.from({ length }, () => []);
};

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
                }
            }
        case SET_NONOGRAM_LOGIC_DATA:
            return {
                ...state,
                nonogramRelatedData: action.payload.nonogramLogicData
            }
        case RESET_NONOGRAM_BOARD:
            if (state.nonogramRelatedData.nonogramSolutionBoard && state.nonogramRelatedData.nonogramSolutionBoard.length > 0) {
                return {
                    ...state,
                    nonogramRelatedData: {
                        ...state.nonogramRelatedData,
                        nonogramSolutionBoard: state.nonogramRelatedData.nonogramSolutionBoard.map(innerArray => innerArray.map(() => "-")),
                        nonogramSolutionBoardWithMarks: state.nonogramRelatedData.nonogramSolutionBoardWithMarks.map(innerArray => innerArray.map(() => "----")),
                        rowsSequencesIdsNotToInclude: createEmptyList(state.nonogramRelatedData.nonogramSolutionBoard.length),
                        columnsSequencesIdsNotToInclude: state.nonogramRelatedData.nonogramSolutionBoard[0] 
                            ? createEmptyList(state.nonogramRelatedData.nonogramSolutionBoard[0].length) 
                            : []
                    }
                };
            }
            return {
                ...state
            };
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