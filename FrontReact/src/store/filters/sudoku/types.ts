import { fa9, faBook, faCubesStacked, IconDefinition } from "@fortawesome/free-solid-svg-icons"
import { sortDirections } from "../common"

export type sudokuSortFiltersNames = "source" | "difficulty" | "filled"

export interface sudokuOptions {
    source : Array<string>,
    year: Array<string>,
    month: Array<string>,
    difficulty : Array<number>,
    filled: Array<number>
}

export interface filterTypeDetails {
    filterName: sudokuSortFiltersNames,
    fontAwesomeIcon: IconDefinition,
    helperText: string
}

export const sudokuSortFilters : filterTypeDetails[] = [
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
        filterName: "filled",
        fontAwesomeIcon: fa9,
        helperText: "Filled cells"
    }
]
 
export interface sortFilter {
    filterName: sudokuSortFiltersNames,
    sortDirection: sortDirections 
}

export interface SudokuFiltersState {
    sortFilters: sortFilter[],

    // only selected
    selectedSources: [],
    selectedYears: [],
    selectedMonths: [],
    selectedDifficulties: [],
    selectedFilled: [],

    // all included
    sources: [],
    years: [],
    months: [],
    difficulties: [],
    filled: []
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
        optionsList: sudokuOptions
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

interface AddSudokuSortFilter {
    type: typeof ADD_SUDOKU_SORT_FILTER,
    payload: {
        filter: sortFilter
    }
}

interface RemoveSudokuSortFilter {
    type: typeof REMOVE_SUDOKU_SORT_FILTER,
    payload: {
        filter: sortFilter
    }
}

interface ChangeSudokuSortFilter {
    type: typeof CHANGE_SUDOKU_SORT_FILTER,
    payload: {
        filter: sortFilter
    }
}

export const ADD_SUDOKU_SORT_FILTER = "ADD_SUDOKU_SORT_FILTER"
export const REMOVE_SUDOKU_SORT_FILTER = "REMOVE_SUDOKU_SORT_FILTER"
export const CHANGE_SUDOKU_SORT_FILTER = "CHANGE_SUDOKU_SORT_FILTER"

export const ADD_OPTION_NAME = "ADD_OPTION_NAME"
export const ADD_NEW_OPTION_VALUE = "ADD_NEW_OPTION_VALUE"
export const INIT_OPTIONS_LIST = "INIT_OPTIONS_LIST"
export const UPDATE_STRING_FILTERS = "UPDATE_STRING_FILTERS"
export const UPDATE_NUMBER_FILTERS = "UPDATE_NUMBER_FILTERS"

export type SudokuFiltersActionTypes = AddSudokuSortFilter | RemoveSudokuSortFilter | ChangeSudokuSortFilter | 
    AddOptionName | AddNewOptionValue | InitOptionsList | UpdateStringValuedFilters | UpdateNumberValuedFilters