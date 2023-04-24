//react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../store"
import { displayModes, SetDisplayMode } from "../../../../store/display"
import { akariSortFilters, akariSortFiltersNames, selectFilterNamePriorityIndicator } from "../../../../store/filters/akari"

// (sub) components
import AkariSortFilter from "./AkariSortFilter/AkariSortFilter"

const mapStateToProps = (state: AppState) => ({
    puzzlesList: state.akariDataReducer.akarisList,
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

type AkariSortFiltersPropsFromRedux = ConnectedProps<typeof connector>

type AkariSortFiltersProps = AkariSortFiltersPropsFromRedux

const AkariSortFilters: React.FC<AkariSortFiltersProps> = ({ priorityIndicator }) => {
    return (
        <React.Fragment>
            {
                akariSortFilters.sort((sortFilterA, sortFilterB) => {
                    return priorityIndicator(sortFilterA.filterName) - priorityIndicator(sortFilterB.filterName)
                }).map((filterDetails) => {
                    return (
                        <AkariSortFilter
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

export default connector(AkariSortFilters)