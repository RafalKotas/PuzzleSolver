// react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../store"
import { displayModes, SetCurrentPage, SetDisplayMode } from "../../../../store/display"
import { slitherlinkSortFiltersNames, selectFilterNamePriorityIndicator } from "../../../../store/filters/slitherlink"
import { slitherlinkInformation, selectListFromMode } from "../../../../store/data/slitherlink"

// (sub) components
import SlitherlinkItemCard from "./SlitherlinkItemCard/SlitherlinkItemCard"

// other (functions)
import commonFunctions from "../../../../functions"

const mapStateToProps = (state: AppState) => ({
    
    properPuzzlesList: selectListFromMode(state.displayReducer, state.slitherlinkDataReducer),

    priorityIndicator: (filterName: slitherlinkSortFiltersNames) => 
        selectFilterNamePriorityIndicator(state.slitherlinkFiltersReducer, filterName),

    slitherlinkSortFiltersList: state.slitherlinkFiltersReducer.sortFilters,

    mode: state.displayReducer.mode,

    currentPage: state.displayReducer.currentPage,
    itemsPerPage: state.displayReducer.itemsPerPage,

    selectionFilters: {
        selectedSources: state.slitherlinkFiltersReducer.selectedSources,
        selectedYears: state.slitherlinkFiltersReducer.selectedYears,
        selectedMonths: state.slitherlinkFiltersReducer.selectedMonths,
        selectedDifficulties: state.slitherlinkFiltersReducer.selectedDifficulties,
        selectedWidths: state.slitherlinkFiltersReducer.selectedWidths,
        selectedHeights: state.slitherlinkFiltersReducer.selectedHeights,
    }

})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setDisplayMode: (displayMode: displayModes) =>
        dispatch(SetDisplayMode(displayMode)),
    setCurrentPage: (updatedPage: number) =>
        dispatch(SetCurrentPage(updatedPage))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SlitherlinkPageItemsPropsFromRedux = ConnectedProps<typeof connector>

type SlitherlinkPageItemsProps = SlitherlinkPageItemsPropsFromRedux

const SlitherlinkPageItems : React.FC<SlitherlinkPageItemsProps> = ({ 
    properPuzzlesList, mode, slitherlinkSortFiltersList, selectionFilters, 
    currentPage, itemsPerPage}) => {

    interface numberSortFilter {
        type: "number",
        sortFunctionNumber: (slitherlink: slitherlinkInformation) => number      
    }

    interface stringSortFilter {
        type: "string",
        sortFunctionString: (slitherlink: slitherlinkInformation) => string
    }
 
    const otherSortFilters : Record<string, numberSortFilter | stringSortFilter> = {
        /*"area": {
            type: "number"
        }*/
    }

    const puzzleDetailsSortFunction = (filterKey : string, direction: string) => {
        
        return function(puzzleA : slitherlinkInformation, puzzleB : slitherlinkInformation) {
                if(filterKey in puzzleA) {
                    let key = filterKey as keyof slitherlinkInformation
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

    const sortedPuzzleList = (puzzleList : slitherlinkInformation[]) => {

        let sorted = puzzleList.sort((puzzleA, puzzleB) => {
            let reducedFunc =  slitherlinkSortFiltersList.reduce((prevValue, currValue) => {
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

    const hasMatchingFilters = (puzzle: slitherlinkInformation) => {
        //if length(of selected options) === 0 match all
        let matchesSource = includeMatchString(selectionFilters.selectedSources, puzzle.source)
        let matchesYear = includeMatchString(selectionFilters.selectedYears, puzzle.year)
        let matchesMonth = includeMatchString(selectionFilters.selectedMonths, puzzle.month)
        let matchesDifficulty = includeMatchNumber(selectionFilters.selectedDifficulties, puzzle.difficulty)
        let matchesWidth = includeMatchNumber(selectionFilters.selectedWidths, puzzle.width)
        let matchesHeight = includeMatchNumber(selectionFilters.selectedHeights, puzzle.height)
        return mode === "CREATE" || (matchesSource && matchesYear && matchesMonth && matchesDifficulty && matchesWidth && matchesHeight)
    }

    const shouldDisplay = (index : number) => {
        return includeMatchNumber([firstItemIndex(), lastItemIndex()], index)
    }

    const puzzlesMatchingFilters = (puzzleList : slitherlinkInformation[]) => {
        return puzzleList.filter((puzzle) => {
            return hasMatchingFilters(puzzle)
        })
    }

    const filteredAndSortedPuzzles = 
        (puzzleList : slitherlinkInformation[]) => sortedPuzzleList(puzzlesMatchingFilters(puzzleList))

    return (
        <React.Fragment>
            {
                filteredAndSortedPuzzles(properPuzzlesList).filter((_, indexS) => {
                    return shouldDisplay(indexS)
                    
                }).map((puzzleToDisplay) => {
                    return <SlitherlinkItemCard
                                key={"slitherlink-" + puzzleToDisplay.label}
                                slitherlinkDetails={puzzleToDisplay}
                                link={mode === "READ" ? "/view/slitherlink-solver" : "/view/slitherlink-editor"}
                            />
                        
                    //</Link>
                })
            }
        </React.Fragment>
    )
}

export default connector(SlitherlinkPageItems)