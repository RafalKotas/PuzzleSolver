import { faBook, faCubesStacked, faTableCells, faTextHeight, faTextWidth, IconDefinition } from "@fortawesome/free-solid-svg-icons"
import { sortDirections } from "../common"

export type slitherlinkSortFiltersNames = "source" | "year" | "month" | "difficulty" | "height" | "width" | "area"

export interface slitherlinkOptions {
    source : Array<string>,
    year: Array<string>,
    month: Array<string>,
    difficulty : Array<number>,
    height: Array<number>,
    width: Array<number>
}

export interface filterTypeDetails {
    filterName: slitherlinkSortFiltersNames,
    fontAwesomeIcon: IconDefinition,
    helperText: string
}

export const slitherlinkSortFilters : filterTypeDetails[] = [
    {
        filterName: "source",
        fontAwesomeIcon : faBook,
        helperText: "Source"
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
    filterName: slitherlinkSortFiltersNames,
    sortDirection: sortDirections 
}

export interface SlitherlinkFiltersState {
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
        optionsList: slitherlinkOptions
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

interface AddSlitherlinkSortFilter {
    type: typeof ADD_SLITHERLINK_SORT_FILTER,
    payload: {
        filter: sortFilter
    }
}

interface RemoveSlitherlinkSortFilter {
    type: typeof REMOVE_SLITHERLINK_SORT_FILTER,
    payload: {
        filter: sortFilter
    }
}

interface ChangeSlitherlinkSortFilter {
    type: typeof CHANGE_SLITHERLINK_SORT_FILTER,
    payload: {
        filter: sortFilter
    }
}

export const ADD_SLITHERLINK_SORT_FILTER = "ADD_SLITHERLINK_SORT_FILTER"
export const REMOVE_SLITHERLINK_SORT_FILTER = "REMOVE_SLITHERLINK_SORT_FILTER"
export const CHANGE_SLITHERLINK_SORT_FILTER = "CHANGE_SLITHERLINK_SORT_FILTER"

export const ADD_OPTION_NAME = "ADD_OPTION_NAME"
export const ADD_NEW_OPTION_VALUE = "ADD_NEW_OPTION_VALUE"
export const INIT_OPTIONS_LIST = "INIT_OPTIONS_LIST"
export const UPDATE_STRING_FILTERS = "UPDATE_STRING_FILTERS"
export const UPDATE_NUMBER_FILTERS = "UPDATE_NUMBER_FILTERS"

export type SlitherlinkFiltersActionTypes = AddSlitherlinkSortFilter | RemoveSlitherlinkSortFilter | ChangeSlitherlinkSortFilter | 
    AddOptionName | AddNewOptionValue | InitOptionsList | UpdateStringValuedFilters | UpdateNumberValuedFilters