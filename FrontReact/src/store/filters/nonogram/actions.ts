import * as nonogramFilterTypes from "./types"

export const AddNonogramSortFilter = (filter : nonogramFilterTypes.sortFilter) => {
    return {
        type: nonogramFilterTypes.ADD_NONOGRAM_SORT_FILTER,
        payload: {
            filter: filter
        }
    }
}

export const RemoveNonogramSortFilter = (filter : nonogramFilterTypes.sortFilter) => {
    return {
        type: nonogramFilterTypes.REMOVE_NONOGRAM_SORT_FILTER,
        payload: {
            filter: filter
        }
    }
}

export const ChangeNonogramSortFilter = (filter : nonogramFilterTypes.sortFilter) => {
    return {
        type: nonogramFilterTypes.CHANGE_NONOGRAM_SORT_FILTER,
        payload: {
            filter: filter
        }
    }
}

export const UpdateStringValuedFilters = (optionName : string, filters : Array<string>) => {
    return {
      type: nonogramFilterTypes.UPDATE_STRING_FILTERS,
      payload: {[optionName] : filters}
    }
}

export const UpdateNumberValuedFilters = (optionName : string, filters: Array<number>) => {
    return {
        type: nonogramFilterTypes.UPDATE_NUMBER_FILTERS,
        payload: {[optionName] : filters}
    }
}

export const AddOptionName = (optionName: string) => {
    return {
        type: nonogramFilterTypes.ADD_OPTION_NAME,
        payload : {
            optionName
        }
    }
}

export const AddNewOptionValue = (optionName: string, optionValue: string | number) => {
    return {
        type: nonogramFilterTypes.ADD_NEW_OPTION_VALUE,
        payload : {
            optionName,
            optionValue
        }
    }
}

export const InitOptionsList = (optionsList: nonogramFilterTypes.nonogramFiltersObject) => {
    return {
        type: nonogramFilterTypes.INIT_OPTIONS_LIST,
        payload : {
            optionsList
        }
    }
}