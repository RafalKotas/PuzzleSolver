import { Reducer } from "redux"
import { AddNonogramSortFilter, ChangeNonogramSortFilter, RemoveNonogramSortFilter } from "./actions"
import { NonogramFiltersActionTypes, NonogramFiltersState, nonogramSortFiltersNames, sortFilter,
    ADD_NONOGRAM_SORT_FILTER, 
    REMOVE_NONOGRAM_SORT_FILTER, 
    CHANGE_NONOGRAM_SORT_FILTER, 
    INIT_OPTIONS_LIST,
     UPDATE_STRING_FILTERS, 
     UPDATE_NUMBER_FILTERS } from "./types"
import { sortDirections } from "../common"

export const initialState: NonogramFiltersState = {

    sortFilters: [],

    selectedSources: [],
    selectedYears: [],
    selectedMonths: [],
    selectedDifficulties: [],
    selectedWidths: [],
    selectedHeights: [],

    sources: [],
    years: [],
    months: [],
    difficulties: [],
    widths: [],
    heights: [],

}

export const nonogramFiltersReducer: Reducer<NonogramFiltersState, NonogramFiltersActionTypes> = 
    (state = initialState, action : NonogramFiltersActionTypes) => {
    
    switch (action.type) {
        case ADD_NONOGRAM_SORT_FILTER:
            return {
                ...state,
                sortFilters: [...state.sortFilters, action.payload.filter]
            }
        case REMOVE_NONOGRAM_SORT_FILTER:
            return {
                ...state,
                sortFilters: state.sortFilters.filter((filter) => { return filter.filterName !== action.payload.filter.filterName })
            }
        case CHANGE_NONOGRAM_SORT_FILTER:
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
                    case "sources":
                        updatedState.sources = valuesList
                        updatedState.selectedSources = valuesList
                        break
                    case "years":
                        updatedState.years = valuesList
                        updatedState.selectedYears = valuesList
                        break
                    case "months":
                        updatedState.months = valuesList
                        updatedState.selectedMonths = valuesList
                        break
                    case "difficulties":
                        updatedState.difficulties = valuesList
                        break
                    case "heights":
                        updatedState.heights = valuesList
                        break
                    case "widths":
                        updatedState.widths = valuesList
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

export const selectFilterAction = (state : NonogramFiltersState, choosenSortFilter : sortFilter) => {
    
    let possibleEntryWithFilter = state.sortFilters.filter((sortFilterEntry) => {
        return sortFilterEntry.filterName === choosenSortFilter.filterName
    })

    // if entry exists
    if(possibleEntryWithFilter.length === 1) {
        if(possibleEntryWithFilter[0].sortDirection === choosenSortFilter.sortDirection) {
            return RemoveNonogramSortFilter(choosenSortFilter)
        } else {
            return ChangeNonogramSortFilter(choosenSortFilter)
        }
    } else {
        return AddNonogramSortFilter(choosenSortFilter)
    }
}

export const filterWithDirectionIncludedSelector = (state : NonogramFiltersState, 
    filterName : nonogramSortFiltersNames, filterDirection: sortDirections) => {
        return (state.sortFilters.filter((sortFilterEntry) => {
            return sortFilterEntry.filterName === filterName && sortFilterEntry.sortDirection === filterDirection
        })).length > 0 ? true : false
}


export const selectFilterNamePriorityIndicator = (state : NonogramFiltersState, filterName: nonogramSortFiltersNames) => {
    let filterIndex = (state.sortFilters.findIndex((sortFilter) => sortFilter.filterName === filterName) + 1 )
    return filterIndex || 100 
}

export const extractSelectionFilters = (filtersState : NonogramFiltersState) => {
    return {
        selectedSources: filtersState.selectedSources,
        selectedYears: filtersState.selectedYears,
        selectedMonths: filtersState.selectedMonths,
        selectedDifficulties: filtersState.selectedDifficulties,
        selectedWidths: filtersState.selectedWidths,
        selectedHeights: filtersState.selectedHeights,
    }
}