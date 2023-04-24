// react
import React from "react"

// redux
import { Dispatch } from "redux"
import { connect, ConnectedProps } from "react-redux"

// redux - store
import { AppState } from "../../../../../store"
import { selectBoardHeight, selectBoardWidth } from "../../../../../store/data/hitori"
import { calculateBoardDimensionInPx, calculateCellWithBorderSize } from "../../../../../store/layout/hitori"

// styles
import "./HitoriDisplay.css"
import HitoriInformationTooltip from "../HitoriInformationTooltip/HitoriInformationTooltip"

const mapStateToProps = (state: AppState) => ({
    hitoriBoard: state.hitoriDataReducer.selectedHitori.board,
    
    boardHeight: selectBoardHeight(state.hitoriDataReducer),
    boardWidth: selectBoardWidth(state.hitoriDataReducer),
    boardHeightInPx: calculateBoardDimensionInPx(state.hitoriLayoutReducer, state.hitoriDataReducer, "height"),
    boardWidthInPx: calculateBoardDimensionInPx(state.hitoriLayoutReducer, state.hitoriDataReducer, "width"),
    
    frameBorder: state.hitoriLayoutReducer.frameBorder,
    cellSize: state.hitoriLayoutReducer.cellSize,
    cellBorder: state.hitoriLayoutReducer.cellBorder,
    cellWithBorderSize: calculateCellWithBorderSize(state.hitoriLayoutReducer)
})

const mapDispatchToProps = (dispatch: Dispatch) => ({

})

const connector = connect(mapStateToProps, mapDispatchToProps)

type HitoriSolverViewPropsFromRedux = ConnectedProps<typeof connector>

type HitoriSolverViewProps = HitoriSolverViewPropsFromRedux

const HitoriSolverView : React.FC<HitoriSolverViewProps> = ({ hitoriBoard, cellSize, cellBorder, cellWithBorderSize,
    frameBorder, boardHeight, boardWidth, boardHeightInPx, boardWidthInPx }) => {

    const borderTopFromRow = (rowIdx : number) => (cellBorder + ((rowIdx === 0) ? frameBorder : 0))
    const borderRightFromColumn = (columnIdx : number) => ((columnIdx === (boardHeight - 1)) ? (cellBorder + frameBorder) : 0)
    const borderBottomFromRow = (rowIdx : number) => ((rowIdx === (boardWidth - 1)) ? (cellBorder + frameBorder) : 0)
    const borderLeftFromColumn = (columnIdx : number) => (cellBorder + ((columnIdx === 0) ? (frameBorder) : 0))

    return (
        <div
        id="hitori-display" 
        style={{
            display: "flex",
            flexDirection: "row",
            height: (boardHeightInPx + frameBorder) + "px",
            width: (boardWidthInPx + frameBorder) + "px",
            minWidth: "fit-content"
        }}>
            <div id="hitori-info">
                <HitoriInformationTooltip />
            </div>
            <div>
                {
                    hitoriBoard.map((rowOfSquares, rowIdx) => {
                        return (<div style={
                                    {
                                        display: "flex",
                                        height: cellWithBorderSize, 
                                        width: boardWidthInPx + frameBorder + "px", 
                                        flexDirection: "row"
                                    }
                                }>
                            {
                                rowOfSquares.map((square, columnIdx) => {
                                    return <div
                                        className="hitori-square"
                                        style={{
                                            height: cellSize,
                                            width: cellSize,
                                            borderTop: borderTopFromRow(rowIdx) + "px solid black",
                                            borderRight: borderRightFromColumn(columnIdx) + "px solid black",
                                            borderBottom: borderBottomFromRow(rowIdx) + "px solid black",
                                            borderLeft: borderLeftFromColumn(columnIdx) + "px solid black",
                                            backgroundColor: "#f6f0d0",
                                            fontWeight: "bold",
                                            fontSize: 2 * (cellSize / 3)
                                        }}
                                    >
                                        {square}
                                    </div>
                                })
                            }
                        </div>)
                    })
                }
            </div>
        </div>
    )
}

export default connector(HitoriSolverView)