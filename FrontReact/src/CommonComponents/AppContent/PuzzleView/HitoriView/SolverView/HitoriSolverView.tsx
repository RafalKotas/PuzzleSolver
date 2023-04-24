// react
import React from "react"

// redux
import { Dispatch } from "redux"
import { connect, ConnectedProps } from "react-redux"

// redux - store
import { AppState } from "../../../../../store"

// (sub) components
import HitoriDisplay from "../HitoriDisplay/HitoriDisplay"

// styles
import "./HitoriSolverView.css"

const mapStateToProps = (state: AppState) => ({
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    /*setCellSize: (cellSize : number) => 
        dispatch(SetCellSize(cellSize))*/
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type HitoriSolverViewPropsFromRedux = ConnectedProps<typeof connector>

type HitoriSolverViewProps = HitoriSolverViewPropsFromRedux

const HitoriSolverView: React.FC<HitoriSolverViewProps> = () => {


    return (
        <div id="selected-hitori-view">
            <div
                id="puzzle-view-container"
                style={{
                    overflowX: "auto",
                    overflowY: "auto",
                    display: "flex",
                    flexDirection: "row",
                    justifyContent: "center",
                    alignItems: "center"
                }}
            >
                <HitoriDisplay />
            </div>
            {/*<HitoriActions />*/}
        </div>
    )
}

export default connector(HitoriSolverView)