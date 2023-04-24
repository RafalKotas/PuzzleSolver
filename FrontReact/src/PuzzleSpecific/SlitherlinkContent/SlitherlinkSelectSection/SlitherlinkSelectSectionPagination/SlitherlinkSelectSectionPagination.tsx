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
import { slitherlinkInformation, selectListFromMode } from "../../../../store/data/slitherlink"


const mapStateToProps = (state: AppState) => ({
    properPuzzlesList: selectListFromMode(state.displayReducer, state.slitherlinkDataReducer),

    puzzleSortFiltersList: state.slitherlinkFiltersReducer.sortFilters,

    selectionFilters: {
        selectedSources: state.slitherlinkFiltersReducer.selectedSources,
        selectedYears: state.slitherlinkFiltersReducer.selectedYears,
        selectedMonths: state.slitherlinkFiltersReducer.selectedMonths,
        selectedDifficulties: state.slitherlinkFiltersReducer.selectedDifficulties,
        selectedWidths: state.slitherlinkFiltersReducer.selectedWidths,
        selectedHeights: state.slitherlinkFiltersReducer.selectedHeights,
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

type SlitherlinkSelectSectionPaginationPropsFromRedux = ConnectedProps<typeof connector>

type SlitherlinkSelectSectionPaginationProps = SlitherlinkSelectSectionPaginationPropsFromRedux

const SlitherlinkSelectSectionPaginationPagination : React.FC<SlitherlinkSelectSectionPaginationProps> 
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

    const hasMatchingFilters = (puzzle: slitherlinkInformation) => {
        //if length(of selected options) === 0 match all
        let matchesSource = includeMatchString(selectionFilters.selectedSources, puzzle.source)
        let matchesYear = includeMatchString(selectionFilters.selectedYears, puzzle.year)
        let matchesMonth = includeMatchString(selectionFilters.selectedMonths, puzzle.month)
        let matchesDifficulty = includeMatchNumber(selectionFilters.selectedDifficulties, puzzle.difficulty)
        let matchesWidth = includeMatchNumber(selectionFilters.selectedWidths, puzzle.width)
        let matchesHeight = includeMatchNumber(selectionFilters.selectedHeights, puzzle.width)
        return matchesSource && matchesYear && matchesMonth && matchesDifficulty && matchesWidth && matchesHeight
    }

    const puzzlesMatchingFilters = (puzzlesList : slitherlinkInformation[]) => {
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

export default connector(SlitherlinkSelectSectionPaginationPagination)