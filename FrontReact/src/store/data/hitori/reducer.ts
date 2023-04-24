// redux
import { Reducer } from "redux"
import { DisplayState } from "../../display"

// hitoriData types
import { HitoriDataActionTypes, HitoriDataState, emptyHitori, SET_HITORIS_LIST, SET_SELECTED_HITORI, ADD_TEMPORARY_HITORI, SAVE_TEMPORARY_HITORI, ADD_HITORI_DETAIL, REMOVE_HITORI_DETAIL, CHANGE_HITORI_DETAIL } from "./types"

export const initialState: HitoriDataState = {

    selectedHitori: {
        source: "example",
        difficulty: 5.0,
        height: 4,
        width: 4,
        board: [
            ["1", "1", "1", "1"],
            ["1", "1", "1", "1"],
            ["1", "1", "1", "1"],
            ["1", "1", "1", "1"]
        ],
        label: "Hitori - non solvable",
        value: "Hitori - non solvable"
    },
    hitorisList: [],

    createdHitori: emptyHitori,
    createdHitorisList: [],

    detailsSet: [],

    hitoriCorrect: 0,

    mode: "READ",
    editMode: false
}

export const hitoriDataReducer: Reducer<HitoriDataState, HitoriDataActionTypes> = (state = initialState, action: HitoriDataActionTypes) => {
    switch (action.type) {

        case SET_HITORIS_LIST:
            return {
                ...state,
                hitorisList: action.payload.hitorisList
            }
        case SET_SELECTED_HITORI:
            return {
                ...state,
                selectedHitori: action.payload.selectedHitori
            }
        case ADD_TEMPORARY_HITORI:
            let newTmpHitori = action.payload.temporaryHitori
            return {
                ...state,
                createdHitorisList: [...state.createdHitorisList, newTmpHitori]
            }
        case SAVE_TEMPORARY_HITORI:
            let hitoriToSave = state.createdHitori
            return {
                ...state,
                createdHitorisList: [...state.createdHitorisList, {
                    ...hitoriToSave,
                    board: Array.from({ length: hitoriToSave.height }, () => {
                        return Array.from({ length: hitoriToSave.width }, () => " ")
                    }),
                    label: "TemplateNo" + (state.createdHitorisList.length + 1) + ".json",
                    value: "TemplateNo" + (state.createdHitorisList.length + 1) + ".json"
                }]
            }
        case ADD_HITORI_DETAIL:
            return {
                ...state,
                detailsSet: state.detailsSet.includes(action.payload.detail) ? state.detailsSet : [...state.detailsSet, action.payload.detail]
            }
        case REMOVE_HITORI_DETAIL:
            return {
                ...state,
                detailsSet: state.detailsSet.filter((detail) => detail !== action.payload.detail)
            }
        case CHANGE_HITORI_DETAIL:
            return {
                ...state,
                createdHitori: {
                    ...state.createdHitori,
                    [action.payload.detail]: action.payload.value
                }
            }
        default:
            return state
    }
}

export const selectListFromMode = (displayState: DisplayState, hitoriDataState: HitoriDataState) => {
    return displayState.mode === "READ" ? hitoriDataState.hitorisList : hitoriDataState.createdHitorisList
}

export const selectBoardWidth = (state: HitoriDataState) => {
    return state.selectedHitori.width
}

export const selectBoardHeight = (state: HitoriDataState) => {
    return state.selectedHitori.height
}