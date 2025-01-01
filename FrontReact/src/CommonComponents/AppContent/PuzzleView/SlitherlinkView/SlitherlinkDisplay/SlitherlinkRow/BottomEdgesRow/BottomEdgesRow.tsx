// react
import React from "react"

// redux
import { Dispatch } from "redux"
import { connect, ConnectedProps } from "react-redux"

// redux - store
import { AppState } from "../../../../../../../store"
import { selectCellSize } from "../../../../../../../store/layout/slitherlink"
import SmallBlacksquare from "../../SmallBlackSquare/SmallBlacksquare"

interface OwnBottomEdgesRowProps {
    boardWidth: number,
    lastRowIdx: number,
    edgeWidthInPx: number,
    boardWidthInPx: number,

}

const mapStateToProps = (state: AppState) => ({
    cellSize: selectCellSize(state.slitherlinkLayoutReducer),
})

const mapDispatchToProps = (dispatch: Dispatch) => ({

})


const connector = connect(mapStateToProps, mapDispatchToProps)

type BottomEdgesRowPropsFromRedux = ConnectedProps<typeof connector>

type BottomEdgesRowProps = BottomEdgesRowPropsFromRedux & OwnBottomEdgesRowProps

const BottomEdgesRow : React.FC<BottomEdgesRowProps> = ({boardWidth, lastRowIdx, cellSize, edgeWidthInPx, boardWidthInPx}) => {

    const edgeKey = (colIdx: number) => {
        return "edge-element-" + lastRowIdx + "" + colIdx + "-" + lastRowIdx + "" + (colIdx + 1);
    }

    return (
        <div style={{ display: "flex", flexDirection: "row", height: edgeWidthInPx, width: boardWidthInPx }}>
            {
                Array.from({ length: boardWidth }, (_, idx) => idx).map((colIdx) => {
                    return (
                        <React.Fragment key={"last-edges-row-" + colIdx}>
                            <SmallBlacksquare edgeWidthInPx={edgeWidthInPx}/>
                            <div
                                id={"edge-" + lastRowIdx + "" + colIdx + "" + lastRowIdx + "" + (colIdx + 1)}
                                className={"white-edge-horizontal"}
                                style={{ display: "flex", flexDirection: "row", height: (edgeWidthInPx - 2), width: cellSize, fontSize: (edgeWidthInPx / 2) + "px" }}
                            >
                                {
                                    [lastRowIdx + "", colIdx + "", "-", lastRowIdx + "", (colIdx + 1) + ""].map((edgeElement, _) => {
                                        return (
                                        <div 
                                            key={edgeKey(colIdx)} 
                                            style={{ width: "20%", cursor: "pointer" }}
                                        >
                                            {edgeElement}
                                        </div>)
                                    })
                                }
                            </div>
                        </React.Fragment>
                    )
                })
            }
            <SmallBlacksquare edgeWidthInPx={edgeWidthInPx}/>
        </div>
    )
}

export default connector(BottomEdgesRow)