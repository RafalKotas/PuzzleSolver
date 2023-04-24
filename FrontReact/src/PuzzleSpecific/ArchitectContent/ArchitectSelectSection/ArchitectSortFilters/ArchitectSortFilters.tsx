//react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../store"
import { displayModes, SetDisplayMode } from "../../../../store/display"
import { architectSortFilters, architectSortFiltersNames, selectFilterNamePriorityIndicator } from "../../../../store/filters/architect"

// (sub) components
import ArchitectSortFilter from "./ArchitectSortFilter/ArchitectSortFilter"

const mapStateToProps = (state: AppState) => ({
    puzzlesList: state.architectDataReducer.architectsList,
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

type ArchitectSortFiltersPropsFromRedux = ConnectedProps<typeof connector>

type ArchitectSortFiltersProps = ArchitectSortFiltersPropsFromRedux

const ArchitectSortFilters: React.FC<ArchitectSortFiltersProps> = ({ priorityIndicator }) => {
    return (
        <React.Fragment>
            {
                architectSortFilters.sort((sortFilterA, sortFilterB) => {
                    return priorityIndicator(sortFilterA.filterName) - priorityIndicator(sortFilterB.filterName)
                }).map((filterDetails) => {
                    return (
                        <ArchitectSortFilter
                            key={"architect-sort-filter-" + filterDetails.filterName}
                            filterDetails={filterDetails}
                            priorityIndicator={priorityIndicator(filterDetails.filterName)}
                        />
                    )
                })
            }
        </React.Fragment>
    )
}

export default connector(ArchitectSortFilters)