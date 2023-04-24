// react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../store"
import { selectedNonogramDetails, SetSelectedNonogram } from "../../../../../store/data/nonogram"
import { calculateBoardDimensionInPx, calculateSequencesSectionDimensionInPx } from "../../../../../store/layout/nonogram"

// (sub)components
import FilledRectangle from "./FilledRectangle/FilledRectangle"
import NonogramBoard from "./NonogramBoard/NonogramBoard"
import NonogramColumnSequencesSection from "./SequencesSection/NonogramColumnSequencesSection/NonogramColumnSequencesSection"
import NonogramRowSequencesSection from "./SequencesSection/NonogramRowSequencesSection/NonogramRowSequencesSection"

interface OwnNonogramDisplayProps {
    
}

const mapStateToProps = (state: AppState) => ({
    boardHeight: calculateBoardDimensionInPx(state.nonogramLayoutReducer, state.nonogramDataReducer, "height"),
    boardWidth: calculateBoardDimensionInPx(state.nonogramLayoutReducer, state.nonogramDataReducer, "width"),
    columnSequencesHeight: calculateSequencesSectionDimensionInPx(state.nonogramLayoutReducer, state.nonogramDataReducer, "height"),
    rowSequencesWidth: calculateSequencesSectionDimensionInPx(state.nonogramLayoutReducer, state.nonogramDataReducer, "width"),
    selectedNonogram: state.displayReducer.mode === "READ" ? state.nonogramDataReducer.selectedNonogram : state.nonogramDataReducer.createdNonogram
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setSelectedNonogram: (nonogram: selectedNonogramDetails) => 
    dispatch(SetSelectedNonogram(nonogram)),
    resetSelectedNonogram: () => SetSelectedNonogram(null)
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type NonogramDisplayPropsFromRedux = ConnectedProps<typeof connector>

type NonogramDisplayProps = NonogramDisplayPropsFromRedux & OwnNonogramDisplayProps

const NonogramDisplay : React.FC<NonogramDisplayProps> = ({
    boardHeight, boardWidth, rowSequencesWidth, columnSequencesHeight}) => {

    return (
        <div style={{
            display: "flex",
            flexDirection: "column",
            width: "fit-content"
        }}>
                <React.Fragment>
                    <div style={{
                        display: "flex",
                        flexDirection: "row",
                        height: columnSequencesHeight
                    }}>
                        <FilledRectangle/>
                        <NonogramColumnSequencesSection />
                    </div>

                    <div style={{
                        display: "flex",
                        flexDirection: "row",
                        width: rowSequencesWidth + boardWidth,
                        height: boardHeight
                    }}>

                        <NonogramRowSequencesSection/>
                        <NonogramBoard/>
                    </div>
                </React.Fragment>
        </div>
    )
}

export default connector(NonogramDisplay)