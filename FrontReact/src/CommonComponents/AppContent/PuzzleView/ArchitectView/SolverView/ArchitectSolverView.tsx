// react
import React from "react"

// redux
import { Dispatch } from "redux"
import { connect, ConnectedProps } from "react-redux"

// redux - store
import { AppState } from "../../../../../store"
import { SetCellSize } from "../../../../../store/layout/nonogram"

// (sub) components
import ArchitectDisplay from "../ArchitectDisplay/ArchitectDisplay"
//import ArchitectActions from "./ArchitectActions/ArchitectActions"

// styles
import "../ArchitectView.css"

const mapStateToProps = (state: AppState) => ({
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setCellSize: (cellSize : number) => 
        dispatch(SetCellSize(cellSize)),
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type ArchitectSolverViewPropsFromRedux = ConnectedProps<typeof connector>

type ArchitectSolverViewProps = ArchitectSolverViewPropsFromRedux

const ArchitectSolverView : React.FC<ArchitectSolverViewProps> = ({ setCellSize }) => {


    return (
        <div id="selected-architect-view">
            <div
                id="architect-view-container"  
                style={{
                        overflowX: "auto",
                        overflowY: "auto"
                        }}
            >
                <ArchitectDisplay/>
            </div>
            {/*<ArchitectActions />*/}
        </div>
    )
}

export default connector(ArchitectSolverView)