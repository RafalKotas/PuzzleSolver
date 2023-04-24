// react
import React from "react"

// redux
import { Dispatch } from "redux"
import { connect, ConnectedProps } from "react-redux"

// redux - store
import { AppState } from "../../../../../store"
import { calculateBoardDimensionInPx, calculateCellWithBorderSize } from "../../../../../store/layout/architect"

// (sub)components
import TanksInColumnsIndicators from "../Indicators/Columns/TanksInColumnsIndicators"
import TanksInRowsIndicators from "../Indicators/Rows/TanksInRowsIndicators"
import ArchitectBoard from "./ArchitectBoard/ArchitectBoard"

// fontawesome
import ArchitectInformationTooltip from "./ArchitectInformationTooltip/ArchitectInformationTooltip"

interface OwnArchitectDisplayProps {

}

const mapStateToProps = (state: AppState) => ({
    architectBoard: state.architectDataReducer.selectedArchitect.board,
    cellWithBorderSize: calculateCellWithBorderSize(state.architectLayoutReducer),
    boardHeight: calculateBoardDimensionInPx(state.architectLayoutReducer, state.architectDataReducer, "height"),
    boardWidth: calculateBoardDimensionInPx(state.architectLayoutReducer, state.architectDataReducer, "width")
})

const mapDispatchToProps = (dispatch: Dispatch) => ({

})

const connector = connect(mapStateToProps, mapDispatchToProps)

type ArchitectDisplayPropsFromRedux = ConnectedProps<typeof connector>

type ArchitectDisplayProps = ArchitectDisplayPropsFromRedux & OwnArchitectDisplayProps

//13 vertical borders(2px), 12 cells(40px) -> 26px + 480px = 506px
const ArchitectDisplay: React.FC<ArchitectDisplayProps> = ({ boardHeight, boardWidth, cellWithBorderSize }) => {

    return (
        <div style={{
            display: "flex",
            flexDirection: "column",
            height: boardHeight + cellWithBorderSize + "px",
            width: boardWidth + cellWithBorderSize + "px"
        }}>
            <div style={{
                display: "flex",
                flexDirection: "row",
                width: cellWithBorderSize + boardWidth + "px",
                height: cellWithBorderSize + "px"
            }}>
                <div style={{
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    minWidth: cellWithBorderSize + "px",
                    minHeight: cellWithBorderSize + "px"
                }}>
                    <ArchitectInformationTooltip />
                </div>
                <TanksInColumnsIndicators />
            </div>
            <div style={{
                display: "flex",
                flexDirection: "row",
                height: boardHeight,
                width: boardWidth
            }}>
                <TanksInRowsIndicators />
                <ArchitectBoard />
            </div>
        </div>
    )
}

export default connector(ArchitectDisplay)