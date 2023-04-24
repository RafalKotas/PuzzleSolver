// react
import { Button } from "@mui/material"
import { useEffect } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../../store"
import { ChangeSudokuDetail, CheckAndSetCorrectness } from "../../../../../../../store/data/sudoku"

const mapStateToProps = (state: AppState) => ({
    isSudokuCorrect: state.sudokuDataReducer.sudokuCorrect,
    createStep: state.sudokuLogicReducer.createStep,
    sudokuBoard: state.sudokuLogicReducer.sudokuBoard
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setCorrectness: (sudokuBoard: Array<Array<number>>) =>
        dispatch(CheckAndSetCorrectness(sudokuBoard)),
    setSudokuBoardData: (sudokuBoard: number[][]) =>
        dispatch(ChangeSudokuDetail("board", sudokuBoard)),

})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SudokuValidationButtonPropsFromRedux = ConnectedProps<typeof connector>

type SudokuValidationButtonProps = SudokuValidationButtonPropsFromRedux

// common component:
// 1. validate function prop
// 2. correct prop
// 3. puzzle-name

const SudokuValidationButton: React.FC<SudokuValidationButtonProps> = ({ sudokuBoard, createStep, isSudokuCorrect, setCorrectness, setSudokuBoardData }) => {

    useEffect(() => {
        if (isSudokuCorrect === 1) {
            setSudokuBoardData(sudokuBoard)
        }
        //eslint-disable-next-line
    }, [isSudokuCorrect])

    const onValidateSudoku = () => {
        setCorrectness(sudokuBoard)
    }

    const correctnessButtonCheckLabel = () => {
        switch (isSudokuCorrect) {
            case -1:
                return "SUDOKU INCORRECT!"
            case 1:
                return "SUDOKU CORRECT!"
            default:
                return "CHECK CORRECTNESS!"
        }
    }

    const returnProperButtonVariant = () => {
        switch (isSudokuCorrect) {
            case -1:
                return "error"
            case 0:
                return "warning"
            case 1:
                return "success"
            default:
                return "warning"
        }
    }

    return (
        <div>
            <Button
                variant="contained"
                color={returnProperButtonVariant()}
                style={{ width: "100%" }}
                onClick={() => onValidateSudoku()}
                disabled={createStep === "EDIT"}
            >
                {correctnessButtonCheckLabel()}
            </Button>
        </div>
    )
}

export default connector(SudokuValidationButton)