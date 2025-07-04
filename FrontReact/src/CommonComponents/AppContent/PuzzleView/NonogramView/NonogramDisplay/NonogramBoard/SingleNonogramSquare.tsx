import { faXmark } from "@fortawesome/free-solid-svg-icons"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { Tooltip } from "@mui/material"
import React, { useEffect } from "react"
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

import { AppState } from "../../../../../../store"
import {
  FillBoardSquare,
  PlaceXBoardSquare,
  selectBoardSquare,
  selectBoardSquareMark
} from "../../../../../../store/puzzleLogic/nonogram"

interface OwnSingleNonogramSquareProps {
  bigSquareColumnIndex: number
  bigSquareRowIndex: number
  bigSquareWidth: number
  bigSquareHeight: number
  boardRowIndex: number
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
type SingleNonogramSquareProps = ConnectedProps<typeof connector> & OwnSingleNonogramSquareProps

const SingleNonogramSquare: React.FC<SingleNonogramSquareProps> = ({
  boardRowIndex, boardColumnIndex,
  bigSquareRowIndex, bigSquareColumnIndex,
  bigSquareWidth, bigSquareHeight,
  cellSize, cellBorder, bigSquareAdditionalBorder,
  squareValue, squareMark, currentMark, xsVisible, marksVisible,
  fillSquare, placeX
}) => {

    useEffect(() => {
        console.log("SingleNonogramSquare squareValue changed!");
    }, [squareValue])    

  const handleNonogramSquareClick = (event: React.MouseEvent<HTMLDivElement>) => {
    switch (currentMark) {
      case "O":
        fillSquare(boardRowIndex, boardColumnIndex)
        break
      case "X":
        placeX(boardRowIndex, boardColumnIndex)
        break
      default:
        break
    }
  }

  const getBackgroundColor = () => {
    console.log(`squareValue of: [${boardRowIndex}, ${boardColumnIndex}] = ${squareValue}`);
    if (squareValue === "O") return "black"
    if (squareValue === "X") return "white"
    return "#fdf5d9"
  }

  const getSquareContent = () => {
    console.log(`squareValue of: [${boardRowIndex}, ${boardColumnIndex}] = ${squareValue}`);
    if (squareValue === "X" && xsVisible) {
      return <FontAwesomeIcon style={{ fontSize: cellSize }} icon={faXmark} />
    }
    if (squareValue === "O" && marksVisible) {
      return squareMark
    }
    return null
  }

  return (
    <Tooltip title={`rowIdx: ${boardRowIndex} , columnIdx: ${boardColumnIndex}`} placement="top">
      <div
        onClick={handleNonogramSquareClick}
        style={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          height: cellSize,
          width: cellSize,
          borderTop: `${cellBorder}px solid black`,
          borderLeft: `${cellBorder}px solid black`,
          borderRight: (bigSquareWidth - 1 === bigSquareColumnIndex)
            ? `${bigSquareAdditionalBorder}px solid black`
            : "",
          borderBottom: (bigSquareHeight - 1 === bigSquareRowIndex)
            ? `${bigSquareAdditionalBorder}px solid black`
            : "",
          backgroundColor: getBackgroundColor(),
          color: squareValue === "O" ? "white" : "black",
          fontSize: (cellSize - 3) / 2,
          cursor: "pointer",
          userSelect: "none"
        }}
      >
        {getSquareContent()}
      </div>
    </Tooltip>
  )
}

export default connector(SingleNonogramSquare)
