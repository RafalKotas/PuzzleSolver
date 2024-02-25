// react
import React from "react"

// redux
import { Dispatch } from "redux"
import { connect, ConnectedProps } from "react-redux"

// redux - store
import { AppState } from "../../../../../../../store"
import { selectCellSize, selectEdgeWidth } from "../../../../../../../store/layout/slitherlink"
import { selectBoardWidth } from "../../../../../../../store/data/slitherlink"

// styles
import "../../SlitherlinkDisplay.css"

interface OwnVerticalWhiteEdgeProps {
    rowIdx: number,
    colIdx: number
}

const mapStateToProps = (state: AppState) => ({
    cellSize: selectCellSize(state.slitherlinkLayoutReducer),
    boardWidth: selectBoardWidth(state.slitherlinkDataReducer),
    edgeWidth: selectEdgeWidth(state.slitherlinkLayoutReducer),
})

const mapDispatchToProps = (dispatch: Dispatch) => ({

})


const connector = connect(mapStateToProps, mapDispatchToProps)

type VerticalWhiteEdgePropsFromRedux = ConnectedProps<typeof connector>

type VerticalWhiteEdgeProps = VerticalWhiteEdgePropsFromRedux & OwnVerticalWhiteEdgeProps

const VerticalWhiteEdge : React.FC<VerticalWhiteEdgeProps> = ({rowIdx, colIdx, cellSize,  edgeWidth}) => {
    return (
        <div className={"white-edge-vertical"} style={{ display: "flex", flexDirection: "column", height: cellSize, width: (edgeWidth - 2), fontSize: edgeWidth / 2 }}>
            {
                [rowIdx + "", colIdx + "", "-", (rowIdx + 1) + "", colIdx + ""].map((edgeElement, idx) => {
                    return (<div key={"vertical-edge-r" + rowIdx + "-c" + colIdx} style={{ transform: "rotate(90deg)" }}>
                        {edgeElement}
                    </div>)
                })
            }
        </div>
    )
}

export default connector(VerticalWhiteEdge)