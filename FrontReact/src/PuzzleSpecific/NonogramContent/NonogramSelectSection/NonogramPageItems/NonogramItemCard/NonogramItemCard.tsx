// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { nonogramInformation, selectedNonogramDetails, SetSelectedNonogram } from "../../../../../store/data/nonogram"
import { AppState } from "../../../../../store"

// mui
import { Card, CardContent, Typography } from "@mui/material"

// react - router
import { useNavigate } from "react-router-dom"

// styles
import "./NonogramItemCard.css"

interface OwnNonogramItemCardProps {
    nonogramDetails : nonogramInformation,
    mode : string
    //link: string
}

const mapStateToProps = (state: AppState) => ({
    displayMode : state.displayReducer.displayMode
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
  setSelectedNonogram: (nonogram: selectedNonogramDetails) => 
    dispatch(SetSelectedNonogram(nonogram)),
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type NonogramItemCardPropsFromRedux = ConnectedProps<typeof connector>

type NonogramItemCardProps = NonogramItemCardPropsFromRedux & OwnNonogramItemCardProps

const NonogramItemCard : React.FC<NonogramItemCardProps> = ({nonogramDetails, displayMode, mode}) => {

  const {filename, source, difficulty, height, width, year, month} = nonogramDetails

  const difficultyColor = (difficulty : number) => {
      if( difficulty <= 2.0) {
          return "#00bb00"
      } else if(difficulty > 2.0 && difficulty <= 3.5) {
          return "yellow"
      } else {
          return "red"
      }
  }

  const navigate = useNavigate()
    
  return (
    <Card
        onClick={(event: React.MouseEvent<HTMLDivElement, MouseEvent>) => {
          if(mode === "READ") {
            navigate("../view/nonogram-solver/" + filename)
          } else {
            navigate("../view/nonogram-editor/" + filename)
          }
        }}
        className="nonogram-card-item"
        style={{
          flex: displayMode === "grid" ? "1 0 26%" : "1 0 90%",
          maxWidth: displayMode === "grid" ? "32%" : "99%"
        }}
    >
      {/*<Link to={link} style={{ textDecoration: "none" }}>*/}
        <CardContent>
          <Typography variant="h5" component="div">
            {filename}
          </Typography>
          <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
            Source: {source}
          </Typography>
          <Typography>year: {year} | month: {month}</Typography>
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
            height: {height} | width: {width}
          </Typography>
        </CardContent>
      {/*</Link>*/}
    </Card>
  )
}

export default connector(NonogramItemCard)