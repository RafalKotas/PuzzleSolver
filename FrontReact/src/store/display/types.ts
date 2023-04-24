export type displayModes = "list" | "grid"

export type modes = "READ" | "CREATE"

interface SetDisplayMode {
    type: typeof SET_DISPLAY_MODE,
    payload: {
        displayMode: displayModes
    }
}

interface SetCurrentPage {
    type: typeof SET_CURRENT_PAGE,
    payload: {
        currentPage : number
    }
}

interface SetItemsPerPage {
    type: typeof SET_ITEMS_PER_PAGE,
    payload: {
        itemsPerPage: number
    }
}

interface SetMode {
    type: typeof SET_MODE,
    payload: {
        mode: modes
    }
}

export const SET_DISPLAY_MODE = "SET_DISPLAY_MODE"
export const SET_CURRENT_PAGE = "SET_CURRENT_PAGE"
export const SET_ITEMS_PER_PAGE = "SET_ITEMS_PER_PAGE"

export const SET_MODE = "SET_MODE"

export type DisplayActionTypes = SetDisplayMode | SetCurrentPage | SetItemsPerPage | SetMode

export interface DisplayState {
    mode: modes,
    displayMode: displayModes,
    currentPage: number,
    itemsPerPage: number
}