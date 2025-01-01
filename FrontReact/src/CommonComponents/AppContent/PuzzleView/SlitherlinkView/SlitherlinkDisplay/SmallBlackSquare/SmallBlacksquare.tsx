// react
import React from "react"

// redux
import { Dispatch } from "redux"
import { connect, ConnectedProps } from "react-redux"

// redux - store
import { AppState } from "../../../../../../store"
import { selectCellSize } from "../../../../../../store/layout/slitherlink"

interface OwnSmallBlackSquareProps {
    edgeWidthInPx: number
}

const mapStateToProps = (state: AppState) => ({
    cellSize: selectCellSize(state.slitherlinkLayoutReducer),
})

const mapDispatchToProps = (dispatch: Dispatch) => ({

})


const connector = connect(mapStateToProps, mapDispatchToProps)

type SmallBlackSquarePropsFromRedux = ConnectedProps<typeof connector>

type SmallBlackSquareProps = SmallBlackSquarePropsFromRedux & OwnSmallBlackSquareProps

const SmallBlackSquare : React.FC<SmallBlackSquareProps> = ({edgeWidthInPx}) => {
    return (
        <div className="small-black-square" style={{ height: edgeWidthInPx, width: edgeWidthInPx }}>

        </div>
    )
}

export default connector(SmallBlackSquare)