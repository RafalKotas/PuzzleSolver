// react
import React from "react"
import { useEffect } from "react"

// redux
import { Dispatch } from "redux"
import { connect, ConnectedProps } from "react-redux"

// redux - store
import { AppState } from "../../../../../store"
import { calculateBoardDimensionInPx, selectCellPlusEdgeHeight, selectCellSize, selectEdgeWidth, SetCellSize } from "../../../../../store/layout/slitherlink"

//

// styles
import "./SlitherlinkDisplay.css"
import { selectBoardHeight, selectBoardWidth } from "../../../../../store/data/slitherlink"
import SlitherLinkInformationTooltip from "./SlitherLinkInformationTooltip/SlitherLinkInformationTooltip"

interface OwnSlitherlinkDisplayProps {

}

const mapStateToProps = (state: AppState) => ({
    slitherlinkBoard: state.slitherlinkDataReducer.selectedSlitherlink.board,
    boardHeight: selectBoardHeight(state.slitherlinkDataReducer),
    boardWidth: selectBoardWidth(state.slitherlinkDataReducer),
    boardHeightInPx: calculateBoardDimensionInPx(state.slitherlinkLayoutReducer, state.slitherlinkDataReducer, "height"),
    boardWidthInPx: calculateBoardDimensionInPx(state.slitherlinkLayoutReducer, state.slitherlinkDataReducer, "width"),
    cellSize: selectCellSize(state.slitherlinkLayoutReducer),
    edgeWidth: selectEdgeWidth(state.slitherlinkLayoutReducer),
    cellPlusEdgeHeight: selectCellPlusEdgeHeight(state.slitherlinkLayoutReducer)
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setCellSize: (cellSize: number) =>
        dispatch(SetCellSize(cellSize)),
})


const connector = connect(mapStateToProps, mapDispatchToProps)

type SlitherlinkDisplayPropsFromRedux = ConnectedProps<typeof connector>

type SlitherlinkDisplayProps = SlitherlinkDisplayPropsFromRedux & OwnSlitherlinkDisplayProps

const SlitherlinkDisplay: React.FC<SlitherlinkDisplayProps> = ({ slitherlinkBoard, boardHeightInPx, boardWidthInPx,
    boardWidth, cellSize, edgeWidth, cellPlusEdgeHeight }) => {

    useEffect(() => {

        // eslint-disable-next-line
    }, [])

    return (
        <div
            id="slitherlink-display"
            style={{
                display: "flex",
                marginLeft: "30px",
                flexDirection: "column",
                height: (boardHeightInPx + 30) + "px",
                width: (boardWidthInPx + 30) + "px"
            }}
        >
            <div id="slitherlink-info">
                <SlitherLinkInformationTooltip/>
            </div>
            <div id="slitherlink-board">
                {
                    slitherlinkBoard.map((slitherlinkRow, rowIdx) => {
                        return (
                            <div style={{
                                height: cellPlusEdgeHeight + "px",
                                width: boardWidthInPx + "px",
                                display: "flex",
                                flexDirection: "column",
                                justifyContent: "center",
                                alignItems: "center"
                            }}>
                                {/*row of horizontal edges */}
                                <div style={{
                                    display: "flex",
                                    flexDirection: "row",
                                    height: edgeWidth
                                }}>
                                    {
                                        slitherlinkRow.map((slitherlinkCell, colIdx) => {
                                            return (
                                                <React.Fragment>
                                                    <div className="small-black-square" style={{ height: edgeWidth, width: edgeWidth }}>

                                                    </div>
                                                    <div
                                                        id={"edge-" + rowIdx + "" + colIdx + "" + rowIdx + "" + (colIdx + 1)}
                                                        className={"white-edge-horizontal"}
                                                        style={{ display: "flex", justifyContent: "center", height: (edgeWidth - 2), width: cellSize, fontSize: (edgeWidth / 2) + "px" }}
                                                    >
                                                        {
                                                            [rowIdx + "", colIdx + "", "-", rowIdx + "", (colIdx + 1) + ""].map((edgeElement, idx) => {
                                                                return (<div style={{ width: "20%" }}>
                                                                    {edgeElement}
                                                                </div>)
                                                            })
                                                        }
                                                    </div>
                                                </React.Fragment>
                                            )
                                        })
                                    }
                                    <div className="small-black-square" style={{ height: edgeWidth, width: edgeWidth }}>

                                    </div>
                                </div>
                                {/* row of vertical edges and cells */}
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
                                                    <div
                                                        className={"white-edge-vertical"}
                                                        style={{
                                                            display: "flex",
                                                            flexDirection: "column",
                                                            justifyContent: "center",
                                                            height: cellSize,
                                                            width: (edgeWidth - 2),
                                                            fontSize: (edgeWidth / 2) + "px"
                                                        }}
                                                    >
                                                        {
                                                            [rowIdx + "", colIdx + "", "-", (rowIdx + 1) + "", (colIdx + 1) + ""].map((edgeElement, idx) => {
                                                                return (<div style={{ transform: "rotate(90deg)" }}>
                                                                    {edgeElement}
                                                                </div>)
                                                            })
                                                        }
                                                    </div>
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
                                    <div className={"white-edge-vertical"} style={{ display: "flex", flexDirection: "column", height: cellSize, width: (edgeWidth - 2), fontSize: edgeWidth / 2 }}>
                                        {
                                            [rowIdx + "", boardWidth + "", "-", rowIdx + "", (boardWidth + 1) + ""].map((edgeElement, idx) => {
                                                return (<div key={"vertical-edge-r" + rowIdx + "-c" + idx} style={{ transform: "rotate(90deg)" }}>
                                                    {edgeElement}
                                                </div>)
                                            })
                                        }
                                    </div>
                                </div>
                            </div>
                        )
                    })
                }
                <div style={{ display: "flex", flexDirection: "row", height: edgeWidth, width: boardWidthInPx }}>
                    {
                        Array.from({ length: slitherlinkBoard[0].length }, (_, idx) => idx).map((colIdx) => {
                            let rowIdx = slitherlinkBoard.length
                            return (
                                <React.Fragment key={"last-edges-row-" + colIdx}>
                                    <div className="small-black-square" style={{ height: edgeWidth, width: edgeWidth }}></div>
                                    <div
                                        id={"edge-" + rowIdx + "" + colIdx + "" + rowIdx + "" + (colIdx + 1)}
                                        className={"white-edge-horizontal"}
                                        style={{ display: "flex", flexDirection: "row", height: (edgeWidth - 2), width: cellSize, fontSize: (edgeWidth / 2) + "px" }}
                                    >
                                        {
                                            [rowIdx + "", colIdx + "", "-", rowIdx + "", (colIdx + 1) + ""].map((edgeElement, idx) => {
                                                return (<div key={"edge-element-" + rowIdx + "" + colIdx + "-" + rowIdx + "" + (colIdx + 1) + " " + idx} style={{ width: "20%" }}>
                                                    {edgeElement}
                                                </div>)
                                            })
                                        }
                                    </div>
                                </React.Fragment>
                            )
                        })
                    }
                    <div className="small-black-square" style={{ height: edgeWidth, width: edgeWidth }}>

                    </div>
                </div>
            </div>
        </div>
    )
}

export default connector(SlitherlinkDisplay)