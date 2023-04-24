import { Reducer } from "redux"
import { AddSlitherlinkSortFilter, ChangeSlitherlinkSortFilter, RemoveSlitherlinkSortFilter} from "./actions"
import { SlitherlinkFiltersActionTypes, SlitherlinkFiltersState, slitherlinkSortFiltersNames, sortFilter,
    ADD_SLITHERLINK_SORT_FILTER, 
    REMOVE_SLITHERLINK_SORT_FILTER, 
    CHANGE_SLITHERLINK_SORT_FILTER, 
    INIT_OPTIONS_LIST,
     UPDATE_STRING_FILTERS, 
     UPDATE_NUMBER_FILTERS } from "./types"
import { sortDirections } from "../common"

export const initialState: SlitherlinkFiltersState = {

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

export const slitherlinkFiltersReducer: Reducer<SlitherlinkFiltersState, SlitherlinkFiltersActionTypes> = 
    (state = initialState, action : SlitherlinkFiltersActionTypes) => {
    
    switch (action.type) {
        case ADD_SLITHERLINK_SORT_FILTER:
            return {
                ...state,
                sortFilters: [...state.sortFilters, action.payload.filter]
            }
        case REMOVE_SLITHERLINK_SORT_FILTER:
            return {
                ...state,
                sortFilters: state.sortFilters.filter((filter) => { return filter.filterName !== action.payload.filter.filterName })
            }
        case CHANGE_SLITHERLINK_SORT_FILTER:
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
                    case "height":
                        updatedState.heights = valuesList
                        break
                    case "width":
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

export const selectFilterAction = (state : SlitherlinkFiltersState, choosenSortFilter : sortFilter) => {
    
    let possibleEntryWithFilter = state.sortFilters.filter((sortFilterEntry) => {
        return sortFilterEntry.filterName === choosenSortFilter.filterName
    })

    // if entry exists
    if(possibleEntryWithFilter.length === 1) {
        if(possibleEntryWithFilter[0].sortDirection === choosenSortFilter.sortDirection) {
            return RemoveSlitherlinkSortFilter(choosenSortFilter)
        } else {
            return ChangeSlitherlinkSortFilter(choosenSortFilter)
        }
    } else {
        return AddSlitherlinkSortFilter(choosenSortFilter)
    }
}

export const filterWithDirectionIncludedSelector = (state : SlitherlinkFiltersState, 
    filterName : slitherlinkSortFiltersNames, filterDirection: sortDirections) => {
        return (state.sortFilters.filter((sortFilterEntry) => {
            return sortFilterEntry.filterName === filterName && sortFilterEntry.sortDirection === filterDirection
        })).length > 0 ? true : false
}


export const selectFilterNamePriorityIndicator = (state : SlitherlinkFiltersState, filterName: slitherlinkSortFiltersNames) => {
    let filterIndex = (state.sortFilters.findIndex((sortFilter) => sortFilter.filterName === filterName) + 1 )
    return filterIndex || 100 
}