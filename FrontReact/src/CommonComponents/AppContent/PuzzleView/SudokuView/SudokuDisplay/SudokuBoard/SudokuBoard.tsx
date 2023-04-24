// react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../store"

// (sub) components
import EditSquare from "../EditSquare/EditSquare"
import CorrectFilledSquare from "../CorrectFilledSquare/CorrectFilledSquare"

interface OwnSudokuBoardProps {

}

const mapStateToProps = (state: AppState) => ({
    mode: state.displayReducer.mode,
    filled: state.sudokuLogicReducer.filled,
    sudokuBoardLogic: state.sudokuLogicReducer.sudokuBoard,
    createStep: state.sudokuLogicReducer.createStep
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SudokuBoardPropsFromRedux = ConnectedProps<typeof connector>

type SudokuBoardProps = SudokuBoardPropsFromRedux & OwnSudokuBoardProps

const SudokuBoard: React.FC<SudokuBoardProps> = ({ sudokuBoardLogic, mode, createStep }) => {
    return (
        <div style={{
            display: "flex",
            flexDirection: "column",
            height: "419px",
            width: "419px",
            border: "4px solid black"
        }}>
            {
                sudokuBoardLogic.map((rowOfSquares, rowIdx) => {
                    return (<div 
                        key={"row-of-squares-" + rowIdx}
                        style={
                        {
                            display: "flex",
                            height: "45px",
                            width: "423px",
                            borderBottom: ((rowIdx % 3 === 2 && rowIdx !== 8) ? "4px" : "1px") + " solid black",
                            flexDirection: "row"
                        }
                    }>
                        {
                            rowOfSquares.map((digit, columnIdx) => {
                                return <div
                                    key={"sudoku-square-" + rowIdx + "-" + columnIdx + "-" + sudokuBoardLogic[rowIdx][columnIdx]}
                                    className="sudoku-square"
                                    style={{
                                        borderRight: ((columnIdx % 3 === 2 && columnIdx !== 8) ? "4px" : "1px") + " solid black"
                                    }}>
                                    {((mode === "CREATE" && createStep === "EDIT") || digit === 0)
                                        ?
                                        <EditSquare squareCoords={{ rowIdx: rowIdx, columnIdx: columnIdx }} />
                                        :
                                        <CorrectFilledSquare digit={digit} coords={{ rowIdx, columnIdx }} />}
                                </div>
                            })
                        }
                    </div>)
                })
            }
        </div>
    )
}

export default connector(SudokuBoard)