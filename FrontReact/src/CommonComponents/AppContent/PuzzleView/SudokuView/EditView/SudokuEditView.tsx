// react
import { useEffect } from "react"

// redux
import { 
    connect, 
    ConnectedProps 
} from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "@store/index"
import { 
    InitializeSudokuBoardEdit, 
    ResetSudokuEditData, 
    ToggleEditMode 
} from "store/puzzleLogic/sudoku"
import { SetCorrectness } from "store/data/sudoku"

// (sub)component(s)
import SudokuDisplay from "CommonComponents/AppContent/PuzzleView/SudokuView/SudokuDisplay/SudokuDisplay"
import SudokuValidationPanel from "CommonComponents/AppContent/PuzzleView/SudokuView/EditView/ValidationPanel/SudokuValidationPanel"

// styles
import "../SudokuView.css"

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