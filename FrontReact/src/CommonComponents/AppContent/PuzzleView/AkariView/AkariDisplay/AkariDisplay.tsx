// react
import { useEffect } from "react"

// redux
import { Dispatch } from "redux"
import { connect, ConnectedProps } from "react-redux"

// redux - store
import { AppState } from "../../../../../store"
import { calculateBoardDimensionInPx, calculateCellWithBorderSize, SetCellSize } from "../../../../../store/layout/akari"

// (sub) components
import AkariCell from "./AkariCell/AkariCell"
import AkariInformationTooltip from "./AkariInformationTooltip/AkariInformationTooltip"

// styles
import "./AkariDisplay.css"

interface OwnAkariDisplayProps {

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
    setCellSize: (cellSize: number) =>
        dispatch(SetCellSize(cellSize)),
})


const connector = connect(mapStateToProps, mapDispatchToProps)

type AkariDisplayPropsFromRedux = ConnectedProps<typeof connector>

type AkariDisplayProps = AkariDisplayPropsFromRedux & OwnAkariDisplayProps

const AkariDisplay: React.FC<AkariDisplayProps> = ({ akariBoard, cellSize, setCellSize, borderWidth, boardHeight, boardWidth, cellWithBorderSize }) => {

    const heightWithoutScroll = 470
    const minCellSize = 25

    useEffect(() => {

        let maximumCellHeight = Math.floor(heightWithoutScroll / akariBoard.length)
        let maximumCellWidth = Math.floor(heightWithoutScroll / akariBoard[0].length)
        let bestCellSize = Math.max(Math.min(maximumCellHeight, maximumCellWidth), minCellSize)

        setCellSize(bestCellSize)
        // eslint-disable-next-line
    }, [])

    return (
        <div
            id="akari-display"
            style={{
                display: "flex",
                flexDirection: "row",
                marginLeft: "30px",
                height: (boardHeight) + "px",
                width: (boardWidth) + "px"
            }}
        >
            {<div id="akari-info">
                <AkariInformationTooltip />
            </div>}
            <div>
                {
                    akariBoard.map((akariRow, rowIdx) => {
                        return (
                            <div style={{
                                height: cellWithBorderSize + "px",
                                width: boardWidth + "px",
                                display: "flex",
                                justifyContent: "center",
                                alignItems: "center"
                            }}>
                                {
                                    akariRow.map((akariCell, colIdx) => {
                                        return <AkariCell akariCell={akariCell} rowIdx={rowIdx} colIdx={colIdx} />
                                    })
                                }
                            </div>
                        )
                    })
                }
            </div>
        </div>
    )
}

export default connector(AkariDisplay)