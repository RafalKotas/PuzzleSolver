// react
import React from "react"

// redux
import { Dispatch } from "redux"
import { connect, ConnectedProps } from "react-redux"

// redux - store
import { AppState } from "../../../../../store"
import { SetCellSize } from "../../../../../store/layout/nonogram"

// (sub) components
import AkariDisplay from "../AkariDisplay/AkariDisplay"

//styles
import "../AkariView.css"

const mapStateToProps = (state: AppState) => ({
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setCellSize: (cellSize : number) => 
        dispatch(SetCellSize(cellSize)),
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type AkariSolverViewPropsFromRedux = ConnectedProps<typeof connector>

type AkariSolverViewProps = AkariSolverViewPropsFromRedux

const AkariSolverView : React.FC<AkariSolverViewProps> = ({ setCellSize }) => {


    return (
        <div id="selected-akari-view">
            <div
                id="akari-view-container"  
                style={{
                        overflowX: "auto",
                        overflowY: "auto"
                        }}
            >
                <AkariDisplay/>
            </div>
            {/*<AkariActions />*/}
        </div>
    )
}

export default connector(AkariSolverView)