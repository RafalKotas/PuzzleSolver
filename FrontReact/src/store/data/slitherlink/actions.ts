import { slitherlinkInformation, SET_SLITHERLINKS_LIST, SET_SELECTED_SLITHERLINK, ADD_SLITHERLINK_DETAIL, REMOVE_SLITHERLINK_DETAIL, CHANGE_SLITHERLINK_DETAIL, SAVE_TEMPORARY_SLITHERLINK } from "./types"

export const SetSlitherlinksList = (slitherlinksList : slitherlinkInformation[]) => {
    return {
        type: SET_SLITHERLINKS_LIST,
        payload: {
            slitherlinksList
        }
    }
}

export const SetSelectedSlitherlink = (selectedSlitherlink : slitherlinkInformation) => {
    return {
        type: SET_SELECTED_SLITHERLINK,
        payload: {
            selectedSlitherlink
        }
    }
}

export const AddSlitherlinkDetail = (detail : keyof slitherlinkInformation) => {
    return {
        type: ADD_SLITHERLINK_DETAIL,
        payload: {
            detail
        }
    }
}

export const RemoveSlitherlinkDetail = (detail : keyof slitherlinkInformation) => {
    return {
        type: REMOVE_SLITHERLINK_DETAIL,
        payload: {
            detail
        }
    }
}

export const ChangeSlitherlinkDetail = (detail : keyof slitherlinkInformation, value : string | number | number[]) => {
    return {
        type: CHANGE_SLITHERLINK_DETAIL,
        payload: {
            detail,
            value
        }
    }
}

export const SaveTemporarySlitherlink = () => {
    return {
        type: SAVE_TEMPORARY_SLITHERLINK
    }
}