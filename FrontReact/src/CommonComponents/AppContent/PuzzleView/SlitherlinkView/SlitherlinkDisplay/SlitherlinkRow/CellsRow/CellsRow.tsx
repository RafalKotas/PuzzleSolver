// react
import React from "react"

// redux
import { Dispatch } from "redux"
import { connect, ConnectedProps } from "react-redux"

// redux - store
import { AppState } from "../../../../../../../store"
import { calculateBoardDimensionInPx, selectCellSize, selectEdgeWidth } from "../../../../../../../store/layout/slitherlink"
import { selectBoardWidth } from "../../../../../../../store/data/slitherlink"

// styles
import "../../SlitherlinkDisplay.css"
import VerticalWhiteEdge from "./VerticalWhiteEdge"

interface OwnCellsRowProps {
    rowIdx: number,
    slitherlinkRow: number[]

}

const mapStateToProps = (state: AppState) => ({
    cellSize: selectCellSize(state.slitherlinkLayoutReducer),
    boardWidth: selectBoardWidth(state.slitherlinkDataReducer),
    boardWidthInPx: calculateBoardDimensionInPx(state.slitherlinkLayoutReducer, state.slitherlinkDataReducer, "width"),
    edgeWidth: selectEdgeWidth(state.slitherlinkLayoutReducer),
})

const mapDispatchToProps = (dispatch: Dispatch) => ({

})


const connector = connect(mapStateToProps, mapDispatchToProps)

type CellsRowPropsFromRedux = ConnectedProps<typeof connector>

type CellsRowProps = CellsRowPropsFromRedux & OwnCellsRowProps

const CellsRow : React.FC<CellsRowProps> = ({rowIdx, slitherlinkRow, cellSize, boardWidth, boardWidthInPx, edgeWidth}) => {
    return (
        <div style={{
            display: "flex",
            flexDirection: "row",
            height: cellSize,
            width: boardWidthInPx + "px"
        }}>
            {
                slitherlinkRow.map((slitherlinkCell, colIdx) => {
                    return (
                        <React.Fragment>
                            <VerticalWhiteEdge rowIdx={rowIdx} colIdx={colIdx}/>
                            <div
                                className={"slitherlink-cell"}
                                style={{
                                    display: "flex",
                                    alignItems: "center",
                                    justifyContent: "center",
                                    fontSize: 2 * (cellSize / 3),
                                    height: cellSize,
                                    width: cellSize
                                }}
                            >
                                {slitherlinkCell !== -1 ? slitherlinkCell.toString() : ""}
                            </div>
                        </React.Fragment>
                    )
                })
            }
            <VerticalWhiteEdge rowIdx={rowIdx} colIdx={boardWidth}/>
        </div>
    )
}

export default connector(CellsRow)