// react
import React, { useEffect } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../store"
import SaveSection from "../../../NonogramView/EditView/ValidateNonogramSection/SaveSection/SaveSection"

// (sub) components
import SudokuPreviewButton from "./SudokuPreviewButton/SudokuPreviewButton"
import SudokuValidationButton from "./SudokuValidationButton/SudokuValidationButton"

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
        if(isSudokuCorrect === 1) {
            
        }
    }, [isSudokuCorrect])

    return <div id="sudoku-validation-panel" className={""}>
        <SudokuPreviewButton />
        <SudokuValidationButton />
        <SaveSection disabledCondition={!isSudokuCorrect} />
    </div>
}

export default connector(SudokuValidationPanel)