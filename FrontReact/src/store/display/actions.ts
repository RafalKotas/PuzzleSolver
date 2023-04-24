import * as displayTypes from "./types"

export const SetMode = (mode : displayTypes.modes) => {
    return {
        type: displayTypes.SET_MODE,
        payload: {
            mode: mode
        }
    }
}

export const SetDisplayMode = (displayMode : displayTypes.displayModes) => {
    return {
        type: displayTypes.SET_DISPLAY_MODE,
        payload: {
            displayMode: displayMode
        }
    }
}

export const SetCurrentPage = (updatedCurrentPage: number) => {
    return {
        type: displayTypes.SET_CURRENT_PAGE,
        payload: {
            currentPage: updatedCurrentPage
        }
    }
}

export const SetItemsPerPage = (updatedItemsPerPage: number) => {
    return {
        type: displayTypes.SET_ITEMS_PER_PAGE,
        payload: {
            itemsPerPage: updatedItemsPerPage
        }
    }
}