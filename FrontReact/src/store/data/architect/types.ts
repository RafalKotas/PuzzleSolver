import { modes } from "../common"

export interface architectInformation {
    source : string,
    year: string,
    month: string,
    difficulty : number,
    height: number,
    width: number,
    tanksInRows: Array<number>,
    tanksInColumns: Array<number>,

    board: Array<Array<string>>,
    label : string,
    value : string
}

export const emptyArchitect : architectInformation = {
    source : "",
    year: "",
    month: "",
    difficulty : 0.0,
    height: 3,
    width: 3,
    tanksInRows: [0, 0, 0],
    tanksInColumns: [0, 0, 0],

    board: [
        ['-', '-', '-'],
        ['-', '-', '-'],
        ['-', '-', '-']
    ],
    label : "",
    value : ""    
}

export interface ArchitectDataState {

    selectedArchitect: architectInformation,
    architectsList : architectInformation[],
    
    createdArchitect: architectInformation,
    createdArchitectsList : architectInformation[],

    architectCorrect: -1 | 0 | 1

    mode: modes,
    editMode: boolean,

    detailsSet: string[]
}

export const possibleArchitectOptions = ["source", "difficulty", "width", "height"]

export const SET_SELECTED_ARCHITECT = "SET_SELECTED_ARCHITECT"
export const SET_ARCHITECTS_LIST = "SET_ARCHITECTS_LIST"

export const ADD_ARCHITECT_DETAIL = "ADD_ARCHITECT_DETAIL"
export const REMOVE_ARCHITECT_DETAIL = "REMOVE_ARCHITECT_DETAIL"
export const CHANGE_ARCHITECT_DETAIL = "CHANGE_ARCHITECT_DETAIL"
export const ADD_TEMPORARY_ARCHITECT = "ADD_TEMPORARY_ARCHITECT"
export const SAVE_TEMPORARY_ARCHITECT = "SAVE_TEMPORARY_ARCHITECT"

interface SetArchitectsList {
    type: typeof SET_ARCHITECTS_LIST,
    payload: {
        architectsList: architectInformation[]
    }
}

interface SetSelectedArchitect {
    type: typeof SET_SELECTED_ARCHITECT,
    payload: {
        selectedArchitect: architectInformation
    }
}

interface AddArchitectDetail {
    type: typeof ADD_ARCHITECT_DETAIL,
    payload: {
        detail : keyof architectInformation
    }
}

interface RemoveArchitectDetail {
    type: typeof REMOVE_ARCHITECT_DETAIL,
    payload: {
        detail : keyof architectInformation
    }
}

interface ChangeArchitectStringDetail {
    type: typeof CHANGE_ARCHITECT_DETAIL,
    payload: {
        detail: keyof architectInformation,
        value: string
    }
}

interface AddTemporaryArchitect {
    type: typeof ADD_TEMPORARY_ARCHITECT,
    payload: {
        temporaryArchitect: architectInformation
    }
}

interface SaveTemporaryArchitect {
    type: typeof SAVE_TEMPORARY_ARCHITECT
}

export type ArchitectDataActionTypes = SetArchitectsList | SetSelectedArchitect | AddArchitectDetail | RemoveArchitectDetail | ChangeArchitectStringDetail |
AddTemporaryArchitect | SaveTemporaryArchitect