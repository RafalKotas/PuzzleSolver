import { ADD_HITORI_DETAIL, CHANGE_HITORI_DETAIL, hitoriInformation, REMOVE_HITORI_DETAIL, SAVE_TEMPORARY_HITORI, SET_HITORIS_LIST, SET_SELECTED_HITORI } from "./types"

export const SetHitorisList = (hitorisList : hitoriInformation[]) => {
    return {
        type: SET_HITORIS_LIST,
        payload: {
            hitorisList
        }
    }
}

export const SetSelectedHitori = (selectedHitori : hitoriInformation) => {
    return {
        type: SET_SELECTED_HITORI,
        payload: {
            selectedHitori
        }
    }
}

export const AddHitoriDetail = (detail : keyof hitoriInformation) => {
    return {
        type: ADD_HITORI_DETAIL,
        payload: {
            detail
        }
    }
}

export const RemoveHitoriDetail = (detail : keyof hitoriInformation) => {
    return {
        type: REMOVE_HITORI_DETAIL,
        payload: {
            detail
        }
    }
}

export const ChangeHitoriDetail = (detail : keyof hitoriInformation, value : string | number | number[]) => {
    return {
        type: CHANGE_HITORI_DETAIL,
        payload: {
            detail,
            value
        }
    }
}

export const SaveTemporaryHitori = () => {
    return {
        type: SAVE_TEMPORARY_HITORI
    }
}