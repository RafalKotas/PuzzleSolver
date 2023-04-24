//react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../store"
import { displayModes, SetDisplayMode } from "../../../../store/display"
import { slitherlinkSortFilters, slitherlinkSortFiltersNames, selectFilterNamePriorityIndicator } from "../../../../store/filters/slitherlink"

// (sub) components
import SlitherlinkSortFilter from "./SlitherlinkSortFilter/SlitherlinkSortFilter"

const mapStateToProps = (state: AppState) => ({
    puzzlesList: state.slitherlinkDataReducer.slitherlinksList,
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

type SlitherlinkSortFiltersPropsFromRedux = ConnectedProps<typeof connector>

type SlitherlinkSortFiltersProps = SlitherlinkSortFiltersPropsFromRedux

const SlitherlinkSortFilters: React.FC<SlitherlinkSortFiltersProps> = ({ priorityIndicator }) => {
    return (
        <React.Fragment>
            {
                slitherlinkSortFilters.sort((sortFilterA, sortFilterB) => {
                    return priorityIndicator(sortFilterA.filterName) - priorityIndicator(sortFilterB.filterName)
                }).map((filterDetails) => {
                    return (
                        <SlitherlinkSortFilter
                            key={"slitherlink-filter-" + filterDetails.filterName}
                            filterDetails={filterDetails}
                            priorityIndicator={priorityIndicator(filterDetails.filterName)}
                        />
                    )
                })
            }
        </React.Fragment>
    )
}

export default connector(SlitherlinkSortFilters)