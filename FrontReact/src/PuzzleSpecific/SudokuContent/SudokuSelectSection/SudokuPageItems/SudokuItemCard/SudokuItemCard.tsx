// react
import React from "react"

// react - router
import { Link } from "react-router-dom"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { sudokuInformation, SetSelectedSudoku, SetCreatedSudoku, CopySudokuToCreatedList, CopySudokuToSolverList } from "../../../../../store/data/sudoku"
import { AppState } from "../../../../../store"

// mui
import { Card, CardContent, Typography } from "@mui/material"

// (sub) components
import CopySection from "./CopySection/CopySection"

// styles
import "./SudokuItemCard.css"

interface OwnSudokuItemCardProps {
    sudokuDetails : sudokuInformation,
    link: string
}

const mapStateToProps = (state: AppState) => ({
    displayMode : state.displayReducer.displayMode,
    mode: state.displayReducer.mode
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setSelectedSudoku: (sudoku: sudokuInformation) => 
      dispatch(SetSelectedSudoku(sudoku)),
    setCreatedSudoku: (createdSudoku: sudokuInformation) =>
      dispatch(SetCreatedSudoku(createdSudoku)),
    copySudokuToCreatedList: (sudoku: sudokuInformation) =>
      dispatch(CopySudokuToCreatedList(sudoku)),
    copySudokuToSolverList: (sudoku: sudokuInformation) =>
      dispatch(CopySudokuToSolverList(sudoku))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SudokuItemCardPropsFromRedux = ConnectedProps<typeof connector>

type SudokuItemCardProps = SudokuItemCardPropsFromRedux & OwnSudokuItemCardProps

const SudokuItemCard : React.FC<SudokuItemCardProps> = ({sudokuDetails, displayMode, link, mode, 
  setSelectedSudoku, setCreatedSudoku}) => {

  const { label, source, difficulty, filled } = sudokuDetails

  const difficultyColor = (difficulty : number) => {
      if( difficulty <= 2.0) {
          return "#00bb00"
      } else if(difficulty > 2.0 && difficulty <= 3.5) {
          return "yellow"
      } else {
          return "red"
      }
  }

  const setSudoku = () => {
    if(mode === "READ") {
      setSelectedSudoku(sudokuDetails)
    } else {
      setCreatedSudoku(sudokuDetails)
    }
  }
    
  return (
    <Card
      className={"sudoku-item-card"}
        onClick={(event: React.MouseEvent<HTMLDivElement, MouseEvent>) => setSudoku()}
        style={{
          display: "flex",
          flexDirection: "row",
          flex: displayMode === "grid" ? "1 0 26%" : "1 0 90%",
          maxWidth: displayMode === "grid" ? "32%" : "99%"
        }}
    >
        <div className="card-content">
          <Link to={link} style={{ textDecoration: "none" }}>
            <CardContent>
              <Typography variant="h5" component="div">
                {label.substring(0, label.length - 5)}
              </Typography>
              <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                Source: {source}
              </Typography>
              <Typography>
                <div style={{display: "flex", flexDirection: "row", justifyContent: "center"}}>
                  Difficulty: 
                  <div style={{display: "flex", justifyContent: "center", alignContent: "center",
                    width: "20px", height: "20px", 
                    backgroundColor: difficultyColor(difficulty), borderRadius: "10%"}}>
                    {difficulty}
                  </div>
                </div>
              </Typography>
              <Typography>
                filled : {filled}
              </Typography>
            </CardContent>
          </Link>
        </div>
      <CopySection sudokuDetails={sudokuDetails}/>
    </Card>
  )
}

export default connector(SudokuItemCard)