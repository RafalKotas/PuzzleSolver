export type correctnessIndicator = -1 | 0 | 1

export type modes = "READ" | "CREATE"

export interface nonogramInformation {
    filename: string,
    source : string,
    year : string,
    month : string,
    difficulty : number,
    height: number,
    width: number
}

export interface selectedNonogramDetails {
    filename: string,
    source : string,
    year : string,
    month : string,
    difficulty : number,
    height: number,
    width: number,

    rowSequences: Array<Array<number>>,
    columnSequences: Array<Array<number>>
}

export const emptyNonogram : selectedNonogramDetails = {
    filename : "",
    source : "",
    year : "",
    month : "",
    difficulty : 0.0,
    height: 0,
    width: 0,

    rowSequences: [],
    columnSequences: []
}

export interface nonogramSquareEdit {
    editing : boolean,
    squareSection : "rowSequences" | "columnSequences",
    coords: {
        no: number,
        seqNo: number
    }
}

export const nonSelectedSquareEdit : nonogramSquareEdit = {
    editing: false,
    squareSection: "rowSequences",
    coords: {
        no: -1,
        seqNo: -1
    }
}

// actions

interface SetNonogramsList {
    type: typeof SET_NONOGRAMS_LIST,
    payload: {
        nonogramsList: nonogramInformation[]
    }
}

interface SetSelectedNonogram {
    type: typeof SET_SELECTED_NONOGRAM,
    payload: {
        updatedNonogram: selectedNonogramDetails
    }
}

interface AddTemporaryNonogram {
    type: typeof ADD_TEMPORARY_NONOGRAM,
    payload: {
        temporaryNonogram: nonogramInformation
    }
}

interface AddNonogramDetail {
    type: typeof ADD_NONOGRAM_DETAIL,
    payload: {
        detail : keyof nonogramInformation
    }
}

interface RemoveNonogramDetail {
    type: typeof REMOVE_NONOGRAM_DETAIL,
    payload: {
        detail : keyof nonogramInformation
    }
}

interface ChangeNonogramStringDetail {
    type: typeof CHANGE_NONOGRAM_STRING_DETAIL,
    payload: {
        detail: keyof nonogramInformation,
        value: string
    }
}

interface SaveTemporaryNonogram {
    type: typeof SAVE_TEMPORARY_NONOGRAM
}

interface SetEditMode {
    type: typeof SET_EDIT_MODE,
    payload: {
        editMode: boolean
    }
}

interface SetEditedSquare {
    type: typeof SET_EDITED_SQUARE,
    payload: {
        squareEdit: nonogramSquareEdit
    }
}

interface ModifySequence {
    type: typeof MODIFY_SEQUENCE,
    payload: {
        value: number,
        section: "rowSequences" | "columnSequences",
        index: number,
        seqNo: number
    }
}

interface ModifyRow {
    type: typeof MODIFY_ROW,
    payload: {
        rowIndex: number,
        rowSequences: Array<number>
    }
}

interface ModifyColumn {
    type: typeof MODIFY_COLUMN,
    payload: {
        columnIndex: number,
        columnSequences: Array<number>
    }
}

interface PreviewNonogram {
    type: typeof PREVIEW_NONOGRAM,
    payload: {

    }
}

interface ClosePreviewNonogram {
    type: typeof CLOSE_PREVIEW_NONOGRAM,
    payload: {

    }
}

interface ToggleMarksVisibility {
    type: typeof TOGGLE_MARKS_VISIBILITY,
    payload: {
        marksVisibility: boolean
    }
}

interface ToggleXsVisibility {
    type: typeof TOGGLE_XS_VISIBILITY,
    payload: {
        xsVisibility: boolean
    }
}

interface SetCorrectness {
    type: typeof SET_CORRECTNESS,
    payload: {
        correct : correctnessIndicator
    }
}

interface AddRows {
    type: typeof ADD_ROWS,
    payload: {
        startRowAddIdx: number,
        endRowAddIdx: number
    }
}

interface RemoveRows {
    type: typeof REMOVE_ROWS,
    payload: {
        startRowRemoveIdx: number,
        endRowRemoveIdx: number
    }
}

interface AddColumns {
    type: typeof ADD_COLUMNS,
    payload: {
        startColumnAddIdx: number,
        endColumnAddIdx: number
    }
}

interface RemoveColumns {
    type: typeof REMOVE_COLUMNS,
    payload: {
        startColumnRemoveIdx: number,
        endColumnRemoveIdx: number
    }
}

type ManipulateNonogramDataActions = SetSelectedNonogram | SetNonogramsList | AddTemporaryNonogram 
| AddNonogramDetail | RemoveNonogramDetail | ChangeNonogramStringDetail | SaveTemporaryNonogram

type ModifyNonogramDataActions = ModifySequence | ModifyRow | ModifyColumn | AddRows | AddColumns | RemoveRows | RemoveColumns

type ModeManipulateActions =  SetEditMode | SetEditedSquare | PreviewNonogram | ClosePreviewNonogram | SetCorrectness 
    | ToggleMarksVisibility | ToggleXsVisibility

export type NonogramDataActionTypes = ManipulateNonogramDataActions | ModifyNonogramDataActions | ModeManipulateActions

export interface NonogramDataState {

    // list of all nonograms with details
    nonogramsList : nonogramInformation[],
    createdNonogramsList : nonogramInformation[],
    
    // current selected nonogram
    selectedNonogram: selectedNonogramDetails | null,

    createdNonogram: selectedNonogramDetails,
    nextCreatedNonogramFileNumber: number,

    // -1 - no(wrong) , 0 - undefined, 1 - yes(correct)
    nonogramCorrect: -1 | 0 | 1

    //only reading(and solving) or editing
    mode: modes,

    //edit mode (EDIT or PREVIEW without editing possibility)
    editMode: boolean,

    marksVisible: boolean,
    xsVisible: boolean,

    detailsSet: string[],

    squareEdit: nonogramSquareEdit
}

export const transformNonogramInformationIntoNonogramDetails : (nonogramInfo : nonogramInformation) => selectedNonogramDetails = (nonogramInfo : nonogramInformation) => {
    return {
        ...nonogramInfo,
        rowSequences: Array.from({length: nonogramInfo.height}, () => [0]),
        columnSequences: Array.from({length: nonogramInfo.width}, () => [0])
    }
}

export const SET_SELECTED_NONOGRAM = "SET_SELECTED_NONOGRAM"
export const SET_NONOGRAMS_LIST = "SET_NONOGRAMS_LIST"

export const ADD_TEMPORARY_NONOGRAM = "ADD_TEMPORARY_NONOGRAM"
export const ADD_NONOGRAM_DETAIL = "ADD_NONOGRAM_DETAIL"
export const REMOVE_NONOGRAM_DETAIL = "REMOVE_NONOGRAM_DETAIL"
export const CHANGE_NONOGRAM_STRING_DETAIL = "CHANGE_NONOGRAM_STRING_DETAIL"
export const SAVE_TEMPORARY_NONOGRAM = "SAVE_TEMPORARY_NONOGRAM"

export const SET_DISPLAY_MODE = "SET_DISPLAY_MODE"
export const SET_EDIT_MODE = "SET_EDIT_MODE"

export const SET_EDITED_SQUARE = "SET_EDITED_SQUARE"
export const RESET_EDITED_SQUARE = "RESET_EDITED_SQUARE"
export const MODIFY_SEQUENCE = "MODIFY_SEQUENCE"
export const PREVIEW_NONOGRAM = "PREVIEW_NONOGRAM"
export const CLOSE_PREVIEW_NONOGRAM = "CLOSE_PREVIEW_NONOGRAM"
export const TOGGLE_MARKS_VISIBILITY = "TOGGLE_MARKS_VISIBILITY"
export const TOGGLE_XS_VISIBILITY = "TOGGLE_XS_VISIBILITY"

export const MODIFY_ROW = "MODIFY_ROW"
export const MODIFY_COLUMN = "MODIFY_COLUMN"

export const ADD_ROWS = "ADD_ROWS"
export const REMOVE_ROWS = "REMOVE_ROWS"
export const ADD_COLUMNS = "ADD_COLUMNS"
export const REMOVE_COLUMNS = "REMOVE_COLUMNS"

export const SET_CORRECTNESS = "SET_CORRECTNESS"

export const possibleNonogramOptions = ["source", "year", "month", "difficulty", "width", "height"]