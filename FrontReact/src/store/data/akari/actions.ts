import { ADD_AKARI_DETAIL, akariInformation, CHANGE_AKARI_DETAIL, REMOVE_AKARI_DETAIL, SAVE_TEMPORARY_AKARI, SET_AKARIS_LIST, SET_SELECTED_AKARI } from "./types"

export const SetAkarisList = (akarisList : akariInformation[]) => {
    return {
        type: SET_AKARIS_LIST,
        payload: {
            akarisList
        }
    }
}

export const SetSelectedAkari = (selectedAkari : akariInformation) => {
    return {
        type: SET_SELECTED_AKARI,
        payload: {
            selectedAkari
        }
    }
}

export const AddAkariDetail = (detail : keyof akariInformation) => {
    return {
        type: ADD_AKARI_DETAIL,
        payload: {
            detail
        }
    }
}

export const RemoveAkariDetail = (detail : keyof akariInformation) => {
    return {
        type: REMOVE_AKARI_DETAIL,
        payload: {
            detail
        }
    }
}

export const ChangeAkariDetail = (detail : keyof akariInformation, value : string | number | number[]) => {
    return {
        type: CHANGE_AKARI_DETAIL,
        payload: {
            detail,
            value
        }
    }
}

export const SaveTemporaryAkari = () => {
    return {
        type: SAVE_TEMPORARY_AKARI
    }
}