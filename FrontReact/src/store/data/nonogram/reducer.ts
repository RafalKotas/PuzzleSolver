import { Reducer } from "redux"
import {NonogramDataState, NonogramDataActionTypes, nonSelectedSquareEdit, emptyNonogram, nonogramInformation,
    SET_SELECTED_NONOGRAM,
    SET_EDITED_SQUARE, 
    MODIFY_SEQUENCE,
    ADD_NONOGRAM_DETAIL, 
    REMOVE_NONOGRAM_DETAIL,
    PREVIEW_NONOGRAM, 
    CLOSE_PREVIEW_NONOGRAM, 
    SET_CORRECTNESS, 
    SET_NONOGRAMS_LIST, 
    ADD_TEMPORARY_NONOGRAM, 
    CHANGE_NONOGRAM_STRING_DETAIL, 
    SAVE_TEMPORARY_NONOGRAM, 
    ADD_COLUMNS, 
    ADD_ROWS, 
    REMOVE_ROWS, 
    REMOVE_COLUMNS,
    SET_EDIT_MODE,
    MODIFY_ROW,
    MODIFY_COLUMN,
    TOGGLE_MARKS_VISIBILITY,
    TOGGLE_XS_VISIBILITY,
    selectedNonogramDetails
} from "./types"

import commonFunctions from "../../../functions"
import { DisplayState } from "../../display"
import { extractSelectionFilters, NonogramFiltersState } from "../../filters/nonogram"

export const initialNonogramDataState: NonogramDataState = {

    selectedNonogram: null,
    nonogramsList: [],

    createdNonogram: emptyNonogram,
    createdNonogramsList: [],

    detailsSet: [],

    marksVisible: true,
    xsVisible: true,

    nonogramCorrect: 0,

    nextCreatedNonogramFileNumber: 1,

    mode: "READ",
    editMode: true,

    squareEdit : nonSelectedSquareEdit
}


export const nonogramDataReducer: Reducer<NonogramDataState, NonogramDataActionTypes> = (state = initialNonogramDataState, action : NonogramDataActionTypes) => {
    switch (action.type) {
        case SET_EDIT_MODE:
            return {
                ...state,
                editMode: action.payload.editMode,
                nonogramCorrect: 0
            }
        case SET_NONOGRAMS_LIST:
            return {
                ...state,
                nonogramsList: action.payload.nonogramsList
            }
        case SET_SELECTED_NONOGRAM:
            console.log("set selected nonogram action triggered")
            return {...state,
                selectedNonogram: action.payload.updatedNonogram
            }
        case ADD_TEMPORARY_NONOGRAM:
            let newTmpNonogram = action.payload.temporaryNonogram
            return {
                ...state,
                createdNonogramsList: [...state.createdNonogramsList, newTmpNonogram]
            }
        case ADD_NONOGRAM_DETAIL:
            return {
                ...state,
                detailsSet: state.detailsSet.includes(action.payload.detail) ? state.detailsSet : [...state.detailsSet, action.payload.detail]
            }
        case REMOVE_NONOGRAM_DETAIL:
            return {
                ...state,
                detailsSet: state.detailsSet.filter((detail) => detail !== action.payload.detail)
            }
        case CHANGE_NONOGRAM_STRING_DETAIL:
            return {
                ...state,
                createdNonogram: {
                    ...state.createdNonogram,
                    [action.payload.detail]: action.payload.value
                }
            }
        case SAVE_TEMPORARY_NONOGRAM:
            let nonogramToSave = state.createdNonogram
            nonogramToSave.filename = "Template_" + state.nextCreatedNonogramFileNumber
            return {
                ...state,
                nextCreatedNonogramFileNumber: state.nextCreatedNonogramFileNumber + 1,
                createdNonogramsList: [...state.createdNonogramsList, {
                    ...nonogramToSave,
                    rowSequences: Array.from({length: nonogramToSave.height}, () => [0]),
                    columnSequences: Array.from({length: nonogramToSave.width}, () => [0])
                }]
            }
        case SET_EDITED_SQUARE:
            return {
                ...state, 
                squareEdit: action.payload.squareEdit
            }
        case MODIFY_ROW:

            return {
                ...state,
                selectedNonogram: (state.selectedNonogram && {
                    ...state.selectedNonogram,
                    rowSequences: state.selectedNonogram.rowSequences.map((rowSequence, index) => {
                        return index === action.payload.rowIndex ? action.payload.rowSequences : rowSequence
                    })
                }) || null
            }
        case MODIFY_COLUMN:
            return {
                ...state,
                selectedNonogram: (state.selectedNonogram && {
                    ...state.selectedNonogram,
                    columnSequences: state.selectedNonogram.columnSequences.map((columnSequence, index) => {
                        return index === action.payload.columnIndex ? action.payload.columnSequences : columnSequence
                    })
                }) || null
            }
        case MODIFY_SEQUENCE:
            let {value, section, index, seqNo} = action.payload

            let updatedSequences

            if(state.selectedNonogram) {

                updatedSequences = state.selectedNonogram[ section ][index]

                //if changing last sequence in row/column
                if(seqNo === state.selectedNonogram[ section ][index].length - 1) {
                    let elementsBeforeSeq = state.selectedNonogram[ section ][index].slice(0, -1)
                    if(value !== 0) {
                        updatedSequences = [...elementsBeforeSeq, value, 0]
                    } else {
                        updatedSequences = [...elementsBeforeSeq, 0]
                    }
                } 
                // if changing sequence in the middle of array or at the beginning
                else {
                    let elementsBeforeSeq = state.selectedNonogram[ section ][ index ].slice(0, seqNo)
                    let elementsAfterSeq = state.selectedNonogram[ section ][ index ].slice(seqNo + 1)
                    if(value !== 0) {
                        updatedSequences = [...elementsBeforeSeq, value, ...elementsAfterSeq]
                    } else {
                        updatedSequences = [...elementsBeforeSeq, ...elementsAfterSeq]
                    }
                }

                //all rows/columns sequences updated
                let updatedSequencesSection = [
                    ...state.selectedNonogram[section].slice(0, index),
                    updatedSequences,
                    ...state.selectedNonogram[section].slice(index + 1)
                ]

                return {
                    ...state,
                    selectedNonogram: {
                        ...state.selectedNonogram,
                        [section] : updatedSequencesSection
                    },
                    squareEdit : nonSelectedSquareEdit
                }
            }
            else {
                return state
            }
        case PREVIEW_NONOGRAM:
            if(state.selectedNonogram) {
                let updatedColumnsSequencesPreview = state.selectedNonogram.columnSequences.map((sequences) => {
                    return sequences.length !== 1 ? sequences.filter((sequence, index) => {
                        return sequence !== 0
                    }) : sequences
                })
                let updatedRowsSequencesPreview = state.selectedNonogram.rowSequences.map((sequences) => {
                    return sequences.length !== 1 ? sequences.filter((sequence) => {
                        return sequence !== 0
                    }) : sequences
                })
                return {
                    ...state,
                    selectedNonogram: {
                        ...state.selectedNonogram,
                        columnSequences: updatedColumnsSequencesPreview,
                        rowSequences: updatedRowsSequencesPreview
                    },
                    editMode: false
                }
            } else {
                return {
                    ...state
                }
            }
        case CLOSE_PREVIEW_NONOGRAM:
            if(state.selectedNonogram) {
                let updatedColumnsSequences = state.selectedNonogram.columnSequences.map((sequences) => {
                    return sequences[sequences.length - 1] === 0 ? sequences : [...sequences, 0]
                })
                let updatedRowsSequences = state.selectedNonogram.rowSequences.map((sequences) => {
                    return sequences[sequences.length - 1] === 0 ? sequences : [...sequences, 0]
                })
                return {
                    ...state,
                    selectedNonogram: {
                        ...state.selectedNonogram,
                        columnSequences: updatedColumnsSequences,
                        rowSequences: updatedRowsSequences
                    },
                    editMode: true,
                    nonogramCorrect: 0
                }
            } else {
                return {
                    ...state
                }
            }
        case TOGGLE_MARKS_VISIBILITY:
            return {
                ...state,
                marksVisible: action.payload.marksVisibility
            }
        case TOGGLE_XS_VISIBILITY:
            return {
                ...state,
                xsVisible: action.payload.xsVisibility
            }
        case SET_CORRECTNESS:
            return {
                ...state,
                nonogramCorrect: action.payload.correct
            }
        case ADD_ROWS:
            if(state.selectedNonogram) {
                let { startRowAddIdx, endRowAddIdx } = action.payload
                let rowsToAddNumber = endRowAddIdx - startRowAddIdx + 1
                let rowsToAdd = Array.from({length: rowsToAddNumber}, () => [0])
    
                let rowsSequencesAfterAddition = state.selectedNonogram.rowSequences.slice(0, startRowAddIdx)
                                                .concat(rowsToAdd)
                                                .concat(state.selectedNonogram.rowSequences.slice(startRowAddIdx))
    
                return {
                    ...state,
                    selectedNonogram: {
                        ...state.selectedNonogram,
                        rowSequences: rowsSequencesAfterAddition,
                        height: rowsSequencesAfterAddition.length
                    }
                }
            } else {
                return {
                    ...state
                }
            }
        case REMOVE_ROWS:
            let { startRowRemoveIdx, endRowRemoveIdx } = action.payload
            if(state.selectedNonogram) {
                if((endRowRemoveIdx - startRowRemoveIdx + 1) >= state.selectedNonogram.height) {
                    return {
                        ...state
                    }
                } else {
                    let rowsSequencesAfterDeletion = state.selectedNonogram.rowSequences.filter((sequences, index) => {
                        return !commonFunctions.isValueInRange(index, [startRowRemoveIdx, endRowRemoveIdx])
                    })
                    return {
                        ...state,
                        selectedNonogram: {
                            ...state.selectedNonogram,
                            rowSequences: rowsSequencesAfterDeletion,
                            height: rowsSequencesAfterDeletion.length
                        }
                    }
                }
            } else {
                return {
                    ...state
                }
            }
        case ADD_COLUMNS:
            if(state.selectedNonogram) {
                let { startColumnAddIdx, endColumnAddIdx } = action.payload
                let columnsToAddNumber = endColumnAddIdx - startColumnAddIdx + 1
                let columnsToAdd = Array.from({length: columnsToAddNumber}, () => [0])
                
                let columnsSequencesAfterAddition = state.selectedNonogram.columnSequences.slice(0, startColumnAddIdx)
                .concat(columnsToAdd)
                .concat(state.selectedNonogram.columnSequences.slice(startColumnAddIdx))
    
                return {
                    ...state,
                    selectedNonogram: {
                        ...state.selectedNonogram,
                        columnSequences: columnsSequencesAfterAddition,
                        width: columnsSequencesAfterAddition.length
                    }
                }
            } else {
                return {
                    ...state
                }
            }
        case REMOVE_COLUMNS:
            let { startColumnRemoveIdx, endColumnRemoveIdx } = action.payload
            if(state.selectedNonogram) {
                if((endColumnRemoveIdx - startColumnRemoveIdx + 1) >= state.selectedNonogram.width) {
                    return {
                        ...state
                    }
                } else {
                    let columnsSequencesAfterDeletion = state.selectedNonogram.columnSequences.filter((_, index) => {
                        return !commonFunctions.isValueInRange(index, [startColumnRemoveIdx, endColumnRemoveIdx])
                    })
    
                    return {
                        ...state,
                        selectedNonogram: {
                            ...state.selectedNonogram,
                            columnSequences: columnsSequencesAfterDeletion,
                            width: columnsSequencesAfterDeletion.length
                        }
                    }
                }   
            } else {
                return {
                    ...state
                }
            }
        default:
            return state
      }
}

export const selectListFromMode = (displayState: DisplayState , nonogramDataState : NonogramDataState) => {
    return displayState.mode === "READ" ? nonogramDataState.nonogramsList : nonogramDataState.createdNonogramsList
}

export const selectColumnsSequencesLength = (state : NonogramDataState) => state.selectedNonogram ? state.selectedNonogram.columnSequences.length : 0
export const selectRowsSequencesLength = (state : NonogramDataState) => state.selectedNonogram ? state.selectedNonogram.rowSequences.length : 0

export const selectArea = (nonogram : nonogramInformation) => nonogram.height * nonogram.width

export const selectedNonogramDifficulty = (state: NonogramDataState) =>  state.selectedNonogram ? state.selectedNonogram.difficulty : 0

const includeMatchNumber = (valueRange : number[], optionValue: number) => {
    return valueRange.length > 0 ? commonFunctions.isValueInRange(optionValue, valueRange) : true
}

const includeMatchString = (selectedValues : string[] , optionValue: string) => {
    return selectedValues.length > 0 ? selectedValues.includes(optionValue) : true
}

export const findSelectedNonogramListIndex = (displayState : DisplayState, dataState : NonogramDataState, filterState: NonogramFiltersState) => {
    let nonogram : selectedNonogramDetails | null = null
    if(dataState.mode === "READ") {
        nonogram = dataState.selectedNonogram
    } else {
        nonogram = dataState.createdNonogram
    }

    if(nonogram) {
        let list = sortedNonogramsWhichMetSelectedFilters(displayState, dataState, filterState)
        return list.reduce((previousIndex, currentValue, currentIndex) => {
            return (nonogram && currentValue.filename === nonogram.filename) ? currentIndex : previousIndex
        }, -2)
    } else {
        return -2
    }
}

export const findPreviousNonogramName = (displayState : DisplayState, dataState : NonogramDataState, filterState: NonogramFiltersState) => {
    let list = sortedNonogramsWhichMetSelectedFilters(displayState, dataState, filterState)
    let nonogramListIndex = findSelectedNonogramListIndex(displayState, dataState, filterState)
    if(nonogramListIndex > list.length - 1 || nonogramListIndex <= 0) {
        return null
    } else {
        return list[nonogramListIndex - 1].filename
    }
}

export const findNextNonogramName = (displayState: DisplayState, dataState : NonogramDataState, filterState: NonogramFiltersState) => {
    let list = sortedNonogramsWhichMetSelectedFilters(displayState, dataState, filterState)
    let nonogramListIndex = findSelectedNonogramListIndex(displayState, dataState, filterState)
    if(nonogramListIndex >= list.length - 1 || nonogramListIndex < 0) {
        return null
    } else {
        return list[nonogramListIndex + 1].filename
    }
}

export const sortedNonogramsWhichMetSelectedFilters = (displayState: DisplayState, dataState : NonogramDataState, filtersState : NonogramFiltersState) => {
    let nonogramsWhichMetSelectedFilters = selectNonogramsWhichMetSelectedFilters(displayState, dataState, filtersState)
    return sortedNonograms(filtersState, nonogramsWhichMetSelectedFilters)
}

export const selectNonogramsWhichMetSelectedFilters = (displayState: DisplayState, dataState : NonogramDataState, filtersState : NonogramFiltersState) => {
    let initialNonogramsList = displayState.mode === "READ" ? dataState.nonogramsList : dataState.createdNonogramsList
    
    console.log("mode: " + displayState.mode)
    console.log(initialNonogramsList)

    if(displayState.mode === "READ") {
        let selectionFilters = extractSelectionFilters(filtersState)

        return initialNonogramsList.filter((nonogramInfo : nonogramInformation) => {
            let matchesSource = includeMatchString(selectionFilters.selectedSources, nonogramInfo.source)
            let matchesYear = includeMatchString(selectionFilters.selectedYears, nonogramInfo.year)
            let matchesMonth = includeMatchString(selectionFilters.selectedMonths, nonogramInfo.month)
            let matchesDifficulties = includeMatchNumber(selectionFilters.selectedDifficulties, nonogramInfo.difficulty)
            let matchesWidths = includeMatchNumber(selectionFilters.selectedWidths, nonogramInfo.width)
            let matchesHeights = includeMatchNumber(selectionFilters.selectedHeights, nonogramInfo.height)
            return matchesSource && matchesYear && matchesMonth && matchesDifficulties && matchesWidths && matchesHeights
        })
    } else {
        return initialNonogramsList
    }
}

const areaCalculator = (nonogram : nonogramInformation) => selectArea(nonogram)

interface numberSortFilter {
    type: "number",
    sortFunctionNumber: (nonogram : nonogramInformation) => number      
}

interface stringSortFilter {
    type: "string",
    sortFunctionString: (nonogram : nonogramInformation) => string
}

const otherSortFilters : Record<string, numberSortFilter | stringSortFilter> = {
    "area": {
        type: "number",
        sortFunctionNumber: areaCalculator
    }
}

const nonogramDetailsSortFunction = (filterKey : string, direction: string) => {
        
    return function(nonogramA : nonogramInformation, nonogramB : nonogramInformation) {
            if(filterKey in nonogramA) {
                let key = filterKey as keyof nonogramInformation
                if(typeof(nonogramA[key]) === "string" && typeof(nonogramB[key]) === "string") {
                    let [nAstr, nBstr] = [nonogramA[key] as string, nonogramB[key] as string]
                    return direction === "ascending" ? nAstr.localeCompare(nBstr) : nBstr.localeCompare(nAstr) 
                } else if(typeof(nonogramA[key]) === "number" && typeof(nonogramB[key]) === "number") {
                    let [nA, nB] = [nonogramA[key] as number, nonogramB[key] as number]
                    let ascendingValue = nA - nB
                    let descendingValue = nB - nA
                    return direction === "ascending" ? ascendingValue : descendingValue
                }
            } else if (Object.keys(otherSortFilters).includes(filterKey)) {
                let filterDetails = otherSortFilters[filterKey]
                if(filterDetails.type === "number") {
                    let ascending = filterDetails.sortFunctionNumber(nonogramA) - filterDetails.sortFunctionNumber(nonogramB)
                    return direction === "ascending" ? ascending : -1 * ascending
                } else if(filterDetails.type === "string") {
                    let ascending = filterDetails.sortFunctionString(nonogramA).localeCompare(filterDetails.sortFunctionString(nonogramB))
                    return direction === "ascending" ? ascending : -1 * ascending
                }
            }
            return 0
    }
}

const sortedNonograms = (nonogramFiltersState : NonogramFiltersState, listOfNonograms : nonogramInformation[]) => {

    let sorted = listOfNonograms.sort((nonogramA, nonogramB) => {
        let reducedFunc =  nonogramFiltersState.sortFilters.reduce((prevValue, currValue) => {
            return prevValue || nonogramDetailsSortFunction(currValue.filterName, currValue.sortDirection)(nonogramA, nonogramB)
        }, 0 as any)
        return reducedFunc
    })
    return sorted
}