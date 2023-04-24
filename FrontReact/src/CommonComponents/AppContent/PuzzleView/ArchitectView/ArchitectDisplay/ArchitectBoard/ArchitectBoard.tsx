// react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../store"
import { calculateBoardDimensionInPx, calculateCellWithBorderSize } from "../../../../../../store/layout/architect"

// fontawesome
import { faHouseChimney } from "@fortawesome/free-solid-svg-icons"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"

// (sub)components

interface OwnArchitectBoardProps {

}

const mapStateToProps = (state: AppState) => ({
    architectBoard: state.architectDataReducer.selectedArchitect.board,
    cellSize: state.architectLayoutReducer.cellSize,
    borderWidth: state.architectLayoutReducer.cellBorder,
    cellWithBorderSize: calculateCellWithBorderSize(state.architectLayoutReducer),
    boardHeight: calculateBoardDimensionInPx(state.architectLayoutReducer, state.architectDataReducer, "height"),
    boardWidth: calculateBoardDimensionInPx(state.architectLayoutReducer, state.architectDataReducer, "width"),
})

const mapDispatchToProps = (dispatch: Dispatch) => ({

})

const connector = connect(mapStateToProps, mapDispatchToProps)

type ArchitectBoardPropsFromRedux = ConnectedProps<typeof connector>

type ArchitectBoardProps = ArchitectBoardPropsFromRedux & OwnArchitectBoardProps

const ArchitectBoard : React.FC<ArchitectBoardProps> = ({architectBoard, cellSize, borderWidth, cellWithBorderSize, boardHeight, boardWidth}) => {
    
    return (<div 
    id="architect-board"
    style={{
        display: "flex",
        flexDirection: "column",
        height: boardHeight + "px",
        width: boardWidth + "px"
    }}>
        {
            architectBoard.map((rowOfSquares, rowIdx) => {
                return (<div style={
                    {
                        display: "flex",
                        height: cellWithBorderSize,
                        width: boardWidth,
                        flexDirection: "row"
                    }
                }>
                    {
                        rowOfSquares.map((square, columnIdx) => {
                            return <div
                                className="architect-single-square"
                                style={{
                                    height: cellSize + "px",
                                    width: cellSize + "px",
                                    display: "flex",
                                    justifyContent: "center",
                                    alignItems: "center",
                                    fontSize: (2 * cellWithBorderSize) / 3 + "px",
                                    borderLeft: borderWidth + "px solid black",
                                    borderTop: borderWidth + "px solid black",
                                    borderRight: ((columnIdx === architectBoard[0].length - 1) ? borderWidth : "0") + "px solid black",
                                    borderBottom: ((rowIdx === architectBoard.length - 1) ? borderWidth : "0") + "px solid black"
                                }}>
                                {
                                    architectBoard[rowIdx][columnIdx] === "H" && <FontAwesomeIcon icon={faHouseChimney} />
                                }
                            </div>
                        })
                    }
                </div>)
            })
        }
    </div>)

}

export default connector(ArchitectBoard)