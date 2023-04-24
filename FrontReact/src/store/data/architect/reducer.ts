// redux
import { Reducer } from "redux"
import { DisplayState } from "../../display"

// architectData types
import { ADD_ARCHITECT_DETAIL, ADD_TEMPORARY_ARCHITECT, ArchitectDataActionTypes, ArchitectDataState, CHANGE_ARCHITECT_DETAIL, emptyArchitect, REMOVE_ARCHITECT_DETAIL, SAVE_TEMPORARY_ARCHITECT, SET_ARCHITECTS_LIST, SET_SELECTED_ARCHITECT } from "./types"

export const initialState: ArchitectDataState = {
    
    selectedArchitect: {
        source: "Logi",
        year : "2022",
        month : "07_logi-Mix",
        difficulty : 1,
        height: 12,
        width: 12,
        tanksInRows: [4, 1, 5, 1, 3, 3, 2, 2, 4, 1, 2, 3],
        tanksInColumns: [5, 0, 5, 1, 1, 4, 0, 5, 0, 4, 2, 4],
        board: [
            ["-", "-", "-", "-", "-", "-", "-", "H", "-", "-", "-", "H"], 
            ["H", "-", "H", "-", "H", "-", "-", "-", "-", "-", "-", "-"],
            ["-", "-", "-", "-", "H", "-", "-", "-", "-", "-", "H", "-"],
            ["H", "-", "-", "-", "-", "H", "-", "-", "-", "H", "-", "H"],
            ["-", "-", "-", "-", "-", "-", "H", "H", "-", "-", "-", "-"],
            ["-", "-", "-", "-", "-", "-", "H", "-", "-", "-", "-", "H"],
            ["H", "-", "H", "-", "-", "-", "-", "-", "-", "-", "H", "-"],
            ["-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "H"],
            ["H", "-", "H", "-", "-", "-", "H", "-", "-", "-", "H", "-"],
            ["H", "H", "-", "-", "-", "H", "-", "-", "-", "-", "H", "H"],
            ["-", "-", "-", "-", "-", "H", "-", "-", "-", "-", "-", "-"],
            ["-", "H", "-", "-", "-", "-", "-", "-", "H", "-", "-", "-"]
        ],
        label: "a05367.json",
        value: "a05367.json"
    },
    architectsList : [],
    
    createdArchitect: emptyArchitect,
    createdArchitectsList : [],

    detailsSet: [],

    architectCorrect: 0,

    mode: "READ",
    editMode: false
}

export const architectDataReducer: Reducer<ArchitectDataState, ArchitectDataActionTypes> = (state = initialState, action : ArchitectDataActionTypes) => {
    switch (action.type) {

        case SET_ARCHITECTS_LIST:
            return {
                ...state,
                architectsList: action.payload.architectsList
            }
        case SET_SELECTED_ARCHITECT:
            return {...state,
                selectedArchitect: action.payload.selectedArchitect
            }
        case ADD_TEMPORARY_ARCHITECT:
            let newTmpArchitect = action.payload.temporaryArchitect
            return {
                ...state,
                createdArchitectsList: [...state.createdArchitectsList, newTmpArchitect]
            }
        case SAVE_TEMPORARY_ARCHITECT:
            let architectToSave = state.createdArchitect
            return {
                ...state,
                createdArchitectsList: [...state.createdArchitectsList, {
                    ...architectToSave,
                    board: Array.from({ length: architectToSave.height }, () => {
                        return Array.from({ length: architectToSave.width }, () => "-")
                    }),
                    tanksInRows: Array.from({ length: architectToSave.height }, () => 0),
                    tanksInColumns: Array.from({ length: architectToSave.width }, () => 0),
                    label: "TemplateNo" + (state.createdArchitectsList.length + 1) + ".json",
                    value: "TemplateNo" + (state.createdArchitectsList.length + 1) + ".json"
                }]
            }
        case ADD_ARCHITECT_DETAIL:
            return {
                ...state,
                detailsSet: state.detailsSet.includes(action.payload.detail) ? state.detailsSet : [...state.detailsSet, action.payload.detail]
            }
        case REMOVE_ARCHITECT_DETAIL:
            return {
                ...state,
                detailsSet: state.detailsSet.filter((detail) => detail !== action.payload.detail)
            }
        case CHANGE_ARCHITECT_DETAIL:
            return {
                ...state,
                createdArchitect: {
                    ...state.createdArchitect,
                    [action.payload.detail]: action.payload.value
                }
            }
        default:
            return state
      }
}

export const selectListFromMode = (displayState : DisplayState, architectDataState : ArchitectDataState) => {
    return displayState.mode === "READ" ? architectDataState.architectsList : architectDataState.createdArchitectsList
}