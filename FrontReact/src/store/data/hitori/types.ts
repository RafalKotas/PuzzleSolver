import { modes } from "../common"

export interface hitoriInformation {
    source : string,
    difficulty : number,
    height: number,
    width: number,

    board: Array<Array<string>>,
    label : string,
    value : string
}

export const emptyHitori : hitoriInformation = {
    source : "",
    difficulty : 0.0,
    height: 0,
    width: 0,

    board: [["1", "1", "1", "1"],
            ["1", "1", "1", "1"],
            ["1", "1", "1", "1"],
            ["1", "1", "1", "1"]
        ],
    label : "",
    value : ""    
}

export interface HitoriDataState {

    selectedHitori: hitoriInformation,
    hitorisList : hitoriInformation[],
    
    createdHitori: hitoriInformation,
    createdHitorisList : hitoriInformation[],

    hitoriCorrect: -1 | 0 | 1

    mode: modes,
    editMode: boolean,

    detailsSet: string[]
}

export const possibleHitoriOptions = ["source", "difficulty", "width", "height"]

export const SET_SELECTED_HITORI = "SET_SELECTED_HITORI"
export const SET_HITORIS_LIST = "SET_HITORIS_LIST"

export const ADD_HITORI_DETAIL = "ADD_HITORI_DETAIL"
export const REMOVE_HITORI_DETAIL = "REMOVE_HITORI_DETAIL"
export const CHANGE_HITORI_DETAIL = "CHANGE_HITORI_DETAIL"
export const ADD_TEMPORARY_HITORI = "ADD_TEMPORARY_HITORI"
export const SAVE_TEMPORARY_HITORI = "SAVE_TEMPORARY_HITORI"

interface SetHitorisList {
    type: typeof SET_HITORIS_LIST,
    payload: {
        hitorisList: hitoriInformation[]
    }
}

interface SetSelectedHitori {
    type: typeof SET_SELECTED_HITORI,
    payload: {
        selectedHitori: hitoriInformation
    }
}

interface AddHitoriDetail {
    type: typeof ADD_HITORI_DETAIL,
    payload: {
        detail : keyof hitoriInformation
    }
}

interface RemoveHitoriDetail {
    type: typeof REMOVE_HITORI_DETAIL,
    payload: {
        detail : keyof hitoriInformation
    }
}

interface ChangeHitoriStringDetail {
    type: typeof CHANGE_HITORI_DETAIL,
    payload: {
        detail: keyof hitoriInformation,
        value: string
    }
}

interface AddTemporaryHitori {
    type: typeof ADD_TEMPORARY_HITORI,
    payload: {
        temporaryHitori: hitoriInformation
    }
}

interface SaveTemporaryHitori {
    type: typeof SAVE_TEMPORARY_HITORI
}

export type HitoriDataActionTypes = SetHitorisList | SetSelectedHitori | 
AddHitoriDetail | RemoveHitoriDetail | ChangeHitoriStringDetail | AddTemporaryHitori | SaveTemporaryHitori