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
import { hitoriInformation, selectListFromMode } from "../../../../store/data/hitori"


const mapStateToProps = (state: AppState) => ({
    properPuzzlesList: selectListFromMode(state.displayReducer, state.hitoriDataReducer),

    puzzleSortFiltersList: state.hitoriFiltersReducer.sortFilters,

    selectionFilters: {
        selectedSources: state.hitoriFiltersReducer.selectedSources,
        selectedDifficulties: state.hitoriFiltersReducer.selectedDifficulties,
        selectedWidths: state.hitoriFiltersReducer.selectedWidths,
        selectedHeights: state.hitoriFiltersReducer.selectedHeights,
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

type HitoriSelectSectionPaginationPropsFromRedux = ConnectedProps<typeof connector>

type HitoriSelectSectionPaginationProps = HitoriSelectSectionPaginationPropsFromRedux

const HitoriSelectSectionPaginationPagination : React.FC<HitoriSelectSectionPaginationProps> 
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

    const hasMatchingFilters = (puzzle: hitoriInformation) => {
        //if length(of selected options) === 0 match all
        let matchesSource = includeMatchString(selectionFilters.selectedSources, puzzle.source)
        let matchesDifficulties = includeMatchNumber(selectionFilters.selectedDifficulties, puzzle.difficulty)
        let matchesWidths = includeMatchNumber(selectionFilters.selectedWidths, puzzle.width)
        let matchesHeights = includeMatchNumber(selectionFilters.selectedHeights, puzzle.width)
        return matchesSource && matchesDifficulties && matchesWidths && matchesHeights
    }

    const puzzlesMatchingFilters = (puzzlesList : hitoriInformation[]) => {
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

export default connector(HitoriSelectSectionPaginationPagination)