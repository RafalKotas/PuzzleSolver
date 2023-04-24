// react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../store"
import { displayModes, SetCurrentPage, SetDisplayMode } from "../../../../store/display"
import { architectSortFiltersNames, selectFilterNamePriorityIndicator } from "../../../../store/filters/architect"
import { architectInformation, selectListFromMode } from "../../../../store/data/architect"

// (sub) components
import ArchitectItemCard from "./ArchitectItemCard/ArchitectItemCard"

// other (functions)
import commonFunctions from "../../../../functions"

const mapStateToProps = (state: AppState) => ({
    
    properPuzzlesList: selectListFromMode(state.displayReducer, state.architectDataReducer),

    priorityIndicator: (filterName: architectSortFiltersNames) => 
        selectFilterNamePriorityIndicator(state.architectFiltersReducer, filterName),

    architectSortFiltersList: state.architectFiltersReducer.sortFilters,

    mode: state.displayReducer.mode,

    currentPage: state.displayReducer.currentPage,
    itemsPerPage: state.displayReducer.itemsPerPage,

    selectionFilters: {
        selectedSources: state.architectFiltersReducer.selectedSources,
        selectedDifficulties: state.architectFiltersReducer.selectedDifficulties,
        selectedWidths: state.architectFiltersReducer.selectedWidths,
        selectedHeights: state.architectFiltersReducer.selectedHeights,
    }

})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setDisplayMode: (displayMode: displayModes) =>
        dispatch(SetDisplayMode(displayMode)),
    setCurrentPage: (updatedPage: number) =>
        dispatch(SetCurrentPage(updatedPage))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type ArchitectPageItemsPropsFromRedux = ConnectedProps<typeof connector>

type ArchitectPageItemsProps = ArchitectPageItemsPropsFromRedux

const ArchitectPageItems : React.FC<ArchitectPageItemsProps> = ({ 
    properPuzzlesList, mode, architectSortFiltersList, selectionFilters, 
    currentPage, itemsPerPage}) => {

    interface numberSortFilter {
        type: "number",
        sortFunctionNumber: (architect: architectInformation) => number      
    }

    interface stringSortFilter {
        type: "string",
        sortFunctionString: (architect: architectInformation) => string
    }
 
    const otherSortFilters : Record<string, numberSortFilter | stringSortFilter> = {
        /*"area": {
            type: "number"
        }*/
    }

    const puzzleDetailsSortFunction = (filterKey : string, direction: string) => {
        
        return function(puzzleA : architectInformation, puzzleB : architectInformation) {
                if(filterKey in puzzleA) {
                    let key = filterKey as keyof architectInformation
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

    const sortedPuzzleList = (puzzleList : architectInformation[]) => {

        let sorted = puzzleList.sort((puzzleA, puzzleB) => {
            let reducedFunc =  architectSortFiltersList.reduce((prevValue, currValue) => {
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

    const hasMatchingFilters = (puzzle: architectInformation) => {
        //if length(of selected options) === 0 match all
        let matchesSource = includeMatchString(selectionFilters.selectedSources, puzzle.source)
        let matchesDifficulties = includeMatchNumber(selectionFilters.selectedDifficulties, puzzle.difficulty)
        let matchesWidths = includeMatchNumber(selectionFilters.selectedWidths, puzzle.width)
        let matchesHeights = includeMatchNumber(selectionFilters.selectedHeights, puzzle.height)
        return mode.localeCompare("CREATE") === 0 || (matchesSource && matchesDifficulties && matchesWidths && matchesHeights)
    }

    const shouldDisplay = (index : number) => {
        return includeMatchNumber([firstItemIndex(), lastItemIndex()], index)
    }

    const puzzlesMatchingFilters = (puzzleList : architectInformation[]) => {
        return puzzleList.filter((puzzle) => {
            return hasMatchingFilters(puzzle)
        })
    }

    const filteredAndSortedPuzzles = 
        (puzzleList : architectInformation[]) => sortedPuzzleList(puzzlesMatchingFilters(puzzleList))

    return (
        <React.Fragment>
            {
                filteredAndSortedPuzzles(properPuzzlesList).filter((_, indexS) => {
                    return shouldDisplay(indexS)
                    
                }).map((puzzleToDisplay) => {
                    return <ArchitectItemCard
                                key={"architect-page-item-" + puzzleToDisplay.label}
                                architectDetails={puzzleToDisplay}
                                link={mode === "READ" ? "/view/architect-solver" : "/view/architect-editor"}
                            />
                })
            }
        </React.Fragment>
    )
}

export default connector(ArchitectPageItems)