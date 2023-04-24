// react
import React from "react"

// redux
import { Dispatch } from "redux"
import { connect, ConnectedProps } from "react-redux"

// redux - store
import { AppState } from "../../../../../store"

// (sub) components
import SlitherlinkDisplay from "../SlitherlinkDisplay/SlitherlinkDisplay"

const mapStateToProps = (state: AppState) => ({
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    /*setCellSize: (cellSize : number) => 
        dispatch(SetCellSize(cellSize))*/
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SlitherlinkSolverViewPropsFromRedux = ConnectedProps<typeof connector>

type SlitherlinkSolverViewProps = SlitherlinkSolverViewPropsFromRedux

const SlitherlinkSolverView : React.FC<SlitherlinkSolverViewProps> = () => {


    return (
        <div id="selected-slitherlink-view">
            <div
                id="puzzle-view-container"  
                style={{
                        overflowX: "auto",
                        overflowY: "auto",
                        display: "flex",
                        flexDirection: "row",
                        justifyContent: "center"
                        }}
            >
                <SlitherlinkDisplay/>
            </div>
            {/*<SlitherlinkActions />*/}
        </div>
    )
}

export default connector(SlitherlinkSolverView)