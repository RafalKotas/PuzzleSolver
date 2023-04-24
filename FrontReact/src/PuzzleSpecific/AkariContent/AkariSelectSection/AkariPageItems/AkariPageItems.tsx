// react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../store"
import { displayModes, SetCurrentPage, SetDisplayMode } from "../../../../store/display"
import { akariSortFiltersNames, selectFilterNamePriorityIndicator } from "../../../../store/filters/akari"
import { akariInformation, selectListFromMode } from "../../../../store/data/akari"

// (sub) components
import AkariItemCard from "./AkariItemCard/AkariItemCard"

// other (functions)
import commonFunctions from "../../../../functions"

const mapStateToProps = (state: AppState) => ({
    
    properPuzzlesList: selectListFromMode(state.displayReducer, state.akariDataReducer),

    priorityIndicator: (filterName: akariSortFiltersNames) => 
        selectFilterNamePriorityIndicator(state.akariFiltersReducer, filterName),

    akariSortFiltersList: state.akariFiltersReducer.sortFilters,

    mode: state.displayReducer.mode,

    currentPage: state.displayReducer.currentPage,
    itemsPerPage: state.displayReducer.itemsPerPage,

    selectionFilters: {
        selectedSources: state.akariFiltersReducer.selectedSources,
        selectedDifficulties: state.akariFiltersReducer.selectedDifficulties,
        selectedWidths: state.akariFiltersReducer.selectedWidths,
        selectedHeights: state.akariFiltersReducer.selectedHeights,
    }

})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setDisplayMode: (displayMode: displayModes) =>
        dispatch(SetDisplayMode(displayMode)),
    setCurrentPage: (updatedPage: number) =>
        dispatch(SetCurrentPage(updatedPage))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type AkariPageItemsPropsFromRedux = ConnectedProps<typeof connector>

type AkariPageItemsProps = AkariPageItemsPropsFromRedux

const AkariPageItems : React.FC<AkariPageItemsProps> = ({ 
    properPuzzlesList, mode, akariSortFiltersList, selectionFilters, 
    currentPage, itemsPerPage}) => {

    interface numberSortFilter {
        type: "number",
        sortFunctionNumber: (akari: akariInformation) => number      
    }

    interface stringSortFilter {
        type: "string",
        sortFunctionString: (akari: akariInformation) => string
    }
 
    const otherSortFilters : Record<string, numberSortFilter | stringSortFilter> = {
        /*"area": {
            type: "number"
        }*/
    }

    const puzzleDetailsSortFunction = (filterKey : string, direction: string) => {
        
        return function(puzzleA : akariInformation, puzzleB : akariInformation) {
                if(filterKey in puzzleA) {
                    let key = filterKey as keyof akariInformation
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

    const sortedPuzzleList = (puzzleList : akariInformation[]) => {

        let sorted = puzzleList.sort((puzzleA, puzzleB) => {
            let reducedFunc =  akariSortFiltersList.reduce((prevValue, currValue) => {
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

    //itemsOnPage : 30, page 1 -> 0, page 2 -> 30, page 3 -> 60
    const firstItemIndex = () => {
        return ((currentPage - 1) * itemsPerPage)//((page - 1) * itemsOnPage)
    }
    
    const lastItemIndex = () => {
        return firstItemIndex() + itemsPerPage - 1//firstItemIndex() + itemsOnPage - 1
    }

    const hasMatchingFilters = (puzzle: akariInformation) => {
        //if length(of selected options) === 0 match all
        let matchesSource = includeMatchString(selectionFilters.selectedSources, puzzle.source)
        let matchesDifficulties = includeMatchNumber(selectionFilters.selectedDifficulties, puzzle.difficulty)
        let matchesWidths = includeMatchNumber(selectionFilters.selectedWidths, puzzle.width)
        let matchesHeights = includeMatchNumber(selectionFilters.selectedHeights, puzzle.height)
        return mode === "CREATE" || (matchesSource && matchesDifficulties && matchesWidths && matchesHeights)
    }

    const shouldDisplay = (index : number) => {
        return includeMatchNumber([firstItemIndex(), lastItemIndex()], index)
    }

    const puzzlesMatchingFilters = (puzzleList : akariInformation[]) => {
        return puzzleList.filter((puzzle) => {
            return hasMatchingFilters(puzzle)
        })
    }

    const filteredAndSortedPuzzles = 
        (puzzleList : akariInformation[]) => sortedPuzzleList(puzzlesMatchingFilters(puzzleList))

    return (
        <React.Fragment>
            {
                filteredAndSortedPuzzles(properPuzzlesList).filter((_, indexS) => {
                    return shouldDisplay(indexS)
                    
                }).map((puzzleToDisplay) => {
                    return <AkariItemCard
                                key={puzzleToDisplay.label}
                                akariDetails={puzzleToDisplay}
                                link={mode === "READ" ? "/view/akari-solver" : "/view/akari-editor"}
                            />
                })
            }
        </React.Fragment>
    )
}

export default connector(AkariPageItems)