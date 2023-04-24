// react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../store"
import { displayModes, SetDisplayMode } from "../../../store/display"
import { architectSortFiltersNames, selectFilterNamePriorityIndicator } from "../../../store/filters/architect"

// (sub) components
import ArchitectSortFilters from "./ArchitectSortFilters/ArchitectSortFilters"
import ArchitectPageItems from "./ArchitectPageItems/ArchitectPageItems"
import ArchitectSelectSectionPagination from "./ArchitectSelectSectionPagination/ArchitectSelectSectionPagination"

// fontawesome
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { faList, faGrip} from "@fortawesome/free-solid-svg-icons"

const mapStateToProps = (state: AppState) => ({
    displayMode: state.displayReducer.displayMode,

    priorityIndicator: (filterName: architectSortFiltersNames) => 
        selectFilterNamePriorityIndicator(state.architectFiltersReducer, filterName),

    architectSortFiltersList: state.architectFiltersReducer.sortFilters, 

    selectionFilters: {
        selectedSources: state.architectFiltersReducer.selectedSources,
        selectedDifficulties: state.architectFiltersReducer.selectedDifficulties,
        selectedWidths: state.architectFiltersReducer.selectedWidths,
        selectedHeights: state.architectFiltersReducer.selectedHeights,
    }
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setDisplayMode: (displayMode: displayModes) =>
        dispatch(SetDisplayMode(displayMode))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type ArchitectSelectSectionPropsFromRedux = ConnectedProps<typeof connector>

type ArchitectSelectSectionProps = ArchitectSelectSectionPropsFromRedux

const ArchitectSelectSection : React.FC<ArchitectSelectSectionProps> = ({
    displayMode, setDisplayMode}) => {

    return (
        <section id="select-puzzle-section">
            <header id="select-display-mode-section">
                <div id="sort-section">
                    <h1>SORT:</h1>
                    <div id="puzzle-sort-filters-section">
                        <ArchitectSortFilters />
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
                <ArchitectPageItems />
            </div>
            <ArchitectSelectSectionPagination />
        </section>
    )
}

export default connector(ArchitectSelectSection)