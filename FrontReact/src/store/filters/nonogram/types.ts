import { faBook, faCalendarDay, faCalendarDays, faCubesStacked, faTableCells, faTextHeight, faTextWidth, IconDefinition } from "@fortawesome/free-solid-svg-icons"
import { sortDirections } from "../common"

interface AddOptionName {
    type: typeof ADD_OPTION_NAME,
    payload: {
        optionName: string
    }
}

interface AddNewOptionValue {
    type: typeof ADD_NEW_OPTION_VALUE,
    payload: {
        optionName: string,
        value: number | string
    }
}

interface InitOptionsList {
    type: typeof INIT_OPTIONS_LIST,
    payload: {
        optionsList: nonogramFiltersObject
    }
}

interface UpdateStringValuedFilters {
    type: typeof UPDATE_STRING_FILTERS,
    payload: {
        filters: Array<string>
    }
}

interface UpdateNumberValuedFilters {
    type: typeof UPDATE_NUMBER_FILTERS,
    payload: {
        filters: Array<number>
    }
}

interface AddNonogramSortFilter {
    type: typeof ADD_NONOGRAM_SORT_FILTER,
    payload: {
        filter: sortFilter
    }
}

interface RemoveNononogramSortFilter {
    type: typeof REMOVE_NONOGRAM_SORT_FILTER,
    payload: {
        filter: sortFilter
    }
}

interface ChangeNonogramSortFilter {
    type: typeof CHANGE_NONOGRAM_SORT_FILTER,
    payload: {
        filter: sortFilter
    }
}

export const ADD_NONOGRAM_SORT_FILTER = "ADD_NONOGRAM_SORT_FILTER"
export const REMOVE_NONOGRAM_SORT_FILTER = "REMOVE_NONOGRAM_SORT_FILTER"
export const CHANGE_NONOGRAM_SORT_FILTER = "CHANGE_NONOGRAM_SORT_FILTER"

export const ADD_OPTION_NAME = "ADD_OPTION_NAME"
export const ADD_NEW_OPTION_VALUE = "ADD_NEW_OPTION_VALUE"
export const INIT_OPTIONS_LIST = "INIT_OPTIONS_LIST"
export const UPDATE_STRING_FILTERS = "UPDATE_STRING_FILTERS"
export const UPDATE_NUMBER_FILTERS = "UPDATE_NUMBER_FILTERS"

export type NonogramFiltersActionTypes = AddNonogramSortFilter | RemoveNononogramSortFilter | ChangeNonogramSortFilter | 
    AddOptionName | AddNewOptionValue | InitOptionsList | UpdateStringValuedFilters | UpdateNumberValuedFilters

export type nonogramSortFiltersNames = "source" | "year" | "month" | "difficulty" | "height" | "width" | "area"

export interface nonogramFiltersObject {
    sources: string[],
    years: string[],
    month: string[],
    difficulties: number[],
    heights: number[],
    widths: number[]
}

export interface filterTypeDetails {
    filterName: nonogramSortFiltersNames,
    fontAwesomeIcon: IconDefinition,
    helperText: string
}

export const nonogramSortFilters : filterTypeDetails[] = [
    {
        filterName: "source",
        fontAwesomeIcon : faBook,
        helperText: "Source"
    },
    {
        filterName: "year",
        fontAwesomeIcon: faCalendarDays,
        helperText: "Year"
    },
    {
        filterName: "month",
        fontAwesomeIcon: faCalendarDay,
        helperText: "Month"
    },
    {
        filterName: "difficulty",
        fontAwesomeIcon: faCubesStacked,
        helperText: "Difficulty"
    },
    {
        filterName: "height",
        fontAwesomeIcon: faTextHeight,
        helperText: "Height"
    },
    {
        filterName: "width",
        fontAwesomeIcon: faTextWidth,
        helperText: "Width"
    },
    {
        filterName: "area",
        fontAwesomeIcon: faTableCells,
        helperText: "Area"
    }
]
 
export interface sortFilter {
    filterName: nonogramSortFiltersNames,
    sortDirection: sortDirections 
}

export interface NonogramFiltersState {
    sortFilters: sortFilter[],

    // just selected
    selectedSources: [],
    selectedYears: [],
    selectedMonths: [],
    selectedDifficulties: [],
    selectedWidths: [],
    selectedHeights: [],

    // all included
    sources: [],
    years: [],
    months: [],
    difficulties: [],
    widths: [],
    heights: [],
}