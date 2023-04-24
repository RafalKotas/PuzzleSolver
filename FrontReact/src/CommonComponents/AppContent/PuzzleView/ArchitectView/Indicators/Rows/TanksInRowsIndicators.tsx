// react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../store"
import { calculateBoardDimensionInPx, calculateCellWithBorderSize } from "../../../../../../store/layout/architect"

interface OwnTanksInRowsIndicatorsProps {

}

const mapStateToProps = (state: AppState) => ({
    tanksInRows: state.architectDataReducer.selectedArchitect.tanksInRows,
    cellSize: state.architectLayoutReducer.cellSize,
    borderWidth: state.architectLayoutReducer.cellBorder,
    cellWithBorderSize: calculateCellWithBorderSize(state.architectLayoutReducer),
    boardHeight: calculateBoardDimensionInPx(state.architectLayoutReducer, state.architectDataReducer, "height"),
})

const mapDispatchToProps = (dispatch: Dispatch) => ({

})

const connector = connect(mapStateToProps, mapDispatchToProps)

type TanksInRowsIndicatorsPropsFromRedux = ConnectedProps<typeof connector>

type TanksInRowsIndicatorsProps = TanksInRowsIndicatorsPropsFromRedux & OwnTanksInRowsIndicatorsProps

const TanksInRowsIndicators : React.FC<TanksInRowsIndicatorsProps> = ({tanksInRows, cellSize, borderWidth, cellWithBorderSize, boardHeight}) => {
    return (
        <div
            id="tanks-in-rows-indicator" 
            style={{
            display: "flex",
            flexDirection: "column",
            width: cellWithBorderSize + "px",
            height: boardHeight + "px"
        }}>
            {
                tanksInRows.map((tanksInRow) => {
                    return <div style={{
                        height: cellWithBorderSize + "px",
                        width: cellWithBorderSize + "px",
                        display: "flex",
                        flexDirection: "row",
                        justifyContent: "center",
                        alignItems: "center",
                        fontSize: (2 * cellWithBorderSize) / 3 + "px"
                    }}>
                        {tanksInRow}
                    </div>
                })
            }
        </div>
    )
}

export default connector(TanksInRowsIndicators)