// redux
import { Dispatch } from "redux"
import { connect, ConnectedProps } from "react-redux"

// redux - store
import { AppState } from "../../../../../../store"
import { calculateBoardDimensionInPx, calculateCellWithBorderSize, SetCellSize } from "../../../../../../store/layout/akari"

interface OwnAkariCellProps {
    akariCell: string,
    rowIdx: number,
    colIdx: number
}

const mapStateToProps = (state: AppState) => ({
    akariBoard: state.akariDataReducer.selectedAkari.board,
    borderWidth: state.akariLayoutReducer.cellBorder,
    cellSize: state.akariLayoutReducer.cellSize,
    cellWithBorderSize: calculateCellWithBorderSize(state.akariLayoutReducer),
    boardHeight: calculateBoardDimensionInPx(state.akariLayoutReducer, state.akariDataReducer, "height"),
    boardWidth: calculateBoardDimensionInPx(state.akariLayoutReducer, state.akariDataReducer, "width")
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setCellSize: (cellSize : number) => 
        dispatch(SetCellSize(cellSize)),
})


const connector = connect(mapStateToProps, mapDispatchToProps)

type AkariCellPropsFromRedux = ConnectedProps<typeof connector>

type AkariCellProps = AkariCellPropsFromRedux & OwnAkariCellProps

const AkariCell : React.FC<AkariCellProps> = ({akariBoard, akariCell, cellSize, borderWidth, rowIdx ,colIdx}) => {
    return (
        <div style={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            height: cellSize + "px",
            width: cellSize + "px",
            fontSize: (cellSize * 3) / 5,
            fontWeight: "bold",
            backgroundColor: (akariCell !== "-") ? "black" : "#e9f0ea",
            color: (akariCell !== "-") ? "white" : "black",
            borderTop: borderWidth + "px solid black",
            borderRight: ((colIdx === akariBoard[0].length - 1) ? (borderWidth) : 0) + "px solid black",
            borderLeft: borderWidth + "px solid black",
            borderBottom: ((rowIdx === akariBoard.length - 1) ? (borderWidth) : 0) + "px solid black",
            cursor: (akariCell === "-") ? "pointer" : "normal"
        }}>
            {
                akariCell === "-" ? "" : (akariCell === "5" ? "" : akariCell)
            }
        </div>
    )
}

export default connector(AkariCell)