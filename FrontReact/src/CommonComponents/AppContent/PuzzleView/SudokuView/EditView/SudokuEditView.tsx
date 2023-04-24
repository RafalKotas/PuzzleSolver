// react
import { useEffect } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../store"
import { InitializeSudokuBoardEdit, ResetSudokuEditData, ToggleEditMode } from "../../../../../store/puzzleLogic/sudoku"

// (sub) components
import SudokuDisplay from "../SudokuDisplay/SudokuDisplay"
import SudokuValidationPanel from "./ValidationPanel/SudokuValidationPanel"

// styles
import "../SudokuView.css"
import { SetCorrectness } from "../../../../../store/data/sudoku"

const mapStateToProps = (state: AppState) => ({
    // ok
    sudokuBoardData: state.sudokuDataReducer.createdSudoku.board
})

const mapDispatchToProps = (dispatch : Dispatch) => ({
    initializeSudokuBoardEditMode: (sudokuBoard : Array<Array<number>>) =>
        dispatch(InitializeSudokuBoardEdit(sudokuBoard)),
    resetSudokuEditBoard: () => dispatch(ResetSudokuEditData()),
    unsetCorrectness: () => dispatch(SetCorrectness(0)),
    toggleEditMode: (sudokuBoard : number[][]) => dispatch(ToggleEditMode(sudokuBoard))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SudokuEditViewPropsFromRedux = ConnectedProps<typeof connector>

type SudokuEditViewProps = SudokuEditViewPropsFromRedux

const SudokuEditView : React.FC<SudokuEditViewProps> = ({sudokuBoardData, 
    initializeSudokuBoardEditMode, unsetCorrectness, toggleEditMode}) => {

    useEffect(() => {
        initializeSudokuBoardEditMode(sudokuBoardData)
        unsetCorrectness()
        toggleEditMode(sudokuBoardData)
        //eslint-disable-next-line
    }, [])

    return (
        <div id="selected-sudoku-view">
            <SudokuValidationPanel />
            <div
                id="edit-view-container"  
                style={{
                        overflowX: "auto",
                        overflowY: "auto",
                        flexDirection: "row"
                        }}
            >
                <SudokuDisplay/>
            </div>
            {/*<EditSudokuActions key={"edit-sudoku-actions"}/>*/}
        </div>
    )
}

export default connector(SudokuEditView)