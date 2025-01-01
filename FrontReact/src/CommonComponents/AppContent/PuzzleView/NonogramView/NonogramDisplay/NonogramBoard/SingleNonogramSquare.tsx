// react
import { faXmark } from "@fortawesome/free-solid-svg-icons"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { Tooltip } from "@material-ui/core"
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../store"
import { FillBoardSquare, PlaceXBoardSquare, selectBoardSquare, selectBoardSquareMark } from "../../../../../../store/puzzleLogic/nonogram"

interface OwnSingleNonogramSquareProps {
    bigSquareColumnIndex: number,
    bigSquareRowIndex: number,
    bigSquareWidth: number,
    bigSquareHeight: number,

    boardRowIndex: number,
    boardColumnIndex: number
}


const mapStateToProps = (state: AppState, ownProps: OwnSingleNonogramSquareProps) => ({
    selectedNonogram: state.nonogramDataReducer.selectedNonogram,
    squareValue: selectBoardSquare(state.nonogramLogicReducer, ownProps.boardRowIndex, ownProps.boardColumnIndex),
    squareMark: selectBoardSquareMark(state.nonogramLogicReducer, ownProps.boardRowIndex, ownProps.boardColumnIndex),
    marksVisible: state.nonogramDataReducer.marksVisible,
    xsVisible: state.nonogramDataReducer.xsVisible,
    currentMark: state.nonogramLogicReducer.currentMark,

    cellSize: state.nonogramLayoutReducer.cellSize,
    cellBorder: state.nonogramLayoutReducer.cellBorder,
    bigSquareAdditionalBorder: state.nonogramLayoutReducer.bigSquareAdditionalBorder,
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    fillSquare: (rowIdx: number, columnIdx: number) =>
        dispatch(FillBoardSquare(rowIdx, columnIdx)),
    placeX: (rowIdx: number, columnIdx: number) =>
        dispatch(PlaceXBoardSquare(rowIdx, columnIdx))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SingleNonogramSquarePropsFromRedux = ConnectedProps<typeof connector>

type SingleNonogramSquareProps = SingleNonogramSquarePropsFromRedux & OwnSingleNonogramSquareProps

const SingleNonogramSquare: React.FC<SingleNonogramSquareProps> = ({ boardRowIndex, boardColumnIndex,
    bigSquareRowIndex, bigSquareColumnIndex, bigSquareWidth, bigSquareHeight,
    cellSize, cellBorder, bigSquareAdditionalBorder, squareValue, squareMark, currentMark, xsVisible, marksVisible, fillSquare, placeX }) => {

    const handleNonogramSquareClick = (event: React.MouseEvent<HTMLDivElement, MouseEvent>) => {
        switch(currentMark) {
            case "O" :
                fillSquare(boardRowIndex, boardColumnIndex)
                break
            case "X" :
                placeX(boardRowIndex, boardColumnIndex)
                break
            default:
                break       
        }
    }

    const squareContent = () => {
        return squareValue === "X" ? squareWithX() : (
            squareValue === "O" ? filledSquare() : ""
        ) 
    }

    const squareWithX = () => {
        return (
            xsVisible ? <FontAwesomeIcon style={{fontSize: cellSize}} icon={faXmark}/> : <div style={{fontSize: cellSize}}>

            </div>
        )
    }
    
    const filledSquare = () => {
        return (
            <div style={{
                    height: cellSize , 
                    width: cellSize , 
                    backgroundColor: "black",
                    fontSize: (cellSize - 3) / 2,
                    color: "white"
                }}
            >
                {marksVisible ? squareMark : ""}
            </div>
        )
    }

    return <Tooltip title={"rowIdx: " + boardRowIndex + " , columnIdx: " + boardColumnIndex} placement="top">
        <div
            onClick={(event: React.MouseEvent<HTMLDivElement, MouseEvent>) => handleNonogramSquareClick(event)}
            style={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                height: cellSize,
                width: cellSize,
                borderTop: cellBorder + "px solid black",
                borderLeft: cellBorder + "px solid black",
                borderRight: (bigSquareWidth - 1 === bigSquareColumnIndex) ? (bigSquareAdditionalBorder + "px solid black") : "",
                borderBottom: (bigSquareHeight - 1 === bigSquareRowIndex) ? (bigSquareAdditionalBorder + "px solid black") : "",
                backgroundColor: squareValue === "-" ? "#fdf5d9" : "",
                cursor: "pointer"
            }}>
                {squareContent()}
        </div>
    </Tooltip>
}

export default connector(SingleNonogramSquare)