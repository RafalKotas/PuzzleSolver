// react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../store"
import { calculateBoardDimensionInPx, calculateCellWithBorderSize } from "../../../../../../store/layout/architect"

interface OwnTanksInColumnsIndicatorsProps {

}

const mapStateToProps = (state: AppState) => ({
    tanksInColumns: state.architectDataReducer.selectedArchitect.tanksInColumns,
    cellSize: state.architectLayoutReducer.cellSize,
    borderWidth: state.architectLayoutReducer.cellBorder,
    cellWithBorderSize: calculateCellWithBorderSize(state.architectLayoutReducer),
    boardWidth: calculateBoardDimensionInPx(state.architectLayoutReducer, state.architectDataReducer, "width")
})

const mapDispatchToProps = (dispatch: Dispatch) => ({

})

const connector = connect(mapStateToProps, mapDispatchToProps)

type TanksInColumnsIndicatorsPropsFromRedux = ConnectedProps<typeof connector>

type TanksInColumnsIndicatorsProps = TanksInColumnsIndicatorsPropsFromRedux & OwnTanksInColumnsIndicatorsProps

const TanksInColumnsIndicators: React.FC<TanksInColumnsIndicatorsProps> = ({ tanksInColumns, cellSize, borderWidth, cellWithBorderSize, boardWidth }) => {
    
    return (

        <div style={{
            display: "flex",
            flexDirection: "row",
            width: boardWidth + "px",
            height: cellWithBorderSize + "px"
        }}>
            {
                tanksInColumns.map((tanksInColumn) => {
                    return <div style={{
                        height: cellWithBorderSize + "px",
                        width: cellWithBorderSize + "px",
                        display: "flex",
                        flexDirection: "row",
                        justifyContent: "center",
                        alignItems: "center",
                        fontSize: (2 * cellWithBorderSize) / 3 + "px"
                    }}>
                        {tanksInColumn}
                    </div>
                })
            }
        </div>
    )
}

export default connector(TanksInColumnsIndicators)