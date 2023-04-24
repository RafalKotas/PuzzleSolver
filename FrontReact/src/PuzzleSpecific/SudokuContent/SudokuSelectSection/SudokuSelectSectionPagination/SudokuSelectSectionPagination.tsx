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
import { sudokuInformation, selectListFromMode } from "../../../../store/data/sudoku"


const mapStateToProps = (state: AppState) => ({
    properPuzzlesList: selectListFromMode(state.displayReducer, state.sudokuDataReducer),

    puzzleSortFiltersList: state.sudokuFiltersReducer.sortFilters,

    selectionFilters: {
        selectedSources: state.sudokuFiltersReducer.selectedSources,
        selectedDifficulties: state.sudokuFiltersReducer.selectedDifficulties,
        selectedFilled: state.sudokuFiltersReducer.selectedFilled
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

type SudokuSelectSectionPaginationPropsFromRedux = ConnectedProps<typeof connector>

type SudokuSelectSectionPaginationProps = SudokuSelectSectionPaginationPropsFromRedux

const SudokuSelectSectionPaginationPagination : React.FC<SudokuSelectSectionPaginationProps> 
    = ({properPuzzlesList, selectionFilters, puzzleSortFiltersList, itemsPerPage, currentPage, setCurrentPage}) => {


    useEffect(() => {
        setCurrentPage(1)
        //eslint-disable-next-line
    }, [selectionFilters.selectedDifficulties, selectionFilters.selectedSources, selectionFilters.selectedFilled,
         puzzleSortFiltersList.length])

    const handleChange = (event: React.ChangeEvent<unknown>, value: number) => {
        setCurrentPage(value)
    }

    const includeMatchString = (selectedValues : string[] , optionValue: string) => {
        return selectedValues.length > 0 ? selectedValues.includes(optionValue) : true
    }

    const includeMatchNumber = (valueRange : number[], optionValue: number) => {
        return valueRange.length > 0 ? commonFunctions.isValueInRange(optionValue, valueRange) : true
    }

    const hasMatchingFilters = (puzzle: sudokuInformation) => {
        //if length(of selected options) === 0 match all
        let matchesSource = includeMatchString(selectionFilters.selectedSources, puzzle.source)
        let matchesDifficulties = includeMatchNumber(selectionFilters.selectedDifficulties, puzzle.difficulty)
        let matchesFilled = includeMatchNumber(selectionFilters.selectedFilled, puzzle.filled)
        return matchesSource && matchesDifficulties && matchesFilled
    }

    const puzzlesMatchingFilters = (puzzlesList : sudokuInformation[]) => {
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

export default connector(SudokuSelectSectionPaginationPagination)