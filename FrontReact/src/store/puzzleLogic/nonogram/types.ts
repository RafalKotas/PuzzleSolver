export type nonogramBoardMarks = "X" | "O" | "-"

export interface NonogramLogicState {

    nonogramRelatedData: nonogramRelatedLogicData,
    currentMark: nonogramBoardMarks
}

export interface nonogramRelatedLogicData {
    // data need for init state to solve
    rowsSequences:                    Array<       Array<number>      >,
    columnsSequences:                 Array<       Array<number>      >,
    
    affectedRowsIndexes:              Array<       number             >,
    affectedColumnsIndexes:           Array<       number             >,

    // data inferred from upper properties 
    nonogramSolutionBoardWithMarks:  Array<       Array<string>      >,
    nonogramSolutionBoard:           Array<       Array<string>      >,

    rowsSequencesRanges:             Array< Array< Array< number > > >,
    columnsSequencesRanges:          Array< Array< Array< number > > >,

    rowsFieldsNotToInclude:          Array<        Array<number>     >,
    columnsFieldsNotToInclude:       Array<        Array<number>     >,

    rowsSequencesIdsNotToInclude:    Array<        Array<number>     >,
    columnsSequencesIdsNotToInclude: Array<        Array<number>     >
}

export const createObjectToInitSolverData = (rowsSequences : number[][], columnsSequences : number[][]) => {
    return ({
        rowsSequences: rowsSequences,
        columnsSequences: columnsSequences,
        nonogramSolutionBoardWithMarks: [] as string[][],
        affectedRowsIndexes: [] as number[],
        affectedColumnsIndexes: [] as number[],
        nonogramSolutionBoard: [] as string[][],
        rowsSequencesRanges: [] as number[][][],
        columnsSequencesRanges: [] as number[][][],
        rowsFieldsNotToInclude: [] as number[][],
        columnsFieldsNotToInclude: [] as number[][],
        rowsSequencesIdsNotToInclude: [] as number[][],
        columnsSequencesIdsNotToInclude: [] as number[][]
    })
}

// description

// rowSequences, columnSequences
    // taken from nonogramDataReducer on init

// nonogramSolutionBoard
    // height: rowSequences.length, width: columnSequences.length

export const createEmptyNonogramBoard = (height: number, width: number, chars: number) => {
    return Array.from({ length: height }, () => {
        return Array.from({ length: width }, () => "-".repeat(chars))
    })
}

// rowsSequencesRanges 
    // need to implement function:
    // generate sequences of letters (letters number === sequences number, f.e. [4, 2, 4] => ["a", "b", "c"])
    // f.e. width = 15 generate 2 arrays, from start and from end:
    // aaaaxbbxccccxxx(1)
    // xxxaaaaxbbxcccc(2)
    // for every letter find first index in (1) and last in (2) 

// columnsSequencesRanges
    // same as above

// rowsFieldsNotToInclude (when colouring overlapping sequences)
export const emptyRowsFieldsNotToInclude = (height: number) => {
    return Array.from({ length: height }, () => {
        return []
    })
}

// columnsFieldsNotToInclude (-||-)
export const emptyColumnFieldsNotToInclude = (width: number) => {
    return Array.from({ length: width }, () => {
        return []
    })
}

// rowsSequencesIdsNotToInclude (omit them when creating overlapping array from begin and end)
export const emptyRowsSequencesIdsNotToInclude = (height: number) => {
    return  Array.from({ length: height }, () => {
        return []
    })
}

// columnsSequencesIdsNotToInclude (-||-)
export const emptyColumnsSequencesIdsNotToInclude = (width: number) => {
    return Array.from({ length: width }, () => {
        return []
    })
}

export const INIT_SOLVER_DATA = "INIT_SOLVER_DATA"
export const SET_NONOGRAM_LOGIC_DATA = "SET_NONOGRAM_LOGIC_DATA"
export const RESET_NONOGRAM_BOARD = "RESET_NONOGRAM_BOARD"

interface InitializeSolverData {
    type: typeof INIT_SOLVER_DATA,
    payload: {
        rowsSequences: Array<Array<number>>,
        columnsSequences : Array<Array<number>>
    }
}

interface SetNonogramLogicData {
    type: typeof SET_NONOGRAM_LOGIC_DATA,
    payload: {
        nonogramLogicData: nonogramRelatedLogicData
    }
}

interface ResetNonogramBoard {
    type: typeof RESET_NONOGRAM_BOARD,
    payload: {

    }
}

export const FILL_BOARD_SQUARE = "FILL_BOARD_SQUARE"
export const PLACE_X_BOARD_SQUARE = "PLACE_X_BOARD_SQUARE"

interface FillBoardSquare {
    type: typeof FILL_BOARD_SQUARE,
    payload: {
        rowIdx: number,
        columnIdx: number
    }
}

interface PlaceXBoardSquare {
    type: typeof PLACE_X_BOARD_SQUARE,
    payload: {
        rowIdx: number,
        columnIdx: number
    }
}


export const SET_CURRENT_NONOGRAM_MARK = "SET_CURRENT_NONOGRAM_MARK"

interface SetCurrentMark {
    type: typeof SET_CURRENT_NONOGRAM_MARK,
    payload: {
        mark: nonogramBoardMarks
    }
}

export const COLOUR_FIELDS_IN_COLUMNS_RANGE = "COLOUR_FIELDS_IN_COLUMNS_RANGE"

interface ColourColumnFieldsInColumnsRange {
    type: typeof COLOUR_FIELDS_IN_COLUMNS_RANGE,
    payload: {
        columnBegin : number,
        columnEnd : number
    }
}

type LocalLogicActions = InitializeSolverData | SetNonogramLogicData | ResetNonogramBoard |
     FillBoardSquare | PlaceXBoardSquare | SetCurrentMark

type NonogramSolverActionTypes = ColourColumnFieldsInColumnsRange

export type NonogramLogicActionTypes = LocalLogicActions | NonogramSolverActionTypes