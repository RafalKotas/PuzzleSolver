export interface akariInformation {
    source : string,
    difficulty : number,
    height: number,
    width: number,

    board: Array<Array<string>>,
    label : string,
    value : string
}

export const emptyAkari : akariInformation = {
    source : "",
    difficulty : 0.0,
    height: 0,
    width: 0,

    board: [["-"]],
    label : "",
    value : ""    
}

export interface AkariDataState {

    selectedAkari: akariInformation,
    akarisList : akariInformation[],
    
    createdAkari: akariInformation,
    createdAkarisList : akariInformation[],

    akariCorrect: -1 | 0 | 1

    editMode: boolean,

    detailsSet: string[]
}

export const possibleAkariOptions = ["source", "difficulty", "width", "height"]

export const SET_SELECTED_AKARI = "SET_SELECTED_AKARI"
export const SET_AKARIS_LIST = "SET_AKARIS_LIST"

export const ADD_AKARI_DETAIL = "ADD_AKARI_DETAIL"
export const REMOVE_AKARI_DETAIL = "REMOVE_AKARI_DETAIL"
export const CHANGE_AKARI_DETAIL = "CHANGE_AKARI_DETAIL"
export const ADD_TEMPORARY_AKARI = "ADD_TEMPORARY_AKARI"
export const SAVE_TEMPORARY_AKARI = "SAVE_TEMPORARY_AKARI"

interface SetAkarisList {
    type: typeof SET_AKARIS_LIST,
    payload: {
        akarisList: akariInformation[]
    }
}

interface SetSelectedAkari {
    type: typeof SET_SELECTED_AKARI,
    payload: {
        selectedAkari: akariInformation
    }
}

interface AddAkariDetail {
    type: typeof ADD_AKARI_DETAIL,
    payload: {
        detail : keyof akariInformation
    }
}

interface RemoveAkariDetail {
    type: typeof REMOVE_AKARI_DETAIL,
    payload: {
        detail : keyof akariInformation
    }
}

interface ChangeAkariStringDetail {
    type: typeof CHANGE_AKARI_DETAIL,
    payload: {
        detail: keyof akariInformation,
        value: string
    }
}

interface AddTemporaryAkari {
    type: typeof ADD_TEMPORARY_AKARI,
    payload: {
        temporaryAkari: akariInformation
    }
}

interface SaveTemporaryAkari {
    type: typeof SAVE_TEMPORARY_AKARI
}

export type AkariDataActionTypes = SetAkarisList | SetSelectedAkari | 
AddAkariDetail | RemoveAkariDetail | ChangeAkariStringDetail | AddTemporaryAkari | SaveTemporaryAkari