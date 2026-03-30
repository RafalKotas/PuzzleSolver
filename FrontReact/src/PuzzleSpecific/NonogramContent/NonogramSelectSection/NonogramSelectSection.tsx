// react
import React, { useEffect } from "react"

// redux
import { 
    connect, 
    ConnectedProps 
} from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "@store/index"
import { 
    displayModes, 
    SetCurrentPage, 
    SetDisplayMode 
} from "store/display"
import { 
    nonogramSortFiltersNames, 
    selectFilterNamePriorityIndicator 
} from "store/filters/nonogram"

// (sub)component(s)
import NonogramSortFilters from "PuzzleSpecific/NonogramContent/NonogramSelectSection/NonogramSortFilters/NonogramSortFilters"
import NonogramPageItems from "PuzzleSpecific/NonogramContent/NonogramSelectSection/NonogramPageItems/NonogramPageItems"
import NonogramSelectSectionPagination from "PuzzleSpecific/NonogramContent/NonogramSelectSection/NonogramSelectSectionPagination/NonogramSelectSectionPagination"

// fortawesome
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { 
    faList, 
    faGrip 
} from "@fortawesome/free-solid-svg-icons"

// styles
import "PuzzleSpecific/NonogramContent/NonogramSelectSection/NonogramSelectSection.css"

const mapStateToProps = (state: AppState) => ({
    displayMode: state.displayReducer.displayMode,

    priorityIndicator: (filterName: nonogramSortFiltersNames) => 
        selectFilterNamePriorityIndicator(state.nonogramFiltersReducer, filterName),

    nonogramSortFiltersList: state.nonogramFiltersReducer.sortFilters, 

    selectionFilters: {
        selectedSources: state.nonogramFiltersReducer.selectedSources,
        selectedYears: state.nonogramFiltersReducer.selectedYears,
        selectedMonths: state.nonogramFiltersReducer.selectedMonths,
        selectedDifficulties: state.nonogramFiltersReducer.selectedDifficulties,
        selectedWidths: state.nonogramFiltersReducer.selectedWidths,
        selectedHeights: state.nonogramFiltersReducer.selectedHeights,
    }
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setDisplayMode: (displayMode: displayModes) =>
        dispatch(SetDisplayMode(displayMode)),
    setCurrentPage: (updatedPage: number) =>
        dispatch(SetCurrentPage(updatedPage))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type NonogramSelectSectionPropsFromRedux = ConnectedProps<typeof connector>

type NonogramSelectSectionProps = NonogramSelectSectionPropsFromRedux

const NonogramSelectSection : React.FC<NonogramSelectSectionProps> = ({
    displayMode, setDisplayMode}) => {

    useEffect(() => {
        //eslint-disable-next-line
    }, [])

    return (
        <section id="select-puzzle-section">
            <header id="select-display-mode-section">
                <div id="sort-section">
                    <h1>SORT:</h1>
                    <div id="puzzle-sort-filters-section">
                        <NonogramSortFilters />
                    </div>
                </div>
                <div id="display-section">
                    <h1>DISPLAY:</h1>
                    <FontAwesomeIcon
                        onClick={() => setDisplayMode("list")}
                        className="display-change-icon" 
                        style={{height: "40px", backgroundColor: displayMode === "list" ? "#ffba65" : ""}} 
                        icon={faList} 
                    />
                    <FontAwesomeIcon 
                        onClick={() => setDisplayMode("grid")}
                        className="display-change-icon" 
                        style={{height: "40px", backgroundColor: displayMode === "grid" ? "#ffba65" : ""}}
                        icon={faGrip} 
                    />
                </div>
            </header>
            <div id="puzzle-view">
                <NonogramPageItems />
            </div>
            <NonogramSelectSectionPagination />
        </section>
    )
}

export default connector(NonogramSelectSection)