export const nonogramSolverActionsNames : string[] = ["COLOUR", "PLACE_X", "MARK", "CORRECT RANGES"]

export type nonogramActionsNames = "COLOUR" | "PLACE_X" | "MARK" | "CORRECT RANGES" | "CUSTOM SOLVER" | "SAVE SOLUTION" | "COMPARE WITH SOLUTION"

export type nonogramBoardMarks = "X" | "O" | "-"


export type nonogramSolverRowActions = "CORRECT_ROW_SEQUENCES_RANGES" | "CORRECT_ROW_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS" | "CORRECT_ROW_SEQUENCES_RANGES_IF_X_ON_WAY" 
| "CORRECT_ROW_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES" | "CORRECT_ROW_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE" | "COLOUR_OVERLAPPING_FIELDS_IN_ROW" 
| "COLOUR_FIELDS_IN_ROW_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE" | "EXTEND_COLOURED_FIELDS_NEAR_X_IN_ROW" | "COLOUR_FIELDS_IN_ROW_IF_X_CAUSES_ASSIGNMENT_CONFLICT" | "PLACE_XS_ROW_AT_UNREACHABLE_FIELDS" 
| "PLACE_XS_ROW_AROUND_LONGEST_SEQUENCES" | "PLACE_XS_ROW_AT_TOO_SHORT_EMPTY_SEQUENCES" | "PLACE_XS_ROW_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE" 
| "PLACE_XS_ROW_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE" | "ROW_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH" | "MARK_AVAILABLE_FIELDS_IN_ROW"

export type nonogramSolverColumnAction = "CORRECT_COLUMN_SEQUENCES_RANGES" | "CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MET_COLOURED_FIELDS" | "CORRECT_COLUMN_SEQUENCES_RANGES_IF_X_ON_WAY"
| "CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_MATCHING_FIELDS_TO_SEQUENCES" | "CORRECT_COLUMN_SEQUENCES_RANGES_WHEN_START_FROM_EDGE_INDEX_WILL_CREATE_TOO_LONG_SEQUENCE" | "COLOUR_OVERLAPPING_FIELDS_IN_COLUMN"
| "COLOUR_FIELDS_IN_COLUMN_IF_X_WOULD_FORCE_TOO_LONG_COLOURED_FIELDS_SEQUENCE" | "EXTEND_COLOURED_FIELDS_NEAR_X_IN_COLUMN" | "COLOUR_FIELDS_IN_COLUMN_IF_X_CAUSES_ASSIGNMENT_CONFLICT"
| "PLACE_XS_COLUMN_AT_UNREACHABLE_FIELDS" | "PLACE_XS_COLUMN_AROUND_LONGEST_SEQUENCES" | "PLACE_XS_COLUMN_AT_TOO_SHORT_EMPTY_SEQUENCES" | "PLACE_XS_COLUMN_IF_O_WILL_MERGE_NEAR_FIELDS_TO_TOO_LONG_COLOURED_SEQUENCE"
| "PLACE_XS_COLUMN_IF_O_NEAR_X_WILL_BEGIN_TOO_LONG_POSSIBLE_COLOURED_SEQUENCE" | "COLUMN_PREVENT_EXTENDING_COLOURED_SEQUENCE_TO_EXCESS_LENGTH" | "MARK_AVAILABLE_FIELDS_IN_COLUMN"

export type nonogramSolverActions = nonogramSolverRowActions | nonogramSolverColumnAction

export interface NonogramLogicState {
    nonogramRelatedData: nonogramRelatedLogicData,
    currentMark: nonogramBoardMarks
}

export interface rules {
    rowsSequencesLengths:                    Array<       Array<number>      >,
    columnsSequencesLengths:                 Array<       Array<number>      >,
    height: number,
    width: number
}

export interface nonogramState {
    newStepsMade: number,
    invalidSolution: boolean
}

export interface nonogramActionDetails {
    index: number, 
    actionName: nonogramSolverActions,
}

export interface nonogramRelatedLogicData {

    nonogramRules: rules,

    // data inferred from upper properties 
    nonogramSolutionBoard:           Array<       Array<string>      >,
    nonogramSolutionBoardWithMarks:  Array<       Array<string>      >,

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
export const SET_NONOGRAM_RELATED_LOGIC_DATA = "SET_NONOGRAM_RELATED_LOGIC_DATA"
export const RESET_NONOGRAM_BOARD = "RESET_NONOGRAM_BOARD"

interface InitializeSolverData {
    type: typeof INIT_SOLVER_DATA,
    payload: {
        rowsSequences: Array<Array<number>>,
        columnsSequences : Array<Array<number>>
    }
}

interface SetNonogramRelatedLogicData {
    type: typeof SET_NONOGRAM_RELATED_LOGIC_DATA,
    payload: {
        nonogramRelatedLogicData: nonogramRelatedLogicData
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

type LocalLogicActions = InitializeSolverData | SetNonogramRelatedLogicData | ResetNonogramBoard |
     FillBoardSquare | PlaceXBoardSquare | SetCurrentMark

type NonogramSolverActionTypes = ColourColumnFieldsInColumnsRange

export type NonogramLogicActionTypes = LocalLogicActions | NonogramSolverActionTypes