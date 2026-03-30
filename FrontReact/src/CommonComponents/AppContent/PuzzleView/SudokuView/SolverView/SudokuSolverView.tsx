// react
import React, { useEffect } from "react"

// redux
import { 
    connect, 
    ConnectedProps 
} from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "@store/index"
import { InitializeSudokuBoardSolver } from "store/puzzleLogic/sudoku"

// (sub)component(s)
import SudokuDisplay from "CommonComponents/AppContent/PuzzleView/SudokuView/SudokuDisplay/SudokuDisplay"
import SudokuActions from "CommonComponents/AppContent/PuzzleView/SudokuView/SolverView/SudokuActions/SudokuActions"

// styles
import "../SudokuView.css"

const mapStateToProps = (state: AppState) => ({
    sudokuBoardData: state.sudokuDataReducer.selectedSudoku.board
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    initializeSudokuBoardToLogicState: (sudokuBoard : Array<Array<number>>) => dispatch(InitializeSudokuBoardSolver(sudokuBoard))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SudokuSolverViewPropsFromRedux = ConnectedProps<typeof connector>

type SudokuSolverViewProps = SudokuSolverViewPropsFromRedux

const SudokuSolverView : React.FC<SudokuSolverViewProps> = ({ sudokuBoardData, initializeSudokuBoardToLogicState }) => {

    useEffect(() => {
        initializeSudokuBoardToLogicState(sudokuBoardData)
        //eslint-disable-next-line
    }, [])

    return (
        <div id="selected-sudoku-view">
            <div
                id="puzzle-view-container"  
                style={{
                        overflowX: "auto",
                        overflowY: "auto"
                        }}
            >
                <SudokuDisplay/>
            </div>
            <SudokuActions />
        </div>
    )
}

export default connector(SudokuSolverView)