// redux
import { Reducer } from "redux"
import { DisplayState } from "../../display"

// akariData types
import { ADD_AKARI_DETAIL, ADD_TEMPORARY_AKARI, AkariDataActionTypes, AkariDataState, CHANGE_AKARI_DETAIL, emptyAkari, REMOVE_AKARI_DETAIL, SAVE_TEMPORARY_AKARI, SET_AKARIS_LIST, SET_SELECTED_AKARI } from "./types"

export const initialState: AkariDataState = {
    
    selectedAkari: {
        source: "pazyl_pl",
        difficulty : 2.5,
        height: 7,
        width : 7,
        board : [
            ["-", "-", "-", "-", "-", "-", "-"],
            ["-", "-", "0", "0", "-", "-", "-"],
            ["-", "-", "-", "-", "1", "-", "-"],
            ["1", "0", "-", "0", "-", "-", "-"],
            ["1", "-", "-", "-", "-", "-", "2"],
            ["-", "-", "-", "-", "1", "-", "-"],
            ["-", "1", "-", "-", "-", "-", "-"]],
        label : "Zadanie logiczne 655 - Akari 7 x 7",
        value : "Zadanie logiczne 655 - Akari 7 x 7"
    },
    akarisList : [],
    
    createdAkari: emptyAkari,
    createdAkarisList : [],

    detailsSet: [],

    akariCorrect: 0,

    editMode: false
}

export const akariDataReducer: Reducer<AkariDataState, AkariDataActionTypes> = (state = initialState, action : AkariDataActionTypes) => {
    switch (action.type) {

        case SET_AKARIS_LIST:
            return {
                ...state,
                akarisList: action.payload.akarisList
            }
        case SET_SELECTED_AKARI:
            return {
                ...state,
                selectedAkari: action.payload.selectedAkari
            }
        case ADD_TEMPORARY_AKARI:
            let newTmpAkari = action.payload.temporaryAkari
            return {
                ...state,
                createdAkarisList: [...state.createdAkarisList, newTmpAkari]
            }
        case SAVE_TEMPORARY_AKARI:
            let akariToSave = state.createdAkari
            return {
                ...state,
                createdAkarisList: [...state.createdAkarisList, {
                    ...akariToSave,
                    board: Array.from({length: akariToSave.height}, () => {
                        return Array.from({length: akariToSave.width}, () => "-")
                    }),
                    label: "TemplateNo" + (state.createdAkarisList.length + 1) + ".json",
                    value: "TemplateNo" + (state.createdAkarisList.length + 1) + ".json"
                }]
            }
        case ADD_AKARI_DETAIL:
            return {
                ...state,
                detailsSet: state.detailsSet.includes(action.payload.detail) ? state.detailsSet : [...state.detailsSet, action.payload.detail]
            }
        case REMOVE_AKARI_DETAIL:
            return {
                ...state,
                detailsSet: state.detailsSet.filter((detail) => detail !== action.payload.detail)
            }
        case CHANGE_AKARI_DETAIL:
            return {
                ...state,
                createdAkari: {
                    ...state.createdAkari,
                    [action.payload.detail]: action.payload.value
                }
            }
        default:
            return state
      }
}

export const selectListFromMode = (displayState : DisplayState, akariDataState: AkariDataState) => {
    return displayState.mode === "READ" ? akariDataState.akarisList : akariDataState.createdAkarisList
}