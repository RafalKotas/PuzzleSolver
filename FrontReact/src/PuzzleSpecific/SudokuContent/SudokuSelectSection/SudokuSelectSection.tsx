// react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../store"
import { displayModes, SetDisplayMode } from "../../../store/display"
import { sudokuSortFiltersNames, selectFilterNamePriorityIndicator } from "../../../store/filters/sudoku"

// (sub) components
import SudokuSortFilters from "./SudokuSortFilters/SudokuSortFilters"
import SudokuPageItems from "./SudokuPageItems/SudokuPageItems"
import SudokuSelectSectionPagination from "./SudokuSelectSectionPagination/SudokuSelectSectionPagination"

// fontawesome
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { faList, faGrip} from "@fortawesome/free-solid-svg-icons"

const mapStateToProps = (state: AppState) => ({
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

type SudokuSelectSectionPropsFromRedux = ConnectedProps<typeof connector>

type SudokuSelectSectionProps = SudokuSelectSectionPropsFromRedux

const SudokuSelectSection : React.FC<SudokuSelectSectionProps> = ({
    displayMode, setDisplayMode}) => {

    return (
        <section id="select-puzzle-section">
            <header id="select-display-mode-section">
                <div id="sort-section">
                    <h1>SORT:</h1>
                    <div id="puzzle-sort-filters-section">
                        <SudokuSortFilters />
                    </div>
                </div>
                <div id="display-section">
                    <h1>DISPLAY:</h1>
                    <FontAwesomeIcon
                        onClick={() => setDisplayMode("list")}
                        className="display-change-icon" 
                        style={{height: "40px", backgroundColor: displayMode === "list" ? "#63a871" : ""}} 
                        icon={faList} 
                    />
                    <FontAwesomeIcon 
                        onClick={() => setDisplayMode("grid")}
                        className="display-change-icon" 
                        style={{height: "40px", backgroundColor: displayMode === "grid" ? "#63a871" : ""}}
                        icon={faGrip} 
                    />
                </div>
            </header>
            <div id="puzzle-view">
                <SudokuPageItems />
            </div>
            <SudokuSelectSectionPagination />
        </section>
    )
}

export default connector(SudokuSelectSection)