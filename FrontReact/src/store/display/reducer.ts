import { Reducer } from 'redux'
import {DisplayActionTypes, DisplayState, SET_CURRENT_PAGE, SET_DISPLAY_MODE, SET_ITEMS_PER_PAGE, SET_MODE} from './types'

export const initialState: DisplayState = {
    mode: "READ",
    displayMode: "list",
    currentPage: 1,
    itemsPerPage: 30

}

export const displayReducer: Reducer<DisplayState, DisplayActionTypes> = (state = initialState, action : DisplayActionTypes) => {
    switch (action.type) {
        case SET_MODE:
            let updatedMode = action.payload.mode
            return {
                ...state,
                mode: updatedMode
            }
        case SET_DISPLAY_MODE:
            return {
                ...state,
                displayMode: action.payload.displayMode
            }
        case SET_CURRENT_PAGE:
            return {
                ...state,
                currentPage: action.payload.currentPage
            }
        case SET_ITEMS_PER_PAGE:
            return {
                ...state,
                itemsPerPage: action.payload.itemsPerPage
            }
        default:
            return state
      }
}
