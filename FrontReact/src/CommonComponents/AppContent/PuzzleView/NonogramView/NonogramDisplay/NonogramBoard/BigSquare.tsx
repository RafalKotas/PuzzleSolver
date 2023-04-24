// react
import React from "react"

// redux
import { Dispatch } from "redux"
import { connect, ConnectedProps } from "react-redux"

// redux - store
import { AppState } from "../../../../../../store"
import SingleNonogramSquare from "./SingleNonogramSquare"
import { calculateCellWithBorderSize } from "../../../../../../store/layout/nonogram"

interface OwnBigSquareProps {
    firstRowNoInBigSquare: number,
    firstColNoInBigSquare: number,
    bigSquareHeightInCells: number
}

const mapStateToProps = (state: AppState) => ({
    selectedNonogram: state.nonogramDataReducer.selectedNonogram,

    bigSquareAdditionalBorder: state.nonogramLayoutReducer.bigSquareAdditionalBorder,

    cellOverallSize: () => calculateCellWithBorderSize(state.nonogramLayoutReducer)
})

const mapDispatchToProps = (dispatch: Dispatch) => ({

})

const connector = connect(mapStateToProps, mapDispatchToProps)

type BigSquarePropsFromRedux = ConnectedProps<typeof connector>

type BigSquareProps = BigSquarePropsFromRedux & OwnBigSquareProps

const BigSquare: React.FC<BigSquareProps> = ({ selectedNonogram, cellOverallSize, bigSquareAdditionalBorder,
    firstRowNoInBigSquare, firstColNoInBigSquare, bigSquareHeightInCells }) => {

    let bigSquareWidthInCells = Math.min(5, selectedNonogram ? selectedNonogram.width - firstColNoInBigSquare : 5)
    let bigSquareWidthPx = bigSquareWidthInCells * cellOverallSize() + bigSquareAdditionalBorder
    let bigSquareHeightPx = bigSquareHeightInCells * cellOverallSize() + bigSquareAdditionalBorder

    return (
        <div
            id={"big-square-r" + firstRowNoInBigSquare + "-c" + firstColNoInBigSquare}
            style={{
                height: bigSquareHeightPx,
                width: bigSquareWidthPx,
                display: "flex",
                flexDirection: "column"
            }}
        >
            {
                // draw windowHeight rows first (row1[col1,col2,col3,col4,col5], row2[col1,col2,col3,col4,col5], ...)
                Array.from(Array(bigSquareHeightInCells).keys()).map((bigSquareRowNo: number, rowIndex) => {
                    return (<div
                        
                        style={{
                            height: cellOverallSize(),
                            width: bigSquareWidthPx,
                            display: "flex",
                            flexDirection: "row"
                        }}>
                        {
                            Array.from(Array(bigSquareWidthInCells).keys()).map((bigSquareColNo: number, cellInRowIndex) => {
                                return <SingleNonogramSquare
                                    key={"single-square-r" + rowIndex + "c" + (firstColNoInBigSquare + cellInRowIndex)}
                                    boardRowIndex={firstRowNoInBigSquare + bigSquareRowNo}
                                    boardColumnIndex={firstColNoInBigSquare + bigSquareColNo}
                                    bigSquareRowIndex={rowIndex}
                                    bigSquareColumnIndex={cellInRowIndex}
                                    bigSquareWidth={bigSquareWidthInCells}
                                    bigSquareHeight={bigSquareHeightInCells}
                                />
                            })
                        }
                    </div>)
                })
            }
        </div>
    )
}

export default connector(BigSquare)