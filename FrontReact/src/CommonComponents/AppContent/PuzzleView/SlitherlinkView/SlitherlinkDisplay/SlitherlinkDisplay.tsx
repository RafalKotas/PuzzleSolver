// react
import React from "react"
import { useEffect } from "react"

// redux
import { Dispatch } from "redux"
import { connect, ConnectedProps } from "react-redux"

// redux - store
import { AppState } from "../../../../../store"
import { calculateBoardDimensionInPx, selectCellPlusEdgeHeight, selectEdgeWidth, SetCellSize } from "../../../../../store/layout/slitherlink"

// subcomponents
import BottomEdgesRow from "./SlitherlinkRow/BottomEdgesRow/BottomEdgesRow"

// styles
import "./SlitherlinkDisplay.css"
import { selectBoardHeight, selectBoardWidth } from "../../../../../store/data/slitherlink"
import SlitherLinkInformationTooltip from "./SlitherLinkInformationTooltip/SlitherLinkInformationTooltip"
import SmallBlacksquare from "./SmallBlackSquare/SmallBlacksquare"
import EdgesRow from "./SlitherlinkRow/SimpleRow/EdgesRow"
import CellsRow from "./SlitherlinkRow/CellsRow/CellsRow"

interface OwnSlitherlinkDisplayProps {

}

const mapStateToProps = (state: AppState) => ({
    slitherlinkBoard: state.slitherlinkDataReducer.selectedSlitherlink.board,
    boardHeight: selectBoardHeight(state.slitherlinkDataReducer),
    boardWidth: selectBoardWidth(state.slitherlinkDataReducer),
    boardHeightInPx: calculateBoardDimensionInPx(state.slitherlinkLayoutReducer, state.slitherlinkDataReducer, "height"),
    boardWidthInPx: calculateBoardDimensionInPx(state.slitherlinkLayoutReducer, state.slitherlinkDataReducer, "width"),
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
    boardHeight, boardWidth, edgeWidth, cellPlusEdgeHeight }) => {

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
                                        slitherlinkRow.map((_, colIdx) => {
                                            return (
                                                <EdgesRow edgeWidth={edgeWidth} rowIdx={rowIdx} colIdx={colIdx} />
                                            )
                                        })
                                    }
                                    <SmallBlacksquare edgeWidthInPx={edgeWidth}/>
                                </div>
                                {/* row of vertical edges and cells */}
                                <CellsRow rowIdx={rowIdx} slitherlinkRow={slitherlinkRow} />
                            </div>
                        )
                    })
                }
                <BottomEdgesRow 
                    boardWidthInPx={boardWidthInPx} 
                    boardWidth={boardWidth} 
                    lastRowIdx={boardHeight} 
                    edgeWidthInPx={edgeWidth} 
                />
            </div>
        </div>
    )
}

export default connector(SlitherlinkDisplay)