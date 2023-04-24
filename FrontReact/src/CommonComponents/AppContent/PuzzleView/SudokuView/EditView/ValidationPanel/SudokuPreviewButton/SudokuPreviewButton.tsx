// react
import { useEffect, useState } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../../store"
import { ToggleEditMode, TogglePreviewMode } from "../../../../../../../store/puzzleLogic/sudoku"

import { Button } from "@mui/material"
import { SetCorrectness } from "../../../../../../../store/data/sudoku"

const mapStateToProps = (state: AppState) => ({
    availableNumbers: state.sudokuLogicReducer.availableNumbers,
    sudokuBoardForEditMode: state.sudokuDataReducer.createdSudoku.board
})

const mapDispatchToProps = (dispatch : Dispatch) => ({
    saveSudokuBoard: () =>
        dispatch(TogglePreviewMode()),
    toggleEditMode: (sudokuBoardForEditMode: number[][]) =>
        dispatch(ToggleEditMode(sudokuBoardForEditMode)),
    unSetCorrectness: () => dispatch(SetCorrectness(0))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SudokuPreviewButtonPropsFromRedux = ConnectedProps<typeof connector>

type SudokuPreviewButtonProps = SudokuPreviewButtonPropsFromRedux

const SudokuPreviewButton : React.FC<SudokuPreviewButtonProps> = ({sudokuBoardForEditMode, 
    saveSudokuBoard, toggleEditMode, unSetCorrectness}) => {

    const [mode, setMode] = useState<"PREVIEW" | "EDIT">("EDIT")

    const toggleMode = () => {
        if(mode === "EDIT") {
            setMode("PREVIEW")
            saveSudokuBoard()
        } else {
            toggleEditMode(sudokuBoardForEditMode)
            setMode("EDIT")
            unSetCorrectness()
        }
    }

    useEffect(() => {

    }, [])

    const buttonLabel = () => {
        return mode === "PREVIEW" ? "EDIT" : "PREVIEW"
    }

    return (
        <Button variant="contained" onClick={(event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => toggleMode()}>
            {buttonLabel()}!
        </Button>
    )
}

export default connector(SudokuPreviewButton)