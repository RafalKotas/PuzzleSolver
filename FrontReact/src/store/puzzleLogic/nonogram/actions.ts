import { COLOUR_FIELDS_IN_COLUMNS_RANGE, 
    FILL_BOARD_SQUARE, 
    INIT_SOLVER_DATA, 
    PLACE_X_BOARD_SQUARE, 
    RESET_NONOGRAM_BOARD, 
    SET_CURRENT_NONOGRAM_MARK, 
    SET_NONOGRAM_LOGIC_DATA,
    nonogramBoardMarks, 
    nonogramRelatedLogicData } from "./types"

export const InitializeSolverData = (rowsSequences: Array<Array<number>>, columnsSequences : Array<Array<number>>) => {
    return {
        type: INIT_SOLVER_DATA,
        payload: {
            rowsSequences,
            columnsSequences
        }
    }
}

export const SetNonogramLogicData = (nonogramLogicData: nonogramRelatedLogicData) => {
    return {
        type: SET_NONOGRAM_LOGIC_DATA,
        payload: {
            nonogramLogicData
        }
    }
}

export const ResetNonogramBoard = () => {
    return {
        type: RESET_NONOGRAM_BOARD,

    }
}

export const FillBoardSquare = (rowIdx : number, columnIdx : number) => {
    return {
        type: FILL_BOARD_SQUARE,
        payload: {
            rowIdx,
            columnIdx
        }
    }
}

export const PlaceXBoardSquare = (rowIdx : number, columnIdx : number) => {
    return {
        type: PLACE_X_BOARD_SQUARE,
        payload: {
            rowIdx,
            columnIdx
        }
    }
}

export const SetCurrentNonogramMark = (mark : nonogramBoardMarks) => {
    return {
        type: SET_CURRENT_NONOGRAM_MARK,
        payload: {
            mark
        }
    }
}

export const ColourFieldsInColumnsRange = (columnBegin: number, columnEnd: number) => {
    return {
        type: COLOUR_FIELDS_IN_COLUMNS_RANGE,
        payload: {
            columnBegin,
            columnEnd
        }
    }
}