import { modes } from "../common"

export interface slitherlinkInformation {
    source : string,
    year: string,
    month: string,
    difficulty : number,
    height: number,
    width: number,

    board: Array<Array<number>>,
    label : string,
    value : string
}

export const emptySlitherlink : slitherlinkInformation = {
    source : "",
    year: "",
    month: "",
    difficulty : 0.0,
    height: 0,
    width: 0,

    board: [
		[-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1], 
		[-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1],
		[-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1],
		[-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1],
		[-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1],
		[-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1],
		[-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1],
		[-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1],
		[-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1],
		[-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1],
		[-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1]
	],
    label : "",
    value : ""    
}

export interface SlitherlinkDataState {

    selectedSlitherlink: slitherlinkInformation,
    slitherlinksList : slitherlinkInformation[],
    
    createdSlitherlink: slitherlinkInformation,
    createdSlitherlinksList : slitherlinkInformation[],

    slitherlinkCorrect: -1 | 0 | 1

    mode: modes,
    editMode: boolean,

    detailsSet: string[]
}

export const possibleSlitherlinkOptions = ["source", "year", "month", "difficulty", "width", "height"]

export const SET_SELECTED_SLITHERLINK = "SET_SELECTED_SLITHERLINK"
export const SET_SLITHERLINKS_LIST = "SET_SLITHERLINKS_LIST"

export const ADD_SLITHERLINK_DETAIL = "ADD_SLITHERLINK_DETAIL"
export const REMOVE_SLITHERLINK_DETAIL = "REMOVE_SLITHERLINK_DETAIL"
export const CHANGE_SLITHERLINK_DETAIL = "CHANGE_SLITHERLINK_DETAIL"
export const ADD_TEMPORARY_SLITHERLINK = "ADD_TEMPORARY_SLITHERLINK"
export const SAVE_TEMPORARY_SLITHERLINK = "SAVE_TEMPORARY_SLITHERLINK"

interface SetSlitherlinksList {
    type: typeof SET_SLITHERLINKS_LIST,
    payload: {
        slitherlinksList: slitherlinkInformation[]
    }
}

interface SetSelectedSlitherlink {
    type: typeof SET_SELECTED_SLITHERLINK,
    payload: {
        selectedSlitherlink: slitherlinkInformation
    }
}

interface AddSlitherlinkDetail {
    type: typeof ADD_SLITHERLINK_DETAIL,
    payload: {
        detail : keyof slitherlinkInformation
    }
}

interface RemoveSlitherlinkDetail {
    type: typeof REMOVE_SLITHERLINK_DETAIL,
    payload: {
        detail : keyof slitherlinkInformation
    }
}

interface ChangeSlitherlinkStringDetail {
    type: typeof CHANGE_SLITHERLINK_DETAIL,
    payload: {
        detail: keyof slitherlinkInformation,
        value: string
    }
}

interface AddTemporarySlitherlink {
    type: typeof ADD_TEMPORARY_SLITHERLINK,
    payload: {
        temporarySlitherlink: slitherlinkInformation
    }
}

interface SaveTemporarySlitherlink {
    type: typeof SAVE_TEMPORARY_SLITHERLINK
}


export type SlitherlinkDataActionTypes = SetSlitherlinksList | SetSelectedSlitherlink | AddSlitherlinkDetail | RemoveSlitherlinkDetail |
ChangeSlitherlinkStringDetail | AddTemporarySlitherlink | SaveTemporarySlitherlink