// react
import React from "react"

// redux
import { Dispatch } from "redux"
import { connect, ConnectedProps } from "react-redux"

// redux - store
import { AppState } from "../../../../../../../store"
import { selectCellSize } from "../../../../../../../store/layout/slitherlink"
import SmallBlacksquare from "../../SmallBlackSquare/SmallBlacksquare"

// style
import "./EdgesRow.css"

interface OwnEdgesRowProps {
    rowIdx: number,
    colIdx: number,
    edgeWidth: number
}

const mapStateToProps = (state: AppState) => ({
    cellSize: selectCellSize(state.slitherlinkLayoutReducer),
})

const mapDispatchToProps = (dispatch: Dispatch) => ({

})


const connector = connect(mapStateToProps, mapDispatchToProps)

type EdgesRowPropsFromRedux = ConnectedProps<typeof connector>

type EdgesRowProps = EdgesRowPropsFromRedux & OwnEdgesRowProps

const EdgesRow : React.FC<EdgesRowProps> = ({rowIdx, colIdx, edgeWidth, cellSize}) => {
    return (
        <React.Fragment>
            <SmallBlacksquare edgeWidthInPx={edgeWidth}/>
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
}

export default connector(EdgesRow)