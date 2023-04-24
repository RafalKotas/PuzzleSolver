//react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../store"
import { displayModes, SetDisplayMode } from "../../../../store/display"
import { hitoriSortFilters, hitoriSortFiltersNames, selectFilterNamePriorityIndicator } from "../../../../store/filters/hitori"

// (sub) components
import HitoriSortFilter from "./HitoriSortFilter/HitoriSortFilter"

const mapStateToProps = (state: AppState) => ({
    puzzlesList: state.hitoriDataReducer.hitorisList,
    displayMode: state.displayReducer.displayMode,

    priorityIndicator: (filterName: hitoriSortFiltersNames) =>
        selectFilterNamePriorityIndicator(state.hitoriFiltersReducer, filterName),

    hitoriSortFiltersList: state.hitoriFiltersReducer.sortFilters,

    selectionFilters: {
        selectedSources: state.hitoriFiltersReducer.selectedSources,
        selectedDifficulties: state.hitoriFiltersReducer.selectedDifficulties,
        selectedWidths: state.hitoriFiltersReducer.selectedWidths,
        selectedHeights: state.hitoriFiltersReducer.selectedHeights,
    }
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setDisplayMode: (displayMode: displayModes) =>
        dispatch(SetDisplayMode(displayMode))
})


const connector = connect(mapStateToProps, mapDispatchToProps)

type HitoriSortFiltersPropsFromRedux = ConnectedProps<typeof connector>

type HitoriSortFiltersProps = HitoriSortFiltersPropsFromRedux

const HitoriSortFilters: React.FC<HitoriSortFiltersProps> = ({ priorityIndicator }) => {
    return (
        <React.Fragment>
            {
                hitoriSortFilters.sort((sortFilterA, sortFilterB) => {
                    return priorityIndicator(sortFilterA.filterName) - priorityIndicator(sortFilterB.filterName)
                }).map((filterDetails) => {
                    return (
                        <HitoriSortFilter
                            key={"hitori-sort-filter-" + filterDetails.filterName}
                            filterDetails={filterDetails}
                            priorityIndicator={priorityIndicator(filterDetails.filterName)}
                        />
                    )
                })
            }
        </React.Fragment>
    )
}

export default connector(HitoriSortFilters)