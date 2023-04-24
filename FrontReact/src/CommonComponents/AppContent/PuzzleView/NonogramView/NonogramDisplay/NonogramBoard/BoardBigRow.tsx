// redux
import React from "react"
import { connect, ConnectedProps } from "react-redux"

// redux - store
import { Dispatch } from "redux"
import { AppState } from "../../../../../../store"
import { calculateBoardDimensionInPx, calculateCellWithBorderSize } from "../../../../../../store/layout/nonogram"

// (sub) components
import BigSquare from "./BigSquare"

interface OwnBoardBigRowProps {
    rowNo: number
}


const mapStateToProps = (state: AppState) => ({
    selectedNonogram: state.nonogramDataReducer.selectedNonogram,

    bigSquareAdditionalBorder: state.nonogramLayoutReducer.bigSquareAdditionalBorder,

    cellOverallSize: calculateCellWithBorderSize(state.nonogramLayoutReducer),
    boardWidth: calculateBoardDimensionInPx(state.nonogramLayoutReducer, state.nonogramDataReducer, "width")
})

const mapDispatchToProps = (dispatch: Dispatch) => ({

})

const connector = connect(mapStateToProps, mapDispatchToProps)

type BoardBigRowPropsFromRedux = ConnectedProps<typeof connector>

type BoardBigRowProps = BoardBigRowPropsFromRedux & OwnBoardBigRowProps

//draws row with height in range [1, 5], consisting of big squares with dimension 5x5 or less
const BoardBigRow : React.FC<BoardBigRowProps> = ({rowNo, 
    selectedNonogram, boardWidth, cellOverallSize, bigSquareAdditionalBorder}) => {
    
    let bigRowHeightInCells = Math.min(5, selectedNonogram ? selectedNonogram.height - rowNo : 5)
    let bigRowHeightPx = bigRowHeightInCells * cellOverallSize + bigSquareAdditionalBorder 

    return (
        <div 
            id={"nonogram-row-" + rowNo}
            style={{
                display: "flex",
                flexDirection: "row",
                width: boardWidth,
                height: bigRowHeightPx
            }}
        >
            {
                selectedNonogram && Array.from(Array(selectedNonogram.width).keys()).map((colNo : number) => {
                    if(colNo % 5 === 0) {
                        return <BigSquare
                            key={"big-square-rowNo-" + rowNo + "-colNo-" + colNo}
                            firstRowNoInBigSquare={rowNo} 
                            firstColNoInBigSquare={colNo} 
                            bigSquareHeightInCells={bigRowHeightInCells}
                        />
                        //bigSquare max 5x5 fields, place rows colum by column (flexDirection: "column")
                    } else return <React.Fragment></React.Fragment>
                })
            }
        </div>  
    )
}

export default connector(BoardBigRow)