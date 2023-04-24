// redux
import { Reducer } from "redux"
import { DisplayState } from "../../display"

// slitherlinkData types
import { SlitherlinkDataActionTypes, SlitherlinkDataState, emptySlitherlink, SET_SLITHERLINKS_LIST, SET_SELECTED_SLITHERLINK, ADD_TEMPORARY_SLITHERLINK, SAVE_TEMPORARY_SLITHERLINK, ADD_SLITHERLINK_DETAIL, REMOVE_SLITHERLINK_DETAIL, CHANGE_SLITHERLINK_DETAIL } from "./types"

export const initialState: SlitherlinkDataState = {

    selectedSlitherlink: {
        source: "Logi",
        year: "2022",
        month: "06_logi-Mix",
        difficulty: 1,
        height: 11,
        width: 11,
        board: [
            [3, -1, 2, 1, -1, 2, 1, -1, 3, 2, 2],
            [1, -1, -1, 1, -1, 3, -1, 1, -1, -1, 2],
            [-1, -1, -1, -1, 3, 1, -1, -1, -1, 2, -1],
            [-1, -1, -1, 2, -1, 2, -1, 1, -1, 2, 3],
            [1, -1, -1, 3, -1, 3, 2, -1, 3, -1, -1],
            [-1, -1, -1, -1, -1, 2, -1, -1, 1, 2, -1],
            [-1, 2, -1, 1, -1, 3, -1, -1, -1, 3, 3],
            [2, 1, -1, 2, 3, -1, 0, 3, -1, -1, -1],
            [3, -1, 2, 1, 2, -1, 1, -1, -1, -1, 2],
            [-1, -1, -1, 2, 0, 2, 2, -1, -1, 3, -1],
            [2, 2, 1, -1, -1, 2, -1, -1, 3, 2, -1]
        ],
        value: "w05370.json",
        label: "w05370.json"
    },
    slitherlinksList: [],

    createdSlitherlink: emptySlitherlink,
    createdSlitherlinksList: [],

    detailsSet: [],

    slitherlinkCorrect: 0,

    mode: "READ",
    editMode: false
}

export const slitherlinkDataReducer: Reducer<SlitherlinkDataState, SlitherlinkDataActionTypes> = (state = initialState, action: SlitherlinkDataActionTypes) => {
    switch (action.type) {

        case SET_SLITHERLINKS_LIST:
            return {
                ...state,
                slitherlinksList: action.payload.slitherlinksList
            }
        case SET_SELECTED_SLITHERLINK:
            return {
                ...state,
                selectedSlitherlink: action.payload.selectedSlitherlink
            }
        case ADD_TEMPORARY_SLITHERLINK:
            let newTmpSlitherlink = action.payload.temporarySlitherlink
            return {
                ...state,
                createdSlitherlinksList: [...state.createdSlitherlinksList, newTmpSlitherlink]
            }
        case SAVE_TEMPORARY_SLITHERLINK:
            let slitherlinkToSave = state.createdSlitherlink
            return {
                ...state,
                createdSlitherlinksList: [...state.createdSlitherlinksList, {
                    ...slitherlinkToSave,
                    board: Array.from({ length: slitherlinkToSave.height }, () => {
                        return Array.from({ length: slitherlinkToSave.width }, () => -1)
                    }),
                    tanksInRows: Array.from({ length: slitherlinkToSave.height }, () => 0),
                    tanksInColumns: Array.from({ length: slitherlinkToSave.width }, () => 0),
                    label: "TemplateNo" + (state.createdSlitherlinksList.length + 1) + ".json",
                    value: "TemplateNo" + (state.createdSlitherlinksList.length + 1) + ".json"
                }]
            }
        case ADD_SLITHERLINK_DETAIL:
            return {
                ...state,
                detailsSet: state.detailsSet.includes(action.payload.detail) ? state.detailsSet : [...state.detailsSet, action.payload.detail]
            }
        case REMOVE_SLITHERLINK_DETAIL:
            return {
                ...state,
                detailsSet: state.detailsSet.filter((detail) => detail !== action.payload.detail)
            }
        case CHANGE_SLITHERLINK_DETAIL:
            return {
                ...state,
                createdSlitherlink: {
                    ...state.createdSlitherlink,
                    [action.payload.detail]: action.payload.value
                }
            }
        default:
            return state
    }
}

export const selectListFromMode = (displayState: DisplayState, slitherlinkDataState: SlitherlinkDataState) => {
    return displayState.mode === "READ" ? slitherlinkDataState.slitherlinksList : slitherlinkDataState.createdSlitherlinksList
}

export const selectBoardWidth = (state: SlitherlinkDataState) => {
    return state.selectedSlitherlink.width
}

export const selectBoardHeight = (state: SlitherlinkDataState) => {
    return state.selectedSlitherlink.height
}