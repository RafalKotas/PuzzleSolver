// react
import { useEffect} from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../store"
import { displayModes, SetCurrentPage, SetDisplayMode } from "../../../../store/display"
import { nonogramInformation, selectListFromMode } from "../../../../store/data/nonogram"

// mui
import { Pagination } from "@mui/material"

// own functions
import commonFunctions from "../../../../functions"


const mapStateToProps = (state: AppState) => ({
    properNonogramsList: selectListFromMode(state.displayReducer, state.nonogramDataReducer),

    nonogramSortFiltersList: state.nonogramFiltersReducer.sortFilters,

    selectionFilters: {
        selectedSources: state.nonogramFiltersReducer.selectedSources,
        selectedYears: state.nonogramFiltersReducer.selectedYears,
        selectedMonths: state.nonogramFiltersReducer.selectedMonths,
        selectedDifficulties: state.nonogramFiltersReducer.selectedDifficulties,
        selectedWidths: state.nonogramFiltersReducer.selectedWidths,
        selectedHeights: state.nonogramFiltersReducer.selectedHeights,
    },

    currentPage: state.displayReducer.currentPage,
    itemsPerPage: state.displayReducer.itemsPerPage
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setDisplayMode: (displayMode: displayModes) =>
        dispatch(SetDisplayMode(displayMode)),
    setCurrentPage: (currentPage: number) =>
        dispatch(SetCurrentPage(currentPage))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type NonogramSelectSectionPaginationPropsFromRedux = ConnectedProps<typeof connector>

type NonogramSelectSectionPaginationProps = NonogramSelectSectionPaginationPropsFromRedux

const NonogramSelectSectionPaginationPagination : React.FC<NonogramSelectSectionPaginationProps> 
    = ({properNonogramsList, selectionFilters, nonogramSortFiltersList, itemsPerPage, currentPage, setCurrentPage}) => {

    useEffect(() => {
        setCurrentPage(1)
        //eslint-disable-next-line
    }, [selectionFilters.selectedDifficulties, selectionFilters.selectedHeights, selectionFilters.selectedWidths, 
    selectionFilters.selectedYears, selectionFilters.selectedMonths, selectionFilters.selectedSources, 
    nonogramSortFiltersList.length])

    const handleChange = (event: React.ChangeEvent<unknown>, value: number) => {
        setCurrentPage(value)
    }

    const includeMatchString = (selectedValues : string[] , optionValue: string) => {
        return selectedValues.length > 0 ? selectedValues.includes(optionValue) : true
    }

    const includeMatchNumber = (valueRange : number[], optionValue: number) => {
        return valueRange.length > 0 ? commonFunctions.isValueInRange(optionValue, valueRange) : true
    }

    const hasMatchingFilters = (nonogram: nonogramInformation) => {
        //if length(of selected options) === 0 match all
        let matchesSource = includeMatchString(selectionFilters.selectedSources, nonogram.source)
        let matchesYear = includeMatchString(selectionFilters.selectedYears, nonogram.year)
        let matchesMonth = includeMatchString(selectionFilters.selectedMonths, nonogram.month)
        let matchesDifficulties = includeMatchNumber(selectionFilters.selectedDifficulties, nonogram.difficulty)
        let matchesWidths = includeMatchNumber(selectionFilters.selectedWidths, nonogram.width)
        let matchesHeights = includeMatchNumber(selectionFilters.selectedHeights, nonogram.height)
        return matchesSource && matchesYear && matchesMonth && matchesDifficulties && matchesWidths && matchesHeights
    }

    const nonogramsMatchingFilters = (listOfNonograms : nonogramInformation[]) => {
        return listOfNonograms.filter((nonogram) => {
            return hasMatchingFilters(nonogram)
        })
    }

    return (
        <Pagination 
            count={Math.ceil(nonogramsMatchingFilters(properNonogramsList).length / itemsPerPage)} 
            page={currentPage}
            onChange={handleChange} 
            showFirstButton 
            showLastButton 
        />
    )
}

export default connector(NonogramSelectSectionPaginationPagination)