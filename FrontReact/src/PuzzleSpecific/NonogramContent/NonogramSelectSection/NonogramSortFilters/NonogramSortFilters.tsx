//react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../store"
import { displayModes, SetDisplayMode } from "../../../../store/display"
import { nonogramSortFilters, nonogramSortFiltersNames, 
    selectFilterNamePriorityIndicator } from "../../../../store/filters/nonogram"

// (sub) components
import NonogramSortFilter from "./NonogramSortFilter/NonogramSortFilter"

const mapStateToProps = (state: AppState) => ({
    nonogramsList: state.nonogramDataReducer.nonogramsList,
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
        dispatch(SetDisplayMode(displayMode))
})


const connector = connect(mapStateToProps, mapDispatchToProps)

type NonogramSortFiltersPropsFromRedux = ConnectedProps<typeof connector>

type NonogramSortFiltersProps = NonogramSortFiltersPropsFromRedux

const NonogramSortFilters : React.FC<NonogramSortFiltersProps> = ({priorityIndicator}) => {
    return (
        <React.Fragment>
            {
                nonogramSortFilters.sort((sortFilterA, sortFilterB) => {
                    return priorityIndicator(sortFilterA.filterName) - priorityIndicator(sortFilterB.filterName)
                    }).map((filterDetails) => {
                        return (
                                <NonogramSortFilter
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

export default connector(NonogramSortFilters)