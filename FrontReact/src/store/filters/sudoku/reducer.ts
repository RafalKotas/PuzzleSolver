import { Reducer } from "redux"
import { AddSudokuSortFilter, ChangeSudokuSortFilter, RemoveSudokuSortFilter} from "./actions"
import { SudokuFiltersActionTypes, SudokuFiltersState, sudokuSortFiltersNames, sortFilter,
    ADD_SUDOKU_SORT_FILTER, 
    REMOVE_SUDOKU_SORT_FILTER, 
    CHANGE_SUDOKU_SORT_FILTER, 
    INIT_OPTIONS_LIST,
     UPDATE_STRING_FILTERS, 
     UPDATE_NUMBER_FILTERS } from "./types"
import { sortDirections } from "../common"

export const initialState: SudokuFiltersState = {

    sortFilters: [],

    selectedSources: [],
    selectedYears: [],
    selectedMonths: [],
    selectedDifficulties: [],
    selectedFilled: [],

    sources: [],
    years: [],
    months: [],
    difficulties: [],
    filled: []

}

export const sudokuFiltersReducer: Reducer<SudokuFiltersState, SudokuFiltersActionTypes> = 
    (state = initialState, action : SudokuFiltersActionTypes) => {
    
    switch (action.type) {
        case ADD_SUDOKU_SORT_FILTER:
            return {
                ...state,
                sortFilters: [...state.sortFilters, action.payload.filter]
            }
        case REMOVE_SUDOKU_SORT_FILTER:
            return {
                ...state,
                sortFilters: state.sortFilters.filter((filter) => { return filter.filterName !== action.payload.filter.filterName })
            }
        case CHANGE_SUDOKU_SORT_FILTER:
            return {
                ...state,
                sortFilters: state.sortFilters.map((sortFilter) => {
                    return sortFilter.filterName !== action.payload.filter.filterName ? sortFilter : action.payload.filter
                })
            }
        case INIT_OPTIONS_LIST:
            let optionsList = action.payload.optionsList
            let updatedState = state
            Object.entries(optionsList).forEach(([option, valuesList]) => {
                switch (option) {
                    case "source":
                        updatedState.sources = valuesList
                        break
                    case "difficulty":
                        updatedState.difficulties = valuesList
                        break
                    case "filled":
                        updatedState.filled = valuesList
                        break
                    default:
                        break
                }
            })
            return updatedState
        case UPDATE_STRING_FILTERS:
            return { ...state, ...action.payload }
        case UPDATE_NUMBER_FILTERS:
            return { ...state, ...action.payload }
        default:
            return state
      }

}

export const selectFilterAction = (state : SudokuFiltersState, choosenSortFilter : sortFilter) => {
    
    let possibleEntryWithFilter = state.sortFilters.filter((sortFilterEntry) => {
        return sortFilterEntry.filterName === choosenSortFilter.filterName
    })

    // if entry exists
    if(possibleEntryWithFilter.length === 1) {
        if(possibleEntryWithFilter[0].sortDirection === choosenSortFilter.sortDirection) {
            return RemoveSudokuSortFilter(choosenSortFilter)
        } else {
            return ChangeSudokuSortFilter(choosenSortFilter)
        }
    } else {
        return AddSudokuSortFilter(choosenSortFilter)
    }
}

export const filterWithDirectionIncludedSelector = (state : SudokuFiltersState, 
    filterName : sudokuSortFiltersNames, filterDirection: sortDirections) => {
        return (state.sortFilters.filter((sortFilterEntry) => {
            return sortFilterEntry.filterName === filterName && sortFilterEntry.sortDirection === filterDirection
        })).length > 0 ? true : false
}


export const selectFilterNamePriorityIndicator = (state : SudokuFiltersState, filterName: sudokuSortFiltersNames) => {
    let filterIndex = (state.sortFilters.findIndex((sortFilter) => sortFilter.filterName === filterName) + 1 )
    return filterIndex || 100 
}