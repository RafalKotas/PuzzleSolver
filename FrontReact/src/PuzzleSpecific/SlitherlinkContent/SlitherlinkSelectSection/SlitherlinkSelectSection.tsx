// react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../store"
import { displayModes, SetDisplayMode } from "../../../store/display"
import { slitherlinkSortFiltersNames, selectFilterNamePriorityIndicator } from "../../../store/filters/slitherlink"

// (sub) components
import SlitherlinkSortFilters from "./SlitherlinkSortFilters/SlitherlinkSortFilters"
import SlitherlinkPageItems from "./SlitherlinkPageItems/SlitherlinkPageItems"
import SlitherlinkSelectSectionPagination from "./SlitherlinkSelectSectionPagination/SlitherlinkSelectSectionPagination"

// fontawesome
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { faList, faGrip} from "@fortawesome/free-solid-svg-icons"

const mapStateToProps = (state: AppState) => ({
    displayMode: state.displayReducer.displayMode,

    priorityIndicator: (filterName: slitherlinkSortFiltersNames) => 
        selectFilterNamePriorityIndicator(state.slitherlinkFiltersReducer, filterName),

    slitherlinkSortFiltersList: state.slitherlinkFiltersReducer.sortFilters, 

    selectionFilters: {
        selectedSources: state.slitherlinkFiltersReducer.selectedSources,
        selectedDifficulties: state.slitherlinkFiltersReducer.selectedDifficulties,
        selectedWidths: state.slitherlinkFiltersReducer.selectedWidths,
        selectedHeights: state.slitherlinkFiltersReducer.selectedHeights,
    }
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setDisplayMode: (displayMode: displayModes) =>
        dispatch(SetDisplayMode(displayMode))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SlitherlinkSelectSectionPropsFromRedux = ConnectedProps<typeof connector>

type SlitherlinkSelectSectionProps = SlitherlinkSelectSectionPropsFromRedux

const SlitherlinkSelectSection : React.FC<SlitherlinkSelectSectionProps> = ({
    displayMode, setDisplayMode}) => {

    return (
        <section id="select-puzzle-section">
            <header id="select-display-mode-section">
                <div id="sort-section">
                    <h1>SORT:</h1>
                    <div id="puzzle-sort-filters-section">
                        <SlitherlinkSortFilters />
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
                <SlitherlinkPageItems />
            </div>
            <SlitherlinkSelectSectionPagination />
        </section>
    )
}

export default connector(SlitherlinkSelectSection)