import { ADD_ARCHITECT_DETAIL, architectInformation, CHANGE_ARCHITECT_DETAIL, REMOVE_ARCHITECT_DETAIL, SAVE_TEMPORARY_ARCHITECT, SET_ARCHITECTS_LIST, SET_SELECTED_ARCHITECT } from "./types"

export const SetArchitectsList = (architectsList : architectInformation[]) => {
    return {
        type: SET_ARCHITECTS_LIST,
        payload: {
            architectsList
        }
    }
}

export const SetSelectedArchitect = (selectedArchitect : architectInformation) => {
    return {
        type: SET_SELECTED_ARCHITECT,
        payload: {
            selectedArchitect
        }
    }
}

export const AddArchitectDetail = (detail : keyof architectInformation) => {
    return {
        type: ADD_ARCHITECT_DETAIL,
        payload: {
            detail
        }
    }
}

export const RemoveArchitectDetail = (detail : keyof architectInformation) => {
    return {
        type: REMOVE_ARCHITECT_DETAIL,
        payload: {
            detail
        }
    }
}

export const ChangeArchitectDetail = (detail : keyof architectInformation, value : string | number | number[]) => {
    return {
        type: CHANGE_ARCHITECT_DETAIL,
        payload: {
            detail,
            value
        }
    }
}

export const SaveTemporaryArchitect = () => {
    return {
        type: SAVE_TEMPORARY_ARCHITECT
    }
}