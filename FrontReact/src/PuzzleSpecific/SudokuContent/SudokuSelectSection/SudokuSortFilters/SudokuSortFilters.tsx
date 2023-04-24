//react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../store"
import { displayModes, SetDisplayMode } from "../../../../store/display"
import { sudokuSortFilters, sudokuSortFiltersNames, selectFilterNamePriorityIndicator } from "../../../../store/filters/sudoku"

// (sub) components
import SudokuSortFilter from "./SudokuSortFilter/SudokuSortFilter"

const mapStateToProps = (state: AppState) => ({
    puzzlesList: state.sudokuDataReducer.sudokusList,
    displayMode: state.displayReducer.displayMode,

    priorityIndicator: (filterName: sudokuSortFiltersNames) =>
        selectFilterNamePriorityIndicator(state.sudokuFiltersReducer, filterName),

    sudokuSortFiltersList: state.sudokuFiltersReducer.sortFilters,

    selectionFilters: {
        selectedSources: state.sudokuFiltersReducer.selectedSources,
        selectedDifficulties: state.sudokuFiltersReducer.selectedDifficulties,
        selectedFilled: state.sudokuFiltersReducer.selectedFilled
    }
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setDisplayMode: (displayMode: displayModes) =>
        dispatch(SetDisplayMode(displayMode))
})


const connector = connect(mapStateToProps, mapDispatchToProps)

type SudokuSortFiltersPropsFromRedux = ConnectedProps<typeof connector>

type SudokuSortFiltersProps = SudokuSortFiltersPropsFromRedux

const SudokuSortFilters: React.FC<SudokuSortFiltersProps> = ({ priorityIndicator }) => {
    return (
        <React.Fragment>
            {
                sudokuSortFilters.sort((sortFilterA, sortFilterB) => {
                    return priorityIndicator(sortFilterA.filterName) - priorityIndicator(sortFilterB.filterName)
                }).map((filterDetails) => {
                    return (
                        <SudokuSortFilter
                            key={filterDetails.filterName}
                            filterDetails={filterDetails}
                            priorityIndicator={priorityIndicator(filterDetails.filterName)}
                        />
                    )
                })
            }
        </React.Fragment>
    )
}

export default connector(SudokuSortFilters)