import { 
    sortFilter,
    ADD_SUDOKU_SORT_FILTER, 
    ADD_NEW_OPTION_VALUE, 
    ADD_OPTION_NAME, 
    CHANGE_SUDOKU_SORT_FILTER, 
    INIT_OPTIONS_LIST,
    REMOVE_SUDOKU_SORT_FILTER,
    UPDATE_NUMBER_FILTERS,
    UPDATE_STRING_FILTERS, 
    sudokuOptions} from "./types"

export const AddSudokuSortFilter = (filter : sortFilter) => {
    return {
        type: ADD_SUDOKU_SORT_FILTER,
        payload: {
            filter: filter
        }
    }
}

export const RemoveSudokuSortFilter = (filter : sortFilter) => {
    return {
        type: REMOVE_SUDOKU_SORT_FILTER,
        payload: {
            filter: filter
        }
    }
}

export const ChangeSudokuSortFilter = (filter : sortFilter) => {
    return {
        type: CHANGE_SUDOKU_SORT_FILTER,
        payload: {
            filter: filter
        }
    }
}

export const UpdateStringValuedFilters = (optionName : string, filters : Array<string>) => {
    return {
      type: UPDATE_STRING_FILTERS,
      payload: {[optionName] : filters}
    }
}

export const UpdateNumberValuedFilters = (optionName : string, filters: Array<number>) => {
    return {
        type: UPDATE_NUMBER_FILTERS,
        payload: {[optionName] : filters}
    }
}

export const AddOptionName = (optionName: string) => {
    return {
        type: ADD_OPTION_NAME,
        payload : {
            optionName
        }
    }
}

export const AddNewOptionValue = (optionName: string, optionValue: string | number) => {
    return {
        type: ADD_NEW_OPTION_VALUE,
        payload : {
            optionName,
            optionValue
        }
    }
}

export const InitOptionsList = (optionsList: sudokuOptions) => {
    return {
        type: INIT_OPTIONS_LIST,
        payload : {
            optionsList
        }
    }
}