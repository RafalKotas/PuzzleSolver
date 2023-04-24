// react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../store"
import { displayModes, SetDisplayMode } from "../../../store/display"
import { akariSortFiltersNames, selectFilterNamePriorityIndicator } from "../../../store/filters/akari"

// (sub) components
import AkariSortFilters from "./AkariSortFilters/AkariSortFilters"
import AkariPageItems from "./AkariPageItems/AkariPageItems"
import AkariSelectSectionPagination from "./AkariSelectSectionPagination/AkariSelectSectionPagination"

// fontawesome
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { faList, faGrip} from "@fortawesome/free-solid-svg-icons"

const mapStateToProps = (state: AppState) => ({
    displayMode: state.displayReducer.displayMode,

    priorityIndicator: (filterName: akariSortFiltersNames) => 
        selectFilterNamePriorityIndicator(state.akariFiltersReducer, filterName),

    akariSortFiltersList: state.akariFiltersReducer.sortFilters, 

    selectionFilters: {
        selectedSources: state.akariFiltersReducer.selectedSources,
        selectedDifficulties: state.akariFiltersReducer.selectedDifficulties,
        selectedWidths: state.akariFiltersReducer.selectedWidths,
        selectedHeights: state.akariFiltersReducer.selectedHeights,
    }
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setDisplayMode: (displayMode: displayModes) =>
        dispatch(SetDisplayMode(displayMode))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type AkariSelectSectionPropsFromRedux = ConnectedProps<typeof connector>

type AkariSelectSectionProps = AkariSelectSectionPropsFromRedux

const AkariSelectSection : React.FC<AkariSelectSectionProps> = ({
    displayMode, setDisplayMode}) => {

    return (
        <section id="select-puzzle-section">
            <header id="select-display-mode-section">
                <div id="sort-section">
                    <h1>SORT:</h1>
                    <div id="puzzle-sort-filters-section">
                        <AkariSortFilters />
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
                <AkariPageItems />
            </div>
            <AkariSelectSectionPagination />
        </section>
    )
}

export default connector(AkariSelectSection)