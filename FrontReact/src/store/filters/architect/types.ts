import { faBook, faCubesStacked, faTableCells, faTextHeight, faTextWidth, IconDefinition } from "@fortawesome/free-solid-svg-icons"
import { sortDirections } from "../common"

export type architectSortFiltersNames = "source" | "difficulty" | "height" | "width" | "area"

export interface architectOptions {
    source : Array<string>,
    year: Array<string>,
    month: Array<string>,
    difficulty : Array<number>,
    height: Array<number>,
    width: Array<number>
}

export interface filterTypeDetails {
    filterName: architectSortFiltersNames,
    fontAwesomeIcon: IconDefinition,
    helperText: string
}

export const architectSortFilters : filterTypeDetails[] = [
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
    filterName: architectSortFiltersNames,
    sortDirection: sortDirections 
}

export interface ArchitectFiltersState {
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
        optionsList: architectOptions
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

interface AddArchitectSortFilter {
    type: typeof ADD_ARCHITECT_SORT_FILTER,
    payload: {
        filter: sortFilter
    }
}

interface RemoveArchitectSortFilter {
    type: typeof REMOVE_ARCHITECT_SORT_FILTER,
    payload: {
        filter: sortFilter
    }
}

interface ChangeArchitectSortFilter {
    type: typeof CHANGE_ARCHITECT_SORT_FILTER,
    payload: {
        filter: sortFilter
    }
}

export const ADD_ARCHITECT_SORT_FILTER = "ADD_ARCHITECT_SORT_FILTER"
export const REMOVE_ARCHITECT_SORT_FILTER = "REMOVE_ARCHITECT_SORT_FILTER"
export const CHANGE_ARCHITECT_SORT_FILTER = "CHANGE_ARCHITECT_SORT_FILTER"

export const ADD_OPTION_NAME = "ADD_OPTION_NAME"
export const ADD_NEW_OPTION_VALUE = "ADD_NEW_OPTION_VALUE"
export const INIT_OPTIONS_LIST = "INIT_OPTIONS_LIST"
export const UPDATE_STRING_FILTERS = "UPDATE_STRING_FILTERS"
export const UPDATE_NUMBER_FILTERS = "UPDATE_NUMBER_FILTERS"

export type ArchitectFiltersActionTypes = AddArchitectSortFilter | RemoveArchitectSortFilter | ChangeArchitectSortFilter | 
    AddOptionName | AddNewOptionValue | InitOptionsList | UpdateStringValuedFilters | UpdateNumberValuedFilters