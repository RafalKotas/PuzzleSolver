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

// (sub)component(s)
import SudokuPreviewButton from "CommonComponents/AppContent/PuzzleView/SudokuView/EditView/ValidationPanel/SudokuPreviewButton/SudokuPreviewButton"
import SudokuValidationButton from "CommonComponents/AppContent/PuzzleView/SudokuView/EditView/ValidationPanel/SudokuValidationButton/SudokuValidationButton"
import SaveSection from "CommonComponents/AppContent/PuzzleView/NonogramView/EditView/ValidateNonogramSection/SaveSection/SaveSection"

// styles
import "./SudokuValidationPanel.css"

const mapStateToProps = (state: AppState) => ({
    sudokuBoardData: state.sudokuDataReducer.selectedSudoku.board,
    isSudokuCorrect: state.sudokuDataReducer.sudokuCorrect
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SudokuValidationPanelPropsFromRedux = ConnectedProps<typeof connector>

type SudokuValidationPanelProps = SudokuValidationPanelPropsFromRedux

const SudokuValidationPanel: React.FC<SudokuValidationPanelProps> = ({ isSudokuCorrect }) => {

    useEffect(() => {
        if (isSudokuCorrect === 1) {
            
        }
    }, [isSudokuCorrect])

    return <div id="sudoku-validation-panel" className={""}>
        <SudokuPreviewButton />
        <SudokuValidationButton />
        <SaveSection disabledCondition={!isSudokuCorrect} />
    </div>
}

export default connector(SudokuValidationPanel)