// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { slitherlinkInformation, SetSelectedSlitherlink } from "../../../../../store/data/slitherlink"
import { AppState } from "../../../../../store"

// mui
import { Card, CardContent, Typography } from "@mui/material"

// react - router
import { Link } from "react-router-dom"

// styles
import "./SlitherlinkItemCard.css"

interface OwnSlitherlinkItemCardProps {
    slitherlinkDetails : slitherlinkInformation,
    link: string
}

const mapStateToProps = (state: AppState) => ({
    displayMode : state.displayReducer.displayMode
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    SetSelectedSlitherlink: (slitherlink: slitherlinkInformation) => 
        dispatch(SetSelectedSlitherlink(slitherlink)),
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SlitherlinkItemCardPropsFromRedux = ConnectedProps<typeof connector>

type SlitherlinkItemCardProps = SlitherlinkItemCardPropsFromRedux & OwnSlitherlinkItemCardProps

const SlitherlinkItemCard : React.FC<SlitherlinkItemCardProps> = ({slitherlinkDetails, displayMode, SetSelectedSlitherlink, link}) => {

  const { label, source, year, month, difficulty, height, width } = slitherlinkDetails

  const difficultyColor = (difficulty : number) => {
      if( difficulty <= 2.0) {
          return "#00bb00"
      } else if(difficulty > 2.0 && difficulty <= 3.5) {
          return "yellow"
      } else {
          return "red"
      }
  }
    
  return (
    <Card 
        className={"slitherlink-card-item puzzle-item"}
        onClick={(event: React.MouseEvent<HTMLDivElement, MouseEvent>) => SetSelectedSlitherlink(slitherlinkDetails)}
        style={{
          flex: displayMode === "grid" ? "1 0 26%" : "1 0 90%",
          maxWidth: displayMode === "grid" ? "30%" : "99%",
          justifyContent: "center"
        }}
    >
      <Link to={link} style={{ textDecoration: "none" }}>
        <CardContent>
          <Typography variant="h5" component="div">
            {label.substring(0, label.length - 5)}
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
      </Link>
    </Card>
  )
}

export default connector(SlitherlinkItemCard)