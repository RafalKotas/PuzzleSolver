// react
import React, { useEffect } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../store"
import { displayModes, SetCurrentPage, SetDisplayMode } from "../../../../store/display"
import { selectedNonogramDetails, SetSelectedNonogram, sortedNonogramsWhichMetSelectedFilters } from "../../../../store/data/nonogram"

// (sub) components
import NonogramItemCard from "./NonogramItemCard/NonogramItemCard"

// other (functions)
import commonFunctions from "../../../../functions"

const mapStateToProps = (state: AppState) => ({

    nonogramSortFiltersList: state.nonogramFiltersReducer.sortFilters,

    mode: state.displayReducer.mode,

    currentPage: state.displayReducer.currentPage,
    itemsPerPage: state.displayReducer.itemsPerPage,

    sortedAndFilteredNonograms: sortedNonogramsWhichMetSelectedFilters(state.displayReducer, state.nonogramDataReducer, state.nonogramFiltersReducer)
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setDisplayMode: (displayMode: displayModes) =>
        dispatch(SetDisplayMode(displayMode)),
    setCurrentPage: (updatedPage: number) =>
        dispatch(SetCurrentPage(updatedPage)),
    setSelectedNonogram: (nonogram: selectedNonogramDetails | null) => 
        dispatch(SetSelectedNonogram(nonogram))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type NonogramPageItemsPropsFromRedux = ConnectedProps<typeof connector>

type NonogramPageItemsProps = NonogramPageItemsPropsFromRedux

const NonogramPageItems : React.FC<NonogramPageItemsProps> = ({ 
    mode, sortedAndFilteredNonograms, currentPage, itemsPerPage, setSelectedNonogram}) => {

    console.log(sortedAndFilteredNonograms)

    useEffect(() => {
        setSelectedNonogram(null)
    }, [setSelectedNonogram])

    const includeMatchNumber = (valueRange : number[], optionValue: number) => {
        return valueRange.length > 0 ? commonFunctions.isValueInRange(optionValue, valueRange) : true
    }

    const firstItemIndex = () => {
        //itemsOnPage : 30, page 1 -> 0, page 2 -> 30, page 3 -> 60
        return ((currentPage - 1) * itemsPerPage)//((page - 1) * itemsOnPage)
    }
    
    const lastItemIndex = () => {
        return firstItemIndex() + itemsPerPage - 1//firstItemIndex() + itemsOnPage - 1
    }

    const shouldDisplay = (index : number) => {
        return includeMatchNumber([firstItemIndex(), lastItemIndex()], index)
    }

    return (
        <React.Fragment>
            {
                sortedAndFilteredNonograms.filter((nInfo, indexS) => {
                    return shouldDisplay(indexS)
                    
                }).map((puzzleToDisplay) => {
                    return <NonogramItemCard
                        key={puzzleToDisplay.filename}
                        nonogramDetails={puzzleToDisplay}
                        mode={mode}
                     />
                })
            }
        </React.Fragment>
    )
}

export default connector(NonogramPageItems)