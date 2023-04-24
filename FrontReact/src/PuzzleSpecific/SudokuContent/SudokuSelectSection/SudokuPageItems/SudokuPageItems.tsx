// react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../store"
import { displayModes, SetCurrentPage, SetDisplayMode } from "../../../../store/display"
import { sudokuSortFiltersNames, selectFilterNamePriorityIndicator } from "../../../../store/filters/sudoku"
import { sudokuInformation, selectListFromMode } from "../../../../store/data/sudoku"

// (sub) components
import SudokuItemCard from "./SudokuItemCard/SudokuItemCard"

// other (functions)
import commonFunctions from "../../../../functions"

const mapStateToProps = (state: AppState) => ({
    
    properPuzzlesList: selectListFromMode(state.displayReducer, state.sudokuDataReducer),

    priorityIndicator: (filterName: sudokuSortFiltersNames) => 
        selectFilterNamePriorityIndicator(state.sudokuFiltersReducer, filterName),

    sudokuSortFiltersList: state.sudokuFiltersReducer.sortFilters,

    mode: state.displayReducer.mode,

    currentPage: state.displayReducer.currentPage,
    itemsPerPage: state.displayReducer.itemsPerPage,

    selectionFilters: {
        selectedSources: state.sudokuFiltersReducer.selectedSources,
        selectedDifficulties: state.sudokuFiltersReducer.selectedDifficulties,
        selectedFilled: state.sudokuFiltersReducer.selectedFilled
    }

})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setDisplayMode: (displayMode: displayModes) =>
        dispatch(SetDisplayMode(displayMode)),
    setCurrentPage: (updatedPage: number) =>
        dispatch(SetCurrentPage(updatedPage))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SudokuPageItemsPropsFromRedux = ConnectedProps<typeof connector>

type SudokuPageItemsProps = SudokuPageItemsPropsFromRedux

const SudokuPageItems : React.FC<SudokuPageItemsProps> = ({ 
    properPuzzlesList, mode, sudokuSortFiltersList, selectionFilters, 
    currentPage, itemsPerPage}) => {

    interface numberSortFilter {
        type: "number",
        sortFunctionNumber: (sudoku: sudokuInformation) => number      
    }

    interface stringSortFilter {
        type: "string",
        sortFunctionString: (sudoku: sudokuInformation) => string
    }
 
    const otherSortFilters : Record<string, numberSortFilter | stringSortFilter> = {
        /*"area": {
            type: "number"
        }*/
    }

    const puzzleDetailsSortFunction = (filterKey : string, direction: string) => {
        
        return function(puzzleA : sudokuInformation, puzzleB : sudokuInformation) {
                if(filterKey in puzzleA) {
                    let key = filterKey as keyof sudokuInformation
                    if(typeof(puzzleA[key]) === "string" && typeof(puzzleB[key]) === "string") {
                        let [puzzleAstrProp, puzzleBstrProp] = [puzzleA[key] as string, puzzleB[key] as string]
                        return direction === "ascending" ? puzzleAstrProp.localeCompare(puzzleBstrProp) : 
                            puzzleBstrProp.localeCompare(puzzleAstrProp) 
                    } else if(typeof(puzzleA[key]) === "number" && typeof(puzzleB[key]) === "number") {
                        let [puzzleAnumbProp, puzzleBnumbProp] = [puzzleA[key] as number, puzzleB[key] as number]
                        let ascendingValue = puzzleAnumbProp - puzzleBnumbProp
                        let descendingValue = puzzleBnumbProp - puzzleAnumbProp
                        return direction === "ascending" ? ascendingValue : descendingValue
                    }
                } else if (Object.keys(otherSortFilters).includes(filterKey)) {
                    let filterDetails = otherSortFilters[filterKey]
                    if(filterDetails.type === "number") {
                        let ascending = filterDetails.sortFunctionNumber(puzzleA) - filterDetails.sortFunctionNumber(puzzleB)
                        return direction === "ascending" ? ascending : -1 * ascending
                    } else if(filterDetails.type === "string") {
                        let ascending = filterDetails.sortFunctionString(puzzleA).localeCompare(filterDetails.sortFunctionString(puzzleB))
                        return direction === "ascending" ? ascending : -1 * ascending
                    }
                }
                return 0
        }
    }

    const sortedPuzzleList = (puzzleList : sudokuInformation[]) => {

        let sorted = puzzleList.sort((puzzleA, puzzleB) => {
            let reducedFunc =  sudokuSortFiltersList.reduce((prevValue, currValue) => {
                return prevValue || puzzleDetailsSortFunction(currValue.filterName, currValue.sortDirection)(puzzleA, puzzleB)
            }, 0 as any)
            return reducedFunc
        })
        return sorted
    }

    const includeMatchNumber = (valueRange : number[], optionValue: number) => {
        return valueRange.length > 0 ? commonFunctions.isValueInRange(optionValue, valueRange) : true
    }

    const includeMatchString = (selectedValues : string[] , optionValue: string) => {
        return selectedValues.length > 0 ? selectedValues.includes(optionValue) : true
    }

    const firstItemIndex = () => {
        //itemsOnPage : 30, page 1 -> 0, page 2 -> 30, page 3 -> 60
        return ((currentPage - 1) * itemsPerPage)//((page - 1) * itemsOnPage)
    }
    
    const lastItemIndex = () => {
        return firstItemIndex() + itemsPerPage - 1//firstItemIndex() + itemsOnPage - 1
    }

    const hasMatchingFilters = (puzzle: sudokuInformation) => {
        //if length(of selected options) === 0 match all
        let matchesSource = includeMatchString(selectionFilters.selectedSources, puzzle.source)
        let matchesDifficulties = includeMatchNumber(selectionFilters.selectedDifficulties, puzzle.difficulty)
        // need to be implemented TODO
        let matchesFilled = includeMatchNumber(selectionFilters.selectedFilled, puzzle.filled)
        return matchesSource && matchesDifficulties && matchesFilled
    }

    const shouldDisplay = (index : number) => {
        return includeMatchNumber([firstItemIndex(), lastItemIndex()], index)
    }

    const puzzlesMatchingFilters = (puzzleList : sudokuInformation[]) => {
        return puzzleList.filter((puzzle) => {
            return hasMatchingFilters(puzzle)
        })
    }

    const filteredAndSortedPuzzles = 
        (puzzleList : sudokuInformation[]) => sortedPuzzleList( puzzlesMatchingFilters(puzzleList))

    return (
        <React.Fragment>
            {
                (mode === "READ" ? filteredAndSortedPuzzles(properPuzzlesList) : properPuzzlesList ).filter((puzzleInfo, indexS) => {
                    return shouldDisplay(indexS)
                    
                }).map((puzzleToDisplay) => {
                    return <SudokuItemCard
                                key={puzzleToDisplay.label} 
                                sudokuDetails={puzzleToDisplay}
                                link={mode === "READ" ? "/view/sudoku-solver" : "/view/sudoku-editor"}
                            />
                })
            }
        </React.Fragment>
    )
}

export default connector(SudokuPageItems)