//react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../store"
import { calculateSequencesSectionDimensionInPx } from "../../../../../../store/layout/nonogram"
import { selectedNonogramDifficulty } from "../../../../../../store/data/nonogram"

// sub(components)
import InfoCardTooltip from "./NonogramInformationTooltip/NonogramInformationTooltip"

//fontawesome
import { faStar} from "@fortawesome/free-solid-svg-icons"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"

//styles
import "./FilledRectangle.css"

interface OwnFilledRectangleProps {
}

const mapStateToProps = (state: AppState) => ({
    columnSequencesHeight: calculateSequencesSectionDimensionInPx(state.nonogramLayoutReducer, state.nonogramDataReducer, "column"),
    rowSequencesWidth: calculateSequencesSectionDimensionInPx(state.nonogramLayoutReducer, state.nonogramDataReducer, "row"),
    difficulty: selectedNonogramDifficulty(state.nonogramDataReducer)
})

const mapDispatchToProps = (dispatch: Dispatch) => ({

})

const connector = connect(mapStateToProps, mapDispatchToProps)

type FilledRectanglePropsFromRedux = ConnectedProps<typeof connector>

type FilledRectangleProps = FilledRectanglePropsFromRedux & OwnFilledRectangleProps

const FilledRectangle : React.FC<FilledRectangleProps> = ({columnSequencesHeight, rowSequencesWidth, difficulty}) => {
    
    const roundedDifficulty = () => {
        return Math.round(difficulty)
    }

    const difficultyStars = Array.from({length: roundedDifficulty()}, (_, index) => {
        return <FontAwesomeIcon key={"difficulty-star-" + index} icon={faStar} style={{
            color: "yellow"
        }}/>
      });
    
    return (
        <div id="filled-rectangle" style={{
            height: columnSequencesHeight + "px",
            width: rowSequencesWidth + "px"
        }}>
            <div id="stars">
                {difficultyStars}
            </div>
            <InfoCardTooltip />
        </div>
    )
}

export default connector(FilledRectangle)