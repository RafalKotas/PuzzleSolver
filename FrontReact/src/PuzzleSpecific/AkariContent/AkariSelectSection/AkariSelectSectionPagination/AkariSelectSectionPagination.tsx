// react
import { useEffect} from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../store"
import { displayModes, SetCurrentPage, SetDisplayMode } from "../../../../store/display"

// mui
import { Pagination } from "@mui/material"

// own functions
import commonFunctions from "../../../../functions"
import { akariInformation, selectListFromMode } from "../../../../store/data/akari"


const mapStateToProps = (state: AppState) => ({
    properPuzzlesList: selectListFromMode(state.displayReducer, state.akariDataReducer),

    puzzleSortFiltersList: state.akariFiltersReducer.sortFilters,

    selectionFilters: {
        selectedSources: state.akariFiltersReducer.selectedSources,
        selectedDifficulties: state.akariFiltersReducer.selectedDifficulties,
        selectedWidths: state.akariFiltersReducer.selectedWidths,
        selectedHeights: state.akariFiltersReducer.selectedHeights,
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

type AkariSelectSectionPaginationPropsFromRedux = ConnectedProps<typeof connector>

type AkariSelectSectionPaginationProps = AkariSelectSectionPaginationPropsFromRedux

const AkariSelectSectionPaginationPagination : React.FC<AkariSelectSectionPaginationProps> 
    = ({properPuzzlesList, selectionFilters, puzzleSortFiltersList, itemsPerPage, currentPage, setCurrentPage}) => {

    useEffect(() => {
        setCurrentPage(1)
        //eslint-disable-next-line
    }, [selectionFilters.selectedDifficulties, selectionFilters.selectedHeights, 
        selectionFilters.selectedWidths, selectionFilters.selectedSources, puzzleSortFiltersList.length])

    const handleChange = (event: React.ChangeEvent<unknown>, value: number) => {
        setCurrentPage(value)
    }

    const includeMatchString = (selectedValues : string[] , optionValue: string) => {
        return selectedValues.length > 0 ? selectedValues.includes(optionValue) : true
    }

    const includeMatchNumber = (valueRange : number[], optionValue: number) => {
        return valueRange.length > 0 ? commonFunctions.isValueInRange(optionValue, valueRange) : true
    }

    const hasMatchingFilters = (puzzle: akariInformation) => {
        //if length(of selected options) === 0 match all
        let matchesSource = includeMatchString(selectionFilters.selectedSources, puzzle.source)
        let matchesDifficulties = includeMatchNumber(selectionFilters.selectedDifficulties, puzzle.difficulty)
        let matchesWidths = includeMatchNumber(selectionFilters.selectedWidths, puzzle.width)
        let matchesHeights = includeMatchNumber(selectionFilters.selectedHeights, puzzle.width)
        return matchesSource && matchesDifficulties && matchesWidths && matchesHeights
    }

    const puzzlesMatchingFilters = (puzzlesList : akariInformation[]) => {
        return puzzlesList.filter((puzzle) => {
            return hasMatchingFilters(puzzle)
        })
    }

    return (
        <Pagination 
            count={Math.ceil(puzzlesMatchingFilters(properPuzzlesList).length / itemsPerPage)} 
            page={currentPage}
            onChange={handleChange} 
            showFirstButton 
            showLastButton 
        />
    )
}

export default connector(AkariSelectSectionPaginationPagination)