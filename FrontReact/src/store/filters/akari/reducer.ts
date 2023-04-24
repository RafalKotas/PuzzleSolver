import { Reducer } from "redux"
import { AddAkariSortFilter, ChangeAkariSortFilter, RemoveAkariSortFilter} from "./actions"
import { AkariFiltersActionTypes, AkariFiltersState, akariSortFiltersNames, sortFilter,
    ADD_AKARI_SORT_FILTER, 
    REMOVE_AKARI_SORT_FILTER, 
    CHANGE_AKARI_SORT_FILTER, 
    INIT_OPTIONS_LIST,
     UPDATE_STRING_FILTERS, 
     UPDATE_NUMBER_FILTERS } from "./types"
import { sortDirections } from "../common"

export const initialState: AkariFiltersState = {

    sortFilters: [],

    selectedSources: [],
    selectedDifficulties: [],
    selectedWidths: [],
    selectedHeights: [],

    sources: [],
    difficulties: [],
    widths: [],
    heights: [],

}

export const akariFiltersReducer: Reducer<AkariFiltersState, AkariFiltersActionTypes> = 
    (state = initialState, action : AkariFiltersActionTypes) => {
    
    switch (action.type) {
        case ADD_AKARI_SORT_FILTER:
            return {
                ...state,
                sortFilters: [...state.sortFilters, action.payload.filter]
            }
        case REMOVE_AKARI_SORT_FILTER:
            return {
                ...state,
                sortFilters: state.sortFilters.filter((filter) => { return filter.filterName !== action.payload.filter.filterName })
            }
        case CHANGE_AKARI_SORT_FILTER:
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

export const selectFilterAction = (state : AkariFiltersState, choosenSortFilter : sortFilter) => {
    
    let possibleEntryWithFilter = state.sortFilters.filter((sortFilterEntry) => {
        return sortFilterEntry.filterName === choosenSortFilter.filterName
    })

    // if entry exists
    if(possibleEntryWithFilter.length === 1) {
        if(possibleEntryWithFilter[0].sortDirection === choosenSortFilter.sortDirection) {
            return RemoveAkariSortFilter(choosenSortFilter)
        } else {
            return ChangeAkariSortFilter(choosenSortFilter)
        }
    } else {
        return AddAkariSortFilter(choosenSortFilter)
    }
}

export const filterWithDirectionIncludedSelector = (state : AkariFiltersState, 
    filterName : akariSortFiltersNames, filterDirection: sortDirections) => {
        return (state.sortFilters.filter((sortFilterEntry) => {
            return sortFilterEntry.filterName === filterName && sortFilterEntry.sortDirection === filterDirection
        })).length > 0 ? true : false
}


export const selectFilterNamePriorityIndicator = (state : AkariFiltersState, filterName: akariSortFiltersNames) => {
    let filterIndex = (state.sortFilters.findIndex((sortFilter) => sortFilter.filterName === filterName) + 1 )
    return filterIndex || 100 
}